package kz.codingwolves.uniquora.dto;

import kz.codingwolves.uniquora.models.User;

/**
 * Created by sagynysh on 2/19/17.
 */
public class ValidationDto {

    public String email;
    public String fullname;
    public String schoolInfo;
    public String id;
    public String rating;

    public static ValidationDto fromUser(User user) {
        ValidationDto result = new ValidationDto();
        result.email = user.getEmail();
        result.fullname = user.getFullname();
        result.schoolInfo = user.getSchool().toString() + " " + user.getRegistrarId().substring(0, 3);
        result.rating = user.getRating().toString();
        return result;
    }
}
