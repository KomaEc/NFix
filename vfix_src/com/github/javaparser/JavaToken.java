package com.github.javaparser;

import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Utils;
import java.util.List;
import java.util.Optional;

public class JavaToken {
   public static final JavaToken INVALID = new JavaToken();
   private Range range;
   private int kind;
   private String text;
   private JavaToken previousToken;
   private JavaToken nextToken;

   private JavaToken() {
      this((Range)null, 0, "INVALID", (JavaToken)null, (JavaToken)null);
   }

   public JavaToken(int kind, String text) {
      this((Range)null, kind, text, (JavaToken)null, (JavaToken)null);
   }

   JavaToken(Token token, List<JavaToken> tokens) {
      this.previousToken = null;
      this.nextToken = null;
      Range range = Range.range(token.beginLine, token.beginColumn, token.endLine, token.endColumn);
      String text = token.image;
      if (token.kind == 141) {
         range = Range.range(token.beginLine, token.beginColumn, token.endLine, token.beginColumn);
         text = ">";
      } else if (token.kind == 140) {
         range = Range.range(token.beginLine, token.beginColumn, token.endLine, token.beginColumn + 1);
         text = ">>";
      }

      this.range = range;
      this.kind = token.kind;
      this.text = text;
      if (!tokens.isEmpty()) {
         JavaToken previousToken = (JavaToken)tokens.get(tokens.size() - 1);
         this.previousToken = previousToken;
         previousToken.nextToken = this;
      } else {
         this.previousToken = null;
      }

   }

   public JavaToken(int kind) {
      this.previousToken = null;
      this.nextToken = null;
      String content = GeneratedJavaParserConstants.tokenImage[kind];
      if (content.startsWith("\"")) {
         content = content.substring(1, content.length() - 1);
      }

      if (TokenTypes.isEndOfLineToken(kind)) {
         content = Utils.EOL;
      } else if (TokenTypes.isWhitespace(kind)) {
         content = " ";
      }

      this.kind = kind;
      this.text = content;
   }

   public JavaToken(Range range, int kind, String text, JavaToken previousToken, JavaToken nextToken) {
      this.previousToken = null;
      this.nextToken = null;
      Utils.assertNotNull(text);
      this.range = range;
      this.kind = kind;
      this.text = text;
      this.previousToken = previousToken;
      this.nextToken = nextToken;
   }

   public Optional<Range> getRange() {
      return Optional.ofNullable(this.range);
   }

   public int getKind() {
      return this.kind;
   }

   void setKind(int kind) {
      this.kind = kind;
   }

   public String getText() {
      return this.text;
   }

   public Optional<JavaToken> getNextToken() {
      return Optional.ofNullable(this.nextToken);
   }

   public Optional<JavaToken> getPreviousToken() {
      return Optional.ofNullable(this.previousToken);
   }

   public void setRange(Range range) {
      this.range = range;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String asString() {
      return this.text;
   }

   public TokenRange toTokenRange() {
      return new TokenRange(this.findFirstToken(), this.findLastToken());
   }

   public String toString() {
      String text = this.getText().replace("\n", "\\n").replace("\r", "\\r").replace("\r\n", "\\r\\n").replace("\t", "\\t");
      return CodeGenerationUtils.f("\"%s\"   <%s>   %s", text, this.getKind(), this.getRange().map(Range::toString).orElse("(?)-(?)"));
   }

   public boolean valid() {
      return !this.invalid();
   }

   public boolean invalid() {
      return this == INVALID;
   }

   public JavaToken.Category getCategory() {
      return TokenTypes.getCategory(this.kind);
   }

   public void insert(JavaToken newToken) {
      Utils.assertNotNull(newToken);
      this.getPreviousToken().ifPresent((p) -> {
         p.nextToken = newToken;
         newToken.previousToken = p;
      });
      this.previousToken = newToken;
      newToken.nextToken = this;
   }

   public void insertAfter(JavaToken newToken) {
      Utils.assertNotNull(newToken);
      this.getNextToken().ifPresent((n) -> {
         n.previousToken = newToken;
         newToken.nextToken = n;
      });
      this.nextToken = newToken;
      newToken.previousToken = this;
   }

   public void deleteToken() {
      Optional<JavaToken> nextToken = this.getNextToken();
      Optional<JavaToken> previousToken = this.getPreviousToken();
      previousToken.ifPresent((p) -> {
         p.nextToken = (JavaToken)nextToken.orElse((Object)null);
      });
      nextToken.ifPresent((n) -> {
         n.previousToken = (JavaToken)previousToken.orElse((Object)null);
      });
   }

   public void replaceToken(JavaToken newToken) {
      Utils.assertNotNull(newToken);
      this.getPreviousToken().ifPresent((p) -> {
         p.nextToken = newToken;
         newToken.previousToken = p;
      });
      this.getNextToken().ifPresent((n) -> {
         n.previousToken = newToken;
         newToken.nextToken = n;
      });
   }

   public JavaToken findLastToken() {
      JavaToken current;
      for(current = this; current.getNextToken().isPresent(); current = (JavaToken)current.getNextToken().get()) {
      }

      return current;
   }

   public JavaToken findFirstToken() {
      JavaToken current;
      for(current = this; current.getPreviousToken().isPresent(); current = (JavaToken)current.getPreviousToken().get()) {
      }

      return current;
   }

   public int hashCode() {
      int result = this.kind;
      result = 31 * result + this.text.hashCode();
      return result;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavaToken javaToken = (JavaToken)o;
         if (this.kind != javaToken.kind) {
            return false;
         } else {
            return this.text.equals(javaToken.text);
         }
      } else {
         return false;
      }
   }

   public static enum Kind {
      EOF(0),
      SPACE(1),
      WINDOWS_EOL(2),
      UNIX_EOL(3),
      OLD_MAC_EOL(4),
      SINGLE_LINE_COMMENT(5),
      ENTER_JAVADOC_COMMENT(6),
      ENTER_MULTILINE_COMMENT(7),
      JAVADOC_COMMENT(8),
      MULTI_LINE_COMMENT(9),
      COMMENT_CONTENT(10),
      ABSTRACT(11),
      ASSERT(12),
      BOOLEAN(13),
      BREAK(14),
      BYTE(15),
      CASE(16),
      CATCH(17),
      CHAR(18),
      CLASS(19),
      CONST(20),
      CONTINUE(21),
      _DEFAULT(22),
      DO(23),
      DOUBLE(24),
      ELSE(25),
      ENUM(26),
      EXTENDS(27),
      FALSE(28),
      FINAL(29),
      FINALLY(30),
      FLOAT(31),
      FOR(32),
      GOTO(33),
      IF(34),
      IMPLEMENTS(35),
      IMPORT(36),
      INSTANCEOF(37),
      INT(38),
      INTERFACE(39),
      LONG(40),
      NATIVE(41),
      NEW(42),
      NULL(43),
      PACKAGE(44),
      PRIVATE(45),
      PROTECTED(46),
      PUBLIC(47),
      RETURN(48),
      SHORT(49),
      STATIC(50),
      STRICTFP(51),
      SUPER(52),
      SWITCH(53),
      SYNCHRONIZED(54),
      THIS(55),
      THROW(56),
      THROWS(57),
      TRANSIENT(58),
      TRUE(59),
      TRY(60),
      VOID(61),
      VOLATILE(62),
      WHILE(63),
      REQUIRES(64),
      TO(65),
      WITH(66),
      OPEN(67),
      OPENS(68),
      USES(69),
      MODULE(70),
      EXPORTS(71),
      PROVIDES(72),
      TRANSITIVE(73),
      LONG_LITERAL(74),
      INTEGER_LITERAL(75),
      DECIMAL_LITERAL(76),
      HEX_LITERAL(77),
      OCTAL_LITERAL(78),
      BINARY_LITERAL(79),
      FLOATING_POINT_LITERAL(80),
      DECIMAL_FLOATING_POINT_LITERAL(81),
      DECIMAL_EXPONENT(82),
      HEXADECIMAL_FLOATING_POINT_LITERAL(83),
      HEXADECIMAL_EXPONENT(84),
      HEX_DIGITS(85),
      UNICODE_ESCAPE(86),
      CHARACTER_LITERAL(87),
      STRING_LITERAL(88),
      IDENTIFIER(89),
      LETTER(90),
      PART_LETTER(91),
      LPAREN(92),
      RPAREN(93),
      LBRACE(94),
      RBRACE(95),
      LBRACKET(96),
      RBRACKET(97),
      SEMICOLON(98),
      COMMA(99),
      DOT(100),
      AT(101),
      ASSIGN(102),
      LT(103),
      BANG(104),
      TILDE(105),
      HOOK(106),
      COLON(107),
      EQ(108),
      LE(109),
      GE(110),
      NE(111),
      SC_OR(112),
      SC_AND(113),
      INCR(114),
      DECR(115),
      PLUS(116),
      MINUS(117),
      STAR(118),
      SLASH(119),
      BIT_AND(120),
      BIT_OR(121),
      XOR(122),
      REM(123),
      LSHIFT(124),
      PLUSASSIGN(125),
      MINUSASSIGN(126),
      STARASSIGN(127),
      SLASHASSIGN(128),
      ANDASSIGN(129),
      ORASSIGN(130),
      XORASSIGN(131),
      REMASSIGN(132),
      LSHIFTASSIGN(133),
      RSIGNEDSHIFTASSIGN(134),
      RUNSIGNEDSHIFTASSIGN(135),
      ELLIPSIS(136),
      ARROW(137),
      DOUBLECOLON(138),
      RUNSIGNEDSHIFT(139),
      RSIGNEDSHIFT(140),
      GT(141),
      CTRL_Z(142);

      private final int kind;

      private Kind(int kind) {
         this.kind = kind;
      }

      public static JavaToken.Kind valueOf(int kind) {
         switch(kind) {
         case 0:
            return EOF;
         case 1:
            return SPACE;
         case 2:
            return WINDOWS_EOL;
         case 3:
            return UNIX_EOL;
         case 4:
            return OLD_MAC_EOL;
         case 5:
            return SINGLE_LINE_COMMENT;
         case 6:
            return ENTER_JAVADOC_COMMENT;
         case 7:
            return ENTER_MULTILINE_COMMENT;
         case 8:
            return JAVADOC_COMMENT;
         case 9:
            return MULTI_LINE_COMMENT;
         case 10:
            return COMMENT_CONTENT;
         case 11:
            return ABSTRACT;
         case 12:
            return ASSERT;
         case 13:
            return BOOLEAN;
         case 14:
            return BREAK;
         case 15:
            return BYTE;
         case 16:
            return CASE;
         case 17:
            return CATCH;
         case 18:
            return CHAR;
         case 19:
            return CLASS;
         case 20:
            return CONST;
         case 21:
            return CONTINUE;
         case 22:
            return _DEFAULT;
         case 23:
            return DO;
         case 24:
            return DOUBLE;
         case 25:
            return ELSE;
         case 26:
            return ENUM;
         case 27:
            return EXTENDS;
         case 28:
            return FALSE;
         case 29:
            return FINAL;
         case 30:
            return FINALLY;
         case 31:
            return FLOAT;
         case 32:
            return FOR;
         case 33:
            return GOTO;
         case 34:
            return IF;
         case 35:
            return IMPLEMENTS;
         case 36:
            return IMPORT;
         case 37:
            return INSTANCEOF;
         case 38:
            return INT;
         case 39:
            return INTERFACE;
         case 40:
            return LONG;
         case 41:
            return NATIVE;
         case 42:
            return NEW;
         case 43:
            return NULL;
         case 44:
            return PACKAGE;
         case 45:
            return PRIVATE;
         case 46:
            return PROTECTED;
         case 47:
            return PUBLIC;
         case 48:
            return RETURN;
         case 49:
            return SHORT;
         case 50:
            return STATIC;
         case 51:
            return STRICTFP;
         case 52:
            return SUPER;
         case 53:
            return SWITCH;
         case 54:
            return SYNCHRONIZED;
         case 55:
            return THIS;
         case 56:
            return THROW;
         case 57:
            return THROWS;
         case 58:
            return TRANSIENT;
         case 59:
            return TRUE;
         case 60:
            return TRY;
         case 61:
            return VOID;
         case 62:
            return VOLATILE;
         case 63:
            return WHILE;
         case 64:
            return REQUIRES;
         case 65:
            return TO;
         case 66:
            return WITH;
         case 67:
            return OPEN;
         case 68:
            return OPENS;
         case 69:
            return USES;
         case 70:
            return MODULE;
         case 71:
            return EXPORTS;
         case 72:
            return PROVIDES;
         case 73:
            return TRANSITIVE;
         case 74:
            return LONG_LITERAL;
         case 75:
            return INTEGER_LITERAL;
         case 76:
            return DECIMAL_LITERAL;
         case 77:
            return HEX_LITERAL;
         case 78:
            return OCTAL_LITERAL;
         case 79:
            return BINARY_LITERAL;
         case 80:
            return FLOATING_POINT_LITERAL;
         case 81:
            return DECIMAL_FLOATING_POINT_LITERAL;
         case 82:
            return DECIMAL_EXPONENT;
         case 83:
            return HEXADECIMAL_FLOATING_POINT_LITERAL;
         case 84:
            return HEXADECIMAL_EXPONENT;
         case 85:
            return HEX_DIGITS;
         case 86:
            return UNICODE_ESCAPE;
         case 87:
            return CHARACTER_LITERAL;
         case 88:
            return STRING_LITERAL;
         case 89:
            return IDENTIFIER;
         case 90:
            return LETTER;
         case 91:
            return PART_LETTER;
         case 92:
            return LPAREN;
         case 93:
            return RPAREN;
         case 94:
            return LBRACE;
         case 95:
            return RBRACE;
         case 96:
            return LBRACKET;
         case 97:
            return RBRACKET;
         case 98:
            return SEMICOLON;
         case 99:
            return COMMA;
         case 100:
            return DOT;
         case 101:
            return AT;
         case 102:
            return ASSIGN;
         case 103:
            return LT;
         case 104:
            return BANG;
         case 105:
            return TILDE;
         case 106:
            return HOOK;
         case 107:
            return COLON;
         case 108:
            return EQ;
         case 109:
            return LE;
         case 110:
            return GE;
         case 111:
            return NE;
         case 112:
            return SC_OR;
         case 113:
            return SC_AND;
         case 114:
            return INCR;
         case 115:
            return DECR;
         case 116:
            return PLUS;
         case 117:
            return MINUS;
         case 118:
            return STAR;
         case 119:
            return SLASH;
         case 120:
            return BIT_AND;
         case 121:
            return BIT_OR;
         case 122:
            return XOR;
         case 123:
            return REM;
         case 124:
            return LSHIFT;
         case 125:
            return PLUSASSIGN;
         case 126:
            return MINUSASSIGN;
         case 127:
            return STARASSIGN;
         case 128:
            return SLASHASSIGN;
         case 129:
            return ANDASSIGN;
         case 130:
            return ORASSIGN;
         case 131:
            return XORASSIGN;
         case 132:
            return REMASSIGN;
         case 133:
            return LSHIFTASSIGN;
         case 134:
            return RSIGNEDSHIFTASSIGN;
         case 135:
            return RUNSIGNEDSHIFTASSIGN;
         case 136:
            return ELLIPSIS;
         case 137:
            return ARROW;
         case 138:
            return DOUBLECOLON;
         case 139:
            return RUNSIGNEDSHIFT;
         case 140:
            return RSIGNEDSHIFT;
         case 141:
            return GT;
         case 142:
            return CTRL_Z;
         default:
            throw new IllegalArgumentException(CodeGenerationUtils.f("Token kind %i is unknown.", kind));
         }
      }

      public int getKind() {
         return this.kind;
      }
   }

   public static enum Category {
      WHITESPACE_NO_EOL,
      EOL,
      COMMENT,
      IDENTIFIER,
      KEYWORD,
      LITERAL,
      SEPARATOR,
      OPERATOR;

      public boolean isWhitespaceOrComment() {
         return this.isWhitespace() || this == COMMENT;
      }

      public boolean isWhitespace() {
         return this == WHITESPACE_NO_EOL || this == EOL;
      }

      public boolean isEndOfLine() {
         return this == EOL;
      }

      public boolean isComment() {
         return this == COMMENT;
      }

      public boolean isWhitespaceButNotEndOfLine() {
         return this == WHITESPACE_NO_EOL;
      }

      public boolean isIdentifier() {
         return this == IDENTIFIER;
      }

      public boolean isKeyword() {
         return this == KEYWORD;
      }

      public boolean isLiteral() {
         return this == LITERAL;
      }

      public boolean isSeparator() {
         return this == SEPARATOR;
      }

      public boolean isOperator() {
         return this == OPERATOR;
      }
   }
}
