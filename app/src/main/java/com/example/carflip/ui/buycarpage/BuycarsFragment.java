package com.example.carflip.ui.buycarpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carflip.CarLogoActivity;
import com.example.carflip.R;
import com.example.carflip.databinding.FragmentBuycarsBinding;
import com.example.carflip.homepageadapter.CarIconsAdapter;

public class BuycarsFragment extends Fragment {

    private FragmentBuycarsBinding binding;
    private GridView carIconsGridView;
    private RecyclerView allCarsRecyclerView;

    private Integer[] carIcons = {
            R.drawable.maruti_suzuki, R.drawable.tata, R.drawable.hyundai, R.drawable.mahindra,
            R.drawable.bmw, R.drawable.toyota, R.drawable.honda, R.drawable.volkswagen, R.drawable.renault, R.drawable.volvo, R.drawable.jeep, R.drawable.land_rover, R.drawable.nissan, R.drawable.mercedes_benz, R.drawable.mercedes_maybach, R.drawable.mercedes_amg,
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BuycarsViewModel buycarsViewModel = new ViewModelProvider(this).get(BuycarsViewModel.class);
        binding = FragmentBuycarsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupCarIconsGridView(root);
        setupAllCarsRecyclerView(root);

        return root;
    }

    private void setupCarIconsGridView(View root) {
        carIconsGridView = root.findViewById(R.id.car_icons);
        CarIconsAdapter carIconsAdapter = new CarIconsAdapter(requireContext(), carIcons);
        carIconsGridView.setAdapter(carIconsAdapter);
        carIconsGridView.setOnItemClickListener((parent, view, position, id) -> onCarIconClicked(position));
    }

    private void setupAllCarsRecyclerView(View root) {
        allCarsRecyclerView = root.findViewById(R.id.all_cars);
        BuycarsAdapter buycarsAdapter = new BuycarsAdapter(new BuycarsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click event here
                Toast.makeText(requireContext(), "Clicked item at position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        allCarsRecyclerView.setAdapter(buycarsAdapter);
        allCarsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void onCarIconClicked(int position) {
        Intent intent = new Intent(requireContext(), CarLogoActivity.class);
        intent.putExtra("selectedCarIcon", position); // Use "selectedCarIcon" as the key
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
