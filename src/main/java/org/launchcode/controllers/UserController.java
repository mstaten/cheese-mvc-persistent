package org.launchcode.controllers;

import org.launchcode.models.User;
import org.launchcode.models.data.UserDao;
import org.launchcode.models.forms.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title","List of Users");
        model.addAttribute("users", userDao.findAll());
        return "user/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Add User");
        model.addAttribute(new User());
        return "user/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid User user,
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

        userDao.save(user);
        return "redirect:";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("loginForm", new LoginForm());
        return "user/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute @Valid LoginForm form, // form: LoginForm@8299
                        Errors errors, Model model) {

        // check for errors
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors("username")) {
                form.setUsername("");
            }

            model.addAttribute("title", "Login");
            return "user/login";
        }

        // get user by username, if username exists in database
        User thisUser = userDao.findByUsername(form.getUsername());

        // check whether username exists, check for correct username-password combo
        if (thisUser == null || !form.getPassword().equals(thisUser.getPassword())) {
            form.setCustomError("No matching username and password");
            model.addAttribute("title", "Login");
            return "user/login";
        }

        // all correct
        // do something to keep user logged in
        return "redirect:";
    }

}
