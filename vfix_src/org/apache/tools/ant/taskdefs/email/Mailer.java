package org.apache.tools.ant.taskdefs.email;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.DateUtils;

public abstract class Mailer {
   protected String host = null;
   protected int port = -1;
   protected String user = null;
   protected String password = null;
   protected boolean SSL = false;
   protected Message message;
   protected EmailAddress from;
   protected Vector replyToList = null;
   protected Vector toList = null;
   protected Vector ccList = null;
   protected Vector bccList = null;
   protected Vector files = null;
   protected String subject = null;
   protected Task task;
   protected boolean includeFileNames = false;
   protected Vector headers = null;

   public void setHost(String host) {
      this.host = host;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setSSL(boolean ssl) {
      this.SSL = ssl;
   }

   public void setMessage(Message m) {
      this.message = m;
   }

   public void setFrom(EmailAddress from) {
      this.from = from;
   }

   public void setReplyToList(Vector list) {
      this.replyToList = list;
   }

   public void setToList(Vector list) {
      this.toList = list;
   }

   public void setCcList(Vector list) {
      this.ccList = list;
   }

   public void setBccList(Vector list) {
      this.bccList = list;
   }

   public void setFiles(Vector files) {
      this.files = files;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public void setTask(Task task) {
      this.task = task;
   }

   public void setIncludeFileNames(boolean b) {
      this.includeFileNames = b;
   }

   public void setHeaders(Vector v) {
      this.headers = v;
   }

   public abstract void send() throws BuildException;

   protected final String getDate() {
      return DateUtils.getDateForHeader();
   }
}
