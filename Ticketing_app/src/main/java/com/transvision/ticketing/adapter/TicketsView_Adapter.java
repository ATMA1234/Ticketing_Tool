package com.transvision.ticketing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.transvision.ticketing.R;
import com.transvision.ticketing.ViewTicketDetails;
import com.transvision.ticketing.extra.GetSetValues;
import java.util.ArrayList;

public class TicketsView_Adapter extends RecyclerView.Adapter<TicketsView_Adapter.TicketHolder> implements Filterable {
    private ArrayList<GetSetValues> arrayList;
    private ArrayList<GetSetValues> filteredList;
    private GetSetValues getSetValues;
    private Context context;

    public TicketsView_Adapter(ArrayList<GetSetValues> arrayList, GetSetValues getSetValues, Context context) {
        this.arrayList = arrayList;
        this.filteredList = arrayList;
        this.getSetValues = getSetValues;
        this.context = context;
    }

    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_view_details, parent, false);
        return new TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        GetSetValues getSetValues = filteredList.get(position);
        holder.tv_tic_no.setText(getSetValues.getTic_id());
        holder.tv_tic_narr.setText(getSetValues.getTic_narr());
        holder.tv_tic_file.setText(getSetValues.getTic_file());
        holder.tv_tic_gen_by.setText(getSetValues.getTic_genby());
        holder.tv_tic_gen_on.setText(getSetValues.getTic_genon());
        holder.tic_close_layout.setVisibility(View.GONE);
        holder.tv_tic_close.setText(getSetValues.getTic_close());
        holder.tv_tic_status.setText(getSetValues.getTic_status());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    //*********************************************** Filter *************************************************************
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty())
                    filteredList = arrayList;
                else {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        GetSetValues getSetValues = arrayList.get(i);
                        if (getSetValues.getTic_id().contains(search)) {
                            filterlist.add(getSetValues);
                        }
                    }
                    filteredList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<GetSetValues>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //********************************* TicketHolder ************************************************************
    public class TicketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_tic_no, tv_tic_narr, tv_tic_file, tv_tic_gen_by, tv_tic_gen_on, tv_tic_status, tv_tic_close;
        LinearLayout tic_close_layout;

        public TicketHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tic_close_layout = itemView.findViewById(R.id.tic_close_layout);
            tv_tic_no = itemView.findViewById(R.id.tic_no);
            tv_tic_narr = itemView.findViewById(R.id.tic_narr);
            tv_tic_file = itemView.findViewById(R.id.tic_file);
            tv_tic_gen_by = itemView.findViewById(R.id.tic_gen_by);
            tv_tic_gen_on = itemView.findViewById(R.id.tic_gen_on);
            tv_tic_status = itemView.findViewById(R.id.tic_status);
            tv_tic_close = itemView.findViewById(R.id.tic_close);
            tic_close_layout.setVisibility(View.GONE);
        }

        //*****************************************************************************************************************************
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            getSetValues = filteredList.get(pos);
            Intent intent = new Intent(context, ViewTicketDetails.class);
            intent.putExtra("TicketId", getSetValues.getTic_id());
            Log.d("debug", "TicketId" + getSetValues.getTic_id());
            intent.putExtra("TicketGeneratedOn", getSetValues.getTic_genon());
            intent.putExtra("TicketGeneratedBy", getSetValues.getTic_genby());
            intent.putExtra("TicketStatus", getSetValues.getTic_status());
            intent.putExtra("TicketNarration", getSetValues.getTic_narr());
            intent.putExtra("TicketFile", getSetValues.getTic_file());
            intent.putExtra("TicketClose", getSetValues.getTic_close());
            intent.putExtra("Priority", getSetValues.getTic_priority());
            intent.putExtra("Severity", getSetValues.getTic_severity());
            intent.putExtra("Assign", getSetValues.getTic_assign());
            intent.putExtra("Hescom_Tvd", getSetValues.getTic_hescom());
            intent.putExtra("TicketTitle", getSetValues.getTic_title());
            intent.putExtra("TicketDescription", getSetValues.getTic_desc());
            intent.putExtra("TicketSubdivisionCode", getSetValues.getSubdivision_code());
            intent.putExtra("TicketMrCode", getSetValues.getTic_mr_code());
            Log.d("debug", "Selected MR: " + getSetValues.getTic_mr_code());
            intent.putExtra("TicketComment", getSetValues.getTic_comment());
            context.startActivity(intent);
            Toast.makeText(context, "Ticket Id is:" + " " + getSetValues.getTic_id(), Toast.LENGTH_SHORT).show();
        }
    }

    //********************************************************************************************************************
    public void remove(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
    }
}
