package kz.codingwolves.mail;

import kz.codingwolves.jwt.JwtTokenUtil;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by sagynysh on 1/3/17.
 */
public class CustomGmailMailSender extends JavaMailSenderImpl {

    public CustomGmailMailSender() {
        setHost("smtp.gmail.com");
        setPort(587);
        setProtocol("smtp");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.quitwait", "false");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        setJavaMailProperties(properties);
    }

    public void init() {
        String password = System.getProperty("password");
        String email = System.getProperty("gmail");
        if (password == null || email == null) {
            System.out.println("Email or password is null. Shutting down the server");
            System.exit(0);
        } else {
            JwtTokenUtil.SECRET = password;
            setPassword(password);
            setUsername(email);
        }
    }
}
