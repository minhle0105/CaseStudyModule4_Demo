package com.casestudy.model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Validator {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Enter your email address ")
    @Size(min = 2,max = 45, message = "Email address must be 2-45 characters long ")
    @Column(name = "email")
    private String email;

    @NotNull
    @Size(min = 2,max = 45, message = "Name must be 2-45 characters long ")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Enter your username ")
    @Column
    @Size(min = 2, max = 15,message = "Username must be 2-15 characters long ")
    private String username;

    @NotNull(message = "Enter your password ")
    private String password;

    @NotNull(message = "Enter your phone number ")
    @Size(min = 9,max = 11,message = "Phone number must be 9-11 digits long ")
    @Column
    private String phoneNumber;

    @NotNull(message = "Enter your address ")
    @Size(min = 2, max = 100,message = "Address must be 2-100 characters long ")
    @Column
    private String addRess;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddRess() {
        return addRess;
    }

    public void setAddRess(String addRess) {
        this.addRess = addRess;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validate(Object target, Errors errors, List<User> users) {

        User user = (User) target;
        String email = user.getEmail();
        String username = user.getUsername();
        String phoneNumber = user.getPhoneNumber();

        for (User u:users) {
            if (u.getEmail().equals(email)){
                errors.rejectValue("email", "email.unique");
            }
            if (u.getUsername().equals(username)){
                errors.rejectValue("username", "username.unique");
            }
            if (u.getPhoneNumber().equals(phoneNumber)){
                errors.rejectValue("phoneNumber", "phoneNumber.unique");
            }
        }
    }
}