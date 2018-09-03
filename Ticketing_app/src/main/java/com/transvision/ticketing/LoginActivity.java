package com.transvision.ticketing;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.ticketing.adapter.RoleAdapter;
import com.transvision.ticketing.extra.FunctionsCall;
import com.transvision.ticketing.extra.GetSetValues;
import com.transvision.ticketing.posting.FTPAPI;
import com.transvision.ticketing.posting.SendingData;

import java.io.File;
import java.util.ArrayList;

import static com.transvision.ticketing.extra.Constants.APK_FILE_DOWNLOADED;
import static com.transvision.ticketing.extra.Constants.APK_FILE_FOUND;
import static com.transvision.ticketing.extra.Constants.APK_FILE_NOT_FOUND;
import static com.transvision.ticketing.extra.Constants.LOGIN_FAILURE;
import static com.transvision.ticketing.extra.Constants.LOGIN_SUCCESS;

public class LoginActivity extends AppCompatActivity {
    private static final int DLG_LOGIN = 2;
    private static final int DLG_LOGIN_MR = 3;
    private static final int DLG_APK_UPDATE_SUCCESS = 4;
    private static final int DLG_APK_NOT_FOUND = 5;
    Spinner role_spinner1;
    ArrayList<GetSetValues> roles_list1;
    RoleAdapter roleAdapter1;
    GetSetValues getSetValues;
    FunctionsCall fcall;
    Button login_btn;
    String main_role1 = "", login_id = "", role_password = "";
    Boolean Internet = false;
    ProgressDialog progressDialog;
    ArrayList<GetSetValues> tickets_list;
    SendingData sendingData;
    private ArrayList<GetSetValues> arrayList;
    TextView vesrion;
    String main_curr_version = "";
    FTPAPI ftpapi;
    //*********************************************handler***************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    progressDialog.dismiss();
                    if (fcall.compare(main_curr_version, getSetValues.getApp_version()))
                        showdialog(DLG_APK_UPDATE_SUCCESS);
                    else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("ROLE", main_role1);
                        intent.putExtra("UserId", login_id);
                        intent.putExtra("UserPassword", role_password);
                        intent.putExtra("SUBDIVCODE", getSetValues.getSubdivision());
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case LOGIN_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();
                    break;

                case APK_FILE_DOWNLOADED:
                    progressDialog.dismiss();
                    fcall.updateApp(LoginActivity.this, new File(fcall.filepath("ApkFolder") +
                            File.separator + "Ticketing_app_" + getSetValues.getApp_version() + ".apk"));
                    break;

                case APK_FILE_FOUND:
                    progressDialog.dismiss();
                    break;

                case APK_FILE_NOT_FOUND:
                    progressDialog.dismiss();
                    showDialog(DLG_APK_NOT_FOUND);
                    break;

            }
            return false;
        }
    });

    //************************************************************************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //*****************************set app version to drawer**************************************************************
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            main_curr_version = pInfo.versionName;
        }
        //****************************************************************************************************************
        vesrion = findViewById(R.id.text_version);
        vesrion.setText("Version" + " : " + main_curr_version);
        role_spinner1 = findViewById(R.id.login_users_spin2);
        roles_list1 = new ArrayList<>();
        roleAdapter1 = new RoleAdapter(roles_list1, this);
        role_spinner1.setAdapter(roleAdapter1);
        login_btn = findViewById(R.id.login_btn);
        fcall = new FunctionsCall();
        sendingData = new SendingData();
        getSetValues = new GetSetValues();
        tickets_list = new ArrayList<>();
        arrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(LoginActivity.this);
        ftpapi = new FTPAPI();

        for (int i = 0; i < getResources().getStringArray(R.array.first_login).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.first_login)[i]);
            roles_list1.add(getSetValues);
            roleAdapter1.notifyDataSetChanged();
        }
        role_spinner1.setSelection(0);

//*****************************************login button*********************************************************************
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvrole = findViewById(R.id.spinner_txt);
                String role = tvrole.getText().toString();
                if (!role.equals("--SELECT--")) {
                    main_role1 = role;
                    if (role.equals("AEE")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("ADMIN")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("AAO")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("CW")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("CORP_MD")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("CORP_DT")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("CORP_SE")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("CORP_EE")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("CORP_AO")) {
                        showdialog(DLG_LOGIN);
                    } else if (role.equals("CORP_TL")) {
                        showdialog(DLG_LOGIN);
//                    } else if (role.equals("MR")) {
//                        showdialog(DLG_LOGIN_MR);
                    }
                }
            }
        });
    }

    //********************************************show login dailog************************************************************
    private void showdialog(int id) {
        Dialog dialog;
        switch (id) {
            case DLG_LOGIN:
                AlertDialog.Builder login_dlg = new AlertDialog.Builder(this);
                login_dlg.setTitle(getResources().getString(R.string.dialog_login));
                login_dlg.setCancelable(false);
                LinearLayout dlg_linear = (LinearLayout) getLayoutInflater().inflate(R.layout.login_layout, null);
                login_dlg.setView(dlg_linear);
                final EditText et_loginid = dlg_linear.findViewById(R.id.et_login_id);
                final EditText et_password = dlg_linear.findViewById(R.id.et_login_password);
                login_dlg.setPositiveButton(getResources().getString(R.string.dialog_login), null);
                login_dlg.setNegativeButton(getResources().getString(android.R.string.cancel), null);
                final AlertDialog login_dialog = login_dlg.create();
                login_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positive = login_dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negative = login_dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login_id = et_loginid.getText().toString();
                                Internet = fcall.isInternetOn(LoginActivity.this);
                                if (!TextUtils.isEmpty(login_id)) {
                                    role_password = et_password.getText().toString();
                                    if (!TextUtils.isEmpty(role_password)) {
                                        login_dialog.dismiss();
                                        progressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.login),
                                                getResources().getString(R.string.login_message));
                                        if (fcall.isInternetOn(LoginActivity.this)) {

                                            SendingData.Login login = sendingData.new Login(getSetValues, handler, arrayList);
                                            login.execute(login_id, role_password);

                                        } else {
                                            Toast.makeText(LoginActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } else
                                        et_password.setError(getResources().getString(R.string.dialog_login_password_error));
                                } else
                                    et_loginid.setError(getResources().getString(R.string.dialog_login_id_error));
                            }
                        });
                        negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login_dialog.dismiss();
                            }
                        });
                    }
                });
                login_dialog.show();
                (login_dialog).getButton(AlertDialog.BUTTON_POSITIVE).
                        setTextColor(Color.BLUE);
                (login_dialog).getButton(AlertDialog.BUTTON_NEGATIVE).
                        setTextColor(Color.RED);
                break;

            case DLG_APK_UPDATE_SUCCESS:
                android.app.AlertDialog.Builder appupdate = new android.app.AlertDialog.Builder(this);
                appupdate.setTitle("App Updates");
                appupdate.setCancelable(false);
                appupdate.setMessage("Your current version number : " + main_curr_version +
                        "\n" + "\n" +
                        "New version is available : " + getSetValues.getApp_version() + "\n");
                appupdate.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FTPAPI.Download_apk downloadApk = ftpapi.new Download_apk(handler, progressDialog,
                                getSetValues.getApp_version());
                        downloadApk.execute();
                    }
                });
                appupdate.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog = appupdate.create();
                dialog.show();
                break;

            case DLG_APK_NOT_FOUND:
                AlertDialog.Builder apknotfound = new AlertDialog.Builder(this);
                apknotfound.setTitle("App Update");
                apknotfound.setCancelable(false);
                apknotfound.setMessage("Apk not found to download from server.." + "\n" + "Please ask to update in server..");
                apknotfound.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog = apknotfound.create();
                dialog.show();
                break;
//**********************************************MR login***************************************************************
//            case DLG_LOGIN_MR:
//                AlertDialog.Builder login_dlg_mr = new AlertDialog.Builder(this);
//                login_dlg_mr.setTitle(getResources().getString(R.string.dialog_login));
//                login_dlg_mr.setCancelable(false);
//                LinearLayout dlg_linear_mr = (LinearLayout) getLayoutInflater().inflate(R.layout.login_layout, null);
//                login_dlg_mr.setView(dlg_linear_mr);
//                final EditText et_loginid_mr =  dlg_linear_mr.findViewById(R.id.et_login_id);
//                final EditText et_password_mr =  dlg_linear_mr.findViewById(R.id.et_login_password);
//                login_dlg_mr.setPositiveButton(getResources().getString(R.string.dialog_login), null);
//                login_dlg_mr.setNegativeButton(getResources().getString(android.R.string.cancel), null);
//                final AlertDialog login_dialog_mr = login_dlg_mr.create();
//                login_dialog_mr.setOnShowListener(new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface dialog) {
//                        Button positive = login_dialog_mr.getButton(AlertDialog.BUTTON_POSITIVE);
//                        Button negative = login_dialog_mr.getButton(AlertDialog.BUTTON_NEGATIVE);
//                        positive.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                login_id = et_loginid_mr.getText().toString();
//                                if (!TextUtils.isEmpty(login_id)) {
//                                    role_password = et_password_mr.getText().toString();
//                                    if (!TextUtils.isEmpty(role_password)) {
//                                        login_dialog_mr.dismiss();
//                                        progressDialog = ProgressDialog.show(LoginActivity.this,
//                                                getResources().getString(R.string.login),
//                                                getResources().getString(R.string.login_message));
//                                        if (fcall.isInternetOn(LoginActivity.this)) {
//                                            SendingData.MRLogin mrLogin = sendingData.new MRLogin(getSetValues, handler, arrayList);
//                                            mrLogin.execute(login_id, role_password);
//
//                                        } else {
//                                            Toast.makeText(LoginActivity.this, "Please Connect to" +
//                                                    " Internet", Toast.LENGTH_SHORT).show();
//                                            progressDialog.dismiss();
//                                        }
//                                    } else
//                                        et_password_mr.setError(getResources().getString(R.string.dialog_login_password_error));
//                                } else
//                                    et_loginid_mr.setError(getResources().getString(R.string.dialog_login_id_error));
//                            }
//                        });
//                        negative.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                login_dialog_mr.dismiss();
//                            }
//                        });
//                    }
//                });
//                login_dialog_mr.show();
//                ( login_dialog_mr).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
//                ( login_dialog_mr).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
//                break;
        }
    }
}

