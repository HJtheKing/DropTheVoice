package com.ssafy.a505.domain.Voice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Voice {
    long id;
    long userId;
    long like;
    long listenCount;
    String title;
    String userName;
    String imageUrl;
    String voiceUrl;
    VoiceType type;
    long latitude;
    long longitude;
    LocalDateTime dateTime;
}
