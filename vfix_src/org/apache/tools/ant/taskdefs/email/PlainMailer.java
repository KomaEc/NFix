package org.apache.tools.ant.taskdefs.email;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import org.apache.tools.ant.BuildException;
import org.apache.tools.mail.MailMessage;

class PlainMailer extends Mailer {
   public void send() {
      try {
         MailMessage mailMessage = new MailMessage(this.host, this.port);
         mailMessage.from(this.from.toString());
         Enumeration e = this.replyToList.elements();

         while(e.hasMoreElements()) {
            mailMessage.replyto(e.nextElement().toString());
         }

         e = this.toList.elements();

         while(e.hasMoreElements()) {
            mailMessage.to(e.nextElement().toString());
         }

         e = this.ccList.elements();

         while(e.hasMoreElements()) {
            mailMessage.cc(e.nextElement().toString());
         }

         e = this.bccList.elements();

         while(e.hasMoreElements()) {
            mailMessage.bcc(e.nextElement().toString());
         }

         if (this.subject != null) {
            mailMessage.setSubject(this.subject);
         }

         mailMessage.setHeader("Date", this.getDate());
         if (this.message.getCharset() != null) {
            mailMessage.setHeader("Content-Type", this.message.getMimeType() + "; charset=\"" + this.message.getCharset() + "\"");
         } else {
            mailMessage.setHeader("Content-Type", this.message.getMimeType());
         }

         e = this.headers.elements();

         while(e.hasMoreElements()) {
            Header h = (Header)e.nextElement();
            mailMessage.setHeader(h.getName(), h.getValue());
         }

         PrintStream out = mailMessage.getPrintStream();
         this.message.print(out);
         e = this.files.elements();

         while(e.hasMoreElements()) {
            this.attach((File)e.nextElement(), out);
         }

         mailMessage.sendAndClose();
      } catch (IOException var4) {
         throw new BuildException("IO error sending mail", var4);
      }
   }

   protected void attach(File file, PrintStream out) throws IOException {
      if (file.exists() && file.canRead()) {
         if (this.includeFileNames) {
            out.println();
            String filename = file.getName();
            int filenamelength = filename.length();
            out.println(filename);

            for(int star = 0; star < filenamelength; ++star) {
               out.print('=');
            }

            out.println();
         }

         int maxBuf = true;
         byte[] buf = new byte[1024];
         FileInputStream finstr = new FileInputStream(file);

         try {
            BufferedInputStream in = new BufferedInputStream(finstr, buf.length);

            int length;
            while((length = in.read(buf)) != -1) {
               out.write(buf, 0, length);
            }
         } finally {
            finstr.close();
         }

      } else {
         throw new BuildException("File \"" + file.getName() + "\" does not exist or is not " + "readable.");
      }
   }
}
