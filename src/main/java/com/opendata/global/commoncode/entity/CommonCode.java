package com.opendata.global.commoncode.entity;

import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "common_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommonCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "common_code_id")
    private Long id;

    @Column(name = "parent_common_code_id")
    private Long parentId;

    @Column(name = "common_code_lvl")
    private Integer level;

    @Column(name = "common_code_kor_nm")
    private String korName;

    @Column(name = "common_code_eng_nm")
    private String engName;
}