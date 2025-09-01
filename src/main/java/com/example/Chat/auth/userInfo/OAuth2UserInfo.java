package com.example.Chat.auth.userInfo;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getProviderEmail();
    String getProviderName();
}
