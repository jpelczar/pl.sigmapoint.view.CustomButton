package pl.sigmapoint.customview;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jpelczar on 02.09.2015.
 */
public class CustomButton extends LinearLayout implements View.OnClickListener {

    //WARNING: If you want to change it, you should change it in attr xml too.
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    private final int COLOR_INDEX_PRESSED = 0;
    private final int COLOR_INDEX_NORMAL = 1;
    private final int COLOR_INDEX_DISABLED = 2;
    private final int[][] stateArray = new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_enabled}, new int[]{}}; //array for support states DON'T TOUCH THIS

    protected TextView textView; //text container
    protected LinearLayout container; //all content container
    protected ImageView imageContainer;

    private int primaryColor, secondaryColor;
    private int primaryColorLight, secondaryColorLight;
    private int backgroundColorPressed, backgroundColorDisabled, backgroundColorNormal; // colors for each backgroundColorNormal state
    private ColorStateList backgroundColorState; // color state list for backgroundColorNormal
    private int textColorPressed, textColorDisabled, textColorNormal; // colors for each text color state
    private ColorStateList textColorState; // color state list for text
    private float textSize;
    private String text; // text inside button
    private int textPadding;
    private int[] textPaddingArray;
    private int textWeight;
    private float shapeRadius; // corner radius
    private int shapeTypeAttr; // shape type (from xml - converted  to shape type constants)
    private int frameColorPressed, frameColorDisabled, frameColorNormal; // colors for each frame color state
    private ColorStateList frameColorState;
    private float frameSize; // frame size
    private boolean isElevationEnabled; // is elevation should be displayed >=API 21
    private Drawable drawableNormal, drawableDisabled, drawablePressed;
    private int drawableColorNormal, drawableColorDisabled, drawableColorPressed;
    private ColorStateList drawableColorStateListAttr;
    private int drawablePosition;
    private int imagePadding;
    private int[] imagePaddingArray;
    private int imageScaleTypeAttr;
    private int imageWeight;

    private int shapeType; // converted shape type from shapeTypeAttr
    private ImageView.ScaleType imageScaleType;
    private int[] textColorArray;
    private ColorStateList textColorList;

    private void init(Context context) {
        textPaddingArray = new int[4];
        imagePaddingArray = new int[4];
        textColorArray = new int[3];
        container = new LinearLayout(context);
        textView = new TextView(context);
        imageContainer = new ImageView(context);
    }

    public CustomButton(Context context, ViewGroup.LayoutParams params, int primaryColor, int secondaryColor, Drawable imageNormal) {
        super(context);
        init(context);
        setLayoutParams(params);

        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;

        this.drawableNormal = imageNormal;

        updatePrimaryColor(primaryColor);
        updateSecondaryColor(secondaryColor);

        this.shapeType = 0;
        this.shapeRadius = 0;
        this.imageScaleType = ImageView.ScaleType.FIT_CENTER;

        if (drawableNormal != null) this.drawablePosition = LEFT; //default position
        else drawablePosition = -1;

        setContent();
        setButtonLayoutParams();

        setShapeBackground(); // set shape and backgroundColorNormal to button
        setElevationEnabled(true); // this method will work only for post-L android. Set elevation or disable it and set margins if needed

        setOnClickListener(this);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);

        try {

            primaryColor = attributes.getColor(R.styleable.CustomButton_cb_primary_color, Color.LTGRAY);
            secondaryColor = attributes.getColor(R.styleable.CustomButton_cb_secondary_color, Color.GRAY);

            backgroundColorNormal = attributes.getColor(R.styleable.CustomButton_cb_background, primaryColor);
            backgroundColorPressed = attributes.getColor(R.styleable.CustomButton_cb_background_pressed, secondaryColor);
            backgroundColorDisabled = attributes.getColor(R.styleable.CustomButton_cb_background_disabled, lighterColor(primaryColor));
            backgroundColorState = attributes.getColorStateList(R.styleable.CustomButton_cb_background_state_list);

            textColorNormal = attributes.getColor(R.styleable.CustomButton_cb_text_color, secondaryColor);
            textColorPressed = attributes.getColor(R.styleable.CustomButton_cb_text_color_pressed, primaryColor);
            textColorDisabled = attributes.getColor(R.styleable.CustomButton_cb_text_color_disabled, lighterColor(secondaryColor));
            textColorState = attributes.getColorStateList(R.styleable.CustomButton_android_textColor);
            textSize = attributes.getDimension(R.styleable.CustomButton_cb_text_size, 0);
            text = attributes.getString(R.styleable.CustomButton_android_text);
            textPadding = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding, 0);
            textPaddingArray[LEFT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_left, textPadding);
            textPaddingArray[TOP] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_top, textPadding);
            textPaddingArray[RIGHT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_right, textPadding);
            textPaddingArray[BOTTOM] = (int) attributes.getDimension(R.styleable.CustomButton_cb_text_padding_bottom, textPadding);
            textWeight = attributes.getInteger(R.styleable.CustomButton_cb_text_weight, 1);

            shapeRadius = attributes.getDimension(R.styleable.CustomButton_cb_shape_radius, 0);
            shapeTypeAttr = attributes.getInt(R.styleable.CustomButton_cb_shape_type, 0);
            frameColorNormal = attributes.getColor(R.styleable.CustomButton_cb_frame_color, secondaryColor);
            frameColorPressed = attributes.getColor(R.styleable.CustomButton_cb_frame_color_pressed, primaryColor);
            frameColorDisabled = attributes.getColor(R.styleable.CustomButton_cb_frame_color_disabled, lighterColor(secondaryColor));
            frameColorState = attributes.getColorStateList(R.styleable.CustomButton_cb_frame_state_list);
            frameSize = attributes.getDimension(R.styleable.CustomButton_cb_frame_size, 0);

            isElevationEnabled = attributes.getBoolean(R.styleable.CustomButton_cb_elevation_enabled, true);

            drawablePosition = attributes.getInteger(R.styleable.CustomButton_cb_image_position, -1);
            drawableNormal = attributes.getDrawable(R.styleable.CustomButton_cb_image);
            if (drawableNormal == null)
                drawableNormal = attributes.getDrawable(R.styleable.CustomButton_cb_image_normal);
            drawablePressed = attributes.getDrawable(R.styleable.CustomButton_cb_image_pressed);
            drawableDisabled = attributes.getDrawable(R.styleable.CustomButton_cb_image_disabled);
            drawableColorStateListAttr = attributes.getColorStateList(R.styleable.CustomButton_cb_image_color_list);
            drawableColorNormal = attributes.getColor(R.styleable.CustomButton_cb_image_color_normal, Integer.MAX_VALUE);
            drawableColorPressed = attributes.getColor(R.styleable.CustomButton_cb_image_color_pressed, Integer.MAX_VALUE);
            drawableColorDisabled = attributes.getColor(R.styleable.CustomButton_cb_image_color_disabled, Integer.MAX_VALUE);
            imageScaleTypeAttr = attributes.getInteger(R.styleable.CustomButton_cb_image_scale_type, 3);
            imagePadding = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding, 0);
            imagePaddingArray[LEFT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_left, imagePadding);
            imagePaddingArray[TOP] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_top, imagePadding);
            imagePaddingArray[RIGHT] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_right, imagePadding);
            imagePaddingArray[BOTTOM] = (int) attributes.getDimension(R.styleable.CustomButton_cb_image_padding_bottom, imagePadding);
            imageWeight = attributes.getInteger(R.styleable.CustomButton_cb_image_weight, 0);

            if (backgroundColorState != null) { // if backgroundColorNormal state is not null the set color from color state list to specific colors
                backgroundColorStateListToIntegers(backgroundColorState);
            }

            if (frameColorState != null) { // if frame state is not null the set color from color state list to specific colors
                frameColorStateListToIntegers(frameColorState);
            }

            if (drawableColorStateListAttr != null)
                imageColorStateListToIntegers(drawableColorStateListAttr);
            setImageColorStateList();
            if (drawableNormal != null) {
                if (drawableColorNormal != Integer.MAX_VALUE) {
                    drawableNormal = changeDrawableColor(((BitmapDrawable) drawableNormal).getBitmap(), drawableColorNormal);
                }
                if (drawableColorPressed != Integer.MAX_VALUE) {
                    if (drawablePressed == null)
                        drawablePressed = drawableNormal.getConstantState().newDrawable().mutate();
                    drawablePressed = changeDrawableColor(((BitmapDrawable) drawablePressed).getBitmap(), drawableColorPressed);
                }
                if (drawableColorDisabled != Integer.MAX_VALUE) {
                    if (drawableDisabled == null)
                        drawableDisabled = drawableNormal.getConstantState().newDrawable().mutate();
                    drawableDisabled = changeDrawableColor(((BitmapDrawable) drawableDisabled).getBitmap(), drawableColorDisabled);
                }
            }

            //WARNING: If you want to change it, you should change it in attr xml too
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

            setContent();

            shapeType = (shapeTypeAttr == 0) ? GradientDrawable.RECTANGLE : GradientDrawable.OVAL; // convert xml attributes value to shape type constant

            setShapeBackground(); // set shape and backgroundColorNormal to button
            setElevationEnabled(isElevationEnabled); // this method will work only for post-L android. Set elevation or disable it and set margins if needed

        } finally {
            attributes.recycle();
        }

        setOnClickListener(this);

        setButtonLayoutParams();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textView.setEnabled(enabled); //chain child views with parent state
        container.setEnabled(enabled);
        imageContainer.setEnabled(enabled);
    }

    private void setContent() {
        removeAllViews();
        container.removeAllViews();

        if (container.getLayoutParams() == null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.setLayoutParams(layoutParams);
        }

        container.setOrientation(VERTICAL);

        setTextPadding(textPaddingArray);

        LayoutParams layoutParamsText;
        LayoutParams layoutParamsImage;
        if (drawablePosition >= 0 || drawablePosition <= 3 && drawableNormal != null) {
            int width = (drawablePosition % 2 == 0) ? 0 : ViewGroup.LayoutParams.MATCH_PARENT;
            int height = (drawablePosition % 2 == 0) ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
            layoutParamsText = new LinearLayout.LayoutParams(width, height);
            layoutParamsImage = new LayoutParams(width, height);
            layoutParamsText.weight = (text != null) ? ((textWeight == 0) ? 1 : textWeight) : 0;
            textWeight = (int) layoutParamsText.weight;
            if (imageWeight > 0) {
                layoutParamsImage = new LinearLayout.LayoutParams(width, height);
                layoutParamsImage.weight = imageWeight;
                imageWeight = (int) layoutParamsImage.weight;
            } else {
                if (text == null) {
                    layoutParamsImage.weight = imageWeight;
                    imageWeight = (int) layoutParamsImage.weight;
                }
            }
            textView.setLayoutParams(layoutParamsText);
            imageContainer.setLayoutParams(layoutParamsImage);

            if (drawablePressed == null)
                drawablePressed = drawableNormal.getConstantState().newDrawable().mutate();
            if (drawableDisabled == null)
                drawablePressed = drawableNormal.getConstantState().newDrawable().mutate();

            StateListDrawable listDrawable = new StateListDrawable();
            listDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
            listDrawable.addState(new int[]{android.R.attr.state_enabled}, drawableNormal);
            listDrawable.addState(new int[]{}, drawableDisabled);

            if (imagePaddingArray != null)
                imageContainer.setPadding(imagePaddingArray[LEFT], imagePaddingArray[TOP], imagePaddingArray[RIGHT], imagePaddingArray[BOTTOM]);
            imageContainer.setScaleType(imageScaleType);

            imageContainer.setImageDrawable(listDrawable);

            switch (drawablePosition) {
                case LEFT:
                    container.setOrientation(HORIZONTAL);
                    container.addView(imageContainer);
                    break;
                case RIGHT:
                    container.setOrientation(HORIZONTAL);
                    break;
                case TOP:
                    container.addView(imageContainer);
                    break;
            }
            if (text != null) {
                textView.setGravity(Gravity.CENTER);
                container.addView(textView);
            }

            switch (drawablePosition) {
                case RIGHT:
                    container.addView(imageContainer);
                    break;
                case BOTTOM:
                    container.addView(imageContainer);
                    break;
            }

        } else {
            layoutParamsText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParamsText);
            if (text != null) {
                textView.setGravity(Gravity.CENTER);
                container.addView(textView);
            }
        }

        addView(container);

        updateText();
    }

    /**
     * Set shape backgrounds to button. Used global backgroundColorNormal variables.
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
        drawableNormal.setColor(backgroundColorNormal);
        drawablePressed.setColor(backgroundColorPressed);
        drawableDisabled.setColor(backgroundColorDisabled);
        if (frameSize > 0) {
            drawableNormal.setStroke((int) frameSize, frameColorNormal);
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

            RippleDrawable drawable = new RippleDrawable(new ColorStateList(new int[][]{new int[]{}}, new int[]{backgroundColorPressed}), stateListDrawable, null);
            container.setBackground(drawable);
        }
    }

    /**
     * Convert color state list to three integers. It is helper method to set backgroundColorNormal color.
     *
     * @param colorStateList is a backgroundColorNormal color state list which should be converted. Should have three states:
     *                       enabled - for normal state,
     *                       pressed - for pressed state,
     *                       (empty) - for disabled state.
     */
    private void backgroundColorStateListToIntegers(ColorStateList colorStateList) {

        int globalColor = colorStateList.getColorForState(new int[]{}, 0);

        backgroundColorPressed = colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, globalColor);
        backgroundColorNormal = colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, globalColor);
        backgroundColorDisabled = globalColor;
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
        frameColorNormal = colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, globalColor);
        frameColorDisabled = globalColor;
    }

    private void imageColorStateListToIntegers(ColorStateList colorStateList) {

        int globalColor = colorStateList.getColorForState(new int[]{}, 0);

        drawableColorPressed = colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, globalColor);
        drawableColorNormal = colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, globalColor);
        drawableColorDisabled = globalColor;
    }

    private void updateText() {
        textColorArray = new int[]{textColorPressed, textColorNormal, textColorDisabled};
        textColorList = new ColorStateList(stateArray, textColorArray);

        if (text != null) {
            textView.setText(text);
            if (textSize > 0) textView.setTextSize(textSize);
            if (textColorNormal != 0) textView.setTextColor(textColorList);
            else if (textColorState != null) textView.setTextColor(textColorState);
        } else {
            container.removeView(textView);
        }
    }

    private void updatePrimaryColor(int color) {

        this.primaryColor = color;
        this.primaryColorLight = lighterColor(color);

        this.backgroundColorNormal = color;
        this.textColorPressed = color;
        this.frameColorPressed = color;
        this.textColorDisabled = primaryColorLight;
        this.frameColorDisabled = primaryColorLight;
    }

    private void updateSecondaryColor(int color) {

        this.secondaryColor = color;
        this.secondaryColorLight = lighterColor(color);

        this.backgroundColorPressed = color;
        this.textColorNormal = color;
        this.frameColorNormal = color;
        this.backgroundColorDisabled = secondaryColorLight;
    }

    private int lighterColor(int color) {

        int redPC = Color.red(color);
        int greenPC = Color.green(color);
        int blueRC = Color.blue(color);
        float hsv[] = new float[3];

        Color.RGBToHSV(redPC, greenPC, blueRC, hsv);
        hsv[1] = (hsv[1] - 0.3f) < 0 ? 0.1f : hsv[1] - 0.3f;
        return Color.HSVToColor(hsv);
    }

    private void setImageColorStateList() {
        if (drawableNormal != null) {
            if (drawableColorNormal != Integer.MAX_VALUE) {
                drawableNormal = changeDrawableColor(((BitmapDrawable) drawableNormal).getBitmap(), drawableColorNormal);
            }
            if (drawableColorPressed != Integer.MAX_VALUE) {
                if (drawablePressed == null)
                    drawablePressed = drawableNormal.getConstantState().newDrawable().mutate();
                drawablePressed = changeDrawableColor(((BitmapDrawable) drawablePressed).getBitmap(), drawableColorPressed);
            }
            if (drawableColorDisabled != Integer.MAX_VALUE) {
                if (drawableDisabled == null)
                    drawableDisabled = drawableNormal.getConstantState().newDrawable().mutate();
                drawableDisabled = changeDrawableColor(((BitmapDrawable) drawableDisabled).getBitmap(), drawableColorDisabled);
            }
        }
    }

    private void setButtonLayoutParams() {
        if (drawableNormal != null && imageWeight == 0)
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LayoutParams layoutParams = (LayoutParams) imageContainer.getLayoutParams();
                    float scale;
                    if (drawablePosition == LEFT || drawablePosition == RIGHT) {
                        scale = ((float) container.getHeight()) / ((float) drawableNormal.getIntrinsicHeight());
                        layoutParams.width = (int) (scale * drawableNormal.getIntrinsicWidth());
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        if (layoutParams.width > container.getWidth() / 2) {
                            layoutParams.width = 0;
                            layoutParams.weight = 1;
                            imageWeight = 1;
                        } else {
                            if (drawablePosition == LEFT)
                                ((LayoutParams) textView.getLayoutParams()).rightMargin = layoutParams.width;
                            else
                                ((LayoutParams) textView.getLayoutParams()).leftMargin = layoutParams.width;
                        }
                        if (text == null) {
                            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            layoutParams.width = 0;
                            layoutParams.weight = 1;
                            imageWeight = 1;
                        }
                    } else {
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = 0;
                        layoutParams.weight = 1;
                        imageWeight = 1;
                    }
                    imageContainer.setLayoutParams(layoutParams);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    else
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
    }

    private Drawable changeDrawableColor(Bitmap bitmap, int color) {
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Paint paint = new Paint();
        paint.setShader(shader);
        ColorFilter filter = new LightingColorFilter(color, color);
        paint.setColorFilter(filter);
        Bitmap b = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(b);
        canvas.drawPaint(paint);
        return new BitmapDrawable(b);
    }

    /**
     * Set shape backgroundColorNormal.
     *
     * @param shapeType   only GradientDrawable.OVAL or RECTANGLE
     * @param shapeRadius is corner radius
     */
    public void setShapeBackground(int shapeType, int shapeRadius) {
        this.shapeType = shapeType;
        this.shapeRadius = shapeRadius;
        setShapeBackground();
    }

    public void setPrimaryColor(int color) {

        updatePrimaryColor(color);

        updateText();
        setShapeBackground();
    }

    public void setSecondaryColor(int color) {

        updateSecondaryColor(color);

        updateText();
        setShapeBackground();
    }

    public void setMainColors(int primaryColor, int secondaryColor) {

        updatePrimaryColor(primaryColor);
        updateSecondaryColor(secondaryColor);

        updateText();
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

    public void setBackgroundColor(int color) {
        this.backgroundColorNormal = color;
        this.backgroundColorDisabled = color;
        this.backgroundColorPressed = color;
        setShapeBackground();
    }

    public void setBackgroundColorPressed(int backgroundColorPressed) {
        this.backgroundColorPressed = backgroundColorPressed;
        setShapeBackground();
    }

    public void setBackgroundColorDisabled(int backgroundColorDisabled) {
        this.backgroundColorDisabled = backgroundColorDisabled;
        setShapeBackground();
    }

    public void setBackgroundColorNormal(int backgroundColorNormal) {
        this.backgroundColorNormal = backgroundColorNormal;
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
        frameColorNormal = color;
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

    public void setFrameColorPressed(int frameColorPressed) {
        this.frameColorPressed = frameColorPressed;
        setShapeBackground();
    }

    public void setFrameColorDisabled(int frameColorDisabled) {
        this.frameColorDisabled = frameColorDisabled;
        setShapeBackground();
    }

    public void setFrameColorNormal(int frameColor) {
        this.frameColorNormal = frameColor;
        setShapeBackground();
    }

    public void setFrameColor(int frameColor) {
        this.frameColorNormal = frameColor;
        this.frameColorDisabled = frameColor;
        this.frameColorPressed = frameColor;
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
     * Set text in button.
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        textView.setText(text);
        if (textView.getParent() != container) {
            setContent();
        }
    }

    /**
     * Set text color.
     *
     * @param color integer color number
     */
    public void setTextColorNormal(int color) {
        textColorNormal = color;
        textColorArray[COLOR_INDEX_NORMAL] = color;
        textColorState = new ColorStateList(stateArray, textColorArray);
        textView.setTextColor(textColorState);
    }

    public void setTextColorPressed(int textColorPressed) {
        this.textColorPressed = textColorPressed;
        textColorArray[COLOR_INDEX_PRESSED] = textColorPressed;
        textColorState = new ColorStateList(stateArray, textColorArray);
        textView.setTextColor(textColorState);
    }

    public void setTextColorDisabled(int textColorDisabled) {
        this.textColorDisabled = textColorDisabled;
        textColorArray[COLOR_INDEX_DISABLED] = textColorDisabled;
        textColorState = new ColorStateList(stateArray, textColorArray);
        textView.setTextColor(textColorState);
    }

    /**
     * Set text color.
     *
     * @param color integer color number
     */
    public void setTextColor(int color) {
        textColorNormal = color;
        textColorPressed = color;
        textColorDisabled = color;
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

    public void setTextSize(float size) {
        this.textSize = size;
        textView.setTextSize(size);
    }

    public void setTextParams(int weight, int[] padding) {
        this.textWeight = weight;
        this.textPaddingArray = padding;
        ((LayoutParams) textView.getLayoutParams()).weight = weight;
        if (textPaddingArray != null)
            textView.setPadding(textPaddingArray[LEFT], textPaddingArray[TOP], textPaddingArray[RIGHT], textPaddingArray[BOTTOM]);
    }

    /**
     * Set text view padding
     *
     * @param padding 4 elements array {CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM}
     */
    public void setTextPadding(int[] padding) {
        if (padding != null)
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
    public void setImage(int position, Drawable drawableNormal, Drawable drawablePressed, Drawable drawableDisabled, ImageView.ScaleType scaleType, int weight, int[] padding) {
        this.drawableDisabled = drawableDisabled;
        this.drawablePressed = drawablePressed;
        this.drawableNormal = drawableNormal;
        this.drawablePosition = position;
        this.imageScaleType = scaleType;
        this.imageWeight = weight;
        this.imagePaddingArray = padding;

        if (padding != null)
            imageContainer.setPadding(padding[LEFT], padding[TOP], padding[RIGHT], padding[BOTTOM]);
        else
            imageContainer.setPadding(0, 0, 0, 0);
        if (scaleType != null)
            imageContainer.setScaleType(scaleType);

        setContent();
        setButtonLayoutParams();
    }

    /**
     * Set image to button
     *
     * @param position  CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM
     * @param drawable  image resource
     * @param scaleType all without MATRIX
     * @param padding   4 elements array {CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM}
     */
    public void setImage(int position, Drawable drawable, ImageView.ScaleType scaleType, int weight, int[] padding) {
        setImage(position, drawable, drawable, drawable, scaleType, weight, padding);
    }

    public void setImageColors(int normal, int pressed, int disabled) {
        this.drawableColorNormal = normal;
        this.drawableColorPressed = pressed;
        this.drawableColorDisabled = disabled;

        setImageColorStateList();
        setContent();
    }

    public void setImageColors(ColorStateList colorStateList) {
        imageColorStateListToIntegers(colorStateList);
        setImageColorStateList();
        setContent();
    }

    public void setImageNormalColor(int color) {
        this.drawableColorNormal = color;
        setImageColorStateList();
        setContent();
    }

    public void setImagePressedColor(int color) {
        this.drawableColorPressed = color;
        setImageColorStateList();
        setContent();
    }

    public void setImageDisableColor(int color) {
        this.drawableColorDisabled = color;
        setImageColorStateList();
        setContent();
    }

    /**
     * Set elevation to button. If enabled button is smaller because shadow must have space to show.
     *
     * @param enabled true - enable elevation
     * @since API 21
     */
    public void setElevationEnabled(boolean enabled) {

        isElevationEnabled = enabled;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

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

    public boolean isElevationEnabled() {
        return isElevationEnabled;
    }

    public int getBackgroundColorPressed() {
        return backgroundColorPressed;
    }

    public int getBackgroundColorDisabled() {
        return backgroundColorDisabled;
    }

    public int getBackgroundColorNormal() {
        return backgroundColorNormal;
    }

    public int getBackgroundColor() {
        return backgroundColorNormal;
    }

    public ColorStateList getBackgroundColorState() {
        return backgroundColorState;
    }

    public String getText() {
        return String.valueOf(textView.getText());
    }

    public int getTextColorPressed() {
        return textColorPressed;
    }

    public int getTextColorDisabled() {
        return textColorDisabled;
    }

    public int getTextColorNormal() {
        return textColorNormal;
    }

    public int getTextColor() {
        return textColorNormal;
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
        return frameColorNormal;
    }

    public ColorStateList getFrameColorState() {
        return frameColorState;
    }

    public float getFrameSize() {
        return frameSize;
    }

    public Drawable getDrawable() {
        return drawableNormal;
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

    public int getImageWeight() {
        return imageWeight;
    }

    public int getTextWeight() {
        return textWeight;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public int getImageColorNormal() {
        return drawableColorNormal;
    }

    public int getImageColorDisabled() {
        return drawableColorDisabled;
    }

    public int getImageColorPressed() {
        return drawableColorPressed;
    }
}