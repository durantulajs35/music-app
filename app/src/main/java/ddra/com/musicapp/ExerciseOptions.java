package ddra.com.musicapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;


public class ExerciseOptions extends ActionBarActivity{

    Theory theory[];

    TextView sbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get exercise type number selected
        int iType = getIntent().getIntExtra(ExerciseSelection.MESSAGE_EXERSEL, -1);

        //set activity title and labels
        setLabels(iType);

        setupSeekBar();

        theory = createTheory (iType);

        createList(theory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //set labels and title
    public void setLabels (int iType){
        String sType;
        //translate type into words
        switch (iType){
            case 0:
                sType = "Intervals";
                break;
            case 1:
                sType = "Scales";
                break;
            case 2:
                sType = "Chords";
                break;
            case 3:
                sType = "Melodic Dictation";
                break;
            case 4:
                sType = "Rhythmic Dictation";
                break;
            default:
                sType = "ERROR";
        }

        //set ActionBar title
        setTitle(sType + " Training Options");
        setContentView(R.layout.activity_exercise_options);

        //set selection label
        TextView t = (TextView) findViewById (R.id.exerOps_txt_selectLbl);
        t.setText(sType + " Selection");
    }

    public void setupSeekBar () {
        //set up TextView for SeekBar Listener
        sbProgress = (TextView) findViewById(R.id.exerOps_txt_sbProgress);
        sbProgress.setText("inf.");

        //set up seekbar to listener
        SeekBar sb = (SeekBar)findViewById(R.id.exerOps_sb_qNum);
        sb.setOnSeekBarChangeListener(sbListener);
    }

    public Theory[] createTheory(int iType){
        int numOf = 0;
        Theory theo[];
        switch (iType) {
            case 0: //intervals
                numOf = DATABASE.intervalName.length;
                int stepArray[] = new int[1];
                theo = new Theory[numOf];
                for (int i = 0; i < numOf; i++) {
                    stepArray[0] = i;
                    theo[i] = new Theory (DATABASE.intervalName[i], DATABASE.intervalAbbre[i], stepArray);
                } break;
            case 1: //scales
                numOf = DATABASE.scaleName.length;
                theo = new Theory[numOf];
                for (int i = 0; i < numOf; i++) {
                    theo[i] = new Theory (DATABASE.scaleName[i], null, DATABASE.scaleSteps[i]);
                } break;
            case 2: //chords
                numOf = DATABASE.chordName.length;
                theo = new Theory[numOf];
                for (int i = 0; i < numOf; i++) {
                    theo[i] = new Theory (DATABASE.chordName[i], DATABASE.chordAbbre[i], DATABASE.chordSteps[i]);
                } break;
            default: //ERROR
                theo = new Theory[0];
        }
        return theo;
    }

    public void createList (Theory type[]) {
        LinearLayout listbox = (LinearLayout) findViewById(R.id.exerOps_lLay_list);
        int numOf = type.length;
        if (type[0].abbre != null) {
            for (int i = 0; i < numOf; i++) {
                CheckBox listSelection = new CheckBox(this);
                listSelection.setText(type[i].name + " (" + type[i].abbre + ")");
                listSelection.setId(i);
                listbox.addView(listSelection);
            }
        } else {
            for (int i = 0; i < numOf; i++) {
                CheckBox listSelection = new CheckBox(this);
                listSelection.setText(type[i].name);
                listSelection.setId(i);
                listbox.addView(listSelection);
            }
        }
    }

    public void selectAll (View v) {
        int counter = 0;
        for (int i = 0; i < theory.length; i++){
            CheckBox cb = (CheckBox)findViewById(i);
            if (cb.isChecked())
                counter++;
        }

        if (counter < theory.length){
            for (int i = 0; i < theory.length; i++){
                CheckBox cb = (CheckBox)findViewById(i);
                cb.setChecked(true);
            }
        } else {
            for (int i = 0; i < theory.length; i++){
                CheckBox cb = (CheckBox)findViewById(i);
                cb.setChecked(false);
            }
        }
    }

    public boolean validateOptions () {
        ToggleButton asc = (ToggleButton)findViewById(R.id.exerOps_tBtn_ascend);
        ToggleButton des = (ToggleButton)findViewById(R.id.exerOps_tBtn_descend);
        ToggleButton melo = (ToggleButton)findViewById(R.id.exerOps_tBtn_melo);
        ToggleButton harm = (ToggleButton)findViewById(R.id.exerOps_tBtn_harm);
        boolean isOneChecked = false;
        for (int i = 0; i < theory.length && !isOneChecked; i++){
            CheckBox cb = (CheckBox) findViewById(i);
            if (cb.isChecked())
                isOneChecked = true;
        }

        if (!(asc.isChecked() || des.isChecked())){
            TextView lbl = (TextView)findViewById(R.id.exerOps_txt_type);
            lbl.setTextColor(Color.RED);
        } else {
            TextView lbl = (TextView)findViewById(R.id.exerOps_txt_type);
            lbl.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        }
        if (!(melo.isChecked() || harm.isChecked())){
            TextView lbl = (TextView)findViewById(R.id.exerOps_txt_timing);
            lbl.setTextColor(Color.RED);
        } else {
            TextView lbl = (TextView)findViewById(R.id.exerOps_txt_timing);
            lbl.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        }
        if (!isOneChecked){
            TextView lbl = (TextView)findViewById(R.id.exerOps_txt_selectLbl);
            lbl.setTextColor(Color.RED);
        } else {
            TextView lbl = (TextView)findViewById(R.id.exerOps_txt_selectLbl);
            lbl.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
        }
        return ((asc.isChecked() || des.isChecked()) && (melo.isChecked() || harm.isChecked()) && isOneChecked);
    }

    public void goToExercise (View v){
        if (validateOptions()) {
            //PUT ALL THE EXTRAS INTO AN INTENT AND CHANGE ACTIVITY
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(ExerciseOptions.this).create();
            alertDialog.setTitle("Settings Invalid");
            alertDialog.setMessage("At least one option under each category must be selected!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        public void onStartTrackingTouch (SeekBar seekbar) {}

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (i == 0)
                sbProgress.setText ("inf.");
            else
                sbProgress.setText ("" + i);
        }
    };


}
