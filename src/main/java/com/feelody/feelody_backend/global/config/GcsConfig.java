package com.feelody.feelody_backend.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcsConfig {

    @Value("${spring.cloud.gcp.credentials.location}")
    private String gcpCredentialsLocation;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials;

        if (gcpCredentialsLocation == null || gcpCredentialsLocation.isBlank()
            || gcpCredentialsLocation.equals("disabled")) {
            credentials = GoogleCredentials.getApplicationDefault();
        } else {
            try (InputStream inputStream = new FileInputStream(
                gcpCredentialsLocation.replaceFirst("file:", ""))) {
                credentials = GoogleCredentials.fromStream(inputStream);
            }
        }

        return StorageOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(credentials)
            .build()
            .getService();

    }
}
