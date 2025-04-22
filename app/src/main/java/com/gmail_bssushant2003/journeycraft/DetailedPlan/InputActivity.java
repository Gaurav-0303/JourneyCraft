package com.gmail_bssushant2003.journeycraft.DetailedPlan;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail_bssushant2003.journeycraft.R;
import com.google.android.material.textfield.TextInputLayout;
import com.qandeelabbassi.dropsy.DropDownItem;
import com.qandeelabbassi.dropsy.DropDownView;

public class InputActivity extends AppCompatActivity {

    ImageView help;

    DropDownView dropdown;

    Button startTime;

    Button endTime;
    TextView startTimetv;
    TextView endTimetv;

    String startTimeString;
    int placeInt;
    String endTimeString;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.white));
        }


//        help = findViewById(R.id.image_view_help);
        dropdown = findViewById(R.id.dropdown_places);
        startTime = findViewById(R.id.start_time_button);
        endTime = findViewById(R.id.end_time_button);
        startTimetv = findViewById(R.id.start_time_label);
        endTimetv = findViewById(R.id.end_time_label);

//
//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(InputActivity.this, ChatBotActivity.class);
//                startActivity(intent);
//            }
//        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartTimePickerDialog();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndTimePickerDialog();
            }
        });

        dropdown.setItemClickListener(new DropDownView.ItemClickListener() {
            @Override
            public void onItemClick(int i, @NonNull DropDownItem dropDownItem) {
//                Toast.makeText(InputActivity.this,dropDownItem.getText() + "Clicked at Index " + i,Toast.LENGTH_SHORT).show();
                placeInt = i;
            }
        });

        findViewById(R.id.add_button_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(placeInt >= 0 && startTimeString != null && endTimeString != null){
                    Intent intent = new Intent(InputActivity.this, PlanActivity.class);
                    intent.putExtra("Place", placeInt);
                    intent.putExtra("StartTime", startTimeString.substring(0, 2));
                    intent.putExtra("EndTime", endTimeString.substring(0,2));
                    startActivity(intent);
                }else{
                    Toast.makeText(InputActivity.this,"Please select Start and End time",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void openStartTimePickerDialog() {
        int initialHourOfDay = 15; // Default initial hour for start time
        int initialMinute = 0; // Default initial minute

        TimePickerDialog dialog = new TimePickerDialog(this,R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                // Convert hours and minutes to a formatted string (e.g., "15:30")
                String selectedTime = String.format("%02d:%02d", hours, minutes);
                startTimeString = selectedTime;

                // Set the selected start time to the TextView
                startTimetv.setText(selectedTime);
            }
        }, initialHourOfDay, initialMinute, true);

        dialog.show(); // Show the dialog
    }

    private void openEndTimePickerDialog() {
        int initialHourOfDay = 0; // Default initial hour for end time
        int initialMinute = 0; // Default initial minute

        TimePickerDialog dialog = new TimePickerDialog(this,R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                // Convert hours and minutes to a formatted string (e.g., "15:30")
                String selectedTime = String.format("%02d:%02d", hours, minutes);
                endTimeString = selectedTime;
                // Set the selected end time to the TextView
                endTimetv.setText(selectedTime);
            }
        }, initialHourOfDay, initialMinute, true);

        dialog.show(); // Show the dialog
    }

}