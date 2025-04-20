package com.recipe.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")
public class Recipe {
    @Id
    private String id;
    private String name;
    private List<String> ingredients;
    private int prepTimeInMinutes;

    public Recipe() {
    }

    public Recipe(String name, List<String> ingredients, int prepTimeInMinutes) {
        this.name = name;
        this.ingredients = ingredients;
        this.prepTimeInMinutes = prepTimeInMinutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public int getPrepTimeInMinutes() {
        return prepTimeInMinutes;
    }

    public void setPrepTimeInMinutes(int prepTimeInMinutes) {
        this.prepTimeInMinutes = prepTimeInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return prepTimeInMinutes == recipe.prepTimeInMinutes &&
                (id == null ? recipe.id == null : id.equals(recipe.id)) &&
                (name == null ? recipe.name == null : name.equals(recipe.name)) &&
                (ingredients == null ? recipe.ingredients == null : ingredients.equals(recipe.ingredients));
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, ingredients, prepTimeInMinutes);
    }
} 