package pl.sigmapoint.customview;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jpelczar on 02.09.2015.
 */
public class CustomButton extends LinearLayout implements View.OnClickListener {

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    protected TextView textView; //text container
    protected LinearLayout container; //all content container
    protected RelativeLayout relativeContainer;
    protected ImageView imageContainer;

    private int backgroundPressed, backgroundDisabled, backgroundColor; // colors for each backgroundColor state
    private ColorStateList backgroundColorState; // color state list for backgroundColor
    private int textColorPressed, textColorDisabled, textColor; // colors for each text color state
    private ColorStateList textColorState; // color state list for text
    private float textSize; // text size
    private String text; // text inside button
    private int textPadding;
    private int[] textPaddingArray;
    private float shapeRadius; // corner radius
    private int shapeTypeAttr; // shape type (from xml - converted  to shape type constants)
    private int frameColorPressed, frameColorDisabled, frameColor; // colors for each frame color state
    private ColorStateList frameColorState;
    private float frameSize; // frame size
    private boolean isElevationEnabled; // is elevation should be displayed >=API 21
    private Drawable drawable, drawableNormal, drawableDisabled, drawablePressed;
    private int drawablePosition;
    private int imagePadding;
    private int[] imagePaddingArray;
    private int imageScaleTypeAttr;

    private int shapeType; // converted shape type from shapeTypeAttr
    private ImageView.ScaleType imageScaleType;

    //TODO stosunek podziału image / text

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaddingArray = new int[4];
        imagePaddingArray = new int[4];
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);

        try {
            backgroundColor = attributes.getColor(R.styleable.CustomButton_cb_background, 0);
            backgroundPressed = attributes.getColor(R.styleable.CustomButton_cb_background_pressed, backgroundColor);
            backgroundDisabled = attributes.getColor(R.styleable.CustomButton_cb_background_disabled, backgroundColor);
            backgroundColorState = attributes.getColorStateList(R.styleable.CustomButton_cb_background_state_list);

            textColor = attributes.getColor(R.styleable.CustomButton_cb_text_color, 0);
            textColorPressed = attributes.getColor(R.styleable.CustomButton_cb_text_color_pressed, textColor);
            textColorDisabled = attributes.getColor(R.styleable.CustomButton_cb_text_color_disabled, textColor);
            textColorState = attributes.getColorStateList(R.styleable.CustomButton_android_textColor);
            textSize = attributes.getDimension(R.styleable.CustomButton_cb_text_size, 0);
            text = attributes.getString(R.styleable.CustomButton_android_text);
            textPadding = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding, 0);
            textPaddingArray[LEFT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_left, textPadding);
            textPaddingArray[TOP] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_top, textPadding);
            textPaddingArray[RIGHT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_right, textPadding);
            textPaddingArray[BOTTOM] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_bottom, textPadding);

            shapeRadius = attributes.getDimension(R.styleable.CustomButton_cb_shape_radius, 0);
            shapeTypeAttr = attributes.getInt(R.styleable.CustomButton_cb_shape_type, 0);
            frameColor = attributes.getColor(R.styleable.CustomButton_cb_frame_color, 0);
            frameColorPressed = attributes.getColor(R.styleable.CustomButton_cb_frame_color_pressed, frameColor);
            frameColorDisabled = attributes.getColor(R.styleable.CustomButton_cb_frame_color_disabled, frameColor);
            frameColorState = attributes.getColorStateList(R.styleable.CustomButton_cb_frame_state_list);
            frameSize = attributes.getDimension(R.styleable.CustomButton_cb_frame_size, 0);

            isElevationEnabled = attributes.getBoolean(R.styleable.CustomButton_cb_elevation_enabled, true);

            drawablePosition = attributes.getInteger(R.styleable.CustomButton_cb_image_position, -1);
            drawable = attributes.getDrawable(R.styleable.CustomButton_cb_image);
            drawableNormal = attributes.getDrawable(R.styleable.CustomButton_cb_image_normal);
            drawablePressed = attributes.getDrawable(R.styleable.CustomButton_cb_image_pressed);
            drawableDisabled = attributes.getDrawable(R.styleable.CustomButton_cb_image_disabled);
            imageScaleTypeAttr = attributes.getInteger(R.styleable.CustomButton_cb_image_scale_type, 0);
            imagePadding = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding, 0);
            imagePaddingArray[LEFT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_left, imagePadding);
            imagePaddingArray[TOP] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_top, imagePadding);
            imagePaddingArray[RIGHT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_right, imagePadding);
            imagePaddingArray[BOTTOM] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_bottom, imagePadding);

            if (backgroundColorState != null) { // if backgroundColor state is not null the set color from color state list to specific colors
                backgroundColorStateListToIntegers(backgroundColorState);
            }

            if (frameColorState != null) { // if frame state is not null the set color from color state list to specific colors
                frameColorStateListToIntegers(frameColorState);
            }

            switch (imageScaleTypeAttr) {
                case 0:
                    imageScaleType = ImageView.ScaleType.CENTER;
                    break;
                case 1:
                    imageScaleType = ImageView.ScaleType.CENTER_INSIDE;
                    break;
                case 2:
                    imageScaleType = ImageView.ScaleType.CENTER_CROP;
                    break;
                case 3:
                    imageScaleType = ImageView.ScaleType.FIT_CENTER;
                    break;
                case 4:
                    imageScaleType = ImageView.ScaleType.FIT_START;
                    break;
                case 5:
                    imageScaleType = ImageView.ScaleType.FIT_END;
                    break;
                case 6:
                    imageScaleType = ImageView.ScaleType.FIT_XY;
                    break;
            }

            setContent(context);

            shapeType = (shapeTypeAttr == 0) ? GradientDrawable.RECTANGLE : GradientDrawable.OVAL; // convert xml attributes value to shape type constant

            setShapeBackground(); // set shape and backgroundColor to button
            setElevationEnabled(isElevationEnabled); // this method will work only for post-L android. Set elevation or disable it and set margins if needed

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
        textView.setEnabled(enabled); //chain child views with parent state
        container.setEnabled(enabled);
    }

    private void setContent(Context context) {
        removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView = new TextView(context);
        setTextPadding(textPaddingArray);
        container = new LinearLayout(context);
        container.setLayoutParams(layoutParams);
        container.setOrientation(VERTICAL);
        layoutParams.weight = 1;

        if (drawablePosition < 0 && drawablePosition > 4) {
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            container.addView(textView);
        } else {
            StateListDrawable listDrawable = new StateListDrawable();
            listDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
            listDrawable.addState(new int[]{android.R.attr.state_enabled}, drawableNormal);
            listDrawable.addState(new int[]{}, drawableDisabled);

            imageContainer = new ImageView(context);
            imageContainer.setPadding(imagePaddingArray[LEFT], imagePaddingArray[TOP], imagePaddingArray[RIGHT], imagePaddingArray[BOTTOM]);
            imageContainer.setScaleType(imageScaleType);
            imageContainer.setLayoutParams(layoutParams);
            if (drawable != null) {
                imageContainer.setImageDrawable(drawable);
            } else {
                imageContainer.setImageDrawable(listDrawable);
            }

            switch (drawablePosition) {
                case LEFT:
                    container.setOrientation(HORIZONTAL);
                    container.addView(imageContainer);
                    break;
                case TOP:
                    container.addView(imageContainer);
                    break;
            }
            if (text != null) {
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.CENTER);
                container.addView(textView);
            }

            switch (drawablePosition) {
                case RIGHT:
                    container.addView(imageContainer);
                    break;
                case BOTTOM:
                    container.setOrientation(HORIZONTAL);
                    container.addView(imageContainer);
                    break;
            }
        }

        addView(container);

        ColorStateList textColorList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_enabled}, new int[]{}},
                new int[]{textColorPressed, textColor, textColorDisabled});

        textView.setText(text);
        if (textSize > 0) textView.setTextSize(textSize);
        if (textColor != 0) textView.setTextColor(textColorList);
        else if (textColorState != null) textView.setTextColor(textColorState);
    }

    /**
     * Set shape backgrounds to button. Used global backgroundColor variables.
     */
    private void setShapeBackground() {

        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(shapeType);
        gd.setCornerRadius(shapeRadius);

        GradientDrawable drawableNormal = (GradientDrawable) gd.getConstantState().newDrawable().mutate();
        GradientDrawable drawablePressed = (GradientDrawable) gd.getConstantState().newDrawable().mutate();
        GradientDrawable drawableDisabled = (GradientDrawable) gd.getConstantState().newDrawable().mutate();

        //Set color from color state list work only >=API 21 devices
        drawableNormal.setColor(backgroundColor);
        drawablePressed.setColor(backgroundPressed);
        drawableDisabled.setColor(backgroundDisabled);
        if (frameSize > 0) {
            drawableNormal.setStroke((int) frameSize, frameColor);
            drawablePressed.setStroke((int) frameSize, frameColorPressed);
            drawableDisabled.setStroke((int) frameSize, frameColorDisabled);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
            stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, drawableNormal);
            stateListDrawable.addState(new int[]{}, drawableDisabled);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                container.setBackground(stateListDrawable);
            else
                container.setBackgroundDrawable(stateListDrawable);

        } else {
            stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, drawableNormal);
            stateListDrawable.addState(new int[]{}, drawableDisabled);

            RippleDrawable drawable = new RippleDrawable(new ColorStateList(new int[][]{new int[]{}}, new int[]{backgroundPressed}), stateListDrawable, null);
            container.setBackground(drawable);
        }
    }

    /**
     * Convert color state list to three integers. It is helper method to set backgroundColor color.
     *
     * @param colorStateList is a backgroundColor color state list which should be converted. Should have three states:
     *                       enabled - for normal state,
     *                       pressed - for pressed state,
     *                       (empty) - for disabled state.
     */
    private void backgroundColorStateListToIntegers(ColorStateList colorStateList) {

        int globalColor = colorStateList.getColorForState(new int[]{}, 0);

        backgroundPressed = colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, globalColor);
        backgroundColor = colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, globalColor);
        backgroundDisabled = globalColor;
    }

    /**
     * Convert color state list to three integers. It is helper method to set frame color.
     *
     * @param colorStateList is a frame color state list which should be converted. Should have three states:
     *                       enabled - for normal state,
     *                       pressed - for pressed state,
     *                       (empty) - for disabled state.
     */
    private void frameColorStateListToIntegers(ColorStateList colorStateList) {

        int globalColor = colorStateList.getColorForState(new int[]{}, 0);

        frameColorPressed = colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, globalColor);
        frameColor = colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, globalColor);
        frameColorDisabled = globalColor;
    }

    /**
     * Set shape backgroundColor.
     *
     * @param shapeType   only GradientDrawable.OVAL or RECTANGLE
     * @param shapeRadius is corner radius
     */
    public void setShapeBackground(int shapeType, int shapeRadius) {
        this.shapeType = shapeType;
        this.shapeRadius = shapeRadius;
        setShapeBackground();
    }

    /**
     * Set background color from color state list.
     * Only three states are use: disabled, pressed, normal.
     *
     * @param colorStateList is a background color state list. Should have three states:
     *                       enabled - for normal state,
     *                       pressed - for pressed state,
     *                       (empty) - for disabled state.
     */

    public void setBackgroundColorStateList(ColorStateList colorStateList) {

        backgroundColorStateListToIntegers(colorStateList);
        setShapeBackground();
    }

    /**
     * Set Frame color and size.
     *
     * @param color Integer frame color
     * @param size  Float frame size
     */

    public void setFrame(int color, float size) {
        frameSize = size;
        frameColor = color;
        frameColorDisabled = color;
        frameColorPressed = color;
        setShapeBackground();
    }

    /**
     * Set frame size and color from color state list.
     * Only three states are use: disabled, pressed, normal.
     *
     * @param colorStateList is a frame color state list. Should have three states:
     *                       enabled - for normal state,
     *                       pressed - for pressed state,
     *                       (empty) - for disabled state.
     * @param size           float frame size
     */
    public void setFrame(ColorStateList colorStateList, float size) {
        frameSize = size;
        frameColorStateListToIntegers(colorStateList);
        setShapeBackground();
    }

    /**
     * Set frame size
     *
     * @param frameSize frame height
     */
    public void setFrameSize(float frameSize) {
        this.frameSize = frameSize;
        setShapeBackground();
    }

    public void removeFrame() {
        frameSize = 0;
        setShapeBackground();
    }

    /**
     * Set text color.
     *
     * @param color integer color number
     */
    public void setTextColor(int color) {
        textColor = color;
        textView.setTextColor(color);
    }

    /**
     * Set text color from color state list.
     * Only three states are use: disabled, pressed, normal.
     *
     * @param colorStateList is a text color state list. Should have three states:
     *                       enabled - for normal state,
     *                       pressed - for pressed state,
     *                       (empty) - for disabled state.
     */
    public void setTextColor(ColorStateList colorStateList) {
        this.textColorState = colorStateList;
        textView.setTextColor(colorStateList);
    }

    /**
     * Set text in button.
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public void setTextSize(float size) {
        this.textSize = size;
        textView.setTextSize(size);
    }

    /**
     * Set text view padding
     *
     * @param padding 4 elements array {CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM}
     */
    public void setTextPadding(int[] padding) {
        textView.setPadding(padding[LEFT], padding[TOP], padding[RIGHT], padding[BOTTOM]);
    }

    /**
     * Set image to button
     *
     * @param position         CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM
     * @param drawableNormal   state normal
     * @param drawablePressed  state pressed
     * @param drawableDisabled state disabled
     * @param scaleType        all without MATRIX
     * @param padding          4 elements array {CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM}
     */
    public void setImage(int position, Drawable drawableNormal, Drawable drawablePressed, Drawable drawableDisabled, ImageView.ScaleType scaleType, int[] padding) {
        this.drawableDisabled = drawableDisabled;
        this.drawablePressed = drawablePressed;
        this.drawableNormal = drawableNormal;
        this.drawablePosition = position;
        this.imageScaleType = scaleType;

        if (padding != null)
            imageContainer.setPadding(padding[LEFT], padding[TOP], padding[RIGHT], padding[BOTTOM]);
        if (scaleType != null)
            imageContainer.setScaleType(scaleType);

        setContent(getContext());
    }

    /**
     * Set image to button
     *
     * @param position  CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM
     * @param drawable  image resource
     * @param scaleType all without MATRIX
     * @param padding   4 elements array {CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM}
     */
    public void setImage(int position, Drawable drawable, ImageView.ScaleType scaleType, int[] padding) {
        this.drawable = drawable;
        this.drawablePosition = position;

        if (padding != null)
            imageContainer.setPadding(padding[LEFT], padding[TOP], padding[RIGHT], padding[BOTTOM]);
        if (scaleType != null)
            imageContainer.setScaleType(scaleType);

        setContent(getContext());
    }

    /**
     * Set elevation to button. If enabled button is smaller because shadow must have space to show.
     *
     * @param enabled true - enable elevation
     * @since API 21
     */
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
            LayoutParams layoutParams = (LayoutParams) container.getLayoutParams();
            layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var, getResources().getDisplayMetrics());
            container.setLayoutParams(layoutParams);
        }
    }

    /**
     * Get background shape type.
     *
     * @return background shape type (GradientDrawable.RECTANGLE or OVAL)
     */
    public int getShapeType() {
        return shapeType;
    }

    /**
     * Get text from button.
     *
     * @return text from button
     */
    public String getText() {
        return String.valueOf(textView.getText());
    }

    /**
     * Check if elevation is enabled.
     * On pre-Lollipop return true.
     *
     * @return true if enabled
     */
    public boolean isElevationEnabled() {
        return isElevationEnabled;
    }

    public int getBackgroundPressed() {
        return backgroundPressed;
    }

    public int getBackgroundDisabled() {
        return backgroundDisabled;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public ColorStateList getBackgroundColorState() {
        return backgroundColorState;
    }

    public int getTextColorPressed() {
        return textColorPressed;
    }

    public int getTextColorDisabled() {
        return textColorDisabled;
    }

    public ColorStateList getTextColorState() {
        return textColorState;
    }

    public float getShapeRadius() {
        return shapeRadius;
    }

    public int getFrameColorPressed() {
        return frameColorPressed;
    }

    public int getFrameColorDisabled() {
        return frameColorDisabled;
    }

    public int getFrameColor() {
        return frameColor;
    }

    public ColorStateList getFrameColorState() {
        return frameColorState;
    }

    public float getFrameSize() {
        return frameSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public int getTextPadding() {
        return textPadding;
    }

    public int[] getTextPaddingArray() {
        return textPaddingArray;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public Drawable getDrawableNormal() {
        return drawableNormal;
    }

    public Drawable getDrawableDisabled() {
        return drawableDisabled;
    }

    public Drawable getDrawablePressed() {
        return drawablePressed;
    }

    public int getDrawablePosition() {
        return drawablePosition;
    }

    public int getImagePadding() {
        return imagePadding;
    }

    public int[] getImagePaddingArray() {
        return imagePaddingArray;
    }

    public ImageView.ScaleType getImageScaleType() {
        return imageScaleType;
    }
}