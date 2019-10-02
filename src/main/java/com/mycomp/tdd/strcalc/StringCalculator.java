package com.mycomp.tdd.strcalc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringCalculator {

    private final String fixedDelimiter = ",";
    private final String newLine = "\n";
    private final String empty = "";
    private final String isNumeric = "^[-+]?\\d+";
    private final String sqStart = "[";
    private final String sqEnd = "]";

    private String delimiter = empty;

    public int add(String number) {
        if (number.isEmpty()) {
            return 0;
        }

        setDelimiterFrom(number);

        if (isValidToCalculateSum(number)) {
            number = formatWithFixedDelimiter(number);
            String numbers[] = splitWithFixedDelimiter(number);
            ensureAllNonNegatives(numbers);
            return calculateSum(numbers);
        } else {
            return doNumber(number);
        }
    }

    private void setDelimiterFrom(String number) {
        initDelimiter(number);
        handleSquareBrackets();
        ifEmptyDelimiterSetToDefault(number);
    }

    private void initDelimiter(String number) {
        delimiter = empty;
        if (isWithDelimiters(number)) {
            delimiter = number.substring(2, number.indexOf(newLine));
        }
    }

    private void handleSquareBrackets() {
        if (delimiter.contains(sqStart))
            delimiter = delimiter.replaceAll(Pattern.quote(sqStart), empty);
        if (delimiter.contains(sqEnd))
            delimiter = delimiter.replaceAll(Pattern.quote(sqEnd), empty);
    }

    private void ifEmptyDelimiterSetToDefault(String number) {
        delimiter = setDelimiterIfEmptyTo(number, fixedDelimiter);
        delimiter = setDelimiterIfEmptyTo(number, newLine);
    }

    private boolean isValidToCalculateSum(String number) {
        boolean contains;
        if (multiDelim(delimiter)) {
            List<String> delimiters = regExDelimiter(delimiter);
            contains = doesNumberMatches(number, delimiters);
        } else {
            contains = number.contains(delimiter);
        }
        return (!number.matches(isNumeric) && contains);
    }

    private String formatWithFixedDelimiter(String number) {
        if (isWithDelimiters(number)) {
            number = number.substring(number.indexOf(newLine) + newLine.length());
        }
        if (multiDelim(delimiter)) {
            List<String> delimiters = regExDelimiter(delimiter);
            for (String delim : delimiters) {
                number = number.replaceAll(Pattern.quote(delim), fixedDelimiter);
            }
            return number;
        } else if (number.contains(delimiter)) {
            String[] numbers = number.split(Pattern.quote(delimiter));
            return String.join(fixedDelimiter, numbers);
        } else {
            return number;
        }
    }

    private String[] splitWithFixedDelimiter(String number) {
        String[] numbers = number.split(Pattern.quote(fixedDelimiter));
        if (allBlanks(numbers))
            throw new RuntimeException("Invalid input");
        return numbers;
    }

    private void ensureAllNonNegatives(String[] numbers) {
        List<String> negatives = new ArrayList<>();
        for (int i = 0; i < numbers.length; i++) {
            int num = parseAndGet(i, numbers);
            if (num < 0) {
                negatives.add(numbers[i]);
            }
        }
        if (!negatives.isEmpty())
            throw new RuntimeException("negatives not allowed:" + errorMessage(negatives));
    }

    private int calculateSum(String[] numbers) {
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            int num = parseAndGet(i, numbers);
            sum += num;
        }
        return sum;
    }

    private boolean multiDelim(String delimiter) {
        String[] parts = delimiter.split(empty);
        Set<String> set = Arrays.stream(parts)
                .collect(Collectors.toSet());
        return set.size() > 1;
    }

    private int doNumber(String number) {
        int num = integerOf(number);
        if (num < 0)
            throw new RuntimeException("negatives not allowed:" + number);
        return num;
    }

    private Integer integerOf(String number) {
        try {
            return Integer.valueOf(number);
        } catch (Exception e) {
            throw new RuntimeException("Invalid input");
        }
    }

    private boolean allBlanks(String[] numbers) {
        return String.join(empty, numbers).isEmpty();
    }

    private String errorMessage(List<String> negatives) {
        return String.join(fixedDelimiter, negatives);
    }

    private List<String> regExDelimiter(String delimiter) {
        return Arrays.asList(delimiter.split(empty));
    }

    private boolean isWithDelimiters(String number) {
        return number.startsWith("//") && number.contains(newLine);
    }

    private boolean doesNumberMatches(String number, List<String> delimiter) {
        boolean contains = true;
        for (String delim : delimiter) {
            contains = contains && number.contains(delim);
            if (!contains)
                return false;
        }
        return contains;
    }

    private String setDelimiterIfEmptyTo(String number, String delim) {
        if (delimiter.isEmpty()) {
            if (number.contains(delim))
                delimiter = delim;
        } else {
            if (number.contains(delim))
                delimiter += delim;
        }
        return delimiter;
    }

    private int parseAndGet(int index, String[] numbers) {
        int num = numbers[index].length() == 0 ? 0 : integerOf(numbers[index]);
        if (num > 1000)
            return 0;
        return num;
    }
}
