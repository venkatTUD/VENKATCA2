package com.example.ead.be;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class Persistence {
  @Autowired
  private MongoTemplate mongoTemplate;

  // a few pre-wired recipes we can insert into the database as examples.
  public static List<Recipe> recipes = Arrays.asList(
          new Recipe("elotes",
                  Arrays.asList("corn", "mayonnaise", "cotija cheese", "sour cream", "lime" ),
                  35),
          new Recipe("loco moco",
                  Arrays.asList("ground beef", "butter", "onion", "egg", "bread bun", "mushrooms" ),
                  54),
          new Recipe("patatas bravas",
                  Arrays.asList("potato", "tomato", "olive oil", "onion", "garlic", "paprika" ),
                  80),
          new Recipe("fried rice",
                  Arrays.asList("rice", "soy sauce", "egg", "onion", "pea", "carrot", "sesame oil" ),
                  40)
  );

  public List<Recipe> getAllRecipes()
  {
    System.out.println("Getting all recipes from MongoDB...");
    List<Recipe> allRecipes = mongoTemplate.findAll(Recipe.class);
    System.out.println("Found " + allRecipes.size() + " recipes");
    return allRecipes;
  }

  public int addRecipes (List<Recipe> recipes)
  {
    try {
      System.out.println("Attempting to insert " + recipes.size() + " recipes...");
      Collection<Recipe> savedRecipes = mongoTemplate.insertAll(recipes);
      System.out.println("Successfully inserted " + savedRecipes.size() + " recipes");
      return savedRecipes.size();
    } catch (MongoException me) {
      System.err.println("Unable to insert any recipes into MongoDB due to an error: " + me);
      return -1;
    }
  }

  public int deleteRecipes (Bson deleteFilter)
  {
    try {
      DeleteResult deleteResult = mongoTemplate.getCollection("ead_2024").deleteMany(deleteFilter);
      return (int) deleteResult.getDeletedCount();
    } catch (MongoException me) {
      System.err.println("Unable to delete any recipes due to an error: " + me);
      return -1;
    }
  }

  public int deleteRecipesByName (List<String> recipeNames)
  {
    Bson deleteFilter = Filters.in("name", recipeNames);
    return this.deleteRecipes(deleteFilter);
  }

  // The aim of this method is to mimic main to check if my refactoring (in prep for the webservices) is OK.
  public void main2() {
    System.out.println("Starting main2() - clearing existing recipes...");
    mongoTemplate.dropCollection(Recipe.class);
    
    System.out.println("Inserting sample recipes...");
    int insertedCount = addRecipes(recipes);
    System.out.println("Successfully inserted " + insertedCount + " sample recipes");

    List<Recipe> myRecipes = getAllRecipes();
    System.out.println("After insertion, found " + myRecipes.size() + " recipes");
    for (Recipe currentRecipe : myRecipes) {
      System.out.printf("%s has %d ingredients and takes %d minutes to make\n",
              currentRecipe.getName(), currentRecipe.getIngredients().size(), currentRecipe.getPrepTimeInMinutes());
    }
  }
}
