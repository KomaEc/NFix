package org.apache.tools.ant.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.email.EmailAddress;
import org.apache.tools.ant.taskdefs.email.Mailer;
import org.apache.tools.ant.taskdefs.email.Message;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.mail.MailMessage;

public class MailLogger extends DefaultLogger {
   private StringBuffer buffer = new StringBuffer();
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$listener$MailLogger;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$email$Mailer;

   public void buildFinished(BuildEvent event) {
      super.buildFinished(event);
      Project project = event.getProject();
      Hashtable properties = project.getProperties();
      Properties fileProperties = new Properties();
      String filename = (String)properties.get("MailLogger.properties.file");
      if (filename != null) {
         FileInputStream is = null;

         try {
            is = new FileInputStream(filename);
            fileProperties.load(is);
         } catch (IOException var25) {
         } finally {
            if (is != null) {
               try {
                  is.close();
               } catch (IOException var24) {
               }
            }

         }
      }

      Enumeration e = fileProperties.keys();

      String prefix;
      while(e.hasMoreElements()) {
         prefix = (String)e.nextElement();
         String value = fileProperties.getProperty(prefix);
         properties.put(prefix, project.replaceProperties(value));
      }

      boolean success = event.getException() == null;
      prefix = success ? "success" : "failure";

      try {
         boolean notify = Project.toBoolean(this.getValue(properties, prefix + ".notify", "on"));
         if (!notify) {
            return;
         }

         String mailhost = this.getValue(properties, "mailhost", "localhost");
         int port = Integer.parseInt(this.getValue(properties, "port", String.valueOf(25)));
         String user = this.getValue(properties, "user", "");
         String password = this.getValue(properties, "password", "");
         boolean ssl = Project.toBoolean(this.getValue(properties, "ssl", "off"));
         String from = this.getValue(properties, "from", (String)null);
         String replytoList = this.getValue(properties, "replyto", "");
         String toList = this.getValue(properties, prefix + ".to", (String)null);
         String subject = this.getValue(properties, prefix + ".subject", success ? "Build Success" : "Build Failure");
         if (user.equals("") && password.equals("") && !ssl) {
            this.sendMail(mailhost, port, from, replytoList, toList, subject, this.buffer.substring(0));
         } else {
            this.sendMimeMail(event.getProject(), mailhost, port, user, password, ssl, from, replytoList, toList, subject, this.buffer.substring(0));
         }
      } catch (Exception var27) {
         System.out.println("MailLogger failed to send e-mail!");
         var27.printStackTrace(System.err);
      }

   }

   protected void log(String message) {
      this.buffer.append(message).append(StringUtils.LINE_SEP);
   }

   private String getValue(Hashtable properties, String name, String defaultValue) throws Exception {
      String propertyName = "MailLogger." + name;
      String value = (String)properties.get(propertyName);
      if (value == null) {
         value = defaultValue;
      }

      if (value == null) {
         throw new Exception("Missing required parameter: " + propertyName);
      } else {
         return value;
      }
   }

   private void sendMail(String mailhost, int port, String from, String replyToList, String toList, String subject, String message) throws IOException {
      MailMessage mailMessage = new MailMessage(mailhost, port);
      mailMessage.setHeader("Date", DateUtils.getDateForHeader());
      mailMessage.from(from);
      StringTokenizer t;
      if (!replyToList.equals("")) {
         t = new StringTokenizer(replyToList, ", ", false);

         while(t.hasMoreTokens()) {
            mailMessage.replyto(t.nextToken());
         }
      }

      t = new StringTokenizer(toList, ", ", false);

      while(t.hasMoreTokens()) {
         mailMessage.to(t.nextToken());
      }

      mailMessage.setSubject(subject);
      PrintStream ps = mailMessage.getPrintStream();
      ps.println(message);
      mailMessage.sendAndClose();
   }

   private void sendMimeMail(Project project, String host, int port, String user, String password, boolean ssl, String from, String replyToString, String toString, String subject, String message) {
      Mailer mailer = null;

      try {
         mailer = (Mailer)ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.MimeMailer", (class$org$apache$tools$ant$listener$MailLogger == null ? (class$org$apache$tools$ant$listener$MailLogger = class$("org.apache.tools.ant.listener.MailLogger")) : class$org$apache$tools$ant$listener$MailLogger).getClassLoader(), class$org$apache$tools$ant$taskdefs$email$Mailer == null ? (class$org$apache$tools$ant$taskdefs$email$Mailer = class$("org.apache.tools.ant.taskdefs.email.Mailer")) : class$org$apache$tools$ant$taskdefs$email$Mailer);
      } catch (BuildException var16) {
         Throwable t = var16.getCause() == null ? var16 : var16.getCause();
         this.log("Failed to initialise MIME mail: " + ((Throwable)t).getMessage());
         return;
      }

      Vector replyToList = this.vectorizeEmailAddresses(replyToString);
      mailer.setHost(host);
      mailer.setPort(port);
      mailer.setUser(user);
      mailer.setPassword(password);
      mailer.setSSL(ssl);
      Message mymessage = new Message(message);
      mymessage.setProject(project);
      mailer.setMessage(mymessage);
      mailer.setFrom(new EmailAddress(from));
      mailer.setReplyToList(replyToList);
      Vector toList = this.vectorizeEmailAddresses(toString);
      mailer.setToList(toList);
      mailer.setCcList(new Vector());
      mailer.setBccList(new Vector());
      mailer.setFiles(new Vector());
      mailer.setSubject(subject);
      mailer.send();
   }

   private Vector vectorizeEmailAddresses(String listString) {
      Vector emailList = new Vector();
      StringTokenizer tokens = new StringTokenizer(listString, ",");

      while(tokens.hasMoreTokens()) {
         emailList.addElement(new EmailAddress(tokens.nextToken()));
      }

      return emailList;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
