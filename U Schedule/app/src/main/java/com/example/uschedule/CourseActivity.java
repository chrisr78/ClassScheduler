package com.example.uschedule;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import com.example.uschedule.Entities.CourseEntity;
import com.example.uschedule.Entities.TermEntity;
import com.example.uschedule.UI.CourseAdapter;
import com.example.uschedule.ViewModel.CourseViewModel;
import com.example.uschedule.ViewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private EditText mEditName;
    private TextView mEditStart;
    private TextView mEditEnd;
    private DatePickerDialog.OnDateSetListener mStartDateListener;
    private DatePickerDialog.OnDateSetListener mEndDateListener;
    private boolean mNewTerm;
    static int numCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mEditName = findViewById(R.id.editTerm);
        mEditStart = findViewById(R.id.editStartDate);
        mEditEnd = findViewById(R.id.editEndDate);


        FloatingActionButton fab2 = findViewById(R.id.add_course_fab);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, AssessActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        if (getIntent().getStringExtra("termName") != null) {
            setTitle("Edit Term");
            mEditName.setText(getIntent().getStringExtra("termName"));
            mEditStart.setText(getIntent().getStringExtra("termStart"));
            mEditEnd.setText(getIntent().getStringExtra("termEnd"));
        }else {
            setTitle("New Term");
            fab2.setVisibility(View.GONE);
            mNewTerm = true;
        }

        RecyclerView recyclerView = findViewById(R.id.associated_courses);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(@Nullable final List<CourseEntity> courses) {

                List<CourseEntity> filteredWords = new ArrayList<>();
                for (CourseEntity p : courses)
                    if (p.getTermID() == getIntent().getIntExtra("termID", 0))
                        filteredWords.add(p);
                adapter.setWords(filteredWords);
                numCourses = filteredWords.size();

            }
        });

        final Button button = findViewById(R.id.term_button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String name = mEditName.getText().toString();
                String start = mEditStart.getText().toString();
                String end = mEditEnd.getText().toString();

                replyIntent.putExtra("termName", name);
                replyIntent.putExtra("termStart", start);
                replyIntent.putExtra("termEnd", end);
                if (getIntent().getStringExtra("termName") != null) {
                    int id = getIntent().getIntExtra("termID", 0);
                    TermEntity term = new TermEntity(id, name, start, end);
                    termViewModel.insert(term);
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

                DatePickerDialog startDialog = new DatePickerDialog(CourseActivity.this,
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

                DatePickerDialog startDialog = new DatePickerDialog(CourseActivity.this,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!mNewTerm) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_delete) {
            if(numCourses==0) {
                termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
                    @Override
                    public void onChanged(List<TermEntity> terms) {
                        for (TermEntity t : terms)
                            if(t.getTermID() == getIntent().getIntExtra("termID", 0))
                                termViewModel.delete(t);
                                navigateUpToFromChild(CourseActivity.this, getIntent());
                                Toast.makeText(getApplicationContext(),"Term Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(), "Cannot delete a Term with Courses associated.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {

            CourseEntity course = new CourseEntity(courseViewModel.lastID() + 1,
                    intent.getStringExtra("courseName"),
                    intent.getStringExtra("courseStart"),
                    intent.getStringExtra("courseEnd"),
                    intent.getStringExtra("status"),
                    intent.getStringExtra("mentor"),
                    intent.getStringExtra("note"),
                    getIntent().getIntExtra("termID", 0));
            courseViewModel.insert(course);
        }
    }

}
