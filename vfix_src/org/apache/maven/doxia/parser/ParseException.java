package org.apache.maven.doxia.parser;

public class ParseException extends Exception {
   private String fileName;
   private int lineNumber;

   public ParseException(String message) {
      this((Exception)null, message, (String)null, -1);
   }

   public ParseException(String message, Exception e) {
      this(e, message, (String)null, -1);
   }

   public ParseException(Exception e) {
      this(e, (String)null, (String)null, -1);
   }

   public ParseException(Exception e, String file, int line) {
      this(e, (String)null, file, line);
   }

   public ParseException(Exception e, String message, String file, int line) {
      super(message == null ? (e == null ? null : e.getMessage()) : message, e);
      this.fileName = file;
      this.lineNumber = line;
   }

   public String getFileName() {
      return this.fileName;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }
}
