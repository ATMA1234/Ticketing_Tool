package com.transvision.ticketing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ViewUpdateDeatils extends AppCompatActivity {
    private static final int DLG_DOWNLOAD_PREVIEW = 1;
    LinearLayout lin_comment;
    TextView tv_tic_no, tv_subdiv_code, tv_tic_narr, tv_tic_file, tv_tic_gen_by, tv_tic_gen_on, tv_tic_status, tv_tic_close,
            tv_priority, tv_severity, tv_assign_to, tv_hescom_tvd, tv_title, tv_description, tv_comment, down_file;
    LinearLayout tic_close_layout;
    Button btn_edit;
    String ticket_no = "", ticket_narration = "", ticket_file = "", ticket_generated_by = "", ticket_generated_on = "",
            ticket_status = "", ticket_close = "", ticket_subdivision_code = "", ticket_mr_code = "", tic_comment = "",
            ticket_priority = "", ticket_severity = "", ticket_assign_to = "", ticket_hescom_tvd = "", ticket_title = "",
            ticket_description = "";
    Toolbar toolbar;
    GetSetValues getSetValues;
    ProgressDialog progressDialog;
    FunctionsCall functionsCall;
    SendingData sendingData;
    ArrayList<GetSetValues> arrayList;

    //****************************************handler**********************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_FILE_SUCCESS:
                    progressDialog.dismiss();
                    showdialog(DLG_DOWNLOAD_PREVIEW);
                    break;

                case DOWNLOAD_FILE_FAILURE:
                    new FTPAPI().new Download_file(ticket_file, handler).execute();
                    break;

                case DOWNLOAD_FILE_ERROR:
                    progressDialog.dismiss();
                    functionsCall.showtoast(ViewUpdateDeatils.this,
                            "File is not downloading please check it...");
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
        setContentView(R.layout.activity_view_update_deatils);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView font_toolbar_title =  toolbar.findViewById(R.id.toolbar_title);
        font_toolbar_title.setText("Ticketing Tool");
        ImageView back_icon =  findViewById(R.id.toolbar_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

//*********************************Initialization*********************************************************************
        tic_close_layout =  findViewById(R.id.tic_close_layout);
        lin_comment = findViewById(R.id.tic_cmnt);
        tv_tic_no =  findViewById(R.id.tic_no);
        tv_subdiv_code =  findViewById(R.id.tic_subdiv_code);
        tv_tic_narr =  findViewById(R.id.tic_narr);
        tv_tic_file = findViewById(R.id.tic_file);
        down_file =  findViewById(R.id.download); //download file
        tv_tic_gen_by =  findViewById(R.id.tic_gen_by);
        tv_tic_gen_on =  findViewById(R.id.tic_gen_on);
        tv_tic_status =  findViewById(R.id.tic_status);
        tv_tic_close =  findViewById(R.id.tic_close);//close date
        tv_priority =  findViewById(R.id.tic_priority);
        tv_severity =  findViewById(R.id.tic_severity);
        tv_assign_to =  findViewById(R.id.tic_assign_to);
        tv_hescom_tvd =  findViewById(R.id.tic_hescom_tvd);
        tv_title =  findViewById(R.id.tic_title);
        tv_description = findViewById(R.id.tic_desc);
        tv_comment =  findViewById(R.id.txt_tic_comm);//comment
        btn_edit =  findViewById(R.id.btn_edit);
        getSetValues = new GetSetValues();
        progressDialog = new ProgressDialog(this);
        functionsCall = new FunctionsCall();
        sendingData = new SendingData();
        arrayList = new ArrayList<>();

//********************Getting Data***********************************************************************
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        ticket_no = extras.getString("TicketId");
        ticket_subdivision_code = extras.getString("TicketSubdivisionCode");
        ticket_narration = extras.getString("TicketNarration");
        ticket_file = extras.getString("TicketFile");            //download files
        ticket_generated_by = extras.getString("TicketGeneratedBy");
        ticket_generated_on = extras.getString("TicketGeneratedOn");
        ticket_status = extras.getString("TicketStatus");
        ticket_close = extras.getString("TicketClose"); //close date
        ticket_priority = extras.getString("Priority");
        ticket_severity = extras.getString("Severity");
        ticket_assign_to = extras.getString("Assign");
        ticket_hescom_tvd = extras.getString("Hescom_Tvd");
        ticket_title = extras.getString("TicketTitle");  //ticket title
        ticket_description = extras.getString("TicketDescription");
        ticket_mr_code = extras.getString("TicketMrCode");
        tic_comment = extras.getString("TicketComment");

        tv_tic_no.setText(ticket_no);
        tv_subdiv_code.setText(ticket_subdivision_code);
        tv_tic_file.setText(ticket_file);  //download file
        tv_tic_gen_by.setText(ticket_generated_by);
        tv_tic_gen_on.setText(ticket_generated_on);
        tv_tic_status.setText(ticket_status);
        tv_tic_close.setText(ticket_close);  //close date
        tv_priority.setText(ticket_priority);
        tv_severity.setText(ticket_severity);
        tv_assign_to.setText(ticket_assign_to);
        tv_hescom_tvd.setText(ticket_hescom_tvd);
        tv_title.setText(ticket_title);
        tv_description.setText(ticket_description);
        tv_tic_narr.setText(ticket_narration);

        if(!TextUtils.isEmpty(tic_comment)) {
            tv_comment.setText(tic_comment); //comment
        }
        else lin_comment.setVisibility(View.INVISIBLE);

//**************************************************download file********************************************************
        down_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ticket_file)) {
                    functionsCall.showprogressdialog("Downloading",
                            "Downloading file please wait to download...", progressDialog);
                    new FTPAPI().new Download_file(ticket_file, handler).execute();
                } else {
                    down_file.setEnabled(false);
                }
            }
        });
    }

    //************************************file download dialog*******************************************************
    public void showdialog(int id) {
        AlertDialog dialog;
        switch (id) {
            case DLG_DOWNLOAD_PREVIEW:
                AlertDialog.Builder download = new AlertDialog.Builder(ViewUpdateDeatils.this);
                download.setCancelable(false);
                download.setTitle("Download file View");
                download.setMessage("Do you want to view download file..??");
                download.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File myFile = new File(functionsCall.filepath("Documents") + File.separator + ticket_file);
                        functionsCall.logStatus("Filepath: " + myFile.getAbsolutePath());
                        FileOpen.openFile(ViewUpdateDeatils.this, myFile);
                    }
                });
                download.setNeutralButton("NO", null);
                dialog = download.create();
                dialog.show();
                break;
        }
    }

    //************************************************************************************************************************
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
