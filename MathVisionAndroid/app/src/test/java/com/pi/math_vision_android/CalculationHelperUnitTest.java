package com.pi.math_vision_android;


import org.junit.Test;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import static org.junit.Assert.*;

import com.pi.math_vision_android.helpers.CalculationHelper;

public class CalculationHelperUnitTest {

    private String testPrefix = "$$";
    private String testSufix = "$$</br>";

    private String writeExpected(String text){
        return testPrefix + text + testSufix;
    }

    @Test
    public void testAdditionOK(){
        assertEquals(writeExpected("4102"), CalculationHelper.getAnswer("1777+2325"));
    }

    @Test
    public void testSubtractionOK(){
        assertEquals(writeExpected("-5"), CalculationHelper.getAnswer("15-20"));
    }

    @Test
    public void testMultiplicationOK(){
        assertEquals(writeExpected("59360"), CalculationHelper.getAnswer("224*265"));
    }

    @Test
    public void testDivisionOK(){
        assertEquals(writeExpected("20/7"), CalculationHelper.getAnswer("80/28"));
    }

    @Test
    public void testExponentiationOK(){
        assertEquals(writeExpected("64"), CalculationHelper.getAnswer("2^6"));
    }

    @Test
    public void testSquareRootOK(){
        assertEquals(writeExpected("8"), CalculationHelper.getAnswer("64^(1/2)"));
    }

    @Test
    public void testRootingOK(){
        assertEquals(writeExpected("3"), CalculationHelper.getAnswer("27^(1/3)"));
    }

    @Test
    public void testNegativeRoot(){
        assertEquals(writeExpected("I*2"), CalculationHelper.getAnswer("(-4)^(1/2)"));
    }

    @Test
    public void testSimpleEquation(){
        assertEquals(writeExpected("x=2"), CalculationHelper.getAnswer("34+x=36"));
    }

    @Test
    public void testRuntimeException(){
        assertThrows(RuntimeException.class, () -> CalculationHelper.getAnswer("15¨5 + 99x"));
    }

    @Test
    public void testMathError(){
        assertEquals("Math Error", CalculationHelper.getAnswer("1=0"));
    }
}
