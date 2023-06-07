package com.obushko.timemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Fragment1 extends Fragment {

    private int pageNumber;
    private Button buttonAlarm;
    private View rootView;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public Fragment1() {

    }


    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_1, container, false);
        buttonAlarm = rootView.findViewById(R.id.buttonAlarm);


        initListeners();

        return rootView;
    }

    private void initListeners(){
        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select the time")
                        .build();

                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                        calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());

                        AlarmManager alarmManager = (AlarmManager)  requireContext().getSystemService(Context.ALARM_SERVICE);
                        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),getAlarmInfoPendingIntent());
                        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
                        Toast.makeText(requireContext(), "Alarm set on " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                    }

                });
                materialTimePicker.show(getParentFragmentManager(), "tag_picker");
            }
        });
    }
    private PendingIntent getAlarmInfoPendingIntent(){
        Intent alarmInfoIntent = new Intent(requireContext(), requireActivity().getClass());
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(requireContext(), 0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getAlarmActionPendingIntent(){
        Intent intent = new Intent(requireContext(), AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(requireContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}