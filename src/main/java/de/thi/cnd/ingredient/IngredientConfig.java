package de.thi.cnd.ingredient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class IngredientConfig {

    @Bean
    CommandLineRunner commandLineRunner(IngredientRepository repository) {
        return args -> {
            Ingredient mehl = new Ingredient("Mehl", "Gramm", Arrays.asList("vegan", "vegetarisch"));
            Ingredient butter = new Ingredient("Butter", "Gramm", Arrays.asList("vegetarisch"));

            repository.saveAll(List.of(mehl, butter));
        };
    }

}
