package com.example.luis.placerest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;


public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener, LocationListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String url= "http://api.v3.factual.com/t/places?";
    private String Key ="&KEY=Fe1VbH3aYNfVeODXyT2ecPcq7FBeeCIeGbEfFIk5&limit=20";
    List result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ((SearchView) findViewById(R.id.searchView)).setOnQueryTextListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mAdapter = new RestaurantAdapter(createList());
        mRecyclerView.setAdapter(mAdapter);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
           updateList(savedInstanceState.getParcelableArrayList("restaurants"));
        }
    }

    private List createList() {
        result = new ArrayList();
        return result;
    }

    @Override
    public void onClick(View view) {

        Location loc = requestLocation();
        if(loc != null){

            requestSomething(url+"geo={\"$circle\":{\"$center\":["+loc.getLatitude()+","+loc.getLongitude()+"],\"$meters\":10000}}" +
                    "&filters={\"category_labels\":{\"$includes\":\"restaurant\"}}"+Key);
        }
        Snackbar.make(view, "Loading", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        requestSomething(url+"filters={\"$and\":[{\"country\":{\"$eq\":\"US\"}},{\"locality\":{\"$search\":\""+query+"\"}}," + "{\"category_labels\":{\"$includes\":\"restaurant\"}}]}"+Key);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/
/*        if (id == R.id.action_go) {
            requestSomething(url+"filters={\"$and\":[{\"country\":{\"$eq\":\"PR\"}}," + "{\"category_labels\":{\"$includes\":\"restaurant\"}}]}"+Key);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void updateList(List lis) {
        getList().addAll(lis);
        this.mAdapter.notifyDataSetChanged();
    }

    public List getList() {
        return this.result;
    }

    public void requestSomething(String URl) {
       /* String url = "https://rocky-shore-7054.herokuapp.com/api/top";*/
        @SuppressWarnings("unchecked")
        GsonRequest gReq = new GsonRequest(URl, Restaurant.class, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                //noinspection unchecked
                updateList((List<Restaurant>) response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.networkResponse);
                    }
                });
        RequestSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(gReq);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location requestLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAlert();
            return null;
        }
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        return locationManager.getLastKnownLocation(locationProvider);

    }
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app\n Try again by pressing the location button")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("restaurants", (ArrayList<? extends Parcelable>) result);
        super.onSaveInstanceState(outState);
    }
}
