package org.launchcode.models.service;

import org.launchcode.models.User;
import org.launchcode.models.data.UserDto;

public interface IUserService {

    User registerNewUser(final UserDto userDto);

    User findUserByUsername(final String username);

    User findUserByEmail(final String email);

    User getUserById(final Integer id);

    // getUser

    // saveRegisteredUser

    // deleteUser
}
