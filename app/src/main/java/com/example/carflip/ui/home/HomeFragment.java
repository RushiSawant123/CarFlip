package com.example.carflip.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carflip.MainActivity;
import com.example.carflip.R;
import com.example.carflip.Register2;
import com.example.carflip.homepageadapter.PopularCarsAdapter;
import com.example.carflip.homepageadapter.popularcarsmodel;
import com.example.carflip.search_View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private TextView greetingTextView;
    private RecyclerView popularCarsRecyclerView;
    private Activity view;
    private View root;

    ImageView Search_symbol;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        greetingTextView = root.findViewById(R.id.greetings);
        popularCarsRecyclerView = root.findViewById(R.id.popularcars);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        popularCarsRecyclerView.setLayoutManager(layoutManager);
        Search_symbol = root.findViewById(R.id.search_symbol);



            Search_symbol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireContext(), search_View.class);
                    startActivity(intent);
                }
            });


        new FetchPopularCarsTask().execute();

        ImageButton locationButton = root.findViewById(R.id.LocationButton);
        locationButton.setOnClickListener(view -> checkLocationPermission());

        setGreetingText();

        return root;
    }

    private class FetchPopularCarsTask extends AsyncTask<Void, Void, List<popularcarsmodel>> {

        @Override
        protected List<popularcarsmodel> doInBackground(Void... voids) {
            List<popularcarsmodel> popularCarsList = new ArrayList<>();

            try {
                String url = "https://www.cartrade.com/";
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                Elements popularCars = doc.select("div.ver_shimg-out");

                for (Element carElement : popularCars) {
                    String carName = carElement.select("a.var_upc_tit").text().trim();
                    String carPrice = carElement.select("div.ver_onlakd1").text().trim();
                    String carImage = carElement.select("img").attr("data-original");

                    popularcarsmodel car = new popularcarsmodel(carImage, carName, carPrice);
                    popularCarsList.add(car);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return popularCarsList;
        }

        @Override
        protected void onPostExecute(List<popularcarsmodel> popularCarsList) {
            super.onPostExecute(popularCarsList);

            PopularCarsAdapter adapter = new PopularCarsAdapter(popularCarsList);
            popularCarsRecyclerView.setAdapter(adapter);
        }
    }

    private void setGreetingText() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        String emoji;

        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = "Good Morning";
            emoji = HomeFragment.this.getEmojiByUnicode(0x1F305); // Sunrise emoji
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = "Good Afternoon";
            emoji = HomeFragment.this.getEmojiByUnicode(0x1F31E); // Sun with face emoji
        } else if (hourOfDay >= 18 && hourOfDay < 24) {
            greeting = "Good Evening";
            emoji = HomeFragment.this.getEmojiByUnicode(0x1F319); // Crescent moon emoji
        } else {
            greeting = "Good Night";
            emoji = HomeFragment.this.getEmojiByUnicode(0x1F634); // Sleeping face emoji
        }

        greetingTextView.setText(greeting + " " + emoji);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void requestLocationUpdates() {
        // Implement location update logic here
        // You can use the code from the previous response to handle location updates
        // Make sure to handle onResume and onPause for starting and stopping location updates
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
