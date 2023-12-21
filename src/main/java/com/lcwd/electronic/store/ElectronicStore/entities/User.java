package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_gender")
    private String userGender;

    @Column(name = "user_about")
    private String userAbout;

    @Column(name = "user_image_name")
    private String userImageName;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Order> orders=new ArrayList<>();

}
