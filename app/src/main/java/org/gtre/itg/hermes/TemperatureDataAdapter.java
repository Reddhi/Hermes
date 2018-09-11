package org.gtre.itg.hermes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TemperatureDataAdapter  extends RecyclerView.Adapter<TemperatureDataAdapter.ViewHolder> {
    private ArrayList<SensorData> mDataSet;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView temperatureView;
        TextView dateTimeView;

        ViewHolder(LinearLayout view) {
            super(view);
            temperatureView = view.findViewById(R.id.temperature_value);
            dateTimeView = view.findViewById(R.id.date_time_value);
        }
    }

    TemperatureDataAdapter(ArrayList<SensorData> myDataSet) {
        mDataSet = myDataSet;
    }

    @Override
    @NonNull public TemperatureDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.temperature_list_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("00.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String temperatureText = df.format(mDataSet.get(position).getTemperature())+"  \u00b0C";
        holder.temperatureView.setText(temperatureText);
        holder.dateTimeView.setText(mDataSet.get(position).getDatetime());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}