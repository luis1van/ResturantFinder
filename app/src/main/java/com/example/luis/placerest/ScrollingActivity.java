package com.example.luis.placerest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;


public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

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



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mAdapter = new RestaurantAdapter(createList(1));
        mRecyclerView.setAdapter(mAdapter);
    }

    private List createList(int size) {

        result = new ArrayList();
        for (int i=1; i <= size; i++) {
            Restaurant ci = new Restaurant();
            ci.setName("Luis");
            ci.setAddress("Some Where drive");
            result.add(ci);

        }

        return result;
    }
    @Override
    public void onClick(View view) {

        requestSomething();

        Snackbar.make(view, "Loading", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_go) {
            requestSomething();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateList(List lis){
        getList().addAll(lis);
        this.mAdapter.notifyDataSetChanged();
    }
    public List getList(){
        return this.result;
    }
    public void requestSomething(){
        String url="http://api.v3.factual.com/t/places?filters={\"$and\":[{\"country\":{\"$eq\":\"PR\"}}," +
                "{\"category_labels\":{\"$includes\":\"restaurant\"}}]}&KEY=Fe1VbH3aYNfVeODXyT2ecPcq7FBeeCIeGbEfFIk5&limit=20";
       /* String url = "https://rocky-shore-7054.herokuapp.com/api/top";*/
        GsonRequest gReq = new GsonRequest(url, Restaurant.class, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                updateList((List<Restaurant>)response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.networkResponse.data);
                    }
                });
        RequestSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(gReq);
    }
}
