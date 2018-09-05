package org.gtre.itg.hermes;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    final String TAG = "Firebase";
    private ArrayList<SensorData> temperatureHistory;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DatabaseReference myRef = FirebaseDatabaseUtility.getDatabase().getReference("data");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot historySnapshot) {
                temperatureHistory = new ArrayList<>();
                for (DataSnapshot dataSnapshot: historySnapshot.getChildren()) {
                    SensorData data = dataSnapshot.getValue(SensorData.class);
                    temperatureHistory.add(data);
                }
                mAdapter = new TemperatureDataAdapter(temperatureHistory);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
