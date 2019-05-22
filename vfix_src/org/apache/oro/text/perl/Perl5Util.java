package org.apache.oro.text.perl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import org.apache.oro.text.PatternCache;
import org.apache.oro.text.PatternCacheLRU;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.apache.oro.util.Cache;
import org.apache.oro.util.CacheLRU;

public final class Perl5Util implements MatchResult {
   private static final String __matchExpression = "m?(\\W)(.*)\\1([imsx]*)";
   private PatternCache __patternCache;
   private Cache __expressionCache;
   private Perl5Matcher __matcher;
   private Pattern __matchPattern;
   private MatchResult __lastMatch;
   private ArrayList __splitList;
   private Object __originalInput;
   private int __inputBeginOffset;
   private int __inputEndOffset;
   private static final String __nullString = "";
   public static final int SPLIT_ALL = 0;

   public Perl5Util(PatternCache var1) {
      this.__splitList = new ArrayList();
      this.__matcher = new Perl5Matcher();
      this.__patternCache = var1;
      this.__expressionCache = new CacheLRU(var1.capacity());
      this.__compilePatterns();
   }

   public Perl5Util() {
      this(new PatternCacheLRU());
   }

   private void __compilePatterns() {
      Perl5Compiler var1 = new Perl5Compiler();

      try {
         this.__matchPattern = var1.compile((String)"m?(\\W)(.*)\\1([imsx]*)", 16);
      } catch (MalformedPatternException var3) {
         throw new RuntimeException(var3.getMessage());
      }
   }

   private Pattern __parseMatchExpression(String var1) throws MalformedPerl5PatternException {
      Object var2 = this.__expressionCache.getElement(var1);

      try {
         if (var2 != null) {
            return (Pattern)var2;
         }
      } catch (ClassCastException var10) {
      }

      if (!this.__matcher.matches(var1, this.__matchPattern)) {
         throw new MalformedPerl5PatternException("Invalid expression: " + var1);
      } else {
         MatchResult var4 = this.__matcher.getMatch();
         String var5 = var4.group(2);
         int var6 = 0;
         String var7 = var4.group(3);
         if (var7 != null) {
            int var8 = var7.length();

            while(var8-- > 0) {
               switch(var7.charAt(var8)) {
               case 'i':
                  var6 |= 1;
                  break;
               case 'm':
                  var6 |= 8;
                  break;
               case 's':
                  var6 |= 16;
                  break;
               case 'x':
                  var6 |= 32;
                  break;
               default:
                  throw new MalformedPerl5PatternException("Invalid options: " + var7);
               }
            }
         }

         Pattern var9 = this.__patternCache.getPattern(var5, var6);
         this.__expressionCache.addElement(var1, var9);
         return var9;
      }
   }

   public synchronized boolean match(String var1, char[] var2) throws MalformedPerl5PatternException {
      this.__parseMatchExpression(var1);
      boolean var3 = this.__matcher.contains(var2, this.__parseMatchExpression(var1));
      if (var3) {
         this.__lastMatch = this.__matcher.getMatch();
         this.__originalInput = var2;
         this.__inputBeginOffset = 0;
         this.__inputEndOffset = var2.length;
      }

      return var3;
   }

   public synchronized boolean match(String var1, String var2) throws MalformedPerl5PatternException {
      return this.match(var1, var2.toCharArray());
   }

   public synchronized boolean match(String var1, PatternMatcherInput var2) throws MalformedPerl5PatternException {
      boolean var3 = this.__matcher.contains(var2, this.__parseMatchExpression(var1));
      if (var3) {
         this.__lastMatch = this.__matcher.getMatch();
         this.__originalInput = var2.getInput();
         this.__inputBeginOffset = var2.getBeginOffset();
         this.__inputEndOffset = var2.getEndOffset();
      }

      return var3;
   }

   public synchronized MatchResult getMatch() {
      return this.__lastMatch;
   }

   public synchronized int substitute(StringBuffer var1, String var2, String var3) throws MalformedPerl5PatternException {
      Object var4 = this.__expressionCache.getElement(var2);
      ParsedSubstitutionEntry var5;
      int var7;
      if (var4 != null) {
         label124: {
            try {
               var5 = (ParsedSubstitutionEntry)var4;
            } catch (ClassCastException var22) {
               break label124;
            }

            var7 = Util.substitute(var1, this.__matcher, var5._pattern, var5._substitution, (String)var3, var5._numSubstitutions);
            this.__lastMatch = this.__matcher.getMatch();
            return var7;
         }
      }

      char[] var8 = var2.toCharArray();
      if (var8.length >= 4 && var8[0] == 's' && !Character.isLetterOrDigit(var8[1]) && var8[1] != '-') {
         char var9 = var8[1];
         byte var10 = 2;
         int var11 = -1;
         int var12 = -1;
         boolean var13 = false;

         int var14;
         for(var14 = var10; var14 < var8.length; ++var14) {
            if (var8[var14] == '\\') {
               var13 = !var13;
            } else {
               if (var8[var14] == var9 && !var13) {
                  var12 = var14;
                  break;
               }

               if (var13) {
                  var13 = !var13;
               }
            }
         }

         if (var12 != -1 && var12 != var8.length - 1) {
            var13 = false;
            boolean var15 = true;
            StringBuffer var16 = new StringBuffer(var8.length - var12);

            for(var14 = var12 + 1; var14 < var8.length; ++var14) {
               if (var8[var14] == '\\') {
                  var13 = !var13;
                  if (var13 && var14 + 1 < var8.length && var8[var14 + 1] == var9 && var2.lastIndexOf(var9, var8.length - 1) != var14 + 1) {
                     var15 = false;
                     continue;
                  }
               } else {
                  if (var8[var14] == var9 && var15) {
                     var11 = var14;
                     break;
                  }

                  var13 = false;
                  var15 = true;
               }

               var16.append(var8[var14]);
            }

            if (var11 == -1) {
               throw new MalformedPerl5PatternException("Invalid expression: " + var2);
            } else {
               int var17 = 0;
               byte var18 = 1;
               byte var19;
               if (var9 != '\'') {
                  var19 = 0;
               } else {
                  var19 = -1;
               }

               for(var14 = var11 + 1; var14 < var8.length; ++var14) {
                  switch(var8[var14]) {
                  case 'g':
                     var18 = -1;
                     break;
                  case 'h':
                  case 'j':
                  case 'k':
                  case 'l':
                  case 'n':
                  case 'p':
                  case 'q':
                  case 'r':
                  case 't':
                  case 'u':
                  case 'v':
                  case 'w':
                  default:
                     throw new MalformedPerl5PatternException("Invalid option: " + var8[var14]);
                  case 'i':
                     var17 |= 1;
                     break;
                  case 'm':
                     var17 |= 8;
                     break;
                  case 'o':
                     var19 = 1;
                     break;
                  case 's':
                     var17 |= 16;
                     break;
                  case 'x':
                     var17 |= 32;
                  }
               }

               Pattern var20 = this.__patternCache.getPattern(new String(var8, var10, var12 - var10), var17);
               Perl5Substitution var21 = new Perl5Substitution(var16.toString(), var19);
               var5 = new ParsedSubstitutionEntry(var20, var21, var18);
               this.__expressionCache.addElement(var2, var5);
               var7 = Util.substitute(var1, this.__matcher, var20, var21, (String)var3, var18);
               this.__lastMatch = this.__matcher.getMatch();
               return var7;
            }
         } else {
            throw new MalformedPerl5PatternException("Invalid expression: " + var2);
         }
      } else {
         throw new MalformedPerl5PatternException("Invalid expression: " + var2);
      }
   }

   public synchronized String substitute(String var1, String var2) throws MalformedPerl5PatternException {
      StringBuffer var3 = new StringBuffer();
      this.substitute(var3, var1, var2);
      return var3.toString();
   }

   public synchronized void split(Collection var1, String var2, String var3, int var4) throws MalformedPerl5PatternException {
      MatchResult var5 = null;
      Pattern var6 = this.__parseMatchExpression(var2);
      PatternMatcherInput var7 = new PatternMatcherInput(var3);
      int var8 = 0;

      while(true) {
         --var4;
         if (var4 == 0 || !this.__matcher.contains(var7, var6)) {
            this.__splitList.add(var3.substring(var8, var3.length()));

            for(int var12 = this.__splitList.size() - 1; var12 >= 0; --var12) {
               String var13 = (String)this.__splitList.get(var12);
               if (var13.length() != 0) {
                  break;
               }

               this.__splitList.remove(var12);
            }

            var1.addAll(this.__splitList);
            this.__splitList.clear();
            this.__lastMatch = var5;
            return;
         }

         var5 = this.__matcher.getMatch();
         this.__splitList.add(var3.substring(var8, var5.beginOffset(0)));
         int var9;
         if ((var9 = var5.groups()) > 1) {
            for(int var10 = 1; var10 < var9; ++var10) {
               String var11 = var5.group(var10);
               if (var11 != null && var11.length() > 0) {
                  this.__splitList.add(var11);
               }
            }
         }

         var8 = var5.endOffset(0);
      }
   }

   public synchronized void split(Collection var1, String var2, String var3) throws MalformedPerl5PatternException {
      this.split(var1, var2, var3, 0);
   }

   public synchronized void split(Collection var1, String var2) throws MalformedPerl5PatternException {
      this.split(var1, "/\\s+/", var2);
   }

   /** @deprecated */
   public synchronized Vector split(String var1, String var2, int var3) throws MalformedPerl5PatternException {
      Vector var4 = new Vector(20);
      this.split(var4, var1, var2, var3);
      return var4;
   }

   /** @deprecated */
   public synchronized Vector split(String var1, String var2) throws MalformedPerl5PatternException {
      return this.split(var1, var2, 0);
   }

   /** @deprecated */
   public synchronized Vector split(String var1) throws MalformedPerl5PatternException {
      return this.split("/\\s+/", var1);
   }

   public synchronized int length() {
      return this.__lastMatch.length();
   }

   public synchronized int groups() {
      return this.__lastMatch.groups();
   }

   public synchronized String group(int var1) {
      return this.__lastMatch.group(var1);
   }

   public synchronized int begin(int var1) {
      return this.__lastMatch.begin(var1);
   }

   public synchronized int end(int var1) {
      return this.__lastMatch.end(var1);
   }

   public synchronized int beginOffset(int var1) {
      return this.__lastMatch.beginOffset(var1);
   }

   public synchronized int endOffset(int var1) {
      return this.__lastMatch.endOffset(var1);
   }

   public synchronized String toString() {
      return this.__lastMatch == null ? null : this.__lastMatch.toString();
   }

   public synchronized String preMatch() {
      if (this.__originalInput == null) {
         return "";
      } else {
         int var1 = this.__lastMatch.beginOffset(0);
         if (var1 <= 0) {
            return "";
         } else if (this.__originalInput instanceof char[]) {
            char[] var3 = (char[])this.__originalInput;
            if (var1 > var3.length) {
               var1 = var3.length;
            }

            return new String(var3, this.__inputBeginOffset, var1);
         } else if (this.__originalInput instanceof String) {
            String var2 = (String)this.__originalInput;
            if (var1 > var2.length()) {
               var1 = var2.length();
            }

            return var2.substring(this.__inputBeginOffset, var1);
         } else {
            return "";
         }
      }
   }

   public synchronized String postMatch() {
      if (this.__originalInput == null) {
         return "";
      } else {
         int var1 = this.__lastMatch.endOffset(0);
         if (var1 < 0) {
            return "";
         } else if (this.__originalInput instanceof char[]) {
            char[] var3 = (char[])this.__originalInput;
            return var1 >= var3.length ? "" : new String(var3, var1, this.__inputEndOffset - var1);
         } else if (this.__originalInput instanceof String) {
            String var2 = (String)this.__originalInput;
            return var1 >= var2.length() ? "" : var2.substring(var1, this.__inputEndOffset);
         } else {
            return "";
         }
      }
   }

   public synchronized char[] preMatchCharArray() {
      char[] var1 = null;
      if (this.__originalInput == null) {
         return null;
      } else {
         int var2 = this.__lastMatch.beginOffset(0);
         if (var2 <= 0) {
            return null;
         } else {
            if (this.__originalInput instanceof char[]) {
               char[] var3 = (char[])this.__originalInput;
               if (var2 >= var3.length) {
                  var2 = var3.length;
               }

               var1 = new char[var2 - this.__inputBeginOffset];
               System.arraycopy(var3, this.__inputBeginOffset, var1, 0, var1.length);
            } else if (this.__originalInput instanceof String) {
               String var4 = (String)this.__originalInput;
               if (var2 >= var4.length()) {
                  var2 = var4.length();
               }

               var1 = new char[var2 - this.__inputBeginOffset];
               var4.getChars(this.__inputBeginOffset, var2, var1, 0);
            }

            return var1;
         }
      }
   }

   public synchronized char[] postMatchCharArray() {
      char[] var1 = null;
      if (this.__originalInput == null) {
         return null;
      } else {
         int var2 = this.__lastMatch.endOffset(0);
         if (var2 < 0) {
            return null;
         } else {
            if (this.__originalInput instanceof char[]) {
               char[] var3 = (char[])this.__originalInput;
               if (var2 >= var3.length) {
                  return null;
               }

               int var4 = this.__inputEndOffset - var2;
               var1 = new char[var4];
               System.arraycopy(var3, var2, var1, 0, var4);
            } else if (this.__originalInput instanceof String) {
               String var5 = (String)this.__originalInput;
               if (var2 >= this.__inputEndOffset) {
                  return null;
               }

               var1 = new char[this.__inputEndOffset - var2];
               var5.getChars(var2, this.__inputEndOffset, var1, 0);
            }

            return var1;
         }
      }
   }
}
