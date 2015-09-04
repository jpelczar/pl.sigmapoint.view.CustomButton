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

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        int backgroundPressed, backgroundDisabled, background;
        int textColorPressed, textColorDisabled, textColor;
        ColorStateList textColorState;
        float textSize;
        String text;
        float shapeRadius;
        int shapeType;
        int strokeColorPressed, strokeColorDisabled, strokeColor;
        float strokeSize;
        boolean isElevationEnabled;

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);

        try {
            background = attributes.getColor(R.styleable.CustomButton_bc_background, 0);
            backgroundPressed = attributes.getColor(R.styleable.CustomButton_bc_background_pressed, background);
            backgroundDisabled = attributes.getColor(R.styleable.CustomButton_bc_background_disabled, background);

            textColor = attributes.getColor(R.styleable.CustomButton_bc_text_color, 0);
            textColorPressed = attributes.getColor(R.styleable.CustomButton_bc_text_color_pressed, textColor);
            textColorDisabled = attributes.getColor(R.styleable.CustomButton_bc_text_color_disabled, textColor);
            textColorState = attributes.getColorStateList(R.styleable.CustomButton_android_textColor);
            textSize = attributes.getDimension(R.styleable.CustomButton_bc_text_size, 0);
            text = attributes.getString(R.styleable.CustomButton_android_text);

            shapeRadius = attributes.getDimension(R.styleable.CustomButton_bc_shape_radius, 0);
            shapeType = attributes.getInt(R.styleable.CustomButton_bc_shape_type, 0);
            strokeColor = attributes.getColor(R.styleable.CustomButton_bc_stroke_color, Color.DKGRAY);
            strokeColorPressed = attributes.getColor(R.styleable.CustomButton_bc_stroke_color_pressed, strokeColor);
            strokeColorDisabled = attributes.getColor(R.styleable.CustomButton_bc_stroke_color_disabled, strokeColor);
            strokeSize = attributes.getDimension(R.styleable.CustomButton_bc_stroke_size, 0);

            isElevationEnabled = attributes.getBoolean(R.styleable.CustomButton_bc_elevation_enabled, true);

            inflate(context, R.layout.button_custom, this);

            textView = (TextView) findViewById(R.id.text);
            container = (FrameLayout) findViewById(R.id.container);

            ColorStateList textColorList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_enabled}, new int[]{}},
                    new int[]{textColorPressed, textColor, textColorDisabled});

            textView.setText(text);
            if (textSize > 0) textView.setTextSize(textSize);
            if (textColor != 0) textView.setTextColor(textColorList);
            else if (textColorState != null) textView.setTextColor(textColorState);

            int shapeTypeDecision = (shapeType == 0) ? GradientDrawable.RECTANGLE : GradientDrawable.OVAL;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                StateListDrawable stateListDrawable = new StateListDrawable();
                GradientDrawable drawableNormal = new GradientDrawable();
                GradientDrawable drawablePressed;
                GradientDrawable drawableDisabled;

                drawableNormal.setShape(shapeTypeDecision);
                drawableNormal.setCornerRadius(shapeRadius);

                drawablePressed = (GradientDrawable) drawableNormal.getConstantState().newDrawable().mutate();
                drawableDisabled = (GradientDrawable) drawableNormal.getConstantState().newDrawable().mutate();

                drawableNormal.setColor(background);
                drawablePressed.setColor(backgroundPressed);
                drawableDisabled.setColor(backgroundDisabled);
                drawableNormal.setStroke((int) strokeSize, strokeColor);
                drawablePressed.setStroke((int) strokeSize, strokeColorPressed);
                drawableDisabled.setStroke((int) strokeSize, strokeColorDisabled);

                stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
                stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, drawableNormal);
                stateListDrawable.addState(new int[]{}, drawableDisabled);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    container.setBackground(stateListDrawable);
                else container.setBackgroundDrawable(stateListDrawable);
            } else {
                GradientDrawable gd = new GradientDrawable();
                gd.setShape(shapeTypeDecision);
                gd.setCornerRadius(shapeRadius);
                gd.setColor(background);
                gd.setStroke((int) strokeSize, strokeColor);

                RippleDrawable drawable = new RippleDrawable(new ColorStateList(new int[][]{new int[]{}}, new int[]{backgroundPressed}), gd, null);

                container.setBackground(drawable);
                container.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, R.anim.elevation_button_custom));
                if (isElevationEnabled) {
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) container.getLayoutParams();
                    layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
                    layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
                    layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
                    layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
                    container.setLayoutParams(layoutParams);
                }
            }
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
}
