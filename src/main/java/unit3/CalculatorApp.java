package unit3;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CalculatorApp {

    public static void main(String[] args) {

        while ( true ) {
           String expression = promptForExpression();

            expression = expression.replace(" ", "");

            var operands = parseOperands(expression);
            var operators = parseOperators(expression);

            double result = calculate(operands, operators);

            // If the result is an integer, print it as an integer
            if ( Math.abs(result - (int)result) < 0.00000000000001 ) {
                System.out.printf("%s = %d%n", expression, (int)result);
            } else {
                System.out.printf("%s = %f%n", expression, result);
            }

        }

    }

    /**
     * Returns the result of evaluating the given arithmetic expression.
     * The nth operator in the expression is applied to the nth and (n+1)th operands.
     * WARNING: This solution does NOT follow the order of operations.
     * @param operands The sequence of operands in the expression
     * @param operators The sequence of operators in the expression
     * @return The result of evaluating the expression operand[0] operator[0] operand[1] operators[1] ...
     */
    static double calculateWrong(List<Integer> operands, List<String> operators ) {
        double result = operands.get(0);
        for ( int i = 0; i < operators.size(); i += 1 ) {
            String operator = operators.get(i);
            int operand = operands.get(i+1);
            switch (operator) {
                case "+" -> result += operand;
                case "-" -> result -= operand;
                case "*" -> result *= operand;
                case "/" -> result /= (double)operand;
            }
        }
        return result;
    }

    /**
     * Returns the result of evaluating the given arithmetic expression.
     * @param operands The sequence of operands in the expression
     * @param operators The sequence of operators in the expression
     * @return The result of evaluating the expression operand[0] operator[0] operand[1] operators[1] ...
     */
    static double calculate(List<Integer> operands, List<String> operators) {

        // Convert list of ints to list of doubles
        var operandsCopy = new ArrayList<Double>();
        for ( int i = 0; i < operands.size(); i += 1 ) {
            operandsCopy.add((double)operands.get(i));
        }

        var operatorsCopy = new ArrayList<String>(operators);


        // First, evaluate all multiplications and divisions
        int i = 0;
        // Note the use of a labeled loop here (see the "break main" statement below)
        main: while ( i < operatorsCopy.size() ) {
            String operator = operatorsCopy.get(i);

            // Skip over additions and subtractions to start with
            while ( operator.equals("+") || operator.equals("-") ) {
                i += 1;

                // If we've reached the end of the operators we're done
                if (i >= operatorsCopy.size()) {
                    break main;  // Break out of the loop labeled "main"
                }

                operator = operatorsCopy.get(i);
            }

            // Now we're at a multiplication or division; process it AND any immediately following multiplications or divisions
            while ( operator.equals("*") || operator.equals("/") ) {
                switch (operator) {
                    case "*" -> operandsCopy.set(i, operandsCopy.get(i) * operandsCopy.get(i + 1));
                    case "/" -> operandsCopy.set(i, operandsCopy.get(i) / (double) operandsCopy.get(i + 1));
                }
                // Remove the operator and operand that we just processed
                // Note that we don't increment i here because the next operator is now at index i
                // (I.e. for multiplication/division, we evaluate the operation in place and 'consume' the next operand)
                operatorsCopy.remove(i);
                operandsCopy.remove(i + 1);

                // Since we are shortening the list on each multiplication/division,
                // we will eventually reach the end of the operators, so we need to check for that
                if (i >= operatorsCopy.size()) {
                    break;
                }
                operator = operatorsCopy.get(i);
            }
        }

        // Now we're done with multiplications and divisions, so we can evaluate the remaining additions and subtractions
        if ( operatorsCopy.size() > 0 ) {
            double result = operandsCopy.get(0);
            for (i = 0; i < operatorsCopy.size(); i += 1) {
                String operator = operatorsCopy.get(i);
                switch (operator) {
                    case "+" -> result += operandsCopy.get(i + 1);
                    case "-" -> result -= operandsCopy.get(i + 1);
                }
            }
            return result;
        } else {
            // Or if there were no additions or subtractions, just return the first operand,
            // which is the result of the sequence of multiplications and divisions that was in the expression
            return operandsCopy.get(0);
        }
    }

    /**
     * Repeatedly prompts the user for an arithmetic expression until a valid expression is entered.
     * @return The (valid) expression entered by the user
     */
     static String promptForExpression() {
        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile("\\d+(\\s*[+\\-*/]\\s*\\d+)+");

        while ( true ) {
            System.out.print("Enter an expression: ");
            String line = scanner.nextLine();

            if (!pattern.matcher(line).matches()) {
                System.out.println("Invalid calculation");
            } else {
                return line;
            }
        }
    }

    /**
     * Returns the list of Integer operands in the given arithmetic expression.
     * Assumes that the expression contains no spaces and has the form
     * operand operator operand operator operand ...
     * @param expression the arithmetic expression
     * @return the list of operands
     */
    static List<Integer> parseOperands(String expression) {

        // One way to do it:
//        String operandStr = "";
//        var operands = new ArrayList<Integer>();
//
//        // Loop through the characters in the expression...
//        for ( int i = 0; i < expression.length(); i += 1 ) {
//            char c = expression.charAt(i);
//            // ...just add digits to the operand string...
//            if ( Character.isDigit(c) ) {
//                operandStr += c;
//            // ...but when we see an operator, add the operand to the list and reset the operand string
//            } else {
//                operands.add(Integer.parseInt(operandStr));
//                operandStr = "";
//            }
//        }
//        operands.add(Integer.parseInt(operandStr));
//
//        return operands

        // Another (simpler) way to do it:
        // Use a regular expression to split the expression into operand strings
        var operandStrings = expression.split("[+\\-*/]");
        var operands = new ArrayList<Integer>();
        // Then simply convert each operand string to an integer and add it to the list
        for ( String operandStr : operandStrings ) {
            operands.add(Integer.parseInt(operandStr));
        }

        return operands;

    }

    /**
     * Returns the list of operators in the given arithmetic expression.
     * Assumes that the expression contains no spaces and has the form
     * operand operator operand operator operand ...
     * @param expression the arithmetic expression
     * @return the list of operators
     */
    static List<String> parseOperators(String expression) {

        // One way to do it:
//        var operators = new ArrayList<String>();
//
//        // Loop through the characters in the expression...
//        for ( int i = 0; i < expression.length(); i += 1 ) {
//            char c = expression.charAt(i);
//            // ...and add only the operators to the list
//            if ( ! Character.isDigit(c) ) {
//                operators.add(c+"");
//            }
//        }
//        return operators;

        // Another way to do it:
        // Remove all digits from the expression and convert the remaining string of operators to a list
        var chars = expression.replaceAll("\\d", "").toCharArray();
        var operators = new ArrayList<String>();
        for ( char c : chars ) {
            operators.add(c+"");
        }
        return operators;
    }

}