package pl.edu.pwr.drozd.lab1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BmiCalculatorKgMTest {

    private float invalidMassUnder0Kg = -1.0f;
    private float invalidHeightUnder0M = -1.0f;

    private float invalidMassOver250Kg = 250.1f;
    private float invalidHeightOver2_5M = 2.51f;

    private float validMassKg = 100.0f;
    private float validHeightM = 2.0f;

    private float validBMIKgM = 25.0f;

    private BmiCalculator mCalculator;

    @Before
    public void beforeClass() {
        mCalculator = new BmiCalculator(BmiCalculator.KG_M);
        mCalculator.setMode(BmiCalculator.KG_M);
    }

    @Test
    public void mass_under_0Kg_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isMassValid(invalidMassUnder0Kg));
    }

    @Test
    public void height_under_0M_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isHeightValid(invalidHeightUnder0M));
    }

    @Test
    public void mass_over_250Kg_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isMassValid(invalidMassOver250Kg));
    }

    @Test
    public void height_over_2_5M_isInvalid() throws Exception {
        assertEquals(false, mCalculator.isHeightValid(invalidHeightOver2_5M));
    }

    @Test
    public void count_Bmi_KgM_isCorrect() throws Exception {
        assertEquals(validBMIKgM, mCalculator.count(validMassKg, validHeightM), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massUnder0Kg_heightOver2_5M() throws Exception {
        mCalculator.count(invalidMassUnder0Kg, invalidHeightOver2_5M);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massUnder0Kg_heightUnder0M() throws Exception {
        mCalculator.count(invalidMassUnder0Kg, invalidHeightUnder0M);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massOver250Kg_heightOver2_5M() throws Exception {
        mCalculator.count(invalidMassOver250Kg, invalidHeightOver2_5M);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_heightOver2_5M() throws Exception {
        mCalculator.count(validMassKg, invalidHeightOver2_5M);
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_Bmi_isInvalid_massOver250Kg() throws Exception {
        mCalculator.count(invalidMassOver250Kg, validHeightM);
    }

    @Test
    public void calculate_correct_weight() {
        assertEquals(mCalculator.calculateCorrectWeight(validHeightM), 88.0, 0.01);
    }

}
