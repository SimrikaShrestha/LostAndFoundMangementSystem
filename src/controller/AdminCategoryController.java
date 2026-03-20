package controller;

import java.util.List;

import model.Category;
import model.CategoryCrud;

public class AdminCategoryController {
    private CategoryCrud crud = new CategoryCrud();

    public List<Category> getAllCategories() { return crud.getAllCategories(); }

    public void addCategory(String name, String description) {
        crud.createCategory(name, description);
    }

    public void updateCategory(int id, String name, String description) {
        crud.updateCategory(id, name, description);
    }

    public void deleteCategory(int id) { crud.deleteCategory(id); }
}