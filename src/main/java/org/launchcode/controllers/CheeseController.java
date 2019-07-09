package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {

        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese()); // pass in cheese to bind to form
        model.addAttribute("categories", categoryDao.findAll());

        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute @Valid Cheese newCheese,
                                       Errors errors, Model model,
                                       @RequestParam int categoryId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }

        // find category with given id, set category of newCheese, save newCheese
        newCheese.setCategory(categoryDao.findOne(categoryId));
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {

        // need checkedId as a checkbox on our form that is always checked,
        // in case user submits form without checking a box
        int checkedId = 0;
        model.addAttribute("checkedId", checkedId);
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");

        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        // iterate over checked cheeses from the form
        for (int cheeseId : cheeseIds) {
            if (cheeseId != 0) {
                // 0 is the default; see displayRemoveCheeseForm
                cheeseDao.delete(cheeseId);
            }
        }

        return "redirect:";
    }

    @RequestMapping(value = "category/{categoryId}", method = RequestMethod.GET)
    public String category(Model model, @PathVariable int categoryId) {

        // find category with given id
        Category theCategory = categoryDao.findOne(categoryId);

        // find all cheeses in this category
        Iterable<Cheese> cheeses = theCategory.getCheeses();
        String title = "Cheeses in the '" + theCategory.getName() + "' category";

        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title", title);

        return "cheese/index";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId) {

        // find cheese w/given id
        Cheese cheese = cheeseDao.findOne(cheeseId);

        // auto-select current cheese's category on category drop-down menu
        Category autoSelectCategory = cheese.getCategory();
        String title = "Edit cheese " + cheese.getName();

        // id needs to be persistent (otherwise causes problems when processing form w/errors)
        model.addAttribute("id", cheeseId);
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("cheese", cheese);
        model.addAttribute("title", title);
        model.addAttribute("autoSelectCategory", autoSelectCategory);

        return "cheese/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(@ModelAttribute @Valid Cheese modelCheese,
                                  Errors errors, int id, Model model) {

        if (errors.hasErrors()) {
            // find cheese w/given id already saved in cheeseDao
            Cheese origCheese = cheeseDao.findOne(id);

            // need modelCheese added to model since it has errors attached
            // but set name and desc of modelCheese to those of origCheese
            modelCheese.setName(origCheese.getName());
            modelCheese.setDescription(origCheese.getDescription());
            model.addAttribute("cheese", modelCheese);

            // auto-select current cheese's category on category drop-down menu
            Category autoSelectCategory = modelCheese.getCategory();
            String title = "Edit cheese " + modelCheese.getName();

            // id needs to be persistent
            model.addAttribute("id", id);
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute("title", title);
            model.addAttribute("autoSelectCategory", autoSelectCategory);
            return "cheese/edit";
        }

        // find cheese to be edited
        Cheese theCheese = cheeseDao.findOne(id);

        // edit fields
        theCheese.setName(modelCheese.getName());
        theCheese.setDescription(modelCheese.getDescription());
        theCheese.setCategory(modelCheese.getCategory());

        // save edits
        cheeseDao.save(theCheese);
        return "redirect:";
    }

}
