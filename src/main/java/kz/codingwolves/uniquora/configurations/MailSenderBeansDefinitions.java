package kz.codingwolves.uniquora.configurations;

import kz.codingwolves.mail.CustomGmailMailSender;
import kz.codingwolves.mail.MailSenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sagynysh on 1/3/17.
 */
@Configuration
public class MailSenderBeansDefinitions {

    @Bean(initMethod = "init")
    public CustomGmailMailSender gmailMailSender1() {
        return new CustomGmailMailSender();
    }

    @Bean
    public MailSenderService mailSenderService() {
        try {
            return new MailSenderService(gmailMailSender1());
        } catch (Exception e) {
            return null;
        }
    }
}
