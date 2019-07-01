package org.launchcode.models.service;

import org.launchcode.models.User;
import org.launchcode.models.data.UserDao;
import org.launchcode.models.data.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/** Class to register users, add to database **/

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // register new user
    @Override
    public User registerNewUser(final UserDto userDto) {

        if (emailExists(userDto.getEmail())) {
            // if email exists in database already
            throw new RuntimeException();
        }
        if (usernameExists(userDto.getUsername())) {
            // if username already exists
            throw new RuntimeException();
        }

        final User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null) {
            user.setPassword( passwordEncoder.encode(userDto.getPassword()) );
        } else {
            user.setPassword( passwordEncoder.encode("rawPassword"));
        }
        return userDao.save(user);
    }

    // getUser

    // saveRegisteredUser

    // deleteUser

    // returns true if email does exist
    private boolean emailExists(final String email) {
        return userDao.findByEmail(email) != null;
    }

    private boolean usernameExists(final String username) {
        return userDao.findByUsername(username) != null;
    }

}
