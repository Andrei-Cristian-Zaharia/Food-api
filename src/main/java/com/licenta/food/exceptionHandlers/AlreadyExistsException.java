package com.licenta.food.exceptionHandlers;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException() { super("This object already exists, try creating a new one !"); }
}
