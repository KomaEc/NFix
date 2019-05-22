package org.apache.maven.scm.command.changelog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmVersion;

public class ChangeLogSet {
   public static final String DEFAULT_ENCODING = "ISO-8859-1";
   private List<ChangeSet> entries;
   private Date startDate;
   private Date endDate;
   private ScmVersion startVersion;
   private ScmVersion endVersion;

   public ChangeLogSet(Date startDate, Date endDate) {
      this.startDate = startDate;
      this.endDate = endDate;
   }

   public ChangeLogSet(List<ChangeSet> entries, Date startDate, Date endDate) {
      this(startDate, endDate);
      this.setChangeSets(entries);
   }

   public Date getStartDate() {
      return this.startDate;
   }

   public Date getEndDate() {
      return this.endDate;
   }

   public ScmVersion getStartVersion() {
      return this.startVersion;
   }

   public void setStartVersion(ScmVersion startVersion) {
      this.startVersion = startVersion;
   }

   public ScmVersion getEndVersion() {
      return this.endVersion;
   }

   public void setEndVersion(ScmVersion endVersion) {
      this.endVersion = endVersion;
   }

   public List<ChangeSet> getChangeSets() {
      return this.entries;
   }

   public void setChangeSets(List<ChangeSet> changeSets) {
      this.entries = changeSets;
   }

   public String toXML() {
      return this.toXML("ISO-8859-1");
   }

   public String toXML(String encoding) {
      String encodingString = encoding;
      if (encoding == null) {
         encodingString = "ISO-8859-1";
      }

      StringBuilder buffer = new StringBuilder();
      String pattern = "yyyyMMdd HH:mm:ss z";
      SimpleDateFormat formatter = new SimpleDateFormat(pattern);
      buffer.append("<?xml version=\"1.0\" encoding=\"" + encodingString + "\"?>\n");
      buffer.append("<changeset datePattern=\"").append(pattern).append("\"");
      if (this.startDate != null) {
         buffer.append(" start=\"").append(formatter.format(this.getStartDate())).append("\"");
      }

      if (this.endDate != null) {
         buffer.append(" end=\"").append(formatter.format(this.getEndDate())).append("\"");
      }

      if (this.startVersion != null) {
         buffer.append(" startVersion=\"").append(this.getStartVersion()).append("\"");
      }

      if (this.endVersion != null) {
         buffer.append(" endVersion=\"").append(this.getEndVersion()).append("\"");
      }

      buffer.append(">\n");
      Iterator i$ = this.getChangeSets().iterator();

      while(i$.hasNext()) {
         ChangeSet changeSet = (ChangeSet)i$.next();
         buffer.append(changeSet.toXML());
      }

      buffer.append("</changeset>\n");
      return buffer.toString();
   }
}
