package com.opendata.domain.user.entity;

import com.opendata.domain.course.entity.Course;
import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership", nullable = false)
    private MemberShip membership;

    @Column(name = "user_email")
    private String email;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courseList;

    public static User create(String email, String name, MemberShip membership) {
        return User.builder()
                .email(email)
                .name(name)
                .membership(membership)
                .build();
    }
    public void updateUserInfo(String name) {
        this.name = name;
    }




}