package controller;

import java.util.List;
import model.Category;
import model.CategoryCrud;

public class AdminCategoryController {
    
    // Encapsulation
    private CategoryCrud crud = new CategoryCrud();

    // Returns the list of all categories
    public List<Category> getAllCategories() { 
        return crud.getAllCategories(); 
    }

    // Adds a new category by calling the create method
    public void addCategory(String name, String description) {
        crud.createCategory(name, description);
    }

    // Updates an existing category using its id
    public void updateCategory(int id, String name, String description) {
        crud.updateCategory(id, name, description);
    }

    // Deletes a category using its id
    public void deleteCategory(int id) { 
        crud.deleteCategory(id); 
    }
}