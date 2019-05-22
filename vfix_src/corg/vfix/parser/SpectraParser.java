package corg.vfix.parser;

import corg.vfix.fl.spectrum.FLClass;
import corg.vfix.fl.spectrum.FLSpectrum;
import corg.vfix.fl.spectrum.FLStmt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SpectraParser {
   private static String file_spectra;

   public static void init(String fs) {
      file_spectra = fs;
      FLSpectrum.clearAll();
   }

   public static String getSpectraFile() {
      return file_spectra;
   }

   public static void parse() {
      File file = new File(file_spectra);

      try {
         BufferedReader br = new BufferedReader(new FileReader(file));
         String s = "";
         s = br.readLine();

         while((s = br.readLine()) != null) {
            if (s.contains(".java<")) {
               int classNameStartIndex = s.indexOf(".java<") + 6;
               int classNameEndIndex = s.indexOf("{");
               String className = s.substring(classNameStartIndex, classNameEndIndex);
               int lineNumberStartIndex = s.indexOf("#") + 1;
               int lineNumberEndIndex = s.indexOf(",");
               int lineNumber = Integer.parseInt(s.substring(lineNumberStartIndex, lineNumberEndIndex));
               Double possibility = new Double(Double.parseDouble(s.substring(lineNumberEndIndex + 1)));
               if (possibility > 0.0D) {
                  FLStmt flStmt = new FLStmt(lineNumber, possibility);
                  FLClass flCls = FLSpectrum.getClassByName(className);
                  flCls.addStmt(flStmt);
               }
            }
         }

         br.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }
}
