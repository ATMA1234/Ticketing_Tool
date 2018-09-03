package com.transvision.ticketing.fragment;

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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.ticketing.MainActivity;
import com.transvision.ticketing.R;
import com.transvision.ticketing.adapter.RoleAdapter;
import com.transvision.ticketing.extra.FunctionsCall;
import com.transvision.ticketing.extra.GetSetValues;
import com.transvision.ticketing.posting.FTPAPI;
import com.transvision.ticketing.posting.SendingData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.transvision.ticketing.MainActivity.Steps.FORM0;
import static com.transvision.ticketing.extra.Constants.FILE_UPLOADED;
import static com.transvision.ticketing.extra.Constants.FILE_UPLOADED_ERROR;
import static com.transvision.ticketing.extra.Constants.GENERATE_TICKET_FAILURE;
import static com.transvision.ticketing.extra.Constants.GENERATE_TICKET_SUCCESS;
import static com.transvision.ticketing.extra.FunctionsCall.getPath;

public class AdminTicketing extends Fragment {
    private static final int RESULT_CANCELED = 3;
    private static final int DLG_TICKET_ID = 4;
    private static final int FILE_MANAGER = 5;
    private final int CAMERA = 2, GALLERY = 1;
    private ImageView imageview;
    FTPAPI ftpapi;
    String regex = "!'~@#$%^&*:;<>.,/}";
    View view;
    LinearLayout profile, hideshow;
    EditText et_title, et_description, et_narration;
    TextView requestedby, requestedby_value, tvTicketId, tvMobileNo, tvZoneName, tvSubdivCode, tvSubdivName, txtRequestedBy;
    Button btn_submit;
    Button buttonpick;
    ImageView ivUploadedImage;
    Spinner priority, severity, assign_to, department, status;
    ArrayList<GetSetValues> priority_list, severity_list, assign_list, hescom_list, tvd_list, status_list;
    RoleAdapter priority_adapter, severity_adapter, assigned_adapter, hescom_adapter, tvd_adapter, status_adapter;
    GetSetValues getSetValues, getSet;
    FunctionsCall functionsCall;
    String main_role = "";
    Intent intent;
    String Ticket_Id = "";
    String ticGenOn = "", ticTitle = "", ticdesc = "";
    String titleText = "", csd_hescom = "", descText = "", narration = "", imageNameOnly = "";
    String assign_status_role = "", assign_to_role = "", assign_priority = "", assign_severity = "", assign_hescom_tvd = "",
            filepathImage = "", mr_code = "", generated_by;
    String ImageDecode = "";
    ProgressDialog progressDialog;
    //*******************************************************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
//                case FILE_UPLOADED:
//                    SendingData.GenerateTicket generateTicket = new SendingData().new GenerateTicket(handler, getSet);
//                    generateTicket.execute(narration, imageNameOnly, ticGenOn, generated_by,
//                            ((MainActivity) Objects.requireNonNull(getActivity())).getSubdiv_code(), assign_status_role,
//                            assign_priority, titleText,
//                            descText, assign_severity, assign_to_role, assign_hescom_tvd, mr_code, csd_hescom);
//                    break;
//
//                case FILE_UPLOADED_ERROR:
//                    progressDialog.dismiss();
//                    Toast.makeText(getActivity(), "File not uploaded to server....", Toast.LENGTH_SHORT).show();
//                    break;

                case GENERATE_TICKET_SUCCESS:
                    progressDialog.dismiss();
                    showdialog(DLG_TICKET_ID);
                    break;

                case GENERATE_TICKET_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Ticket not generated....", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    public AdminTicketing() {
    }

    //*******************************************************************************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_ticketing, container, false);
        initialize();
//***********************************************************************************************************
        priority.setSelection(0);
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
        //**************************Setting priority spinner*********************************************
        for (int i = 0; i < getResources().getStringArray(R.array.priority).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.priority)[i]);
            priority_list.add(getSetValues);
            priority_adapter.notifyDataSetChanged();
        }
        //******************************Setting Severity_to spinner************************************************
        for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
            severity_list.add(getSetValues);
            severity_adapter.notifyDataSetChanged();
        }

        //*******************************Setting assign_to spinner***********************************************
        for (int i = 0; i < getResources().getStringArray(R.array.assign_to).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to)[i]);
            assign_list.add(getSetValues);
            assigned_adapter.notifyDataSetChanged();
        }
        //***********************************************Select Hescom/Tvd Assign***********************************************
        for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd_select).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd_select)[i]);
            tvd_list.add(getSetValues);
            tvd_adapter.notifyDataSetChanged();
        }
        //***********************************************Setting status spinner***********************************************
        for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
            status_list.add(getSetValues);
            status_adapter.notifyDataSetChanged();
        }
//*********************************************pick file************************************************************************
        buttonpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        return view;
    }

    //**********************************initialize***********************************************************************************
    @SuppressLint("CutPasteId")
    private void initialize() {
        profile = view.findViewById(R.id.linear_profile);
        hideshow = view.findViewById(R.id.linear_hideshow);
        profile.setVisibility(View.GONE);
        hideshow.setVisibility(View.GONE);
        buttonpick = view.findViewById(R.id.button_pick);
        imageview = view.findViewById(R.id.image);
        btn_submit = view.findViewById(R.id.btn_submit);
        et_title = view.findViewById(R.id.et_title);
        et_title.setFilters(new InputFilter[] { filter });
        et_description = view.findViewById(R.id.et_description);
        et_description.setFilters(new InputFilter[] { filter });
        et_narration = view.findViewById(R.id.et_narration);
        et_narration.setFilters(new InputFilter[] { filter });
        tvTicketId = view.findViewById(R.id.tv_ticket_id);
        tvMobileNo = view.findViewById(R.id.tv_mobile);
        tvZoneName = view.findViewById(R.id.tv_zone);
        tvSubdivCode = view.findViewById(R.id.tv_subdiv_code);
        tvSubdivName = view.findViewById(R.id.tv_subdiv_name);
        txtRequestedBy = view.findViewById(R.id.txt_requested_by_values);
        ivUploadedImage = view.findViewById(R.id.iv_upload_image);
        ticTitle = et_title.getText().toString();
        getSetValues = new GetSetValues();

        Log.d("debug", "TicketingTitle" + ticTitle);
        ticdesc = et_description.getText().toString();
        Log.d("debug", "TicketingDesc" + ticdesc);
        progressDialog = new ProgressDialog(getActivity());
        priority = view.findViewById(R.id.spinner_priority);
        assign_to = view.findViewById(R.id.spinner_assignto);
        department = view.findViewById(R.id.spinner_hescomtvd);
        status = view.findViewById(R.id.spinner_status);
        severity = view.findViewById(R.id.spinner_severity);
        getSet = ((MainActivity) Objects.requireNonNull(getActivity())).getSetValues();
        intent = ((MainActivity) getActivity()).getintent();
        mr_code = Objects.requireNonNull(intent.getExtras()).getString("UserId");
        generated_by = intent.getExtras().getString("UserId");
        Log.d("debug", "mr_code" + mr_code);
        Log.d("debug", "UserRole:" + intent.getExtras().getString("UserId"));
        requestedby = view.findViewById(R.id.txt_requested_by);
        requestedby_value = view.findViewById(R.id.txt_requested_by_values);
        priority_list = new ArrayList<>();
        priority_adapter = new RoleAdapter(priority_list, getActivity());
        priority.setAdapter(priority_adapter);
        severity_list = new ArrayList<>();
        severity_adapter = new RoleAdapter(severity_list, getActivity());
        severity.setAdapter(severity_adapter);
        assign_list = new ArrayList<>();
        assigned_adapter = new RoleAdapter(assign_list, getActivity());
        assign_to.setAdapter(assigned_adapter);
        status_list = new ArrayList<>();
        status_adapter = new RoleAdapter(status_list, getActivity());
        status.setAdapter(status_adapter);
        hescom_list = new ArrayList<>();
        hescom_adapter = new RoleAdapter(hescom_list, getActivity());
        department.setAdapter(hescom_adapter);
        tvd_list = new ArrayList<>();
        tvd_adapter = new RoleAdapter(tvd_list, getActivity());
        department.setAdapter(tvd_adapter);
        functionsCall = new FunctionsCall();
        ftpapi = new FTPAPI();

        //*********************************Status value insertion in server ***********************************/
        status.setSelection(0);
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
                    // Toast.makeText(getActivity(), "NEW Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_status_role.equalsIgnoreCase("Assigned")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Assigned Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_status_role.equalsIgnoreCase("Pending")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Pending Selected", Toast.LENGTH_SHORT).show();

                } else if (assign_status_role.equalsIgnoreCase("Resolved")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Resolved Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_status_role.equalsIgnoreCase("Reopened")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Reopened Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_status_role.equalsIgnoreCase("Closed")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Closed Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_status_role.equalsIgnoreCase("Invalid")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Invalid Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_status_role.equalsIgnoreCase("Not Applicable")) {
                    status_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.status).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.status)[i]);
                        status_list.add(getSetValues);
                        status_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "Not Applicable Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //** ***********************priority value insertion in server *******************************************/
        priority.setSelection(0);
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_priority = assign_role.getText().toString();
                if (!assign_role.equals("--SELECT--")) {
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
                    Toast.makeText(getActivity(), "P1 High Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_priority.equalsIgnoreCase("P2-Medium")) {
                    priority_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.priority).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.priority)[i]);
                        priority_list.add(getSetValues);
                        priority_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "P2 Medium Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_priority.equalsIgnoreCase("P3-Low")) {
                    priority_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.priority).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.priority)[i]);
                        priority_list.add(getSetValues);
                        priority_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "P3 Low Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //** ****************************severity value insertion in server ****************************************/
        severity.setSelection(0);
        severity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_severity = assign_role.getText().toString();
                if (!assign_role.equals("--SELECT--")) {
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
                    Toast.makeText(getActivity(), "S1 Critical Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_severity.equalsIgnoreCase("S2-Major")) {
                    severity_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
                        severity_list.add(getSetValues);
                        severity_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "S2 Major Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_severity.equalsIgnoreCase("S3-Moderate")) {
                    severity_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
                        severity_list.add(getSetValues);
                        severity_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "S3 Moderate Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_severity.equalsIgnoreCase("S4-Low")) {
                    severity_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.severity).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.severity)[i]);
                        severity_list.add(getSetValues);
                        severity_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "S4 Low Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //** *******************************department slection value insertion in server **********************/
        department.setSelection(0);
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_hescom_tvd = assign_role.getText().toString();
                if (!assign_role.equals("--SELECT--")) {
                    main_role = assign_hescom_tvd;
                }
                if (assign_hescom_tvd.equalsIgnoreCase("EEIT")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.corporate_level).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.corporate_level)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "EEIT Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_hescom_tvd.equalsIgnoreCase("CORP_AEE")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.corporate_level).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.corporate_level)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "CORP_AEE Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_hescom_tvd.equalsIgnoreCase("CORP_TL1")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.corporate_level).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.corporate_level)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "CORP_TL1 Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_hescom_tvd.equalsIgnoreCase("CORP_TL2")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.corporate_level).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.corporate_level)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "CORP_TL2 Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_hescom_tvd.equalsIgnoreCase("CORP_AO")) {
                    hescom_list.clear();

                    for (int i = 0; i < getResources().getStringArray(R.array.corporate_level).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.corporate_level)[i]);
                        hescom_list.add(getSetValues);
                        hescom_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "CORP_AO Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //*************************************assign hescom/tvd selection role ***********************************/
        assign_to.setSelection(0);
        assign_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView assign_role = view.findViewById(R.id.spinner_txt);
                assign_to_role = assign_role.getText().toString();
                if (!assign_role.equals("--SELECT--")) {
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
                    Toast.makeText(getActivity(), "Hescom Selected", Toast.LENGTH_SHORT).show();
                } else if (assign_to_role.equalsIgnoreCase("TVD")) {
                    tvd_list.clear();
                    for (int i = 0; i < getResources().getStringArray(R.array.assign_to_tvd).length; i++) {
                        getSetValues = new GetSetValues();
                        getSetValues.setLogin_role(getResources().getStringArray(R.array.assign_to_tvd)[i]);
                        tvd_list.add(getSetValues);
                        tvd_adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), "TVD Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//*******************************************submit button****************************************************************
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assign_priority.equals("--SELECT--")) {
                    if (!assign_severity.equals("--SELECT--")) {
                        if (!assign_to_role.equals("--SELECT--")) {
                            if (!assign_hescom_tvd.equals("--SELECT--")) {
                                if (!assign_status_role.equals("--SELECT--")) {

                                    titleText = et_title.getText().toString();//1
                                    if (!TextUtils.isEmpty(titleText)) {
                                            descText = et_description.getText().toString();//2
                                            if (!TextUtils.isEmpty(descText)) {
                                                    narration = et_narration.getText().toString();//3
                                                    if (!TextUtils.isEmpty(narration)) {
                                                            csd_hescom = generated_by + assign_hescom_tvd;
                                                            functionsCall.showprogressdialog(getResources().getString(R.string.ticketing),
                                                                    getResources().getString(R.string.ticketing_message), progressDialog);
                                                            String file_encode = "";
                                                            if (!TextUtils.isEmpty(filepathImage)) {
                                                                File file = new File(filepathImage);
                                                                imageNameOnly = file.getName();
                                                                file_encode = functionsCall.encoded(filepathImage);
                                                            }
                                                            SendingData.GenerateTicket generateTicket = new SendingData().new GenerateTicket(handler, getSetValues);
                                                            generateTicket.execute(narration, imageNameOnly, generated_by,
                                                                    ((MainActivity) getActivity()).getSubdiv_code(), assign_status_role,
                                                                    assign_priority, titleText, descText, assign_severity,
                                                                    assign_to_role, assign_hescom_tvd, mr_code, csd_hescom, file_encode);
                                                    } else et_narration.setError("Enter Narration");
                                            } else et_description.setError("Enter Description");
                                    } else et_title.setError("Enter Title");
                                } else
                                    functionsCall.showtoast(getActivity(), "Please Select Status");
                            } else
                                functionsCall.showtoast(getActivity(), "Please Select Department");
                        } else functionsCall.showtoast(getActivity(), "Please Select Assign To");
                    } else functionsCall.showtoast(getActivity(), "Please Select Severity");
                } else functionsCall.showtoast(getActivity(), "Please Select Priority");
            }
        });
    }
    //*************************************************Ticket Dialog****************************************************************
    private void showdialog(int id) {
        Ticket_Id = getSetValues.getGenerated_tic_id();
        switch (id) {
            case DLG_TICKET_ID:
                AlertDialog.Builder ticket_id = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                ticket_id.setTitle("Ticket Result");
                ticket_id.setCancelable(false);
                ticket_id.setMessage("Your Ticket ID " + Ticket_Id + " is Generated successfully!!");
                ticket_id.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        email();
                        ((MainActivity) getActivity()).switchContent(FORM0, getResources().getString(R.string.app_name));
                    }
                });
                AlertDialog dialog = ticket_id.create();
                dialog.show();
                break;
        }
    }
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && regex.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    //*************************************************send email**********************************************************
    public void email() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"notification@hescomtrm.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Ticket ID " + Ticket_Id + " is generated successfully.");
        email.putExtra(Intent.EXTRA_TEXT, "Dear user, \n\n Title : " + titleText + "\n" + "File : " + imageNameOnly);
        email.setType("plain/text");
        try {
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    // *************Select image from camera and gallery**********************************************************
    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Option");
        String[] pictureDialogItems = {"Gallery", "Camera", "File Manager"};
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
                    assert URI != null;
                    Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(URI, FILE,
                            null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(FILE[0]);
                    ImageDecode = cursor.getString(columnIndex);
                    cursor.close();
                    filepathImage = getPath(getContext(), URI);
                    Toast.makeText(getContext(), "Uplaoded File is" + filepathImage, Toast.LENGTH_SHORT).show();
                    buttonpick.setText(filepathImage);

                }
            } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                imageview.setImageBitmap(photo);
                Uri tempUri = getImageUri(getContext(), photo);
                filepathImage = (getRealPathFromURI(tempUri));
                Toast.makeText(getContext(), "Uplaoded File is" + filepathImage, Toast.LENGTH_SHORT).show();
                buttonpick.setText(filepathImage);
            } else if (requestCode == FILE_MANAGER) {
                if (data != null) {
                    Uri URI = data.getData();
                    String[] FILE = {MediaStore.Images.Media.DATA};
                    ContentResolver cr = Objects.requireNonNull(getContext()).getContentResolver();
                    Cursor cursor = cr.query(URI, FILE, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(FILE[0]);
                    ImageDecode = cursor.getString(columnIndex);
                    cursor.close();
                    filepathImage = getPath(getContext(), URI);
                    Toast.makeText(getContext(), "Uplaoded File is" + filepathImage, Toast.LENGTH_SHORT).show();
                    buttonpick.setText(filepathImage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "File not attached, try again...", Toast.LENGTH_SHORT).show();
        }
    }

    //*************************************************************************************************************************
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title",
                null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        @SuppressLint("Recycle") Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri,
                null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
//*************************************************************************************************************************
