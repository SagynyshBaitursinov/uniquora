package kz.codingwolves.uniquora.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.codingwolves.uniquora.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagynysh on 12/28/16.
 */
public class CourseDto {

    public Long id;

    //Names are not written in a correct form of Java Coding style conventions, but it is a Dto object
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long INSTANCEID;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String COURSECODE;

    public String COURSETITLE;

    public CourseDto(Course course, boolean full) {
        this.id = course.getId();
        if (full) {
            this.INSTANCEID = course.getRegistrarId();
        }
        this.COURSECODE = course.getCode();
        this.COURSETITLE = course.getTitle();
    }

    public static List<CourseDto> fromList(List<Course> courses, boolean full) {
        List<CourseDto> result = new ArrayList<>();
        for (Course course: courses) {
            result.add(new CourseDto(course, full));
        }
        return result;
    }
}
