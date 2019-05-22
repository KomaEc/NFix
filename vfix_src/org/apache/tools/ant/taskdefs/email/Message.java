package org.apache.tools.ant.taskdefs.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.tools.ant.ProjectComponent;

public class Message extends ProjectComponent {
   private File messageSource = null;
   private StringBuffer buffer = new StringBuffer();
   private String mimeType = "text/plain";
   private boolean specified = false;
   private String charset = null;

   public Message() {
   }

   public Message(String text) {
      this.addText(text);
   }

   public Message(File file) {
      this.messageSource = file;
   }

   public void addText(String text) {
      this.buffer.append(text);
   }

   public void setSrc(File src) {
      this.messageSource = src;
   }

   public void setMimeType(String mimeType) {
      this.mimeType = mimeType;
      this.specified = true;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public void print(PrintStream ps) throws IOException {
      PrintWriter out = this.charset != null ? new PrintWriter(new OutputStreamWriter(ps, this.charset)) : new PrintWriter(ps);
      if (this.messageSource != null) {
         FileReader freader = new FileReader(this.messageSource);

         try {
            BufferedReader in = new BufferedReader(freader);
            String line = null;

            while((line = in.readLine()) != null) {
               out.println(this.getProject().replaceProperties(line));
            }
         } finally {
            freader.close();
         }
      } else {
         out.println(this.getProject().replaceProperties(this.buffer.substring(0)));
      }

      out.flush();
   }

   public boolean isMimeTypeSpecified() {
      return this.specified;
   }

   public void setCharset(String charset) {
      this.charset = charset;
   }

   public String getCharset() {
      return this.charset;
   }
}
