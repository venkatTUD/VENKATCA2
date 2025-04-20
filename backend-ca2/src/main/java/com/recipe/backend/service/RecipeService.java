package com.recipe.backend.service;

import com.recipe.backend.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Optional<Recipe> getRecipeById(String id);
    Recipe createRecipe(Recipe recipe);
    Optional<Recipe> updateRecipe(String id, Recipe recipe);
    boolean deleteRecipe(String id);
} 