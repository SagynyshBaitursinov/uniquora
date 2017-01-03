package kz.codingwolves.mail;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by sagynysh on 1/3/17.
 */
public class CustomGmailMailSender extends JavaMailSenderImpl {

    public CustomGmailMailSender(String email, String password) {
        setUsername(email);
        setPassword(password);
        setHost("smtp.gmail.com");
        setPort(587);
        setProtocol("smtp");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.quitwait", "false");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        setJavaMailProperties(properties);
    }
}
