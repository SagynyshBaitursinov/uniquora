package kz.codingwolves.uniquora.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.codingwolves.uniquora.models.User;

/**
 * Created by sagynysh on 1/7/17.
 */
public class UserDto {

    public Long id;

    public String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String school;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer rating;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String year;

    public UserDto(User user, boolean full) {
        this.id = user.getId();
        this.name = user.getFullname();
        if (full) {
            this.email = user.getEmail();
            this.school = user.getSchool().toString();
            this.rating = user.getRating();
            this.year = user.getRegistrarId().substring(0, 4);
        }
    }
}
