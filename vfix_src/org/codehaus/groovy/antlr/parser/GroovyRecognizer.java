package org.codehaus.groovy.antlr.parser;

import groovyjarjarantlr.ASTFactory;
import groovyjarjarantlr.ASTPair;
import groovyjarjarantlr.CommonToken;
import groovyjarjarantlr.InputBuffer;
import groovyjarjarantlr.LLkParser;
import groovyjarjarantlr.LexerSharedInputState;
import groovyjarjarantlr.NoViableAltException;
import groovyjarjarantlr.ParserSharedInputState;
import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.SemanticException;
import groovyjarjarantlr.Token;
import groovyjarjarantlr.TokenBuffer;
import groovyjarjarantlr.TokenStream;
import groovyjarjarantlr.TokenStreamException;
import groovyjarjarantlr.TokenStreamRecognitionException;
import groovyjarjarantlr.collections.AST;
import groovyjarjarantlr.collections.impl.ASTArray;
import groovyjarjarantlr.collections.impl.BitSet;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.SourceInfo;

public class GroovyRecognizer extends LLkParser implements GroovyTokenTypes {
   private static GroovySourceAST dummyVariableToforceClassLoaderToFindASTClass = new GroovySourceAST();
   List warningList;
   GroovyLexer lexer;
   private SourceBuffer sourceBuffer;
   public static boolean tracing = false;
   private AST currentClass;
   private int sepToken;
   private boolean argListHasLabels;
   private AST lastPathExpression;
   private final int LC_STMT;
   private final int LC_INIT;
   private int ltCounter;
   private static final boolean ANTLR_LOOP_EXIT = false;
   public static final String[] _tokenNames = new String[]{"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "BLOCK", "MODIFIERS", "OBJBLOCK", "SLIST", "METHOD_DEF", "VARIABLE_DEF", "INSTANCE_INIT", "STATIC_INIT", "TYPE", "CLASS_DEF", "INTERFACE_DEF", "PACKAGE_DEF", "ARRAY_DECLARATOR", "EXTENDS_CLAUSE", "IMPLEMENTS_CLAUSE", "PARAMETERS", "PARAMETER_DEF", "LABELED_STAT", "TYPECAST", "INDEX_OP", "POST_INC", "POST_DEC", "METHOD_CALL", "EXPR", "IMPORT", "UNARY_MINUS", "UNARY_PLUS", "CASE_GROUP", "ELIST", "FOR_INIT", "FOR_CONDITION", "FOR_ITERATOR", "EMPTY_STAT", "\"final\"", "\"abstract\"", "\"goto\"", "\"const\"", "\"do\"", "\"strictfp\"", "SUPER_CTOR_CALL", "CTOR_CALL", "CTOR_IDENT", "VARIABLE_PARAMETER_DEF", "STRING_CONSTRUCTOR", "STRING_CTOR_MIDDLE", "CLOSABLE_BLOCK", "IMPLICIT_PARAMETERS", "SELECT_SLOT", "DYNAMIC_MEMBER", "LABELED_ARG", "SPREAD_ARG", "SPREAD_MAP_ARG", "LIST_CONSTRUCTOR", "MAP_CONSTRUCTOR", "FOR_IN_ITERABLE", "STATIC_IMPORT", "ENUM_DEF", "ENUM_CONSTANT_DEF", "FOR_EACH_CLAUSE", "ANNOTATION_DEF", "ANNOTATIONS", "ANNOTATION", "ANNOTATION_MEMBER_VALUE_PAIR", "ANNOTATION_FIELD_DEF", "ANNOTATION_ARRAY_INIT", "TYPE_ARGUMENTS", "TYPE_ARGUMENT", "TYPE_PARAMETERS", "TYPE_PARAMETER", "WILDCARD_TYPE", "TYPE_UPPER_BOUNDS", "TYPE_LOWER_BOUNDS", "CLOSURE_LIST", "a script header", "\"package\"", "\"import\"", "\"static\"", "\"def\"", "'['", "']'", "an identifier", "a string literal", "'<'", "'.'", "'('", "\"class\"", "\"interface\"", "\"enum\"", "'@'", "'?'", "\"extends\"", "\"super\"", "','", "'>'", "'>>'", "'>>>'", "\"void\"", "\"boolean\"", "\"byte\"", "\"char\"", "\"short\"", "\"int\"", "\"float\"", "\"long\"", "\"double\"", "'*'", "\"as\"", "\"private\"", "\"public\"", "\"protected\"", "\"transient\"", "\"native\"", "\"threadsafe\"", "\"synchronized\"", "\"volatile\"", "')'", "'='", "'&'", "'{'", "'}'", "';'", "\"default\"", "\"throws\"", "\"implements\"", "\"this\"", "'...'", "'->'", "':'", "\"if\"", "\"else\"", "\"while\"", "\"switch\"", "\"for\"", "\"in\"", "\"return\"", "\"break\"", "\"continue\"", "\"throw\"", "\"assert\"", "'+'", "'-'", "\"case\"", "\"try\"", "\"finally\"", "\"catch\"", "'*.'", "'?.'", "'.&'", "\"false\"", "\"instanceof\"", "\"new\"", "\"null\"", "\"true\"", "'+='", "'-='", "'*='", "'/='", "'%='", "'>>='", "'>>>='", "'<<='", "'&='", "'^='", "'|='", "'**='", "'?:'", "'||'", "'&&'", "'|'", "'^'", "'=~'", "'==~'", "'!='", "'=='", "'==='", "'!=='", "'<=>'", "'<='", "'>='", "'<<'", "'..'", "'..<'", "'++'", "'/'", "'%'", "'--'", "'**'", "'~'", "'!'", "STRING_CTOR_START", "a string literal end", "a numeric literal", "NUM_FLOAT", "NUM_LONG", "NUM_DOUBLE", "NUM_BIG_INT", "NUM_BIG_DECIMAL", "some newlines, whitespace or comments", "'$'", "whitespace", "a newline", "a single line comment", "a comment", "a string character", "a regular expression literal", "a regular expression literal end", "a regular expression character", "an escape sequence", "a newline inside a string", "a hexadecimal digit", "a character", "a letter", "a digit", "an exponent", "a float or double suffix", "a big decimal suffix"};
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
   public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
   public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
   public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
   public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
   public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
   public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
   public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
   public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
   public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
   public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
   public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
   public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
   public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
   public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
   public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
   public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
   public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
   public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
   public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
   public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
   public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
   public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
   public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());
   public static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());
   public static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());
   public static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());
   public static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());
   public static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());
   public static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());
   public static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());
   public static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());
   public static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());
   public static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());
   public static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());
   public static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());
   public static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());
   public static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());
   public static final BitSet _tokenSet_64 = new BitSet(mk_tokenSet_64());
   public static final BitSet _tokenSet_65 = new BitSet(mk_tokenSet_65());
   public static final BitSet _tokenSet_66 = new BitSet(mk_tokenSet_66());
   public static final BitSet _tokenSet_67 = new BitSet(mk_tokenSet_67());
   public static final BitSet _tokenSet_68 = new BitSet(mk_tokenSet_68());
   public static final BitSet _tokenSet_69 = new BitSet(mk_tokenSet_69());
   public static final BitSet _tokenSet_70 = new BitSet(mk_tokenSet_70());
   public static final BitSet _tokenSet_71 = new BitSet(mk_tokenSet_71());
   public static final BitSet _tokenSet_72 = new BitSet(mk_tokenSet_72());
   public static final BitSet _tokenSet_73 = new BitSet(mk_tokenSet_73());
   public static final BitSet _tokenSet_74 = new BitSet(mk_tokenSet_74());
   public static final BitSet _tokenSet_75 = new BitSet(mk_tokenSet_75());
   public static final BitSet _tokenSet_76 = new BitSet(mk_tokenSet_76());
   public static final BitSet _tokenSet_77 = new BitSet(mk_tokenSet_77());
   public static final BitSet _tokenSet_78 = new BitSet(mk_tokenSet_78());
   public static final BitSet _tokenSet_79 = new BitSet(mk_tokenSet_79());
   public static final BitSet _tokenSet_80 = new BitSet(mk_tokenSet_80());
   public static final BitSet _tokenSet_81 = new BitSet(mk_tokenSet_81());
   public static final BitSet _tokenSet_82 = new BitSet(mk_tokenSet_82());
   public static final BitSet _tokenSet_83 = new BitSet(mk_tokenSet_83());
   public static final BitSet _tokenSet_84 = new BitSet(mk_tokenSet_84());
   public static final BitSet _tokenSet_85 = new BitSet(mk_tokenSet_85());
   public static final BitSet _tokenSet_86 = new BitSet(mk_tokenSet_86());
   public static final BitSet _tokenSet_87 = new BitSet(mk_tokenSet_87());
   public static final BitSet _tokenSet_88 = new BitSet(mk_tokenSet_88());
   public static final BitSet _tokenSet_89 = new BitSet(mk_tokenSet_89());
   public static final BitSet _tokenSet_90 = new BitSet(mk_tokenSet_90());
   public static final BitSet _tokenSet_91 = new BitSet(mk_tokenSet_91());
   public static final BitSet _tokenSet_92 = new BitSet(mk_tokenSet_92());
   public static final BitSet _tokenSet_93 = new BitSet(mk_tokenSet_93());
   public static final BitSet _tokenSet_94 = new BitSet(mk_tokenSet_94());
   public static final BitSet _tokenSet_95 = new BitSet(mk_tokenSet_95());
   public static final BitSet _tokenSet_96 = new BitSet(mk_tokenSet_96());
   public static final BitSet _tokenSet_97 = new BitSet(mk_tokenSet_97());
   public static final BitSet _tokenSet_98 = new BitSet(mk_tokenSet_98());
   public static final BitSet _tokenSet_99 = new BitSet(mk_tokenSet_99());
   public static final BitSet _tokenSet_100 = new BitSet(mk_tokenSet_100());
   public static final BitSet _tokenSet_101 = new BitSet(mk_tokenSet_101());
   public static final BitSet _tokenSet_102 = new BitSet(mk_tokenSet_102());
   public static final BitSet _tokenSet_103 = new BitSet(mk_tokenSet_103());

   public static GroovyRecognizer make(GroovyLexer lexer) {
      GroovyRecognizer parser = new GroovyRecognizer(lexer.plumb());
      parser.lexer = lexer;
      lexer.parser = parser;
      parser.getASTFactory().setASTNodeClass(GroovySourceAST.class);
      parser.warningList = new ArrayList();
      return parser;
   }

   public static GroovyRecognizer make(InputStream in) {
      return make(new GroovyLexer(in));
   }

   public static GroovyRecognizer make(Reader in) {
      return make(new GroovyLexer(in));
   }

   public static GroovyRecognizer make(InputBuffer in) {
      return make(new GroovyLexer(in));
   }

   public static GroovyRecognizer make(LexerSharedInputState in) {
      return make(new GroovyLexer(in));
   }

   public List getWarningList() {
      return this.warningList;
   }

   public GroovyLexer getLexer() {
      return this.lexer;
   }

   public void setFilename(String f) {
      super.setFilename(f);
      this.lexer.setFilename(f);
   }

   public void setSourceBuffer(SourceBuffer sourceBuffer) {
      this.sourceBuffer = sourceBuffer;
   }

   public AST create(int type, String txt, AST first) {
      AST t = this.astFactory.create(type, txt);
      if (t != null && first != null) {
         t.initialize(first);
         t.initialize(type, txt);
      }

      return t;
   }

   private AST attachLast(AST t, Object last) {
      if (t instanceof GroovySourceAST && last instanceof SourceInfo) {
         SourceInfo lastInfo = (SourceInfo)last;
         GroovySourceAST node = (GroovySourceAST)t;
         node.setColumnLast(lastInfo.getColumn());
         node.setLineLast(lastInfo.getLine());
      }

      return t;
   }

   public AST create(int type, String txt, Token first, Token last) {
      return this.attachLast(this.create(type, txt, this.astFactory.create(first)), last);
   }

   public AST create(int type, String txt, AST first, Token last) {
      return this.attachLast(this.create(type, txt, first), last);
   }

   public AST create(int type, String txt, AST first, AST last) {
      return this.attachLast(this.create(type, txt, first), last);
   }

   public Token cloneToken(Token t) {
      CommonToken clone = new CommonToken(t.getType(), t.getText());
      clone.setLine(t.getLine());
      clone.setColumn(t.getColumn());
      return clone;
   }

   public void traceIn(String rname) throws TokenStreamException {
      if (tracing) {
         super.traceIn(rname);
      }
   }

   public void traceOut(String rname) throws TokenStreamException {
      if (tracing) {
         if (this.returnAST != null) {
            rname = rname + this.returnAST.toStringList();
         }

         super.traceOut(rname);
      }
   }

   public void requireFailed(String problem, String solution) throws SemanticException {
      Token lt = null;
      int lineNum = Token.badToken.getLine();
      int colNum = Token.badToken.getColumn();

      try {
         lt = this.LT(1);
         if (lt != null) {
            lineNum = lt.getLine();
            colNum = lt.getColumn();
         }
      } catch (TokenStreamException var7) {
         if (var7 instanceof TokenStreamRecognitionException) {
            lineNum = ((TokenStreamRecognitionException)var7).recog.getLine();
            colNum = ((TokenStreamRecognitionException)var7).recog.getColumn();
         }
      }

      throw new SemanticException(problem + ";\n   solution: " + solution, this.getFilename(), lineNum, colNum);
   }

   public void addWarning(String warning, String solution) {
      Token lt = null;

      try {
         lt = this.LT(1);
      } catch (TokenStreamException var5) {
      }

      if (lt == null) {
         lt = Token.badToken;
      }

      Map row = new HashMap();
      row.put("warning", warning);
      row.put("solution", solution);
      row.put("filename", this.getFilename());
      row.put("line", lt.getLine());
      row.put("column", lt.getColumn());
      this.warningList.add(row);
   }

   private void require(boolean z, String problem, String solution) throws SemanticException {
      if (!z) {
         this.requireFailed(problem, solution);
      }

   }

   private boolean matchGenericTypeBrackets(boolean z, String problem, String solution) throws SemanticException {
      if (!z) {
         this.matchGenericTypeBracketsFailed(problem, solution);
      }

      return z;
   }

   public void matchGenericTypeBracketsFailed(String problem, String solution) throws SemanticException {
      Token lt = null;
      int lineNum = Token.badToken.getLine();
      int colNum = Token.badToken.getColumn();

      try {
         lt = this.LT(1);
         if (lt != null) {
            lineNum = lt.getLine();
            colNum = lt.getColumn();
         }
      } catch (TokenStreamException var7) {
         if (var7 instanceof TokenStreamRecognitionException) {
            lineNum = ((TokenStreamRecognitionException)var7).recog.getLine();
            colNum = ((TokenStreamRecognitionException)var7).recog.getColumn();
         }
      }

      throw new SemanticException(problem + ";\n   solution: " + solution, this.getFilename(), lineNum, colNum);
   }

   private boolean isUpperCase(Token x) {
      if (x != null && x.getType() == 84) {
         String xtext = x.getText();
         return xtext.length() > 0 && Character.isUpperCase(xtext.charAt(0));
      } else {
         return false;
      }
   }

   private boolean isConstructorIdent(Token x) {
      if (this.currentClass == null) {
         return false;
      } else if (this.currentClass.getType() != 84) {
         return false;
      } else {
         String cname = this.currentClass.getText();
         return x != null && x.getType() == 84 ? cname.equals(x.getText()) : false;
      }
   }

   protected GroovyRecognizer(TokenBuffer tokenBuf, int k) {
      super(tokenBuf, k);
      this.currentClass = null;
      this.sepToken = 1;
      this.argListHasLabels = false;
      this.lastPathExpression = null;
      this.LC_STMT = 1;
      this.LC_INIT = 2;
      this.ltCounter = 0;
      this.tokenNames = _tokenNames;
      this.buildTokenTypeASTClassMap();
      this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
   }

   public GroovyRecognizer(TokenBuffer tokenBuf) {
      this((TokenBuffer)tokenBuf, 2);
   }

   protected GroovyRecognizer(TokenStream lexer, int k) {
      super(lexer, k);
      this.currentClass = null;
      this.sepToken = 1;
      this.argListHasLabels = false;
      this.lastPathExpression = null;
      this.LC_STMT = 1;
      this.LC_INIT = 2;
      this.ltCounter = 0;
      this.tokenNames = _tokenNames;
      this.buildTokenTypeASTClassMap();
      this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
   }

   public GroovyRecognizer(TokenStream lexer) {
      this((TokenStream)lexer, 2);
   }

   public GroovyRecognizer(ParserSharedInputState state) {
      super((ParserSharedInputState)state, 2);
      this.currentClass = null;
      this.sepToken = 1;
      this.argListHasLabels = false;
      this.lastPathExpression = null;
      this.LC_STMT = 1;
      this.LC_INIT = 2;
      this.ltCounter = 0;
      this.tokenNames = _tokenNames;
      this.buildTokenTypeASTClassMap();
      this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
   }

   public final void compilationUnit() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST compilationUnit_AST = null;
      switch(this.LA(1)) {
      case 1:
      case 37:
      case 38:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
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
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
      case 124:
      case 128:
      case 132:
      case 134:
      case 135:
      case 136:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 146:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
      case 201:
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 83:
      case 86:
      case 87:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      case 119:
      case 120:
      case 121:
      case 123:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
      case 133:
      case 137:
      case 145:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 77:
         this.match(77);
      }

      this.nls();
      boolean synPredMatched5 = false;
      if ((this.LA(1) == 78 || this.LA(1) == 92) && (this.LA(2) == 84 || this.LA(2) == 90)) {
         int _m5 = this.mark();
         synPredMatched5 = true;
         ++this.inputState.guessing;

         try {
            this.annotationsOpt();
            this.match(78);
         } catch (RecognitionException var6) {
            synPredMatched5 = false;
         }

         this.rewind(_m5);
         --this.inputState.guessing;
      }

      if (synPredMatched5) {
         this.packageDefinition();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else {
         if (!_tokenSet_0.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         switch(this.LA(1)) {
         case 1:
         case 124:
         case 201:
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 83:
         case 86:
         case 87:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 123:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 133:
         case 137:
         case 145:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 37:
         case 38:
         case 42:
         case 79:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 132:
         case 134:
         case 135:
         case 136:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 146:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.statement(1);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }
      }

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 1:
         case 124:
         case 201:
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 83:
         case 86:
         case 87:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 123:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 133:
         case 137:
         case 145:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 37:
         case 38:
         case 42:
         case 79:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 132:
         case 134:
         case 135:
         case 136:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 146:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.statement(this.sepToken);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }
      }

      this.match(1);
      compilationUnit_AST = currentAST.root;
      this.returnAST = compilationUnit_AST;
   }

   public final void nls() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST nls_AST = null;
      if (this.LA(1) == 201 && _tokenSet_2.member(this.LA(2))) {
         this.match(201);
      } else if (!_tokenSet_2.member(this.LA(1)) || !_tokenSet_3.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = (AST)nls_AST;
   }

   public final void annotationsOpt() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationsOpt_AST = null;
      Token first = this.LT(1);
      if (_tokenSet_4.member(this.LA(1)) && _tokenSet_5.member(this.LA(2))) {
         this.annotationsInternal();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (!_tokenSet_6.member(this.LA(1)) || !_tokenSet_7.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         annotationsOpt_AST = currentAST.root;
         annotationsOpt_AST = this.astFactory.make((new ASTArray(2)).add(this.create(64, "ANNOTATIONS", (Token)first, (Token)this.LT(1))).add(annotationsOpt_AST));
         currentAST.root = annotationsOpt_AST;
         currentAST.child = annotationsOpt_AST != null && annotationsOpt_AST.getFirstChild() != null ? annotationsOpt_AST.getFirstChild() : annotationsOpt_AST;
         currentAST.advanceChildToEnd();
      }

      annotationsOpt_AST = currentAST.root;
      this.returnAST = annotationsOpt_AST;
   }

   public final void packageDefinition() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST packageDefinition_AST = null;
      AST an_AST = null;
      AST id_AST = null;
      Token first = this.LT(1);
      this.annotationsOpt();
      an_AST = this.returnAST;
      this.match(78);
      this.identifier();
      id_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         packageDefinition_AST = currentAST.root;
         packageDefinition_AST = this.astFactory.make((new ASTArray(3)).add(this.create(15, "package", (Token)first, (Token)this.LT(1))).add(an_AST).add(id_AST));
         currentAST.root = packageDefinition_AST;
         currentAST.child = packageDefinition_AST != null && packageDefinition_AST.getFirstChild() != null ? packageDefinition_AST.getFirstChild() : packageDefinition_AST;
         currentAST.advanceChildToEnd();
      }

      packageDefinition_AST = currentAST.root;
      this.returnAST = packageDefinition_AST;
   }

   public final void statement(int prevToken) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST statement_AST = null;
      AST pfx_AST = null;
      AST es_AST = null;
      AST ale_AST = null;
      AST ifCbs_AST = null;
      AST elseCbs_AST = null;
      AST while_sce_AST = null;
      Token s = null;
      AST s_AST = null;
      AST while_cbs_AST = null;
      AST m_AST = null;
      AST switchSce_AST = null;
      AST cg_AST = null;
      AST synch_sce_AST = null;
      AST synch_cs_AST = null;
      boolean sce = false;
      Token first = this.LT(1);
      AST casesGroup_AST = null;
      boolean synPredMatched291;
      int _m291;
      switch(this.LA(1)) {
      case 132:
         this.match(132);
         this.match(88);
         this.assignmentLessExpression();
         ale_AST = this.returnAST;
         this.match(119);
         this.nlsWarn();
         this.compatibleBodyStatement();
         ifCbs_AST = this.returnAST;
         synPredMatched291 = false;
         if (_tokenSet_8.member(this.LA(1)) && _tokenSet_9.member(this.LA(2))) {
            _m291 = this.mark();
            synPredMatched291 = true;
            ++this.inputState.guessing;

            try {
               switch(this.LA(1)) {
               case 124:
               case 201:
                  this.sep();
               case 133:
                  this.match(133);
                  break;
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               }
            } catch (RecognitionException var29) {
               synPredMatched291 = false;
            }

            this.rewind(_m291);
            --this.inputState.guessing;
         }

         if (synPredMatched291) {
            switch(this.LA(1)) {
            case 124:
            case 201:
               this.sep();
            case 133:
               this.match(133);
               this.nlsWarn();
               this.compatibleBodyStatement();
               elseCbs_AST = this.returnAST;
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         } else if (!_tokenSet_10.member(this.LA(1)) || !_tokenSet_11.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            statement_AST = currentAST.root;
            statement_AST = this.astFactory.make((new ASTArray(4)).add(this.create(132, "if", (Token)first, (Token)this.LT(1))).add(ale_AST).add(ifCbs_AST).add(elseCbs_AST));
            currentAST.root = statement_AST;
            currentAST.child = statement_AST != null && statement_AST.getFirstChild() != null ? statement_AST.getFirstChild() : statement_AST;
            currentAST.advanceChildToEnd();
         }

         statement_AST = currentAST.root;
         break;
      case 133:
      case 137:
      case 143:
      case 144:
      case 145:
      default:
         synPredMatched291 = false;
         if (_tokenSet_12.member(this.LA(1)) && _tokenSet_13.member(this.LA(2))) {
            _m291 = this.mark();
            synPredMatched291 = true;
            ++this.inputState.guessing;

            try {
               this.genericMethodStart();
            } catch (RecognitionException var34) {
               synPredMatched291 = false;
            }

            this.rewind(_m291);
            --this.inputState.guessing;
         }

         if (synPredMatched291) {
            this.genericMethod();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            statement_AST = currentAST.root;
         } else {
            boolean synPredMatched280 = false;
            if (_tokenSet_12.member(this.LA(1)) && _tokenSet_14.member(this.LA(2))) {
               int _m280 = this.mark();
               synPredMatched280 = true;
               ++this.inputState.guessing;

               try {
                  this.multipleAssignmentDeclarationStart();
               } catch (RecognitionException var33) {
                  synPredMatched280 = false;
               }

               this.rewind(_m280);
               --this.inputState.guessing;
            }

            if (synPredMatched280) {
               this.multipleAssignmentDeclaration();
               this.astFactory.addASTChild(currentAST, this.returnAST);
               statement_AST = currentAST.root;
            } else {
               boolean synPredMatched282 = false;
               if (_tokenSet_15.member(this.LA(1)) && _tokenSet_16.member(this.LA(2))) {
                  int _m282 = this.mark();
                  synPredMatched282 = true;
                  ++this.inputState.guessing;

                  try {
                     this.declarationStart();
                  } catch (RecognitionException var32) {
                     synPredMatched282 = false;
                  }

                  this.rewind(_m282);
                  --this.inputState.guessing;
               }

               if (synPredMatched282) {
                  this.declaration();
                  this.astFactory.addASTChild(currentAST, this.returnAST);
                  statement_AST = currentAST.root;
               } else {
                  boolean synPredMatched284 = false;
                  if (this.LA(1) == 84 && this.LA(2) == 131) {
                     int _m284 = this.mark();
                     synPredMatched284 = true;
                     ++this.inputState.guessing;

                     try {
                        this.match(84);
                        this.match(131);
                     } catch (RecognitionException var31) {
                        synPredMatched284 = false;
                     }

                     this.rewind(_m284);
                     --this.inputState.guessing;
                  }

                  int _m295;
                  boolean synPredMatched295;
                  if (synPredMatched284) {
                     this.statementLabelPrefix();
                     pfx_AST = this.returnAST;
                     if (this.inputState.guessing == 0) {
                        statement_AST = currentAST.root;
                        currentAST.root = pfx_AST;
                        currentAST.child = pfx_AST != null && pfx_AST.getFirstChild() != null ? pfx_AST.getFirstChild() : pfx_AST;
                        currentAST.advanceChildToEnd();
                     }

                     synPredMatched295 = false;
                     if (this.LA(1) == 122 && _tokenSet_17.member(this.LA(2))) {
                        _m295 = this.mark();
                        synPredMatched295 = true;
                        ++this.inputState.guessing;

                        try {
                           this.match(122);
                        } catch (RecognitionException var28) {
                           synPredMatched295 = false;
                        }

                        this.rewind(_m295);
                        --this.inputState.guessing;
                     }

                     if (synPredMatched295) {
                        this.openOrClosableBlock();
                        this.astFactory.addASTChild(currentAST, this.returnAST);
                     } else {
                        if (!_tokenSet_18.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2))) {
                           throw new NoViableAltException(this.LT(1), this.getFilename());
                        }

                        this.statement(131);
                        this.astFactory.addASTChild(currentAST, this.returnAST);
                     }

                     statement_AST = currentAST.root;
                  } else if (_tokenSet_19.member(this.LA(1)) && _tokenSet_1.member(this.LA(2))) {
                     this.expressionStatement(prevToken);
                     es_AST = this.returnAST;
                     this.astFactory.addASTChild(currentAST, this.returnAST);
                     statement_AST = currentAST.root;
                  } else {
                     synPredMatched295 = false;
                     if ((this.LA(1) == 79 || this.LA(1) == 92) && _tokenSet_20.member(this.LA(2))) {
                        _m295 = this.mark();
                        synPredMatched295 = true;
                        ++this.inputState.guessing;

                        try {
                           this.annotationsOpt();
                           this.match(79);
                        } catch (RecognitionException var30) {
                           synPredMatched295 = false;
                        }

                        this.rewind(_m295);
                        --this.inputState.guessing;
                     }

                     if (synPredMatched295) {
                        this.importStatement();
                        this.astFactory.addASTChild(currentAST, this.returnAST);
                        statement_AST = currentAST.root;
                     } else {
                        if (_tokenSet_21.member(this.LA(1)) && _tokenSet_22.member(this.LA(2))) {
                           this.modifiersOpt();
                           m_AST = this.returnAST;
                           this.typeDefinitionInternal(m_AST);
                           this.astFactory.addASTChild(currentAST, this.returnAST);
                           statement_AST = currentAST.root;
                           break;
                        }

                        if (this.LA(1) != 117 || this.LA(2) != 88) {
                           throw new NoViableAltException(this.LT(1), this.getFilename());
                        }

                        this.match(117);
                        this.match(88);
                        sce = this.strictContextExpression(false);
                        synch_sce_AST = this.returnAST;
                        this.match(119);
                        this.nlsWarn();
                        this.compoundStatement();
                        synch_cs_AST = this.returnAST;
                        if (this.inputState.guessing == 0) {
                           statement_AST = currentAST.root;
                           statement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(117, "synchronized", (Token)first, (Token)this.LT(1))).add(synch_sce_AST).add(synch_cs_AST));
                           currentAST.root = statement_AST;
                           currentAST.child = statement_AST != null && statement_AST.getFirstChild() != null ? statement_AST.getFirstChild() : statement_AST;
                           currentAST.advanceChildToEnd();
                        }

                        statement_AST = currentAST.root;
                     }
                  }
               }
            }
         }
         break;
      case 134:
         this.match(134);
         this.match(88);
         sce = this.strictContextExpression(false);
         while_sce_AST = this.returnAST;
         this.match(119);
         this.nlsWarn();
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 79:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 132:
         case 134:
         case 135:
         case 136:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 146:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.compatibleBodyStatement();
            while_cbs_AST = this.returnAST;
            break;
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 83:
         case 86:
         case 87:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 123:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 133:
         case 137:
         case 145:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 124:
            s = this.LT(1);
            s_AST = this.astFactory.create(s);
            this.match(124);
         }

         if (this.inputState.guessing == 0) {
            statement_AST = currentAST.root;
            if (s_AST != null) {
               statement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(134, "Literal_while", (Token)first, (Token)this.LT(1))).add(while_sce_AST).add(s_AST));
            } else {
               statement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(134, "Literal_while", (Token)first, (Token)this.LT(1))).add(while_sce_AST).add(while_cbs_AST));
            }

            currentAST.root = statement_AST;
            currentAST.child = statement_AST != null && statement_AST.getFirstChild() != null ? statement_AST.getFirstChild() : statement_AST;
            currentAST.advanceChildToEnd();
         }

         statement_AST = currentAST.root;
         break;
      case 135:
         this.match(135);
         this.match(88);
         sce = this.strictContextExpression(false);
         switchSce_AST = this.returnAST;
         this.match(119);
         this.nlsWarn();
         this.match(122);
         this.nls();

         while(this.LA(1) == 125 || this.LA(1) == 145) {
            this.casesGroup();
            cg_AST = this.returnAST;
            if (this.inputState.guessing == 0) {
               casesGroup_AST = this.astFactory.make((new ASTArray(3)).add((AST)null).add(casesGroup_AST).add(cg_AST));
            }
         }

         this.match(123);
         if (this.inputState.guessing == 0) {
            statement_AST = currentAST.root;
            statement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(135, "switch", (Token)first, (Token)this.LT(1))).add(switchSce_AST).add(casesGroup_AST));
            currentAST.root = statement_AST;
            currentAST.child = statement_AST != null && statement_AST.getFirstChild() != null ? statement_AST.getFirstChild() : statement_AST;
            currentAST.advanceChildToEnd();
         }

         statement_AST = currentAST.root;
         break;
      case 136:
         this.forStatement();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         statement_AST = currentAST.root;
         break;
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
         this.branchStatement();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         statement_AST = currentAST.root;
         break;
      case 146:
         this.tryBlock();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         statement_AST = currentAST.root;
      }

      this.returnAST = statement_AST;
   }

   public final void sep() throws RecognitionException, TokenStreamException {
      Object sep_AST;
      this.returnAST = null;
      new ASTPair();
      sep_AST = null;
      label49:
      switch(this.LA(1)) {
      case 124:
         this.match(124);

         while(this.LA(1) == 201 && _tokenSet_23.member(this.LA(2))) {
            this.match(201);
         }

         if (this.inputState.guessing == 0) {
            this.sepToken = 124;
         }
         break;
      case 201:
         this.match(201);
         if (this.inputState.guessing == 0) {
            this.sepToken = 201;
         }

         while(true) {
            if (this.LA(1) != 124 || !_tokenSet_23.member(this.LA(2))) {
               break label49;
            }

            this.match(124);

            while(this.LA(1) == 201 && _tokenSet_23.member(this.LA(2))) {
               this.match(201);
            }

            if (this.inputState.guessing == 0) {
               this.sepToken = 124;
            }
         }
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = (AST)sep_AST;
   }

   public final void snippetUnit() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST snippetUnit_AST = null;
      this.nls();
      this.blockBody(1);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      snippetUnit_AST = currentAST.root;
      this.returnAST = snippetUnit_AST;
   }

   public final void blockBody(int prevToken) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST blockBody_AST = null;
      switch(this.LA(1)) {
      case 1:
      case 123:
      case 124:
      case 201:
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 83:
      case 86:
      case 87:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      case 119:
      case 120:
      case 121:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
      case 133:
      case 137:
      case 145:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 37:
      case 38:
      case 42:
      case 79:
      case 80:
      case 81:
      case 82:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
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
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
      case 128:
      case 132:
      case 134:
      case 135:
      case 136:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 146:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.statement(prevToken);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 1:
         case 123:
         case 124:
         case 201:
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 83:
         case 86:
         case 87:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 133:
         case 137:
         case 145:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 37:
         case 38:
         case 42:
         case 79:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 132:
         case 134:
         case 135:
         case 136:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 146:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.statement(this.sepToken);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }
      }

      blockBody_AST = currentAST.root;
      this.returnAST = blockBody_AST;
   }

   public final void identifier() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST identifier_AST = null;
      Token i1 = null;
      AST i1_AST = null;
      Token d = null;
      AST d_AST = null;
      Token i2 = null;
      AST i2_AST = null;
      Token first = this.LT(1);
      i1 = this.LT(1);
      i1_AST = this.astFactory.create(i1);
      this.match(84);

      while(this.LA(1) == 87) {
         d = this.LT(1);
         this.astFactory.create(d);
         this.match(87);
         this.nls();
         i2 = this.LT(1);
         i2_AST = this.astFactory.create(i2);
         this.match(84);
         if (this.inputState.guessing == 0) {
            i1_AST = this.astFactory.make((new ASTArray(3)).add(this.create(87, ".", (Token)first, (Token)this.LT(1))).add(i1_AST).add(i2_AST));
         }
      }

      if (this.inputState.guessing == 0) {
         identifier_AST = currentAST.root;
         currentAST.root = i1_AST;
         currentAST.child = i1_AST != null && i1_AST.getFirstChild() != null ? i1_AST.getFirstChild() : i1_AST;
         currentAST.advanceChildToEnd();
      }

      identifier_AST = currentAST.root;
      this.returnAST = identifier_AST;
   }

   public final void importStatement() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST importStatement_AST = null;
      AST an_AST = null;
      AST is_AST = null;
      Token first = this.LT(1);
      boolean isStatic = false;
      this.annotationsOpt();
      an_AST = this.returnAST;
      this.astFactory.addASTChild(currentAST, this.returnAST);
      this.match(79);
      switch(this.LA(1)) {
      case 80:
         this.match(80);
         if (this.inputState.guessing == 0) {
            isStatic = true;
         }
      case 84:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.identifierStar();
      is_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         importStatement_AST = currentAST.root;
         if (isStatic) {
            importStatement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(59, "static_import", (Token)first, (Token)this.LT(1))).add(an_AST).add(is_AST));
         } else {
            importStatement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(28, "import", (Token)first, (Token)this.LT(1))).add(an_AST).add(is_AST));
         }

         currentAST.root = importStatement_AST;
         currentAST.child = importStatement_AST != null && importStatement_AST.getFirstChild() != null ? importStatement_AST.getFirstChild() : importStatement_AST;
         currentAST.advanceChildToEnd();
      }

      importStatement_AST = currentAST.root;
      this.returnAST = importStatement_AST;
   }

   public final void identifierStar() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST identifierStar_AST = null;
      Token i1 = null;
      AST i1_AST = null;
      Token d1 = null;
      AST d1_AST = null;
      Token i2 = null;
      AST i2_AST = null;
      Token d2 = null;
      AST d2_AST = null;
      Token s = null;
      AST s_AST = null;
      Token alias = null;
      AST alias_AST = null;
      Token first = this.LT(1);
      i1 = this.LT(1);
      i1_AST = this.astFactory.create(i1);
      this.match(84);

      while(this.LA(1) == 87 && (this.LA(2) == 84 || this.LA(2) == 201)) {
         d1 = this.LT(1);
         this.astFactory.create(d1);
         this.match(87);
         this.nls();
         i2 = this.LT(1);
         i2_AST = this.astFactory.create(i2);
         this.match(84);
         if (this.inputState.guessing == 0) {
            i1_AST = this.astFactory.make((new ASTArray(3)).add(this.create(87, ".", (Token)first, (Token)this.LT(1))).add(i1_AST).add(i2_AST));
         }
      }

      switch(this.LA(1)) {
      case 1:
      case 123:
      case 124:
      case 125:
      case 133:
      case 145:
      case 201:
         break;
      case 87:
         d2 = this.LT(1);
         this.astFactory.create(d2);
         this.match(87);
         this.nls();
         s = this.LT(1);
         s_AST = this.astFactory.create(s);
         this.match(109);
         if (this.inputState.guessing == 0) {
            i1_AST = this.astFactory.make((new ASTArray(3)).add(this.create(87, ".", (Token)first, (Token)this.LT(1))).add(i1_AST).add(s_AST));
         }
         break;
      case 110:
         this.match(110);
         this.nls();
         alias = this.LT(1);
         alias_AST = this.astFactory.create(alias);
         this.match(84);
         if (this.inputState.guessing == 0) {
            i1_AST = this.astFactory.make((new ASTArray(3)).add(this.create(110, "as", (Token)first, (Token)this.LT(1))).add(i1_AST).add(alias_AST));
         }
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         identifierStar_AST = currentAST.root;
         currentAST.root = i1_AST;
         currentAST.child = i1_AST != null && i1_AST.getFirstChild() != null ? i1_AST.getFirstChild() : i1_AST;
         currentAST.advanceChildToEnd();
      }

      identifierStar_AST = currentAST.root;
      this.returnAST = identifierStar_AST;
   }

   protected final void typeDefinitionInternal(AST mods) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeDefinitionInternal_AST = null;
      AST cd_AST = null;
      AST id_AST = null;
      AST ed_AST = null;
      AST ad_AST = null;
      switch(this.LA(1)) {
      case 89:
         this.classDefinition(mods);
         cd_AST = this.returnAST;
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            typeDefinitionInternal_AST = currentAST.root;
            currentAST.root = cd_AST;
            currentAST.child = cd_AST != null && cd_AST.getFirstChild() != null ? cd_AST.getFirstChild() : cd_AST;
            currentAST.advanceChildToEnd();
         }

         typeDefinitionInternal_AST = currentAST.root;
         break;
      case 90:
         this.interfaceDefinition(mods);
         id_AST = this.returnAST;
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            typeDefinitionInternal_AST = currentAST.root;
            currentAST.root = id_AST;
            currentAST.child = id_AST != null && id_AST.getFirstChild() != null ? id_AST.getFirstChild() : id_AST;
            currentAST.advanceChildToEnd();
         }

         typeDefinitionInternal_AST = currentAST.root;
         break;
      case 91:
         this.enumDefinition(mods);
         ed_AST = this.returnAST;
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            typeDefinitionInternal_AST = currentAST.root;
            currentAST.root = ed_AST;
            currentAST.child = ed_AST != null && ed_AST.getFirstChild() != null ? ed_AST.getFirstChild() : ed_AST;
            currentAST.advanceChildToEnd();
         }

         typeDefinitionInternal_AST = currentAST.root;
         break;
      case 92:
         this.annotationDefinition(mods);
         ad_AST = this.returnAST;
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            typeDefinitionInternal_AST = currentAST.root;
            currentAST.root = ad_AST;
            currentAST.child = ad_AST != null && ad_AST.getFirstChild() != null ? ad_AST.getFirstChild() : ad_AST;
            currentAST.advanceChildToEnd();
         }

         typeDefinitionInternal_AST = currentAST.root;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = typeDefinitionInternal_AST;
   }

   public final void classDefinition(AST modifiers) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST classDefinition_AST = null;
      AST tp_AST = null;
      AST sc_AST = null;
      AST ic_AST = null;
      AST cb_AST = null;
      Token first = this.cloneToken(this.LT(1));
      AST prevCurrentClass = this.currentClass;
      if (modifiers != null) {
         first.setLine(modifiers.getLine());
         first.setColumn(modifiers.getColumn());
      }

      this.match(89);
      AST tmp29_AST = null;
      tmp29_AST = this.astFactory.create(this.LT(1));
      this.match(84);
      this.nls();
      if (this.inputState.guessing == 0) {
         this.currentClass = tmp29_AST;
      }

      switch(this.LA(1)) {
      case 86:
         this.typeParameters();
         tp_AST = this.returnAST;
         this.nls();
      case 94:
      case 122:
      case 127:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.superClassClause();
      sc_AST = this.returnAST;
      this.implementsClause();
      ic_AST = this.returnAST;
      this.classBlock();
      cb_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         classDefinition_AST = currentAST.root;
         classDefinition_AST = this.astFactory.make((new ASTArray(7)).add(this.create(13, "CLASS_DEF", (Token)first, (Token)this.LT(1))).add(modifiers).add(tmp29_AST).add(tp_AST).add(sc_AST).add(ic_AST).add(cb_AST));
         currentAST.root = classDefinition_AST;
         currentAST.child = classDefinition_AST != null && classDefinition_AST.getFirstChild() != null ? classDefinition_AST.getFirstChild() : classDefinition_AST;
         currentAST.advanceChildToEnd();
      }

      if (this.inputState.guessing == 0) {
         this.currentClass = prevCurrentClass;
      }

      this.returnAST = classDefinition_AST;
   }

   public final void interfaceDefinition(AST modifiers) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST interfaceDefinition_AST = null;
      AST tp_AST = null;
      AST ie_AST = null;
      AST ib_AST = null;
      Token first = this.cloneToken(this.LT(1));
      if (modifiers != null) {
         first.setLine(modifiers.getLine());
         first.setColumn(modifiers.getColumn());
      }

      this.match(90);
      AST tmp31_AST = null;
      tmp31_AST = this.astFactory.create(this.LT(1));
      this.match(84);
      this.nls();
      switch(this.LA(1)) {
      case 86:
         this.typeParameters();
         tp_AST = this.returnAST;
         this.nls();
      case 94:
      case 122:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.interfaceExtends();
      ie_AST = this.returnAST;
      this.interfaceBlock();
      ib_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         interfaceDefinition_AST = currentAST.root;
         interfaceDefinition_AST = this.astFactory.make((new ASTArray(6)).add(this.create(14, "INTERFACE_DEF", (Token)first, (Token)this.LT(1))).add(modifiers).add(tmp31_AST).add(tp_AST).add(ie_AST).add(ib_AST));
         currentAST.root = interfaceDefinition_AST;
         currentAST.child = interfaceDefinition_AST != null && interfaceDefinition_AST.getFirstChild() != null ? interfaceDefinition_AST.getFirstChild() : interfaceDefinition_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = interfaceDefinition_AST;
   }

   public final void enumDefinition(AST modifiers) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST enumDefinition_AST = null;
      AST ic_AST = null;
      AST eb_AST = null;
      Token first = this.cloneToken(this.LT(1));
      AST prevCurrentClass = this.currentClass;
      if (modifiers != null) {
         first.setLine(modifiers.getLine());
         first.setColumn(modifiers.getColumn());
      }

      this.match(91);
      AST tmp33_AST = null;
      tmp33_AST = this.astFactory.create(this.LT(1));
      this.match(84);
      if (this.inputState.guessing == 0) {
         this.currentClass = tmp33_AST;
      }

      this.nls();
      this.implementsClause();
      ic_AST = this.returnAST;
      this.nls();
      this.enumBlock();
      eb_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         enumDefinition_AST = currentAST.root;
         enumDefinition_AST = this.astFactory.make((new ASTArray(5)).add(this.create(60, "ENUM_DEF", (Token)first, (Token)this.LT(1))).add(modifiers).add(tmp33_AST).add(ic_AST).add(eb_AST));
         currentAST.root = enumDefinition_AST;
         currentAST.child = enumDefinition_AST != null && enumDefinition_AST.getFirstChild() != null ? enumDefinition_AST.getFirstChild() : enumDefinition_AST;
         currentAST.advanceChildToEnd();
      }

      if (this.inputState.guessing == 0) {
         this.currentClass = prevCurrentClass;
      }

      this.returnAST = enumDefinition_AST;
   }

   public final void annotationDefinition(AST modifiers) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationDefinition_AST = null;
      AST ab_AST = null;
      Token first = this.cloneToken(this.LT(1));
      if (modifiers != null) {
         first.setLine(modifiers.getLine());
         first.setColumn(modifiers.getColumn());
      }

      AST tmp34_AST = null;
      tmp34_AST = this.astFactory.create(this.LT(1));
      this.match(92);
      this.match(90);
      AST tmp36_AST = null;
      tmp36_AST = this.astFactory.create(this.LT(1));
      this.match(84);
      this.annotationBlock();
      ab_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         annotationDefinition_AST = currentAST.root;
         annotationDefinition_AST = this.astFactory.make((new ASTArray(4)).add(this.create(63, "ANNOTATION_DEF", (Token)first, (Token)this.LT(1))).add(modifiers).add(tmp36_AST).add(ab_AST));
         currentAST.root = annotationDefinition_AST;
         currentAST.child = annotationDefinition_AST != null && annotationDefinition_AST.getFirstChild() != null ? annotationDefinition_AST.getFirstChild() : annotationDefinition_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = annotationDefinition_AST;
   }

   public final void declaration() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST declaration_AST = null;
      AST m_AST = null;
      AST t_AST = null;
      AST v_AST = null;
      AST t2_AST = null;
      AST v2_AST = null;
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 92:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
         this.modifiers();
         m_AST = this.returnAST;
         if (_tokenSet_24.member(this.LA(1)) && _tokenSet_25.member(this.LA(2))) {
            this.typeSpec(false);
            t_AST = this.returnAST;
         } else if (this.LA(1) != 84 && this.LA(1) != 85 || !_tokenSet_26.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.variableDefinitions(m_AST, t_AST);
         v_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            declaration_AST = currentAST.root;
            declaration_AST = v_AST;
            currentAST.root = v_AST;
            currentAST.child = v_AST != null && v_AST.getFirstChild() != null ? v_AST.getFirstChild() : v_AST;
            currentAST.advanceChildToEnd();
         }
         break;
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 82:
      case 83:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 84:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.typeSpec(false);
         t2_AST = this.returnAST;
         this.variableDefinitions((AST)null, t2_AST);
         v2_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            declaration_AST = currentAST.root;
            declaration_AST = v2_AST;
            currentAST.root = v2_AST;
            currentAST.child = v2_AST != null && v2_AST.getFirstChild() != null ? v2_AST.getFirstChild() : v2_AST;
            currentAST.advanceChildToEnd();
         }
      }

      this.returnAST = declaration_AST;
   }

   public final void modifiers() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST modifiers_AST = null;
      Token first = this.LT(1);
      this.modifiersInternal();
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.inputState.guessing == 0) {
         modifiers_AST = currentAST.root;
         modifiers_AST = this.astFactory.make((new ASTArray(2)).add(this.create(5, "MODIFIERS", (Token)first, (Token)this.LT(1))).add(modifiers_AST));
         currentAST.root = modifiers_AST;
         currentAST.child = modifiers_AST != null && modifiers_AST.getFirstChild() != null ? modifiers_AST.getFirstChild() : modifiers_AST;
         currentAST.advanceChildToEnd();
      }

      modifiers_AST = currentAST.root;
      this.returnAST = modifiers_AST;
   }

   public final void typeSpec(boolean addImagNode) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeSpec_AST = null;
      switch(this.LA(1)) {
      case 84:
         this.classTypeSpec(addImagNode);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         typeSpec_AST = currentAST.root;
         break;
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.builtInTypeSpec(addImagNode);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         typeSpec_AST = currentAST.root;
      }

      this.returnAST = typeSpec_AST;
   }

   public final void variableDefinitions(AST mods, AST t) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST variableDefinitions_AST = null;
      Token id = null;
      AST id_AST = null;
      Token qid = null;
      AST qid_AST = null;
      AST param_AST = null;
      AST tc_AST = null;
      AST mb_AST = null;
      Token first = this.cloneToken(this.LT(1));
      if (mods != null) {
         first.setLine(mods.getLine());
         first.setColumn(mods.getColumn());
      } else if (t != null) {
         first.setLine(t.getLine());
         first.setColumn(t.getColumn());
      }

      if (this.LA(1) == 84 && _tokenSet_27.member(this.LA(2))) {
         this.listOfVariables(mods, t, first);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         variableDefinitions_AST = currentAST.root;
      } else {
         if (this.LA(1) != 84 && this.LA(1) != 85 || this.LA(2) != 88) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         switch(this.LA(1)) {
         case 84:
            id = this.LT(1);
            id_AST = this.astFactory.create(id);
            this.astFactory.addASTChild(currentAST, id_AST);
            this.match(84);
            break;
         case 85:
            qid = this.LT(1);
            qid_AST = this.astFactory.create(qid);
            this.astFactory.addASTChild(currentAST, qid_AST);
            this.match(85);
            if (this.inputState.guessing == 0) {
               qid_AST.setType(84);
            }
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.match(88);
         this.parameterDeclarationList();
         param_AST = this.returnAST;
         this.match(119);
         boolean synPredMatched233 = false;
         int _m233;
         if ((this.LA(1) == 126 || this.LA(1) == 201) && _tokenSet_28.member(this.LA(2))) {
            _m233 = this.mark();
            synPredMatched233 = true;
            ++this.inputState.guessing;

            try {
               this.nls();
               this.match(126);
            } catch (RecognitionException var17) {
               synPredMatched233 = false;
            }

            this.rewind(_m233);
            --this.inputState.guessing;
         }

         if (synPredMatched233) {
            this.throwsClause();
            tc_AST = this.returnAST;
         } else if (!_tokenSet_29.member(this.LA(1)) || !_tokenSet_11.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         synPredMatched233 = false;
         if ((this.LA(1) == 122 || this.LA(1) == 201) && _tokenSet_30.member(this.LA(2))) {
            _m233 = this.mark();
            synPredMatched233 = true;
            ++this.inputState.guessing;

            try {
               this.nls();
               this.match(122);
            } catch (RecognitionException var16) {
               synPredMatched233 = false;
            }

            this.rewind(_m233);
            --this.inputState.guessing;
         }

         if (synPredMatched233) {
            this.nlsWarn();
            this.openBlock();
            mb_AST = this.returnAST;
         } else if (!_tokenSet_10.member(this.LA(1)) || !_tokenSet_11.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            variableDefinitions_AST = currentAST.root;
            if (qid_AST != null) {
               id_AST = qid_AST;
            }

            variableDefinitions_AST = this.astFactory.make((new ASTArray(7)).add(this.create(8, "METHOD_DEF", (Token)first, (Token)this.LT(1))).add(mods).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t))).add(id_AST).add(param_AST).add(tc_AST).add(mb_AST));
            currentAST.root = variableDefinitions_AST;
            currentAST.child = variableDefinitions_AST != null && variableDefinitions_AST.getFirstChild() != null ? variableDefinitions_AST.getFirstChild() : variableDefinitions_AST;
            currentAST.advanceChildToEnd();
         }

         variableDefinitions_AST = currentAST.root;
      }

      this.returnAST = variableDefinitions_AST;
   }

   public final void genericMethod() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST genericMethod_AST = null;
      AST m_AST = null;
      AST p_AST = null;
      AST t_AST = null;
      AST v_AST = null;
      this.modifiers();
      m_AST = this.returnAST;
      this.typeParameters();
      p_AST = this.returnAST;
      this.typeSpec(false);
      t_AST = this.returnAST;
      this.variableDefinitions(m_AST, t_AST);
      v_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         genericMethod_AST = currentAST.root;
         genericMethod_AST = v_AST;
         AST old = v_AST.getFirstChild();
         v_AST.setFirstChild(p_AST);
         p_AST.setNextSibling(old);
         currentAST.root = v_AST;
         currentAST.child = v_AST != null && v_AST.getFirstChild() != null ? v_AST.getFirstChild() : v_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = genericMethod_AST;
   }

   public final void typeParameters() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeParameters_AST = null;
      Token first = this.LT(1);
      int currentLtLevel = 0;
      if (this.inputState.guessing == 0) {
         currentLtLevel = this.ltCounter;
      }

      this.match(86);
      if (this.inputState.guessing == 0) {
         ++this.ltCounter;
      }

      this.nls();
      this.typeParameter();
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96) {
         this.match(96);
         this.nls();
         this.typeParameter();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      this.nls();
      switch(this.LA(1)) {
      case 84:
      case 94:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 122:
      case 127:
      case 201:
         break;
      case 97:
      case 98:
      case 99:
         this.typeArgumentsOrParametersEnd();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (!this.matchGenericTypeBrackets(currentLtLevel != 0 || this.ltCounter == currentLtLevel, "Missing closing bracket '>' for generics types", "Please specify the missing bracket!")) {
         throw new SemanticException("matchGenericTypeBrackets(((currentLtLevel != 0) || ltCounter == currentLtLevel),\n        \"Missing closing bracket '>' for generics types\", \"Please specify the missing bracket!\")");
      } else {
         if (this.inputState.guessing == 0) {
            typeParameters_AST = currentAST.root;
            typeParameters_AST = this.astFactory.make((new ASTArray(2)).add(this.create(71, "TYPE_PARAMETERS", (Token)first, (Token)this.LT(1))).add(typeParameters_AST));
            currentAST.root = typeParameters_AST;
            currentAST.child = typeParameters_AST != null && typeParameters_AST.getFirstChild() != null ? typeParameters_AST.getFirstChild() : typeParameters_AST;
            currentAST.advanceChildToEnd();
         }

         typeParameters_AST = currentAST.root;
         this.returnAST = typeParameters_AST;
      }
   }

   public final void singleDeclarationNoInit() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST singleDeclarationNoInit_AST = null;
      AST m_AST = null;
      AST t_AST = null;
      AST v_AST = null;
      AST t2_AST = null;
      AST v2_AST = null;
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 92:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
         this.modifiers();
         m_AST = this.returnAST;
         if (_tokenSet_24.member(this.LA(1)) && _tokenSet_31.member(this.LA(2))) {
            this.typeSpec(false);
            t_AST = this.returnAST;
         } else if (this.LA(1) != 84 || !_tokenSet_32.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.singleVariable(m_AST, t_AST);
         v_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            singleDeclarationNoInit_AST = currentAST.root;
            singleDeclarationNoInit_AST = v_AST;
            currentAST.root = v_AST;
            currentAST.child = v_AST != null && v_AST.getFirstChild() != null ? v_AST.getFirstChild() : v_AST;
            currentAST.advanceChildToEnd();
         }
         break;
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 82:
      case 83:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 84:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.typeSpec(false);
         t2_AST = this.returnAST;
         this.singleVariable((AST)null, t2_AST);
         v2_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            singleDeclarationNoInit_AST = currentAST.root;
            singleDeclarationNoInit_AST = v2_AST;
            currentAST.root = v2_AST;
            currentAST.child = v2_AST != null && v2_AST.getFirstChild() != null ? v2_AST.getFirstChild() : v2_AST;
            currentAST.advanceChildToEnd();
         }
      }

      this.returnAST = singleDeclarationNoInit_AST;
   }

   public final void singleVariable(AST mods, AST t) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST singleVariable_AST = null;
      AST id_AST = null;
      Token first = this.LT(1);
      this.variableName();
      id_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         singleVariable_AST = currentAST.root;
         singleVariable_AST = this.astFactory.make((new ASTArray(4)).add(this.create(9, "VARIABLE_DEF", (Token)first, (Token)this.LT(1))).add(mods).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t))).add(id_AST));
         currentAST.root = singleVariable_AST;
         currentAST.child = singleVariable_AST != null && singleVariable_AST.getFirstChild() != null ? singleVariable_AST.getFirstChild() : singleVariable_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = singleVariable_AST;
   }

   public final void singleDeclaration() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST singleDeclaration_AST = null;
      AST sd_AST = null;
      this.singleDeclarationNoInit();
      sd_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         singleDeclaration_AST = currentAST.root;
         currentAST.root = sd_AST;
         currentAST.child = sd_AST != null && sd_AST.getFirstChild() != null ? sd_AST.getFirstChild() : sd_AST;
         currentAST.advanceChildToEnd();
      }

      switch(this.LA(1)) {
      case 120:
         this.varInitializer();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      case 1:
      case 83:
      case 96:
      case 119:
      case 124:
         singleDeclaration_AST = currentAST.root;
         this.returnAST = singleDeclaration_AST;
         return;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final void varInitializer() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST varInitializer_AST = null;
      AST tmp41_AST = null;
      tmp41_AST = this.astFactory.create(this.LT(1));
      this.astFactory.makeASTRoot(currentAST, tmp41_AST);
      this.match(120);
      this.nls();
      this.expression(2);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      varInitializer_AST = currentAST.root;
      this.returnAST = varInitializer_AST;
   }

   public final void declarationStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST declarationStart_AST = null;
      int _cnt29 = 0;

      while(true) {
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
            this.modifier();
            this.nls();
            break;
         case 81:
            this.match(81);
            this.nls();
            break;
         case 92:
            this.annotation();
            this.nls();
            break;
         default:
            if (!_tokenSet_24.member(this.LA(1)) || !_tokenSet_33.member(this.LA(2))) {
               if (_cnt29 >= 1) {
                  AST tmp46_AST;
                  switch(this.LA(1)) {
                  case 84:
                     tmp46_AST = null;
                     tmp46_AST = this.astFactory.create(this.LT(1));
                     this.match(84);
                     break;
                  case 85:
                     tmp46_AST = null;
                     tmp46_AST = this.astFactory.create(this.LT(1));
                     this.match(85);
                     break;
                  default:
                     throw new NoViableAltException(this.LT(1), this.getFilename());
                  }

                  this.returnAST = (AST)declarationStart_AST;
                  return;
               }

               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            if (this.LA(1) == 84 && _tokenSet_34.member(this.LA(2))) {
               this.upperCaseIdent();
            } else if (this.LA(1) >= 100 && this.LA(1) <= 108) {
               this.builtInType();
            } else {
               if (this.LA(1) != 84 || this.LA(2) != 87) {
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               }

               this.qualifiedTypeName();
            }

            switch(this.LA(1)) {
            case 37:
            case 38:
            case 42:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 92:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
               break;
            case 39:
            case 40:
            case 41:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 83:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 109:
            case 110:
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            case 86:
               this.typeArguments();
            }

            while(this.LA(1) == 82) {
               AST tmp43_AST = null;
               tmp43_AST = this.astFactory.create(this.LT(1));
               this.match(82);
               this.balancedTokens();
               AST tmp44_AST = null;
               tmp44_AST = this.astFactory.create(this.LT(1));
               this.match(83);
            }
         }

         ++_cnt29;
      }
   }

   public final void modifier() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST modifier_AST = null;
      AST tmp57_AST;
      switch(this.LA(1)) {
      case 37:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(37);
         modifier_AST = currentAST.root;
         break;
      case 38:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(38);
         modifier_AST = currentAST.root;
         break;
      case 42:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(42);
         modifier_AST = currentAST.root;
         break;
      case 80:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(80);
         modifier_AST = currentAST.root;
         break;
      case 111:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(111);
         modifier_AST = currentAST.root;
         break;
      case 112:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(112);
         modifier_AST = currentAST.root;
         break;
      case 113:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(113);
         modifier_AST = currentAST.root;
         break;
      case 114:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(114);
         modifier_AST = currentAST.root;
         break;
      case 115:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(115);
         modifier_AST = currentAST.root;
         break;
      case 116:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(116);
         modifier_AST = currentAST.root;
         break;
      case 117:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(117);
         modifier_AST = currentAST.root;
         break;
      case 118:
         tmp57_AST = null;
         tmp57_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp57_AST);
         this.match(118);
         modifier_AST = currentAST.root;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = modifier_AST;
   }

   public final void annotation() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotation_AST = null;
      AST i_AST = null;
      AST args_AST = null;
      Token first = this.LT(1);
      this.match(92);
      this.identifier();
      i_AST = this.returnAST;
      this.nls();
      if (this.LA(1) == 88 && _tokenSet_35.member(this.LA(2))) {
         this.match(88);
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 39:
         case 41:
         case 42:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
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
         case 122:
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
         case 143:
         case 144:
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
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.annotationArguments();
            args_AST = this.returnAST;
         case 119:
            this.match(119);
            break;
         case 40:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 83:
         case 86:
         case 87:
         case 93:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 120:
         case 121:
         case 123:
         case 124:
         case 129:
         case 130:
         case 131:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } else if (!_tokenSet_36.member(this.LA(1)) || !_tokenSet_37.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         annotation_AST = currentAST.root;
         annotation_AST = this.astFactory.make((new ASTArray(3)).add(this.create(65, "ANNOTATION", (Token)first, (Token)this.LT(1))).add(i_AST).add(args_AST));
         currentAST.root = annotation_AST;
         currentAST.child = annotation_AST != null && annotation_AST.getFirstChild() != null ? annotation_AST.getFirstChild() : annotation_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = annotation_AST;
   }

   public final void upperCaseIdent() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST upperCaseIdent_AST = null;
      if (!this.isUpperCase(this.LT(1))) {
         throw new SemanticException("isUpperCase(LT(1))");
      } else {
         AST tmp62_AST = null;
         tmp62_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp62_AST);
         this.match(84);
         upperCaseIdent_AST = currentAST.root;
         this.returnAST = upperCaseIdent_AST;
      }
   }

   public final void builtInType() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST builtInType_AST = null;
      AST tmp71_AST;
      switch(this.LA(1)) {
      case 100:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(100);
         builtInType_AST = currentAST.root;
         break;
      case 101:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(101);
         builtInType_AST = currentAST.root;
         break;
      case 102:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(102);
         builtInType_AST = currentAST.root;
         break;
      case 103:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(103);
         builtInType_AST = currentAST.root;
         break;
      case 104:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(104);
         builtInType_AST = currentAST.root;
         break;
      case 105:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(105);
         builtInType_AST = currentAST.root;
         break;
      case 106:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(106);
         builtInType_AST = currentAST.root;
         break;
      case 107:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(107);
         builtInType_AST = currentAST.root;
         break;
      case 108:
         tmp71_AST = null;
         tmp71_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp71_AST);
         this.match(108);
         builtInType_AST = currentAST.root;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = builtInType_AST;
   }

   public final void qualifiedTypeName() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST qualifiedTypeName_AST = null;
      AST tmp72_AST = null;
      tmp72_AST = this.astFactory.create(this.LT(1));
      this.match(84);
      AST tmp73_AST = null;
      tmp73_AST = this.astFactory.create(this.LT(1));
      this.match(87);

      while(this.LA(1) == 84 && this.LA(2) == 87) {
         AST tmp74_AST = null;
         tmp74_AST = this.astFactory.create(this.LT(1));
         this.match(84);
         AST tmp75_AST = null;
         tmp75_AST = this.astFactory.create(this.LT(1));
         this.match(87);
      }

      this.upperCaseIdent();
      this.returnAST = (AST)qualifiedTypeName_AST;
   }

   public final void typeArguments() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeArguments_AST = null;
      Token first = this.LT(1);
      int currentLtLevel = 0;
      if (this.inputState.guessing == 0) {
         currentLtLevel = this.ltCounter;
      }

      this.match(86);
      if (this.inputState.guessing == 0) {
         ++this.ltCounter;
      }

      this.nls();
      this.typeArgument();
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96 && _tokenSet_38.member(this.LA(2)) && (this.inputState.guessing != 0 || this.ltCounter == currentLtLevel + 1)) {
         this.match(96);
         this.nls();
         this.typeArgument();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      this.nls();
      if (this.LA(1) >= 97 && this.LA(1) <= 99 && _tokenSet_39.member(this.LA(2))) {
         this.typeArgumentsOrParametersEnd();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (!_tokenSet_39.member(this.LA(1)) || !_tokenSet_3.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (!this.matchGenericTypeBrackets(currentLtLevel != 0 || this.ltCounter == currentLtLevel, "Missing closing bracket '>' for generics types", "Please specify the missing bracket!")) {
         throw new SemanticException("matchGenericTypeBrackets(((currentLtLevel != 0) || ltCounter == currentLtLevel),\n        \"Missing closing bracket '>' for generics types\", \"Please specify the missing bracket!\")");
      } else {
         if (this.inputState.guessing == 0) {
            typeArguments_AST = currentAST.root;
            typeArguments_AST = this.astFactory.make((new ASTArray(2)).add(this.create(69, "TYPE_ARGUMENTS", (Token)first, (Token)this.LT(1))).add(typeArguments_AST));
            currentAST.root = typeArguments_AST;
            currentAST.child = typeArguments_AST != null && typeArguments_AST.getFirstChild() != null ? typeArguments_AST.getFirstChild() : typeArguments_AST;
            currentAST.advanceChildToEnd();
         }

         typeArguments_AST = currentAST.root;
         this.returnAST = typeArguments_AST;
      }
   }

   public final void balancedTokens() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      Object balancedTokens_AST = null;

      while(true) {
         while(!_tokenSet_40.member(this.LA(1))) {
            if (!_tokenSet_41.member(this.LA(1))) {
               this.returnAST = (AST)balancedTokens_AST;
               return;
            }

            this.match(_tokenSet_41);
         }

         this.balancedBrackets();
      }
   }

   public final void genericMethodStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST genericMethodStart_AST = null;
      int _cnt33 = 0;

      while(true) {
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
            this.modifier();
            this.nls();
            break;
         case 81:
            this.match(81);
            this.nls();
            break;
         case 92:
            this.annotation();
            this.nls();
            break;
         default:
            if (_cnt33 >= 1) {
               AST tmp80_AST = null;
               tmp80_AST = this.astFactory.create(this.LT(1));
               this.match(86);
               this.returnAST = (AST)genericMethodStart_AST;
               return;
            }

            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         ++_cnt33;
      }
   }

   public final void constructorStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST constructorStart_AST = null;
      Token id = null;
      AST id_AST = null;
      this.modifiersOpt();
      id = this.LT(1);
      this.astFactory.create(id);
      this.match(84);
      if (!this.isConstructorIdent(id)) {
         throw new SemanticException("isConstructorIdent(id)");
      } else {
         this.nls();
         this.match(88);
         this.returnAST = (AST)constructorStart_AST;
      }
   }

   public final void modifiersOpt() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST modifiersOpt_AST = null;
      Token first = this.LT(1);
      if (_tokenSet_12.member(this.LA(1)) && _tokenSet_42.member(this.LA(2))) {
         this.modifiersInternal();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (!_tokenSet_43.member(this.LA(1)) || !_tokenSet_44.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         modifiersOpt_AST = currentAST.root;
         modifiersOpt_AST = this.astFactory.make((new ASTArray(2)).add(this.create(5, "MODIFIERS", (Token)first, (Token)this.LT(1))).add(modifiersOpt_AST));
         currentAST.root = modifiersOpt_AST;
         currentAST.child = modifiersOpt_AST != null && modifiersOpt_AST.getFirstChild() != null ? modifiersOpt_AST.getFirstChild() : modifiersOpt_AST;
         currentAST.advanceChildToEnd();
      }

      modifiersOpt_AST = currentAST.root;
      this.returnAST = modifiersOpt_AST;
   }

   public final void typeDeclarationStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST typeDeclarationStart_AST = null;
      this.modifiersOpt();
      switch(this.LA(1)) {
      case 89:
         this.match(89);
         break;
      case 90:
         this.match(90);
         break;
      case 91:
         this.match(91);
         break;
      case 92:
         AST tmp85_AST = null;
         tmp85_AST = this.astFactory.create(this.LT(1));
         this.match(92);
         this.match(90);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = (AST)typeDeclarationStart_AST;
   }

   public final void classTypeSpec(boolean addImagNode) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST classTypeSpec_AST = null;
      AST ct_AST = null;
      Token first = this.LT(1);
      this.classOrInterfaceType(false);
      ct_AST = this.returnAST;
      this.declaratorBrackets(ct_AST);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.inputState.guessing == 0) {
         classTypeSpec_AST = currentAST.root;
         if (addImagNode) {
            classTypeSpec_AST = this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(classTypeSpec_AST));
         }

         currentAST.root = classTypeSpec_AST;
         currentAST.child = classTypeSpec_AST != null && classTypeSpec_AST.getFirstChild() != null ? classTypeSpec_AST.getFirstChild() : classTypeSpec_AST;
         currentAST.advanceChildToEnd();
      }

      classTypeSpec_AST = currentAST.root;
      this.returnAST = classTypeSpec_AST;
   }

   public final void builtInTypeSpec(boolean addImagNode) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST builtInTypeSpec_AST = null;
      AST bt_AST = null;
      Token first = this.LT(1);
      this.builtInType();
      bt_AST = this.returnAST;
      this.declaratorBrackets(bt_AST);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.inputState.guessing == 0) {
         builtInTypeSpec_AST = currentAST.root;
         if (addImagNode) {
            builtInTypeSpec_AST = this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(builtInTypeSpec_AST));
         }

         currentAST.root = builtInTypeSpec_AST;
         currentAST.child = builtInTypeSpec_AST != null && builtInTypeSpec_AST.getFirstChild() != null ? builtInTypeSpec_AST.getFirstChild() : builtInTypeSpec_AST;
         currentAST.advanceChildToEnd();
      }

      builtInTypeSpec_AST = currentAST.root;
      this.returnAST = builtInTypeSpec_AST;
   }

   public final void classOrInterfaceType(boolean addImagNode) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST classOrInterfaceType_AST = null;
      Token i1 = null;
      AST i1_AST = null;
      Token d = null;
      AST d_AST = null;
      Token i2 = null;
      AST i2_AST = null;
      AST ta_AST = null;
      Token first = this.LT(1);
      i1 = this.LT(1);
      i1_AST = this.astFactory.create(i1);
      this.astFactory.makeASTRoot(currentAST, i1_AST);
      this.match(84);
      switch(this.LA(1)) {
      case 1:
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
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
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 128:
      case 129:
      case 130:
      case 131:
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
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
      case 148:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
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
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
      case 201:
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 109:
      case 149:
      case 150:
      case 151:
      case 181:
      case 182:
      case 183:
      case 184:
      case 185:
      case 187:
      case 188:
      case 190:
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 86:
         this.typeArguments();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      while(this.LA(1) == 87 && this.LA(2) == 84) {
         d = this.LT(1);
         this.astFactory.create(d);
         this.match(87);
         i2 = this.LT(1);
         i2_AST = this.astFactory.create(i2);
         this.match(84);
         switch(this.LA(1)) {
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 40:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 109:
         case 149:
         case 150:
         case 151:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 187:
         case 188:
         case 190:
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 86:
            this.typeArguments();
            ta_AST = this.returnAST;
         case 1:
         case 37:
         case 38:
         case 39:
         case 41:
         case 42:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
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
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
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
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 152:
         case 153:
         case 154:
         case 155:
         case 156:
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
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
         case 201:
            if (this.inputState.guessing == 0) {
               i1_AST = this.astFactory.make((new ASTArray(4)).add(this.create(87, ".", (Token)first, (Token)this.LT(1))).add(i1_AST).add(i2_AST).add(ta_AST));
            }
         }
      }

      if (this.inputState.guessing == 0) {
         classOrInterfaceType_AST = currentAST.root;
         classOrInterfaceType_AST = i1_AST;
         if (addImagNode) {
            classOrInterfaceType_AST = this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(i1_AST));
         }

         currentAST.root = classOrInterfaceType_AST;
         currentAST.child = classOrInterfaceType_AST != null && classOrInterfaceType_AST.getFirstChild() != null ? classOrInterfaceType_AST.getFirstChild() : classOrInterfaceType_AST;
         currentAST.advanceChildToEnd();
      }

      classOrInterfaceType_AST = currentAST.root;
      this.returnAST = classOrInterfaceType_AST;
   }

   public final void declaratorBrackets(AST typ) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST declaratorBrackets_AST = null;
      if (this.inputState.guessing == 0) {
         declaratorBrackets_AST = currentAST.root;
         currentAST.root = typ;
         currentAST.child = typ != null && typ.getFirstChild() != null ? typ.getFirstChild() : typ;
         currentAST.advanceChildToEnd();
      }

      while(this.LA(1) == 82 && this.LA(2) == 83) {
         this.match(82);
         this.match(83);
         if (this.inputState.guessing == 0) {
            declaratorBrackets_AST = currentAST.root;
            declaratorBrackets_AST = this.astFactory.make((new ASTArray(2)).add(this.create(16, "[", (AST)typ, (Token)this.LT(1))).add(declaratorBrackets_AST));
            currentAST.root = declaratorBrackets_AST;
            currentAST.child = declaratorBrackets_AST != null && declaratorBrackets_AST.getFirstChild() != null ? declaratorBrackets_AST.getFirstChild() : declaratorBrackets_AST;
            currentAST.advanceChildToEnd();
         }
      }

      declaratorBrackets_AST = currentAST.root;
      this.returnAST = declaratorBrackets_AST;
   }

   public final void typeArgumentSpec() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeArgumentSpec_AST = null;
      switch(this.LA(1)) {
      case 84:
         this.classTypeSpec(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         typeArgumentSpec_AST = currentAST.root;
         break;
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.builtInTypeArraySpec(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         typeArgumentSpec_AST = currentAST.root;
      }

      this.returnAST = typeArgumentSpec_AST;
   }

   public final void builtInTypeArraySpec(boolean addImagNode) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST builtInTypeArraySpec_AST = null;
      AST bt_AST = null;
      Token first = this.LT(1);
      this.builtInType();
      bt_AST = this.returnAST;
      boolean synPredMatched65 = false;
      if (_tokenSet_39.member(this.LA(1)) && _tokenSet_3.member(this.LA(2))) {
         int _m65 = this.mark();
         synPredMatched65 = true;
         ++this.inputState.guessing;

         try {
            this.match(82);
         } catch (RecognitionException var9) {
            synPredMatched65 = false;
         }

         this.rewind(_m65);
         --this.inputState.guessing;
      }

      if (synPredMatched65) {
         this.declaratorBrackets(bt_AST);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else {
         if (!_tokenSet_39.member(this.LA(1)) || !_tokenSet_3.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            this.require(false, "primitive type parameters not allowed here", "use the corresponding wrapper type, such as Integer for int");
         }
      }

      if (this.inputState.guessing == 0) {
         builtInTypeArraySpec_AST = currentAST.root;
         if (addImagNode) {
            builtInTypeArraySpec_AST = this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(builtInTypeArraySpec_AST));
         }

         currentAST.root = builtInTypeArraySpec_AST;
         currentAST.child = builtInTypeArraySpec_AST != null && builtInTypeArraySpec_AST.getFirstChild() != null ? builtInTypeArraySpec_AST.getFirstChild() : builtInTypeArraySpec_AST;
         currentAST.advanceChildToEnd();
      }

      builtInTypeArraySpec_AST = currentAST.root;
      this.returnAST = builtInTypeArraySpec_AST;
   }

   public final void typeArgument() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeArgument_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 84:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.typeArgumentSpec();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 93:
         this.wildcardType();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         typeArgument_AST = currentAST.root;
         typeArgument_AST = this.astFactory.make((new ASTArray(2)).add(this.create(70, "TYPE_ARGUMENT", (Token)first, (Token)this.LT(1))).add(typeArgument_AST));
         currentAST.root = typeArgument_AST;
         currentAST.child = typeArgument_AST != null && typeArgument_AST.getFirstChild() != null ? typeArgument_AST.getFirstChild() : typeArgument_AST;
         currentAST.advanceChildToEnd();
      }

      typeArgument_AST = currentAST.root;
      this.returnAST = typeArgument_AST;
   }

   public final void wildcardType() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST wildcardType_AST = null;
      AST tmp89_AST = null;
      tmp89_AST = this.astFactory.create(this.LT(1));
      this.astFactory.addASTChild(currentAST, tmp89_AST);
      this.match(93);
      boolean synPredMatched54 = false;
      if ((this.LA(1) == 94 || this.LA(1) == 95) && (this.LA(2) == 84 || this.LA(2) == 201)) {
         int _m54 = this.mark();
         synPredMatched54 = true;
         ++this.inputState.guessing;

         try {
            switch(this.LA(1)) {
            case 94:
               this.match(94);
               break;
            case 95:
               this.match(95);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         } catch (RecognitionException var7) {
            synPredMatched54 = false;
         }

         this.rewind(_m54);
         --this.inputState.guessing;
      }

      if (synPredMatched54) {
         this.typeArgumentBounds();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (!_tokenSet_39.member(this.LA(1)) || !_tokenSet_3.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         wildcardType_AST = currentAST.root;
         wildcardType_AST.setType(73);
      }

      wildcardType_AST = currentAST.root;
      this.returnAST = wildcardType_AST;
   }

   public final void typeArgumentBounds() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeArgumentBounds_AST = null;
      Token first = this.LT(1);
      boolean isUpperBounds = false;
      switch(this.LA(1)) {
      case 94:
         this.match(94);
         if (this.inputState.guessing == 0) {
            isUpperBounds = true;
         }
         break;
      case 95:
         this.match(95);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.nls();
      this.classOrInterfaceType(true);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      this.nls();
      if (this.inputState.guessing == 0) {
         typeArgumentBounds_AST = currentAST.root;
         if (isUpperBounds) {
            typeArgumentBounds_AST = this.astFactory.make((new ASTArray(2)).add(this.create(74, "TYPE_UPPER_BOUNDS", (Token)first, (Token)this.LT(1))).add(typeArgumentBounds_AST));
         } else {
            typeArgumentBounds_AST = this.astFactory.make((new ASTArray(2)).add(this.create(75, "TYPE_LOWER_BOUNDS", (Token)first, (Token)this.LT(1))).add(typeArgumentBounds_AST));
         }

         currentAST.root = typeArgumentBounds_AST;
         currentAST.child = typeArgumentBounds_AST != null && typeArgumentBounds_AST.getFirstChild() != null ? typeArgumentBounds_AST.getFirstChild() : typeArgumentBounds_AST;
         currentAST.advanceChildToEnd();
      }

      typeArgumentBounds_AST = currentAST.root;
      this.returnAST = typeArgumentBounds_AST;
   }

   protected final void typeArgumentsOrParametersEnd() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeArgumentsOrParametersEnd_AST = null;
      switch(this.LA(1)) {
      case 97:
         this.match(97);
         if (this.inputState.guessing == 0) {
            --this.ltCounter;
         }

         typeArgumentsOrParametersEnd_AST = currentAST.root;
         break;
      case 98:
         this.match(98);
         if (this.inputState.guessing == 0) {
            this.ltCounter -= 2;
         }

         typeArgumentsOrParametersEnd_AST = currentAST.root;
         break;
      case 99:
         this.match(99);
         if (this.inputState.guessing == 0) {
            this.ltCounter -= 3;
         }

         typeArgumentsOrParametersEnd_AST = currentAST.root;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = typeArgumentsOrParametersEnd_AST;
   }

   public final void type() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST type_AST = null;
      switch(this.LA(1)) {
      case 84:
         this.classOrInterfaceType(false);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         type_AST = currentAST.root;
         break;
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.builtInType();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         type_AST = currentAST.root;
      }

      this.returnAST = type_AST;
   }

   public final void modifiersInternal() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST modifiersInternal_AST = null;
      int seenDef = 0;
      int _cnt78 = 0;

      while(true) {
         if (this.LA(1) == 81 && seenDef++ == 0) {
            this.match(81);
            this.nls();
         } else if (_tokenSet_45.member(this.LA(1))) {
            this.modifier();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.nls();
         } else if (this.LA(1) == 92 && this.LA(2) == 90) {
            if (this.inputState.guessing == 0) {
               break;
            }

            AST tmp96_AST = null;
            tmp96_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp96_AST);
            this.match(92);
            AST tmp97_AST = null;
            tmp97_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp97_AST);
            this.match(90);
         } else {
            if (this.LA(1) != 92 || this.LA(2) != 84) {
               if (_cnt78 < 1) {
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               }
               break;
            }

            this.annotation();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.nls();
         }

         ++_cnt78;
      }

      modifiersInternal_AST = currentAST.root;
      this.returnAST = modifiersInternal_AST;
   }

   public final void annotationArguments() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationArguments_AST = null;
      AST v_AST = null;
      if (_tokenSet_46.member(this.LA(1)) && _tokenSet_47.member(this.LA(2))) {
         this.annotationMemberValueInitializer();
         v_AST = this.returnAST;
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            annotationArguments_AST = currentAST.root;
            Token itkn = new Token(84, "value");
            AST i = this.astFactory.make((new ASTArray(1)).add(this.create(84, "value", (Token)itkn, (Token)itkn)));
            annotationArguments_AST = this.astFactory.make((new ASTArray(3)).add(this.create(66, "ANNOTATION_MEMBER_VALUE_PAIR", (Token)this.LT(1), (Token)this.LT(1))).add(i).add(v_AST));
            currentAST.root = annotationArguments_AST;
            currentAST.child = annotationArguments_AST != null && annotationArguments_AST.getFirstChild() != null ? annotationArguments_AST.getFirstChild() : annotationArguments_AST;
            currentAST.advanceChildToEnd();
         }

         annotationArguments_AST = currentAST.root;
      } else {
         if (!_tokenSet_48.member(this.LA(1)) || this.LA(2) != 120) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.annotationMemberValuePairs();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         annotationArguments_AST = currentAST.root;
      }

      this.returnAST = annotationArguments_AST;
   }

   public final void annotationsInternal() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationsInternal_AST = null;

      while(true) {
         if (this.LA(1) == 92 && this.LA(2) == 90) {
            if (this.inputState.guessing == 0) {
               break;
            }

            AST tmp98_AST = null;
            tmp98_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp98_AST);
            this.match(92);
            AST tmp99_AST = null;
            tmp99_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp99_AST);
            this.match(90);
         } else {
            if (this.LA(1) != 92 || this.LA(2) != 84) {
               break;
            }

            this.annotation();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.nls();
         }
      }

      annotationsInternal_AST = currentAST.root;
      this.returnAST = annotationsInternal_AST;
   }

   public final void annotationMemberValueInitializer() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationMemberValueInitializer_AST = null;
      switch(this.LA(1)) {
      case 82:
      case 84:
      case 85:
      case 88:
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
      case 122:
      case 128:
      case 143:
      case 144:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.conditionalExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         annotationMemberValueInitializer_AST = currentAST.root;
         break;
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
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
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
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
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 92:
         this.annotation();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         annotationMemberValueInitializer_AST = currentAST.root;
      }

      this.returnAST = annotationMemberValueInitializer_AST;
   }

   public final void annotationMemberValuePairs() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationMemberValuePairs_AST = null;
      this.annotationMemberValuePair();
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96) {
         this.match(96);
         this.nls();
         this.annotationMemberValuePair();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      annotationMemberValuePairs_AST = currentAST.root;
      this.returnAST = annotationMemberValuePairs_AST;
   }

   public final void annotationMemberValuePair() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationMemberValuePair_AST = null;
      AST i_AST = null;
      AST v_AST = null;
      Token first = this.LT(1);
      this.annotationIdent();
      i_AST = this.returnAST;
      this.match(120);
      this.nls();
      this.annotationMemberValueInitializer();
      v_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         annotationMemberValuePair_AST = currentAST.root;
         annotationMemberValuePair_AST = this.astFactory.make((new ASTArray(3)).add(this.create(66, "ANNOTATION_MEMBER_VALUE_PAIR", (Token)first, (Token)this.LT(1))).add(i_AST).add(v_AST));
         currentAST.root = annotationMemberValuePair_AST;
         currentAST.child = annotationMemberValuePair_AST != null && annotationMemberValuePair_AST.getFirstChild() != null ? annotationMemberValuePair_AST.getFirstChild() : annotationMemberValuePair_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = annotationMemberValuePair_AST;
   }

   public final void annotationIdent() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationIdent_AST = null;
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 89:
      case 90:
      case 91:
      case 94:
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
      case 125:
      case 126:
      case 127:
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
         this.keywordPropertyNames();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         annotationIdent_AST = currentAST.root;
         break;
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 82:
      case 83:
      case 85:
      case 86:
      case 87:
      case 88:
      case 92:
      case 93:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 119:
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 128:
      case 129:
      case 130:
      case 131:
      case 143:
      case 144:
      case 149:
      case 150:
      case 151:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 84:
         AST tmp102_AST = null;
         tmp102_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp102_AST);
         this.match(84);
         annotationIdent_AST = currentAST.root;
      }

      this.returnAST = annotationIdent_AST;
   }

   public final void keywordPropertyNames() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST keywordPropertyNames_AST = null;
      AST tmp144_AST;
      switch(this.LA(1)) {
      case 37:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(37);
         break;
      case 38:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(38);
         break;
      case 39:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(39);
         break;
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 92:
      case 93:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 119:
      case 120:
      case 121:
      case 122:
      case 123:
      case 124:
      case 128:
      case 129:
      case 130:
      case 131:
      case 143:
      case 144:
      case 149:
      case 150:
      case 151:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 41:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(41);
         break;
      case 42:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(42);
         break;
      case 78:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(78);
         break;
      case 79:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(79);
         break;
      case 80:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(80);
         break;
      case 81:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(81);
         break;
      case 89:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(89);
         break;
      case 90:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(90);
         break;
      case 91:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(91);
         break;
      case 94:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(94);
         break;
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.builtInType();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      case 110:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(110);
         break;
      case 111:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(111);
         break;
      case 112:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(112);
         break;
      case 113:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(113);
         break;
      case 114:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(114);
         break;
      case 115:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(115);
         break;
      case 116:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(116);
         break;
      case 117:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(117);
         break;
      case 118:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(118);
         break;
      case 125:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(125);
         break;
      case 126:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(126);
         break;
      case 127:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(127);
         break;
      case 132:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(132);
         break;
      case 133:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(133);
         break;
      case 134:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(134);
         break;
      case 135:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(135);
         break;
      case 136:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(136);
         break;
      case 137:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(137);
         break;
      case 138:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(138);
         break;
      case 139:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(139);
         break;
      case 140:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(140);
         break;
      case 141:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(141);
         break;
      case 142:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(142);
         break;
      case 145:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(145);
         break;
      case 146:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(146);
         break;
      case 147:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(147);
         break;
      case 148:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(148);
         break;
      case 152:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(152);
         break;
      case 153:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(153);
         break;
      case 154:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(154);
         break;
      case 155:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(155);
         break;
      case 156:
         tmp144_AST = null;
         tmp144_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp144_AST);
         this.match(156);
      }

      if (this.inputState.guessing == 0) {
         keywordPropertyNames_AST = currentAST.root;
         keywordPropertyNames_AST.setType(84);
      }

      keywordPropertyNames_AST = currentAST.root;
      this.returnAST = keywordPropertyNames_AST;
   }

   public final void conditionalExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST conditionalExpression_AST = null;
      this.logicalOrExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      AST tmp148_AST;
      switch(this.LA(1)) {
      case 1:
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 94:
      case 95:
      case 96:
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
      case 120:
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 128:
      case 130:
      case 131:
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
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
      case 148:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
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
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
      case 201:
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 86:
      case 87:
      case 92:
      case 97:
      case 98:
      case 99:
      case 109:
      case 121:
      case 129:
      case 149:
      case 150:
      case 151:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 93:
         tmp148_AST = null;
         tmp148_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp148_AST);
         this.match(93);
         this.nls();
         this.assignmentExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         this.match(131);
         this.nls();
         this.conditionalExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      case 169:
         tmp148_AST = null;
         tmp148_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp148_AST);
         this.match(169);
         this.nls();
         this.conditionalExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      conditionalExpression_AST = currentAST.root;
      this.returnAST = conditionalExpression_AST;
   }

   public final void annotationMemberArrayValueInitializer() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationMemberArrayValueInitializer_AST = null;
      switch(this.LA(1)) {
      case 82:
      case 84:
      case 85:
      case 88:
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
      case 122:
      case 128:
      case 143:
      case 144:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.conditionalExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         annotationMemberArrayValueInitializer_AST = currentAST.root;
         break;
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
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
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
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
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 92:
         this.annotation();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         this.nls();
         annotationMemberArrayValueInitializer_AST = currentAST.root;
      }

      this.returnAST = annotationMemberArrayValueInitializer_AST;
   }

   public final void superClassClause() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST superClassClause_AST = null;
      AST c_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 94:
         this.match(94);
         this.nls();
         this.classOrInterfaceType(false);
         c_AST = this.returnAST;
         this.nls();
      case 122:
      case 127:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         superClassClause_AST = currentAST.root;
         superClassClause_AST = this.astFactory.make((new ASTArray(2)).add(this.create(17, "EXTENDS_CLAUSE", (Token)first, (Token)this.LT(1))).add(c_AST));
         currentAST.root = superClassClause_AST;
         currentAST.child = superClassClause_AST != null && superClassClause_AST.getFirstChild() != null ? superClassClause_AST.getFirstChild() : superClassClause_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = superClassClause_AST;
   }

   public final void implementsClause() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST implementsClause_AST = null;
      Token i = null;
      AST i_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 127:
         i = this.LT(1);
         this.astFactory.create(i);
         this.match(127);
         this.nls();
         this.classOrInterfaceType(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(this.LA(1) == 96) {
            this.match(96);
            this.nls();
            this.classOrInterfaceType(true);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         this.nls();
      case 122:
      case 201:
         if (this.inputState.guessing == 0) {
            implementsClause_AST = currentAST.root;
            implementsClause_AST = this.astFactory.make((new ASTArray(2)).add(this.create(18, "IMPLEMENTS_CLAUSE", (Token)first, (Token)this.LT(1))).add(implementsClause_AST));
            currentAST.root = implementsClause_AST;
            currentAST.child = implementsClause_AST != null && implementsClause_AST.getFirstChild() != null ? implementsClause_AST.getFirstChild() : implementsClause_AST;
            currentAST.advanceChildToEnd();
         }

         implementsClause_AST = currentAST.root;
         this.returnAST = implementsClause_AST;
         return;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final void classBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST classBlock_AST = null;
      Token first = this.LT(1);
      this.match(122);
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 84:
      case 89:
      case 90:
      case 91:
      case 92:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
         this.classField();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      case 123:
      case 124:
      case 201:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 84:
         case 89:
         case 90:
         case 91:
         case 92:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
            this.classField();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         case 123:
         case 124:
         case 201:
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      }

      this.match(123);
      if (this.inputState.guessing == 0) {
         classBlock_AST = currentAST.root;
         classBlock_AST = this.astFactory.make((new ASTArray(2)).add(this.create(6, "OBJBLOCK", (Token)first, (Token)this.LT(1))).add(classBlock_AST));
         currentAST.root = classBlock_AST;
         currentAST.child = classBlock_AST != null && classBlock_AST.getFirstChild() != null ? classBlock_AST.getFirstChild() : classBlock_AST;
         currentAST.advanceChildToEnd();
      }

      classBlock_AST = currentAST.root;
      this.returnAST = classBlock_AST;
   }

   public final void interfaceExtends() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST interfaceExtends_AST = null;
      Token e = null;
      AST e_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 94:
         e = this.LT(1);
         this.astFactory.create(e);
         this.match(94);
         this.nls();
         this.classOrInterfaceType(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(this.LA(1) == 96) {
            this.match(96);
            this.nls();
            this.classOrInterfaceType(true);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         this.nls();
      case 122:
         if (this.inputState.guessing == 0) {
            interfaceExtends_AST = currentAST.root;
            interfaceExtends_AST = this.astFactory.make((new ASTArray(2)).add(this.create(17, "EXTENDS_CLAUSE", (Token)first, (Token)this.LT(1))).add(interfaceExtends_AST));
            currentAST.root = interfaceExtends_AST;
            currentAST.child = interfaceExtends_AST != null && interfaceExtends_AST.getFirstChild() != null ? interfaceExtends_AST.getFirstChild() : interfaceExtends_AST;
            currentAST.advanceChildToEnd();
         }

         interfaceExtends_AST = currentAST.root;
         this.returnAST = interfaceExtends_AST;
         return;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final void interfaceBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST interfaceBlock_AST = null;
      Token first = this.LT(1);
      this.match(122);
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 84:
      case 89:
      case 90:
      case 91:
      case 92:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
         this.interfaceField();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      case 123:
      case 124:
      case 201:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 84:
         case 89:
         case 90:
         case 91:
         case 92:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
            this.interfaceField();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         case 123:
         case 124:
         case 201:
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      }

      this.match(123);
      if (this.inputState.guessing == 0) {
         interfaceBlock_AST = currentAST.root;
         interfaceBlock_AST = this.astFactory.make((new ASTArray(2)).add(this.create(6, "OBJBLOCK", (Token)first, (Token)this.LT(1))).add(interfaceBlock_AST));
         currentAST.root = interfaceBlock_AST;
         currentAST.child = interfaceBlock_AST != null && interfaceBlock_AST.getFirstChild() != null ? interfaceBlock_AST.getFirstChild() : interfaceBlock_AST;
         currentAST.advanceChildToEnd();
      }

      interfaceBlock_AST = currentAST.root;
      this.returnAST = interfaceBlock_AST;
   }

   public final void enumBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST enumBlock_AST = null;
      Token first = this.LT(1);
      this.match(122);
      this.nls();
      boolean synPredMatched135 = false;
      if ((this.LA(1) == 84 || this.LA(1) == 92) && _tokenSet_49.member(this.LA(2))) {
         int _m135 = this.mark();
         synPredMatched135 = true;
         ++this.inputState.guessing;

         try {
            this.enumConstantsStart();
         } catch (RecognitionException var7) {
            synPredMatched135 = false;
         }

         this.rewind(_m135);
         --this.inputState.guessing;
      }

      if (synPredMatched135) {
         this.enumConstants();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else {
         if (!_tokenSet_50.member(this.LA(1)) || !_tokenSet_51.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 84:
         case 89:
         case 90:
         case 91:
         case 92:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
            this.classField();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         case 123:
         case 124:
         case 201:
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      }

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 84:
         case 89:
         case 90:
         case 91:
         case 92:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
            this.classField();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         case 123:
         case 124:
         case 201:
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      }

      this.match(123);
      if (this.inputState.guessing == 0) {
         enumBlock_AST = currentAST.root;
         enumBlock_AST = this.astFactory.make((new ASTArray(2)).add(this.create(6, "OBJBLOCK", (Token)first, (Token)this.LT(1))).add(enumBlock_AST));
         currentAST.root = enumBlock_AST;
         currentAST.child = enumBlock_AST != null && enumBlock_AST.getFirstChild() != null ? enumBlock_AST.getFirstChild() : enumBlock_AST;
         currentAST.advanceChildToEnd();
      }

      enumBlock_AST = currentAST.root;
      this.returnAST = enumBlock_AST;
   }

   public final void annotationBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationBlock_AST = null;
      Token first = this.LT(1);
      this.match(122);
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 84:
      case 89:
      case 90:
      case 91:
      case 92:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
         this.annotationField();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      case 123:
      case 124:
      case 201:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 84:
         case 89:
         case 90:
         case 91:
         case 92:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
            this.annotationField();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         case 123:
         case 124:
         case 201:
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      }

      this.match(123);
      if (this.inputState.guessing == 0) {
         annotationBlock_AST = currentAST.root;
         annotationBlock_AST = this.astFactory.make((new ASTArray(2)).add(this.create(6, "OBJBLOCK", (Token)first, (Token)this.LT(1))).add(annotationBlock_AST));
         currentAST.root = annotationBlock_AST;
         currentAST.child = annotationBlock_AST != null && annotationBlock_AST.getFirstChild() != null ? annotationBlock_AST.getFirstChild() : annotationBlock_AST;
         currentAST.advanceChildToEnd();
      }

      annotationBlock_AST = currentAST.root;
      this.returnAST = annotationBlock_AST;
   }

   public final void typeParameter() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeParameter_AST = null;
      Token id = null;
      AST id_AST = null;
      Token first = this.LT(1);
      id = this.LT(1);
      id_AST = this.astFactory.create(id);
      this.astFactory.addASTChild(currentAST, id_AST);
      this.match(84);
      if (this.LA(1) != 94 || this.LA(2) != 84 && this.LA(2) != 201) {
         if (!_tokenSet_52.member(this.LA(1)) || !_tokenSet_53.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } else {
         this.typeParameterBounds();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         typeParameter_AST = currentAST.root;
         typeParameter_AST = this.astFactory.make((new ASTArray(2)).add(this.create(72, "TYPE_PARAMETER", (Token)first, (Token)this.LT(1))).add(typeParameter_AST));
         currentAST.root = typeParameter_AST;
         currentAST.child = typeParameter_AST != null && typeParameter_AST.getFirstChild() != null ? typeParameter_AST.getFirstChild() : typeParameter_AST;
         currentAST.advanceChildToEnd();
      }

      typeParameter_AST = currentAST.root;
      this.returnAST = typeParameter_AST;
   }

   public final void typeParameterBounds() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeParameterBounds_AST = null;
      Token first = this.LT(1);
      this.match(94);
      this.nls();
      this.classOrInterfaceType(true);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 121) {
         this.match(121);
         this.nls();
         this.classOrInterfaceType(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         typeParameterBounds_AST = currentAST.root;
         typeParameterBounds_AST = this.astFactory.make((new ASTArray(2)).add(this.create(74, "TYPE_UPPER_BOUNDS", (Token)first, (Token)this.LT(1))).add(typeParameterBounds_AST));
         currentAST.root = typeParameterBounds_AST;
         currentAST.child = typeParameterBounds_AST != null && typeParameterBounds_AST.getFirstChild() != null ? typeParameterBounds_AST.getFirstChild() : typeParameterBounds_AST;
         currentAST.advanceChildToEnd();
      }

      typeParameterBounds_AST = currentAST.root;
      this.returnAST = typeParameterBounds_AST;
   }

   public final void classField() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST classField_AST = null;
      AST mc_AST = null;
      AST ctor_AST = null;
      AST dg_AST = null;
      AST mad_AST = null;
      AST dd_AST = null;
      AST mods_AST = null;
      AST td_AST = null;
      AST s3_AST = null;
      AST s4_AST = null;
      Token first = this.LT(1);
      boolean synPredMatched189 = false;
      if (_tokenSet_54.member(this.LA(1)) && _tokenSet_55.member(this.LA(2))) {
         int _m189 = this.mark();
         synPredMatched189 = true;
         ++this.inputState.guessing;

         try {
            this.constructorStart();
         } catch (RecognitionException var24) {
            synPredMatched189 = false;
         }

         this.rewind(_m189);
         --this.inputState.guessing;
      }

      if (synPredMatched189) {
         this.modifiersOpt();
         mc_AST = this.returnAST;
         this.constructorDefinition(mc_AST);
         ctor_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            classField_AST = currentAST.root;
            classField_AST = ctor_AST;
            currentAST.root = ctor_AST;
            currentAST.child = ctor_AST != null && ctor_AST.getFirstChild() != null ? ctor_AST.getFirstChild() : ctor_AST;
            currentAST.advanceChildToEnd();
         }
      } else {
         boolean synPredMatched191 = false;
         if (_tokenSet_12.member(this.LA(1)) && _tokenSet_13.member(this.LA(2))) {
            int _m191 = this.mark();
            synPredMatched191 = true;
            ++this.inputState.guessing;

            try {
               this.genericMethodStart();
            } catch (RecognitionException var23) {
               synPredMatched191 = false;
            }

            this.rewind(_m191);
            --this.inputState.guessing;
         }

         if (synPredMatched191) {
            this.genericMethod();
            dg_AST = this.returnAST;
            if (this.inputState.guessing == 0) {
               classField_AST = currentAST.root;
               classField_AST = dg_AST;
               currentAST.root = dg_AST;
               currentAST.child = dg_AST != null && dg_AST.getFirstChild() != null ? dg_AST.getFirstChild() : dg_AST;
               currentAST.advanceChildToEnd();
            }
         } else {
            boolean synPredMatched193 = false;
            if (_tokenSet_12.member(this.LA(1)) && _tokenSet_14.member(this.LA(2))) {
               int _m193 = this.mark();
               synPredMatched193 = true;
               ++this.inputState.guessing;

               try {
                  this.multipleAssignmentDeclarationStart();
               } catch (RecognitionException var22) {
                  synPredMatched193 = false;
               }

               this.rewind(_m193);
               --this.inputState.guessing;
            }

            if (synPredMatched193) {
               this.multipleAssignmentDeclaration();
               mad_AST = this.returnAST;
               if (this.inputState.guessing == 0) {
                  classField_AST = currentAST.root;
                  classField_AST = mad_AST;
                  currentAST.root = mad_AST;
                  currentAST.child = mad_AST != null && mad_AST.getFirstChild() != null ? mad_AST.getFirstChild() : mad_AST;
                  currentAST.advanceChildToEnd();
               }
            } else {
               boolean synPredMatched195 = false;
               if (_tokenSet_15.member(this.LA(1)) && _tokenSet_16.member(this.LA(2))) {
                  int _m195 = this.mark();
                  synPredMatched195 = true;
                  ++this.inputState.guessing;

                  try {
                     this.declarationStart();
                  } catch (RecognitionException var21) {
                     synPredMatched195 = false;
                  }

                  this.rewind(_m195);
                  --this.inputState.guessing;
               }

               if (synPredMatched195) {
                  this.declaration();
                  dd_AST = this.returnAST;
                  if (this.inputState.guessing == 0) {
                     classField_AST = currentAST.root;
                     classField_AST = dd_AST;
                     currentAST.root = dd_AST;
                     currentAST.child = dd_AST != null && dd_AST.getFirstChild() != null ? dd_AST.getFirstChild() : dd_AST;
                     currentAST.advanceChildToEnd();
                  }
               } else {
                  boolean synPredMatched197 = false;
                  if (_tokenSet_21.member(this.LA(1)) && _tokenSet_22.member(this.LA(2))) {
                     int _m197 = this.mark();
                     synPredMatched197 = true;
                     ++this.inputState.guessing;

                     try {
                        this.typeDeclarationStart();
                     } catch (RecognitionException var20) {
                        synPredMatched197 = false;
                     }

                     this.rewind(_m197);
                     --this.inputState.guessing;
                  }

                  if (synPredMatched197) {
                     this.modifiersOpt();
                     mods_AST = this.returnAST;
                     this.typeDefinitionInternal(mods_AST);
                     td_AST = this.returnAST;
                     if (this.inputState.guessing == 0) {
                        classField_AST = currentAST.root;
                        classField_AST = td_AST;
                        currentAST.root = td_AST;
                        currentAST.child = td_AST != null && td_AST.getFirstChild() != null ? td_AST.getFirstChild() : td_AST;
                        currentAST.advanceChildToEnd();
                     }
                  } else if (this.LA(1) == 80 && (this.LA(2) == 122 || this.LA(2) == 201)) {
                     this.match(80);
                     this.nls();
                     this.compoundStatement();
                     s3_AST = this.returnAST;
                     if (this.inputState.guessing == 0) {
                        classField_AST = currentAST.root;
                        classField_AST = this.astFactory.make((new ASTArray(2)).add(this.create(11, "STATIC_INIT", (Token)first, (Token)this.LT(1))).add(s3_AST));
                        currentAST.root = classField_AST;
                        currentAST.child = classField_AST != null && classField_AST.getFirstChild() != null ? classField_AST.getFirstChild() : classField_AST;
                        currentAST.advanceChildToEnd();
                     }
                  } else {
                     if (this.LA(1) != 122) {
                        throw new NoViableAltException(this.LT(1), this.getFilename());
                     }

                     this.compoundStatement();
                     s4_AST = this.returnAST;
                     if (this.inputState.guessing == 0) {
                        classField_AST = currentAST.root;
                        classField_AST = this.astFactory.make((new ASTArray(2)).add(this.create(10, "INSTANCE_INIT", (Token)first, (Token)this.LT(1))).add(s4_AST));
                        currentAST.root = classField_AST;
                        currentAST.child = classField_AST != null && classField_AST.getFirstChild() != null ? classField_AST.getFirstChild() : classField_AST;
                        currentAST.advanceChildToEnd();
                     }
                  }
               }
            }
         }
      }

      this.returnAST = classField_AST;
   }

   public final void interfaceField() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST interfaceField_AST = null;
      AST d_AST = null;
      AST mods_AST = null;
      AST td_AST = null;
      boolean synPredMatched201 = false;
      if (_tokenSet_15.member(this.LA(1)) && _tokenSet_16.member(this.LA(2))) {
         int _m201 = this.mark();
         synPredMatched201 = true;
         ++this.inputState.guessing;

         try {
            this.declarationStart();
         } catch (RecognitionException var11) {
            synPredMatched201 = false;
         }

         this.rewind(_m201);
         --this.inputState.guessing;
      }

      if (synPredMatched201) {
         this.declaration();
         d_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            interfaceField_AST = currentAST.root;
            interfaceField_AST = d_AST;
            currentAST.root = d_AST;
            currentAST.child = d_AST != null && d_AST.getFirstChild() != null ? d_AST.getFirstChild() : d_AST;
            currentAST.advanceChildToEnd();
         }
      } else {
         boolean synPredMatched203 = false;
         if (_tokenSet_21.member(this.LA(1)) && _tokenSet_22.member(this.LA(2))) {
            int _m203 = this.mark();
            synPredMatched203 = true;
            ++this.inputState.guessing;

            try {
               this.typeDeclarationStart();
            } catch (RecognitionException var10) {
               synPredMatched203 = false;
            }

            this.rewind(_m203);
            --this.inputState.guessing;
         }

         if (!synPredMatched203) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.modifiersOpt();
         mods_AST = this.returnAST;
         this.typeDefinitionInternal(mods_AST);
         td_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            interfaceField_AST = currentAST.root;
            interfaceField_AST = td_AST;
            currentAST.root = td_AST;
            currentAST.child = td_AST != null && td_AST.getFirstChild() != null ? td_AST.getFirstChild() : td_AST;
            currentAST.advanceChildToEnd();
         }
      }

      this.returnAST = interfaceField_AST;
   }

   public final void annotationField() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST annotationField_AST = null;
      AST mods_AST = null;
      AST td_AST = null;
      AST t_AST = null;
      Token i = null;
      AST i_AST = null;
      AST amvi_AST = null;
      AST v_AST = null;
      Token first = this.LT(1);
      this.modifiersOpt();
      mods_AST = this.returnAST;
      switch(this.LA(1)) {
      case 84:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.typeSpec(false);
         t_AST = this.returnAST;
         boolean synPredMatched159 = false;
         if (this.LA(1) == 84 && this.LA(2) == 88) {
            int _m159 = this.mark();
            synPredMatched159 = true;
            ++this.inputState.guessing;

            try {
               this.match(84);
               this.match(88);
            } catch (RecognitionException var14) {
               synPredMatched159 = false;
            }

            this.rewind(_m159);
            --this.inputState.guessing;
         }

         if (synPredMatched159) {
            i = this.LT(1);
            i_AST = this.astFactory.create(i);
            this.match(84);
            this.match(88);
            this.match(119);
            switch(this.LA(1)) {
            case 123:
            case 124:
            case 201:
               break;
            case 125:
               this.match(125);
               this.nls();
               this.annotationMemberValueInitializer();
               amvi_AST = this.returnAST;
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            if (this.inputState.guessing == 0) {
               annotationField_AST = currentAST.root;
               annotationField_AST = this.astFactory.make((new ASTArray(5)).add(this.create(67, "ANNOTATION_FIELD_DEF", (Token)first, (Token)this.LT(1))).add(mods_AST).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t_AST))).add(i_AST).add(amvi_AST));
               currentAST.root = annotationField_AST;
               currentAST.child = annotationField_AST != null && annotationField_AST.getFirstChild() != null ? annotationField_AST.getFirstChild() : annotationField_AST;
               currentAST.advanceChildToEnd();
            }
         } else {
            if (this.LA(1) != 84 && this.LA(1) != 85 || !_tokenSet_56.member(this.LA(2))) {
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.variableDefinitions(mods_AST, t_AST);
            v_AST = this.returnAST;
            if (this.inputState.guessing == 0) {
               annotationField_AST = currentAST.root;
               annotationField_AST = v_AST;
               currentAST.root = v_AST;
               currentAST.child = v_AST != null && v_AST.getFirstChild() != null ? v_AST.getFirstChild() : v_AST;
               currentAST.advanceChildToEnd();
            }
         }
         break;
      case 85:
      case 86:
      case 87:
      case 88:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 89:
      case 90:
      case 91:
      case 92:
         this.typeDefinitionInternal(mods_AST);
         td_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            annotationField_AST = currentAST.root;
            annotationField_AST = td_AST;
            currentAST.root = td_AST;
            currentAST.child = td_AST != null && td_AST.getFirstChild() != null ? td_AST.getFirstChild() : td_AST;
            currentAST.advanceChildToEnd();
         }
      }

      this.returnAST = annotationField_AST;
   }

   public final void enumConstantsStart() throws RecognitionException, TokenStreamException {
      ASTPair currentAST;
      AST enumConstantsStart_AST;
      this.returnAST = null;
      currentAST = new ASTPair();
      enumConstantsStart_AST = null;
      this.annotationsOpt();
      this.astFactory.addASTChild(currentAST, this.returnAST);
      AST tmp168_AST = null;
      tmp168_AST = this.astFactory.create(this.LT(1));
      this.astFactory.addASTChild(currentAST, tmp168_AST);
      this.match(84);
      AST tmp171_AST;
      label16:
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 84:
      case 92:
      case 96:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 123:
      case 124:
      case 201:
         this.nls();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 84:
         case 92:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
            this.declarationStart();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            break label16;
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 82:
         case 83:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 93:
         case 94:
         case 95:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 122:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 96:
            tmp171_AST = null;
            tmp171_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp171_AST);
            this.match(96);
            break label16;
         case 123:
            tmp171_AST = null;
            tmp171_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp171_AST);
            this.match(123);
            break label16;
         case 124:
            tmp171_AST = null;
            tmp171_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp171_AST);
            this.match(124);
            break label16;
         }
      case 88:
         tmp171_AST = null;
         tmp171_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp171_AST);
         this.match(88);
         break;
      case 122:
         tmp171_AST = null;
         tmp171_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp171_AST);
         this.match(122);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      enumConstantsStart_AST = currentAST.root;
      this.returnAST = enumConstantsStart_AST;
   }

   public final void enumConstants() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST enumConstants_AST = null;
      this.enumConstant();
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(true) {
         boolean synPredMatched147 = false;
         if (_tokenSet_57.member(this.LA(1)) && _tokenSet_58.member(this.LA(2))) {
            int _m147 = this.mark();
            synPredMatched147 = true;
            ++this.inputState.guessing;

            try {
               this.nls();
               switch(this.LA(1)) {
               case 37:
               case 38:
               case 42:
               case 80:
               case 81:
               case 84:
               case 89:
               case 90:
               case 91:
               case 92:
               case 100:
               case 101:
               case 102:
               case 103:
               case 104:
               case 105:
               case 106:
               case 107:
               case 108:
               case 111:
               case 112:
               case 113:
               case 114:
               case 115:
               case 116:
               case 117:
               case 118:
               case 122:
                  this.classField();
                  break;
               case 39:
               case 40:
               case 41:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 48:
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 64:
               case 65:
               case 66:
               case 67:
               case 68:
               case 69:
               case 70:
               case 71:
               case 72:
               case 73:
               case 74:
               case 75:
               case 76:
               case 77:
               case 78:
               case 79:
               case 82:
               case 83:
               case 85:
               case 86:
               case 87:
               case 88:
               case 93:
               case 94:
               case 95:
               case 96:
               case 97:
               case 98:
               case 99:
               case 109:
               case 110:
               case 119:
               case 120:
               case 121:
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               case 123:
                  this.match(123);
               }
            } catch (RecognitionException var8) {
               synPredMatched147 = false;
            }

            this.rewind(_m147);
            --this.inputState.guessing;
         }

         if (synPredMatched147) {
            if (this.inputState.guessing == 0) {
               break;
            }
         } else {
            if (this.LA(1) != 96 && this.LA(1) != 201 || !_tokenSet_59.member(this.LA(2))) {
               break;
            }

            this.nls();
            this.match(96);
            boolean synPredMatched151 = false;
            if (_tokenSet_57.member(this.LA(1)) && _tokenSet_58.member(this.LA(2))) {
               int _m151 = this.mark();
               synPredMatched151 = true;
               ++this.inputState.guessing;

               try {
                  this.nls();
                  switch(this.LA(1)) {
                  case 37:
                  case 38:
                  case 42:
                  case 80:
                  case 81:
                  case 84:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 100:
                  case 101:
                  case 102:
                  case 103:
                  case 104:
                  case 105:
                  case 106:
                  case 107:
                  case 108:
                  case 111:
                  case 112:
                  case 113:
                  case 114:
                  case 115:
                  case 116:
                  case 117:
                  case 118:
                  case 122:
                     this.classField();
                     break;
                  case 39:
                  case 40:
                  case 41:
                  case 43:
                  case 44:
                  case 45:
                  case 46:
                  case 47:
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 59:
                  case 60:
                  case 61:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 75:
                  case 76:
                  case 77:
                  case 78:
                  case 79:
                  case 82:
                  case 83:
                  case 85:
                  case 86:
                  case 87:
                  case 88:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 109:
                  case 110:
                  case 119:
                  case 120:
                  case 121:
                  default:
                     throw new NoViableAltException(this.LT(1), this.getFilename());
                  case 123:
                     this.match(123);
                  }
               } catch (RecognitionException var10) {
                  synPredMatched151 = false;
               }

               this.rewind(_m151);
               --this.inputState.guessing;
            }

            if (synPredMatched151) {
               if (this.inputState.guessing == 0) {
                  break;
               }
            } else {
               boolean synPredMatched153 = false;
               if (_tokenSet_60.member(this.LA(1)) && _tokenSet_61.member(this.LA(2))) {
                  int _m153 = this.mark();
                  synPredMatched153 = true;
                  ++this.inputState.guessing;

                  try {
                     this.nls();
                     this.annotationsOpt();
                     this.match(84);
                  } catch (RecognitionException var9) {
                     synPredMatched153 = false;
                  }

                  this.rewind(_m153);
                  --this.inputState.guessing;
               }

               if (!synPredMatched153) {
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               }

               this.nls();
               this.enumConstant();
               this.astFactory.addASTChild(currentAST, this.returnAST);
            }
         }
      }

      enumConstants_AST = currentAST.root;
      this.returnAST = enumConstants_AST;
   }

   public final void enumConstant() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST enumConstant_AST = null;
      AST an_AST = null;
      Token i = null;
      AST i_AST = null;
      AST a_AST = null;
      AST b_AST = null;
      Token first = this.LT(1);
      this.annotationsOpt();
      an_AST = this.returnAST;
      i = this.LT(1);
      i_AST = this.astFactory.create(i);
      this.match(84);
      switch(this.LA(1)) {
      case 88:
         this.match(88);
         this.argList();
         a_AST = this.returnAST;
         this.match(119);
      case 96:
      case 122:
      case 123:
      case 124:
      case 201:
         switch(this.LA(1)) {
         case 96:
         case 123:
         case 124:
         case 201:
            break;
         case 122:
            this.enumConstantBlock();
            b_AST = this.returnAST;
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            enumConstant_AST = currentAST.root;
            enumConstant_AST = this.astFactory.make((new ASTArray(5)).add(this.create(61, "ENUM_CONSTANT_DEF", (Token)first, (Token)this.LT(1))).add(an_AST).add(i_AST).add(a_AST).add(b_AST));
            currentAST.root = enumConstant_AST;
            currentAST.child = enumConstant_AST != null && enumConstant_AST.getFirstChild() != null ? enumConstant_AST.getFirstChild() : enumConstant_AST;
            currentAST.advanceChildToEnd();
         }

         this.returnAST = enumConstant_AST;
         return;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final void argList() throws RecognitionException, TokenStreamException {
      ASTPair currentAST;
      AST argList_AST;
      int hls;
      this.returnAST = null;
      currentAST = new ASTPair();
      argList_AST = null;
      Token first = this.LT(1);
      Token lastComma = null;
      hls = 0;
      int hls2 = false;
      boolean hasClosureList = false;
      boolean trailingComma = false;
      boolean sce = false;
      label105:
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
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
      case 109:
      case 110:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
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
      case 143:
      case 144:
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
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         hls = this.argument();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         switch(this.LA(1)) {
         case 83:
         case 96:
         case 119:
            while(this.LA(1) == 96) {
               if (this.inputState.guessing == 0) {
                  lastComma = this.LT(1);
               }

               this.match(96);
               switch(this.LA(1)) {
               case 37:
               case 38:
               case 39:
               case 41:
               case 42:
               case 78:
               case 79:
               case 80:
               case 81:
               case 82:
               case 84:
               case 85:
               case 88:
               case 89:
               case 90:
               case 91:
               case 92:
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
               case 109:
               case 110:
               case 111:
               case 112:
               case 113:
               case 114:
               case 115:
               case 116:
               case 117:
               case 118:
               case 122:
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
               case 143:
               case 144:
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
               case 191:
               case 192:
               case 193:
               case 195:
               case 196:
               case 197:
               case 198:
               case 199:
               case 200:
                  int hls2 = this.argument();
                  this.astFactory.addASTChild(currentAST, this.returnAST);
                  if (this.inputState.guessing == 0) {
                     hls |= hls2;
                  }
                  break;
               case 40:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 48:
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 64:
               case 65:
               case 66:
               case 67:
               case 68:
               case 69:
               case 70:
               case 71:
               case 72:
               case 73:
               case 74:
               case 75:
               case 76:
               case 77:
               case 86:
               case 87:
               case 93:
               case 97:
               case 98:
               case 99:
               case 120:
               case 121:
               case 123:
               case 124:
               case 129:
               case 130:
               case 131:
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
               case 194:
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               case 83:
               case 96:
               case 119:
                  if (this.inputState.guessing == 0) {
                     if (trailingComma) {
                        throw new NoViableAltException(lastComma, this.getFilename());
                     }

                     trailingComma = true;
                  }
               }
            }

            if (this.inputState.guessing == 0) {
               argList_AST = currentAST.root;
               argList_AST = this.astFactory.make((new ASTArray(2)).add(this.create(32, "ELIST", (Token)first, (Token)this.LT(1))).add(argList_AST));
               currentAST.root = argList_AST;
               currentAST.child = argList_AST != null && argList_AST.getFirstChild() != null ? argList_AST.getFirstChild() : argList_AST;
               currentAST.advanceChildToEnd();
            }
            break label105;
         case 124:
            int _cnt507;
            for(_cnt507 = 0; this.LA(1) == 124; ++_cnt507) {
               this.match(124);
               if (this.inputState.guessing == 0) {
                  hasClosureList = true;
               }

               switch(this.LA(1)) {
               case 37:
               case 38:
               case 42:
               case 80:
               case 81:
               case 82:
               case 84:
               case 85:
               case 88:
               case 92:
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
               case 111:
               case 112:
               case 113:
               case 114:
               case 115:
               case 116:
               case 117:
               case 118:
               case 122:
               case 128:
               case 138:
               case 139:
               case 140:
               case 141:
               case 142:
               case 143:
               case 144:
               case 152:
               case 154:
               case 155:
               case 156:
               case 186:
               case 189:
               case 191:
               case 192:
               case 193:
               case 195:
               case 196:
               case 197:
               case 198:
               case 199:
               case 200:
                  sce = this.strictContextExpression(true);
                  this.astFactory.addASTChild(currentAST, this.returnAST);
                  break;
               case 39:
               case 40:
               case 41:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 48:
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 64:
               case 65:
               case 66:
               case 67:
               case 68:
               case 69:
               case 70:
               case 71:
               case 72:
               case 73:
               case 74:
               case 75:
               case 76:
               case 77:
               case 78:
               case 79:
               case 86:
               case 87:
               case 89:
               case 90:
               case 91:
               case 93:
               case 94:
               case 96:
               case 97:
               case 98:
               case 99:
               case 109:
               case 110:
               case 120:
               case 121:
               case 123:
               case 125:
               case 126:
               case 127:
               case 129:
               case 130:
               case 131:
               case 132:
               case 133:
               case 134:
               case 135:
               case 136:
               case 137:
               case 145:
               case 146:
               case 147:
               case 148:
               case 149:
               case 150:
               case 151:
               case 153:
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
               case 194:
               default:
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               case 83:
               case 119:
               case 124:
                  if (this.inputState.guessing == 0) {
                     this.astFactory.addASTChild(currentAST, this.astFactory.create(36, "EMPTY_STAT"));
                  }
               }
            }

            if (_cnt507 < 1) {
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            if (this.inputState.guessing == 0) {
               argList_AST = currentAST.root;
               argList_AST = this.astFactory.make((new ASTArray(2)).add(this.create(76, "CLOSURE_LIST", (Token)first, (Token)this.LT(1))).add(argList_AST));
               currentAST.root = argList_AST;
               currentAST.child = argList_AST != null && argList_AST.getFirstChild() != null ? argList_AST.getFirstChild() : argList_AST;
               currentAST.advanceChildToEnd();
            }
            break label105;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 86:
      case 87:
      case 93:
      case 96:
      case 97:
      case 98:
      case 99:
      case 120:
      case 121:
      case 123:
      case 124:
      case 129:
      case 130:
      case 131:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 83:
      case 119:
         if (this.inputState.guessing == 0) {
            argList_AST = currentAST.root;
            argList_AST = this.create(32, "ELIST", (Token)first, (Token)this.LT(1));
            currentAST.root = argList_AST;
            currentAST.child = argList_AST != null && argList_AST.getFirstChild() != null ? argList_AST.getFirstChild() : argList_AST;
            currentAST.advanceChildToEnd();
         }
      }

      if (this.inputState.guessing == 0) {
         this.argListHasLabels = (hls & 1) != 0;
      }

      argList_AST = currentAST.root;
      this.returnAST = argList_AST;
   }

   public final void enumConstantBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST enumConstantBlock_AST = null;
      Token first = this.LT(1);
      this.match(122);
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 84:
      case 86:
      case 89:
      case 90:
      case 91:
      case 92:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
         this.enumConstantField();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      case 123:
      case 124:
      case 201:
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 84:
         case 86:
         case 89:
         case 90:
         case 91:
         case 92:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
            this.enumConstantField();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         case 123:
         case 124:
         case 201:
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      }

      this.match(123);
      if (this.inputState.guessing == 0) {
         enumConstantBlock_AST = currentAST.root;
         enumConstantBlock_AST = this.astFactory.make((new ASTArray(2)).add(this.create(6, "OBJBLOCK", (Token)first, (Token)this.LT(1))).add(enumConstantBlock_AST));
         currentAST.root = enumConstantBlock_AST;
         currentAST.child = enumConstantBlock_AST != null && enumConstantBlock_AST.getFirstChild() != null ? enumConstantBlock_AST.getFirstChild() : enumConstantBlock_AST;
         currentAST.advanceChildToEnd();
      }

      enumConstantBlock_AST = currentAST.root;
      this.returnAST = enumConstantBlock_AST;
   }

   public final void enumConstantField() throws RecognitionException, TokenStreamException {
      AST enumConstantField_AST;
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      enumConstantField_AST = null;
      AST mods_AST = null;
      AST td_AST = null;
      AST tp_AST = null;
      AST t_AST = null;
      AST param_AST = null;
      AST tc_AST = null;
      AST s2_AST = null;
      AST v_AST = null;
      AST s4_AST = null;
      Token first = this.LT(1);
      label138:
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 84:
      case 86:
      case 89:
      case 90:
      case 91:
      case 92:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
         this.modifiersOpt();
         mods_AST = this.returnAST;
         switch(this.LA(1)) {
         case 84:
         case 86:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
            switch(this.LA(1)) {
            case 85:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            case 86:
               this.typeParameters();
               tp_AST = this.returnAST;
            case 84:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
               this.typeSpec(false);
               t_AST = this.returnAST;
               boolean synPredMatched174 = false;
               if (this.LA(1) == 84 && this.LA(2) == 88) {
                  int _m174 = this.mark();
                  synPredMatched174 = true;
                  ++this.inputState.guessing;

                  try {
                     this.match(84);
                     this.match(88);
                  } catch (RecognitionException var19) {
                     synPredMatched174 = false;
                  }

                  this.rewind(_m174);
                  --this.inputState.guessing;
               }

               if (synPredMatched174) {
                  AST tmp181_AST = null;
                  tmp181_AST = this.astFactory.create(this.LT(1));
                  this.match(84);
                  this.match(88);
                  this.parameterDeclarationList();
                  param_AST = this.returnAST;
                  this.match(119);
                  boolean synPredMatched177 = false;
                  if ((this.LA(1) == 126 || this.LA(1) == 201) && _tokenSet_28.member(this.LA(2))) {
                     int _m177 = this.mark();
                     synPredMatched177 = true;
                     ++this.inputState.guessing;

                     try {
                        this.nls();
                        this.match(126);
                     } catch (RecognitionException var18) {
                        synPredMatched177 = false;
                     }

                     this.rewind(_m177);
                     --this.inputState.guessing;
                  }

                  if (synPredMatched177) {
                     this.throwsClause();
                     tc_AST = this.returnAST;
                  } else if (!_tokenSet_62.member(this.LA(1)) || !_tokenSet_63.member(this.LA(2))) {
                     throw new NoViableAltException(this.LT(1), this.getFilename());
                  }

                  switch(this.LA(1)) {
                  case 122:
                     this.compoundStatement();
                     s2_AST = this.returnAST;
                  case 123:
                  case 124:
                  case 201:
                     break;
                  default:
                     throw new NoViableAltException(this.LT(1), this.getFilename());
                  }

                  if (this.inputState.guessing == 0) {
                     enumConstantField_AST = currentAST.root;
                     enumConstantField_AST = this.astFactory.make((new ASTArray(8)).add(this.create(8, "METHOD_DEF", (Token)first, (Token)this.LT(1))).add(mods_AST).add(tp_AST).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t_AST))).add(tmp181_AST).add(param_AST).add(tc_AST).add(s2_AST));
                     currentAST.root = enumConstantField_AST;
                     currentAST.child = enumConstantField_AST != null && enumConstantField_AST.getFirstChild() != null ? enumConstantField_AST.getFirstChild() : enumConstantField_AST;
                     currentAST.advanceChildToEnd();
                  }
                  break label138;
               } else {
                  if ((this.LA(1) == 84 || this.LA(1) == 85) && _tokenSet_56.member(this.LA(2))) {
                     this.variableDefinitions(mods_AST, t_AST);
                     v_AST = this.returnAST;
                     if (this.inputState.guessing == 0) {
                        enumConstantField_AST = currentAST.root;
                        enumConstantField_AST = v_AST;
                        currentAST.root = v_AST;
                        currentAST.child = v_AST != null && v_AST.getFirstChild() != null ? v_AST.getFirstChild() : v_AST;
                        currentAST.advanceChildToEnd();
                     }
                     break label138;
                  }

                  throw new NoViableAltException(this.LT(1), this.getFilename());
               }
            }
         case 85:
         case 87:
         case 88:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 89:
         case 90:
         case 91:
         case 92:
            this.typeDefinitionInternal(mods_AST);
            td_AST = this.returnAST;
            if (this.inputState.guessing == 0) {
               enumConstantField_AST = currentAST.root;
               enumConstantField_AST = td_AST;
               currentAST.root = td_AST;
               currentAST.child = td_AST != null && td_AST.getFirstChild() != null ? td_AST.getFirstChild() : td_AST;
               currentAST.advanceChildToEnd();
            }
            break label138;
         }
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 82:
      case 83:
      case 85:
      case 87:
      case 88:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      case 119:
      case 120:
      case 121:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 122:
         this.compoundStatement();
         s4_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            enumConstantField_AST = currentAST.root;
            enumConstantField_AST = this.astFactory.make((new ASTArray(2)).add(this.create(10, "INSTANCE_INIT", (Token)first, (Token)this.LT(1))).add(s4_AST));
            currentAST.root = enumConstantField_AST;
            currentAST.child = enumConstantField_AST != null && enumConstantField_AST.getFirstChild() != null ? enumConstantField_AST.getFirstChild() : enumConstantField_AST;
            currentAST.advanceChildToEnd();
         }
      }

      this.returnAST = enumConstantField_AST;
   }

   public final void parameterDeclarationList() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST parameterDeclarationList_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 37:
      case 81:
      case 84:
      case 92:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 129:
         this.parameterDeclaration();
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(this.LA(1) == 96) {
            this.match(96);
            this.nls();
            this.parameterDeclaration();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }
      case 119:
      case 130:
      case 201:
         if (this.inputState.guessing == 0) {
            parameterDeclarationList_AST = currentAST.root;
            parameterDeclarationList_AST = this.astFactory.make((new ASTArray(2)).add(this.create(19, "PARAMETERS", (Token)first, (Token)this.LT(1))).add(parameterDeclarationList_AST));
            currentAST.root = parameterDeclarationList_AST;
            currentAST.child = parameterDeclarationList_AST != null && parameterDeclarationList_AST.getFirstChild() != null ? parameterDeclarationList_AST.getFirstChild() : parameterDeclarationList_AST;
            currentAST.advanceChildToEnd();
         }

         parameterDeclarationList_AST = currentAST.root;
         this.returnAST = parameterDeclarationList_AST;
         return;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final void throwsClause() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST throwsClause_AST = null;
      this.nls();
      AST tmp185_AST = null;
      tmp185_AST = this.astFactory.create(this.LT(1));
      this.astFactory.makeASTRoot(currentAST, tmp185_AST);
      this.match(126);
      this.nls();
      this.identifier();
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96) {
         this.match(96);
         this.nls();
         this.identifier();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      throwsClause_AST = currentAST.root;
      this.returnAST = throwsClause_AST;
   }

   public final void compoundStatement() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST compoundStatement_AST = null;
      this.openBlock();
      this.astFactory.addASTChild(currentAST, this.returnAST);
      compoundStatement_AST = currentAST.root;
      this.returnAST = compoundStatement_AST;
   }

   public final void constructorDefinition(AST mods) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST constructorDefinition_AST = null;
      Token id = null;
      AST id_AST = null;
      AST param_AST = null;
      AST tc_AST = null;
      AST cb_AST = null;
      Token first = this.cloneToken(this.LT(1));
      if (mods != null) {
         first.setLine(mods.getLine());
         first.setColumn(mods.getColumn());
      }

      id = this.LT(1);
      id_AST = this.astFactory.create(id);
      this.astFactory.addASTChild(currentAST, id_AST);
      this.match(84);
      this.match(88);
      this.parameterDeclarationList();
      param_AST = this.returnAST;
      this.match(119);
      boolean synPredMatched238 = false;
      if ((this.LA(1) == 126 || this.LA(1) == 201) && _tokenSet_28.member(this.LA(2))) {
         int _m238 = this.mark();
         synPredMatched238 = true;
         ++this.inputState.guessing;

         try {
            this.nls();
            this.match(126);
         } catch (RecognitionException var13) {
            synPredMatched238 = false;
         }

         this.rewind(_m238);
         --this.inputState.guessing;
      }

      if (synPredMatched238) {
         this.throwsClause();
         tc_AST = this.returnAST;
      } else if (this.LA(1) != 122 && this.LA(1) != 201 || !_tokenSet_64.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.nlsWarn();
      if (this.inputState.guessing == 0) {
         this.isConstructorIdent(id);
      }

      this.constructorBody();
      cb_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         constructorDefinition_AST = currentAST.root;
         constructorDefinition_AST = this.astFactory.make((new ASTArray(5)).add(this.create(45, "CTOR_IDENT", (Token)first, (Token)this.LT(1))).add(mods).add(param_AST).add(tc_AST).add(cb_AST));
         currentAST.root = constructorDefinition_AST;
         currentAST.child = constructorDefinition_AST != null && constructorDefinition_AST.getFirstChild() != null ? constructorDefinition_AST.getFirstChild() : constructorDefinition_AST;
         currentAST.advanceChildToEnd();
      }

      constructorDefinition_AST = currentAST.root;
      this.returnAST = constructorDefinition_AST;
   }

   public final void multipleAssignmentDeclarationStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST multipleAssignmentDeclarationStart_AST = null;

      while(true) {
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
            this.modifier();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.nls();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            break;
         case 92:
            this.annotation();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.nls();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            break;
         default:
            AST tmp189_AST = null;
            tmp189_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp189_AST);
            this.match(81);
            this.nls();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            AST tmp190_AST = null;
            tmp190_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp190_AST);
            this.match(88);
            multipleAssignmentDeclarationStart_AST = currentAST.root;
            this.returnAST = multipleAssignmentDeclarationStart_AST;
            return;
         }
      }
   }

   public final void multipleAssignmentDeclaration() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST multipleAssignmentDeclaration_AST = null;
      AST mods_AST = null;
      AST t_AST = null;
      Token first = this.cloneToken(this.LT(1));
      this.modifiers();
      mods_AST = this.returnAST;
      switch(this.LA(1)) {
      case 84:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.typeSpec(false);
         t_AST = this.returnAST;
         break;
      case 85:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 88:
      }

      AST tmp191_AST = null;
      tmp191_AST = this.astFactory.create(this.LT(1));
      this.astFactory.makeASTRoot(currentAST, tmp191_AST);
      this.match(88);
      this.nls();
      this.typeNamePairs(mods_AST, first);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      this.match(119);
      AST tmp193_AST = null;
      tmp193_AST = this.astFactory.create(this.LT(1));
      this.astFactory.makeASTRoot(currentAST, tmp193_AST);
      this.match(120);
      this.nls();
      this.assignmentExpression(0);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.inputState.guessing == 0) {
         multipleAssignmentDeclaration_AST = currentAST.root;
         multipleAssignmentDeclaration_AST = this.astFactory.make((new ASTArray(4)).add(this.create(9, "VARIABLE_DEF", (Token)first, (Token)this.LT(1))).add(mods_AST).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t_AST))).add(multipleAssignmentDeclaration_AST));
         currentAST.root = multipleAssignmentDeclaration_AST;
         currentAST.child = multipleAssignmentDeclaration_AST != null && multipleAssignmentDeclaration_AST.getFirstChild() != null ? multipleAssignmentDeclaration_AST.getFirstChild() : multipleAssignmentDeclaration_AST;
         currentAST.advanceChildToEnd();
      }

      multipleAssignmentDeclaration_AST = currentAST.root;
      this.returnAST = multipleAssignmentDeclaration_AST;
   }

   public final void constructorBody() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST constructorBody_AST = null;
      AST eci_AST = null;
      AST bb1_AST = null;
      AST bb2_AST = null;
      Token first = this.LT(1);
      this.match(122);
      this.nls();
      boolean synPredMatched208 = false;
      if (_tokenSet_65.member(this.LA(1)) && _tokenSet_66.member(this.LA(2))) {
         int _m208 = this.mark();
         synPredMatched208 = true;
         ++this.inputState.guessing;

         try {
            this.explicitConstructorInvocation();
         } catch (RecognitionException var10) {
            synPredMatched208 = false;
         }

         this.rewind(_m208);
         --this.inputState.guessing;
      }

      if (synPredMatched208) {
         this.explicitConstructorInvocation();
         eci_AST = this.returnAST;
         switch(this.LA(1)) {
         case 123:
            break;
         case 124:
         case 201:
            this.sep();
            this.blockBody(this.sepToken);
            bb1_AST = this.returnAST;
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      } else {
         if (!_tokenSet_30.member(this.LA(1)) || !_tokenSet_67.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.blockBody(1);
         bb2_AST = this.returnAST;
      }

      this.match(123);
      if (this.inputState.guessing == 0) {
         constructorBody_AST = currentAST.root;
         if (eci_AST != null) {
            constructorBody_AST = this.astFactory.make((new ASTArray(3)).add(this.create(7, "{", (Token)first, (Token)this.LT(1))).add(eci_AST).add(bb1_AST));
         } else {
            constructorBody_AST = this.astFactory.make((new ASTArray(2)).add(this.create(7, "{", (Token)first, (Token)this.LT(1))).add(bb2_AST));
         }

         currentAST.root = constructorBody_AST;
         currentAST.child = constructorBody_AST != null && constructorBody_AST.getFirstChild() != null ? constructorBody_AST.getFirstChild() : constructorBody_AST;
         currentAST.advanceChildToEnd();
      }

      constructorBody_AST = currentAST.root;
      this.returnAST = constructorBody_AST;
   }

   public final void explicitConstructorInvocation() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST explicitConstructorInvocation_AST = null;
      Token lp1 = null;
      AST lp1_AST = null;
      Token lp2 = null;
      AST lp2_AST = null;
      switch(this.LA(1)) {
      case 86:
         this.typeArguments();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      case 95:
      case 128:
         switch(this.LA(1)) {
         case 95:
            this.match(95);
            lp2 = this.LT(1);
            lp2_AST = this.astFactory.create(lp2);
            this.astFactory.makeASTRoot(currentAST, lp2_AST);
            this.match(88);
            this.argList();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.match(119);
            if (this.inputState.guessing == 0) {
               lp2_AST.setType(43);
            }
            break;
         case 128:
            this.match(128);
            lp1 = this.LT(1);
            lp1_AST = this.astFactory.create(lp1);
            this.astFactory.makeASTRoot(currentAST, lp1_AST);
            this.match(88);
            this.argList();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.match(119);
            if (this.inputState.guessing == 0) {
               lp1_AST.setType(44);
            }
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         explicitConstructorInvocation_AST = currentAST.root;
         this.returnAST = explicitConstructorInvocation_AST;
         return;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final void listOfVariables(AST mods, AST t, Token first) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST listOfVariables_AST = null;
      this.variableDeclarator(this.getASTFactory().dupTree(mods), this.getASTFactory().dupTree(t), first);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96) {
         this.match(96);
         this.nls();
         if (this.inputState.guessing == 0) {
            first = this.LT(1);
         }

         this.variableDeclarator(this.getASTFactory().dupTree(mods), this.getASTFactory().dupTree(t), first);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      listOfVariables_AST = currentAST.root;
      this.returnAST = listOfVariables_AST;
   }

   public final void variableDeclarator(AST mods, AST t, Token first) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST variableDeclarator_AST = null;
      AST id_AST = null;
      AST v_AST = null;
      this.variableName();
      id_AST = this.returnAST;
      switch(this.LA(1)) {
      case 1:
      case 96:
      case 119:
      case 123:
      case 124:
      case 125:
      case 133:
      case 145:
      case 201:
         break;
      case 120:
         this.varInitializer();
         v_AST = this.returnAST;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         variableDeclarator_AST = currentAST.root;
         variableDeclarator_AST = this.astFactory.make((new ASTArray(5)).add(this.create(9, "VARIABLE_DEF", (Token)first, (Token)this.LT(1))).add(mods).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t))).add(id_AST).add(v_AST));
         currentAST.root = variableDeclarator_AST;
         currentAST.child = variableDeclarator_AST != null && variableDeclarator_AST.getFirstChild() != null ? variableDeclarator_AST.getFirstChild() : variableDeclarator_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = variableDeclarator_AST;
   }

   public final void typeNamePairs(AST mods, Token first) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST typeNamePairs_AST = null;
      AST t_AST = null;
      AST tn_AST = null;
      if (_tokenSet_24.member(this.LA(1)) && _tokenSet_31.member(this.LA(2))) {
         this.typeSpec(false);
         t_AST = this.returnAST;
      } else if (this.LA(1) != 84 || this.LA(2) != 96 && this.LA(2) != 119) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.singleVariable(this.getASTFactory().dupTree(mods), t_AST);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96) {
         this.match(96);
         this.nls();
         if (this.inputState.guessing == 0) {
            first = this.LT(1);
         }

         if (_tokenSet_24.member(this.LA(1)) && _tokenSet_31.member(this.LA(2))) {
            this.typeSpec(false);
            tn_AST = this.returnAST;
         } else if (this.LA(1) != 84 || this.LA(2) != 96 && this.LA(2) != 119) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.singleVariable(this.getASTFactory().dupTree(mods), tn_AST);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      typeNamePairs_AST = currentAST.root;
      this.returnAST = typeNamePairs_AST;
   }

   public final void assignmentExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST assignmentExpression_AST = null;
      this.conditionalExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      switch(this.LA(1)) {
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 86:
      case 87:
      case 92:
      case 93:
      case 97:
      case 98:
      case 99:
      case 109:
      case 121:
      case 129:
      case 149:
      case 150:
      case 151:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 120:
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
         AST tmp214_AST;
         switch(this.LA(1)) {
         case 120:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(120);
            break;
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
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
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 153:
         case 154:
         case 155:
         case 156:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 157:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(157);
            break;
         case 158:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(158);
            break;
         case 159:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(159);
            break;
         case 160:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(160);
            break;
         case 161:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(161);
            break;
         case 162:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(162);
            break;
         case 163:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(163);
            break;
         case 164:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(164);
            break;
         case 165:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(165);
            break;
         case 166:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(166);
            break;
         case 167:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(167);
            break;
         case 168:
            tmp214_AST = null;
            tmp214_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp214_AST);
            this.match(168);
         }

         this.nls();
         this.assignmentExpression(lc_stmt == 1 ? 2 : 0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      case 1:
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 94:
      case 95:
      case 96:
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
      case 122:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 128:
      case 130:
      case 131:
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
      case 143:
      case 144:
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
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
      case 201:
         assignmentExpression_AST = currentAST.root;
         this.returnAST = assignmentExpression_AST;
      }
   }

   public final void nlsWarn() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST nlsWarn_AST = null;
      boolean synPredMatched552 = false;
      if (_tokenSet_68.member(this.LA(1)) && _tokenSet_1.member(this.LA(2))) {
         int _m552 = this.mark();
         synPredMatched552 = true;
         ++this.inputState.guessing;

         try {
            this.match(201);
         } catch (RecognitionException var6) {
            synPredMatched552 = false;
         }

         this.rewind(_m552);
         --this.inputState.guessing;
      }

      if (synPredMatched552) {
         if (this.inputState.guessing == 0) {
            this.addWarning("A newline at this point does not follow the Groovy Coding Conventions.", "Keep this statement on one line, or use curly braces to break across multiple lines.");
         }
      } else if (!_tokenSet_68.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.nls();
      this.returnAST = (AST)nlsWarn_AST;
   }

   public final void openBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST openBlock_AST = null;
      AST bb_AST = null;
      Token first = this.LT(1);
      this.match(122);
      this.nls();
      this.blockBody(1);
      bb_AST = this.returnAST;
      this.match(123);
      if (this.inputState.guessing == 0) {
         openBlock_AST = currentAST.root;
         openBlock_AST = this.astFactory.make((new ASTArray(2)).add(this.create(7, "{", (Token)first, (Token)this.LT(1))).add(bb_AST));
         currentAST.root = openBlock_AST;
         currentAST.child = openBlock_AST != null && openBlock_AST.getFirstChild() != null ? openBlock_AST.getFirstChild() : openBlock_AST;
         currentAST.advanceChildToEnd();
      }

      openBlock_AST = currentAST.root;
      this.returnAST = openBlock_AST;
   }

   public final void variableName() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST variableName_AST = null;
      AST tmp217_AST = null;
      tmp217_AST = this.astFactory.create(this.LT(1));
      this.astFactory.addASTChild(currentAST, tmp217_AST);
      this.match(84);
      variableName_AST = currentAST.root;
      this.returnAST = variableName_AST;
   }

   public final void expression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST expression_AST = null;
      Token lp = null;
      AST lp_AST = null;
      AST m_AST = null;
      boolean synPredMatched368 = false;
      if (this.LA(1) == 88 && _tokenSet_24.member(this.LA(2))) {
         int _m368 = this.mark();
         synPredMatched368 = true;
         ++this.inputState.guessing;

         try {
            this.match(88);
            this.typeSpec(true);
            this.match(119);
            this.expression(lc_stmt);
         } catch (RecognitionException var11) {
            synPredMatched368 = false;
         }

         this.rewind(_m368);
         --this.inputState.guessing;
      }

      if (synPredMatched368) {
         lp = this.LT(1);
         lp_AST = this.astFactory.create(lp);
         this.astFactory.makeASTRoot(currentAST, lp_AST);
         this.match(88);
         if (this.inputState.guessing == 0) {
            lp_AST.setType(22);
         }

         this.typeSpec(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         this.match(119);
         this.expression(lc_stmt);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         expression_AST = currentAST.root;
      } else {
         boolean synPredMatched372 = false;
         if (this.LA(1) == 88 && (this.LA(2) == 84 || this.LA(2) == 201)) {
            int _m372 = this.mark();
            synPredMatched372 = true;
            ++this.inputState.guessing;

            try {
               this.match(88);
               this.nls();
               this.match(84);

               while(this.LA(1) == 96) {
                  this.match(96);
                  this.nls();
                  this.match(84);
               }

               this.match(119);
               this.match(120);
            } catch (RecognitionException var12) {
               synPredMatched372 = false;
            }

            this.rewind(_m372);
            --this.inputState.guessing;
         }

         if (synPredMatched372) {
            this.multipleAssignment(lc_stmt);
            m_AST = this.returnAST;
            this.astFactory.addASTChild(currentAST, this.returnAST);
            if (this.inputState.guessing == 0) {
               expression_AST = currentAST.root;
               currentAST.root = m_AST;
               currentAST.child = m_AST != null && m_AST.getFirstChild() != null ? m_AST.getFirstChild() : m_AST;
               currentAST.advanceChildToEnd();
            }

            expression_AST = currentAST.root;
         } else {
            if (!_tokenSet_19.member(this.LA(1)) || !_tokenSet_37.member(this.LA(2))) {
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.assignmentExpression(lc_stmt);
            this.astFactory.addASTChild(currentAST, this.returnAST);
            expression_AST = currentAST.root;
         }
      }

      this.returnAST = expression_AST;
   }

   public final void parameterDeclaration() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST parameterDeclaration_AST = null;
      AST pm_AST = null;
      AST t_AST = null;
      Token id = null;
      AST id_AST = null;
      AST exp_AST = null;
      Token first = this.LT(1);
      boolean spreadParam = false;
      this.parameterModifiersOpt();
      pm_AST = this.returnAST;
      if (_tokenSet_24.member(this.LA(1)) && _tokenSet_69.member(this.LA(2))) {
         this.typeSpec(false);
         t_AST = this.returnAST;
      } else if (this.LA(1) != 84 && this.LA(1) != 129 || !_tokenSet_70.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      switch(this.LA(1)) {
      case 129:
         this.match(129);
         if (this.inputState.guessing == 0) {
            spreadParam = true;
         }
      case 84:
         id = this.LT(1);
         id_AST = this.astFactory.create(id);
         this.match(84);
         switch(this.LA(1)) {
         case 96:
         case 119:
         case 130:
         case 201:
            break;
         case 120:
            this.varInitializer();
            exp_AST = this.returnAST;
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            parameterDeclaration_AST = currentAST.root;
            if (spreadParam) {
               parameterDeclaration_AST = this.astFactory.make((new ASTArray(5)).add(this.create(46, "VARIABLE_PARAMETER_DEF", (Token)first, (Token)this.LT(1))).add(pm_AST).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t_AST))).add(id_AST).add(exp_AST));
            } else {
               parameterDeclaration_AST = this.astFactory.make((new ASTArray(5)).add(this.create(20, "PARAMETER_DEF", (Token)first, (Token)this.LT(1))).add(pm_AST).add(this.astFactory.make((new ASTArray(2)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))).add(t_AST))).add(id_AST).add(exp_AST));
            }

            currentAST.root = parameterDeclaration_AST;
            currentAST.child = parameterDeclaration_AST != null && parameterDeclaration_AST.getFirstChild() != null ? parameterDeclaration_AST.getFirstChild() : parameterDeclaration_AST;
            currentAST.advanceChildToEnd();
         }

         this.returnAST = parameterDeclaration_AST;
         return;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final void parameterModifiersOpt() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST parameterModifiersOpt_AST = null;
      Token first = this.LT(1);
      int var4 = 0;

      while(true) {
         while(true) {
            switch(this.LA(1)) {
            case 37:
               AST tmp220_AST = null;
               tmp220_AST = this.astFactory.create(this.LT(1));
               this.astFactory.addASTChild(currentAST, tmp220_AST);
               this.match(37);
               this.nls();
               break;
            case 92:
               this.annotation();
               this.astFactory.addASTChild(currentAST, this.returnAST);
               this.nls();
               break;
            default:
               if (this.LA(1) != 81 || var4++ != 0) {
                  if (this.inputState.guessing == 0) {
                     parameterModifiersOpt_AST = currentAST.root;
                     parameterModifiersOpt_AST = this.astFactory.make((new ASTArray(2)).add(this.create(5, "MODIFIERS", (Token)first, (Token)this.LT(1))).add(parameterModifiersOpt_AST));
                     currentAST.root = parameterModifiersOpt_AST;
                     currentAST.child = parameterModifiersOpt_AST != null && parameterModifiersOpt_AST.getFirstChild() != null ? parameterModifiersOpt_AST.getFirstChild() : parameterModifiersOpt_AST;
                     currentAST.advanceChildToEnd();
                  }

                  parameterModifiersOpt_AST = currentAST.root;
                  this.returnAST = parameterModifiersOpt_AST;
                  return;
               }

               this.match(81);
               this.nls();
            }
         }
      }
   }

   public final void closableBlockParamsOpt(boolean addImplicit) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST closableBlockParamsOpt_AST = null;
      boolean synPredMatched263 = false;
      if (_tokenSet_71.member(this.LA(1)) && _tokenSet_72.member(this.LA(2))) {
         int _m263 = this.mark();
         synPredMatched263 = true;
         ++this.inputState.guessing;

         try {
            this.parameterDeclarationList();
            this.nls();
            this.match(130);
         } catch (RecognitionException var7) {
            synPredMatched263 = false;
         }

         this.rewind(_m263);
         --this.inputState.guessing;
      }

      if (synPredMatched263) {
         this.parameterDeclarationList();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         this.nls();
         this.match(130);
         this.nls();
         closableBlockParamsOpt_AST = currentAST.root;
      } else if (_tokenSet_30.member(this.LA(1)) && _tokenSet_73.member(this.LA(2)) && addImplicit) {
         this.implicitParameters();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         closableBlockParamsOpt_AST = currentAST.root;
      } else {
         if (!_tokenSet_30.member(this.LA(1)) || !_tokenSet_73.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         closableBlockParamsOpt_AST = currentAST.root;
      }

      this.returnAST = closableBlockParamsOpt_AST;
   }

   public final void implicitParameters() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST implicitParameters_AST = null;
      Token first = this.LT(1);
      if (this.inputState.guessing == 0) {
         implicitParameters_AST = currentAST.root;
         implicitParameters_AST = this.astFactory.make((new ASTArray(1)).add(this.create(50, "IMPLICIT_PARAMETERS", (Token)first, (Token)this.LT(1))));
         currentAST.root = implicitParameters_AST;
         currentAST.child = implicitParameters_AST != null && implicitParameters_AST.getFirstChild() != null ? implicitParameters_AST.getFirstChild() : implicitParameters_AST;
         currentAST.advanceChildToEnd();
      }

      implicitParameters_AST = currentAST.root;
      this.returnAST = implicitParameters_AST;
   }

   public final void closableBlockParamsStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST closableBlockParamsStart_AST = null;
      this.parameterDeclarationList();
      this.nls();
      AST tmp223_AST = null;
      tmp223_AST = this.astFactory.create(this.LT(1));
      this.match(130);
      this.returnAST = (AST)closableBlockParamsStart_AST;
   }

   public final void closableBlockParam() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST closableBlockParam_AST = null;
      Token id = null;
      AST id_AST = null;
      Token first = this.LT(1);
      id = this.LT(1);
      id_AST = this.astFactory.create(id);
      this.match(84);
      if (this.inputState.guessing == 0) {
         closableBlockParam_AST = currentAST.root;
         closableBlockParam_AST = this.astFactory.make((new ASTArray(4)).add(this.create(20, "PARAMETER_DEF", (Token)first, (Token)this.LT(1))).add(this.astFactory.make((new ASTArray(1)).add(this.create(5, "MODIFIERS", (Token)first, (Token)this.LT(1))))).add(this.astFactory.make((new ASTArray(1)).add(this.create(12, "TYPE", (Token)first, (Token)this.LT(1))))).add(id_AST));
         currentAST.root = closableBlockParam_AST;
         currentAST.child = closableBlockParam_AST != null && closableBlockParam_AST.getFirstChild() != null ? closableBlockParam_AST.getFirstChild() : closableBlockParam_AST;
         currentAST.advanceChildToEnd();
      }

      this.returnAST = closableBlockParam_AST;
   }

   public final void closableBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST closableBlock_AST = null;
      AST cbp_AST = null;
      AST bb_AST = null;
      Token first = this.LT(1);
      this.match(122);
      this.nls();
      this.closableBlockParamsOpt(true);
      cbp_AST = this.returnAST;
      this.blockBody(1);
      bb_AST = this.returnAST;
      this.match(123);
      if (this.inputState.guessing == 0) {
         closableBlock_AST = currentAST.root;
         closableBlock_AST = this.astFactory.make((new ASTArray(3)).add(this.create(49, "{", (Token)first, (Token)this.LT(1))).add(cbp_AST).add(bb_AST));
         currentAST.root = closableBlock_AST;
         currentAST.child = closableBlock_AST != null && closableBlock_AST.getFirstChild() != null ? closableBlock_AST.getFirstChild() : closableBlock_AST;
         currentAST.advanceChildToEnd();
      }

      closableBlock_AST = currentAST.root;
      this.returnAST = closableBlock_AST;
   }

   public final void openOrClosableBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST openOrClosableBlock_AST = null;
      AST cp_AST = null;
      AST bb_AST = null;
      Token first = this.LT(1);
      this.match(122);
      this.nls();
      this.closableBlockParamsOpt(false);
      cp_AST = this.returnAST;
      this.blockBody(1);
      bb_AST = this.returnAST;
      this.match(123);
      if (this.inputState.guessing == 0) {
         openOrClosableBlock_AST = currentAST.root;
         if (cp_AST == null) {
            openOrClosableBlock_AST = this.astFactory.make((new ASTArray(2)).add(this.create(7, "{", (Token)first, (Token)this.LT(1))).add(bb_AST));
         } else {
            openOrClosableBlock_AST = this.astFactory.make((new ASTArray(3)).add(this.create(49, "{", (Token)first, (Token)this.LT(1))).add(cp_AST).add(bb_AST));
         }

         currentAST.root = openOrClosableBlock_AST;
         currentAST.child = openOrClosableBlock_AST != null && openOrClosableBlock_AST.getFirstChild() != null ? openOrClosableBlock_AST.getFirstChild() : openOrClosableBlock_AST;
         currentAST.advanceChildToEnd();
      }

      openOrClosableBlock_AST = currentAST.root;
      this.returnAST = openOrClosableBlock_AST;
   }

   public final void statementLabelPrefix() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST statementLabelPrefix_AST = null;
      Token c = null;
      AST c_AST = null;
      AST tmp228_AST = null;
      tmp228_AST = this.astFactory.create(this.LT(1));
      this.astFactory.addASTChild(currentAST, tmp228_AST);
      this.match(84);
      c = this.LT(1);
      c_AST = this.astFactory.create(c);
      this.astFactory.makeASTRoot(currentAST, c_AST);
      this.match(131);
      if (this.inputState.guessing == 0) {
         c_AST.setType(21);
      }

      this.nls();
      statementLabelPrefix_AST = currentAST.root;
      this.returnAST = statementLabelPrefix_AST;
   }

   public final void expressionStatement(int prevToken) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST expressionStatement_AST = null;
      AST head_AST = null;
      AST cmd_AST = null;
      Token first = this.LT(1);
      boolean isPathExpr = false;
      boolean synPredMatched326 = false;
      if (_tokenSet_19.member(this.LA(1)) && _tokenSet_1.member(this.LA(2))) {
         int _m326 = this.mark();
         synPredMatched326 = true;
         ++this.inputState.guessing;

         try {
            this.suspiciousExpressionStatementStart();
         } catch (RecognitionException var11) {
            synPredMatched326 = false;
         }

         this.rewind(_m326);
         --this.inputState.guessing;
      }

      if (synPredMatched326) {
         this.checkSuspiciousExpressionStatement(prevToken);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (!_tokenSet_19.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.expression(1);
      head_AST = this.returnAST;
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.inputState.guessing == 0) {
         isPathExpr = head_AST == this.lastPathExpression;
      }

      if (_tokenSet_74.member(this.LA(1)) && _tokenSet_75.member(this.LA(2)) && isPathExpr) {
         this.commandArguments(head_AST);
         cmd_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            expressionStatement_AST = currentAST.root;
            currentAST.root = cmd_AST;
            currentAST.child = cmd_AST != null && cmd_AST.getFirstChild() != null ? cmd_AST.getFirstChild() : cmd_AST;
            currentAST.advanceChildToEnd();
         }
      } else if (!_tokenSet_10.member(this.LA(1)) || !_tokenSet_11.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         expressionStatement_AST = currentAST.root;
         expressionStatement_AST = this.astFactory.make((new ASTArray(2)).add(this.create(27, "EXPR", (Token)first, (Token)this.LT(1))).add(expressionStatement_AST));
         currentAST.root = expressionStatement_AST;
         currentAST.child = expressionStatement_AST != null && expressionStatement_AST.getFirstChild() != null ? expressionStatement_AST.getFirstChild() : expressionStatement_AST;
         currentAST.advanceChildToEnd();
      }

      expressionStatement_AST = currentAST.root;
      this.returnAST = expressionStatement_AST;
   }

   public final void assignmentLessExpression() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST assignmentLessExpression_AST = null;
      Token first = this.LT(1);
      this.conditionalExpression(0);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.inputState.guessing == 0) {
         assignmentLessExpression_AST = currentAST.root;
         assignmentLessExpression_AST = this.astFactory.make((new ASTArray(2)).add(this.create(27, "EXPR", (Token)first, (Token)this.LT(1))).add(assignmentLessExpression_AST));
         currentAST.root = assignmentLessExpression_AST;
         currentAST.child = assignmentLessExpression_AST != null && assignmentLessExpression_AST.getFirstChild() != null ? assignmentLessExpression_AST.getFirstChild() : assignmentLessExpression_AST;
         currentAST.advanceChildToEnd();
      }

      assignmentLessExpression_AST = currentAST.root;
      this.returnAST = assignmentLessExpression_AST;
   }

   public final void compatibleBodyStatement() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST compatibleBodyStatement_AST = null;
      boolean synPredMatched315 = false;
      if (this.LA(1) == 122 && _tokenSet_30.member(this.LA(2))) {
         int _m315 = this.mark();
         synPredMatched315 = true;
         ++this.inputState.guessing;

         try {
            this.match(122);
         } catch (RecognitionException var6) {
            synPredMatched315 = false;
         }

         this.rewind(_m315);
         --this.inputState.guessing;
      }

      if (synPredMatched315) {
         this.compoundStatement();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         compatibleBodyStatement_AST = currentAST.root;
      } else {
         if (!_tokenSet_18.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.statement(1);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         compatibleBodyStatement_AST = currentAST.root;
      }

      this.returnAST = compatibleBodyStatement_AST;
   }

   public final void forStatement() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST forStatement_AST = null;
      AST cl_AST = null;
      AST fic_AST = null;
      Token s = null;
      AST s_AST = null;
      AST forCbs_AST = null;
      Token first = this.LT(1);
      this.match(136);
      this.match(88);
      boolean synPredMatched302 = false;
      if (_tokenSet_76.member(this.LA(1)) && _tokenSet_77.member(this.LA(2))) {
         int _m302 = this.mark();
         synPredMatched302 = true;
         ++this.inputState.guessing;

         try {
            switch(this.LA(1)) {
            case 37:
            case 38:
            case 42:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 88:
            case 92:
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
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 122:
            case 128:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 152:
            case 154:
            case 155:
            case 156:
            case 186:
            case 189:
            case 191:
            case 192:
            case 193:
            case 195:
            case 196:
            case 197:
            case 198:
            case 199:
            case 200:
               this.strictContextExpression(true);
               this.match(124);
               break;
            case 39:
            case 40:
            case 41:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 83:
            case 86:
            case 87:
            case 89:
            case 90:
            case 91:
            case 93:
            case 94:
            case 96:
            case 97:
            case 98:
            case 99:
            case 109:
            case 110:
            case 119:
            case 120:
            case 121:
            case 123:
            case 125:
            case 126:
            case 127:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 153:
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
            case 194:
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            case 124:
               this.match(124);
            }
         } catch (RecognitionException var12) {
            synPredMatched302 = false;
         }

         this.rewind(_m302);
         --this.inputState.guessing;
      }

      if (synPredMatched302) {
         this.closureList();
         cl_AST = this.returnAST;
      } else {
         if (!_tokenSet_15.member(this.LA(1)) || !_tokenSet_78.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.forInClause();
         fic_AST = this.returnAST;
      }

      this.match(119);
      this.nls();
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 79:
      case 80:
      case 81:
      case 82:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
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
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
      case 128:
      case 132:
      case 134:
      case 135:
      case 136:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 146:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.compatibleBodyStatement();
         forCbs_AST = this.returnAST;
         break;
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 83:
      case 86:
      case 87:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      case 119:
      case 120:
      case 121:
      case 123:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
      case 133:
      case 137:
      case 145:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 124:
         s = this.LT(1);
         s_AST = this.astFactory.create(s);
         this.match(124);
      }

      if (this.inputState.guessing == 0) {
         forStatement_AST = currentAST.root;
         if (cl_AST != null) {
            if (s_AST != null) {
               forStatement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(136, "for", (Token)first, (Token)this.LT(1))).add(cl_AST).add(s_AST));
            } else {
               forStatement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(136, "for", (Token)first, (Token)this.LT(1))).add(cl_AST).add(forCbs_AST));
            }
         } else if (s_AST != null) {
            forStatement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(136, "for", (Token)first, (Token)this.LT(1))).add(fic_AST).add(s_AST));
         } else {
            forStatement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(136, "for", (Token)first, (Token)this.LT(1))).add(fic_AST).add(forCbs_AST));
         }

         currentAST.root = forStatement_AST;
         currentAST.child = forStatement_AST != null && forStatement_AST.getFirstChild() != null ? forStatement_AST.getFirstChild() : forStatement_AST;
         currentAST.advanceChildToEnd();
      }

      forStatement_AST = currentAST.root;
      this.returnAST = forStatement_AST;
   }

   public final boolean strictContextExpression(boolean allowDeclaration) throws RecognitionException, TokenStreamException {
      boolean hasDeclaration = false;
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST strictContextExpression_AST = null;
      Token first = this.LT(1);
      boolean synPredMatched487 = false;
      if (_tokenSet_15.member(this.LA(1)) && _tokenSet_79.member(this.LA(2))) {
         int _m487 = this.mark();
         synPredMatched487 = true;
         ++this.inputState.guessing;

         try {
            if (!allowDeclaration) {
               throw new SemanticException("allowDeclaration");
            }

            this.declarationStart();
         } catch (RecognitionException var9) {
            synPredMatched487 = false;
         }

         this.rewind(_m487);
         --this.inputState.guessing;
      }

      if (synPredMatched487) {
         if (this.inputState.guessing == 0) {
            hasDeclaration = true;
         }

         this.singleDeclaration();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (_tokenSet_19.member(this.LA(1)) && _tokenSet_37.member(this.LA(2))) {
         this.expression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (this.LA(1) >= 138 && this.LA(1) <= 142) {
         this.branchStatement();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else {
         if (this.LA(1) != 92 || this.LA(2) != 84) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.annotation();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         strictContextExpression_AST = currentAST.root;
         strictContextExpression_AST = this.astFactory.make((new ASTArray(2)).add(this.create(27, "EXPR", (Token)first, (Token)this.LT(1))).add(strictContextExpression_AST));
         currentAST.root = strictContextExpression_AST;
         currentAST.child = strictContextExpression_AST != null && strictContextExpression_AST.getFirstChild() != null ? strictContextExpression_AST.getFirstChild() : strictContextExpression_AST;
         currentAST.advanceChildToEnd();
      }

      strictContextExpression_AST = currentAST.root;
      this.returnAST = strictContextExpression_AST;
      return hasDeclaration;
   }

   public final void casesGroup() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST casesGroup_AST = null;
      Token first = this.LT(1);

      int _cnt338;
      for(_cnt338 = 0; this.LA(1) == 125 || this.LA(1) == 145; ++_cnt338) {
         this.aCase();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (_cnt338 < 1) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      } else {
         this.caseSList();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            casesGroup_AST = currentAST.root;
            casesGroup_AST = this.astFactory.make((new ASTArray(2)).add(this.create(31, "CASE_GROUP", (Token)first, (Token)this.LT(1))).add(casesGroup_AST));
            currentAST.root = casesGroup_AST;
            currentAST.child = casesGroup_AST != null && casesGroup_AST.getFirstChild() != null ? casesGroup_AST.getFirstChild() : casesGroup_AST;
            currentAST.advanceChildToEnd();
         }

         casesGroup_AST = currentAST.root;
         this.returnAST = casesGroup_AST;
      }
   }

   public final void tryBlock() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST tryBlock_AST = null;
      AST tryCs_AST = null;
      AST h_AST = null;
      AST fc_AST = null;
      Token first = this.LT(1);
      new ArrayList();
      AST newHandler_AST = null;
      this.match(146);
      this.nlsWarn();
      this.compoundStatement();
      tryCs_AST = this.returnAST;

      while((this.LA(1) == 148 || this.LA(1) == 201) && (this.LA(2) == 88 || this.LA(2) == 148) && (this.LA(1) != 201 || this.LA(2) != 88)) {
         this.nls();
         this.handler();
         h_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            newHandler_AST = this.astFactory.make((new ASTArray(3)).add((AST)null).add(newHandler_AST).add(h_AST));
         }
      }

      if ((this.LA(1) == 147 || this.LA(1) == 201) && _tokenSet_80.member(this.LA(2))) {
         this.nls();
         this.finallyClause();
         fc_AST = this.returnAST;
      } else if (!_tokenSet_10.member(this.LA(1)) || !_tokenSet_11.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         tryBlock_AST = currentAST.root;
         tryBlock_AST = this.astFactory.make((new ASTArray(4)).add(this.create(146, "try", (Token)first, (Token)this.LT(1))).add(tryCs_AST).add(newHandler_AST).add(fc_AST));
         currentAST.root = tryBlock_AST;
         currentAST.child = tryBlock_AST != null && tryBlock_AST.getFirstChild() != null ? tryBlock_AST.getFirstChild() : tryBlock_AST;
         currentAST.advanceChildToEnd();
      }

      tryBlock_AST = currentAST.root;
      this.returnAST = tryBlock_AST;
   }

   public final void branchStatement() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST branchStatement_AST = null;
      AST returnE_AST = null;
      Token breakI = null;
      AST breakI_AST = null;
      Token contI = null;
      AST contI_AST = null;
      AST throwE_AST = null;
      AST assertAle_AST = null;
      AST assertE_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 138:
         this.match(138);
         switch(this.LA(1)) {
         case 1:
         case 83:
         case 96:
         case 119:
         case 123:
         case 124:
         case 125:
         case 133:
         case 145:
         case 201:
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 86:
         case 87:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 120:
         case 121:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 132:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 82:
         case 84:
         case 85:
         case 88:
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
         case 122:
         case 128:
         case 143:
         case 144:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.expression(0);
            returnE_AST = this.returnAST;
         }

         if (this.inputState.guessing == 0) {
            branchStatement_AST = currentAST.root;
            branchStatement_AST = this.astFactory.make((new ASTArray(2)).add(this.create(138, "return", (Token)first, (Token)this.LT(1))).add(returnE_AST));
            currentAST.root = branchStatement_AST;
            currentAST.child = branchStatement_AST != null && branchStatement_AST.getFirstChild() != null ? branchStatement_AST.getFirstChild() : branchStatement_AST;
            currentAST.advanceChildToEnd();
         }

         branchStatement_AST = currentAST.root;
         break;
      case 139:
         this.match(139);
         switch(this.LA(1)) {
         case 1:
         case 83:
         case 96:
         case 119:
         case 123:
         case 124:
         case 125:
         case 133:
         case 145:
         case 201:
            break;
         case 84:
            breakI = this.LT(1);
            breakI_AST = this.astFactory.create(breakI);
            this.match(84);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            branchStatement_AST = currentAST.root;
            branchStatement_AST = this.astFactory.make((new ASTArray(2)).add(this.create(139, "break", (Token)first, (Token)this.LT(1))).add(breakI_AST));
            currentAST.root = branchStatement_AST;
            currentAST.child = branchStatement_AST != null && branchStatement_AST.getFirstChild() != null ? branchStatement_AST.getFirstChild() : branchStatement_AST;
            currentAST.advanceChildToEnd();
         }

         branchStatement_AST = currentAST.root;
         break;
      case 140:
         this.match(140);
         switch(this.LA(1)) {
         case 1:
         case 83:
         case 96:
         case 119:
         case 123:
         case 124:
         case 125:
         case 133:
         case 145:
         case 201:
            break;
         case 84:
            contI = this.LT(1);
            contI_AST = this.astFactory.create(contI);
            this.match(84);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            branchStatement_AST = currentAST.root;
            branchStatement_AST = this.astFactory.make((new ASTArray(2)).add(this.create(140, "continue", (Token)first, (Token)this.LT(1))).add(contI_AST));
            currentAST.root = branchStatement_AST;
            currentAST.child = branchStatement_AST != null && branchStatement_AST.getFirstChild() != null ? branchStatement_AST.getFirstChild() : branchStatement_AST;
            currentAST.advanceChildToEnd();
         }

         branchStatement_AST = currentAST.root;
         break;
      case 141:
         this.match(141);
         this.expression(0);
         throwE_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            branchStatement_AST = currentAST.root;
            branchStatement_AST = this.astFactory.make((new ASTArray(2)).add(this.create(141, "throw", (Token)first, (Token)this.LT(1))).add(throwE_AST));
            currentAST.root = branchStatement_AST;
            currentAST.child = branchStatement_AST != null && branchStatement_AST.getFirstChild() != null ? branchStatement_AST.getFirstChild() : branchStatement_AST;
            currentAST.advanceChildToEnd();
         }

         branchStatement_AST = currentAST.root;
         break;
      case 142:
         this.match(142);
         this.assignmentLessExpression();
         assertAle_AST = this.returnAST;
         if ((this.LA(1) == 96 || this.LA(1) == 131) && _tokenSet_81.member(this.LA(2))) {
            switch(this.LA(1)) {
            case 96:
               this.match(96);
               this.nls();
               break;
            case 131:
               this.match(131);
               this.nls();
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.expression(0);
            assertE_AST = this.returnAST;
         } else if (!_tokenSet_82.member(this.LA(1)) || !_tokenSet_11.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            branchStatement_AST = currentAST.root;
            branchStatement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(142, "assert", (Token)first, (Token)this.LT(1))).add(assertAle_AST).add(assertE_AST));
            currentAST.root = branchStatement_AST;
            currentAST.child = branchStatement_AST != null && branchStatement_AST.getFirstChild() != null ? branchStatement_AST.getFirstChild() : branchStatement_AST;
            currentAST.advanceChildToEnd();
         }

         branchStatement_AST = currentAST.root;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = branchStatement_AST;
   }

   public final void closureList() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST closureList_AST = null;
      Token first = this.LT(1);
      boolean sce = false;
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 82:
      case 84:
      case 85:
      case 88:
      case 92:
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
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
      case 128:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         sce = this.strictContextExpression(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      case 119:
      case 120:
      case 121:
      case 123:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 137:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 124:
         if (this.inputState.guessing == 0) {
            this.astFactory.addASTChild(currentAST, this.astFactory.create(36, "EMPTY_STAT"));
         }
      }

      int _cnt307 = 0;

      while(true) {
         if (this.LA(1) == 124 && _tokenSet_83.member(this.LA(2))) {
            this.match(124);
            sce = this.strictContextExpression(true);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         } else {
            if (this.LA(1) != 124 || this.LA(2) != 119 && this.LA(2) != 124) {
               if (_cnt307 < 1) {
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               } else {
                  if (this.inputState.guessing == 0) {
                     closureList_AST = currentAST.root;
                     closureList_AST = this.astFactory.make((new ASTArray(2)).add(this.create(76, "CLOSURE_LIST", (Token)first, (Token)this.LT(1))).add(closureList_AST));
                     currentAST.root = closureList_AST;
                     currentAST.child = closureList_AST != null && closureList_AST.getFirstChild() != null ? closureList_AST.getFirstChild() : closureList_AST;
                     currentAST.advanceChildToEnd();
                  }

                  closureList_AST = currentAST.root;
                  this.returnAST = closureList_AST;
                  return;
               }
            }

            this.match(124);
            if (this.inputState.guessing == 0) {
               this.astFactory.addASTChild(currentAST, this.astFactory.create(36, "EMPTY_STAT"));
            }
         }

         ++_cnt307;
      }
   }

   public final void forInClause() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST forInClause_AST = null;
      AST decl_AST = null;
      Token i = null;
      AST i_AST = null;
      Token c = null;
      AST c_AST = null;
      boolean synPredMatched311 = false;
      if (_tokenSet_15.member(this.LA(1)) && _tokenSet_79.member(this.LA(2))) {
         int _m311 = this.mark();
         synPredMatched311 = true;
         ++this.inputState.guessing;

         try {
            this.declarationStart();
         } catch (RecognitionException var11) {
            synPredMatched311 = false;
         }

         this.rewind(_m311);
         --this.inputState.guessing;
      }

      if (synPredMatched311) {
         this.singleDeclarationNoInit();
         decl_AST = this.returnAST;
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else {
         if (this.LA(1) != 84 || this.LA(2) != 131 && this.LA(2) != 137) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         AST tmp242_AST = null;
         tmp242_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp242_AST);
         this.match(84);
      }

      switch(this.LA(1)) {
      case 131:
         if (this.inputState.guessing == 0) {
            this.addWarning("A colon at this point is legal Java but not recommended in Groovy.", "Use the 'in' keyword.");
            this.require(decl_AST != null, "Java-style for-each statement requires a type declaration.", "Use the 'in' keyword, as for (x in y) {...}");
         }

         c = this.LT(1);
         c_AST = this.astFactory.create(c);
         this.astFactory.makeASTRoot(currentAST, c_AST);
         this.match(131);
         if (this.inputState.guessing == 0) {
            c_AST.setType(58);
         }

         this.expression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      case 137:
         i = this.LT(1);
         i_AST = this.astFactory.create(i);
         this.astFactory.makeASTRoot(currentAST, i_AST);
         this.match(137);
         if (this.inputState.guessing == 0) {
            i_AST.setType(58);
         }

         this.shiftExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      forInClause_AST = currentAST.root;
      this.returnAST = forInClause_AST;
   }

   public final void shiftExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST shiftExpression_AST = null;
      this.additiveExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(_tokenSet_84.member(this.LA(1))) {
         AST tmp243_AST;
         label19:
         switch(this.LA(1)) {
         case 98:
         case 99:
         case 183:
            switch(this.LA(1)) {
            case 98:
               tmp243_AST = null;
               tmp243_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp243_AST);
               this.match(98);
               break label19;
            case 99:
               tmp243_AST = null;
               tmp243_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp243_AST);
               this.match(99);
               break label19;
            case 183:
               tmp243_AST = null;
               tmp243_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp243_AST);
               this.match(183);
               break label19;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }
         case 184:
            tmp243_AST = null;
            tmp243_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp243_AST);
            this.match(184);
            break;
         case 185:
            tmp243_AST = null;
            tmp243_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp243_AST);
            this.match(185);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.nls();
         this.additiveExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      shiftExpression_AST = currentAST.root;
      this.returnAST = shiftExpression_AST;
   }

   public final void suspiciousExpressionStatementStart() throws RecognitionException, TokenStreamException {
      ASTPair currentAST;
      AST suspiciousExpressionStatementStart_AST;
      this.returnAST = null;
      currentAST = new ASTPair();
      suspiciousExpressionStatementStart_AST = null;
      AST tmp249_AST;
      label18:
      switch(this.LA(1)) {
      case 82:
      case 88:
      case 122:
         switch(this.LA(1)) {
         case 82:
            tmp249_AST = null;
            tmp249_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp249_AST);
            this.match(82);
            break label18;
         case 88:
            tmp249_AST = null;
            tmp249_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp249_AST);
            this.match(88);
            break label18;
         case 122:
            tmp249_AST = null;
            tmp249_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp249_AST);
            this.match(122);
            break label18;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      case 143:
      case 144:
         switch(this.LA(1)) {
         case 143:
            tmp249_AST = null;
            tmp249_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp249_AST);
            this.match(143);
            break label18;
         case 144:
            tmp249_AST = null;
            tmp249_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp249_AST);
            this.match(144);
            break label18;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      suspiciousExpressionStatementStart_AST = currentAST.root;
      this.returnAST = suspiciousExpressionStatementStart_AST;
   }

   public final void checkSuspiciousExpressionStatement(int prevToken) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST checkSuspiciousExpressionStatement_AST = null;
      boolean synPredMatched330 = false;
      if (_tokenSet_19.member(this.LA(1)) && _tokenSet_1.member(this.LA(2))) {
         int _m330 = this.mark();
         synPredMatched330 = true;
         ++this.inputState.guessing;

         try {
            if (_tokenSet_85.member(this.LA(1))) {
               this.matchNot(122);
            } else {
               if (this.LA(1) != 122) {
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               }

               this.match(122);
               this.closableBlockParamsStart();
            }
         } catch (RecognitionException var7) {
            synPredMatched330 = false;
         }

         this.rewind(_m330);
         --this.inputState.guessing;
      }

      if (!synPredMatched330) {
         if (_tokenSet_19.member(this.LA(1)) && _tokenSet_1.member(this.LA(2)) && prevToken == 201) {
            if (this.inputState.guessing == 0) {
               this.require(false, "Ambiguous expression could be a parameterless closure expression, an isolated open code block, or it may continue a previous statement", "Add an explicit parameter list, e.g. {it -> ...}, or force it to be treated as an open block by giving it a label, e.g. L:{...}, and also either remove the previous newline, or add an explicit semicolon ';'");
            }

            checkSuspiciousExpressionStatement_AST = currentAST.root;
         } else {
            if (!_tokenSet_19.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2)) || prevToken == 201) {
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            if (this.inputState.guessing == 0) {
               this.require(false, "Ambiguous expression could be either a parameterless closure expression or an isolated open code block", "Add an explicit closure parameter list, e.g. {it -> ...}, or force it to be treated as an open block by giving it a label, e.g. L:{...}");
            }

            checkSuspiciousExpressionStatement_AST = currentAST.root;
         }
      } else {
         if (_tokenSet_19.member(this.LA(1)) && _tokenSet_1.member(this.LA(2)) && prevToken == 201) {
            if (this.inputState.guessing == 0) {
               this.addWarning("Expression statement looks like it may continue a previous statement", "Either remove the previous newline, or add an explicit semicolon ';'.");
            }
         } else if (!_tokenSet_19.member(this.LA(1)) || !_tokenSet_1.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         checkSuspiciousExpressionStatement_AST = currentAST.root;
      }

      this.returnAST = checkSuspiciousExpressionStatement_AST;
   }

   public final void commandArguments(AST head) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST commandArguments_AST = null;
      Token first = this.LT(1);
      int hls = false;
      this.commandArgument();
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96) {
         this.match(96);
         this.nls();
         this.commandArgument();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         commandArguments_AST = currentAST.root;
         AST elist = this.astFactory.make((new ASTArray(2)).add(this.create(32, "ELIST", (Token)first, (Token)this.LT(1))).add(commandArguments_AST));
         AST headid = this.getASTFactory().dup(head);
         headid.setType(26);
         headid.setText("<command>");
         commandArguments_AST = this.astFactory.make((new ASTArray(3)).add(headid).add(head).add(elist));
         currentAST.root = commandArguments_AST;
         currentAST.child = commandArguments_AST != null && commandArguments_AST.getFirstChild() != null ? commandArguments_AST.getFirstChild() : commandArguments_AST;
         currentAST.advanceChildToEnd();
      }

      commandArguments_AST = currentAST.root;
      this.returnAST = commandArguments_AST;
   }

   public final void aCase() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST aCase_AST = null;
      AST tmp254_AST;
      switch(this.LA(1)) {
      case 125:
         tmp254_AST = null;
         tmp254_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp254_AST);
         this.match(125);
         break;
      case 145:
         tmp254_AST = null;
         tmp254_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp254_AST);
         this.match(145);
         this.expression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.match(131);
      this.nls();
      aCase_AST = currentAST.root;
      this.returnAST = aCase_AST;
   }

   public final void caseSList() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST caseSList_AST = null;
      Token first = this.LT(1);
      this.statement(131);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 124 || this.LA(1) == 201) {
         this.sep();
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 79:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 132:
         case 134:
         case 135:
         case 136:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 146:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.statement(this.sepToken);
            this.astFactory.addASTChild(currentAST, this.returnAST);
            break;
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 83:
         case 86:
         case 87:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 133:
         case 137:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 123:
         case 124:
         case 125:
         case 145:
         case 201:
         }
      }

      if (this.inputState.guessing == 0) {
         caseSList_AST = currentAST.root;
         caseSList_AST = this.astFactory.make((new ASTArray(2)).add(this.create(7, "SLIST", (Token)first, (Token)this.LT(1))).add(caseSList_AST));
         currentAST.root = caseSList_AST;
         currentAST.child = caseSList_AST != null && caseSList_AST.getFirstChild() != null ? caseSList_AST.getFirstChild() : caseSList_AST;
         currentAST.advanceChildToEnd();
      }

      caseSList_AST = currentAST.root;
      this.returnAST = caseSList_AST;
   }

   public final void forInit() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST forInit_AST = null;
      Token first = this.LT(1);
      boolean synPredMatched347 = false;
      if (_tokenSet_15.member(this.LA(1)) && _tokenSet_16.member(this.LA(2))) {
         int _m347 = this.mark();
         synPredMatched347 = true;
         ++this.inputState.guessing;

         try {
            this.declarationStart();
         } catch (RecognitionException var7) {
            synPredMatched347 = false;
         }

         this.rewind(_m347);
         --this.inputState.guessing;
      }

      if (synPredMatched347) {
         this.declaration();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         forInit_AST = currentAST.root;
      } else {
         if (!_tokenSet_86.member(this.LA(1)) || !_tokenSet_75.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         switch(this.LA(1)) {
         case 1:
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 83:
         case 86:
         case 87:
         case 89:
         case 90:
         case 91:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.controlExpressionList();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         if (this.inputState.guessing == 0) {
            forInit_AST = currentAST.root;
            forInit_AST = this.astFactory.make((new ASTArray(2)).add(this.create(33, "FOR_INIT", (Token)first, (Token)this.LT(1))).add(forInit_AST));
            currentAST.root = forInit_AST;
            currentAST.child = forInit_AST != null && forInit_AST.getFirstChild() != null ? forInit_AST.getFirstChild() : forInit_AST;
            currentAST.advanceChildToEnd();
         }

         forInit_AST = currentAST.root;
      }

      this.returnAST = forInit_AST;
   }

   public final void controlExpressionList() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST controlExpressionList_AST = null;
      Token first = this.LT(1);
      boolean sce = false;
      sce = this.strictContextExpression(false);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 96) {
         this.match(96);
         this.nls();
         sce = this.strictContextExpression(false);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         controlExpressionList_AST = currentAST.root;
         controlExpressionList_AST = this.astFactory.make((new ASTArray(2)).add(this.create(32, "ELIST", (Token)first, (Token)this.LT(1))).add(controlExpressionList_AST));
         currentAST.root = controlExpressionList_AST;
         currentAST.child = controlExpressionList_AST != null && controlExpressionList_AST.getFirstChild() != null ? controlExpressionList_AST.getFirstChild() : controlExpressionList_AST;
         currentAST.advanceChildToEnd();
      }

      controlExpressionList_AST = currentAST.root;
      this.returnAST = controlExpressionList_AST;
   }

   public final void forCond() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST forCond_AST = null;
      Token first = this.LT(1);
      boolean sce = false;
      switch(this.LA(1)) {
      case 1:
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      case 119:
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 137:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 82:
      case 84:
      case 85:
      case 88:
      case 92:
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
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
      case 128:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         sce = this.strictContextExpression(false);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         forCond_AST = currentAST.root;
         forCond_AST = this.astFactory.make((new ASTArray(2)).add(this.create(34, "FOR_CONDITION", (Token)first, (Token)this.LT(1))).add(forCond_AST));
         currentAST.root = forCond_AST;
         currentAST.child = forCond_AST != null && forCond_AST.getFirstChild() != null ? forCond_AST.getFirstChild() : forCond_AST;
         currentAST.advanceChildToEnd();
      }

      forCond_AST = currentAST.root;
      this.returnAST = forCond_AST;
   }

   public final void forIter() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST forIter_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 1:
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 39:
      case 40:
      case 41:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 110:
      case 119:
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 137:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 37:
      case 38:
      case 42:
      case 80:
      case 81:
      case 82:
      case 84:
      case 85:
      case 88:
      case 92:
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
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 122:
      case 128:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 152:
      case 154:
      case 155:
      case 156:
      case 186:
      case 189:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.controlExpressionList();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      if (this.inputState.guessing == 0) {
         forIter_AST = currentAST.root;
         forIter_AST = this.astFactory.make((new ASTArray(2)).add(this.create(35, "FOR_ITERATOR", (Token)first, (Token)this.LT(1))).add(forIter_AST));
         currentAST.root = forIter_AST;
         currentAST.child = forIter_AST != null && forIter_AST.getFirstChild() != null ? forIter_AST.getFirstChild() : forIter_AST;
         currentAST.advanceChildToEnd();
      }

      forIter_AST = currentAST.root;
      this.returnAST = forIter_AST;
   }

   public final void handler() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST handler_AST = null;
      AST pd_AST = null;
      AST handlerCs_AST = null;
      Token first = this.LT(1);
      this.match(148);
      this.match(88);
      this.parameterDeclaration();
      pd_AST = this.returnAST;
      this.match(119);
      this.nlsWarn();
      this.compoundStatement();
      handlerCs_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         handler_AST = currentAST.root;
         handler_AST = this.astFactory.make((new ASTArray(3)).add(this.create(148, "catch", (Token)first, (Token)this.LT(1))).add(pd_AST).add(handlerCs_AST));
         currentAST.root = handler_AST;
         currentAST.child = handler_AST != null && handler_AST.getFirstChild() != null ? handler_AST.getFirstChild() : handler_AST;
         currentAST.advanceChildToEnd();
      }

      handler_AST = currentAST.root;
      this.returnAST = handler_AST;
   }

   public final void finallyClause() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST finallyClause_AST = null;
      AST finallyCs_AST = null;
      Token first = this.LT(1);
      this.match(147);
      this.nlsWarn();
      this.compoundStatement();
      finallyCs_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         finallyClause_AST = currentAST.root;
         finallyClause_AST = this.astFactory.make((new ASTArray(2)).add(this.create(147, "finally", (Token)first, (Token)this.LT(1))).add(finallyCs_AST));
         currentAST.root = finallyClause_AST;
         currentAST.child = finallyClause_AST != null && finallyClause_AST.getFirstChild() != null ? finallyClause_AST.getFirstChild() : finallyClause_AST;
         currentAST.advanceChildToEnd();
      }

      finallyClause_AST = currentAST.root;
      this.returnAST = finallyClause_AST;
   }

   public final void commandArgument() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST commandArgument_AST = null;
      Token c = null;
      AST c_AST = null;
      boolean synPredMatched364 = false;
      if (_tokenSet_87.member(this.LA(1)) && _tokenSet_88.member(this.LA(2))) {
         int _m364 = this.mark();
         synPredMatched364 = true;
         ++this.inputState.guessing;

         try {
            this.argumentLabel();
            this.match(131);
         } catch (RecognitionException var8) {
            synPredMatched364 = false;
         }

         this.rewind(_m364);
         --this.inputState.guessing;
      }

      if (synPredMatched364) {
         this.argumentLabel();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         c = this.LT(1);
         c_AST = this.astFactory.create(c);
         this.astFactory.makeASTRoot(currentAST, c_AST);
         this.match(131);
         this.expression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            c_AST.setType(53);
         }

         commandArgument_AST = currentAST.root;
      } else {
         if (!_tokenSet_19.member(this.LA(1)) || !_tokenSet_75.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.expression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         commandArgument_AST = currentAST.root;
      }

      this.returnAST = commandArgument_AST;
   }

   public final void argumentLabel() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST argumentLabel_AST = null;
      Token id = null;
      AST id_AST = null;
      AST kw_AST = null;
      boolean synPredMatched522 = false;
      if (this.LA(1) == 84 && this.LA(2) == 131) {
         int _m522 = this.mark();
         synPredMatched522 = true;
         ++this.inputState.guessing;

         try {
            this.match(84);
         } catch (RecognitionException var11) {
            synPredMatched522 = false;
         }

         this.rewind(_m522);
         --this.inputState.guessing;
      }

      if (synPredMatched522) {
         id = this.LT(1);
         id_AST = this.astFactory.create(id);
         this.astFactory.addASTChild(currentAST, id_AST);
         this.match(84);
         if (this.inputState.guessing == 0) {
            id_AST.setType(85);
         }

         argumentLabel_AST = currentAST.root;
      } else {
         boolean synPredMatched524 = false;
         if (_tokenSet_89.member(this.LA(1)) && this.LA(2) == 131) {
            int _m524 = this.mark();
            synPredMatched524 = true;
            ++this.inputState.guessing;

            try {
               this.keywordPropertyNames();
            } catch (RecognitionException var10) {
               synPredMatched524 = false;
            }

            this.rewind(_m524);
            --this.inputState.guessing;
         }

         if (synPredMatched524) {
            this.keywordPropertyNames();
            kw_AST = this.returnAST;
            this.astFactory.addASTChild(currentAST, this.returnAST);
            if (this.inputState.guessing == 0) {
               kw_AST.setType(85);
            }

            argumentLabel_AST = currentAST.root;
         } else {
            if (!_tokenSet_90.member(this.LA(1)) || !_tokenSet_88.member(this.LA(2))) {
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.primaryExpression();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            argumentLabel_AST = currentAST.root;
         }
      }

      this.returnAST = argumentLabel_AST;
   }

   public final void multipleAssignment(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST multipleAssignment_AST = null;
      Token first = this.cloneToken(this.LT(1));
      AST tmp262_AST = null;
      tmp262_AST = this.astFactory.create(this.LT(1));
      this.astFactory.makeASTRoot(currentAST, tmp262_AST);
      this.match(88);
      this.nls();
      this.listOfVariables((AST)null, (AST)null, first);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      this.match(119);
      AST tmp264_AST = null;
      tmp264_AST = this.astFactory.create(this.LT(1));
      this.astFactory.makeASTRoot(currentAST, tmp264_AST);
      this.match(120);
      this.nls();
      this.assignmentExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      multipleAssignment_AST = currentAST.root;
      this.returnAST = multipleAssignment_AST;
   }

   public final void pathExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST pathExpression_AST = null;
      AST pre_AST = null;
      AST pe_AST = null;
      AST apb_AST = null;
      AST prefix = null;
      this.primaryExpression();
      pre_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         prefix = pre_AST;
      }

      while(true) {
         while(true) {
            boolean synPredMatched380 = false;
            if (_tokenSet_91.member(this.LA(1)) && _tokenSet_92.member(this.LA(2))) {
               int _m380 = this.mark();
               synPredMatched380 = true;
               ++this.inputState.guessing;

               try {
                  this.pathElementStart();
               } catch (RecognitionException var13) {
                  synPredMatched380 = false;
               }

               this.rewind(_m380);
               --this.inputState.guessing;
            }

            if (synPredMatched380) {
               this.nls();
               this.pathElement(prefix);
               pe_AST = this.returnAST;
               if (this.inputState.guessing == 0) {
                  prefix = pe_AST;
               }
            } else {
               boolean synPredMatched382 = false;
               if ((this.LA(1) == 122 || this.LA(1) == 201) && _tokenSet_17.member(this.LA(2)) && (lc_stmt == 1 || lc_stmt == 2)) {
                  int _m382 = this.mark();
                  synPredMatched382 = true;
                  ++this.inputState.guessing;

                  try {
                     this.nls();
                     this.match(122);
                  } catch (RecognitionException var12) {
                     synPredMatched382 = false;
                  }

                  this.rewind(_m382);
                  --this.inputState.guessing;
               }

               if (!synPredMatched382) {
                  if (this.inputState.guessing == 0) {
                     pathExpression_AST = currentAST.root;
                     this.lastPathExpression = prefix;
                     currentAST.root = prefix;
                     currentAST.child = prefix != null && prefix.getFirstChild() != null ? prefix.getFirstChild() : prefix;
                     currentAST.advanceChildToEnd();
                  }

                  pathExpression_AST = currentAST.root;
                  this.returnAST = pathExpression_AST;
                  return;
               }

               this.nlsWarn();
               this.appendedBlock(prefix);
               apb_AST = this.returnAST;
               if (this.inputState.guessing == 0) {
                  prefix = apb_AST;
               }
            }
         }
      }
   }

   public final void primaryExpression() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST primaryExpression_AST = null;
      AST pe_AST = null;
      Token first = this.LT(1);
      AST tmp266_AST;
      switch(this.LA(1)) {
      case 82:
         this.listOrMapConstructorExpression();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         primaryExpression_AST = currentAST.root;
         break;
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
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
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
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
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 186:
      case 187:
      case 188:
      case 189:
      case 190:
      case 191:
      case 192:
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 84:
         tmp266_AST = null;
         tmp266_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp266_AST);
         this.match(84);
         primaryExpression_AST = currentAST.root;
         break;
      case 85:
      case 152:
      case 155:
      case 156:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.constant();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         primaryExpression_AST = currentAST.root;
         break;
      case 88:
         this.parenthesizedExpression();
         pe_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            primaryExpression_AST = currentAST.root;
            primaryExpression_AST = this.astFactory.make((new ASTArray(2)).add(this.create(27, "EXPR", (Token)first, (Token)this.LT(1))).add(pe_AST));
            currentAST.root = primaryExpression_AST;
            currentAST.child = primaryExpression_AST != null && primaryExpression_AST.getFirstChild() != null ? primaryExpression_AST.getFirstChild() : primaryExpression_AST;
            currentAST.advanceChildToEnd();
         }

         primaryExpression_AST = currentAST.root;
         break;
      case 95:
         tmp266_AST = null;
         tmp266_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp266_AST);
         this.match(95);
         primaryExpression_AST = currentAST.root;
         break;
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         this.builtInType();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         primaryExpression_AST = currentAST.root;
         break;
      case 122:
         this.closableBlockConstructorExpression();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         primaryExpression_AST = currentAST.root;
         break;
      case 128:
         tmp266_AST = null;
         tmp266_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp266_AST);
         this.match(128);
         primaryExpression_AST = currentAST.root;
         break;
      case 154:
         this.newExpression();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         primaryExpression_AST = currentAST.root;
         break;
      case 193:
         this.stringConstructorExpression();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         primaryExpression_AST = currentAST.root;
      }

      this.returnAST = primaryExpression_AST;
   }

   public final void pathElementStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST pathElementStart_AST = null;
      AST tmp271_AST;
      switch(this.LA(1)) {
      case 82:
         tmp271_AST = null;
         tmp271_AST = this.astFactory.create(this.LT(1));
         this.match(82);
         break;
      case 87:
      case 201:
         this.nls();
         tmp271_AST = null;
         tmp271_AST = this.astFactory.create(this.LT(1));
         this.match(87);
         break;
      case 88:
         tmp271_AST = null;
         tmp271_AST = this.astFactory.create(this.LT(1));
         this.match(88);
         break;
      case 122:
         tmp271_AST = null;
         tmp271_AST = this.astFactory.create(this.LT(1));
         this.match(122);
         break;
      case 149:
         tmp271_AST = null;
         tmp271_AST = this.astFactory.create(this.LT(1));
         this.match(149);
         break;
      case 150:
         tmp271_AST = null;
         tmp271_AST = this.astFactory.create(this.LT(1));
         this.match(150);
         break;
      case 151:
         tmp271_AST = null;
         tmp271_AST = this.astFactory.create(this.LT(1));
         this.match(151);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = (AST)pathElementStart_AST;
   }

   public final void pathElement(AST prefix) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST pathElement_AST = null;
      AST ta_AST = null;
      AST np_AST = null;
      AST mca_AST = null;
      AST apb_AST = null;
      AST ipa_AST = null;
      Token thisPart = null;
      AST thisPart_AST = null;
      Token operator = this.LT(1);
      switch(this.LA(1)) {
      case 82:
         this.indexPropertyArgs(prefix);
         ipa_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            pathElement_AST = currentAST.root;
            currentAST.root = ipa_AST;
            currentAST.child = ipa_AST != null && ipa_AST.getFirstChild() != null ? ipa_AST.getFirstChild() : ipa_AST;
            currentAST.advanceChildToEnd();
         }

         pathElement_AST = currentAST.root;
         break;
      case 88:
         this.methodCallArgs(prefix);
         mca_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            pathElement_AST = currentAST.root;
            currentAST.root = mca_AST;
            currentAST.child = mca_AST != null && mca_AST.getFirstChild() != null ? mca_AST.getFirstChild() : mca_AST;
            currentAST.advanceChildToEnd();
         }

         pathElement_AST = currentAST.root;
         break;
      case 122:
         this.appendedBlock(prefix);
         apb_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            pathElement_AST = currentAST.root;
            currentAST.root = apb_AST;
            currentAST.child = apb_AST != null && apb_AST.getFirstChild() != null ? apb_AST.getFirstChild() : apb_AST;
            currentAST.advanceChildToEnd();
         }

         pathElement_AST = currentAST.root;
         break;
      default:
         if (_tokenSet_93.member(this.LA(1)) && _tokenSet_94.member(this.LA(2))) {
            if (this.inputState.guessing == 0) {
               pathElement_AST = currentAST.root;
               currentAST.root = prefix;
               currentAST.child = prefix != null && prefix.getFirstChild() != null ? prefix.getFirstChild() : prefix;
               currentAST.advanceChildToEnd();
            }

            switch(this.LA(1)) {
            case 87:
            case 201:
               this.nls();
               this.match(87);
               break;
            case 149:
               this.match(149);
               break;
            case 150:
               this.match(150);
               break;
            case 151:
               this.match(151);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.nls();
            switch(this.LA(1)) {
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 78:
            case 79:
            case 80:
            case 81:
            case 84:
            case 85:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 94:
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
            case 122:
            case 125:
            case 126:
            case 127:
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
            case 193:
               break;
            case 40:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 82:
            case 83:
            case 87:
            case 93:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 109:
            case 119:
            case 120:
            case 121:
            case 123:
            case 124:
            case 128:
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
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            case 86:
               this.typeArguments();
               ta_AST = this.returnAST;
            }

            this.namePart();
            np_AST = this.returnAST;
            if (this.inputState.guessing == 0) {
               pathElement_AST = currentAST.root;
               pathElement_AST = this.astFactory.make((new ASTArray(4)).add(this.create(operator.getType(), operator.getText(), prefix, this.LT(1))).add(prefix).add(ta_AST).add(np_AST));
               currentAST.root = pathElement_AST;
               currentAST.child = pathElement_AST != null && pathElement_AST.getFirstChild() != null ? pathElement_AST.getFirstChild() : pathElement_AST;
               currentAST.advanceChildToEnd();
            }

            pathElement_AST = currentAST.root;
         } else {
            if (this.LA(1) != 87 || this.LA(2) != 128 && this.LA(2) != 201) {
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.match(87);
            this.nls();
            thisPart = this.LT(1);
            thisPart_AST = this.astFactory.create(thisPart);
            this.match(128);
            if (this.inputState.guessing == 0) {
               pathElement_AST = currentAST.root;
               pathElement_AST = this.astFactory.make((new ASTArray(3)).add(this.create(operator.getType(), operator.getText(), prefix, this.LT(1))).add(prefix).add(thisPart_AST));
               currentAST.root = pathElement_AST;
               currentAST.child = pathElement_AST != null && pathElement_AST.getFirstChild() != null ? pathElement_AST.getFirstChild() : pathElement_AST;
               currentAST.advanceChildToEnd();
            }

            pathElement_AST = currentAST.root;
         }
      }

      this.returnAST = pathElement_AST;
   }

   public final void appendedBlock(AST callee) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST appendedBlock_AST = null;
      AST cb_AST = null;
      this.closableBlock();
      cb_AST = this.returnAST;
      if (this.inputState.guessing == 0) {
         appendedBlock_AST = currentAST.root;
         if (callee != null && callee.getType() == 26) {
            appendedBlock_AST = this.astFactory.make((new ASTArray(3)).add(this.create(26, "(", (AST)callee, (Token)this.LT(1))).add(callee.getFirstChild()).add(cb_AST));
         } else {
            appendedBlock_AST = this.astFactory.make((new ASTArray(3)).add(this.create(26, "{", (AST)callee, (Token)this.LT(1))).add(callee).add(cb_AST));
         }

         currentAST.root = appendedBlock_AST;
         currentAST.child = appendedBlock_AST != null && appendedBlock_AST.getFirstChild() != null ? appendedBlock_AST.getFirstChild() : appendedBlock_AST;
         currentAST.advanceChildToEnd();
      }

      appendedBlock_AST = currentAST.root;
      this.returnAST = appendedBlock_AST;
   }

   public final void namePart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST namePart_AST = null;
      Token ats = null;
      AST ats_AST = null;
      Token sl = null;
      AST sl_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 82:
      case 83:
      case 86:
      case 87:
      case 93:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 119:
      case 120:
      case 121:
      case 123:
      case 124:
      case 128:
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
      case 186:
      case 187:
      case 188:
      case 189:
      case 190:
      case 191:
      case 192:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 92:
         ats = this.LT(1);
         ats_AST = this.astFactory.create(ats);
         this.astFactory.makeASTRoot(currentAST, ats_AST);
         this.match(92);
         if (this.inputState.guessing == 0) {
            ats_AST.setType(51);
         }
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 94:
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
      case 122:
      case 125:
      case 126:
      case 127:
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
      case 193:
         switch(this.LA(1)) {
         case 37:
         case 38:
         case 39:
         case 41:
         case 42:
         case 78:
         case 79:
         case 80:
         case 81:
         case 89:
         case 90:
         case 91:
         case 94:
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
         case 125:
         case 126:
         case 127:
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
            this.keywordPropertyNames();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            break;
         case 40:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 82:
         case 83:
         case 86:
         case 87:
         case 92:
         case 93:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 119:
         case 120:
         case 121:
         case 123:
         case 124:
         case 128:
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
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 191:
         case 192:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 84:
            AST tmp280_AST = null;
            tmp280_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp280_AST);
            this.match(84);
            break;
         case 85:
            sl = this.LT(1);
            sl_AST = this.astFactory.create(sl);
            this.astFactory.addASTChild(currentAST, sl_AST);
            this.match(85);
            if (this.inputState.guessing == 0) {
               sl_AST.setType(84);
            }
            break;
         case 88:
         case 193:
            this.dynamicMemberName();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            break;
         case 122:
            this.openBlock();
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         namePart_AST = currentAST.root;
         this.returnAST = namePart_AST;
      }
   }

   public final void methodCallArgs(AST callee) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST methodCallArgs_AST = null;
      AST al_AST = null;
      this.match(88);
      this.argList();
      al_AST = this.returnAST;
      this.match(119);
      if (this.inputState.guessing == 0) {
         methodCallArgs_AST = currentAST.root;
         if (callee != null && callee.getFirstChild() != null) {
            methodCallArgs_AST = this.astFactory.make((new ASTArray(3)).add(this.create(26, "(", (AST)callee.getFirstChild(), (Token)this.LT(1))).add(callee).add(al_AST));
         } else {
            methodCallArgs_AST = this.astFactory.make((new ASTArray(3)).add(this.create(26, "(", (AST)callee, (Token)this.LT(1))).add(callee).add(al_AST));
         }

         currentAST.root = methodCallArgs_AST;
         currentAST.child = methodCallArgs_AST != null && methodCallArgs_AST.getFirstChild() != null ? methodCallArgs_AST.getFirstChild() : methodCallArgs_AST;
         currentAST.advanceChildToEnd();
      }

      methodCallArgs_AST = currentAST.root;
      this.returnAST = methodCallArgs_AST;
   }

   public final void indexPropertyArgs(AST indexee) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST indexPropertyArgs_AST = null;
      Token lb = null;
      AST lb_AST = null;
      AST al_AST = null;
      lb = this.LT(1);
      lb_AST = this.astFactory.create(lb);
      this.astFactory.addASTChild(currentAST, lb_AST);
      this.match(82);
      this.argList();
      al_AST = this.returnAST;
      this.match(83);
      if (this.inputState.guessing == 0) {
         indexPropertyArgs_AST = currentAST.root;
         if (indexee != null && indexee.getFirstChild() != null) {
            indexPropertyArgs_AST = this.astFactory.make((new ASTArray(4)).add(this.create(23, "INDEX_OP", (AST)indexee.getFirstChild(), (Token)this.LT(1))).add(lb_AST).add(indexee).add(al_AST));
         } else {
            indexPropertyArgs_AST = this.astFactory.make((new ASTArray(4)).add(this.create(23, "INDEX_OP", (AST)indexee, (Token)this.LT(1))).add(lb_AST).add(indexee).add(al_AST));
         }

         currentAST.root = indexPropertyArgs_AST;
         currentAST.child = indexPropertyArgs_AST != null && indexPropertyArgs_AST.getFirstChild() != null ? indexPropertyArgs_AST.getFirstChild() : indexPropertyArgs_AST;
         currentAST.advanceChildToEnd();
      }

      indexPropertyArgs_AST = currentAST.root;
      this.returnAST = indexPropertyArgs_AST;
   }

   public final void dynamicMemberName() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST dynamicMemberName_AST = null;
      AST pe_AST = null;
      Token first = this.LT(1);
      switch(this.LA(1)) {
      case 88:
         this.parenthesizedExpression();
         pe_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            dynamicMemberName_AST = currentAST.root;
            dynamicMemberName_AST = this.astFactory.make((new ASTArray(2)).add(this.create(27, "EXPR", (Token)first, (Token)this.LT(1))).add(pe_AST));
            currentAST.root = dynamicMemberName_AST;
            currentAST.child = dynamicMemberName_AST != null && dynamicMemberName_AST.getFirstChild() != null ? dynamicMemberName_AST.getFirstChild() : dynamicMemberName_AST;
            currentAST.advanceChildToEnd();
         }
         break;
      case 193:
         this.stringConstructorExpression();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      if (this.inputState.guessing == 0) {
         dynamicMemberName_AST = currentAST.root;
         dynamicMemberName_AST = this.astFactory.make((new ASTArray(2)).add(this.create(52, "DYNAMIC_MEMBER", (Token)first, (Token)this.LT(1))).add(dynamicMemberName_AST));
         currentAST.root = dynamicMemberName_AST;
         currentAST.child = dynamicMemberName_AST != null && dynamicMemberName_AST.getFirstChild() != null ? dynamicMemberName_AST.getFirstChild() : dynamicMemberName_AST;
         currentAST.advanceChildToEnd();
      }

      dynamicMemberName_AST = currentAST.root;
      this.returnAST = dynamicMemberName_AST;
   }

   public final void parenthesizedExpression() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST parenthesizedExpression_AST = null;
      Token first = this.LT(1);
      Token declaration = null;
      boolean hasClosureList = false;
      boolean firstContainsDeclaration = false;
      boolean sce = false;
      this.match(88);
      if (this.inputState.guessing == 0) {
         declaration = this.LT(1);
      }

      firstContainsDeclaration = this.strictContextExpression(true);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 124) {
         this.match(124);
         if (this.inputState.guessing == 0) {
            hasClosureList = true;
         }

         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            sce = this.strictContextExpression(true);
            this.astFactory.addASTChild(currentAST, this.returnAST);
            break;
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 83:
         case 86:
         case 87:
         case 89:
         case 90:
         case 91:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 120:
         case 121:
         case 123:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 119:
         case 124:
            if (this.inputState.guessing == 0) {
               this.astFactory.addASTChild(currentAST, this.astFactory.create(36, "EMPTY_STAT"));
            }
         }
      }

      if (this.inputState.guessing == 0 && firstContainsDeclaration && !hasClosureList) {
         throw new NoViableAltException(declaration, this.getFilename());
      } else {
         this.match(119);
         if (this.inputState.guessing == 0) {
            parenthesizedExpression_AST = currentAST.root;
            if (hasClosureList) {
               parenthesizedExpression_AST = this.astFactory.make((new ASTArray(2)).add(this.create(76, "CLOSURE_LIST", (Token)first, (Token)this.LT(1))).add(parenthesizedExpression_AST));
            }

            currentAST.root = parenthesizedExpression_AST;
            currentAST.child = parenthesizedExpression_AST != null && parenthesizedExpression_AST.getFirstChild() != null ? parenthesizedExpression_AST.getFirstChild() : parenthesizedExpression_AST;
            currentAST.advanceChildToEnd();
         }

         parenthesizedExpression_AST = currentAST.root;
         this.returnAST = parenthesizedExpression_AST;
      }
   }

   public final void stringConstructorExpression() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST stringConstructorExpression_AST = null;
      Token cs = null;
      AST cs_AST = null;
      Token cm = null;
      AST cm_AST = null;
      Token ce = null;
      AST ce_AST = null;
      Token first = this.LT(1);
      cs = this.LT(1);
      cs_AST = this.astFactory.create(cs);
      this.astFactory.addASTChild(currentAST, cs_AST);
      this.match(193);
      if (this.inputState.guessing == 0) {
         cs_AST.setType(85);
      }

      this.stringConstructorValuePart();
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 48) {
         cm = this.LT(1);
         cm_AST = this.astFactory.create(cm);
         this.astFactory.addASTChild(currentAST, cm_AST);
         this.match(48);
         if (this.inputState.guessing == 0) {
            cm_AST.setType(85);
         }

         this.stringConstructorValuePart();
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      ce = this.LT(1);
      ce_AST = this.astFactory.create(ce);
      this.astFactory.addASTChild(currentAST, ce_AST);
      this.match(194);
      if (this.inputState.guessing == 0) {
         stringConstructorExpression_AST = currentAST.root;
         ce_AST.setType(85);
         stringConstructorExpression_AST = this.astFactory.make((new ASTArray(2)).add(this.create(47, "STRING_CONSTRUCTOR", (Token)first, (Token)this.LT(1))).add(stringConstructorExpression_AST));
         currentAST.root = stringConstructorExpression_AST;
         currentAST.child = stringConstructorExpression_AST != null && stringConstructorExpression_AST.getFirstChild() != null ? stringConstructorExpression_AST.getFirstChild() : stringConstructorExpression_AST;
         currentAST.advanceChildToEnd();
      }

      stringConstructorExpression_AST = currentAST.root;
      this.returnAST = stringConstructorExpression_AST;
   }

   public final void logicalOrExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST logicalOrExpression_AST = null;
      this.logicalAndExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 170) {
         AST tmp287_AST = null;
         tmp287_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp287_AST);
         this.match(170);
         this.nls();
         this.logicalAndExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      logicalOrExpression_AST = currentAST.root;
      this.returnAST = logicalOrExpression_AST;
   }

   public final void logicalAndExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST logicalAndExpression_AST = null;
      this.inclusiveOrExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 171) {
         AST tmp288_AST = null;
         tmp288_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp288_AST);
         this.match(171);
         this.nls();
         this.inclusiveOrExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      logicalAndExpression_AST = currentAST.root;
      this.returnAST = logicalAndExpression_AST;
   }

   public final void inclusiveOrExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST inclusiveOrExpression_AST = null;
      this.exclusiveOrExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 172) {
         AST tmp289_AST = null;
         tmp289_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp289_AST);
         this.match(172);
         this.nls();
         this.exclusiveOrExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      inclusiveOrExpression_AST = currentAST.root;
      this.returnAST = inclusiveOrExpression_AST;
   }

   public final void exclusiveOrExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST exclusiveOrExpression_AST = null;
      this.andExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 173) {
         AST tmp290_AST = null;
         tmp290_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp290_AST);
         this.match(173);
         this.nls();
         this.andExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      exclusiveOrExpression_AST = currentAST.root;
      this.returnAST = exclusiveOrExpression_AST;
   }

   public final void andExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST andExpression_AST = null;
      this.regexExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 121) {
         AST tmp291_AST = null;
         tmp291_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp291_AST);
         this.match(121);
         this.nls();
         this.regexExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      andExpression_AST = currentAST.root;
      this.returnAST = andExpression_AST;
   }

   public final void regexExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST regexExpression_AST = null;
      this.equalityExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 174 || this.LA(1) == 175) {
         AST tmp293_AST;
         switch(this.LA(1)) {
         case 174:
            tmp293_AST = null;
            tmp293_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp293_AST);
            this.match(174);
            break;
         case 175:
            tmp293_AST = null;
            tmp293_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp293_AST);
            this.match(175);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.nls();
         this.equalityExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      regexExpression_AST = currentAST.root;
      this.returnAST = regexExpression_AST;
   }

   public final void equalityExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST equalityExpression_AST = null;
      this.relationalExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) >= 176 && this.LA(1) <= 180) {
         AST tmp298_AST;
         switch(this.LA(1)) {
         case 176:
            tmp298_AST = null;
            tmp298_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp298_AST);
            this.match(176);
            break;
         case 177:
            tmp298_AST = null;
            tmp298_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp298_AST);
            this.match(177);
            break;
         case 178:
            tmp298_AST = null;
            tmp298_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp298_AST);
            this.match(178);
            break;
         case 179:
            tmp298_AST = null;
            tmp298_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp298_AST);
            this.match(179);
            break;
         case 180:
            tmp298_AST = null;
            tmp298_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp298_AST);
            this.match(180);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.nls();
         this.relationalExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      equalityExpression_AST = currentAST.root;
      this.returnAST = equalityExpression_AST;
   }

   public final void relationalExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST relationalExpression_AST = null;
      this.shiftExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      AST tmp305_AST;
      if (_tokenSet_95.member(this.LA(1)) && _tokenSet_81.member(this.LA(2))) {
         switch(this.LA(1)) {
         case 86:
            tmp305_AST = null;
            tmp305_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp305_AST);
            this.match(86);
            break;
         case 97:
            tmp305_AST = null;
            tmp305_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp305_AST);
            this.match(97);
            break;
         case 137:
            tmp305_AST = null;
            tmp305_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp305_AST);
            this.match(137);
            break;
         case 181:
            tmp305_AST = null;
            tmp305_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp305_AST);
            this.match(181);
            break;
         case 182:
            tmp305_AST = null;
            tmp305_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp305_AST);
            this.match(182);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.nls();
         this.shiftExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (this.LA(1) == 153 && _tokenSet_96.member(this.LA(2))) {
         tmp305_AST = null;
         tmp305_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp305_AST);
         this.match(153);
         this.nls();
         this.typeSpec(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (this.LA(1) == 110 && _tokenSet_96.member(this.LA(2))) {
         tmp305_AST = null;
         tmp305_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp305_AST);
         this.match(110);
         this.nls();
         this.typeSpec(true);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      } else if (!_tokenSet_97.member(this.LA(1)) || !_tokenSet_73.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      relationalExpression_AST = currentAST.root;
      this.returnAST = relationalExpression_AST;
   }

   public final void additiveExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST additiveExpression_AST = null;
      this.multiplicativeExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while((this.LA(1) == 143 || this.LA(1) == 144) && _tokenSet_81.member(this.LA(2))) {
         AST tmp307_AST;
         switch(this.LA(1)) {
         case 143:
            tmp307_AST = null;
            tmp307_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp307_AST);
            this.match(143);
            break;
         case 144:
            tmp307_AST = null;
            tmp307_AST = this.astFactory.create(this.LT(1));
            this.astFactory.makeASTRoot(currentAST, tmp307_AST);
            this.match(144);
            break;
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         this.nls();
         this.multiplicativeExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      additiveExpression_AST = currentAST.root;
      this.returnAST = additiveExpression_AST;
   }

   public final void multiplicativeExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST multiplicativeExpression_AST = null;
      AST tmp326_AST;
      AST tmp319_AST;
      switch(this.LA(1)) {
      case 82:
      case 84:
      case 85:
      case 88:
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
      case 122:
      case 128:
      case 152:
      case 154:
      case 155:
      case 156:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.powerExpressionNotPlusMinus(lc_stmt);
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(_tokenSet_98.member(this.LA(1))) {
            switch(this.LA(1)) {
            case 109:
               tmp326_AST = null;
               tmp326_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp326_AST);
               this.match(109);
               break;
            case 187:
               tmp326_AST = null;
               tmp326_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp326_AST);
               this.match(187);
               break;
            case 188:
               tmp326_AST = null;
               tmp326_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp326_AST);
               this.match(188);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.nls();
            this.powerExpression(0);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         multiplicativeExpression_AST = currentAST.root;
         break;
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
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
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
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
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 143:
         tmp326_AST = null;
         tmp326_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp326_AST);
         this.match(143);
         if (this.inputState.guessing == 0) {
            tmp326_AST.setType(30);
         }

         this.nls();
         this.powerExpressionNotPlusMinus(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(_tokenSet_98.member(this.LA(1))) {
            switch(this.LA(1)) {
            case 109:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(109);
               break;
            case 187:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(187);
               break;
            case 188:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(188);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.nls();
            this.powerExpression(0);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         multiplicativeExpression_AST = currentAST.root;
         break;
      case 144:
         tmp326_AST = null;
         tmp326_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp326_AST);
         this.match(144);
         if (this.inputState.guessing == 0) {
            tmp326_AST.setType(29);
         }

         this.nls();
         this.powerExpressionNotPlusMinus(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(_tokenSet_98.member(this.LA(1))) {
            switch(this.LA(1)) {
            case 109:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(109);
               break;
            case 187:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(187);
               break;
            case 188:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(188);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.nls();
            this.powerExpression(0);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         multiplicativeExpression_AST = currentAST.root;
         break;
      case 186:
         tmp326_AST = null;
         tmp326_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp326_AST);
         this.match(186);
         this.nls();
         this.powerExpressionNotPlusMinus(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(_tokenSet_98.member(this.LA(1))) {
            switch(this.LA(1)) {
            case 109:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(109);
               break;
            case 187:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(187);
               break;
            case 188:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(188);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.nls();
            this.powerExpression(0);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         multiplicativeExpression_AST = currentAST.root;
         break;
      case 189:
         tmp326_AST = null;
         tmp326_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp326_AST);
         this.match(189);
         this.nls();
         this.powerExpressionNotPlusMinus(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);

         while(_tokenSet_98.member(this.LA(1))) {
            switch(this.LA(1)) {
            case 109:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(109);
               break;
            case 187:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(187);
               break;
            case 188:
               tmp319_AST = null;
               tmp319_AST = this.astFactory.create(this.LT(1));
               this.astFactory.makeASTRoot(currentAST, tmp319_AST);
               this.match(188);
               break;
            default:
               throw new NoViableAltException(this.LT(1), this.getFilename());
            }

            this.nls();
            this.powerExpression(0);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         }

         multiplicativeExpression_AST = currentAST.root;
      }

      this.returnAST = multiplicativeExpression_AST;
   }

   public final void powerExpressionNotPlusMinus(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST powerExpressionNotPlusMinus_AST = null;
      this.unaryExpressionNotPlusMinus(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 190) {
         AST tmp327_AST = null;
         tmp327_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp327_AST);
         this.match(190);
         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      powerExpressionNotPlusMinus_AST = currentAST.root;
      this.returnAST = powerExpressionNotPlusMinus_AST;
   }

   public final void powerExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST powerExpression_AST = null;
      this.unaryExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);

      while(this.LA(1) == 190) {
         AST tmp328_AST = null;
         tmp328_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp328_AST);
         this.match(190);
         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
      }

      powerExpression_AST = currentAST.root;
      this.returnAST = powerExpression_AST;
   }

   public final void unaryExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST unaryExpression_AST = null;
      AST tmp331_AST;
      switch(this.LA(1)) {
      case 82:
      case 84:
      case 85:
      case 88:
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
      case 122:
      case 128:
      case 152:
      case 154:
      case 155:
      case 156:
      case 191:
      case 192:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.unaryExpressionNotPlusMinus(lc_stmt);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         unaryExpression_AST = currentAST.root;
         break;
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
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
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
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
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 143:
         tmp331_AST = null;
         tmp331_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp331_AST);
         this.match(143);
         if (this.inputState.guessing == 0) {
            tmp331_AST.setType(30);
         }

         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         unaryExpression_AST = currentAST.root;
         break;
      case 144:
         tmp331_AST = null;
         tmp331_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp331_AST);
         this.match(144);
         if (this.inputState.guessing == 0) {
            tmp331_AST.setType(29);
         }

         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         unaryExpression_AST = currentAST.root;
         break;
      case 186:
         tmp331_AST = null;
         tmp331_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp331_AST);
         this.match(186);
         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         unaryExpression_AST = currentAST.root;
         break;
      case 189:
         tmp331_AST = null;
         tmp331_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp331_AST);
         this.match(189);
         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         unaryExpression_AST = currentAST.root;
      }

      this.returnAST = unaryExpression_AST;
   }

   public final void unaryExpressionNotPlusMinus(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST unaryExpressionNotPlusMinus_AST = null;
      Token lpb = null;
      AST lpb_AST = null;
      Token lp = null;
      AST lp_AST = null;
      AST tmp334_AST;
      switch(this.LA(1)) {
      case 82:
      case 84:
      case 85:
      case 88:
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
      case 122:
      case 128:
      case 152:
      case 154:
      case 155:
      case 156:
      case 193:
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         boolean synPredMatched474 = false;
         if (this.LA(1) == 88 && this.LA(2) >= 100 && this.LA(2) <= 108) {
            int _m474 = this.mark();
            synPredMatched474 = true;
            ++this.inputState.guessing;

            try {
               this.match(88);
               this.builtInTypeSpec(true);
               this.match(119);
               this.unaryExpression(0);
            } catch (RecognitionException var13) {
               synPredMatched474 = false;
            }

            this.rewind(_m474);
            --this.inputState.guessing;
         }

         if (synPredMatched474) {
            lpb = this.LT(1);
            lpb_AST = this.astFactory.create(lpb);
            this.astFactory.makeASTRoot(currentAST, lpb_AST);
            this.match(88);
            if (this.inputState.guessing == 0) {
               lpb_AST.setType(22);
            }

            this.builtInTypeSpec(true);
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.match(119);
            this.unaryExpression(0);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         } else {
            boolean synPredMatched476 = false;
            if (this.LA(1) == 88 && this.LA(2) == 84) {
               int _m476 = this.mark();
               synPredMatched476 = true;
               ++this.inputState.guessing;

               try {
                  this.match(88);
                  this.classTypeSpec(true);
                  this.match(119);
                  this.unaryExpressionNotPlusMinus(0);
               } catch (RecognitionException var12) {
                  synPredMatched476 = false;
               }

               this.rewind(_m476);
               --this.inputState.guessing;
            }

            if (synPredMatched476) {
               lp = this.LT(1);
               lp_AST = this.astFactory.create(lp);
               this.astFactory.makeASTRoot(currentAST, lp_AST);
               this.match(88);
               if (this.inputState.guessing == 0) {
                  lp_AST.setType(22);
               }

               this.classTypeSpec(true);
               this.astFactory.addASTChild(currentAST, this.returnAST);
               this.match(119);
               this.unaryExpressionNotPlusMinus(0);
               this.astFactory.addASTChild(currentAST, this.returnAST);
            } else {
               if (!_tokenSet_90.member(this.LA(1)) || !_tokenSet_37.member(this.LA(2))) {
                  throw new NoViableAltException(this.LT(1), this.getFilename());
               }

               this.postfixExpression(lc_stmt);
               this.astFactory.addASTChild(currentAST, this.returnAST);
            }
         }

         unaryExpressionNotPlusMinus_AST = currentAST.root;
         break;
      case 83:
      case 86:
      case 87:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
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
      case 120:
      case 121:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 129:
      case 130:
      case 131:
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
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 153:
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
      case 186:
      case 187:
      case 188:
      case 189:
      case 190:
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 191:
         tmp334_AST = null;
         tmp334_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp334_AST);
         this.match(191);
         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         unaryExpressionNotPlusMinus_AST = currentAST.root;
         break;
      case 192:
         tmp334_AST = null;
         tmp334_AST = this.astFactory.create(this.LT(1));
         this.astFactory.makeASTRoot(currentAST, tmp334_AST);
         this.match(192);
         this.nls();
         this.unaryExpression(0);
         this.astFactory.addASTChild(currentAST, this.returnAST);
         unaryExpressionNotPlusMinus_AST = currentAST.root;
      }

      this.returnAST = unaryExpressionNotPlusMinus_AST;
   }

   public final void postfixExpression(int lc_stmt) throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST postfixExpression_AST = null;
      Token in = null;
      AST in_AST = null;
      Token de = null;
      AST de_AST = null;
      this.pathExpression(lc_stmt);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.LA(1) == 186 && _tokenSet_99.member(this.LA(2))) {
         in = this.LT(1);
         in_AST = this.astFactory.create(in);
         this.astFactory.makeASTRoot(currentAST, in_AST);
         this.match(186);
         if (this.inputState.guessing == 0) {
            in_AST.setType(24);
         }
      } else if (this.LA(1) == 189 && _tokenSet_99.member(this.LA(2))) {
         de = this.LT(1);
         de_AST = this.astFactory.create(de);
         this.astFactory.makeASTRoot(currentAST, de_AST);
         this.match(189);
         if (this.inputState.guessing == 0) {
            de_AST.setType(25);
         }
      } else if (!_tokenSet_99.member(this.LA(1)) || !_tokenSet_73.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      postfixExpression_AST = currentAST.root;
      this.returnAST = postfixExpression_AST;
   }

   public final void constant() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST constant_AST = null;
      AST tmp338_AST;
      switch(this.LA(1)) {
      case 85:
         tmp338_AST = null;
         tmp338_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp338_AST);
         this.match(85);
         constant_AST = currentAST.root;
         break;
      case 152:
         tmp338_AST = null;
         tmp338_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp338_AST);
         this.match(152);
         constant_AST = currentAST.root;
         break;
      case 155:
         tmp338_AST = null;
         tmp338_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp338_AST);
         this.match(155);
         constant_AST = currentAST.root;
         break;
      case 156:
         tmp338_AST = null;
         tmp338_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp338_AST);
         this.match(156);
         constant_AST = currentAST.root;
         break;
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.constantNumber();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         constant_AST = currentAST.root;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = constant_AST;
   }

   public final void newExpression() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST newExpression_AST = null;
      AST ta_AST = null;
      AST t_AST = null;
      AST mca_AST = null;
      AST cb_AST = null;
      AST ad_AST = null;
      Token first = this.LT(1);
      this.match(154);
      this.nls();
      switch(this.LA(1)) {
      case 84:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
         break;
      case 85:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 86:
         this.typeArguments();
         ta_AST = this.returnAST;
      }

      this.type();
      t_AST = this.returnAST;
      switch(this.LA(1)) {
      case 82:
         this.newArrayDeclarator();
         ad_AST = this.returnAST;
         if (this.inputState.guessing == 0) {
            newExpression_AST = currentAST.root;
            newExpression_AST = this.astFactory.make((new ASTArray(4)).add(this.create(154, "new", (Token)first, (Token)this.LT(1))).add(ta_AST).add(t_AST).add(ad_AST));
            currentAST.root = newExpression_AST;
            currentAST.child = newExpression_AST != null && newExpression_AST.getFirstChild() != null ? newExpression_AST.getFirstChild() : newExpression_AST;
            currentAST.advanceChildToEnd();
         }
         break;
      case 88:
      case 201:
         this.nls();
         this.methodCallArgs((AST)null);
         mca_AST = this.returnAST;
         if (this.LA(1) == 122 && _tokenSet_50.member(this.LA(2))) {
            this.classBlock();
            cb_AST = this.returnAST;
            this.astFactory.addASTChild(currentAST, this.returnAST);
         } else if (!_tokenSet_100.member(this.LA(1)) || !_tokenSet_73.member(this.LA(2))) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         if (this.inputState.guessing == 0) {
            newExpression_AST = currentAST.root;
            mca_AST = mca_AST.getFirstChild();
            newExpression_AST = this.astFactory.make((new ASTArray(5)).add(this.create(154, "new", (Token)first, (Token)this.LT(1))).add(ta_AST).add(t_AST).add(mca_AST).add(cb_AST));
            currentAST.root = newExpression_AST;
            currentAST.child = newExpression_AST != null && newExpression_AST.getFirstChild() != null ? newExpression_AST.getFirstChild() : newExpression_AST;
            currentAST.advanceChildToEnd();
         }
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      newExpression_AST = currentAST.root;
      this.returnAST = newExpression_AST;
   }

   public final void closableBlockConstructorExpression() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST closableBlockConstructorExpression_AST = null;
      this.closableBlock();
      this.astFactory.addASTChild(currentAST, this.returnAST);
      closableBlockConstructorExpression_AST = currentAST.root;
      this.returnAST = closableBlockConstructorExpression_AST;
   }

   public final void listOrMapConstructorExpression() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST listOrMapConstructorExpression_AST = null;
      Token lcon = null;
      AST lcon_AST = null;
      AST args_AST = null;
      Token emcon = null;
      AST emcon_AST = null;
      boolean hasLabels = false;
      if (this.LA(1) == 82 && _tokenSet_101.member(this.LA(2))) {
         lcon = this.LT(1);
         lcon_AST = this.astFactory.create(lcon);
         this.match(82);
         this.argList();
         args_AST = this.returnAST;
         this.astFactory.addASTChild(currentAST, this.returnAST);
         if (this.inputState.guessing == 0) {
            hasLabels |= this.argListHasLabels;
         }

         this.match(83);
         if (this.inputState.guessing == 0) {
            listOrMapConstructorExpression_AST = currentAST.root;
            int type = hasLabels ? 57 : 56;
            listOrMapConstructorExpression_AST = this.astFactory.make((new ASTArray(2)).add(this.create(type, "[", lcon_AST, this.LT(1))).add(args_AST));
            currentAST.root = listOrMapConstructorExpression_AST;
            currentAST.child = listOrMapConstructorExpression_AST != null && listOrMapConstructorExpression_AST.getFirstChild() != null ? listOrMapConstructorExpression_AST.getFirstChild() : listOrMapConstructorExpression_AST;
            currentAST.advanceChildToEnd();
         }

         listOrMapConstructorExpression_AST = currentAST.root;
      } else {
         if (this.LA(1) != 82 || this.LA(2) != 131) {
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }

         emcon = this.LT(1);
         emcon_AST = this.astFactory.create(emcon);
         this.astFactory.makeASTRoot(currentAST, emcon_AST);
         this.match(82);
         this.match(131);
         this.match(83);
         if (this.inputState.guessing == 0) {
            emcon_AST.setType(57);
         }

         listOrMapConstructorExpression_AST = currentAST.root;
      }

      this.returnAST = listOrMapConstructorExpression_AST;
   }

   public final void stringConstructorValuePart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST stringConstructorValuePart_AST = null;
      AST tmp345_AST;
      switch(this.LA(1)) {
      case 84:
         this.identifier();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      case 95:
         tmp345_AST = null;
         tmp345_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp345_AST);
         this.match(95);
         break;
      case 122:
         this.openOrClosableBlock();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         break;
      case 128:
         tmp345_AST = null;
         tmp345_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp345_AST);
         this.match(128);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      stringConstructorValuePart_AST = currentAST.root;
      this.returnAST = stringConstructorValuePart_AST;
   }

   public final void newArrayDeclarator() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST newArrayDeclarator_AST = null;
      Token lb = null;
      AST lb_AST = null;
      int _cnt532 = 0;

      while(this.LA(1) == 82 && _tokenSet_102.member(this.LA(2))) {
         lb = this.LT(1);
         lb_AST = this.astFactory.create(lb);
         this.astFactory.makeASTRoot(currentAST, lb_AST);
         this.match(82);
         if (this.inputState.guessing == 0) {
            lb_AST.setType(16);
         }

         switch(this.LA(1)) {
         case 82:
         case 84:
         case 85:
         case 88:
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
         case 122:
         case 128:
         case 143:
         case 144:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            this.expression(0);
            this.astFactory.addASTChild(currentAST, this.returnAST);
         case 83:
            this.match(83);
            ++_cnt532;
            break;
         case 86:
         case 87:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
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
         case 120:
         case 121:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 131:
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
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         }
      }

      if (_cnt532 >= 1) {
         newArrayDeclarator_AST = currentAST.root;
         this.returnAST = newArrayDeclarator_AST;
      } else {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }
   }

   public final byte argument() throws RecognitionException, TokenStreamException {
      byte hasLabelOrSpread = 0;
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST argument_AST = null;
      Token c = null;
      AST c_AST = null;
      Token sp = null;
      AST sp_AST = null;
      boolean sce = false;
      boolean synPredMatched518 = false;
      if (_tokenSet_87.member(this.LA(1)) && _tokenSet_88.member(this.LA(2))) {
         int _m518 = this.mark();
         synPredMatched518 = true;
         ++this.inputState.guessing;

         try {
            this.argumentLabelStart();
         } catch (RecognitionException var12) {
            synPredMatched518 = false;
         }

         this.rewind(_m518);
         --this.inputState.guessing;
      }

      if (synPredMatched518) {
         this.argumentLabel();
         this.astFactory.addASTChild(currentAST, this.returnAST);
         c = this.LT(1);
         c_AST = this.astFactory.create(c);
         this.astFactory.makeASTRoot(currentAST, c_AST);
         this.match(131);
         if (this.inputState.guessing == 0) {
            c_AST.setType(53);
         }

         if (this.inputState.guessing == 0) {
            hasLabelOrSpread = (byte)(hasLabelOrSpread | 1);
         }
      } else if (this.LA(1) == 109) {
         sp = this.LT(1);
         sp_AST = this.astFactory.create(sp);
         this.astFactory.makeASTRoot(currentAST, sp_AST);
         this.match(109);
         if (this.inputState.guessing == 0) {
            sp_AST.setType(54);
         }

         if (this.inputState.guessing == 0) {
            hasLabelOrSpread = (byte)(hasLabelOrSpread | 2);
         }

         switch(this.LA(1)) {
         case 37:
         case 38:
         case 42:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 88:
         case 92:
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
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 122:
         case 128:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 152:
         case 154:
         case 155:
         case 156:
         case 186:
         case 189:
         case 191:
         case 192:
         case 193:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
            break;
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 83:
         case 86:
         case 87:
         case 89:
         case 90:
         case 91:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 119:
         case 120:
         case 121:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 129:
         case 130:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 153:
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
         case 194:
         default:
            throw new NoViableAltException(this.LT(1), this.getFilename());
         case 131:
            this.match(131);
            if (this.inputState.guessing == 0) {
               sp_AST.setType(55);
            }

            if (this.inputState.guessing == 0) {
               hasLabelOrSpread = (byte)(hasLabelOrSpread | 1);
            }
         }
      } else if (!_tokenSet_83.member(this.LA(1)) || !_tokenSet_103.member(this.LA(2))) {
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      sce = this.strictContextExpression(true);
      this.astFactory.addASTChild(currentAST, this.returnAST);
      if (this.inputState.guessing == 0) {
         this.require(this.LA(1) != 131, "illegal colon after argument expression", "a complex label expression before a colon must be parenthesized");
      }

      argument_AST = currentAST.root;
      this.returnAST = argument_AST;
      return hasLabelOrSpread;
   }

   public final void argumentLabelStart() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST argumentLabelStart_AST = null;
      AST tmp351_AST;
      switch(this.LA(1)) {
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 78:
      case 79:
      case 80:
      case 81:
      case 89:
      case 90:
      case 91:
      case 94:
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
      case 125:
      case 126:
      case 127:
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
         this.keywordPropertyNames();
         break;
      case 40:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 83:
      case 86:
      case 87:
      case 92:
      case 93:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 109:
      case 119:
      case 120:
      case 121:
      case 123:
      case 124:
      case 128:
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
      case 186:
      case 187:
      case 188:
      case 189:
      case 190:
      case 191:
      case 192:
      case 194:
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      case 82:
      case 88:
      case 122:
      case 193:
         this.balancedBrackets();
         break;
      case 84:
         tmp351_AST = null;
         tmp351_AST = this.astFactory.create(this.LT(1));
         this.match(84);
         break;
      case 85:
         tmp351_AST = null;
         tmp351_AST = this.astFactory.create(this.LT(1));
         this.match(85);
         break;
      case 195:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
         this.constantNumber();
      }

      tmp351_AST = null;
      tmp351_AST = this.astFactory.create(this.LT(1));
      this.match(131);
      this.returnAST = (AST)argumentLabelStart_AST;
   }

   public final void constantNumber() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      ASTPair currentAST = new ASTPair();
      AST constantNumber_AST = null;
      AST tmp357_AST;
      switch(this.LA(1)) {
      case 195:
         tmp357_AST = null;
         tmp357_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp357_AST);
         this.match(195);
         constantNumber_AST = currentAST.root;
         break;
      case 196:
         tmp357_AST = null;
         tmp357_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp357_AST);
         this.match(196);
         constantNumber_AST = currentAST.root;
         break;
      case 197:
         tmp357_AST = null;
         tmp357_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp357_AST);
         this.match(197);
         constantNumber_AST = currentAST.root;
         break;
      case 198:
         tmp357_AST = null;
         tmp357_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp357_AST);
         this.match(198);
         constantNumber_AST = currentAST.root;
         break;
      case 199:
         tmp357_AST = null;
         tmp357_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp357_AST);
         this.match(199);
         constantNumber_AST = currentAST.root;
         break;
      case 200:
         tmp357_AST = null;
         tmp357_AST = this.astFactory.create(this.LT(1));
         this.astFactory.addASTChild(currentAST, tmp357_AST);
         this.match(200);
         constantNumber_AST = currentAST.root;
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = constantNumber_AST;
   }

   public final void balancedBrackets() throws RecognitionException, TokenStreamException {
      this.returnAST = null;
      new ASTPair();
      AST balancedBrackets_AST = null;
      AST tmp364_AST;
      AST tmp365_AST;
      switch(this.LA(1)) {
      case 82:
         tmp364_AST = null;
         tmp364_AST = this.astFactory.create(this.LT(1));
         this.match(82);
         this.balancedTokens();
         tmp365_AST = null;
         tmp365_AST = this.astFactory.create(this.LT(1));
         this.match(83);
         break;
      case 88:
         tmp364_AST = null;
         tmp364_AST = this.astFactory.create(this.LT(1));
         this.match(88);
         this.balancedTokens();
         tmp365_AST = null;
         tmp365_AST = this.astFactory.create(this.LT(1));
         this.match(119);
         break;
      case 122:
         tmp364_AST = null;
         tmp364_AST = this.astFactory.create(this.LT(1));
         this.match(122);
         this.balancedTokens();
         tmp365_AST = null;
         tmp365_AST = this.astFactory.create(this.LT(1));
         this.match(123);
         break;
      case 193:
         tmp364_AST = null;
         tmp364_AST = this.astFactory.create(this.LT(1));
         this.match(193);
         this.balancedTokens();
         tmp365_AST = null;
         tmp365_AST = this.astFactory.create(this.LT(1));
         this.match(194);
         break;
      default:
         throw new NoViableAltException(this.LT(1), this.getFilename());
      }

      this.returnAST = (AST)balancedBrackets_AST;
   }

   protected void buildTokenTypeASTClassMap() {
      this.tokenTypeToASTClassMap = null;
   }

   private static final long[] mk_tokenSet_0() {
      long[] data = new long[8];
      data[0] = 4810363371522L;
      data[1] = 1477075058612994048L;
      data[2] = -6629298651002438191L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_1() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -36028801313947648L;
      data[2] = -1L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_2() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -16384L;
      data[2] = -6620291452234629121L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_3() {
      long[] data = new long[16];
      data[0] = -14L;

      for(int i = 1; i <= 2; ++i) {
         data[i] = -1L;
      }

      data[3] = 268435455L;
      return data;
   }

   private static final long[] mk_tokenSet_4() {
      long[] data = new long[]{0L, 269533184L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_5() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2053535812893736960L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_6() {
      long[] data = new long[]{0L, 1097728L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_7() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2053535812826628096L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_8() {
      long[] data = new long[8];
      data[1] = 1152921504606846976L;
      data[2] = 32L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_9() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 1477075058612994048L;
      data[2] = -6629298651002438159L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_10() {
      long[] data = new long[8];
      data[0] = 2L;
      data[1] = 4035225266123964416L;
      data[2] = 131104L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_11() {
      long[] data = new long[8];
      data[0] = 289034119151618L;
      data[1] = -16384L;
      data[2] = -3L;
      data[3] = 1023L;
      return data;
   }

   private static final long[] mk_tokenSet_12() {
      long[] data = new long[]{4810363371520L, 35888059799240704L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_13() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35888059871592448L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_14() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35923175536787456L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_15() {
      long[] data = new long[]{4810363371520L, 35923175452901376L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_16() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35923175534952448L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_17() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2053535810916417536L;
      data[2] = -6629298651002438185L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_18() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 324153554006147072L;
      data[2] = -6629298651002438191L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_19() {
      long[] data = new long[8];
      data[1] = 288265493971992576L;
      data[2] = -6629298651002732543L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_20() {
      long[] data = new long[]{0L, 68222976L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_21() {
      long[] data = new long[]{4810363371520L, 35888060034121728L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_22() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35888060035170304L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_23() {
      long[] data = new long[8];
      data[0] = 4810363371522L;
      data[1] = 4359378820134305792L;
      data[2] = -6629298651002307087L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_24() {
      long[] data = new long[]{0L, 35115653660672L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_25() {
      long[] data = new long[]{0L, 15990784L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_26() {
      long[] data = new long[8];
      data[0] = 2L;
      data[1] = 4107282864473636864L;
      data[2] = 131104L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_27() {
      long[] data = new long[8];
      data[0] = 2L;
      data[1] = 4107282864456859648L;
      data[2] = 131104L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_28() {
      long[] data = new long[8];
      data[1] = 4611686018428436480L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_29() {
      long[] data = new long[8];
      data[0] = 2L;
      data[1] = 4323455642275676160L;
      data[2] = 131104L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_30() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2053535810916417536L;
      data[2] = -6629298651002438191L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_31() {
      long[] data = new long[]{0L, 13893632L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_32() {
      long[] data = new long[]{2L, 1261007899959230464L, 520L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_33() {
      long[] data = new long[]{4810363371520L, 35923175467843584L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_34() {
      long[] data = new long[]{4810363371520L, 35923175459454976L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_35() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -1945590288370647040L;
      data[2] = -6629298650967179279L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_36() {
      long[] data = new long[8];
      data[0] = 4810363371522L;
      data[1] = 1801334233935626240L;
      data[2] = 2L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_37() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -16384L;
      data[2] = -1L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_38() {
      long[] data = new long[8];
      data[1] = 35116190531584L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_39() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -35184376299520L;
      data[2] = -6620291452249309185L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_40() {
      long[] data = new long[8];
      data[1] = 288230376168751104L;
      data[3] = 2L;
      return data;
   }

   private static final long[] mk_tokenSet_41() {
      long[] data = new long[16];
      data[0] = -16L;
      data[1] = -900719925491662849L;
      data[2] = -1L;
      data[3] = 268435449L;
      return data;
   }

   private static final long[] mk_tokenSet_42() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35923175691976704L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_43() {
      long[] data = new long[]{0L, 35116161171456L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_44() {
      long[] data = new long[8];
      data[0] = 2L;
      data[1] = 99876864L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_45() {
      long[] data = new long[]{4810363371520L, 35888059530674176L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_46() {
      long[] data = new long[8];
      data[1] = 288265494240428032L;
      data[2] = -6629298651002732543L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_47() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -72057598332911616L;
      data[2] = -2198486384641L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_48() {
      long[] data = new long[]{7559142440960L, -2269849463976378368L, 522092528L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_49() {
      long[] data = new long[8];
      data[1] = 2017612637441884160L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_50() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2053535808749764608L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_51() {
      long[] data = new long[8];
      data[0] = 4810363371522L;
      data[1] = 4359378820142694400L;
      data[2] = -6629298651002307087L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_52() {
      long[] data = new long[8];
      data[1] = -8935106479551152128L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_53() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = -7169836166886785024L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_54() {
      long[] data = new long[]{4810363371520L, 35888059800289280L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_55() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35888059884175360L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_56() {
      long[] data = new long[8];
      data[1] = 1801439855259942912L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_57() {
      long[] data = new long[8];
      data[1] = 1729382261205237760L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_58() {
      long[] data = new long[8];
      data[0] = 4810363371522L;
      data[1] = 4359378822258425856L;
      data[2] = 131104L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_59() {
      long[] data = new long[8];
      data[1] = 1729382261474721792L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_60() {
      long[] data = new long[8];
      data[1] = 269484032L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_61() {
      long[] data = new long[8];
      data[1] = 2017612637710319616L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_62() {
      long[] data = new long[8];
      data[1] = 2017612633061982208L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_63() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2053535815215579136L;
      data[2] = -6629298651002438191L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_64() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2053535810920611840L;
      data[2] = -6629298651002438191L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_65() {
      long[] data = new long[]{0L, 2151677952L, 1L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_66() {
      long[] data = new long[8];
      data[1] = 35116207308800L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_67() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -36028801313947648L;
      data[2] = -1L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_68() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 1477075058612994048L;
      data[2] = -6629298651002438191L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_69() {
      long[] data = new long[]{0L, 13893632L, 2L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_70() {
      long[] data = new long[8];
      data[1] = 108086395352907776L;
      data[2] = 4L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_71() {
      long[] data = new long[8];
      data[0] = 137438953472L;
      data[1] = 35115922227200L;
      data[2] = 6L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_72() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 2125593409261895680L;
      data[2] = -6629298651002438185L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_73() {
      long[] data = new long[8];
      data[0] = 289034119151618L;
      data[1] = -16384L;
      data[2] = -1L;
      data[3] = 1023L;
      return data;
   }

   private static final long[] mk_tokenSet_74() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -1981619085658046464L;
      data[2] = -6629298650967179279L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_75() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -36028797018980352L;
      data[2] = -1L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_76() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 1477075058378080256L;
      data[2] = -6629298651002700799L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_77() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -4294983680L;
      data[2] = -1L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_78() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35923175532855296L;
      data[2] = 520L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_79() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 35923175532855296L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_80() {
      long[] data = new long[8];
      data[1] = 288230376151711744L;
      data[2] = 524288L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_81() {
      long[] data = new long[8];
      data[1] = 288265493971992576L;
      data[2] = -6629298651002732543L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_82() {
      long[] data = new long[8];
      data[0] = 2L;
      data[1] = 4071254067438419968L;
      data[2] = 131104L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_83() {
      long[] data = new long[8];
      data[0] = 4810363371520L;
      data[1] = 324153553771233280L;
      data[2] = -6629298651002700799L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_84() {
      long[] data = new long[]{0L, 51539607552L, 252201579132747776L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_85() {
      long[] data = new long[8];
      data[0] = -16L;
      data[1] = -288230376151711745L;
      data[2] = -1L;
      data[3] = 268435455L;
      return data;
   }

   private static final long[] mk_tokenSet_86() {
      long[] data = new long[8];
      data[0] = 4810363371522L;
      data[1] = 324153553771233280L;
      data[2] = -6629298651002700799L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_87() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -1981619085658046464L;
      data[2] = 522092529L;
      data[3] = 506L;
      return data;
   }

   private static final long[] mk_tokenSet_88() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -252201644102533120L;
      data[2] = -6629298650967179265L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_89() {
      long[] data = new long[]{7559142440960L, -2269849463977426944L, 522092528L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_90() {
      long[] data = new long[8];
      data[1] = 288265493971992576L;
      data[2] = 486539265L;
      data[3] = 506L;
      return data;
   }

   private static final long[] mk_tokenSet_91() {
      long[] data = new long[8];
      data[1] = 288230376177139712L;
      data[2] = 14680064L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_92() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -216172847075180544L;
      data[2] = -6629298650952499209L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_93() {
      long[] data = new long[8];
      data[1] = 8388608L;
      data[2] = 14680064L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_94() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -1981619087524773888L;
      data[2] = 522092528L;
      data[3] = 514L;
      return data;
   }

   private static final long[] mk_tokenSet_95() {
      long[] data = new long[]{0L, 8594128896L, 27021597764223488L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_96() {
      long[] data = new long[8];
      data[1] = 35115653660672L;
      data[3] = 512L;
      return data;
   }

   private static final long[] mk_tokenSet_97() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -35244782665728L;
      data[2] = -6620291452249309187L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_98() {
      long[] data = new long[]{0L, 35184372088832L, 1729382256910270464L, 0L, 0L, 0L};
      return data;
   }

   private static final long[] mk_tokenSet_99() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -276840448L;
      data[2] = -14680067L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_100() {
      long[] data = new long[8];
      data[0] = 7559142440962L;
      data[1] = -268451840L;
      data[2] = -3L;
      data[3] = 1019L;
      return data;
   }

   private static final long[] mk_tokenSet_101() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -1981583901016997888L;
      data[2] = -6629298650967179279L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_102() {
      long[] data = new long[8];
      data[1] = 288265493972516864L;
      data[2] = -6629298651002732543L;
      data[3] = 507L;
      return data;
   }

   private static final long[] mk_tokenSet_103() {
      long[] data = new long[8];
      data[0] = 7559142440960L;
      data[1] = -16384L;
      data[2] = -1L;
      data[3] = 1019L;
      return data;
   }
}
