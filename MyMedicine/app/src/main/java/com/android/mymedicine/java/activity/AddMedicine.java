package com.android.mymedicine.java.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymedicine.R;
import com.android.mymedicine.java.adapter.TimeSlotListAdapter;
import com.android.mymedicine.java.customview.PopupWindow;
import com.android.mymedicine.java.customview.UploadPickerPopUp;
import com.android.mymedicine.java.db.DbService;
import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.service.MyMedicineService;
import com.android.mymedicine.java.utils.Utils;
import com.android.mymedicine.java.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import static com.android.mymedicine.java.customview.UploadPickerPopUp.CAMERA_REQUEST;
import static com.android.mymedicine.java.customview.UploadPickerPopUp.PICK_IMAGE_REQUEST;
import static com.android.mymedicine.java.utils.Utils.convertDateToMilliSeconds;

/**
 * Created by razin on 25/11/17.
 */

public class AddMedicine extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    Spinner sp_add_medicine_unit;
    Spinner sp_add_medicine_frequency;
    Spinner sp_add_medicine_times;
    RecyclerView rv_add_medicine_time_slot;
    TimeSlotListAdapter timeSlotListAdapter;
    DatePickerDialog dpd;
    public Calendar selectedDateCalendar = new GregorianCalendar();

    boolean isSelectedStartDate = false;
    boolean isSelectedSunday = false;
    boolean isSelectedMonday = false;
    boolean isSelectedTuesday = false;
    boolean isSelectedWednesday = false;
    boolean isSelectedThursday = false;
    boolean isSelectedFriday = false;
    boolean isSelectedSaturday = false;

    private android.widget.PopupWindow showPopup;
    public Uri selectedImageUri;
    public static Bitmap thumbnail;
    public byte[] compressedImage;
    ImageView img_add_medicine_image;

    CardView cv_add_medicine_days;
    LinearLayout ll_add_medicine_time_selection;
    public static MedicineModel medicineModel;

    TextInputLayout til_add_medicine_medication_name;
    TextInputLayout til_add_medicine_dosage;
    TextInputLayout til_add_medicine_dosage_type;

    TextInputEditText et_add_medicine_medication_name;
    TextInputEditText et_add_medicine_dosage;
    TextInputEditText et_add_medicine_dosage_type;

    TextView tv_add_medicine_start_date;
    TextView tv_add_medicine_end_date;

    TextView tv_add_medicine_day_sunday;
    TextView tv_add_medicine_day_monday;
    TextView tv_add_medicine_day_tuesday;
    TextView tv_add_medicine_day_wednesday;
    TextView tv_add_medicine_day_thursday;
    TextView tv_add_medicine_day_friday;
    TextView tv_add_medicine_day_saturday;

    Button bt_add_medicine_done;

    String unitValue = "";
    String frequencyValue = "";
    String imagePath = "";
    int howManyTimesValue = 0;

    private final int PERMISSION_REQUEST_CODE = 101;
    private final int STORAGE_PERMISSION_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        medicineModel = new MedicineModel();
        requestPermission();
        setInitUI();
        setRegisterUI();
        setTextWatcher();
        setUnitAdapter();
        setFrequencyAdapter();
        setHowManyTimesAdapter();
        iniDateTimePicker();
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                break;
        }
    }


    private void setTextWatcher() {
        et_add_medicine_medication_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_add_medicine_medication_name.getText().length() == 0) {
                    til_add_medicine_medication_name.setError(getResources().getString(R.string.please_provide_medicine_name));
                } else {
                    til_add_medicine_medication_name.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_add_medicine_dosage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_add_medicine_dosage.getText().length() == 0) {
                    til_add_medicine_dosage.setError(getResources().getString(R.string.please_provide_dosage));
                } else {
                    til_add_medicine_dosage.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_add_medicine_dosage_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_add_medicine_dosage_type.getText().length() == 0) {
                    til_add_medicine_dosage_type.setError(getResources().getString(R.string.please_provide_dosage_type));
                } else {
                    til_add_medicine_dosage_type.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(12);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void iniDateTimePicker() {
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(getResources().getColor(R.color.bg_button));
        dpd.setOkColor(Color.BLACK);
        dpd.setCancelColor(Color.BLACK);
        dpd.setMinDate(now);
        dpd.vibrate(false);
    }

    private void setRegisterUI() {
        tv_add_medicine_start_date.setOnClickListener(this);
        tv_add_medicine_end_date.setOnClickListener(this);
        tv_add_medicine_day_sunday.setOnClickListener(this);
        tv_add_medicine_day_monday.setOnClickListener(this);
        tv_add_medicine_day_tuesday.setOnClickListener(this);
        tv_add_medicine_day_wednesday.setOnClickListener(this);
        tv_add_medicine_day_thursday.setOnClickListener(this);
        tv_add_medicine_day_friday.setOnClickListener(this);
        tv_add_medicine_day_saturday.setOnClickListener(this);
        img_add_medicine_image.setOnClickListener(this);
        findViewById(R.id.img_actionbar_back).setOnClickListener(this);
        Utils.buttonTouch(bt_add_medicine_done);
        bt_add_medicine_done.setOnClickListener(this);

    }

    private void setInitUI() {
        findViewById(R.id.img_actionbar_menu).setVisibility(View.GONE);
        findViewById(R.id.img_actionbar_back).setVisibility(View.VISIBLE);
        TextView tv_actionbar_title = findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText(getResources().getString(R.string.add_medicine));
        sp_add_medicine_unit = findViewById(R.id.sp_add_medicine_unit);
        sp_add_medicine_frequency = findViewById(R.id.sp_add_medicine_frequency);
        sp_add_medicine_times = findViewById(R.id.sp_add_medicine_times);
        rv_add_medicine_time_slot = findViewById(R.id.rv_add_medicine_time_slot);
        tv_add_medicine_start_date = findViewById(R.id.tv_add_medicine_start_date);
        tv_add_medicine_end_date = findViewById(R.id.tv_add_medicine_end_date);
        tv_add_medicine_day_sunday = findViewById(R.id.tv_add_medicine_day_sunday);
        tv_add_medicine_day_monday = findViewById(R.id.tv_add_medicine_day_monday);
        tv_add_medicine_day_tuesday = findViewById(R.id.tv_add_medicine_day_tuesday);
        tv_add_medicine_day_wednesday = findViewById(R.id.tv_add_medicine_day_wednesday);
        tv_add_medicine_day_thursday = findViewById(R.id.tv_add_medicine_day_thursday);
        tv_add_medicine_day_friday = findViewById(R.id.tv_add_medicine_day_friday);
        tv_add_medicine_day_saturday = findViewById(R.id.tv_add_medicine_day_saturday);
        img_add_medicine_image = findViewById(R.id.img_add_medicine_image);
        cv_add_medicine_days = findViewById(R.id.cv_add_medicine_days);
        ll_add_medicine_time_selection = findViewById(R.id.ll_add_medicine_time_selection);
        til_add_medicine_medication_name = findViewById(R.id.til_add_medicine_medication_name);
        til_add_medicine_dosage = findViewById(R.id.til_add_medicine_dosage);
        til_add_medicine_dosage_type = findViewById(R.id.til_add_medicine_dosage_type);
        et_add_medicine_medication_name = findViewById(R.id.et_add_medicine_medication_name);
        et_add_medicine_dosage = findViewById(R.id.et_add_medicine_dosage);
        et_add_medicine_dosage_type = findViewById(R.id.et_add_medicine_dosage_type);
        tv_add_medicine_start_date = findViewById(R.id.tv_add_medicine_start_date);
        tv_add_medicine_end_date = findViewById(R.id.tv_add_medicine_end_date);
        bt_add_medicine_done = findViewById(R.id.bt_add_medicine_done);
    }

    private void setTimeSlotAdapter(int timesPerDay) {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(AddMedicine.this);
        rv_add_medicine_time_slot.setLayoutManager(LayoutManager);
        rv_add_medicine_time_slot.setItemAnimator(new DefaultItemAnimator());
        rv_add_medicine_time_slot.addItemDecoration(new VerticalSpaceItemDecoration(1));
        timeSlotListAdapter = new TimeSlotListAdapter(AddMedicine.this, timesPerDay);
        rv_add_medicine_time_slot.setAdapter(timeSlotListAdapter);
        ll_add_medicine_time_selection.setVisibility(View.VISIBLE);
    }


    private void setHowManyTimesAdapter() {
        final List<String> howmanytimesList = new ArrayList<String>();
        howmanytimesList.add(getResources().getString(R.string.how_many_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.one_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.two_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.three_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.four_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.five_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.six_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.seven_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.eight_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.nine_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.ten_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.eleven_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.twelve_times_a_day));
        howmanytimesList.add(getResources().getString(R.string.twentyfour_times_a_day));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, howmanytimesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_add_medicine_times.setAdapter(dataAdapter);
        sp_add_medicine_times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinner.setVisibility(View.GONE);
                String item = parent.getItemAtPosition(position).toString();
                if (!item.isEmpty() && !item.equalsIgnoreCase(getResources().getString(R.string.how_many_times_a_day))) {
                    setTimeSlotAdapter(position);
                    howManyTimesValue = position;
                } else {
                    ll_add_medicine_time_selection.setVisibility(View.GONE);
                    howManyTimesValue = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                howManyTimesValue = 0;
            }

        });
    }

    private void setFrequencyAdapter() {
        final List<String> frequencyList = new ArrayList<String>();
        frequencyList.add(getResources().getString(R.string.frequency));
        frequencyList.add(getResources().getString(R.string.as_needed));
        frequencyList.add(getResources().getString(R.string.every_day));
        frequencyList.add(getResources().getString(R.string.specific_days));
        frequencyList.add(getResources().getString(R.string.days_interval));
        frequencyList.add(getResources().getString(R.string.birth_control_cycle));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_add_medicine_frequency.setAdapter(dataAdapter);
        sp_add_medicine_frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinner.setVisibility(View.GONE);
                String item = parent.getItemAtPosition(position).toString();
                if (!item.isEmpty() && !item.equalsIgnoreCase(getResources().getString(R.string.specific_days))) {
                    isSelectedSunday = true;
                    isSelectedMonday = true;
                    isSelectedTuesday = true;
                    isSelectedWednesday = true;
                    isSelectedThursday = true;
                    isSelectedFriday = true;
                    isSelectedSaturday = true;
                    cv_add_medicine_days.setVisibility(View.GONE);
                    frequencyValue = item;
                } else if (item.equalsIgnoreCase(getResources().getString(R.string.specific_days))) {
                    frequencyValue = item;
                    isSelectedSunday = false;
                    isSelectedMonday = false;
                    isSelectedTuesday = false;
                    isSelectedWednesday = false;
                    isSelectedThursday = false;
                    isSelectedFriday = false;
                    isSelectedSaturday = false;
                    cv_add_medicine_days.setVisibility(View.VISIBLE);
                } else {
                    isSelectedSunday = false;
                    isSelectedMonday = false;
                    isSelectedTuesday = false;
                    isSelectedWednesday = false;
                    isSelectedThursday = false;
                    isSelectedFriday = false;
                    isSelectedSaturday = false;
                    frequencyValue = "";
                    cv_add_medicine_days.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                frequencyValue = "";
            }

        });
    }

    private void setUnitAdapter() {
        final List<String> unitList = new ArrayList<String>();
        unitList.add(getResources().getString(R.string.Unit_optional));
        unitList.add(getResources().getString(R.string.Pills));
        unitList.add(getResources().getString(R.string.cc));
        unitList.add(getResources().getString(R.string.ml));
        unitList.add(getResources().getString(R.string.gr));
        unitList.add(getResources().getString(R.string.mg));
        unitList.add(getResources().getString(R.string.Drops));
        unitList.add(getResources().getString(R.string.Pieces));
        unitList.add(getResources().getString(R.string.Puffs));
        unitList.add(getResources().getString(R.string.Units));
        unitList.add(getResources().getString(R.string.teaspoon));
        unitList.add(getResources().getString(R.string.tablespoon));
        unitList.add(getResources().getString(R.string.patch));
        unitList.add(getResources().getString(R.string.mcg));
        unitList.add(getResources().getString(R.string.iu));
        unitList.add(getResources().getString(R.string.meq));
        unitList.add(getResources().getString(R.string.Carton));
        unitList.add(getResources().getString(R.string.Spray));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_add_medicine_unit.setAdapter(dataAdapter);
        sp_add_medicine_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (!item.isEmpty() && !item.equalsIgnoreCase(getResources().getString(R.string.Unit_optional))) {
                    unitValue = item;
                } else {
                    unitValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                unitValue = "";
            }

        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mm;
        String dd;
        if ((monthOfYear + 1) < 10) {
            mm = "0" + (monthOfYear + 1);
        } else {
            mm = "" + (monthOfYear + 1);
        }
        if (dayOfMonth < 10) {
            dd = "0" + dayOfMonth;
        } else {
            dd = "" + dayOfMonth;
        }
        String date = dd + "-" + mm + "-" + year;
        if (isSelectedStartDate) {
            selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedDateCalendar.set(Calendar.MONTH, monthOfYear + 1 - 1);
            selectedDateCalendar.set(Calendar.YEAR, year);
            tv_add_medicine_start_date.setText(date);
            tv_add_medicine_start_date.setError(null);
            isSelectedStartDate = false;
        } else {
            tv_add_medicine_end_date.setText(date);
            tv_add_medicine_end_date.setError(null);
        }
    }

    private void showAttachmentPopup(View view) {
        String[] menuText = getResources().getStringArray(R.array.attachment);
        int[] images = {R.drawable.ic_camera_black_24dp, R.drawable.ic_photo_library_black_24dp};
        GridView menuGrid;
        showPopup = PopupWindow.getPopup(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.upload_picker_layout, null);
        menuGrid = (GridView) popupView.findViewById(R.id.grid_layout);
        UploadPickerPopUp adapter = new UploadPickerPopUp(this, menuText, images);
        menuGrid.setAdapter(adapter);
        showPopup.setContentView(popupView);

        float widthPixels = getResources().getDisplayMetrics().widthPixels;

        showPopup.setWidth((int) widthPixels - 30);
        showPopup.setHeight(Toolbar.LayoutParams.WRAP_CONTENT);
        showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);
//        showPopup.showAsDropDown(view);
        showPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    public void launchCameraIntent() {
        if (showPopup != null) {
            showPopup.dismiss();
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void launchGalleryIntent() {
        if (showPopup != null) {
            showPopup.dismiss();
        }
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                Log.d("", DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                if (!TextUtils.isEmpty(picturePath)) {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
                    myDir.mkdirs();
                    File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
                    copy(new File(picturePath), destination);
                    imagePath = destination.getPath();
                    Picasso.with(this).load(destination).into(img_add_medicine_image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File myDir = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName() + "/Med_images");
            myDir.mkdirs();
            File destination = new File(myDir, "MyMed_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                imagePath = destination.getPath();
                Picasso.with(this).load(destination).into(img_add_medicine_image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_actionbar_back:
                finish();
                break;
            case R.id.tv_add_medicine_start_date:
                isSelectedStartDate = true;
                Calendar now = Calendar.getInstance();
                dpd.setMinDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.tv_add_medicine_end_date:
                isSelectedStartDate = false;
                dpd.setMinDate(selectedDateCalendar);
                dpd.show(getFragmentManager(), "Datepickerdialog");

                break;
            case R.id.tv_add_medicine_day_sunday:
                isSelectedSunday = !isSelectedSunday;
                if (isSelectedSunday) {
                    tv_add_medicine_day_sunday.setBackgroundResource(R.color.bg_selected_day);
                    tv_add_medicine_day_sunday.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_black_24dp), null);
                } else {
                    tv_add_medicine_day_sunday.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    tv_add_medicine_day_sunday.setBackgroundResource(R.color.bg_unselected_day);
                }
                break;
            case R.id.tv_add_medicine_day_monday:
                isSelectedMonday = !isSelectedMonday;
                if (isSelectedMonday) {
                    tv_add_medicine_day_monday.setBackgroundResource(R.color.bg_selected_day);
                    tv_add_medicine_day_monday.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_black_24dp), null);
                } else {
                    tv_add_medicine_day_monday.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    tv_add_medicine_day_monday.setBackgroundResource(R.color.bg_unselected_day);
                }
                break;
            case R.id.tv_add_medicine_day_tuesday:
                isSelectedTuesday = !isSelectedTuesday;
                if (isSelectedTuesday) {
                    tv_add_medicine_day_tuesday.setBackgroundResource(R.color.bg_selected_day);
                    tv_add_medicine_day_tuesday.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_black_24dp), null);
                } else {
                    tv_add_medicine_day_tuesday.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    tv_add_medicine_day_tuesday.setBackgroundResource(R.color.bg_unselected_day);
                }
                break;
            case R.id.tv_add_medicine_day_wednesday:
                isSelectedWednesday = !isSelectedWednesday;
                if (isSelectedWednesday) {
                    tv_add_medicine_day_wednesday.setBackgroundResource(R.color.bg_selected_day);
                    tv_add_medicine_day_wednesday.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_black_24dp), null);
                } else {
                    tv_add_medicine_day_wednesday.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    tv_add_medicine_day_wednesday.setBackgroundResource(R.color.bg_unselected_day);
                }
                break;
            case R.id.tv_add_medicine_day_thursday:
                isSelectedThursday = !isSelectedThursday;
                if (isSelectedThursday) {
                    tv_add_medicine_day_thursday.setBackgroundResource(R.color.bg_selected_day);
                    tv_add_medicine_day_thursday.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_black_24dp), null);
                } else {
                    tv_add_medicine_day_thursday.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    tv_add_medicine_day_thursday.setBackgroundResource(R.color.bg_unselected_day);
                }
                break;
            case R.id.tv_add_medicine_day_friday:
                isSelectedFriday = !isSelectedFriday;
                if (isSelectedFriday) {
                    tv_add_medicine_day_friday.setBackgroundResource(R.color.bg_selected_day);
                    tv_add_medicine_day_friday.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_black_24dp), null);
                } else {
                    tv_add_medicine_day_friday.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    tv_add_medicine_day_friday.setBackgroundResource(R.color.bg_unselected_day);
                }
                break;
            case R.id.tv_add_medicine_day_saturday:
                isSelectedSaturday = !isSelectedSaturday;
                if (isSelectedSaturday) {
                    tv_add_medicine_day_saturday.setBackgroundResource(R.color.bg_selected_day);
                    tv_add_medicine_day_saturday.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_black_24dp), null);
                } else {
                    tv_add_medicine_day_saturday.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    tv_add_medicine_day_saturday.setBackgroundResource(R.color.bg_unselected_day);
                }
                break;

            case R.id.img_add_medicine_image:
                showAttachmentPopup(view);
                break;
            case R.id.bt_add_medicine_done:
                if (TextUtils.isEmpty(et_add_medicine_medication_name.getText().toString())) {
                    til_add_medicine_medication_name.setError(getResources().getString(R.string.please_provide_medicine_name));
                    et_add_medicine_medication_name.requestFocus();
                } else if (TextUtils.isEmpty(et_add_medicine_dosage.getText().toString())) {
                    til_add_medicine_dosage.setError(getResources().getString(R.string.please_provide_dosage));
                    et_add_medicine_dosage.requestFocus();
                } else if (TextUtils.isEmpty(et_add_medicine_dosage_type.getText().toString())) {
                    til_add_medicine_dosage_type.setError(getResources().getString(R.string.please_provide_dosage_type));
                    et_add_medicine_dosage_type.requestFocus();
                } else if (frequencyValue.equalsIgnoreCase("")) {
                    Toast.makeText(AddMedicine.this, getResources().getString(R.string.please_select_frequency), Toast.LENGTH_SHORT).show();
                    sp_add_medicine_frequency.requestFocus();
                } else if (TextUtils.isEmpty(tv_add_medicine_start_date.getText().toString())) {
                    tv_add_medicine_start_date.setError(getResources().getString(R.string.please_select_start_date));
                    tv_add_medicine_start_date.requestFocus();
                } else if (TextUtils.isEmpty(tv_add_medicine_end_date.getText().toString())) {
                    tv_add_medicine_end_date.setError(getResources().getString(R.string.please_select_end_date));
                    tv_add_medicine_end_date.requestFocus();
                } else if (howManyTimesValue == 0) {
                    Toast.makeText(AddMedicine.this, getResources().getString(R.string.please_select_how_many_times), Toast.LENGTH_SHORT).show();
                    sp_add_medicine_times.requestFocus();
                } else if (frequencyValue.equalsIgnoreCase(getResources().getString(R.string.specific_days)) && !isSelectedSunday && !isSelectedMonday && !isSelectedTuesday && !isSelectedWednesday && !isSelectedThursday && !isSelectedFriday && !isSelectedSaturday) {
                    Toast.makeText(AddMedicine.this, getResources().getString(R.string.please_select_a_day), Toast.LENGTH_SHORT).show();
                    tv_add_medicine_day_sunday.requestFocus();
                } else {
                    saveMedicine();
                }
                break;
        }
    }

    private void saveMedicine() {

        medicineModel.setMedicineId(System.currentTimeMillis());
        medicineModel.setMedicineName(et_add_medicine_medication_name.getText().toString());
        medicineModel.setDosage(Integer.parseInt(et_add_medicine_dosage.getText().toString()));
        medicineModel.setDosageType(et_add_medicine_dosage_type.getText().toString());
        medicineModel.setUnit(unitValue);
        medicineModel.setLocalImagePath(imagePath);
        medicineModel.setServerImagePath("");
        medicineModel.setFrequency(frequencyValue);
        medicineModel.setStartDate(convertDateToMilliSeconds(tv_add_medicine_start_date.getText().toString()));
        medicineModel.setEndDate(convertDateToMilliSeconds(tv_add_medicine_end_date.getText().toString()));
        medicineModel.setHowManyTimes(howManyTimesValue);
        medicineModel.setIsSelectedsunday(isSelectedSunday);
        medicineModel.setIsSelectedmonday(isSelectedMonday);
        medicineModel.setIsSelectedtuesday(isSelectedTuesday);
        medicineModel.setIsSelectedwednesday(isSelectedWednesday);
        medicineModel.setIsSelectedthursday(isSelectedThursday);
        medicineModel.setIsSelectedfriday(isSelectedFriday);
        medicineModel.setIsSelectedsaturday(isSelectedSaturday);
        medicineModel.setCreatedDatetime(Calendar.getInstance().getTimeInMillis());
        medicineModel.setMedicineTaken(0);
        medicineModel.setMedicineSkipped(0);
        medicineModel.setNextNotificationTime(0);
        medicineModel.setNextMedicineCount(0);
        DbService dbService = new DbService(this);
        if (dbService.saveMedicine(medicineModel)) {
            dbService.updateNotificationTime(medicineModel.getMedicineId());
            Toast.makeText(this, getResources().getString(R.string.medicine_saved), Toast.LENGTH_SHORT).show();
            MyMedicineService.setAlarm(this);
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.medicine_not_saved), Toast.LENGTH_SHORT).show();
        }

    }
}
