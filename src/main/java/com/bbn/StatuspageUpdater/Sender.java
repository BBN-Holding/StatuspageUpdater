package com.bbn.StatuspageUpdater;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Sender {

    private Config config;

    public Sender(Config config) {
        this.config = config;
    }

    private String sendPost(String uri, String json) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        HttpEntity stringEntity = new StringEntity(json,ContentType.APPLICATION_JSON);
        httpPost.addHeader("Authorization", "OAuth "+config.getApiKey());
        httpPost.setEntity(stringEntity);
        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity, "UTF-8");
            System.out.println(string);
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMetric(String value, String timestamp, String metric_id) {
        sendPost(
                "https://api.statuspage.io/v1/pages/"+config.getPageID()+"/metrics/"+metric_id+"/data.json",
                new JSONObject().put("data", new JSONObject().put("timestamp", timestamp).put("value", value)).toString()
        );
    }

    private void sendState(String email, boolean online) {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", config.getSMTPServer());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "25");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(config.getEmail()));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email, false));

            msg.setSubject((online) ? "UP" : "DOWN");
            msg.setText("Gud Email");
            msg.setSentDate(new Date());

            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            t.connect(config.getSMTPServer(), config.getUsername(), config.getPassword());

            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void setState(String email, boolean online) {
        this.sendState(email, online);
    }

}
