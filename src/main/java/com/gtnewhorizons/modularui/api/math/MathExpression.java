package com.gtnewhorizons.modularui.api.math;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

public class MathExpression {

    private static final List<Object> DEFAULT = Collections.singletonList(0);

    public static double parseMathExpression(String expr) {
        return parseMathExpression(expr, 0);
    }

    public static double parseMathExpression(String expr, double onFailReturn) {
        List<Object> parsed = buildParsedList(expr, onFailReturn);
        if (parsed == DEFAULT || parsed.size() == 0) {
            return onFailReturn;
        }
        if (parsed.size() == 1) {
            Object value = parsed.get(0);
            return value instanceof Double ? (double) value : onFailReturn;
        }
        Double lastNum = null;
        for (int i = 0; i < parsed.size(); i++) {
            Object obj = parsed.get(i);
            if (lastNum == null && obj instanceof Double) {
                lastNum = (Double) obj;
                continue;
            }
            if (obj == Operator.POWER) {
                Double newNum = Math.pow(lastNum, (Double) parsed.get(i + 1));
                parsed.remove(i - 1);
                parsed.remove(i - 1);
                parsed.remove(i - 1);
                parsed.add(i - 1, newNum);
                lastNum = newNum;
                i--;
                continue;
            }
            lastNum = null;
        }
        if (lastNum != null) {
            lastNum = null;
        }
        if (parsed.size() > 1) {
            for (int i = 0; i < parsed.size(); i++) {
                Object obj = parsed.get(i);
                if (lastNum == null && obj instanceof Double) {
                    lastNum = (Double) obj;
                    continue;
                }
                if (obj == Operator.MULTIPLY) {
                    Double newNum = lastNum * (Double) parsed.get(i + 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.add(i - 1, newNum);
                    lastNum = newNum;
                    i--;
                    continue;
                }
                if (obj == Operator.DIVIDE) {
                    Double newNum = lastNum / (Double) parsed.get(i + 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.add(i - 1, newNum);
                    lastNum = newNum;
                    i--;
                    continue;
                }
                if (obj == Operator.MOD) {
                    Double newNum = lastNum % (Double) parsed.get(i + 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.add(i - 1, newNum);
                    lastNum = newNum;
                    i--;
                    continue;
                }
                lastNum = null;
            }
            if (lastNum != null) {
                lastNum = null;
            }
        }
        if (parsed.size() > 1) {
            for (int i = 0; i < parsed.size(); i++) {
                Object obj = parsed.get(i);
                if (lastNum == null && obj instanceof Double) {
                    lastNum = (Double) obj;
                    continue;
                }
                if (obj == Operator.PLUS) {
                    Double newNum = lastNum + (Double) parsed.get(i + 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.add(i - 1, newNum);
                    lastNum = newNum;
                    i--;
                    continue;
                }
                if (obj == Operator.MINUS) {
                    Double newNum = lastNum - (Double) parsed.get(i + 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.remove(i - 1);
                    parsed.add(i - 1, newNum);
                    lastNum = newNum;
                    i--;
                    continue;
                }
                lastNum = null;
            }
        }
        if (parsed.size() != 1) {
            throw new IllegalStateException("Calculated expr has more than 1 result. " + parsed);
        }
        return (Double) parsed.get(0);
    }

    public static List<Object> buildParsedList(String expr, double onFailReturn) {
        List<Object> parsed = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        if (expr == null || expr.isEmpty()) return parsed;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            switch (c) {
                case '+': {
                    if (builder.length() > 0) {
                        parsed.add(parse(builder.toString(), onFailReturn));
                        builder.delete(0, builder.length());
                    }
                    parsed.add(Operator.PLUS);
                    break;
                }
                case '-': {
                    if (builder.length() > 0) {
                        parsed.add(parse(builder.toString(), onFailReturn));
                        builder.delete(0, builder.length());
                    }
                    parsed.add(Operator.MINUS);
                    break;
                }
                case '*': {
                    if (builder.length() > 0) {
                        parsed.add(parse(builder.toString(), onFailReturn));
                        builder.delete(0, builder.length());
                    }
                    parsed.add(Operator.MULTIPLY);
                    break;
                }
                case '/': {
                    if (builder.length() > 0) {
                        parsed.add(parse(builder.toString(), onFailReturn));
                        builder.delete(0, builder.length());
                    }
                    parsed.add(Operator.DIVIDE);
                    break;
                }
                case '%': {
                    if (builder.length() > 0) {
                        parsed.add(parse(builder.toString(), onFailReturn));
                        builder.delete(0, builder.length());
                    }
                    parsed.add(Operator.MOD);
                    break;
                }
                case '^': {
                    if (builder.length() > 0) {
                        parsed.add(parse(builder.toString(), onFailReturn));
                        builder.delete(0, builder.length());
                    }
                    parsed.add(Operator.POWER);
                    break;
                }
                default:
                    builder.append(c);
            }
        }
        if (builder.length() > 0) {
            parsed.add(parse(builder.toString(), onFailReturn));
        }
        if (parsed.size() >= 2 && parsed.get(0) == Operator.MINUS && parsed.get(1) instanceof Double) {
            parsed.add(0, 0.0);
        }
        boolean shouldBeOperator = false;
        for (Object object : parsed) {
            if (shouldBeOperator) {
                if (!(object instanceof Operator)) {
                    return DEFAULT;
                }
                shouldBeOperator = false;
            } else {
                if (!(object instanceof Double)) {
                    return DEFAULT;
                }
                shouldBeOperator = true;
            }
        }
        while (parsed.get(parsed.size() - 1) instanceof Operator) {
            parsed.remove(parsed.size() - 1);
        }
        return parsed;
    }

    public static double parse(String num, double onFailReturn) {
        try {
            return TextFieldWidget.format.parse(num).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return onFailReturn;
    }

    public enum Operator {

        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        MOD("%"),
        POWER("^");

        public final String sign;

        Operator(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return sign;
        }
    }
}
