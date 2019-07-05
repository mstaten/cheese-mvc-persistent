package org.launchcode.models.service;

import org.launchcode.models.UserPrincipal;
import org.launchcode.models.User;
import org.launchcode.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/** Class that loads user-specific data **/

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    public MyUserDetailsService() {}

    // API

    /** this method simplifies support for new data-access strategies **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User 404");
        }

        return new UserPrincipal(user);

        /*try {
            final User user;
            // check if username or email
            if (username.contains("@")) {
                // is an email
                user = userDao.findByEmail(username);
            } else { // is a username
                user = userDao.findByUsername(username);
            }
            if (user == null) {
                throw new UsernameNotFoundException("No user found with username" + username);
            }

            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                user.getPassword(), user.isEnabled(),true,
                    true, true, getAuthorities(user.getRoles()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }*/
    }


}