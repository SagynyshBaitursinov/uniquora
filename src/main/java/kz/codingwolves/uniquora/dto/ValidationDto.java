package kz.codingwolves.uniquora.dto;

import kz.codingwolves.uniquora.models.User;

/**
 * Created by sagynysh on 2/19/17.
 */
public class ValidationDto {

    public String email;
    public String name;
    public String school;
    public String id;
    public String year;
    public String rating;

    public static ValidationDto fromUser(User user) {
        ValidationDto result = new ValidationDto();
        result.id = user.getId().toString();
        result.email = user.getEmail();
        result.name = user.getFullname();
        result.school = user.getSchool().toString();
        result.year = user.getRegistrarId().substring(0, 4);
        result.rating = user.getRating().toString();
        return result;
    }
}
