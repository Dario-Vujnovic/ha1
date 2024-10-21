package htw.berlin.prog2.ha1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Retro calculator")
class CalculatorTest {

    @Test
    @DisplayName("should display result after adding two positive multi-digit numbers")
    void testPositiveAddition() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(2);
        calc.pressDigitKey(0);
        calc.pressBinaryOperationKey("+");
        calc.pressDigitKey(2);
        calc.pressDigitKey(0);
        calc.pressEqualsKey();

        String expected = "40";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display result after getting the square root of two")
    void testSquareRoot() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(2);
        calc.pressUnaryOperationKey("√");

        String expected = "1.41421356";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display error when dividing by zero")
    void testDivisionByZero() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(7);
        calc.pressBinaryOperationKey("/");
        calc.pressDigitKey(0);
        calc.pressEqualsKey();

        String expected = "Error";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display error when drawing the square root of a negative number")
    void testSquareRootOfNegative() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(7);
        calc.pressNegativeKey();
        calc.pressUnaryOperationKey("√");

        String expected = "Error";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should not allow multiple decimal dots")
    void testMultipleDecimalDots() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(1);
        calc.pressDotKey();
        calc.pressDigitKey(7);
        calc.pressDotKey();
        calc.pressDigitKey(8);

        String expected = "1.78";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }


    //TODO hier weitere Tests erstellen
    //erster selbst geschriebener Test bei dem ich mir nicht sicher bin ob die Aufgabenstellung abgedeckt ist
    @Test
    @DisplayName("It should not allow multiple plus signs in a row without numbers in between during additions.")
    void testMultiplePlusSignsInARow(){
        Calculator calc = new Calculator();

        calc.pressDigitKey(4);
        calc.pressBinaryOperationKey("+");
        calc.pressBinaryOperationKey("+");
        calc.pressDigitKey(4);

        String expected = "Error";
        String actual = calc.readScreen();

        assertEquals(expected, actual);

    }

    //zweiter selbstgeschriebener Test die definitiv die Aufgabenstellung erfüllen sollte
    @Test
    @DisplayName("It should correctly multiply two numbers")
    void testSimpleMultiplication() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(6); // Drücke: 6
        calc.pressBinaryOperationKey("x"); // Drück: x (Multiplikation)
        calc.pressDigitKey(7); // Drücke: 7
        calc.pressEqualsKey(); // Drücke: das Istgleich

        String expected = "42"; // Erwartung: "42"
        String actual = calc.readScreen();

        assertEquals(expected, actual); // Vergleich von Erwartung und tatsächlichem Ergebnis
    }

    //Teilaufgabe2 ab hier
    @Test
    @DisplayName("should display error when trying to divide without an operand and pressing equals")
    void testDivisionWithoutOperandAndEquals() {
        Calculator calc = new Calculator();

        calc.pressBinaryOperationKey("/"); // Divisionstaste ohne vorherige Ziffer
        calc.pressEqualsKey(); // Drücke gleich

        String expected = "Error"; // Erwartet: "Error"
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }



}

