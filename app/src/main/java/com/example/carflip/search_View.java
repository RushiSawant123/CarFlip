package com.example.carflip;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carflip.Data;
import com.example.carflip.DataAdapter;
import com.example.carflip.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class search_View extends AppCompatActivity {

    SearchView searchView;
    DataAdapter adapter;
    RecyclerView searchResultRecyclerView;
    RecyclerView recyclerView;
    List<Data> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        searchResultRecyclerView = findViewById(R.id.search_result_recycle_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(search_View.this, 1);
        searchResultRecyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(search_View.this);
        builder.setCancelable(false);
        builder.setView(R.layout.activity_processlayout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new DataAdapter(search_View.this, dataList);
        searchResultRecyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("CarflipData");
            Log.d("errordb", String.valueOf(databaseReference.getDatabase()));
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Data dataClass = itemSnapshot.getValue(Data.class);
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    public void searchList(String text) {
        List<Data> searchList = new ArrayList<>();
        for (Data data : dataList) {
            if (data.getDataCarBrand().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(data);
            }
        }
        adapter.searchDataList((ArrayList<Data>) searchList);
    }
}