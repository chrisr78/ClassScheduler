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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uschedule.Entities.AssessEntity;
import com.example.uschedule.Entities.CourseEntity;
import com.example.uschedule.Entities.MentorInfo;
import com.example.uschedule.Tools.AlertReceiver;
import com.example.uschedule.UI.AssessAdapter;
import com.example.uschedule.ViewModel.AssessViewModel;
import com.example.uschedule.ViewModel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.uschedule.CourseActivity.numCourses;

public class AssessActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private AssessViewModel assessViewModel;
    private CourseViewModel courseViewModel;
    private EditText mEditName;
    private TextView mEditStart;
    private TextView mEditEnd;
    private Spinner mEditStatus;
    private Spinner mEditMentor;
    private EditText mEditNote;
    private ArrayList<MentorInfo> mentorInfo;
    private TextView mentorNumber;
    private TextView mentorEmail;
    private DatePickerDialog.OnDateSetListener mStartDateListener;
    private DatePickerDialog.OnDateSetListener mEndDateListener;
    private boolean mNewCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mEditName = findViewById(R.id.editCourseName);
        mEditStart = findViewById(R.id.editStartDate);
        mEditEnd = findViewById(R.id.editEndDate);
        mEditStatus = findViewById(R.id.pickStatus);
        mEditMentor = findViewById(R.id.pickMentor);
        mentorNumber = findViewById(R.id.mentorNumber);
        mentorEmail = findViewById(R.id.mentorEmail);
        mEditNote = findViewById(R.id.editNote);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEditStatus.setAdapter(statusAdapter);
        mEditStatus.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> mentorAdapter = ArrayAdapter.createFromResource(this, R.array.mentor, android.R.layout.simple_spinner_item);
        mEditMentor.setAdapter(mentorAdapter);
        mEditMentor.setOnItemSelectedListener(this);

        final Button mentorInfoBtn = findViewById(R.id.mentor_info_button);
        mentorInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = mEditMentor.getSelectedItem().toString();
                if(mentorInfoBtn.getText().toString().contains("Show Info")) {
                    mentorInfo = new ArrayList<>();
                    mentorInfo.add(new MentorInfo("Carolyn Sher-DeCusatis", "(385) 428-7192 Ext 7192", "carolyn.sher@wgu.edu"));
                    mentorInfo.add(new MentorInfo("Mark Kinkead", "385-428-2432 Ext 2432", "mark.kinkead@wgu.edu"));
                    mentorInfo.add(new MentorInfo("Tom Jones", "800-800-8000", "tom.jones@fake.email"));
                    mentorInfo.add(new MentorInfo("John Doe", "555-555-5555", "john.doe@dead.onarrival"));

                    for (MentorInfo m : mentorInfo) {
                        if (m.getMentorName().equals(selected)) {
                            mentorNumber.setVisibility(View.VISIBLE);
                            mentorNumber.setText(m.getMentorNumber());
                            mentorEmail.setVisibility(View.VISIBLE);
                            mentorEmail.setText(m.getMentorEmail());
                        }
                    }

                    mentorInfoBtn.setText(R.string.hide_info);

                }else if (mentorInfoBtn.getText().toString().contains("Hide Info")){

                    mentorInfoBtn.setText(R.string.show_info);
                    mentorNumber.setVisibility(View.GONE);
                    mentorEmail.setVisibility(View.GONE);
                }
            }
        });

        final Button setStartReminder = findViewById(R.id.courseStartRemind);
        setStartReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Calendar remind = Calendar.getInstance();
                String dateString = mEditStart.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date date = formatter.parse(dateString);
                    remind.setTime(date);
                    Toast toast = Toast.makeText(AssessActivity.this, "Reminder Set for: "+date.toString(), Toast.LENGTH_LONG);
                    toast.show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(AssessActivity.this, "Invalid date format using current date.", Toast.LENGTH_SHORT);
                    toast.show();
                    Date date = Calendar.getInstance().getTime();
                    remind.setTime(date);
                }


                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                intent.putExtra("what", mEditName.getText().toString() + " has started!");
                int id = getIntent().getIntExtra("courseID", 0);
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 1001+id, intent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, remind.getTimeInMillis(), broadcast);
            }
        });

        final Button setEndReminder = findViewById(R.id.courseEndRemind);
        setEndReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Calendar remind = Calendar.getInstance();
                String dateString = mEditEnd.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date date = formatter.parse(dateString);
                    remind.setTime(date);
                    Toast toast = Toast.makeText(AssessActivity.this, "Reminder Set for: "+date.toString(), Toast.LENGTH_LONG);
                    toast.show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(AssessActivity.this, "Invalid date format using current date.", Toast.LENGTH_SHORT);
                    toast.show();
                    Date date = Calendar.getInstance().getTime();
                    remind.setTime(date);
                }


                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                intent.putExtra("what", mEditName.getText().toString() + " has ended!");
                int id = getIntent().getIntExtra("courseID", 0);
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 1002+id, intent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, remind.getTimeInMillis(), broadcast);
            }
        });

        FloatingActionButton fab3 = findViewById(R.id.add_assess_fab);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessActivity.this, AssessDetail.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        final Button showNote = findViewById(R.id.showNote);
        final Button shareNote = findViewById(R.id.shareNote);

        if (getIntent().getStringExtra("courseName") != null) {
            setTitle("Edit Course");
            mEditName.setText(getIntent().getStringExtra("courseName"));
            mEditStart.setText(getIntent().getStringExtra("courseStart"));
            mEditEnd.setText(getIntent().getStringExtra("courseEnd"));
            mEditStatus.setSelection(statusAdapter.getPosition(getIntent().getStringExtra("status")));
            mEditMentor.setSelection(mentorAdapter.getPosition(getIntent().getStringExtra("mentor")));
            mEditNote.setText(getIntent().getStringExtra("note"));
        }else {
            setTitle("New Course");
            fab3.setVisibility(View.GONE);
            setStartReminder.setVisibility(View.GONE);
            setEndReminder.setVisibility(View.GONE);
            mentorInfoBtn.setVisibility(View.GONE);
            mNewCourse = true;
        }

        RecyclerView recyclerView = findViewById(R.id.associated_assess);
        final AssessAdapter adapter = new AssessAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessViewModel = new ViewModelProvider(this).get(AssessViewModel.class);
        assessViewModel.getAllAssess().observe(this, new Observer<List<AssessEntity>>() {
            @Override
            public void onChanged(@Nullable final List<AssessEntity> assess) {

                List<AssessEntity> filteredWords = new ArrayList<>();
                for (AssessEntity p : assess)
                    if (p.getCourseID() == getIntent().getIntExtra("courseID", 0)) filteredWords.add(p);
                adapter.setWords(filteredWords);
                numCourses = filteredWords.size();

            }
        });


        showNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showNote.getText().toString().equals("Show Note")){
                    mEditNote.setVisibility(View.VISIBLE);
                    if(mEditNote.getText().toString().trim().length()<1)
                        shareNote.setVisibility(View.GONE);
                    else
                        shareNote.setVisibility(View.VISIBLE);
                    showNote.setText(R.string.hide_note);
                }else if (showNote.getText().toString().equals("Hide Note")) {
                    mEditNote.setVisibility(View.GONE);
                    shareNote.setVisibility(View.GONE);
                    showNote.setText(R.string.show_note);
                }else {
                    mEditNote.setVisibility(View.VISIBLE);
                }
            }
        });


        shareNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mEditNote.getText().toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        final Button button = findViewById(R.id.save_course);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String name = mEditName.getText().toString();
                String start = mEditStart.getText().toString();
                String end = mEditEnd.getText().toString();
                String status = mEditStatus.getSelectedItem().toString();
                String mentor = mEditMentor.getSelectedItem().toString();
                String note = mEditNote.getText().toString();
                int termID = getIntent().getIntExtra("termID", 0);

                replyIntent.putExtra("courseName", name);
                replyIntent.putExtra("courseStart", start);
                replyIntent.putExtra("courseEnd", end);
                replyIntent.putExtra("status", status);
                replyIntent.putExtra("mentor", mentor);
                replyIntent.putExtra("note", note);
                if (getIntent().getStringExtra("courseName") != null) {
                    int id = getIntent().getIntExtra("courseID", 0);
                    CourseEntity course = new CourseEntity(id, name, start, end, status, mentor, note, termID);
                    courseViewModel.insert(course);
                }
                setResult(RESULT_OK, replyIntent);
                finish();
            }

        });

        mEditStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar start = Calendar.getInstance();
                int year = start.get(Calendar.YEAR);
                int month = start.get(Calendar.MONTH);
                int day = start.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDialog = new DatePickerDialog(AssessActivity.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        mStartDateListener, year, month, day);
                startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDialog.show();
            }
        });

        mStartDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mEditStart.setText(date);
            }
        };

        mEditEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar start = Calendar.getInstance();
                int year = start.get(Calendar.YEAR);
                int month = start.get(Calendar.MONTH);
                int day = start.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDialog = new DatePickerDialog(AssessActivity.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        mEndDateListener, year, month, day);
                startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDialog.show();
            }
        });

        mEndDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mEditEnd.setText(date);
            }
        };


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!mNewCourse) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_delete) {
            if(numCourses==0) {
                courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
                    @Override
                    public void onChanged(List<CourseEntity> courses) {
                        for (CourseEntity c : courses)
                            if (c.getCourseID() == getIntent().getIntExtra("courseID", 0))
                                courseViewModel.delete(c);
                        navigateUpToFromChild(AssessActivity.this, getIntent());
                        Toast.makeText(getApplicationContext(), "Course Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(), "Cannot delete a Course with Assessment associated.", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {

            AssessEntity assess = new AssessEntity(assessViewModel.lastID() + 1,
                    intent.getStringExtra("assessName"),
                    intent.getStringExtra("assessType"),
                    intent.getStringExtra("assessDue"),
                    getIntent().getIntExtra("courseID", 0));
            assessViewModel.insert(assess);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
