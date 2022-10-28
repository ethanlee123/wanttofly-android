package com.example.wanttofly.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanttofly.R;

import java.util.List;

public class FlightSummaryAdapter extends RecyclerView.Adapter<FlightSummaryAdapter.ViewHolder>{

    List<String> flightSummariesList;
    IOnItemClickListener listener;
    public FlightSummaryAdapter(List<String> flightSummariesList, IOnItemClickListener listener) {
        this.flightSummariesList = flightSummariesList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView airline;

        public ViewHolder(View itemView) {
            super(itemView);

            airline = itemView.findViewById(R.id.tv_airline);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_summary_search, parent, false);

        view.setOnClickListener(view1 -> listener.onItemClick());

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.airline.setText(flightSummariesList.get(position));
    }

    @Override
    public int getItemCount() {
        return flightSummariesList.size();
    }

    public interface IOnItemClickListener {
        void onItemClick();
    }
}
