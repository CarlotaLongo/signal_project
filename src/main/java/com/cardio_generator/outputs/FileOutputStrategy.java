package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileOutputStrategy writes patient data to individual files based on labels.
 */
public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory;
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the base directory if it doesn't exist
            Files.createDirectories(Paths.get(baseDirectory));

            // Get or create the file path for the label
            String filePath = fileMap.computeIfAbsent(label,
                    k -> Paths.get(baseDirectory, label + ".txt").toString());

            // Write the data to the file
            try (PrintWriter out = new PrintWriter(
                    Files.newBufferedWriter(Paths.get(filePath),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
                out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                        patientId, timestamp, label, data);
            }

        } catch (IOException e) {
            System.err.println("Error handling file output: " + e.getMessage());
        }
    }
}
