package org.netbeans.lib.cvsclient.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SimpleStringPattern implements StringPattern {
   private static final char MATCH_EACH = '*';
   private static final char MATCH_ONE = '?';
   private final List subPatterns = new LinkedList();

   public SimpleStringPattern(String var1) {
      this.splitInSubPattern(var1);
   }

   public boolean doesMatch(String var1) {
      int var2 = 0;
      SimpleStringPattern.SubPattern var3 = null;
      Iterator var4 = this.subPatterns.iterator();

      do {
         if (!var4.hasNext()) {
            if (var2 == var1.length()) {
               return true;
            }

            if (var3 == null) {
               return false;
            }

            return var3.checkEnding(var1, var2);
         }

         var3 = (SimpleStringPattern.SubPattern)var4.next();
         var2 = var3.doesMatch(var1, var2);
      } while(var2 >= 0);

      return false;
   }

   private void splitInSubPattern(String var1) {
      char var2 = ' ';
      int var3 = 0;
      int var4 = 0;

      while(var4 >= 0) {
         var3 = var4;
         var4 = var1.indexOf(42, var4);
         String var5;
         if (var4 >= 0) {
            var5 = var1.substring(var3, var4);
            this.addSubPattern(var5, var2);
            var2 = '*';
            ++var4;
         } else {
            var4 = var1.indexOf(63, var3);
            if (var4 >= 0) {
               var5 = var1.substring(var3, var4);
               this.addSubPattern(var5, var2);
               var2 = '?';
               ++var4;
            }
         }
      }

      String var6 = var1.substring(var3);
      this.addSubPattern(var6, var2);
   }

   private void addSubPattern(String var1, char var2) {
      Object var3 = null;
      switch(var2) {
      case '*':
         var3 = new SimpleStringPattern.MatchEachCharPattern(var1);
         break;
      case '?':
         var3 = new SimpleStringPattern.MatchOneCharPattern(var1);
         break;
      default:
         var3 = new SimpleStringPattern.MatchExactSubPattern(var1);
      }

      this.subPatterns.add(var3);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.subPatterns.iterator();

      while(var2.hasNext()) {
         SimpleStringPattern.SubPattern var3 = (SimpleStringPattern.SubPattern)var2.next();
         var1.append(var3.toString());
      }

      return var1.toString();
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof SimpleStringPattern) ? false : this.subPatterns.equals(((SimpleStringPattern)var1).subPatterns);
   }

   public int hashCode() {
      return -this.subPatterns.hashCode();
   }

   public static void main(String[] var0) {
      SimpleStringPattern var1 = new SimpleStringPattern("a*b");
      test(var1, "ab", true);
      test(var1, "aab", true);
      test(var1, "ba", false);
      test(var1, "abc", false);
      var1 = new SimpleStringPattern("*.txt");
      test(var1, "datei.txt", true);
      test(var1, ".txt", true);
      test(var1, "datei.tx", false);
      test(var1, "datei.txt.txt", true);
      var1 = new SimpleStringPattern("datei*1*");
      test(var1, "datei0.txt", false);
      test(var1, "datei1.txt", true);
      test(var1, "datei.tx", false);
      test(var1, "datei1.txt.txt", true);
      var1 = new SimpleStringPattern("Makefile");
      test(var1, "Makefile", true);
      test(var1, "Makefile.mak", false);
      test(var1, "Makefile1", false);
      test(var1, ".Makefile", false);
      test(var1, ".Makefile.", false);
      var1 = new SimpleStringPattern("*~");
      test(var1, "datei~", true);
      test(var1, "datei~1", false);
      test(var1, "datei~1~", true);
      SimpleStringPattern var2 = new SimpleStringPattern("*.class");
      SimpleStringPattern var3 = new SimpleStringPattern("*.class");
      System.err.println(var2 + ".equals(" + var3 + ") = " + var2.equals(var3));
      var2 = new SimpleStringPattern("?.class");
      var3 = new SimpleStringPattern("*.class");
      System.err.println(var2 + ".equals(" + var3 + ") = " + var2.equals(var3));
      var2 = new SimpleStringPattern("*.clazz");
      var3 = new SimpleStringPattern("*.class");
      System.err.println(var2 + ".equals(" + var3 + ") = " + var2.equals(var3));
   }

   private static void test(StringPattern var0, String var1, boolean var2) {
      System.err.print('"' + var0.toString() + '"' + ": " + var1 + " " + var2);
      boolean var3 = var0.doesMatch(var1);
      if (var3 == var2) {
         System.err.println(" proved");
      } else {
         System.err.println(" **denied**");
      }

   }

   private static class MatchOneCharPattern extends SimpleStringPattern.MatchExactSubPattern {
      public MatchOneCharPattern(String var1) {
         super(var1);
      }

      public int doesMatch(String var1, int var2) {
         ++var2;
         return var1.length() < var2 ? -1 : super.doesMatch(var1, var2);
      }

      public String toString() {
         return '?' + this.match;
      }
   }

   private static class MatchEachCharPattern extends SimpleStringPattern.SubPattern {
      public MatchEachCharPattern(String var1) {
         super(var1);
      }

      public int doesMatch(String var1, int var2) {
         int var3 = var1.indexOf(this.match, var2);
         return var3 < 0 ? -1 : var3 + this.match.length();
      }

      public boolean checkEnding(String var1, int var2) {
         return var1.endsWith(this.match);
      }

      public String toString() {
         return '*' + this.match;
      }
   }

   private static class MatchExactSubPattern extends SimpleStringPattern.SubPattern {
      public MatchExactSubPattern(String var1) {
         super(var1);
      }

      public int doesMatch(String var1, int var2) {
         return !var1.startsWith(this.match, var2) ? -1 : var2 + this.match.length();
      }

      public String toString() {
         return this.match;
      }
   }

   private abstract static class SubPattern {
      protected final String match;

      protected SubPattern(String var1) {
         this.match = var1;
      }

      public abstract int doesMatch(String var1, int var2);

      public boolean checkEnding(String var1, int var2) {
         return false;
      }

      public boolean equals(Object var1) {
         return !this.getClass().isInstance(var1) ? false : this.match.equals(((SimpleStringPattern.SubPattern)var1).match);
      }

      public int hashCode() {
         return -this.match.hashCode();
      }
   }
}
