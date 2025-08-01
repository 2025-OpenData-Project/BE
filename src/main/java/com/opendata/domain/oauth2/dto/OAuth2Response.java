package com.opendata.domain.oauth2.dto;

public interface OAuth2Response
{
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
