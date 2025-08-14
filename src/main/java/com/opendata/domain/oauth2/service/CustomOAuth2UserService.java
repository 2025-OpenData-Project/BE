package com.opendata.domain.oauth2.service;

import com.opendata.domain.oauth2.dto.GoogleResponse;
import com.opendata.domain.oauth2.dto.OAuth2Response;
import com.opendata.domain.oauth2.dto.user.CustomOAuth2User;
import com.opendata.domain.oauth2.dto.user.UserDto;
import com.opendata.domain.user.entity.User;
import com.opendata.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.opendata.domain.user.entity.MemberShip.FREE;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService
{
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("User {} loaded", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }
        User existData= userRepository.findUserByEmail(oAuth2Response.getEmail());
        if (existData == null) {

            User user= User.create(oAuth2Response.getEmail(),oAuth2Response.getName(),FREE);
            userRepository.save(user);

            UserDto userDTO = new UserDto();
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_MEMBER");

            return new CustomOAuth2User(userDTO);
        }
        else {
            existData.updateUserInfo(oAuth2Response.getName());
            UserDto userDTO = new UserDto();
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_MEMBER");

            return new CustomOAuth2User(userDTO);
        }
    }
}
