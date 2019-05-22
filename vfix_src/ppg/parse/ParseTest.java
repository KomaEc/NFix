package ppg.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import ppg.lex.Lexer;
import ppg.spec.Spec;
import ppg.util.CodeWriter;

public class ParseTest {
   private static final String HEADER = "ppg [parsetest]: ";

   private ParseTest() {
   }

   public static void main(String[] args) {
      String filename = null;

      FileInputStream fileInput;
      try {
         filename = args[0];
         fileInput = new FileInputStream(filename);
      } catch (FileNotFoundException var12) {
         System.out.println("Error: " + filename + " is not found.");
         return;
      } catch (ArrayIndexOutOfBoundsException var13) {
         System.out.println("ppg [parsetest]: Error: No file name given.");
         return;
      }

      File f = new File(filename);
      String simpleName = f.getName();
      Lexer lex = new Lexer(fileInput, simpleName);
      Parser parser = new Parser(filename, lex);

      try {
         parser.parse();
      } catch (Exception var11) {
         System.out.println("ppg [parsetest]: Exception: " + var11.getMessage());
         return;
      }

      Spec spec = (Spec)Parser.getProgramNode();
      CodeWriter cw = new CodeWriter(System.out, 72);

      try {
         spec.unparse(cw);
         cw.flush();
         fileInput.close();
      } catch (IOException var10) {
         System.out.println("ppg [parsetest]: exception: " + var10.getMessage());
      }
   }
}
