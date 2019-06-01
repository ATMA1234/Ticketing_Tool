package com.transvision.ticketing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.ticketing.extra.FileOpen;
import com.transvision.ticketing.extra.FunctionsCall;
import com.transvision.ticketing.extra.GetSetValues;
import com.transvision.ticketing.posting.FTPAPI;
import com.transvision.ticketing.posting.SendingData;

import java.io.File;
import java.util.ArrayList;

import static com.transvision.ticketing.extra.Constants.DOWNLOAD_FILE_ERROR;
import static com.transvision.ticketing.extra.Constants.DOWNLOAD_FILE_FAILURE;
import static com.transvision.ticketing.extra.Constants.DOWNLOAD_FILE_SUCCESS;
import static com.transvision.ticketing.extra.Constants.GETSET;
import static com.transvision.ticketing.extra.Constants.TICKET_UPDATE_VIEWFAILURE;
import static com.transvision.ticketing.extra.Constants.TICKET_UPDATE_VIEWSUCCESS;

public class ViewTicketDetails extends AppCompatActivity {
    private static final int DLG_DOWNLOAD_PREVIEW = 1;
    LinearLayout lin_comment;
    TextView tv_tic_no, tv_subdiv_code, tv_tic_narr, tv_tic_file, tv_tic_gen_by, tv_tic_gen_on, tv_tic_status, tv_tic_close,
            tv_priority, tv_severity, tv_assign_to, tv_hescom_tvd, tv_title, tv_description, file_download, tv_comment;
    LinearLayout tic_close_layout;
    Button btn_edit, btn_details;
    String ticket_no = "", ticket_narration = "", ticket_file = "", ticket_generated_by = "", ticket_generated_on = "",
            ticket_status = "", ticket_close = "", ticket_subdivision_code = "", ticket_mr_code = "", tic_comment = "",
            ticket_priority = "", ticket_severity = "", ticket_assign_to = "", ticket_hescom_tvd = "", ticket_title = "", ticket_description = "";
    Toolbar toolbar;
    GetSetValues getSetValues;
    ProgressDialog progressDialog;
    FunctionsCall functionsCall;
    SendingData sendingData;
    ArrayList<GetSetValues> arrayList1;
    FTPAPI ftpapi;
    //************************************************************************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_FILE_SUCCESS:
                    progressDialog.dismiss();
                    showdialog(DLG_DOWNLOAD_PREVIEW);
                    break;

                case DOWNLOAD_FILE_FAILURE:
                    FTPAPI.Download_file upload_file = ftpapi.new Download_file(ticket_file, handler);
                    upload_file.execute();
                    break;

                case DOWNLOAD_FILE_ERROR:
                    progressDialog.dismiss();
                    functionsCall.showtoast(ViewTicketDetails.this, "File is not downloading please check it...");
                    break;

                case TICKET_UPDATE_VIEWSUCCESS:
                    Intent intent = new Intent(ViewTicketDetails.this, ViewUpdateTickets.class);
                    intent.putExtra("list", arrayList1);
                    intent.putExtra(GETSET, getSetValues);//passing whole oject
                    startActivity(intent);
                    finish();
                    break;

                case TICKET_UPDATE_VIEWFAILURE:
                    Toast.makeText(ViewTicketDetails.this, "Ticket Not Found", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    //***************************************************************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket_details);

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

//*********************************Initialization************************************************************************
        lin_comment = findViewById(R.id.tic_cmnt);
        tic_close_layout = findViewById(R.id.tic_close_layout);
        tv_tic_no = findViewById(R.id.tic_no);
        tv_subdiv_code = findViewById(R.id.tic_subdiv_code);
        tv_tic_narr = findViewById(R.id.tic_narr);
        tv_tic_file = findViewById(R.id.tic_file);
        file_download = findViewById(R.id.download);
        tv_tic_gen_by = findViewById(R.id.tic_gen_by);
        tv_tic_gen_on = findViewById(R.id.tic_gen_on);
        tv_tic_status = findViewById(R.id.tic_status);
        tv_tic_close = findViewById(R.id.tic_close);//close date
        tv_priority = findViewById(R.id.tic_priority);
        tv_severity = findViewById(R.id.tic_severity);
        tv_assign_to = findViewById(R.id.tic_assign_to);
        tv_hescom_tvd = findViewById(R.id.tic_hescom_tvd);
        tv_title = findViewById(R.id.tic_title);
        tv_description = findViewById(R.id.tic_desc);
        tv_comment = findViewById(R.id.txt_tic_comm);//comment
        btn_edit = findViewById(R.id.btn_edit);
        btn_details = findViewById(R.id.btn_details);
        getSetValues = new GetSetValues();
        progressDialog = new ProgressDialog(this);
        functionsCall = new FunctionsCall();
        sendingData = new SendingData(this);
        arrayList1 = new ArrayList<>();
        ftpapi = new FTPAPI(this);

        //********************Getting Data***********************************************************************
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        ticket_no = extras.getString("TicketId");
        ticket_subdivision_code = extras.getString("TicketSubdivisionCode");
        ticket_narration = extras.getString("TicketNarration");
        ticket_file = extras.getString("TicketFile");
        ticket_generated_by = extras.getString("TicketGeneratedBy");
        ticket_generated_on = extras.getString("TicketGeneratedOn");
        ticket_status = extras.getString("TicketStatus");
        ticket_close = extras.getString("TicketClose");
        ticket_priority = extras.getString("Priority");
        ticket_severity = extras.getString("Severity");
        ticket_assign_to = extras.getString("Assign");
        ticket_hescom_tvd = extras.getString("Hescom_Tvd");
        ticket_title = extras.getString("TicketTitle");
        ticket_description = extras.getString("TicketDescription");
        ticket_mr_code = extras.getString("TicketMrCode");
        tic_comment = extras.getString("TicketComment");

        tv_tic_no.setText(ticket_no);
        tv_subdiv_code.setText(ticket_subdivision_code);
        tv_tic_file.setText(ticket_file);
        tv_tic_gen_by.setText(ticket_generated_by);
        tv_tic_gen_on.setText(ticket_generated_on);
        tv_tic_status.setText(ticket_status);
        tv_tic_close.setText(ticket_close); //close
        tv_priority.setText(ticket_priority);
        tv_severity.setText(ticket_severity);
        tv_assign_to.setText(ticket_assign_to);
        tv_hescom_tvd.setText(ticket_hescom_tvd);
        tv_title.setText(ticket_title);
        tv_description.setText(ticket_description);
        tv_tic_narr.setText(ticket_narration);
        if (!TextUtils.isEmpty(tic_comment)) {
            tv_comment.setText(tic_comment); //comment
        } else lin_comment.setVisibility(View.INVISIBLE);
        //****************************************************************************************************
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList1.clear();
                SendingData.View_Update_Tickets view_update_tickets = sendingData.new View_Update_Tickets(getSetValues, handler, arrayList1);
                view_update_tickets.execute(ticket_no);
            }
        });

//****************************************************************************************************
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTicketDetails.this, UpdateTicket.class);
                intent.putExtra("MyTicketId", ticket_no);
                Log.d("debug", "Ticket" + getSetValues.getTic_id());
                intent.putExtra("MyTicketSubdivisionCode", ticket_subdivision_code);
                intent.putExtra("MyTicketGeneratedOn", ticket_generated_on);
                intent.putExtra("MyTicketGeneratedBy", ticket_generated_by);
                intent.putExtra("MyTicketStatus", ticket_status);
                intent.putExtra("MyTicketNarration", ticket_narration);
                intent.putExtra("MyTicketFile", ticket_file);
                intent.putExtra("MyTicketClose", ticket_close);
                intent.putExtra("MyTicketPriority", ticket_priority);
                intent.putExtra("MyTicketSeverity", ticket_severity);
                intent.putExtra("MyTicketTitle", ticket_title);
                intent.putExtra("MyTicketDesc", ticket_description);
                intent.putExtra("MyTicketAssignTo", ticket_assign_to);
                intent.putExtra("MyTicketHescomTvd", ticket_hescom_tvd);
                intent.putExtra("MyTicketMrCode", ticket_mr_code);
                functionsCall.logStatus("MRCODE: " + ticket_mr_code);
                startActivity(intent);
            }
        });
//*************************************uploaded file**********************************************
        file_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ticket_file)) {
                    functionsCall.showprogressdialog("Downloading.....", "Downloading file please wait to download...", progressDialog);
                    FTPAPI.Download_file download_file = ftpapi.new Download_file(ticket_file, handler);
                    download_file.execute();
                } else {
                    file_download.setEnabled(false);
                }
            }
        });
    }

    //*******************************************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //*****************************************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                SendingData.View_Update_Tickets view_update_tickets = sendingData.new View_Update_Tickets(getSetValues, handler, arrayList1);
                view_update_tickets.execute(ticket_no);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //************************************download dialog************************************************
    private void showdialog(int id) {
        AlertDialog dialog;
        switch (id) {
            case DLG_DOWNLOAD_PREVIEW:
                AlertDialog.Builder download = new AlertDialog.Builder(ViewTicketDetails.this);
                download.setCancelable(false);
                download.setTitle("View file");
                download.setMessage("Do you want to view this file?");
                download.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File myFile = new File(functionsCall.filepath("Documents") + File.separator + ticket_file);
                        functionsCall.logStatus("Filepath: " + myFile.getAbsolutePath());
                        FileOpen.openFile(ViewTicketDetails.this, myFile);
                    }
                });
                download.setNeutralButton("NO", null);
                dialog = download.create();
                dialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
