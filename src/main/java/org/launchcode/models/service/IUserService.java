package org.launchcode.models.service;

import org.launchcode.error.PasswordsMismatchException;
import org.launchcode.error.UserAlreadyExistsException;
import org.launchcode.models.User;
import org.launchcode.models.data.UserDto;

public interface IUserService {

    User registerNewUser(final UserDto userDto) throws UserAlreadyExistsException, PasswordsMismatchException;

    User findUserByUsername(final String username);

    User findUserByEmail(final String email);

    User findUserById(final Integer id);

    Boolean checkPasswords(final User user, final String oldPassword);

    void changeUserPassword(final User user, final String password);

    // getUser

    // saveRegisteredUser

    // deleteUser
}
