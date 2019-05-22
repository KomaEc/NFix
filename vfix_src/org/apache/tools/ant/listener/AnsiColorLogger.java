package org.apache.tools.ant.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import org.apache.tools.ant.DefaultLogger;

public final class AnsiColorLogger extends DefaultLogger {
   private static final int ATTR_DIM = 2;
   private static final int FG_RED = 31;
   private static final int FG_GREEN = 32;
   private static final int FG_BLUE = 34;
   private static final int FG_MAGENTA = 35;
   private static final int FG_CYAN = 36;
   private static final String PREFIX = "\u001b[";
   private static final String SUFFIX = "m";
   private static final char SEPARATOR = ';';
   private static final String END_COLOR = "\u001b[m";
   private String errColor = "\u001b[2;31m";
   private String warnColor = "\u001b[2;35m";
   private String infoColor = "\u001b[2;36m";
   private String verboseColor = "\u001b[2;32m";
   private String debugColor = "\u001b[2;34m";
   private boolean colorsSet = false;

   private void setColors() {
      String userColorFile = System.getProperty("ant.logger.defaults");
      String systemColorFile = "/org/apache/tools/ant/listener/defaults.properties";
      Object in = null;

      try {
         Properties prop = new Properties();
         if (userColorFile != null) {
            in = new FileInputStream(userColorFile);
         } else {
            in = this.getClass().getResourceAsStream(systemColorFile);
         }

         if (in != null) {
            prop.load((InputStream)in);
         }

         String errC = prop.getProperty("AnsiColorLogger.ERROR_COLOR");
         String warn = prop.getProperty("AnsiColorLogger.WARNING_COLOR");
         String info = prop.getProperty("AnsiColorLogger.INFO_COLOR");
         String verbose = prop.getProperty("AnsiColorLogger.VERBOSE_COLOR");
         String debug = prop.getProperty("AnsiColorLogger.DEBUG_COLOR");
         if (errC != null) {
            this.errColor = "\u001b[" + errC + "m";
         }

         if (warn != null) {
            this.warnColor = "\u001b[" + warn + "m";
         }

         if (info != null) {
            this.infoColor = "\u001b[" + info + "m";
         }

         if (verbose != null) {
            this.verboseColor = "\u001b[" + verbose + "m";
         }

         if (debug != null) {
            this.debugColor = "\u001b[" + debug + "m";
         }
      } catch (IOException var18) {
      } finally {
         if (in != null) {
            try {
               ((InputStream)in).close();
            } catch (IOException var17) {
            }
         }

      }

   }

   protected void printMessage(String message, PrintStream stream, int priority) {
      if (message != null && stream != null) {
         if (!this.colorsSet) {
            this.setColors();
            this.colorsSet = true;
         }

         StringBuffer msg = new StringBuffer(message);
         switch(priority) {
         case 0:
            msg.insert(0, this.errColor);
            msg.append("\u001b[m");
            break;
         case 1:
            msg.insert(0, this.warnColor);
            msg.append("\u001b[m");
            break;
         case 2:
            msg.insert(0, this.infoColor);
            msg.append("\u001b[m");
            break;
         case 3:
            msg.insert(0, this.verboseColor);
            msg.append("\u001b[m");
            break;
         case 4:
         default:
            msg.insert(0, this.debugColor);
            msg.append("\u001b[m");
         }

         String strmessage = msg.toString();
         stream.println(strmessage);
      }

   }
}
