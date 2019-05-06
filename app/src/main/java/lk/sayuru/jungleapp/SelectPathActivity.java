package lk.sayuru.jungleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import lk.sayuru.jungleapp.content.PathContent;
import lk.sayuru.jungleapp.content.PlaceContent;
import lk.sayuru.jungleapp.db.entity.PathPoint;
import lk.sayuru.jungleapp.path.PathFragment;
import lk.sayuru.jungleapp.path.PlaceFragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SelectPathActivity extends AppCompatActivity implements PlaceFragment.OnListFragmentInteractionListener, PathFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_path);
    }

    @Override
    public void onListFragmentInteraction(PlaceContent.PlaceItem item) {
        PathFragment.PATH_NAME=item.id;
        Navigation.findNavController(getCurrentFocus()).navigate(R.id.action_place_to_path);
    }

    @Override
    public void onListFragmentInteraction(final PathContent.PathItem item) {
        MainActivity.mRef.child("Map").child(PathFragment.PATH_NAME).child(item.id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key=(String)dataSnapshot.getKey();
                if(key.toLowerCase().equals("path")){
                    List<HashMap<String,Double>> latLngs=(List<HashMap<String,Double>>)dataSnapshot.getValue();
                    for (HashMap<String, Double> latLng : latLngs) {
                        MainActivity.pathPointRepository.insert(
                                new PathPoint(
                                        new LatLng(latLng.get("latitude"),latLng.get("longitude")),
                                        true
                                )
                        );
                        System.out.println(latLng.get("latitude")+ " "+ latLng.get("longitude"));
                    }
//                    for (LatLng latLng : latLngs) {
//                        MainActivity.pathPointRepository.insert(new PathPoint(latLng,true));
//                    }
                    startActivity(new Intent(SelectPathActivity.this,MapActivity.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
}
