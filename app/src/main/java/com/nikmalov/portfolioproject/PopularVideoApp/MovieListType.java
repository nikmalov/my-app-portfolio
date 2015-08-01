package com.nikmalov.portfolioproject.PopularVideoApp;

public enum MovieListType {

    POPULAR("/popular"),
    TOP_RATED("/top_rated");

    String urlPostfix;

    MovieListType(String urlPostfix) {
        this.urlPostfix = urlPostfix;
    }
}
