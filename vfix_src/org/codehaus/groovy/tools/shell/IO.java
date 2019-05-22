package org.codehaus.groovy.tools.shell;

import com.gzoltar.shaded.org.fusesource.jansi.AnsiRenderWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import org.codehaus.groovy.tools.shell.util.Preferences;

public class IO {
   public final InputStream inputStream;
   public final OutputStream outputStream;
   public final OutputStream errorStream;
   public final Reader in;
   public final PrintWriter out;
   public final PrintWriter err;

   public IO(InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
      assert inputStream != null;

      assert outputStream != null;

      assert errorStream != null;

      this.inputStream = inputStream;
      this.outputStream = outputStream;
      this.errorStream = errorStream;
      this.in = new InputStreamReader(inputStream);
      this.out = new AnsiRenderWriter(outputStream, true);
      this.err = new AnsiRenderWriter(errorStream, true);
   }

   public IO() {
      this(System.in, System.out, System.err);
   }

   public void setVerbosity(IO.Verbosity verbosity) {
      assert verbosity != null;

      Preferences.verbosity = verbosity;
   }

   public IO.Verbosity getVerbosity() {
      return Preferences.verbosity;
   }

   public boolean isQuiet() {
      return this.getVerbosity() == IO.Verbosity.QUIET;
   }

   public boolean isInfo() {
      return this.getVerbosity() == IO.Verbosity.INFO;
   }

   public boolean isVerbose() {
      return this.getVerbosity() == IO.Verbosity.VERBOSE;
   }

   public boolean isDebug() {
      return this.getVerbosity() == IO.Verbosity.DEBUG;
   }

   public void flush() throws IOException {
      this.out.flush();
      this.err.flush();
   }

   public void close() throws IOException {
      this.in.close();
      this.out.close();
      this.err.close();
   }

   public static final class Verbosity {
      public static final IO.Verbosity QUIET = new IO.Verbosity("QUIET");
      public static final IO.Verbosity INFO = new IO.Verbosity("INFO");
      public static final IO.Verbosity VERBOSE = new IO.Verbosity("VERBOSE");
      public static final IO.Verbosity DEBUG = new IO.Verbosity("DEBUG");
      public final String name;

      private Verbosity(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      public static IO.Verbosity forName(String name) {
         assert name != null;

         if (QUIET.name.equalsIgnoreCase(name)) {
            return QUIET;
         } else if (INFO.name.equalsIgnoreCase(name)) {
            return INFO;
         } else if (VERBOSE.name.equalsIgnoreCase(name)) {
            return VERBOSE;
         } else if (DEBUG.name.equalsIgnoreCase(name)) {
            return DEBUG;
         } else {
            throw new IllegalArgumentException("Invalid verbosity name: " + name);
         }
      }
   }
}
