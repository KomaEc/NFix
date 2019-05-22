package org.testng.reporters;

import java.io.File;
import java.io.IOException;
import org.testng.internal.Utils;

public class HtmlHelper {
   private static final String CSS_FILE_NAME = "testng.css";
   private static final String MY_CSS_FILE_NAME = "my-testng.css";

   public static String getCssString() {
      return getCssString("..");
   }

   public static String getCssString(String directory) {
      return "<link href=\"" + directory + "/" + "testng.css" + "\" rel=\"stylesheet\" type=\"text/css\" />\n" + "<link href=\"" + directory + "/" + "my-testng.css" + "\" rel=\"stylesheet\" type=\"text/css\" />\n";
   }

   public static File generateStylesheet(String outputDirectory) throws IOException {
      File stylesheetFile = new File(outputDirectory, "testng.css");
      if (!stylesheetFile.exists()) {
         Utils.writeResourceToFile(stylesheetFile, "testng.css", TestHTMLReporter.class);
      }

      return stylesheetFile;
   }
}
