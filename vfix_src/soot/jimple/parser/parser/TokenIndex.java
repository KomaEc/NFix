package soot.jimple.parser.parser;

import soot.jimple.parser.analysis.AnalysisAdapter;
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

class TokenIndex extends AnalysisAdapter {
   int index;

   public void caseTAbstract(TAbstract node) {
      this.index = 0;
   }

   public void caseTFinal(TFinal node) {
      this.index = 1;
   }

   public void caseTNative(TNative node) {
      this.index = 2;
   }

   public void caseTPublic(TPublic node) {
      this.index = 3;
   }

   public void caseTProtected(TProtected node) {
      this.index = 4;
   }

   public void caseTPrivate(TPrivate node) {
      this.index = 5;
   }

   public void caseTStatic(TStatic node) {
      this.index = 6;
   }

   public void caseTSynchronized(TSynchronized node) {
      this.index = 7;
   }

   public void caseTTransient(TTransient node) {
      this.index = 8;
   }

   public void caseTVolatile(TVolatile node) {
      this.index = 9;
   }

   public void caseTStrictfp(TStrictfp node) {
      this.index = 10;
   }

   public void caseTEnum(TEnum node) {
      this.index = 11;
   }

   public void caseTAnnotation(TAnnotation node) {
      this.index = 12;
   }

   public void caseTClass(TClass node) {
      this.index = 13;
   }

   public void caseTInterface(TInterface node) {
      this.index = 14;
   }

   public void caseTVoid(TVoid node) {
      this.index = 15;
   }

   public void caseTBoolean(TBoolean node) {
      this.index = 16;
   }

   public void caseTByte(TByte node) {
      this.index = 17;
   }

   public void caseTShort(TShort node) {
      this.index = 18;
   }

   public void caseTChar(TChar node) {
      this.index = 19;
   }

   public void caseTInt(TInt node) {
      this.index = 20;
   }

   public void caseTLong(TLong node) {
      this.index = 21;
   }

   public void caseTFloat(TFloat node) {
      this.index = 22;
   }

   public void caseTDouble(TDouble node) {
      this.index = 23;
   }

   public void caseTNullType(TNullType node) {
      this.index = 24;
   }

   public void caseTUnknown(TUnknown node) {
      this.index = 25;
   }

   public void caseTExtends(TExtends node) {
      this.index = 26;
   }

   public void caseTImplements(TImplements node) {
      this.index = 27;
   }

   public void caseTBreakpoint(TBreakpoint node) {
      this.index = 28;
   }

   public void caseTCase(TCase node) {
      this.index = 29;
   }

   public void caseTCatch(TCatch node) {
      this.index = 30;
   }

   public void caseTCmp(TCmp node) {
      this.index = 31;
   }

   public void caseTCmpg(TCmpg node) {
      this.index = 32;
   }

   public void caseTCmpl(TCmpl node) {
      this.index = 33;
   }

   public void caseTDefault(TDefault node) {
      this.index = 34;
   }

   public void caseTEntermonitor(TEntermonitor node) {
      this.index = 35;
   }

   public void caseTExitmonitor(TExitmonitor node) {
      this.index = 36;
   }

   public void caseTGoto(TGoto node) {
      this.index = 37;
   }

   public void caseTIf(TIf node) {
      this.index = 38;
   }

   public void caseTInstanceof(TInstanceof node) {
      this.index = 39;
   }

   public void caseTInterfaceinvoke(TInterfaceinvoke node) {
      this.index = 40;
   }

   public void caseTLengthof(TLengthof node) {
      this.index = 41;
   }

   public void caseTLookupswitch(TLookupswitch node) {
      this.index = 42;
   }

   public void caseTNeg(TNeg node) {
      this.index = 43;
   }

   public void caseTNew(TNew node) {
      this.index = 44;
   }

   public void caseTNewarray(TNewarray node) {
      this.index = 45;
   }

   public void caseTNewmultiarray(TNewmultiarray node) {
      this.index = 46;
   }

   public void caseTNop(TNop node) {
      this.index = 47;
   }

   public void caseTRet(TRet node) {
      this.index = 48;
   }

   public void caseTReturn(TReturn node) {
      this.index = 49;
   }

   public void caseTSpecialinvoke(TSpecialinvoke node) {
      this.index = 50;
   }

   public void caseTStaticinvoke(TStaticinvoke node) {
      this.index = 51;
   }

   public void caseTDynamicinvoke(TDynamicinvoke node) {
      this.index = 52;
   }

   public void caseTTableswitch(TTableswitch node) {
      this.index = 53;
   }

   public void caseTThrow(TThrow node) {
      this.index = 54;
   }

   public void caseTThrows(TThrows node) {
      this.index = 55;
   }

   public void caseTVirtualinvoke(TVirtualinvoke node) {
      this.index = 56;
   }

   public void caseTNull(TNull node) {
      this.index = 57;
   }

   public void caseTFrom(TFrom node) {
      this.index = 58;
   }

   public void caseTTo(TTo node) {
      this.index = 59;
   }

   public void caseTWith(TWith node) {
      this.index = 60;
   }

   public void caseTCls(TCls node) {
      this.index = 61;
   }

   public void caseTComma(TComma node) {
      this.index = 62;
   }

   public void caseTLBrace(TLBrace node) {
      this.index = 63;
   }

   public void caseTRBrace(TRBrace node) {
      this.index = 64;
   }

   public void caseTSemicolon(TSemicolon node) {
      this.index = 65;
   }

   public void caseTLBracket(TLBracket node) {
      this.index = 66;
   }

   public void caseTRBracket(TRBracket node) {
      this.index = 67;
   }

   public void caseTLParen(TLParen node) {
      this.index = 68;
   }

   public void caseTRParen(TRParen node) {
      this.index = 69;
   }

   public void caseTColon(TColon node) {
      this.index = 70;
   }

   public void caseTDot(TDot node) {
      this.index = 71;
   }

   public void caseTQuote(TQuote node) {
      this.index = 72;
   }

   public void caseTColonEquals(TColonEquals node) {
      this.index = 73;
   }

   public void caseTEquals(TEquals node) {
      this.index = 74;
   }

   public void caseTAnd(TAnd node) {
      this.index = 75;
   }

   public void caseTOr(TOr node) {
      this.index = 76;
   }

   public void caseTXor(TXor node) {
      this.index = 77;
   }

   public void caseTMod(TMod node) {
      this.index = 78;
   }

   public void caseTCmpeq(TCmpeq node) {
      this.index = 79;
   }

   public void caseTCmpne(TCmpne node) {
      this.index = 80;
   }

   public void caseTCmpgt(TCmpgt node) {
      this.index = 81;
   }

   public void caseTCmpge(TCmpge node) {
      this.index = 82;
   }

   public void caseTCmplt(TCmplt node) {
      this.index = 83;
   }

   public void caseTCmple(TCmple node) {
      this.index = 84;
   }

   public void caseTShl(TShl node) {
      this.index = 85;
   }

   public void caseTShr(TShr node) {
      this.index = 86;
   }

   public void caseTUshr(TUshr node) {
      this.index = 87;
   }

   public void caseTPlus(TPlus node) {
      this.index = 88;
   }

   public void caseTMinus(TMinus node) {
      this.index = 89;
   }

   public void caseTMult(TMult node) {
      this.index = 90;
   }

   public void caseTDiv(TDiv node) {
      this.index = 91;
   }

   public void caseTQuotedName(TQuotedName node) {
      this.index = 92;
   }

   public void caseTFullIdentifier(TFullIdentifier node) {
      this.index = 93;
   }

   public void caseTIdentifier(TIdentifier node) {
      this.index = 94;
   }

   public void caseTAtIdentifier(TAtIdentifier node) {
      this.index = 95;
   }

   public void caseTBoolConstant(TBoolConstant node) {
      this.index = 96;
   }

   public void caseTIntegerConstant(TIntegerConstant node) {
      this.index = 97;
   }

   public void caseTFloatConstant(TFloatConstant node) {
      this.index = 98;
   }

   public void caseTStringConstant(TStringConstant node) {
      this.index = 99;
   }

   public void caseEOF(EOF node) {
      this.index = 100;
   }
}
