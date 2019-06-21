package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=4, max=15, message = "Username must be between 4 and 15 characters")
    private String username;

    @NotNull
    @Size(min=10, max = 30, message = "Email must be between 10 and 30 characters")
    private String email;

    @NotNull
    @Size(min=4, max=15, message = "Password must be between 4 and 15 characters")
    private String password;

    @NotNull(message = "Passwords don't match")
    private String verify;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        checkPassword();
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
        checkPassword();
    }

    public int getId() {
        return id;
    }

    private void checkPassword() {
        if (!password.equals(verify) && verify!=null) {
            setVerify(null);
        }
    }
}
