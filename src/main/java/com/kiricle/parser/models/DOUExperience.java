package com.kiricle.parser.models;

public enum DOUExperience {
    ANY(""),
    LESS_THAN_ONE_YEAR("0-1"),
    ONE_TO_THREE_YEARS("1-3"),
    THREE_TO_FIVE_YEARS("3-5"),
    MORE_THAN_FIVE("5plus");

    private final String experience;

    DOUExperience(String experience) {
        this.experience = experience;
    }

    public String getExperience() {
        return experience;
    }

}
