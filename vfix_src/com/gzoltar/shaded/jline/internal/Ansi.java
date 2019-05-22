package com.gzoltar.shaded.jline.internal;

import com.gzoltar.shaded.org.fusesource.jansi.AnsiOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Ansi {
   public static String stripAnsi(String str) {
      if (str == null) {
         return "";
      } else {
         try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            AnsiOutputStream aos = new AnsiOutputStream(baos);
            aos.write(str.getBytes());
            aos.close();
            return baos.toString();
         } catch (IOException var3) {
            return str;
         }
      }
   }
}
