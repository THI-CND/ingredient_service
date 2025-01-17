package de.thi.cnd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.thi.cnd.adapter.ingoing.rest.dto.CreateIngredientRequestDTO;
import de.thi.cnd.adapter.ingoing.rest.dto.UpdateIngredientRequestDTO;
import de.thi.cnd.adapter.outgoing.jpa.JpaIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class IngredientRestControllerV1Tests {

    private final MockMvc mvc;
    private final JpaIngredientRepository ingredientRepository;

    @Autowired
    public IngredientRestControllerV1Tests(MockMvc mvc, JpaIngredientRepository ingredientRepository) {
        this.mvc = mvc;
        this.ingredientRepository = ingredientRepository;
    }

    @BeforeEach
    public void cleanUp() {
        ingredientRepository.deleteAll();
    }

    @Test
    void testCreateIngredient() throws Exception {
        CreateIngredientRequestDTO ingredient = new CreateIngredientRequestDTO();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Zucker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value("g"));
    }

    @Test
    void testCreateIngredientWithExistingName() throws Exception {
        CreateIngredientRequestDTO ingredient = new CreateIngredientRequestDTO();
        ingredient.setName("Zucker");
        mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Zucker"));

        mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReadIngredientById() throws Exception {
        CreateIngredientRequestDTO ingredient = new CreateIngredientRequestDTO();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        MvcResult result = mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
        long ingredientId = jsonNode.get("id").asLong();

        mvc.perform(get("/api/v1/ingredients/" + ingredientId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Zucker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value("g"));
    }

    @Test
    void testReadIngredients() throws Exception {
        CreateIngredientRequestDTO ingredient = new CreateIngredientRequestDTO();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/api/v1/ingredients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Zucker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value("g"));
    }

    @Test
    void testReadIngredientsByTag() throws Exception {
        CreateIngredientRequestDTO ingredient = new CreateIngredientRequestDTO();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/api/v1/ingredients?tag=süß")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Zucker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value("g"));
    }

    @Test
    void testUpdateIngredient() throws Exception {
        CreateIngredientRequestDTO ingredient = new CreateIngredientRequestDTO();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));

        MvcResult result = mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
        long ingredientId = jsonNode.get("id").asLong();

        UpdateIngredientRequestDTO updatedIngredient = new UpdateIngredientRequestDTO();
        updatedIngredient.setName("Salz");
        updatedIngredient.setUnit("g");
        updatedIngredient.setTags(List.of("salzig"));
        mvc.perform(put("/api/v1/ingredients/" + ingredientId)
                        .content(asJsonString(updatedIngredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/api/v1/ingredients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Salz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value("g"));
    }

    @Test
    void testDeleteIngredient() throws Exception {
        CreateIngredientRequestDTO ingredient = new CreateIngredientRequestDTO();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        MvcResult result = mvc.perform(post("/api/v1/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
        long ingredientId = jsonNode.get("id").asLong();

        mvc.perform(delete("/api/v1/ingredients/" + ingredientId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/ingredients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
