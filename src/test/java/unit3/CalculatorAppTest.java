package unit3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorAppTest {

    @Test
    void testParseOperands() {
        parseOperandsHelper("2+2", List.of(2, 2));
        parseOperandsHelper("2+2+2+2", List.of(2, 2, 2, 2));
        parseOperandsHelper("2*2", List.of(2, 2));
        parseOperandsHelper("1+2-3*4/5", List.of(1,2,3,4,5));
        parseOperandsHelper("123423+887987654", List.of(123423, 887987654));
        parseOperandsHelper("00001+00003", List.of(1, 3));
    }

    void parseOperandsHelper(String input, List<Integer> expected) {
        var actual = CalculatorApp.parseOperands(input);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void testParseOperators() {
        parseOperatorsHelper("2+2", List.of("+"));
        parseOperatorsHelper("2+2+2+2", List.of("+", "+", "+"));
        parseOperatorsHelper("2*2", List.of("*"));
        parseOperatorsHelper("1+2-3*4/5", List.of("+", "-", "*", "/"));
        parseOperatorsHelper("123423+887987654", List.of("+"));
        parseOperatorsHelper("00001+00003", List.of("+"));
    }

    void parseOperatorsHelper(String input, List<String> expected) {
        var actual = CalculatorApp.parseOperators(input);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void testValidExpressions() {
        promptForExpressionHelper("1+2");
        promptForExpressionHelper("1 + 2    +   3");
        promptForExpressionHelper("1+2+3");
        promptForExpressionHelper("1 + 2 * 434598 /9 - 345");
    }

    @Test
    void testInvalidExpressions() {
        promptForExpressionHelper("1+2+\n3+4", "3+4");
        promptForExpressionHelper("1+2+\n1+2+\n3+4", "3+4");
        promptForExpressionHelper("+\n3+4", "3+4");
        promptForExpressionHelper("+2+3\n3+4", "3+4");
        promptForExpressionHelper("1++2\n3+4", "3+4");
    }

    void promptForExpressionHelper(String input, String expected) {
        var original = System.in;
        try {
            System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
            var actual = CalculatorApp.promptForExpression();
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("The app as asking for more input, but it shouldn't be.");
        } finally {
            System.setIn(original);
        }
    }
    void promptForExpressionHelper(String input) {
        promptForExpressionHelper(input, input);
    }

    @Test
    void testCalculate() {
        calculateHelper(List.of(1, 2), List.of("+"), 3);
        calculateHelper(List.of(1, 2, 3), List.of("+", "+"), 6);
        calculateHelper(List.of(1, 2), List.of("-"), -1);
        calculateHelper(List.of(1, 2, 3, 4), List.of("-", "-", "-"), -8);
        calculateHelper(List.of(1, 2), List.of("*"), 2);
        calculateHelper(List.of(1, 2, 3, 4), List.of("*", "*", "*"), 24);
        calculateHelper(List.of(1, 2), List.of("/"), 0.5);
        calculateHelper(List.of(1, 2, 3, 4), List.of("/", "/", "/"), 0.041666666666666666666666666666666666667);

        calculateHelper(List.of(1,2,3,4,5), List.of("+","/","*","-"), -1.33333333333333333333333333333333333333);
        calculateHelper(List.of(1, 2, 3, 4), List.of("*", "/", "+"), 4.666666666666666666666666666666666666667);
        calculateHelper(List.of(1, 2, 3, 4), List.of("+", "*", "/"), 2.5);
        calculateHelper(List.of(1, 2, 3, 3, 4, 5, 2, 2), List.of("+", "*", "/", "-", "+", "*", "/"), 4.0);
        calculateHelper(List.of(1, 2, 4, 1, 5, 5, 1, 1, 5, 5), List.of("*", "/", "-", "+", "/", "*", "*", "-", "+"), 0.5);

    }

    void calculateHelper(List<Integer> operands, List<String> operators, double expected) {
        var actual = CalculatorApp.calculate(operands, operators);
        assertCloseEnough(expected, actual);
    }

    /**
     * Asserts that two doubles are close enough to each other to be considered equal.
     * @param expected The expected value
     * @param actual The actual value
     */
    void assertCloseEnough(double expected, double actual) {
        var delta = Math.abs(expected - actual);
        var epsilon = 0.0000000000001;
        assertTrue(delta < epsilon, "Expected " + expected + " but got " + actual);
    }
}