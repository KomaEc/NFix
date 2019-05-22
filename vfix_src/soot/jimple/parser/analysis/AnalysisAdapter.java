package soot.jimple.parser.analysis;

import java.util.Hashtable;
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
import soot.jimple.parser.node.Node;
import soot.jimple.parser.node.Start;
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

public class AnalysisAdapter implements Analysis {
   private Hashtable<Node, Object> in;
   private Hashtable<Node, Object> out;

   public Object getIn(Node node) {
      return this.in == null ? null : this.in.get(node);
   }

   public void setIn(Node node, Object o) {
      if (this.in == null) {
         this.in = new Hashtable(1);
      }

      if (o != null) {
         this.in.put(node, o);
      } else {
         this.in.remove(node);
      }

   }

   public Object getOut(Node node) {
      return this.out == null ? null : this.out.get(node);
   }

   public void setOut(Node node, Object o) {
      if (this.out == null) {
         this.out = new Hashtable(1);
      }

      if (o != null) {
         this.out.put(node, o);
      } else {
         this.out.remove(node);
      }

   }

   public void caseStart(Start node) {
      this.defaultCase(node);
   }

   public void caseAFile(AFile node) {
      this.defaultCase(node);
   }

   public void caseAAbstractModifier(AAbstractModifier node) {
      this.defaultCase(node);
   }

   public void caseAFinalModifier(AFinalModifier node) {
      this.defaultCase(node);
   }

   public void caseANativeModifier(ANativeModifier node) {
      this.defaultCase(node);
   }

   public void caseAPublicModifier(APublicModifier node) {
      this.defaultCase(node);
   }

   public void caseAProtectedModifier(AProtectedModifier node) {
      this.defaultCase(node);
   }

   public void caseAPrivateModifier(APrivateModifier node) {
      this.defaultCase(node);
   }

   public void caseAStaticModifier(AStaticModifier node) {
      this.defaultCase(node);
   }

   public void caseASynchronizedModifier(ASynchronizedModifier node) {
      this.defaultCase(node);
   }

   public void caseATransientModifier(ATransientModifier node) {
      this.defaultCase(node);
   }

   public void caseAVolatileModifier(AVolatileModifier node) {
      this.defaultCase(node);
   }

   public void caseAStrictfpModifier(AStrictfpModifier node) {
      this.defaultCase(node);
   }

   public void caseAEnumModifier(AEnumModifier node) {
      this.defaultCase(node);
   }

   public void caseAAnnotationModifier(AAnnotationModifier node) {
      this.defaultCase(node);
   }

   public void caseAClassFileType(AClassFileType node) {
      this.defaultCase(node);
   }

   public void caseAInterfaceFileType(AInterfaceFileType node) {
      this.defaultCase(node);
   }

   public void caseAExtendsClause(AExtendsClause node) {
      this.defaultCase(node);
   }

   public void caseAImplementsClause(AImplementsClause node) {
      this.defaultCase(node);
   }

   public void caseAFileBody(AFileBody node) {
      this.defaultCase(node);
   }

   public void caseASingleNameList(ASingleNameList node) {
      this.defaultCase(node);
   }

   public void caseAMultiNameList(AMultiNameList node) {
      this.defaultCase(node);
   }

   public void caseAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
      this.defaultCase(node);
   }

   public void caseAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
      this.defaultCase(node);
   }

   public void caseAFieldMember(AFieldMember node) {
      this.defaultCase(node);
   }

   public void caseAMethodMember(AMethodMember node) {
      this.defaultCase(node);
   }

   public void caseAVoidType(AVoidType node) {
      this.defaultCase(node);
   }

   public void caseANovoidType(ANovoidType node) {
      this.defaultCase(node);
   }

   public void caseASingleParameterList(ASingleParameterList node) {
      this.defaultCase(node);
   }

   public void caseAMultiParameterList(AMultiParameterList node) {
      this.defaultCase(node);
   }

   public void caseAParameter(AParameter node) {
      this.defaultCase(node);
   }

   public void caseAThrowsClause(AThrowsClause node) {
      this.defaultCase(node);
   }

   public void caseABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseAByteBaseTypeNoName(AByteBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseACharBaseTypeNoName(ACharBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseAShortBaseTypeNoName(AShortBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseAIntBaseTypeNoName(AIntBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseALongBaseTypeNoName(ALongBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseANullBaseTypeNoName(ANullBaseTypeNoName node) {
      this.defaultCase(node);
   }

   public void caseABooleanBaseType(ABooleanBaseType node) {
      this.defaultCase(node);
   }

   public void caseAByteBaseType(AByteBaseType node) {
      this.defaultCase(node);
   }

   public void caseACharBaseType(ACharBaseType node) {
      this.defaultCase(node);
   }

   public void caseAShortBaseType(AShortBaseType node) {
      this.defaultCase(node);
   }

   public void caseAIntBaseType(AIntBaseType node) {
      this.defaultCase(node);
   }

   public void caseALongBaseType(ALongBaseType node) {
      this.defaultCase(node);
   }

   public void caseAFloatBaseType(AFloatBaseType node) {
      this.defaultCase(node);
   }

   public void caseADoubleBaseType(ADoubleBaseType node) {
      this.defaultCase(node);
   }

   public void caseANullBaseType(ANullBaseType node) {
      this.defaultCase(node);
   }

   public void caseAClassNameBaseType(AClassNameBaseType node) {
      this.defaultCase(node);
   }

   public void caseABaseNonvoidType(ABaseNonvoidType node) {
      this.defaultCase(node);
   }

   public void caseAQuotedNonvoidType(AQuotedNonvoidType node) {
      this.defaultCase(node);
   }

   public void caseAIdentNonvoidType(AIdentNonvoidType node) {
      this.defaultCase(node);
   }

   public void caseAFullIdentNonvoidType(AFullIdentNonvoidType node) {
      this.defaultCase(node);
   }

   public void caseAArrayBrackets(AArrayBrackets node) {
      this.defaultCase(node);
   }

   public void caseAEmptyMethodBody(AEmptyMethodBody node) {
      this.defaultCase(node);
   }

   public void caseAFullMethodBody(AFullMethodBody node) {
      this.defaultCase(node);
   }

   public void caseADeclaration(ADeclaration node) {
      this.defaultCase(node);
   }

   public void caseAUnknownJimpleType(AUnknownJimpleType node) {
      this.defaultCase(node);
   }

   public void caseANonvoidJimpleType(ANonvoidJimpleType node) {
      this.defaultCase(node);
   }

   public void caseALocalName(ALocalName node) {
      this.defaultCase(node);
   }

   public void caseASingleLocalNameList(ASingleLocalNameList node) {
      this.defaultCase(node);
   }

   public void caseAMultiLocalNameList(AMultiLocalNameList node) {
      this.defaultCase(node);
   }

   public void caseALabelStatement(ALabelStatement node) {
      this.defaultCase(node);
   }

   public void caseABreakpointStatement(ABreakpointStatement node) {
      this.defaultCase(node);
   }

   public void caseAEntermonitorStatement(AEntermonitorStatement node) {
      this.defaultCase(node);
   }

   public void caseAExitmonitorStatement(AExitmonitorStatement node) {
      this.defaultCase(node);
   }

   public void caseATableswitchStatement(ATableswitchStatement node) {
      this.defaultCase(node);
   }

   public void caseALookupswitchStatement(ALookupswitchStatement node) {
      this.defaultCase(node);
   }

   public void caseAIdentityStatement(AIdentityStatement node) {
      this.defaultCase(node);
   }

   public void caseAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
      this.defaultCase(node);
   }

   public void caseAAssignStatement(AAssignStatement node) {
      this.defaultCase(node);
   }

   public void caseAIfStatement(AIfStatement node) {
      this.defaultCase(node);
   }

   public void caseAGotoStatement(AGotoStatement node) {
      this.defaultCase(node);
   }

   public void caseANopStatement(ANopStatement node) {
      this.defaultCase(node);
   }

   public void caseARetStatement(ARetStatement node) {
      this.defaultCase(node);
   }

   public void caseAReturnStatement(AReturnStatement node) {
      this.defaultCase(node);
   }

   public void caseAThrowStatement(AThrowStatement node) {
      this.defaultCase(node);
   }

   public void caseAInvokeStatement(AInvokeStatement node) {
      this.defaultCase(node);
   }

   public void caseALabelName(ALabelName node) {
      this.defaultCase(node);
   }

   public void caseACaseStmt(ACaseStmt node) {
      this.defaultCase(node);
   }

   public void caseAConstantCaseLabel(AConstantCaseLabel node) {
      this.defaultCase(node);
   }

   public void caseADefaultCaseLabel(ADefaultCaseLabel node) {
      this.defaultCase(node);
   }

   public void caseAGotoStmt(AGotoStmt node) {
      this.defaultCase(node);
   }

   public void caseACatchClause(ACatchClause node) {
      this.defaultCase(node);
   }

   public void caseANewExpression(ANewExpression node) {
      this.defaultCase(node);
   }

   public void caseACastExpression(ACastExpression node) {
      this.defaultCase(node);
   }

   public void caseAInstanceofExpression(AInstanceofExpression node) {
      this.defaultCase(node);
   }

   public void caseAInvokeExpression(AInvokeExpression node) {
      this.defaultCase(node);
   }

   public void caseAReferenceExpression(AReferenceExpression node) {
      this.defaultCase(node);
   }

   public void caseABinopExpression(ABinopExpression node) {
      this.defaultCase(node);
   }

   public void caseAUnopExpression(AUnopExpression node) {
      this.defaultCase(node);
   }

   public void caseAImmediateExpression(AImmediateExpression node) {
      this.defaultCase(node);
   }

   public void caseASimpleNewExpr(ASimpleNewExpr node) {
      this.defaultCase(node);
   }

   public void caseAArrayNewExpr(AArrayNewExpr node) {
      this.defaultCase(node);
   }

   public void caseAMultiNewExpr(AMultiNewExpr node) {
      this.defaultCase(node);
   }

   public void caseAArrayDescriptor(AArrayDescriptor node) {
      this.defaultCase(node);
   }

   public void caseAReferenceVariable(AReferenceVariable node) {
      this.defaultCase(node);
   }

   public void caseALocalVariable(ALocalVariable node) {
      this.defaultCase(node);
   }

   public void caseABinopBoolExpr(ABinopBoolExpr node) {
      this.defaultCase(node);
   }

   public void caseAUnopBoolExpr(AUnopBoolExpr node) {
      this.defaultCase(node);
   }

   public void caseANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
      this.defaultCase(node);
   }

   public void caseAStaticInvokeExpr(AStaticInvokeExpr node) {
      this.defaultCase(node);
   }

   public void caseADynamicInvokeExpr(ADynamicInvokeExpr node) {
      this.defaultCase(node);
   }

   public void caseABinopExpr(ABinopExpr node) {
      this.defaultCase(node);
   }

   public void caseAUnopExpr(AUnopExpr node) {
      this.defaultCase(node);
   }

   public void caseASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
      this.defaultCase(node);
   }

   public void caseAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
      this.defaultCase(node);
   }

   public void caseAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
      this.defaultCase(node);
   }

   public void caseAUnnamedMethodSignature(AUnnamedMethodSignature node) {
      this.defaultCase(node);
   }

   public void caseAMethodSignature(AMethodSignature node) {
      this.defaultCase(node);
   }

   public void caseAArrayReference(AArrayReference node) {
      this.defaultCase(node);
   }

   public void caseAFieldReference(AFieldReference node) {
      this.defaultCase(node);
   }

   public void caseAIdentArrayRef(AIdentArrayRef node) {
      this.defaultCase(node);
   }

   public void caseAQuotedArrayRef(AQuotedArrayRef node) {
      this.defaultCase(node);
   }

   public void caseALocalFieldRef(ALocalFieldRef node) {
      this.defaultCase(node);
   }

   public void caseASigFieldRef(ASigFieldRef node) {
      this.defaultCase(node);
   }

   public void caseAFieldSignature(AFieldSignature node) {
      this.defaultCase(node);
   }

   public void caseAFixedArrayDescriptor(AFixedArrayDescriptor node) {
      this.defaultCase(node);
   }

   public void caseASingleArgList(ASingleArgList node) {
      this.defaultCase(node);
   }

   public void caseAMultiArgList(AMultiArgList node) {
      this.defaultCase(node);
   }

   public void caseALocalImmediate(ALocalImmediate node) {
      this.defaultCase(node);
   }

   public void caseAConstantImmediate(AConstantImmediate node) {
      this.defaultCase(node);
   }

   public void caseAIntegerConstant(AIntegerConstant node) {
      this.defaultCase(node);
   }

   public void caseAFloatConstant(AFloatConstant node) {
      this.defaultCase(node);
   }

   public void caseAStringConstant(AStringConstant node) {
      this.defaultCase(node);
   }

   public void caseAClzzConstant(AClzzConstant node) {
      this.defaultCase(node);
   }

   public void caseANullConstant(ANullConstant node) {
      this.defaultCase(node);
   }

   public void caseAAndBinop(AAndBinop node) {
      this.defaultCase(node);
   }

   public void caseAOrBinop(AOrBinop node) {
      this.defaultCase(node);
   }

   public void caseAXorBinop(AXorBinop node) {
      this.defaultCase(node);
   }

   public void caseAModBinop(AModBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpBinop(ACmpBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpgBinop(ACmpgBinop node) {
      this.defaultCase(node);
   }

   public void caseACmplBinop(ACmplBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpeqBinop(ACmpeqBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpneBinop(ACmpneBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpgtBinop(ACmpgtBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpgeBinop(ACmpgeBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpltBinop(ACmpltBinop node) {
      this.defaultCase(node);
   }

   public void caseACmpleBinop(ACmpleBinop node) {
      this.defaultCase(node);
   }

   public void caseAShlBinop(AShlBinop node) {
      this.defaultCase(node);
   }

   public void caseAShrBinop(AShrBinop node) {
      this.defaultCase(node);
   }

   public void caseAUshrBinop(AUshrBinop node) {
      this.defaultCase(node);
   }

   public void caseAPlusBinop(APlusBinop node) {
      this.defaultCase(node);
   }

   public void caseAMinusBinop(AMinusBinop node) {
      this.defaultCase(node);
   }

   public void caseAMultBinop(AMultBinop node) {
      this.defaultCase(node);
   }

   public void caseADivBinop(ADivBinop node) {
      this.defaultCase(node);
   }

   public void caseALengthofUnop(ALengthofUnop node) {
      this.defaultCase(node);
   }

   public void caseANegUnop(ANegUnop node) {
      this.defaultCase(node);
   }

   public void caseAQuotedClassName(AQuotedClassName node) {
      this.defaultCase(node);
   }

   public void caseAIdentClassName(AIdentClassName node) {
      this.defaultCase(node);
   }

   public void caseAFullIdentClassName(AFullIdentClassName node) {
      this.defaultCase(node);
   }

   public void caseAQuotedName(AQuotedName node) {
      this.defaultCase(node);
   }

   public void caseAIdentName(AIdentName node) {
      this.defaultCase(node);
   }

   public void caseTIgnored(TIgnored node) {
      this.defaultCase(node);
   }

   public void caseTAbstract(TAbstract node) {
      this.defaultCase(node);
   }

   public void caseTFinal(TFinal node) {
      this.defaultCase(node);
   }

   public void caseTNative(TNative node) {
      this.defaultCase(node);
   }

   public void caseTPublic(TPublic node) {
      this.defaultCase(node);
   }

   public void caseTProtected(TProtected node) {
      this.defaultCase(node);
   }

   public void caseTPrivate(TPrivate node) {
      this.defaultCase(node);
   }

   public void caseTStatic(TStatic node) {
      this.defaultCase(node);
   }

   public void caseTSynchronized(TSynchronized node) {
      this.defaultCase(node);
   }

   public void caseTTransient(TTransient node) {
      this.defaultCase(node);
   }

   public void caseTVolatile(TVolatile node) {
      this.defaultCase(node);
   }

   public void caseTStrictfp(TStrictfp node) {
      this.defaultCase(node);
   }

   public void caseTEnum(TEnum node) {
      this.defaultCase(node);
   }

   public void caseTAnnotation(TAnnotation node) {
      this.defaultCase(node);
   }

   public void caseTClass(TClass node) {
      this.defaultCase(node);
   }

   public void caseTInterface(TInterface node) {
      this.defaultCase(node);
   }

   public void caseTVoid(TVoid node) {
      this.defaultCase(node);
   }

   public void caseTBoolean(TBoolean node) {
      this.defaultCase(node);
   }

   public void caseTByte(TByte node) {
      this.defaultCase(node);
   }

   public void caseTShort(TShort node) {
      this.defaultCase(node);
   }

   public void caseTChar(TChar node) {
      this.defaultCase(node);
   }

   public void caseTInt(TInt node) {
      this.defaultCase(node);
   }

   public void caseTLong(TLong node) {
      this.defaultCase(node);
   }

   public void caseTFloat(TFloat node) {
      this.defaultCase(node);
   }

   public void caseTDouble(TDouble node) {
      this.defaultCase(node);
   }

   public void caseTNullType(TNullType node) {
      this.defaultCase(node);
   }

   public void caseTUnknown(TUnknown node) {
      this.defaultCase(node);
   }

   public void caseTExtends(TExtends node) {
      this.defaultCase(node);
   }

   public void caseTImplements(TImplements node) {
      this.defaultCase(node);
   }

   public void caseTBreakpoint(TBreakpoint node) {
      this.defaultCase(node);
   }

   public void caseTCase(TCase node) {
      this.defaultCase(node);
   }

   public void caseTCatch(TCatch node) {
      this.defaultCase(node);
   }

   public void caseTCmp(TCmp node) {
      this.defaultCase(node);
   }

   public void caseTCmpg(TCmpg node) {
      this.defaultCase(node);
   }

   public void caseTCmpl(TCmpl node) {
      this.defaultCase(node);
   }

   public void caseTDefault(TDefault node) {
      this.defaultCase(node);
   }

   public void caseTEntermonitor(TEntermonitor node) {
      this.defaultCase(node);
   }

   public void caseTExitmonitor(TExitmonitor node) {
      this.defaultCase(node);
   }

   public void caseTGoto(TGoto node) {
      this.defaultCase(node);
   }

   public void caseTIf(TIf node) {
      this.defaultCase(node);
   }

   public void caseTInstanceof(TInstanceof node) {
      this.defaultCase(node);
   }

   public void caseTInterfaceinvoke(TInterfaceinvoke node) {
      this.defaultCase(node);
   }

   public void caseTLengthof(TLengthof node) {
      this.defaultCase(node);
   }

   public void caseTLookupswitch(TLookupswitch node) {
      this.defaultCase(node);
   }

   public void caseTNeg(TNeg node) {
      this.defaultCase(node);
   }

   public void caseTNew(TNew node) {
      this.defaultCase(node);
   }

   public void caseTNewarray(TNewarray node) {
      this.defaultCase(node);
   }

   public void caseTNewmultiarray(TNewmultiarray node) {
      this.defaultCase(node);
   }

   public void caseTNop(TNop node) {
      this.defaultCase(node);
   }

   public void caseTRet(TRet node) {
      this.defaultCase(node);
   }

   public void caseTReturn(TReturn node) {
      this.defaultCase(node);
   }

   public void caseTSpecialinvoke(TSpecialinvoke node) {
      this.defaultCase(node);
   }

   public void caseTStaticinvoke(TStaticinvoke node) {
      this.defaultCase(node);
   }

   public void caseTDynamicinvoke(TDynamicinvoke node) {
      this.defaultCase(node);
   }

   public void caseTTableswitch(TTableswitch node) {
      this.defaultCase(node);
   }

   public void caseTThrow(TThrow node) {
      this.defaultCase(node);
   }

   public void caseTThrows(TThrows node) {
      this.defaultCase(node);
   }

   public void caseTVirtualinvoke(TVirtualinvoke node) {
      this.defaultCase(node);
   }

   public void caseTNull(TNull node) {
      this.defaultCase(node);
   }

   public void caseTFrom(TFrom node) {
      this.defaultCase(node);
   }

   public void caseTTo(TTo node) {
      this.defaultCase(node);
   }

   public void caseTWith(TWith node) {
      this.defaultCase(node);
   }

   public void caseTCls(TCls node) {
      this.defaultCase(node);
   }

   public void caseTComma(TComma node) {
      this.defaultCase(node);
   }

   public void caseTLBrace(TLBrace node) {
      this.defaultCase(node);
   }

   public void caseTRBrace(TRBrace node) {
      this.defaultCase(node);
   }

   public void caseTSemicolon(TSemicolon node) {
      this.defaultCase(node);
   }

   public void caseTLBracket(TLBracket node) {
      this.defaultCase(node);
   }

   public void caseTRBracket(TRBracket node) {
      this.defaultCase(node);
   }

   public void caseTLParen(TLParen node) {
      this.defaultCase(node);
   }

   public void caseTRParen(TRParen node) {
      this.defaultCase(node);
   }

   public void caseTColon(TColon node) {
      this.defaultCase(node);
   }

   public void caseTDot(TDot node) {
      this.defaultCase(node);
   }

   public void caseTQuote(TQuote node) {
      this.defaultCase(node);
   }

   public void caseTColonEquals(TColonEquals node) {
      this.defaultCase(node);
   }

   public void caseTEquals(TEquals node) {
      this.defaultCase(node);
   }

   public void caseTAnd(TAnd node) {
      this.defaultCase(node);
   }

   public void caseTOr(TOr node) {
      this.defaultCase(node);
   }

   public void caseTXor(TXor node) {
      this.defaultCase(node);
   }

   public void caseTMod(TMod node) {
      this.defaultCase(node);
   }

   public void caseTCmpeq(TCmpeq node) {
      this.defaultCase(node);
   }

   public void caseTCmpne(TCmpne node) {
      this.defaultCase(node);
   }

   public void caseTCmpgt(TCmpgt node) {
      this.defaultCase(node);
   }

   public void caseTCmpge(TCmpge node) {
      this.defaultCase(node);
   }

   public void caseTCmplt(TCmplt node) {
      this.defaultCase(node);
   }

   public void caseTCmple(TCmple node) {
      this.defaultCase(node);
   }

   public void caseTShl(TShl node) {
      this.defaultCase(node);
   }

   public void caseTShr(TShr node) {
      this.defaultCase(node);
   }

   public void caseTUshr(TUshr node) {
      this.defaultCase(node);
   }

   public void caseTPlus(TPlus node) {
      this.defaultCase(node);
   }

   public void caseTMinus(TMinus node) {
      this.defaultCase(node);
   }

   public void caseTMult(TMult node) {
      this.defaultCase(node);
   }

   public void caseTDiv(TDiv node) {
      this.defaultCase(node);
   }

   public void caseTQuotedName(TQuotedName node) {
      this.defaultCase(node);
   }

   public void caseTFullIdentifier(TFullIdentifier node) {
      this.defaultCase(node);
   }

   public void caseTIdentifier(TIdentifier node) {
      this.defaultCase(node);
   }

   public void caseTAtIdentifier(TAtIdentifier node) {
      this.defaultCase(node);
   }

   public void caseTBoolConstant(TBoolConstant node) {
      this.defaultCase(node);
   }

   public void caseTIntegerConstant(TIntegerConstant node) {
      this.defaultCase(node);
   }

   public void caseTFloatConstant(TFloatConstant node) {
      this.defaultCase(node);
   }

   public void caseTStringConstant(TStringConstant node) {
      this.defaultCase(node);
   }

   public void caseEOF(EOF node) {
      this.defaultCase(node);
   }

   public void defaultCase(Node node) {
   }
}
