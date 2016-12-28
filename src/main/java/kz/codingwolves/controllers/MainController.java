package kz.codingwolves.controllers;

import kz.codingwolves.dto.RegistrationDto;
import kz.codingwolves.enums.Messages;
import kz.codingwolves.models.User;
import kz.codingwolves.repositories.CourseRepository;
import kz.codingwolves.repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by sagynysh on 12/17/16.
 */
@RestController
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public byte[] index(HttpServletResponse response) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("readme.txt");
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody RegistrationDto registration) {
        if (registration.email == null) {
            return Messages.fill.toString();
        } else {
            User user = userRepository.findByEmail(registration.email);
            if (user == null) {
                return Messages.notfound.toString();
            }
            user.setRegistered(true);
            String uuid = UUID.randomUUID().toString();
            user.setPassword(uuid.substring(0, 7));
            userRepository.merge(user);
            logger.info("User registered " + user.getEmail());
            //TODO: Send email with password as in Unichat?
            return "success";
        }
    }

    @RequestMapping(value = "/cookie", method =  RequestMethod.GET)
    public String cookie(HttpServletRequest request) {
        return request.getHeader("cookie");
    }
}
