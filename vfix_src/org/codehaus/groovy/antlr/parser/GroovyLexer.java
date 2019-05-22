package org.codehaus.groovy.antlr.parser;

import groovyjarjarantlr.ANTLRHashString;
import groovyjarjarantlr.ByteBuffer;
import groovyjarjarantlr.CharBuffer;
import groovyjarjarantlr.CharScanner;
import groovyjarjarantlr.CharStreamException;
import groovyjarjarantlr.CharStreamIOException;
import groovyjarjarantlr.InputBuffer;
import groovyjarjarantlr.LexerSharedInputState;
import groovyjarjarantlr.MismatchedCharException;
import groovyjarjarantlr.NoViableAltForCharException;
import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.SemanticException;
import groovyjarjarantlr.Token;
import groovyjarjarantlr.TokenStream;
import groovyjarjarantlr.TokenStreamException;
import groovyjarjarantlr.TokenStreamIOException;
import groovyjarjarantlr.TokenStreamRecognitionException;
import groovyjarjarantlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import org.codehaus.groovy.antlr.GroovySourceToken;

public class GroovyLexer extends CharScanner implements GroovyTokenTypes, TokenStream {
   private boolean assertEnabled;
   private boolean enumEnabled;
   private boolean whitespaceIncluded;
   protected int parenLevel;
   protected int suppressNewline;
   protected static final int SCS_TYPE = 3;
   protected static final int SCS_VAL = 4;
   protected static final int SCS_LIT = 8;
   protected static final int SCS_LIMIT = 16;
   protected static final int SCS_SQ_TYPE = 0;
   protected static final int SCS_TQ_TYPE = 1;
   protected static final int SCS_RE_TYPE = 2;
   protected int stringCtorState;
   protected ArrayList parenLevelStack;
   protected int lastSigTokenType;
   public static boolean tracing = false;
   private static HashMap ttypes;
   protected GroovyRecognizer parser;
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

   public void enableAssert(boolean shouldEnable) {
      this.assertEnabled = shouldEnable;
   }

   public boolean isAssertEnabled() {
      return this.assertEnabled;
   }

   public void enableEnum(boolean shouldEnable) {
      this.enumEnabled = shouldEnable;
   }

   public boolean isEnumEnabled() {
      return this.enumEnabled;
   }

   public void setWhitespaceIncluded(boolean z) {
      this.whitespaceIncluded = z;
   }

   public boolean isWhitespaceIncluded() {
      return this.whitespaceIncluded;
   }

   public void setTokenObjectClass(String name) {
   }

   protected Token makeToken(int t) {
      GroovySourceToken tok = new GroovySourceToken(t);
      tok.setColumn(this.inputState.getTokenStartColumn());
      tok.setLine(this.inputState.getTokenStartLine());
      tok.setColumnLast(this.inputState.getColumn());
      tok.setLineLast(this.inputState.getLine());
      return tok;
   }

   protected void pushParenLevel() {
      this.parenLevelStack.add(this.parenLevel * 16 + this.stringCtorState);
      this.parenLevel = 0;
      this.stringCtorState = 0;
   }

   protected void popParenLevel() {
      int npl = this.parenLevelStack.size();
      if (npl != 0) {
         --npl;
         int i = (Integer)this.parenLevelStack.remove(npl);
         this.parenLevel = i / 16;
         this.stringCtorState = i % 16;
      }
   }

   protected void restartStringCtor(boolean expectLiteral) {
      if (this.stringCtorState != 0) {
         this.stringCtorState = (expectLiteral ? 8 : 4) + (this.stringCtorState & 3);
      }

   }

   protected boolean allowRegexpLiteral() {
      return !isExpressionEndingToken(this.lastSigTokenType);
   }

   protected static boolean isExpressionEndingToken(int ttype) {
      switch(ttype) {
      case 78:
      case 79:
      case 80:
      case 81:
      case 83:
      case 84:
      case 85:
      case 89:
      case 90:
      case 91:
      case 94:
      case 95:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 110:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 119:
      case 123:
      case 125:
      case 126:
      case 127:
      case 128:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 137:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 145:
      case 146:
      case 147:
      case 148:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 194:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         return true;
      case 82:
      case 86:
      case 87:
      case 88:
      case 92:
      case 93:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 120:
      case 121:
      case 122:
      case 124:
      case 129:
      case 130:
      case 131:
      case 143:
      case 144:
      case 149:
      case 150:
      case 151:
      case 157:
      case 158:
      case 159:
      case 160:
      case 161:
      case 162:
      case 163:
      case 164:
      case 165:
      case 166:
      case 167:
      case 168:
      case 169:
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
      case 176:
      case 177:
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 183:
      case 184:
      case 185:
      case 187:
      case 188:
      case 190:
      case 191:
      case 192:
      case 193:
      default:
         return false;
      }
   }

   protected void newlineCheck(boolean check) throws RecognitionException {
      if (check && this.suppressNewline > 0) {
         this.require(this.suppressNewline == 0, "end of line reached within a simple string 'x' or \"x\" or /x/", "for multi-line literals, use triple quotes '''x''' or \"\"\"x\"\"\"");
         this.suppressNewline = 0;
      }

      this.newline();
   }

   protected boolean atValidDollarEscape() throws CharStreamException {
      int k = 1;
      int var3 = k + 1;
      char lc = this.LA(k);
      if (lc != '$') {
         return false;
      } else {
         lc = this.LA(var3++);
         if (lc == '*') {
            lc = this.LA(var3++);
         }

         return lc == '{' || lc != '$' && Character.isJavaIdentifierStart(lc);
      }
   }

   public TokenStream plumb() {
      return new TokenStream() {
         public Token nextToken() throws TokenStreamException {
            if (GroovyLexer.this.stringCtorState >= 8) {
               int quoteType = GroovyLexer.this.stringCtorState & 3;
               GroovyLexer.this.stringCtorState = 0;
               GroovyLexer.this.resetText();

               try {
                  switch(quoteType) {
                  case 0:
                     GroovyLexer.this.mSTRING_CTOR_END(true, false, false);
                     break;
                  case 1:
                     GroovyLexer.this.mSTRING_CTOR_END(true, false, true);
                     break;
                  case 2:
                     GroovyLexer.this.mREGEXP_CTOR_END(true, false);
                     break;
                  default:
                     throw new AssertionError(false);
                  }

                  GroovyLexer.this.lastSigTokenType = GroovyLexer.this._returnToken.getType();
                  return GroovyLexer.this._returnToken;
               } catch (RecognitionException var3) {
                  throw new TokenStreamRecognitionException(var3);
               } catch (CharStreamException var4) {
                  if (var4 instanceof CharStreamIOException) {
                     throw new TokenStreamIOException(((CharStreamIOException)var4).io);
                  } else {
                     throw new TokenStreamException(var4.getMessage());
                  }
               }
            } else {
               Token token = GroovyLexer.this.nextToken();
               int lasttype = token.getType();
               if (GroovyLexer.this.whitespaceIncluded) {
                  switch(lasttype) {
                  case 203:
                  case 204:
                  case 205:
                  case 206:
                     lasttype = GroovyLexer.this.lastSigTokenType;
                  }
               }

               GroovyLexer.this.lastSigTokenType = lasttype;
               return token;
            }
         }
      };
   }

   public void traceIn(String rname) throws CharStreamException {
      if (tracing) {
         super.traceIn(rname);
      }
   }

   public void traceOut(String rname) throws CharStreamException {
      if (tracing) {
         if (this._returnToken != null) {
            rname = rname + tokenStringOf(this._returnToken);
         }

         super.traceOut(rname);
      }
   }

   private static String tokenStringOf(Token t) {
      if (ttypes == null) {
         HashMap map = new HashMap();
         Field[] fields = GroovyTokenTypes.class.getDeclaredFields();

         for(int i = 0; i < fields.length; ++i) {
            if (fields[i].getType() == Integer.TYPE) {
               try {
                  map.put(fields[i].get((Object)null), fields[i].getName());
               } catch (IllegalAccessException var5) {
               }
            }
         }

         ttypes = map;
      }

      Integer tt = t.getType();
      Object ttn = ttypes.get(tt);
      if (ttn == null) {
         ttn = "<" + tt + ">";
      }

      return "[" + ttn + ",\"" + t.getText() + "\"]";
   }

   private void require(boolean z, String problem, String solution) throws SemanticException {
      if (!z) {
         this.parser.requireFailed(problem, solution);
      }

   }

   public GroovyLexer(InputStream in) {
      this((InputBuffer)(new ByteBuffer(in)));
   }

   public GroovyLexer(Reader in) {
      this((InputBuffer)(new CharBuffer(in)));
   }

   public GroovyLexer(InputBuffer ib) {
      this(new LexerSharedInputState(ib));
   }

   public GroovyLexer(LexerSharedInputState state) {
      super(state);
      this.assertEnabled = true;
      this.enumEnabled = true;
      this.whitespaceIncluded = false;
      this.setTabSize(1);
      this.parenLevel = 0;
      this.suppressNewline = 0;
      this.stringCtorState = 0;
      this.parenLevelStack = new ArrayList();
      this.lastSigTokenType = 1;
      this.caseSensitiveLiterals = true;
      this.setCaseSensitive(true);
      this.literals = new Hashtable();
      this.literals.put(new ANTLRHashString("byte", this), new Integer(102));
      this.literals.put(new ANTLRHashString("public", this), new Integer(112));
      this.literals.put(new ANTLRHashString("case", this), new Integer(145));
      this.literals.put(new ANTLRHashString("short", this), new Integer(104));
      this.literals.put(new ANTLRHashString("break", this), new Integer(139));
      this.literals.put(new ANTLRHashString("while", this), new Integer(134));
      this.literals.put(new ANTLRHashString("new", this), new Integer(154));
      this.literals.put(new ANTLRHashString("instanceof", this), new Integer(153));
      this.literals.put(new ANTLRHashString("implements", this), new Integer(127));
      this.literals.put(new ANTLRHashString("synchronized", this), new Integer(117));
      this.literals.put(new ANTLRHashString("const", this), new Integer(40));
      this.literals.put(new ANTLRHashString("float", this), new Integer(106));
      this.literals.put(new ANTLRHashString("package", this), new Integer(78));
      this.literals.put(new ANTLRHashString("return", this), new Integer(138));
      this.literals.put(new ANTLRHashString("throw", this), new Integer(141));
      this.literals.put(new ANTLRHashString("null", this), new Integer(155));
      this.literals.put(new ANTLRHashString("def", this), new Integer(81));
      this.literals.put(new ANTLRHashString("threadsafe", this), new Integer(116));
      this.literals.put(new ANTLRHashString("protected", this), new Integer(113));
      this.literals.put(new ANTLRHashString("class", this), new Integer(89));
      this.literals.put(new ANTLRHashString("throws", this), new Integer(126));
      this.literals.put(new ANTLRHashString("do", this), new Integer(41));
      this.literals.put(new ANTLRHashString("strictfp", this), new Integer(42));
      this.literals.put(new ANTLRHashString("super", this), new Integer(95));
      this.literals.put(new ANTLRHashString("transient", this), new Integer(114));
      this.literals.put(new ANTLRHashString("native", this), new Integer(115));
      this.literals.put(new ANTLRHashString("interface", this), new Integer(90));
      this.literals.put(new ANTLRHashString("final", this), new Integer(37));
      this.literals.put(new ANTLRHashString("if", this), new Integer(132));
      this.literals.put(new ANTLRHashString("double", this), new Integer(108));
      this.literals.put(new ANTLRHashString("volatile", this), new Integer(118));
      this.literals.put(new ANTLRHashString("as", this), new Integer(110));
      this.literals.put(new ANTLRHashString("assert", this), new Integer(142));
      this.literals.put(new ANTLRHashString("catch", this), new Integer(148));
      this.literals.put(new ANTLRHashString("try", this), new Integer(146));
      this.literals.put(new ANTLRHashString("goto", this), new Integer(39));
      this.literals.put(new ANTLRHashString("enum", this), new Integer(91));
      this.literals.put(new ANTLRHashString("int", this), new Integer(105));
      this.literals.put(new ANTLRHashString("for", this), new Integer(136));
      this.literals.put(new ANTLRHashString("extends", this), new Integer(94));
      this.literals.put(new ANTLRHashString("boolean", this), new Integer(101));
      this.literals.put(new ANTLRHashString("char", this), new Integer(103));
      this.literals.put(new ANTLRHashString("private", this), new Integer(111));
      this.literals.put(new ANTLRHashString("default", this), new Integer(125));
      this.literals.put(new ANTLRHashString("false", this), new Integer(152));
      this.literals.put(new ANTLRHashString("this", this), new Integer(128));
      this.literals.put(new ANTLRHashString("static", this), new Integer(80));
      this.literals.put(new ANTLRHashString("abstract", this), new Integer(38));
      this.literals.put(new ANTLRHashString("continue", this), new Integer(140));
      this.literals.put(new ANTLRHashString("finally", this), new Integer(147));
      this.literals.put(new ANTLRHashString("else", this), new Integer(133));
      this.literals.put(new ANTLRHashString("import", this), new Integer(79));
      this.literals.put(new ANTLRHashString("in", this), new Integer(137));
      this.literals.put(new ANTLRHashString("void", this), new Integer(100));
      this.literals.put(new ANTLRHashString("switch", this), new Integer(135));
      this.literals.put(new ANTLRHashString("true", this), new Integer(156));
      this.literals.put(new ANTLRHashString("long", this), new Integer(107));
   }

   public Token nextToken() throws TokenStreamException {
      Token var1 = null;

      while(true) {
         Token _token = null;
         int _ttype = false;
         this.resetText();

         try {
            try {
               switch(this.LA(1)) {
               case '\t':
               case '\f':
               case ' ':
               case '\\':
                  this.mWS(true);
                  var1 = this._returnToken;
                  break;
               case '\n':
               case '\r':
                  this.mNLS(true);
                  var1 = this._returnToken;
                  break;
               case '\u000b':
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
               case '$':
               case '%':
               case '&':
               case '*':
               case '+':
               case '-':
               case '.':
               case '/':
               case '<':
               case '=':
               case '>':
               case '?':
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
               case '^':
               case '_':
               case '`':
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
               case '|':
               default:
                  if (this.LA(1) == '>' && this.LA(2) == '>' && this.LA(3) == '>' && this.LA(4) == '=') {
                     this.mBSR_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '<' && this.LA(2) == '=' && this.LA(3) == '>') {
                     this.mCOMPARE_TO(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '=' && this.LA(2) == '=' && this.LA(3) == '=') {
                     this.mIDENTICAL(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '!' && this.LA(2) == '=' && this.LA(3) == '=') {
                     this.mNOT_IDENTICAL(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '>' && this.LA(2) == '>' && this.LA(3) == '=') {
                     this.mSR_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '>' && this.LA(2) == '>' && this.LA(3) == '>') {
                     this.mBSR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '<' && this.LA(2) == '<' && this.LA(3) == '=') {
                     this.mSL_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '.' && this.LA(2) == '.' && this.LA(3) == '<') {
                     this.mRANGE_EXCLUSIVE(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '.' && this.LA(2) == '.' && this.LA(3) == '.') {
                     this.mTRIPLE_DOT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '=' && this.LA(2) == '=' && this.LA(3) == '~') {
                     this.mREGEX_MATCH(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '*' && this.LA(2) == '*' && this.LA(3) == '=') {
                     this.mSTAR_STAR_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '=' && this.LA(2) == '=') {
                     this.mEQUAL(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '!' && this.LA(2) == '=') {
                     this.mNOT_EQUAL(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '+' && this.LA(2) == '=') {
                     this.mPLUS_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '+' && this.LA(2) == '+') {
                     this.mINC(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '-' && this.LA(2) == '=') {
                     this.mMINUS_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '-' && this.LA(2) == '-') {
                     this.mDEC(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '*' && this.LA(2) == '=') {
                     this.mSTAR_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '%' && this.LA(2) == '=') {
                     this.mMOD_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '>' && this.LA(2) == '>') {
                     this.mSR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '>' && this.LA(2) == '=') {
                     this.mGE(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '<' && this.LA(2) == '<') {
                     this.mSL(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '<' && this.LA(2) == '=') {
                     this.mLE(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '^' && this.LA(2) == '=') {
                     this.mBXOR_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '|' && this.LA(2) == '=') {
                     this.mBOR_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '|' && this.LA(2) == '|') {
                     this.mLOR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '&' && this.LA(2) == '=') {
                     this.mBAND_ASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '&' && this.LA(2) == '&') {
                     this.mLAND(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '.' && this.LA(2) == '.') {
                     this.mRANGE_INCLUSIVE(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '*' && this.LA(2) == '.') {
                     this.mSPREAD_DOT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '?' && this.LA(2) == '.') {
                     this.mOPTIONAL_DOT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '?' && this.LA(2) == ':') {
                     this.mELVIS_OPERATOR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '.' && this.LA(2) == '&') {
                     this.mMEMBER_POINTER(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '=' && this.LA(2) == '~') {
                     this.mREGEX_FIND(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '*' && this.LA(2) == '*') {
                     this.mSTAR_STAR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '-' && this.LA(2) == '>') {
                     this.mCLOSABLE_BLOCK_OP(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '/' && this.LA(2) == '/') {
                     this.mSL_COMMENT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '/' && this.LA(2) == '*') {
                     this.mML_COMMENT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '?') {
                     this.mQUESTION(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '.') {
                     this.mDOT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '=') {
                     this.mASSIGN(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '!') {
                     this.mLNOT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '+') {
                     this.mPLUS(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '-') {
                     this.mMINUS(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '*') {
                     this.mSTAR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '%') {
                     this.mMOD(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '>') {
                     this.mGT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '<') {
                     this.mLT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '^') {
                     this.mBXOR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '|') {
                     this.mBOR(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '&') {
                     this.mBAND(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '#' && this.getLine() == 1 && this.getColumn() == 1) {
                     this.mSH_COMMENT(true);
                     var1 = this._returnToken;
                  } else if (this.LA(1) == '/') {
                     this.mREGEXP_LITERAL(true);
                     var1 = this._returnToken;
                  } else if (_tokenSet_0.member(this.LA(1))) {
                     this.mIDENT(true);
                     var1 = this._returnToken;
                  } else {
                     if (this.LA(1) != '\uffff') {
                        throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                     }

                     this.uponEOF();
                     this._returnToken = this.makeToken(1);
                  }
                  break;
               case '"':
               case '\'':
                  this.mSTRING_LITERAL(true);
                  var1 = this._returnToken;
                  break;
               case '(':
                  this.mLPAREN(true);
                  var1 = this._returnToken;
                  break;
               case ')':
                  this.mRPAREN(true);
                  var1 = this._returnToken;
                  break;
               case ',':
                  this.mCOMMA(true);
                  var1 = this._returnToken;
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
                  this.mNUM_INT(true);
                  var1 = this._returnToken;
                  break;
               case ':':
                  this.mCOLON(true);
                  var1 = this._returnToken;
                  break;
               case ';':
                  this.mSEMI(true);
                  var1 = this._returnToken;
                  break;
               case '@':
                  this.mAT(true);
                  var1 = this._returnToken;
                  break;
               case '[':
                  this.mLBRACK(true);
                  var1 = this._returnToken;
                  break;
               case ']':
                  this.mRBRACK(true);
                  var1 = this._returnToken;
                  break;
               case '{':
                  this.mLCURLY(true);
                  var1 = this._returnToken;
                  break;
               case '}':
                  this.mRCURLY(true);
                  var1 = this._returnToken;
                  break;
               case '~':
                  this.mBNOT(true);
                  var1 = this._returnToken;
               }

               if (this._returnToken != null) {
                  int _ttype = this._returnToken.getType();
                  this._returnToken.setType(_ttype);
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

   public final void mQUESTION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 93;
      this.match('?');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 88;
      this.match('(');
      if (this.inputState.guessing == 0) {
         ++this.parenLevel;
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 119;
      this.match(')');
      if (this.inputState.guessing == 0) {
         --this.parenLevel;
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 82;
      this.match('[');
      if (this.inputState.guessing == 0) {
         ++this.parenLevel;
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mRBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 83;
      this.match(']');
      if (this.inputState.guessing == 0) {
         --this.parenLevel;
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 122;
      this.match('{');
      if (this.inputState.guessing == 0) {
         this.pushParenLevel();
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mRCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 123;
      this.match('}');
      if (this.inputState.guessing == 0) {
         this.popParenLevel();
         if (this.stringCtorState != 0) {
            this.restartStringCtor(true);
         }
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 131;
      this.match(':');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 96;
      this.match(',');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mDOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 87;
      this.match('.');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 120;
      this.match('=');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mCOMPARE_TO(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 180;
      this.match("<=>");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mEQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 177;
      this.match("==");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mIDENTICAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 178;
      this.match("===");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 192;
      this.match('!');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 191;
      this.match('~');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mNOT_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 176;
      this.match("!=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mNOT_IDENTICAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 179;
      this.match("!==");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mDIV(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 187;
      this.match('/');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mDIV_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 160;
      this.match("/=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 143;
      this.match('+');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mPLUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 157;
      this.match("+=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mINC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 186;
      this.match("++");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 144;
      this.match('-');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mMINUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 158;
      this.match("-=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mDEC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 189;
      this.match("--");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSTAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 109;
      this.match('*');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSTAR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 159;
      this.match("*=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mMOD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 188;
      this.match('%');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mMOD_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 161;
      this.match("%=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 98;
      this.match(">>");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 162;
      this.match(">>=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBSR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 99;
      this.match(">>>");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBSR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 163;
      this.match(">>>=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mGE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 182;
      this.match(">=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mGT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 97;
      this.match(">");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 183;
      this.match("<<");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSL_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 164;
      this.match("<<=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 181;
      this.match("<=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 86;
      this.match('<');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBXOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 173;
      this.match('^');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBXOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 166;
      this.match("^=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 172;
      this.match('|');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 167;
      this.match("|=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 170;
      this.match("||");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 121;
      this.match('&');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mBAND_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 165;
      this.match("&=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mLAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 171;
      this.match("&&");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSEMI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 124;
      this.match(';');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mDOLLAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 202;
      this.match('$');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mRANGE_INCLUSIVE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 184;
      this.match("..");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mRANGE_EXCLUSIVE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 185;
      this.match("..<");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mTRIPLE_DOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 129;
      this.match("...");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSPREAD_DOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 149;
      this.match("*.");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mOPTIONAL_DOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 150;
      this.match("?.");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mELVIS_OPERATOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 169;
      this.match("?:");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mMEMBER_POINTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 151;
      this.match(".&");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mREGEX_FIND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 174;
      this.match("=~");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mREGEX_MATCH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 175;
      this.match("==~");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSTAR_STAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 190;
      this.match("**");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSTAR_STAR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 168;
      this.match("**=");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mCLOSABLE_BLOCK_OP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 130;
      this.match("->");
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 203;
      int _cnt617 = 0;

      while(true) {
         if (this.LA(1) != '\\' || this.LA(2) != '\n' && this.LA(2) != '\r') {
            if (this.LA(1) == ' ') {
               this.match(' ');
            } else if (this.LA(1) == '\t') {
               this.match('\t');
            } else {
               if (this.LA(1) != '\f') {
                  if (_cnt617 >= 1) {
                     if (this.inputState.guessing == 0 && !this.whitespaceIncluded) {
                        _ttype = -1;
                     }

                     if (_createToken && _token == null && _ttype != -1) {
                        _token = this.makeToken(_ttype);
                        _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
                     }

                     this._returnToken = _token;
                     return;
                  }

                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }

               this.match('\f');
            }
         } else {
            this.match('\\');
            this.mONE_NL(false, false);
         }

         ++_cnt617;
      }
   }

   protected final void mONE_NL(boolean _createToken, boolean check) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 204;
      int _saveIndex;
      if (this.LA(1) == '\r' && this.LA(2) == '\n') {
         _saveIndex = this.text.length();
         this.match("\r\n");
         this.text.setLength(_saveIndex);
      } else if (this.LA(1) == '\r') {
         _saveIndex = this.text.length();
         this.match('\r');
         this.text.setLength(_saveIndex);
      } else {
         if (this.LA(1) != '\n') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         _saveIndex = this.text.length();
         this.match('\n');
         this.text.setLength(_saveIndex);
      }

      if (this.inputState.guessing == 0) {
         this.newlineCheck(check);
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mNLS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 201;
      this.mONE_NL(false, true);
      if ((this.LA(1) == '\t' || this.LA(1) == '\n' || this.LA(1) == '\f' || this.LA(1) == '\r' || this.LA(1) == ' ' || this.LA(1) == '/' || this.LA(1) == '\\') && !this.whitespaceIncluded) {
         int _cnt623 = 0;

         label57:
         while(true) {
            switch(this.LA(1)) {
            case '\t':
            case '\f':
            case ' ':
            case '\\':
               this.mWS(false);
               break;
            case '\n':
            case '\r':
               this.mONE_NL(false, true);
               break;
            default:
               if (this.LA(1) == '/' && this.LA(2) == '/') {
                  this.mSL_COMMENT(false);
               } else {
                  if (this.LA(1) != '/' || this.LA(2) != '*') {
                     if (_cnt623 < 1) {
                        throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                     }
                     break label57;
                  }

                  this.mML_COMMENT(false);
               }
            }

            ++_cnt623;
         }
      }

      if (this.inputState.guessing == 0 && !this.whitespaceIncluded) {
         if (this.parenLevel != 0) {
            _ttype = -1;
         } else {
            this.text.setLength(_begin);
            this.text.append("<newline>");
         }
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mSL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 205;
      this.match("//");

      while(_tokenSet_1.member(this.LA(1))) {
         this.match(_tokenSet_1);
      }

      if (this.inputState.guessing == 0 && !this.whitespaceIncluded) {
         _ttype = -1;
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 206;
      this.match("/*");

      while(true) {
         while(true) {
            boolean synPredMatched635 = false;
            if (this.LA(1) == '*' && this.LA(2) >= 0 && this.LA(2) <= '\ufffe' && this.LA(3) >= 0 && this.LA(3) <= '\ufffe') {
               int _m635 = this.mark();
               synPredMatched635 = true;
               ++this.inputState.guessing;

               try {
                  this.match('*');
                  this.matchNot('/');
               } catch (RecognitionException var8) {
                  synPredMatched635 = false;
               }

               this.rewind(_m635);
               --this.inputState.guessing;
            }

            if (synPredMatched635) {
               this.match('*');
            } else if (this.LA(1) != '\n' && this.LA(1) != '\r') {
               if (!_tokenSet_2.member(this.LA(1))) {
                  this.match("*/");
                  if (this.inputState.guessing == 0 && !this.whitespaceIncluded) {
                     _ttype = -1;
                  }

                  if (_createToken && _token == null && _ttype != -1) {
                     _token = this.makeToken(_ttype);
                     _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
                  }

                  this._returnToken = _token;
                  return;
               }

               this.match(_tokenSet_2);
            } else {
               this.mONE_NL(false, true);
            }
         }
      }
   }

   public final void mSH_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 77;
      if (this.getLine() == 1 && this.getColumn() == 1) {
         this.match("#!");

         while(_tokenSet_1.member(this.LA(1))) {
            this.match(_tokenSet_1);
         }

         if (this.inputState.guessing == 0 && !this.whitespaceIncluded) {
            _ttype = -1;
         }

         if (_createToken && _token == null && _ttype != -1) {
            _token = this.makeToken(_ttype);
            _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
         }

         this._returnToken = _token;
      } else {
         throw new SemanticException("getLine() == 1 && getColumn() == 1");
      }
   }

   public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 85;
      int tt = false;
      boolean synPredMatched640 = false;
      if (this.LA(1) == '\'' && this.LA(2) == '\'' && this.LA(3) == '\'' && this.LA(4) >= 0 && this.LA(4) <= '\ufffe') {
         int _m640 = this.mark();
         synPredMatched640 = true;
         ++this.inputState.guessing;

         try {
            this.match("'''");
         } catch (RecognitionException var13) {
            synPredMatched640 = false;
         }

         this.rewind(_m640);
         --this.inputState.guessing;
      }

      int _m644;
      int _saveIndex;
      boolean synPredMatched644;
      if (synPredMatched640) {
         _saveIndex = this.text.length();
         this.match("'''");
         this.text.setLength(_saveIndex);

         label149:
         while(true) {
            while(true) {
               switch(this.LA(1)) {
               case '\n':
               case '\r':
                  this.mSTRING_NL(false, true);
                  break;
               case '"':
                  this.match('"');
                  break;
               case '$':
                  this.match('$');
                  break;
               case '\\':
                  this.mESC(false);
                  break;
               default:
                  synPredMatched644 = false;
                  if (this.LA(1) == '\'' && this.LA(2) >= 0 && this.LA(2) <= '\ufffe' && this.LA(3) >= 0 && this.LA(3) <= '\ufffe' && this.LA(4) >= 0 && this.LA(4) <= '\ufffe') {
                     _m644 = this.mark();
                     synPredMatched644 = true;
                     ++this.inputState.guessing;

                     try {
                        this.match('\'');
                        if (_tokenSet_3.member(this.LA(1))) {
                           this.matchNot('\'');
                        } else {
                           if (this.LA(1) != '\'') {
                              throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                           }

                           this.match('\'');
                           this.matchNot('\'');
                        }
                     } catch (RecognitionException var11) {
                        synPredMatched644 = false;
                     }

                     this.rewind(_m644);
                     --this.inputState.guessing;
                  }

                  if (synPredMatched644) {
                     this.match('\'');
                  } else {
                     if (!_tokenSet_4.member(this.LA(1))) {
                        _saveIndex = this.text.length();
                        this.match("'''");
                        this.text.setLength(_saveIndex);
                        break label149;
                     }

                     this.mSTRING_CH(false);
                  }
               }
            }
         }
      } else {
         synPredMatched644 = false;
         if (this.LA(1) == '"' && this.LA(2) == '"' && this.LA(3) == '"' && this.LA(4) >= 0 && this.LA(4) <= '\ufffe') {
            _m644 = this.mark();
            synPredMatched644 = true;
            ++this.inputState.guessing;

            try {
               this.match("\"\"\"");
            } catch (RecognitionException var12) {
               synPredMatched644 = false;
            }

            this.rewind(_m644);
            --this.inputState.guessing;
         }

         int tt;
         if (synPredMatched644) {
            _saveIndex = this.text.length();
            this.match("\"\"\"");
            this.text.setLength(_saveIndex);
            tt = this.mSTRING_CTOR_END(false, true, true);
            if (this.inputState.guessing == 0) {
               _ttype = tt;
            }
         } else if (this.LA(1) == '\'' && _tokenSet_1.member(this.LA(2))) {
            _saveIndex = this.text.length();
            this.match('\'');
            this.text.setLength(_saveIndex);
            if (this.inputState.guessing == 0) {
               ++this.suppressNewline;
            }

            label135:
            while(true) {
               while(true) {
                  switch(this.LA(1)) {
                  case '"':
                     this.match('"');
                     break;
                  case '$':
                     this.match('$');
                     break;
                  case '\\':
                     this.mESC(false);
                     break;
                  default:
                     if (!_tokenSet_4.member(this.LA(1))) {
                        if (this.inputState.guessing == 0) {
                           --this.suppressNewline;
                        }

                        _saveIndex = this.text.length();
                        this.match('\'');
                        this.text.setLength(_saveIndex);
                        break label135;
                     }

                     this.mSTRING_CH(false);
                  }
               }
            }
         } else {
            if (this.LA(1) != '"' || this.LA(2) < 0 || this.LA(2) > '\ufffe') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            _saveIndex = this.text.length();
            this.match('"');
            this.text.setLength(_saveIndex);
            if (this.inputState.guessing == 0) {
               ++this.suppressNewline;
            }

            tt = this.mSTRING_CTOR_END(false, true, false);
            if (this.inputState.guessing == 0) {
               _ttype = tt;
            }
         }
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mSTRING_CH(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 207;
      this.match(_tokenSet_4);
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 211;
      int _saveIndex;
      if (this.LA(1) != '\\' || this.LA(2) != '"' && this.LA(2) != '$' && this.LA(2) != '\'' && this.LA(2) != '0' && this.LA(2) != '1' && this.LA(2) != '2' && this.LA(2) != '3' && this.LA(2) != '4' && this.LA(2) != '5' && this.LA(2) != '6' && this.LA(2) != '7' && this.LA(2) != '\\' && this.LA(2) != 'b' && this.LA(2) != 'f' && this.LA(2) != 'n' && this.LA(2) != 'r' && this.LA(2) != 't' && this.LA(2) != 'u') {
         if (this.LA(1) != '\\' || this.LA(2) != '\n' && this.LA(2) != '\r') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         _saveIndex = this.text.length();
         this.match('\\');
         this.text.setLength(_saveIndex);
         _saveIndex = this.text.length();
         this.mONE_NL(false, false);
         this.text.setLength(_saveIndex);
      } else {
         _saveIndex = this.text.length();
         this.match('\\');
         this.text.setLength(_saveIndex);
         char ch;
         switch(this.LA(1)) {
         case '"':
            this.match('"');
            break;
         case '$':
            this.match('$');
            break;
         case '\'':
            this.match('\'');
            break;
         case '0':
         case '1':
         case '2':
         case '3':
            this.matchRange('0', '3');
            if (this.LA(1) >= '0' && this.LA(1) <= '7' && this.LA(2) >= 0 && this.LA(2) <= '\ufffe') {
               this.matchRange('0', '7');
               if (this.LA(1) >= '0' && this.LA(1) <= '7' && this.LA(2) >= 0 && this.LA(2) <= '\ufffe') {
                  this.matchRange('0', '7');
               } else if (this.LA(1) < 0 || this.LA(1) > '\ufffe') {
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            } else if (this.LA(1) < 0 || this.LA(1) > '\ufffe') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            if (this.inputState.guessing == 0) {
               ch = (char)Integer.parseInt(new String(this.text.getBuffer(), _begin, this.text.length() - _begin), 8);
               this.text.setLength(_begin);
               this.text.append(ch);
            }
            break;
         case '4':
         case '5':
         case '6':
         case '7':
            this.matchRange('4', '7');
            if (this.LA(1) >= '0' && this.LA(1) <= '7' && this.LA(2) >= 0 && this.LA(2) <= '\ufffe') {
               this.matchRange('0', '7');
            } else if (this.LA(1) < 0 || this.LA(1) > '\ufffe') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            if (this.inputState.guessing == 0) {
               ch = (char)Integer.parseInt(new String(this.text.getBuffer(), _begin, this.text.length() - _begin), 8);
               this.text.setLength(_begin);
               this.text.append(ch);
            }
            break;
         case '\\':
            this.match('\\');
            break;
         case 'b':
            this.match('b');
            if (this.inputState.guessing == 0) {
               this.text.setLength(_begin);
               this.text.append("\b");
            }
            break;
         case 'f':
            this.match('f');
            if (this.inputState.guessing == 0) {
               this.text.setLength(_begin);
               this.text.append("\f");
            }
            break;
         case 'n':
            this.match('n');
            if (this.inputState.guessing == 0) {
               this.text.setLength(_begin);
               this.text.append("\n");
            }
            break;
         case 'r':
            this.match('r');
            if (this.inputState.guessing == 0) {
               this.text.setLength(_begin);
               this.text.append("\r");
            }
            break;
         case 't':
            this.match('t');
            if (this.inputState.guessing == 0) {
               this.text.setLength(_begin);
               this.text.append("\t");
            }
            break;
         case 'u':
            int _cnt674;
            for(_cnt674 = 0; this.LA(1) == 'u'; ++_cnt674) {
               this.match('u');
            }

            if (_cnt674 < 1) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            if (this.inputState.guessing == 0) {
               this.text.setLength(_begin);
               this.text.append("");
            }

            this.mHEX_DIGIT(false);
            this.mHEX_DIGIT(false);
            this.mHEX_DIGIT(false);
            this.mHEX_DIGIT(false);
            if (this.inputState.guessing == 0) {
               ch = (char)Integer.parseInt(new String(this.text.getBuffer(), _begin, this.text.length() - _begin), 16);
               this.text.setLength(_begin);
               this.text.append(ch);
            }
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mSTRING_NL(boolean _createToken, boolean allowNewline) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 212;
      if (this.inputState.guessing == 0 && !allowNewline) {
         throw new MismatchedCharException('\n', '\n', true, this);
      } else {
         this.mONE_NL(false, false);
         if (this.inputState.guessing == 0) {
            this.text.setLength(_begin);
            this.text.append('\n');
         }

         if (_createToken && _token == null && _ttype != -1) {
            _token = this.makeToken(_ttype);
            _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
         }

         this._returnToken = _token;
      }
   }

   protected final int mSTRING_CTOR_END(boolean _createToken, boolean fromStart, boolean tripleQuote) throws RecognitionException, CharStreamException, TokenStreamException {
      int tt = 194;
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 194;
      boolean dollarOK = false;

      while(true) {
         while(true) {
            switch(this.LA(1)) {
            case '\n':
            case '\r':
               this.mSTRING_NL(false, tripleQuote);
               break;
            case '\'':
               this.match('\'');
               break;
            case '\\':
               this.mESC(false);
               break;
            default:
               boolean synPredMatched654 = false;
               if (this.LA(1) == '"' && this.LA(2) >= 0 && this.LA(2) <= '\ufffe' && tripleQuote) {
                  int _m654 = this.mark();
                  synPredMatched654 = true;
                  ++this.inputState.guessing;

                  try {
                     this.match('"');
                     if (_tokenSet_5.member(this.LA(1))) {
                        this.matchNot('"');
                     } else {
                        if (this.LA(1) != '"') {
                           throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                        }

                        this.match('"');
                        this.matchNot('"');
                     }
                  } catch (RecognitionException var13) {
                     synPredMatched654 = false;
                  }

                  this.rewind(_m654);
                  --this.inputState.guessing;
               }

               if (synPredMatched654) {
                  this.match('"');
               } else {
                  if (!_tokenSet_4.member(this.LA(1))) {
                     int _saveIndex;
                     switch(this.LA(1)) {
                     case '"':
                        if (this.LA(1) == '"' && this.LA(2) == '"' && tripleQuote) {
                           _saveIndex = this.text.length();
                           this.match("\"\"\"");
                           this.text.setLength(_saveIndex);
                        } else {
                           if (this.LA(1) != '"' || tripleQuote) {
                              throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                           }

                           _saveIndex = this.text.length();
                           this.match("\"");
                           this.text.setLength(_saveIndex);
                        }

                        if (this.inputState.guessing == 0) {
                           if (fromStart) {
                              tt = 85;
                           }

                           if (!tripleQuote) {
                              --this.suppressNewline;
                           }
                        }
                        break;
                     case '$':
                        if (this.inputState.guessing == 0) {
                           dollarOK = this.atValidDollarEscape();
                        }

                        _saveIndex = this.text.length();
                        this.match('$');
                        this.text.setLength(_saveIndex);
                        if (this.inputState.guessing == 0) {
                           this.require(dollarOK, "illegal string body character after dollar sign", "either escape a literal dollar sign \"\\$5\" or bracket the value expression \"${5}\"");
                           tt = fromStart ? 193 : 48;
                           this.stringCtorState = 4 + (tripleQuote ? 1 : 0);
                        }
                        break;
                     default:
                        throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                     }

                     if (this.inputState.guessing == 0) {
                        _ttype = tt;
                     }

                     if (_createToken && _token == null && _ttype != -1) {
                        _token = this.makeToken(_ttype);
                        _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
                     }

                     this._returnToken = _token;
                     return tt;
                  }

                  this.mSTRING_CH(false);
               }
            }
         }
      }
   }

   public final void mREGEXP_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 208;
      int tt = 0;
      if (this.LA(1) == '/' && _tokenSet_6.member(this.LA(2)) && this.allowRegexpLiteral()) {
         int _saveIndex = this.text.length();
         this.match('/');
         this.text.setLength(_saveIndex);
         if (this.inputState.guessing == 0) {
            ++this.suppressNewline;
         }

         if (this.LA(1) == '$' && _tokenSet_2.member(this.LA(2)) && !this.atValidDollarEscape()) {
            this.match('$');
            tt = this.mREGEXP_CTOR_END(false, true);
         } else if (_tokenSet_7.member(this.LA(1))) {
            this.mREGEXP_SYMBOL(false);
            tt = this.mREGEXP_CTOR_END(false, true);
         } else {
            if (this.LA(1) != '$') {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            _saveIndex = this.text.length();
            this.match('$');
            this.text.setLength(_saveIndex);
            if (this.inputState.guessing == 0) {
               tt = 193;
               this.stringCtorState = 6;
            }
         }

         if (this.inputState.guessing == 0) {
            _ttype = tt;
         }
      } else if (this.LA(1) == '/' && this.LA(2) == '=') {
         this.mDIV_ASSIGN(false);
         if (this.inputState.guessing == 0) {
            _ttype = 160;
         }
      } else {
         if (this.LA(1) != '/') {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.mDIV(false);
         if (this.inputState.guessing == 0) {
            _ttype = 187;
         }
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mREGEXP_SYMBOL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 210;
      if (this.LA(1) == '\\' && this.LA(2) == '/' && _tokenSet_1.member(this.LA(3))) {
         this.match('\\');
         this.match('/');
         if (this.inputState.guessing == 0) {
            this.text.setLength(_begin);
            this.text.append('/');
         }
      } else if (this.LA(1) == '\\' && _tokenSet_1.member(this.LA(2)) && this.LA(2) != '/' && this.LA(2) != '\n' && this.LA(2) != '\r') {
         this.match('\\');
      } else if (this.LA(1) != '\\' || this.LA(2) != '\n' && this.LA(2) != '\r') {
         if (!_tokenSet_8.member(this.LA(1))) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.match(_tokenSet_8);
      } else {
         int _saveIndex = this.text.length();
         this.match('\\');
         this.text.setLength(_saveIndex);
         _saveIndex = this.text.length();
         this.mONE_NL(false, false);
         this.text.setLength(_saveIndex);
         if (this.inputState.guessing == 0) {
            this.text.setLength(_begin);
            this.text.append('\n');
         }
      }

      while(this.LA(1) == '*') {
         this.match('*');
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final int mREGEXP_CTOR_END(boolean _createToken, boolean fromStart) throws RecognitionException, CharStreamException, TokenStreamException {
      int tt = 194;
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 209;

      while(true) {
         while(this.LA(1) != '$' || !_tokenSet_2.member(this.LA(2)) || this.atValidDollarEscape()) {
            if (!_tokenSet_7.member(this.LA(1))) {
               int _saveIndex;
               switch(this.LA(1)) {
               case '$':
                  _saveIndex = this.text.length();
                  this.match('$');
                  this.text.setLength(_saveIndex);
                  if (this.inputState.guessing == 0) {
                     tt = fromStart ? 193 : 48;
                     this.stringCtorState = 6;
                  }
                  break;
               case '/':
                  _saveIndex = this.text.length();
                  this.match('/');
                  this.text.setLength(_saveIndex);
                  if (this.inputState.guessing == 0) {
                     if (fromStart) {
                        tt = 85;
                     }

                     --this.suppressNewline;
                  }
                  break;
               default:
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }

               if (this.inputState.guessing == 0) {
                  _ttype = tt;
               }

               if (_createToken && _token == null && _ttype != -1) {
                  _token = this.makeToken(_ttype);
                  _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
               }

               this._returnToken = _token;
               return tt;
            }

            this.mREGEXP_SYMBOL(false);
         }

         this.match('$');
      }
   }

   protected final void mHEX_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 213;
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
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
         this.matchRange('A', 'F');
         break;
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
         this.matchRange('a', 'f');
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mVOCAB(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 214;
      this.matchRange('\u0003', 'ÿ');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mIDENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 84;
      if (_tokenSet_0.member(this.LA(1)) && this.stringCtorState == 0) {
         if (this.LA(1) == '$') {
            this.mDOLLAR(false);
         } else {
            if (!_tokenSet_9.member(this.LA(1))) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            this.mLETTER(false);
         }

         label110:
         while(true) {
            while(true) {
               switch(this.LA(1)) {
               case '$':
                  this.mDOLLAR(false);
                  break;
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
               default:
                  if (!_tokenSet_9.member(this.LA(1))) {
                     break label110;
                  }

                  this.mLETTER(false);
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
                  this.mDIGIT(false);
               }
            }
         }
      } else {
         if (!_tokenSet_9.member(this.LA(1))) {
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         this.mLETTER(false);

         label99:
         while(true) {
            while(!_tokenSet_9.member(this.LA(1))) {
               if (this.LA(1) < '0' || this.LA(1) > '9') {
                  break label99;
               }

               this.mDIGIT(false);
            }

            this.mLETTER(false);
         }
      }

      if (this.inputState.guessing == 0) {
         if (this.stringCtorState != 0) {
            if (this.LA(1) == '.' && this.LA(2) != '$' && Character.isJavaIdentifierStart(this.LA(2))) {
               this.restartStringCtor(false);
            } else {
               this.restartStringCtor(true);
            }
         }

         int ttype = this.testLiteralsTable(84);
         if ((ttype == 110 || ttype == 81 || ttype == 137) && (this.LA(1) == '.' || this.lastSigTokenType == 87 || this.lastSigTokenType == 78)) {
            ttype = 84;
         }

         if (ttype == 80 && this.LA(1) == '.') {
            ttype = 84;
         }

         _ttype = ttype;
         if (this.assertEnabled && "assert".equals(new String(this.text.getBuffer(), _begin, this.text.length() - _begin))) {
            _ttype = 142;
         }

         if (this.enumEnabled && "enum".equals(new String(this.text.getBuffer(), _begin, this.text.length() - _begin))) {
            _ttype = 91;
         }
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mLETTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 215;
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
      case '{':
      case '|':
      case '}':
      case '~':
      case '\u007f':
      case '\u0080':
      case '\u0081':
      case '\u0082':
      case '\u0083':
      case '\u0084':
      case '\u0085':
      case '\u0086':
      case '\u0087':
      case '\u0088':
      case '\u0089':
      case '\u008a':
      case '\u008b':
      case '\u008c':
      case '\u008d':
      case '\u008e':
      case '\u008f':
      case '\u0090':
      case '\u0091':
      case '\u0092':
      case '\u0093':
      case '\u0094':
      case '\u0095':
      case '\u0096':
      case '\u0097':
      case '\u0098':
      case '\u0099':
      case '\u009a':
      case '\u009b':
      case '\u009c':
      case '\u009d':
      case '\u009e':
      case '\u009f':
      case ' ':
      case '¡':
      case '¢':
      case '£':
      case '¤':
      case '¥':
      case '¦':
      case '§':
      case '¨':
      case '©':
      case 'ª':
      case '«':
      case '¬':
      case '\u00ad':
      case '®':
      case '¯':
      case '°':
      case '±':
      case '²':
      case '³':
      case '´':
      case 'µ':
      case '¶':
      case '·':
      case '¸':
      case '¹':
      case 'º':
      case '»':
      case '¼':
      case '½':
      case '¾':
      case '¿':
      case '×':
      case '÷':
      default:
         if (this.LA(1) >= 256 && this.LA(1) <= '\ufffe') {
            this.matchRange('Ā', '\ufffe');
            break;
         }

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
         break;
      case 'À':
      case 'Á':
      case 'Â':
      case 'Ã':
      case 'Ä':
      case 'Å':
      case 'Æ':
      case 'Ç':
      case 'È':
      case 'É':
      case 'Ê':
      case 'Ë':
      case 'Ì':
      case 'Í':
      case 'Î':
      case 'Ï':
      case 'Ð':
      case 'Ñ':
      case 'Ò':
      case 'Ó':
      case 'Ô':
      case 'Õ':
      case 'Ö':
         this.matchRange('À', 'Ö');
         break;
      case 'Ø':
      case 'Ù':
      case 'Ú':
      case 'Û':
      case 'Ü':
      case 'Ý':
      case 'Þ':
      case 'ß':
      case 'à':
      case 'á':
      case 'â':
      case 'ã':
      case 'ä':
      case 'å':
      case 'æ':
      case 'ç':
      case 'è':
      case 'é':
      case 'ê':
      case 'ë':
      case 'ì':
      case 'í':
      case 'î':
      case 'ï':
      case 'ð':
      case 'ñ':
      case 'ò':
      case 'ó':
      case 'ô':
      case 'õ':
      case 'ö':
         this.matchRange('Ø', 'ö');
         break;
      case 'ø':
      case 'ù':
      case 'ú':
      case 'û':
      case 'ü':
      case 'ý':
      case 'þ':
      case 'ÿ':
         this.matchRange('ø', 'ÿ');
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 216;
      this.matchRange('0', '9');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mNUM_INT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 195;
      Token f2 = null;
      Token g2 = null;
      Token f3 = null;
      Token g3 = null;
      Token f4 = null;
      boolean isDecimal = false;
      Token t = null;
      int _cnt718;
      boolean synPredMatched715;
      switch(this.LA(1)) {
      case '0':
         this.match('0');
         if (this.inputState.guessing == 0) {
            isDecimal = true;
         }

         if (this.LA(1) != 'X' && this.LA(1) != 'x') {
            synPredMatched715 = false;
            if (this.LA(1) >= '0' && this.LA(1) <= '9') {
               _cnt718 = this.mark();
               synPredMatched715 = true;
               ++this.inputState.guessing;

               try {
                  int _cnt699;
                  for(_cnt699 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++_cnt699) {
                     this.matchRange('0', '9');
                  }

                  if (_cnt699 < 1) {
                     throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                  }

                  switch(this.LA(1)) {
                  case '.':
                     this.match('.');
                     this.matchRange('0', '9');
                     break;
                  case 'D':
                  case 'F':
                  case 'd':
                  case 'f':
                     this.mFLOAT_SUFFIX(false);
                     break;
                  case 'E':
                  case 'e':
                     this.mEXPONENT(false);
                     break;
                  default:
                     throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                  }
               } catch (RecognitionException var16) {
                  synPredMatched715 = false;
               }

               this.rewind(_cnt718);
               --this.inputState.guessing;
            }

            if (synPredMatched715) {
               for(_cnt718 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++_cnt718) {
                  this.matchRange('0', '9');
               }

               if (_cnt718 < 1) {
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }
            } else if (this.LA(1) >= '0' && this.LA(1) <= '7') {
               for(_cnt718 = 0; this.LA(1) >= '0' && this.LA(1) <= '7'; ++_cnt718) {
                  this.matchRange('0', '7');
               }

               if (_cnt718 < 1) {
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }

               if (this.inputState.guessing == 0) {
                  isDecimal = false;
               }
            }
         } else {
            switch(this.LA(1)) {
            case 'X':
               this.match('X');
               break;
            case 'x':
               this.match('x');
               break;
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            if (this.inputState.guessing == 0) {
               isDecimal = false;
            }

            int _cnt696;
            for(_cnt696 = 0; _tokenSet_10.member(this.LA(1)); ++_cnt696) {
               this.mHEX_DIGIT(false);
            }

            if (_cnt696 < 1) {
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }
         }
         break;
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
         this.matchRange('1', '9');

         while(this.LA(1) >= '0' && this.LA(1) <= '9') {
            this.matchRange('0', '9');
         }

         if (this.inputState.guessing == 0) {
            isDecimal = true;
         }
         break;
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      switch(this.LA(1)) {
      case 'G':
      case 'g':
         this.mBIG_SUFFIX(false);
         if (this.inputState.guessing == 0) {
            _ttype = 199;
         }
         break;
      case 'I':
      case 'i':
         switch(this.LA(1)) {
         case 'I':
            this.match('I');
            break;
         case 'i':
            this.match('i');
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         if (this.inputState.guessing == 0) {
            _ttype = 195;
         }
         break;
      case 'L':
      case 'l':
         switch(this.LA(1)) {
         case 'L':
            this.match('L');
            break;
         case 'l':
            this.match('l');
            break;
         default:
            throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
         }

         if (this.inputState.guessing == 0) {
            _ttype = 197;
         }
         break;
      default:
         synPredMatched715 = false;
         if ((this.LA(1) == '.' || this.LA(1) == 'D' || this.LA(1) == 'E' || this.LA(1) == 'F' || this.LA(1) == 'd' || this.LA(1) == 'e' || this.LA(1) == 'f') && isDecimal) {
            _cnt718 = this.mark();
            synPredMatched715 = true;
            ++this.inputState.guessing;

            try {
               if (_tokenSet_11.member(this.LA(1))) {
                  this.matchNot('.');
               } else {
                  if (this.LA(1) != '.') {
                     throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
                  }

                  this.match('.');
                  this.matchRange('0', '9');
               }
            } catch (RecognitionException var15) {
               synPredMatched715 = false;
            }

            this.rewind(_cnt718);
            --this.inputState.guessing;
         }

         if (synPredMatched715) {
            label220:
            switch(this.LA(1)) {
            case '.':
               this.match('.');

               for(_cnt718 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++_cnt718) {
                  this.matchRange('0', '9');
               }

               if (_cnt718 < 1) {
                  throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
               }

               if (this.LA(1) == 'E' || this.LA(1) == 'e') {
                  this.mEXPONENT(false);
               }

               switch(this.LA(1)) {
               case 'D':
               case 'F':
               case 'd':
               case 'f':
                  this.mFLOAT_SUFFIX(true);
                  f2 = this._returnToken;
                  if (this.inputState.guessing == 0) {
                     t = f2;
                  }
                  break label220;
               case 'G':
               case 'g':
                  this.mBIG_SUFFIX(true);
                  g2 = this._returnToken;
                  if (this.inputState.guessing == 0) {
                     t = g2;
                  }
               default:
                  break label220;
               }
            case 'D':
            case 'F':
            case 'd':
            case 'f':
               this.mFLOAT_SUFFIX(true);
               f4 = this._returnToken;
               if (this.inputState.guessing == 0) {
                  t = f4;
               }
               break;
            case 'E':
            case 'e':
               this.mEXPONENT(false);
               switch(this.LA(1)) {
               case 'D':
               case 'F':
               case 'd':
               case 'f':
                  this.mFLOAT_SUFFIX(true);
                  f3 = this._returnToken;
                  if (this.inputState.guessing == 0) {
                     t = f3;
                  }
                  break label220;
               case 'G':
               case 'g':
                  this.mBIG_SUFFIX(true);
                  g3 = this._returnToken;
                  if (this.inputState.guessing == 0) {
                     t = g3;
                  }
               default:
                  break label220;
               }
            default:
               throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
            }

            if (this.inputState.guessing == 0) {
               String txt = t == null ? "" : t.getText().toUpperCase();
               if (txt.indexOf(70) >= 0) {
                  _ttype = 196;
               } else if (txt.indexOf(71) >= 0) {
                  _ttype = 200;
               } else {
                  _ttype = 198;
               }
            }
         }
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mEXPONENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 217;
      switch(this.LA(1)) {
      case 'E':
         this.match('E');
         break;
      case 'e':
         this.match('e');
         break;
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      switch(this.LA(1)) {
      case '+':
         this.match('+');
         break;
      case ',':
      case '.':
      case '/':
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      case '-':
         this.match('-');
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
      }

      int _cnt727;
      for(_cnt727 = 0; this.LA(1) >= '0' && this.LA(1) <= '9'; ++_cnt727) {
         this.matchRange('0', '9');
      }

      if (_cnt727 >= 1) {
         if (_createToken && _token == null && _ttype != -1) {
            _token = this.makeToken(_ttype);
            _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
         }

         this._returnToken = _token;
      } else {
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }
   }

   protected final void mFLOAT_SUFFIX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 218;
      switch(this.LA(1)) {
      case 'D':
         this.match('D');
         break;
      case 'F':
         this.match('F');
         break;
      case 'd':
         this.match('d');
         break;
      case 'f':
         this.match('f');
         break;
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   protected final void mBIG_SUFFIX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 219;
      switch(this.LA(1)) {
      case 'G':
         this.match('G');
         break;
      case 'g':
         this.match('g');
         break;
      default:
         throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
      }

      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   public final void mAT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
      Token _token = null;
      int _begin = this.text.length();
      int _ttype = 92;
      this.match('@');
      if (_createToken && _token == null && _ttype != -1) {
         _token = this.makeToken(_ttype);
         _token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
      }

      this._returnToken = _token;
   }

   private static final long[] mk_tokenSet_0() {
      long[] data = new long[2560];
      data[0] = 68719476736L;
      data[1] = 576460745995190270L;
      data[3] = -36028797027352577L;

      for(int i = 4; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_1() {
      long[] data = new long[2048];
      data[0] = -9217L;

      for(int i = 1; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_2() {
      long[] data = new long[2048];
      data[0] = -4398046520321L;

      for(int i = 1; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_3() {
      long[] data = new long[2048];
      data[0] = -549755813889L;

      for(int i = 1; i <= 1023; ++i) {
         data[i] = -1L;
      }

      return data;
   }

   private static final long[] mk_tokenSet_4() {
      long[] data = new long[2048];
      data[0] = -635655169025L;
      data[1] = -268435457L;

      for(int i = 2; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_5() {
      long[] data = new long[2048];
      data[0] = -17179869185L;

      for(int i = 1; i <= 1023; ++i) {
         data[i] = -1L;
      }

      return data;
   }

   private static final long[] mk_tokenSet_6() {
      long[] data = new long[2048];
      data[0] = -145135534875649L;

      for(int i = 1; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_7() {
      long[] data = new long[2048];
      data[0] = -145204254352385L;

      for(int i = 1; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_8() {
      long[] data = new long[2048];
      data[0] = -145204254352385L;
      data[1] = -268435457L;

      for(int i = 2; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_9() {
      long[] data = new long[2560];
      data[1] = 576460745995190270L;
      data[3] = -36028797027352577L;

      for(int i = 4; i <= 1022; ++i) {
         data[i] = -1L;
      }

      data[1023] = Long.MAX_VALUE;
      return data;
   }

   private static final long[] mk_tokenSet_10() {
      long[] data = new long[1025];
      data[0] = 287948901175001088L;
      data[1] = 541165879422L;
      return data;
   }

   private static final long[] mk_tokenSet_11() {
      long[] data = new long[2048];
      data[0] = -70368744177665L;

      for(int i = 1; i <= 1023; ++i) {
         data[i] = -1L;
      }

      return data;
   }
}
