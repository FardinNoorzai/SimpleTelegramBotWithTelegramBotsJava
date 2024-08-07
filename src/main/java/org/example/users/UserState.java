package org.example.users;

import org.example.admins.AdminManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserState {
    public void process(TelegramLongPollingBot pollingBot, Update update, UserManager userManager);

}
