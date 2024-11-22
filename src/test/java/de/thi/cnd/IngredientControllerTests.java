package de.thi.cnd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.thi.cnd.adapter.api.rest.dto.CreateIngredientRequest;
import de.thi.cnd.adapter.api.rest.dto.UpdateIngredientRequest;
import de.thi.cnd.adapter.jpa.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@DataJpaTest
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@ActiveProfiles("test")
public class IngredientControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    public void cleanUp() {
        ingredientRepository.deleteAll();
    }

    @Test
    public void testCreateIngredient() throws Exception {
        CreateIngredientRequest ingredient = new CreateIngredientRequest();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        mvc.perform(post("/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Zucker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value("g"));
    }

    @Test
    public void testCreateIngredientWithExistingName() throws Exception {
        CreateIngredientRequest ingredient = new CreateIngredientRequest();
        ingredient.setName("Zucker");
        mvc.perform(post("/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Zucker"));

        mvc.perform(post("/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testReadIngredients() throws Exception {
        CreateIngredientRequest ingredient = new CreateIngredientRequest();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        mvc.perform(post("/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/ingredients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Zucker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value("g"));
    }

    @Test
    public void testReadIngredientsByTag() throws Exception {
        CreateIngredientRequest ingredient = new CreateIngredientRequest();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));
        mvc.perform(post("/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/ingredients?tag=süß")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Zucker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value("g"));
    }

    @Test
    public void testUpdateIngredient() throws Exception {
        CreateIngredientRequest ingredient = new CreateIngredientRequest();
        ingredient.setName("Zucker");
        ingredient.setUnit("g");
        ingredient.setTags(List.of("süß"));

        MvcResult result = mvc.perform(post("/ingredients")
                        .content(asJsonString(ingredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
        Long ingredientId = jsonNode.get("id").asLong();
        System.out.println(ingredientId);

        UpdateIngredientRequest updatedIngredient = new UpdateIngredientRequest();
        updatedIngredient.setName("Salz");
        updatedIngredient.setUnit("g");
        updatedIngredient.setTags(List.of("salzig"));
        mvc.perform(put("/ingredients/" + ingredientId)
                        .content(asJsonString(updatedIngredient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/ingredients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Salz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].unit").value("g"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
