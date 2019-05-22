package com.gzoltar.shaded.org.pitest.reloc.antlr.common.actions.python;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.ByteBuffer;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharBuffer;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharScanner;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharStreamException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CharStreamIOException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.InputBuffer;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.LexerSharedInputState;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.NoViableAltForCharException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.RecognitionException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.Token;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStream;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStreamException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStreamIOException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.TokenStreamRecognitionException;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.Tool;
import com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;

public class CodeLexer extends CharScanner implements CodeLexerTokenTypes, TokenStream {
   protected int lineOffset;
   private Tool antlrTool;
   public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
   public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

   public CodeLexer(String var1, String var2, int var3, Tool var4) {
      this((Reader)(new StringReader(var1)));
      this.setLine(var3);
      this.setFilename(var2);
      this.antlrTool = var4;
   }

   public void setLineOffset(int var1) {
      this.setLine(var1);
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

   public CodeLexer(InputStream var1) {
      this((InputBuffer)(new ByteBuffer(var1)));
   }

   public CodeLexer(Reader var1) {
      this((InputBuffer)(new CharBuffer(var1)));
   }

   public CodeLexer(InputBuffer var1) {
      this(new LexerSharedInputState(var1));
   }

   public CodeLexer(LexerSharedInputState var1) {
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
               this.mACTION(true);
               var1 = this._returnToken;
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

      while(this.LA(1) >= 3 && this.LA(1) <= 255) {
         this.mSTUFF(false);
      }

      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mSTUFF(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 5;
      if (this.LA(1) != '/' || this.LA(2) != '*' && this.LA(2) != '/') {
         if (this.LA(1) == '\r' && this.LA(2) == '\n') {
            this.match("\r\n");
            this.newline();
         } else if (this.LA(1) == '/' && _tokenSet_0.member(this.LA(2))) {
            this.match('/');
            this.match(_tokenSet_0);
         } else if (this.LA(1) == '\r') {
            this.match('\r');
            this.newline();
         } else if (this.LA(1) == '\n') {
            this.match('\n');
            this.newline();
         } else {
            if (!_tokenSet_1.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.match(_tokenSet_1);
         }
      } else {
         this.mCOMMENT(false);
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
      byte var4 = 6;
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

   protected final void mSL_COMMENT(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 7;
      int var5 = this.text.length();
      this.match("//");
      this.text.setLength(var5);
      this.text.append("#");

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
      byte var4 = 9;
      boolean var5 = false;
      int var6 = this.text.length();
      this.match("/*");
      this.text.setLength(var6);
      this.text.append("#");

      while(this.LA(1) != '*' || this.LA(2) != '/') {
         if (this.LA(1) == '\r' && this.LA(2) == '\n') {
            this.match('\r');
            this.match('\n');
            var6 = this.text.length();
            this.mIGNWS(false);
            this.text.setLength(var6);
            this.newline();
            this.text.append("# ");
         } else if (this.LA(1) == '\r' && this.LA(2) >= 3 && this.LA(2) <= 255) {
            this.match('\r');
            var6 = this.text.length();
            this.mIGNWS(false);
            this.text.setLength(var6);
            this.newline();
            this.text.append("# ");
         } else if (this.LA(1) == '\n' && this.LA(2) >= 3 && this.LA(2) <= 255) {
            this.match('\n');
            var6 = this.text.length();
            this.mIGNWS(false);
            this.text.setLength(var6);
            this.newline();
            this.text.append("# ");
         } else {
            if (this.LA(1) < 3 || this.LA(1) > 255 || this.LA(2) < 3 || this.LA(2) > 255) {
               break;
            }

            this.matchNot('\uffff');
         }
      }

      this.text.append("\n");
      var6 = this.text.length();
      this.match("*/");
      this.text.setLength(var6);
      if (var1 && var2 == null && var4 != -1) {
         var2 = this.makeToken(var4);
         var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
      }

      this._returnToken = var2;
   }

   protected final void mIGNWS(boolean var1) throws RecognitionException, CharStreamException, TokenStreamException {
      Token var2 = null;
      int var3 = this.text.length();
      byte var4 = 8;

      while(true) {
         while(this.LA(1) == ' ' && this.LA(2) >= 3 && this.LA(2) <= 255) {
            this.match(' ');
         }

         if (this.LA(1) != '\t' || this.LA(2) < 3 || this.LA(2) > 255) {
            if (var1 && var2 == null && var4 != -1) {
               var2 = this.makeToken(var4);
               var2.setText(new String(this.text.getBuffer(), var3, this.text.length() - var3));
            }

            this._returnToken = var2;
            return;
         }

         this.match('\t');
      }
   }

   private static final long[] mk_tokenSet_0() {
      long[] var0 = new long[8];
      var0[0] = -145135534866440L;

      for(int var1 = 1; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }

   private static final long[] mk_tokenSet_1() {
      long[] var0 = new long[8];
      var0[0] = -140737488364552L;

      for(int var1 = 1; var1 <= 3; ++var1) {
         var0[var1] = -1L;
      }

      return var0;
   }
}
