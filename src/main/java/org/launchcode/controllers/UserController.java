package org.launchcode.controllers;

import org.launchcode.models.User;
import org.launchcode.models.data.RoleDao;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IUserService userService;

    @Autowired
    private RoleDao roleDao;

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

        final User newUser = userService.registerNewUser(user);
        userDao.save(newUser);
        return "redirect:";
    }

    @RequestMapping(value = "login/username", method = RequestMethod.GET)
    public String loginUsername(Model model) {
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
        if (thisUser == null || !form.getPassword().equals(thisUser.getPassword())) {
            form.setCustomError("Invalid username and password");
            model.addAttribute("title", "Login");
            return "user/loginUsername";
        }

        // all correct
        // do something HERE to keep user logged in
        return "redirect:";
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
        if (thisUser == null || !form.getPassword().equals(thisUser.getPassword())) {
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

}
