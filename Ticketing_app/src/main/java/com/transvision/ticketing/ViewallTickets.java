package com.transvision.ticketing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.transvision.ticketing.adapter.TicketsView_Adapter;
import com.transvision.ticketing.extra.GetSetValues;
import com.transvision.ticketing.posting.SendingData;
import java.util.ArrayList;
import static com.transvision.ticketing.extra.Constants.GETSET;
import static com.transvision.ticketing.extra.Constants.TICKETS_VIEWFAILURE;
import static com.transvision.ticketing.extra.Constants.TICKETS_VIEWSUCCESS;

public class ViewallTickets extends AppCompatActivity {
    GetSetValues getSetValues;
    RecyclerView tickets_view;
    ArrayList<GetSetValues> tickets_list;
    TicketsView_Adapter tickets_adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    SendingData sendingData;
    String user_role = "", user_id = "", user_password = "";

    //***************************************Handler****************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TICKETS_VIEWSUCCESS:
                    Toast.makeText(ViewallTickets.this, "Success", Toast.LENGTH_SHORT).show();
                    break;

                case TICKETS_VIEWFAILURE:
                    Toast.makeText(ViewallTickets.this, "Ticket Not Found", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });


    //******************************************************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewall_tickets);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        sendingData = new SendingData();
        Intent intent = getIntent();
        getSetValues = (GetSetValues) intent.getSerializableExtra(GETSET);
        user_id = getSetValues.getUserId();
        user_password = getSetValues.getPassword();
        user_role = getSetValues.getUser_role();
        tickets_view = findViewById(R.id.tickets_view);
        tickets_list = (ArrayList<GetSetValues>) intent.getSerializableExtra("list");
        tickets_adapter = new TicketsView_Adapter(tickets_list, getSetValues, this);
        tickets_adapter.notifyDataSetChanged();
        tickets_view.setHasFixedSize(true);
        tickets_view.setLayoutManager(new LinearLayoutManager(this));
        tickets_view.setAdapter(tickets_adapter);

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        // ********************init SwipeRefreshLayout and ListView********************************************
        swipeRefreshLayout = findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink, R.color.indigo, R.color.lime);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        //shuffleItems();
                    }
                }, 2000L);
            }
        });
    }

    //***********************************************************************************************************
    public void shuffleItems() {
        tickets_adapter = new TicketsView_Adapter(tickets_list, getSetValues, this);
        tickets_adapter.notifyDataSetChanged();
        tickets_view.setAdapter(tickets_adapter); // set the Adapter to RecyclerView
        SendingData.View_All_Tickets viewAllTickets = sendingData.new View_All_Tickets(getSetValues, handler, tickets_list);
        viewAllTickets.execute(user_id, user_password, user_role);
    }

    //*************search record in a list*********************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //*******************************************************************************************************
    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tickets_adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    //******************************delete ticket by swipe***********************************************************
    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(ViewallTickets.this, R.drawable.ic_clear_24dp);
                assert xMark != null;
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView tickets_view, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                TicketsView_Adapter tickets_adapter = (TicketsView_Adapter) tickets_view.getAdapter();
                tickets_adapter.remove(swipedPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }
                if (!initiated) {
                    init();
                }
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();
                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                xMark.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(tickets_view);
    }

    private void setUpAnimationDecoratorHelper() {
        tickets_view.addItemDecoration(new RecyclerView.ItemDecoration() {
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                if (!initiated) {
                    init();
                }
                if (parent.getItemAnimator().isRunning()) {
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;
                    int left = 0;
                    int right = parent.getWidth();
                    int top = 0;
                    int bottom = 0;
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }
                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }
                    background.setBounds(left, top, right, bottom);
                    background.draw(c);
                }
                super.onDraw(c, parent, state);
            }
        });
    }
}
//*******************************************************************************************************************