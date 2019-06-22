package org.launchcode.models.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginUsernameForm {

    // do i need user id?

    @NotNull
    @Size(min=4, max=15, message = "Username must be between 4 and 15 characters")
    private String username;

    @NotNull
    @Size(min=4, max=15, message = "Password must be between 4 and 15 characters")
    private String password;

    private String customError;

    public LoginUsernameForm() {}

    public LoginUsernameForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginUsernameForm(String username, String password, String customError) {
        this.username = username;
        this.password = password;
        this.customError = customError;
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

    public String getCustomError() {
        return customError;
    }

    public void setCustomError(String customError) {
        this.customError = customError;
    }
}
