package Grapher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A utility class to parse function inputs and evaluate the function at certain points
 */
public class Function
{
  // region Data fields
  /**
   * A list of legal numbers in the input function
   */
  private static final ArrayList<String> LEGAL_NUMBERS = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "x"));

  /**
   * A list of legal operators in the input function
   */
  private static final ArrayList<String> LEGAL_OPERATORS = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "^", "."));
  // endregion

  /**
   * A method to evaluate a function for a certain value of x
   *
   * @param rawFunction A function of x
   * @param x The x value to evaluate the function at
   * @return The result f(x)
   */
  private static double evaluateFunction(String rawFunction, double x)
  {
    ArrayList<String> function = parseFunction(rawFunction);

    // Convert String instances of "x" to its value, e.g. "x" --> "1.23"
    for (int i = 0; i < function.size(); i++)
    {
      if (function.get(i).equals("x"))
      {
        function.set(i, Double.toString(x));
      }
    }

    while (function.size() > 1)
    {
      // Establish starting point at rightmost (. If none, default to start of function
      int cursorStart = 0;
      if (function.contains("("))
      {
        cursorStart = function.lastIndexOf("(");
      }

      // Establish endpoint at first ) after cursorStart. If none, default to end of function
      int cursorEnd = (function.size() - 1);
      for (int i = (cursorStart + 1); i < function.size(); i++)
      {
        if (function.get(i).equals(")"))
        {
          cursorEnd = i;
          break;
        }
      }

      // Create new expression of only stuff in between (). If none, default to entire function
      ArrayList<String> functionExcerpt = new ArrayList<>();
      if (function.get(cursorStart).equals("(") && function.get(cursorEnd).equals(")"))
      {
        for (int i = (cursorStart + 1); i < cursorEnd; i++)
        {
          functionExcerpt.add(function.get(i));
        }
      }
      else
      {
        functionExcerpt.addAll(function);
      }

      // Evaluate functionExcerpt
      String result = evaluateExpression(functionExcerpt).get(0);

      // Replace entire parenthesized excerpt with result value
      function.set(cursorStart, result);
      int removeCursor = cursorStart + 1;
      for (int i = (cursorStart + 1); i <= cursorEnd; i++)
      {
        if (removeCursor <= (function.size() - 1))
        {
          function.remove(removeCursor);
        }
      }
    }
    // Loop the algorithm above until the entire function is evaluated

    // Final answer is one number
    return Double.parseDouble(function.get(0));
  }

  /**
   * A private helper method to parse a function String into an ArrayList
   * <p>
   * Ex: {@code (x^2 + 2.54)} --> {@code [x, ^, 2, +, 2.54]}
   *
   * @param functionString The function input as a String
   * @return An ArrayList representation of the function
   */
  private static ArrayList<String> parseFunction(String functionString)
  {
    ArrayList<String> function = new ArrayList<>();

    // Add "+0" to the function if it is one number
    boolean noOperators = true;
    for (int i = 0; i < functionString.length(); i++)
    {
      if (LEGAL_OPERATORS.contains(Character.toString(functionString.charAt(i))))
      {
        noOperators = false;
        break;
      }
    }
    if (noOperators)
    {
      functionString += "+0";
    }

    // Split function into individual characters, removing spaces
    for (int i = 0; i < functionString.length(); i++)
    {
      if (functionString.charAt(i) != (' '))
      {
        function.add(Character.toString(functionString.charAt(i)));
      }
    }

    int operatorCursor = 0;
    int numberCursor = 0;

    // Retrieve index of first operator in function (except parentheses)
    for (String i : function)
    {
      if (LEGAL_OPERATORS.contains(i) && !i.equals("(") && !i.equals(")") && !i.contains("."))
      {
        operatorCursor = function.indexOf(i);
        break;
      }
    }

    // Run algorithm below until (but not including) the last operator
    boolean moreOperators = true;
    while (moreOperators)
    {
      // Make ArrayList containing number to left of operator. Store index of number's first digit
      ArrayList<String> numberToLeft = new ArrayList<>();
      for (int i = (operatorCursor - 1); i >= 0; i--)
      {
        if (LEGAL_NUMBERS.contains(function.get(i)) || function.get(i).contains("."))
        {
          numberToLeft.add(function.get(i));
          numberCursor = i;
        }
        else if (function.get(i).equals(")")) // Can happen e.g. for the "*" in (1+2) * 3
        {
          continue;
        }
        else
        {
          break;
        }
      }
      Collections.reverse(numberToLeft);

      // Convert number to left into a String
      String result = String.join("", numberToLeft);

      // In function, replace numberToLeft ArrayList with String result
      function.set(numberCursor, result);
      int removeCursor = numberCursor + 1;
      while (LEGAL_NUMBERS.contains(function.get(removeCursor)) || function.get(removeCursor).contains("."))
      {
        function.remove(removeCursor);
        operatorCursor--;
      }

      // Move operatorCursor to next operator. If none left, exit loop
      moreOperators = false;
      for (int i = (operatorCursor + 1); i < function.size(); i++)
      {
        if (LEGAL_OPERATORS.contains(function.get(i))
            && !function.get(i).equals("(")
            && !function.get(i).equals(")")
            && !function.get(i).contains("."))
        {
          moreOperators = true;
          operatorCursor = i;
          break;
        }
      }
    }
    // End repeated algorithm

    // Make ArrayList of number to right of last operator. Store index of number's first digit
    ArrayList<String> numberToRight = new ArrayList<>();
    for (int i = (operatorCursor + 1); i < function.size(); i++)
    {
      if (LEGAL_NUMBERS.contains(function.get(i)) || function.get(i).contains("."))
      {
        numberToRight.add(function.get(i));
        numberCursor = i;
      }
      else if (function.get(i).equals("("))
      {
        continue;
      }
      else
      {
        break;
      }
    }
    numberCursor -= (numberToRight.size() - 1);

    // Convert numberToRight into a String
    String result = String.join("", numberToRight);

    // In function, replace numberToRight ArrayList with String result
    function.set(numberCursor, result);

    int removeCursor = numberCursor + 1;
    while (removeCursor <= (function.size() - 1)
        && (LEGAL_NUMBERS.contains(function.get(removeCursor))
        || function.get(removeCursor).contains(".")))
    {
      function.remove(removeCursor);
    }

    return function;
  }

  /**
   * A private helper method to evaluate an arbitrary-length arithmetic expression with no ()
   *
   * @param expression An expression to evaluate
   * @return The result of the expression
   */
  private static ArrayList<String> evaluateExpression(ArrayList<String> expression)
  {
    int operatorIndex = 0; // Operator to evaluate
    double[] numbers = {0.0, 0.0}; // Numbers to left and right of operator
    String result = "0.0";

    while (expression.size() > 1)
    {
      if (expression.contains("^"))
      {
        operatorIndex = expression.indexOf("^");
        numbers[0] = Double.parseDouble(expression.get(operatorIndex - 1));
        numbers[1] = Double.parseDouble(expression.get(operatorIndex + 1));

        result = Double.toString(Math.pow(numbers[0], numbers[1]));
      }
      else if (expression.contains("*") || expression.contains("/"))
      {
        if (expression.contains("*") && expression.contains("/"))
        {
          operatorIndex = expression.indexOf("*");
          if (expression.indexOf("/") < operatorIndex)
          {
            operatorIndex = expression.indexOf("/");
          }
          numbers[0] = Double.parseDouble(expression.get(operatorIndex - 1));
          numbers[1] = Double.parseDouble(expression.get(operatorIndex + 1));

          result = switch (expression.get(operatorIndex))
          {
            case "*" -> Double.toString(numbers[0] * numbers[1]);
            case "/" -> Double.toString(numbers[0] / numbers[1]);
            default -> result;
          };
        }
        else if (expression.contains("*") && !expression.contains("/"))
        {
          operatorIndex = expression.indexOf("*");
          numbers[0] = Double.parseDouble(expression.get(operatorIndex - 1));
          numbers[1] = Double.parseDouble(expression.get(operatorIndex + 1));

          result = Double.toString(numbers[0] * numbers[1]);
        }
        else if (!expression.contains("*") && expression.contains("/"))
        {
          operatorIndex = expression.indexOf("/");
          numbers[0] = Double.parseDouble(expression.get(operatorIndex - 1));
          numbers[1] = Double.parseDouble(expression.get(operatorIndex + 1));

          result = Double.toString(numbers[0] / numbers[1]);
        }
      }
      else if (expression.contains("+") || expression.contains("-"))
      {
        if (expression.contains("+") && expression.contains("-"))
        {
          operatorIndex = expression.indexOf("+");
          if (expression.indexOf("-") < operatorIndex)
          {
            operatorIndex = expression.indexOf("-");
          }
          numbers[0] = Double.parseDouble(expression.get(operatorIndex - 1));
          numbers[1] = Double.parseDouble(expression.get(operatorIndex + 1));

          result = switch (expression.get(operatorIndex))
          {
            case "+" -> Double.toString(numbers[0] + numbers[1]);
            case "-" -> Double.toString(numbers[0] - numbers[1]);
            default -> result;
          };
        }
        else if (expression.contains("+") && !expression.contains("-"))
        {
          operatorIndex = expression.indexOf("+");
          numbers[0] = Double.parseDouble(expression.get(operatorIndex - 1));
          numbers[1] = Double.parseDouble(expression.get(operatorIndex + 1));

          result = Double.toString(numbers[0] + numbers[1]);
        }
        else if (!expression.contains("+") && expression.contains("-"))
        {
          operatorIndex = expression.indexOf("-");
          numbers[0] = Double.parseDouble(expression.get(operatorIndex - 1));
          numbers[1] = Double.parseDouble(expression.get(operatorIndex + 1));

          result = Double.toString(numbers[0] - numbers[1]);
        }
      }

      // Replace left number, operator, and right number with result value
      int resultIndex = (operatorIndex - 1);
      expression.set(resultIndex, result);

      expression.remove(operatorIndex);
      expression.remove(operatorIndex);
    }

    // Should be one number
    return expression;
  }

  /**
   * A method to evaluate the output of a function at the provided input bounds
   *
   * @param xBounds The left and right bounds of the input
   * @param function The function to evaluate at each boundary point
   * @return The function evaluation at each boundary point
   */
  public static double[] evaluateBounds(double[] xBounds, String function)
  {
    double[] yBounds = new double[2];

    yBounds[0] = evaluateFunction(function, xBounds[0]);
    yBounds[1] = evaluateFunction(function, xBounds[1]);

    return yBounds;
  }
}
