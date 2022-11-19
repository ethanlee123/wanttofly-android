package com.example.wanttofly.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanttofly.R;

import java.util.Collection;
import java.util.List;

public class FlightSummaryAdapter extends RecyclerView.Adapter<FlightSummaryAdapter.ViewHolder>{

    List<FlightSummaryData> flightSummariesList;
    IOnItemClickListener listener;
    Context context;

    public FlightSummaryAdapter(Context context,
                                Collection<FlightSummaryData> flightSummariesList,
                                IOnItemClickListener listener) {
        this.context = context;
        this.flightSummariesList = (List<FlightSummaryData>) flightSummariesList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView airline;
        TextView destination;
        TextView flightNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            airline = itemView.findViewById(R.id.tv_airline);
            destination = itemView.findViewById(R.id.tv_destination);
            flightNumber = itemView.findViewById(R.id.tv_flight_number);
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
        holder.airline.setText(flightSummariesList.get(position).getAirlineName());
        holder.destination.setText(flightSummariesList.get(position).getArrivalAirport());
        String flightNum = context.getString(R.string.hashtag)
                + flightSummariesList.get(position).getFlightNumber();
        holder.flightNumber.setText(flightNum);
    }

    @Override
    public int getItemCount() {
        return flightSummariesList.size();
    }

    /**
     * Better way of update data set, search DiffUtil
     * @param newData
     */
    public void updateData(List<FlightSummaryData> newData) {
        flightSummariesList.clear();
        flightSummariesList.addAll(newData);
        notifyDataSetChanged();
    }

    public interface IOnItemClickListener {
        void onItemClick();
    }
}
