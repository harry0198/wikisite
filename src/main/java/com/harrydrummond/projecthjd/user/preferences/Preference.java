package com.harrydrummond.projecthjd.user.preferences;

public enum Preference {

    ACCOUNT_SUMMARY("ACCOUNT_SUMMARY"),
    PROMOTIONS("PROMOTIONS"),
    BRAND_INFO("BRAND_INFO");

    private final String name;

    Preference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}