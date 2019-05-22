package org.apache.oro.text;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;

public final class GlobCompiler implements PatternCompiler {
   public static final int DEFAULT_MASK = 0;
   public static final int CASE_INSENSITIVE_MASK = 1;
   public static final int STAR_CANNOT_MATCH_NULL_MASK = 2;
   public static final int QUESTION_MATCHES_ZERO_OR_ONE_MASK = 4;
   public static final int READ_ONLY_MASK = 8;
   private Perl5Compiler __perl5Compiler = new Perl5Compiler();

   private static boolean __isPerl5MetaCharacter(char var0) {
      return var0 == '*' || var0 == '?' || var0 == '+' || var0 == '[' || var0 == ']' || var0 == '(' || var0 == ')' || var0 == '|' || var0 == '^' || var0 == '$' || var0 == '.' || var0 == '{' || var0 == '}' || var0 == '\\';
   }

   private static boolean __isGlobMetaCharacter(char var0) {
      return var0 == '*' || var0 == '?' || var0 == '[' || var0 == ']';
   }

   public static String globToPerl5(char[] var0, int var1) {
      boolean var2 = false;
      StringBuffer var3 = new StringBuffer(2 * var0.length);
      boolean var4 = false;
      boolean var5 = (var1 & 4) != 0;
      var2 = (var1 & 2) != 0;

      for(int var6 = 0; var6 < var0.length; ++var6) {
         switch(var0[var6]) {
         case '*':
            if (var4) {
               var3.append('*');
            } else if (var2) {
               var3.append(".+");
            } else {
               var3.append(".*");
            }
            break;
         case '?':
            if (var4) {
               var3.append('?');
            } else if (var5) {
               var3.append(".?");
            } else {
               var3.append('.');
            }
            break;
         case '[':
            var4 = true;
            var3.append(var0[var6]);
            if (var6 + 1 < var0.length) {
               switch(var0[var6 + 1]) {
               case '!':
               case '^':
                  var3.append('^');
                  ++var6;
                  break;
               case ']':
                  var3.append(']');
                  ++var6;
               }
            }
            break;
         case '\\':
            var3.append('\\');
            if (var6 == var0.length - 1) {
               var3.append('\\');
            } else if (__isGlobMetaCharacter(var0[var6 + 1])) {
               ++var6;
               var3.append(var0[var6]);
            } else {
               var3.append('\\');
            }
            break;
         case ']':
            var4 = false;
            var3.append(var0[var6]);
            break;
         default:
            if (!var4 && __isPerl5MetaCharacter(var0[var6])) {
               var3.append('\\');
            }

            var3.append(var0[var6]);
         }
      }

      return var3.toString();
   }

   public Pattern compile(char[] var1, int var2) throws MalformedPatternException {
      int var3 = 0;
      if ((var2 & 1) != 0) {
         var3 |= 1;
      }

      if ((var2 & 8) != 0) {
         var3 |= 32768;
      }

      return this.__perl5Compiler.compile(globToPerl5(var1, var2), var3);
   }

   public Pattern compile(char[] var1) throws MalformedPatternException {
      return this.compile((char[])var1, 0);
   }

   public Pattern compile(String var1) throws MalformedPatternException {
      return this.compile((char[])var1.toCharArray(), 0);
   }

   public Pattern compile(String var1, int var2) throws MalformedPatternException {
      return this.compile(var1.toCharArray(), var2);
   }
}
