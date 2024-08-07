package org.example.database;

public class TelegramUserBuilder {
    private String id;
    private String name;
    private String username;
    private String userid;
    private String role;

    public TelegramUserBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TelegramUserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TelegramUserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public TelegramUserBuilder setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public TelegramUserBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    public TelegramUser build() {
        TelegramUser user = new TelegramUser();
        user.setId(this.id);
        user.setName(this.name);
        user.setUsername(this.username);
        user.setUserid(this.userid);
        user.setRole(this.role);
        return user;
    }
}
