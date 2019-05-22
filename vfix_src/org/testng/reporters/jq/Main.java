package org.testng.reporters.jq;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.internal.Utils;
import org.testng.reporters.Files;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.XmlSuite;

public class Main implements IReporter {
   private static final String[] RESOURCES = new String[]{"jquery-1.7.1.min.js", "testng-reports.css", "testng-reports.js", "passed.png", "failed.png", "skipped.png", "navigator-bullet.png", "bullet_point.png", "collapseall.gif"};
   private Model m_model;
   private String m_outputDirectory;

   public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
      this.m_model = new Model(suites);
      this.m_outputDirectory = outputDirectory;
      XMLStringBuffer xsb = new XMLStringBuffer("    ");
      (new BannerPanel(this.m_model)).generate(xsb);
      List<INavigatorPanel> panels = Arrays.asList(new TestNgXmlPanel(this.m_model), new TestPanel(this.m_model), new GroupPanel(this.m_model), new TimesPanel(this.m_model), new ReporterPanel(this.m_model), new IgnoredMethodsPanel(this.m_model), new ChronologicalPanel(this.m_model));
      (new NavigatorPanel(this.m_model, panels)).generate(xsb);
      xsb.push("div", "class", "wrapper");
      xsb.push("div", "class", "main-panel-root");
      (new SuitePanel(this.m_model)).generate(xsb);
      Iterator i$ = panels.iterator();

      while(i$.hasNext()) {
         INavigatorPanel panel = (INavigatorPanel)i$.next();
         panel.generate(xsb);
      }

      xsb.pop("div");
      xsb.pop("div");
      xsb.addString("  </body>\n");
      xsb.addString("</html>\n");

      try {
         InputStream header = this.getClass().getResourceAsStream("/header");
         Throwable var8 = null;

         try {
            if (header == null) {
               throw new RuntimeException("Couldn't find resource header");
            }

            String[] arr$ = RESOURCES;
            int len$ = arr$.length;
            int i$ = 0;

            while(true) {
               if (i$ >= len$) {
                  String all = Files.readFile(header);
                  Utils.writeUtf8File(this.m_outputDirectory, "index.html", xsb, all);
                  break;
               }

               String fileName = arr$[i$];
               InputStream is = this.getClass().getResourceAsStream("/" + fileName);
               Throwable var14 = null;

               try {
                  if (is == null) {
                     throw new AssertionError("Couldn't find resource: " + fileName);
                  }

                  Files.copyFile(is, new File(this.m_outputDirectory, fileName));
               } catch (Throwable var39) {
                  var14 = var39;
                  throw var39;
               } finally {
                  if (is != null) {
                     if (var14 != null) {
                        try {
                           is.close();
                        } catch (Throwable var38) {
                           var14.addSuppressed(var38);
                        }
                     } else {
                        is.close();
                     }
                  }

               }

               ++i$;
            }
         } catch (Throwable var41) {
            var8 = var41;
            throw var41;
         } finally {
            if (header != null) {
               if (var8 != null) {
                  try {
                     header.close();
                  } catch (Throwable var37) {
                     var8.addSuppressed(var37);
                  }
               } else {
                  header.close();
               }
            }

         }
      } catch (IOException var43) {
         var43.printStackTrace();
      }

   }
}
