package kz.codingwolves.uniquora.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kz.codingwolves.uniquora.dto.NewQuestionDto;
import kz.codingwolves.uniquora.dto.QuestionDto;
import kz.codingwolves.uniquora.dto.ResponseDto;
import kz.codingwolves.uniquora.enums.Message;
import kz.codingwolves.uniquora.models.Answer;
import kz.codingwolves.uniquora.models.Course;
import kz.codingwolves.uniquora.models.Question;
import kz.codingwolves.uniquora.models.User;
import kz.codingwolves.uniquora.repositories.AnswerRepository;
import kz.codingwolves.uniquora.repositories.CourseRepository;
import kz.codingwolves.uniquora.repositories.QuestionRepository;
import kz.codingwolves.uniquora.repositories.UserRepository;
import kz.codingwolves.uniquora.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * Created by sagynysh on 12/30/16.
 */
@RestController
@RequestMapping("/questions")
public class QuestionsController {

    public final static Integer pageSize = 20;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody NewQuestionDto questionDto, Principal principal, HttpServletResponse response) {
        if (questionDto == null || questionDto.courseId == null || questionDto.text == null || questionDto.text.trim().isEmpty() || questionDto.title == null || questionDto.title.trim().isEmpty()) {
            response.setStatus(400);
            return Message.fill.toString();
        }
        User user = userRepository.findByEmail(principal.getName());
        Course course = courseRepository.findById(questionDto.courseId);
        if (course == null) {
            response.setStatus(400);
            return Message.fill.toString();
        }
        Question question = new Question();
        question.setAnonymous(questionDto.isAnonymous == null ? false : questionDto.isAnonymous);
        question.setCourse(course);
        question.setRating(0);
        question.setCreator(user);
        question.setText(questionDto.text);
        question.setTitle(questionDto.title);
        question.setCreatedDate(new Date());
        question.setRemoved(false);
        question.setModifiedDate(new Date());
        try {
            question = questionRepository.merge(question);
        } catch (DataIntegrityViolationException exception) {
            return Message.notunique.toString();
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        QuestionDto result = new QuestionDto(question, false);
        try {
            return new ObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return new Gson().toJson(result);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseDto<QuestionDto> questionsList(@RequestParam(value = "page", required =  false, defaultValue = "1") Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        List<Question> questionList = questionRepository.list(page);
        for (Question question: questionList) {
            question.setAnswersNumber(answerRepository.getAnswersNumber(question).intValue());
        }
        Integer totalCount = questionRepository.count().intValue();
        Integer totalPages = MathUtil.totalPages(totalCount, pageSize);
        return new ResponseDto(page, totalPages, QuestionDto.fromList(questionList, false));
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public QuestionDto get(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Question question = questionRepository.findById(id);
        if (question == null) {
            response.sendError(404, Message.notfound.toString());
            return null;
        }
        List<Answer> answersList = answerRepository.getAnswersByQuestion(question);
        question.setAnswers(answersList);
        question.setAnswersNumber(answersList.size());
        return new QuestionDto(question, true);
    }
}
