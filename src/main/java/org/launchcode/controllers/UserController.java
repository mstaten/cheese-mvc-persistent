package org.launchcode.controllers;

import org.launchcode.models.User;
import org.launchcode.models.data.UserDao;
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
    public String add(@ModelAttribute @Valid User user, Errors errors,
                      Model model) {
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

}
