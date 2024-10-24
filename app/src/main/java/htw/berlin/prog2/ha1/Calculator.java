package htw.berlin.prog2.ha1;

/**
 * Eine Klasse, die das Verhalten des Online Taschenrechners imitiert, welcher auf
 * https://www.online-calculator.com/ aufgerufen werden kann (ohne die Memory-Funktionen)
 * und dessen Bildschirm bis zu zehn Ziffern plus einem Dezimaltrennzeichen darstellen kann.
 * Enthält mit Absicht noch diverse Bugs oder unvollständige Funktionen.
 */
public class Calculator {

    private String screen = "0";
    private double latestValue;
    private String latestOperation = "";
    private double latestOperand = 0.0;  // Neue Variable zum Speichern des letzten Operanden

    /**
     * @return den aktuellen Bildschirminhalt als String
     */
    public String readScreen() {
        return screen;
    }

    /**
     * Empfängt den Wert einer gedrückten Zifferntaste. Da man nur eine Taste auf einmal
     * drücken kann muss der Wert positiv und einstellig sein und zwischen 0 und 9 liegen.
     * Führt in jedem Fall dazu, dass die gerade gedrückte Ziffer auf dem Bildschirm angezeigt
     * oder rechts an die zuvor gedrückte Ziffer angehängt angezeigt wird.
     * @param digit Die Ziffer, deren Taste gedrückt wurde
     */
    public void pressDigitKey(int digit) {
        if (digit > 9 || digit < 0) throw new IllegalArgumentException();

        if (screen.equals("0") || latestValue == Double.parseDouble(screen)) screen = "";

        screen = screen + digit;
    }

    /**
     * Empfängt den Befehl der C- bzw. CE-Taste (Clear bzw. Clear Entry).
     * Einmaliges Drücken der Taste löscht die zuvor eingegebenen Ziffern auf dem Bildschirm
     * so dass "0" angezeigt wird, jedoch ohne zuvor zwischengespeicherte Werte zu löschen.
     * Wird daraufhin noch einmal die Taste gedrückt, dann werden auch zwischengespeicherte
     * Werte sowie der aktuelle Operationsmodus zurückgesetzt, so dass der Rechner wieder
     * im Ursprungszustand ist.
     */
    public void pressClearKey() {
        screen = "0";
        latestOperation = "";
        latestValue = 0.0;
        latestOperand = 0.0;  // Reset auch des letzten Operanden für den zweiten roten Fall gebraucht
    }

    /**
     * Empfängt den Wert einer gedrückten binären Operationstaste, also eine der vier Operationen
     * Addition, Substraktion, Division, oder Multiplikation, welche zwei Operanden benötigen.
     * Beim ersten Drücken der Taste wird der Bildschirminhalt nicht verändert, sondern nur der
     * Rechner in den passenden Operationsmodus versetzt.
     * Beim zweiten Drücken nach Eingabe einer weiteren Zahl wird direkt das aktuelle Zwischenergebnis
     * auf dem Bildschirm angezeigt. Falls hierbei eine Division durch Null auftritt, wird "Error" angezeigt.
     * @param operation "+" für Addition, "-" für Substraktion, "x" für Multiplikation, "/" für Division
     */

    //Anpassung für den zweiten roten Fall
    public void pressBinaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);  // Der aktuelle Bildschirmwert wird als neuer Ausgangswert verwendet
        latestOperation = operation;  // Die neue Operation wird gespeichert
        latestOperand = 0.0;  // Reset des letzten Operanden für neue Eingabe
    }

    /**
     * Empfängt den Wert einer gedrückten unären Operationstaste, also eine der drei Operationen
     * Quadratwurzel, Prozent, Inversion, welche nur einen Operanden benötigen.
     * Beim Drücken der Taste wird direkt die Operation auf den aktuellen Zahlenwert angewendet und
     * der Bildschirminhalt mit dem Ergebnis aktualisiert.
     * @param operation "√" für Quadratwurzel, "%" für Prozent, "1/x" für Inversion
     */
    public void pressUnaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;
        var result = switch (operation) {
            case "√" -> Math.sqrt(Double.parseDouble(screen));
            case "%" -> Double.parseDouble(screen) / 100;
            case "1/x" -> 1 / Double.parseDouble(screen);
            default -> throw new IllegalArgumentException();
        };
        screen = Double.toString(result);
        if (screen.equals("NaN")) screen = "Error";
        if (screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10);
    }

    /**
     * Empfängt den Befehl der gedrückten Dezimaltrennzeichentaste, im Englischen üblicherweise ".".
     * Fügt beim ersten Mal Drücken dem aktuellen Bildschirminhalt das Trennzeichen auf der rechten
     * Seite hinzu und aktualisiert den Bildschirm. Daraufhin eingegebene Zahlen werden rechts vom
     * Trennzeichen angegeben und daher als Dezimalziffern interpretiert.
     * Beim zweimaligem Drücken, oder wenn bereits ein Trennzeichen angezeigt wird, passiert nichts.
     */
    public void pressDotKey() {
        if (!screen.contains(".")) screen = screen + ".";
    }

    /**
     * Empfängt den Befehl der gedrückten Vorzeichenumkehrstaste ("+/-").
     * Zeigt der Bildschirm einen positiven Wert an, so wird ein "-" links angehängt, der Bildschirm
     * aktualisiert und der Inhalt fortan als negativ interpretiert.
     * Zeigt der Bildschirm bereits einen negativen Wert mit führendem Minus an, dann wird dieses
     * entfernt und der Inhalt fortan als positiv interpretiert.
     */
    public void pressNegativeKey() {
        screen = screen.startsWith("-") ? screen.substring(1) : "-" + screen;
    }

    /**
     * Empfängt den Befehl der gedrückten "="-Taste.
     * Wurde zuvor keine Operationstaste gedrückt, passiert nichts.
     * Wurde zuvor eine binäre Operationstaste gedrückt und zwei Operanden eingegeben, wird das
     * Ergebnis der Operation angezeigt. Falls hierbei eine Division durch Null auftritt, wird "Error" angezeigt.
     * Wird die Taste weitere Male gedrückt (ohne andere Tasten dazwischen), so wird die letzte
     * Operation (ggf. inklusive letztem Operand) erneut auf den aktuellen Bildschirminhalt angewandt
     * und das Ergebnis direkt angezeigt.
     */
    public void pressEqualsKey() {
        double currentValue = Double.parseDouble(screen);  // Den aktuellen Wert vom Bildschirm holen

        // Wenn keine Operation vorher festgelegt wurde, beenden (keine Berechnung)
        if (latestOperation.isEmpty()) return;

        // Nur wenn der Operand noch nicht gesetzt wurde (also beim ersten Drücken von "="), speichern wir ihn
        if (latestOperand == 0.0) {
            latestOperand = currentValue;  // Speichern des aktuellen Werts als zweiten Operand für spätere Wiederholung
        }

        // Berechnung basierend auf der gespeicherten Operation und dem gespeicherten Operand
        var result = switch (latestOperation) {
            case "+" -> latestValue + latestOperand;  // Wiederholte Addition
            case "-" -> latestValue - latestOperand;  // Wiederholte Subtraktion
            case "x" -> latestValue * latestOperand;  // Wiederholte Multiplikation
            case "/" -> latestOperand == 0 ? Double.POSITIVE_INFINITY : latestValue / latestOperand;  // Division, Fehler bei Division durch 0
            default -> throw new IllegalArgumentException();  // Falls keine gültige Operation vorhanden ist
        };

        // Das Ergebnis der Berechnung wird als neuer Wert gespeichert
        latestValue = result;
        screen = Double.toString(result);  // Der Bildschirm zeigt das Ergebnis der Berechnung an

        // Falls eine Division durch Null erfolgt ist, zeigen wir "Error" an
        if (screen.equals("Infinity")) screen = "Error";

        // Falls das Ergebnis auf .0 endet, entfernen wir die Nachkommastellen
        if (screen.endsWith(".0")) screen = screen.substring(0, screen.length() - 2);

        // Wenn das Ergebnis zu viele Stellen hat, kürzen wir es auf maximal 10 Zeichen
        if (screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10);
    }

}

