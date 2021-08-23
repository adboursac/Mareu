package com.lamzone.mareu.ui;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lamzone.mareu.data.meeting.MeetingTimeHelper;

import java.time.LocalTime;

public class TimePicker {

    public static void setTimePickerOnTextInput(TextInputEditText textInput, FragmentManager fragmentManager) {
        textInput.setOnClickListener(v -> openTimePicker(textInput, fragmentManager));
    }

    public static void openTimePicker(TextInputEditText textInput, FragmentManager fragmentManager) {
        LocalTime time = MeetingTimeHelper.toTime(textInput.getText().toString(), LocalTime.of(0, 0));

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(time.getHour())
                .setMinute(time.getMinute())
                .build();

        picker.show(fragmentManager, null);

        picker.addOnPositiveButtonClickListener(v -> {
            LocalTime pickedTime = LocalTime.of(picker.getHour(), picker.getMinute());
            textInput.setText(MeetingTimeHelper.toString(pickedTime));
        });
    }
}
