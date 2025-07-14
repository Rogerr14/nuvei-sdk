package com.nuvei.nuvei_sdk.views;

import com.nuvei.nuvei_sdk.builders.IDeleteEmptyListener;

public class BackupFieldDeleteListener implements IDeleteEmptyListener {
    private EditTextWidget backUpTarget;

    BackupFieldDeleteListener(EditTextWidget backUpTarget) {
        this.backUpTarget = backUpTarget;
    }
    @Override
    public void onDeleteEmpty() {
        String fieldText = backUpTarget.getText().toString();
        if (fieldText.length() > 1) {
            backUpTarget.setText(
                    fieldText.substring(0, fieldText.length() - 1));
        }
        backUpTarget.requestFocus();
        backUpTarget.setSelection(backUpTarget.length());
    }
}
