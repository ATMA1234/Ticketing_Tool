package com.transvision.ticketing.posting;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import com.transvision.ticketing.extra.FunctionsCall;
import com.transvision.ticketing.extra.GetSetValues;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import static com.transvision.ticketing.extra.Constants.Service;
import static com.transvision.ticketing.extra.Constants.TRM_URL;

public class SendingData {
    private ReceivingData receivingData = new ReceivingData();
    private static final String BASE_URL = TRM_URL + Service;
    private FunctionsCall functionsCall = new FunctionsCall();

    private String urlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(datamap));
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        } else {
            response = new StringBuilder();
        }
        return response.toString();
    }

    private String getPostDataString(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String urlGetConnection(String Get_Url) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = new URL(Get_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        } else {
            response = new StringBuilder();
        }
        return response.toString();
    }

    //********************************view all tickets******************************************************
    @SuppressLint("StaticFieldLeak")
    public class View_All_Tickets extends AsyncTask<String, String, String> {
        String response = "";
        GetSetValues getSetValues;
        Handler handler;
        ArrayList<GetSetValues> arrayList;

        public View_All_Tickets(GetSetValues getSetValues, Handler handler, ArrayList<GetSetValues> arrayList) {
            this.getSetValues = getSetValues;
            this.handler = handler;
            this.arrayList = arrayList;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("uname", params[0]);
            datamap.put("pass", params[1]);
            datamap.put("userrole", params[2]);
            try {
                response = urlPostConnection(BASE_URL + "TicketDetails", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.all_tickets_status(result, getSetValues, handler, arrayList);
        }
    }

    //******************************************Generate Ticket***************************************************************
    @SuppressLint("StaticFieldLeak")
    public class GenerateTicket extends AsyncTask<String, String, String> {
        String requestUrl = "";
        Handler handler;
        GetSetValues getSetValues;

        public GenerateTicket(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("NARRATION", params[0]);
            datamap.put("TIC_FILE", params[1]);
            datamap.put("TIC_GENBY", params[2]);
            datamap.put("TIC_SUBCODE", params[3]);
            datamap.put("TIC_STATUS", params[4]);
            datamap.put("PRIORITY", params[5]);
            datamap.put("TITLE", params[6]);
            datamap.put("DESCRIPTION", params[7]);
            datamap.put("SEVIRITY", params[8]);
            datamap.put("ASSIGN", params[9]);
            datamap.put("HESCOM", params[10]);
            datamap.put("MR_CODE", params[11]);
            datamap.put("CSD_HESCOM", params[12]);
            datamap.put("Encodefile", params[13]);

            functionsCall.logStatus("NARRATION: " + params[0]);
            functionsCall.logStatus("TIC_FILE: " + params[1]);
            functionsCall.logStatus("TIC_GENBY: " + params[2]);
            functionsCall.logStatus("TIC_SUBCODE: " + params[3]);
            functionsCall.logStatus("TIC_STATUS: " + params[4]);
            functionsCall.logStatus("PRIORITY: " + params[5]);
            functionsCall.logStatus("TITLE: " + params[6]);
            functionsCall.logStatus("DESCRIPTION: " + params[7]);
            functionsCall.logStatus("SEVIRITY: " + params[8]);
            functionsCall.logStatus("ASSIGN: " + params[9]);
            functionsCall.logStatus("HESCOM: " + params[10]);
            functionsCall.logStatus("MR_CODE: " + params[11]);
            functionsCall.logStatus("CSD_HESCOM: " + params[12]);
            functionsCall.logStatus("Encodefile: " + params[13]);
            try {
                requestUrl = urlPostConnection(BASE_URL + "TicketDataInsert", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return requestUrl;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.generate_ticket_status(result, handler, getSetValues);
        }
    }

    //***********************************update ticket********************************************************
    @SuppressLint("StaticFieldLeak")
    public class TicketUpdate extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;

        public TicketUpdate(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("TIC_ID", params[0]);
            datamap.put("NARRATION", params[1]);
            datamap.put("TIC_FILE", params[2]);
            datamap.put("TIC_GENBY", params[3]);
            datamap.put("TIC_SUBCODE", params[4]);
            datamap.put("TIC_STATUS", params[5]);
            datamap.put("PRIORITY", params[6]);
            datamap.put("SEVIRITY", params[7]);
            datamap.put("TITLE", params[8]);
            datamap.put("DESCRIPTION", params[9]);
            datamap.put("ASSIGN", params[10]);
            datamap.put("HESCOM", params[11]);
            datamap.put("MR_CODE", params[12]);
            datamap.put("COMMENT", params[13]);
            datamap.put("Encodefile", params[14]);

            functionsCall.logStatus("TIC_ID: " + params[0]);
            functionsCall.logStatus("NARRATION: " + params[1]);
            functionsCall.logStatus("TIC_FILE: " + params[2]);
            functionsCall.logStatus("TIC_GENBY: " + params[3]);
            functionsCall.logStatus("TIC_SUBCODE: " + params[4]);
            functionsCall.logStatus("TIC_STATUS: " + params[5]);
            functionsCall.logStatus("PRIORITY: " + params[6]);
            functionsCall.logStatus("SEVIRITY: " + params[7]);
            functionsCall.logStatus("TITLE: " + params[8]);
            functionsCall.logStatus("DESCRIPTION: " + params[9]);
            functionsCall.logStatus("ASSIGN: " + params[10]);
            functionsCall.logStatus("HESCOM: " + params[11]);
            functionsCall.logStatus("MR_CODE: " + params[12]);
            functionsCall.logStatus("COMMENT: " + params[13]);
            functionsCall.logStatus("Encodefile: " + params[14]);
            try {
                response = urlPostConnection(BASE_URL + "TicketDataUpdate", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.ticket_update_status(result, handler, getSetValues);
        }
    }
    //********************************view update tickets******************************************************
    @SuppressLint("StaticFieldLeak")
    public class View_Update_Tickets extends AsyncTask<String, String, String> {
        String response = "";
        GetSetValues getSetValues;
        Handler handler;
        ArrayList<GetSetValues> arrayList;

        public View_Update_Tickets(GetSetValues getSetValues, Handler handler, ArrayList<GetSetValues> arrayList) {
            this.getSetValues = getSetValues;
            this.handler = handler;
            this.arrayList = arrayList;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("ticket_id", params[0]);
            try {
                response = urlPostConnection(BASE_URL + "TicketUpdateDetails", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.all_tickets_update_status(result, getSetValues, handler, arrayList);
        }
    }

    //***********************************For user Login***********************************************************************
    @SuppressLint("StaticFieldLeak")
    public class Login extends AsyncTask<String, String, String> {
        String response = "";
        GetSetValues getSetValues;
        Handler handler;
        ArrayList<GetSetValues> arrayList;

        public Login(GetSetValues getSetValues, Handler handler, ArrayList<GetSetValues> arrayList) {
            this.getSetValues = getSetValues;
            this.handler = handler;
            this.arrayList = arrayList;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap();
            datamap.put("username", params[0]);
            datamap.put("password", params[1]);
            try {
                response = urlPostConnection(BASE_URL + "loginDetails", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.get_Details(result, getSetValues, handler, arrayList);
        }
    }
    //***********************************For MR Login***********************************************************************
//    @SuppressLint("StaticFieldLeak")
//    public class MRLogin extends AsyncTask<String, String, String> {
//        String response = "";
//        GetSetValues getSetValues;
//        Handler handler;
//        ArrayList<GetSetValues> arrayList;
//
//        public MRLogin(GetSetValues getSetValues, Handler handler, ArrayList<GetSetValues> arrayList) {
//            this.getSetValues = getSetValues;
//            this.handler = handler;
//            this.arrayList = arrayList;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HashMap<String, String> datamap = new HashMap();
//            datamap.put("username", params[0]);
//            datamap.put("password", params[1]);
//            try {
//                response = urlPostConnection(BASE_URL + "loginDetails", datamap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            receivingData.getMR_Details(result, getSetValues, handler, arrayList);
//        }
//    }
    //*************************************Application_Update**********************************************************************
//    @SuppressLint("StaticFieldLeak")
//    public class Application_Update extends AsyncTask<String, String, String> {
//        String response="";
//        Handler handler;
//        GetSetValues getSetValues;
//
//        public Application_Update(Handler handler, GetSetValues getSetValues) {
//            this.handler = handler;
//            this.getSetValues = getSetValues;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                response = urlGetConnection(BASE_URL+"AndroidVersion");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            receivingData.getApplication_Status(result, handler, getSetValues);
//        }
//    }
}
