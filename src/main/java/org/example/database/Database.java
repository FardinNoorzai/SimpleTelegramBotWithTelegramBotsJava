package org.example.database;

import org.apache.log4j.Logger;
import org.example.users.TelegramFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {
    static Database database = null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    static Connection connection;
    static final Properties properties;
    static final Logger logger = Logger.getLogger(Database.class);

    static {
        try {
            properties = new Properties();
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Database() {
        if (database != null) {
            logger.warn("Someone tried to create two instance of singleton database class");
            throw new IllegalStateException();
        }
        String username = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");
        logger.info("password and username was retrieved from the properties file");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/telegrambot", username, password);
            logger.info("connection to the database was established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logger.error("there was an error connecting to the database");
        }

    }

    public List<TelegramUser> findAllUsers() {
        logger.info("find all user method of database class was called");
        return findUsersBySQLQuery("select * from users");
    }

    public List<TelegramUser> findAllByRole(String role) {
        logger.info("find all user by role method of database class was called");
        return findUsersBySQLQuery("select * from users where role='" + role + "'");
    }

    public List<TelegramUser> findUsersBySQLQuery(String query) {
        logger.info("find all user by query database class was called");
        List<TelegramUser> telegramUsers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String id = resultSet.getString("id");
                String userid = resultSet.getString("userid");
                String role = resultSet.getString("role");
                TelegramUser user = new TelegramUserBuilder().setId(id).setName(name).setRole(role).setUsername(username).setUserid(userid).build();
                telegramUsers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return telegramUsers;
    }

    public void add(Update update, String role) {
        logger.info("add user method of database class was called");
        String username = update.getMessage().getFrom().getUserName();
        String name = update.getMessage().getFrom().getFirstName();
        String userid = String.valueOf(update.getMessage().getFrom().getId());
        try {
            preparedStatement = connection.prepareStatement("insert into users(username,name,userid,role) values (?,?,?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, userid);
            preparedStatement.setString(4, role);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
//


    public void addFile(Update update) {
        logger.info("add file method of database class was called");

        Message message = update.getMessage();
        String fileId = message.getDocument().getFileId();
        String fileName = message.getDocument().getFileName();
        Long file_size = message.getDocument().getFileSize();
        String size = String.valueOf(file_size / (1024 * 1024)) + " MB";
        try {
            preparedStatement = connection.prepareStatement("insert into files(file_name,file_size,file_id) values (?,?,?)");
            preparedStatement.setString(1, fileName);
            preparedStatement.setString(2, size);
            preparedStatement.setString(3, fileId);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ;
        }
    }

    public String searchFileByName(String fileName) {
        logger.info("search file by name method of database class was called");
        String query = "SELECT * FROM files WHERE file_name LIKE ?";
        StringBuilder result = new StringBuilder();
        result.append("result of search\n\n---------------------\n\n");
        String searchPattern = "%" + fileName + "%";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, searchPattern);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fileId = resultSet.getString("file_id");
                String fileSize = resultSet.getString("file_size");
                String fileNameResult = resultSet.getString("file_name");
                result
                        .append("File Name: ").append(fileNameResult)
                        .append("\nFile Size: ").append(fileSize)
                        .append("\nDownload : /dl" + id)
                        .append("\n-------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return result.length() > 0 ? result.toString() : null;
    }

    public TelegramFile findFileById(String fileId) {
        String query = "SELECT * FROM files WHERE id = ?";
        TelegramFile telegramFile = new TelegramFile();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,fileId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String fileName = resultSet.getString("file_name");
                String fileSize = resultSet.getString("file_size");
                String fileIdResult = resultSet.getString("file_id");
                telegramFile.setFile_id(fileIdResult);
                telegramFile.setName(fileName);
                telegramFile.setSize(fileSize);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return telegramFile;
    }

    public static Database getInstance(){
        if(database == null){
            synchronized (Database.class){
                if(database == null){
                    database = new Database();
                    logger.info("database instance was created");
                }
            }
        }
        return database;
    }
}
