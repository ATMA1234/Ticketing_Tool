package com.transvision.ticketing;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.ticketing.adapter.RoleAdapter;
import com.transvision.ticketing.extra.FunctionsCall;
import com.transvision.ticketing.extra.GetSetValues;
import com.transvision.ticketing.posting.SendingData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.transvision.ticketing.extra.Constants.GETSET;
import static com.transvision.ticketing.extra.Constants.UPDATE_TICKET_ERROR;
import static com.transvision.ticketing.extra.Constants.UPDATE_TICKET_FAILURE;
import static com.transvision.ticketing.extra.Constants.UPDATE_TICKET_SUCCESS;
import static com.transvision.ticketing.extra.FunctionsCall.getPath;

public class UpdateTicket extends AppCompatActivity {
    private static final int RESULT_CANCELED = 3;
    private static final int DLG_TICKET_UPDATE = 4;
    private static final int FILE_MANAGER = 5;
    private final int CAMERA = 2, GALLERY = 1;
    static ProgressDialog progressDialog;
    FunctionsCall functionsCall;
    String regex = "!'~@#$%^&*:;<>.,/}";
    SendingData sendingData;

    private ImageView imageview;
    LinearLayout profile, hideshow;
    EditText et_title, et_description, et_narration, et_comments;
    TextView requestedby, requestedby_value, tvTicketId, tvMobileNo, tvZoneName, tvSubdivCode, tvSubdivName, txtRequestedBy;
    Button buttonpick, btn_upadte;
    Spinner priority, severity, assign_to, department, status;
    ArrayList<GetSetValues> priority_list, severity_list, assign_list, hescom_list, tvd_list, status_list;
    RoleAdapter priority_adapter, severity_adapter, assigned_adapter, hescom_adapter, tvd_adapter, status_adapter;
    GetSetValues getSetValues;
    String main_role = "";
    String ticTitle = "", ticdesc = "", tic_comment = "";
    String assign_status_role = "", assign_to_role = "", assign_priority = "", assign_severity = "", assign_hescom_tvd = "",
            filepathImage = "";
    String myTicketId = "", myTicketSubdivCode = "", myNarration = "", myPriority = "", mySeverity = "", myTicketStatus = "",
            myGeneratedBy = "", myMrCode = "", myTitle = "", myDescription = "", myAssignTo = "", myHescomTvd = "";
    String ImageDecode = "", newTitleText = "", newDescText = "", newNarration = "", imageNameOnly = "";
    String file_encode = "";
    //****************************************************************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TICKET_SUCCESS:
                    progressDialog.dismiss();
                    showdialog(DLG_TICKET_UPDATE);
                    break;

                case UPDATE_TICKET_FAILURE:
                    progressDialog.dismiss();
                    functionsCall.showtoast(UpdateTicket.this, "Ticket update is failed.. Check once again...");
                    break;

                case UPDATE_TICKET_ERROR:
                    progressDialog.dismiss();
                    Toast.makeText(UpdateTicket.this, "Server problem...Please check!!", Toast.LENGTH_SHORT).show();
                    break;

//                case FILE_UPLOADED:
//                    new SendingData().new TicketUpdate(handler, getSetValues).execute(myTicketId, newNarration, imageNameOnly,
//                            myGeneratedOn, myGeneratedBy, myTicketSubdivCode, assign_status_role, assign_priority,
//                            assign_severity, newTitleText, newDescText, assign_to_role, assign_hescom_tvd, myMrCode, tic_comment);
//                    break;
//                case FILE_UPLOADED_ERROR:
//                    progressDialog.dismiss();
//                    functionsCall.showtoast(UpdateTicket.this, "File upload error... Check once again...");
//                    break;
            }
            return false;
        }
    });

    //********************************************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_update_ticket);

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
        functionsCall = new FunctionsCall();
        sendingData = new SendingData(this);
        initialize();
//*******************************profile spinner*************************************************************
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestedby_value.setTextColor(Color.BLUE);
                if (hideshow.getVisibility() == View.VISIBLE) {
                    hideshow.setVisibility(View.GONE);
                    requestedby.setTextColor(Color.BLACK);
                } else {
                    hideshow.setVisibility(View.VISIBLE);
                    requestedby.setTextColor(Color.BLUE);
                }
            }
        });
//*********************************pick file********************************************************
        buttonpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }

    //*********************************initialize*********************************************************
    @SuppressLint("CutPasteId")
    private void initialize() {
        profile = findViewById(R.id.linear_profile);
        hideshow = findViewById(R.id.linear_hideshow);
        profile.setVisibility(View.GONE);
        imageview = findViewById(R.id.image);
        hideshow.setVisibility(View.GONE);
        buttonpick = findViewById(R.id.button_pick);
        btn_upadte = findViewById(R.id.btn_submit);
        et_title = findViewById(R.id.et_title);
        et_title.setFilters(new InputFilter[]{filter});
        et_comments = findViewById(R.id.et_comment); //comment
        et_comments.setFilters(new InputFilter[]{filter});
        et_description = findViewById(R.id.et_description);
        et_description.setFilters(new InputFilter[]{filter});
        et_narration = findViewById(R.id.et_narration);
        et_description.setFilters(new InputFilter[]{filter});
        tvTicketId = findViewById(R.id.tv_ticket_id);
        tvMobileNo = findViewById(R.id.tv_mobile);
        tvZoneName = findViewById(R.id.tv_zone);
        tvSubdivCode = findViewById(R.id.tv_subdiv_code);
        tvSubdivName = findViewById(R.id.tv_subdiv_name);
        txtRequestedBy = findViewById(R.id.txt_requested_by_values);
        requestedby = findViewById(R.id.txt_requested_by);
        requestedby_value = findViewById(R.id.txt_requested_by_values);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            myGeneratedBy = getBundle(bundle, "MyTicketGeneratedBy");
            myTicketId = getBundle(bundle, "MyTicketId");
            myTicketSubdivCode = getBundle(bundle, "MyTicketSubdivisionCode");
            myNarration = getBundle(bundle, "MyTicketNarration");
            myPriority = getBundle(bundle, "MyTicketPriority");
            mySeverity = getBundle(bundle, "MyTicketSeverity");
            myAssignTo = getBundle(bundle, "MyTicketAssignTo");
            myHescomTvd = getBundle(bundle, "MyTicketHescomTvd");
            myTicketStatus = getBundle(bundle, "MyTicketStatus");
            myTitle = getBundle(bundle, "MyTicketTitle");
            myDescription = getBundle(bundle, "MyTicketDesc");
            myMrCode = getBundle(bundle, "MyTicketMrCode");
        }
        et_narration.setText(myNarration);
        et_title.setText(myTitle);
        et_title.setEnabled(false);
        et_description.setText(myDescription);
        txtRequestedBy.setText(myGeneratedBy);
        tvTicketId.setText(myTicketId);
        ticTitle = et_title.getText().toString();
        ticdesc = et_description.getText().toString();
        progressDialog = new ProgressDialog(this);
        priority = findViewById(R.id.spinner_priority);
        severity = findViewById(R.id.spinner_severity);
        assign_to = findViewById(R.id.spinner_assignto);
        department = findViewById(R.id.spinner_hescomtvd);
        status = findViewById(R.id.spinner_status);
        getSetValues = (GetSetValues) intent.getSerializableExtra(GETSET);

        //** ****************Getting Value of Generated Ticket for update *********************************/
        priority_list = new ArrayList<>();
        priority_adapter = new RoleAdapter(priority_list, getApplicationContext());
        priority.setAdapter(priority_adapter);
        severity_list = new ArrayList<>();
        severity_adapter = new RoleAdapter(severity_list, getApplicationContext());
        severity.setAdapter(severity_adapter);
        assign_list = new ArrayList<>();
        assigned_adapter = new RoleAdapter(assign_list, getApplicationContext());
        assign_to.setAdapter(assigned_adapter);
        status_list = new ArrayList<>();
        status_adapter = new RoleAdapter(status_list, getApplicationContext());
        status.setAdapter(status_adapter);

        //******************hescom/Tvd hescom List ******************/
        hescom_list = new ArrayList<>();
        hescom_adapter = new RoleAdapter(hescom_list, getApplicationContext());
        department.setAdapter(hescom_adapter);

        //****************** Hescom/Tvd tvd List ******************/
        tvd_list = new ArrayList<>();
        tvd_adapter = new RoleAdapter(tvd_list, getApplicationContext());
        department.setAdapter(tvd_adapter);

        //****************** priority value insertion in server ******************/
        for (int i = 0; i < getResources().getStringArray(R.array.priority).length; i++) {
            GetSetValues getSetValues = new GetSetValues();
            String value = getResources().getStringArray(R.array.priority)[i];
            getSetValues.setLogin_role(value);
            priority_list.add(getSetValues);
            priority_adapter.notifyDataSetChanged();
        }
        for (int i = 0; i < priority_list.size(); i++) {
            GetSetValues details = priority_list.get(i);
            String value = myPriority;
            String value1 = details.getLogin_role();
            if (value.equals(value1)) {
                priority.setSelection(i);
                break;
            }
        }
        //****************Setting Severity_to spinner****************
        for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
            GetSetValues getSetValues = new GetSetValues();
            String value = getResources().getStringArray(R.array.severity)[i];
            getSetValues.setLogin_role(value);
            severity_list.add(getSetValues);
            severity_adapter.notifyDataSetChanged();
        }
        for (int i = 0; i < severity_list.size(); i++) {
            GetSetValues details = severity_list.get(i);
            String value = mySeverity;
            String value1 = details.getLogin_role();
            if (value.equals(value1)) {
                severity.setSelection(i);
                break;
            }
        }

        //****************Setting assign_to spinner****************
        for (int i = 0; i < getResources().getStringArray(R.array.assign_to).length; i++) {
            GetSetValues getSetValues = new GetSetValues();
            String value = getResources().getStringArray(R.array.assign_to)[i];
            getSetValues.setLogin_role(value);
            assign_list.add(getSetValues);
            assigned_adapter.notifyDataSetChanged();
        }
        for (int i = 0; i < assign_list.size(); i++) {
            GetSetValues details = assign_list.get(i);
            String value = myAssignTo;
            String value1 = details.getLogin_role();
            if (value.equals(value1)) {
                assign_to.setSelection(i);
                break;
            }
        }

        //****************Select department****************
        for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
            GetSetValues getSetValues = new GetSetValues();
            String value = getResources().getStringArray(R.array.department)[i];
            getSetValues.setLogin_role(value);
            hescom_list.add(getSetValues);
            hescom_adapter.notifyDataSetChanged();
        }
        //****************Setting status spinner****************
        for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
            GetSetValues getSetValues = new GetSetValues();
            String value = getResources().getStringArray(R.array.status)[i];
            getSetValues.setLogin_role(value);
            status_list.add(getSetValues);
            status_adapter.notifyDataSetChanged();
        }

        for (int i = 0; i < status_list.size(); i++) {
            GetSetValues details = status_list.get(i);
            String value = myTicketStatus;
            String value1 = details.getLogin_role();
            if (value.equals(value1)) {
                status.setSelection(i);
                break;
            }
        }

        //** ****************priority value insertion in server ******************/
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_priority = assign_role.getText().toString();
                if (!assign_priority.equals("--SELECT--")) {
                    main_role = assign_priority;
                }

                if (assign_priority.equalsIgnoreCase("P1-High")) {
                    priority_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.priority).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.priority)[i]);
                        priority_list.add(getSetValues);
                        priority_adapter.notifyDataSetChanged();
                    }
                } else if (assign_priority.equalsIgnoreCase("P2-Medium")) {
                    priority_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.priority).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.priority)[i]);
                        priority_list.add(getSetValues);
                        priority_adapter.notifyDataSetChanged();
                    }
                } else if (assign_priority.equalsIgnoreCase("P3-Low")) {
                    priority_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.priority).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.priority)[i]);
                        priority_list.add(getSetValues);
                        priority_adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //******************** severity value insertion in server ********************/
        severity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_severity = assign_role.getText().toString();
                if (!assign_severity.equals("--SELECT--")) {
                    main_role = assign_severity;
                }
                if (assign_severity.equalsIgnoreCase("S1-Critical")) {
                    severity_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
                        severity_list.add(getSetValues);
                        severity_adapter.notifyDataSetChanged();
                    }
                } else if (assign_severity.equalsIgnoreCase("S2-Major")) {
                    severity_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
                        severity_list.add(getSetValues);
                        severity_adapter.notifyDataSetChanged();
                    }
                } else if (assign_severity.equalsIgnoreCase("S3-Moderate")) {
                    severity_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
                        severity_list.add(getSetValues);
                        severity_adapter.notifyDataSetChanged();
                    }
                } else if (assign_severity.equalsIgnoreCase("S4-Low")) {
                    severity_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
                        severity_list.add(getSetValues);
                        severity_adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //** ********************assign hescom/tvd selection role **************************************/
        assign_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_to_role = assign_role.getText().toString();
                if (!assign_to_role.equals("--SELECT--")) {
                    main_role = assign_to_role;
                }
                if (assign_to_role.equalsIgnoreCase("HESCOM")) {
                    tvd_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        tvd_list.add(getSetValues);
                        tvd_adapter.notifyDataSetChanged();
                    }
                    for (int i = 0; i < tvd_list.size(); i++) {
                        GetSetValues details = tvd_list.get(i);
                        String value = myHescomTvd;
                        Log.d("debug", "hescom_tvd1" + "" + value);
                        String value1 = details.getLogin_role();
                        Log.d("debug", "hescom_tvd2" + "" + value1);
                        if (value.equals(value1)) {
                            department.setSelection(i);
                            break;
                        }
                    }
                } else if (assign_to_role.equalsIgnoreCase("TVD")) {
                    tvd_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd)[i]);
                        tvd_list.add(getSetValues);
                        tvd_adapter.notifyDataSetChanged();
                    }
                    for (int i = 0; i < tvd_list.size(); i++) {
                        GetSetValues details = tvd_list.get(i);
                        String value = myHescomTvd;
                        Log.d("debug", "hescom_tvd1" + "" + value);
                        String value1 = details.getLogin_role();
                        Log.d("debug", "hescom_tvd2" + "" + value1);
                        if (value.equals(value1)) {
                            department.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //************************************** department slection value insertion in server **************************************/
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_hescom_tvd = assign_role.getText().toString();
                if (!assign_hescom_tvd.equals("--SELECT--")) {
                    main_role = assign_hescom_tvd;
                }
                if (assign_hescom_tvd.equalsIgnoreCase("EEIT")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("AAO")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("AEE")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("SEIT")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("TL1")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("TL2")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("CORPAO")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.department).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.department)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("CSD2")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("SOFTWARE")) {
                    hescom_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("HARDWARE")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("MANAGEMENT")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                } else if (assign_hescom_tvd.equalsIgnoreCase("HR")) {
                    hescom_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //************************************ Status Selection Click Listener *********************************************************
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_status_role = assign_role.getText().toString();
//                if (!assign_role.equals("--SELECT--")) {
//                    main_role = assign_status_role;
//                }
                if (assign_status_role.equalsIgnoreCase("NEW")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                } else if (assign_status_role.equalsIgnoreCase("Assigned")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                } else if (assign_status_role.equalsIgnoreCase("Pending")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                } else if (assign_status_role.equalsIgnoreCase("Resolved")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                } else if (assign_status_role.equalsIgnoreCase("Reopened")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                } else if (assign_status_role.equalsIgnoreCase("Closed")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                } else if (assign_status_role.equalsIgnoreCase("Invalid")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                } else if (assign_status_role.equalsIgnoreCase("Not Applicable")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //*************************************************Update Ticket***********************************************************************
        btn_upadte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assign_priority.equals("--SELECT--")) {
                    if (!assign_severity.equals("--SELECT--")) {
                        if (!assign_to_role.equals("--SELECT--")) {
                            if (!assign_hescom_tvd.equals("--SELECT--")) {
                                if (!assign_status_role.equals("--SELECT--")) {
                                    newTitleText = et_title.getText().toString();//1
                                    newDescText = et_description.getText().toString();//2
                                    newNarration = et_narration.getText().toString();//3
                                    tic_comment = et_comments.getText().toString();//4
                                    if (!TextUtils.isEmpty(tic_comment)) {
                                        functionsCall.showprogressdialog(getResources().getString(R.string.update), getResources().getString(R.string.update_message), progressDialog);
                                        if (!TextUtils.isEmpty(filepathImage)) {
                                            File file = new File(filepathImage);
                                            imageNameOnly = file.getName();
                                            file_encode = functionsCall.encoded(filepathImage);
                                        }
                                        SendingData.TicketUpdate ticketUpdate = sendingData.new TicketUpdate(handler, getSetValues);
                                        ticketUpdate.execute(myTicketId, newNarration, imageNameOnly, myGeneratedBy, myTicketSubdivCode,
                                                assign_status_role, assign_priority, assign_severity, newTitleText, newDescText,
                                                assign_to_role, assign_hescom_tvd, myMrCode, tic_comment, file_encode);
                                    } else
                                        Toast.makeText(UpdateTicket.this, "Enter Comment", Toast.LENGTH_SHORT).show();
                                } else
                                    functionsCall.showtoast(UpdateTicket.this, "Please Select Status");
                            } else
                                functionsCall.showtoast(UpdateTicket.this, "Please Select Department");
                        } else
                            functionsCall.showtoast(UpdateTicket.this, "Please Select Assign To");
                    } else functionsCall.showtoast(UpdateTicket.this, "Please Select Severity");
                } else functionsCall.showtoast(UpdateTicket.this, "Please Select Priority");
            }
        });
    }

    private String getBundle(Bundle bundle, String value) {
        return bundle.getString(value);
    }

    //************************************************************************************************************************************
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && regex.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    //*************************************************Update Dialog*******************************************************************************
    private void showdialog(int id) {
        switch (id) {
            case DLG_TICKET_UPDATE:
                AlertDialog.Builder ticket_id = new AlertDialog.Builder(this);
                ticket_id.setTitle("Update Result");
                ticket_id.setCancelable(false);
                ticket_id.setMessage("Your Ticket ID " + myTicketId + " is Updated successfully.");
                ticket_id.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        email();
                    }
                });
                AlertDialog dialog = ticket_id.create();
                dialog.show();
                break;
        }
    }

    //*************************************************send email*************************************************************************
    public void email() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"notification@hescomtrm.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Ticket ID " + myTicketId + " is Updated successfully.");
        email.putExtra(Intent.EXTRA_TEXT, "Dear user, \n\n Title : " + myTitle + "\n" + "Comment : " + tic_comment + "\n" + "File : " + imageNameOnly);
        email.setType("plain/text");
        try {
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(UpdateTicket.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    // ****************************Select image from camera or gallery**********************************************************
    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Option");
        String[] pictureDialogItems = {"Gallery", "Camera", "File Manager", "Cancel"};
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choosePhotoFromGallary();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                    case 2:
                        takeFromFileManager();
                        break;
                    case 3:
                        cancel_file();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void takeFromFileManager() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_MANAGER);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        try {
            if (requestCode == GALLERY) {
                if (data != null) {
                    Uri URI = data.getData();
                    String[] FILE = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getApplicationContext().getContentResolver().query(Objects.requireNonNull(URI), FILE, null, null, null);
                    Objects.requireNonNull(cursor).moveToFirst();
                    int columnIndex = cursor.getColumnIndex(FILE[0]);
                    ImageDecode = cursor.getString(columnIndex);
                    cursor.close();
                    filepathImage = getPath(getApplicationContext(), URI);
                    Toast.makeText(getApplicationContext(), "Uplaoded File is" + filepathImage, Toast.LENGTH_SHORT).show();
                    buttonpick.setText(filepathImage);

                }
            } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                imageview.setImageBitmap(photo);
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                filepathImage = (getRealPathFromURI(tempUri));
                Toast.makeText(getApplicationContext(), "Uplaoded File is" + filepathImage, Toast.LENGTH_SHORT).show();
                buttonpick.setText(filepathImage);

            } else if (requestCode == FILE_MANAGER) {
                if (data != null) {
                    Uri URI = data.getData();
                    String[] FILE = {MediaStore.Images.Media.DATA};
                    ContentResolver cr = getApplicationContext().getContentResolver();
                    Cursor cursor = cr.query(Objects.requireNonNull(URI), FILE, null, null, null);
                    Objects.requireNonNull(cursor).moveToFirst();
                    int columnIndex = cursor.getColumnIndex(FILE[0]);
                    ImageDecode = cursor.getString(columnIndex);
                    cursor.close();
                    filepathImage = getPath(getApplicationContext(), URI);
                    Toast.makeText(getApplicationContext(), "Uplaoded File is" + filepathImage, Toast.LENGTH_SHORT).show();
                    buttonpick.setText(filepathImage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File not attached, try again...", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel_file() {
        buttonpick.setText("");

    }

    //******************************get ImageUri & compress****************************************************************************
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title",
                null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        @SuppressLint("Recycle") Cursor cursor = getApplicationContext().getContentResolver().query(uri,
                null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
//*************************************************************************************************************************



