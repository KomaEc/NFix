package com.gzoltar.shaded.org.pitest.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
   private static final Logger LOGGER = Logger.getLogger("PIT");

   public static Logger getLogger() {
      return LOGGER;
   }

   private static void addOrSetHandler(Handler handler) {
      if (LOGGER.getHandlers().length == 0) {
         LOGGER.addHandler(handler);
      } else {
         LOGGER.getHandlers()[0] = handler;
      }

   }

   public static void setVerbose(boolean on) {
      if (on) {
         setLevel(Level.FINEST);
      } else {
         setLevel(Level.INFO);
      }

   }

   private static void setLevel(Level level) {
      LOGGER.setLevel(level);
      Handler[] arr$ = LOGGER.getHandlers();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Handler each = arr$[i$];
         each.setLevel(level);
      }

   }

   public static boolean isVerbose() {
      return Level.FINEST.equals(LOGGER.getLevel());
   }

   static {
      if (System.getProperty("java.util.logging.config.file") == null && System.getProperty("java.util.logging.config.class") == null) {
         LOGGER.setUseParentHandlers(false);
         Handler handler = new ConsoleHandler();
         handler.setFormatter(new Log.PlainFormatter());
         addOrSetHandler(handler);
         LOGGER.setLevel(Level.INFO);
         handler.setLevel(Level.ALL);
      }

   }

   static class PlainFormatter extends Formatter {
      private static final String LINE_SEPARATOR = System.getProperty("line.separator");
      private final DateFormat dateFormat = DateFormat.getTimeInstance();

      public String format(LogRecord record) {
         StringBuilder buf = new StringBuilder(180);
         buf.append(this.dateFormat.format(new Date(record.getMillis())));
         buf.append(" PIT >> ");
         buf.append(record.getLevel());
         buf.append(" : ");
         buf.append(this.formatMessage(record));
         buf.append(LINE_SEPARATOR);
         Throwable throwable = record.getThrown();
         if (throwable != null) {
            StringWriter sink = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sink, true));
            buf.append(sink.toString());
         }

         return buf.toString();
      }
   }
}
