package com.opendata.domain.user.entity;

import com.opendata.domain.course.entity.Course;
import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "membership_id", nullable = false)
    private Long membershipId;

    @Column(name = "user_email")
    private String email;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courseList;

    public static User create(String email, String name, Long membershipId) {
        return User.builder()
                .email(email)
                .name(name)
                .membershipId(membershipId)
                .build();
    }
    public void updateUserInfo(String name) {
        this.name = name;
    }




}