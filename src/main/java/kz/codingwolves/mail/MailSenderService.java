package kz.codingwolves.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

public class MailSenderService {

    private List<JavaMailSenderImpl> javaMailSenderList;
    private int currentSender;
    private int mailSenderSize;

    public String getCurrentSender() {
        return javaMailSenderList.get(currentSender).getUsername();
    }

    public MailSenderService(JavaMailSenderImpl... javaMailSenders) throws Exception {
        if (javaMailSenders == null || javaMailSenders.length == 0) {
            throw new Exception("Put at least one mailSender instance");
        }
        javaMailSenderList = Arrays.asList(javaMailSenders);
        currentSender = 0;
        mailSenderSize = javaMailSenderList.size();
    }

    public void send(String email, String password, boolean forgot, String queryParams) throws MessagingException {
        JavaMailSender senderInstance;
        synchronized(this) {
            senderInstance = javaMailSenderList.get(currentSender);
            currentSender++;
            if (currentSender >= mailSenderSize) {
                currentSender = 0;
            }
        }
        MimeMessage message = senderInstance.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
        if (forgot) {
            message.setContent("<center><h3 style=\"color:#00897b\">Hello!</h3><p style=\"color: #000000\">Here is your password: </p><p style=\"color: #000000\"><b>" + password + "</b></p><p><span style=\"color: #000000\">Sincerly, </span><a href=\"http://uniquora.kz\">uniquora administration</a></p></center>", "text/html");
        } else {
            message.setContent("<center><h3 style=\"color:#00897b\">Thank you for registering!</h3><p style=\"color: #000000\">You have registered with password </p><p style=\"color: #000000\"><b>" + password + "</b></p><p>In order to confirm the registration follow the <a href=\"http://uniquora.kz/confirm" + queryParams + "\">link</a>.</p><p><span style=\"color: #000000\">Sincerly, </span><a href=\"http://uniquora.kz\">uniquora</a> administration</p></center>", "text/html");
        }
        helper.setTo(email);
        if (forgot) {
            helper.setSubject("Uniquora service");
        } else {
            helper.setSubject("Welcome to Uniquora!");
        }
        senderInstance.send(message);
    }
}
