package lk.sayuru.jungleapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import lk.sayuru.jungleapp.db.entity.Contact;
import lk.sayuru.jungleapp.db.entity.PathPoint;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback {
    private static final String TAG = "MainActivity";

    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    public static boolean IS_TO_VIEW_PATH=false;
    private boolean mLocationPermissionGranted=false;
    private GoogleMap mMap;
    Button btnSOS;


    Polyline polyline;
    Polyline polylineViewPath;
    int count;


    private boolean waitUntilFinish;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLocationPermission();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMarkerPoint();
            }
        });

        Button btnSave=findViewById(R.id.btnSavePath);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSaveClicked();

            }
        });
        btnSOS = findViewById(R.id.btnSOS);
        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
//                btnSOS.setEnabled(false);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LiveData<List<PathPoint>> allLive = MainActivity.pathPointRepository.getAllLive();
        allLive.observe(this, new Observer<List<PathPoint>>() {
            @Override
            public void onChanged(@Nullable List<PathPoint> pathPoints) {
                while (mMap==null){

                }
                if (polyline!=null)polyline.remove();
                if (polylineViewPath!=null)polylineViewPath.remove();
                PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);
                PolylineOptions optionsViewPath = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
                count = 0;
                final LatLngBounds.Builder[] builder=new LatLngBounds.Builder[1];
                builder[0]= new LatLngBounds.Builder();
                for (PathPoint pathPoint : pathPoints) {
                    if(pathPoint.isToView()){
                        optionsViewPath.add(pathPoint.getLatLng());

                    }else {
                        options.add(pathPoint.getLatLng());
                    }
                    builder[0].include(pathPoint.getLatLng());
                    count++;
                }
                polyline = mMap.addPolyline(options);
                polylineViewPath = mMap.addPolyline(optionsViewPath);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if(count>0){
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder[0].build(), 48));
                        }
                    }
                });

            }
        });
    }

    private void btnSaveClicked() {
        final String[] place = {"",""};
        LinearLayout layout = new LinearLayout(MapActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText placeEditText = new EditText(MapActivity.this);
        placeEditText.setHint("Place");
        final EditText pathNameEditText = new EditText(MapActivity.this);
        pathNameEditText.setHint("Road name");
        layout.addView(placeEditText);
        layout.addView(pathNameEditText);
        AlertDialog dialog = new AlertDialog.Builder(MapActivity.this)
                .setTitle("Add path to your name:")
                .setView(layout)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<LatLng> all = MainActivity.pathPointRepository.getAllPoints();
                        if(all.size()==0){
                            Toast.makeText(MapActivity.this, "There is no points add", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                        place[0] =(placeEditText.getText().toString());
                        place[1]=pathNameEditText.getText().toString();
                        if(place[0].equals("") || place[1].equals("")){
                            if (place[0].equals(""))placeEditText.setError("This field is required");
                            if (place[1].equals(""))pathNameEditText.setError("This field is required");
                            return;
                        }else {

                            String PlaceName = place[0].replace(" ", "");
                            String pathName = place[1].replace(" ", "");
                            MainActivity.mRef.child("Map")
                                    .child(PlaceName)
                                    .child(pathName)
                                    .child("Path").setValue(all);
                            MainActivity.mRef.child("Map")
                                    .child(PlaceName)
                                    .child(pathName)
                                    .child("Publisher").setValue(MainActivity.FIREBASE_USER.getUid());
                            MainActivity.pathPointRepository.deleteAllDataAddedPoints();
                            Toast.makeText(MapActivity.this, "Uploading successful", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void addMarkerPoint() {
        LatLng latLng = mMap.getCameraPosition().target;
        final PathPoint pathPoint = new PathPoint();
        pathPoint.setLatLng(latLng);
        MainActivity.pathPointRepository.insert(pathPoint);
    }
//    private void getDescriptionAndInsertData(final PathPoint pathPoint) {
//        final EditText taskEditText = new EditText(this);
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Add new point")
//                .setMessage("What are the significance of this point")
//                .setView(taskEditText)
//                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        pathPoint.setDescription(taskEditText.getText().toString());
//                        MainActivity.pathPointRepository.insert(pathPoint);
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .create();
//        dialog.show();
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            MainActivity.pathPointRepository.deleteAllViewPoints();
            if (polylineViewPath!=null)polylineViewPath.remove();
            count=0;
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(MapActivity.this, ContactActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MapActivity.this,SensorValues.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getDeviceLocation: Not Permitted for in the getDeviceLocation");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            mMap.setMyLocationEnabled(true);
//            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                @Override
//                public boolean onMyLocationButtonClick() {
//                    return false;
//                }
//            });
            mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    System.out.println(location.toString());
                }
            });


            LocationManager manager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = manager.getLastKnownLocation(String.valueOf(manager.getBestProvider(new Criteria(), true)));
            do{
                if (lastKnownLocation==null)continue;
                CameraUpdate center =
                        CameraUpdateFactory.newLatLng(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }while (lastKnownLocation ==null);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: Called");
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (permissions.length > 1) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                    Log.d(TAG, "onRequestPermissionsResult: Map is initialized");
                    getLocationPermission();
                } else {
                    // Permission was denied. Display an error message.
                    Log.d(TAG, "onRequestPermissionsResult: Permission Denied");
                    Toast.makeText(this, "Cannot load the map the permission was denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSMessage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
    }


    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void sendSMSMessage(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else {
            if (waitUntilFinish)return;
            waitUntilFinish=true;
            final SmsManager smsManager = SmsManager.getDefault();

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                List<Contact> contacts = MainActivity.contactRepository.getAll();
                                String msg="http://www.google.com/maps/place/"+location.getLatitude()+","+location.getLongitude();
                                for (Contact contact : contacts) {
                                    String[] split = contact.getPhoneNo().split(",");
                                    for (String s : split) {
                                        smsManager.sendTextMessage(s, null, msg, null, null);
                                    }
                                }
                            }
                        }
                    });

            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
            waitUntilFinish=false;
//            btnSOS.setEnabled(true);
        }
    }


}
