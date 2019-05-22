package corg.vfix.parser;

import corg.vfix.fl.stack.StackFrame;
import corg.vfix.fl.stack.StackTrace;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TraceParser {
   private static String filename = "";

   public static void init(String f) {
      filename = f;
      StackTrace.clearAll();
   }

   public static void parse() throws IOException {
      File file = new File(filename);
      if (!file.exists()) {
         throw new FileNotFoundException();
      } else {
         String delims = "[(): ]+";
         BufferedReader br = new BufferedReader(new FileReader(file));
         String s = null;
         s = br.readLine();

         while((s = br.readLine()) != null) {
            String[] tokens = s.split(delims);
            if (tokens.length < 4) {
               System.out.println("Standard trace log file not found");
               br.close();
               throw new FileNotFoundException();
            }

            String num = tokens[3];
            if (isNum(num)) {
               StackFrame frame = new StackFrame(tokens[1], Integer.valueOf(tokens[3]));
               StackTrace.insert(frame);
            }
         }

         br.close();
         StackTrace.backwardPrint();
      }
   }

   public static boolean isNum(String str) {
      for(int i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         if (c < '0' || c > '9') {
            return false;
         }
      }

      return true;
   }
}
