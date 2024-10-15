package com.example.on2txgit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText edtName, edtTime;
    private Spinner spinnerDay;
    private RadioGroup rgBuoi;
    private CheckBox cbLoop;
    private ListView lvBaoThuc;
    private ArrayList<String> dsBaoThuc;
    private ArrayAdapter<String> adapter;
    private Button btnAdd, btnTime, btnUpdate, btnTong;
    private int selectedIndex = -1;
    private TextView viewTong;
    private int tongBaoThuc = 0;
    private int soLanSang = 0;
    private int soLanChieu = 0;
    private int soLanLap = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.name);
        spinnerDay = findViewById(R.id.spinnerDay);
        edtTime = findViewById(R.id.time);
        rgBuoi = findViewById(R.id.grBuoi);
        cbLoop = findViewById(R.id.loop);
        lvBaoThuc = findViewById(R.id.lv);
        btnAdd = findViewById(R.id.btnAdd);
        btnTime = findViewById(R.id.btnTime);
        btnTong = findViewById(R.id.btnTong);
        btnUpdate = findViewById(R.id.btnUpdate);
        viewTong = findViewById(R.id.textTong);

        rgBuoi.check(R.id.rbSang);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);

        dsBaoThuc = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsBaoThuc);
        lvBaoThuc.setAdapter(adapter);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog timePicker = new TimePickerDialog(MainActivity.this,
//                        (view, hourOfDay, minute) -> edtTime.setText(hourOfDay + ":" + (minute < 10 ? "0" + minute : minute)),
//                        cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                        (view, hourOfDay, minute) -> {
                        int hour = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
                        if(hourOfDay < 12){
                            rgBuoi.check(R.id.rbSang);
                        }else if(hourOfDay > 12){
                            rgBuoi.check(R.id.rbChieu);
                        }
                        if (hour == 0) hour = 12;
                        String formattedMinute = (minute < 10) ? "0" + minute : String.valueOf(minute);

                        edtTime.setText(hour + ":" + formattedMinute);
                        edtTime.setSelection(edtTime.getText().length());

                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);


                timePicker.setTitle("Chọn giờ");
                timePicker.show();
            }
        });
        // Nhap so gio tu ban phim
//        edtTime.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    if (s.length() == 2) {
//                        // Chuyển con trỏ đến vị trí nhập phút
//                        edtTime.setSelection(s.length() + 1); // +1 để nhảy qua dấu ':'
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Định dạng thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });



        lvBaoThuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
                String[] alarmDetails = dsBaoThuc.get(position).split(" - ");

                edtName.setText(alarmDetails[0]);
                edtTime.setText(alarmDetails[2]);

                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerDay.getAdapter();
                spinnerDay.setSelection(adapter.getPosition(alarmDetails[1]));

                if (alarmDetails[3].equals("Sáng")) {
                    rgBuoi.check(R.id.rbSang);
                } else {
                    rgBuoi.check(R.id.rbChieu);
                }

                cbLoop.setChecked(alarmDetails[4].equals("Lap lai"));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baoThuc = createAlarmString();
                if (baoThuc != null) {
                    dsBaoThuc.add(baoThuc);
                    adapter.notifyDataSetChanged();

                    int selectTime =rgBuoi.getCheckedRadioButtonId();
                    if(selectTime == R.id.rbSang){
                        soLanSang++;
                    }else if(selectTime == R.id.rbChieu){
                        soLanChieu++;
                    }

                    int selectLoop = cbLoop.isChecked()?1:0;
                    soLanLap+=selectLoop;

                    clear();

                    tongBaoThuc++;
                    viewTong.setText("Tong bao thuc: " + tongBaoThuc);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIndex != -1) {
                    String updatedBaoThuc = createAlarmString();
                    if (updatedBaoThuc != null) {
                        dsBaoThuc.set(selectedIndex, updatedBaoThuc);
                        adapter.notifyDataSetChanged();
                        clear();
                        selectedIndex = -1;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Chọn báo thức để cập nhật", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnTong.setOnClickListener(view -> {
            String tongKet ="Tổng số lần đặt là: "+tongBaoThuc+" \n"+
                    "Sáng: "+soLanSang+"\n"+
                    "Chiều: "+soLanChieu+" \n"+
                    "Số lần lặp: "+soLanLap+" - "
                    +(cbLoop.isChecked()?" Có ":" Không ");// Không cần thiết cái này
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Tổng Kết")
                    .setMessage(tongKet)
                    .setPositiveButton("OK",null).show();
        });
    }

    private String createAlarmString() {
        String name = edtName.getText().toString();
        String day = spinnerDay.getSelectedItem().toString();
        String time = edtTime.getText().toString();

        if (name.isEmpty() || time.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return null;
        }

//        if (!time.matches("\\d{1,2}:\\d{2}")) {
//            Toast.makeText(MainActivity.this, "Vui lòng nhập thời gian theo định dạng HH:MM", Toast.LENGTH_SHORT).show();
//            return null;
//        }

        int selectedBuoiId = rgBuoi.getCheckedRadioButtonId();
        if (selectedBuoiId == -1) {
            Toast.makeText(MainActivity.this, "Vui lòng chọn buổi", Toast.LENGTH_SHORT).show();
            return null;
        }
        RadioButton selectedBuoi = findViewById(selectedBuoiId);
        String buoi = selectedBuoi.getText().toString();

        String loop = cbLoop.isChecked() ? "Lap lai" : "Khong lap lai";

        return name + " - " + day + " - " + time + " - " + buoi + " - " + loop;
    }

    void clear() {
        edtName.setText("");
        edtTime.setText("");
        spinnerDay.setSelection(0);
//        rgBuoi.clearCheck();
        cbLoop.setChecked(false);
    }
}
