package com.transvision.ticketing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.transvision.ticketing.adapter.TicketUpdate_Adapter;
import com.transvision.ticketing.extra.GetSetValues;
import java.util.ArrayList;
import static com.transvision.ticketing.extra.Constants.GETSET;

public class ViewUpdateTickets extends AppCompatActivity {
    GetSetValues getSetValues;
    RecyclerView tickets_view;
    ArrayList<GetSetValues> arrayList;
    TicketUpdate_Adapter tickets_adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_update_tickets);

        Intent intent = getIntent();
        getSetValues = (GetSetValues) intent.getSerializableExtra(GETSET);
        tickets_view = findViewById(R.id.tickets_view);
        arrayList = (ArrayList<GetSetValues>) intent.getSerializableExtra("list");
        tickets_adapter = new TicketUpdate_Adapter(arrayList, getSetValues, this);
        tickets_adapter.notifyDataSetChanged();
        tickets_view.setHasFixedSize(true);
        tickets_view.setLayoutManager(new LinearLayoutManager(this));
        tickets_view.setAdapter(tickets_adapter);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        TextView font_toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        font_toolbar_title.setText("Ticketing Tool");
        ImageView back_icon = findViewById(R.id.toolbar_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
    }
}

