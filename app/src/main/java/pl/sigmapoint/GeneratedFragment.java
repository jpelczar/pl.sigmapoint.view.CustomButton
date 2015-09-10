package pl.sigmapoint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import pl.sigmapoint.customview.CustomButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratedFragment extends Fragment {

    private View view;
    private ToggleButton enableTB;
    private CustomButton button;

    public GeneratedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_generated, container, false);

        enableTB = (ToggleButton) view.findViewById(R.id.enable);
        enableTB.setChecked(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            button = ((MainActivity) getActivity()).createdButton;
            ((FrameLayout) view.findViewById(R.id.generated_container)).addView(button);
        }

        enableTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                button.setEnabled(isChecked);
            }
        });

    }
}
