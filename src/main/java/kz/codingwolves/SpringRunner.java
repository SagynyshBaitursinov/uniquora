package kz.codingwolves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sagynysh on 12/17/16.
 */
@SpringBootApplication
@Configuration
public class SpringRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringRunner.class, args);
    }
}
