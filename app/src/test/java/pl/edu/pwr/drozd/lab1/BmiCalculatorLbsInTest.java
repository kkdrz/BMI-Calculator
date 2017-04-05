package pl.edu.pwr.drozd.lab1;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BmiCalculatorLbsInTest {

    private float invalidMassUnder0Lbs = -1.0f;
    private float invalidHeightUnder0In = -1.0f;

    private float invalidMassOver550Lbs = 552.0f;
    private float invalidHeightOver100In = 101.0f;

    private float validMassLbs = 220.0f;
    private float validHeightIn = 79.0f;

    private float validBMILbsIn = 24.78f;

    private BmiCalculator mCalculator;

    @Before
    public void beforeClass() {
        mCalculator = new BmiCalculator(BmiCalculator.KG_M);
        mCalculator.setMode(BmiCalculator.LBS_IN);
    }

    @Test
    public void mass_under_0Lbs_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isMassValid(invalidMassUnder0Lbs));
    }

    @Test
    public void height_under_0In_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isHeightValid(invalidHeightUnder0In));
    }

    @Test
    public void mass_over_550Lbs_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isMassValid(invalidMassOver550Lbs));
    }

    @Test
    public void height_over_100In_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isHeightValid(invalidHeightOver100In));
    }

    @Test
    public void count_Bmi_LbsIn_isCorrect() throws Exception {
        assertEquals(validBMILbsIn, mCalculator.count(validMassLbs, validHeightIn), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massUnder0Lbs_heightOver100In() throws Exception {
        mCalculator.count(invalidMassUnder0Lbs, invalidHeightOver100In);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massUnder0Lbs_heightUnder0In() throws Exception {
        mCalculator.count(invalidMassUnder0Lbs, invalidHeightUnder0In);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massOver550Lbs_heightOver100In() throws Exception {
        mCalculator.count(invalidMassOver550Lbs, invalidHeightOver100In);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_heightOver100In() throws Exception {
        mCalculator.count(validMassLbs, invalidHeightOver100In);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massOver550Lbs() throws Exception {
        mCalculator.count(invalidMassOver550Lbs, validHeightIn);
    }

    @Test
    public void calculate_correct_weight() {
        assertEquals(mCalculator.calculateCorrectWeight(validHeightIn), 195.3, 0.1);
    }
}
