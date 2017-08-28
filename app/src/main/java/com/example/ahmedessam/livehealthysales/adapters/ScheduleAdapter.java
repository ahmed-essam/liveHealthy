package com.example.ahmedessam.livehealthysales.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.models.Day;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<Day> days;
    private boolean hideDelete;
    private onItemRemovedListener listener;
    private String[] dayesArray;

    public ScheduleAdapter(List<Day> days, onItemRemovedListener listener, Context context) {
        this.days = days;
        this.listener = listener;
        dayesArray= new String[]{context.getResources().getString(R.string.sunday),
                context.getResources().getString(R.string.monday),
                context.getResources().getString(R.string.tuesday),
                context.getResources().getString(R.string.wednesday),
                context.getResources().getString(R.string.thursday),
                context.getResources().getString(R.string.friday),
                context.getResources().getString(R.string.saturday)};
    }

    public void setHideDelete(boolean hideDelete) {
        this.hideDelete = hideDelete;
    }

    public void setDays(List<Day> days) {
        this.days = days;
        notifyDataSetChanged();
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.times_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface onItemRemovedListener {
        void onItemRemoved();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.appointment)
        TextView details;
        @BindView(R.id.cancelButton)
        ImageButton cancelButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(int position) {
            Day item = days.get(position);
            if (item.getDay()==null){
                int dayaID= item.getDayId()+1;
                day.setText(dayesArray[dayaID]);
            }else {
                day.setText(item.getDay());
            }
            details.setText(
                    String.format("%s %s %s %s",
                            itemView.getContext().getString(R.string.from), formatTime(item.getFromHour()),
                            itemView.getContext().getString(R.string.to), formatTime(item.getToHour()))
            );
            if (hideDelete) {
                cancelButton.setVisibility(View.GONE);
            } else {
                cancelButton.setVisibility(View.VISIBLE);
            }
        }

        @OnClick(R.id.cancelButton)
        void onCancelClicked() {
            int position = getLayoutPosition();
            days.remove(position);
            notifyItemRemoved(position);
            if (listener != null)
                listener.onItemRemoved();
        }
    }
}
