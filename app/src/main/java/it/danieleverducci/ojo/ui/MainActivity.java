package it.danieleverducci.ojo.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import it.danieleverducci.ojo.R;
import it.danieleverducci.ojo.SharedPreferencesManager;
import it.danieleverducci.ojo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private NavController navController;
    private boolean rotationEnabledSetting;
    private OnBackButtonPressedListener onBackButtonPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rotationEnabledSetting = SharedPreferencesManager.loadRotationEnabled(this);
        this.setRequestedOrientation(this.rotationEnabledSetting ? ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Show FAB only on first fragment
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.HomeFragment)
                binding.fab.show();
            else
                binding.fab.hide();
        });

        binding.fab.setOnClickListener(view -> navigateToFragment(R.id.action_homeToSettings));
    }

    public void setOnBackButtonPressedListener(OnBackButtonPressedListener onBackButtonPressedListener) {
        this.onBackButtonPressedListener = onBackButtonPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (this.onBackButtonPressedListener != null && this.onBackButtonPressedListener.onBackPressed())
            return;
        super.onBackPressed();
    }

    public void navigateToFragment(int actionId) {
        navigateToFragment(actionId, null);
    }

    public void navigateToFragment(int actionId, Bundle bundle) {
        if (navController == null) {
            Log.e(TAG, "Not initialized");
            return;
        }

        try {
            if (bundle != null)
                navController.navigate(actionId, bundle);
            else
                navController.navigate(actionId);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Unable to navigate to fragment: " + e.getMessage());
        }
    }

    public boolean getRotationEnabledSetting() {
        return this.rotationEnabledSetting;
    }

    public void toggleRotationEnabledSetting() {
        this.rotationEnabledSetting = !this.rotationEnabledSetting;
        this.setRequestedOrientation(this.rotationEnabledSetting ? ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}