package org.schmuh.quisine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name="Ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column
    public String name;
    @Column
    public int amount;
    @Column
    public boolean vegetarian;
//
//
//    @ManyToMany(mappedBy = "recipe")
//    private Recipe recipes;
}
