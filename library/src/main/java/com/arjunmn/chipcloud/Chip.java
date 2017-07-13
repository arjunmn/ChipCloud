package com.arjunmn.chipcloud;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewGroupCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Chip extends ConstraintLayout implements View.OnClickListener {

    private int index = -1;
    private boolean selected = false;
    private ChipListener listener = null;
    private int selectedFontColor = -1;
    private int unselectedFontColor = -1;
    private TransitionDrawable crossfader;
    private int selectTransitionMS = 750;
    private int deselectTransitionMS = 500;
    private boolean isLocked = false;
    private ChipCloud.Mode mode;
    private Object chipData = null;
    private boolean removable = false;

    public void setChipData(Object object){
        this.chipData = object;
    }

    public Object getChipData(){
        return this.chipData;
    }

    public void setChipListener(ChipListener listener) {
        this.listener = listener;
    }

    public void setChipRemovable(boolean isRemovable){
        this.removable = isRemovable;
    }

    public Chip(Context context) {
        super(context);
        init();
    }

    public Chip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void initChip(Context context, int index, String label, Typeface typeface, int textSizePx,
                         boolean allCaps, int selectedColor, int selectedFontColor, int unselectedColor,
                         int unselectedFontColor, ChipCloud.Mode mode, Object chipData, boolean removable) {

        this.index = index;
        this.selectedFontColor = selectedFontColor;
        this.unselectedFontColor = unselectedFontColor;
        this.mode = mode;

        Drawable selectedDrawable = ContextCompat.getDrawable(context, R.drawable.chip_selected);

        if (selectedColor == -1) {
            selectedDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.dark_grey), PorterDuff.Mode.MULTIPLY));
        } else {
            selectedDrawable.setColorFilter(new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.MULTIPLY));
        }

        if (selectedFontColor == -1) {
            this.selectedFontColor = ContextCompat.getColor(context, R.color.white);
        }

        Drawable unselectedDrawable = ContextCompat.getDrawable(context, R.drawable.chip_selected);
        if (unselectedColor == -1) {
            unselectedDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.light_grey), PorterDuff.Mode.MULTIPLY));
        } else {
            unselectedDrawable.setColorFilter(new PorterDuffColorFilter(unselectedColor, PorterDuff.Mode.MULTIPLY));
        }

        if (unselectedFontColor == -1) {
            this.unselectedFontColor = ContextCompat.getColor(context, R.color.chip);
        }

        Drawable backgrounds[] = new Drawable[2];
        backgrounds[0] = unselectedDrawable;
        backgrounds[1] = selectedDrawable;

        crossfader = new TransitionDrawable(backgrounds);

        //Bug reported on KitKat where padding was removed, so we read the padding values then set again after setting background
        int leftPad = getPaddingLeft();
        int topPad = getPaddingTop();
        int rightPad = getPaddingRight();
        int bottomPad = getPaddingBottom();

        setBackgroundCompat(crossfader);

        setPadding(leftPad, topPad, rightPad, bottomPad);

        setText(label);
        unselect();

        if (typeface != null) {
            setTypeface(typeface);
        }
        setAllCaps(allCaps);
        if (textSizePx > 0) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);
        }
        setChipData(chipData);
        setChipRemovable(removable);
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setSelectTransitionMS(int selectTransitionMS) {
        this.selectTransitionMS = selectTransitionMS;
    }

    public void setDeselectTransitionMS(int deselectTransitionMS) {
        this.deselectTransitionMS = deselectTransitionMS;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.remove_chip){
            listener.chipRemoved(index, chipData);
            return;
        }
        if (mode != ChipCloud.Mode.NONE)
            if (selected && !isLocked) {
                //set as unselected
                unselect();
                if (listener != null) {
                    listener.chipDeselected(index, chipData);
                }
            } else if (!selected) {
                //set as selected
                crossfader.startTransition(selectTransitionMS);

                setTextColor(selectedFontColor);
                if (listener != null) {
                    listener.chipSelected(index, chipData);
                }
            }
        selected = !selected;
    }

    public void select() {
        selected = true;
        crossfader.startTransition(selectTransitionMS);
        setTextColor(selectedFontColor);
        if (listener != null) {
            listener.chipSelected(index, chipData);
        }
    }

    private void unselect() {
        if (selected) {
            crossfader.reverseTransition(deselectTransitionMS);
        } else {
            crossfader.resetTransition();
        }

        setTextColor(unselectedFontColor);
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(Drawable background) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(background);
        } else {
            setBackground(background);
        }
    }

    public void deselect() {
        unselect();
        selected = false;
    }

    public static class ChipBuilder {
        private int index;
        private String label;
        private Typeface typeface;
        private int textSizePx;
        private boolean allCaps;
        private int selectedColor;
        private int selectedFontColor;
        private int unselectedColor;
        private int unselectedFontColor;
        private int chipHeight;
        private int selectTransitionMS = 750;
        private int deselectTransitionMS = 500;

        private ChipListener chipListener;
        private ChipCloud.Mode mode;

        private Object chipData;
        private boolean removable = false;

        public ChipBuilder removable(boolean isRemovable){
            this.removable = isRemovable;
            return this;
        }

        public ChipBuilder chipData(Object data){
            this.chipData = data;
            return this;
        }

        public ChipBuilder index(int index) {
            this.index = index;
            return this;
        }

        public ChipBuilder selectedColor(int selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public ChipBuilder selectedFontColor(int selectedFontColor) {
            this.selectedFontColor = selectedFontColor;
            return this;
        }

        public ChipBuilder unselectedColor(int unselectedColor) {
            this.unselectedColor = unselectedColor;
            return this;
        }

        public ChipBuilder unselectedFontColor(int unselectedFontColor) {
            this.unselectedFontColor = unselectedFontColor;
            return this;
        }

        public ChipBuilder label(String label) {
            this.label = label;
            return this;
        }

        public ChipBuilder typeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        public ChipBuilder allCaps(boolean allCaps) {
            this.allCaps = allCaps;
            return this;
        }

        public ChipBuilder textSize(int textSizePx) {
            this.textSizePx = textSizePx;
            return this;
        }

        public ChipBuilder chipHeight(int chipHeight) {
            this.chipHeight = chipHeight;
            return this;
        }

        public ChipBuilder chipListener(ChipListener chipListener) {
            this.chipListener = chipListener;
            return this;
        }

        public ChipBuilder mode(ChipCloud.Mode mode) {
            this.mode = mode;
            return this;
        }

        public ChipBuilder selectTransitionMS(int selectTransitionMS) {
            this.selectTransitionMS = selectTransitionMS;
            return this;
        }

        public ChipBuilder deselectTransitionMS(int deselectTransitionMS) {
            this.deselectTransitionMS = deselectTransitionMS;
            return this;
        }

        public Chip build(Context context) {
            final Chip chip = (Chip) LayoutInflater.from(context).inflate(R.layout.chip, null);
            ImageView iv = (ImageView) chip.findViewById(R.id.remove_chip);
            if(removable){
                iv.setVisibility(VISIBLE);
            }else{
                iv.setVisibility(GONE);
            }
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chipListener != null) {
                        //chipListener.chipSelected(index, chipData);
                        chip.onClick(v);
                    }
                }
            });
            chip.initChip(context, index, label, typeface, textSizePx, allCaps, selectedColor,
                    selectedFontColor, unselectedColor, unselectedFontColor, mode, chipData, removable);
            chip.setSelectTransitionMS(selectTransitionMS);
            chip.setDeselectTransitionMS(deselectTransitionMS);
            chip.setChipListener(chipListener);
            chip.setMinimumHeight(chipHeight);
            chip.setMaxHeight(chipHeight); // Hack to test if it works with CL
            chip.setChipData(chipData);
            chip.setChipRemovable(removable);
            return chip;
        }
    }

    public void setText(String text){
        TextView tv = (TextView) this.findViewById(R.id.chip);
        tv.setText(text);
    }

    public void setAllCaps(boolean allCaps){
        TextView tv = (TextView) this.findViewById(R.id.chip);
        tv.setAllCaps(allCaps);
    }

    public void setTypeface(Typeface typeface){
        TextView tv = (TextView) this.findViewById(R.id.chip);
        tv.setTypeface(typeface);
    }

    public void setTextSize(int mode, int size){
        TextView tv = (TextView) this.findViewById(R.id.chip);
        tv.setTextSize(mode, size);
    }

    public void setTextColor(int color){
        TextView tv = (TextView) this.findViewById(R.id.chip);
        tv.setTextColor(color);
    }
}
