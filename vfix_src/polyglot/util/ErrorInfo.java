package polyglot.util;

public class ErrorInfo {
   public static final int WARNING = 0;
   public static final int INTERNAL_ERROR = 1;
   public static final int IO_ERROR = 2;
   public static final int LEXICAL_ERROR = 3;
   public static final int SYNTAX_ERROR = 4;
   public static final int SEMANTIC_ERROR = 5;
   public static final int POST_COMPILER_ERROR = 6;
   protected int kind;
   protected String message;
   protected Position position;

   public ErrorInfo(int kind, String message, Position position) {
      this.kind = kind;
      this.message = message;
      this.position = position;
   }

   public int getErrorKind() {
      return this.kind;
   }

   public String getMessage() {
      return this.message;
   }

   public Position getPosition() {
      return this.position;
   }

   public String getErrorString() {
      return getErrorString(this.kind);
   }

   public static String getErrorString(int kind) {
      switch(kind) {
      case 0:
         return "Warning";
      case 1:
         return "Internal Error";
      case 2:
         return "I/O Error";
      case 3:
         return "Lexical Error";
      case 4:
         return "Syntax Error";
      case 5:
         return "Semantic Error";
      case 6:
         return "Post-compiler Error";
      default:
         return "(Unknown)";
      }
   }
}
