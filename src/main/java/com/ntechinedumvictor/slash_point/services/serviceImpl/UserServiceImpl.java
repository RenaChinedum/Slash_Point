package com.ntechinedumvictor.slash_point.services.serviceImpl;

import com.ntechinedumvictor.slash_point.dto.ChangePasswordDTO;
import com.ntechinedumvictor.slash_point.dto.UserDTO;
import com.ntechinedumvictor.slash_point.enums.Role;
import com.ntechinedumvictor.slash_point.model.User;
import com.ntechinedumvictor.slash_point.repository.UserRepository;
import com.ntechinedumvictor.slash_point.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void createNewUser(UserDTO userDTO) {
        Role role = Role.USER;
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setContact(userDTO.getContact());
        user.setRole(role);

        if (userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Email Taken, User Another Email");
            }
            userRepository.save(user);
        }
        throw new RuntimeException("Password Must Match");

    }
    @Override
    public void updateProfile(UserDTO userDTO, HttpServletRequest request){
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        User user = userRepository.findUserById(userID).orElse(null);
        assert user != null;
         user.setFirstName(userDTO.getFirstName());
         user.setLastName(userDTO.getLastName());
         user.setContact(userDTO.getContact());
         user.setAddress(userDTO.getAddress());
         user.setCity(userDTO.getCity());
         user.setState(userDTO.getState());
         user.setPostalCode(userDTO.getPostalCode());
         userRepository.save(user);
    }

    @Override
    public User changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request){
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        User user = userRepository.findUserById(userID).orElse(null);
        assert user != null;
        if(!passwordDTO.getCurrentPassword().equals(user.getPassword())){
            throw new RuntimeException("Incorrect old password, enter a correct password");
        }
        if(!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())){
            throw new RuntimeException("Password must match");
        }
        user.setPassword(passwordDTO.getPassword());
        return userRepository.save(user);
    }
    @Override
    public User updateProfilePicture(UserDTO userDTO,
                                     MultipartFile image,
                                     HttpServletRequest request) throws IOException
    {
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        User user = userRepository.findUserById(userID).orElse(null);

        assert user != null;
            byte[] imageBytes = image.getBytes();
            user.setUserImage(imageBytes);

        return userRepository.save(user);
    }



    @Override
    public User findUser(UserDTO userDTO) {
        User user = userRepository.findUserByEmail(userDTO.getEmail());
        //TODO:EXPLAIN REASON FOR ERROR WITH HASHING
        boolean match = user.checkpassword(userDTO.getPassword(), user.getPassword());
        if (match) {
            return user;
        }
        return null;
    }

    @Override
    public boolean loginUser(String email, String password, HttpServletRequest request) throws Exception {
        User user = userRepository.findByEmail(email).get();
        System.out.println("user = " + user);
        if (!user.getPassword().equals(password) || user.getEmail() == null) {
            throw new Exception("User Details Not found");
        }
        HttpSession session = request.getSession();
        session.setAttribute("userID", user.getId());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("lastName", user.getLastName());
        int id = (int) session.getAttribute("userID");
        return true;
    }
    @Override
    public void deleteAccount(UserDTO userDTO, HttpServletRequest request){
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        User user = userRepository.findUserById(userID).orElse(null);
        assert user != null;
        if(!userDTO.getPassword().equals(user.getPassword())){
            throw new RuntimeException("Incorrect old password, enter a correct password");
        }
      userRepository.delete(user);
    }


}
