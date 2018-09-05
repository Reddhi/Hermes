package org.gtre.itg.hermes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    private TextView tempView, presView, humView, timeView;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        tempView = findViewById(R.id.temperature_view);
        presView = findViewById(R.id.pressure_view);
        humView = findViewById(R.id.humidity_view);
        timeView = findViewById(R.id.timestamp_view);
        Button historyButton = findViewById(R.id.show_history_button);
        mDatabase = FirebaseDatabaseUtility.getDatabase();
        DatabaseReference myRef = mDatabase.getReference("data");
        Query lastQuery = myRef.orderByKey().limitToLast(1);
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SensorData sensorData = snapshot.getValue(SensorData.class);
                    DecimalFormat df = new DecimalFormat("00.0");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    if(sensorData!=null) {
                        String temperatureText = df.format(sensorData.getTemperature()) + "\u00b0C";
                        String pressureText = "P: " + df.format(sensorData.getPressure())+ " mbar";
                        String humidityText = "H: " + df.format(sensorData.getHumidity())+ "% RH";
                        tempView.setText(temperatureText);
                        presView.setText(pressureText);
                        humView.setText(humidityText);
                        timeView.setText(sensorData.getDatetime());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Handle possible errors.
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HistoryActivity.class);
                mContext.startActivity(intent);
            }
        });
        getFCMToken();
    }

    private void getFCMToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String fcmToken = instanceIdResult.getToken();
                updateDatabase(fcmToken);
            }
        });

    }

    private void updateDatabase(final String token){
        final DatabaseReference dbRef = mDatabase.getReference("users/");
        ValueEventListener dbListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> fcmTokens = new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String str = snapshot.getValue(String.class);
                    if(str!=null && str.equals(token)){
                        return;
                    }
                    fcmTokens.add(str);
                }
                fcmTokens.add(token);
                dbRef.setValue(fcmTokens);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Handle possible errors.
            }
        };
        dbRef.addListenerForSingleValueEvent(dbListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(mContext, SettingsActivity.class);
            mContext.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
