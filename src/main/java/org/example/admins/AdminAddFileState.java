package org.example.admins;

import org.apache.log4j.Logger;
import org.example.database.Database;
import org.example.database.TelegramUser;
import org.example.database.TelegramUserBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminAddFileState implements AdminState{
    Database database;
    static final Logger logger = Logger.getLogger(AdminAddFileState.class);
    public AdminAddFileState() {
        database = Database.getInstance();
    }

    @Override
    public void process(TelegramLongPollingBot pollingBot, Update update, AdminManager adminManager) {
        String userid = String.valueOf(update.getMessage().getFrom().getId());
        String username = update.getMessage().getFrom().getUserName();
        String name = update.getMessage().getFrom().getFirstName();
        String text = update.getMessage().getText();
        Message message = update.getMessage();
        if(message.hasDocument()){
            database.addFile(update);
            logger.info("a file was added to database");
            SendMessage sendMessage = SendMessage.builder().chatId(userid).text("your file was added send more or press cancel").build();
            try {
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
                logger.error("an error occurred"+e.getMessage());
            }
        }else if(message.getText().equalsIgnoreCase("Cancel")){
            TelegramUser telegramUser = new TelegramUserBuilder().setUserid(userid).build();
            SendMessage sendMessage = KeyboardUtils.createHomeKeyboard(userid);
            sendMessage.setText("you are back to home");
            adminManager.users.put(telegramUser,new AdminHomeState());
            logger.info("a user was back to home from upload file state");
            try{
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        } else{
            SendMessage sendMessage = SendMessage.builder().chatId(userid).text("Please send a document").build();
            try {
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
