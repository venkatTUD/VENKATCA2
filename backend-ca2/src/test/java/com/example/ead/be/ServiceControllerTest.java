package com.example.ead.be;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceControllerTest {

    @Mock
    private Persistence persistence;

    @InjectMocks
    private ServiceController serviceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        // Arrange
        doNothing().when(persistence).main2();

        // Act
        String result = serviceController.index();

        // Assert
        assertEquals("Greetings from EAD CA2 Template project 2023-24!", result);
        verify(persistence, times(1)).main2();
    }

    @Test
    void testGetAllRecipes_WhenRecipesExist() {
        // Arrange
        List<Recipe> expectedRecipes = Arrays.asList(
            new Recipe("Recipe1", Arrays.asList("ing1", "ing2"), 30),
            new Recipe("Recipe2", Arrays.asList("ing3", "ing4"), 45)
        );
        when(persistence.getAllRecipes()).thenReturn(expectedRecipes);

        // Act
        List<Recipe> result = serviceController.getAllRecipes();

        // Assert
        assertEquals(expectedRecipes, result);
        verify(persistence, times(1)).getAllRecipes();
        verify(persistence, never()).main2();
    }

    @Test
    void testGetAllRecipes_WhenNoRecipesExist() {
        // Arrange
        List<Recipe> emptyList = Collections.emptyList();
        List<Recipe> expectedRecipes = Arrays.asList(
            new Recipe("Sample1", Arrays.asList("ing1", "ing2"), 30),
            new Recipe("Sample2", Arrays.asList("ing3", "ing4"), 45)
        );
        
        when(persistence.getAllRecipes())
            .thenReturn(emptyList)
            .thenReturn(expectedRecipes);
        doNothing().when(persistence).main2();

        // Act
        List<Recipe> result = serviceController.getAllRecipes();

        // Assert
        assertEquals(expectedRecipes, result);
        verify(persistence, times(2)).getAllRecipes();
        verify(persistence, times(1)).main2();
    }

    @Test
    void testDeleteRecipe() {
        // Arrange
        String recipeName = "TestRecipe";
        int expectedResult = 1;
        when(persistence.deleteRecipesByName(Arrays.asList(recipeName))).thenReturn(expectedResult);

        // Act
        int result = serviceController.deleteRecipe(recipeName);

        // Assert
        assertEquals(expectedResult, result);
        verify(persistence, times(1)).deleteRecipesByName(Arrays.asList(recipeName));
    }

    @Test
    void testSaveRecipe() {
        // Arrange
        Recipe recipe = new Recipe("NewRecipe", Arrays.asList("ing1", "ing2"), 30);
        int expectedResult = 1;
        when(persistence.addRecipes(Arrays.asList(recipe))).thenReturn(expectedResult);

        // Act
        int result = serviceController.saveRecipe(recipe);

        // Assert
        assertEquals(expectedResult, result);
        verify(persistence, times(1)).addRecipes(Arrays.asList(recipe));
    }
} 