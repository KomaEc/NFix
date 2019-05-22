package org.apache.maven.surefire.report;

public class CategorizedReportEntry extends SimpleReportEntry implements ReportEntry {
   private static final String GROUP_PREFIX = " (of ";
   private static final String GROUP_SUFIX = ")";
   private final String group;

   public CategorizedReportEntry(String source, String name, String group) {
      this(source, name, group, (StackTraceWriter)null, (Integer)null);
   }

   public CategorizedReportEntry(String source, String name, String group, StackTraceWriter stackTraceWriter, Integer elapsed) {
      super(source, name, stackTraceWriter, elapsed);
      this.group = group;
   }

   public CategorizedReportEntry(String source, String name, String group, StackTraceWriter stackTraceWriter, Integer elapsed, String message) {
      super(source, name, stackTraceWriter, elapsed, message);
      this.group = group;
   }

   public static ReportEntry reportEntry(String source, String name, String group, StackTraceWriter stackTraceWriter, Integer elapsed, String message) {
      return (ReportEntry)(group != null ? new CategorizedReportEntry(source, name, group, stackTraceWriter, elapsed, message) : new SimpleReportEntry(source, name, stackTraceWriter, elapsed, message));
   }

   public String getGroup() {
      return this.group;
   }

   public String getNameWithGroup() {
      StringBuilder result = new StringBuilder();
      result.append(this.getName());
      if (this.getGroup() != null && !this.getName().equals(this.getGroup())) {
         result.append(" (of ");
         result.append(this.getGroup());
         result.append(")");
      }

      return result.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            boolean var10000;
            label41: {
               CategorizedReportEntry that = (CategorizedReportEntry)o;
               if (this.group != null) {
                  if (this.group.equals(that.group)) {
                     break label41;
                  }
               } else if (that.group == null) {
                  break label41;
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + (this.group != null ? this.group.hashCode() : 0);
      return result;
   }
}
