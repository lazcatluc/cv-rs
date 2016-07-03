package com.zee.cv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class NodeData {
    public final long key;
    public final String name;
    public final Property properties[];
    public final Method methods[];

    @JsonCreator
    public NodeData(@JsonProperty("key") long key, @JsonProperty("name") String name,
            @JsonProperty("properties") Property[] properties, @JsonProperty("methods") Method[] methods) {
        this.key = key;
        this.name = name;
        this.properties = properties;
        this.methods = methods;
    }

    public static final class Property {
        public final String name;
        public final String type;

        public final String visibility;

        @JsonCreator
        public Property(@JsonProperty("name") String name, @JsonProperty("type") String type,
                @JsonProperty("visibility") String visibility) {
            this.name = name;
            this.type = type;
            this.visibility = visibility;
        }
    }

    public static final class Method {
        public final String name;
        public final Parameter parameters[];
        public final String visibility;

        @JsonCreator
        public Method(@JsonProperty("name") String name, @JsonProperty("parameters") Parameter[] parameters,
                @JsonProperty("visibility") String visibility) {
            this.name = name;
            this.parameters = parameters;
            this.visibility = visibility;
        }

        public static final class Parameter {
            public final String name;
            public final String type;

            @JsonCreator
            public Parameter(@JsonProperty("name") String name, @JsonProperty("type") String type) {
                this.name = name;
                this.type = type;
            }
        }
    }
}