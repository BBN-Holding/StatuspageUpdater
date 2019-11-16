package com.bbn.StatuspageUpdater;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.time.Instant;

public class Updater {

    public static void main(String[] args) {
        Config config = new Config("./config.json");
        if (!config.fileExists()) {
            config.create();
            System.out.println("Please fill out the config file.");
            System.exit(0);
        }
        config.load();

        Sender sender = new Sender(config);

        JDA jda;
        try {
            jda = new JDABuilder().setToken(config.getBotToken()).addEventListeners(new Listener(sender, config)).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
