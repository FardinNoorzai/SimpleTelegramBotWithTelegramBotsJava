package org.example.admins;

import org.apache.log4j.Logger;
import org.example.database.TelegramUser;
import org.example.database.TelegramUserBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeState implements AdminState {
    Logger logger = Logger.getLogger(AdminHomeState.class);
    @Override
    public void process(TelegramLongPollingBot pollingBot, Update update,AdminManager manager) {
        String userid = String.valueOf(update.getMessage().getFrom().getId());
        String username = update.getMessage().getFrom().getUserName();
        String name = update.getMessage().getFrom().getFirstName();
        String text = update.getMessage().getText();
        TelegramUser telegramUser = new TelegramUserBuilder().setUserid(userid).build();
        if(text.equals("Upload a file")){
            manager.users.put(telegramUser,new AdminAddFileState());
            SendMessage message = KeyboardUtils.createCancelKeyboard(userid);
            message.setText("Now upload or forward a file");
            logger.info("admin changed state to upload file");
            try {
                pollingBot.execute(message);
            }catch (Exception e){
                System.out.println(e.getMessage());
                logger.error("an error occurred" + e.getMessage());

            }
        } else if (text.equalsIgnoreCase("Broadcast")) {
            manager.users.put(telegramUser,new BroadcastState());
            SendMessage sendMessage = KeyboardUtils.createCancelKeyboard(userid);
            sendMessage.setText("Send your message");
            sendMessage.setChatId(userid);
            logger.info("admin changed state to broadcast");

            try {
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
                logger.error("an error occurred" + e.getMessage());

            }
        } else if (text.equalsIgnoreCase("Files")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(userid);
            sendMessage.setText("Coming soon...");
            try {
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
                logger.error("an error occurred" + e.getMessage());

            }
        } else if (text.equalsIgnoreCase("Users")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(userid);
            sendMessage.setText("Coming soon...");
            try {
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
                logger.error("an error occurred" + e.getMessage());

            }
        } else{
            SendMessage message = KeyboardUtils.createHomeKeyboard(userid);
            try {
                pollingBot.execute(message);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
                logger.error("an error occurred" + e.getMessage());

            }
        }

    }
}
