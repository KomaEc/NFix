package soot.jimple.parser.lexer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PushbackReader;
import soot.jimple.parser.node.EOF;
import soot.jimple.parser.node.TAbstract;
import soot.jimple.parser.node.TAnd;
import soot.jimple.parser.node.TAnnotation;
import soot.jimple.parser.node.TAtIdentifier;
import soot.jimple.parser.node.TBoolConstant;
import soot.jimple.parser.node.TBoolean;
import soot.jimple.parser.node.TBreakpoint;
import soot.jimple.parser.node.TByte;
import soot.jimple.parser.node.TCase;
import soot.jimple.parser.node.TCatch;
import soot.jimple.parser.node.TChar;
import soot.jimple.parser.node.TClass;
import soot.jimple.parser.node.TCls;
import soot.jimple.parser.node.TCmp;
import soot.jimple.parser.node.TCmpeq;
import soot.jimple.parser.node.TCmpg;
import soot.jimple.parser.node.TCmpge;
import soot.jimple.parser.node.TCmpgt;
import soot.jimple.parser.node.TCmpl;
import soot.jimple.parser.node.TCmple;
import soot.jimple.parser.node.TCmplt;
import soot.jimple.parser.node.TCmpne;
import soot.jimple.parser.node.TColon;
import soot.jimple.parser.node.TColonEquals;
import soot.jimple.parser.node.TComma;
import soot.jimple.parser.node.TDefault;
import soot.jimple.parser.node.TDiv;
import soot.jimple.parser.node.TDot;
import soot.jimple.parser.node.TDouble;
import soot.jimple.parser.node.TDynamicinvoke;
import soot.jimple.parser.node.TEntermonitor;
import soot.jimple.parser.node.TEnum;
import soot.jimple.parser.node.TEquals;
import soot.jimple.parser.node.TExitmonitor;
import soot.jimple.parser.node.TExtends;
import soot.jimple.parser.node.TFinal;
import soot.jimple.parser.node.TFloat;
import soot.jimple.parser.node.TFloatConstant;
import soot.jimple.parser.node.TFrom;
import soot.jimple.parser.node.TFullIdentifier;
import soot.jimple.parser.node.TGoto;
import soot.jimple.parser.node.TIdentifier;
import soot.jimple.parser.node.TIf;
import soot.jimple.parser.node.TIgnored;
import soot.jimple.parser.node.TImplements;
import soot.jimple.parser.node.TInstanceof;
import soot.jimple.parser.node.TInt;
import soot.jimple.parser.node.TIntegerConstant;
import soot.jimple.parser.node.TInterface;
import soot.jimple.parser.node.TInterfaceinvoke;
import soot.jimple.parser.node.TLBrace;
import soot.jimple.parser.node.TLBracket;
import soot.jimple.parser.node.TLParen;
import soot.jimple.parser.node.TLengthof;
import soot.jimple.parser.node.TLong;
import soot.jimple.parser.node.TLookupswitch;
import soot.jimple.parser.node.TMinus;
import soot.jimple.parser.node.TMod;
import soot.jimple.parser.node.TMult;
import soot.jimple.parser.node.TNative;
import soot.jimple.parser.node.TNeg;
import soot.jimple.parser.node.TNew;
import soot.jimple.parser.node.TNewarray;
import soot.jimple.parser.node.TNewmultiarray;
import soot.jimple.parser.node.TNop;
import soot.jimple.parser.node.TNull;
import soot.jimple.parser.node.TNullType;
import soot.jimple.parser.node.TOr;
import soot.jimple.parser.node.TPlus;
import soot.jimple.parser.node.TPrivate;
import soot.jimple.parser.node.TProtected;
import soot.jimple.parser.node.TPublic;
import soot.jimple.parser.node.TQuote;
import soot.jimple.parser.node.TQuotedName;
import soot.jimple.parser.node.TRBrace;
import soot.jimple.parser.node.TRBracket;
import soot.jimple.parser.node.TRParen;
import soot.jimple.parser.node.TRet;
import soot.jimple.parser.node.TReturn;
import soot.jimple.parser.node.TSemicolon;
import soot.jimple.parser.node.TShl;
import soot.jimple.parser.node.TShort;
import soot.jimple.parser.node.TShr;
import soot.jimple.parser.node.TSpecialinvoke;
import soot.jimple.parser.node.TStatic;
import soot.jimple.parser.node.TStaticinvoke;
import soot.jimple.parser.node.TStrictfp;
import soot.jimple.parser.node.TStringConstant;
import soot.jimple.parser.node.TSynchronized;
import soot.jimple.parser.node.TTableswitch;
import soot.jimple.parser.node.TThrow;
import soot.jimple.parser.node.TThrows;
import soot.jimple.parser.node.TTo;
import soot.jimple.parser.node.TTransient;
import soot.jimple.parser.node.TUnknown;
import soot.jimple.parser.node.TUshr;
import soot.jimple.parser.node.TVirtualinvoke;
import soot.jimple.parser.node.TVoid;
import soot.jimple.parser.node.TVolatile;
import soot.jimple.parser.node.TWith;
import soot.jimple.parser.node.TXor;
import soot.jimple.parser.node.Token;

public class Lexer {
   protected Token token;
   protected Lexer.State state;
   private PushbackReader in;
   private int line;
   private int pos;
   private boolean cr;
   private boolean eof;
   private final StringBuffer text;
   private static int[][][][] gotoTable;
   private static int[][] accept;

   protected void filter() throws LexerException, IOException {
   }

   public Lexer(PushbackReader in) {
      this.state = Lexer.State.INITIAL;
      this.text = new StringBuffer();
      this.in = in;
   }

   public Token peek() throws LexerException, IOException {
      while(this.token == null) {
         this.token = this.getToken();
         this.filter();
      }

      return this.token;
   }

   public Token next() throws LexerException, IOException {
      while(this.token == null) {
         this.token = this.getToken();
         this.filter();
      }

      Token result = this.token;
      this.token = null;
      return result;
   }

   protected Token getToken() throws IOException, LexerException {
      int dfa_state = 0;
      int start_pos = this.pos;
      int start_line = this.line;
      int accept_state = -1;
      int accept_token = -1;
      int accept_length = -1;
      int accept_pos = -1;
      int accept_line = -1;
      int[][][] gotoTable = Lexer.gotoTable[this.state.id()];
      int[] accept = Lexer.accept[this.state.id()];
      this.text.setLength(0);

      while(true) {
         int c = this.getChar();
         if (c == -1) {
            dfa_state = -1;
         } else {
            switch(c) {
            case 10:
               if (this.cr) {
                  this.cr = false;
               } else {
                  ++this.line;
                  this.pos = 0;
               }
               break;
            case 13:
               ++this.line;
               this.pos = 0;
               this.cr = true;
               break;
            default:
               ++this.pos;
               this.cr = false;
            }

            this.text.append((char)c);

            do {
               int oldState = dfa_state < -1 ? -2 - dfa_state : dfa_state;
               dfa_state = -1;
               int[][] tmp1 = gotoTable[oldState];
               int low = 0;
               int high = tmp1.length - 1;

               while(low <= high) {
                  int middle = low + high >>> 1;
                  int[] tmp2 = tmp1[middle];
                  if (c < tmp2[0]) {
                     high = middle - 1;
                  } else {
                     if (c <= tmp2[1]) {
                        dfa_state = tmp2[2];
                        break;
                     }

                     low = middle + 1;
                  }
               }
            } while(dfa_state < -1);
         }

         if (dfa_state >= 0) {
            if (accept[dfa_state] != -1) {
               accept_state = dfa_state;
               accept_token = accept[dfa_state];
               accept_length = this.text.length();
               accept_pos = this.pos;
               accept_line = this.line;
            }
         } else {
            if (accept_state == -1) {
               if (this.text.length() > 0) {
                  throw new LexerException("[" + (start_line + 1) + "," + (start_pos + 1) + "] Unknown token: " + this.text);
               }

               EOF token = new EOF(start_line + 1, start_pos + 1);
               return token;
            }

            Token token;
            switch(accept_token) {
            case 0:
               token = this.new0(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 1:
               token = this.new1(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 2:
               token = this.new2(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 3:
               token = this.new3(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 4:
               token = this.new4(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 5:
               token = this.new5(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 6:
               token = this.new6(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 7:
               token = this.new7(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 8:
               token = this.new8(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 9:
               token = this.new9(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 10:
               token = this.new10(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 11:
               token = this.new11(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 12:
               token = this.new12(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 13:
               token = this.new13(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 14:
               token = this.new14(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 15:
               token = this.new15(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 16:
               token = this.new16(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 17:
               token = this.new17(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 18:
               token = this.new18(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 19:
               token = this.new19(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 20:
               token = this.new20(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 21:
               token = this.new21(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 22:
               token = this.new22(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 23:
               token = this.new23(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 24:
               token = this.new24(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 25:
               token = this.new25(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 26:
               token = this.new26(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 27:
               token = this.new27(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 28:
               token = this.new28(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 29:
               token = this.new29(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 30:
               token = this.new30(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 31:
               token = this.new31(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 32:
               token = this.new32(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 33:
               token = this.new33(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 34:
               token = this.new34(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 35:
               token = this.new35(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 36:
               token = this.new36(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 37:
               token = this.new37(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 38:
               token = this.new38(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 39:
               token = this.new39(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 40:
               token = this.new40(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 41:
               token = this.new41(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 42:
               token = this.new42(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 43:
               token = this.new43(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 44:
               token = this.new44(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 45:
               token = this.new45(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 46:
               token = this.new46(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 47:
               token = this.new47(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 48:
               token = this.new48(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 49:
               token = this.new49(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 50:
               token = this.new50(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 51:
               token = this.new51(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 52:
               token = this.new52(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 53:
               token = this.new53(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 54:
               token = this.new54(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 55:
               token = this.new55(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 56:
               token = this.new56(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 57:
               token = this.new57(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 58:
               token = this.new58(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 59:
               token = this.new59(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 60:
               token = this.new60(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 61:
               token = this.new61(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 62:
               token = this.new62(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 63:
               token = this.new63(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 64:
               token = this.new64(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 65:
               token = this.new65(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 66:
               token = this.new66(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 67:
               token = this.new67(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 68:
               token = this.new68(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 69:
               token = this.new69(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 70:
               token = this.new70(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 71:
               token = this.new71(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 72:
               token = this.new72(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 73:
               token = this.new73(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 74:
               token = this.new74(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 75:
               token = this.new75(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 76:
               token = this.new76(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 77:
               token = this.new77(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 78:
               token = this.new78(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 79:
               token = this.new79(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 80:
               token = this.new80(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 81:
               token = this.new81(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 82:
               token = this.new82(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 83:
               token = this.new83(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 84:
               token = this.new84(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 85:
               token = this.new85(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 86:
               token = this.new86(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 87:
               token = this.new87(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 88:
               token = this.new88(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 89:
               token = this.new89(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 90:
               token = this.new90(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 91:
               token = this.new91(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 92:
               token = this.new92(start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 93:
               token = this.new93(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 94:
               token = this.new94(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 95:
               token = this.new95(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 96:
               token = this.new96(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 97:
               token = this.new97(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 98:
               token = this.new98(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 99:
               token = this.new99(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            case 100:
               token = this.new100(this.getText(accept_length), start_line + 1, start_pos + 1);
               this.pushBack(accept_length);
               this.pos = accept_pos;
               this.line = accept_line;
               return token;
            }
         }
      }
   }

   Token new0(String text, int line, int pos) {
      return new TIgnored(text, line, pos);
   }

   Token new1(int line, int pos) {
      return new TAbstract(line, pos);
   }

   Token new2(int line, int pos) {
      return new TFinal(line, pos);
   }

   Token new3(int line, int pos) {
      return new TNative(line, pos);
   }

   Token new4(int line, int pos) {
      return new TPublic(line, pos);
   }

   Token new5(int line, int pos) {
      return new TProtected(line, pos);
   }

   Token new6(int line, int pos) {
      return new TPrivate(line, pos);
   }

   Token new7(int line, int pos) {
      return new TStatic(line, pos);
   }

   Token new8(int line, int pos) {
      return new TSynchronized(line, pos);
   }

   Token new9(int line, int pos) {
      return new TTransient(line, pos);
   }

   Token new10(int line, int pos) {
      return new TVolatile(line, pos);
   }

   Token new11(int line, int pos) {
      return new TStrictfp(line, pos);
   }

   Token new12(int line, int pos) {
      return new TEnum(line, pos);
   }

   Token new13(int line, int pos) {
      return new TAnnotation(line, pos);
   }

   Token new14(int line, int pos) {
      return new TClass(line, pos);
   }

   Token new15(int line, int pos) {
      return new TInterface(line, pos);
   }

   Token new16(int line, int pos) {
      return new TVoid(line, pos);
   }

   Token new17(int line, int pos) {
      return new TBoolean(line, pos);
   }

   Token new18(int line, int pos) {
      return new TByte(line, pos);
   }

   Token new19(int line, int pos) {
      return new TShort(line, pos);
   }

   Token new20(int line, int pos) {
      return new TChar(line, pos);
   }

   Token new21(int line, int pos) {
      return new TInt(line, pos);
   }

   Token new22(int line, int pos) {
      return new TLong(line, pos);
   }

   Token new23(int line, int pos) {
      return new TFloat(line, pos);
   }

   Token new24(int line, int pos) {
      return new TDouble(line, pos);
   }

   Token new25(int line, int pos) {
      return new TNullType(line, pos);
   }

   Token new26(int line, int pos) {
      return new TUnknown(line, pos);
   }

   Token new27(int line, int pos) {
      return new TExtends(line, pos);
   }

   Token new28(int line, int pos) {
      return new TImplements(line, pos);
   }

   Token new29(int line, int pos) {
      return new TBreakpoint(line, pos);
   }

   Token new30(int line, int pos) {
      return new TCase(line, pos);
   }

   Token new31(int line, int pos) {
      return new TCatch(line, pos);
   }

   Token new32(int line, int pos) {
      return new TCmp(line, pos);
   }

   Token new33(int line, int pos) {
      return new TCmpg(line, pos);
   }

   Token new34(int line, int pos) {
      return new TCmpl(line, pos);
   }

   Token new35(int line, int pos) {
      return new TDefault(line, pos);
   }

   Token new36(int line, int pos) {
      return new TEntermonitor(line, pos);
   }

   Token new37(int line, int pos) {
      return new TExitmonitor(line, pos);
   }

   Token new38(int line, int pos) {
      return new TGoto(line, pos);
   }

   Token new39(int line, int pos) {
      return new TIf(line, pos);
   }

   Token new40(int line, int pos) {
      return new TInstanceof(line, pos);
   }

   Token new41(int line, int pos) {
      return new TInterfaceinvoke(line, pos);
   }

   Token new42(int line, int pos) {
      return new TLengthof(line, pos);
   }

   Token new43(int line, int pos) {
      return new TLookupswitch(line, pos);
   }

   Token new44(int line, int pos) {
      return new TNeg(line, pos);
   }

   Token new45(int line, int pos) {
      return new TNew(line, pos);
   }

   Token new46(int line, int pos) {
      return new TNewarray(line, pos);
   }

   Token new47(int line, int pos) {
      return new TNewmultiarray(line, pos);
   }

   Token new48(int line, int pos) {
      return new TNop(line, pos);
   }

   Token new49(int line, int pos) {
      return new TRet(line, pos);
   }

   Token new50(int line, int pos) {
      return new TReturn(line, pos);
   }

   Token new51(int line, int pos) {
      return new TSpecialinvoke(line, pos);
   }

   Token new52(int line, int pos) {
      return new TStaticinvoke(line, pos);
   }

   Token new53(int line, int pos) {
      return new TDynamicinvoke(line, pos);
   }

   Token new54(int line, int pos) {
      return new TTableswitch(line, pos);
   }

   Token new55(int line, int pos) {
      return new TThrow(line, pos);
   }

   Token new56(int line, int pos) {
      return new TThrows(line, pos);
   }

   Token new57(int line, int pos) {
      return new TVirtualinvoke(line, pos);
   }

   Token new58(int line, int pos) {
      return new TNull(line, pos);
   }

   Token new59(int line, int pos) {
      return new TFrom(line, pos);
   }

   Token new60(int line, int pos) {
      return new TTo(line, pos);
   }

   Token new61(int line, int pos) {
      return new TWith(line, pos);
   }

   Token new62(int line, int pos) {
      return new TCls(line, pos);
   }

   Token new63(int line, int pos) {
      return new TComma(line, pos);
   }

   Token new64(int line, int pos) {
      return new TLBrace(line, pos);
   }

   Token new65(int line, int pos) {
      return new TRBrace(line, pos);
   }

   Token new66(int line, int pos) {
      return new TSemicolon(line, pos);
   }

   Token new67(int line, int pos) {
      return new TLBracket(line, pos);
   }

   Token new68(int line, int pos) {
      return new TRBracket(line, pos);
   }

   Token new69(int line, int pos) {
      return new TLParen(line, pos);
   }

   Token new70(int line, int pos) {
      return new TRParen(line, pos);
   }

   Token new71(int line, int pos) {
      return new TColon(line, pos);
   }

   Token new72(int line, int pos) {
      return new TDot(line, pos);
   }

   Token new73(int line, int pos) {
      return new TQuote(line, pos);
   }

   Token new74(int line, int pos) {
      return new TColonEquals(line, pos);
   }

   Token new75(int line, int pos) {
      return new TEquals(line, pos);
   }

   Token new76(int line, int pos) {
      return new TAnd(line, pos);
   }

   Token new77(int line, int pos) {
      return new TOr(line, pos);
   }

   Token new78(int line, int pos) {
      return new TXor(line, pos);
   }

   Token new79(int line, int pos) {
      return new TMod(line, pos);
   }

   Token new80(int line, int pos) {
      return new TCmpeq(line, pos);
   }

   Token new81(int line, int pos) {
      return new TCmpne(line, pos);
   }

   Token new82(int line, int pos) {
      return new TCmpgt(line, pos);
   }

   Token new83(int line, int pos) {
      return new TCmpge(line, pos);
   }

   Token new84(int line, int pos) {
      return new TCmplt(line, pos);
   }

   Token new85(int line, int pos) {
      return new TCmple(line, pos);
   }

   Token new86(int line, int pos) {
      return new TShl(line, pos);
   }

   Token new87(int line, int pos) {
      return new TShr(line, pos);
   }

   Token new88(int line, int pos) {
      return new TUshr(line, pos);
   }

   Token new89(int line, int pos) {
      return new TPlus(line, pos);
   }

   Token new90(int line, int pos) {
      return new TMinus(line, pos);
   }

   Token new91(int line, int pos) {
      return new TMult(line, pos);
   }

   Token new92(int line, int pos) {
      return new TDiv(line, pos);
   }

   Token new93(String text, int line, int pos) {
      return new TQuotedName(text, line, pos);
   }

   Token new94(String text, int line, int pos) {
      return new TFullIdentifier(text, line, pos);
   }

   Token new95(String text, int line, int pos) {
      return new TIdentifier(text, line, pos);
   }

   Token new96(String text, int line, int pos) {
      return new TAtIdentifier(text, line, pos);
   }

   Token new97(String text, int line, int pos) {
      return new TBoolConstant(text, line, pos);
   }

   Token new98(String text, int line, int pos) {
      return new TIntegerConstant(text, line, pos);
   }

   Token new99(String text, int line, int pos) {
      return new TFloatConstant(text, line, pos);
   }

   Token new100(String text, int line, int pos) {
      return new TStringConstant(text, line, pos);
   }

   private int getChar() throws IOException {
      if (this.eof) {
         return -1;
      } else {
         int result = this.in.read();
         if (result == -1) {
            this.eof = true;
         }

         return result;
      }
   }

   private void pushBack(int acceptLength) throws IOException {
      int length = this.text.length();

      for(int i = length - 1; i >= acceptLength; --i) {
         this.eof = false;
         this.in.unread(this.text.charAt(i));
      }

   }

   protected void unread(Token token) throws IOException {
      String text = token.getText();
      int length = text.length();

      for(int i = length - 1; i >= 0; --i) {
         this.eof = false;
         this.in.unread(text.charAt(i));
      }

      this.pos = token.getPos() - 1;
      this.line = token.getLine() - 1;
   }

   private String getText(int acceptLength) {
      StringBuffer s = new StringBuffer(acceptLength);

      for(int i = 0; i < acceptLength; ++i) {
         s.append(this.text.charAt(i));
      }

      return s.toString();
   }

   static {
      try {
         DataInputStream s = new DataInputStream(new BufferedInputStream(Lexer.class.getResourceAsStream("/lexer.dat")));
         int length = s.readInt();
         gotoTable = new int[length][][][];

         int i;
         int j;
         for(i = 0; i < gotoTable.length; ++i) {
            length = s.readInt();
            gotoTable[i] = new int[length][][];

            for(j = 0; j < gotoTable[i].length; ++j) {
               length = s.readInt();
               gotoTable[i][j] = new int[length][3];

               for(int k = 0; k < gotoTable[i][j].length; ++k) {
                  for(int l = 0; l < 3; ++l) {
                     gotoTable[i][j][k][l] = s.readInt();
                  }
               }
            }
         }

         length = s.readInt();
         accept = new int[length][];

         for(i = 0; i < accept.length; ++i) {
            length = s.readInt();
            accept[i] = new int[length];

            for(j = 0; j < accept[i].length; ++j) {
               accept[i][j] = s.readInt();
            }
         }

         s.close();
      } catch (Exception var6) {
         throw new RuntimeException("The file \"lexer.dat\" is either missing or corrupted.");
      }
   }

   public static class State {
      public static final Lexer.State INITIAL = new Lexer.State(0);
      private int id;

      private State(int id) {
         this.id = id;
      }

      public int id() {
         return this.id;
      }
   }
}
