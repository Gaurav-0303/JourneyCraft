package com.gmail_bssushant2003.journeycraft.MustVisitPlaces;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.opencsv.CSVReader;
import com.airbnb.lottie.L;
import com.gmail_bssushant2003.journeycraft.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MustVisitPlaces extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaceAdapter adapter;
    private List<Place> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_must_visit_places);

        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(getResources().getColor(android.R.color.white,getTheme()));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placeList = readCsvData();

        adapter = new PlaceAdapter(this,placeList);
        recyclerView.setAdapter(adapter);
    }

    private List<Place> readCsvData(){
        List<Place> placesList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(getAssets().open("places_data.csv"))
            );
            CSVReader reader = new CSVReader(br);

            reader.readNext();


            String[] line;
            while ((line = reader.readNext()) != null) {
                String name = line[0];
                String imageUrl = line[1];
                String bestTimeToVisit = line[2];
                Place place = new Place(name, imageUrl,bestTimeToVisit);
                placesList.add(place);
            }

            reader.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return placesList;
    }
}