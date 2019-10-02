package com.mycomp.tdd.strcalc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class StringCalculatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private StringCalculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new StringCalculator();
    }

    @Test
    public void shouldReturnZeroForEmptyString() throws Exception {
        assertCalculation(0, "");
    }

    @Test
    public void shouldReturnSameNumberForSingleInput() throws Exception {
        assertCalculation(100, "100");
    }

    @Test
    public void shouldReturnSumForTwoInputs() throws Exception {
        assertCalculation(3, "1,2");
        assertCalculation(300, "100,200");
    }

    @Test
    public void shouldReturnNumberWhenOnlyOneNumberIsPresent() throws Exception {
        assertCalculation(1, "1,");
        assertCalculation(1, ",1");
    }

    @Test
    public void shouldAddMultipleNumbers() throws Exception {
        assertCalculation(6, "1,2,3");
    }

    @Test
    public void shouldThrowExceptionForInvalidAmountOfInput() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage("Invalid input");
        calculate(",,,,");
    }

    @Test
    public void shouldAddTwoNumbersSeparatedWithNewLine() throws Exception {
        assertCalculation(5, "2\n3");
    }

    @Test
    public void shouldThrowExceptionForNegativeInputSingle() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage("negatives not allowed:-100");
        calculate("-100");
    }

    @Test
    public void shouldThrowExceptionForNegativeInputs() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage("negatives not allowed:-1");
        calculate("-1,1");

        exception.expectMessage("negatives not allowed:-1,-1");
        calculate("-1,-1");

        exception.expectMessage("negatives not allowed:-1");
        calculate("1,-1");
    }

    @Test
    public void shouldIgnoreNumberGreaterThan1000() throws Exception {
        assertCalculation(2, "2,1001");
        assertCalculation(2, "1002,2");
    }


    @Test
    public void shouldSupportCustomDelimet() throws Exception {
        assertCalculation(5, "//;\n2;3");
    }

    @Test
    public void shouldSupportMulticharDelimiter() throws Exception {
        assertCalculation(5, "//[***]\n2***3");
        assertCalculation(9, "//[***]\n2***3***4");
    }

    @Test
    public void shouldAllowMultipleDelimiters() throws Exception {
        assertCalculation(6, "//[*][%]\n1*2%3");
    }

    @Test
    public void shouldAllowMulticharMultipleDelimiters() throws Exception {
        assertCalculation(14, "//[**][%%]\n1**2%%3**5%%3");
    }

    @Test
    public void shouldThrowInvalidExceptionForIncorrectDelimiterInput() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage("Invalid input");
        calculate("//;;\n2**3");
    }

    @Test
    public void shouldAcceptDifferentDelimiters() throws Exception {
        assertCalculation(6, "1,2\n3");
        assertCalculation(16, "//[**][%%]\n1**2%%3**5%%3,1\n1");
    }

    private void assertCalculation(int expected, String number) {
        assertEquals(expected, calculate(number));
    }

    private int calculate(String number) {
        return calculator.add(number);
    }
}
