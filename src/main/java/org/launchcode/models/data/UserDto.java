package org.launchcode.models.data;

import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** Class transports data of potential user from the View to Controller **/

@Component
public class UserDto {

    @NotNull
    @Size(min=4, max=15, message = "Password must be between 4 and 15 characters")
    @Column(length = 60)
    private String password;

    @NotNull(message = "Passwords don't match")
    @Column(length = 60)
    private String verify;

    @NotNull
    @Size(min=10, max = 30, message = "Email must be between 10 and 30 characters")
    private String email;

    @NotNull
    @Size(min=4, max=15, message = "Username must be between 4 and 15 characters")
    private String username;

    public UserDto() {}

    public UserDto(String password, String verify, String email) {
        this.password = password;
        this.verify = verify;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
