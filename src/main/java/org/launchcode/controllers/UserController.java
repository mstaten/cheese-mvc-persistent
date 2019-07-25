package org.launchcode.controllers;

import org.launchcode.error.PasswordsMismatchException;
import org.launchcode.error.UserAlreadyExistsException;
import org.launchcode.models.User;
import org.launchcode.models.data.PasswordDto;
import org.launchcode.models.data.UserDao;
import org.launchcode.models.data.UserDto;
import org.launchcode.models.forms.LoginEmailForm;
import org.launchcode.models.forms.LoginUsernameForm;
import org.launchcode.models.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title","List of Users");
        model.addAttribute("users", userDao.findAll());
        return "user/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Add User");
        model.addAttribute(new UserDto());
        return "user/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid UserDto user,
                      Errors errors, Model model) {

        // if user input is invalid
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors("email")) {
                user.setEmail("");
            }
            if (errors.hasFieldErrors("username")) {
                user.setUsername("");
            }

            model.addAttribute("title", "Add User");
            return "user/add";
        }

        User newUser;

        // try to register new user, see if username / email already exists
        try {
            // might throw UserAlreadyExists exception
            newUser = userService.registerNewUser(user);

        } catch (UserAlreadyExistsException | PasswordsMismatchException ex) {

            // display add form again with error
            model.addAttribute("title", "Add User");
            model.addAttribute("ex", ex);
            return "user/add";
        }

        // only save if username and email don't already exist
        userDao.save(newUser);
        return "redirect:";
    }

    @RequestMapping(value = "login/username?error=true", method = RequestMethod.GET)
    public String loginUsernameAfterError(Model model) {
        // if there's an error message, show it
        model.addAttribute("errorMessage", "Bad creds, babe");
        model.addAttribute("title", "Login");
        model.addAttribute("loginUsernameForm", new LoginUsernameForm());
        return "user/loginUsername";
    }

    @RequestMapping(value = "login/username", method = RequestMethod.GET)
    public String loginUsername(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        // if already logged in, redirect to cheese index
        if (principal != null) {
            return "redirect:/cheese";
        }
        //Object msg = request.getAttribute("message");
        //request.getSession();
        //String msg = request.getSession().getAttribute("msg")

        model.addAttribute("title", "Login");
        model.addAttribute("loginUsernameForm", new LoginUsernameForm());
        return "user/loginUsername";
    }

    @RequestMapping(value = "login/username", method = RequestMethod.POST)
    public String loginUsername(@ModelAttribute @Valid LoginUsernameForm form,
                                Errors errors, Model model) {

        // check for errors
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors("username")) {
                form.setUsername("");
            }

            model.addAttribute("title", "Login");
            return "user/loginUsername";
        }

        // get user by username, if username exists in database
        User thisUser = userDao.findByUsername(form.getUsername());

        // check whether username exists, check for correct username-password combo
        /*if (thisUser == null || !form.getPassword().equals(thisUser.getPassword())) {
            form.setCustomError("Invalid username and password");
            model.addAttribute("title", "Login");
            return "user/loginUsername";
        }*/

        // all correct
        // do something HERE to keep user logged in
        return "redirect:";
    }

    @RequestMapping(value = "login/error")
    public String loginError(Model model, HttpServletRequest request) {
        Object msg = request.getAttribute("msg");
        //Object msg = request.getSession().getAttribute("msg");
        LoginUsernameForm form = new LoginUsernameForm();
        form.setCustomError("bad creds babe");

        model.addAttribute("title", "Login");
        model.addAttribute("loginUsernameForm", form);
        return "user/loginUsername";
    }

    @RequestMapping(value = "login/email", method = RequestMethod.GET)
    public String loginEmail(Model model) {
        model.addAttribute("title", "Login by Email");
        model.addAttribute("loginEmailForm", new LoginEmailForm());
        return "user/loginEmail";
    }

    @RequestMapping(value = "login/email", method = RequestMethod.POST)
    public String loginEmail(@ModelAttribute @Valid LoginEmailForm form,
                             Errors errors, Model model) {

        // check for errors in errors obj
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors("email")) {
                form.setEmail("");
            }

            model.addAttribute("title", "Login by Email");
            return "user/loginEmail";
        }

        // get user by email, if email exists in database
        User thisUser = userDao.findByEmail(form.getEmail());

        // check whether email exists, check for correct email-password combo
        if (thisUser == null || userService.checkPasswords(thisUser, form.getPassword())) {
            form.setCustomError("Invalid email and password");
            model.addAttribute("title", "Login by Email");
            return "user/loginEmail";
        }

        // else: correct email and password combo
        // log user in
        return "redirect:/user";
    }

    @RequestMapping(value = "invalidSession")
    public String invalidSession(Model model) {
        model.addAttribute("title", "Invalid Session");
        return "user/invalidSession";
    }

    @RequestMapping(value = "expiredSession")
    public String expiredSession(Model model) {
        model.addAttribute("title", "Expired Session");
        return "user/expiredSession";
    }

    @RequestMapping(value = "profile/{userId}", method = RequestMethod.GET)
    public String showProfile(@PathVariable int userId, Model model,
                              Principal principal) {

        User user = userDao.findByUsername(principal.getName());
        if (userId != user.getId()) {
            // doesn't belong to current user, shouldn't be able to view it
            return "redirect:/user/unauthorized";
        }

        model.addAttribute("passwordDto", new PasswordDto());
        model.addAttribute("title", "My Account");
        return "user/profile";
    }

    @RequestMapping(value = "profile/{userId}", method = RequestMethod.POST)
    public String changePassword(@PathVariable int userId,
                                 @ModelAttribute @Valid PasswordDto passwordDto,
                                 Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "My Account");
            return "user/profile";
        }

        final User user = userDao.findById(userId); // should probably do try-catch here or sth

        // see if old passwords match
        if (userService.checkPasswords(user, passwordDto.getOldPassword())) {
            // if true, may change password
            userService.changeUserPassword(user, passwordDto.getNewPassword());
            model.addAttribute("successMessage", "Password updated!");
        } else {
            // passwords don't match, may not change
            model.addAttribute("failureMessage", "Incorrect current password");
        }

        model.addAttribute("title", "My Account");
        return "user/profile";
    }

    @RequestMapping(value = "unauthorized")
    public String unauthorized(Model model) {
        model.addAttribute("title", "Unauthorized");
        return "user/unauthorized";
    }

}
