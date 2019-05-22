package org.apache.tools.ant.taskdefs.email;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.ClasspathUtils;

public class EmailTask extends Task {
   public static final String AUTO = "auto";
   public static final String MIME = "mime";
   public static final String UU = "uu";
   public static final String PLAIN = "plain";
   private String encoding = "auto";
   private String host = "localhost";
   private int port = 25;
   private String subject = null;
   private Message message = null;
   private boolean failOnError = true;
   private boolean includeFileNames = false;
   private String messageMimeType = null;
   private EmailAddress from = null;
   private Vector replyToList = new Vector();
   private Vector toList = new Vector();
   private Vector ccList = new Vector();
   private Vector bccList = new Vector();
   private Vector headers = new Vector();
   private Path attachments = null;
   private String charset = null;
   private String user = null;
   private String password = null;
   private boolean ssl = false;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$email$EmailTask;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$email$Mailer;

   public void setUser(String user) {
      this.user = user;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setSSL(boolean ssl) {
      this.ssl = ssl;
   }

   public void setEncoding(EmailTask.Encoding encoding) {
      this.encoding = encoding.getValue();
   }

   public void setMailport(int port) {
      this.port = port;
   }

   public void setMailhost(String host) {
      this.host = host;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public void setMessage(String message) {
      if (this.message != null) {
         throw new BuildException("Only one message can be sent in an email");
      } else {
         this.message = new Message(message);
         this.message.setProject(this.getProject());
      }
   }

   public void setMessageFile(File file) {
      if (this.message != null) {
         throw new BuildException("Only one message can be sent in an email");
      } else {
         this.message = new Message(file);
         this.message.setProject(this.getProject());
      }
   }

   public void setMessageMimeType(String type) {
      this.messageMimeType = type;
   }

   public void addMessage(Message message) throws BuildException {
      if (this.message != null) {
         throw new BuildException("Only one message can be sent in an email");
      } else {
         this.message = message;
      }
   }

   public void addFrom(EmailAddress address) {
      if (this.from != null) {
         throw new BuildException("Emails can only be from one address");
      } else {
         this.from = address;
      }
   }

   public void setFrom(String address) {
      if (this.from != null) {
         throw new BuildException("Emails can only be from one address");
      } else {
         this.from = new EmailAddress(address);
      }
   }

   public void addReplyTo(EmailAddress address) {
      this.replyToList.add(address);
   }

   public void setReplyTo(String address) {
      this.replyToList.add(new EmailAddress(address));
   }

   public void addTo(EmailAddress address) {
      this.toList.addElement(address);
   }

   public void setToList(String list) {
      StringTokenizer tokens = new StringTokenizer(list, ",");

      while(tokens.hasMoreTokens()) {
         this.toList.addElement(new EmailAddress(tokens.nextToken()));
      }

   }

   public void addCc(EmailAddress address) {
      this.ccList.addElement(address);
   }

   public void setCcList(String list) {
      StringTokenizer tokens = new StringTokenizer(list, ",");

      while(tokens.hasMoreTokens()) {
         this.ccList.addElement(new EmailAddress(tokens.nextToken()));
      }

   }

   public void addBcc(EmailAddress address) {
      this.bccList.addElement(address);
   }

   public void setBccList(String list) {
      StringTokenizer tokens = new StringTokenizer(list, ",");

      while(tokens.hasMoreTokens()) {
         this.bccList.addElement(new EmailAddress(tokens.nextToken()));
      }

   }

   public void setFailOnError(boolean failOnError) {
      this.failOnError = failOnError;
   }

   public void setFiles(String filenames) {
      StringTokenizer t = new StringTokenizer(filenames, ", ");

      while(t.hasMoreTokens()) {
         this.createAttachments().add((ResourceCollection)(new FileResource(this.getProject().resolveFile(t.nextToken()))));
      }

   }

   public void addFileset(FileSet fs) {
      this.createAttachments().add((ResourceCollection)fs);
   }

   public Path createAttachments() {
      if (this.attachments == null) {
         this.attachments = new Path(this.getProject());
      }

      return this.attachments.createPath();
   }

   public Header createHeader() {
      Header h = new Header();
      this.headers.add(h);
      return h;
   }

   public void setIncludefilenames(boolean includeFileNames) {
      this.includeFileNames = includeFileNames;
   }

   public boolean getIncludeFileNames() {
      return this.includeFileNames;
   }

   public void execute() {
      Message savedMessage = this.message;

      try {
         Mailer mailer = null;
         boolean autoFound = false;
         Object t;
         if (this.encoding.equals("mime") || this.encoding.equals("auto") && !autoFound) {
            try {
               mailer = (Mailer)ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.MimeMailer", (class$org$apache$tools$ant$taskdefs$email$EmailTask == null ? (class$org$apache$tools$ant$taskdefs$email$EmailTask = class$("org.apache.tools.ant.taskdefs.email.EmailTask")) : class$org$apache$tools$ant$taskdefs$email$EmailTask).getClassLoader(), class$org$apache$tools$ant$taskdefs$email$Mailer == null ? (class$org$apache$tools$ant$taskdefs$email$Mailer = class$("org.apache.tools.ant.taskdefs.email.Mailer")) : class$org$apache$tools$ant$taskdefs$email$Mailer);
               autoFound = true;
               this.log("Using MIME mail", 3);
            } catch (BuildException var14) {
               t = var14.getCause() == null ? var14 : var14.getCause();
               this.log("Failed to initialise MIME mail: " + ((Throwable)t).getMessage(), 1);
               return;
            }
         }

         label459: {
            if (autoFound || this.user == null && this.password == null || !this.encoding.equals("uu") && !this.encoding.equals("plain")) {
               if (autoFound || !this.ssl || !this.encoding.equals("uu") && !this.encoding.equals("plain")) {
                  if (this.encoding.equals("uu") || this.encoding.equals("auto") && !autoFound) {
                     try {
                        mailer = (Mailer)ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.UUMailer", (class$org$apache$tools$ant$taskdefs$email$EmailTask == null ? (class$org$apache$tools$ant$taskdefs$email$EmailTask = class$("org.apache.tools.ant.taskdefs.email.EmailTask")) : class$org$apache$tools$ant$taskdefs$email$EmailTask).getClassLoader(), class$org$apache$tools$ant$taskdefs$email$Mailer == null ? (class$org$apache$tools$ant$taskdefs$email$Mailer = class$("org.apache.tools.ant.taskdefs.email.Mailer")) : class$org$apache$tools$ant$taskdefs$email$Mailer);
                        autoFound = true;
                        this.log("Using UU mail", 3);
                     } catch (BuildException var13) {
                        t = var13.getCause() == null ? var13 : var13.getCause();
                        this.log("Failed to initialise UU mail: " + ((Throwable)t).getMessage(), 1);
                        return;
                     }
                  }

                  if (this.encoding.equals("plain") || this.encoding.equals("auto") && !autoFound) {
                     mailer = new PlainMailer();
                     autoFound = true;
                     this.log("Using plain mail", 3);
                  }

                  if (mailer == null) {
                     throw new BuildException("Failed to initialise encoding: " + this.encoding);
                  }

                  if (this.message == null) {
                     this.message = new Message();
                     this.message.setProject(this.getProject());
                  }

                  if (this.from != null && this.from.getAddress() != null) {
                     if (this.toList.isEmpty() && this.ccList.isEmpty() && this.bccList.isEmpty()) {
                        throw new BuildException("At least one of to, cc or bcc must be supplied");
                     }

                     if (this.messageMimeType == null) {
                        break label459;
                     }

                     if (this.message.isMimeTypeSpecified()) {
                        throw new BuildException("The mime type can only be specified in one location");
                     }

                     this.message.setMimeType(this.messageMimeType);
                     break label459;
                  }

                  throw new BuildException("A from element is required");
               }

               throw new BuildException("SSL only possible with MIME mail");
            }

            throw new BuildException("SMTP auth only possible with MIME mail");
         }

         if (this.charset != null) {
            if (this.message.getCharset() != null) {
               throw new BuildException("The charset can only be specified in one location");
            }

            this.message.setCharset(this.charset);
         }

         Vector files = new Vector();
         if (this.attachments != null) {
            Iterator iter = this.attachments.iterator();

            while(iter.hasNext()) {
               FileResource fr = (FileResource)iter.next();
               files.addElement(fr.getFile());
            }
         }

         this.log("Sending email: " + this.subject, 2);
         this.log("From " + this.from, 3);
         this.log("ReplyTo " + this.replyToList, 3);
         this.log("To " + this.toList, 3);
         this.log("Cc " + this.ccList, 3);
         this.log("Bcc " + this.bccList, 3);
         ((Mailer)mailer).setHost(this.host);
         ((Mailer)mailer).setPort(this.port);
         ((Mailer)mailer).setUser(this.user);
         ((Mailer)mailer).setPassword(this.password);
         ((Mailer)mailer).setSSL(this.ssl);
         ((Mailer)mailer).setMessage(this.message);
         ((Mailer)mailer).setFrom(this.from);
         ((Mailer)mailer).setReplyToList(this.replyToList);
         ((Mailer)mailer).setToList(this.toList);
         ((Mailer)mailer).setCcList(this.ccList);
         ((Mailer)mailer).setBccList(this.bccList);
         ((Mailer)mailer).setFiles(files);
         ((Mailer)mailer).setSubject(this.subject);
         ((Mailer)mailer).setTask(this);
         ((Mailer)mailer).setIncludeFileNames(this.includeFileNames);
         ((Mailer)mailer).setHeaders(this.headers);
         ((Mailer)mailer).send();
         int count = files.size();
         this.log("Sent email with " + count + " attachment" + (count == 1 ? "" : "s"), 2);
      } catch (BuildException var15) {
         Throwable t = var15.getCause() == null ? var15 : var15.getCause();
         this.log("Failed to send email: " + ((Throwable)t).getMessage(), 1);
         if (this.failOnError) {
            throw var15;
         }
      } catch (Exception var16) {
         this.log("Failed to send email: " + var16.getMessage(), 1);
         if (this.failOnError) {
            throw new BuildException(var16);
         }
      } finally {
         this.message = savedMessage;
      }

   }

   public void setCharset(String charset) {
      this.charset = charset;
   }

   public String getCharset() {
      return this.charset;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class Encoding extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"auto", "mime", "uu", "plain"};
      }
   }
}
