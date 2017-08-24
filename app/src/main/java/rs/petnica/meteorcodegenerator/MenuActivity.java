package rs.petnica.meteorcodegenerator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MenuActivity extends AppCompatActivity {

    public static final int WRITE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        groupDropdown = (Spinner) findViewById(R.id.group_dropdown);
        String[] groups = new String[CodeGen.GROUP_PREFIXES.length];
        for(int i = 0; i < CodeGen.GROUP_PREFIXES.length; i++) {
            groups[i] = getString(R.string.group) + " " + (i + 1);
        }
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this, R.layout.group_layout, groups);
        groupDropdown.setAdapter(groupAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, WRITE_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    public void codeGenerator(View v) {
        Intent intent = new Intent(this, CodeGenActivity.class);

        intent.putExtra(CodeGenActivity.GROUP_NUMBER_NAME, groupDropdown.getSelectedItemPosition());

        startActivity(intent);
    }

    private Spinner groupDropdown;

}
