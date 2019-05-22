package ppg.lex;

import java.io.IOException;
import java.io.OutputStream;

interface LexerResult {
   void unparse(OutputStream var1) throws IOException;

   int lineNumber();
}
