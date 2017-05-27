# Sigmapoint Custom Button

To start work with CustomButton you should clone or download this repository.
Next in Android Studio choose File -> New -> Import module.
Then select library folder in project and click import. </p>
```
min API 8
```

## Features

* Use only cb tags to change state colors of text, background, frame and drawable
* Add only one drawable and change it color dynamically
* Set text, background, frame and drawable color for normal, pressed and disabled state
* Image nad text position will be centered with relation to whole button

Example: 

![](https://github.com/Sigmapoint/pl.sigmapoint.view.CustomButton/blob/develop/resources/screen_2.png)

Custom Facebook button:
```xml
<pl.sigmapoint.customview.CustomButton
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="5"
        android:text="Log in with facebook"
        app:cb_image="@drawable/ic_facebook"
        app:cb_image_color_disabled="#c0c0c0"
        app:cb_image_color_normal="#FFFFFF"
        app:cb_image_color_pressed="#c4cde0"
        app:cb_image_padding="5dp"
        app:cb_image_position="left"
        app:cb_primary_color="#3B5998"
        app:cb_secondary_color="#17233c"
        app:cb_shape_radius="5dp"
        app:cb_text_color="#FFFFFF" />
```

## Reference 
Default image position is left. Default text position is center.
When image is bigger than half of width, then weight automatically is setting to 1.
Only when image is in horizontal position text can be center. 
### XML tag's

This is the base tag of CustomButton:
```xml
<pl.sigmapoint.customview.CustomButton
     android:id="@+id/sigmapoint_button"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:text="text"/>
```

You can specify the following things: 
```
cb_primary_color - you can specify only two colors for all button. This color was set to bacground state normal,
frame and text sate pressed. But it will be override by background, text, frame color. {format - color}
cb_secondary_color - you can specify only two colors for all button. This color was set to bacground state pressed, frame and text state normal. But it will be override by background, text, frame color. {format - color}
cb_background - background color of normal button  {format - color}
cb_background_pressed - background color of pressed button {format - color}
cb_background_disabled - background color of disabled button {format - color}
cb_background_state_list - {format - ColorStateList}
cb_text_color_pressed - color of pressed text {format - color} 
cb_text_color_disabled - color of disabled text {format - color} 
cb_text_color or android:textColor - color of normal text {format - color}
cb_text_size - {format - dimension} 
cb_text_padding - {format - dimension} 
cb_text_padding_left - {format - dimension} 
cb_text_padding_top - {format - dimension} 
cb_text_padding_right - {format - dimension} 
cb_text_padding_bottom - {format - dimension} 
cb_text_weight - {format - integer}
cb_text_center or android:textAlignment - only works when image weight doesn't specify. Center text in button. {format - boolean or only "center" value} 

cb_shape_radius - corner radius {format - dimension}
cb_shape_type - shape type, you can choose: rect or oval 
cb_frame_color - {format - color}
cb_frame_color_pressed - {format - color}
cb_frame_color_disabled - {format - color}
cb_frame_size - frame height {format - dimension}
cb_frame_state_list - {format - ColorStateList}

cb_elevation_enabled - on >=API 21 devices enable elvation (default) {format - boolean}

cb_image_position - image position in button, can choose left, top, right, bottom 
cb_image - image source {format - drawable or color}
cb_image_normal - normal state image source (if you choose cb_image, it will be override) {format - drawable or color}
cb_image_disabled - disabled state image source (if you choose cb_image, it will be override) {format - drawable or color} 
cb_image_pressed - pressed state image source (if you choose cb_image, it will be override) {format - drawable or color} 
cb_image_color_normal - only for image {format - color}
cb_image_color_pressed - only for image {format - color}
cb_image_color_disable - only for image {format - color}
cb_image_color_list - only for image {format - colorStateList}
cb_image_padding - {format - dimension}
cb_image_padding_left - {format - dimension}
cb_image_padding_top - {format - dimension}
cb_image_padding_right - {format - dimension} 
cb_image_padding_bottom - {format - dimension}
cb_image_scale_type - {center, center_inside, center_crop, fit_center, fit_start, fit_end, fit_xy}
cb_image_weight - {format - integer}
```
### Java Code
```java
CustomButton(Context context, LayoutParams layoutParams, int primaryColor, int secondaryColor, Drawable icon)
setPrimaryColor(int color)
setSecondaryColor(int color)
setMainColors(int primaryColor, int secondaryColor)
setShapeBackground(int shapeType, int shapeRadius)
setBackgroundColorStateList(ColorStateList colorStateList)
setBackgroundColor(int color)
setBackgroundColorNormal(int color)
setBackgroundColorPressed(int color)
setBackgroundColorDosabled(int color)
setFrame(int color, float size)
setFrame(ColorStateList colorStateList, float size)
setFrameColor(int color)
setFrameColorNormal(int color)
setFrameColorPressed(int color)
setFrameColorDisabled(int color)
setFrameSize(float frameSize)
removeFrame() 
setText(String text)
setTextCenter(boolean center)
setTextColor(int color)
setTextColorNormal(int color)
setTextColorPressed(int color)
setTextColorDisabled(int color)
setTextColor(ColorStateList colorStateList)
setTextSize(float size)
setTextPadding(int[] padding) - int[4]{CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM}
setTextParams(int weight, int[] padding)
setImage(int position, Drawable drawableNormal, Drawable drawablePressed, Drawable drawableDisabled, ImageView.ScaleType scaleType, int weight, int[] padding)
setImage(int position, Drawable drawable, ImageView.ScaleType scaleType, int weight, int[] padding)
setImageColor(int color)
setImageColors(int nomal, int pressed, int disabled)
setImageNormalColor(int color)
setImagePressedColor(int color)
setImageDisableColor(int color)
setElevationEnabled(boolean enabled) 
and evry XML tag have getter
```
### Static field
LEFT, TOP, RIGHT, BOTTOM - for image position 

#### Tips 
If you add image to the button, set frame and want to center text without respect frame size you should set text padding same as frame size.

## Dependencies
In demo application  [Ambil Warna](https://github.com/yukuku/ambilwarna) Library was used.

##License

CustomButton is licensed under the MIT license. (http://opensource.org/licenses/MIT)
