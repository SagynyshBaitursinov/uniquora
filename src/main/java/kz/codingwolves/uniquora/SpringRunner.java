package kz.codingwolves.uniquora;

import kz.codingwolves.identicons.IdenticonGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagynysh on 12/17/16.
 */
@SpringBootApplication
@Configuration
public class SpringRunner {

    private static List<String> administration;
    private static String filesPath = "/opt/uniquora/";

    public static void main(String[] args) throws Exception {
        fillAdministratorEmails();
        SpringApplication.run(SpringRunner.class, args);
    }

    private static void fillAdministratorEmails() {
        administration = new ArrayList<>();
        //Hardcoded, yes, but who cares? Yolo!
        //TODO: this part has to be done by spring security, or at least admins have to be retrieved from the database
        administration.add("sagynysh.baitursinov@nu.edu.kz");
    }

    public static boolean isAdmin(String email) {
        return administration.contains(email);
    }

    public static String getFilesPath() {
        return filesPath;
    }

    @Bean
    public IdenticonGenerator identiconGenerator() {
        return new IdenticonGenerator();
    }
}
