package org.apache.tools.ant;

import java.io.Serializable;
import org.apache.tools.ant.util.FileUtils;
import org.xml.sax.Locator;

public class Location implements Serializable {
   private String fileName;
   private int lineNumber;
   private int columnNumber;
   public static final Location UNKNOWN_LOCATION = new Location();
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

   private Location() {
      this((String)null, 0, 0);
   }

   public Location(String fileName) {
      this(fileName, 0, 0);
   }

   public Location(Locator loc) {
      this(loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber());
   }

   public Location(String fileName, int lineNumber, int columnNumber) {
      if (fileName != null && fileName.startsWith("file:")) {
         this.fileName = FILE_UTILS.fromURI(fileName);
      } else {
         this.fileName = fileName;
      }

      this.lineNumber = lineNumber;
      this.columnNumber = columnNumber;
   }

   public String getFileName() {
      return this.fileName;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      if (this.fileName != null) {
         buf.append(this.fileName);
         if (this.lineNumber != 0) {
            buf.append(":");
            buf.append(this.lineNumber);
         }

         buf.append(": ");
      }

      return buf.toString();
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (other == null) {
         return false;
      } else {
         return other.getClass() != this.getClass() ? false : this.toString().equals(other.toString());
      }
   }

   public int hashCode() {
      return this.toString().hashCode();
   }
}
