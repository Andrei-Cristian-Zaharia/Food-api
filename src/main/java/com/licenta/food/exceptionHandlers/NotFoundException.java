package com.licenta.food.exceptionHandlers;

import com.licenta.food.enums.ObjectType;
import com.licenta.food.models.Ingredient;
import com.licenta.food.models.Recipe;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;

public class NotFoundException extends RuntimeException{

    private static final  String NOT_FOUND = " not found.";

    public NotFoundException(String message) { super(message); }

    public NotFoundException(ObjectType objectType, String name) {

        switch (objectType) {
            case RECIPE -> throw new NotFoundException("Recipe with name " + name + NOT_FOUND);
            case INGREDIENT -> throw new NotFoundException("Ingredient with name " + name + NOT_FOUND);
            case PERSON -> throw new NotFoundException("Person with name " + name + NOT_FOUND);
        }
    }

    public NotFoundException(ObjectType objectType, Long id) {

        switch (objectType) {
            case RECIPE -> throw new NotFoundException("Recipe with id " + id + NOT_FOUND);
            case INGREDIENT -> throw new NotFoundException("Ingredient with id " + id + NOT_FOUND);
            case PERSON -> throw new NotFoundException("Person with id " + id + NOT_FOUND);
        }
    }
}
