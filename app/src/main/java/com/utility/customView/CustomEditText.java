package com.utility.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.perfect11.R;


/**
 * The Class CustomEditText.
 */
public class CustomEditText extends AppCompatEditText {

    /**
     * The object of Context.
     */
    private final Context mContext;

    /**
     * The keyboard close listener.
     */
    private OnKeyboardCloseListener keyboardCloseListener;

    /**
     * Instantiates a new custom edit text.
     *
     * @param context object
     */
    public CustomEditText(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * Instantiates a new custom edit text.
     *
     * @param context object
     * @param attrs   the object of AttributeSet
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initFromXML(context, attrs);
    }

    /**
     * Instantiates a new custom edit text.
     *
     * @param context  object
     * @param attrs    the object of AttributeSet
     * @param defStyle is integer value
     */
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initFromXML(context, attrs);
    }

    /**
     * Initialize from XML.
     *
     * @param context object
     * @param attrs   the object of AttributeSet
     */
    private void initFromXML(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);

            if (a.length() > 0) {
                for (int i = 0; i < a.length(); i++) {
                    int attr = a.getIndex(i);
                    if (attr == R.styleable.CustomFont_fontFace) {
                        int typefaceValue = a.getInteger(attr, -1);
                        setFontFace(typefaceValue);
                    }

                }
            }
            a.recycle();
        }

    }

    /**
     * Sets the on keyboard close listener.
     *
     * @param keyboardCloseListener the new on keyboard close listener
     */
    public void setOnKeyboardCloseListener(OnKeyboardCloseListener keyboardCloseListener) {
        this.keyboardCloseListener = keyboardCloseListener;
    }

    /**
     * The listener interface for receiving onKeyboardClose events.
     * The class that is interested in processing a onKeyboardClose
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addOnKeyboardCloseListener<code> method. When
     * the onKeyboardClose event occurs, that object's appropriate
     * method is invoked.
     *
     * @see OnKeyboardCloseListener
     */
    public interface OnKeyboardCloseListener {

        /**
         * On keyboard close.
         */
        void onKeyboardClose();
    }


    /* (non-Javadoc)
     * @see android.widget.TextView#onKeyPreIme(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyPreIme(int keyCode, @SuppressWarnings("NullableProblems") KeyEvent event) {
        System.out.println("--event.getKeyCode() " + event.getKeyCode());
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//			System.out.println("--event.getKeyCode() " + event.getKeyCode());
            if (keyboardCloseListener != null)
                keyboardCloseListener.onKeyboardClose();
            dispatchKeyEvent(event);
            return false;
//		} else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // System.out.println("-- enter key");
        }
//		System.out.println("--event.getKeyCode() " + event.getKeyCode());
        return super.onKeyPreIme(keyCode, event);
    }

    /**
     * Sets the font face.
     *
     * @param font_type is the integer value
     */
    private void setFontFace(int font_type) {
        String fontType;
        switch (font_type) {
            case 0:
                fontType = "roboto_condensed_regular.ttf";
                break;
            case 1:
                fontType = "roboto_condensed_bold.ttf";
                break;
            case 2:
                fontType = "roboto_condensed_light.ttf";
                break;
            default:
                fontType = "roboto_condensed_regular.ttf";
                break;
        }

        // System.out.println("FF: " + fontType + "font_val " + font_type);
        if (font_type != -1) {
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + fontType);
            this.setTypeface(tf);
        }

    }
}
