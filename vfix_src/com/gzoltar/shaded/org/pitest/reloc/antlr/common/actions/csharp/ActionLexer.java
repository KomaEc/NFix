package com.gzoltar.shaded.org.pitest.reloc.antlr.common.actions.csharp;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.ActionTransInfo;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.ByteBuffer;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharBuffer;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharScanner;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharStreamException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharStreamIOException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CodeGenerator;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.GrammarAtom;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.InputBuffer;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.LexerSharedInputState;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.NoViableAltForCharException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.RecognitionException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.RuleBlock;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.Token;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStream;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStreamException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStreamIOException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStreamRecognitionException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.Tool;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.impl.BitSet;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.impl.Vector;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;

public class ActionLexer extends CharScanner implements ActionLexerTokenTypes, TokenStream {
   protected RuleBlock currentRule;
   protected CodeGenerator generator;
   protected int lineOffset;
   private Tool antlrTool;
   ActionTransInfo transInfo;
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
   public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
   public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
   public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
   public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
   public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
   public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
   public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
   public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
   public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
   public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
   public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
   public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
   public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
   public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
   public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
   public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
   public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
   public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
   public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
   public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
   public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
   public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
   public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
   public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
   public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
   public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());

   public ActionLexer(String var1, RuleBlock var2, CodeGenerator var3, ActionTransInfo var4) {
      this((Reader)(new StringReader(var1)));
      this.currentRule = var2;
      this.generator = var3;
      this.transInfo = var4;
   }

   public void setLineOffset(int var1) {
      this.setLine(var1);
   }

   public void setTool(Tool var1) {
      this.antlrTool = var1;
   }

   public void reportError(RecognitionException var1) {
      this.antlrTool.error("Syntax error in action: " + var1, this.getFilename(), this.getLine(), this.getColumn());
   }

   public void reportError(String var1) {
      this.antlrTool.error(var1, this.getFilename(), this.getLine(), this.getColumn());
   }

   public void reportWarning(String var1) {
      if (this.getFilename() == null) {
         this.antlrTool.warning(var1);
      } else {
         this.antlrTool.warning(var1, this.getFilename(), this.getLine(), this.getColumn());
      }

   }

   public ActionLexer(InputStream var1) {
      this((InputBuffer)(new ByteBuffer(var1)));
   }

   public ActionLexer(Reader var1) {
      this((InputBuffer)(new CharBuffer(var1)));
   }

   public ActionLexer(InputBuffer var1) {
      this(new LexerSharedInputState(var1));
   }

   public ActionLexer(LexerSharedInputState var1) {
      super(var1);
      this.lineOffset = 0;
      this.caseSensitiveLiterals = true;
      this.setCaseSensitive(true);
      this.literals = new Hashtable();
   }

   public Token nextToken() throws TokenStreamException {
      Token var1 = null;

      while(true) {
         Object var2 = null;
         boolean var3 = false;
         this.resetText();

         try {
            try {
               if (this.LA(1) >= 3 && this.LA(1) <= 255) {
                  this.mACTION(true);
                  var1 = this._returnToken;
               } else {
                  if (this.LA(1) != '\uffff') {
                     throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                  }

                  this.uponEOF();
                  this._returnToken = this.makeToken(1);
               }

               if (this._returnToken != null) {
                  int var7 = this._returnToken.getType();
                  this._returnToken.setType(var7);
                  return this._returnToken;
               }
            } catch (RecognitionException var5) {
               throw new TokenStreamRecognitionException(var5);
            }
         } catch (CharStreamException var6) {
            if (var6 instanceof CharStreamIOException) {
               throw new TokenStreamIOException(((CharStreamIOException)var6).io);
            }

            throw new TokenStreamException(var6.getMessage());
         }
      }
   }

   public final void mACTION(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 4;
      int var5 = 0;

      while(true) {
         switch(this.LA(1)) {
         case '#':
            this.mAST_ITEM(false);
            break;
         case '$':
            this.mTEXT_ITEM(false);
            break;
         default:
            if (!_tokenSet_0.member(this.LA(1))) {
               if (var5 >= 1) {
                  if (var1 && var2 == null && var4 != -1) {
                     var2 = this.makeToken(var4);
                     var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
                  }

                  this._returnToken = var2;
                  return;
               }

               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.mSTUFF(false);
         }

         ++var5;
      }
   }

   protected final void mSTUFF(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 5;
      switch(this.LA(1)) {
      case '\n':
         this.match('\n');
         this.newline();
         break;
      case '"':
         this.mSTRING(false);
         break;
      case '\'':
         this.mCHAR(false);
         break;
      default:
         if (this.LA(1) == '/' && (this.LA(2) == '*' || this.LA(2) == '/')) {
            this.mCOMMENT(false);
         } else if (this.LA(1) == '\r' && this.LA(2) == '\n') {
            this.match("\r\n");
            this.newline();
         } else if (this.LA(1) == '\\' && this.LA(2) == '#') {
            this.match('\\');
            this.match('#');
            this.text.setLength(var3);
            this.text.append("#");
         } else if (this.LA(1) == '/' && _tokenSet_1.member(this.LA(2))) {
            this.match('/');
            this.match(_tokenSet_1);
         } else if (this.LA(1) == '\r') {
            this.match('\r');
            this.newline();
         } else {
            if (!_tokenSet_2.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.match(_tokenSet_2);
         }
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mAST_ITEM(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 6;
      Token var5 = null;
      Token var6 = null;
      Token var7 = null;
      int var8;
      if (this.LA(1) == '#' && this.LA(2) == '(') {
         var8 = this.text.length();
         this.match('#');
         this.text.setLength(var8);
         this.mTREE(true);
         var5 = this._returnToken;
      } else {
         String var9;
         if (this.LA(1) == '#' && _tokenSet_3.member(this.LA(2))) {
            var8 = this.text.length();
            this.match('#');
            this.text.setLength(var8);
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.mWS(false);
               break;
            case '\u000b':
            case '\f':
            case '\u000e':
            case '\u000f':
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0017':
            case '\u0018':
            case '\u0019':
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            case '\u001f':
            case '!':
            case '"':
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '`':
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '_':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            }

            this.mID(true);
            var6 = this._returnToken;
            var9 = var6.getText();
            String var10 = this.generator.mapTreeId(var6.getText(), this.transInfo);
            if (var10 != null && !var9.equals(var10)) {
               this.text.setLength(var3);
               this.text.append(var10);
            } else if (var9.equals("define") || var9.equals("undef") || var9.equals("if") || var9.equals("elif") || var9.equals("else") || var9.equals("endif") || var9.equals("line") || var9.equals("error") || var9.equals("warning") || var9.equals("region") || var9.equals("endregion")) {
               this.text.setLength(var3);
               this.text.append("#" + var9);
            }

            if (_tokenSet_4.member(this.LA(1))) {
               this.mWS(false);
            }

            if (this.LA(1) == '=') {
               this.mVAR_ASSIGN(false);
            }
         } else if (this.LA(1) == '#' && this.LA(2) == '[') {
            var8 = this.text.length();
            this.match('#');
            this.text.setLength(var8);
            this.mAST_CONSTRUCTOR(true);
            var7 = this._returnToken;
         } else {
            if (this.LA(1) != '#' || this.LA(2) != '#') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.match("##");
            if (this.currentRule != null) {
               var9 = this.currentRule.getRuleName() + "_AST";
               this.text.setLength(var3);
               this.text.append(var9);
               if (this.transInfo != null) {
                  this.transInfo.refRuleRoot = var9;
               }
            } else {
               this.reportWarning("\"##\" not valid in this context");
               this.text.setLength(var3);
               this.text.append("##");
            }

            if (_tokenSet_4.member(this.LA(1))) {
               this.mWS(false);
            }

            if (this.LA(1) == '=') {
               this.mVAR_ASSIGN(false);
            }
         }
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mTEXT_ITEM(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 7;
      Token var5 = null;
      Token var6 = null;
      Token var7 = null;
      Token var8 = null;
      Token var9 = null;
      Token var10 = null;
      String var11;
      String var12;
      if (this.LA(1) == '$' && this.LA(2) == 'F' && this.LA(3) == 'O') {
         this.match("$FOLLOW");
         if (_tokenSet_5.member(this.LA(1)) && _tokenSet_6.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.mWS(false);
            case '(':
               this.match('(');
               this.mTEXT_ARG(true);
               var9 = this._returnToken;
               this.match(')');
               break;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }

         var11 = this.currentRule.getRuleName();
         if (var9 != null) {
            var11 = var9.getText();
         }

         var12 = this.generator.getFOLLOWBitSet(var11, 1);
         if (var12 == null) {
            this.reportError("$FOLLOW(" + var11 + ")" + ": unknown rule or bad lookahead computation");
         } else {
            this.text.setLength(var3);
            this.text.append(var12);
         }
      } else if (this.LA(1) == '$' && this.LA(2) == 'F' && this.LA(3) == 'I') {
         this.match("$FIRST");
         if (_tokenSet_5.member(this.LA(1)) && _tokenSet_6.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.mWS(false);
            case '(':
               this.match('(');
               this.mTEXT_ARG(true);
               var10 = this._returnToken;
               this.match(')');
               break;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }

         var11 = this.currentRule.getRuleName();
         if (var10 != null) {
            var11 = var10.getText();
         }

         var12 = this.generator.getFIRSTBitSet(var11, 1);
         if (var12 == null) {
            this.reportError("$FIRST(" + var11 + ")" + ": unknown rule or bad lookahead computation");
         } else {
            this.text.setLength(var3);
            this.text.append(var12);
         }
      } else if (this.LA(1) == '$' && this.LA(2) == 'a') {
         this.match("$append");
         switch(this.LA(1)) {
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            this.mWS(false);
         case '(':
            this.match('(');
            this.mTEXT_ARG(true);
            var5 = this._returnToken;
            this.match(')');
            var11 = "text.Append(" + var5.getText() + ")";
            this.text.setLength(var3);
            this.text.append(var11);
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      } else if (this.LA(1) == '$' && this.LA(2) == 's') {
         this.match("$set");
         if (this.LA(1) == 'T' && this.LA(2) == 'e') {
            this.match("Text");
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.mWS(false);
            case '(':
               this.match('(');
               this.mTEXT_ARG(true);
               var6 = this._returnToken;
               this.match(')');
               var11 = "text.Length = _begin; text.Append(" + var6.getText() + ")";
               this.text.setLength(var3);
               this.text.append(var11);
               break;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         } else if (this.LA(1) == 'T' && this.LA(2) == 'o') {
            this.match("Token");
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.mWS(false);
            case '(':
               this.match('(');
               this.mTEXT_ARG(true);
               var7 = this._returnToken;
               this.match(')');
               var11 = "_token = " + var7.getText();
               this.text.setLength(var3);
               this.text.append(var11);
               break;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         } else {
            if (this.LA(1) != 'T' || this.LA(2) != 'y') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.match("Type");
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.mWS(false);
            case '(':
               this.match('(');
               this.mTEXT_ARG(true);
               var8 = this._returnToken;
               this.match(')');
               var11 = "_ttype = " + var8.getText();
               this.text.setLength(var3);
               this.text.append(var11);
               break;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }
      } else {
         if (this.LA(1) != '$' || this.LA(2) != 'g') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.match("$getText");
         this.text.setLength(var3);
         this.text.append("text.ToString(_begin, text.Length-_begin)");
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mCOMMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 19;
      if (this.LA(1) == '/' && this.LA(2) == '/') {
         this.mSL_COMMENT(false);
      } else {
         if (this.LA(1) != '/' || this.LA(2) != '*') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.mML_COMMENT(false);
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mSTRING(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 23;
      this.match('"');

      while(true) {
         while(this.LA(1) != '\\') {
            if (!_tokenSet_7.member(this.LA(1))) {
               this.match('"');
               if (var1 && var2 == null && var4 != -1) {
                  var2 = this.makeToken(var4);
                  var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
               }

               this._returnToken = var2;
               return;
            }

            this.matchNot('"');
         }

         this.mESC(false);
      }
   }

   protected final void mCHAR(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 22;
      this.match('\'');
      if (this.LA(1) == '\\') {
         this.mESC(false);
      } else {
         if (!_tokenSet_8.member(this.LA(1))) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.matchNot('\'');
      }

      this.match('\'');
      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mTREE(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 8;
      Token var5 = null;
      Token var6 = null;
      new StringBuffer();
      boolean var8 = false;
      Vector var9 = new Vector(10);
      int var10 = this.text.length();
      this.match('(');
      this.text.setLength(var10);
      switch(this.LA(1)) {
      case '\t':
      case '\n':
      case '\r':
      case ' ':
         var10 = this.text.length();
         this.mWS(false);
         this.text.setLength(var10);
      case '"':
      case '#':
      case '(':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '[':
      case '_':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
         var10 = this.text.length();
         this.mTREE_ELEMENT(true);
         this.text.setLength(var10);
         var5 = this._returnToken;
         var9.appendElement(this.generator.processStringForASTConstructor(var5.getText()));
         switch(this.LA(1)) {
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            var10 = this.text.length();
            this.mWS(false);
            this.text.setLength(var10);
         case ')':
         case ',':
            while(this.LA(1) == ',') {
               var10 = this.text.length();
               this.match(',');
               this.text.setLength(var10);
               switch(this.LA(1)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  var10 = this.text.length();
                  this.mWS(false);
                  this.text.setLength(var10);
                  break;
               case '\u000b':
               case '\f':
               case '\u000e':
               case '\u000f':
               case '\u0010':
               case '\u0011':
               case '\u0012':
               case '\u0013':
               case '\u0014':
               case '\u0015':
               case '\u0016':
               case '\u0017':
               case '\u0018':
               case '\u0019':
               case '\u001a':
               case '\u001b':
               case '\u001c':
               case '\u001d':
               case '\u001e':
               case '\u001f':
               case '!':
               case '$':
               case '%':
               case '&':
               case '\'':
               case ')':
               case '*':
               case '+':
               case ',':
               case '-':
               case '.':
               case '/':
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
               case '\\':
               case ']':
               case '^':
               case '`':
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               case '"':
               case '#':
               case '(':
               case 'A':
               case 'B':
               case 'C':
               case 'D':
               case 'E':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'S':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case '_':
               case 'a':
               case 'b':
               case 'c':
               case 'd':
               case 'e':
               case 'f':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'n':
               case 'o':
               case 'p':
               case 'q':
               case 'r':
               case 's':
               case 't':
               case 'u':
               case 'v':
               case 'w':
               case 'x':
               case 'y':
               case 'z':
               }

               var10 = this.text.length();
               this.mTREE_ELEMENT(true);
               this.text.setLength(var10);
               var6 = this._returnToken;
               var9.appendElement(this.generator.processStringForASTConstructor(var6.getText()));
               switch(this.LA(1)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  var10 = this.text.length();
                  this.mWS(false);
                  this.text.setLength(var10);
               case ')':
               case ',':
                  break;
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            }

            this.text.setLength(var3);
            this.text.append(this.generator.getASTCreateString(var9));
            var10 = this.text.length();
            this.match(')');
            this.text.setLength(var10);
            if (var1 && var2 == null && var4 != -1) {
               var2 = this.makeToken(var4);
               var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
            }

            this._returnToken = var2;
            return;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      case '\u000b':
      case '\f':
      case '\u000e':
      case '\u000f':
      case '\u0010':
      case '\u0011':
      case '\u0012':
      case '\u0013':
      case '\u0014':
      case '\u0015':
      case '\u0016':
      case '\u0017':
      case '\u0018':
      case '\u0019':
      case '\u001a':
      case '\u001b':
      case '\u001c':
      case '\u001d':
      case '\u001e':
      case '\u001f':
      case '!':
      case '$':
      case '%':
      case '&':
      case '\'':
      case ')':
      case '*':
      case '+':
      case ',':
      case '-':
      case '.':
      case '/':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case ':':
      case ';':
      case '<':
      case '=':
      case '>':
      case '?':
      case '@':
      case '\\':
      case ']':
      case '^':
      case '`':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   protected final void mWS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 28;
      int var5 = 0;

      while(true) {
         if (this.LA(1) == '\r' && this.LA(2) == '\n') {
            this.match('\r');
            this.match('\n');
            this.newline();
         } else if (this.LA(1) == ' ') {
            this.match(' ');
         } else if (this.LA(1) == '\t') {
            this.match('\t');
         } else if (this.LA(1) == '\r') {
            this.match('\r');
            this.newline();
         } else {
            if (this.LA(1) != '\n') {
               if (var5 >= 1) {
                  if (var1 && var2 == null && var4 != -1) {
                     var2 = this.makeToken(var4);
                     var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
                  }

                  this._returnToken = var2;
                  return;
               }

               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.match('\n');
            this.newline();
         }

         ++var5;
      }
   }

   protected final void mID(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 17;
      switch(this.LA(1)) {
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
         this.matchRange('A', 'Z');
         break;
      case '[':
      case '\\':
      case ']':
      case '^':
      case '`':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      case '_':
         this.match('_');
         break;
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
         this.matchRange('a', 'z');
      }

      while(_tokenSet_9.member(this.LA(1))) {
         switch(this.LA(1)) {
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
            this.matchRange('0', '9');
            break;
         case ':':
         case ';':
         case '<':
         case '=':
         case '>':
         case '?':
         case '@':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '`':
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
            this.matchRange('A', 'Z');
            break;
         case '_':
            this.match('_');
            break;
         case 'a':
         case 'b':
         case 'c':
         case 'd':
         case 'e':
         case 'f':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'n':
         case 'o':
         case 'p':
         case 'q':
         case 'r':
         case 's':
         case 't':
         case 'u':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         case 'z':
            this.matchRange('a', 'z');
         }
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mVAR_ASSIGN(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 18;
      this.match('=');
      if (this.LA(1) != '=' && this.transInfo != null && this.transInfo.refRuleRoot != null) {
         this.transInfo.assignToRoot = true;
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mAST_CONSTRUCTOR(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 10;
      Token var5 = null;
      Token var6 = null;
      Token var7 = null;
      int var8 = this.text.length();
      this.match('[');
      this.text.setLength(var8);
      switch(this.LA(1)) {
      case '\t':
      case '\n':
      case '\r':
      case ' ':
         var8 = this.text.length();
         this.mWS(false);
         this.text.setLength(var8);
      case '"':
      case '#':
      case '(':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '[':
      case '_':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
         var8 = this.text.length();
         this.mAST_CTOR_ELEMENT(true);
         this.text.setLength(var8);
         var5 = this._returnToken;
         switch(this.LA(1)) {
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            var8 = this.text.length();
            this.mWS(false);
            this.text.setLength(var8);
         case ',':
         case ']':
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         if (this.LA(1) == ',' && _tokenSet_10.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
            var8 = this.text.length();
            this.match(',');
            this.text.setLength(var8);
            label66:
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               var8 = this.text.length();
               this.mWS(false);
               this.text.setLength(var8);
            case '"':
            case '#':
            case '(':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '_':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
               var8 = this.text.length();
               this.mAST_CTOR_ELEMENT(true);
               this.text.setLength(var8);
               var6 = this._returnToken;
               switch(this.LA(1)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  var8 = this.text.length();
                  this.mWS(false);
                  this.text.setLength(var8);
               case ',':
               case ']':
                  break label66;
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            case '\u000b':
            case '\f':
            case '\u000e':
            case '\u000f':
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0017':
            case '\u0018':
            case '\u0019':
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            case '\u001f':
            case '!':
            case '$':
            case '%':
            case '&':
            case '\'':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '\\':
            case ']':
            case '^':
            case '`':
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         } else if (this.LA(1) != ',' && this.LA(1) != ']') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         switch(this.LA(1)) {
         case ',':
            var8 = this.text.length();
            this.match(',');
            this.text.setLength(var8);
            label59:
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               var8 = this.text.length();
               this.mWS(false);
               this.text.setLength(var8);
            case '"':
            case '#':
            case '(':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '_':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
               var8 = this.text.length();
               this.mAST_CTOR_ELEMENT(true);
               this.text.setLength(var8);
               var7 = this._returnToken;
               switch(this.LA(1)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  var8 = this.text.length();
                  this.mWS(false);
                  this.text.setLength(var8);
               case ']':
                  break label59;
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            case '\u000b':
            case '\f':
            case '\u000e':
            case '\u000f':
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0017':
            case '\u0018':
            case '\u0019':
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            case '\u001f':
            case '!':
            case '$':
            case '%':
            case '&':
            case '\'':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '\\':
            case ']':
            case '^':
            case '`':
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         case ']':
            var8 = this.text.length();
            this.match(']');
            this.text.setLength(var8);
            String var9 = this.generator.processStringForASTConstructor(var5.getText());
            if (var6 != null) {
               var9 = var9 + "," + var6.getText();
            }

            if (var7 != null) {
               var9 = var9 + "," + var7.getText();
            }

            this.text.setLength(var3);
            this.text.append(this.generator.getASTCreateString((GrammarAtom)null, var9));
            if (var1 && var2 == null && var4 != -1) {
               var2 = this.makeToken(var4);
               var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
            }

            this._returnToken = var2;
            return;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      case '\u000b':
      case '\f':
      case '\u000e':
      case '\u000f':
      case '\u0010':
      case '\u0011':
      case '\u0012':
      case '\u0013':
      case '\u0014':
      case '\u0015':
      case '\u0016':
      case '\u0017':
      case '\u0018':
      case '\u0019':
      case '\u001a':
      case '\u001b':
      case '\u001c':
      case '\u001d':
      case '\u001e':
      case '\u001f':
      case '!':
      case '$':
      case '%':
      case '&':
      case '\'':
      case ')':
      case '*':
      case '+':
      case ',':
      case '-':
      case '.':
      case '/':
      case ':':
      case ';':
      case '<':
      case '=':
      case '>':
      case '?':
      case '@':
      case '\\':
      case ']':
      case '^':
      case '`':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   protected final void mTEXT_ARG(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 13;
      switch(this.LA(1)) {
      case '\t':
      case '\n':
      case '\r':
      case ' ':
         this.mWS(false);
      case '"':
      case '$':
      case '\'':
      case '+':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '_':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
         int var5 = 0;

         for(; _tokenSet_11.member(this.LA(1)) && this.LA(2) >= 3 && this.LA(2) <= 255; ++var5) {
            this.mTEXT_ARG_ELEMENT(false);
            if (_tokenSet_4.member(this.LA(1)) && _tokenSet_12.member(this.LA(2))) {
               this.mWS(false);
            } else if (!_tokenSet_12.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }

         if (var5 >= 1) {
            if (var1 && var2 == null && var4 != -1) {
               var2 = this.makeToken(var4);
               var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
            }

            this._returnToken = var2;
            return;
         } else {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      case '\u000b':
      case '\f':
      case '\u000e':
      case '\u000f':
      case '\u0010':
      case '\u0011':
      case '\u0012':
      case '\u0013':
      case '\u0014':
      case '\u0015':
      case '\u0016':
      case '\u0017':
      case '\u0018':
      case '\u0019':
      case '\u001a':
      case '\u001b':
      case '\u001c':
      case '\u001d':
      case '\u001e':
      case '\u001f':
      case '!':
      case '#':
      case '%':
      case '&':
      case '(':
      case ')':
      case '*':
      case ',':
      case '-':
      case '.':
      case '/':
      case ':':
      case ';':
      case '<':
      case '=':
      case '>':
      case '?':
      case '@':
      case '[':
      case '\\':
      case ']':
      case '^':
      case '`':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   protected final void mTREE_ELEMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 9;
      Token var5 = null;
      switch(this.LA(1)) {
      case '"':
         this.mSTRING(false);
         break;
      case '#':
      case '$':
      case '%':
      case '&':
      case '\'':
      case ')':
      case '*':
      case '+':
      case ',':
      case '-':
      case '.':
      case '/':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case ':':
      case ';':
      case '<':
      case '=':
      case '>':
      case '?':
      case '@':
      case '\\':
      case ']':
      case '^':
      case '`':
      default:
         int var6;
         if (this.LA(1) == '#' && this.LA(2) == '(') {
            var6 = this.text.length();
            this.match('#');
            this.text.setLength(var6);
            this.mTREE(false);
         } else if (this.LA(1) == '#' && this.LA(2) == '[') {
            var6 = this.text.length();
            this.match('#');
            this.text.setLength(var6);
            this.mAST_CONSTRUCTOR(false);
         } else {
            String var8;
            if (this.LA(1) == '#' && _tokenSet_13.member(this.LA(2))) {
               var6 = this.text.length();
               this.match('#');
               this.text.setLength(var6);
               boolean var7 = this.mID_ELEMENT(true);
               var5 = this._returnToken;
               if (!var7) {
                  var8 = this.generator.mapTreeId(var5.getText(), (ActionTransInfo)null);
                  if (var8 != null) {
                     this.text.setLength(var3);
                     this.text.append(var8);
                  }
               }
            } else {
               if (this.LA(1) == '#' && this.LA(2) == '#') {
                  this.match("##");
                  if (this.currentRule != null) {
                     var8 = this.currentRule.getRuleName() + "_AST";
                     this.text.setLength(var3);
                     this.text.append(var8);
                  } else {
                     this.reportError("\"##\" not valid in this context");
                     this.text.setLength(var3);
                     this.text.append("##");
                  }
                  break;
               }

               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }
         break;
      case '(':
         this.mTREE(false);
         break;
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '_':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
         this.mID_ELEMENT(false);
         break;
      case '[':
         this.mAST_CONSTRUCTOR(false);
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final boolean mID_ELEMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      boolean var2 = false;
      Token var3 = null;
      int var4 = this.text.length();
      byte var5 = 12;
      Token var6 = null;
      this.mID(true);
      var6 = this._returnToken;
      int var7;
      if (_tokenSet_4.member(this.LA(1)) && _tokenSet_14.member(this.LA(2))) {
         var7 = this.text.length();
         this.mWS(false);
         this.text.setLength(var7);
      } else if (!_tokenSet_14.member(this.LA(1))) {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      label112:
      switch(this.LA(1)) {
      case '(':
         this.match('(');
         if (_tokenSet_4.member(this.LA(1)) && _tokenSet_15.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
            var7 = this.text.length();
            this.mWS(false);
            this.text.setLength(var7);
         } else if (!_tokenSet_15.member(this.LA(1)) || this.LA(2) < 3 || this.LA(2) > 255) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         switch(this.LA(1)) {
         case '\u000b':
         case '\f':
         case '\u000e':
         case '\u000f':
         case '\u0010':
         case '\u0011':
         case '\u0012':
         case '\u0013':
         case '\u0014':
         case '\u0015':
         case '\u0016':
         case '\u0017':
         case '\u0018':
         case '\u0019':
         case '\u001a':
         case '\u001b':
         case '\u001c':
         case '\u001d':
         case '\u001e':
         case '\u001f':
         case '!':
         case '$':
         case '%':
         case '&':
         case '*':
         case '+':
         case ',':
         case '-':
         case '.':
         case '/':
         case ':':
         case ';':
         case '<':
         case '=':
         case '>':
         case '?':
         case '@':
         case '\\':
         case ']':
         case '^':
         case '`':
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         case '"':
         case '#':
         case '\'':
         case '(':
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '[':
         case '_':
         case 'a':
         case 'b':
         case 'c':
         case 'd':
         case 'e':
         case 'f':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'n':
         case 'o':
         case 'p':
         case 'q':
         case 'r':
         case 's':
         case 't':
         case 'u':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         case 'z':
            this.mARG(false);

            while(this.LA(1) == ',') {
               this.match(',');
               switch(this.LA(1)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  var7 = this.text.length();
                  this.mWS(false);
                  this.text.setLength(var7);
               case '"':
               case '#':
               case '\'':
               case '(':
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
               case 'A':
               case 'B':
               case 'C':
               case 'D':
               case 'E':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'S':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case '_':
               case 'a':
               case 'b':
               case 'c':
               case 'd':
               case 'e':
               case 'f':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'n':
               case 'o':
               case 'p':
               case 'q':
               case 'r':
               case 's':
               case 't':
               case 'u':
               case 'v':
               case 'w':
               case 'x':
               case 'y':
               case 'z':
                  this.mARG(false);
                  break;
               case '\u000b':
               case '\f':
               case '\u000e':
               case '\u000f':
               case '\u0010':
               case '\u0011':
               case '\u0012':
               case '\u0013':
               case '\u0014':
               case '\u0015':
               case '\u0016':
               case '\u0017':
               case '\u0018':
               case '\u0019':
               case '\u001a':
               case '\u001b':
               case '\u001c':
               case '\u001d':
               case '\u001e':
               case '\u001f':
               case '!':
               case '$':
               case '%':
               case '&':
               case ')':
               case '*':
               case '+':
               case ',':
               case '-':
               case '.':
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
               case '\\':
               case ']':
               case '^':
               case '`':
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            }
         case '\t':
         case '\n':
         case '\r':
         case ' ':
         case ')':
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               var7 = this.text.length();
               this.mWS(false);
               this.text.setLength(var7);
            case ')':
               this.match(')');
               break label112;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }
      case '.':
         this.match('.');
         this.mID_ELEMENT(false);
         break;
      case '[':
         int var8 = 0;

         while(this.LA(1) == '[') {
            this.match('[');
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               var7 = this.text.length();
               this.mWS(false);
               this.text.setLength(var7);
            case '"':
            case '#':
            case '\'':
            case '(':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '_':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
               this.mARG(false);
               switch(this.LA(1)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  var7 = this.text.length();
                  this.mWS(false);
                  this.text.setLength(var7);
               case ']':
                  this.match(']');
                  ++var8;
                  continue;
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            case '\u000b':
            case '\f':
            case '\u000e':
            case '\u000f':
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0017':
            case '\u0018':
            case '\u0019':
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            case '\u001f':
            case '!':
            case '$':
            case '%':
            case '&':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '\\':
            case ']':
            case '^':
            case '`':
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }

         if (var8 < 1) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
         break;
      default:
         if (this.LA(1) == '-' && this.LA(2) == '>' && _tokenSet_13.member(this.LA(3))) {
            this.match("->");
            this.mID_ELEMENT(false);
         } else {
            if (!_tokenSet_16.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            var2 = true;
            String var9 = this.generator.mapTreeId(var6.getText(), this.transInfo);
            if (var9 != null) {
               this.text.setLength(var4);
               this.text.append(var9);
            }

            if (_tokenSet_17.member(this.LA(1)) && _tokenSet_16.member(this.LA(2)) && this.transInfo != null && this.transInfo.refRuleRoot != null) {
               switch(this.LA(1)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  this.mWS(false);
               case '=':
                  this.mVAR_ASSIGN(false);
                  break;
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            } else if (!_tokenSet_18.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }
      }

      if (var1 && var3 == null && var5 != -1) {
         var3 = this.makeToken(var5);
         var3.setText(new String(this.text.getBuffer(), var4, this.text.length() - var4));
      }

      this._returnToken = var3;
      return var2;
   }

   protected final void mAST_CTOR_ELEMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 11;
      if (this.LA(1) == '"' && this.LA(2) >= 3 && this.LA(2) <= 255 && this.LA(3) >= 3 && this.LA(3) <= 255) {
         this.mSTRING(false);
      } else if (_tokenSet_19.member(this.LA(1)) && this.LA(2) >= 3 && this.LA(2) <= 255) {
         this.mTREE_ELEMENT(false);
      } else {
         if (this.LA(1) < '0' || this.LA(1) > '9') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.mINT(false);
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mINT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 26;

      int var5;
      for(var5 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++var5) {
         this.mDIGIT(false);
      }

      if (var5 >= 1) {
         if (var1 && var2 == null && var4 != -1) {
            var2 = this.makeToken(var4);
            var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
         }

         this._returnToken = var2;
      } else {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   protected final void mARG(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 16;
      switch(this.LA(1)) {
      case '\'':
         this.mCHAR(false);
         break;
      case '(':
      case ')':
      case '*':
      case '+':
      case ',':
      case '-':
      case '.':
      case '/':
      default:
         if (_tokenSet_19.member(this.LA(1)) && this.LA(2) >= 3 && this.LA(2) <= 255 && this.LA(3) >= 3 && this.LA(3) <= 255) {
            this.mTREE_ELEMENT(false);
            break;
         }

         if (this.LA(1) != '"' || this.LA(2) < 3 || this.LA(2) > 255 || this.LA(3) < 3 || this.LA(3) > 255) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.mSTRING(false);
         break;
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
         this.mINT_OR_FLOAT(false);
      }

      while(_tokenSet_20.member(this.LA(1)) && _tokenSet_21.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
         switch(this.LA(1)) {
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            this.mWS(false);
         case '*':
         case '+':
         case '-':
         case '/':
            switch(this.LA(1)) {
            case '*':
               this.match('*');
               break;
            case '+':
               this.match('+');
               break;
            case ',':
            case '.':
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            case '-':
               this.match('-');
               break;
            case '/':
               this.match('/');
            }

            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.mWS(false);
            case '"':
            case '#':
            case '\'':
            case '(':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '_':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
               this.mARG(false);
               continue;
            case '\u000b':
            case '\f':
            case '\u000e':
            case '\u000f':
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0017':
            case '\u0018':
            case '\u0019':
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            case '\u001f':
            case '!':
            case '$':
            case '%':
            case '&':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '\\':
            case ']':
            case '^':
            case '`':
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mTEXT_ARG_ELEMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 14;
      switch(this.LA(1)) {
      case '"':
         this.mSTRING(false);
         break;
      case '#':
      case '%':
      case '&':
      case '(':
      case ')':
      case '*':
      case ',':
      case '-':
      case '.':
      case '/':
      case ':':
      case ';':
      case '<':
      case '=':
      case '>':
      case '?':
      case '@':
      case '[':
      case '\\':
      case ']':
      case '^':
      case '`':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      case '$':
         this.mTEXT_ITEM(false);
         break;
      case '\'':
         this.mCHAR(false);
         break;
      case '+':
         this.match('+');
         break;
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
         this.mINT_OR_FLOAT(false);
         break;
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '_':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
         this.mTEXT_ARG_ID_ELEMENT(false);
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mTEXT_ARG_ID_ELEMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 15;
      Token var5 = null;
      this.mID(true);
      var5 = this._returnToken;
      int var6;
      if (_tokenSet_4.member(this.LA(1)) && _tokenSet_22.member(this.LA(2))) {
         var6 = this.text.length();
         this.mWS(false);
         this.text.setLength(var6);
      } else if (!_tokenSet_22.member(this.LA(1))) {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      label104:
      switch(this.LA(1)) {
      case '\t':
      case '\n':
      case '\r':
      case ' ':
      case '"':
      case '$':
      case '\'':
      case ')':
      case '+':
      case ',':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case ']':
      case '_':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
         break;
      case '\u000b':
      case '\f':
      case '\u000e':
      case '\u000f':
      case '\u0010':
      case '\u0011':
      case '\u0012':
      case '\u0013':
      case '\u0014':
      case '\u0015':
      case '\u0016':
      case '\u0017':
      case '\u0018':
      case '\u0019':
      case '\u001a':
      case '\u001b':
      case '\u001c':
      case '\u001d':
      case '\u001e':
      case '\u001f':
      case '!':
      case '#':
      case '%':
      case '&':
      case '*':
      case '/':
      case ':':
      case ';':
      case '<':
      case '=':
      case '>':
      case '?':
      case '@':
      case '\\':
      case '^':
      case '`':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      case '(':
         this.match('(');
         if (_tokenSet_4.member(this.LA(1)) && _tokenSet_23.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
            var6 = this.text.length();
            this.mWS(false);
            this.text.setLength(var6);
         } else if (!_tokenSet_23.member(this.LA(1)) || this.LA(2) < 3 || this.LA(2) > 255) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         while(_tokenSet_24.member(this.LA(1)) && this.LA(2) >= 3 && this.LA(2) <= 255 && this.LA(3) >= 3 && this.LA(3) <= 255) {
            this.mTEXT_ARG(false);

            while(this.LA(1) == ',') {
               this.match(',');
               this.mTEXT_ARG(false);
            }
         }

         switch(this.LA(1)) {
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            var6 = this.text.length();
            this.mWS(false);
            this.text.setLength(var6);
         case ')':
            this.match(')');
            break label104;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      case '-':
         this.match("->");
         this.mTEXT_ARG_ID_ELEMENT(false);
         break;
      case '.':
         this.match('.');
         this.mTEXT_ARG_ID_ELEMENT(false);
         break;
      case '[':
         int var7 = 0;

         while(true) {
            if (this.LA(1) != '[') {
               if (var7 < 1) {
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
               break;
            }

            this.match('[');
            if (_tokenSet_4.member(this.LA(1)) && _tokenSet_24.member(this.LA(2)) && this.LA(3) >= 3 && this.LA(3) <= 255) {
               var6 = this.text.length();
               this.mWS(false);
               this.text.setLength(var6);
            } else if (!_tokenSet_24.member(this.LA(1)) || this.LA(2) < 3 || this.LA(2) > 255 || this.LA(3) < 3 || this.LA(3) > 255) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.mTEXT_ARG(false);
            switch(this.LA(1)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               var6 = this.text.length();
               this.mWS(false);
               this.text.setLength(var6);
            case ']':
               this.match(']');
               ++var7;
               break;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mINT_OR_FLOAT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 27;

      int var5;
      for(var5 = 0; this.LA(1) >= '0' && this.LA(1) <= '9' && _tokenSet_25.member(this.LA(2)); ++var5) {
         this.mDIGIT(false);
      }

      if (var5 < 1) {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      } else {
         if (this.LA(1) == 'L' && _tokenSet_26.member(this.LA(2))) {
            this.match('L');
         } else if (this.LA(1) == 'l' && _tokenSet_26.member(this.LA(2))) {
            this.match('l');
         } else if (this.LA(1) == '.') {
            this.match('.');

            while(this.LA(1) >= '0' && this.LA(1) <= '9' && _tokenSet_26.member(this.LA(2))) {
               this.mDIGIT(false);
            }
         } else if (!_tokenSet_26.member(this.LA(1))) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         if (var1 && var2 == null && var4 != -1) {
            var2 = this.makeToken(var4);
            var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
         }

         this._returnToken = var2;
      }
   }

   protected final void mSL_COMMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 20;
      this.match("//");

      while(this.LA(1) != '\n' && this.LA(1) != '\r' && this.LA(1) >= 3 && this.LA(1) <= 255 && this.LA(2) >= 3 && this.LA(2) <= 255) {
         this.matchNot('\uffff');
      }

      if (this.LA(1) == '\r' && this.LA(2) == '\n') {
         this.match("\r\n");
      } else if (this.LA(1) == '\n') {
         this.match('\n');
      } else {
         if (this.LA(1) != '\r') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.match('\r');
      }

      this.newline();
      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mML_COMMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 21;
      this.match("/*");

      while(this.LA(1) != '*' || this.LA(2) != '/') {
         if (this.LA(1) == '\r' && this.LA(2) == '\n' && this.LA(3) >= 3 && this.LA(3) <= 255) {
            this.match('\r');
            this.match('\n');
            this.newline();
         } else if (this.LA(1) == '\r' && this.LA(2) >= 3 && this.LA(2) <= 255 && this.LA(3) >= 3 && this.LA(3) <= 255) {
            this.match('\r');
            this.newline();
         } else if (this.LA(1) == '\n' && this.LA(2) >= 3 && this.LA(2) <= 255 && this.LA(3) >= 3 && this.LA(3) <= 255) {
            this.match('\n');
            this.newline();
         } else {
            if (this.LA(1) < 3 || this.LA(1) > 255 || this.LA(2) < 3 || this.LA(2) > 255 || this.LA(3) < 3 || this.LA(3) > 255) {
               break;
            }

            this.matchNot('\uffff');
         }
      }

      this.match("*/");
      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mESC(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 24;
      this.match('\\');
      switch(this.LA(1)) {
      case '"':
         this.match('"');
         break;
      case '\'':
         this.match('\'');
         break;
      case '0':
      case '1':
      case '2':
      case '3':
         this.matchRange('0', '3');
         if (this.LA(1) >= '0' && this.LA(1) <= '9' && this.LA(2) >= 3 && this.LA(2) <= 255) {
            this.mDIGIT(false);
            if (this.LA(1) >= '0' && this.LA(1) <= '9' && this.LA(2) >= 3 && this.LA(2) <= 255) {
               this.mDIGIT(false);
               break;
            } else {
               if (this.LA(1) >= 3 && this.LA(1) <= 255) {
                  break;
               }

               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         } else {
            if (this.LA(1) >= 3 && this.LA(1) <= 255) {
               break;
            }

            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      case '4':
      case '5':
      case '6':
      case '7':
         this.matchRange('4', '7');
         if (this.LA(1) >= '0' && this.LA(1) <= '9' && this.LA(2) >= 3 && this.LA(2) <= 255) {
            this.mDIGIT(false);
            break;
         } else {
            if (this.LA(1) >= 3 && this.LA(1) <= 255) {
               break;
            }

            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      case '\\':
         this.match('\\');
         break;
      case 'b':
         this.match('b');
         break;
      case 'f':
         this.match('f');
         break;
      case 'n':
         this.match('n');
         break;
      case 'r':
         this.match('r');
         break;
      case 't':
         this.match('t');
         break;
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mDIGIT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 25;
      this.matchRange('0', '9');
      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[8];
      var0[0] = -103079215112L;

      for(int var1 = 1; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }

   private static final long[] mk_tokenSet_1() {
      long[] var0 = new long[8];
      var0[0] = -145135534866440L;

      for(int var1 = 1; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }

   private static final long[] mk_tokenSet_2() {
      long[] var0 = new long[8];
      var0[0] = -141407503262728L;

      for(int var1 = 1; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }

   private static final long[] mk_tokenSet_3() {
      long[] var0 = new long[]{4294977024L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_4() {
      long[] var0 = new long[]{4294977024L, 0L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_5() {
      long[] var0 = new long[]{1103806604800L, 0L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_6() {
      long[] var0 = new long[]{287959436729787904L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_7() {
      long[] var0 = new long[8];
      var0[0] = -17179869192L;
      var0[1] = -268435457L;

      for(int var1 = 2; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }

   private static final long[] mk_tokenSet_8() {
      long[] var0 = new long[8];
      var0[0] = -549755813896L;
      var0[1] = -268435457L;

      for(int var1 = 2; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }

   private static final long[] mk_tokenSet_9() {
      long[] var0 = new long[]{287948901175001088L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_10() {
      long[] var0 = new long[]{287950056521213440L, 576460746129407998L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_11() {
      long[] var0 = new long[]{287958332923183104L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_12() {
      long[] var0 = new long[]{287978128427460096L, 576460746532061182L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_13() {
      long[] var0 = new long[]{0L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_14() {
      long[] var0 = new long[]{2306123388973753856L, 671088640L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_15() {
      long[] var0 = new long[]{287952805300282880L, 576460746129407998L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_16() {
      long[] var0 = new long[]{2306051920717948416L, 536870912L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_17() {
      long[] var0 = new long[]{2305843013508670976L, 0L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_18() {
      long[] var0 = new long[]{208911504254464L, 536870912L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_19() {
      long[] var0 = new long[]{1151051235328L, 576460746129407998L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_20() {
      long[] var0 = new long[]{189120294954496L, 0L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_21() {
      long[] var0 = new long[]{288139722277004800L, 576460746129407998L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_22() {
      long[] var0 = new long[]{288084781055354368L, 576460746666278910L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_23() {
      long[] var0 = new long[]{287960536241415680L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_24() {
      long[] var0 = new long[]{287958337218160128L, 576460745995190270L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_25() {
      long[] var0 = new long[]{288228817078593024L, 576460746532061182L, 0L, 0L, 0L};
      return var0;
   }

   private static final long[] mk_tokenSet_26() {
      long[] var0 = new long[]{288158448334415360L, 576460746532061182L, 0L, 0L, 0L};
      return var0;
   }
}
