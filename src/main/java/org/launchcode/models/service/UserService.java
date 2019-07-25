package org.launchcode.models.service;

import org.launchcode.error.PasswordsMismatchException;
import org.launchcode.error.UserAlreadyExistsException;
import org.launchcode.models.Role;
import org.launchcode.models.User;
import org.launchcode.models.data.RoleDao;
import org.launchcode.models.data.UserDao;
import org.launchcode.models.data.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/** Class to register users and add to database **/

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;

    /**
     * Register new user
     *
     * @param userDto
     * @return user
     * @throws UserAlreadyExistsException
     */
    @Override
    public User registerNewUser(final UserDto userDto) throws UserAlreadyExistsException, PasswordsMismatchException {

        if (usernameExists(userDto.getUsername())) {
            // if username already exists
            String username = userDto.getUsername();
            userDto.setUsername("");
            throw new UserAlreadyExistsException("There is already an account with username: " + username);
        }
        if (emailExists(userDto.getEmail())) {
            // if email exists in database already
            String email = userDto.getEmail();
            userDto.setEmail("");
            throw new UserAlreadyExistsException("There is already an account with email address: " + email);
        }

        // check if password and verify string match
        if (!verifyNewUserPasswords(userDto.getPassword(), userDto.getVerify())) {
            // if don't match, return error
            throw new PasswordsMismatchException("Passwords don't match");
        }

        // actually create the new user w/given fields
        final User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()) );

        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findByName("ROLE_USER")); // automatically make role USER
        user.setRoles(roles);

        return userDao.save(user);
    }

    private boolean verifyNewUserPasswords(String password, String verify) {
        return (password.equals(verify));
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
    public User findUserById(Integer id) {
        return userDao.findOne(id);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
    }

    @Override
    public Boolean checkPasswords(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
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
