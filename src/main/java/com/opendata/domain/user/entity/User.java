package com.opendata.domain.user.entity;

import com.opendata.domain.register.dto.RegisterRequest;
import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;

    @Column(name = "membership_id", nullable = false)
    private Long membershipId;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_pw")
    private String password;

    @Column(name = "user_nickname")
    private String userNickname;
}