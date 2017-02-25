package kz.codingwolves.uniquora.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.codingwolves.uniquora.models.Question;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sagynysh on 1/7/17.
 */
public class QuestionDto {

    public Long id;

    public String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String text;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public UserDto creator;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer rating;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public CourseDto course;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Date createdDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public AnswerDto latestAnswer;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer answersNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<AnswerDto> answerList;

    public QuestionDto() {

    }

    public QuestionDto(Question question, boolean full) {
        this.id = question.getId();
        this.title = question.getTitle();
        if (full) {
            this.text = question.getText();
            this.answerList = AnswerDto.fromList(question.getAnswers());
        }
        this.creator = question.isAnonymous() ? null : new UserDto(question.getCreator(), true);
        this.createdDate = question.getCreatedDate();
        this.course = new CourseDto(question.getCourse(), false);
        this.rating = question.getRating();
        this.answersNumber = question.getAnswersNumber();
        if (question.getLatestAnswer() != null) {
            this.latestAnswer = new AnswerDto(question.getLatestAnswer());
        }
    }

    public static List<QuestionDto> fromList(List<Question> list, boolean full) {
        List<QuestionDto> result = new ArrayList<>();
        for (Question question: list) {
            if (!question.getRemoved()) {
                result.add(new QuestionDto(question, full));
            }
        }
        return result;
    }

    public static List<QuestionDto> shortFromList(List<Question> list) {
        List<QuestionDto> result = new ArrayList<>();
        for (Question question: list) {
            QuestionDto questionDto = new QuestionDto();
            questionDto.id = question.getId();
            questionDto.title = question.getTitle();
            if (!question.getRemoved()) {
                result.add(questionDto);
            }
        }
        return result;
    }
}
