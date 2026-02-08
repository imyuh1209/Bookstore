package fit.hutech.TruongGiaHuy.services;

import fit.hutech.TruongGiaHuy.entities.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OAuthServiceIntegrationTest {

    @Test
    void processOAuthUser_savesUserUsingAttributes() {
        UserService userService = Mockito.mock(UserService.class);
        OAuthService oauthService = new OAuthService(userService);
        
        OAuth2User oauth2User = Mockito.mock(OAuth2User.class);
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("email", "integration@example.com");
        attrs.put("name", "Integration User");
        attrs.put("sub", "123456789");
        when(oauth2User.getAttributes()).thenReturn(attrs);
        when(oauth2User.getAuthorities()).thenReturn(Collections.emptyList());
        
        // Mock userService.saveOauthUser to return a User
        User mockUser = new User();
        mockUser.setUsername("user");
        mockUser.setRoles(Collections.emptyList()); 
        when(userService.saveOauthUser(anyString(), anyString(), anyString())).thenReturn(mockUser);

        oauthService.processAttributes(oauth2User);

        verify(userService, times(1)).saveOauthUser(eq("integration@example.com"), eq("Integration User"), anyString());
    }
}


