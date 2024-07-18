package com.ssafy.a505.Domain.Voice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Voice {
    long id;
    long userId;
    long like;
    long listenCount;
    String title;
    String imageUrl;
    String voiceUrl;
    VoiceType type;
    long latitude;
    long longitude;
    LocalDateTime dateTime;
}
