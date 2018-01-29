package kr.co.mikarusoft.seoul_happyplus_finder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    ArrayList<GPS> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);









    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //-----------------------------------

        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Double latitude = 0.;
        Double longitude = 0.;






        try{
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }else
            {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location != null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Toast.makeText(this.getApplicationContext(),latitude + " : " + longitude,Toast.LENGTH_LONG).show();
                }else
                {

                    Toast.makeText(this.getApplicationContext(),"위치정보없음",Toast.LENGTH_LONG).show();


                }

            }

        }catch(Exception e)
        {
            Toast.makeText(this.getApplicationContext(),"못가져옴",Toast.LENGTH_LONG).show();
        }

        list = new ArrayList<GPS>();
        list.add(new GPS("내 위치", latitude, longitude));

        //받아오기
        Intent getintent = getIntent();
        final String name = getintent.getExtras().getString("name");
        latitude = getintent.getExtras().getDouble("latitude");
        longitude = getintent.getExtras().getDouble("longitude");

        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        list.add(new GPS(name, latitude, longitude));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));

        Button go_to_web = (Button)findViewById(R.id.go_web);
        go_to_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        Button go_to_bbs = (Button)findViewById(R.id.go_bbs);
        go_to_bbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebBbsView.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}

class GPS{
    String name;
    double latitude;
    double longitude;
    GPS(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
