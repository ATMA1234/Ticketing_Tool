package com.transvision.ticketing.posting;

import android.os.Handler;
import com.transvision.ticketing.extra.FunctionsCall;
import com.transvision.ticketing.extra.GetSetValues;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import static com.transvision.ticketing.extra.Constants.GENERATE_TICKET_FAILURE;
import static com.transvision.ticketing.extra.Constants.GENERATE_TICKET_SUCCESS;
import static com.transvision.ticketing.extra.Constants.LOGIN_FAILURE;
import static com.transvision.ticketing.extra.Constants.LOGIN_SUCCESS;
import static com.transvision.ticketing.extra.Constants.TICKETS_VIEWFAILURE;
import static com.transvision.ticketing.extra.Constants.TICKETS_VIEWSUCCESS;
import static com.transvision.ticketing.extra.Constants.TICKET_UPDATE_VIEWFAILURE;
import static com.transvision.ticketing.extra.Constants.TICKET_UPDATE_VIEWSUCCESS;
import static com.transvision.ticketing.extra.Constants.UPDATE_TICKET_FAILURE;
import static com.transvision.ticketing.extra.Constants.UPDATE_TICKET_SUCCESS;

public class ReceivingData {
    private FunctionsCall functionsCall = new FunctionsCall();

    private String parseServerXML(String result) {
        String value = "";
        XmlPullParserFactory pullParserFactory;
        InputStream res;
        try {
            res = new ByteArrayInputStream(result.getBytes());
            pullParserFactory = XmlPullParserFactory.newInstance();
            pullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(res, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case "string":
                                value = parser.nextText();
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    //******************************************all tickets*******************************************************
    public void all_tickets_status(String result, GetSetValues getSetValues, Handler handler, ArrayList<GetSetValues> arrayList) {
        result = parseServerXML(result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    getSetValues.setTic_id(jsonObject.getString("TIC_ID")); //1
                    getSetValues.setTic_narr(jsonObject.getString("TIC_NARR"));
                    getSetValues.setTic_file(jsonObject.getString("TIC_FILE"));
                    getSetValues.setTic_genby(jsonObject.getString("TIC_GENBY"));
                    getSetValues.setTic_genon(jsonObject.getString("TIC_GENON"));
                    getSetValues.setTic_status(jsonObject.getString("TIC_STATUS"));
                    getSetValues.setTic_close(jsonObject.getString("CLOSED_ON"));
                    getSetValues.setSubdivision_code(jsonObject.getString("TIC_SUBCODE"));
                    getSetValues.setClear_on(jsonObject.getString("TIC_CLEARON"));
                    getSetValues.setTic_priority(jsonObject.getString("PRIORITY"));
                    getSetValues.setTic_assign(jsonObject.getString("ASSIGN"));
                    getSetValues.setTic_title(jsonObject.getString("TITLE"));
                    getSetValues.setTic_desc(jsonObject.getString("DESCRIPTION"));
                    getSetValues.setTic_severity(jsonObject.getString("SEVIRITY"));
                    getSetValues.setTic_assigned_by(jsonObject.getString("ASSIGNED_BY"));
                    getSetValues.setTic_hescom(jsonObject.getString("HESCOM"));
                    getSetValues.setCsd_hescom(jsonObject.getString("CSD_HESCOM"));
                    getSetValues.setTic_mr_code(jsonObject.getString("MR_CODE"));
                    getSetValues.setTic_comment(jsonObject.getString("COMMENT")); //19
                    arrayList.add(getSetValues);
                }
                handler.sendEmptyMessage(TICKETS_VIEWSUCCESS);
            } else handler.sendEmptyMessage(TICKETS_VIEWFAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(TICKETS_VIEWFAILURE);
        }
    }

    //**************************************generate_ticket_status***********************************************************
    public void generate_ticket_status(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);
        functionsCall.logStatus("Ticket Status: " + result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String message = jsonObject.getString("message");
            if (StringUtils.startsWithIgnoreCase(message, "Success")) {
                getSetValues.setGenerated_tic_id(jsonObject.getString("id"));
                handler.sendEmptyMessage(GENERATE_TICKET_SUCCESS);
            } else handler.sendEmptyMessage(GENERATE_TICKET_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(GENERATE_TICKET_FAILURE);
        }
    }

    //************************************ticket_update_status****************************************************************
    public void ticket_update_status(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);
        functionsCall.logStatus("Update Ticket: " + result);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            if (StringUtils.startsWithIgnoreCase(jsonObject.getString("message"), "Success"))
                handler.sendEmptyMessage(UPDATE_TICKET_SUCCESS);
            else handler.sendEmptyMessage(UPDATE_TICKET_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(UPDATE_TICKET_FAILURE);
        }
    }
    //******************************FOR getting result based on user LOGIN*****************************************************
    public void get_Details(String result, GetSetValues getSetValues, Handler handler, ArrayList<GetSetValues> arrayList) {
        result = parseServerXML(result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String message = jsonObject.getString("message");
            if (StringUtils.startsWithIgnoreCase(message, "Success")) {
                getSetValues.setUSER_NAME(jsonObject.getString("USERNAME"));//1
                getSetValues.setEMAILID(jsonObject.getString("EMAIL_ID"));
                getSetValues.setMOBILE_NO(jsonObject.getString("MOBILE_NO"));
                getSetValues.setSubdivision(jsonObject.getString("SUBDIVCODE"));
                getSetValues.setApp_version(jsonObject.getString("TIC_VERSION"));//5

                handler.sendEmptyMessage(LOGIN_SUCCESS);
            } else handler.sendEmptyMessage(LOGIN_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(LOGIN_FAILURE);
        }
    }

    //******************************************Update_tickets_status******************************************************
    public void all_tickets_update_status(String result, GetSetValues getSetValues, Handler handler,
                                          ArrayList<GetSetValues> arrayList1) {
        result = parseServerXML(result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    getSetValues.setUp_tic_id(jsonObject.getString("TIC_ID")); //1
                    getSetValues.setUp_tic_narr(jsonObject.getString("TIC_NARR"));
                    getSetValues.setUp_tic_file(jsonObject.getString("TIC_FILE"));
                    getSetValues.setUp_tic_genby(jsonObject.getString("TIC_GENBY"));
                    getSetValues.setUp_tic_genon(jsonObject.getString("TIC_GENON"));
                    getSetValues.setUp_tic_status(jsonObject.getString("TIC_STATUS"));
                    getSetValues.setUp_tic_close(jsonObject.getString("CLOSED_ON"));
                    getSetValues.setUp_subdivision_code(jsonObject.getString("TIC_SUBCODE"));
                    getSetValues.setUp_tic_priority(jsonObject.getString("PRIORITY"));
                    getSetValues.setUp_tic_assign(jsonObject.getString("ASSIGN"));
                    getSetValues.setUp_tic_title(jsonObject.getString("TITLE"));
                    getSetValues.setUp_tic_desc(jsonObject.getString("DESCRIPTION"));
                    getSetValues.setUp_tic_severity(jsonObject.getString("SEVIRITY"));
                    getSetValues.setUp_tic_assigned_by(jsonObject.getString("ASSIGNED_BY"));
                    getSetValues.setUp_tic_hescom(jsonObject.getString("THESCOM"));
                    getSetValues.setUp_csd_hescom(jsonObject.getString("CSD_HESCOM"));
                    getSetValues.setUp_tic_comment(jsonObject.getString("COMMENT")); //17
                    arrayList1.add(getSetValues);
                }
                handler.sendEmptyMessage(TICKET_UPDATE_VIEWSUCCESS);
            } else handler.sendEmptyMessage(TICKET_UPDATE_VIEWFAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(TICKET_UPDATE_VIEWFAILURE);
        }
    }
    //******************************FOR getting result based on MR LOGIN*****************************************************
//    public void getMR_Details(String result, GetSetValues getSetValues, Handler handler, ArrayList<GetSetValues> arrayList) {
//        result = parseServerXML(result);
//        JSONArray jsonArray;
//        try {
//            jsonArray = new JSONArray(result);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String message = jsonObject.getString("message");
//                if (StringUtils.startsWithIgnoreCase(message, "Success")) {
//                    getSetValues.setUSER_NAME(jsonObject.getString("USERNAME"));
//                    getSetValues.setEMAILID(jsonObject.getString("EMAIL_ID"));
//                    getSetValues.setMOBILE_NO(jsonObject.getString("MOBILE_NO"));
//                    handler.sendEmptyMessage(LOGIN_SUCCESS);
//                } else handler.sendEmptyMessage(LOGIN_FAILURE);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            handler.sendEmptyMessage(LOGIN_FAILURE);
//        }
//    }
    //**********************************get Application_Status***************************************************************
//    public void getApplication_Status(String result, Handler handler, GetSetValues getSetValues) {
//        result = parseServerXML(result);
//        functionsCall.logStatus("Apk Version: "+result);
//        getSetValues.setApp_version(result); //Version
//        handler.sendEmptyMessage(APPLICATION_STATUS);
//    }
}
