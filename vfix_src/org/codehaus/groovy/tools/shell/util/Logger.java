package org.codehaus.groovy.tools.shell.util;

import com.gzoltar.shaded.org.fusesource.jansi.Ansi;
import org.codehaus.groovy.tools.shell.IO;

public final class Logger {
   public static IO io;
   public final String name;
   private static final String DEBUG = "DEBUG";
   private static final String WARN = "WARN";
   private static final String ERROR = "ERROR";

   private Logger(String name) {
      assert name != null;

      this.name = name;
   }

   private void log(String level, Object msg, Throwable cause) throws Exception {
      assert level != null;

      assert msg != null;

      if (io == null) {
         io = new IO();
      }

      if (cause == null && msg instanceof Throwable) {
         cause = (Throwable)msg;
         msg = cause.getMessage();
      }

      Ansi.Color color = Ansi.Color.GREEN;
      if ("WARN".equals(level) || "ERROR".equals(level)) {
         color = Ansi.Color.RED;
      }

      io.out.println(Ansi.ansi().a(Ansi.Attribute.INTENSITY_BOLD).a((Object)color).a(level).reset().a(" [").a(this.name).a("] ").a(msg));
      if (cause != null) {
         cause.printStackTrace(io.out);
      }

      io.flush();
   }

   public boolean isDebugEnabled() {
      return Preferences.verbosity == IO.Verbosity.DEBUG;
   }

   public boolean isDebug() {
      return this.isDebugEnabled();
   }

   public void debug(Object msg) throws Exception {
      if (this.isDebugEnabled()) {
         this.log("DEBUG", msg, (Throwable)null);
      }

   }

   public void debug(Object msg, Throwable cause) throws Exception {
      if (this.isDebugEnabled()) {
         this.log("DEBUG", msg, cause);
      }

   }

   public void warn(Object msg) throws Exception {
      this.log("WARN", msg, (Throwable)null);
   }

   public void warn(Object msg, Throwable cause) throws Exception {
      this.log("WARN", msg, cause);
   }

   public void error(Object msg) throws Exception {
      this.log("ERROR", msg, (Throwable)null);
   }

   public void error(Object msg, Throwable cause) throws Exception {
      this.log("ERROR", msg, cause);
   }

   public static Logger create(Class type) {
      return new Logger(type.getName());
   }

   public static Logger create(Class type, String suffix) {
      return new Logger(type.getName() + "." + suffix);
   }
}
