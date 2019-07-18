package org.launchcode.models.data;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** Class transports data of potential new password from the View to Controller **/

@Component
public class PasswordDto {

    @NotNull
    @Size(min=4, max=15, message = "Incorrect current password")
    private String oldPassword;

    @NotNull
    @Size(min=4, max=15, message = "Password must be between 4 and 15 characters")
    private String newPassword;

    public PasswordDto() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
