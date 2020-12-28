package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String showUploadForm(@PathVariable String recipeId, Model model) {
        log.debug("Get recipe image");
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String handleImagePost(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(recipeId), file);

        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("recipe/{id}/recipeimage")
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));

        byte[] bytesArray = new byte[recipeCommand.getImage().length];

        int i = 0;

        for (Byte wrappedByte : recipeCommand.getImage()) {
            bytesArray[i++] = wrappedByte;
        }

        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(bytesArray);
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
