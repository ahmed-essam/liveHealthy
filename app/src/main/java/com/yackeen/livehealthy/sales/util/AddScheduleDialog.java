package com.yackeen.livehealthy.sales.util;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yackeen.livehealthy.sales.R;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * Created by Abdelrhman Walid on 7/16/2017.
 */

public class AddScheduleDialog {
    AlertDialog dialog;
    FragmentActivity fragmentActivity;
    @BindView(R.id.days_spinner)
    Spinner daysSpinner;
    @BindView(R.id.from)
    EditText from;
    @BindView(R.id.to)
    EditText to;
    private int day;
    private String fromText, toText, dayText;
    private boolean resultOk;
    private onDismissListener listener;
    private List<String> days;

    public AddScheduleDialog(FragmentActivity fragmentActivity, onDismissListener listener) {
        this.fragmentActivity = fragmentActivity;
        this.listener = listener;
        init();
    }

    public String getDayText() {
        return dayText;
    }

    public int getDay() {
        return day;
    }

    public String getFromText() {
        return fromText;
    }

    public String getToText() {
        return toText;
    }

    public boolean isResultOk() {
        return resultOk;
    }

    private void init() {
        View view = LayoutInflater.from(fragmentActivity)
                .inflate(R.layout.dialog_add_schedule, null, false);
        dialog = new AlertDialog.Builder(fragmentActivity)
                .setTitle(R.string.add_schedule)
                .setView(view)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (listener != null)
                            listener.onDismiss(AddScheduleDialog.this);
                    }
                })
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resultOk = true;
                        day = daysSpinner.getSelectedItemPosition() + 1;
                        dayText = days.get(day - 1);
                    }
                }).create();

        ButterKnife.bind(this, view);

        daysSpinner.setAdapter(new ArrayAdapter<String>(
                fragmentActivity,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getDays()
        ));
    }

    public void show() {
        dialog.show();
    }

    private List<String> getDays() {
        if (days == null) {
            days = new ArrayList<>(7);
            days.add(fragmentActivity.getString(R.string.sunday));
            days.add(fragmentActivity.getString(R.string.monday));
            days.add(fragmentActivity.getString(R.string.tuesday));
            days.add(fragmentActivity.getString(R.string.wednesday));
            days.add(fragmentActivity.getString(R.string.thursday));
            days.add(fragmentActivity.getString(R.string.friday));
            days.add(fragmentActivity.getString(R.string.saturday));
        }
        return days;
    }


    private void setFromTime() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                from.setText(formatTimeForView(hourOfDay, minute, second));
                fromText = formatTimeForBackend(hourOfDay, minute, second);
            }
        }, false);
        timePickerDialog.setAccentColor(ContextCompat.getColor(fragmentActivity, R.color.colorPrimary));
        timePickerDialog.show(fragmentActivity.getFragmentManager(), "FROM_TIME");
    }

    private void setToTime() {
       TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                to.setText(formatTimeForView(hourOfDay, minute, second));
                toText = formatTimeForBackend(hourOfDay, minute, second);
            }
        }, false);
        timePickerDialog.setAccentColor(ContextCompat.getColor(fragmentActivity, R.color.colorPrimary));
        timePickerDialog.show(fragmentActivity.getFragmentManager(), "TO_TIME");
    }

    private String formatTimeForView(int hourOfDay, int minute, int second) {
        return FormatUtil.formatTime(String.valueOf(hourOfDay), String.valueOf(minute));
    }

    private String formatTimeForBackend(int hourOfDay, int minute, int second) {
        return String.format(Locale.US, "%02d:%02d:%02d", hourOfDay, minute, second);
    }

    @OnTouch({R.id.from, R.id.to})
    public boolean onViewClicked(View view, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP)
            return false;
        switch (view.getId()) {
            case R.id.from:
                setFromTime();
                break;
            case R.id.to:
                setToTime();
                break;
        }
        return true;
    }

    public interface onDismissListener {
        void onDismiss(AddScheduleDialog dialog);
    }
}
