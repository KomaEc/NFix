package bsh;

public class ParseException extends EvalError {
   String sourceFile;
   protected boolean specialConstructor;
   public Token currentToken;
   public int[][] expectedTokenSequences;
   public String[] tokenImage;
   protected String eol;

   public void setErrorSourceFile(String var1) {
      this.sourceFile = var1;
   }

   public String getErrorSourceFile() {
      return this.sourceFile;
   }

   public ParseException(Token var1, int[][] var2, String[] var3) {
      this();
      this.specialConstructor = true;
      this.currentToken = var1;
      this.expectedTokenSequences = var2;
      this.tokenImage = var3;
   }

   public ParseException() {
      this("");
      this.specialConstructor = false;
   }

   public ParseException(String var1) {
      super(var1, (SimpleNode)null, (CallStack)null);
      this.sourceFile = "<unknown>";
      this.eol = System.getProperty("line.separator", "\n");
      this.specialConstructor = false;
   }

   public String getMessage() {
      return this.getMessage(false);
   }

   public String getMessage(boolean var1) {
      if (!this.specialConstructor) {
         return super.getMessage();
      } else {
         String var2 = "";
         int var3 = 0;

         for(int var4 = 0; var4 < this.expectedTokenSequences.length; ++var4) {
            if (var3 < this.expectedTokenSequences[var4].length) {
               var3 = this.expectedTokenSequences[var4].length;
            }

            for(int var5 = 0; var5 < this.expectedTokenSequences[var4].length; ++var5) {
               var2 = var2 + this.tokenImage[this.expectedTokenSequences[var4][var5]] + " ";
            }

            if (this.expectedTokenSequences[var4][this.expectedTokenSequences[var4].length - 1] != 0) {
               var2 = var2 + "...";
            }

            var2 = var2 + this.eol + "    ";
         }

         String var8 = "In file: " + this.sourceFile + " Encountered \"";
         Token var6 = this.currentToken.next;

         for(int var7 = 0; var7 < var3; ++var7) {
            if (var7 != 0) {
               var8 = var8 + " ";
            }

            if (var6.kind == 0) {
               var8 = var8 + this.tokenImage[0];
               break;
            }

            var8 = var8 + this.add_escapes(var6.image);
            var6 = var6.next;
         }

         var8 = var8 + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn + "." + this.eol;
         if (var1) {
            if (this.expectedTokenSequences.length == 1) {
               var8 = var8 + "Was expecting:" + this.eol + "    ";
            } else {
               var8 = var8 + "Was expecting one of:" + this.eol + "    ";
            }

            var8 = var8 + var2;
         }

         return var8;
      }
   }

   protected String add_escapes(String var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         switch(var1.charAt(var3)) {
         case '\u0000':
            break;
         case '\b':
            var2.append("\\b");
            break;
         case '\t':
            var2.append("\\t");
            break;
         case '\n':
            var2.append("\\n");
            break;
         case '\f':
            var2.append("\\f");
            break;
         case '\r':
            var2.append("\\r");
            break;
         case '"':
            var2.append("\\\"");
            break;
         case '\'':
            var2.append("\\'");
            break;
         case '\\':
            var2.append("\\\\");
            break;
         default:
            char var4;
            if ((var4 = var1.charAt(var3)) >= ' ' && var4 <= '~') {
               var2.append(var4);
            } else {
               String var5 = "0000" + Integer.toString(var4, 16);
               var2.append("\\u" + var5.substring(var5.length() - 4, var5.length()));
            }
         }
      }

      return var2.toString();
   }

   public int getErrorLineNumber() {
      return this.currentToken.next.beginLine;
   }

   public String getErrorText() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.expectedTokenSequences.length; ++var2) {
         if (var1 < this.expectedTokenSequences[var2].length) {
            var1 = this.expectedTokenSequences[var2].length;
         }
      }

      String var3 = "";
      Token var4 = this.currentToken.next;

      for(int var5 = 0; var5 < var1; ++var5) {
         if (var5 != 0) {
            var3 = var3 + " ";
         }

         if (var4.kind == 0) {
            var3 = var3 + this.tokenImage[0];
            break;
         }

         var3 = var3 + this.add_escapes(var4.image);
         var4 = var4.next;
      }

      return var3;
   }

   public String toString() {
      return this.getMessage();
   }
}
