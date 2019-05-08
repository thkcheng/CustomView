package app.view.custom.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import app.view.custom.R;
import app.view.custom.widget.SplitView;

public class SplitActivity extends AppCompatActivity {

    private SplitView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        sv = findViewById(R.id.sv);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sv.close();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
