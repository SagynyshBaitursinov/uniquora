package kz.codingwolves.dto;

import kz.codingwolves.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagynysh on 12/28/16.
 */
public class CourseDto {

    public Long id;

    //Names are not written in a correct form as Java Coding style conventions say, but it is a Dto object
    public Long INSTANCEID;

    public String COURSECODE;

    public String COURSETITLE;

    public CourseDto() {
        //
    }

    public CourseDto(Course course) {
        this.id = course.getId();
        this.INSTANCEID = course.getRegistrarId();
        this.COURSECODE = course.getCode();
        this.COURSETITLE = course.getTitle();
    }

    public static List<CourseDto> fromList(List<Course> courses) {
        List<CourseDto> result = new ArrayList<>();
        for (Course course: courses) {
            result.add(new CourseDto(course));
        }
        return result;
    }
}
