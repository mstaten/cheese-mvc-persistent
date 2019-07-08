package org.launchcode.models.service;

import org.launchcode.models.Role;
import org.launchcode.models.User;
import org.launchcode.models.data.RoleDao;
import org.launchcode.models.data.UserDao;
import org.launchcode.models.data.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/** Class to register users, add to database **/

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;

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

        // actually create the new user w/given fields
        final User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        //user.setRoles(Arrays.asList(roleDao.findByName("ROLE_USER")));
        List<Role> roles = Arrays.asList(roleDao.findByName("ROLE_USER"));
        user.setRoles(roles);

        user.setPassword( passwordEncoder.encode(userDto.getPassword()) );
        return userDao.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User getUserById(Integer id) {
        return userDao.findOne(id);
    }

    private Boolean emailExist(final String email) {
        return userDao.findByEmail(email) != null;
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
