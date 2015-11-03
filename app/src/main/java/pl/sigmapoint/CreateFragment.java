package pl.sigmapoint;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import pl.sigmapoint.customview.CustomButton;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment implements View.OnClickListener {

    private final int BG_NORMAL = 0;
    private final int BG_PRESSED = 1;
    private final int BG_DISABLED = 2;
    private final int TXT_NORMAL = 3;
    private final int TXT_PRESSED = 4;
    private final int TXT_DISABLED = 5;
    private final int FRAME_NORMAL = 6;
    private final int FRAME_PRESSED = 7;
    private final int FRAME_DISABLED = 8;

    private View view;

    private CustomButton bgColorNormalCB, bgColorPressedCB, bgColorDisabledCB;
    private CustomButton txtColorNormalCB, txtColorPressedCB, txtColorDisabledCB;
    private CustomButton frameColorNormalCB, frameColorPressedCB, frameColorDisabledCB;
    private CustomButton generateCB;
    private EditText btTextET, cornerRadiusET, frameSizeET;
    private ToggleButton elevationTB, iconTB, enableTB;
    private Spinner iconPositionS;

    private AmbilWarnaDialog colorPickerDialog;

    private int decision = -1;
    private String[] array;

    private CustomButton generatedCB;

    public CreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_start, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setListeners();
    }

    private void init() {

        bgColorNormalCB = ((CustomButton) view.findViewById(R.id.bg_normal));
        bgColorPressedCB = ((CustomButton) view.findViewById(R.id.bg_pressed));
        bgColorDisabledCB = ((CustomButton) view.findViewById(R.id.bg_disabled));
        txtColorNormalCB = ((CustomButton) view.findViewById(R.id.txt_normal));
        txtColorPressedCB = ((CustomButton) view.findViewById(R.id.txt_pressed));
        txtColorDisabledCB = ((CustomButton) view.findViewById(R.id.txt_disabled));
        frameColorNormalCB = ((CustomButton) view.findViewById(R.id.frame_normal));
        frameColorPressedCB = ((CustomButton) view.findViewById(R.id.frame_pressed));
        frameColorDisabledCB = ((CustomButton) view.findViewById(R.id.frame_disabled));
        generateCB = ((CustomButton) view.findViewById(R.id.generate));
        btTextET = ((EditText) view.findViewById(R.id.et_text));
        cornerRadiusET = ((EditText) view.findViewById(R.id.et_corner_radius));
        frameSizeET = ((EditText) view.findViewById(R.id.et_frame_size));
        elevationTB = ((ToggleButton) view.findViewById(R.id.tb_elevation));
        iconTB = ((ToggleButton) view.findViewById(R.id.tb_icon));
        iconPositionS = ((Spinner) view.findViewById(R.id.spinner_icon_position));
        enableTB = (ToggleButton) view.findViewById(R.id.enable);

        enableTB.setChecked(true);

        array = new String[4];
        array[CustomButton.LEFT] = "Left";
        array[CustomButton.TOP] = "Top";
        array[CustomButton.RIGHT] = "Right";
        array[CustomButton.BOTTOM] = "Bottom";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, array);
        iconPositionS.setAdapter(adapter);

        colorPickerDialog = new AmbilWarnaDialog(getActivity(), Color.WHITE, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                ambilWarnaDialog.getDialog().dismiss();
            }

            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                switch (decision) {
                    case BG_NORMAL:
                        bgColorNormalCB.setBackgroundColorNormal(color);
                        break;
                    case BG_PRESSED:
                        bgColorPressedCB.setBackgroundColorPressed(color);
                        break;
                    case BG_DISABLED:
                        bgColorDisabledCB.setBackgroundColorDisabled(color);
                        break;
                    case TXT_NORMAL:
                        txtColorNormalCB.setTextColorNormal(color);
                        break;
                    case TXT_PRESSED:
                        txtColorPressedCB.setTextColorPressed(color);
                        break;
                    case TXT_DISABLED:
                        txtColorDisabledCB.setTextColorDisabled(color);
                        break;
                    case FRAME_NORMAL:
                        frameColorNormalCB.setFrameColorNormal(color);
                        break;
                    case FRAME_PRESSED:
                        frameColorPressedCB.setFrameColorPressed(color);
                        break;
                    case FRAME_DISABLED:
                        frameColorDisabledCB.setFrameColorDisabled(color);
                        break;
                    default:
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private void setListeners() {
        bgColorNormalCB.setOnClickListener(this);
        bgColorPressedCB.setOnClickListener(this);
        bgColorDisabledCB.setOnClickListener(this);
        txtColorNormalCB.setOnClickListener(this);
        txtColorPressedCB.setOnClickListener(this);
        txtColorDisabledCB.setOnClickListener(this);
        frameColorNormalCB.setOnClickListener(this);
        frameColorPressedCB.setOnClickListener(this);
        frameColorDisabledCB.setOnClickListener(this);
        generateCB.setOnClickListener(this);

        enableTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bgColorDisabledCB.setEnabled(isChecked);
                txtColorDisabledCB.setEnabled(isChecked);
                frameColorDisabledCB.setEnabled(isChecked);
                generateCB.setEnabled(isChecked);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bg_normal:
                decision = BG_NORMAL;
                colorPickerDialog.show();
                break;
            case R.id.bg_pressed:
                decision = BG_PRESSED;
                colorPickerDialog.show();
                break;
            case R.id.bg_disabled:
                decision = BG_DISABLED;
                colorPickerDialog.show();
                break;
            case R.id.txt_normal:
                decision = TXT_NORMAL;
                colorPickerDialog.show();
                break;
            case R.id.txt_pressed:
                decision = TXT_PRESSED;
                colorPickerDialog.show();
                break;
            case R.id.txt_disabled:
                decision = TXT_DISABLED;
                colorPickerDialog.show();
                break;
            case R.id.frame_normal:
                decision = FRAME_NORMAL;
                colorPickerDialog.show();
                break;
            case R.id.frame_pressed:
                decision = FRAME_PRESSED;
                colorPickerDialog.show();
                break;
            case R.id.frame_disabled:
                decision = FRAME_DISABLED;
                colorPickerDialog.show();
                break;
            case R.id.generate:
                generate();
                break;
        }
    }

    private void generate() {
        generatedCB = new CustomButton(getActivity(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200),
                bgColorNormalCB.getBackgroundColorNormal(),
                txtColorNormalCB.getTextColorNormal(),
                null);
        generatedCB.setBackgroundColorPressed(bgColorPressedCB.getBackgroundColorPressed());
        generatedCB.setBackgroundColorDisabled(bgColorDisabledCB.getBackgroundColorDisabled());
        generatedCB.setTextColorPressed(txtColorPressedCB.getTextColorPressed());
        generatedCB.setTextColorDisabled(txtColorDisabledCB.getTextColorDisabled());
        generatedCB.setFrameColorNormal(frameColorNormalCB.getFrameColor());
        generatedCB.setFrameColorPressed(frameColorPressedCB.getFrameColorPressed());
        generatedCB.setFrameColorDisabled(frameColorDisabledCB.getFrameColorDisabled());

        if (btTextET.getText().length() > 0) generatedCB.setText(btTextET.getText().toString());
        if (cornerRadiusET.getText().length() > 0)
            generatedCB.setBackground(GradientDrawable.RECTANGLE, Integer.parseInt(cornerRadiusET.getText().toString()));
        if (frameSizeET.getText().length() > 0)
            generatedCB.setFrameSize(Float.parseFloat(frameSizeET.getText().toString()));
        if (iconTB.isChecked()) {
            int position = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(iconPositionS.getSelectedItem())) {
                    position = i;
                    break;
                }
            }
            generatedCB.setImage(position, getResources().getDrawable(R.drawable.ic_owl), ImageView.ScaleType.FIT_CENTER, 0, null);
        }
        generatedCB.setElevationEnabled(elevationTB.isChecked());
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).createdButton = generatedCB;
            ((MainActivity) getActivity()).startGenerated();
        }
    }
}
