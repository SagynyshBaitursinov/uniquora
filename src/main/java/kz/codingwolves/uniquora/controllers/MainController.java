package kz.codingwolves.uniquora.controllers;

import com.google.gson.Gson;
import kz.codingwolves.identicons.IdenticonGenerator;
import kz.codingwolves.jwt.JwtTokenUtil;
import kz.codingwolves.mail.MailSenderService;
import kz.codingwolves.uniquora.dto.LoginDto;
import kz.codingwolves.uniquora.dto.ValidationDto;
import kz.codingwolves.uniquora.enums.Message;
import kz.codingwolves.uniquora.models.Confirmation;
import kz.codingwolves.uniquora.models.User;
import kz.codingwolves.uniquora.repositories.ConfirmationRepository;
import kz.codingwolves.uniquora.repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
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

    @Value("${avatars.folder}")
    private String filesPath;

    @Autowired
    private IdenticonGenerator identiconGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private AuthenticationManager customAuthenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void index(HttpServletResponse response) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("readme.txt");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setStatus(200);
        response.getOutputStream().write(IOUtils.toByteArray(in));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        //Extracting principal and Credentials from request
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) {
            response.setStatus(403);
            return Message.forbidden.toString();
        }
        LoginDto loginDto = new Gson().fromJson(jb.toString(), LoginDto.class);
        if (loginDto == null) {
            response.setStatus(403);
            return Message.forbidden.toString();
        }
        try {
            customAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password));
        } catch (AuthenticationException exception) {
            response.setStatus(403);
            return Message.forbidden.toString();
        }
        return jwtTokenUtil.generateToken(loginDto.email);
    }

    @RequestMapping(value = "/isregistered", method = RequestMethod.GET)
    public String isRegistered(@RequestParam(value = "email") String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return Message.notfound.toString();
        }
        if (user.isRegistered()) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody LoginDto registration) {
        if (registration.email == null) {
            return Message.fill.toString();
        } else {
            User user = userRepository.findByEmail(registration.email);
            if (user == null) {
                return Message.notfound.toString();
            }
            if (user.isRegistered()) {
                return Message.forbidden.toString();
            }
            Confirmation existingConfirmation = confirmationRepository.getLastByUser(user);
            if (existingConfirmation != null && TimeUnit.DAYS.convert(new Date().getTime() - existingConfirmation.getCreatedDate().getTime(), TimeUnit.MILLISECONDS) < 1) {
                return Message.frequencylimit.toString();
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
            confirmation = confirmationRepository.merge(confirmation);
            logger.info("User registration attempt " + user.getEmail());
            //Send email with confirmation.id and confirmation.code showing a confirmation.passwordCandidate
            try {
                mailSenderService.send(user.getEmail(), confirmation.getPasswordCandidate(), false, "?code=" + confirmation.getCode() + "&id=" + confirmation.getId());
            } catch (Exception e) {
                logger.info("Mail sender " + mailSenderService.getCurrentSender() + " gave an error, " + e.getMessage());
                confirmation.setActive(false);
                confirmationRepository.merge(confirmation);
                return Message.internalerror.toString();
            }
            return Message.success.toString();
        }
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirm(@RequestParam("code") String code, @RequestParam("id") Long id, @RequestParam(value = "password", required = false) String password) {
        Confirmation confirmation = confirmationRepository.getById(id);
        if (confirmation == null || !confirmation.isActive()) {
            return Message.notfound.toString();
        }
        if (!confirmation.getCode().equals(code)) {
            return Message.forbidden.toString();
        }
        User user = confirmation.getUser();
        try {
            user.setPassword(confirmation.getPasswordCandidate());
        } catch (Exception e) {}
        user.setRegistered(true);
        user.setModifiedDate(new Date());
        if (password != null) {
            try {
                //TODO: Password has to be hashed
                user.setPassword(password);
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        File outputfile = new File(filesPath + "avatars/" + user.getId() + ".png");
        try {
            ImageIO.write(identiconGenerator.generate(user.getEmail()), "png", outputfile);
        } catch (IOException e) {
            return Message.internalerror.toString();
        }
        userRepository.merge(user);
        List<Confirmation> confirmationList = confirmationRepository.getByUser(user);
        for (Confirmation eachConfirmation: confirmationList) {
            eachConfirmation.setActive(false);
            confirmationRepository.merge(eachConfirmation);
        }
        return Message.success.toString();
    }

    @RequestMapping(value = "/avatar/{id}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        File outputfile = new File(filesPath + "avatars/" + id + ".png");
        if (outputfile.exists()) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            try {
                response.getOutputStream().write(IOUtils.toByteArray(new FileInputStream(outputfile)));
            } catch (Exception e) {
                response.setStatus(404);
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write(Message.notfound.toString());
            }
        } else {
            response.setStatus(404);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write(Message.notfound.toString() + filesPath);
        }
    }

    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    public ValidationDto whoAmI(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        return ValidationDto.fromUser(user);
    }
}
