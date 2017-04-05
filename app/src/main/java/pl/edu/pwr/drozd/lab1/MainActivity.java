package pl.edu.pwr.drozd.lab1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static pl.edu.pwr.drozd.lab1.BmiCalculator.KG_M;

public class MainActivity extends AppCompatActivity {

    //pwr-mszczepanik na githubie

    private static final String MASS_ET_STATE = "mass_state";
    private static final String HEIGHT_ET_STATE = "height_state";
    private static final String BMI_RESULT_TV_STATE = "bmi_state";
    private static final String BMI_OPINION_TV_STATE = "bmi_opinion_state";
    private static final String BMI_OPINION_COLOR_TV_STATE = "bmi_opinion_color_state";
    @BmiCalculator.Mode private static final int DEFAULT_CALCULATOR_MODE = KG_M;

    @BindView(R.id.et_height)       EditText mHeightET;
    @BindView(R.id.et_mass)         EditText mMassET;
    @BindView(R.id.tv_bmi_result)   TextView mBMIResultTV;
    @BindView(R.id.tv_bmi_opinion)  TextView mBMIOpinionTV;
    @BindView(R.id.mode_spinner)    Spinner mModeSpinner;
    @BindView(R.id.tv_height_mode)  TextView mHeightModeTV;
    @BindView(R.id.tv_mass_mode)    TextView mMassModeTV;
    @BindView(R.id.btn_save)        ImageButton mSaveBTN;
    @BindView(R.id.btn_share)       ImageButton mShareBTN;

    private BmiCalculator mCalculator;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCalculator = new BmiCalculator(DEFAULT_CALCULATOR_MODE);
        initializeSpinner();
        loadParamsFromSharedPrefs();
        hideKeyboard(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String givenMass = mMassET.getText().toString();
        String givenHeight = mHeightET.getText().toString();

        if (validateMass(givenMass) && validateHeight(givenHeight)) {
            outState.putString(MASS_ET_STATE, givenMass);
            outState.putString(HEIGHT_ET_STATE, givenHeight);
            if (mBMIResultTV.getVisibility() == View.VISIBLE)   {
                outState.putString(BMI_RESULT_TV_STATE, mBMIResultTV.getText().toString());
            }
            if (mBMIOpinionTV.getVisibility() == View.VISIBLE)  {
                outState.putString(BMI_OPINION_TV_STATE, mBMIOpinionTV.getText().toString());
                outState.putInt(BMI_OPINION_COLOR_TV_STATE, mBMIOpinionTV.getCurrentTextColor());
            }
        }
        super.onSaveInstanceState(outState);
        hideKeyboard(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mMassET.setText(savedInstanceState.getString(MASS_ET_STATE));
        mHeightET.setText(savedInstanceState.getString(HEIGHT_ET_STATE));
        mBMIResultTV.setText(savedInstanceState.getString(BMI_RESULT_TV_STATE));
        mBMIOpinionTV.setText(savedInstanceState.getString(BMI_OPINION_TV_STATE));
        mBMIOpinionTV.setTextColor(savedInstanceState.getInt(BMI_OPINION_COLOR_TV_STATE));
        mBMIOpinionTV.setVisibility(View.VISIBLE);
        mBMIResultTV.setVisibility(View.VISIBLE);
        if (mBMIResultTV.getText().toString().length() != 0) {
            mShareBTN.setVisibility(View.VISIBLE);
            mSaveBTN.setVisibility(View.VISIBLE);
        }
        super.onRestoreInstanceState(savedInstanceState);
        hideKeyboard(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_about: {
                Intent aboutIntent = new Intent(this, AuthorActivity.class);
                startActivity(aboutIntent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_mode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mModeSpinner.setAdapter(adapter);
        mModeSpinner.setOnItemSelectedListener(spinnerListener);
    }

    @OnClick(R.id.btn_count)
    public void count() {
        hideKeyboard(this);
        String givenMass = mMassET.getText().toString();
        String givenHeight = mHeightET.getText().toString();
        
        if (validateMass(givenMass) && validateHeight(givenHeight)) {
            float mass = Float.valueOf(givenMass);
            float height = parseHeight(givenHeight);
            float bmiResult = mCalculator.count(mass, height);

            displayBmiResult(bmiResult);
            displayOpinion(bmiResult, height);
            displayShareSaveButtons(true);
        } else {
            displayShareSaveButtons(false);
            Toast.makeText(this, getString(R.string.wrong_input), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayShareSaveButtons(boolean visible) {
        if (visible) {
            mSaveBTN.setVisibility(View.VISIBLE);
            mShareBTN.setVisibility(View.VISIBLE);
        } else  {
            mSaveBTN.setVisibility(View.INVISIBLE);
            mShareBTN.setVisibility(View.INVISIBLE);
        }

    }

    @OnClick(R.id.btn_save)
    public void saveButton() {
        hideKeyboard(this);
        if(saveParams()) Toast.makeText(this, getString(R.string.input_saved), Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, getString(R.string.wrong_input), Toast.LENGTH_SHORT).show();
    }

    private boolean saveParams() {
        String givenMass = mMassET.getText().toString();
        String givenHeight = mHeightET.getText().toString();
        if (validateMass(givenMass) && validateHeight(givenHeight)) {
            saveParamsInSharedPrefs(givenMass, givenHeight);
            return true;
        }
        return false;
    }

    @OnClick(R.id.btn_share)
    public void shareButton() {
        if (mBMIResultTV.getVisibility() == View.VISIBLE)
            share(constructShareText());
    }

    private String constructShareText() {
        float bmi = Float.valueOf(mBMIResultTV.getText().toString());
        String shareText = String.format(Locale.ENGLISH, "%s %.0f.", getText(R.string.my_bmi_equals), bmi);
        switch (mCalculator.bmiRating(bmi)) {
            case BmiCalculator.UNDERWEIGHT:
                shareText = String.format(Locale.ENGLISH, "%s %s :(", shareText, getText(R.string.i_m_thin));
                break;
            case BmiCalculator.CORRECT_WEIGHT:
                shareText = String.format(Locale.ENGLISH, "%s %s :)", shareText, getText(R.string.i_m_normal));
                break;
            case BmiCalculator.OVERWEIGHT:
                shareText = String.format(Locale.ENGLISH, "%s %s :(", shareText, getText(R.string.i_m_fat));
                break;
        }
        return shareText;
    }

    private void share(String toShare) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, toShare);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void saveParamsInSharedPrefs(String givenMass, String givenHeight) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString(HEIGHT_ET_STATE, givenHeight);
        prefsEditor.putString(MASS_ET_STATE, givenMass);
        prefsEditor.apply();
    }

    private void loadParamsFromSharedPrefs() {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mass = mSharedPrefs.getString(MASS_ET_STATE, "");
        String height = mSharedPrefs.getString(HEIGHT_ET_STATE, "");

        mHeightET.setText(height);
        mMassET.setText(mass);
    }

    private float parseHeight(String givenHeight) {
        if (mCalculator.getMode() == KG_M)
            return Float.valueOf(givenHeight)/100.0f;
        else return Float.valueOf(givenHeight);
    }

    private void displayBmiResult(float bmiResult) {
        mBMIResultTV.setText(String.format(Locale.ENGLISH, "%.1f", bmiResult));
        mBMIResultTV.setVisibility(View.VISIBLE);
    }

    private void displayOpinion(float bmi, float height) {
        String opinion = "";
        float correctWeight = mCalculator.calculateCorrectWeight(height);

        switch (mCalculator.bmiRating(bmi)) {
            case BmiCalculator.UNDERWEIGHT:
                opinion = getString(R.string.underweight) + ". "
                        + getString(R.string.BMI_opinion)
                        + String.format(Locale.ENGLISH, " %d", (int) correctWeight)
                        + (mCalculator.getMode() == KG_M ? "kg" : "lbs");
                mBMIOpinionTV.setTextColor(ContextCompat.getColor(this, R.color.underweight));
                break;
            case BmiCalculator.CORRECT_WEIGHT:
                opinion = getString(R.string.correctweight);
                mBMIOpinionTV.setTextColor(ContextCompat.getColor(this, R.color.correctweight));
                break;
            case BmiCalculator.OVERWEIGHT:
                opinion = getString(R.string.overweight) + ". "
                        + getString(R.string.BMI_opinion)
                        + String.format(Locale.ENGLISH, " %d", (int) correctWeight)
                        + (mCalculator.getMode() == KG_M ? "kg" : "lbs");
                mBMIOpinionTV.setTextColor(ContextCompat.getColor(this, R.color.overweight));
                break;
        }
        mBMIOpinionTV.setText(opinion);
        mBMIOpinionTV.setVisibility(View.VISIBLE);
    }

    private boolean validateMass(String mass) {
        if (mass.length() < 1 || !mCalculator.isMassValid(Float.valueOf(mass))) {
            mMassET.setError(getString(R.string.wrong_mass));
            hideBmiInfo();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateHeight(String height) {
        if (height.length() < 1 || !mCalculator.isHeightValid(parseHeight(height))) {
            mHeightET.setError(getString(R.string.wrong_height));
            hideBmiInfo();
            return false;
        } else {
            return true;
        }
    }

    private void hideBmiInfo() {
        mBMIOpinionTV.setVisibility(View.INVISIBLE);
        mBMIResultTV.setVisibility(View.INVISIBLE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    mCalculator.setMode(KG_M);
                    mHeightModeTV.setText(getText(R.string.cm));
                    mMassModeTV.setText(getText(R.string.kg));
                    mMassET.setHint(getText(R.string.mass_kg_range));
                    mHeightET.setHint(getText(R.string.height_cm_range));
                    break;
                case 1:
                    mCalculator.setMode(BmiCalculator.LBS_IN);
                    mHeightModeTV.setText(getText(R.string.in));
                    mMassModeTV.setText(getText(R.string.lbs));
                    mMassET.setHint(getText(R.string.mass_lbs_range));
                    mHeightET.setHint(getText(R.string.height_in_range));
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
