package com.example.uschedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uschedule.Entities.TermEntity;
import com.example.uschedule.UI.TermAdapter;
import com.example.uschedule.ViewModel.CourseViewModel;
import com.example.uschedule.ViewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TermActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.add_course_fab);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermActivity.this, CourseActivity.class);
                intent.putExtra("termID", termViewModel.lastID() + 1);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(@Nullable final List<TermEntity> terms) {
                adapter.setWords(terms);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {

            TermEntity term = new TermEntity(termViewModel.lastID() + 1,
                    intent.getStringExtra("termName"),
                    intent.getStringExtra("termStart"),
                    intent.getStringExtra("termEnd"));
            termViewModel.insert(term);
        }
    }

}
