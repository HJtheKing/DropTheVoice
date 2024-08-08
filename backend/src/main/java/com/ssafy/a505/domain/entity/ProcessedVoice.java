package com.ssafy.a505.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedVoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "processed_voice_id")
    private Long processedVoiceId;

    @Column(columnDefinition = "LONGTEXT")
    private String processedPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_id")
    private Voice voice;

    @Enumerated(EnumType.STRING)
    private VoiceType voiceType;
}
