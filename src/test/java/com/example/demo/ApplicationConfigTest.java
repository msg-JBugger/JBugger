package com.example.demo;

import com.example.demo.config.ApplicationConfig;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationConfigTest {
    @Autowired
    UserRepositoryInterface userRepositoryInterface;

    @Test
    public void testUserDetailServiceWorks(){
        ApplicationConfig config = new ApplicationConfig(userRepositoryInterface);
        UserDetailsService userDetailsService = config.userDetailsService();
        assertNotNull(userDetailsService);
    }

    @Test
    public void testUserDetailService(){
        String username = "mockUsername";
        User mockUser = new User();
        mockUser.setUsername(username);
        userRepositoryInterface.save(mockUser);

        ApplicationConfig config = new ApplicationConfig(userRepositoryInterface);
        UserDetailsService userDetailsService = config.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertEquals(username, userDetails.getUsername());
        userRepositoryInterface.deleteAll();
    }

    @Test
    public void testUserDetailServiceException(){
        String username = "nonExistentUsername";
        ApplicationConfig config = new ApplicationConfig(userRepositoryInterface);
        UserDetailsService userDetailsService = config.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void testAuthenticationProvider() {
        ApplicationConfig config = new ApplicationConfig(userRepositoryInterface);
        var authenticationProvider = config.authenticationProvider();
        assertNotNull(authenticationProvider);
    }

//    @Test
//    void testAuthenticationManager() throws Exception {
//
//        ApplicationConfig config = new ApplicationConfig(userRepositoryInterface);
//        AuthenticationConfiguration authenticationConfiguration = new AuthenticationConfiguration();
//        var authenticationManager = config.authenticationManager(authenticationConfiguration);
//
//        assertNotNull(authenticationManager);
//
//    }


    @Test
    void testPasswordEncoder() {
        ApplicationConfig config = new ApplicationConfig(userRepositoryInterface);
        var passwordEncoder = config.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
}
