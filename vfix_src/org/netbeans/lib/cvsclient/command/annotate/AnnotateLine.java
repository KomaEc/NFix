package org.netbeans.lib.cvsclient.command.annotate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnnotateLine {
   private static final DateFormat DATE_FORMAT;
   private String author;
   private String revision;
   private Date date;
   private String dateString;
   private String content;
   private int lineNum;

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String var1) {
      this.author = var1;
   }

   public String getRevision() {
      return this.revision;
   }

   public void setRevision(String var1) {
      this.revision = var1;
   }

   public Date getDate() {
      return this.date;
   }

   public String getDateString() {
      return this.dateString;
   }

   public void setDateString(String var1) {
      this.dateString = var1;

      try {
         this.date = DATE_FORMAT.parse(var1);
      } catch (ParseException var3) {
         var3.printStackTrace();
      }

   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String var1) {
      this.content = var1;
   }

   public int getLineNum() {
      return this.lineNum;
   }

   public Integer getLineNumInteger() {
      return new Integer(this.lineNum);
   }

   public void setLineNum(int var1) {
      this.lineNum = var1;
   }

   static {
      DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy", Locale.US);
   }
}
