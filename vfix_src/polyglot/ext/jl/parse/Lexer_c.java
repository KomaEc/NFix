package polyglot.ext.jl.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.HashMap;
import polyglot.lex.BooleanLiteral;
import polyglot.lex.CharacterLiteral;
import polyglot.lex.DoubleLiteral;
import polyglot.lex.EOF;
import polyglot.lex.EscapedUnicodeReader;
import polyglot.lex.FloatLiteral;
import polyglot.lex.Identifier;
import polyglot.lex.IntegerLiteral;
import polyglot.lex.Keyword;
import polyglot.lex.Lexer;
import polyglot.lex.LongLiteral;
import polyglot.lex.NullLiteral;
import polyglot.lex.Operator;
import polyglot.lex.StringLiteral;
import polyglot.lex.Token;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;

public class Lexer_c implements Lexer {
   public static final int YYEOF = -1;
   private static final int YY_BUFFERSIZE = 16384;
   public static final int END_OF_LINE_COMMENT = 4;
   public static final int STRING = 1;
   public static final int YYINITIAL = 0;
   public static final int CHARACTER = 2;
   public static final int TRADITIONAL_COMMENT = 3;
   private static final String yycmap_packed = "\t\u0005\u0001\u0003\u0001\u0001\u0001\u0000\u0001\u0003\u0001\u0002\u000e\u0005\u0004\u0000\u0001\u0003\u0001(\u0001\u0013\u0001\u0000\u0001\u0004\u00010\u0001,\u0001\u0012\u0001\u001d\u0001\u001e\u0001\u0011\u0001.\u0001$\u0001\r\u0001\u000b\u0001\u0010\u0001\u0006\u0003\u000f\u0004\n\u0002\u0007\u0001+\u0001#\u0001'\u0001%\u0001&\u0001*\u0001\u0000\u0003\t\u00013\u0001\f\u00012\u0005\u0004\u00011\u000b\u0004\u0001\b\u0002\u0004\u0001!\u0001\u000e\u0001\"\u0001/\u0001\u0004\u0001\u0000\u0001\u0019\u00014\u0001\t\u00013\u0001\u0017\u0001\u0018\u0005\u0004\u0001\u001a\u0001\u0004\u0001\u001c\u0003\u0004\u0001\u0015\u0001\u001b\u0001\u0014\u0001\u0016\u0002\u0004\u0001\b\u0002\u0004\u0001\u001f\u0001-\u0001 \u0001)!\u0005\u0002\u0000\u0004\u0004\u0004\u0000\u0001\u0004\n\u0000\u0001\u0004\u0004\u0000\u0001\u0004\u0005\u0000\u0017\u0004\u0001\u0000\u001f\u0004\u0001\u0000Ĩ\u0004\u0002\u0000\u0012\u0004\u001c\u0000^\u0004\u0002\u0000\t\u0004\u0002\u0000\u0007\u0004\u000e\u0000\u0002\u0004\u000e\u0000\u0005\u0004\t\u0000\u0001\u0004\u0011\u0000O\u0005\u0011\u0000\u0003\u0005\u0017\u0000\u0001\u0004\u000b\u0000\u0001\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0014\u0004\u0001\u0000,\u0004\u0001\u0000\b\u0004\u0002\u0000\u001a\u0004\f\u0000\u0082\u0004\u0001\u0000\u0004\u0005\u0005\u00009\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0002\u0004\u0003\u0000&\u0004\u0002\u0000\u0002\u00047\u0000&\u0004\u0002\u0000\u0001\u0004\u0007\u0000'\u0004\t\u0000\u0011\u0005\u0001\u0000\u0017\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0001\u0005\u0001\u0000\u0002\u0005\u0001\u0000\u0001\u0005\u000b\u0000\u001b\u0004\u0005\u0000\u0003\u0004.\u0000\u001a\u0004\u0005\u0000\u000b\u0004\u000b\u0005\n\u0000\n\u0005\u0006\u0000\u0001\u0005c\u0004\u0001\u0000\u0001\u0004\u0007\u0005\u0002\u0000\u0006\u0005\u0002\u0004\u0002\u0005\u0001\u0000\u0004\u0005\u0002\u0000\n\u0005\u0003\u0004\u0012\u0000\u0001\u0005\u0001\u0004\u0001\u0005\u001b\u0004\u0003\u0000\u001b\u00055\u0000&\u0004\u000b\u0005Ő\u0000\u0003\u0005\u0001\u00005\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0010\u0005\u0002\u0000\u0001\u0004\u0004\u0005\u0003\u0000\n\u0004\u0002\u0005\u0002\u0000\n\u0005\u0011\u0000\u0003\u0005\u0001\u0000\b\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0004\u0004\u0002\u0000\u0001\u0005\u0001\u0000\u0007\u0005\u0002\u0000\u0002\u0005\u0002\u0000\u0003\u0005\t\u0000\u0001\u0005\u0004\u0000\u0002\u0004\u0001\u0000\u0003\u0004\u0002\u0005\u0002\u0000\n\u0005\u0004\u0004\u000e\u0000\u0001\u0005\u0002\u0000\u0006\u0004\u0004\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0002\u0000\u0001\u0005\u0001\u0000\u0005\u0005\u0004\u0000\u0002\u0005\u0002\u0000\u0003\u0005\u000b\u0000\u0004\u0004\u0001\u0000\u0001\u0004\u0007\u0000\f\u0005\u0003\u0004\f\u0000\u0003\u0005\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0005\u0004\u0002\u0000\u0001\u0005\u0001\u0004\b\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0003\u0005\u0002\u0000\u0001\u0004\u000f\u0000\u0001\u0004\u0005\u0000\n\u0005\u0011\u0000\u0003\u0005\u0001\u0000\b\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0002\u0000\u0004\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0006\u0005\u0003\u0000\u0002\u0005\u0002\u0000\u0003\u0005\b\u0000\u0002\u0005\u0004\u0000\u0002\u0004\u0001\u0000\u0003\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\u0006\u0004\u0003\u0000\u0003\u0004\u0001\u0000\u0004\u0004\u0003\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0003\u0000\u0002\u0004\u0003\u0000\u0003\u0004\u0003\u0000\b\u0004\u0001\u0000\u0003\u0004\u0004\u0000\u0005\u0005\u0003\u0000\u0003\u0005\u0001\u0000\u0004\u0005\t\u0000\u0001\u0005\u000f\u0000\t\u0005\u0011\u0000\u0003\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\n\u0004\u0001\u0000\u0005\u0004\u0004\u0000\u0007\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0004\u0005\u0007\u0000\u0002\u0005\t\u0000\u0002\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\n\u0004\u0001\u0000\u0005\u0004\u0004\u0000\u0007\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0004\u0005\u0007\u0000\u0002\u0005\u0007\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\u0010\u0004\u0004\u0000\u0006\u0005\u0002\u0000\u0003\u0005\u0001\u0000\u0004\u0005\t\u0000\u0001\u0005\b\u0000\u0002\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\u0012\u0004\u0003\u0000\u0018\u0004\u0001\u0000\t\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0007\u0004\u0003\u0000\u0001\u0005\u0004\u0000\u0006\u0005\u0001\u0000\u0001\u0005\u0001\u0000\b\u0005\u0012\u0000\u0002\u0005\r\u00000\u0004\u0001\u0005\u0002\u0004\u0007\u0005\u0004\u0000\b\u0004\b\u0005\u0001\u0000\n\u0005'\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0001\u0004\u0006\u0000\u0004\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0002\u0004\u0001\u0000\u0004\u0004\u0001\u0005\u0002\u0004\u0006\u0005\u0001\u0000\u0002\u0005\u0001\u0004\u0002\u0000\u0005\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0006\u0005\u0002\u0000\n\u0005\u0002\u0000\u0002\u0004\"\u0000\u0001\u0004\u0017\u0000\u0002\u0005\u0006\u0000\n\u0005\u000b\u0000\u0001\u0005\u0001\u0000\u0001\u0005\u0001\u0000\u0001\u0005\u0004\u0000\u0002\u0005\b\u0004\u0001\u0000\"\u0004\u0006\u0000\u0014\u0005\u0001\u0000\u0002\u0005\u0004\u0004\u0004\u0000\b\u0005\u0001\u0000$\u0005\t\u0000\u0001\u00059\u0000\"\u0004\u0001\u0000\u0005\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0007\u0005\u0003\u0000\u0004\u0005\u0006\u0000\n\u0005\u0006\u0000\u0006\u0004\u0004\u0005F\u0000&\u0004\n\u0000'\u0004\t\u0000Z\u0004\u0005\u0000D\u0004\u0005\u0000R\u0004\u0006\u0000\u0007\u0004\u0001\u0000?\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000'\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u001f\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0017\u0004\u0001\u0000\u001f\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000'\u0004\u0001\u0000\u0013\u0004\u000e\u0000\t\u0005.\u0000U\u0004\f\u0000ɬ\u0004\u0002\u0000\b\u0004\n\u0000\u001a\u0004\u0005\u0000K\u0004\u0095\u00004\u0004 \u0005\u0007\u0000\u0001\u0004\u0004\u0000\n\u0005!\u0000\u0004\u0005\u0001\u0000\n\u0005\u0006\u0000X\u0004\b\u0000)\u0004\u0001\u0005Ֆ\u0000\u009c\u0004\u0004\u0000Z\u0004\u0006\u0000\u0016\u0004\u0002\u0000\u0006\u0004\u0002\u0000&\u0004\u0002\u0000\u0006\u0004\u0002\u0000\b\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u001f\u0004\u0002\u00005\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0003\u0004\u0001\u0000\u0007\u0004\u0003\u0000\u0004\u0004\u0002\u0000\u0006\u0004\u0004\u0000\r\u0004\u0005\u0000\u0003\u0004\u0001\u0000\u0007\u0004\u000f\u0000\u0004\u0005\u001a\u0000\u0005\u0005\u0010\u0000\u0002\u0004)\u0000\u0006\u0005\u000f\u0000\u0001\u0004 \u0000\u0010\u0004 \u0000\r\u0005\u0004\u0000\u0001\u0005 \u0000\u0001\u0004\u0004\u0000\u0001\u0004\u0002\u0000\n\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0005\u0004\u0006\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0007\u0004&\u0000$\u0004ກ\u0000\u0003\u0004\u0019\u0000\t\u0004\u0006\u0005\u0001\u0000\u0005\u0004\u0002\u0000\u0003\u0004\u0006\u0000T\u0004\u0004\u0000\u0002\u0005\u0002\u0000\u0002\u0004\u0002\u0000^\u0004\u0006\u0000(\u0004\u0004\u0000^\u0004\u0011\u0000\u0018\u0004Ɉ\u0000ᦶ\u0004J\u0000冦\u0004Z\u0000ҍ\u0004ݳ\u0000\u2ba4\u0004⅜\u0000Į\u0004Ò\u0000\u0007\u0004\f\u0000\u0005\u0004\u0005\u0000\u0001\u0004\u0001\u0005\n\u0004\u0001\u0000\r\u0004\u0001\u0000\u0005\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0001\u0000l\u0004!\u0000ū\u0004\u0012\u0000@\u0004\u0002\u00006\u0004(\u0000\f\u0004$\u0000\u0004\u0005\u000f\u0000\u0002\u0004\u0018\u0000\u0003\u0004\u0019\u0000\u0001\u0004\u0006\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0087\u0004\u0002\u0000\u0001\u0005\u0004\u0000\u0001\u0004\u000b\u0000\n\u0005\u0007\u0000\u001a\u0004\u0004\u0000\u0001\u0004\u0001\u0000\u001a\u0004\n\u0000Z\u0004\u0003\u0000\u0006\u0004\u0002\u0000\u0006\u0004\u0002\u0000\u0006\u0004\u0002\u0000\u0003\u0004\u0003\u0000\u0002\u0004\u0003\u0000\u0002\u0004\u0012\u0000\u0003\u0005\u0004\u0000";
   private static final char[] yycmap = yy_unpack_cmap("\t\u0005\u0001\u0003\u0001\u0001\u0001\u0000\u0001\u0003\u0001\u0002\u000e\u0005\u0004\u0000\u0001\u0003\u0001(\u0001\u0013\u0001\u0000\u0001\u0004\u00010\u0001,\u0001\u0012\u0001\u001d\u0001\u001e\u0001\u0011\u0001.\u0001$\u0001\r\u0001\u000b\u0001\u0010\u0001\u0006\u0003\u000f\u0004\n\u0002\u0007\u0001+\u0001#\u0001'\u0001%\u0001&\u0001*\u0001\u0000\u0003\t\u00013\u0001\f\u00012\u0005\u0004\u00011\u000b\u0004\u0001\b\u0002\u0004\u0001!\u0001\u000e\u0001\"\u0001/\u0001\u0004\u0001\u0000\u0001\u0019\u00014\u0001\t\u00013\u0001\u0017\u0001\u0018\u0005\u0004\u0001\u001a\u0001\u0004\u0001\u001c\u0003\u0004\u0001\u0015\u0001\u001b\u0001\u0014\u0001\u0016\u0002\u0004\u0001\b\u0002\u0004\u0001\u001f\u0001-\u0001 \u0001)!\u0005\u0002\u0000\u0004\u0004\u0004\u0000\u0001\u0004\n\u0000\u0001\u0004\u0004\u0000\u0001\u0004\u0005\u0000\u0017\u0004\u0001\u0000\u001f\u0004\u0001\u0000Ĩ\u0004\u0002\u0000\u0012\u0004\u001c\u0000^\u0004\u0002\u0000\t\u0004\u0002\u0000\u0007\u0004\u000e\u0000\u0002\u0004\u000e\u0000\u0005\u0004\t\u0000\u0001\u0004\u0011\u0000O\u0005\u0011\u0000\u0003\u0005\u0017\u0000\u0001\u0004\u000b\u0000\u0001\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0014\u0004\u0001\u0000,\u0004\u0001\u0000\b\u0004\u0002\u0000\u001a\u0004\f\u0000\u0082\u0004\u0001\u0000\u0004\u0005\u0005\u00009\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0002\u0004\u0003\u0000&\u0004\u0002\u0000\u0002\u00047\u0000&\u0004\u0002\u0000\u0001\u0004\u0007\u0000'\u0004\t\u0000\u0011\u0005\u0001\u0000\u0017\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0001\u0005\u0001\u0000\u0002\u0005\u0001\u0000\u0001\u0005\u000b\u0000\u001b\u0004\u0005\u0000\u0003\u0004.\u0000\u001a\u0004\u0005\u0000\u000b\u0004\u000b\u0005\n\u0000\n\u0005\u0006\u0000\u0001\u0005c\u0004\u0001\u0000\u0001\u0004\u0007\u0005\u0002\u0000\u0006\u0005\u0002\u0004\u0002\u0005\u0001\u0000\u0004\u0005\u0002\u0000\n\u0005\u0003\u0004\u0012\u0000\u0001\u0005\u0001\u0004\u0001\u0005\u001b\u0004\u0003\u0000\u001b\u00055\u0000&\u0004\u000b\u0005Ő\u0000\u0003\u0005\u0001\u00005\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0010\u0005\u0002\u0000\u0001\u0004\u0004\u0005\u0003\u0000\n\u0004\u0002\u0005\u0002\u0000\n\u0005\u0011\u0000\u0003\u0005\u0001\u0000\b\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0004\u0004\u0002\u0000\u0001\u0005\u0001\u0000\u0007\u0005\u0002\u0000\u0002\u0005\u0002\u0000\u0003\u0005\t\u0000\u0001\u0005\u0004\u0000\u0002\u0004\u0001\u0000\u0003\u0004\u0002\u0005\u0002\u0000\n\u0005\u0004\u0004\u000e\u0000\u0001\u0005\u0002\u0000\u0006\u0004\u0004\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0002\u0000\u0001\u0005\u0001\u0000\u0005\u0005\u0004\u0000\u0002\u0005\u0002\u0000\u0003\u0005\u000b\u0000\u0004\u0004\u0001\u0000\u0001\u0004\u0007\u0000\f\u0005\u0003\u0004\f\u0000\u0003\u0005\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0005\u0004\u0002\u0000\u0001\u0005\u0001\u0004\b\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0003\u0005\u0002\u0000\u0001\u0004\u000f\u0000\u0001\u0004\u0005\u0000\n\u0005\u0011\u0000\u0003\u0005\u0001\u0000\b\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0002\u0000\u0004\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0006\u0005\u0003\u0000\u0002\u0005\u0002\u0000\u0003\u0005\b\u0000\u0002\u0005\u0004\u0000\u0002\u0004\u0001\u0000\u0003\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\u0006\u0004\u0003\u0000\u0003\u0004\u0001\u0000\u0004\u0004\u0003\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0003\u0000\u0002\u0004\u0003\u0000\u0003\u0004\u0003\u0000\b\u0004\u0001\u0000\u0003\u0004\u0004\u0000\u0005\u0005\u0003\u0000\u0003\u0005\u0001\u0000\u0004\u0005\t\u0000\u0001\u0005\u000f\u0000\t\u0005\u0011\u0000\u0003\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\n\u0004\u0001\u0000\u0005\u0004\u0004\u0000\u0007\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0004\u0005\u0007\u0000\u0002\u0005\t\u0000\u0002\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\n\u0004\u0001\u0000\u0005\u0004\u0004\u0000\u0007\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0004\u0005\u0007\u0000\u0002\u0005\u0007\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\u0010\u0004\u0004\u0000\u0006\u0005\u0002\u0000\u0003\u0005\u0001\u0000\u0004\u0005\t\u0000\u0001\u0005\b\u0000\u0002\u0004\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\u0012\u0004\u0003\u0000\u0018\u0004\u0001\u0000\t\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0007\u0004\u0003\u0000\u0001\u0005\u0004\u0000\u0006\u0005\u0001\u0000\u0001\u0005\u0001\u0000\b\u0005\u0012\u0000\u0002\u0005\r\u00000\u0004\u0001\u0005\u0002\u0004\u0007\u0005\u0004\u0000\b\u0004\b\u0005\u0001\u0000\n\u0005'\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0001\u0004\u0006\u0000\u0004\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0002\u0004\u0001\u0000\u0004\u0004\u0001\u0005\u0002\u0004\u0006\u0005\u0001\u0000\u0002\u0005\u0001\u0004\u0002\u0000\u0005\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0006\u0005\u0002\u0000\n\u0005\u0002\u0000\u0002\u0004\"\u0000\u0001\u0004\u0017\u0000\u0002\u0005\u0006\u0000\n\u0005\u000b\u0000\u0001\u0005\u0001\u0000\u0001\u0005\u0001\u0000\u0001\u0005\u0004\u0000\u0002\u0005\b\u0004\u0001\u0000\"\u0004\u0006\u0000\u0014\u0005\u0001\u0000\u0002\u0005\u0004\u0004\u0004\u0000\b\u0005\u0001\u0000$\u0005\t\u0000\u0001\u00059\u0000\"\u0004\u0001\u0000\u0005\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0007\u0005\u0003\u0000\u0004\u0005\u0006\u0000\n\u0005\u0006\u0000\u0006\u0004\u0004\u0005F\u0000&\u0004\n\u0000'\u0004\t\u0000Z\u0004\u0005\u0000D\u0004\u0005\u0000R\u0004\u0006\u0000\u0007\u0004\u0001\u0000?\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000'\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u001f\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0017\u0004\u0001\u0000\u001f\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000'\u0004\u0001\u0000\u0013\u0004\u000e\u0000\t\u0005.\u0000U\u0004\f\u0000ɬ\u0004\u0002\u0000\b\u0004\n\u0000\u001a\u0004\u0005\u0000K\u0004\u0095\u00004\u0004 \u0005\u0007\u0000\u0001\u0004\u0004\u0000\n\u0005!\u0000\u0004\u0005\u0001\u0000\n\u0005\u0006\u0000X\u0004\b\u0000)\u0004\u0001\u0005Ֆ\u0000\u009c\u0004\u0004\u0000Z\u0004\u0006\u0000\u0016\u0004\u0002\u0000\u0006\u0004\u0002\u0000&\u0004\u0002\u0000\u0006\u0004\u0002\u0000\b\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u001f\u0004\u0002\u00005\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0003\u0004\u0001\u0000\u0007\u0004\u0003\u0000\u0004\u0004\u0002\u0000\u0006\u0004\u0004\u0000\r\u0004\u0005\u0000\u0003\u0004\u0001\u0000\u0007\u0004\u000f\u0000\u0004\u0005\u001a\u0000\u0005\u0005\u0010\u0000\u0002\u0004)\u0000\u0006\u0005\u000f\u0000\u0001\u0004 \u0000\u0010\u0004 \u0000\r\u0005\u0004\u0000\u0001\u0005 \u0000\u0001\u0004\u0004\u0000\u0001\u0004\u0002\u0000\n\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0005\u0004\u0006\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0007\u0004&\u0000$\u0004ກ\u0000\u0003\u0004\u0019\u0000\t\u0004\u0006\u0005\u0001\u0000\u0005\u0004\u0002\u0000\u0003\u0004\u0006\u0000T\u0004\u0004\u0000\u0002\u0005\u0002\u0000\u0002\u0004\u0002\u0000^\u0004\u0006\u0000(\u0004\u0004\u0000^\u0004\u0011\u0000\u0018\u0004Ɉ\u0000ᦶ\u0004J\u0000冦\u0004Z\u0000ҍ\u0004ݳ\u0000\u2ba4\u0004⅜\u0000Į\u0004Ò\u0000\u0007\u0004\f\u0000\u0005\u0004\u0005\u0000\u0001\u0004\u0001\u0005\n\u0004\u0001\u0000\r\u0004\u0001\u0000\u0005\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0001\u0000l\u0004!\u0000ū\u0004\u0012\u0000@\u0004\u0002\u00006\u0004(\u0000\f\u0004$\u0000\u0004\u0005\u000f\u0000\u0002\u0004\u0018\u0000\u0003\u0004\u0019\u0000\u0001\u0004\u0006\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0087\u0004\u0002\u0000\u0001\u0005\u0004\u0000\u0001\u0004\u000b\u0000\n\u0005\u0007\u0000\u001a\u0004\u0004\u0000\u0001\u0004\u0001\u0000\u001a\u0004\n\u0000Z\u0004\u0003\u0000\u0006\u0004\u0002\u0000\u0006\u0004\u0002\u0000\u0006\u0004\u0002\u0000\u0003\u0004\u0003\u0000\u0002\u0004\u0003\u0000\u0002\u0004\u0012\u0000\u0003\u0005\u0004\u0000");
   private static final int[] yy_rowMap = new int[]{0, 53, 106, 159, 212, 265, 265, 318, 371, 424, 477, 530, 583, 636, 689, 265, 265, 742, 795, 848, 265, 265, 265, 265, 265, 265, 265, 265, 901, 954, 1007, 1060, 265, 265, 265, 1113, 1166, 1219, 1272, 1325, 1378, 265, 1431, 1484, 265, 1537, 265, 1590, 1643, 265, 1696, 265, 1749, 1802, 1855, 1908, 1961, 2014, 265, 265, 265, 265, 265, 265, 265, 265, 265, 2067, 2120, 2173, 265, 265, 2226, 265, 2279, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 2332, 2385, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 2438, 2491, 2544, 2597, 2650, 2703, 265, 2756, 265, 265, 265, 371, 2809, 371, 265, 371};
   private static final String yy_packed0 = "\u0001\u0006\u0001\u0007\u0001\b\u0001\u0007\u0001\t\u0001\u0006\u0001\n\u0001\u000b\u0002\t\u0001\u000b\u0001\f\u0001\t\u0001\r\u0001\u0006\u0001\u000b\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0003\t\u0001\u0013\u0003\t\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0017\u0001\u0018\u0001\u0019\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0001\"\u0001#\u0001$\u0001%\u0001&\u0001'\u0001(\u0004\t\u0001)\u0001*\u0001+\u000b)\u0001,\u0004)\u0001-!)\u0001.\u0001/\u00010\u000b.\u00011\u0003.\u00012\".\u0011\u0007\u00013$\u0007\u00014\u000152\u00076\u0000\u0001\u00077\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\t\t\u0014\u0000\u0004\t\u0006\u0000\u00016\u00017\u00018\u0001\u0000\u00016\u00019\u0001:\u0002\u0000\u00016\u0007\u0000\u0001:\u0001;\u0001\u0000\u0001<\u0016\u0000\u0001<\u0001;\u0001=\u0007\u0000\u0002\u000b\u0002\u0000\u0001\u000b\u00019\u0001:\u0002\u0000\u0001\u000b\u0007\u0000\u0001:\u0001;\u0001\u0000\u0001<\u0016\u0000\u0001<\u0001;\u0001=\u0007\u0000\u00029\u0002\u0000\u00019\u0004\u0000\u000192\u0000\u0001>\u0017\u0000\u0001?\u001f\u0000\u0001@\u0001A\u0013\u0000\u0001B4\u0000\u0001C\u0013\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0001\t\u0001D\u0007\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0005\t\u0001E\u0003\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0002\t\u0001F\u0006\t\u0014\u0000\u0004\t%\u0000\u0001G4\u0000\u0001H\u0001I3\u0000\u0001J\u0001\u0000\u0001K2\u0000\u0001L4\u0000\u0001M\u0006\u0000\u0001N-\u0000\u0001O\u0007\u0000\u0001P,\u0000\u0001Q\b\u0000\u0001R+\u0000\u0001S4\u0000\u0001T\u000f\u0000\u0001)\u0002\u0000\u000b)\u0001\u0000\u0004)\u0001\u0000!)\u0001\u0000\u0001*3\u0000\u0001U\u0001\u0000\u0004U\u0001V\u0003U\u0001W\u0003U\u0001X\u0001V\u0002U\u0001Y\u0001Z\u0001[\u0001\\\u0002U\u0001]\u0003U\u0001^\u0017U\u0001_\u0001.\u0002\u0000\u000b.\u0001\u0000\u0003.\u0001\u0000\".\u0001\u0000\u0001/3\u0000\u0001U\u0001\u0000\u0004U\u0001V\u0003U\u0001W\u0003U\u0001`\u0001V\u0002U\u0001a\u0001b\u0001c\u0001d\u0002U\u0001e\u0003U\u0001f\u0017U\u0001g\u0010\u0000\u00014%\u0000\u000149\u0000\u00016\u00017\u0002\u0000\u00016\u00019\u0001:\u0002\u0000\u00016\u0007\u0000\u0001:\u0002\u0000\u0001h\u0016\u0000\u0001h\t\u0000\u00027\u0002\u0000\u00017\u00019\u0001:\u0002\u0000\u00017\u0007\u0000\u0001:#\u0000\u0002i\u0001\u0000\u0002i\u0001\u0000\u0001i\u0002\u0000\u0001i\u0007\u0000\u0003i\u0018\u0000\u0003i\u0006\u0000\u00029\u0002\u0000\u00019\u0001\u0000\u0001:\u0002\u0000\u00019\u0007\u0000\u0001:\u0001;\u0019\u0000\u0001;\u0001=\u0007\u0000\u0002j\u0002\u0000\u0001j\u0002\u0000\u0001k\u0001\u0000\u0001j\u001e\u0000\u0001k\n\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0002\t\u0001l\u0006\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0006\t\u0001m\u0002\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0006\t\u0001n\u0002\t\u0014\u0000\u0004\t%\u0000\u0001o\u0001p3\u0000\u0001q\u0015\u0000\u0001W\u0003\u0000\u0001W\u0004\u0000\u0001W+\u0000\u0001r\u0003\u0000\u0001r\u0004\u0000\u0001r+\u0000\u0002i\u0001\u0000\u0002i\u0001\u0000\u0001i\u0002\u0000\u0001i\u0007\u0000\u0003i\u0001s\u0016\u0000\u0001s\u0003i\u0006\u0000\u0002j\u0002\u0000\u0001j\u0004\u0000\u0001j\b\u0000\u0001;\u0019\u0000\u0001;\u0001=\u0007\u0000\u0002j\u0002\u0000\u0001j\u0004\u0000\u0001j)\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0003\t\u0001t\u0005\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0007\t\u0001u\u0001\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0006\t\u0001v\u0002\t\u0014\u0000\u0004\t%\u0000\u0001w\u0013\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0003\t\u0001x\u0005\t\u0014\u0000\u0004\t";
   private static final int[] yytrans = yy_unpack();
   private static final int YY_UNKNOWN_ERROR = 0;
   private static final int YY_ILLEGAL_STATE = 1;
   private static final int YY_NO_MATCH = 2;
   private static final int YY_PUSHBACK_2BIG = 3;
   private static final String[] YY_ERROR_MSG = new String[]{"Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large"};
   private static final byte[] YY_ATTRIBUTE = new byte[]{0, 0, 0, 0, 0, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 9, 9, 9, 1, 1, 1, 1, 1, 1, 9, 1, 1, 9, 1, 9, 1, 1, 9, 1, 9, 1, 1, 0, 0, 1, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 9, 9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 0, 1, 1, 1, 9, 1, 9, 9, 9, 1, 1, 1, 9, 1};
   private Reader yy_reader;
   private int yy_state;
   private int yy_lexical_state;
   private char[] yy_buffer;
   private int yy_markedPos;
   private int yy_pushbackPos;
   private int yy_currentPos;
   private int yy_startRead;
   private int yy_endRead;
   private int yyline;
   private int yychar;
   private int yycolumn;
   private boolean yy_atBOL;
   private boolean yy_atEOF;
   StringBuffer sb;
   String file;
   ErrorQueue eq;
   HashMap keywords;

   public Lexer_c(InputStream in, String file, ErrorQueue eq) {
      this((Reader)(new BufferedReader(new InputStreamReader(in))), file, eq);
   }

   public Lexer_c(Reader reader, String file, ErrorQueue eq) {
      this((Reader)(new EscapedUnicodeReader(reader)));
      this.file = file;
      this.eq = eq;
      this.keywords = new HashMap();
      this.init_keywords();
   }

   protected void init_keywords() {
      this.keywords.put("abstract", new Integer(29));
      this.keywords.put("assert", new Integer(105));
      this.keywords.put("boolean", new Integer(2));
      this.keywords.put("break", new Integer(51));
      this.keywords.put("byte", new Integer(3));
      this.keywords.put("case", new Integer(46));
      this.keywords.put("catch", new Integer(56));
      this.keywords.put("char", new Integer(7));
      this.keywords.put("class", new Integer(35));
      this.keywords.put("const", new Integer(106));
      this.keywords.put("continue", new Integer(52));
      this.keywords.put("default", new Integer(47));
      this.keywords.put("do", new Integer(48));
      this.keywords.put("double", new Integer(9));
      this.keywords.put("else", new Integer(44));
      this.keywords.put("extends", new Integer(36));
      this.keywords.put("final", new Integer(30));
      this.keywords.put("finally", new Integer(57));
      this.keywords.put("float", new Integer(8));
      this.keywords.put("for", new Integer(50));
      this.keywords.put("goto", new Integer(107));
      this.keywords.put("if", new Integer(43));
      this.keywords.put("implements", new Integer(37));
      this.keywords.put("import", new Integer(24));
      this.keywords.put("instanceof", new Integer(74));
      this.keywords.put("int", new Integer(5));
      this.keywords.put("interface", new Integer(42));
      this.keywords.put("long", new Integer(6));
      this.keywords.put("native", new Integer(31));
      this.keywords.put("new", new Integer(58));
      this.keywords.put("package", new Integer(23));
      this.keywords.put("private", new Integer(27));
      this.keywords.put("protected", new Integer(26));
      this.keywords.put("public", new Integer(25));
      this.keywords.put("return", new Integer(53));
      this.keywords.put("short", new Integer(4));
      this.keywords.put("static", new Integer(28));
      this.keywords.put("strictfp", new Integer(104));
      this.keywords.put("super", new Integer(41));
      this.keywords.put("switch", new Integer(45));
      this.keywords.put("synchronized", new Integer(32));
      this.keywords.put("this", new Integer(40));
      this.keywords.put("throw", new Integer(54));
      this.keywords.put("throws", new Integer(39));
      this.keywords.put("transient", new Integer(33));
      this.keywords.put("try", new Integer(55));
      this.keywords.put("void", new Integer(38));
      this.keywords.put("volatile", new Integer(34));
      this.keywords.put("while", new Integer(49));
   }

   public String file() {
      return this.file;
   }

   private Position pos() {
      return new Position(this.file, this.yyline + 1, this.yycolumn, this.yyline + 1, this.yycolumn + this.yytext().length());
   }

   private Position pos(int len) {
      return new Position(this.file, this.yyline + 1, this.yycolumn - len - 1, this.yyline + 1, this.yycolumn + 1);
   }

   private Token key(int symbol) {
      return new Keyword(this.pos(), this.yytext(), symbol);
   }

   private Token op(int symbol) {
      return new Operator(this.pos(), this.yytext(), symbol);
   }

   private Token id() {
      return new Identifier(this.pos(), this.yytext(), 12);
   }

   private Token int_lit(String s, int radix) {
      BigInteger x = new BigInteger(s, radix);
      boolean boundary = radix == 10 && s.equals("2147483648");
      int bits = radix == 10 ? 31 : 32;
      if (x.bitLength() > bits && !boundary) {
         this.eq.enqueue(3, "Integer literal \"" + this.yytext() + "\" out of range.", this.pos());
      }

      return new IntegerLiteral(this.pos(), x.intValue(), boundary ? 95 : 94);
   }

   private Token long_lit(String s, int radix) {
      BigInteger x = new BigInteger(s, radix);
      boolean boundary = radix == 10 && s.equals("9223372036854775808");
      int bits = radix == 10 ? 63 : 64;
      if (x.bitLength() > bits && !boundary) {
         this.eq.enqueue(3, "Long literal \"" + this.yytext() + "\" out of range.", this.pos());
      }

      return new LongLiteral(this.pos(), x.longValue(), boundary ? 97 : 96);
   }

   private Token float_lit(String s) {
      try {
         Float x = Float.valueOf(s);
         boolean zero = true;

         for(int i = 0; i < s.length(); ++i) {
            if ('1' <= s.charAt(i) && s.charAt(i) <= '9') {
               zero = false;
               break;
            }

            if (s.charAt(i) == 'e' || s.charAt(i) == 'E') {
               break;
            }
         }

         if (x.isInfinite() || x.isNaN() || x == 0.0F && !zero) {
            this.eq.enqueue(3, "Illegal float literal \"" + this.yytext() + "\"", this.pos());
         }

         return new FloatLiteral(this.pos(), x, 99);
      } catch (NumberFormatException var5) {
         this.eq.enqueue(3, "Illegal float literal \"" + this.yytext() + "\"", this.pos());
         return new FloatLiteral(this.pos(), 0.0F, 99);
      }
   }

   private Token double_lit(String s) {
      try {
         Double x = Double.valueOf(s);
         boolean zero = true;

         for(int i = 0; i < s.length(); ++i) {
            if ('1' <= s.charAt(i) && s.charAt(i) <= '9') {
               zero = false;
               break;
            }

            if (s.charAt(i) == 'e' || s.charAt(i) == 'E') {
               break;
            }
         }

         if (x.isInfinite() || x.isNaN() || x == 0.0D && !zero) {
            this.eq.enqueue(3, "Illegal double literal \"" + this.yytext() + "\"", this.pos());
         }

         return new DoubleLiteral(this.pos(), x, 98);
      } catch (NumberFormatException var5) {
         this.eq.enqueue(3, "Illegal double literal \"" + this.yytext() + "\"", this.pos());
         return new DoubleLiteral(this.pos(), 0.0D, 98);
      }
   }

   private Token char_lit(String s) {
      if (s.length() == 1) {
         char x = s.charAt(0);
         return new CharacterLiteral(this.pos(), x, 101);
      } else {
         this.eq.enqueue(3, "Illegal character literal '" + s + "'", this.pos(s.length()));
         return new CharacterLiteral(this.pos(), '\u0000', 101);
      }
   }

   private Token boolean_lit(boolean x) {
      return new BooleanLiteral(this.pos(), x, 100);
   }

   private Token null_lit() {
      return new NullLiteral(this.pos(), 103);
   }

   private Token string_lit() {
      return new StringLiteral(this.pos(this.sb.length()), this.sb.toString(), 102);
   }

   private String chop(int i, int j) {
      return this.yytext().substring(i, this.yylength() - j);
   }

   private String chop(int j) {
      return this.chop(0, j);
   }

   private String chop() {
      return this.chop(0, 1);
   }

   public Lexer_c(Reader in) {
      this.yy_lexical_state = 0;
      this.yy_buffer = new char[16384];
      this.yy_atBOL = true;
      this.sb = new StringBuffer();
      this.yy_reader = in;
   }

   public Lexer_c(InputStream in) {
      this((Reader)(new InputStreamReader(in)));
   }

   private static int[] yy_unpack() {
      int[] trans = new int[2862];
      int offset = 0;
      yy_unpack("\u0001\u0006\u0001\u0007\u0001\b\u0001\u0007\u0001\t\u0001\u0006\u0001\n\u0001\u000b\u0002\t\u0001\u000b\u0001\f\u0001\t\u0001\r\u0001\u0006\u0001\u000b\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0003\t\u0001\u0013\u0003\t\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0017\u0001\u0018\u0001\u0019\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0001\"\u0001#\u0001$\u0001%\u0001&\u0001'\u0001(\u0004\t\u0001)\u0001*\u0001+\u000b)\u0001,\u0004)\u0001-!)\u0001.\u0001/\u00010\u000b.\u00011\u0003.\u00012\".\u0011\u0007\u00013$\u0007\u00014\u000152\u00076\u0000\u0001\u00077\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\t\t\u0014\u0000\u0004\t\u0006\u0000\u00016\u00017\u00018\u0001\u0000\u00016\u00019\u0001:\u0002\u0000\u00016\u0007\u0000\u0001:\u0001;\u0001\u0000\u0001<\u0016\u0000\u0001<\u0001;\u0001=\u0007\u0000\u0002\u000b\u0002\u0000\u0001\u000b\u00019\u0001:\u0002\u0000\u0001\u000b\u0007\u0000\u0001:\u0001;\u0001\u0000\u0001<\u0016\u0000\u0001<\u0001;\u0001=\u0007\u0000\u00029\u0002\u0000\u00019\u0004\u0000\u000192\u0000\u0001>\u0017\u0000\u0001?\u001f\u0000\u0001@\u0001A\u0013\u0000\u0001B4\u0000\u0001C\u0013\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0001\t\u0001D\u0007\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0005\t\u0001E\u0003\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0002\t\u0001F\u0006\t\u0014\u0000\u0004\t%\u0000\u0001G4\u0000\u0001H\u0001I3\u0000\u0001J\u0001\u0000\u0001K2\u0000\u0001L4\u0000\u0001M\u0006\u0000\u0001N-\u0000\u0001O\u0007\u0000\u0001P,\u0000\u0001Q\b\u0000\u0001R+\u0000\u0001S4\u0000\u0001T\u000f\u0000\u0001)\u0002\u0000\u000b)\u0001\u0000\u0004)\u0001\u0000!)\u0001\u0000\u0001*3\u0000\u0001U\u0001\u0000\u0004U\u0001V\u0003U\u0001W\u0003U\u0001X\u0001V\u0002U\u0001Y\u0001Z\u0001[\u0001\\\u0002U\u0001]\u0003U\u0001^\u0017U\u0001_\u0001.\u0002\u0000\u000b.\u0001\u0000\u0003.\u0001\u0000\".\u0001\u0000\u0001/3\u0000\u0001U\u0001\u0000\u0004U\u0001V\u0003U\u0001W\u0003U\u0001`\u0001V\u0002U\u0001a\u0001b\u0001c\u0001d\u0002U\u0001e\u0003U\u0001f\u0017U\u0001g\u0010\u0000\u00014%\u0000\u000149\u0000\u00016\u00017\u0002\u0000\u00016\u00019\u0001:\u0002\u0000\u00016\u0007\u0000\u0001:\u0002\u0000\u0001h\u0016\u0000\u0001h\t\u0000\u00027\u0002\u0000\u00017\u00019\u0001:\u0002\u0000\u00017\u0007\u0000\u0001:#\u0000\u0002i\u0001\u0000\u0002i\u0001\u0000\u0001i\u0002\u0000\u0001i\u0007\u0000\u0003i\u0018\u0000\u0003i\u0006\u0000\u00029\u0002\u0000\u00019\u0001\u0000\u0001:\u0002\u0000\u00019\u0007\u0000\u0001:\u0001;\u0019\u0000\u0001;\u0001=\u0007\u0000\u0002j\u0002\u0000\u0001j\u0002\u0000\u0001k\u0001\u0000\u0001j\u001e\u0000\u0001k\n\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0002\t\u0001l\u0006\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0006\t\u0001m\u0002\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0006\t\u0001n\u0002\t\u0014\u0000\u0004\t%\u0000\u0001o\u0001p3\u0000\u0001q\u0015\u0000\u0001W\u0003\u0000\u0001W\u0004\u0000\u0001W+\u0000\u0001r\u0003\u0000\u0001r\u0004\u0000\u0001r+\u0000\u0002i\u0001\u0000\u0002i\u0001\u0000\u0001i\u0002\u0000\u0001i\u0007\u0000\u0003i\u0001s\u0016\u0000\u0001s\u0003i\u0006\u0000\u0002j\u0002\u0000\u0001j\u0004\u0000\u0001j\b\u0000\u0001;\u0019\u0000\u0001;\u0001=\u0007\u0000\u0002j\u0002\u0000\u0001j\u0004\u0000\u0001j)\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0003\t\u0001t\u0005\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0007\t\u0001u\u0001\t\u0014\u0000\u0004\t\u0004\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0006\t\u0001v\u0002\t\u0014\u0000\u0004\t%\u0000\u0001w\u0013\u0000\u0007\t\u0001\u0000\u0001\t\u0002\u0000\u0001\t\u0004\u0000\u0003\t\u0001x\u0005\t\u0014\u0000\u0004\t", offset, trans);
      return trans;
   }

   private static int yy_unpack(String packed, int offset, int[] trans) {
      int i = 0;
      int j = offset;
      int l = packed.length();

      while(i < l) {
         int count = packed.charAt(i++);
         int value = packed.charAt(i++);
         int value = value - 1;

         while(true) {
            trans[j++] = value;
            --count;
            if (count <= 0) {
               break;
            }
         }
      }

      return j;
   }

   private static char[] yy_unpack_cmap(String packed) {
      char[] map = new char[65536];
      int i = 0;
      int var3 = 0;

      while(i < 1642) {
         int count = packed.charAt(i++);
         char value = packed.charAt(i++);

         while(true) {
            map[var3++] = value;
            --count;
            if (count <= 0) {
               break;
            }
         }
      }

      return map;
   }

   private boolean yy_refill() throws IOException {
      if (this.yy_startRead > 0) {
         System.arraycopy(this.yy_buffer, this.yy_startRead, this.yy_buffer, 0, this.yy_endRead - this.yy_startRead);
         this.yy_endRead -= this.yy_startRead;
         this.yy_currentPos -= this.yy_startRead;
         this.yy_markedPos -= this.yy_startRead;
         this.yy_pushbackPos -= this.yy_startRead;
         this.yy_startRead = 0;
      }

      if (this.yy_currentPos >= this.yy_buffer.length) {
         char[] newBuffer = new char[this.yy_currentPos * 2];
         System.arraycopy(this.yy_buffer, 0, newBuffer, 0, this.yy_buffer.length);
         this.yy_buffer = newBuffer;
      }

      int numRead = this.yy_reader.read(this.yy_buffer, this.yy_endRead, this.yy_buffer.length - this.yy_endRead);
      if (numRead < 0) {
         return true;
      } else {
         this.yy_endRead += numRead;
         return false;
      }
   }

   public final void yyclose() throws IOException {
      this.yy_atEOF = true;
      this.yy_endRead = this.yy_startRead;
      if (this.yy_reader != null) {
         this.yy_reader.close();
      }

   }

   public final void yyreset(Reader reader) throws IOException {
      this.yyclose();
      this.yy_reader = reader;
      this.yy_atBOL = true;
      this.yy_atEOF = false;
      this.yy_endRead = this.yy_startRead = 0;
      this.yy_currentPos = this.yy_markedPos = this.yy_pushbackPos = 0;
      this.yyline = this.yychar = this.yycolumn = 0;
      this.yy_lexical_state = 0;
   }

   public final int yystate() {
      return this.yy_lexical_state;
   }

   public final void yybegin(int newState) {
      this.yy_lexical_state = newState;
   }

   public final String yytext() {
      return new String(this.yy_buffer, this.yy_startRead, this.yy_markedPos - this.yy_startRead);
   }

   public final char yycharat(int pos) {
      return this.yy_buffer[this.yy_startRead + pos];
   }

   public final int yylength() {
      return this.yy_markedPos - this.yy_startRead;
   }

   private void yy_ScanError(int errorCode) {
      String message;
      try {
         message = YY_ERROR_MSG[errorCode];
      } catch (ArrayIndexOutOfBoundsException var4) {
         message = YY_ERROR_MSG[0];
      }

      throw new Error(message);
   }

   private void yypushback(int number) {
      if (number > this.yylength()) {
         this.yy_ScanError(3);
      }

      this.yy_markedPos -= number;
   }

   public Token nextToken() throws IOException {
      int yy_endRead_l = this.yy_endRead;
      char[] yy_buffer_l = this.yy_buffer;
      char[] yycmap_l = yycmap;
      int[] yytrans_l = yytrans;
      int[] yy_rowMap_l = yy_rowMap;
      byte[] yy_attr_l = YY_ATTRIBUTE;

      while(true) {
         int yy_markedPos_l = this.yy_markedPos;
         boolean yy_r = false;

         int yy_currentPos_l;
         for(yy_currentPos_l = this.yy_startRead; yy_currentPos_l < yy_markedPos_l; ++yy_currentPos_l) {
            switch(yy_buffer_l[yy_currentPos_l]) {
            case '\n':
               if (yy_r) {
                  yy_r = false;
               } else {
                  ++this.yyline;
                  this.yycolumn = 0;
               }
               break;
            case '\u000b':
            case '\f':
            case '\u0085':
            case '\u2028':
            case '\u2029':
               ++this.yyline;
               this.yycolumn = 0;
               yy_r = false;
               break;
            case '\r':
               ++this.yyline;
               this.yycolumn = 0;
               yy_r = true;
               break;
            default:
               yy_r = false;
               ++this.yycolumn;
            }
         }

         boolean eof;
         if (yy_r) {
            if (yy_markedPos_l < yy_endRead_l) {
               eof = yy_buffer_l[yy_markedPos_l] == '\n';
            } else if (this.yy_atEOF) {
               eof = false;
            } else {
               boolean eof = this.yy_refill();
               yy_markedPos_l = this.yy_markedPos;
               yy_buffer_l = this.yy_buffer;
               if (eof) {
                  eof = false;
               } else {
                  eof = yy_buffer_l[yy_markedPos_l] == '\n';
               }
            }

            if (eof) {
               --this.yyline;
            }
         }

         int yy_action = -1;
         yy_currentPos_l = this.yy_currentPos = this.yy_startRead = yy_markedPos_l;
         this.yy_state = this.yy_lexical_state;

         int yy_input;
         int x;
         while(true) {
            if (yy_currentPos_l < yy_endRead_l) {
               yy_input = yy_buffer_l[yy_currentPos_l++];
            } else {
               if (this.yy_atEOF) {
                  yy_input = -1;
                  break;
               }

               this.yy_currentPos = yy_currentPos_l;
               this.yy_markedPos = yy_markedPos_l;
               eof = this.yy_refill();
               yy_currentPos_l = this.yy_currentPos;
               yy_markedPos_l = this.yy_markedPos;
               yy_buffer_l = this.yy_buffer;
               yy_endRead_l = this.yy_endRead;
               if (eof) {
                  yy_input = -1;
                  break;
               }

               yy_input = yy_buffer_l[yy_currentPos_l++];
            }

            x = yytrans_l[yy_rowMap_l[this.yy_state] + yycmap_l[yy_input]];
            if (x == -1) {
               break;
            }

            this.yy_state = x;
            int yy_attributes = yy_attr_l[this.yy_state];
            if ((yy_attributes & 1) == 1) {
               yy_action = this.yy_state;
               yy_markedPos_l = yy_currentPos_l;
               if ((yy_attributes & 8) == 8) {
                  break;
               }
            }
         }

         this.yy_markedPos = yy_markedPos_l;
         switch(yy_action) {
         case 5:
         case 43:
         case 48:
            this.eq.enqueue(3, "Illegal character \"" + this.yytext() + "\"", this.pos());
         case 6:
         case 7:
         case 50:
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
         case 193:
         case 194:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         case 200:
         case 201:
         case 202:
         case 203:
         case 204:
         case 205:
         case 206:
         case 207:
         case 208:
         case 209:
         case 210:
         case 211:
            break;
         case 8:
         case 17:
         case 18:
         case 19:
         case 67:
         case 68:
         case 69:
         case 107:
         case 108:
         case 109:
         case 116:
            Integer i = (Integer)this.keywords.get(this.yytext());
            if (i == null) {
               return this.id();
            }

            return this.key(i);
         case 9:
         case 10:
            return this.int_lit(this.yytext(), 10);
         case 11:
            return this.op(13);
         case 12:
            return this.op(62);
         case 13:
            return this.op(65);
         case 14:
            return this.op(15);
         case 15:
            this.yybegin(2);
            this.sb.setLength(0);
            break;
         case 16:
            this.yybegin(1);
            this.sb.setLength(0);
            break;
         case 20:
            return this.op(20);
         case 21:
            return this.op(21);
         case 22:
            return this.op(17);
         case 23:
            return this.op(18);
         case 24:
            return this.op(10);
         case 25:
            return this.op(11);
         case 26:
            return this.op(14);
         case 27:
            return this.op(16);
         case 28:
            return this.op(19);
         case 29:
            return this.op(71);
         case 30:
            return this.op(70);
         case 31:
            return this.op(64);
         case 32:
            return this.op(63);
         case 33:
            return this.op(82);
         case 34:
            return this.op(22);
         case 35:
            return this.op(77);
         case 36:
            return this.op(79);
         case 37:
            return this.op(61);
         case 38:
            return this.op(78);
         case 39:
            return this.op(66);
         case 40:
            this.sb.append(this.yytext());
            break;
         case 41:
         case 42:
            this.yybegin(0);
            this.eq.enqueue(3, "Unclosed string literal", this.pos(this.sb.length()));
            break;
         case 44:
            this.yybegin(0);
            return this.string_lit();
         case 45:
            this.sb.append(this.yytext());
            break;
         case 46:
         case 47:
            this.yybegin(0);
            this.eq.enqueue(3, "Unclosed character literal", this.pos(this.sb.length()));
            break;
         case 49:
            this.yybegin(0);
            return this.char_lit(this.sb.toString());
         case 51:
         case 52:
            this.yybegin(0);
            break;
         case 53:
            return this.int_lit(this.yytext(), 8);
         case 54:
         case 55:
         case 57:
         case 106:
         case 120:
         default:
            if (yy_input == -1 && this.yy_startRead == this.yy_currentPos) {
               this.yy_atEOF = true;
               return new EOF(this.pos(), 0);
            }

            this.yy_ScanError(2);
            break;
         case 56:
         case 105:
            return this.double_lit(this.yytext());
         case 58:
            return this.float_lit(this.chop());
         case 59:
            return this.long_lit(this.chop(), 10);
         case 60:
            return this.double_lit(this.chop());
         case 61:
            return this.op(60);
         case 62:
            return this.op(87);
         case 63:
            this.yybegin(4);
            break;
         case 64:
            this.yybegin(3);
            break;
         case 65:
            return this.op(84);
         case 66:
            return this.op(83);
         case 70:
            return this.op(75);
         case 71:
            return this.op(73);
         case 72:
            return this.op(68);
         case 73:
            return this.op(72);
         case 74:
            return this.op(67);
         case 75:
            return this.op(76);
         case 76:
            return this.op(91);
         case 77:
            return this.op(80);
         case 78:
            return this.op(93);
         case 79:
            return this.op(81);
         case 80:
            return this.op(86);
         case 81:
            return this.op(59);
         case 82:
            return this.op(92);
         case 83:
            return this.op(85);
         case 84:
            this.eq.enqueue(3, "Illegal escape character \"" + this.yytext() + "\"", this.pos());
            break;
         case 85:
         case 86:
         case 113:
            try {
               x = Integer.parseInt(this.chop(1, 0), 8);
               this.sb.append((char)x);
            } catch (NumberFormatException var15) {
               this.eq.enqueue(3, "Illegal octal escape \"" + this.yytext() + "\"", this.pos());
            }
            break;
         case 87:
            this.sb.append('\\');
            break;
         case 88:
            this.sb.append('\'');
            break;
         case 89:
            this.sb.append('"');
            break;
         case 90:
            this.sb.append('\t');
            break;
         case 91:
            this.sb.append('\r');
            break;
         case 92:
            this.sb.append('\f');
            break;
         case 93:
            this.sb.append('\n');
            break;
         case 94:
            this.sb.append('\b');
            break;
         case 95:
            this.sb.append('\\');
            break;
         case 96:
            this.sb.append('\'');
            break;
         case 97:
            this.sb.append('"');
            break;
         case 98:
            this.sb.append('\t');
            break;
         case 99:
            this.sb.append('\r');
            break;
         case 100:
            this.sb.append('\f');
            break;
         case 101:
            this.sb.append('\n');
            break;
         case 102:
            this.sb.append('\b');
            break;
         case 103:
            return this.long_lit(this.chop(), 8);
         case 104:
            return this.int_lit(this.chop(2, 0), 16);
         case 110:
            return this.op(89);
         case 111:
            return this.op(69);
         case 112:
            return this.op(88);
         case 114:
            return this.long_lit(this.chop(2, 1), 16);
         case 115:
            return this.boolean_lit(true);
         case 117:
            return this.null_lit();
         case 118:
            return this.op(90);
         case 119:
            return this.boolean_lit(false);
         }
      }
   }
}
