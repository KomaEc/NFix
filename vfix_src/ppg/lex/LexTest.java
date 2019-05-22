package ppg.lex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LexTest {
   private static final String HEADER = "ppg [lexertest]: ";

   public static void main(String[] args) {
      String filename = null;

      FileInputStream fileInput;
      try {
         filename = args[0];
         fileInput = new FileInputStream(filename);
      } catch (FileNotFoundException var8) {
         System.out.println("Error: " + filename + " is not found.");
         return;
      } catch (ArrayIndexOutOfBoundsException var9) {
         System.out.println("ppg [lexertest]: Error: No file name given.");
         return;
      }

      File f = new File(filename);
      String simpleName = f.getName();
      Lexer lex = new Lexer(fileInput, simpleName);
      Token t = null;

      try {
         while(true) {
            t = lex.getToken();
            t.unparse(System.out);
            if (t.getCode() == 0) {
               fileInput.close();
               break;
            }

            System.out.println();
         }
      } catch (Error var10) {
         System.out.println(var10.getMessage());
         System.exit(1);
      } catch (Exception var11) {
         System.out.println(var11.getMessage());
         System.exit(1);
      }

   }
}
