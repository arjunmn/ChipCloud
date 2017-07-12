package com.arjunmn.chipcloud;

public interface ChipListener {
    void chipSelected(int index, Object chipData);

    void chipDeselected(int index, Object chipData);

    void chipRemoved(int index, Object chipData);
}
