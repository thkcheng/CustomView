package app.view.custom.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.view.custom.R;
import app.view.custom.widget.ExerciseView;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        ExerciseView ev = findViewById(R.id.ev);
        findViewById(R.id.btnStart).setOnClickListener(v -> ev.start());
    }
}
