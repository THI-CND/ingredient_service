package de.thi.cnd.ingredient;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Ingredient {
    @Id
    @SequenceGenerator(name = "ingredients_sequence", sequenceName = "ingredients_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredients_sequence")
    private Long id;
    private String name;
    private String unit;
    @ElementCollection
    private List<String> tags;

    public Ingredient() {
    }

    public Ingredient(Long id, String name, String unit, List<String> tags) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.tags = tags;
    }

    public Ingredient(String name, String unit, List<String> tags) {
        this.name = name;
        this.unit = unit;
        this.tags = tags;
    }

    public Ingredient(String unit, String name) {
        this.unit = unit;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
