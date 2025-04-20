package com.example.ead.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class ServiceController {

	@Autowired
	private Persistence p;

	@GetMapping("/")
	public String index() {
		p.main2();
		return "Greetings from EAD CA2 Template project 2023-24!";
	}

	@GetMapping("/recipes")
	public List<Recipe> getAllRecipes()
	{
		System.out.println("About to get all the recipes in MongoDB!");
		List<Recipe> recipes = p.getAllRecipes();
		if (recipes.isEmpty()) {
			System.out.println("No recipes found, inserting sample recipes...");
			p.main2();
			recipes = p.getAllRecipes();
		}
		return recipes;
	}

	@DeleteMapping("/recipe/{name}")
	public int deleteRecipe(@PathVariable("name") String name)
	{
		System.out.println("About to delete all the recipes named "+name);
		return p.deleteRecipesByName(Arrays.asList(name));
	}

	@PostMapping("/recipe")
	@ResponseStatus(HttpStatus.CREATED)
	public int saveRecipe(@RequestBody Recipe rec)
	{
		System.out.println("About to add the following recipe: "+rec);
		return p.addRecipes(Arrays.asList(rec));
	}
}
