package Utils;

import java.util.*;
import java.util.function.Predicate;

public class PredicateParser {

    static Deque<String> tempStack;
    static List<String> result;

    public static synchronized Predicate<String> parse(String rule) {
        return doParse(toRPN(rule));
    }

    private static List<String> toRPN(String rule) {
        tempStack = new ArrayDeque<>();
        result = new ArrayList<>();
        String[] rulesArray = ruleToArray(rule);

        for (var element : rulesArray) {
            switch (element) {
                case "or", "and", "not" -> gotOper(element);
                case "(" -> tempStack.push(element);
                case ")" -> gotParen();
                default -> result.add(element);
            }
        }
        while (!tempStack.isEmpty()) {
            result.add(tempStack.pop());
        }
        return result;
    }

    private static void gotOper(String operator) {
        while (!tempStack.isEmpty()) {
            String opTop = tempStack.pop();
            if (opTop.equals("(")) {
                tempStack.push(opTop);
                break;
            } else {
                if (getPriority(opTop) < getPriority(operator)) {
                    tempStack.push(opTop);
                    break;
                } else
                    result.add(opTop);
            }
        }
        tempStack.push(operator);
    }

    private static void gotParen() {
        while (!tempStack.isEmpty()) {
            String element = tempStack.pop();
            if (element.equals("("))
                break;
            else
                result.add(element);
        }
    }

    private static String[] ruleToArray(String rule) {
        return rule
                .replace("(", "( ")
                .replace(")", " )")
                .split("\\s+");
    }

    private static int getPriority(String operator) {
        return switch (operator) {
            case "or" -> 1;
            case "and" -> 2;
            case "not" -> 3;
            default -> 0;
        };
    }

    private static Predicate<String> doParse(List<String> rpn) {
        Deque<Predicate<String>> merged = new ArrayDeque<>();
        Predicate<String> pred1, pred2;

        for (var element : rpn) {
            if (element.equals("not")) {
                pred2 = merged.pop();
                merged.push(pred2.negate());
            } else if (element.equals("and") || element.equals("or")) {
                pred2 = merged.pop();
                pred1 = merged.pop();
                merged.push(element.equals("or") ? pred1.or(pred2) : pred1.and(pred2));
            } else {
                merged.push(p -> p.contains(element)); // <-- Create predicate is here
            }
        }
        return merged.pop();
    }
}