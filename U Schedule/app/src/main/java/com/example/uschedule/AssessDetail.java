package com.example.uschedule;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.uschedule.Entities.AssessEntity;
import com.example.uschedule.Tools.AlertReceiver;
import com.example.uschedule.ViewModel.AssessViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AssessDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private AssessViewModel mAssessViewModel;
    private EditText mEditName;
    private Spinner mEditType;
    private TextView mEditDue;
    private DatePickerDialog.OnDateSetListener mDueDateListner;
    private boolean mNewAssess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAssessViewModel = new ViewModelProvider(this).get(AssessViewModel.class);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mEditName = findViewById(R.id.editAssess);
        mEditType = findViewById(R.id.pickAssess);
        mEditDue = findViewById(R.id.editDueDate);

        ArrayAdapter<CharSequence> assessAdapter = ArrayAdapter.createFromResource(this, R.array.assess, android.R.layout.simple_spinner_item);
        assessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEditType.setAdapter(assessAdapter);
        mEditType.setOnItemSelectedListener(this);

        final Button setReminder = findViewById(R.id.setAssessReminder);
        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Calendar remind = Calendar.getInstance();
                String dateString = mEditDue.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date date = formatter.parse(dateString);
                    remind.setTime(date);
                    Toast toast = Toast.makeText(AssessDetail.this, "Reminder Set for: "+date.toString(), Toast.LENGTH_LONG);
                    toast.show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(AssessDetail.this, "Invalid date format using current date.", Toast.LENGTH_SHORT);
                    toast.show();
                    Date date = Calendar.getInstance().getTime();
                    remind.setTime(date);
                }


                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                intent.putExtra("what", mEditName.getText().toString()+ " is due!");

                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, remind.getTimeInMillis(), broadcast);
            }
        });

        if (getIntent().getStringExtra("assessName") != null) {
            setTitle("Edit Assessment");
            mEditName.setText(getIntent().getStringExtra("assessName"));
            mEditType.setSelection(assessAdapter.getPosition(getIntent().getStringExtra("assessType")));
            mEditDue.setText(getIntent().getStringExtra("assessDue"));
        }else {
            setTitle("New Assessment");
            setReminder.setVisibility(View.GONE);
            mNewAssess = true;
        }

        final Button button = findViewById(R.id.save_assess);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String name = mEditName.getText().toString();
                String type = mEditType.getSelectedItem().toString();
                String due = mEditDue.getText().toString();

                replyIntent.putExtra("assessName", name);
                replyIntent.putExtra("assessType", type);
                replyIntent.putExtra("assessDue", due);
                if (getIntent().getStringExtra("assessName") != null) {
                    int id = getIntent().getIntExtra("assessID", 0);
                    int courseID = getIntent().getIntExtra("courseID", 0);
                    AssessEntity assess = new AssessEntity(id, name, type, due, courseID);
                    mAssessViewModel.insert(assess);
                }
                setResult(RESULT_OK, replyIntent);
                finish();
            }

        });

        mEditDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar start = Calendar.getInstance();
                int year = start.get(Calendar.YEAR);
                int month = start.get(Calendar.MONTH);
                int day = start.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDialog = new DatePickerDialog(AssessDetail.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        mDueDateListner, year, month, day);
                startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDialog.show();
            }
        });

        mDueDateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mEditDue.setText(date);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!mNewAssess) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_delete) {

            mAssessViewModel.getAllAssess().observe(this, new Observer<List<AssessEntity>>() {
                @Override
                public void onChanged(List<AssessEntity> assess) {
                    for (AssessEntity a : assess)
                        if(a.getAssessID() == getIntent().getIntExtra("assessID", 0))
                            mAssessViewModel.delete(a);
                    navigateUpToFromChild(AssessDetail.this, getIntent());
                    Toast.makeText(getApplicationContext(),"Assessment Deleted",Toast.LENGTH_SHORT).show();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
