import io.CsvReader;
import model.Achievement;
import service.CardGenerator;
import template.SvgTemplate;

import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path csvPath = Paths.get("conquistas.csv");
        Path outputDir = Paths.get("cards");
        String templateChoice = "default";

        try {
            List<Achievement> conquistas = new CsvReader().read(csvPath);
            SvgTemplate template = SvgTemplate.fromChoice(templateChoice);
            CardGenerator generator = new CardGenerator(template);

            for (Achievement a : conquistas) {
                // Complete-me
            }

            System.out.println("Cartões gerados em: " + outputDir.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
