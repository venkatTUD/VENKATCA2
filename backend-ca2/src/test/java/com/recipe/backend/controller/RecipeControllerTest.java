package com.recipe.backend.controller;

import com.recipe.backend.model.Recipe;
import com.recipe.backend.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRecipes_ShouldReturnListOfRecipes() {
        // Arrange
        Recipe recipe1 = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        Recipe recipe2 = new Recipe("Salad", Arrays.asList("Lettuce", "Tomato"), 10);
        List<Recipe> expectedRecipes = Arrays.asList(recipe1, recipe2);
        
        when(recipeService.getAllRecipes()).thenReturn(expectedRecipes);

        // Act
        ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRecipes, response.getBody());
        verify(recipeService, times(1)).getAllRecipes();
    }

    @Test
    void getRecipeById_WhenRecipeExists_ShouldReturnRecipe() {
        // Arrange
        String id = "123";
        Recipe expectedRecipe = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        when(recipeService.getRecipeById(id)).thenReturn(Optional.of(expectedRecipe));

        // Act
        ResponseEntity<Recipe> response = recipeController.getRecipeById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRecipe, response.getBody());
        verify(recipeService, times(1)).getRecipeById(id);
    }

    @Test
    void getRecipeById_WhenRecipeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        String id = "123";
        when(recipeService.getRecipeById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Recipe> response = recipeController.getRecipeById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(recipeService, times(1)).getRecipeById(id);
    }

    @Test
    void createRecipe_ShouldReturnCreatedRecipe() {
        // Arrange
        Recipe recipeToCreate = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        Recipe createdRecipe = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        createdRecipe.setId("123");
        
        when(recipeService.createRecipe(recipeToCreate)).thenReturn(createdRecipe);

        // Act
        ResponseEntity<Recipe> response = recipeController.createRecipe(recipeToCreate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdRecipe, response.getBody());
        verify(recipeService, times(1)).createRecipe(recipeToCreate);
    }

    @Test
    void updateRecipe_WhenRecipeExists_ShouldReturnUpdatedRecipe() {
        // Arrange
        String id = "123";
        Recipe recipeToUpdate = new Recipe("Updated Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 25);
        Recipe updatedRecipe = new Recipe("Updated Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 25);
        updatedRecipe.setId(id);
        
        when(recipeService.updateRecipe(id, recipeToUpdate)).thenReturn(Optional.of(updatedRecipe));

        // Act
        ResponseEntity<Recipe> response = recipeController.updateRecipe(id, recipeToUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRecipe, response.getBody());
        verify(recipeService, times(1)).updateRecipe(id, recipeToUpdate);
    }

    @Test
    void updateRecipe_WhenRecipeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        String id = "123";
        Recipe recipeToUpdate = new Recipe("Updated Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 25);
        when(recipeService.updateRecipe(id, recipeToUpdate)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Recipe> response = recipeController.updateRecipe(id, recipeToUpdate);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(recipeService, times(1)).updateRecipe(id, recipeToUpdate);
    }

    @Test
    void deleteRecipe_WhenRecipeExists_ShouldReturnNoContent() {
        // Arrange
        String id = "123";
        when(recipeService.deleteRecipe(id)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = recipeController.deleteRecipe(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(recipeService, times(1)).deleteRecipe(id);
    }

    @Test
    void deleteRecipe_WhenRecipeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        String id = "123";
        when(recipeService.deleteRecipe(id)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = recipeController.deleteRecipe(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(recipeService, times(1)).deleteRecipe(id);
    }
} 