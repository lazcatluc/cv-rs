package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class LinkDataArray {
    public final long from;
    public final long to;
    public final String text;

    @JsonCreator
    public LinkDataArray(@JsonProperty("from") long from, @JsonProperty("to") long to,
            @JsonProperty("text") String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }
}