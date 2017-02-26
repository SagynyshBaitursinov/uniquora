package kz.codingwolves.uniquora.controllers;

import kz.codingwolves.uniquora.dto.QuestionDto;
import kz.codingwolves.uniquora.models.Question;
import kz.codingwolves.uniquora.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagynysh on 2/25/17.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private static List<Question> questionList;

    @Autowired
    private QuestionRepository questionRepository;

    public static void addQuestion(Question question) {
        if (questionList != null) {
            if (!questionList.contains(question)) {
                questionList.add(question);
            }
        }
    }

    @PostConstruct
    private void init() {
        questionList = questionRepository.getAllQuestions();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<QuestionDto> query(@RequestParam(value = "query") String query) {
        List<Question> result = new ArrayList<>();
        if (query != null && query.length() > 1) {
            query = query.toLowerCase();
            for (Question question : questionList) {
                if (question.getTitle().toLowerCase().contains(query.toLowerCase()) && !result.contains(question)) {
                    result.add(question);
                }
            }
        }
        return QuestionDto.shortFromList(result);
    }
}
