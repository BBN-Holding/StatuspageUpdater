package com.bbn.StatuspageUpdater;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class Listener extends ListenerAdapter {

    private Sender sender;
    private Config config;

    public Listener(Sender sender, Config config) {
        this.sender = sender;
        this.config = config;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        new Thread(() -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    sender.updateMetric(String.valueOf(event.getJDA().getGatewayPing()), String.valueOf(Instant.now().getEpochSecond()), config.getDiscordGatewayMetricID());
                    event.getJDA().getRestPing().queue(ping ->
                            sender.updateMetric(String.valueOf(ping), String.valueOf(Instant.now().getEpochSecond()), config.getDiscordRestMetricID())
                    );

                    for (int i = 0; i<config.getBotIDs().length(); i++) {
                        String id = config.getBotIDs().getString(i);
                        boolean found = false;
                        for (Guild guild : event.getJDA().getGuilds()) {
                            if (found) break;
                            for (Member member : guild.getMembers()) {
                                if (found) break;
                                if (member.getUser().getId().equals(id.split("/")[0])) {
                                    boolean online = member.getOnlineStatus().equals(OnlineStatus.ONLINE);
                                    sender.setState(id.split("/")[1], online);
                                    found = true;
                                }
                            }
                        }

                    }


                }
            }, 1000, 300000);
        }).start();
    }
}
