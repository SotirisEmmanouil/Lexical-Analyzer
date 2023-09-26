import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/*
  Lexical analyzer for a subset of the Pascal language
 */

public class LexicalAnalyzer {
  
    public static class Token {
       
    	private Type t;
        private String c; 

        public Token(Type t, String c) {		//creating a token using OOP
            this.t = t;
            this.c = c;
        }
        
        public String toString() {			//toString method so tokens can be printed
            return t.toString();
        }
    
        public String getLexeme() {			//get the lexeme attached to the token
        	return c;
        }
       
    }
    
    public static List<Token> lexicallyAnalyze(String input) {
        List<Token> result = new ArrayList<Token>();
        String[] lexemes = input.split("\\s+"); // Split input into tokens using whitespace as separator
        	
        for (String lexeme : lexemes) {			//traverse array
            String currentToken = "";				
            boolean insideString = false;

            for (int i = 0; i < lexeme.length(); i++) {		//traverse each character of the string
                char currentChar = lexeme.charAt(i);

                if (currentChar == '(' || currentChar == '*' || currentChar == '>' || currentChar == '<'
                    || currentChar == ';' || currentChar == ')' || currentChar == '\''  || currentChar == ':' ) {
                    if (!insideString) {//if its one of these 
                        // Add the current token to the result if it's not empty
                        if (!currentToken.isEmpty()) {
                            result.add(new Token(assign(currentToken), currentToken));
                            currentToken = "";
                        }

                        // Check for multi-character tokens
                        if (i < lexeme.length() - 1) {		//if the the next char is one of these
                            String nextTwoChars = lexeme.substring(i, i + 2);
                            if (nextTwoChars.equals("(*") || nextTwoChars.equals("*)")		
                                    || nextTwoChars.equals(">=")  || nextTwoChars.equals(":=")|| nextTwoChars.equals("<=")) {
                                // Add multi-character token and increment the index
                                result.add(new Token(assign(nextTwoChars), nextTwoChars));  //add the two chars to the list 
                                i++;
                                continue;
                            }
                        }

                        // Add the current character as a separate token
                        result.add(new Token(assign(String.valueOf(currentChar)), String.valueOf(currentChar)));
                    } else {
                        // Inside a string, just add the character to the current token
                        currentToken += currentChar;
                    }
                } else if (currentChar == '\'') {
                    insideString = !insideString;
                    currentToken += currentChar;
                } else {
                    // Add the character to the current token
                    currentToken += currentChar;
                }
            }

            // Add the remaining token if it's not empty
            if (!currentToken.isEmpty()) {
                result.add(new Token(assign(currentToken), currentToken));
            }
        }

        return result;
    }



    
    public static Type assign(String input) {
    	
         
        if(input.equalsIgnoreCase("WRITE")) {
    		return Type.WRITE;
    	}
    	else if(input.equals("=")) {
    		return Type.EQL;
    	}
    	else if(input.equals(":=")) {
    		return Type.ASSIGN_OP;
    	}
    	else if(input.equals(";")) {
    		return Type.SEMICOLON;
    	}
    	else if(input.equals("'")) {
    		return Type.SINQUO;
    	}
    	else if(input.equalsIgnoreCase("READ")) {
    		return Type.READ;
    	}
    	else if(input.equalsIgnoreCase("VAR")) {
    		return Type.VAR;
    	}
      else if(input.equalsIgnoreCase("FOR")) {
    		return Type.FOR;
    	}
    	else if(input.equalsIgnoreCase("CONST")) {
    		return Type.CONST;
    	}
    	else if(input.equalsIgnoreCase("PROGRAM")) {
    		return Type.PROGRAM;
    	}
    	else if(input.equalsIgnoreCase("INTEGER")) {
    		return Type.INTEGER;
    	}
    	else if(input.equalsIgnoreCase("DIV")) {
    		return Type.DIV;
    	}
    	else if(input.equalsIgnoreCase("MOD")) {
    		return Type.MOD;
    	}
    	else if(input.equalsIgnoreCase("IF")) {
    		return Type.IFSYM;
    	}
    	else if(input.equalsIgnoreCase("WHILE")) {
    		return Type.WHILE;
    	}
      else if(input.equalsIgnoreCase("TO")) {
    		return Type.TO;
    	}
    	else if(input.equalsIgnoreCase("DO")) {
    		return Type.DO;
    	}
    	else if(input.equals(")")) {
    		return Type.RPAREN;
    	}
    	else if(input.equals("(")) {
    		return Type.LPAREN;
    	}
    	else if(input.equals("+")) {
    		return Type.PLUS;
    	}
    	else if(input.equals(",")) {
    		return Type.COMMA;
    	}
    	else if(input.equals(">=")) {
    		return Type.GEQ;
    	}
    	else if(input.equals("<=")) {
    		return Type.LEQ;
    	}
    	else if(input.equals(">")) {
    		return Type.GTR;
    	}
    	else if(input.equals("<")) {
    		return Type.LSS;
    	}
    	else if(input.equals(":")) {
    		return Type.COLON;
    	}
    	else if(input.equals("-")) {
    		return Type.MINUS;
    	}
    	else if(input.equals("*")) {
    		return Type.TIMES;
    	}
    	else if(input.equals("(*")) {
    		return Type.BEGCOMMENT;
    	}
    	else if(input.equalsIgnoreCase("ELSE")) {
    		return Type.ELSESYM;
    	}
    	else if(input.equalsIgnoreCase("TRUE")) {
    		return Type.TRUESYM;
    	}
    	else if(input.equalsIgnoreCase("FALSE")) {
    		return Type.FALSESYM;
    	}
    	else if(input.equalsIgnoreCase("END.")) {
    		return Type.END;
    	}
    	else if(input.equalsIgnoreCase("THEN")) {
    		return Type.THENSYM;
    	}
    	else if(input.equalsIgnoreCase("BEGIN")) {
    		return Type.BEGIN;
    	}
    	else if(input.equals("*)")) {
    		return Type.ENDCOMMENT;
    	}
    	else if(input.matches("^[a-zA-Z_].*$")) {
    		return Type.IDENT;
    	}
    	else if(input.matches("-?\\d+")) {
    		return Type.NUMLIT;
    	}
    	else {
    	return Type.UNKNOWN;
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
       
    	List<Token> tokens = null;

        try {
            Scanner s = new Scanner(new File("/Users/sotirisemmanouil/git/repository5/316 project 1/src/test1.pas"));
            StringBuilder inputBuilder = new StringBuilder();

            while (s.hasNext()) {
                inputBuilder.append(s.next()).append(" ");
            }

            String input = inputBuilder.toString().trim();
            tokens = lexicallyAnalyze(input);
            
        } 
        catch (IOException e) {
            System.out.println("Error accessing input file!");
        }

       for (Token t : tokens) {
    	   String tokenString = String.format("%-20s", t);
            System.out.print("TOKEN: " + tokenString+"        LEXEME: " + t.getLexeme()+"\n");       
        }
    }

    }
