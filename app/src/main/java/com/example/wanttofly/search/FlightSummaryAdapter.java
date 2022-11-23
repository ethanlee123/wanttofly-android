package com.example.wanttofly.search;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanttofly.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Collection;
import java.util.List;

public class FlightSummaryAdapter extends RecyclerView.Adapter<FlightSummaryAdapter.ViewHolder>{

    List<FlightSummaryData> flightSummariesList;
    IOnItemClickListener listener;
    Context context;

    // Possible improvement: use generics to limit collection to list and queue?
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
        PieChart pieChart;
        TextView flightStatus;
        TextView flightRating;

        public ViewHolder(View itemView) {
            super(itemView);
            pieChart = itemView.findViewById(R.id.pie_chart);
            airline = itemView.findViewById(R.id.tv_airline);
            destination = itemView.findViewById(R.id.tv_destination);
            flightNumber = itemView.findViewById(R.id.tv_flight_number);
            flightStatus = itemView.findViewById(R.id.tv_status);
            flightRating = itemView.findViewById(R.id.tv_flight_score);
        }

        public void bind(View view,
                         FlightSummaryData flightData,
                         IOnItemClickListener clickListener) {
            view.setOnClickListener(view1 -> clickListener.onItemClick(flightData));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_summary_search, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int flightRating = flightSummariesList.get(position).getFlightRating();
        holder.pieChart.clearChart(); // need to clear otherwise the pie slices will stack

        holder.pieChart.addPieSlice(new PieModel("", flightRating,
                Color.parseColor("#FE6DA8")));
        holder.pieChart.addPieSlice(new PieModel("", 100-flightRating,
                Color.parseColor("#56B7F1")));
        holder.flightRating.setText(String.valueOf(flightRating));

        holder.airline.setText(flightSummariesList.get(position).getAirlineName());
        holder.destination.setText(flightSummariesList.get(position).getArrivalAirport());
        String flightNum = context.getString(R.string.hashtag)
                + flightSummariesList.get(position).getFlightNumber();
        holder.flightNumber.setText(flightNum);
        holder.flightStatus.setText(flightSummariesList.get(position).getFlightStatus());

        holder.pieChart.startAnimation();

        holder.bind(holder.itemView, flightSummariesList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return flightSummariesList.size();
    }

    /**
     * Better way of update data set, search DiffUtil
     * @param newData List<FlightSummaryData>
     */
    public void updateData(Collection<FlightSummaryData> newData) {
        flightSummariesList.clear();
        flightSummariesList.addAll(newData);
        notifyDataSetChanged();
    }

    public interface IOnItemClickListener {
        void onItemClick(final FlightSummaryData flightSummaryData);
    }
}
