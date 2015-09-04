package pl.sigmapoint;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pl.sigmapoint.customview.CustomButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomButton customButton = (CustomButton) findViewById(R.id.button);

        findViewById(R.id.change_background_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customButton.setBackgroundColor(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{android.R.attr.state_pressed}, new int[]{}}, new int[]{Color.GREEN, Color.MAGENTA, Color.RED}));
            }
        });

        findViewById(R.id.change_text_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customButton.setText("Custom");
            }
        });

        findViewById(R.id.enable_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customButton.setEnabled(!customButton.isEnabled());
            }
        });

        findViewById(R.id.shape_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customButton.setShape(GradientDrawable.OVAL, 40);
            }
        });

        findViewById(R.id.text_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customButton.setTextColor(Color.BLUE);
            }
        });

        findViewById(R.id.elevation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customButton.setElevationEnabled(!customButton.isElevationEnabled());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
