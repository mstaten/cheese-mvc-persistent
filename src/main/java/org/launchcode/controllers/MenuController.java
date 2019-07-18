package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.User;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.data.UserDao;
import org.launchcode.models.forms.EditMenuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");
        return "menu/index";
    }

    @RequestMapping("{userId}")
    public String viewMenusByUser(@PathVariable int userId, Model model) {
        List<Menu> menus = new ArrayList<>();
        for (Menu menu : menuDao.findAll()) {
            if (menu.getUser().getId() == userId) {
                menus.add(menu);
            }
        }
        model.addAttribute("menus", menus);
        model.addAttribute("title", "Menus by " + userDao.findById(userId).getUsername());
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {

        model.addAttribute(new Menu()); // new menu to bind to form
        model.addAttribute("title", "Add Menu");
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu menu, Errors errors,
                      Model model, Principal principal) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        // get user to add to new menu
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        menu.setUser(user);
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable int menuId, Model model) {

        // find menu w/given id
        Menu menu = menuDao.findOne(menuId);
        model.addAttribute(menu);
        model.addAttribute("title", menu.getName());
        return "menu/view";
    }

    @RequestMapping(value = "edit/{menuId}", method = RequestMethod.GET)
    public String edit(@PathVariable int menuId, Model model) {

        // find menu w/given id
        Menu menu = menuDao.findOne(menuId);

        // get all cheeses
        Iterable<Cheese> allCheeses = cheeseDao.findAll();

        // get all cheeses currently in menu, to preselect in the view
        List<Cheese> preselectCheeses = menu.getCheeses();

        // create menu form
        EditMenuForm form = new EditMenuForm(menuId, allCheeses, preselectCheeses);

        // need checkedId as a checkbox on our form that is always checked,
        // in case user submits form without checking a box
        int checkedId = 0;
        String title = "Edit menu: " + menu.getName();

        model.addAttribute("checkedId", checkedId);
        model.addAttribute("form", form);
        model.addAttribute("title", title);
        return "menu/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute EditMenuForm form) {

        // find relevant menu
        Menu menu = menuDao.findOne(form.getMenuId());

        // get checked cheese ids from the form
        int[] formCheckedCheeseIds = form.getCheckedCheeseIds();

        // delete cheeses currently in menu
        menu.setCheeses(new ArrayList<>());

        // iterate over checked cheese ids, add to menu
        for (int cheeseId : formCheckedCheeseIds) {
            if (cheeseId != 0) { // 0 is default
                // get cheese, add to menu
                Cheese cheese = cheeseDao.findOne(cheeseId);
                menu.addItem(cheese);
            }
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
}