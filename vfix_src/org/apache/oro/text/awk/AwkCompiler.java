package org.apache.oro.text.awk;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;

public final class AwkCompiler implements PatternCompiler {
   public static final int DEFAULT_MASK = 0;
   public static final int CASE_INSENSITIVE_MASK = 1;
   public static final int MULTILINE_MASK = 2;
   static final char _END_OF_INPUT = '\uffff';
   private boolean __inCharacterClass;
   private boolean __caseSensitive;
   private boolean __multiline;
   private boolean __beginAnchor;
   private boolean __endAnchor;
   private char __lookahead;
   private int __position;
   private int __bytesRead;
   private int __expressionLength;
   private char[] __regularExpression;
   private int __openParen;
   private int __closeParen;

   private static boolean __isMetachar(char var0) {
      return var0 == '*' || var0 == '?' || var0 == '+' || var0 == '[' || var0 == ']' || var0 == '(' || var0 == ')' || var0 == '|' || var0 == '.';
   }

   static boolean _isWordCharacter(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z' || var0 >= '0' && var0 <= '9' || var0 == '_';
   }

   static boolean _isLowerCase(char var0) {
      return var0 >= 'a' && var0 <= 'z';
   }

   static boolean _isUpperCase(char var0) {
      return var0 >= 'A' && var0 <= 'Z';
   }

   static char _toggleCase(char var0) {
      if (_isUpperCase(var0)) {
         return (char)(var0 + 32);
      } else {
         return _isLowerCase(var0) ? (char)(var0 - 32) : var0;
      }
   }

   private void __match(char var1) throws MalformedPatternException {
      if (var1 == this.__lookahead) {
         if (this.__bytesRead < this.__expressionLength) {
            this.__lookahead = this.__regularExpression[this.__bytesRead++];
         } else {
            this.__lookahead = '\uffff';
         }

      } else {
         throw new MalformedPatternException("token: " + var1 + " does not match lookahead: " + this.__lookahead + " at position: " + this.__bytesRead);
      }
   }

   private void __putback() {
      if (this.__lookahead != '\uffff') {
         --this.__bytesRead;
      }

      this.__lookahead = this.__regularExpression[this.__bytesRead - 1];
   }

   private SyntaxNode __regex() throws MalformedPatternException {
      SyntaxNode var1 = this.__branch();
      if (this.__lookahead == '|') {
         this.__match('|');
         return new OrNode(var1, this.__regex());
      } else {
         return var1;
      }
   }

   private SyntaxNode __branch() throws MalformedPatternException {
      SyntaxNode var1 = this.__piece();
      if (this.__lookahead == ')') {
         if (this.__openParen > this.__closeParen) {
            return var1;
         } else {
            throw new MalformedPatternException("Parse error: close parenthesis without matching open parenthesis at position " + this.__bytesRead);
         }
      } else if (this.__lookahead != '|' && this.__lookahead != '\uffff') {
         CatNode var2;
         CatNode var3 = var2 = new CatNode();
         var2._left = var1;

         while(true) {
            var1 = this.__piece();
            if (this.__lookahead == ')') {
               if (this.__openParen <= this.__closeParen) {
                  throw new MalformedPatternException("Parse error: close parenthesis without matching open parenthesis at position " + this.__bytesRead);
               }

               var2._right = var1;
               break;
            }

            if (this.__lookahead == '|' || this.__lookahead == '\uffff') {
               var2._right = var1;
               break;
            }

            var2._right = new CatNode();
            var2 = (CatNode)var2._right;
            var2._left = var1;
         }

         return var3;
      } else {
         return var1;
      }
   }

   private SyntaxNode __piece() throws MalformedPatternException {
      SyntaxNode var1 = this.__atom();
      switch(this.__lookahead) {
      case '*':
         this.__match('*');
         return new StarNode(var1);
      case '+':
         this.__match('+');
         return new PlusNode(var1);
      case '?':
         this.__match('?');
         return new QuestionNode(var1);
      case '{':
         return this.__repetition(var1);
      default:
         return var1;
      }
   }

   private int __parseUnsignedInteger(int var1, int var2, int var3) throws MalformedPatternException {
      int var4 = 0;

      StringBuffer var5;
      for(var5 = new StringBuffer(4); Character.digit(this.__lookahead, var1) != -1 && var4 < var3; ++var4) {
         var5.append(this.__lookahead);
         this.__match(this.__lookahead);
      }

      if (var4 >= var2 && var4 <= var3) {
         try {
            int var6 = Integer.parseInt(var5.toString(), var1);
            return var6;
         } catch (NumberFormatException var8) {
            throw new MalformedPatternException("Parse error: numeric value at position " + this.__bytesRead + " is invalid");
         }
      } else {
         throw new MalformedPatternException("Parse error: unexpected number of digits at position " + this.__bytesRead);
      }
   }

   private SyntaxNode __repetition(SyntaxNode var1) throws MalformedPatternException {
      CatNode var2 = null;
      this.__match('{');
      int var3 = this.__parseUnsignedInteger(10, 1, Integer.MAX_VALUE);
      int[] var4 = new int[]{this.__position};
      CatNode var5;
      if (this.__lookahead == '}') {
         this.__match('}');
         if (var3 == 0) {
            throw new MalformedPatternException("Parse error: Superfluous interval specified at position " + this.__bytesRead + ".  Number of occurences was set to zero.");
         }

         if (var3 == 1) {
            return var1;
         }

         var2 = var5 = new CatNode();
         var5._left = var1;

         while(true) {
            --var3;
            if (var3 <= 1) {
               var5._right = var1._clone(var4);
               break;
            }

            var1 = var1._clone(var4);
            var5._right = new CatNode();
            var5 = (CatNode)var5._right;
            var5._left = var1;
         }
      } else {
         if (this.__lookahead != ',') {
            throw new MalformedPatternException("Parse error: unexpected character " + this.__lookahead + " in interval at position " + this.__bytesRead);
         }

         this.__match(',');
         if (this.__lookahead == '}') {
            this.__match('}');
            if (var3 == 0) {
               return new StarNode(var1);
            }

            if (var3 == 1) {
               return new PlusNode(var1);
            }

            var2 = var5 = new CatNode();
            var5._left = var1;

            while(true) {
               --var3;
               if (var3 <= 0) {
                  var5._right = new StarNode(var1._clone(var4));
                  break;
               }

               var1 = var1._clone(var4);
               var5._right = new CatNode();
               var5 = (CatNode)var5._right;
               var5._left = var1;
            }
         } else {
            int var6 = this.__parseUnsignedInteger(10, 1, Integer.MAX_VALUE);
            this.__match('}');
            if (var6 < var3) {
               throw new MalformedPatternException("Parse error: invalid interval; " + var6 + " is less than " + var3 + " at position " + this.__bytesRead);
            }

            if (var6 == 0) {
               throw new MalformedPatternException("Parse error: Superfluous interval specified at position " + this.__bytesRead + ".  Number of occurences was set to zero.");
            }

            Object var8;
            if (var3 == 0) {
               if (var6 == 1) {
                  return new QuestionNode(var1);
               }

               var2 = var5 = new CatNode();
               var8 = new QuestionNode(var1);
               var5._left = (SyntaxNode)var8;

               while(true) {
                  --var6;
                  if (var6 <= 1) {
                     var5._right = ((SyntaxNode)var8)._clone(var4);
                     break;
                  }

                  var8 = ((SyntaxNode)var8)._clone(var4);
                  var5._right = new CatNode();
                  var5 = (CatNode)var5._right;
                  var5._left = (SyntaxNode)var8;
               }
            } else if (var3 == var6) {
               if (var3 == 1) {
                  return var1;
               }

               var2 = var5 = new CatNode();
               var5._left = var1;

               while(true) {
                  --var3;
                  if (var3 <= 1) {
                     var5._right = var1._clone(var4);
                     break;
                  }

                  var1 = var1._clone(var4);
                  var5._right = new CatNode();
                  var5 = (CatNode)var5._right;
                  var5._left = var1;
               }
            } else {
               var2 = var5 = new CatNode();
               var5._left = var1;

               int var7;
               for(var7 = 1; var7 < var3; ++var7) {
                  var1 = var1._clone(var4);
                  var5._right = new CatNode();
                  var5 = (CatNode)var5._right;
                  var5._left = var1;
               }

               var8 = new QuestionNode(var1._clone(var4));
               var7 = var6 - var3;
               if (var7 == 1) {
                  var5._right = (SyntaxNode)var8;
               } else {
                  var5._right = new CatNode();
                  var5 = (CatNode)var5._right;
                  var5._left = (SyntaxNode)var8;

                  while(true) {
                     --var7;
                     if (var7 <= 1) {
                        var5._right = ((SyntaxNode)var8)._clone(var4);
                        break;
                     }

                     var8 = ((SyntaxNode)var8)._clone(var4);
                     var5._right = new CatNode();
                     var5 = (CatNode)var5._right;
                     var5._left = (SyntaxNode)var8;
                  }
               }
            }
         }
      }

      this.__position = var4[0];
      return var2;
   }

   private SyntaxNode __backslashToken() throws MalformedPatternException {
      this.__match('\\');
      Object var1;
      if (this.__lookahead == 'x') {
         this.__match('x');
         var1 = this._newTokenNode((char)this.__parseUnsignedInteger(16, 2, 2), this.__position++);
      } else {
         char var2;
         if (this.__lookahead == 'c') {
            this.__match('c');
            var2 = Character.toUpperCase(this.__lookahead);
            var2 = (char)(var2 > '?' ? var2 - 64 : var2 + 64);
            var1 = new TokenNode(var2, this.__position++);
            this.__match(this.__lookahead);
         } else if (this.__lookahead >= '0' && this.__lookahead <= '9') {
            this.__match(this.__lookahead);
            int var3;
            if (this.__lookahead >= '0' && this.__lookahead <= '9') {
               this.__putback();
               var3 = this.__parseUnsignedInteger(10, 2, 3);
               var3 = Integer.parseInt(Integer.toString(var3), 8);
               var1 = this._newTokenNode((char)var3, this.__position++);
            } else {
               this.__putback();
               if (this.__lookahead == '0') {
                  this.__match('0');
                  var1 = new TokenNode('\u0000', this.__position++);
               } else {
                  var3 = Character.digit(this.__lookahead, 10);
                  var1 = this._newTokenNode(this.__lookahead, this.__position++);
                  this.__match(this.__lookahead);
               }
            }
         } else if (this.__lookahead == 'b') {
            var1 = new TokenNode('\b', this.__position++);
            this.__match('b');
         } else {
            var2 = this.__lookahead;
            switch(this.__lookahead) {
            case 'f':
               var2 = '\f';
               break;
            case 'n':
               var2 = '\n';
               break;
            case 'r':
               var2 = '\r';
               break;
            case 't':
               var2 = '\t';
            }

            CharacterClassNode var4;
            NegativeCharacterClassNode var5;
            switch(var2) {
            case 'D':
               var5 = new NegativeCharacterClassNode(this.__position++);
               var5._addTokenRange(48, 57);
               var1 = var5;
               break;
            case 'S':
               var5 = new NegativeCharacterClassNode(this.__position++);
               var5._addToken(32);
               var5._addToken(12);
               var5._addToken(10);
               var5._addToken(13);
               var5._addToken(9);
               var1 = var5;
               break;
            case 'W':
               var5 = new NegativeCharacterClassNode(this.__position++);
               var5._addTokenRange(48, 57);
               var5._addTokenRange(97, 122);
               var5._addTokenRange(65, 90);
               var5._addToken(95);
               var1 = var5;
               break;
            case 'd':
               var4 = new CharacterClassNode(this.__position++);
               var4._addTokenRange(48, 57);
               var1 = var4;
               break;
            case 's':
               var4 = new CharacterClassNode(this.__position++);
               var4._addToken(32);
               var4._addToken(12);
               var4._addToken(10);
               var4._addToken(13);
               var4._addToken(9);
               var1 = var4;
               break;
            case 'w':
               var4 = new CharacterClassNode(this.__position++);
               var4._addTokenRange(48, 57);
               var4._addTokenRange(97, 122);
               var4._addTokenRange(65, 90);
               var4._addToken(95);
               var1 = var4;
               break;
            default:
               var1 = this._newTokenNode(var2, this.__position++);
            }

            this.__match(this.__lookahead);
         }
      }

      return (SyntaxNode)var1;
   }

   private SyntaxNode __atom() throws MalformedPatternException {
      Object var1;
      if (this.__lookahead == '(') {
         this.__match('(');
         ++this.__openParen;
         var1 = this.__regex();
         this.__match(')');
         ++this.__closeParen;
      } else if (this.__lookahead == '[') {
         var1 = this.__characterClass();
      } else if (this.__lookahead == '.') {
         this.__match('.');
         NegativeCharacterClassNode var2 = new NegativeCharacterClassNode(this.__position++);
         if (this.__multiline) {
            var2._addToken(10);
         }

         var1 = var2;
      } else if (this.__lookahead == '\\') {
         var1 = this.__backslashToken();
      } else {
         if (__isMetachar(this.__lookahead)) {
            throw new MalformedPatternException("Parse error: unexpected character " + this.__lookahead + " at position " + this.__bytesRead);
         }

         var1 = this._newTokenNode(this.__lookahead, this.__position++);
         this.__match(this.__lookahead);
      }

      return (SyntaxNode)var1;
   }

   private SyntaxNode __characterClass() throws MalformedPatternException {
      this.__match('[');
      this.__inCharacterClass = true;
      Object var1;
      if (this.__lookahead == '^') {
         this.__match('^');
         var1 = new NegativeCharacterClassNode(this.__position++);
      } else {
         var1 = new CharacterClassNode(this.__position++);
      }

      label64:
      while(this.__lookahead != ']' && this.__lookahead != '\uffff') {
         SyntaxNode var2;
         char var3;
         char var5;
         if (this.__lookahead == '\\') {
            var2 = this.__backslashToken();
            --this.__position;
            if (!(var2 instanceof TokenNode)) {
               CharacterClassNode var4 = (CharacterClassNode)var2;
               var5 = 0;

               while(true) {
                  if (var5 >= 256) {
                     continue label64;
                  }

                  if (var4._matches(var5)) {
                     ((CharacterClassNode)var1)._addToken(var5);
                  }

                  ++var5;
               }
            }

            var3 = ((TokenNode)var2)._token;
            ((CharacterClassNode)var1)._addToken(var3);
            if (!this.__caseSensitive) {
               ((CharacterClassNode)var1)._addToken(_toggleCase(var3));
            }
         } else {
            var3 = this.__lookahead;
            ((CharacterClassNode)var1)._addToken(this.__lookahead);
            if (!this.__caseSensitive) {
               ((CharacterClassNode)var1)._addToken(_toggleCase(this.__lookahead));
            }

            this.__match(this.__lookahead);
         }

         if (this.__lookahead == '-') {
            this.__match('-');
            if (this.__lookahead == ']') {
               ((CharacterClassNode)var1)._addToken(45);
               break;
            }

            if (this.__lookahead == '\\') {
               var2 = this.__backslashToken();
               --this.__position;
               if (!(var2 instanceof TokenNode)) {
                  throw new MalformedPatternException("Parse error: invalid range specified at position " + this.__bytesRead);
               }

               var5 = ((TokenNode)var2)._token;
            } else {
               var5 = this.__lookahead;
               this.__match(this.__lookahead);
            }

            if (var5 < var3) {
               throw new MalformedPatternException("Parse error: invalid range specified at position " + this.__bytesRead);
            }

            ((CharacterClassNode)var1)._addTokenRange(var3 + 1, var5);
            if (!this.__caseSensitive) {
               ((CharacterClassNode)var1)._addTokenRange(_toggleCase((char)(var3 + 1)), _toggleCase(var5));
            }
         }
      }

      this.__match(']');
      this.__inCharacterClass = false;
      return (SyntaxNode)var1;
   }

   SyntaxNode _newTokenNode(char var1, int var2) {
      if (this.__inCharacterClass || this.__caseSensitive || !_isUpperCase(var1) && !_isLowerCase(var1)) {
         return new TokenNode(var1, var2);
      } else {
         CharacterClassNode var3 = new CharacterClassNode(var2);
         var3._addToken(var1);
         var3._addToken(_toggleCase(var1));
         return var3;
      }
   }

   SyntaxTree _parse(char[] var1) throws MalformedPatternException {
      this.__openParen = this.__closeParen = 0;
      this.__regularExpression = var1;
      this.__bytesRead = 0;
      this.__expressionLength = var1.length;
      this.__inCharacterClass = false;
      this.__position = 0;
      this.__match(this.__lookahead);
      if (this.__lookahead == '^') {
         this.__beginAnchor = true;
         this.__match(this.__lookahead);
      }

      if (this.__expressionLength > 0 && var1[this.__expressionLength - 1] == '$') {
         --this.__expressionLength;
         this.__endAnchor = true;
      }

      SyntaxTree var3;
      if (this.__expressionLength <= 1 && (this.__expressionLength != 1 || this.__beginAnchor)) {
         var3 = new SyntaxTree(new TokenNode('Ā', 0), 1);
      } else {
         CatNode var2 = new CatNode();
         var2._left = this.__regex();
         var2._right = new TokenNode('Ā', this.__position++);
         var3 = new SyntaxTree(var2, this.__position);
      }

      var3._computeFollowPositions();
      return var3;
   }

   public Pattern compile(char[] var1, int var2) throws MalformedPatternException {
      this.__beginAnchor = this.__endAnchor = false;
      this.__caseSensitive = (var2 & 1) == 0;
      this.__multiline = (var2 & 2) != 0;
      SyntaxTree var3 = this._parse(var1);
      AwkPattern var4 = new AwkPattern(new String(var1), var3);
      var4._options = var2;
      var4._hasBeginAnchor = this.__beginAnchor;
      var4._hasEndAnchor = this.__endAnchor;
      return var4;
   }

   public Pattern compile(String var1, int var2) throws MalformedPatternException {
      this.__beginAnchor = this.__endAnchor = false;
      this.__caseSensitive = (var2 & 1) == 0;
      this.__multiline = (var2 & 2) != 0;
      SyntaxTree var3 = this._parse(var1.toCharArray());
      AwkPattern var4 = new AwkPattern(var1, var3);
      var4._options = var2;
      var4._hasBeginAnchor = this.__beginAnchor;
      var4._hasEndAnchor = this.__endAnchor;
      return var4;
   }

   public Pattern compile(char[] var1) throws MalformedPatternException {
      return this.compile((char[])var1, 0);
   }

   public Pattern compile(String var1) throws MalformedPatternException {
      return this.compile((String)var1, 0);
   }
}
