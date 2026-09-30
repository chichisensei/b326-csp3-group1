package com.joysistvi.CyberAccess.cliview;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleInput {

    private final Scanner scanner;
    public ConsoleInput(Scanner scanner) { this.scanner = scanner; }
    public String text(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) throw new EndOfInput();
        return scanner.nextLine().trim();
    }
    public int number(String prompt) {
        while (true) {
            try { return Integer.parseInt(text(prompt)); }
            catch (NumberFormatException e) { System.out.println("Enter a whole number."); }
        }
    }
    public BigDecimal decimal(String prompt) {
        while (true) {
            try { return new BigDecimal(text(prompt)); }
            catch (NumberFormatException e) { System.out.println("Enter a number, for example 25.00."); }
        }
    }
    public boolean confirm(String prompt) { return text(prompt + " (yes/no): ").equalsIgnoreCase("yes"); }
    public static class EndOfInput extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
