package io;

import model.Achievement;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvReader {

    public List<Achievement> read(Path csvPath) throws IOException {
        List<Achievement> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(csvPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.startsWith("Nome")) continue;
                String[] parts = line.split(",", 3);
                if (parts.length < 3) continue;
                list.add(new Achievement(parts[0].trim(), parts[1].trim(), parts[2].trim()));
            }
        }
        return list;
    }
}
