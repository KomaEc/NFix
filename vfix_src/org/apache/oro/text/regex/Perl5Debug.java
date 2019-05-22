package org.apache.oro.text.regex;

public final class Perl5Debug {
   private Perl5Debug() {
   }

   public static String printProgram(Perl5Pattern var0) {
      char var1 = 27;
      char[] var2 = var0._program;
      int var3 = 1;

      StringBuffer var4;
      for(var4 = new StringBuffer(); var1 != 0; var4.append('\n')) {
         var1 = var2[var3];
         var4.append(var3);
         _printOperator(var2, var3, var4);
         int var5 = OpCode._getNext(var2, var3);
         var3 += OpCode._operandLength[var1];
         var4.append("(" + var5 + ")");
         var3 += 2;
         if (var1 == '\t') {
            var3 += 16;
         } else if (var1 != '#' && var1 != '$') {
            if (var1 == 14) {
               ++var3;
               var4.append(" <");

               while(var2[var3] != '\uffff') {
                  var4.append(var2[var3]);
                  ++var3;
               }

               var4.append(">");
               ++var3;
            }
         } else {
            while(var2[var3] != 0) {
               if (var2[var3] == '%') {
                  var3 += 3;
               } else {
                  var3 += 2;
               }
            }

            ++var3;
         }
      }

      if (var0._startString != null) {
         var4.append("start `" + new String(var0._startString) + "' ");
      }

      if (var0._startClassOffset != -1) {
         var4.append("stclass `");
         _printOperator(var2, var0._startClassOffset, var4);
         var4.append("' ");
      }

      if ((var0._anchor & 3) != 0) {
         var4.append("anchored ");
      }

      if ((var0._anchor & 4) != 0) {
         var4.append("plus ");
      }

      if ((var0._anchor & 8) != 0) {
         var4.append("implicit ");
      }

      if (var0._mustString != null) {
         var4.append("must have \"" + new String(var0._mustString) + "\" back " + var0._back + " ");
      }

      var4.append("minlen " + var0._minLength + '\n');
      return var4.toString();
   }

   static void _printOperator(char[] var0, int var1, StringBuffer var2) {
      String var3 = null;
      var2.append(":");
      switch(var0[var1]) {
      case '\u0000':
         var3 = "END";
         break;
      case '\u0001':
         var3 = "BOL";
         break;
      case '\u0002':
         var3 = "MBOL";
         break;
      case '\u0003':
         var3 = "SBOL";
         break;
      case '\u0004':
         var3 = "EOL";
         break;
      case '\u0005':
         var3 = "MEOL";
         break;
      case '\u0006':
      case '%':
      case '/':
      case '0':
      case '1':
      default:
         var2.append("Operator is unrecognized.  Faulty expression code!");
         break;
      case '\u0007':
         var3 = "ANY";
         break;
      case '\b':
         var3 = "SANY";
         break;
      case '\t':
         var3 = "ANYOF";
         break;
      case '\n':
         var2.append("CURLY {");
         var2.append(OpCode._getArg1(var0, var1));
         var2.append(',');
         var2.append(OpCode._getArg2(var0, var1));
         var2.append('}');
         break;
      case '\u000b':
         var2.append("CURLYX {");
         var2.append(OpCode._getArg1(var0, var1));
         var2.append(',');
         var2.append(OpCode._getArg2(var0, var1));
         var2.append('}');
         break;
      case '\f':
         var3 = "BRANCH";
         break;
      case '\r':
         var3 = "BACK";
         break;
      case '\u000e':
         var3 = "EXACTLY";
         break;
      case '\u000f':
         var3 = "NOTHING";
         break;
      case '\u0010':
         var3 = "STAR";
         break;
      case '\u0011':
         var3 = "PLUS";
         break;
      case '\u0012':
         var3 = "ALNUM";
         break;
      case '\u0013':
         var3 = "NALNUM";
         break;
      case '\u0014':
         var3 = "BOUND";
         break;
      case '\u0015':
         var3 = "NBOUND";
         break;
      case '\u0016':
         var3 = "SPACE";
         break;
      case '\u0017':
         var3 = "NSPACE";
         break;
      case '\u0018':
         var3 = "DIGIT";
         break;
      case '\u0019':
         var3 = "NDIGIT";
         break;
      case '\u001a':
         var2.append("REF");
         var2.append(OpCode._getArg1(var0, var1));
         break;
      case '\u001b':
         var2.append("OPEN");
         var2.append(OpCode._getArg1(var0, var1));
         break;
      case '\u001c':
         var2.append("CLOSE");
         var2.append(OpCode._getArg1(var0, var1));
         break;
      case '\u001d':
         var3 = "MINMOD";
         break;
      case '\u001e':
         var3 = "GBOL";
         break;
      case '\u001f':
         var3 = "IFMATCH";
         break;
      case ' ':
         var3 = "UNLESSM";
         break;
      case '!':
         var3 = "SUCCEED";
         break;
      case '"':
         var3 = "WHILEM";
         break;
      case '#':
         var3 = "ANYOFUN";
         break;
      case '$':
         var3 = "NANYOFUN";
         break;
      case '&':
         var3 = "ALPHA";
         break;
      case '\'':
         var3 = "BLANK";
         break;
      case '(':
         var3 = "CNTRL";
         break;
      case ')':
         var3 = "GRAPH";
         break;
      case '*':
         var3 = "LOWER";
         break;
      case '+':
         var3 = "PRINT";
         break;
      case ',':
         var3 = "PUNCT";
         break;
      case '-':
         var3 = "UPPER";
         break;
      case '.':
         var3 = "XDIGIT";
         break;
      case '2':
         var3 = "ALNUMC";
         break;
      case '3':
         var3 = "ASCII";
      }

      if (var3 != null) {
         var2.append(var3);
      }

   }
}
