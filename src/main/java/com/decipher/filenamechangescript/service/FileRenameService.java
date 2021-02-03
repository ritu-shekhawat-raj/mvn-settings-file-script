package com.decipher.filenamechangescript.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileRenameService {

    private Logger log = LoggerFactory.getLogger(FileRenameService.class);

    private static final String SETTINGS_FILE_NAME = "settings.xml";

    public void getRenamedFile(String projectName) throws IOException {

        String mavenDir = getMavenDirectory();
        File[] filesInDir = getFilesInDirectory();

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (filesInDir != null) {
                for (File file : filesInDir) {
                    String name = file.getName();

                    if (name.equals(projectName)) {
                        inputStream = new FileInputStream(mavenDir + "/" + name);
                        outputStream = new FileOutputStream(mavenDir + "/" + SETTINGS_FILE_NAME);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        log.info(name + " changed to " + SETTINGS_FILE_NAME);
                    }
                }
            }
        } finally {
            Objects.requireNonNull(inputStream).close();
            Objects.requireNonNull(outputStream).close();
        }
    }

    private String getMavenDirectory() {
        return "/home/" + System.getenv("USERNAME") + "/" + ".m2";
    }

    public File[] getFilesInDirectory() {
        String path = getMavenDirectory();
        File directory = new File(path);

        return directory.listFiles((dir, name) -> {
            // match file names with .docx extension
            return name.endsWith(".xml");
        });
    }
}
