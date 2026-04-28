package com.robbie08.officetrack.model;

public class LocationOptionModel {
    private final long id;
    private final String name;

    public LocationOptionModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Spinner uses this for display text
    @Override
    public String toString() {
        return name;
    }
}