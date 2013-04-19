/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.pinprogress;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HpPinProgressButton pinProgress1 = (HpPinProgressButton) findViewById(
                R.id.pin_progress_1);
        final CircleProgress pinProgress2 = (CircleProgress) findViewById(
                R.id.pin_progress_2);
        final PinProgressButton pinProgress3 = (PinProgressButton) findViewById(
                R.id.pin_progress_3);
        final PinProgressButton pinProgress4 = (PinProgressButton) findViewById(
                R.id.pin_progress_4);
        final TextView pinProgress1TextView = (TextView) findViewById(R.id.pin_progress_1_TextView);

        CompoundButton.OnCheckedChangeListener checkedChangeListener
                = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                updatePinProgressContentDescription((PinProgressButton) compoundButton);
            }
        };

        pinProgress1.setOnCheckedChangeListener(checkedChangeListener);
        pinProgress3.setOnCheckedChangeListener(checkedChangeListener);
        pinProgress4.setOnCheckedChangeListener(checkedChangeListener);

        SeekBar progressSeekBar = (SeekBar) findViewById(R.id.progress_seek_bar);
        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pinProgress1.setProgress(progress);
                pinProgress2.setProgress(progress);
                pinProgress3.setProgress(progress);
                pinProgress4.setProgress(progress);

                updatePinProgressContentDescription(pinProgress1);
                updatePinProgressContentDescription(pinProgress3);
                updatePinProgressContentDescription(pinProgress4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        pinProgress1.setProgress(progressSeekBar.getProgress());
        pinProgress2.setProgress(progressSeekBar.getProgress());
        pinProgress3.setProgress(progressSeekBar.getProgress());
        pinProgress4.setProgress(progressSeekBar.getProgress());

        updatePinProgressContentDescription(pinProgress1);
        updatePinProgressContentDescription(pinProgress3);
        updatePinProgressContentDescription(pinProgress4);
    }
    private void updatePinProgressContentDescription(HpPinProgressButton button) {
        int progress = button.getProgress();
        if (progress <= 0) {
            button.setContentDescription(getString(button.isChecked()
                    ? R.string.content_desc_pinned_not_downloaded
                    : R.string.content_desc_unpinned_not_downloaded));
        } else if (progress >= 100) {
            button.setContentDescription(getString(button.isChecked()
                    ? R.string.content_desc_pinned_downloaded
                    : R.string.content_desc_unpinned_downloaded));
        } else {
            button.setContentDescription(getString(button.isChecked()
                    ? R.string.content_desc_pinned_downloading
                    : R.string.content_desc_unpinned_downloading));
        }
    }
    private void updatePinProgressContentDescription(PinProgressButton button) {
        int progress = button.getProgress();
        if (progress <= 0) {
            button.setContentDescription(getString(button.isChecked()
                    ? R.string.content_desc_pinned_not_downloaded
                    : R.string.content_desc_unpinned_not_downloaded));
        } else if (progress >= 100) {
            button.setContentDescription(getString(button.isChecked()
                    ? R.string.content_desc_pinned_downloaded
                    : R.string.content_desc_unpinned_downloaded));
        } else {
            button.setContentDescription(getString(button.isChecked()
                    ? R.string.content_desc_pinned_downloading
                    : R.string.content_desc_unpinned_downloading));
        }
    }
}
