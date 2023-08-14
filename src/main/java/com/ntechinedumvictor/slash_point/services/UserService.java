package com.ntechinedumvictor.slash_point.services;

import com.ntechinedumvictor.slash_point.dto.ChangePasswordDTO;
import com.ntechinedumvictor.slash_point.dto.UserDTO;
import com.ntechinedumvictor.slash_point.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    public User findByEmail(String email);

    public void createNewUser(UserDTO userDTO);


    void updateProfile(UserDTO userDTO, HttpServletRequest request);

    User changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request);

    User updateProfilePicture(UserDTO userDTO,
                              MultipartFile image,
                              HttpServletRequest request) throws IOException;

    public User findUser(UserDTO userDTO);


    boolean loginUser(String email, String password, HttpServletRequest request) throws Exception;

    void deleteAccount(UserDTO userDTO, HttpServletRequest request);
}
