package com.example.demo.config;


import com.example.demo.domain.user.Role;
import com.example.demo.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String platform;

    @Builder
    //public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture)
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String platform)
    {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.platform = platform;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        if("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println("###Google attributes### : "+attributes);
        System.out.println("###attributename### : "+userNameAttributeName);

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .platform("google")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String , Object> response = (Map<String, Object>) attributes.get("response");

        System.out.println("###naver attributes### : "+attributes);
        System.out.println("###naver response### : "+response);

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .platform("naver")
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String , Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String , Object> properties = (Map<String, Object>) attributes.get("properties");//???????????? ?????? profile??? propseties?????? ??????.
        /*
        System.out.println("####attributes_key### : "+attributes.keySet());
        System.out.println("####attributes_value### : "+attributes.values());
        System.out.println("####response_key### : "+response.keySet());
        System.out.println("####response_value### : "+response.values());
*/

        /*
        ????????? profile??? ????????? ??????
        properties?????? profile??? ????????? ???????????? ????????? ?????????. ?????? ????????????????????? profile??? ????????? ????????? properties??? ??????
        */
        return OAuthAttributes.builder()
                .name((String) properties.get("nickname"))
                .email((String) response.get("email"))
                .picture((String) properties.get("profile_image"))
                .platform("kakao")
                .attributes(attributes)//key set?????? id??? ????????? ????????? attributes?????? ?????????. ????????? CustomOAuth2UserService?????? id??? ????????? ?????? ????????? attributes??? ??????
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder().name(name).email(email).picture(picture).platform(platform).role(Role.USER).build();
    }
}
