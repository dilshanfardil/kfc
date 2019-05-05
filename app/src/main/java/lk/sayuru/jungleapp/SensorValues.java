package lk.sayuru.jungleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SensorValues extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthLister;

    public static DatabaseReference mRef;
    private DatabaseReference mDatabase;
    private ChildEventListener childEventListener;

    private String selectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_values);

        Toast.makeText(SensorValues.this,"This is Sensor valies Activitiy", Toast.LENGTH_LONG).show();

        final Spinner spinner = (Spinner) findViewById(R.id.stationNames);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.stations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




        mRef = FirebaseDatabase.getInstance().getReference();

        mDatabase = mRef.getDatabase().getReference();
        DatabaseReference sensorValues = mDatabase.child("SensorValues").child("Colombo");
        initialSetText(sensorValues);


        childEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                selectedItem = spinner.getSelectedItem().toString();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren() ;

                for (DataSnapshot element : children) {
                    System.out.println(element.toString() + "*******************************");
                    if(element.getKey().equals(selectedItem)){
                        DatabaseReference sensorValues = mDatabase.child("SensorValues").child(selectedItem);

                        Toast.makeText(SensorValues.this,"Properties Are Changing " + element.getValue(SensorStations.class)  , Toast.LENGTH_LONG).show();
                        setValuesToFields(element.getValue(SensorStations.class));
                    }

                }


                System.out.println("------------------------------- " + selectedItem);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initialSetText(DatabaseReference sensorValues) {

        SensorStations s = new SensorStations(0,0,0,0);
        setValuesToFields(s);

    }

    private void setValuesToFields(SensorStations value) {

        TextView humuduty = findViewById(R.id.txtHumidity);
        TextView pressure = findViewById(R.id.txtPressure);
        TextView rainFall = findViewById(R.id.txtRainfall);
        TextView temp = findViewById(R.id.txtTemparature);

        humuduty.setText(value.getHumidity() + "");
        pressure.setText(value.getPressure() + "");
        rainFall.setText(value.getRainFall() + "");
        temp.setText(value.getTemperature() + "");

    }
}
