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
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());

        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute @Valid Cheese newCheese,
                                       Errors errors, Model model,
                                       @RequestParam int categoryId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
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
            if (cheeseId != 0) { // 0 is the default; see displayRemoveCheeseForm

                // some issue w/ bulk delete on the owning side
                // of a many-to-many relation ---> means:
                // must delete cheese from menus before completely deleting cheese

                // iterate through all menus
                for (Menu menu : menuDao.findAll()) {

                    // iterate through cheeses of current menu
                    List<Cheese> someCheeses = menu.getCheeses();
                    for (int i = 0; i < someCheeses.size(); i++) {

                        // check current cheese's id against @RequestParam cheeseId
                        if (someCheeses.get(i).getId() == cheeseId) {
                            menu.removeItem(someCheeses.get(i));
                        }
                    }
                }

                // only now may you delete the cheese
                cheeseDao.delete(cheeseId);
            }
        }

        return "redirect:";
    }

    @RequestMapping(value = "category/{categoryId}", method = RequestMethod.GET)
    public String category(Model model, @PathVariable int categoryId) {

        Category theCategory = categoryDao.findOne(categoryId);
        Iterable<Cheese> cheeses = theCategory.getCheeses();
        String title = "Cheeses in the '" + theCategory.getName() + "' category";

        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title", title);

        return "cheese/index";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId) {

        Cheese cheese = cheeseDao.findOne(cheeseId);
        String title = "Edit cheese " + cheese.getName();
        Category autoSelectCategory = cheese.getCategory();

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("cheese", cheese);
        model.addAttribute("title", title);
        model.addAttribute("autoSelectCategory", autoSelectCategory);

        return "cheese/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(@ModelAttribute @Valid Cheese modelCheese,
                                  Errors errors, int id) {

        if (errors.hasErrors()) {
            return "redirect:/cheese/edit/" + id;
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
