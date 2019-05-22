package org.apache.tools.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SmtpResponseReader {
   protected BufferedReader reader = null;
   private StringBuffer result = new StringBuffer();

   public SmtpResponseReader(InputStream in) {
      this.reader = new BufferedReader(new InputStreamReader(in));
   }

   public String getResponse() throws IOException {
      this.result.setLength(0);
      String line = this.reader.readLine();
      if (line != null && line.length() >= 3) {
         this.result.append(line.substring(0, 3));
         this.result.append(" ");
      }

      while(line != null) {
         this.append(line);
         if (!this.hasMoreLines(line)) {
            break;
         }

         line = this.reader.readLine();
      }

      return this.result.toString().trim();
   }

   public void close() throws IOException {
      this.reader.close();
   }

   protected boolean hasMoreLines(String line) {
      return line.length() > 3 && line.charAt(3) == '-';
   }

   private void append(String line) {
      if (line.length() > 4) {
         this.result.append(line.substring(4));
         this.result.append(" ");
      }

   }
}
