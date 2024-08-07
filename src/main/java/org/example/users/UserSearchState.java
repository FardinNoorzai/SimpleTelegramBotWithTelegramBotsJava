package org.example.users;

import org.apache.log4j.Logger;
import org.example.database.Database;
import org.example.database.TelegramUser;
import org.example.database.TelegramUserBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UserSearchState implements UserState{
    Database database;
    public UserSearchState() {
        database = Database.getInstance();
    }

    Logger logger = Logger.getLogger(UserSearchState.class);
    @Override
    public void process(TelegramLongPollingBot pollingBot, Update update, UserManager userManager) {
        String userid = String.valueOf(update.getMessage().getFrom().getId());
        String username = update.getMessage().getFrom().getUserName();
        String name = update.getMessage().getFrom().getFirstName();
        String text = update.getMessage().getText();
        TelegramUser telegramUser = new TelegramUserBuilder().setUserid(userid).build();
        if(text.equalsIgnoreCase("/start")){
            SendMessage sendMessage = SendMessage.builder().text("send me a name of a file and i will search in the database if it exist i will send it to you").chatId(userid).build();
            logger.info("a help message was send to user to tell how to use the bot");
            try {
                pollingBot.execute(sendMessage);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        } else if (text.startsWith("/dl")) {
            String id = text.substring(3);
            TelegramFile file = database.findFileById(id);
            InputFile inputFile = new InputFile(file.getFile_id());
            SendDocument document = SendDocument.builder().chatId(userid).document(inputFile).caption(file.getSize()+" "+ file.getName()).build();
            try {
                pollingBot.execute(document);
                logger.info("bot responded was a message for user search");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        } else{
            String response = database.searchFileByName(text);
            SendMessage sendMessage = SendMessage.builder().chatId(userid).text(response).build();
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            try {
                pollingBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
