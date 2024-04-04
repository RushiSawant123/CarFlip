package com.example.carflip;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class all_cars_page extends AppCompatActivity {
RecyclerView recyclerView;
List<Data> dataList;
DatabaseReference databaseReference;
ValueEventListener eventListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_cars_page);
       recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager= new GridLayoutManager(all_cars_page.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(all_cars_page.this);
        builder.setCancelable(false);
        builder.setView(R.layout.activity_processlayout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        DataAdapter adapter = new DataAdapter(all_cars_page.this,dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("0");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot itemSnapshot : snapshot.getChildren()){
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
    }
}