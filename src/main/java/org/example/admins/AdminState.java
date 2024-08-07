package org.example.admins;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdminState {
    public void process(TelegramLongPollingBot pollingBot, Update update, AdminManager adminManager);
}
