package corg.vfix.fl;

import corg.vfix.parser.SpectraParser;
import corg.vfix.parser.TraceParser;
import java.io.IOException;

public class FLMain {
   public static void main() throws IOException {
      System.out.println("NPE trace parsing...\n");
      TraceParser.parse();
      System.out.println("Gzoltar running...\n");
      GzoltarRunner.main();
      System.out.println("spectra parsing...");
      SpectraParser.parse();
   }
}
