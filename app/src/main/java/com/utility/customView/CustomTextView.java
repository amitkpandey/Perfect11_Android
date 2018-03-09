package com.utility.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.perfect11.R;

// TODO: Auto-generated Javadoc

/**
 * The Class CustomTextView.
 */
public class CustomTextView extends AppCompatTextView {

    /**
     * The object of Context.
     */
    private final Context mContext;

    /**
     * Instantiates a new custom text view.
     *
     * @param context object
     */
    public CustomTextView(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * Instantiates a new custom text view.
     *
     * @param context object
     * @param attrs   the object of AttributeSet
     */
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initFromXML(context, attrs);
    }

    /**
     * Instantiates a new custom text view.
     *
     * @param context  object
     * @param attrs    the object of AttributeSet
     * @param defStyle is integer value
     */
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
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
     * Sets the font face.
     *
     * @param font_type the new font face
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

        //System.out.println("FF: " + fontType + "font_val " + font_type);
        if (font_type != -1) {
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + fontType);
            this.setTypeface(tf);
        }

    }
}
