package kz.codingwolves.uniquora.dto;

import kz.codingwolves.uniquora.models.Answer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sagynysh on 1/7/17.
 */
public class AnswerDto {

    public Long id;

    public String text;

    public Integer rating;

    public UserDto creator;

    public Date createdDate;

    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.text = answer.getText();
        this.creator = new UserDto(answer.getCreator(), true);
        this.rating = answer.getRating();
        this.createdDate = new Date();
    }

    public static List<AnswerDto> fromList(List<Answer> answerList) {
        if (answerList == null) {
            return null;
        }
        List<AnswerDto> result = new ArrayList<>();
        for (Answer answer: answerList) {
            result.add(new AnswerDto(answer));
        }
        return result;
    }
}
