package app.view.custom.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import app.view.custom.R;
import app.view.custom.widget.ColorFulBar;

public class GradientRampActivity extends AppCompatActivity {

    ColorFulBar gv;
    EditText etCurrentLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient_ramp);

        gv = findViewById(R.id.gv);
        etCurrentLength = findViewById(R.id.etCurrentLength);


        findViewById(R.id.btnCurrentLength).setOnClickListener(v -> {
            gv.setMaxProgress(100);
            gv.setCurrentProgress(getCurrentProgress());
        });

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            gv.startProgress();
        });

        findViewById(R.id.btnEnd).setOnClickListener(v -> {
            gv.stopProgress();
        });

    }

    private float getCurrentProgress() {
        String maxProgress = etCurrentLength.getText().toString();
        if (TextUtils.isEmpty(maxProgress)) {
            return 0f;
        }
        return Float.valueOf(maxProgress);
    }
}
