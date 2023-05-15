package com.licenta.food.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recipe")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "howtoprepare")
    private String howToPrepare;

    @Column(name = "time")
    private int time;

    @Column(name = "difficulty")
    private int difficulty;

    @Column(name = "spiciness")
    private int spiciness;

    @Column(name = "is_vegan")
    private boolean isVegan;

    @Column(name = "public")
    private boolean publicRecipe;

    @Column(name = "image_address")
    private String imageAddress;

    @OneToOne
    @JoinColumn(name = "id_person")
    private Person person;

    @Column(name = "status")
    private String status;
}
