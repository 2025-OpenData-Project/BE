package com.opendata.domain.user.dto;


import com.opendata.domain.user.entity.MemberShip;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse
{
    private String email;
    private MemberShip membership;
    private String name;

    public static UserResponse toUserResponse(String email, MemberShip membership, String name)
    {
        return UserResponse.builder()
                .email(email)
                .membership(membership)
                .name(name)
                .build();
    }
}
