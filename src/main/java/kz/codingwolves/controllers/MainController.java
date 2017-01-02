package kz.codingwolves.controllers;

import kz.codingwolves.dto.LoginDto;
import kz.codingwolves.enums.Messages;
import kz.codingwolves.models.Confirmation;
import kz.codingwolves.models.User;
import kz.codingwolves.repositories.ConfirmationRepository;
import kz.codingwolves.repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by sagynysh on 12/17/16.
 */
@RestController
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public byte[] index(HttpServletResponse response) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("readme.txt");
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody LoginDto registration) {
        if (registration.email == null) {
            return Messages.fill.toString();
        } else {
            User user = userRepository.findByEmail(registration.email);
            if (user == null) {
                return Messages.notfound.toString();
            }
            if (user.isRegistered()) {
                return Messages.forbidden.toString();
            }
            Confirmation existingConfirmation = confirmationRepository.getLastByUser(user);
            if (existingConfirmation != null && TimeUnit.DAYS.convert(new Date().getTime() - existingConfirmation.getCreatedDate().getTime(), TimeUnit.MILLISECONDS) < 1) {
                return Messages.frequencylimit.toString();
            }
            Confirmation confirmation = new Confirmation();
            confirmation.setActive(true);
            confirmation.setCode(UUID.randomUUID().toString());
            try {
                confirmation.setPasswordCandidate(registration.password);
            } catch (Exception e) {
                return e.getMessage();
            }
            confirmation.setUser(user);
            confirmation.setCreatedDate(new Date());
            confirmationRepository.merge(confirmation);
            logger.info("User registration attempt " + user.getEmail());
            //TODO: Send email with confirmation.id and confirmation.code showing a confirmation.passwordCandidate
            return Messages.success.toString();
        }
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirm(@RequestParam("code") String code, @RequestParam("id") Long id, @RequestParam(value = "password", required = false) String password) {
        Confirmation confirmation = confirmationRepository.getById(id);
        if (confirmation == null || !confirmation.isActive()) {
            return Messages.notfound.toString();
        }
        if (!confirmation.getCode().equals(code)) {
            return Messages.forbidden.toString();
        }
        User user = confirmation.getUser();
        try {
            user.setPassword(confirmation.getPasswordCandidate());
        } catch (Exception e) {}
        user.setRegistered(true);
        user.setModifiedDate(new Date());
        if (password != null) {
            try {
                user.setPassword(password);
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        userRepository.merge(user);
        List<Confirmation> confirmationList = confirmationRepository.getByUser(user);
        for (Confirmation eachConfirmation: confirmationList) {
            eachConfirmation.setActive(false);
            confirmationRepository.merge(eachConfirmation);
        }
        return Messages.success.toString();
    }
}
