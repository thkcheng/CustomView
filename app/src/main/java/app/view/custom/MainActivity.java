package app.view.custom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.view.custom.demo.ArcWhirlActivity;
import app.view.custom.demo.EventMoveActivity;
import app.view.custom.demo.ExerciseActivity;
import app.view.custom.demo.GradientRampActivity;
import app.view.custom.demo.SplashActivity;
import app.view.custom.demo.SplitActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnMove).setOnClickListener(v -> startActivity(new Intent(this, EventMoveActivity.class)));

        findViewById(R.id.btnArc).setOnClickListener(v -> startActivity(new Intent(this, ArcWhirlActivity.class)));

        findViewById(R.id.btnSplash).setOnClickListener(v -> startActivity(new Intent(this, SplashActivity.class)));

        findViewById(R.id.btnSplit).setOnClickListener(v -> startActivity(new Intent(this, SplitActivity.class)));

        findViewById(R.id.btnExercise).setOnClickListener(v -> startActivity(new Intent(this, ExerciseActivity.class)));

        findViewById(R.id.btnGradient).setOnClickListener(v -> startActivity(new Intent(this, GradientRampActivity.class)));

    }
}
