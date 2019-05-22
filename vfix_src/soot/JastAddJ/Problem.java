package soot.JastAddJ;

public class Problem implements Comparable {
   protected int line;
   protected int column;
   protected int endLine;
   protected int endColumn;
   protected String fileName;
   protected String message;
   protected Problem.Severity severity;
   protected Problem.Kind kind;

   public int compareTo(Object o) {
      if (o instanceof Problem) {
         Problem other = (Problem)o;
         if (!this.fileName.equals(other.fileName)) {
            return this.fileName.compareTo(other.fileName);
         } else {
            return this.line != other.line ? this.line - other.line : this.message.compareTo(other.message);
         }
      } else {
         return 0;
      }
   }

   public int line() {
      return this.line;
   }

   public int column() {
      return this.column;
   }

   public int endLine() {
      return this.endLine;
   }

   public int endColumn() {
      return this.endColumn;
   }

   public String fileName() {
      return this.fileName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public String message() {
      return this.message;
   }

   public Problem.Severity severity() {
      return this.severity;
   }

   public Problem.Kind kind() {
      return this.kind;
   }

   public Problem(String fileName, String message) {
      this.line = -1;
      this.column = -1;
      this.endLine = -1;
      this.endColumn = -1;
      this.severity = Problem.Severity.ERROR;
      this.kind = Problem.Kind.OTHER;
      this.fileName = fileName;
      this.message = message;
   }

   public Problem(String fileName, String message, int line) {
      this(fileName, message);
      this.line = line;
   }

   public Problem(String fileName, String message, int line, Problem.Severity severity) {
      this(fileName, message);
      this.line = line;
      this.severity = severity;
   }

   public Problem(String fileName, String message, int line, int column, Problem.Severity severity) {
      this(fileName, message);
      this.line = line;
      this.column = column;
      this.severity = severity;
   }

   public Problem(String fileName, String message, int line, Problem.Severity severity, Problem.Kind kind) {
      this(fileName, message);
      this.line = line;
      this.kind = kind;
      this.severity = severity;
   }

   public Problem(String fileName, String message, int line, int column, Problem.Severity severity, Problem.Kind kind) {
      this(fileName, message);
      this.line = line;
      this.column = column;
      this.kind = kind;
      this.severity = severity;
   }

   public Problem(String fileName, String message, int line, int column, int endLine, int endColumn, Problem.Severity severity, Problem.Kind kind) {
      this(fileName, message);
      this.line = line;
      this.column = column;
      this.endLine = endLine;
      this.endColumn = endColumn;
      this.kind = kind;
      this.severity = severity;
   }

   public String toString() {
      String location = "";
      if (this.line != -1 && this.column != -1) {
         location = this.line + "," + this.column + ":";
      } else if (this.line != -1) {
         location = this.line + ":";
      }

      String s = "";
      if (this.kind == Problem.Kind.LEXICAL) {
         s = "Lexical Error: ";
      } else if (this.kind == Problem.Kind.SYNTACTIC) {
         s = "Syntactic Error: ";
      } else if (this.kind == Problem.Kind.SEMANTIC) {
         s = "Semantic Error: ";
      }

      return this.fileName + ":" + location + "\n  " + s + this.message;
   }

   public static class Kind {
      public static final Problem.Kind OTHER = new Problem.Kind();
      public static final Problem.Kind LEXICAL = new Problem.Kind();
      public static final Problem.Kind SYNTACTIC = new Problem.Kind();
      public static final Problem.Kind SEMANTIC = new Problem.Kind();

      private Kind() {
      }
   }

   public static class Severity {
      public static final Problem.Severity ERROR = new Problem.Severity();
      public static final Problem.Severity WARNING = new Problem.Severity();

      private Severity() {
      }
   }
}
