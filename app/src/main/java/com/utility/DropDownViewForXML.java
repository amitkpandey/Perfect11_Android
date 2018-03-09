package com.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.perfect11.R;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class DropDownViewForXML.
 */
public class DropDownViewForXML extends AppCompatTextView implements OnClickListener, OnItemClickListener,
        OnDismissListener, OnItemSelectedListener {

    /**
     * The pw.
     */
    private PopupWindow pw;

    /**
     * The lv.
     */
    private ListView lv;

    /**
     * The adapter.
     */
    private PopupListItemAdapter adapter;

    /**
     * The list.
     */
    private List<DropMenuItem> list;

    /**
     * The selected position.
     */
    private int selectedPosition;

    /**
     * The on click listener.
     */
    private OnClickListener onClickListener = null;

    /**
     * The on item click listener.
     */
    private OnItemClickListener onItemClickListener;

    /**
     * The on item selected listener.
     */
    private OnItemSelectedListener onItemSelectedListener;

    /**
     * The popup width.
     */
    private int popupHeight, popupWidth;

    /**
     * The popup color.
     */
    private int popupColor = Color.WHITE;

    /**
     * The popup text color.
     */
    private int popupTextColor = Color.BLACK;
    // private final float[] r = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
    /**
     * The r.
     */
    private final float[] r = new float[]{1, 1, 1, 1, 1, 1, 1, 1};

    /**
     * The list drawable.
     */
    private Drawable listDrawable;

    /**
     * The m context.
     */
    private final Context mContext;

    private Typeface tf;

    /**
     * Instantiates a new drop down view for xml.
     *
     * @param context the context
     */
    public DropDownViewForXML(Context context) {
        super(context);
        mContext = context;
        init();
    }

    /**
     * Instantiates a new drop down view for xml.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DropDownViewForXML(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initFromXML(context, attrs);
        init();

    }

    /**
     * Instantiates a new drop down view for xml.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public DropDownViewForXML(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    /**
     * Inits the.
     */
    private void init() {
        if (!isInEditMode()) {
            if (listDrawable == null)
                listDrawable = new ColorDrawable(getResources().getColor(android.R.color.transparent));
            super.setOnClickListener(this);
        }
    }

    /**
     * Inits the from xml.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    private void initFromXML(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownViewForXML);

            if (a.length() > 0) {
                for (int i = 0; i < a.length(); i++) {
                    int attr = a.getIndex(i);
                    if (attr == R.styleable.CustomFont_fontFace) {
                        int typefaceValue = a.getInteger(attr, -1);
                        System.out.println("typefaceValue " + typefaceValue);
//                        setFontFace(typefaceValue);
                    }
                    if (attr == R.styleable.DropDownViewForXML_popupHeight) {
                        popupHeight = a.getInteger(attr, 150);
                        Log.d("popupHeight", "PopUpHeight: " + popupHeight);
                    }
                    if (attr == R.styleable.DropDownViewForXML_popupWidth) {
                        popupWidth = a.getInteger(attr, 250);
                        if (popupWidth == 0) {
                            popupWidth = getMeasuredWidth();
                        }
                        Log.d("popupHeight", "popupWidth: " + popupWidth);
                    }

                    if (attr == R.styleable.DropDownViewForXML_popup_background) {
                        Drawable drawable = a.getDrawable(R.styleable.DropDownViewForXML_popup_background);
                        if (drawable != null) {
                            listDrawable = drawable;
                        }

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
        String fontType = "";
        switch (font_type) {
            case 0:
                fontType = "ubuntu_regular.ttf";
                break;

            case 1:
                fontType = "ubuntu_blod.ttf";
                break;

            case 2:
                fontType = "ubuntu_italic.ttf";
                break;

            case 3:
                fontType = "ubuntu_light.ttf";
                break;

            case 4:
                fontType = "ubuntu_medium.ttf";
                break;

            case 5:
                fontType = "ubuntu_lightItalic.ttf";
                break;

            case 6:
                fontType = "ubuntu_mediumItalic.ttf";
                break;

            default:
                fontType = "ubuntu_regular.ttf";
                break;
        }

        System.out.println("FF: " + fontType + " font_val " + font_type);
        if (!"".equals(font_type)) {
            tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + fontType);
            this.setTypeface(tf);
        }
    }

    /**
     * Gets the round drawable.
     *
     * @return the round drawable
     */
    private Drawable getRoundDrawable() {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(popupColor);
        drawable.setShape(new RoundRectShape(r, null, null));
        return drawable;
    }

    /**
     * Sets the adapter.
     *
     * @param adapter the new adapter
     */
    private void setAdapter(PopupListItemAdapter adapter) {
        if (adapter != null) {
            this.adapter = adapter;

            Log.d("dropDown", "adapter not null....");

            if (lv == null) {
                lv = new ListView(getContext());
                lv.setFooterDividersEnabled(false);
                Log.d("dropDown", "ListView null....");
            }
            lv.setCacheColorHint(0);
            lv.setSelector(listDrawable);
            lv.setAdapter(adapter);
            lv.setDivider(new ColorDrawable(getResources().getColor(android.R.color.darker_gray)));
            lv.setDividerHeight(1);
//			lv.setFooterDividersEnabled(false);
            lv.setOnItemClickListener(this);
            lv.setOnItemSelectedListener(this);
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(5, 15, 5, 5);
            lv.setLayoutParams(layoutParams);
            selectedPosition = 0;
        } else {
            selectedPosition = -1;
            this.adapter = null;
        }
    }

    /**
     * Refresh view.
     */
    private void refreshView() {
        if (adapter != null) {
            DropMenuItem popupListItem = adapter.getItem(selectedPosition);
            setText(popupListItem.getItemTitle());
        }
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {
        if (onClickListener != null)
            onClickListener.onClick(v);
        if (adapter == null || lv == null)
            return;
        if (pw == null || !pw.isShowing()) {
            if (popupWidth == 0)
                popupWidth = getMeasuredWidth();
            if (popupHeight == 0)
                popupHeight = LayoutParams.WRAP_CONTENT;

            pw = new PopupWindow(v);
            lv.setPadding(0, 0, 0, 0);
            pw.setContentView(lv);
            pw.setWidth(popupWidth);
            pw.setHeight(popupHeight);
//             pw.setBackgroundDrawable(new BitmapDrawable());
            pw.setBackgroundDrawable(listDrawable);
            pw.setOutsideTouchable(true);
            pw.setFocusable(true);

            pw.setClippingEnabled(true);
            pw.showAsDropDown(v, 0, 0);
//				pw.showAsDropDown(v, v.getScrollX(), v.getScrollY());
            pw.setOnDismissListener(this);
//			}
        }
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (pw != null)
            pw.dismiss();
        selectedPosition = arg2;
        refreshView();
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(arg0, arg1, arg2, arg3);
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
     */
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (onItemSelectedListener != null)
            onItemSelectedListener.onItemSelected(arg0, arg1, arg2, arg3);
    }

    /**
     * The Class PopupListItemAdapter.
     */
    private class PopupListItemAdapter extends ArrayAdapter<DropMenuItem> {

        /**
         * The object list.
         */
        final List<DropMenuItem> objectList;

        /**
         * Instantiates a new popup list item adapter.
         *
         * @param context the context
         * @param objects the objects
         */
        public PopupListItemAdapter(Context context, List<DropMenuItem> objects) {
            super(context, android.R.layout.activity_list_item, objects);
            objectList = objects;
        }

        /* (non-Javadoc)
         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.drop_menu_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            DropMenuItem menuItem = objectList.get(position);

            viewHolder.tvText.setText(menuItem.getItemTitle());
            viewHolder.tvText.setTextColor(popupTextColor);
            Integer resDrawable = menuItem.getItemIcon();
            if (resDrawable != null) {
                viewHolder.ivIcon.setImageResource(menuItem.getItemIcon());
            } else {
                viewHolder.ivIcon.setVisibility(View.GONE);
            }
            return convertView;
        }

        /**
         * The Class ViewHolder.
         */
        public class ViewHolder {

            /**
             * The tv text.
             */
            final TextView tvText;

            /**
             * The iv icon.
             */
            final ImageView ivIcon;

            /**
             * Instantiates a new view holder.
             *
             * @param v the v
             */
            public ViewHolder(View v) {
                tvText = v.findViewById(R.id.tv_DropMenuTitle);
                ivIcon = v.findViewById(R.id.iv_DropMenuIcon);
            }

        }
    }

    /**
     * The Class DropMenuItem.
     */
    public class DropMenuItem {

        /**
         * The item title.
         */
        private String itemTitle;

        /**
         * The item icon.
         */
        private Integer itemIcon;

        /**
         * Gets the item title.
         *
         * @return the item title
         */
        public String getItemTitle() {
            return itemTitle;
        }

        /**
         * Sets the item title.
         *
         * @param itemTitle the new item title
         */
        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        /**
         * Gets the item icon.
         *
         * @return the item icon
         */
        public Integer getItemIcon() {
            return itemIcon;
        }

        /**
         * Sets the item icon.
         *
         * @param itemIcon the new item icon
         */
        public void setItemIcon(Integer itemIcon) {
            this.itemIcon = itemIcon;
        }
    }

    /**
     * Sets the items.
     *
     * @param arr the new items
     */
    public void setItems(String[] arr) {
        if (arr == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (String text : arr) {
            DropMenuItem menuItem = new DropMenuItem();
            menuItem.setItemIcon(null);
            menuItem.setItemTitle(String.valueOf(Html.fromHtml(text)));
            list.add(menuItem);
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }

    public void setItems(String arr) {
        String[] words = arr.split("@#@");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (String text : words) {
            DropMenuItem menuItem = new DropMenuItem();
            menuItem.setItemIcon(null);
            menuItem.setItemTitle(String.valueOf(Html.fromHtml(text)));
            list.add(menuItem);
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }

    /**
     * Sets the items.
     *
     * @param itemList the new items
     */
    public void setItems(List<String> itemList) {
        if (itemList == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (String text : itemList) {
            DropMenuItem menuItem = new DropMenuItem();
            menuItem.setItemIcon(null);
            menuItem.setItemTitle(String.valueOf(Html.fromHtml(text)));
            list.add(menuItem);
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }

    /**
     * Sets the items with image.
     *
     * @param stringArrayList the string array list
     * @param imageArrayList  the image array list
     */
    public void setItemsWithImage(String[] stringArrayList, Integer[] imageArrayList) {
        if (stringArrayList == null || imageArrayList == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        if (stringArrayList.length == imageArrayList.length || imageArrayList.length != 0) {
            for (int i = 0; i < imageArrayList.length; i++) {
                DropMenuItem menuItem = new DropMenuItem();
                menuItem.setItemTitle(stringArrayList[i]);
                menuItem.setItemIcon(imageArrayList[i]);
                list.add(menuItem);
            }
        }

        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }

    /**
     * Sets the items.
     *
     * @param itemList the new items
     */
    public void setItems(ArrayList<DropMenuItem> itemList) {
        if (itemList == null) {
            throw new NullPointerException("Items Array is null.");
        } else {
            adapter = new PopupListItemAdapter(getContext(), itemList);
            setAdapter(adapter);
        }
    }
   /* public void setItemsWithFood(ArrayList<FoodDto> foodDtos) {
        if (foodDtos == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (FoodDto foodDto: foodDtos) {
            DropMenuItem menuItem = new DropMenuItem();
            if (foodDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(foodDto.type)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }
    public void setItemsWithCulture(ArrayList<CultureDto> cultureDtos) {
        if (cultureDtos == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (CultureDto cultureDto: cultureDtos) {
            DropMenuItem menuItem = new DropMenuItem();
            if (cultureDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(cultureDto.type)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }
    public void setItemsWithCountry(ArrayList<CountryDto> countryDtos) {
        if (countryDtos == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (CountryDto countryDto: countryDtos) {
            DropMenuItem menuItem = new DropMenuItem();
            if (countryDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(countryDto.name)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }
    public void setItemsWithState(ArrayList<StateDto> stateDtos) {
        if (stateDtos == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (StateDto stateDto: stateDtos) {
            DropMenuItem menuItem = new DropMenuItem();
            if (stateDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(stateDto.name)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }
    public void setItemsWithCity(ArrayList<CityDto> cityDtos) {
        if (cityDtos == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (CityDto cityDto: cityDtos) {
            DropMenuItem menuItem = new DropMenuItem();
            if (cityDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(cityDto.name)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }*/
   /* public void setItemsWithWhatsfor(ArrayList<WhatsforDto> whatsfor) {
        if (whatsfor == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (WhatsforDto whatsforDto : whatsfor) {
            DropMenuItem menuItem = new DropMenuItem();
            if (whatsforDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(whatsforDto.title)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }


    public void setItemsWithAreas(ArrayList<AreasDto> areas) {
        if (areas == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (AreasDto areasDto : areas) {
            DropMenuItem menuItem = new DropMenuItem();
            if (areasDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(areasDto.title)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }
    public void setItemsWithreferrals(ArrayList<ReferralSourceDto> referralSource) {
        if (referralSource == null)
            throw new NullPointerException("Items Array is null.");
        if (list == null)
            list = new ArrayList<>();
        list.clear();
        for (ReferralSourceDto referralSourceDto : referralSource) {
            DropMenuItem menuItem = new DropMenuItem();
            if (referralSourceDto != null) {
                menuItem.setItemTitle(String.valueOf(Html.fromHtml(referralSourceDto.title)));
                list.add(menuItem);
            }
        }
        adapter = new PopupListItemAdapter(getContext(), list);
        setAdapter(adapter);
    }*/
    /* (non-Javadoc)
     * @see android.widget.PopupWindow.OnDismissListener#onDismiss()
     */
    public void onDismiss() {
        // if (pw != null) {
        // pw.dismiss();
        // }
        pw = null;
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
     */
    public void onNothingSelected(AdapterView<?> arg0) {
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onNothingSelected(arg0);
        }
    }

    /* (non-Javadoc)
     * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
     */
    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Sets the on item click listener.
     *
     * @param onItemClickListener the new on item click listener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Sets the on item selected listener.
     *
     * @param onItemSelectedListener the new on item selected listener
     */
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    /**
     * Gets the selected position.
     *
     * @return the selected position
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * Sets the selected position.
     *
     * @param selectedPosition the new selected position
     */
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        refreshView();
    }

    /**
     * Sets the popup background color.
     *
     * @param color the new popup background color
     */
    public void setPopupBackgroundColor(int color) {
        this.popupColor = color;
        listDrawable = getRoundDrawable();
        // super.setOnClickListener(this);
        invalidate();
    }

    /**
     * Sets the popup item text color.
     *
     * @param color the new popup item text color
     */
    public void setPopupItemTextColor(int color) {
        this.popupTextColor = color;
        invalidate();
    }
}
