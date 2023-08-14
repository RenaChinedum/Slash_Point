package com.ntechinedumvictor.slash_point.dto;

import com.ntechinedumvictor.slash_point.enums.Role;
import com.ntechinedumvictor.slash_point.model.User;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private int id;

    private String email;
    private String password;
    private String confirmPassword;

    private String firstName;

    private String lastName;
    private String contact;
    private String address;
    private String city;
    private String postalCode;

    private byte[] userImage;
    private String state;
    private Role role;

    public UserDTO( User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.contact = user.getContact();
        this.address = user.getAddress();
        this.city =user.getCity();
        this.postalCode = user.getPostalCode();
        this.state = user.getState();
        this.role = user.getRole();
    }
}
