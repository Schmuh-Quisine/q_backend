package org.schmuh.quisine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column
    public String name;

    @Column
    public String description;

    @Column
    public String image;

    @Column
    public String steps;


//    @ManyToMany()
//    public Ingredient ingredientList;

    @Column
    public int personAmount;

    @Column
    public int durationInMinute;

//    @ManyToMany(mappedBy = "recipe")
//    public Tag tagList;




}
