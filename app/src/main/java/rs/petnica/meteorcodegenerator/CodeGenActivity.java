package rs.petnica.meteorcodegenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CodeGenActivity extends AppCompatActivity {

    public static final long DOUBLECLICK_PERIOD = 400;
    public static final long CODE_DISPLAY_DURATION = 5000;
    public static final String GROUP_NUMBER_NAME = "rs.petnica.meteorcodegenerator.CodeGenActivity.GROUP_NUMBER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_code_gen);

        codeListView = (ListView) findViewById(R.id.code_list);
        codeList = new ArrayList<>();
        codeAdapter = new ArrayAdapter<>(this, R.layout.code_layout, codeList);
        codeListView.setAdapter(codeAdapter);

        codeListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onScreenTouch();
                }
                return false;
            }
        });

        codeGen = CodeGen.getGroupCodeGen(getIntent().getIntExtra(GROUP_NUMBER_NAME, 0));

        lastCode = (TextView) findViewById(R.id.lastCode);
        lastCode.setText('(' + codeGen.getLastCode() + ')');
    }

    private String addNewCode() {
        final String code = codeGen.generate();

        codeList.add(code);
        codeAdapter.notifyDataSetChanged();

        lastCode.setText('(' + code + ')');

        return code;
    }

    private void addCodeExpiration(final String code) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CodeGenActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        codeList.remove(code);
                        codeAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, CODE_DISPLAY_DURATION);
    }

    private long lastTimestamp = -1;

    private void onScreenTouch() {
        long currentTimestamp = System.currentTimeMillis();

        synchronized (this) {
            if ((lastTimestamp > 0) && (currentTimestamp - lastTimestamp <= DOUBLECLICK_PERIOD)) {
                addCodeExpiration(addNewCode());
                lastTimestamp = -1;
            } else {
                lastTimestamp = currentTimestamp;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onScreenTouch();
        }
        return super.onTouchEvent(event);
    }

    private ListView codeListView;
    private ArrayAdapter<String> codeAdapter;
    private ArrayList<String> codeList;

    private TextView lastCode;

    private CodeGen codeGen;
}
