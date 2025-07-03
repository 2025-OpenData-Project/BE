//package com.opendata.domain.wishlist.service;
//
//
//import com.opendata.domain.course.dto.response.CourseResultResponse;
//import com.opendata.domain.course.dto.response.CourseSpecResponse;
//import com.opendata.domain.course.entity.Course;
//import com.opendata.domain.course.exception.CourseNotFoundException;
//import com.opendata.domain.course.repository.CourseRepository;
//import com.opendata.global.jwt.JwtUtil;
//import com.opendata.global.security.CustomUserDetails;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static com.opendata.domain.course.message.CourseMessages.COURSE_NOT_ACTIVE;
//import static com.opendata.domain.course.message.CourseMessages.COURSE_NOT_FOUND;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class WishListService
//{
//    private final CourseRepository courseRepository;
//    private final JwtUtil jwtUtil;
//
//    public Course getCourseOne(Long courseId)
//    {
//        return courseRepository.findById(courseId).get();
//    }
//
//    public List<Course> getCourses(CustomUserDetails userDetails)
//    {
//        Long userId = userDetails.getUserId();
//        log.info("userId:{}",userId);
//        List<Course> CourseList= courseRepository.findCoursesByUserId(userId);
//        return CourseList;
//    }
//
//    public Long deleteCourse(Long courseId)
//    {
//        Course course=courseRepository.findById(courseId).get();
//        if(course==null)
//        {
//            throw new CourseNotFoundException(COURSE_NOT_FOUND);
//        }
//        courseRepository.delete(course);
//        return courseId;
//    }
//
//    @Transactional
//    public Course selectCourse(Long courseId,CustomUserDetails userDetails)
//    {
//        String userId=userDetails.getUserId();
//        log.info("userId:{}",userId);
//        Course course=courseRepository.findById(courseId).get();
//        Course activeCourse= courseRepository.findCourseByIdWithActive(userId);
//        if(course==null )
//        {
//            throw new CourseNotFoundException(COURSE_NOT_FOUND);
//        }
//        if(activeCourse==null )
//        {
//            throw new CourseNotFoundException(COURSE_NOT_ACTIVE);
//        }
//        activeCourse.setActive(false);
//        course.setActive(true);
//        courseRepository.save(activeCourse);
//        courseRepository.save(course);
//        return course;
//
//    }
//    public CourseSpecResponse shareCourse(Long courseId)
//    {
//        Course course=courseRepository.findById(courseId).get();
//        return CourseSpecResponse.from(course);
//    }
//
//
//
//}
