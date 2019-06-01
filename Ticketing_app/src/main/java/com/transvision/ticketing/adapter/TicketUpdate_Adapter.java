package com.transvision.ticketing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.ticketing.R;
import com.transvision.ticketing.ViewUpdateDeatils;
import com.transvision.ticketing.extra.GetSetValues;

import java.util.ArrayList;

public class TicketUpdate_Adapter extends RecyclerView.Adapter<TicketUpdate_Adapter.TicketHolder> implements Filterable {
    private ArrayList<GetSetValues> arrayList1;
    private GetSetValues getSetValues;
    private Context context;

    public TicketUpdate_Adapter(ArrayList<GetSetValues> arrayList1, GetSetValues getSetValues, Context context) {
        this.arrayList1 = arrayList1;
        this.getSetValues = getSetValues;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketUpdate_Adapter.TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_update_details, parent, false);
        return new TicketUpdate_Adapter.TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketUpdate_Adapter.TicketHolder holder, int position) {
        GetSetValues getSetValues = arrayList1.get(position);
        holder.tv_tic_no.setText(getSetValues.getUp_tic_id());
        holder.tv_tic_narr.setText(getSetValues.getUp_tic_narr());
        holder.tv_tic_file.setText(getSetValues.getUp_tic_file());
        holder.tv_tic_gen_by.setText(getSetValues.getUp_tic_genby());
        holder.tv_tic_gen_on.setText(getSetValues.getUp_tic_genon());
        holder.tic_close_layout.setVisibility(View.GONE);
        holder.tv_tic_close.setText(getSetValues.getUp_tic_close());
        holder.tv_tic_status.setText(getSetValues.getUp_tic_status());

        if (!TextUtils.isEmpty(getSetValues.getUp_tic_comment())) {
            holder.tv_tic_comm.setText(getSetValues.getUp_tic_comment()); //comment
        } else holder.lin_comment.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return arrayList1.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    //*********************************************************************************************
    public class TicketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_tic_no, tv_tic_narr, tv_tic_file, tv_tic_gen_by, tv_tic_gen_on, tv_tic_status, tv_tic_close, tv_tic_comm;
        LinearLayout tic_close_layout;
        LinearLayout lin_comment;

        private TicketHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            lin_comment = itemView.findViewById(R.id.comment);
            tic_close_layout = itemView.findViewById(R.id.tic_close_layout);
            tv_tic_no = itemView.findViewById(R.id.tic_no);
            tv_tic_narr = itemView.findViewById(R.id.tic_narr);
            tv_tic_file = itemView.findViewById(R.id.tic_file);
            tv_tic_gen_by = itemView.findViewById(R.id.tic_gen_by);
            tv_tic_gen_on = itemView.findViewById(R.id.tic_gen_on);
            tv_tic_status = itemView.findViewById(R.id.tic_status);
            tv_tic_close = itemView.findViewById(R.id.tic_close);
            tv_tic_comm = itemView.findViewById(R.id.txt_tic_comm);
            tic_close_layout.setVisibility(View.GONE);
        }

        //************************************************************************************************************************
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            GetSetValues getSetValues = arrayList1.get(pos);
            Intent intent = new Intent(context, ViewUpdateDeatils.class);
            intent.putExtra("TicketId", getSetValues.getUp_tic_id()); //1
            intent.putExtra("TicketGeneratedOn", getSetValues.getUp_tic_genon());
            intent.putExtra("TicketGeneratedBy", getSetValues.getUp_tic_genby());
            intent.putExtra("TicketStatus", getSetValues.getUp_tic_status());
            intent.putExtra("TicketNarration", getSetValues.getUp_tic_narr());
            intent.putExtra("TicketFile", getSetValues.getUp_tic_file());
            intent.putExtra("TicketClose", getSetValues.getUp_tic_close());
            intent.putExtra("Priority", getSetValues.getUp_tic_priority());
            intent.putExtra("Severity", getSetValues.getUp_tic_severity());
            intent.putExtra("Assign", getSetValues.getUp_tic_assign());
            intent.putExtra("Hescom_Tvd", getSetValues.getUp_tic_hescom());
            intent.putExtra("TicketTitle", getSetValues.getUp_tic_title());
            intent.putExtra("TicketDescription", getSetValues.getUp_tic_desc());
            intent.putExtra("TicketSubdivisionCode", getSetValues.getUp_subdivision_code());
//            intent.putExtra("TicketMrCode",getSetValues.getUp_tic_mr_code());
            intent.putExtra("TicketComment", getSetValues.getUp_tic_comment()); //15
            context.startActivity(intent);
            Toast.makeText(context, "Ticket Id is:" + " " + getSetValues.getUp_tic_id(), Toast.LENGTH_SHORT).show();

        }
    }
}
