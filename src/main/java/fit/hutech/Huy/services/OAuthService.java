package fit.hutech.Huy.services;

import fit.hutech.Huy.entities.User;
import fit.hutech.Huy.utils.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuthService: Loading user from Google...");
        OAuth2User user = super.loadUser(userRequest);
        return processAttributes(user);
    }
    
    OAuth2User processAttributes(OAuth2User oauth2User) {
        log.info("OAuthService: Processing attributes: {}", oauth2User.getAttributes());
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String sub = (String) attributes.get("sub"); // Google ID
        
        log.info("OAuthService: Email: {}, Name: {}, Sub: {}", email, name, sub);
        
        User user;
        try {
            user = userService.saveOauthUser(email, name, sub);
            log.info("OAuthService: User saved/retrieved successfully: {}", user.getUsername());
        } catch (Exception e) {
            log.error("OAuthService: Error saving user", e);
            throw e;
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>(user.getAuthorities());
        authorities.addAll(oauth2User.getAuthorities());
        
        return new CustomOAuth2User(oauth2User, authorities);
    }
}
