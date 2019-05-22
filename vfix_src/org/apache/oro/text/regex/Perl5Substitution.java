package org.apache.oro.text.regex;

public class Perl5Substitution extends StringSubstitution {
   public static final int INTERPOLATE_ALL = 0;
   public static final int INTERPOLATE_NONE = -1;
   private static final int __OPCODE_STORAGE_SIZE = 32;
   private static final int __MAX_GROUPS = 65535;
   static final int _OPCODE_COPY = -1;
   static final int _OPCODE_LOWERCASE_CHAR = -2;
   static final int _OPCODE_UPPERCASE_CHAR = -3;
   static final int _OPCODE_LOWERCASE_MODE = -4;
   static final int _OPCODE_UPPERCASE_MODE = -5;
   static final int _OPCODE_ENDCASE_MODE = -6;
   int _numInterpolations;
   int[] _subOpcodes;
   int _subOpcodesCount;
   char[] _substitutionChars;
   transient String _lastInterpolation;

   private static final boolean __isInterpolationCharacter(char var0) {
      return Character.isDigit(var0) || var0 == '&';
   }

   private void __addElement(int var1) {
      int var2 = this._subOpcodes.length;
      if (this._subOpcodesCount == var2) {
         int[] var3 = new int[var2 + 32];
         System.arraycopy(this._subOpcodes, 0, var3, 0, var2);
         this._subOpcodes = var3;
      }

      this._subOpcodes[this._subOpcodesCount++] = var1;
   }

   private void __parseSubs(String var1) {
      char[] var2 = this._substitutionChars = var1.toCharArray();
      int var3 = var2.length;
      this._subOpcodes = new int[32];
      this._subOpcodesCount = 0;
      int var4 = 0;
      int var5 = -1;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;

      for(int var9 = 0; var9 < var3; ++var9) {
         char var10 = var2[var9];
         int var11 = var9 + 1;
         if (var6) {
            int var12 = Character.digit(var10, 10);
            if (var12 > -1) {
               if (var4 <= 65535) {
                  var4 *= 10;
                  var4 += var12;
               }

               if (var11 == var3) {
                  this.__addElement(var4);
               }
               continue;
            }

            if (var10 == '&' && var2[var9 - 1] == '$') {
               this.__addElement(0);
               var4 = 0;
               var6 = false;
               continue;
            }

            this.__addElement(var4);
            var4 = 0;
            var6 = false;
         }

         if ((var10 == '$' || var10 == '\\') && !var7) {
            if (var5 >= 0) {
               this.__addElement(var9 - var5);
               var5 = -1;
            }

            if (var11 != var3) {
               char var13 = var2[var11];
               if (var10 == '$') {
                  var6 = __isInterpolationCharacter(var13);
               } else if (var10 == '\\') {
                  if (var13 == 'l') {
                     if (!var8) {
                        this.__addElement(-2);
                        ++var9;
                     }
                  } else if (var13 == 'u') {
                     if (!var8) {
                        this.__addElement(-3);
                        ++var9;
                     }
                  } else if (var13 == 'L') {
                     this.__addElement(-4);
                     ++var9;
                     var8 = true;
                  } else if (var13 == 'U') {
                     this.__addElement(-5);
                     ++var9;
                     var8 = true;
                  } else if (var13 == 'E') {
                     this.__addElement(-6);
                     ++var9;
                     var8 = false;
                  } else {
                     var7 = true;
                  }
               }
            }
         } else {
            var7 = false;
            if (var5 < 0) {
               var5 = var9;
               this.__addElement(-1);
               this.__addElement(var9);
            }

            if (var11 == var3) {
               this.__addElement(var11 - var5);
            }
         }
      }

   }

   String _finalInterpolatedSub(MatchResult var1) {
      StringBuffer var2 = new StringBuffer(10);
      this._calcSub(var2, var1);
      return var2.toString();
   }

   void _calcSub(StringBuffer var1, MatchResult var2) {
      int[] var3 = this._subOpcodes;
      int var4 = 0;
      char[] var5 = this._substitutionChars;
      char[] var6 = var2.group(0).toCharArray();
      int var7 = this._subOpcodesCount;

      for(int var8 = 0; var8 < var7; ++var8) {
         int var9 = var3[var8];
         int var10;
         int var11;
         int var13;
         char[] var14;
         if (var9 >= 0 && var9 < var2.groups()) {
            var10 = var2.begin(var9);
            if (var10 < 0) {
               continue;
            }

            var11 = var2.end(var9);
            if (var11 < 0) {
               continue;
            }

            int var12 = var2.length();
            if (var10 >= var12 || var11 > var12 || var10 >= var11) {
               continue;
            }

            var13 = var11 - var10;
            var14 = var6;
         } else {
            if (var9 != -1) {
               if (var9 != -2 && var9 != -3) {
                  if (var9 != -4 && var9 != -5) {
                     if (var9 == -6) {
                        var4 = 0;
                     }
                     continue;
                  }

                  var4 = var9;
                  continue;
               }

               if (var4 != -4 && var4 != -5) {
                  var4 = var9;
               }
               continue;
            }

            ++var8;
            if (var8 >= var7) {
               continue;
            }

            var10 = var3[var8];
            ++var8;
            if (var8 >= var7) {
               continue;
            }

            var13 = var3[var8];
            var14 = var5;
         }

         if (var4 == -2) {
            var1.append(Character.toLowerCase(var14[var10++]));
            --var13;
            var1.append(var14, var10, var13);
            var4 = 0;
         } else if (var4 == -3) {
            var1.append(Character.toUpperCase(var14[var10++]));
            --var13;
            var1.append(var14, var10, var13);
            var4 = 0;
         } else if (var4 == -4) {
            var11 = var10 + var13;

            while(var10 < var11) {
               var1.append(Character.toLowerCase(var14[var10++]));
            }
         } else if (var4 == -5) {
            var11 = var10 + var13;

            while(var10 < var11) {
               var1.append(Character.toUpperCase(var14[var10++]));
            }
         } else {
            var1.append(var14, var10, var13);
         }
      }

   }

   public Perl5Substitution() {
      this("", 0);
   }

   public Perl5Substitution(String var1) {
      this(var1, 0);
   }

   public Perl5Substitution(String var1, int var2) {
      this.setSubstitution(var1, var2);
   }

   public void setSubstitution(String var1) {
      this.setSubstitution(var1, 0);
   }

   public void setSubstitution(String var1, int var2) {
      super.setSubstitution(var1);
      this._numInterpolations = var2;
      if (var2 == -1 || var1.indexOf(36) == -1 && var1.indexOf(92) == -1) {
         this._subOpcodes = null;
      } else {
         this.__parseSubs(var1);
      }

      this._lastInterpolation = null;
   }

   public void appendSubstitution(StringBuffer var1, MatchResult var2, int var3, PatternMatcherInput var4, PatternMatcher var5, Pattern var6) {
      if (this._subOpcodes == null) {
         super.appendSubstitution(var1, var2, var3, var4, var5, var6);
      } else {
         if (this._numInterpolations >= 1 && var3 >= this._numInterpolations) {
            if (var3 == this._numInterpolations) {
               this._lastInterpolation = this._finalInterpolatedSub(var2);
            }

            var1.append(this._lastInterpolation);
         } else {
            this._calcSub(var1, var2);
         }

      }
   }
}
