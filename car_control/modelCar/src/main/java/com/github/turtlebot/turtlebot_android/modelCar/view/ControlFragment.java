package com.github.turtlebot.turtlebot_android.modelCar.view;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.turtlebot.turtlebot_android.modelCar.ModelCarActivity;
import com.github.turtlebot.turtlebot_android.modelCar.R;

/**
 * Fragment to control the buttons and sliders
 *
 * Created by Daniel Neumann on 29.03.16.
 */
public class ControlFragment extends Fragment {

    private RelativeLayout layout1;
    private Toast lastToast;
    private ModelCarActivity modelCarActivity;
    private short emergency_stop_mode = 0;

    boolean blinker_not_publishing = false;
    final String BLINKER_LEFT = "Lle";
    final String BLINKER_RIGHT = "Lri";
    final String BLINKER_OFF = "LdiL";
    final String BLINKER_STOP = "Lstop";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        buildView();

        modelCarActivity = (ModelCarActivity) getActivity();
        modelCarActivity.setLayoutControl(layout1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        buildView();
    }

    private void buildView() {
        if (getView() != null) {
            ImageButton stopButton = getView().findViewById(R.id.button_stop);
            if (stopButton != null) {
                stopButton.setOnClickListener(stopButtonListener);
            }

            SeekBar speedBar = getView().findViewById(R.id.seekBar_speed);
            if (speedBar != null) {
                speedBar.setOnSeekBarChangeListener(speedBarListener);
            }

            SeekBar steeringBar = getView().findViewById(R.id.seekBar_steering);
            if (steeringBar != null) {
                steeringBar.setOnSeekBarChangeListener(steeringBarListener);
            }

            ToggleButton toggleButtonBlinkLeft = getView().findViewById(R.id.toggleButtonBlinkL);
            if (toggleButtonBlinkLeft != null) {
                toggleButtonBlinkLeft.setOnCheckedChangeListener(toggleButtonBlinkLeftListener);
            }


            ToggleButton toggleButtonBlinkRight = getView().findViewById(R.id.toggleButtonBlinkR);
            if (toggleButtonBlinkRight != null) {
                toggleButtonBlinkRight.setOnCheckedChangeListener(toggleButtonBlinkRightListener);
            }

            // Take a reference to the image view to show incoming panoramic pictures
            layout1 = getView().findViewById(R.id.main_inner);
        }
    }

    /************************************************************
     * Android code:
     ************************************************************/


    private final View.OnClickListener stopButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (getView() != null) {
                ImageButton emergency_stopButton = getView().findViewById(R.id.button_stop);
                if (emergency_stopButton != null) {
                    if (emergency_stop_mode == 1) {
                        emergency_stop_mode = 0;
                        modelCarActivity.callPublishStopStart(emergency_stop_mode);
                        emergency_stopButton.setImageResource(R.drawable.emergency_stop_inactive);

                        blinker_not_publishing = true;
                        ToggleButton toggleButtonBlinkLeft = getView().findViewById(R.id.toggleButtonBlinkL);
                        toggleButtonBlinkLeft.setChecked(false);
                        blinker_not_publishing = true;
                        ToggleButton toggleButtonBlinkRight = getView().findViewById(R.id.toggleButtonBlinkR);
                        toggleButtonBlinkRight.setChecked(false);
                        modelCarActivity.callPublishBlinkerLight(BLINKER_OFF);

                    } else {
                        emergency_stop_mode = 1;
                        modelCarActivity.callPublishStopStart(emergency_stop_mode);
                        emergency_stopButton.setImageResource(R.drawable.emergency_stop_active);
                        SeekBar speedBar1 = getView().findViewById(R.id.seekBar_speed);
                        speedBar1.setProgress(1000);

                        blinker_not_publishing = true;
                        ToggleButton toggleButtonBlinkLeft = getView().findViewById(R.id.toggleButtonBlinkL);
                        toggleButtonBlinkLeft.setChecked(false);
                        blinker_not_publishing = true;
                        ToggleButton toggleButtonBlinkRight = getView().findViewById(R.id.toggleButtonBlinkR);
                        toggleButtonBlinkRight.setChecked(false);
                        modelCarActivity.callPublishBlinkerLight(BLINKER_STOP);

                    }
                }
            }
        }

    };

    private final ToggleButton.OnCheckedChangeListener toggleButtonBlinkLeftListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                modelCarActivity.callPublishBlinkerLight(BLINKER_LEFT);

                buttonView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.turnleft_active, 0, 0, 0);

                blinker_not_publishing = true;
                if (getView() != null) {
                    ToggleButton toggleButtonBlinkRight = getView().findViewById(R.id.toggleButtonBlinkR);
                    if (toggleButtonBlinkRight != null) {
                        toggleButtonBlinkRight.setChecked(false);
                    }
                }

            } else if (!blinker_not_publishing) {
                modelCarActivity.callPublishBlinkerLight(BLINKER_OFF);
            }

            if (!isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.turnleft_inactive, 0, 0, 0);
            }

            if (blinker_not_publishing) {
                blinker_not_publishing = false;
            }

        }
    };

    private final ToggleButton.OnCheckedChangeListener toggleButtonBlinkRightListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            if (isChecked) {
                modelCarActivity.callPublishBlinkerLight(BLINKER_RIGHT);

                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.turnright_active, 0);

                blinker_not_publishing = true;
                if (getView() != null) {
                    ToggleButton toggleButtonBlinkLeft = getView().findViewById(R.id.toggleButtonBlinkL);
                    if (toggleButtonBlinkLeft != null) {
                        toggleButtonBlinkLeft.setChecked(false);
                    }
                }

            } else if (!blinker_not_publishing) {
                modelCarActivity.callPublishBlinkerLight(BLINKER_OFF);
            }

            if (!isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.turnright_inactive, 0);
            }

            if (blinker_not_publishing) {
                blinker_not_publishing = false;
            }

        }
    };

    private final SeekBar.OnSeekBarChangeListener speedBarListener = new SeekBar.OnSeekBarChangeListener() {
        short lastProgress = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            lastProgress = (short) (progress - (seekBar.getMax() / 2));
            modelCarActivity.callPublishSpeed(lastProgress);
            if (lastToast == null)
                lastToast = Toast.makeText(modelCarActivity.getBaseContext(), lastProgress + " rpm", Toast.LENGTH_SHORT);
            else
                lastToast.setText(lastProgress + " rpm");

            lastToast.show();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            modelCarActivity.callPublishSpeed(lastProgress);
        }
    };

    private final SeekBar.OnSeekBarChangeListener steeringBarListener = new SeekBar.OnSeekBarChangeListener() {
        short lastProgress = 30;
        short angle = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            lastProgress = (short) (progress - seekBar.getMax() / 2);
            angle = (short) (((seekBar.getMax() - progress) / 60.0) * 180);
            modelCarActivity.callPublishSteering(angle);
            if (lastToast == null)
                lastToast = Toast.makeText(modelCarActivity.getBaseContext(), lastProgress + " °", Toast.LENGTH_SHORT);
            else
                lastToast.setText(lastProgress + " °");

            lastToast.show();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

}