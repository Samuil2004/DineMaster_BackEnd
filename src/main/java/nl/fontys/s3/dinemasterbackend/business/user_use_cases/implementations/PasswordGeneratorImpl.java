package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGeneratorImpl {
    public String generateValidPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-_+=<>?";
        String allowedChars = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        SecureRandom random = new SecureRandom();

        int passwordLength = random.nextInt(9) + 8;

        StringBuilder password = new StringBuilder();
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < passwordLength; i++) {
            password.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }

        String shuffledPassword = shuffleString(password.toString(), random);

        if (shuffledPassword.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.*\\s).{8,16}$")) {
            return shuffledPassword;
        } else {
            return generateValidPassword();
        }
    }

    private static String shuffleString(String input, SecureRandom random) {
        char[] array = input.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return new String(array);
    }
}
