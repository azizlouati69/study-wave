package com.studywave.repository;

import com.studywave.model.Course;
import com.studywave.model.CourseStatus;
import com.studywave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByInstructor(User instructor);
    List<Course> findByCategory(String category);
    List<Course> findByLevel(String level);
    
    @Query("SELECT c FROM Course c WHERE c.status = :status AND (c.title LIKE %:keyword% OR c.description LIKE %:keyword%)")
    List<Course> searchByKeyword(@Param("keyword") String keyword, @Param("status") CourseStatus status);
    
    @Query("SELECT c FROM Course c JOIN c.enrolledStudents s WHERE s.id = :userId")
    List<Course> findByEnrolledStudent(@Param("userId") Long userId);
}
