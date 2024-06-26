package com.syn.UserManagement.users.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "UserData", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    private String userName;

    private int age;

    private String gender;

    private Date dob;

    private String email;

    private String address;

    private long mobileNumber;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<UserAccount> userAccounts;

}
