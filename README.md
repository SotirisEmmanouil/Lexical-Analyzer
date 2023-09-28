# Lexical-Analyzer
## Performs a Lexical Analysis on a Pascal source code file

This project lexically analyzes a Pascal source code file by taking Pascal lexemes and assigning them a corresponding token. For example, 
if the `(*` lexeme appears, the token code `BEGCOMMENT` will be assigned to it or if the lexeme `THEN` appears, the token code `THENSYM` will be 
assigned to it. The `main` method prints out a side by side comparison of each lexeme and its corresponding token.
The token types are held in an enumerated type. An ArrayList holds the lexemes that have been assigned a token. I also utilized regular 
expressions so as to match certain string characteristics such as if a string starts with a letter of the alphabet or an underscore, or if the string is an integer.
