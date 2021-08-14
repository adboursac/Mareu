package com.lamzone.mareu.ui;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimePicker {

    public static void setTimePickerOnTextInput(TextInputEditText textInput, FragmentManager fragmentManager) {
        textInput.setOnClickListener(v -> {
            openTimePicker(textInput, fragmentManager);
        });
    }

    public static void openTimePicker(TextInputEditText textInput, FragmentManager fragmentManager) {
        LocalTime time;
        String text = textInput.getText().toString();
        try {
            time = LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm"));
        }
        catch (Exception e) {
            time = LocalTime.now();
        }

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(time.getHour())
                .setMinute(time.getMinute())
                .build();

        picker.show(fragmentManager, null);

        picker.addOnPositiveButtonClickListener(v -> {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("[H:m][H:mm][HH:m][hh:mm]");
            LocalTime pickedTime = LocalTime.parse(picker.getHour()+":"+picker.getMinute(),
                    parser);
            String timeText = pickedTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            textInput.setText(timeText);
        });
    }
}
