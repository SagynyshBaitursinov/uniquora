package kz.codingwolves.uniquora.controllers;

import kz.codingwolves.identicons.IdenticonGenerator;
import kz.codingwolves.uniquora.SpringRunner;
import kz.codingwolves.uniquora.dto.LoginDto;
import kz.codingwolves.uniquora.enums.Messages;
import kz.codingwolves.uniquora.models.Confirmation;
import kz.codingwolves.uniquora.models.User;
import kz.codingwolves.uniquora.repositories.ConfirmationRepository;
import kz.codingwolves.uniquora.repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
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
    private IdenticonGenerator identiconGenerator;

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
        File outputfile = new File(SpringRunner.getFilesPath() + "avatars/" + user.getId() + ".png");
        try {
            ImageIO.write(identiconGenerator.generate(user.getEmail()), "png", outputfile);
        } catch (IOException e) {
            Messages.internalerror.toString();
        }
        userRepository.merge(user);
        List<Confirmation> confirmationList = confirmationRepository.getByUser(user);
        for (Confirmation eachConfirmation: confirmationList) {
            eachConfirmation.setActive(false);
            confirmationRepository.merge(eachConfirmation);
        }
        return Messages.success.toString();
    }

    @RequestMapping(value = "/avatar/{id}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        File outputfile = new File(SpringRunner.getFilesPath() + "avatars/" + id + ".png");
        if (outputfile.exists()) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            try {
                response.getOutputStream().write(IOUtils.toByteArray(new FileInputStream(outputfile)));
            } catch (Exception e) {
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write(Messages.notfound.toString());
            }
        } else {
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write(Messages.notfound.toString());
        }
    }
}
