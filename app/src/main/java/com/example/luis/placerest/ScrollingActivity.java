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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.factual.driver.Circle;
import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
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

            /*requestSomething(url+"geo={\"$circle\":{\"$center\":["+loc.getLatitude()+","+loc.getLongitude()+"],\"$meters\":10000}}" +
                    "&filters={\"category_labels\":{\"$includes\":\"restaurant\"}}"+Key);*/
            Query q = new Query()
                    .within(new Circle(loc.getLatitude(), loc.getLongitude(), 5000))
                    .sortAsc("$distance").field("category_labels").includes("restaurant").limit(20);
            new QueryTask().execute(q);

        }
        Snackbar.make(view, "Loading", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Query q = new Query().field("locality").search(query).field("country").isEqual("PR").field("category_labels").includes("restaurant").limit(20);
        new QueryTask().execute(q);

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
       if (id == R.id.action_go) {

           Query q = new Query().field("name").search("Fried Chicken").field("country").isEqual("PR").limit(20);

           new QueryTask().execute(q);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateList(List lis) {
        getList().clear();
        getList().addAll(lis);
        this.mAdapter.notifyDataSetChanged();
    }

    public List getList() {
        return this.result;
    }

 /*   public void requestSomething(String URl) {

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
    }*/

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

    private class QueryTask extends AsyncTask<Query, Void, List> {
        @Override


        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected List doInBackground(Query... q) {

            Factual factual = FactualSingleton.getFactual();
            Gson gson = new Gson();
            String json = factual.fetch("places", q[0]).getJson();
            JsonElement elementR = (new JsonParser()).parse(json).getAsJsonObject();
            JsonArray arr = elementR.getAsJsonObject().get("response").getAsJsonObject().get("data").getAsJsonArray();

            List<Restaurant> list = Arrays.asList(gson.fromJson(arr, Restaurant[].class));

            return list;

        }

         /**The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(List s) {
            updateList(s);
        }


    }
}
