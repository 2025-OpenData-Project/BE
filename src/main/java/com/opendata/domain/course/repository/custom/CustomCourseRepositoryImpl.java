package com.opendata.domain.course.repository.custom;


import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.QCourse;
import com.opendata.domain.course.entity.QCourseComponent;
import com.opendata.domain.tourspot.entity.QTourSpot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Course> findByUuid(String uuid) {
        QCourse qCourse = QCourse.course;

        return Optional.ofNullable(queryFactory.selectFrom(qCourse)
                .where(qCourse.uuid.eq(uuid))
                .fetchFirst());

    }
}
