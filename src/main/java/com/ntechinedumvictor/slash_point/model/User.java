package com.ntechinedumvictor.slash_point.model;


import com.ntechinedumvictor.slash_point.dto.UserDTO;
import com.ntechinedumvictor.slash_point.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String password;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    private String contact;
    private String address;
    private String city;
    private String postalCode;
    private String state;
    private String salt;
    @Lob
    private byte[] userImage;

    private Role role;

    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.contact = userDTO.getContact();
        this.address = userDTO.getAddress();
        this.city = userDTO.getCity();
        this.postalCode = userDTO.getPostalCode();
        this.state = userDTO.getState();
        this.role = userDTO.getRole();
    }

    public User(String email, String password, String firstName, String lastName, String contact) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
    }

    public User(String email, String password) {
        this.email = email;
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, salt);
    }

    public boolean checkpassword(String password, String hashPassword){
        return BCrypt.checkpw(password, hashPassword);
    }


}
