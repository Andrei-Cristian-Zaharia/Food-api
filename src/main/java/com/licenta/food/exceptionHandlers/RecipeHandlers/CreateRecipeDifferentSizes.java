package com.licenta.food.exceptionHandlers.RecipeHandlers;

public class CreateRecipeDifferentSizes extends RuntimeException {

    public CreateRecipeDifferentSizes() { super("Size of ingredient names and ingredient quantities differ."); }
}