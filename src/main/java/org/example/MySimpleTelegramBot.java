package org.example;

import org.apache.log4j.Logger;
import org.example.admins.AdminManager;
import org.example.users.UserManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MySimpleTelegramBot extends TelegramLongPollingBot{
    static final String[] admins;
    static final Properties properties;
    UserManager userManager;
    AdminManager adminManager;
    static String channelId;
    static String channelUsername;
    static final Logger logger = Logger.getLogger(MySimpleTelegramBot.class);
    public MySimpleTelegramBot() {
        adminManager = new AdminManager();
        userManager = new UserManager();
    }
    static {
        try {
            properties = new Properties();
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("config.properties");
            properties.load(inputStream);
            String adminId= (String)properties.get("admin.id");
            admins = adminId.split(",");
            channelId = properties.getProperty("channel.id");
            channelUsername = properties.getProperty("channel.username");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("a message was received");
        Long userId = update.getMessage().getFrom().getId();
        for(String admin: admins){
            if(admin.equals(String.valueOf(userId))){
                logger.debug("Message was received and passed to admin manager");
                adminManager.process(update,this);
            }else{
                logger.debug("Message was received and passed to user manager");
                userManager.process(update,this,channelId,channelUsername);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getProperty("bot.username");
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
    @Override
    public String getBotToken(){
        return properties.getProperty("bot.token");
    }
}
