package com.gmail_bssushant2003.journeycraft.MustVisitPlaces;

public class Place {
    private String name;
    private String imageUrl;
    private String bestTimeToVisit;

    public Place(String name, String imageUrl,String bestTimeToVisit) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.bestTimeToVisit = bestTimeToVisit;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getBestTimeToVisit(){return bestTimeToVisit;}
}
