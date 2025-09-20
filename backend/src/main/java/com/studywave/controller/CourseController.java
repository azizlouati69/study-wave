package com.studywave.controller;

import com.studywave.model.Course;
import com.studywave.model.User;
import com.studywave.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;

    @GetMapping("/public")
    public ResponseEntity<List<Course>> getAllPublishedCourses() {
        List<Course> courses = courseService.getAllPublishedCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String keyword) {
        List<Course> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<List<Course>> getCoursesByCategory(@PathVariable String category) {
        List<Course> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my-courses")
    @PreAuthorize("hasRole('STUDENT') or hasRole('INSTRUCTOR')")
    public ResponseEntity<List<Course>> getMyCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Course> courses = courseService.getEnrolledCourses(user);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Course>> getInstructorCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Course> courses = courseService.getCoursesByInstructor(user);
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@RequestBody Course course, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        course.setInstructor(user);
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Course> existingCourse = courseService.getCourseById(id);
        
        if (existingCourse.isPresent()) {
            Course courseToUpdate = existingCourse.get();
            if (courseToUpdate.getInstructor().getId().equals(user.getId()) || user.getRole().name().equals("ADMIN")) {
                course.setId(id);
                course.setInstructor(courseToUpdate.getInstructor());
                Course updatedCourse = courseService.updateCourse(course);
                return ResponseEntity.ok(updatedCourse);
            } else {
                return ResponseEntity.forbidden().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Course> course = courseService.getCourseById(id);
        
        if (course.isPresent()) {
            if (course.get().getInstructor().getId().equals(user.getId()) || user.getRole().name().equals("ADMIN")) {
                courseService.deleteCourse(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.forbidden().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
