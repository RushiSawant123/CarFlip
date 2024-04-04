package com.example.carflip;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Arrays;
import java.util.List;

public class CarDetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        // Initialize ViewPager2 and DotsIndicator
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);

        // Create a list of image resource IDs (replace with your own drawable resource IDs)
        List<Integer> imageResourceIds = Arrays.asList(
                R.drawable.car_image_1,
                R.drawable.car_image_2,
                R.drawable.car_image_3
        );

        // Create and set adapter for ViewPager2
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageResourceIds);
        viewPager2.setAdapter(adapter);

        // Attach ViewPager2 to DotsIndicator
        dotsIndicator.setViewPager2(viewPager2);


    }
}
