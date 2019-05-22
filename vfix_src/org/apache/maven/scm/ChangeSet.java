package org.apache.maven.scm;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.util.FilenameUtils;
import org.apache.maven.scm.util.ThreadSafeDateFormat;
import org.codehaus.plexus.util.StringUtils;

public class ChangeSet implements Serializable {
   private static final long serialVersionUID = 7097705862222539801L;
   public static final String LESS_THAN_ENTITY = "&lt;";
   public static final String GREATER_THAN_ENTITY = "&gt;";
   public static final String AMPERSAND_ENTITY = "&amp;";
   public static final String APOSTROPHE_ENTITY = "&apos;";
   public static final String QUOTE_ENTITY = "&quot;";
   private static final String DATE_PATTERN = "yyyy-MM-dd";
   private static final ThreadSafeDateFormat DATE_FORMAT = new ThreadSafeDateFormat("yyyy-MM-dd");
   private static final String TIME_PATTERN = "HH:mm:ss";
   private static final ThreadSafeDateFormat TIME_FORMAT = new ThreadSafeDateFormat("HH:mm:ss");
   private static final ThreadSafeDateFormat TIMESTAMP_FORMAT_1 = new ThreadSafeDateFormat("yyyy/MM/dd HH:mm:ss");
   private static final ThreadSafeDateFormat TIMESTAMP_FORMAT_2 = new ThreadSafeDateFormat("yyyy-MM-dd HH:mm:ss");
   private static final ThreadSafeDateFormat TIMESTAMP_FORMAT_3 = new ThreadSafeDateFormat("yyyy/MM/dd HH:mm:ss z");
   private static final ThreadSafeDateFormat TIMESTAMP_FORMAT_4 = new ThreadSafeDateFormat("yyyy-MM-dd HH:mm:ss z");
   private Date date;
   private String author;
   private String comment;
   private List<ChangeFile> files;
   private String revision;
   private String parentRevision;
   private Set<String> mergedRevisions;

   public ChangeSet(String strDate, String userDatePattern, String comment, String author, List<ChangeFile> files) {
      this((Date)null, comment, author, files);
      this.setDate(strDate, userDatePattern);
   }

   public ChangeSet(Date date, String comment, String author, List<ChangeFile> files) {
      this.comment = "";
      this.setDate(date);
      this.setAuthor(author);
      this.setComment(comment);
      this.files = files;
   }

   public ChangeSet() {
      this.comment = "";
   }

   public List<ChangeFile> getFiles() {
      return (List)(this.files == null ? new ArrayList() : this.files);
   }

   public void setFiles(List<ChangeFile> files) {
      this.files = files;
   }

   public void addFile(ChangeFile file) {
      if (this.files == null) {
         this.files = new ArrayList();
      }

      this.files.add(file);
   }

   /** @deprecated */
   public boolean containsFilename(String filename, ScmProviderRepository repository) {
      return this.containsFilename(filename);
   }

   public boolean containsFilename(String filename) {
      if (this.files != null) {
         Iterator i$ = this.files.iterator();

         while(i$.hasNext()) {
            ChangeFile file = (ChangeFile)i$.next();
            String f1 = FilenameUtils.normalizeFilename(file.getName());
            String f2 = FilenameUtils.normalizeFilename(filename);
            if (f1.indexOf(f2) >= 0) {
               return true;
            }
         }
      }

      return false;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public String getComment() {
      return this.comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public Date getDate() {
      return this.date != null ? (Date)this.date.clone() : null;
   }

   public void setDate(Date date) {
      if (date != null) {
         this.date = new Date(date.getTime());
      }

   }

   public void setDate(String date) {
      this.setDate(date, (String)null);
   }

   public void setDate(String date, String userDatePattern) {
      try {
         if (!StringUtils.isEmpty(userDatePattern)) {
            SimpleDateFormat format = new SimpleDateFormat(userDatePattern);
            this.date = format.parse(date);
         } else {
            this.date = TIMESTAMP_FORMAT_3.parse(date);
         }
      } catch (ParseException var15) {
         if (!StringUtils.isEmpty(userDatePattern)) {
            try {
               this.date = TIMESTAMP_FORMAT_3.parse(date);
            } catch (ParseException var14) {
               try {
                  this.date = TIMESTAMP_FORMAT_4.parse(date);
               } catch (ParseException var13) {
                  try {
                     this.date = TIMESTAMP_FORMAT_1.parse(date);
                  } catch (ParseException var12) {
                     try {
                        this.date = TIMESTAMP_FORMAT_2.parse(date);
                     } catch (ParseException var11) {
                        throw new IllegalArgumentException("Unable to parse date: " + date);
                     }
                  }
               }
            }
         } else {
            try {
               this.date = TIMESTAMP_FORMAT_4.parse(date);
            } catch (ParseException var10) {
               try {
                  this.date = TIMESTAMP_FORMAT_1.parse(date);
               } catch (ParseException var9) {
                  try {
                     this.date = TIMESTAMP_FORMAT_2.parse(date);
                  } catch (ParseException var8) {
                     throw new IllegalArgumentException("Unable to parse date: " + date);
                  }
               }
            }
         }
      }

   }

   public String getDateFormatted() {
      return DATE_FORMAT.format(this.getDate());
   }

   public String getTimeFormatted() {
      return TIME_FORMAT.format(this.getDate());
   }

   public String getRevision() {
      return this.revision;
   }

   public void setRevision(String revision) {
      this.revision = revision;
   }

   public String getParentRevision() {
      return this.parentRevision;
   }

   public void setParentRevision(String parentRevision) {
      this.parentRevision = parentRevision;
   }

   public void addMergedRevision(String mergedRevision) {
      if (this.mergedRevisions == null) {
         this.mergedRevisions = new LinkedHashSet();
      }

      this.mergedRevisions.add(mergedRevision);
   }

   public Set<String> getMergedRevisions() {
      return this.mergedRevisions == null ? Collections.emptySet() : this.mergedRevisions;
   }

   public void setMergedRevisions(Set<String> mergedRevisions) {
      this.mergedRevisions = mergedRevisions;
   }

   public String toString() {
      StringBuilder result = new StringBuilder(this.author == null ? " null " : this.author);
      result.append("\n").append(this.date == null ? "null " : this.date.toString()).append("\n");
      if (this.parentRevision != null) {
         result.append("parent: ").append(this.parentRevision);
         if (!this.mergedRevisions.isEmpty()) {
            result.append(" + ");
            result.append(this.mergedRevisions);
         }

         result.append("\n");
      }

      if (this.files != null) {
         Iterator i$ = this.files.iterator();

         while(i$.hasNext()) {
            ChangeFile file = (ChangeFile)i$.next();
            result.append(file == null ? " null " : file.toString()).append("\n");
         }
      }

      result.append(this.comment == null ? " null " : this.comment);
      return result.toString();
   }

   public String toXML() {
      StringBuilder buffer = new StringBuilder("\t<changelog-entry>\n");
      if (this.getDate() != null) {
         buffer.append("\t\t<date pattern=\"yyyy-MM-dd\">").append(this.getDateFormatted()).append("</date>\n").append("\t\t<time pattern=\"HH:mm:ss\">").append(this.getTimeFormatted()).append("</time>\n");
      }

      buffer.append("\t\t<author><![CDATA[").append(this.author).append("]]></author>\n");
      if (this.parentRevision != null) {
         buffer.append("\t\t<parent>").append(this.getParentRevision()).append("</parent>\n");
      }

      Iterator i$ = this.getMergedRevisions().iterator();

      while(i$.hasNext()) {
         String mergedRevision = (String)i$.next();
         buffer.append("\t\t<merge>").append(mergedRevision).append("</merge>\n");
      }

      if (this.files != null) {
         for(i$ = this.files.iterator(); i$.hasNext(); buffer.append("\t\t</file>\n")) {
            ChangeFile file = (ChangeFile)i$.next();
            buffer.append("\t\t<file>\n");
            if (file.getAction() != null) {
               buffer.append("\t\t\t<action>").append(file.getAction()).append("</action>\n");
            }

            buffer.append("\t\t\t<name>").append(escapeValue(file.getName())).append("</name>\n");
            buffer.append("\t\t\t<revision>").append(file.getRevision()).append("</revision>\n");
            if (file.getOriginalName() != null) {
               buffer.append("\t\t\t<orig-name>");
               buffer.append(escapeValue(file.getOriginalName()));
               buffer.append("</orig-name>\n");
            }

            if (file.getOriginalRevision() != null) {
               buffer.append("\t\t\t<orig-revision>");
               buffer.append(file.getOriginalRevision());
               buffer.append("</orig-revision>\n");
            }
         }
      }

      buffer.append("\t\t<msg><![CDATA[").append(this.removeCDataEnd(this.comment)).append("]]></msg>\n");
      buffer.append("\t</changelog-entry>\n");
      return buffer.toString();
   }

   public boolean equals(Object obj) {
      if (obj instanceof ChangeSet) {
         ChangeSet changeSet = (ChangeSet)obj;
         if (this.toString().equals(changeSet.toString())) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.author == null ? 0 : this.author.hashCode());
      result = 31 * result + (this.comment == null ? 0 : this.comment.hashCode());
      result = 31 * result + (this.date == null ? 0 : this.date.hashCode());
      result = 31 * result + (this.parentRevision == null ? 0 : this.parentRevision.hashCode());
      result = 31 * result + (this.mergedRevisions == null ? 0 : this.mergedRevisions.hashCode());
      result = 31 * result + (this.files == null ? 0 : this.files.hashCode());
      return result;
   }

   private String removeCDataEnd(String message) {
      int endCdata;
      while(message != null && (endCdata = message.indexOf("]]>")) > -1) {
         message = message.substring(0, endCdata) + "] ] >" + message.substring(endCdata + 3, message.length());
      }

      return message;
   }

   public static String escapeValue(Object value) {
      StringBuilder buffer = new StringBuilder(value.toString());
      int i = 0;

      for(int size = buffer.length(); i < size; ++i) {
         switch(buffer.charAt(i)) {
         case '"':
            buffer.replace(i, i + 1, "&quot;");
            size += 5;
            i += 5;
            break;
         case '&':
            buffer.replace(i, i + 1, "&amp;");
            size += 4;
            i += 4;
            break;
         case '\'':
            buffer.replace(i, i + 1, "&apos;");
            size += 5;
            i += 5;
            break;
         case '<':
            buffer.replace(i, i + 1, "&lt;");
            size += 3;
            i += 3;
            break;
         case '>':
            buffer.replace(i, i + 1, "&gt;");
            size += 3;
            i += 3;
         }
      }

      return buffer.toString();
   }
}
