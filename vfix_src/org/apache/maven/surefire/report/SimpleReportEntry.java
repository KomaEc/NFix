package org.apache.maven.surefire.report;

public class SimpleReportEntry implements ReportEntry {
   private final String source;
   private final String name;
   private final StackTraceWriter stackTraceWriter;
   private final Integer elapsed;
   private final String message;

   public SimpleReportEntry(String source, String name) {
      this(source, name, (StackTraceWriter)null, (Integer)null);
   }

   private SimpleReportEntry(String source, String name, StackTraceWriter stackTraceWriter) {
      this(source, name, stackTraceWriter, (Integer)null);
   }

   public SimpleReportEntry(String source, String name, Integer elapsed) {
      this(source, name, (StackTraceWriter)null, elapsed);
   }

   protected SimpleReportEntry(String source, String name, StackTraceWriter stackTraceWriter, Integer elapsed, String message) {
      if (source == null) {
         source = "null";
      }

      if (name == null) {
         name = "null";
      }

      this.source = source;
      this.name = name;
      this.stackTraceWriter = stackTraceWriter;
      this.message = message;
      this.elapsed = elapsed;
   }

   public SimpleReportEntry(String source, String name, StackTraceWriter stackTraceWriter, Integer elapsed) {
      this(source, name, stackTraceWriter, elapsed, safeGetMessage(stackTraceWriter));
   }

   public static SimpleReportEntry ignored(String source, String name, String message) {
      return new SimpleReportEntry(source, name, (StackTraceWriter)null, (Integer)null, message);
   }

   public static SimpleReportEntry withException(String source, String name, StackTraceWriter stackTraceWriter) {
      return new SimpleReportEntry(source, name, stackTraceWriter);
   }

   private static String safeGetMessage(StackTraceWriter stackTraceWriter) {
      try {
         return stackTraceWriter != null && stackTraceWriter.getThrowable() != null ? stackTraceWriter.getThrowable().getMessage() : null;
      } catch (Throwable var2) {
         return var2.getMessage();
      }
   }

   public String getSourceName() {
      return this.source;
   }

   public String getName() {
      return this.name;
   }

   public String getGroup() {
      return null;
   }

   public StackTraceWriter getStackTraceWriter() {
      return this.stackTraceWriter;
   }

   public Integer getElapsed() {
      return this.elapsed;
   }

   public String toString() {
      return "ReportEntry{source='" + this.source + '\'' + ", name='" + this.name + '\'' + ", stackTraceWriter=" + this.stackTraceWriter + ", elapsed=" + this.elapsed + ",message=" + this.message + '}';
   }

   public String getMessage() {
      return this.message;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SimpleReportEntry that;
         label57: {
            that = (SimpleReportEntry)o;
            if (this.elapsed != null) {
               if (this.elapsed.equals(that.elapsed)) {
                  break label57;
               }
            } else if (that.elapsed == null) {
               break label57;
            }

            return false;
         }

         label50: {
            if (this.name != null) {
               if (this.name.equals(that.name)) {
                  break label50;
               }
            } else if (that.name == null) {
               break label50;
            }

            return false;
         }

         if (this.source != null) {
            if (!this.source.equals(that.source)) {
               return false;
            }
         } else if (that.source != null) {
            return false;
         }

         if (this.stackTraceWriter != null) {
            if (!this.stackTraceWriter.equals(that.stackTraceWriter)) {
               return false;
            }
         } else if (that.stackTraceWriter != null) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.source != null ? this.source.hashCode() : 0;
      result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 31 * result + (this.stackTraceWriter != null ? this.stackTraceWriter.hashCode() : 0);
      result = 31 * result + (this.elapsed != null ? this.elapsed.hashCode() : 0);
      return result;
   }

   public String getNameWithGroup() {
      return this.getName();
   }
}
