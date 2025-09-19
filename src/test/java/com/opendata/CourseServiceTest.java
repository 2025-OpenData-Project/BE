package com.opendata;
import com.opendata.domain.course.dto.response.CourseResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.course.repository.CourseComponentRepository;
import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.domain.course.service.CourseService;
import com.opendata.domain.user.entity.User;
import com.opendata.global.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional // 테스트 환경에선 DB에 반영되지 않고 롤백.
class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseComponentRepository courseComponentRepository;

    private List<CourseResponse> result;
    private String courseId;

    @BeforeEach
    void setUp() {
        result = courseService.recommendCourses(
                37.5665, 126.9780,
                "2025-09-19 12:00", "2025-09-19 18:00",
                "경복궁"
        );
        courseId = result.get(0).courseId(); // 공통으로 쓸 ID
    }


    @Test
    @DisplayName("코스 생성 테스트")
    void recommendCourses_shouldReturnFromRealDb() {
        assertThat(result).isNotEmpty();

        result.forEach(course -> {
            System.out.println("CourseId: " + course.courseId());
            course.courseComponentDtoList().forEach(component -> {
                System.out.println(component.tourSpotName()
                        + " (" + component.tourspotId() + "), 시간: " + component.time()
                        + ", 혼잡도: " + component.congestionLevel());
            });
        });
    }

    @Test
    @DisplayName("코스 즐겨찾기 테스트")
    void likeCourse_shouldPostAtRealDb(){
        String parsedCourseId = courseId.split("tempCourse:")[1];
        String email = "kamillcream1@gmail.com";

        User user = new User();
        user.setEmail(email);


        CustomUserDetails userDetails = new CustomUserDetails(user);

        List<Course> coursesBefore = courseRepository.findAll();
        int beforeTest = coursesBefore.size();

        courseService.likeCourse(parsedCourseId, userDetails);


        List<Course> coursesAfter = courseRepository.findAll();

        assertThat(coursesAfter).hasSize(beforeTest + 1);
        assertThat(coursesAfter).isNotEmpty();
        assertThat(coursesAfter.get(0).getCourseComponents()).isNotEmpty();
    }

    @Test
    @DisplayName("코스 상세정보 조회 테스트")
    void fetchCourseDetailTest(){
        String parsedCourseId = courseId.split("tempCourse:")[1];

        CourseResponse courseResponse = courseService.fetchCourseDetail(parsedCourseId);
        courseResponse.courseComponentDtoList().forEach(
                courseComponentDto ->{
                    System.out.println(courseComponentDto.tourspotId());
                    System.out.println(courseComponentDto.tourSpotName());
                }
        );

        assertThat(courseResponse).isNotNull();
    }
}
