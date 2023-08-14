package com.ntechinedumvictor.slash_point;

import com.ntechinedumvictor.slash_point.enums.Role;
import com.ntechinedumvictor.slash_point.model.User;
import com.ntechinedumvictor.slash_point.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Task7Application {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Task7Application.class, args);
    }

//        @Bean
//    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
//        return args -> {
//            Role role = Role.ADMIN;
//        User user = new User();
//        int id = 2;
//        user.setId(id);
//        user.setEmail("admin@gmail.com");
//        user.setPassword("admin");
//        user.setFirstName("Admin");
//        user.setLastName("Admin");
//        user.setRole(role);
//        user.setContact("07056352674");
//        userRepository.save(user);
//        };
//    }


}
