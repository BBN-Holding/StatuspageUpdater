package com.bbn.StatuspageUpdater;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Config {

    private Path file;
    private JSONObject config;

    public Config(String path) {
        this.file = Paths.get(path);
    }

    public boolean fileExists() {
        return Files.exists(file);
    }

    public void load() {
        try {
            config = new JSONObject(new String(Files.readAllBytes(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create() {
        try {
            if (Files.notExists(file)) {
                file = Files.createFile(file);
            }
            Files.write(file, defaultConfigContent().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String defaultConfigContent() {
        return new JSONStringer().object()
                .key("BotToken").value(null)
                .key("BotIDs").value(new String[0])
                .key("ServerIPs").value(new String[0])
                .key("ApiKey").value(null)
                .key("PageID").value(null)
                .key("DiscordGateway_metricid").value(null)
                .key("DiscordRest_metricid").value(null)
                .key("Email").object()
                .key("SMTP_SERVER").value(null)
                .key("EMAIL").value(null)
                .key("USERNAME").value(null)
                .key("PASSWORD").value(null)
                .endObject().endObject().toString();
    }

    public String getBotToken() {
        return config.getString("BotToken");
    }

    public JSONArray getBotIDs() {
        return config.getJSONArray("BotIDs");
    }

    public JSONArray getServerIPs() {
        return config.getJSONArray("ServerIPs");
    }

    public String getApiKey() {
        return config.getString("ApiKey");
    }

    public String getPageID() {
        return config.getString("PageID");
    }

    public String getDiscordGatewayMetricID() {
        return config.getString("DiscordGateway_metricid");
    }

    public String getDiscordRestMetricID() {
        return config.getString("DiscordRest_metricid");
    }

    public String getSMTPServer() {
        return config.getJSONObject("Email").getString("SMTP_SERVER");
    }

    public String getEmail() {
        return config.getJSONObject("Email").getString("EMAIL");
    }

    public String getUsername() {
        return config.getJSONObject("Email").getString("USERNAME");
    }

    public String getPassword() {
        return config.getJSONObject("Email").getString("PASSWORD");
    }

}
