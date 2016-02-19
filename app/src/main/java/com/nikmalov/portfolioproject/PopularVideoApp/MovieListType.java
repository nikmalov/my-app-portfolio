package com.nikmalov.portfolioproject.PopularVideoApp;

public enum MovieListType {

    POPULAR("/popular"),
    TOP_RATED("/top_rated"),
    FAVOURITES("");//stored locally

    String urlPostfix;

    MovieListType(String urlPostfix) {
        this.urlPostfix = urlPostfix;
    }
}
