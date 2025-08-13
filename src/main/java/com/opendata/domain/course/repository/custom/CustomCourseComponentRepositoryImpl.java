package com.opendata.domain.course.repository.custom;

import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.course.entity.QCourse;
import com.opendata.domain.course.entity.QCourseComponent;
import com.opendata.domain.tourspot.entity.QTourSpot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomCourseComponentRepositoryImpl implements CustomCourseComponentRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<CourseComponent> findAllByCourseId(Long courseId) {
        QCourseComponent cc = QCourseComponent.courseComponent;
        QTourSpot ts = QTourSpot.tourSpot;

        return queryFactory
                .selectFrom(cc)
                .leftJoin(cc.tourSpot, ts).fetchJoin()
                .where(cc.course.id.eq(courseId))
                .orderBy(cc.tourspotTm.asc())
                .fetch();
    }
}
