package org.launchcode.models.forms;

import org.launchcode.models.Cheese;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EditMenuForm {

    @NotNull
    private int menuId;

    private int[] checkedCheeseIds;

    private Iterable<Cheese> allCheeses;

    private List<Cheese> preselectCheeses;

    /** Constructors **/
    public EditMenuForm() {}

    public EditMenuForm(int menuId, Iterable<Cheese> allCheeses,
                        List<Cheese> preselectCheeses) {
        this.menuId = menuId;
        this.allCheeses = allCheeses;
        this.preselectCheeses = preselectCheeses;
    }

    /** Getters and Setters **/
    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int[] getCheckedCheeseIds() {
        return checkedCheeseIds;
    }

    public void setCheckedCheeseIds(int[] checkedCheeseIds) {
        this.checkedCheeseIds = checkedCheeseIds;
    }

    public Iterable<Cheese> getAllCheeses() {
        return allCheeses;
    }

    public void setAllCheeses(Iterable<Cheese> allCheeses) {
        this.allCheeses = allCheeses;
    }

    public List<Cheese> getPreselectCheeses() {
        return preselectCheeses;
    }

    public void setPreselectCheeses(List<Cheese> preselectCheeses) {
        this.preselectCheeses = preselectCheeses;
    }
}
