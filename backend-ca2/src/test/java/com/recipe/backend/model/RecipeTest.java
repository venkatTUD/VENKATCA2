package com.recipe.backend.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void testNoArgsConstructor() {
        Recipe recipe = new Recipe();
        assertNotNull(recipe);
    }

    @Test
    void testAllArgsConstructor() {
        String name = "Pasta";
        List<String> ingredients = Arrays.asList("Pasta", "Tomato Sauce");
        int prepTime = 20;

        Recipe recipe = new Recipe(name, ingredients, prepTime);

        assertEquals(name, recipe.getName());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(prepTime, recipe.getPrepTimeInMinutes());
    }

    @Test
    void testGettersAndSetters() {
        Recipe recipe = new Recipe();

        String id = "123";
        String name = "Pasta";
        List<String> ingredients = Arrays.asList("Pasta", "Tomato Sauce");
        int prepTime = 20;

        recipe.setId(id);
        recipe.setName(name);
        recipe.setIngredients(ingredients);
        recipe.setPrepTimeInMinutes(prepTime);

        assertEquals(id, recipe.getId());
        assertEquals(name, recipe.getName());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(prepTime, recipe.getPrepTimeInMinutes());
    }

    @Test
    void testEquals() {
        Recipe recipe1 = new Recipe("Pasta", Arrays.asList("Pasta", "Sauce"), 20);
        recipe1.setId("123");

        Recipe recipe2 = new Recipe("Pasta", Arrays.asList("Pasta", "Sauce"), 20);
        recipe2.setId("123");

        Recipe recipe3 = new Recipe("Salad", Arrays.asList("Lettuce", "Tomato"), 10);
        recipe3.setId("456");

        // Test equality
        assertEquals(recipe1, recipe1); // Same object
        assertEquals(recipe1, recipe2); // Equal objects
        assertNotEquals(recipe1, recipe3); // Different objects
        assertNotEquals(recipe1, null); // Null comparison
        assertNotEquals(recipe1, new Object()); // Different class
    }

    @Test
    void testHashCode() {
        Recipe recipe1 = new Recipe("Pasta", Arrays.asList("Pasta", "Sauce"), 20);
        recipe1.setId("123");

        Recipe recipe2 = new Recipe("Pasta", Arrays.asList("Pasta", "Sauce"), 20);
        recipe2.setId("123");

        Recipe recipe3 = new Recipe("Salad", Arrays.asList("Lettuce", "Tomato"), 10);
        recipe3.setId("456");

        // Equal objects should have equal hash codes
        assertEquals(recipe1.hashCode(), recipe2.hashCode());
        // Different objects should have different hash codes
        assertNotEquals(recipe1.hashCode(), recipe3.hashCode());
    }

    @Test
    void testEqualsWithNullFields() {
        Recipe recipe1 = new Recipe();
        recipe1.setId("123");

        Recipe recipe2 = new Recipe();
        recipe2.setId("123");

        assertEquals(recipe1, recipe2);
    }
} 