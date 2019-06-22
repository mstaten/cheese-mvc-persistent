package org.launchcode.models.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginEmailForm {

    //

    @NotNull
    @Size(min=10, max = 30, message = "Email must be between 10 and 30 characters")
    private String email;

    @NotNull
    @Size(min=4, max=15, message = "Password must be between 4 and 15 characters")
    private String password;

    private String customError;

    public LoginEmailForm() {}

    public LoginEmailForm(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginEmailForm(String email, String password, String customError) {
        this.email = email;
        this.password = password;
        this.customError = customError;
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
    }

    public String getCustomError() {
        return customError;
    }

    public void setCustomError(String customError) {
        this.customError = customError;
    }
}
