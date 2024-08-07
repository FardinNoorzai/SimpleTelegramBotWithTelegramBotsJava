package org.example.admins;

import org.apache.log4j.Logger;
import org.example.database.Database;
import org.example.database.TelegramUser;
import org.example.database.TelegramUserBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminManager {
    Map<TelegramUser, AdminState> users;
    static final Logger logger = Logger.getLogger(AdminManager.class);
    Database database;
    public AdminManager() {
        database = Database.getInstance();
        logger.info("admin manager stared initializing");
        users = new HashMap<>();
        List<TelegramUser> telegramUsers =database.findAllByRole("admin");
        for(TelegramUser telegramUser : telegramUsers){
            users.put(telegramUser,new AdminHomeState());
        }
        logger.info("admin manager was initialized");
    }

    public void process(Update update, TelegramLongPollingBot longPollingBot){
        if(isUserExist(update)){
            String userid = String.valueOf(update.getMessage().getFrom().getId());
            TelegramUser telegramUser = new TelegramUserBuilder().setUserid(userid).build();
            logger.info("messages started to be processed");
            users.get(telegramUser).process(longPollingBot,update,this);
        }

    }

    public boolean isUserExist(Update update){
        String userid = String.valueOf(update.getMessage().getFrom().getId());
        String username = update.getMessage().getFrom().getUserName();
        String name = update.getMessage().getFrom().getFirstName();
        for(TelegramUser telegramUser : users.keySet()){
            if(telegramUser.getUserid().equals(userid)){
                logger.info("user existed and was returned");
                return true;
            }
        }

        database.add(update,"admin");
        TelegramUser telegramUser = new TelegramUserBuilder().setName(name).setRole("admin").setUserid(userid).setUsername(username).build();
        users.put(telegramUser,new AdminHomeState());
        logger.info("user did not existed and was created");
        return true;
    }


}
