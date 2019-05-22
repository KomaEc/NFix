package soot.jimple.parser.parser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;
import soot.jimple.parser.analysis.AnalysisAdapter;
import soot.jimple.parser.lexer.Lexer;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.node.AAbstractModifier;
import soot.jimple.parser.node.AAndBinop;
import soot.jimple.parser.node.AAnnotationModifier;
import soot.jimple.parser.node.AArrayBrackets;
import soot.jimple.parser.node.AArrayDescriptor;
import soot.jimple.parser.node.AArrayNewExpr;
import soot.jimple.parser.node.AArrayReference;
import soot.jimple.parser.node.AAssignStatement;
import soot.jimple.parser.node.ABaseNonvoidType;
import soot.jimple.parser.node.ABinopBoolExpr;
import soot.jimple.parser.node.ABinopExpr;
import soot.jimple.parser.node.ABinopExpression;
import soot.jimple.parser.node.ABooleanBaseType;
import soot.jimple.parser.node.ABooleanBaseTypeNoName;
import soot.jimple.parser.node.ABreakpointStatement;
import soot.jimple.parser.node.AByteBaseType;
import soot.jimple.parser.node.AByteBaseTypeNoName;
import soot.jimple.parser.node.ACaseStmt;
import soot.jimple.parser.node.ACastExpression;
import soot.jimple.parser.node.ACatchClause;
import soot.jimple.parser.node.ACharBaseType;
import soot.jimple.parser.node.ACharBaseTypeNoName;
import soot.jimple.parser.node.AClassFileType;
import soot.jimple.parser.node.AClassNameBaseType;
import soot.jimple.parser.node.AClassNameMultiClassNameList;
import soot.jimple.parser.node.AClassNameSingleClassNameList;
import soot.jimple.parser.node.AClzzConstant;
import soot.jimple.parser.node.ACmpBinop;
import soot.jimple.parser.node.ACmpeqBinop;
import soot.jimple.parser.node.ACmpgBinop;
import soot.jimple.parser.node.ACmpgeBinop;
import soot.jimple.parser.node.ACmpgtBinop;
import soot.jimple.parser.node.ACmplBinop;
import soot.jimple.parser.node.ACmpleBinop;
import soot.jimple.parser.node.ACmpltBinop;
import soot.jimple.parser.node.ACmpneBinop;
import soot.jimple.parser.node.AConstantCaseLabel;
import soot.jimple.parser.node.AConstantImmediate;
import soot.jimple.parser.node.ADeclaration;
import soot.jimple.parser.node.ADefaultCaseLabel;
import soot.jimple.parser.node.ADivBinop;
import soot.jimple.parser.node.ADoubleBaseType;
import soot.jimple.parser.node.ADoubleBaseTypeNoName;
import soot.jimple.parser.node.ADynamicInvokeExpr;
import soot.jimple.parser.node.AEmptyMethodBody;
import soot.jimple.parser.node.AEntermonitorStatement;
import soot.jimple.parser.node.AEnumModifier;
import soot.jimple.parser.node.AExitmonitorStatement;
import soot.jimple.parser.node.AExtendsClause;
import soot.jimple.parser.node.AFieldMember;
import soot.jimple.parser.node.AFieldReference;
import soot.jimple.parser.node.AFieldSignature;
import soot.jimple.parser.node.AFile;
import soot.jimple.parser.node.AFileBody;
import soot.jimple.parser.node.AFinalModifier;
import soot.jimple.parser.node.AFixedArrayDescriptor;
import soot.jimple.parser.node.AFloatBaseType;
import soot.jimple.parser.node.AFloatBaseTypeNoName;
import soot.jimple.parser.node.AFloatConstant;
import soot.jimple.parser.node.AFullIdentClassName;
import soot.jimple.parser.node.AFullIdentNonvoidType;
import soot.jimple.parser.node.AFullMethodBody;
import soot.jimple.parser.node.AGotoStatement;
import soot.jimple.parser.node.AGotoStmt;
import soot.jimple.parser.node.AIdentArrayRef;
import soot.jimple.parser.node.AIdentClassName;
import soot.jimple.parser.node.AIdentName;
import soot.jimple.parser.node.AIdentNonvoidType;
import soot.jimple.parser.node.AIdentityNoTypeStatement;
import soot.jimple.parser.node.AIdentityStatement;
import soot.jimple.parser.node.AIfStatement;
import soot.jimple.parser.node.AImmediateExpression;
import soot.jimple.parser.node.AImplementsClause;
import soot.jimple.parser.node.AInstanceofExpression;
import soot.jimple.parser.node.AIntBaseType;
import soot.jimple.parser.node.AIntBaseTypeNoName;
import soot.jimple.parser.node.AIntegerConstant;
import soot.jimple.parser.node.AInterfaceFileType;
import soot.jimple.parser.node.AInterfaceNonstaticInvoke;
import soot.jimple.parser.node.AInvokeExpression;
import soot.jimple.parser.node.AInvokeStatement;
import soot.jimple.parser.node.ALabelName;
import soot.jimple.parser.node.ALabelStatement;
import soot.jimple.parser.node.ALengthofUnop;
import soot.jimple.parser.node.ALocalFieldRef;
import soot.jimple.parser.node.ALocalImmediate;
import soot.jimple.parser.node.ALocalName;
import soot.jimple.parser.node.ALocalVariable;
import soot.jimple.parser.node.ALongBaseType;
import soot.jimple.parser.node.ALongBaseTypeNoName;
import soot.jimple.parser.node.ALookupswitchStatement;
import soot.jimple.parser.node.AMethodMember;
import soot.jimple.parser.node.AMethodSignature;
import soot.jimple.parser.node.AMinusBinop;
import soot.jimple.parser.node.AModBinop;
import soot.jimple.parser.node.AMultBinop;
import soot.jimple.parser.node.AMultiArgList;
import soot.jimple.parser.node.AMultiLocalNameList;
import soot.jimple.parser.node.AMultiNameList;
import soot.jimple.parser.node.AMultiNewExpr;
import soot.jimple.parser.node.AMultiParameterList;
import soot.jimple.parser.node.ANativeModifier;
import soot.jimple.parser.node.ANegUnop;
import soot.jimple.parser.node.ANewExpression;
import soot.jimple.parser.node.ANonstaticInvokeExpr;
import soot.jimple.parser.node.ANonvoidJimpleType;
import soot.jimple.parser.node.ANopStatement;
import soot.jimple.parser.node.ANovoidType;
import soot.jimple.parser.node.ANullBaseType;
import soot.jimple.parser.node.ANullBaseTypeNoName;
import soot.jimple.parser.node.ANullConstant;
import soot.jimple.parser.node.AOrBinop;
import soot.jimple.parser.node.AParameter;
import soot.jimple.parser.node.APlusBinop;
import soot.jimple.parser.node.APrivateModifier;
import soot.jimple.parser.node.AProtectedModifier;
import soot.jimple.parser.node.APublicModifier;
import soot.jimple.parser.node.AQuotedArrayRef;
import soot.jimple.parser.node.AQuotedClassName;
import soot.jimple.parser.node.AQuotedName;
import soot.jimple.parser.node.AQuotedNonvoidType;
import soot.jimple.parser.node.AReferenceExpression;
import soot.jimple.parser.node.AReferenceVariable;
import soot.jimple.parser.node.ARetStatement;
import soot.jimple.parser.node.AReturnStatement;
import soot.jimple.parser.node.AShlBinop;
import soot.jimple.parser.node.AShortBaseType;
import soot.jimple.parser.node.AShortBaseTypeNoName;
import soot.jimple.parser.node.AShrBinop;
import soot.jimple.parser.node.ASigFieldRef;
import soot.jimple.parser.node.ASimpleNewExpr;
import soot.jimple.parser.node.ASingleArgList;
import soot.jimple.parser.node.ASingleLocalNameList;
import soot.jimple.parser.node.ASingleNameList;
import soot.jimple.parser.node.ASingleParameterList;
import soot.jimple.parser.node.ASpecialNonstaticInvoke;
import soot.jimple.parser.node.AStaticInvokeExpr;
import soot.jimple.parser.node.AStaticModifier;
import soot.jimple.parser.node.AStrictfpModifier;
import soot.jimple.parser.node.AStringConstant;
import soot.jimple.parser.node.ASynchronizedModifier;
import soot.jimple.parser.node.ATableswitchStatement;
import soot.jimple.parser.node.AThrowStatement;
import soot.jimple.parser.node.AThrowsClause;
import soot.jimple.parser.node.ATransientModifier;
import soot.jimple.parser.node.AUnknownJimpleType;
import soot.jimple.parser.node.AUnnamedMethodSignature;
import soot.jimple.parser.node.AUnopBoolExpr;
import soot.jimple.parser.node.AUnopExpr;
import soot.jimple.parser.node.AUnopExpression;
import soot.jimple.parser.node.AUshrBinop;
import soot.jimple.parser.node.AVirtualNonstaticInvoke;
import soot.jimple.parser.node.AVoidType;
import soot.jimple.parser.node.AVolatileModifier;
import soot.jimple.parser.node.AXorBinop;
import soot.jimple.parser.node.EOF;
import soot.jimple.parser.node.PArgList;
import soot.jimple.parser.node.PArrayBrackets;
import soot.jimple.parser.node.PArrayDescriptor;
import soot.jimple.parser.node.PArrayRef;
import soot.jimple.parser.node.PBaseType;
import soot.jimple.parser.node.PBaseTypeNoName;
import soot.jimple.parser.node.PBinop;
import soot.jimple.parser.node.PBinopExpr;
import soot.jimple.parser.node.PBoolExpr;
import soot.jimple.parser.node.PCaseLabel;
import soot.jimple.parser.node.PCaseStmt;
import soot.jimple.parser.node.PCatchClause;
import soot.jimple.parser.node.PClassName;
import soot.jimple.parser.node.PClassNameList;
import soot.jimple.parser.node.PConstant;
import soot.jimple.parser.node.PDeclaration;
import soot.jimple.parser.node.PExpression;
import soot.jimple.parser.node.PExtendsClause;
import soot.jimple.parser.node.PFieldRef;
import soot.jimple.parser.node.PFieldSignature;
import soot.jimple.parser.node.PFile;
import soot.jimple.parser.node.PFileBody;
import soot.jimple.parser.node.PFileType;
import soot.jimple.parser.node.PFixedArrayDescriptor;
import soot.jimple.parser.node.PGotoStmt;
import soot.jimple.parser.node.PImmediate;
import soot.jimple.parser.node.PImplementsClause;
import soot.jimple.parser.node.PInvokeExpr;
import soot.jimple.parser.node.PJimpleType;
import soot.jimple.parser.node.PLabelName;
import soot.jimple.parser.node.PLocalName;
import soot.jimple.parser.node.PLocalNameList;
import soot.jimple.parser.node.PMember;
import soot.jimple.parser.node.PMethodBody;
import soot.jimple.parser.node.PMethodSignature;
import soot.jimple.parser.node.PModifier;
import soot.jimple.parser.node.PName;
import soot.jimple.parser.node.PNameList;
import soot.jimple.parser.node.PNewExpr;
import soot.jimple.parser.node.PNonstaticInvoke;
import soot.jimple.parser.node.PNonvoidType;
import soot.jimple.parser.node.PParameter;
import soot.jimple.parser.node.PParameterList;
import soot.jimple.parser.node.PReference;
import soot.jimple.parser.node.PStatement;
import soot.jimple.parser.node.PThrowsClause;
import soot.jimple.parser.node.PType;
import soot.jimple.parser.node.PUnnamedMethodSignature;
import soot.jimple.parser.node.PUnop;
import soot.jimple.parser.node.PUnopExpr;
import soot.jimple.parser.node.PVariable;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.node.Switchable;
import soot.jimple.parser.node.TAbstract;
import soot.jimple.parser.node.TAnd;
import soot.jimple.parser.node.TAnnotation;
import soot.jimple.parser.node.TAtIdentifier;
import soot.jimple.parser.node.TBoolean;
import soot.jimple.parser.node.TBreakpoint;
import soot.jimple.parser.node.TByte;
import soot.jimple.parser.node.TCase;
import soot.jimple.parser.node.TCatch;
import soot.jimple.parser.node.TChar;
import soot.jimple.parser.node.TClass;
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

public class Parser {
   public final Analysis ignoredTokens = new AnalysisAdapter();
   protected ArrayList<Object> nodeList;
   private final Lexer lexer;
   private final ListIterator<Object> stack = (new LinkedList()).listIterator();
   private int last_pos;
   private int last_line;
   private Token last_token;
   private final TokenIndex converter = new TokenIndex();
   private final int[] action = new int[2];
   private static final int SHIFT = 0;
   private static final int REDUCE = 1;
   private static final int ACCEPT = 2;
   private static final int ERROR = 3;
   private static int[][][] actionTable;
   private static int[][][] gotoTable;
   private static String[] errorMessages;
   private static int[] errors;

   public Parser(Lexer lexer) {
      this.lexer = lexer;
   }

   protected void filter() throws ParserException, LexerException, IOException {
   }

   private void push(int numstate, ArrayList<Object> listNode, boolean hidden) throws ParserException, LexerException, IOException {
      this.nodeList = listNode;
      if (!hidden) {
         this.filter();
      }

      if (!this.stack.hasNext()) {
         this.stack.add(new State(numstate, this.nodeList));
      } else {
         State s = (State)this.stack.next();
         s.state = numstate;
         s.nodes = this.nodeList;
      }
   }

   private int goTo(int index) {
      int state = this.state();
      int low = 1;
      int high = gotoTable[index].length - 1;
      int value = gotoTable[index][0][1];

      while(low <= high) {
         int middle = low + high >>> 1;
         if (state < gotoTable[index][middle][0]) {
            high = middle - 1;
         } else {
            if (state <= gotoTable[index][middle][0]) {
               value = gotoTable[index][middle][1];
               break;
            }

            low = middle + 1;
         }
      }

      return value;
   }

   private int state() {
      State s = (State)this.stack.previous();
      this.stack.next();
      return s.state;
   }

   private ArrayList<Object> pop() {
      return ((State)this.stack.previous()).nodes;
   }

   private int index(Switchable token) {
      this.converter.index = -1;
      token.apply(this.converter);
      return this.converter.index;
   }

   public Start parse() throws ParserException, LexerException, IOException {
      this.push(0, (ArrayList)null, true);
      LinkedList ign = null;

      while(true) {
         while(this.index(this.lexer.peek()) != -1) {
            if (ign != null) {
               this.ignoredTokens.setIn(this.lexer.peek(), ign);
               ign = null;
            }

            this.last_pos = this.lexer.peek().getPos();
            this.last_line = this.lexer.peek().getLine();
            this.last_token = this.lexer.peek();
            int index = this.index(this.lexer.peek());
            this.action[0] = actionTable[this.state()][0][1];
            this.action[1] = actionTable[this.state()][0][2];
            int low = 1;
            int high = actionTable[this.state()].length - 1;

            while(low <= high) {
               int middle = (low + high) / 2;
               if (index < actionTable[this.state()][middle][0]) {
                  high = middle - 1;
               } else {
                  if (index <= actionTable[this.state()][middle][0]) {
                     this.action[0] = actionTable[this.state()][middle][1];
                     this.action[1] = actionTable[this.state()][middle][2];
                     break;
                  }

                  low = middle + 1;
               }
            }

            ArrayList list;
            switch(this.action[0]) {
            case 0:
               list = new ArrayList();
               list.add(this.lexer.next());
               this.push(this.action[1], list, false);
               break;
            case 1:
               switch(this.action[1]) {
               case 0:
                  list = this.new0();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 1:
                  list = this.new1();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 2:
                  list = this.new2();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 3:
                  list = this.new3();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 4:
                  list = this.new4();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 5:
                  list = this.new5();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 6:
                  list = this.new6();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 7:
                  list = this.new7();
                  this.push(this.goTo(0), list, false);
                  continue;
               case 8:
                  list = this.new8();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 9:
                  list = this.new9();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 10:
                  list = this.new10();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 11:
                  list = this.new11();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 12:
                  list = this.new12();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 13:
                  list = this.new13();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 14:
                  list = this.new14();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 15:
                  list = this.new15();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 16:
                  list = this.new16();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 17:
                  list = this.new17();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 18:
                  list = this.new18();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 19:
                  list = this.new19();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 20:
                  list = this.new20();
                  this.push(this.goTo(1), list, false);
                  continue;
               case 21:
                  list = this.new21();
                  this.push(this.goTo(2), list, false);
                  continue;
               case 22:
                  list = this.new22();
                  this.push(this.goTo(2), list, false);
                  continue;
               case 23:
                  list = this.new23();
                  this.push(this.goTo(3), list, false);
                  continue;
               case 24:
                  list = this.new24();
                  this.push(this.goTo(4), list, false);
                  continue;
               case 25:
                  list = this.new25();
                  this.push(this.goTo(5), list, false);
                  continue;
               case 26:
                  list = this.new26();
                  this.push(this.goTo(5), list, false);
                  continue;
               case 27:
                  list = this.new27();
                  this.push(this.goTo(6), list, false);
                  continue;
               case 28:
                  list = this.new28();
                  this.push(this.goTo(6), list, false);
                  continue;
               case 29:
                  list = this.new29();
                  this.push(this.goTo(7), list, false);
                  continue;
               case 30:
                  list = this.new30();
                  this.push(this.goTo(7), list, false);
                  continue;
               case 31:
                  list = this.new31();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 32:
                  list = this.new32();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 33:
                  list = this.new33();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 34:
                  list = this.new34();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 35:
                  list = this.new35();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 36:
                  list = this.new36();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 37:
                  list = this.new37();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 38:
                  list = this.new38();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 39:
                  list = this.new39();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 40:
                  list = this.new40();
                  this.push(this.goTo(8), list, false);
                  continue;
               case 41:
                  list = this.new41();
                  this.push(this.goTo(9), list, false);
                  continue;
               case 42:
                  list = this.new42();
                  this.push(this.goTo(9), list, false);
                  continue;
               case 43:
                  list = this.new43();
                  this.push(this.goTo(10), list, false);
                  continue;
               case 44:
                  list = this.new44();
                  this.push(this.goTo(10), list, false);
                  continue;
               case 45:
                  list = this.new45();
                  this.push(this.goTo(11), list, false);
                  continue;
               case 46:
                  list = this.new46();
                  this.push(this.goTo(12), list, false);
                  continue;
               case 47:
                  list = this.new47();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 48:
                  list = this.new48();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 49:
                  list = this.new49();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 50:
                  list = this.new50();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 51:
                  list = this.new51();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 52:
                  list = this.new52();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 53:
                  list = this.new53();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 54:
                  list = this.new54();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 55:
                  list = this.new55();
                  this.push(this.goTo(13), list, false);
                  continue;
               case 56:
                  list = this.new56();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 57:
                  list = this.new57();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 58:
                  list = this.new58();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 59:
                  list = this.new59();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 60:
                  list = this.new60();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 61:
                  list = this.new61();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 62:
                  list = this.new62();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 63:
                  list = this.new63();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 64:
                  list = this.new64();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 65:
                  list = this.new65();
                  this.push(this.goTo(14), list, false);
                  continue;
               case 66:
                  list = this.new66();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 67:
                  list = this.new67();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 68:
                  list = this.new68();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 69:
                  list = this.new69();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 70:
                  list = this.new70();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 71:
                  list = this.new71();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 72:
                  list = this.new72();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 73:
                  list = this.new73();
                  this.push(this.goTo(15), list, false);
                  continue;
               case 74:
                  list = this.new74();
                  this.push(this.goTo(16), list, false);
                  continue;
               case 75:
                  list = this.new75();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 76:
                  list = this.new76();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 77:
                  list = this.new77();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 78:
                  list = this.new78();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 79:
                  list = this.new79();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 80:
                  list = this.new80();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 81:
                  list = this.new81();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 82:
                  list = this.new82();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 83:
                  list = this.new83();
                  this.push(this.goTo(17), list, false);
                  continue;
               case 84:
                  list = this.new84();
                  this.push(this.goTo(18), list, false);
                  continue;
               case 85:
                  list = this.new85();
                  this.push(this.goTo(19), list, false);
                  continue;
               case 86:
                  list = this.new86();
                  this.push(this.goTo(19), list, false);
                  continue;
               case 87:
                  list = this.new87();
                  this.push(this.goTo(20), list, false);
                  continue;
               case 88:
                  list = this.new88();
                  this.push(this.goTo(21), list, false);
                  continue;
               case 89:
                  list = this.new89();
                  this.push(this.goTo(21), list, false);
                  continue;
               case 90:
                  list = this.new90();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 91:
                  list = this.new91();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 92:
                  list = this.new92();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 93:
                  list = this.new93();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 94:
                  list = this.new94();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 95:
                  list = this.new95();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 96:
                  list = this.new96();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 97:
                  list = this.new97();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 98:
                  list = this.new98();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 99:
                  list = this.new99();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 100:
                  list = this.new100();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 101:
                  list = this.new101();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 102:
                  list = this.new102();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 103:
                  list = this.new103();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 104:
                  list = this.new104();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 105:
                  list = this.new105();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 106:
                  list = this.new106();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 107:
                  list = this.new107();
                  this.push(this.goTo(22), list, false);
                  continue;
               case 108:
                  list = this.new108();
                  this.push(this.goTo(23), list, false);
                  continue;
               case 109:
                  list = this.new109();
                  this.push(this.goTo(24), list, false);
                  continue;
               case 110:
                  list = this.new110();
                  this.push(this.goTo(25), list, false);
                  continue;
               case 111:
                  list = this.new111();
                  this.push(this.goTo(25), list, false);
                  continue;
               case 112:
                  list = this.new112();
                  this.push(this.goTo(25), list, false);
                  continue;
               case 113:
                  list = this.new113();
                  this.push(this.goTo(26), list, false);
                  continue;
               case 114:
                  list = this.new114();
                  this.push(this.goTo(27), list, false);
                  continue;
               case 115:
                  list = this.new115();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 116:
                  list = this.new116();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 117:
                  list = this.new117();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 118:
                  list = this.new118();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 119:
                  list = this.new119();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 120:
                  list = this.new120();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 121:
                  list = this.new121();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 122:
                  list = this.new122();
                  this.push(this.goTo(28), list, false);
                  continue;
               case 123:
                  list = this.new123();
                  this.push(this.goTo(29), list, false);
                  continue;
               case 124:
                  list = this.new124();
                  this.push(this.goTo(29), list, false);
                  continue;
               case 125:
                  list = this.new125();
                  this.push(this.goTo(29), list, false);
                  continue;
               case 126:
                  list = this.new126();
                  this.push(this.goTo(30), list, false);
                  continue;
               case 127:
                  list = this.new127();
                  this.push(this.goTo(30), list, false);
                  continue;
               case 128:
                  list = this.new128();
                  this.push(this.goTo(31), list, false);
                  continue;
               case 129:
                  list = this.new129();
                  this.push(this.goTo(31), list, false);
                  continue;
               case 130:
                  list = this.new130();
                  this.push(this.goTo(32), list, false);
                  continue;
               case 131:
                  list = this.new131();
                  this.push(this.goTo(32), list, false);
                  continue;
               case 132:
                  list = this.new132();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 133:
                  list = this.new133();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 134:
                  list = this.new134();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 135:
                  list = this.new135();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 136:
                  list = this.new136();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 137:
                  list = this.new137();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 138:
                  list = this.new138();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 139:
                  list = this.new139();
                  this.push(this.goTo(33), list, false);
                  continue;
               case 140:
                  list = this.new140();
                  this.push(this.goTo(34), list, false);
                  continue;
               case 141:
                  list = this.new141();
                  this.push(this.goTo(35), list, false);
                  continue;
               case 142:
                  list = this.new142();
                  this.push(this.goTo(36), list, false);
                  continue;
               case 143:
                  list = this.new143();
                  this.push(this.goTo(36), list, false);
                  continue;
               case 144:
                  list = this.new144();
                  this.push(this.goTo(36), list, false);
                  continue;
               case 145:
                  list = this.new145();
                  this.push(this.goTo(37), list, false);
                  continue;
               case 146:
                  list = this.new146();
                  this.push(this.goTo(37), list, false);
                  continue;
               case 147:
                  list = this.new147();
                  this.push(this.goTo(38), list, false);
                  continue;
               case 148:
                  list = this.new148();
                  this.push(this.goTo(38), list, false);
                  continue;
               case 149:
                  list = this.new149();
                  this.push(this.goTo(39), list, false);
                  continue;
               case 150:
                  list = this.new150();
                  this.push(this.goTo(39), list, false);
                  continue;
               case 151:
                  list = this.new151();
                  this.push(this.goTo(40), list, false);
                  continue;
               case 152:
                  list = this.new152();
                  this.push(this.goTo(40), list, false);
                  continue;
               case 153:
                  list = this.new153();
                  this.push(this.goTo(41), list, false);
                  continue;
               case 154:
                  list = this.new154();
                  this.push(this.goTo(41), list, false);
                  continue;
               case 155:
                  list = this.new155();
                  this.push(this.goTo(42), list, false);
                  continue;
               case 156:
                  list = this.new156();
                  this.push(this.goTo(43), list, false);
                  continue;
               case 157:
                  list = this.new157();
                  this.push(this.goTo(44), list, false);
                  continue;
               case 158:
                  list = this.new158();
                  this.push(this.goTo(44), list, false);
                  continue;
               case 159:
                  list = this.new159();
                  this.push(this.goTo(45), list, false);
                  continue;
               case 160:
                  list = this.new160();
                  this.push(this.goTo(45), list, false);
                  continue;
               case 161:
                  list = this.new161();
                  this.push(this.goTo(46), list, false);
                  continue;
               case 162:
                  list = this.new162();
                  this.push(this.goTo(46), list, false);
                  continue;
               case 163:
                  list = this.new163();
                  this.push(this.goTo(46), list, false);
                  continue;
               case 164:
                  list = this.new164();
                  this.push(this.goTo(46), list, false);
                  continue;
               case 165:
                  list = this.new165();
                  this.push(this.goTo(46), list, false);
                  continue;
               case 166:
                  list = this.new166();
                  this.push(this.goTo(46), list, false);
                  continue;
               case 167:
                  list = this.new167();
                  this.push(this.goTo(46), list, false);
                  continue;
               case 168:
                  list = this.new168();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 169:
                  list = this.new169();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 170:
                  list = this.new170();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 171:
                  list = this.new171();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 172:
                  list = this.new172();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 173:
                  list = this.new173();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 174:
                  list = this.new174();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 175:
                  list = this.new175();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 176:
                  list = this.new176();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 177:
                  list = this.new177();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 178:
                  list = this.new178();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 179:
                  list = this.new179();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 180:
                  list = this.new180();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 181:
                  list = this.new181();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 182:
                  list = this.new182();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 183:
                  list = this.new183();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 184:
                  list = this.new184();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 185:
                  list = this.new185();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 186:
                  list = this.new186();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 187:
                  list = this.new187();
                  this.push(this.goTo(47), list, false);
                  continue;
               case 188:
                  list = this.new188();
                  this.push(this.goTo(48), list, false);
                  continue;
               case 189:
                  list = this.new189();
                  this.push(this.goTo(48), list, false);
                  continue;
               case 190:
                  list = this.new190();
                  this.push(this.goTo(49), list, false);
                  continue;
               case 191:
                  list = this.new191();
                  this.push(this.goTo(49), list, false);
                  continue;
               case 192:
                  list = this.new192();
                  this.push(this.goTo(49), list, false);
                  continue;
               case 193:
                  list = this.new193();
                  this.push(this.goTo(50), list, false);
                  continue;
               case 194:
                  list = this.new194();
                  this.push(this.goTo(50), list, false);
                  continue;
               case 195:
                  list = this.new195();
                  this.push(this.goTo(51), list, true);
                  continue;
               case 196:
                  list = this.new196();
                  this.push(this.goTo(51), list, true);
                  continue;
               case 197:
                  list = this.new197();
                  this.push(this.goTo(52), list, true);
                  continue;
               case 198:
                  list = this.new198();
                  this.push(this.goTo(52), list, true);
                  continue;
               case 199:
                  list = this.new199();
                  this.push(this.goTo(53), list, true);
                  continue;
               case 200:
                  list = this.new200();
                  this.push(this.goTo(53), list, true);
                  continue;
               case 201:
                  list = this.new201();
                  this.push(this.goTo(54), list, true);
                  continue;
               case 202:
                  list = this.new202();
                  this.push(this.goTo(54), list, true);
                  continue;
               case 203:
                  list = this.new203();
                  this.push(this.goTo(55), list, true);
                  continue;
               case 204:
                  list = this.new204();
                  this.push(this.goTo(55), list, true);
                  continue;
               case 205:
                  list = this.new205();
                  this.push(this.goTo(56), list, true);
                  continue;
               case 206:
                  list = this.new206();
                  this.push(this.goTo(56), list, true);
                  continue;
               case 207:
                  list = this.new207();
                  this.push(this.goTo(57), list, true);
                  continue;
               case 208:
                  list = this.new208();
                  this.push(this.goTo(57), list, true);
                  continue;
               case 209:
                  list = this.new209();
                  this.push(this.goTo(58), list, true);
                  continue;
               case 210:
                  list = this.new210();
                  this.push(this.goTo(58), list, true);
               default:
                  continue;
               }
            case 2:
               EOF node2 = (EOF)this.lexer.next();
               PFile node1 = (PFile)this.pop().get(0);
               Start node = new Start(node1, node2);
               return node;
            case 3:
               throw new ParserException(this.last_token, "[" + this.last_line + "," + this.last_pos + "] " + errorMessages[errors[this.action[1]]]);
            }
         }

         if (ign == null) {
            ign = new LinkedList();
         }

         ign.add(this.lexer.next());
      }
   }

   ArrayList<Object> new0() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      Object nullNode5 = null;
      Object nullNode6 = null;
      PFileType pfiletypeNode3 = (PFileType)nodeArrayList1.get(0);
      PClassName pclassnameNode4 = (PClassName)nodeArrayList2.get(0);
      PFileBody pfilebodyNode7 = (PFileBody)nodeArrayList3.get(0);
      PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, (PExtendsClause)null, (PImplementsClause)null, pfilebodyNode7);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new1() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      Object nullNode6 = null;
      Object nullNode7 = null;
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PFileType pfiletypeNode4 = (PFileType)nodeArrayList2.get(0);
      PClassName pclassnameNode5 = (PClassName)nodeArrayList3.get(0);
      PFileBody pfilebodyNode8 = (PFileBody)nodeArrayList4.get(0);
      PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, (PExtendsClause)null, (PImplementsClause)null, pfilebodyNode8);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new2() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      Object nullNode6 = null;
      PFileType pfiletypeNode3 = (PFileType)nodeArrayList1.get(0);
      PClassName pclassnameNode4 = (PClassName)nodeArrayList2.get(0);
      PExtendsClause pextendsclauseNode5 = (PExtendsClause)nodeArrayList3.get(0);
      PFileBody pfilebodyNode7 = (PFileBody)nodeArrayList4.get(0);
      PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, pextendsclauseNode5, (PImplementsClause)null, pfilebodyNode7);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new3() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      Object nullNode7 = null;
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PFileType pfiletypeNode4 = (PFileType)nodeArrayList2.get(0);
      PClassName pclassnameNode5 = (PClassName)nodeArrayList3.get(0);
      PExtendsClause pextendsclauseNode6 = (PExtendsClause)nodeArrayList4.get(0);
      PFileBody pfilebodyNode8 = (PFileBody)nodeArrayList5.get(0);
      PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, pextendsclauseNode6, (PImplementsClause)null, pfilebodyNode8);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new4() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      Object nullNode5 = null;
      PFileType pfiletypeNode3 = (PFileType)nodeArrayList1.get(0);
      PClassName pclassnameNode4 = (PClassName)nodeArrayList2.get(0);
      PImplementsClause pimplementsclauseNode6 = (PImplementsClause)nodeArrayList3.get(0);
      PFileBody pfilebodyNode7 = (PFileBody)nodeArrayList4.get(0);
      PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, (PExtendsClause)null, pimplementsclauseNode6, pfilebodyNode7);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new5() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      Object nullNode6 = null;
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PFileType pfiletypeNode4 = (PFileType)nodeArrayList2.get(0);
      PClassName pclassnameNode5 = (PClassName)nodeArrayList3.get(0);
      PImplementsClause pimplementsclauseNode7 = (PImplementsClause)nodeArrayList4.get(0);
      PFileBody pfilebodyNode8 = (PFileBody)nodeArrayList5.get(0);
      PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, (PExtendsClause)null, pimplementsclauseNode7, pfilebodyNode8);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new6() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PFileType pfiletypeNode3 = (PFileType)nodeArrayList1.get(0);
      PClassName pclassnameNode4 = (PClassName)nodeArrayList2.get(0);
      PExtendsClause pextendsclauseNode5 = (PExtendsClause)nodeArrayList3.get(0);
      PImplementsClause pimplementsclauseNode6 = (PImplementsClause)nodeArrayList4.get(0);
      PFileBody pfilebodyNode7 = (PFileBody)nodeArrayList5.get(0);
      PFile pfileNode1 = new AFile(listNode2, pfiletypeNode3, pclassnameNode4, pextendsclauseNode5, pimplementsclauseNode6, pfilebodyNode7);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new7() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PFileType pfiletypeNode4 = (PFileType)nodeArrayList2.get(0);
      PClassName pclassnameNode5 = (PClassName)nodeArrayList3.get(0);
      PExtendsClause pextendsclauseNode6 = (PExtendsClause)nodeArrayList4.get(0);
      PImplementsClause pimplementsclauseNode7 = (PImplementsClause)nodeArrayList5.get(0);
      PFileBody pfilebodyNode8 = (PFileBody)nodeArrayList6.get(0);
      PFile pfileNode1 = new AFile(listNode3, pfiletypeNode4, pclassnameNode5, pextendsclauseNode6, pimplementsclauseNode7, pfilebodyNode8);
      nodeList.add(pfileNode1);
      return nodeList;
   }

   ArrayList<Object> new8() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TAbstract tabstractNode2 = (TAbstract)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AAbstractModifier(tabstractNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new9() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TFinal tfinalNode2 = (TFinal)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AFinalModifier(tfinalNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new10() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNative tnativeNode2 = (TNative)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new ANativeModifier(tnativeNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new11() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TPublic tpublicNode2 = (TPublic)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new APublicModifier(tpublicNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new12() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TProtected tprotectedNode2 = (TProtected)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AProtectedModifier(tprotectedNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new13() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TPrivate tprivateNode2 = (TPrivate)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new APrivateModifier(tprivateNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new14() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TStatic tstaticNode2 = (TStatic)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AStaticModifier(tstaticNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new15() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TSynchronized tsynchronizedNode2 = (TSynchronized)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new ASynchronizedModifier(tsynchronizedNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new16() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TTransient ttransientNode2 = (TTransient)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new ATransientModifier(ttransientNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new17() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TVolatile tvolatileNode2 = (TVolatile)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AVolatileModifier(tvolatileNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new18() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TStrictfp tstrictfpNode2 = (TStrictfp)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AStrictfpModifier(tstrictfpNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new19() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TEnum tenumNode2 = (TEnum)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AEnumModifier(tenumNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new20() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TAnnotation tannotationNode2 = (TAnnotation)nodeArrayList1.get(0);
      PModifier pmodifierNode1 = new AAnnotationModifier(tannotationNode2);
      nodeList.add(pmodifierNode1);
      return nodeList;
   }

   ArrayList<Object> new21() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TClass tclassNode2 = (TClass)nodeArrayList1.get(0);
      PFileType pfiletypeNode1 = new AClassFileType(tclassNode2);
      nodeList.add(pfiletypeNode1);
      return nodeList;
   }

   ArrayList<Object> new22() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TInterface tinterfaceNode2 = (TInterface)nodeArrayList1.get(0);
      PFileType pfiletypeNode1 = new AInterfaceFileType(tinterfaceNode2);
      nodeList.add(pfiletypeNode1);
      return nodeList;
   }

   ArrayList<Object> new23() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TExtends textendsNode2 = (TExtends)nodeArrayList1.get(0);
      PClassName pclassnameNode3 = (PClassName)nodeArrayList2.get(0);
      PExtendsClause pextendsclauseNode1 = new AExtendsClause(textendsNode2, pclassnameNode3);
      nodeList.add(pextendsclauseNode1);
      return nodeList;
   }

   ArrayList<Object> new24() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TImplements timplementsNode2 = (TImplements)nodeArrayList1.get(0);
      PClassNameList pclassnamelistNode3 = (PClassNameList)nodeArrayList2.get(0);
      PImplementsClause pimplementsclauseNode1 = new AImplementsClause(timplementsNode2, pclassnamelistNode3);
      nodeList.add(pimplementsclauseNode1);
      return nodeList;
   }

   ArrayList<Object> new25() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      TRBrace trbraceNode4 = (TRBrace)nodeArrayList2.get(0);
      PFileBody pfilebodyNode1 = new AFileBody(tlbraceNode2, listNode3, trbraceNode4);
      nodeList.add(pfilebodyNode1);
      return nodeList;
   }

   ArrayList<Object> new26() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode3 = (LinkedList)nodeArrayList2.get(0);
      if (listNode3 != null) {
         listNode4.addAll(listNode3);
      }

      TRBrace trbraceNode5 = (TRBrace)nodeArrayList3.get(0);
      PFileBody pfilebodyNode1 = new AFileBody(tlbraceNode2, listNode4, trbraceNode5);
      nodeList.add(pfilebodyNode1);
      return nodeList;
   }

   ArrayList<Object> new27() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PName pnameNode2 = (PName)nodeArrayList1.get(0);
      PNameList pnamelistNode1 = new ASingleNameList(pnameNode2);
      nodeList.add(pnamelistNode1);
      return nodeList;
   }

   ArrayList<Object> new28() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PName pnameNode2 = (PName)nodeArrayList1.get(0);
      TComma tcommaNode3 = (TComma)nodeArrayList2.get(0);
      PNameList pnamelistNode4 = (PNameList)nodeArrayList3.get(0);
      PNameList pnamelistNode1 = new AMultiNameList(pnameNode2, tcommaNode3, pnamelistNode4);
      nodeList.add(pnamelistNode1);
      return nodeList;
   }

   ArrayList<Object> new29() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PClassName pclassnameNode2 = (PClassName)nodeArrayList1.get(0);
      PClassNameList pclassnamelistNode1 = new AClassNameSingleClassNameList(pclassnameNode2);
      nodeList.add(pclassnamelistNode1);
      return nodeList;
   }

   ArrayList<Object> new30() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PClassName pclassnameNode2 = (PClassName)nodeArrayList1.get(0);
      TComma tcommaNode3 = (TComma)nodeArrayList2.get(0);
      PClassNameList pclassnamelistNode4 = (PClassNameList)nodeArrayList3.get(0);
      PClassNameList pclassnamelistNode1 = new AClassNameMultiClassNameList(pclassnameNode2, tcommaNode3, pclassnamelistNode4);
      nodeList.add(pclassnamelistNode1);
      return nodeList;
   }

   ArrayList<Object> new31() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PType ptypeNode3 = (PType)nodeArrayList1.get(0);
      PName pnameNode4 = (PName)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode5 = (TSemicolon)nodeArrayList3.get(0);
      PMember pmemberNode1 = new AFieldMember(listNode2, ptypeNode3, pnameNode4, tsemicolonNode5);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new32() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PType ptypeNode4 = (PType)nodeArrayList2.get(0);
      PName pnameNode5 = (PName)nodeArrayList3.get(0);
      TSemicolon tsemicolonNode6 = (TSemicolon)nodeArrayList4.get(0);
      PMember pmemberNode1 = new AFieldMember(listNode3, ptypeNode4, pnameNode5, tsemicolonNode6);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new33() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      Object nullNode6 = null;
      Object nullNode8 = null;
      PType ptypeNode3 = (PType)nodeArrayList1.get(0);
      PName pnameNode4 = (PName)nodeArrayList2.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList3.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList4.get(0);
      PMethodBody pmethodbodyNode9 = (PMethodBody)nodeArrayList5.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, (PParameterList)null, trparenNode7, (PThrowsClause)null, pmethodbodyNode9);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new34() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      Object nullNode7 = null;
      Object nullNode9 = null;
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PType ptypeNode4 = (PType)nodeArrayList2.get(0);
      PName pnameNode5 = (PName)nodeArrayList3.get(0);
      TLParen tlparenNode6 = (TLParen)nodeArrayList4.get(0);
      TRParen trparenNode8 = (TRParen)nodeArrayList5.get(0);
      PMethodBody pmethodbodyNode10 = (PMethodBody)nodeArrayList6.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, (PParameterList)null, trparenNode8, (PThrowsClause)null, pmethodbodyNode10);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new35() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      Object nullNode8 = null;
      PType ptypeNode3 = (PType)nodeArrayList1.get(0);
      PName pnameNode4 = (PName)nodeArrayList2.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList3.get(0);
      PParameterList pparameterlistNode6 = (PParameterList)nodeArrayList4.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList5.get(0);
      PMethodBody pmethodbodyNode9 = (PMethodBody)nodeArrayList6.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, pparameterlistNode6, trparenNode7, (PThrowsClause)null, pmethodbodyNode9);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new36() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      Object nullNode9 = null;
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PType ptypeNode4 = (PType)nodeArrayList2.get(0);
      PName pnameNode5 = (PName)nodeArrayList3.get(0);
      TLParen tlparenNode6 = (TLParen)nodeArrayList4.get(0);
      PParameterList pparameterlistNode7 = (PParameterList)nodeArrayList5.get(0);
      TRParen trparenNode8 = (TRParen)nodeArrayList6.get(0);
      PMethodBody pmethodbodyNode10 = (PMethodBody)nodeArrayList7.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, pparameterlistNode7, trparenNode8, (PThrowsClause)null, pmethodbodyNode10);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new37() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      Object nullNode6 = null;
      PType ptypeNode3 = (PType)nodeArrayList1.get(0);
      PName pnameNode4 = (PName)nodeArrayList2.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList3.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList4.get(0);
      PThrowsClause pthrowsclauseNode8 = (PThrowsClause)nodeArrayList5.get(0);
      PMethodBody pmethodbodyNode9 = (PMethodBody)nodeArrayList6.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, (PParameterList)null, trparenNode7, pthrowsclauseNode8, pmethodbodyNode9);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new38() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      Object nullNode7 = null;
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PType ptypeNode4 = (PType)nodeArrayList2.get(0);
      PName pnameNode5 = (PName)nodeArrayList3.get(0);
      TLParen tlparenNode6 = (TLParen)nodeArrayList4.get(0);
      TRParen trparenNode8 = (TRParen)nodeArrayList5.get(0);
      PThrowsClause pthrowsclauseNode9 = (PThrowsClause)nodeArrayList6.get(0);
      PMethodBody pmethodbodyNode10 = (PMethodBody)nodeArrayList7.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, (PParameterList)null, trparenNode8, pthrowsclauseNode9, pmethodbodyNode10);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new39() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PType ptypeNode3 = (PType)nodeArrayList1.get(0);
      PName pnameNode4 = (PName)nodeArrayList2.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList3.get(0);
      PParameterList pparameterlistNode6 = (PParameterList)nodeArrayList4.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList5.get(0);
      PThrowsClause pthrowsclauseNode8 = (PThrowsClause)nodeArrayList6.get(0);
      PMethodBody pmethodbodyNode9 = (PMethodBody)nodeArrayList7.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode2, ptypeNode3, pnameNode4, tlparenNode5, pparameterlistNode6, trparenNode7, pthrowsclauseNode8, pmethodbodyNode9);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new40() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode2 = (LinkedList)nodeArrayList1.get(0);
      if (listNode2 != null) {
         listNode3.addAll(listNode2);
      }

      PType ptypeNode4 = (PType)nodeArrayList2.get(0);
      PName pnameNode5 = (PName)nodeArrayList3.get(0);
      TLParen tlparenNode6 = (TLParen)nodeArrayList4.get(0);
      PParameterList pparameterlistNode7 = (PParameterList)nodeArrayList5.get(0);
      TRParen trparenNode8 = (TRParen)nodeArrayList6.get(0);
      PThrowsClause pthrowsclauseNode9 = (PThrowsClause)nodeArrayList7.get(0);
      PMethodBody pmethodbodyNode10 = (PMethodBody)nodeArrayList8.get(0);
      PMember pmemberNode1 = new AMethodMember(listNode3, ptypeNode4, pnameNode5, tlparenNode6, pparameterlistNode7, trparenNode8, pthrowsclauseNode9, pmethodbodyNode10);
      nodeList.add(pmemberNode1);
      return nodeList;
   }

   ArrayList<Object> new41() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TVoid tvoidNode2 = (TVoid)nodeArrayList1.get(0);
      PType ptypeNode1 = new AVoidType(tvoidNode2);
      nodeList.add(ptypeNode1);
      return nodeList;
   }

   ArrayList<Object> new42() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PNonvoidType pnonvoidtypeNode2 = (PNonvoidType)nodeArrayList1.get(0);
      PType ptypeNode1 = new ANovoidType(pnonvoidtypeNode2);
      nodeList.add(ptypeNode1);
      return nodeList;
   }

   ArrayList<Object> new43() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PParameter pparameterNode2 = (PParameter)nodeArrayList1.get(0);
      PParameterList pparameterlistNode1 = new ASingleParameterList(pparameterNode2);
      nodeList.add(pparameterlistNode1);
      return nodeList;
   }

   ArrayList<Object> new44() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PParameter pparameterNode2 = (PParameter)nodeArrayList1.get(0);
      TComma tcommaNode3 = (TComma)nodeArrayList2.get(0);
      PParameterList pparameterlistNode4 = (PParameterList)nodeArrayList3.get(0);
      PParameterList pparameterlistNode1 = new AMultiParameterList(pparameterNode2, tcommaNode3, pparameterlistNode4);
      nodeList.add(pparameterlistNode1);
      return nodeList;
   }

   ArrayList<Object> new45() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PNonvoidType pnonvoidtypeNode2 = (PNonvoidType)nodeArrayList1.get(0);
      PParameter pparameterNode1 = new AParameter(pnonvoidtypeNode2);
      nodeList.add(pparameterNode1);
      return nodeList;
   }

   ArrayList<Object> new46() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TThrows tthrowsNode2 = (TThrows)nodeArrayList1.get(0);
      PClassNameList pclassnamelistNode3 = (PClassNameList)nodeArrayList2.get(0);
      PThrowsClause pthrowsclauseNode1 = new AThrowsClause(tthrowsNode2, pclassnamelistNode3);
      nodeList.add(pthrowsclauseNode1);
      return nodeList;
   }

   ArrayList<Object> new47() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TBoolean tbooleanNode2 = (TBoolean)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new ABooleanBaseTypeNoName(tbooleanNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new48() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TByte tbyteNode2 = (TByte)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new AByteBaseTypeNoName(tbyteNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new49() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TChar tcharNode2 = (TChar)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new ACharBaseTypeNoName(tcharNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new50() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TShort tshortNode2 = (TShort)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new AShortBaseTypeNoName(tshortNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new51() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TInt tintNode2 = (TInt)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new AIntBaseTypeNoName(tintNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new52() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TLong tlongNode2 = (TLong)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new ALongBaseTypeNoName(tlongNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new53() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TFloat tfloatNode2 = (TFloat)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new AFloatBaseTypeNoName(tfloatNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new54() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TDouble tdoubleNode2 = (TDouble)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new ADoubleBaseTypeNoName(tdoubleNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new55() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNullType tnulltypeNode2 = (TNullType)nodeArrayList1.get(0);
      PBaseTypeNoName pbasetypenonameNode1 = new ANullBaseTypeNoName(tnulltypeNode2);
      nodeList.add(pbasetypenonameNode1);
      return nodeList;
   }

   ArrayList<Object> new56() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TBoolean tbooleanNode2 = (TBoolean)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new ABooleanBaseType(tbooleanNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new57() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TByte tbyteNode2 = (TByte)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new AByteBaseType(tbyteNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new58() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TChar tcharNode2 = (TChar)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new ACharBaseType(tcharNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new59() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TShort tshortNode2 = (TShort)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new AShortBaseType(tshortNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new60() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TInt tintNode2 = (TInt)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new AIntBaseType(tintNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new61() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TLong tlongNode2 = (TLong)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new ALongBaseType(tlongNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new62() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TFloat tfloatNode2 = (TFloat)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new AFloatBaseType(tfloatNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new63() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TDouble tdoubleNode2 = (TDouble)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new ADoubleBaseType(tdoubleNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new64() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNullType tnulltypeNode2 = (TNullType)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new ANullBaseType(tnulltypeNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new65() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PClassName pclassnameNode2 = (PClassName)nodeArrayList1.get(0);
      PBaseType pbasetypeNode1 = new AClassNameBaseType(pclassnameNode2);
      nodeList.add(pbasetypeNode1);
      return nodeList;
   }

   ArrayList<Object> new66() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      PBaseTypeNoName pbasetypenonameNode2 = (PBaseTypeNoName)nodeArrayList1.get(0);
      PNonvoidType pnonvoidtypeNode1 = new ABaseNonvoidType(pbasetypenonameNode2, listNode3);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new67() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      PBaseTypeNoName pbasetypenonameNode2 = (PBaseTypeNoName)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode3 = (LinkedList)nodeArrayList2.get(0);
      if (listNode3 != null) {
         listNode4.addAll(listNode3);
      }

      PNonvoidType pnonvoidtypeNode1 = new ABaseNonvoidType(pbasetypenonameNode2, listNode4);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new68() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      TQuotedName tquotednameNode2 = (TQuotedName)nodeArrayList1.get(0);
      PNonvoidType pnonvoidtypeNode1 = new AQuotedNonvoidType(tquotednameNode2, listNode3);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new69() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      TQuotedName tquotednameNode2 = (TQuotedName)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode3 = (LinkedList)nodeArrayList2.get(0);
      if (listNode3 != null) {
         listNode4.addAll(listNode3);
      }

      PNonvoidType pnonvoidtypeNode1 = new AQuotedNonvoidType(tquotednameNode2, listNode4);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new70() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      TIdentifier tidentifierNode2 = (TIdentifier)nodeArrayList1.get(0);
      PNonvoidType pnonvoidtypeNode1 = new AIdentNonvoidType(tidentifierNode2, listNode3);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new71() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      TIdentifier tidentifierNode2 = (TIdentifier)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode3 = (LinkedList)nodeArrayList2.get(0);
      if (listNode3 != null) {
         listNode4.addAll(listNode3);
      }

      PNonvoidType pnonvoidtypeNode1 = new AIdentNonvoidType(tidentifierNode2, listNode4);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new72() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      TFullIdentifier tfullidentifierNode2 = (TFullIdentifier)nodeArrayList1.get(0);
      PNonvoidType pnonvoidtypeNode1 = new AFullIdentNonvoidType(tfullidentifierNode2, listNode3);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new73() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      TFullIdentifier tfullidentifierNode2 = (TFullIdentifier)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode3 = (LinkedList)nodeArrayList2.get(0);
      if (listNode3 != null) {
         listNode4.addAll(listNode3);
      }

      PNonvoidType pnonvoidtypeNode1 = new AFullIdentNonvoidType(tfullidentifierNode2, listNode4);
      nodeList.add(pnonvoidtypeNode1);
      return nodeList;
   }

   ArrayList<Object> new74() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TLBracket tlbracketNode2 = (TLBracket)nodeArrayList1.get(0);
      TRBracket trbracketNode3 = (TRBracket)nodeArrayList2.get(0);
      PArrayBrackets parraybracketsNode1 = new AArrayBrackets(tlbracketNode2, trbracketNode3);
      nodeList.add(parraybracketsNode1);
      return nodeList;
   }

   ArrayList<Object> new75() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TSemicolon tsemicolonNode2 = (TSemicolon)nodeArrayList1.get(0);
      PMethodBody pmethodbodyNode1 = new AEmptyMethodBody(tsemicolonNode2);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new76() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      LinkedList<Object> listNode4 = new LinkedList();
      LinkedList<Object> listNode5 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      TRBrace trbraceNode6 = (TRBrace)nodeArrayList2.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode4, listNode5, trbraceNode6);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new77() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      LinkedList<Object> listNode5 = new LinkedList();
      LinkedList<Object> listNode6 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode3 = (LinkedList)nodeArrayList2.get(0);
      if (listNode3 != null) {
         listNode4.addAll(listNode3);
      }

      TRBrace trbraceNode7 = (TRBrace)nodeArrayList3.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode5, listNode6, trbraceNode7);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new78() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      LinkedList<Object> listNode5 = new LinkedList();
      LinkedList<Object> listNode6 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode4 = (LinkedList)nodeArrayList2.get(0);
      if (listNode4 != null) {
         listNode5.addAll(listNode4);
      }

      TRBrace trbraceNode7 = (TRBrace)nodeArrayList3.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode5, listNode6, trbraceNode7);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new79() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      LinkedList<Object> listNode6 = new LinkedList();
      LinkedList<Object> listNode7 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode5 = (LinkedList)nodeArrayList2.get(0);
      if (listNode5 != null) {
         listNode4.addAll(listNode5);
      }

      new LinkedList();
      listNode5 = (LinkedList)nodeArrayList3.get(0);
      if (listNode5 != null) {
         listNode6.addAll(listNode5);
      }

      TRBrace trbraceNode8 = (TRBrace)nodeArrayList4.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode6, listNode7, trbraceNode8);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new80() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      LinkedList<Object> listNode4 = new LinkedList();
      LinkedList<Object> listNode6 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode5 = (LinkedList)nodeArrayList2.get(0);
      if (listNode5 != null) {
         listNode6.addAll(listNode5);
      }

      TRBrace trbraceNode7 = (TRBrace)nodeArrayList3.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode4, listNode6, trbraceNode7);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new81() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      LinkedList<Object> listNode5 = new LinkedList();
      LinkedList<Object> listNode7 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode6 = (LinkedList)nodeArrayList2.get(0);
      if (listNode6 != null) {
         listNode4.addAll(listNode6);
      }

      new LinkedList();
      listNode6 = (LinkedList)nodeArrayList3.get(0);
      if (listNode6 != null) {
         listNode7.addAll(listNode6);
      }

      TRBrace trbraceNode8 = (TRBrace)nodeArrayList4.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode5, listNode7, trbraceNode8);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new82() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      LinkedList<Object> listNode5 = new LinkedList();
      LinkedList<Object> listNode7 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode6 = (LinkedList)nodeArrayList2.get(0);
      if (listNode6 != null) {
         listNode5.addAll(listNode6);
      }

      new LinkedList();
      listNode6 = (LinkedList)nodeArrayList3.get(0);
      if (listNode6 != null) {
         listNode7.addAll(listNode6);
      }

      TRBrace trbraceNode8 = (TRBrace)nodeArrayList4.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode3, listNode5, listNode7, trbraceNode8);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new83() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode4 = new LinkedList();
      LinkedList<Object> listNode6 = new LinkedList();
      LinkedList<Object> listNode8 = new LinkedList();
      TLBrace tlbraceNode2 = (TLBrace)nodeArrayList1.get(0);
      new LinkedList();
      LinkedList<Object> listNode7 = (LinkedList)nodeArrayList2.get(0);
      if (listNode7 != null) {
         listNode4.addAll(listNode7);
      }

      new LinkedList();
      listNode7 = (LinkedList)nodeArrayList3.get(0);
      if (listNode7 != null) {
         listNode6.addAll(listNode7);
      }

      new LinkedList();
      listNode7 = (LinkedList)nodeArrayList4.get(0);
      if (listNode7 != null) {
         listNode8.addAll(listNode7);
      }

      TRBrace trbraceNode9 = (TRBrace)nodeArrayList5.get(0);
      PMethodBody pmethodbodyNode1 = new AFullMethodBody(tlbraceNode2, listNode4, listNode6, listNode8, trbraceNode9);
      nodeList.add(pmethodbodyNode1);
      return nodeList;
   }

   ArrayList<Object> new84() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PJimpleType pjimpletypeNode2 = (PJimpleType)nodeArrayList1.get(0);
      PLocalNameList plocalnamelistNode3 = (PLocalNameList)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList3.get(0);
      PDeclaration pdeclarationNode1 = new ADeclaration(pjimpletypeNode2, plocalnamelistNode3, tsemicolonNode4);
      nodeList.add(pdeclarationNode1);
      return nodeList;
   }

   ArrayList<Object> new85() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TUnknown tunknownNode2 = (TUnknown)nodeArrayList1.get(0);
      PJimpleType pjimpletypeNode1 = new AUnknownJimpleType(tunknownNode2);
      nodeList.add(pjimpletypeNode1);
      return nodeList;
   }

   ArrayList<Object> new86() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PNonvoidType pnonvoidtypeNode2 = (PNonvoidType)nodeArrayList1.get(0);
      PJimpleType pjimpletypeNode1 = new ANonvoidJimpleType(pnonvoidtypeNode2);
      nodeList.add(pjimpletypeNode1);
      return nodeList;
   }

   ArrayList<Object> new87() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PName pnameNode2 = (PName)nodeArrayList1.get(0);
      PLocalName plocalnameNode1 = new ALocalName(pnameNode2);
      nodeList.add(plocalnameNode1);
      return nodeList;
   }

   ArrayList<Object> new88() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLocalName plocalnameNode2 = (PLocalName)nodeArrayList1.get(0);
      PLocalNameList plocalnamelistNode1 = new ASingleLocalNameList(plocalnameNode2);
      nodeList.add(plocalnamelistNode1);
      return nodeList;
   }

   ArrayList<Object> new89() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLocalName plocalnameNode2 = (PLocalName)nodeArrayList1.get(0);
      TComma tcommaNode3 = (TComma)nodeArrayList2.get(0);
      PLocalNameList plocalnamelistNode4 = (PLocalNameList)nodeArrayList3.get(0);
      PLocalNameList plocalnamelistNode1 = new AMultiLocalNameList(plocalnameNode2, tcommaNode3, plocalnamelistNode4);
      nodeList.add(plocalnamelistNode1);
      return nodeList;
   }

   ArrayList<Object> new90() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLabelName plabelnameNode2 = (PLabelName)nodeArrayList1.get(0);
      TColon tcolonNode3 = (TColon)nodeArrayList2.get(0);
      PStatement pstatementNode1 = new ALabelStatement(plabelnameNode2, tcolonNode3);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new91() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TBreakpoint tbreakpointNode2 = (TBreakpoint)nodeArrayList1.get(0);
      TSemicolon tsemicolonNode3 = (TSemicolon)nodeArrayList2.get(0);
      PStatement pstatementNode1 = new ABreakpointStatement(tbreakpointNode2, tsemicolonNode3);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new92() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TEntermonitor tentermonitorNode2 = (TEntermonitor)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList3.get(0);
      PStatement pstatementNode1 = new AEntermonitorStatement(tentermonitorNode2, pimmediateNode3, tsemicolonNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new93() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TExitmonitor texitmonitorNode2 = (TExitmonitor)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList3.get(0);
      PStatement pstatementNode1 = new AExitmonitorStatement(texitmonitorNode2, pimmediateNode3, tsemicolonNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new94() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode8 = new LinkedList();
      TTableswitch ttableswitchNode2 = (TTableswitch)nodeArrayList1.get(0);
      TLParen tlparenNode3 = (TLParen)nodeArrayList2.get(0);
      PImmediate pimmediateNode4 = (PImmediate)nodeArrayList3.get(0);
      TRParen trparenNode5 = (TRParen)nodeArrayList4.get(0);
      TLBrace tlbraceNode6 = (TLBrace)nodeArrayList5.get(0);
      new LinkedList();
      LinkedList<Object> listNode7 = (LinkedList)nodeArrayList6.get(0);
      if (listNode7 != null) {
         listNode8.addAll(listNode7);
      }

      TRBrace trbraceNode9 = (TRBrace)nodeArrayList7.get(0);
      TSemicolon tsemicolonNode10 = (TSemicolon)nodeArrayList8.get(0);
      PStatement pstatementNode1 = new ATableswitchStatement(ttableswitchNode2, tlparenNode3, pimmediateNode4, trparenNode5, tlbraceNode6, listNode8, trbraceNode9, tsemicolonNode10);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new95() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode8 = new LinkedList();
      TLookupswitch tlookupswitchNode2 = (TLookupswitch)nodeArrayList1.get(0);
      TLParen tlparenNode3 = (TLParen)nodeArrayList2.get(0);
      PImmediate pimmediateNode4 = (PImmediate)nodeArrayList3.get(0);
      TRParen trparenNode5 = (TRParen)nodeArrayList4.get(0);
      TLBrace tlbraceNode6 = (TLBrace)nodeArrayList5.get(0);
      new LinkedList();
      LinkedList<Object> listNode7 = (LinkedList)nodeArrayList6.get(0);
      if (listNode7 != null) {
         listNode8.addAll(listNode7);
      }

      TRBrace trbraceNode9 = (TRBrace)nodeArrayList7.get(0);
      TSemicolon tsemicolonNode10 = (TSemicolon)nodeArrayList8.get(0);
      PStatement pstatementNode1 = new ALookupswitchStatement(tlookupswitchNode2, tlparenNode3, pimmediateNode4, trparenNode5, tlbraceNode6, listNode8, trbraceNode9, tsemicolonNode10);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new96() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLocalName plocalnameNode2 = (PLocalName)nodeArrayList1.get(0);
      TColonEquals tcolonequalsNode3 = (TColonEquals)nodeArrayList2.get(0);
      TAtIdentifier tatidentifierNode4 = (TAtIdentifier)nodeArrayList3.get(0);
      PType ptypeNode5 = (PType)nodeArrayList4.get(0);
      TSemicolon tsemicolonNode6 = (TSemicolon)nodeArrayList5.get(0);
      PStatement pstatementNode1 = new AIdentityStatement(plocalnameNode2, tcolonequalsNode3, tatidentifierNode4, ptypeNode5, tsemicolonNode6);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new97() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLocalName plocalnameNode2 = (PLocalName)nodeArrayList1.get(0);
      TColonEquals tcolonequalsNode3 = (TColonEquals)nodeArrayList2.get(0);
      TAtIdentifier tatidentifierNode4 = (TAtIdentifier)nodeArrayList3.get(0);
      TSemicolon tsemicolonNode5 = (TSemicolon)nodeArrayList4.get(0);
      PStatement pstatementNode1 = new AIdentityNoTypeStatement(plocalnameNode2, tcolonequalsNode3, tatidentifierNode4, tsemicolonNode5);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new98() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PVariable pvariableNode2 = (PVariable)nodeArrayList1.get(0);
      TEquals tequalsNode3 = (TEquals)nodeArrayList2.get(0);
      PExpression pexpressionNode4 = (PExpression)nodeArrayList3.get(0);
      TSemicolon tsemicolonNode5 = (TSemicolon)nodeArrayList4.get(0);
      PStatement pstatementNode1 = new AAssignStatement(pvariableNode2, tequalsNode3, pexpressionNode4, tsemicolonNode5);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new99() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TIf tifNode2 = (TIf)nodeArrayList1.get(0);
      PBoolExpr pboolexprNode3 = (PBoolExpr)nodeArrayList2.get(0);
      PGotoStmt pgotostmtNode4 = (PGotoStmt)nodeArrayList3.get(0);
      PStatement pstatementNode1 = new AIfStatement(tifNode2, pboolexprNode3, pgotostmtNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new100() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PGotoStmt pgotostmtNode2 = (PGotoStmt)nodeArrayList1.get(0);
      PStatement pstatementNode1 = new AGotoStatement(pgotostmtNode2);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new101() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNop tnopNode2 = (TNop)nodeArrayList1.get(0);
      TSemicolon tsemicolonNode3 = (TSemicolon)nodeArrayList2.get(0);
      PStatement pstatementNode1 = new ANopStatement(tnopNode2, tsemicolonNode3);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new102() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode3 = null;
      TRet tretNode2 = (TRet)nodeArrayList1.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList2.get(0);
      PStatement pstatementNode1 = new ARetStatement(tretNode2, (PImmediate)null, tsemicolonNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new103() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TRet tretNode2 = (TRet)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList3.get(0);
      PStatement pstatementNode1 = new ARetStatement(tretNode2, pimmediateNode3, tsemicolonNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new104() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode3 = null;
      TReturn treturnNode2 = (TReturn)nodeArrayList1.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList2.get(0);
      PStatement pstatementNode1 = new AReturnStatement(treturnNode2, (PImmediate)null, tsemicolonNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new105() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TReturn treturnNode2 = (TReturn)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList3.get(0);
      PStatement pstatementNode1 = new AReturnStatement(treturnNode2, pimmediateNode3, tsemicolonNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new106() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TThrow tthrowNode2 = (TThrow)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList3.get(0);
      PStatement pstatementNode1 = new AThrowStatement(tthrowNode2, pimmediateNode3, tsemicolonNode4);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new107() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PInvokeExpr pinvokeexprNode2 = (PInvokeExpr)nodeArrayList1.get(0);
      TSemicolon tsemicolonNode3 = (TSemicolon)nodeArrayList2.get(0);
      PStatement pstatementNode1 = new AInvokeStatement(pinvokeexprNode2, tsemicolonNode3);
      nodeList.add(pstatementNode1);
      return nodeList;
   }

   ArrayList<Object> new108() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TIdentifier tidentifierNode2 = (TIdentifier)nodeArrayList1.get(0);
      PLabelName plabelnameNode1 = new ALabelName(tidentifierNode2);
      nodeList.add(plabelnameNode1);
      return nodeList;
   }

   ArrayList<Object> new109() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PCaseLabel pcaselabelNode2 = (PCaseLabel)nodeArrayList1.get(0);
      TColon tcolonNode3 = (TColon)nodeArrayList2.get(0);
      PGotoStmt pgotostmtNode4 = (PGotoStmt)nodeArrayList3.get(0);
      PCaseStmt pcasestmtNode1 = new ACaseStmt(pcaselabelNode2, tcolonNode3, pgotostmtNode4);
      nodeList.add(pcasestmtNode1);
      return nodeList;
   }

   ArrayList<Object> new110() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode3 = null;
      TCase tcaseNode2 = (TCase)nodeArrayList1.get(0);
      TIntegerConstant tintegerconstantNode4 = (TIntegerConstant)nodeArrayList2.get(0);
      PCaseLabel pcaselabelNode1 = new AConstantCaseLabel(tcaseNode2, (TMinus)null, tintegerconstantNode4);
      nodeList.add(pcaselabelNode1);
      return nodeList;
   }

   ArrayList<Object> new111() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCase tcaseNode2 = (TCase)nodeArrayList1.get(0);
      TMinus tminusNode3 = (TMinus)nodeArrayList2.get(0);
      TIntegerConstant tintegerconstantNode4 = (TIntegerConstant)nodeArrayList3.get(0);
      PCaseLabel pcaselabelNode1 = new AConstantCaseLabel(tcaseNode2, tminusNode3, tintegerconstantNode4);
      nodeList.add(pcaselabelNode1);
      return nodeList;
   }

   ArrayList<Object> new112() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TDefault tdefaultNode2 = (TDefault)nodeArrayList1.get(0);
      PCaseLabel pcaselabelNode1 = new ADefaultCaseLabel(tdefaultNode2);
      nodeList.add(pcaselabelNode1);
      return nodeList;
   }

   ArrayList<Object> new113() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TGoto tgotoNode2 = (TGoto)nodeArrayList1.get(0);
      PLabelName plabelnameNode3 = (PLabelName)nodeArrayList2.get(0);
      TSemicolon tsemicolonNode4 = (TSemicolon)nodeArrayList3.get(0);
      PGotoStmt pgotostmtNode1 = new AGotoStmt(tgotoNode2, plabelnameNode3, tsemicolonNode4);
      nodeList.add(pgotostmtNode1);
      return nodeList;
   }

   ArrayList<Object> new114() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList9 = this.pop();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCatch tcatchNode2 = (TCatch)nodeArrayList1.get(0);
      PClassName pclassnameNode3 = (PClassName)nodeArrayList2.get(0);
      TFrom tfromNode4 = (TFrom)nodeArrayList3.get(0);
      PLabelName plabelnameNode5 = (PLabelName)nodeArrayList4.get(0);
      TTo ttoNode6 = (TTo)nodeArrayList5.get(0);
      PLabelName plabelnameNode7 = (PLabelName)nodeArrayList6.get(0);
      TWith twithNode8 = (TWith)nodeArrayList7.get(0);
      PLabelName plabelnameNode9 = (PLabelName)nodeArrayList8.get(0);
      TSemicolon tsemicolonNode10 = (TSemicolon)nodeArrayList9.get(0);
      PCatchClause pcatchclauseNode1 = new ACatchClause(tcatchNode2, pclassnameNode3, tfromNode4, plabelnameNode5, ttoNode6, plabelnameNode7, twithNode8, plabelnameNode9, tsemicolonNode10);
      nodeList.add(pcatchclauseNode1);
      return nodeList;
   }

   ArrayList<Object> new115() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PNewExpr pnewexprNode2 = (PNewExpr)nodeArrayList1.get(0);
      PExpression pexpressionNode1 = new ANewExpression(pnewexprNode2);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new116() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TLParen tlparenNode2 = (TLParen)nodeArrayList1.get(0);
      PNonvoidType pnonvoidtypeNode3 = (PNonvoidType)nodeArrayList2.get(0);
      TRParen trparenNode4 = (TRParen)nodeArrayList3.get(0);
      PImmediate pimmediateNode5 = (PImmediate)nodeArrayList4.get(0);
      PExpression pexpressionNode1 = new ACastExpression(tlparenNode2, pnonvoidtypeNode3, trparenNode4, pimmediateNode5);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new117() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PImmediate pimmediateNode2 = (PImmediate)nodeArrayList1.get(0);
      TInstanceof tinstanceofNode3 = (TInstanceof)nodeArrayList2.get(0);
      PNonvoidType pnonvoidtypeNode4 = (PNonvoidType)nodeArrayList3.get(0);
      PExpression pexpressionNode1 = new AInstanceofExpression(pimmediateNode2, tinstanceofNode3, pnonvoidtypeNode4);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new118() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PInvokeExpr pinvokeexprNode2 = (PInvokeExpr)nodeArrayList1.get(0);
      PExpression pexpressionNode1 = new AInvokeExpression(pinvokeexprNode2);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new119() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PReference preferenceNode2 = (PReference)nodeArrayList1.get(0);
      PExpression pexpressionNode1 = new AReferenceExpression(preferenceNode2);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new120() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PBinopExpr pbinopexprNode2 = (PBinopExpr)nodeArrayList1.get(0);
      PExpression pexpressionNode1 = new ABinopExpression(pbinopexprNode2);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new121() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PUnopExpr punopexprNode2 = (PUnopExpr)nodeArrayList1.get(0);
      PExpression pexpressionNode1 = new AUnopExpression(punopexprNode2);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new122() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PImmediate pimmediateNode2 = (PImmediate)nodeArrayList1.get(0);
      PExpression pexpressionNode1 = new AImmediateExpression(pimmediateNode2);
      nodeList.add(pexpressionNode1);
      return nodeList;
   }

   ArrayList<Object> new123() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNew tnewNode2 = (TNew)nodeArrayList1.get(0);
      PBaseType pbasetypeNode3 = (PBaseType)nodeArrayList2.get(0);
      PNewExpr pnewexprNode1 = new ASimpleNewExpr(tnewNode2, pbasetypeNode3);
      nodeList.add(pnewexprNode1);
      return nodeList;
   }

   ArrayList<Object> new124() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNewarray tnewarrayNode2 = (TNewarray)nodeArrayList1.get(0);
      TLParen tlparenNode3 = (TLParen)nodeArrayList2.get(0);
      PNonvoidType pnonvoidtypeNode4 = (PNonvoidType)nodeArrayList3.get(0);
      TRParen trparenNode5 = (TRParen)nodeArrayList4.get(0);
      PFixedArrayDescriptor pfixedarraydescriptorNode6 = (PFixedArrayDescriptor)nodeArrayList5.get(0);
      PNewExpr pnewexprNode1 = new AArrayNewExpr(tnewarrayNode2, tlparenNode3, pnonvoidtypeNode4, trparenNode5, pfixedarraydescriptorNode6);
      nodeList.add(pnewexprNode1);
      return nodeList;
   }

   ArrayList<Object> new125() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode7 = new LinkedList();
      TNewmultiarray tnewmultiarrayNode2 = (TNewmultiarray)nodeArrayList1.get(0);
      TLParen tlparenNode3 = (TLParen)nodeArrayList2.get(0);
      PBaseType pbasetypeNode4 = (PBaseType)nodeArrayList3.get(0);
      TRParen trparenNode5 = (TRParen)nodeArrayList4.get(0);
      new LinkedList();
      LinkedList<Object> listNode6 = (LinkedList)nodeArrayList5.get(0);
      if (listNode6 != null) {
         listNode7.addAll(listNode6);
      }

      PNewExpr pnewexprNode1 = new AMultiNewExpr(tnewmultiarrayNode2, tlparenNode3, pbasetypeNode4, trparenNode5, listNode7);
      nodeList.add(pnewexprNode1);
      return nodeList;
   }

   ArrayList<Object> new126() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode3 = null;
      TLBracket tlbracketNode2 = (TLBracket)nodeArrayList1.get(0);
      TRBracket trbracketNode4 = (TRBracket)nodeArrayList2.get(0);
      PArrayDescriptor parraydescriptorNode1 = new AArrayDescriptor(tlbracketNode2, (PImmediate)null, trbracketNode4);
      nodeList.add(parraydescriptorNode1);
      return nodeList;
   }

   ArrayList<Object> new127() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TLBracket tlbracketNode2 = (TLBracket)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      TRBracket trbracketNode4 = (TRBracket)nodeArrayList3.get(0);
      PArrayDescriptor parraydescriptorNode1 = new AArrayDescriptor(tlbracketNode2, pimmediateNode3, trbracketNode4);
      nodeList.add(parraydescriptorNode1);
      return nodeList;
   }

   ArrayList<Object> new128() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PReference preferenceNode2 = (PReference)nodeArrayList1.get(0);
      PVariable pvariableNode1 = new AReferenceVariable(preferenceNode2);
      nodeList.add(pvariableNode1);
      return nodeList;
   }

   ArrayList<Object> new129() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLocalName plocalnameNode2 = (PLocalName)nodeArrayList1.get(0);
      PVariable pvariableNode1 = new ALocalVariable(plocalnameNode2);
      nodeList.add(pvariableNode1);
      return nodeList;
   }

   ArrayList<Object> new130() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PBinopExpr pbinopexprNode2 = (PBinopExpr)nodeArrayList1.get(0);
      PBoolExpr pboolexprNode1 = new ABinopBoolExpr(pbinopexprNode2);
      nodeList.add(pboolexprNode1);
      return nodeList;
   }

   ArrayList<Object> new131() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PUnopExpr punopexprNode2 = (PUnopExpr)nodeArrayList1.get(0);
      PBoolExpr pboolexprNode1 = new AUnopBoolExpr(punopexprNode2);
      nodeList.add(pboolexprNode1);
      return nodeList;
   }

   ArrayList<Object> new132() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode7 = null;
      PNonstaticInvoke pnonstaticinvokeNode2 = (PNonstaticInvoke)nodeArrayList1.get(0);
      PLocalName plocalnameNode3 = (PLocalName)nodeArrayList2.get(0);
      TDot tdotNode4 = (TDot)nodeArrayList3.get(0);
      PMethodSignature pmethodsignatureNode5 = (PMethodSignature)nodeArrayList4.get(0);
      TLParen tlparenNode6 = (TLParen)nodeArrayList5.get(0);
      TRParen trparenNode8 = (TRParen)nodeArrayList6.get(0);
      PInvokeExpr pinvokeexprNode1 = new ANonstaticInvokeExpr(pnonstaticinvokeNode2, plocalnameNode3, tdotNode4, pmethodsignatureNode5, tlparenNode6, (PArgList)null, trparenNode8);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new133() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PNonstaticInvoke pnonstaticinvokeNode2 = (PNonstaticInvoke)nodeArrayList1.get(0);
      PLocalName plocalnameNode3 = (PLocalName)nodeArrayList2.get(0);
      TDot tdotNode4 = (TDot)nodeArrayList3.get(0);
      PMethodSignature pmethodsignatureNode5 = (PMethodSignature)nodeArrayList4.get(0);
      TLParen tlparenNode6 = (TLParen)nodeArrayList5.get(0);
      PArgList parglistNode7 = (PArgList)nodeArrayList6.get(0);
      TRParen trparenNode8 = (TRParen)nodeArrayList7.get(0);
      PInvokeExpr pinvokeexprNode1 = new ANonstaticInvokeExpr(pnonstaticinvokeNode2, plocalnameNode3, tdotNode4, pmethodsignatureNode5, tlparenNode6, parglistNode7, trparenNode8);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new134() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode5 = null;
      TStaticinvoke tstaticinvokeNode2 = (TStaticinvoke)nodeArrayList1.get(0);
      PMethodSignature pmethodsignatureNode3 = (PMethodSignature)nodeArrayList2.get(0);
      TLParen tlparenNode4 = (TLParen)nodeArrayList3.get(0);
      TRParen trparenNode6 = (TRParen)nodeArrayList4.get(0);
      PInvokeExpr pinvokeexprNode1 = new AStaticInvokeExpr(tstaticinvokeNode2, pmethodsignatureNode3, tlparenNode4, (PArgList)null, trparenNode6);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new135() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TStaticinvoke tstaticinvokeNode2 = (TStaticinvoke)nodeArrayList1.get(0);
      PMethodSignature pmethodsignatureNode3 = (PMethodSignature)nodeArrayList2.get(0);
      TLParen tlparenNode4 = (TLParen)nodeArrayList3.get(0);
      PArgList parglistNode5 = (PArgList)nodeArrayList4.get(0);
      TRParen trparenNode6 = (TRParen)nodeArrayList5.get(0);
      PInvokeExpr pinvokeexprNode1 = new AStaticInvokeExpr(tstaticinvokeNode2, pmethodsignatureNode3, tlparenNode4, parglistNode5, trparenNode6);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new136() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode6 = null;
      Object nullNode10 = null;
      TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke)nodeArrayList1.get(0);
      TStringConstant tstringconstantNode3 = (TStringConstant)nodeArrayList2.get(0);
      PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature)nodeArrayList3.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList4.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList5.get(0);
      PMethodSignature pmethodsignatureNode8 = (PMethodSignature)nodeArrayList6.get(0);
      TLParen tlparenNode9 = (TLParen)nodeArrayList7.get(0);
      TRParen trparenNode11 = (TRParen)nodeArrayList8.get(0);
      PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, (PArgList)null, trparenNode7, pmethodsignatureNode8, tlparenNode9, (PArgList)null, trparenNode11);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new137() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList9 = this.pop();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode10 = null;
      TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke)nodeArrayList1.get(0);
      TStringConstant tstringconstantNode3 = (TStringConstant)nodeArrayList2.get(0);
      PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature)nodeArrayList3.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList4.get(0);
      PArgList parglistNode6 = (PArgList)nodeArrayList5.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList6.get(0);
      PMethodSignature pmethodsignatureNode8 = (PMethodSignature)nodeArrayList7.get(0);
      TLParen tlparenNode9 = (TLParen)nodeArrayList8.get(0);
      TRParen trparenNode11 = (TRParen)nodeArrayList9.get(0);
      PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, parglistNode6, trparenNode7, pmethodsignatureNode8, tlparenNode9, (PArgList)null, trparenNode11);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new138() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList9 = this.pop();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode6 = null;
      TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke)nodeArrayList1.get(0);
      TStringConstant tstringconstantNode3 = (TStringConstant)nodeArrayList2.get(0);
      PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature)nodeArrayList3.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList4.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList5.get(0);
      PMethodSignature pmethodsignatureNode8 = (PMethodSignature)nodeArrayList6.get(0);
      TLParen tlparenNode9 = (TLParen)nodeArrayList7.get(0);
      PArgList parglistNode10 = (PArgList)nodeArrayList8.get(0);
      TRParen trparenNode11 = (TRParen)nodeArrayList9.get(0);
      PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, (PArgList)null, trparenNode7, pmethodsignatureNode8, tlparenNode9, parglistNode10, trparenNode11);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new139() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList10 = this.pop();
      ArrayList<Object> nodeArrayList9 = this.pop();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TDynamicinvoke tdynamicinvokeNode2 = (TDynamicinvoke)nodeArrayList1.get(0);
      TStringConstant tstringconstantNode3 = (TStringConstant)nodeArrayList2.get(0);
      PUnnamedMethodSignature punnamedmethodsignatureNode4 = (PUnnamedMethodSignature)nodeArrayList3.get(0);
      TLParen tlparenNode5 = (TLParen)nodeArrayList4.get(0);
      PArgList parglistNode6 = (PArgList)nodeArrayList5.get(0);
      TRParen trparenNode7 = (TRParen)nodeArrayList6.get(0);
      PMethodSignature pmethodsignatureNode8 = (PMethodSignature)nodeArrayList7.get(0);
      TLParen tlparenNode9 = (TLParen)nodeArrayList8.get(0);
      PArgList parglistNode10 = (PArgList)nodeArrayList9.get(0);
      TRParen trparenNode11 = (TRParen)nodeArrayList10.get(0);
      PInvokeExpr pinvokeexprNode1 = new ADynamicInvokeExpr(tdynamicinvokeNode2, tstringconstantNode3, punnamedmethodsignatureNode4, tlparenNode5, parglistNode6, trparenNode7, pmethodsignatureNode8, tlparenNode9, parglistNode10, trparenNode11);
      nodeList.add(pinvokeexprNode1);
      return nodeList;
   }

   ArrayList<Object> new140() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PImmediate pimmediateNode2 = (PImmediate)nodeArrayList1.get(0);
      PBinop pbinopNode3 = (PBinop)nodeArrayList2.get(0);
      PImmediate pimmediateNode4 = (PImmediate)nodeArrayList3.get(0);
      PBinopExpr pbinopexprNode1 = new ABinopExpr(pimmediateNode2, pbinopNode3, pimmediateNode4);
      nodeList.add(pbinopexprNode1);
      return nodeList;
   }

   ArrayList<Object> new141() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PUnop punopNode2 = (PUnop)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      PUnopExpr punopexprNode1 = new AUnopExpr(punopNode2, pimmediateNode3);
      nodeList.add(punopexprNode1);
      return nodeList;
   }

   ArrayList<Object> new142() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TSpecialinvoke tspecialinvokeNode2 = (TSpecialinvoke)nodeArrayList1.get(0);
      PNonstaticInvoke pnonstaticinvokeNode1 = new ASpecialNonstaticInvoke(tspecialinvokeNode2);
      nodeList.add(pnonstaticinvokeNode1);
      return nodeList;
   }

   ArrayList<Object> new143() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TVirtualinvoke tvirtualinvokeNode2 = (TVirtualinvoke)nodeArrayList1.get(0);
      PNonstaticInvoke pnonstaticinvokeNode1 = new AVirtualNonstaticInvoke(tvirtualinvokeNode2);
      nodeList.add(pnonstaticinvokeNode1);
      return nodeList;
   }

   ArrayList<Object> new144() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TInterfaceinvoke tinterfaceinvokeNode2 = (TInterfaceinvoke)nodeArrayList1.get(0);
      PNonstaticInvoke pnonstaticinvokeNode1 = new AInterfaceNonstaticInvoke(tinterfaceinvokeNode2);
      nodeList.add(pnonstaticinvokeNode1);
      return nodeList;
   }

   ArrayList<Object> new145() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode5 = null;
      TCmplt tcmpltNode2 = (TCmplt)nodeArrayList1.get(0);
      PType ptypeNode3 = (PType)nodeArrayList2.get(0);
      TLParen tlparenNode4 = (TLParen)nodeArrayList3.get(0);
      TRParen trparenNode6 = (TRParen)nodeArrayList4.get(0);
      TCmpgt tcmpgtNode7 = (TCmpgt)nodeArrayList5.get(0);
      PUnnamedMethodSignature punnamedmethodsignatureNode1 = new AUnnamedMethodSignature(tcmpltNode2, ptypeNode3, tlparenNode4, (PParameterList)null, trparenNode6, tcmpgtNode7);
      nodeList.add(punnamedmethodsignatureNode1);
      return nodeList;
   }

   ArrayList<Object> new146() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmplt tcmpltNode2 = (TCmplt)nodeArrayList1.get(0);
      PType ptypeNode3 = (PType)nodeArrayList2.get(0);
      TLParen tlparenNode4 = (TLParen)nodeArrayList3.get(0);
      PParameterList pparameterlistNode5 = (PParameterList)nodeArrayList4.get(0);
      TRParen trparenNode6 = (TRParen)nodeArrayList5.get(0);
      TCmpgt tcmpgtNode7 = (TCmpgt)nodeArrayList6.get(0);
      PUnnamedMethodSignature punnamedmethodsignatureNode1 = new AUnnamedMethodSignature(tcmpltNode2, ptypeNode3, tlparenNode4, pparameterlistNode5, trparenNode6, tcmpgtNode7);
      nodeList.add(punnamedmethodsignatureNode1);
      return nodeList;
   }

   ArrayList<Object> new147() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode8 = null;
      TCmplt tcmpltNode2 = (TCmplt)nodeArrayList1.get(0);
      PClassName pclassnameNode3 = (PClassName)nodeArrayList2.get(0);
      TColon tcolonNode4 = (TColon)nodeArrayList3.get(0);
      PType ptypeNode5 = (PType)nodeArrayList4.get(0);
      PName pnameNode6 = (PName)nodeArrayList5.get(0);
      TLParen tlparenNode7 = (TLParen)nodeArrayList6.get(0);
      TRParen trparenNode9 = (TRParen)nodeArrayList7.get(0);
      TCmpgt tcmpgtNode10 = (TCmpgt)nodeArrayList8.get(0);
      PMethodSignature pmethodsignatureNode1 = new AMethodSignature(tcmpltNode2, pclassnameNode3, tcolonNode4, ptypeNode5, pnameNode6, tlparenNode7, (PParameterList)null, trparenNode9, tcmpgtNode10);
      nodeList.add(pmethodsignatureNode1);
      return nodeList;
   }

   ArrayList<Object> new148() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList9 = this.pop();
      ArrayList<Object> nodeArrayList8 = this.pop();
      ArrayList<Object> nodeArrayList7 = this.pop();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmplt tcmpltNode2 = (TCmplt)nodeArrayList1.get(0);
      PClassName pclassnameNode3 = (PClassName)nodeArrayList2.get(0);
      TColon tcolonNode4 = (TColon)nodeArrayList3.get(0);
      PType ptypeNode5 = (PType)nodeArrayList4.get(0);
      PName pnameNode6 = (PName)nodeArrayList5.get(0);
      TLParen tlparenNode7 = (TLParen)nodeArrayList6.get(0);
      PParameterList pparameterlistNode8 = (PParameterList)nodeArrayList7.get(0);
      TRParen trparenNode9 = (TRParen)nodeArrayList8.get(0);
      TCmpgt tcmpgtNode10 = (TCmpgt)nodeArrayList9.get(0);
      PMethodSignature pmethodsignatureNode1 = new AMethodSignature(tcmpltNode2, pclassnameNode3, tcolonNode4, ptypeNode5, pnameNode6, tlparenNode7, pparameterlistNode8, trparenNode9, tcmpgtNode10);
      nodeList.add(pmethodsignatureNode1);
      return nodeList;
   }

   ArrayList<Object> new149() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PArrayRef parrayrefNode2 = (PArrayRef)nodeArrayList1.get(0);
      PReference preferenceNode1 = new AArrayReference(parrayrefNode2);
      nodeList.add(preferenceNode1);
      return nodeList;
   }

   ArrayList<Object> new150() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PFieldRef pfieldrefNode2 = (PFieldRef)nodeArrayList1.get(0);
      PReference preferenceNode1 = new AFieldReference(pfieldrefNode2);
      nodeList.add(preferenceNode1);
      return nodeList;
   }

   ArrayList<Object> new151() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TIdentifier tidentifierNode2 = (TIdentifier)nodeArrayList1.get(0);
      PFixedArrayDescriptor pfixedarraydescriptorNode3 = (PFixedArrayDescriptor)nodeArrayList2.get(0);
      PArrayRef parrayrefNode1 = new AIdentArrayRef(tidentifierNode2, pfixedarraydescriptorNode3);
      nodeList.add(parrayrefNode1);
      return nodeList;
   }

   ArrayList<Object> new152() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TQuotedName tquotednameNode2 = (TQuotedName)nodeArrayList1.get(0);
      PFixedArrayDescriptor pfixedarraydescriptorNode3 = (PFixedArrayDescriptor)nodeArrayList2.get(0);
      PArrayRef parrayrefNode1 = new AQuotedArrayRef(tquotednameNode2, pfixedarraydescriptorNode3);
      nodeList.add(parrayrefNode1);
      return nodeList;
   }

   ArrayList<Object> new153() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLocalName plocalnameNode2 = (PLocalName)nodeArrayList1.get(0);
      TDot tdotNode3 = (TDot)nodeArrayList2.get(0);
      PFieldSignature pfieldsignatureNode4 = (PFieldSignature)nodeArrayList3.get(0);
      PFieldRef pfieldrefNode1 = new ALocalFieldRef(plocalnameNode2, tdotNode3, pfieldsignatureNode4);
      nodeList.add(pfieldrefNode1);
      return nodeList;
   }

   ArrayList<Object> new154() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PFieldSignature pfieldsignatureNode2 = (PFieldSignature)nodeArrayList1.get(0);
      PFieldRef pfieldrefNode1 = new ASigFieldRef(pfieldsignatureNode2);
      nodeList.add(pfieldrefNode1);
      return nodeList;
   }

   ArrayList<Object> new155() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList6 = this.pop();
      ArrayList<Object> nodeArrayList5 = this.pop();
      ArrayList<Object> nodeArrayList4 = this.pop();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmplt tcmpltNode2 = (TCmplt)nodeArrayList1.get(0);
      PClassName pclassnameNode3 = (PClassName)nodeArrayList2.get(0);
      TColon tcolonNode4 = (TColon)nodeArrayList3.get(0);
      PType ptypeNode5 = (PType)nodeArrayList4.get(0);
      PName pnameNode6 = (PName)nodeArrayList5.get(0);
      TCmpgt tcmpgtNode7 = (TCmpgt)nodeArrayList6.get(0);
      PFieldSignature pfieldsignatureNode1 = new AFieldSignature(tcmpltNode2, pclassnameNode3, tcolonNode4, ptypeNode5, pnameNode6, tcmpgtNode7);
      nodeList.add(pfieldsignatureNode1);
      return nodeList;
   }

   ArrayList<Object> new156() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TLBracket tlbracketNode2 = (TLBracket)nodeArrayList1.get(0);
      PImmediate pimmediateNode3 = (PImmediate)nodeArrayList2.get(0);
      TRBracket trbracketNode4 = (TRBracket)nodeArrayList3.get(0);
      PFixedArrayDescriptor pfixedarraydescriptorNode1 = new AFixedArrayDescriptor(tlbracketNode2, pimmediateNode3, trbracketNode4);
      nodeList.add(pfixedarraydescriptorNode1);
      return nodeList;
   }

   ArrayList<Object> new157() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PImmediate pimmediateNode2 = (PImmediate)nodeArrayList1.get(0);
      PArgList parglistNode1 = new ASingleArgList(pimmediateNode2);
      nodeList.add(parglistNode1);
      return nodeList;
   }

   ArrayList<Object> new158() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList3 = this.pop();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PImmediate pimmediateNode2 = (PImmediate)nodeArrayList1.get(0);
      TComma tcommaNode3 = (TComma)nodeArrayList2.get(0);
      PArgList parglistNode4 = (PArgList)nodeArrayList3.get(0);
      PArgList parglistNode1 = new AMultiArgList(pimmediateNode2, tcommaNode3, parglistNode4);
      nodeList.add(parglistNode1);
      return nodeList;
   }

   ArrayList<Object> new159() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PLocalName plocalnameNode2 = (PLocalName)nodeArrayList1.get(0);
      PImmediate pimmediateNode1 = new ALocalImmediate(plocalnameNode2);
      nodeList.add(pimmediateNode1);
      return nodeList;
   }

   ArrayList<Object> new160() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      PConstant pconstantNode2 = (PConstant)nodeArrayList1.get(0);
      PImmediate pimmediateNode1 = new AConstantImmediate(pconstantNode2);
      nodeList.add(pimmediateNode1);
      return nodeList;
   }

   ArrayList<Object> new161() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode2 = null;
      TIntegerConstant tintegerconstantNode3 = (TIntegerConstant)nodeArrayList1.get(0);
      PConstant pconstantNode1 = new AIntegerConstant((TMinus)null, tintegerconstantNode3);
      nodeList.add(pconstantNode1);
      return nodeList;
   }

   ArrayList<Object> new162() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TMinus tminusNode2 = (TMinus)nodeArrayList1.get(0);
      TIntegerConstant tintegerconstantNode3 = (TIntegerConstant)nodeArrayList2.get(0);
      PConstant pconstantNode1 = new AIntegerConstant(tminusNode2, tintegerconstantNode3);
      nodeList.add(pconstantNode1);
      return nodeList;
   }

   ArrayList<Object> new163() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      Object nullNode2 = null;
      TFloatConstant tfloatconstantNode3 = (TFloatConstant)nodeArrayList1.get(0);
      PConstant pconstantNode1 = new AFloatConstant((TMinus)null, tfloatconstantNode3);
      nodeList.add(pconstantNode1);
      return nodeList;
   }

   ArrayList<Object> new164() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TMinus tminusNode2 = (TMinus)nodeArrayList1.get(0);
      TFloatConstant tfloatconstantNode3 = (TFloatConstant)nodeArrayList2.get(0);
      PConstant pconstantNode1 = new AFloatConstant(tminusNode2, tfloatconstantNode3);
      nodeList.add(pconstantNode1);
      return nodeList;
   }

   ArrayList<Object> new165() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TStringConstant tstringconstantNode2 = (TStringConstant)nodeArrayList1.get(0);
      PConstant pconstantNode1 = new AStringConstant(tstringconstantNode2);
      nodeList.add(pconstantNode1);
      return nodeList;
   }

   ArrayList<Object> new166() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TClass tclassNode2 = (TClass)nodeArrayList1.get(0);
      TStringConstant tstringconstantNode3 = (TStringConstant)nodeArrayList2.get(0);
      PConstant pconstantNode1 = new AClzzConstant(tclassNode2, tstringconstantNode3);
      nodeList.add(pconstantNode1);
      return nodeList;
   }

   ArrayList<Object> new167() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNull tnullNode2 = (TNull)nodeArrayList1.get(0);
      PConstant pconstantNode1 = new ANullConstant(tnullNode2);
      nodeList.add(pconstantNode1);
      return nodeList;
   }

   ArrayList<Object> new168() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TAnd tandNode2 = (TAnd)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AAndBinop(tandNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new169() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TOr torNode2 = (TOr)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AOrBinop(torNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new170() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TXor txorNode2 = (TXor)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AXorBinop(txorNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new171() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TMod tmodNode2 = (TMod)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AModBinop(tmodNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new172() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmp tcmpNode2 = (TCmp)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpBinop(tcmpNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new173() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmpg tcmpgNode2 = (TCmpg)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpgBinop(tcmpgNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new174() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmpl tcmplNode2 = (TCmpl)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmplBinop(tcmplNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new175() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmpeq tcmpeqNode2 = (TCmpeq)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpeqBinop(tcmpeqNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new176() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmpne tcmpneNode2 = (TCmpne)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpneBinop(tcmpneNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new177() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmpgt tcmpgtNode2 = (TCmpgt)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpgtBinop(tcmpgtNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new178() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmpge tcmpgeNode2 = (TCmpge)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpgeBinop(tcmpgeNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new179() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmplt tcmpltNode2 = (TCmplt)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpltBinop(tcmpltNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new180() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TCmple tcmpleNode2 = (TCmple)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ACmpleBinop(tcmpleNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new181() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TShl tshlNode2 = (TShl)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AShlBinop(tshlNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new182() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TShr tshrNode2 = (TShr)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AShrBinop(tshrNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new183() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TUshr tushrNode2 = (TUshr)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AUshrBinop(tushrNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new184() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TPlus tplusNode2 = (TPlus)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new APlusBinop(tplusNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new185() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TMinus tminusNode2 = (TMinus)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AMinusBinop(tminusNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new186() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TMult tmultNode2 = (TMult)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new AMultBinop(tmultNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new187() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TDiv tdivNode2 = (TDiv)nodeArrayList1.get(0);
      PBinop pbinopNode1 = new ADivBinop(tdivNode2);
      nodeList.add(pbinopNode1);
      return nodeList;
   }

   ArrayList<Object> new188() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TLengthof tlengthofNode2 = (TLengthof)nodeArrayList1.get(0);
      PUnop punopNode1 = new ALengthofUnop(tlengthofNode2);
      nodeList.add(punopNode1);
      return nodeList;
   }

   ArrayList<Object> new189() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TNeg tnegNode2 = (TNeg)nodeArrayList1.get(0);
      PUnop punopNode1 = new ANegUnop(tnegNode2);
      nodeList.add(punopNode1);
      return nodeList;
   }

   ArrayList<Object> new190() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TQuotedName tquotednameNode2 = (TQuotedName)nodeArrayList1.get(0);
      PClassName pclassnameNode1 = new AQuotedClassName(tquotednameNode2);
      nodeList.add(pclassnameNode1);
      return nodeList;
   }

   ArrayList<Object> new191() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TIdentifier tidentifierNode2 = (TIdentifier)nodeArrayList1.get(0);
      PClassName pclassnameNode1 = new AIdentClassName(tidentifierNode2);
      nodeList.add(pclassnameNode1);
      return nodeList;
   }

   ArrayList<Object> new192() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TFullIdentifier tfullidentifierNode2 = (TFullIdentifier)nodeArrayList1.get(0);
      PClassName pclassnameNode1 = new AFullIdentClassName(tfullidentifierNode2);
      nodeList.add(pclassnameNode1);
      return nodeList;
   }

   ArrayList<Object> new193() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TQuotedName tquotednameNode2 = (TQuotedName)nodeArrayList1.get(0);
      PName pnameNode1 = new AQuotedName(tquotednameNode2);
      nodeList.add(pnameNode1);
      return nodeList;
   }

   ArrayList<Object> new194() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      TIdentifier tidentifierNode2 = (TIdentifier)nodeArrayList1.get(0);
      PName pnameNode1 = new AIdentName(tidentifierNode2);
      nodeList.add(pnameNode1);
      return nodeList;
   }

   ArrayList<Object> new195() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PModifier pmodifierNode1 = (PModifier)nodeArrayList1.get(0);
      if (pmodifierNode1 != null) {
         listNode2.add(pmodifierNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new196() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PModifier pmodifierNode2 = (PModifier)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (pmodifierNode2 != null) {
         listNode3.add(pmodifierNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   ArrayList<Object> new197() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PMember pmemberNode1 = (PMember)nodeArrayList1.get(0);
      if (pmemberNode1 != null) {
         listNode2.add(pmemberNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new198() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PMember pmemberNode2 = (PMember)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (pmemberNode2 != null) {
         listNode3.add(pmemberNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   ArrayList<Object> new199() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PArrayBrackets parraybracketsNode1 = (PArrayBrackets)nodeArrayList1.get(0);
      if (parraybracketsNode1 != null) {
         listNode2.add(parraybracketsNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new200() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PArrayBrackets parraybracketsNode2 = (PArrayBrackets)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (parraybracketsNode2 != null) {
         listNode3.add(parraybracketsNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   ArrayList<Object> new201() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PDeclaration pdeclarationNode1 = (PDeclaration)nodeArrayList1.get(0);
      if (pdeclarationNode1 != null) {
         listNode2.add(pdeclarationNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new202() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PDeclaration pdeclarationNode2 = (PDeclaration)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (pdeclarationNode2 != null) {
         listNode3.add(pdeclarationNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   ArrayList<Object> new203() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PStatement pstatementNode1 = (PStatement)nodeArrayList1.get(0);
      if (pstatementNode1 != null) {
         listNode2.add(pstatementNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new204() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PStatement pstatementNode2 = (PStatement)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (pstatementNode2 != null) {
         listNode3.add(pstatementNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   ArrayList<Object> new205() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PCatchClause pcatchclauseNode1 = (PCatchClause)nodeArrayList1.get(0);
      if (pcatchclauseNode1 != null) {
         listNode2.add(pcatchclauseNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new206() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PCatchClause pcatchclauseNode2 = (PCatchClause)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (pcatchclauseNode2 != null) {
         listNode3.add(pcatchclauseNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   ArrayList<Object> new207() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PCaseStmt pcasestmtNode1 = (PCaseStmt)nodeArrayList1.get(0);
      if (pcasestmtNode1 != null) {
         listNode2.add(pcasestmtNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new208() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PCaseStmt pcasestmtNode2 = (PCaseStmt)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (pcasestmtNode2 != null) {
         listNode3.add(pcasestmtNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   ArrayList<Object> new209() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode2 = new LinkedList();
      PArrayDescriptor parraydescriptorNode1 = (PArrayDescriptor)nodeArrayList1.get(0);
      if (parraydescriptorNode1 != null) {
         listNode2.add(parraydescriptorNode1);
      }

      nodeList.add(listNode2);
      return nodeList;
   }

   ArrayList<Object> new210() {
      ArrayList<Object> nodeList = new ArrayList();
      ArrayList<Object> nodeArrayList2 = this.pop();
      ArrayList<Object> nodeArrayList1 = this.pop();
      LinkedList<Object> listNode3 = new LinkedList();
      new LinkedList();
      LinkedList<Object> listNode1 = (LinkedList)nodeArrayList1.get(0);
      PArrayDescriptor parraydescriptorNode2 = (PArrayDescriptor)nodeArrayList2.get(0);
      if (listNode1 != null) {
         listNode3.addAll(listNode1);
      }

      if (parraydescriptorNode2 != null) {
         listNode3.add(parraydescriptorNode2);
      }

      nodeList.add(listNode3);
      return nodeList;
   }

   static {
      try {
         DataInputStream s = new DataInputStream(new BufferedInputStream(Parser.class.getResourceAsStream("/parser.dat")));
         int length = s.readInt();
         actionTable = new int[length][][];

         int i;
         int j;
         int j;
         for(i = 0; i < actionTable.length; ++i) {
            length = s.readInt();
            actionTable[i] = new int[length][3];

            for(j = 0; j < actionTable[i].length; ++j) {
               for(j = 0; j < 3; ++j) {
                  actionTable[i][j][j] = s.readInt();
               }
            }
         }

         length = s.readInt();
         gotoTable = new int[length][][];

         for(i = 0; i < gotoTable.length; ++i) {
            length = s.readInt();
            gotoTable[i] = new int[length][2];

            for(j = 0; j < gotoTable[i].length; ++j) {
               for(j = 0; j < 2; ++j) {
                  gotoTable[i][j][j] = s.readInt();
               }
            }
         }

         length = s.readInt();
         errorMessages = new String[length];

         for(i = 0; i < errorMessages.length; ++i) {
            length = s.readInt();
            StringBuffer buffer = new StringBuffer();

            for(j = 0; j < length; ++j) {
               buffer.append(s.readChar());
            }

            errorMessages[i] = buffer.toString();
         }

         length = s.readInt();
         errors = new int[length];

         for(i = 0; i < errors.length; ++i) {
            errors[i] = s.readInt();
         }

         s.close();
      } catch (Exception var5) {
         throw new RuntimeException("The file \"parser.dat\" is either missing or corrupted.");
      }
   }
}
