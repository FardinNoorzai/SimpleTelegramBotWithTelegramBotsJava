package org.example.users;

import org.apache.log4j.Logger;
import org.example.admins.AdminManager;
import org.example.database.Database;
import org.example.database.TelegramUser;
import org.example.database.TelegramUserBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    Map<TelegramUser, UserState> users;
    static final Logger logger = Logger.getLogger(AdminManager.class);
    Database database;


    public UserManager() {
        database = Database.getInstance();
        logger.info("user manager stared initializing");
        users = new HashMap<>();
        List<TelegramUser> telegramUsers =database.findAllByRole("user");
        for(TelegramUser telegramUser : telegramUsers){
            users.put(telegramUser,new UserSearchState());
        }
        logger.info("user manager was initialized");
    }



    public void process(Update update, TelegramLongPollingBot longPollingBot,String channelId,String channelUsername){
        if(isUserExist(update)){
            String userid = String.valueOf(update.getMessage().getFrom().getId());
            GetChatMember getChatMember = GetChatMember.builder().userId(update.getMessage().getFrom().getId()).chatId(channelId).build();
            try {
                ChatMember chatMember = longPollingBot.execute(getChatMember);
                String status = chatMember.getStatus();
                if (status.equals("member") || status.equals("administrator") || status.equals("creator")) {
                    TelegramUser telegramUser = new TelegramUserBuilder().setUserid(userid).build();
                    users.get(telegramUser).process(longPollingBot,update,this);
                    logger.info("bot processed a user request and responded");
                } else {
                    SendMessage sendMessage = SendMessage.builder().chatId(userid).text("you have to join to this channel before using the bot \n\n\n"+ channelUsername+"\n\nthen press /start").build();
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    longPollingBot.execute(sendMessage);
                    logger.info("bot did not response because user was not join to channel");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
                logger.info("user didn't existed and was created");
                logger.error("an error occurred" + e.getMessage());
            }

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

        database.add(update,"user");
        TelegramUser telegramUser = new TelegramUserBuilder().setName(name).setRole("user").setUserid(userid).setUsername(username).build();
        users.put(telegramUser,new UserSearchState());
        logger.info("user did not existed and was created");
        return true;
    }
}
