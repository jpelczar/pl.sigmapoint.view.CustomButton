package pl.sigmapoint.customview;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by jpelczar on 02.09.2015.
 */
public class CustomButton extends FrameLayout implements View.OnClickListener {

    protected TextView textView;
    protected FrameLayout container;

    private int backgroundPressed, backgroundDisabled, background;
    private ColorStateList backgroundState;
    private int textColorPressed, textColorDisabled, textColor;
    private ColorStateList textColorState;
    private float textSize;
    private String text;
    private float shapeRadius;
    private int shapeTypeAttr;
    private int strokeColorPressed, strokeColorDisabled, strokeColor;
    private float strokeSize;
    private boolean isElevationEnabled;

    private int shapeType;

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);

        try {
            background = attributes.getColor(R.styleable.CustomButton_bc_background, 0);
            backgroundPressed = attributes.getColor(R.styleable.CustomButton_bc_background_pressed, background);
            backgroundDisabled = attributes.getColor(R.styleable.CustomButton_bc_background_disabled, background);
            backgroundState = attributes.getColorStateList(R.styleable.CustomButton_bc_background_state_list);

            textColor = attributes.getColor(R.styleable.CustomButton_bc_text_color, 0);
            textColorPressed = attributes.getColor(R.styleable.CustomButton_bc_text_color_pressed, textColor);
            textColorDisabled = attributes.getColor(R.styleable.CustomButton_bc_text_color_disabled, textColor);
            textColorState = attributes.getColorStateList(R.styleable.CustomButton_android_textColor);
            textSize = attributes.getDimension(R.styleable.CustomButton_bc_text_size, 0);
            text = attributes.getString(R.styleable.CustomButton_android_text);

            shapeRadius = attributes.getDimension(R.styleable.CustomButton_bc_shape_radius, 0);
            shapeTypeAttr = attributes.getInt(R.styleable.CustomButton_bc_shape_type, 0);
            strokeColor = attributes.getColor(R.styleable.CustomButton_bc_stroke_color, Color.DKGRAY);
            strokeColorPressed = attributes.getColor(R.styleable.CustomButton_bc_stroke_color_pressed, strokeColor);
            strokeColorDisabled = attributes.getColor(R.styleable.CustomButton_bc_stroke_color_disabled, strokeColor);
            strokeSize = attributes.getDimension(R.styleable.CustomButton_bc_stroke_size, 0);

            isElevationEnabled = attributes.getBoolean(R.styleable.CustomButton_bc_elevation_enabled, true);

            if (backgroundState != null) {
                backgroundColorStateListToIntegers(backgroundState);
            }

            inflate(context, R.layout.button_custom, this);

            textView = (TextView) findViewById(R.id.text);
            container = (FrameLayout) findViewById(R.id.container);

            ColorStateList textColorList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_enabled}, new int[]{}},
                    new int[]{textColorPressed, textColor, textColorDisabled});

            textView.setText(text);
            if (textSize > 0) textView.setTextSize(textSize);
            if (textColor != 0) textView.setTextColor(textColorList);
            else if (textColorState != null) textView.setTextColor(textColorState);

            shapeType = (shapeTypeAttr == 0) ? GradientDrawable.RECTANGLE : GradientDrawable.OVAL;

            setShape();
            setElevationEnabled(isElevationEnabled);

        } finally {
            attributes.recycle();
        }

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textView.setEnabled(enabled);
        container.setEnabled(enabled);
    }

    private void setShape() {

        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(shapeType);
        gd.setCornerRadius(shapeRadius);

        GradientDrawable drawableNormal = (GradientDrawable) gd.getConstantState().newDrawable().mutate();
        GradientDrawable drawablePressed = (GradientDrawable) gd.getConstantState().newDrawable().mutate();
        GradientDrawable drawableDisabled = (GradientDrawable) gd.getConstantState().newDrawable().mutate();

        drawableNormal.setColor(background);
        drawablePressed.setColor(backgroundPressed);
        drawableDisabled.setColor(backgroundDisabled);
        drawableNormal.setStroke((int) strokeSize, strokeColor);
        drawablePressed.setStroke((int) strokeSize, strokeColorPressed);
        drawableDisabled.setStroke((int) strokeSize, strokeColorDisabled);

        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, drawableNormal);
        stateListDrawable.addState(new int[]{}, drawableDisabled);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                container.setBackground(stateListDrawable);
            else
                container.setBackgroundDrawable(stateListDrawable);

        } else {

            RippleDrawable drawable = new RippleDrawable(new ColorStateList(new int[][]{new int[]{}}, new int[]{backgroundPressed}), stateListDrawable, null);
            container.setBackground(drawable);
        }
    }

    private void backgroundColorStateListToIntegers(ColorStateList colorStateList) {

        int globalColor = colorStateList.getColorForState(new int[]{}, 0);

        background = colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, globalColor);
        backgroundPressed = colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, globalColor);
        backgroundDisabled = globalColor;
    }

    public void setShape(int shapeType, int shapeRadius) {
        this.shapeType = shapeType;
        this.shapeRadius = shapeRadius;
        setShape();

    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextColor(ColorStateList colorStateList) {
        textView.setTextColor(colorStateList);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setElevationEnabled(boolean enabled) {

        isElevationEnabled = enabled;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            int var;

            if (enabled) {
                var = 6;
                container.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.anim.elevation_button_custom));
            } else {
                var = 0;
                container.setTranslationZ(0);
                container.setElevation(0);
                container.setStateListAnimator(null);
            }
            FrameLayout.LayoutParams layoutParams = (LayoutParams) container.getLayoutParams();
            layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            container.setLayoutParams(layoutParams);

        }
    }

    public void setBackgroundColor(ColorStateList colorStateList) {

        backgroundColorStateListToIntegers(colorStateList);
        setShape();
    }

    public int getShapeType() {
        return shapeType;
    }

    public String getText() {
        return String.valueOf(textView.getText());
    }

    public boolean isElevationEnabled() {
        return isElevationEnabled;
    }
}
