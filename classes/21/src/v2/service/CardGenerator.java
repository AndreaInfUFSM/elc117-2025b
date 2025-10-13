package service;

import model.Achievement;
import template.SvgTemplate;
import java.io.IOException;
import java.nio.file.*;

public class CardGenerator {

    private final SvgTemplate template;

    public CardGenerator(SvgTemplate template) {
        this.template = template;
    }

    public String generateCard(Achievement a) {
        String[] parts = a.getTitle().split(" ", 2);
        String title1 = parts[0];
        String title2 = (parts.length > 1) ? parts[1] : "";
        return template.render(title1, title2, a.getName(), a.getReason());
    }

    public void saveCard(Path outputDir, String studentName, String svgContent) throws IOException {
        Files.createDirectories(outputDir);
        String fileName = studentName.replaceAll("\\s+", "_") + ".svg";
        Files.writeString(outputDir.resolve(fileName), svgContent);
    }
}
