package kz.codingwolves.uniquora.controllers;

import com.google.gson.Gson;
import kz.codingwolves.uniquora.dto.AnswerDto;
import kz.codingwolves.uniquora.dto.NewAnswerDto;
import kz.codingwolves.uniquora.enums.Message;
import kz.codingwolves.uniquora.models.Answer;
import kz.codingwolves.uniquora.models.Question;
import kz.codingwolves.uniquora.models.User;
import kz.codingwolves.uniquora.repositories.AnswerRepository;
import kz.codingwolves.uniquora.repositories.QuestionRepository;
import kz.codingwolves.uniquora.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;

/**
 * Created by sagynysh on 1/7/17.
 */
@RestController
@RequestMapping("/answers")
public class AnswersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody NewAnswerDto answerDto, Principal principal, HttpServletResponse response) {
        if (answerDto == null || answerDto.questionId == null || answerDto.text == null || answerDto.text.trim().isEmpty()) {
            response.setStatus(400);
            return Message.fill.toString();
        }
        User user = userRepository.findByEmail(principal.getName());
        Question question = questionRepository.findById(answerDto.questionId);
        if (question == null) {
            response.setStatus(400);
            return Message.fill.toString();
        }
        Answer answer = new Answer();
        answer.setRemoved(false);
        answer.setRating(0);
        answer.setModifiedDate(new Date());
        answer.setCreatedDate(new Date());
        answer.setCreator(user);
        answer.setQuestion(question);
        answer.setText(answerDto.text);
        answer = answerRepository.merge(answer);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return new Gson().toJson(new AnswerDto(answer));
    }
}