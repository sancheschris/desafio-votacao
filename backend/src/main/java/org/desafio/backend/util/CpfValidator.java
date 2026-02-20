package org.desafio.backend.util;

public final class CpfValidator {
    private CpfValidator() {}

    public static boolean isValid(String cpf) {

        if (cpf == null){
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11){
            return false;
        }

        // evita sequência repetida tipo 11111111111
        if (cpf.chars().distinct().count() == 1){
            return false;
        }

        int[] digits = cpf.chars().map(c -> c - '0').toArray();

        int firstDigit = calculateDigit(digits, 9, 10);
        int secondDigit = calculateDigit(digits, 10, 11);

        return digits[9] == firstDigit &&
                digits[10] == secondDigit;
    }

    public static int calculateDigit(int[] digits, int length, int weightStart) {
        int sum = 0;
        int weight = weightStart;

        for (int i = 0; i < length; i++) {
            sum += digits[i] * weight--;
        }

        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}
