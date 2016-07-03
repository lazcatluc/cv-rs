package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class LinkData {
    public final long from;
    public final long to;
    public final String relationship;

    @JsonCreator
    public LinkData(@JsonProperty("from") long from, @JsonProperty("to") long to,
            @JsonProperty("relationship") String relationship) {
        this.from = from;
        this.to = to;
        this.relationship = relationship;
    }
}