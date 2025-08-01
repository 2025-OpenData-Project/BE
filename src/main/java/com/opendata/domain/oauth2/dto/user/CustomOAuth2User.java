package com.opendata.domain.oauth2.dto.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User
{
    private final UserDto userDto;

    @Override
    public Map<String, Object> getAttributes() {

        return new HashMap<>();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return userDto.getName();
    }

    public String getEmail() {

        return userDto.getEmail();
    }
}
