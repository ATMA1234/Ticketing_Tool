package com.transvision.ticketing.extra;

import java.io.Serializable;

public class GetSetValues implements Serializable {
    private String login_role = "", requested_by = "", tab_name = "", subdivision = "", tic_id = "", tic_narr = "", tic_file = "",
            tic_genby = "", tic_genon = "", tic_status = "", tic_close = "", tic_priority = "", tic_severity = "", tic_title = "",
            subdivision_code = "", tic_desc = "", tic_assign = "", tic_hescom = "", generated_tic_id = "", clear_on = "",
            tic_assigned_by = "", tic_mr_code, csd_hescom = "", tic_comment = "";
    private String USER_NAME,EMAILID = "",MOBILE_NO = "",user_role = "", password = "",app_version;
    private String up_requested_by = "",up_subdivision = "",up_tic_id = "",up_tic_narr = "",up_tic_file = "",up_tic_genby = "",
            up_tic_genon = "",up_tic_status = "",up_tic_close = "",up_tic_priority = "",up_tic_severity = "", up_tic_title = "",
            up_subdivision_code = "", up_tic_desc = "", up_tic_assign = "", up_tic_hescom = "", up_generated_tic_id = "",
            up_clear_on = "", up_tic_assigned_by = "", up_tic_mr_code, up_csd_hescom = "", up_tic_comment = "";

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId = "";

    public String getUp_requested_by() {
        return up_requested_by;
    }

    public void setUp_requested_by(String up_requested_by) {
        this.up_requested_by = up_requested_by;
    }

    public String getUp_subdivision() {
        return up_subdivision;
    }

    public void setUp_subdivision(String up_subdivision) {
        this.up_subdivision = up_subdivision;
    }

    public String getUp_tic_id() {
        return up_tic_id;
    }

    public void setUp_tic_id(String up_tic_id) {
        this.up_tic_id = up_tic_id;
    }

    public String getUp_tic_narr() {
        return up_tic_narr;
    }

    public void setUp_tic_narr(String up_tic_narr) {
        this.up_tic_narr = up_tic_narr;
    }

    public String getUp_tic_file() {
        return up_tic_file;
    }

    public void setUp_tic_file(String up_tic_file) {
        this.up_tic_file = up_tic_file;
    }

    public String getUp_tic_genby() {
        return up_tic_genby;
    }

    public void setUp_tic_genby(String up_tic_genby) {
        this.up_tic_genby = up_tic_genby;
    }

    public String getUp_tic_genon() {
        return up_tic_genon;
    }

    public void setUp_tic_genon(String up_tic_genon) {
        this.up_tic_genon = up_tic_genon;
    }

    public String getUp_tic_status() {
        return up_tic_status;
    }

    public void setUp_tic_status(String up_tic_status) {
        this.up_tic_status = up_tic_status;
    }

    public String getUp_tic_close() {
        return up_tic_close;
    }

    public void setUp_tic_close(String up_tic_close) {
        this.up_tic_close = up_tic_close;
    }

    public String getUp_tic_priority() {
        return up_tic_priority;
    }

    public void setUp_tic_priority(String up_tic_priority) {
        this.up_tic_priority = up_tic_priority;
    }

    public String getUp_tic_severity() {
        return up_tic_severity;
    }

    public void setUp_tic_severity(String up_tic_severity) {
        this.up_tic_severity = up_tic_severity;
    }

    public String getUp_tic_title() {
        return up_tic_title;
    }

    public void setUp_tic_title(String up_tic_title) {
        this.up_tic_title = up_tic_title;
    }

    public String getUp_subdivision_code() {
        return up_subdivision_code;
    }

    public void setUp_subdivision_code(String up_subdivision_code) {
        this.up_subdivision_code = up_subdivision_code;
    }

    public String getUp_tic_desc() {
        return up_tic_desc;
    }

    public void setUp_tic_desc(String up_tic_desc) {
        this.up_tic_desc = up_tic_desc;
    }

    public String getUp_tic_assign() {
        return up_tic_assign;
    }

    public void setUp_tic_assign(String up_tic_assign) {
        this.up_tic_assign = up_tic_assign;
    }

    public String getUp_tic_hescom() {
        return up_tic_hescom;
    }

    public void setUp_tic_hescom(String up_tic_hescom) {
        this.up_tic_hescom = up_tic_hescom;
    }

    public String getUp_generated_tic_id() {
        return up_generated_tic_id;
    }

    public void setUp_generated_tic_id(String up_generated_tic_id) {
        this.up_generated_tic_id = up_generated_tic_id;
    }

    public String getUp_clear_on() {
        return up_clear_on;
    }

    public void setUp_clear_on(String up_clear_on) {
        this.up_clear_on = up_clear_on;
    }

    public String getUp_tic_assigned_by() {
        return up_tic_assigned_by;
    }

    public void setUp_tic_assigned_by(String up_tic_assigned_by) {
        this.up_tic_assigned_by = up_tic_assigned_by;
    }

    public String getUp_tic_mr_code() {
        return up_tic_mr_code;
    }

    public void setUp_tic_mr_code(String up_tic_mr_code) {
        this.up_tic_mr_code = up_tic_mr_code;
    }

    public String getUp_csd_hescom() {
        return up_csd_hescom;
    }

    public void setUp_csd_hescom(String up_csd_hescom) {
        this.up_csd_hescom = up_csd_hescom;
    }

    public String getUp_tic_comment() {
        return up_tic_comment;
    }

    public void setUp_tic_comment(String up_tic_comment) {
        this.up_tic_comment = up_tic_comment;
    }

    public String getTic_comment() {
        return tic_comment;
    }

    public void setTic_comment(String tic_comment) {
        this.tic_comment = tic_comment;
    }

    public GetSetValues() {
    }

    public String getLogin_role() {
        return login_role;
    }

    public void setLogin_role(String login_role) {
        this.login_role = login_role;
    }

    public String getCsd_hescom() {
        return csd_hescom;
    }

    public void setCsd_hescom(String csd_hescom) {
        this.csd_hescom = csd_hescom;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }

    public String getTab_name() {
        return tab_name;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getClear_on() {
        return clear_on;
    }

    public void setClear_on(String clear_on) {
        this.clear_on = clear_on;
    }

    public String getTic_assigned_by() {
        return tic_assigned_by;
    }

    public void setTic_assigned_by(String tic_assigned_by) {
        this.tic_assigned_by = tic_assigned_by;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getEMAILID() {
        return EMAILID;
    }

    public void setEMAILID(String EMAILID) {
        this.EMAILID = EMAILID;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getSubdivision_code() {
        return subdivision_code;
    }

    public void setSubdivision_code(String subdivision_code) {
        this.subdivision_code = subdivision_code;
    }

    public String getTic_mr_code() {
        return tic_mr_code;
    }

    public void setTic_mr_code(String tic_mr_code) {
        this.tic_mr_code = tic_mr_code;
    }

    public String getTic_id() {
        return tic_id;
    }

    public void setTic_id(String tic_id) {
        this.tic_id = tic_id;
    }

    public String getTic_narr() {
        return tic_narr;
    }

    public void setTic_narr(String tic_narr) {
        this.tic_narr = tic_narr;
    }

    public String getTic_file() {
        return tic_file;
    }

    public void setTic_file(String tic_file) {
        this.tic_file = tic_file;
    }

    public String getTic_genby() {
        return tic_genby;
    }

    public void setTic_genby(String tic_genby) {
        this.tic_genby = tic_genby;
    }

    public String getTic_genon() {
        return tic_genon;
    }

    public void setTic_genon(String tic_genon) {
        this.tic_genon = tic_genon;
    }

    public String getTic_status() {
        return tic_status;
    }

    public void setTic_status(String tic_status) {
        this.tic_status = tic_status;
    }

    public String getTic_close() {
        return tic_close;
    }

    public void setTic_close(String tic_close) {
        this.tic_close = tic_close;
    }

    public String getTic_priority() {
        return tic_priority;
    }

    public void setTic_priority(String tic_priority) {
        this.tic_priority = tic_priority;
    }

    public String getTic_severity() {
        return tic_severity;
    }

    public void setTic_severity(String tic_severity) {
        this.tic_severity = tic_severity;
    }

    public String getTic_title() {
        return tic_title;
    }

    public void setTic_title(String tic_title) {
        this.tic_title = tic_title;
    }

    public String getTic_desc() {
        return tic_desc;
    }

    public void setTic_desc(String tic_desc) {
        this.tic_desc = tic_desc;
    }

    public String getTic_assign() {
        return tic_assign;
    }

    public void setTic_assign(String tic_assign) {
        this.tic_assign = tic_assign;
    }

    public String getTic_hescom() {
        return tic_hescom;
    }

    public void setTic_hescom(String tic_hescom) {
        this.tic_hescom = tic_hescom;
    }

    public String getGenerated_tic_id() {
        return generated_tic_id;
    }

    public void setGenerated_tic_id(String generated_tic_id) {
        this.generated_tic_id = generated_tic_id;
    }
}