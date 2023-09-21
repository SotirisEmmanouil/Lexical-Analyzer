import java.util.List;
import java.util.Scanner;
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
        
        for (String lexeme : lexemes) {
           
         
               if(lexeme.startsWith("(")){
            	   char secondLetter = lexeme.charAt(1);
            	  
            	   if(Character.isLetter(secondLetter) && lexeme.endsWith(";") && lexeme.contains(")")) {		
            		 int index = lexeme.indexOf(')'), index2 = lexeme.indexOf(';');
            		 result.add(new Token(Type.LPAREN, lexeme.substring(0,1)));
            		 result.add(new Token(Type.IDENT, lexeme.substring(1,index)));
            		 result.add(new Token(Type.RPAREN, lexeme.substring(index,index2)));
            		 result.add(new Token(Type.SEMICOLON, lexeme.substring(index2)));
            	   }
            	   else if (Character.isLetter(secondLetter)) {
              		 result.add(new Token(Type.LPAREN, lexeme.substring(0,1)));
              		 result.add(new Token(Type.IDENT, lexeme.substring(1)));
              	   }
            	   else if(lexeme.charAt(1) == '*') {
                    result.add(new Token(Type.BEGCOMMENT, lexeme));
            	   }
            	   
            	   else if(lexeme.charAt(1) == '“') {
            		   result.add(new Token(Type.LPAREN, lexeme.substring(0,1)));
                       result.add(new Token(Type.QUOTE, lexeme.substring(1,2)));
                       
                       if(lexeme.substring(1).matches("^[A-I].*$")){
                    	 result.add(new Token(Type.QUOTE, lexeme));
                    	 
                    	 int index = lexeme.indexOf('“'), index2 = lexeme.indexOf(')');
                    	 
                    	 result.add(new Token(Type.IDENT, lexeme.substring(1,index)));
                    	 result.add(new Token(Type.QUOTE, lexeme.substring(index,index2)));
                    	 result.add(new Token(Type.RPAREN, lexeme.substring(index2)));
                    	 
                       }
               	   }
                	else 
                         result.add(new Token(Type.LPAREN, lexeme));
                }
               else if(lexeme.endsWith("');") && lexeme.matches("^[A-I].*$")){
        	    	int i = lexeme.indexOf(")"), j = lexeme.indexOf(";"), k = lexeme.indexOf("'");
       	    	  result.add(new Token(Type.IDENT, lexeme.substring(0,k)));
       	    	  result.add(new Token(Type.SINQUO, lexeme.substring(k,i)));
       	    	  result.add(new Token(Type.RPAREN, lexeme.substring(i,j)));
       	    	  result.add(new Token(Type.SEMICOLON, lexeme.substring(j)));
              }
               
               else if(lexeme.endsWith(";")) {
            	  
                 if(lexeme.substring(0,lexeme.length()-1).matches("-?\\d+")) {		//using regex check if its a number before the semicolon
            		   result.add(new Token(Type.NUMLIT, lexeme.substring(0,lexeme.length()-1)));
            		   result.add(new Token(Type.SEMICOLON, lexeme.substring(lexeme.length()-1)));
                 }
                 else if(lexeme.contains("INTEGER") || lexeme.contains("integer")) {
        			   result.add(new Token(Type.INTEGER, lexeme.substring(0,lexeme.length()-1)));
           		       result.add(new Token(Type.SEMICOLON, lexeme.substring(lexeme.length()-1)));
        		  }
                 
                 else if(lexeme.substring(0,lexeme.length()-1).matches("^[A-I].*$")) {		//using regex check if the string 
          		       result.add(new Token(Type.IDENT, lexeme.substring(0,lexeme.length()-1)));
          		       result.add(new Token(Type.SEMICOLON, lexeme.substring(lexeme.length()-1)));
                 }
                                 
          		  else if(lexeme.startsWith("'")) {
          			 result.add(new Token(Type.SINQUO, lexeme.substring(0,1)));
          			 result.add(new Token(Type.RPAREN, lexeme.substring(1,2)));
          			 result.add(new Token(Type.SEMICOLON, lexeme.substring(2,3)));
                 }
                 
          		  else if(lexeme.startsWith("Read(")) {
                 	   result.add(new Token(Type.READ, lexeme.substring(0,4)));
                 	   result.add(new Token(Type.LPAREN, lexeme.substring(4,5)));
                 	 
                 	   if(lexeme.substring(5).matches("^[A-I].*$") && lexeme.substring(5).endsWith(");")) {			//if it contains an IDENT
                 		                   	
                 	     int index = lexeme.indexOf(')'), index2 = lexeme.indexOf(";");
                 		 result.add(new Token(Type.IDENT, lexeme.substring(5,index)));
                 		 result.add(new Token(Type.LPAREN, lexeme.substring(index,index2)));
                 		 result.add(new Token(Type.SEMICOLON, lexeme.substring(index2)));
                 		   
                 	   }
                 	     else {
                 			   result.add(new Token(Type.UNKNOWN, lexeme.substring(5)));
                 			   
                 		   }
                 	  }  
          		
               }
              else if(lexeme.endsWith(")")) {
            	  int index = lexeme.indexOf(')');
            	  
            	  if(lexeme.startsWith("*")) {
            		  result.add(new Token(Type.ENDCOMMENT, lexeme));
            	  }
            	  else if(lexeme.substring(0,lexeme.length()-1).matches("-?\\d+")) {
            		  result.add(new Token(Type.NUMLIT, lexeme.substring(0,lexeme.length()-1)));
             		   result.add(new Token(Type.RPAREN, lexeme.substring(lexeme.length()-1)));
            	  }
            	  else if (lexeme.length() >= 2 && Character.isLetter(lexeme.charAt(lexeme.length() - 2))) {
            		    result.add(new Token(Type.IDENT, lexeme.substring(0, lexeme.length() - 1)));
            		    result.add(new Token(Type.RPAREN, lexeme.substring(lexeme.length() - 1)));
            		}
            	  else 
            		  result.add(new Token(Type.RPAREN,lexeme));           	              	    
                  } 
               
               else if(lexeme.endsWith(":") && lexeme.matches("^[A-I].*$")) {
              		   result.add(new Token(Type.IDENT, lexeme.substring(0,lexeme.length()-1)));
              		   result.add(new Token(Type.COLON, lexeme.substring(lexeme.length()-1)));
              	
                 }
               
               else if(lexeme.startsWith("Write('")) {
            	   result.add(new Token(Type.WRITE, lexeme.substring(0,5)));
            	   result.add(new Token(Type.LPAREN, lexeme.substring(5,6)));
            	   result.add(new Token(Type.SINQUO, lexeme.substring(6,7)));
                   
            	   if(lexeme.substring(7).length() > 0) {
            	  
            		 if(lexeme.substring(7).matches("^[A-I].*$")) {
            			 
            		    if(lexeme.substring(7).endsWith("');")){
                	    	int i = lexeme.indexOf(")"), j = lexeme.indexOf(";"), k = lexeme.indexOf("'");
                	    	  result.add(new Token(Type.IDENT, lexeme.substring(7,k)));
                	    	  result.add(new Token(Type.SINQUO, lexeme.substring(k,i)));
                	    	  result.add(new Token(Type.RPAREN, lexeme.substring(i,j)));
                	    	  result.add(new Token(Type.SEMICOLON, lexeme.substring(j)));
            			  }
            		    else
            	          result.add(new Token(Type.IDENT, lexeme.substring(7)));
            		   }
            	     
                    else if(lexeme.substring(7).matches("-?\\d+")) {
                    	  
                    	if(lexeme.substring(7).endsWith("');")){
                  	    	int i = lexeme.indexOf(")"), j = lexeme.indexOf(";"), k = lexeme.indexOf("'");
                  	    	  result.add(new Token(Type.IDENT, lexeme.substring(7,k)));
                  	    	  result.add(new Token(Type.SINQUO, lexeme.substring(k,i)));
                  	    	  result.add(new Token(Type.RPAREN, lexeme.substring(i,j)));
                  	    	  result.add(new Token(Type.SEMICOLON, lexeme.substring(j)));
              			  }
                    	
            	       result.add(new Token(Type.IDENT, lexeme.substring(7)));
                    }
                    else {
                       result.add(new Token(Type.UNKNOWN, lexeme.substring(7)));
                     }
                  
                     }
            	   else {
            		   //do nothing
            	   }
                }             
               else if(lexeme.equalsIgnoreCase("DIV")) {
            	   result.add(new Token(Type.DIV, lexeme)); 
               }
               else if(lexeme.equalsIgnoreCase("THEN")) {
            	   result.add(new Token(Type.THENSYM, lexeme)); 
               }
               else if(lexeme.equals("*")) {
                    result.add(new Token(Type.TIMES, lexeme));
               }     
               else if(lexeme.equals("+")) {
            	   result.add(new Token(Type.PLUS, lexeme)); 
               } 
                else if(lexeme.equalsIgnoreCase("END.")) {
                    result.add(new Token(Type.END, lexeme));
                }
                else if(lexeme.equalsIgnoreCase("MOD")) {
                    result.add(new Token(Type.MOD, lexeme));
                }
                else if(lexeme.equals("=")) {
                    result.add(new Token(Type.EQL, lexeme));
                }
                else if(lexeme.equalsIgnoreCase("READ")) {
                    result.add(new Token(Type.READ, lexeme));
                }
                else if(lexeme.equalsIgnoreCase("VAR")) {
                    result.add(new Token(Type.VAR, lexeme));
                }
                else if(lexeme.equalsIgnoreCase("CONST")) {
                    result.add(new Token(Type.CONST, lexeme));
                } 
                else if(lexeme.equalsIgnoreCase("-")) {
                    result.add(new Token(Type.MINUS, lexeme));
                }
                else if (lexeme.equalsIgnoreCase("PROGRAM")) {
                    result.add(new Token(Type.PROGRAM, lexeme));
                }
                else if (lexeme.equalsIgnoreCase("IF")){
                    result.add(new Token(Type.IFSYM, lexeme));
                }
                else if(lexeme.equalsIgnoreCase("ELSE")) {
                    result.add(new Token(Type.ELSESYM, lexeme));
                 }
                else if(lexeme.equalsIgnoreCase("INTEGER")) {
                    result.add(new Token(Type.INTEGER, lexeme));
                 }
                else if(lexeme.equalsIgnoreCase("BEGIN")) {
                    result.add(new Token(Type.BEGIN, lexeme));
                 }
                else if(lexeme.equals(":=")) {
                    result.add(new Token(Type.ASSIGN_OP, lexeme));          
                 }
                else if(lexeme.equals(":")) {
                    result.add(new Token(Type.COLON, lexeme));
                 }
                else if(lexeme.matches("-?\\d+")) {
                    result.add(new Token(Type.NUMLIT, lexeme));		//check if its a digit using regular expression
                 }
                else if(lexeme.matches("^[a-zA-Z_].*$")) {
                    result.add(new Token(Type.IDENT, lexeme));		
                  //check if its starts with a letter of the alphabet or underscore using regular expression
                 }
                else {
                	 result.add(new Token(Type.UNKNOWN, lexeme));
            }
        }

               return result;
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
          System.out.print("TOKEN: " + t+"        LEXEME: " + t.getLexeme()+"\n");       
        }
    }

    }
