package dev.e23.dashstar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data 
@Entity 
@Table(name = "users") 
public class User implements Serializable {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    @Column(name = "username", unique = true) 
    private String username;

    @Column(name = "nickname") 
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password") 
    private String password;

    @Column(name = "role") 
    private String role = "user"; 

}