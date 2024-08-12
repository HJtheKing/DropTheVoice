package com.ssafy.a505.global;

import lombok.Getter;

@Getter
public class OfferDto {
    String type;
    String[] sdp;

    public OfferDto() {
    }

    public OfferDto(String type, String[] sdp) {
        this.type = type;
        this.sdp = sdp;
    }
}
