package com.utility.customView;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import java.util.HashMap;

/** Customizing AutoCompleteTextView to return Value corresponding to the selected item */
public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {
	
	public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** Returns the value corresponding to the selected item */
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		/* Each item in the autoCompleteTextView suggestion list is a hashMap object */
		HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
		return hm.get("name");
	}
}
