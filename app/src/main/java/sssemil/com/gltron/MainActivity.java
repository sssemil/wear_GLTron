package sssemil.com.gltron;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartClick(View view) {
        startActivity(new Intent(MainActivity.this, glTron.class));
    }

    public void onSettingsClick(View view) {
        startActivity(new Intent(MainActivity.this, Preferences.class));
    }
}
