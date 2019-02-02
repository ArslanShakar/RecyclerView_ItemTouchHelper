package com.practice.coding.asynctask_urldatafetching;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback.getDefaultUIUtil;

public class StudentsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeHelper.SwipeHelperListener {

    private ArrayList<String> arrayList = new ArrayList<>();
    private MyAdapter adapter;
    private RecyclerView rcv;
    private SwipeRefreshLayout swipeRefreshStudents;
    private String url;
    private ItemTouchHelper helper;
    private SwipeHelper callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        getSupportActionBar().setTitle("Students");

        url = "https://jaxat.com.pk/edu/sns/index.php?api/searchstudents";

        rcv = findViewById(R.id.rcvStudents);
        adapter = new MyAdapter(StudentsActivity.this, arrayList);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(StudentsActivity.this));
        //rcv.setItemAnimator(new DefaultItemAnimator());
        rcv.addItemDecoration(new DividerItemDecoration(StudentsActivity.this, DividerItemDecoration.VERTICAL));


        callback = new SwipeHelper(adapter, this);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rcv);

        swipeRefreshStudents = findViewById(R.id.swipe_refresh_students);
        swipeRefreshStudents.setOnRefreshListener(this);
        swipeRefreshStudents.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary));

        getAllStudents();
    }

    private void getAllStudents() {
        swipeRefreshStudents.setRefreshing(true);
        //token = 117a1ac8e8ade1502e193e6ea4206674
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    arrayList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String name = jsonObject.getString("name");
                        arrayList.add(name);
                    }

                    adapter.notifyDataSetChanged();

                    if (swipeRefreshStudents.isRefreshing()) {
                        swipeRefreshStudents.setRefreshing(false);
                    }

                    msg(arrayList.size() + "  Run On Thread : " + Thread.currentThread().getName());

                } catch (JSONException e) {
                    swipeRefreshStudents.setRefreshing(false);
                    msg(e.getMessage());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshStudents.setRefreshing(false);
                msg(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", "117a1ac8e8ade1502e193e6ea4206674");
                return params;
            }
        };

        queue.add(request);
    }

    private void msg(String mesg) {
        Toast.makeText(this, mesg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        getAllStudents();
    }


    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction, final int position) {

       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You want to Delete this?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.removeItem(position);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();*/


        adapter.removeItem(position);
    }

}
