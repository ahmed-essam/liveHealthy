package com.example.ahmedessam.livehealthysales.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.models.Appointment;
import com.example.ahmedessam.livehealthysales.models.Day;
import com.example.ahmedessam.livehealthysales.util.UserHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmed essam on 09/08/2017.
 */

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.ViewHolder> {

    List<Day> dayList;
    private Context context;


    public TimesAdapter(Context context) {
        this.context = context;
        dayList = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.times_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(dayList.get(position));
    }

    public void addAll(List<Day> days) {
        this.dayList.clear();
        this.dayList.addAll(days);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;

        TextView appointments;
        ImageButton delete;


        public ViewHolder(View itemView) {
            super(itemView);
            dayText =(TextView)itemView.findViewById(R.id.day);
            appointments=(TextView)itemView.findViewById(R.id.appointment);
            delete = (ImageButton) itemView.findViewById(R.id.cancelButton);

        }

        public void bindView(Day day) {
            dayText.setText(day.getDay());
           appointments.setText(
                    String.format("%s %s %s %s",
                            itemView.getContext().getString(R.string.from), formatTime(day.getFromHour()),
                            itemView.getContext().getString(R.string.to), formatTime(day.getToHour()))
            );

        }

        public String createTime(String fromTime, String toTime) {


            String s =   String.format("",itemView.getContext().getString(R.string.from), formatTime(fromTime),
                            itemView.getContext().getString(R.string.to), formatTime(toTime));
            return s;
        }


    }
    private String formatTime(String hour) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Date date = null;
        try {
            date = format.parse(hour);
            return new SimpleDateFormat("hh:mm a", Locale.US).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
