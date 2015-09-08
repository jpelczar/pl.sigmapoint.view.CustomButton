<h1> Sigmapoint Custom Button </h1>

<p> This button can help you to faster create button with background drawable, text color state, image inside button,
you only must specify colors, position, drawables, etc in xml button tags.</p>
<p> If you want start use this button, you must clone or download this repository. Next in Android Studio File -> New -> Import module.
Then select library folder from project. And start use. </p>
<p> >=API 8 </p>

<h2> Reference </h2>
<h3> XML tag's </h3>

This is the base tag of CustomButton:<br>
\<pl.sigmapoint.customview.CustomButton<br>
     &emsp;&emsp;   android:id="@+id/sigmapoint_button"<br>
     &emsp;&emsp;   android:layout_width="match_parent"<br>
     &emsp;&emsp;   android:layout_height="match_parent"<br>
     &emsp;&emsp;   android:text="text"/><br>
</br>
<p> You can specify the following things:<br>
cb_background - background color of normal button  {format - color} - recommended <br>
cb_background_pressed - background color of pressed button {format - color} <br>
cb_background_disabled - background color of disabled button {format - color} <br>
cb_background_state_list - {format - ColorStateList} <br>
<br>
cb_text_color_pressed - color of pressed text {format - color} <br>
cb_text_color_disabled - color of disabled text {format - color}  <br>
cb_text_color or android:textColor - color of normal text {format - color} <br>
cb_text_size - {format - dimension} <br>
cb_text_padding - {format - dimension} <br>
cb_text_padding_left - {format - dimension} <br>
cb_text_padding_top - {format - dimension} <br>
cb_text_padding_right - {format - dimension} <br>
cb_text_padding_bottom - {format - dimension} <br>
<br>
cb_shape_radius - corner radius {format - dimension}<br>
cb_shape_type - shape type, you can choose: rect or oval <br>
cb_frame_color - {format - color} <br>
cb_frame_color_pressed - {format - color} <br>
cb_frame_color_disabled - {format - color} <br>
cb_frame_size - frame height {format - dimension} <br>
cb_frame_state_list - {format - ColorStateList} <br>
<br>
cb_elevation_enabled - on >=API 21 devices enable elvation (default) {format - boolean} <br>
<br>
cb_image_position - image position in button, can choose left, top, right, bottom <br>
cb_image - image source {format - drawable or color} <br>
cb_image_normal - normal state image source (if you choose cb_image, it will be override) {format - drawable or color} <br>
cb_image_disabled - disabled state image source (if you choose cb_image, it will be override) {format - drawable or color} <br>
cb_image_pressed - pressed state image source (if you choose cb_image, it will be override) {format - drawable or color} <br>
cb_image_padding - {format - dimension} <br>
cb_image_padding_left - {format - dimension} <br>
cb_image_padding_top - {format - dimension} <br>
cb_image_padding_right - {format - dimension} <br>
cb_image_padding_bottom - {format - dimension} <br> </p>
<h3> Java Code </h3>
setShapeBackground(int shapeType, int shapeRadius) <br>
setBackgroundColorStateList(ColorStateList colorStateList) <br>
setFrame(int color, float size)	<br>
setFrame(ColorStateList colorStateList, float size) <br>
setFrameSize(float frameSize) <br>
removeFrame() <br>
setTextColor(int color) <br>
setTextColor(ColorStateList colorStateList) <br>
setText(String text) <br>
setTextSize(float size) <br>
setTextPadding(int[] padding) - int[4]{CustomButton.LEFT, CustomButton.TOP, CustomButton.RIGHT, CustomButton.BOTTOM} <br>
setImage(int position, Drawable drawableNormal, Drawable drawablePressed, Drawable drawableDisabled, int[] padding) <br>
setImage(int position, Drawable drawable, int[] padding) <br>
setElevationEnabled(boolean enabled) <br>
and evry XML tag have getter <br>
