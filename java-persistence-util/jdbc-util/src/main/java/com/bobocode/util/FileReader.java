package com.bobocode.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * {@link FileReader} provides an API that allow to read whole file into a {@link String} by file name.
 */
public class FileReader {

    /**
     * Returns a {@link String} that contains whole text from the file specified by name.
     *
     * @param fileName a name of a text file
     * @return string that holds whole file content
     */
    public static String readWholeFileFromResources(String fileName) {
        Path filePath = createPathFromFileName(fileName);
        try (Stream<String> fileLinesStream = openFileLinesStream(filePath)) {
            return fileLinesStream.collect(joining("\n"));
        }
    }

    private static Stream<String> openFileLinesStream(Path filePath) {
        try {
            return Files.lines(filePath);
        } catch (IOException e) {
            throw new FileReaderException("Cannot create stream of file lines!", e);
        }
    }

    private static Path createPathFromFileName(String fileName) {
        String s = Objects.requireNonNull(fileName);
        ClassLoader loader = FileReader.class.getClassLoader();
        URL fileUrl = loader.getResource(s);
        return Paths.get(s);
    }
}
