package pl.sigmapoint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.sigmapoint.customview.CustomButton;

public class MainActivity extends AppCompatActivity {

    public CustomButton createdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, new MainFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    public void startGenerated() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new GeneratedFragment()).addToBackStack(GeneratedFragment.class.getSimpleName()).commit();
    }

    public void startCreate() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CreateFragment()).addToBackStack(CreateFragment.class.getSimpleName()).commit();
    }
}
