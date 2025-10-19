package com.example.assignment1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private TextView Edit_Text;
    private LinearLayout scientificPanel;
    private Button btnScientificToggle;


    private String currentInput = "0";
    private List<String> expression = new ArrayList<>();
    private boolean resetOnNextInput = false;
    private boolean isScientificMode = false;
    private boolean lastInputWasOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Edit_Text = findViewById(R.id.Edit_Text);
        scientificPanel = findViewById(R.id.scientificPanel);
        btnScientificToggle = findViewById(R.id.btnScientificToggle);
        setupClickListeners();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setupLandscapeMode();
        } else {
            setupPortraitMode();
        }
    }

    private void setupClickListeners() {

        findViewById(R.id.btn0).setOnClickListener(v -> onNumberPressed("0"));
        findViewById(R.id.btn1).setOnClickListener(v -> onNumberPressed("1"));
        findViewById(R.id.btn2).setOnClickListener(v -> onNumberPressed("2"));
        findViewById(R.id.btn3).setOnClickListener(v -> onNumberPressed("3"));
        findViewById(R.id.btn4).setOnClickListener(v -> onNumberPressed("4"));
        findViewById(R.id.btn5).setOnClickListener(v -> onNumberPressed("5"));
        findViewById(R.id.btn6).setOnClickListener(v -> onNumberPressed("6"));
        findViewById(R.id.btn7).setOnClickListener(v -> onNumberPressed("7"));
        findViewById(R.id.btn8).setOnClickListener(v -> onNumberPressed("8"));
        findViewById(R.id.btn9).setOnClickListener(v -> onNumberPressed("9"));


        findViewById(R.id.btnPlus).setOnClickListener(v -> onOperatorPressed("+"));
        findViewById(R.id.btnMinus).setOnClickListener(v -> onOperatorPressed("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> onOperatorPressed("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> onOperatorPressed("÷"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> onEqualsPressed());
        findViewById(R.id.btnDecimal).setOnClickListener(v -> onDecimalPressed());


        findViewById(R.id.btnAC).setOnClickListener(v -> onAllClearPressed());
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> onPlusMinusPressed());
        findViewById(R.id.btnPercent).setOnClickListener(v -> onPercentPressed());


        findViewById(R.id.btnSin).setOnClickListener(v -> onScientificFunctionPressed("sin"));
        findViewById(R.id.btnCos).setOnClickListener(v -> onScientificFunctionPressed("cos"));
        findViewById(R.id.btnTan).setOnClickListener(v -> onScientificFunctionPressed("tan"));
        findViewById(R.id.btnLog).setOnClickListener(v -> onScientificFunctionPressed("log"));
        findViewById(R.id.btnLn).setOnClickListener(v -> onScientificFunctionPressed("ln"));
        findViewById(R.id.btnSqrt).setOnClickListener(v -> onScientificFunctionPressed("sqrt"));
        findViewById(R.id.btnSquare).setOnClickListener(v -> onScientificFunctionPressed("square"));
        findViewById(R.id.btnCube).setOnClickListener(v -> onScientificFunctionPressed("cube"));
        findViewById(R.id.btnCubeRoot).setOnClickListener(v -> onScientificFunctionPressed("cuberoot"));
        findViewById(R.id.btnPi).setOnClickListener(v -> onConstantPressed("π"));
        findViewById(R.id.btnE).setOnClickListener(v -> onConstantPressed("e"));
        findViewById(R.id.btnPower).setOnClickListener(v -> onOperatorPressed("^"));
        findViewById(R.id.btnFactorial).setOnClickListener(v -> onScientificFunctionPressed("factorial"));
        findViewById(R.id.btnExp).setOnClickListener(v -> onScientificFunctionPressed("exp"));
        findViewById(R.id.btnMod).setOnClickListener(v -> onOperatorPressed("mod"));
        findViewById(R.id.btnSinH).setOnClickListener(v -> onScientificFunctionPressed("sinh"));
        findViewById(R.id.btnCosH).setOnClickListener(v -> onScientificFunctionPressed("cosh"));
        findViewById(R.id.btnTanH).setOnClickListener(v -> onScientificFunctionPressed("tanh"));
        findViewById(R.id.btnLog2).setOnClickListener(v -> onScientificFunctionPressed("log2"));
        findViewById(R.id.btnLog10).setOnClickListener(v -> onScientificFunctionPressed("log10"));
        findViewById(R.id.btnOpenBracket).setOnClickListener(v -> onBracketPressed("("));
        findViewById(R.id.btnCloseBracket).setOnClickListener(v -> onBracketPressed(")"));
        findViewById(R.id.btnDegRad).setOnClickListener(v -> onDegRadToggle());
        findViewById(R.id.btnInv).setOnClickListener(v -> onInversePressed());
        findViewById(R.id.btnMemory).setOnClickListener(v -> onMemoryPressed());
    }

    private void setupPortraitMode() {
        btnScientificToggle.setOnClickListener(v -> toggleScientificMode());
    }

    private void setupLandscapeMode() {
        if (btnScientificToggle != null) {
            btnScientificToggle.setVisibility(View.GONE);
        }
        isScientificMode = true;
    }

    private void toggleScientificMode() {
        if (isScientificMode) {
            scientificPanel.setVisibility(View.GONE);
            btnScientificToggle.setText("SCI");
        } else {
            scientificPanel.setVisibility(View.VISIBLE);
            btnScientificToggle.setText("BASIC");
        }
        isScientificMode = !isScientificMode;
    }

    private void onNumberPressed(String number) {
        if (resetOnNextInput || currentInput.equals("0") || currentInput.equals("Error")) {
            currentInput = number;
            resetOnNextInput = false;
        } else {
            currentInput += number;
        }
        lastInputWasOperator = false;
        updateDisplay();
    }


    private void onOperatorPressed(String operator) {
        if (currentInput.equals("Error")) return;

        if (lastInputWasOperator && !expression.isEmpty()) {
            expression.set(expression.size() - 1, operator);
        } else {
            expression.add(currentInput);
            expression.add(operator);
            currentInput = "";
        }

        lastInputWasOperator = true;
        updateDisplay();
    }


    private void onEqualsPressed() {
        if (expression.isEmpty() || currentInput.equals("Error")) return;

        if (!lastInputWasOperator) {
            expression.add(currentInput);
        } else {
            expression.remove(expression.size() - 1);
        }

        try {
            double result = evaluateExpression(expression);
            String fullExpression = buildExpressionString(expression);

            currentInput = formatNumber(result);
            expression.clear();
            resetOnNextInput = true;
            lastInputWasOperator = false;

            updateDisplay();
        } catch (Exception ex) {
            showError("Math error");
        }
    }


    private void onScientificFunctionPressed(String function) {
        if (!isScientificMode) return;

        try {
            double value = Double.parseDouble(currentInput);
            double result = applyScientificFunction(value, function);

            currentInput = formatNumber(result);
            resetOnNextInput = true;
            updateDisplay();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    private void onConstantPressed(String constant) {
        if (!isScientificMode) return;

        switch (constant) {
            case "π":
                currentInput = String.valueOf(Math.PI);
                break;
            case "e":
                currentInput = String.valueOf(Math.E);
                break;
        }
        resetOnNextInput = true;
        updateDisplay();
    }


    private void onBracketPressed(String bracket) {
        if (!isScientificMode) return;

        if (resetOnNextInput || currentInput.equals("0") || currentInput.equals("Error")) {
            currentInput = bracket;
            resetOnNextInput = false;
        } else {
            currentInput += bracket;
        }
        updateDisplay();
    }


    private void onAllClearPressed() {
        currentInput = "0";
        expression.clear();
        resetOnNextInput = false;
        lastInputWasOperator = false;
        updateDisplay();
    }

    private void onDecimalPressed() {
        if (resetOnNextInput || currentInput.equals("Error")) {
            currentInput = "0.";
            resetOnNextInput = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        lastInputWasOperator = false;
        updateDisplay();
    }

    private void onPlusMinusPressed() {
        if (!currentInput.equals("0") && !currentInput.equals("Error")) {
            if (currentInput.startsWith("-")) {
                currentInput = currentInput.substring(1);
            } else {
                currentInput = "-" + currentInput;
            }
            updateDisplay();
        }
    }

    private void onPercentPressed() {
        try {
            double value = Double.parseDouble(currentInput);
            currentInput = formatNumber(value / 100);
            resetOnNextInput = true;
            updateDisplay();
        } catch (Exception e) {
            showError("Invalid input");
        }
    }


    private void onDegRadToggle() {
        // Implement degree/radian toggle logic
        showError("DEG/RAD toggle not implemented");
    }

    private void onInversePressed() {
        try {
            double value = Double.parseDouble(currentInput);
            if (value == 0) throw new ArithmeticException("Cannot divide by zero");
            currentInput = formatNumber(1 / value);
            resetOnNextInput = true;
            updateDisplay();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void onMemoryPressed() {
        showError("Memory function not implemented");
    }


    private double applyScientificFunction(double value, String function) {
        switch (function) {
            case "sin":
                return Math.sin(Math.toRadians(value));
            case "cos":
                return Math.cos(Math.toRadians(value));
            case "tan":
                return Math.tan(Math.toRadians(value));
            case "log":
                if (value <= 0) throw new ArithmeticException("Invalid input for log");
                return Math.log10(value);
            case "ln":
                if (value <= 0) throw new ArithmeticException("Invalid input for ln");
                return Math.log(value);
            case "sqrt":
                if (value < 0) throw new ArithmeticException("Cannot square root negative number");
                return Math.sqrt(value);
            case "square":
                return value * value;
            case "cube":
                return value * value * value;
            case "cuberoot":
                return Math.cbrt(value);
            case "factorial":
                if (value < 0 || value != Math.floor(value))
                    throw new ArithmeticException("Invalid input for factorial");
                return factorial((int) value);
            case "exp":
                return Math.exp(value);
            case "sinh":
                return Math.sinh(value);
            case "cosh":
                return Math.cosh(value);
            case "tanh":
                return Math.tanh(value);
            case "log2":
                if (value <= 0) throw new ArithmeticException("Invalid input for log2");
                return Math.log(value) / Math.log(2);
            case "log10":
                if (value <= 0) throw new ArithmeticException("Invalid input for log10");
                return Math.log10(value);
            default:
                throw new IllegalArgumentException("Unknown function: " + function);
        }
    }


    private double evaluateExpression(List<String> expr) {
        if (expr.isEmpty()) return Double.parseDouble(currentInput);

        List<String> expressionCopy = new ArrayList<>(expr);


        for (int i = 0; i < expressionCopy.size(); i++) {
            String token = expressionCopy.get(i);
            if (token.equals("×") || token.equals("÷") || token.equals("^") || token.equals("mod")) {
                double left = Double.parseDouble(expressionCopy.get(i - 1));
                double right = Double.parseDouble(expressionCopy.get(i + 1));
                double result = 0;

                switch (token) {
                    case "×": result = left * right; break;
                    case "÷":
                        if (right == 0) throw new ArithmeticException("Cannot divide by zero");
                        result = left / right;
                        break;
                    case "^": result = Math.pow(left, right); break;
                    case "mod":
                        if (right == 0) throw new ArithmeticException("Cannot mod by zero");
                        result = left % right;
                        break;
                }

                expressionCopy.set(i - 1, String.valueOf(result));
                expressionCopy.remove(i);
                expressionCopy.remove(i);
                i--;
            }
        }


        double result = Double.parseDouble(expressionCopy.get(0));
        for (int i = 1; i < expressionCopy.size(); i += 2) {
            String operator = expressionCopy.get(i);
            double number = Double.parseDouble(expressionCopy.get(i + 1));

            if (operator.equals("+")) {
                result += number;
            } else if (operator.equals("-")) {
                result -= number;
            }
        }

        return result;
    }


    private String buildExpressionString(List<String> expr) {
        StringBuilder sb = new StringBuilder();
        for (String token : expr) {
            sb.append(token);
        }
        return sb.toString();
    }

    private void updateDisplay() {
        Edit_Text.setText(currentInput);
    }

    private void showError(String message) {
        currentInput = message;
        Edit_Text.setText(message);
        resetOnNextInput = true;
        expression.clear();
    }

    private String formatNumber(double number) {
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            throw new ArithmeticException("Math error");
        }

        if (number == (long) number) {
            return String.valueOf((long) number);
        } else {
            String formatted = String.format("%.10f", number);
            return formatted.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    // Factorial function
    private int factorial(int n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }
}