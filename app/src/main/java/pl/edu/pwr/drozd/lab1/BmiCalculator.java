package pl.edu.pwr.drozd.lab1;


import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BmiCalculator implements ICountBmi {

    private static final float MIN_MASS_KG = 10.0f;
    private static final float MAX_MASS_KG = 250.0f;
    private static final float MIN_HEIGHT_M = 0.5f;
    private static final float MAX_HEIGHT_M = 2.5f;

    private static final float MIN_MASS_LBS = 22.0f;
    private static final float MAX_MASS_LBS = 550.0f;
    private static final float MIN_HEIGHT_IN = 20.0f;
    private static final float MAX_HEIGHT_IN = 100.0f;

    private static final float CORRECT_BMI = 22.0f;
    public static final int LBS_IN = 1;
    public static final int KG_M = 0;

    public static final int UNDERWEIGHT = 0;
    public static final int CORRECT_WEIGHT = 1;
    public static final int OVERWEIGHT = 2;

    @IntDef({KG_M, LBS_IN})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {
    }

    @IntDef({UNDERWEIGHT, CORRECT_WEIGHT, OVERWEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface WeightClassification{}

    @Mode
    private int mode;

    public BmiCalculator() {
        this.mode = KG_M;
    }

    public BmiCalculator(@Mode int mode) {
        this.mode = mode;
    }

    @Override
    public boolean isMassValid(float mass) {
        if (mode == KG_M)
            return mass >= MIN_MASS_KG && mass <= MAX_MASS_KG;
        else
            return mass >= MIN_MASS_LBS && mass <= MAX_MASS_LBS;
    }

    @Override
    public boolean isHeightValid(float height) {
        if (mode == KG_M)
            return height >= MIN_HEIGHT_M && height <= MAX_HEIGHT_M;
        else
            return height >= MIN_HEIGHT_IN && height <= MAX_HEIGHT_IN;
    }

    @Override
    public float count(float mass, float height) {
        if (!isMassValid(mass))
            throw new IllegalArgumentException("Mass not valid");
        if (!isHeightValid(height))
            throw new IllegalArgumentException("Height not valid");

        if (mode == KG_M)
            return mass / (height * height);
        else
            return (mass / (height * height)) * 703;
    }

    @WeightClassification
    public int bmiRating(float bmi)    {
        if (bmi < 18.5) return UNDERWEIGHT;
        else if (bmi < 25) return CORRECT_WEIGHT;
        else return OVERWEIGHT;
    }

    public void setMode(@Mode int mode) {
        this.mode = mode;
    }

    @BmiCalculator.Mode
    public int getMode() {
        return mode;
    }

    public float calculateCorrectWeight(float height) {
        if (mode == KG_M)
            return 22 * height * height;
        else return (22 * height * height) / 703.0f;
    }
}
