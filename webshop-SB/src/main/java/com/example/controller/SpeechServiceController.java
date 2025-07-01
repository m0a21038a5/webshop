package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;

import jakarta.annotation.PreDestroy;

@RestController
@RequestMapping("/api/speech")
public class SpeechServiceController {
    private final SpeechClient speechClient;

    public SpeechServiceController() throws Exception {
        String quotaProjectId = "gp-08-osusi"; // プロジェクトIDを指定

        // SpeechSettingsを作成し、クォータプロジェクトを設定
        SpeechSettings speechSettings = SpeechSettings.newBuilder()
                .setHeaderProvider(FixedHeaderProvider.create("x-goog-user-project", quotaProjectId))
                .build();

        this.speechClient = SpeechClient.create(speechSettings);
    }

    @PreDestroy
    public void shutdown() {
        if (speechClient != null) {
            speechClient.shutdown(); // SpeechClientをシャットダウン
        }
    }
}