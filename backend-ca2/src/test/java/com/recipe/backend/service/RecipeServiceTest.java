package com.recipe.backend.service;

import com.recipe.backend.model.Recipe;
import com.recipe.backend.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRecipes_ShouldReturnAllRecipes() {
        // Arrange
        Recipe recipe1 = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        Recipe recipe2 = new Recipe("Salad", Arrays.asList("Lettuce", "Tomato"), 10);
        List<Recipe> expectedRecipes = Arrays.asList(recipe1, recipe2);
        
        when(recipeRepository.findAll()).thenReturn(expectedRecipes);

        // Act
        List<Recipe> actualRecipes = recipeService.getAllRecipes();

        // Assert
        assertEquals(expectedRecipes, actualRecipes);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById_WhenRecipeExists_ShouldReturnRecipe() {
        // Arrange
        String id = "123";
        Recipe expectedRecipe = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(expectedRecipe));

        // Act
        Optional<Recipe> actualRecipe = recipeService.getRecipeById(id);

        // Assert
        assertTrue(actualRecipe.isPresent());
        assertEquals(expectedRecipe, actualRecipe.get());
        verify(recipeRepository, times(1)).findById(id);
    }

    @Test
    void getRecipeById_WhenRecipeDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        String id = "123";
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Recipe> actualRecipe = recipeService.getRecipeById(id);

        // Assert
        assertFalse(actualRecipe.isPresent());
        verify(recipeRepository, times(1)).findById(id);
    }

    @Test
    void createRecipe_ShouldSaveAndReturnRecipe() {
        // Arrange
        Recipe recipeToCreate = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        Recipe savedRecipe = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        savedRecipe.setId("123");
        
        when(recipeRepository.save(recipeToCreate)).thenReturn(savedRecipe);

        // Act
        Recipe createdRecipe = recipeService.createRecipe(recipeToCreate);

        // Assert
        assertEquals(savedRecipe, createdRecipe);
        verify(recipeRepository, times(1)).save(recipeToCreate);
    }

    @Test
    void updateRecipe_WhenRecipeExists_ShouldUpdateAndReturnRecipe() {
        // Arrange
        String id = "123";
        Recipe existingRecipe = new Recipe("Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 20);
        existingRecipe.setId(id);
        Recipe updatedRecipe = new Recipe("Updated Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 25);
        updatedRecipe.setId(id);
        
        when(recipeRepository.findById(id)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(updatedRecipe)).thenReturn(updatedRecipe);

        // Act
        Optional<Recipe> result = recipeService.updateRecipe(id, updatedRecipe);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(updatedRecipe, result.get());
        verify(recipeRepository, times(1)).findById(id);
        verify(recipeRepository, times(1)).save(updatedRecipe);
    }

    @Test
    void updateRecipe_WhenRecipeDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        String id = "123";
        Recipe recipeToUpdate = new Recipe("Updated Pasta", Arrays.asList("Pasta", "Tomato Sauce"), 25);
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Recipe> result = recipeService.updateRecipe(id, recipeToUpdate);

        // Assert
        assertFalse(result.isPresent());
        verify(recipeRepository, times(1)).findById(id);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void deleteRecipe_WhenRecipeExists_ShouldReturnTrue() {
        // Arrange
        String id = "123";
        when(recipeRepository.existsById(id)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(id);

        // Act
        boolean result = recipeService.deleteRecipe(id);

        // Assert
        assertTrue(result);
        verify(recipeRepository, times(1)).existsById(id);
        verify(recipeRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteRecipe_WhenRecipeDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String id = "123";
        when(recipeRepository.existsById(id)).thenReturn(false);

        // Act
        boolean result = recipeService.deleteRecipe(id);

        // Assert
        assertFalse(result);
        verify(recipeRepository, times(1)).existsById(id);
        verify(recipeRepository, never()).deleteById(any());
    }
} 