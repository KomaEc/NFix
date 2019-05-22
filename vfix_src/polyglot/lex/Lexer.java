package polyglot.lex;

import java.io.IOException;

public interface Lexer {
   int YYEOF = -1;

   String file();

   Token nextToken() throws IOException;
}
