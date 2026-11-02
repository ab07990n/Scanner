// TokenStream.java
// progl.jay
// Implementation of the Scanner for JAY

// This code DOES NOT implement a scanner for JAY yet. You have to complete
// the code and also make sure it implements a scanner for JAY - not something
// else.


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

    private boolean isEof = false;
    private char nextChar = ' ';
    private BufferedReader input;

    public boolean isEoFile() {
        return isEof;
    }

    public TokenStream(String fileName) {
        try {
            input = new BufferedReader(new FileReader(fileName));
            nextChar = readChar();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            isEof = true;
        }
    }

    public Token nextToken() {
        Token t = new Token();
        t.setType("Other");
        t.setValue("");

        skipWhiteSpace();

        if (isEof) {
            return t;
        }

        // Skip comments beginning with //
        while (nextChar == '/') {
            nextChar = readChar();

            if (nextChar == '/') {
                while (!isEof && !isEndOfLine(nextChar)) {
                    nextChar = readChar();
                }
                skipWhiteSpace();

                if (isEof) {
                    return t;
                }
            } else {
                t.setType("Operator");
                t.setValue("/");
                return t;
            }
        }

        // Operators, including two-character operators
        if (isOperator(nextChar)) {
            char first = nextChar;
            t.setType("Operator");
            t.setValue("" + first);
            nextChar = readChar();

            if ((first == '<' || first == '>' ||
                 first == '=' || first == '!') && nextChar == '=') {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            } else if ((first == '&' && nextChar == '&') ||
                       (first == '|' && nextChar == '|')) {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            }

            return t;
        }

        // Separators
        if (isSeparator(nextChar)) {
            t.setType("Separator");
            t.setValue("" + nextChar);
            nextChar = readChar();
            return t;
        }

        // Identifiers, keywords, and Boolean literals
        if (isLetter(nextChar)) {
            t.setType("Identifier");

            while (isLetter(nextChar) || isDigit(nextChar)) {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            }

            if (isKeyword(t.getValue())) {
                t.setType("Keyword");
            } else if (t.getValue().equals("true") ||
                       t.getValue().equals("false")) {
                t.setType("Literal");
            }

            if (isEndOfToken(nextChar)) {
                return t;
            }
        }

        // Integer literals
        if (isDigit(nextChar)) {
            t.setType("Literal");

            while (isDigit(nextChar)) {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            }

            if (isEndOfToken(nextChar)) {
                return t;
            }
        }

        // Unrecognized tokens
        t.setType("Other");

        if (isEof) {
            return t;
        }

        while (!isEndOfToken(nextChar)) {
            t.setValue(t.getValue() + nextChar);
            nextChar = readChar();
        }

        skipWhiteSpace();
        return t;
    }

    private char readChar() {
        if (isEof) {
            return (char) 0;
        }

        try {
            int i = input.read();

            if (i == -1) {
                isEof = true;
                return (char) 0;
            }

            return (char) i;
        } catch (IOException e) {
            isEof = true;
            return (char) 0;
        }
    }

    private boolean isKeyword(String s) {
        return s.equals("main")
                || s.equals("if")
                || s.equals("else")
                || s.equals("while")
                || s.equals("int")
                || s.equals("boolean")
                || s.equals("void");
    }

    private boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r'
                || c == '\n' || c == '\f';
    }

    private boolean isEndOfLine(char c) {
        return c == '\r' || c == '\n' || c == '\f';
    }

    private boolean isEndOfToken(char c) {
        return isWhiteSpace(c) || isOperator(c)
                || isSeparator(c) || isEof;
    }

    private void skipWhiteSpace() {
        while (!isEof && isWhiteSpace(nextChar)) {
            nextChar = readChar();
        }
    }

    private boolean isSeparator(char c) {
        return c == ';' || c == ',' || c == '('
                || c == ')' || c == '{' || c == '}';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*'
                || c == '/' || c == '<' || c == '>'
                || c == '=' || c == '!' || c == '&'
                || c == '|';
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public boolean isEndofFile() {
        return isEof;
    }
}
