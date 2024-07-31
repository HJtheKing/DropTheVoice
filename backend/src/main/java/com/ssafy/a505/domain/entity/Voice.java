package com.ssafy.a505.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
public class Voice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long voiceId;
    long userId;
    long heart;
    long listenCount;
    String title;
    String userName;
    String imageUrl;

    private String originalName;

    private String savePath;

    private String saveFolder;

    double latitude;
    double longitude;
    LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private VoiceType voiceType;

    @OneToMany(mappedBy = "voice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProcessedVoice> processedVoices = new ArrayList<>();

    public void addProcessedVoice(ProcessedVoice voice) {
        this.processedVoices.add(voice);
        voice.setVoice(this);
    }
}
