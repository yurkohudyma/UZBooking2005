package ua.hudyma.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class IdGenerator {
    private final static SecureRandom secureRandom = new SecureRandom();

    public static LocalDate generateIssuedOn() {
        var today = LocalDate.now();
        int daysBack = new SecureRandom().nextInt(365 * 10);
        return today.minusDays(daysBack);
    }
    public static String generateId(int letterLength, int numberLength) {
        String letters = generateRandomUppercaseLetters(letterLength);
        String numbers = generateRandomDigits(numberLength);
        return letters + numbers;
    }

    public static Long generateLongId (){
        return ThreadLocalRandom.current().nextLong();
    }

    ////CBFB48A0-AC1D24F1-0001
    public static String generateUzTicketId (){
        var firstPart = generateRandomUppercaseLetters(4) +
                generateRandomDigits(2)
                + generateRandomUppercaseLetters(1)
                + generateRandomDigits(1);
        var secondPart = generateRandomUppercaseLetters(2)
                + generateRandomDigits(1)
                + generateRandomUppercaseLetters(1)
                + generateRandomDigits(2)
                + generateRandomUppercaseLetters(1)
                + generateRandomDigits(1);
        var thirdPart = generateRandomDigits(4);
        return String.format("%s-%s-%s", firstPart, secondPart, thirdPart);
    }

    private static String generateRandomUppercaseLetters(int length) {
        return secureRandom
                .ints('A',
                        'Z' + 1)
                .limit(length)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    private static String generateRandomDigits(int length) {
        return secureRandom
                .ints('0',
                        '9' + 1)
                .limit(length)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    public static <T extends Enum<T>> T getRandomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        int index = secureRandom.nextInt(values.length);
        return values[index];
    }
}