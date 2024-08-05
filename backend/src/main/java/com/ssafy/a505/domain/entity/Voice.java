package com.ssafy.a505.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
public class Voice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long voiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonBackReference
    private Member member;

    @Builder.Default
    private Long heartCount = 0L;

    @Builder.Default
    private Long listenCount = 0L;
    String title;
    String imageUrl;

    private String originalName;

    private String savePath;

    private String saveFolder;

    double latitude;
    double longitude;
    LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private VoiceType voiceType;

    @OneToMany(mappedBy = "voice", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<ProcessedVoice> processedVoices = new ArrayList<>();

    @OneToMany(mappedBy = "voice", cascade = CascadeType.ALL, orphanRemoval = false)
    @OrderBy("heartAt ASC")
    private Set<Heart> hearts = new LinkedHashSet<>();

    public void addProcessedVoice(ProcessedVoice voice) {
        this.processedVoices.add(voice);
        voice.setVoice(this);
    }
}
