package ppg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import ppg.lex.Lexer;
import ppg.parse.Parser;
import ppg.spec.CUPSpec;
import ppg.spec.Spec;
import ppg.util.CodeWriter;

public class PPG {
   public static final String HEADER = "ppg: ";
   public static final String DEBUG_HEADER = "ppg [debug]: ";
   public static boolean debug = false;
   public static String SYMBOL_CLASS_NAME = "sym";
   public static String OUTPUT_FILE = null;

   public static void DEBUG(String s) {
      if (debug) {
         System.out.println("ppg [debug]: " + s);
      }

   }

   public static void main(String[] args) {
      String filename = null;

      try {
         for(int i = 0; i < args.length; ++i) {
            if (args[i].charAt(0) == '-') {
               if (args[i].equals("-symbols")) {
                  if (args.length <= i) {
                     throw new Exception("No filename specified after -symbols");
                  }

                  ++i;
                  SYMBOL_CLASS_NAME = args[i];
               } else {
                  if (!args[i].equals("-o")) {
                     throw new Exception("Invalid switch: " + args[i]);
                  }

                  if (args.length <= i) {
                     throw new Exception("No filename specified after -o");
                  }

                  ++i;
                  OUTPUT_FILE = args[i];
               }
            } else {
               if (filename != null) {
                  throw new Exception("Error: multiple source files specified.");
               }

               filename = args[i];
            }
         }
      } catch (Exception var16) {
         System.err.println("ppg: " + var16.getMessage());
         usage();
      }

      if (filename == null) {
         System.err.println("Error: no filename specified.");
         usage();
      }

      FileInputStream fileInput;
      try {
         fileInput = new FileInputStream(filename);
      } catch (FileNotFoundException var14) {
         System.out.println("Error: " + filename + " is not found.");
         return;
      } catch (ArrayIndexOutOfBoundsException var15) {
         System.out.println("ppg: Error: No file name given.");
         return;
      }

      Lexer lex = new Lexer(fileInput, filename);
      Parser parser = new Parser(filename, lex);

      try {
         parser.parse();
      } catch (Exception var13) {
         System.out.println("ppg: Exception: " + var13.getMessage());
         return;
      }

      Spec spec = (Spec)Parser.getProgramNode();
      File file = new File(filename);
      String parent = file.getParent();
      spec.parseChain(parent == null ? "" : parent);
      PrintStream out = System.out;

      try {
         if (OUTPUT_FILE != null) {
            out = new PrintStream(new FileOutputStream(OUTPUT_FILE));
         }

         CUPSpec combined = spec.coalesce();
         CodeWriter cw = new CodeWriter(out, 72);
         combined.unparse(cw);
         cw.flush();
      } catch (PPGError var11) {
         System.out.println(var11.getMessage());
         System.exit(1);
      } catch (IOException var12) {
         System.out.println("ppg: exception: " + var12.getMessage());
         System.exit(1);
      }

   }

   public static void usage() {
      System.err.println("Usage: ppg [-symbols ConstClass] <input file>\nwhere:\n\t-c <Class>\tclass prepended to token names to pass to <func>\n\t<input>\ta PPG or CUP source file\n");
      System.exit(1);
   }
}
