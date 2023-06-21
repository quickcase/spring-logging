package app.quickcase.logging.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
public class SpringBootTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApplication.class, args);
    }

    @RestController
    public static class TestController {

        private class RequestMappingException extends RuntimeException {
            public static final long serialVersionUID = 4328743; // copy from pmd docs :D

            /* default */ RequestMappingException(String message) {
                super(message);
            }
        }

        @RequestMapping("/public")
        public String publicEndpoint() {
            return "OK";
        }

        @RequestMapping("/protected")
        public String protectedEndpoint() {
            return "OK";
        }

        @RequestMapping("/failing")
        public String failingEndpoint() throws RequestMappingException {
            throw new RequestMappingException("Failing endpoint");
        }
    }

    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfig {

        @Bean
        @SuppressWarnings("PMD.SignatureDeclareThrowsException")
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/public").permitAll()
                                                                      .requestMatchers("/failing").permitAll()
                                                                      .requestMatchers("/destroying").permitAll()
                                                                      .requestMatchers("/error").permitAll()
                                                                      .requestMatchers("/protected").authenticated())
                       .build();
        }
    }
}
