package kz.codingwolves.controllers;

import kz.codingwolves.models.Course;
import kz.codingwolves.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by sagynysh on 12/27/16.
 */
@RestController
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
