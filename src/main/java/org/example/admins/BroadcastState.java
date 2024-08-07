package org.example.admins;

import org.apache.log4j.Logger;
import org.example.database.Database;
import org.example.database.TelegramUser;
import org.example.database.TelegramUserBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class BroadcastState implements AdminState{
    Database database;
    Logger logger = Logger.getLogger(BroadcastState.class);
    public BroadcastState() {
        database = Database.getInstance();
    }

    @Override
    public void process(TelegramLongPollingBot pollingBot, Update update, AdminManager adminManager) {
        Message message = update.getMessage();
        String userid = String.valueOf(update.getMessage().getFrom().getId());
        String username = update.getMessage().getFrom().getUserName();
        String name = update.getMessage().getFrom().getFirstName();
        String text = update.getMessage().getText();
        TelegramUser telegramUser = new TelegramUserBuilder().setUserid(userid).build();
        if(text.equalsIgnoreCase("cancel")){
            SendMessage sendMessage = KeyboardUtils.createHomeKeyboard(userid);
            sendMessage.setText("you are back to home");
            adminManager.users.put(telegramUser,new AdminHomeState());
            logger.info("admin is back to home state from broadcast state");
            try {
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else{
            List<TelegramUser> users = database.findAllUsers();
            for(TelegramUser user : users){
                SendMessage sendMessage = SendMessage.builder().text(update.getMessage().getText()).chatId(user.getUserid()).build();
                try {
                    pollingBot.execute(sendMessage);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            SendMessage sendMessage = KeyboardUtils.createHomeKeyboard(userid);
            sendMessage.setText("you are back to home");
            logger.info("a message was broadcast by admin");
            try {
                pollingBot.execute(sendMessage);
                adminManager.users.put(telegramUser,new AdminHomeState());
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
