package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        service = new ImageServiceImpl(recipeRepository);
    }

    @Test
    public void testSaveImage() throws IOException {
        Long id = 1L;
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        service.saveImageFile(1L, multipartFile);

        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}