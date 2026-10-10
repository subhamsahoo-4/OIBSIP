# Number Guessing Game — SOLID Java Swing

## Features
- Random number generation
- Easy: 1–50, 10 attempts
- Medium: 1–100, 7 attempts
- Hard: 1–200, 5 attempts
- Too High / Too Low / Correct feedback
- Maximum-attempt loss with secret-number reveal
- Play Again prompt
- Round history and score tracking
- Reset statistics
- SOLID-oriented separation of responsibilities

## Run in VS Code
Open the `NumberGuessingGame` folder, then compile and run:

```bash
javac -d out src/model/*.java src/service/*.java src/ui/*.java src/Main.java
java -cp out Main
```

Java 8+ is sufficient.
