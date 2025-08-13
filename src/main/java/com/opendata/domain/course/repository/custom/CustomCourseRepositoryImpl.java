package com.opendata.domain.course.repository.custom;


import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.QCourse;
import com.opendata.domain.course.entity.QCourseComponent;
import com.opendata.domain.tourspot.entity.QTourSpot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomCourseRepositoryImpl implements CustomCourseRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Course> findAllByUserId(Long userId) {
        QCourse c = QCourse.course;
        QCourseComponent cc = QCourseComponent.courseComponent;
        QTourSpot ts = QTourSpot.tourSpot;

        return queryFactory
                .selectDistinct(c)
                .from(c)
                .leftJoin(c.courseComponents, cc).fetchJoin()
                .leftJoin(cc.tourSpot, ts).fetchJoin()
                .where(c.user.id.eq(userId))
                .orderBy(c.startDtm.desc(), cc.tourspotTm.asc())
                .fetch();
    }
}
