package soot.jimple.parser.analysis;

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
import soot.jimple.parser.node.Switch;
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

public interface Analysis extends Switch {
   Object getIn(Node var1);

   void setIn(Node var1, Object var2);

   Object getOut(Node var1);

   void setOut(Node var1, Object var2);

   void caseStart(Start var1);

   void caseAFile(AFile var1);

   void caseAAbstractModifier(AAbstractModifier var1);

   void caseAFinalModifier(AFinalModifier var1);

   void caseANativeModifier(ANativeModifier var1);

   void caseAPublicModifier(APublicModifier var1);

   void caseAProtectedModifier(AProtectedModifier var1);

   void caseAPrivateModifier(APrivateModifier var1);

   void caseAStaticModifier(AStaticModifier var1);

   void caseASynchronizedModifier(ASynchronizedModifier var1);

   void caseATransientModifier(ATransientModifier var1);

   void caseAVolatileModifier(AVolatileModifier var1);

   void caseAStrictfpModifier(AStrictfpModifier var1);

   void caseAEnumModifier(AEnumModifier var1);

   void caseAAnnotationModifier(AAnnotationModifier var1);

   void caseAClassFileType(AClassFileType var1);

   void caseAInterfaceFileType(AInterfaceFileType var1);

   void caseAExtendsClause(AExtendsClause var1);

   void caseAImplementsClause(AImplementsClause var1);

   void caseAFileBody(AFileBody var1);

   void caseASingleNameList(ASingleNameList var1);

   void caseAMultiNameList(AMultiNameList var1);

   void caseAClassNameSingleClassNameList(AClassNameSingleClassNameList var1);

   void caseAClassNameMultiClassNameList(AClassNameMultiClassNameList var1);

   void caseAFieldMember(AFieldMember var1);

   void caseAMethodMember(AMethodMember var1);

   void caseAVoidType(AVoidType var1);

   void caseANovoidType(ANovoidType var1);

   void caseASingleParameterList(ASingleParameterList var1);

   void caseAMultiParameterList(AMultiParameterList var1);

   void caseAParameter(AParameter var1);

   void caseAThrowsClause(AThrowsClause var1);

   void caseABooleanBaseTypeNoName(ABooleanBaseTypeNoName var1);

   void caseAByteBaseTypeNoName(AByteBaseTypeNoName var1);

   void caseACharBaseTypeNoName(ACharBaseTypeNoName var1);

   void caseAShortBaseTypeNoName(AShortBaseTypeNoName var1);

   void caseAIntBaseTypeNoName(AIntBaseTypeNoName var1);

   void caseALongBaseTypeNoName(ALongBaseTypeNoName var1);

   void caseAFloatBaseTypeNoName(AFloatBaseTypeNoName var1);

   void caseADoubleBaseTypeNoName(ADoubleBaseTypeNoName var1);

   void caseANullBaseTypeNoName(ANullBaseTypeNoName var1);

   void caseABooleanBaseType(ABooleanBaseType var1);

   void caseAByteBaseType(AByteBaseType var1);

   void caseACharBaseType(ACharBaseType var1);

   void caseAShortBaseType(AShortBaseType var1);

   void caseAIntBaseType(AIntBaseType var1);

   void caseALongBaseType(ALongBaseType var1);

   void caseAFloatBaseType(AFloatBaseType var1);

   void caseADoubleBaseType(ADoubleBaseType var1);

   void caseANullBaseType(ANullBaseType var1);

   void caseAClassNameBaseType(AClassNameBaseType var1);

   void caseABaseNonvoidType(ABaseNonvoidType var1);

   void caseAQuotedNonvoidType(AQuotedNonvoidType var1);

   void caseAIdentNonvoidType(AIdentNonvoidType var1);

   void caseAFullIdentNonvoidType(AFullIdentNonvoidType var1);

   void caseAArrayBrackets(AArrayBrackets var1);

   void caseAEmptyMethodBody(AEmptyMethodBody var1);

   void caseAFullMethodBody(AFullMethodBody var1);

   void caseADeclaration(ADeclaration var1);

   void caseAUnknownJimpleType(AUnknownJimpleType var1);

   void caseANonvoidJimpleType(ANonvoidJimpleType var1);

   void caseALocalName(ALocalName var1);

   void caseASingleLocalNameList(ASingleLocalNameList var1);

   void caseAMultiLocalNameList(AMultiLocalNameList var1);

   void caseALabelStatement(ALabelStatement var1);

   void caseABreakpointStatement(ABreakpointStatement var1);

   void caseAEntermonitorStatement(AEntermonitorStatement var1);

   void caseAExitmonitorStatement(AExitmonitorStatement var1);

   void caseATableswitchStatement(ATableswitchStatement var1);

   void caseALookupswitchStatement(ALookupswitchStatement var1);

   void caseAIdentityStatement(AIdentityStatement var1);

   void caseAIdentityNoTypeStatement(AIdentityNoTypeStatement var1);

   void caseAAssignStatement(AAssignStatement var1);

   void caseAIfStatement(AIfStatement var1);

   void caseAGotoStatement(AGotoStatement var1);

   void caseANopStatement(ANopStatement var1);

   void caseARetStatement(ARetStatement var1);

   void caseAReturnStatement(AReturnStatement var1);

   void caseAThrowStatement(AThrowStatement var1);

   void caseAInvokeStatement(AInvokeStatement var1);

   void caseALabelName(ALabelName var1);

   void caseACaseStmt(ACaseStmt var1);

   void caseAConstantCaseLabel(AConstantCaseLabel var1);

   void caseADefaultCaseLabel(ADefaultCaseLabel var1);

   void caseAGotoStmt(AGotoStmt var1);

   void caseACatchClause(ACatchClause var1);

   void caseANewExpression(ANewExpression var1);

   void caseACastExpression(ACastExpression var1);

   void caseAInstanceofExpression(AInstanceofExpression var1);

   void caseAInvokeExpression(AInvokeExpression var1);

   void caseAReferenceExpression(AReferenceExpression var1);

   void caseABinopExpression(ABinopExpression var1);

   void caseAUnopExpression(AUnopExpression var1);

   void caseAImmediateExpression(AImmediateExpression var1);

   void caseASimpleNewExpr(ASimpleNewExpr var1);

   void caseAArrayNewExpr(AArrayNewExpr var1);

   void caseAMultiNewExpr(AMultiNewExpr var1);

   void caseAArrayDescriptor(AArrayDescriptor var1);

   void caseAReferenceVariable(AReferenceVariable var1);

   void caseALocalVariable(ALocalVariable var1);

   void caseABinopBoolExpr(ABinopBoolExpr var1);

   void caseAUnopBoolExpr(AUnopBoolExpr var1);

   void caseANonstaticInvokeExpr(ANonstaticInvokeExpr var1);

   void caseAStaticInvokeExpr(AStaticInvokeExpr var1);

   void caseADynamicInvokeExpr(ADynamicInvokeExpr var1);

   void caseABinopExpr(ABinopExpr var1);

   void caseAUnopExpr(AUnopExpr var1);

   void caseASpecialNonstaticInvoke(ASpecialNonstaticInvoke var1);

   void caseAVirtualNonstaticInvoke(AVirtualNonstaticInvoke var1);

   void caseAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke var1);

   void caseAUnnamedMethodSignature(AUnnamedMethodSignature var1);

   void caseAMethodSignature(AMethodSignature var1);

   void caseAArrayReference(AArrayReference var1);

   void caseAFieldReference(AFieldReference var1);

   void caseAIdentArrayRef(AIdentArrayRef var1);

   void caseAQuotedArrayRef(AQuotedArrayRef var1);

   void caseALocalFieldRef(ALocalFieldRef var1);

   void caseASigFieldRef(ASigFieldRef var1);

   void caseAFieldSignature(AFieldSignature var1);

   void caseAFixedArrayDescriptor(AFixedArrayDescriptor var1);

   void caseASingleArgList(ASingleArgList var1);

   void caseAMultiArgList(AMultiArgList var1);

   void caseALocalImmediate(ALocalImmediate var1);

   void caseAConstantImmediate(AConstantImmediate var1);

   void caseAIntegerConstant(AIntegerConstant var1);

   void caseAFloatConstant(AFloatConstant var1);

   void caseAStringConstant(AStringConstant var1);

   void caseAClzzConstant(AClzzConstant var1);

   void caseANullConstant(ANullConstant var1);

   void caseAAndBinop(AAndBinop var1);

   void caseAOrBinop(AOrBinop var1);

   void caseAXorBinop(AXorBinop var1);

   void caseAModBinop(AModBinop var1);

   void caseACmpBinop(ACmpBinop var1);

   void caseACmpgBinop(ACmpgBinop var1);

   void caseACmplBinop(ACmplBinop var1);

   void caseACmpeqBinop(ACmpeqBinop var1);

   void caseACmpneBinop(ACmpneBinop var1);

   void caseACmpgtBinop(ACmpgtBinop var1);

   void caseACmpgeBinop(ACmpgeBinop var1);

   void caseACmpltBinop(ACmpltBinop var1);

   void caseACmpleBinop(ACmpleBinop var1);

   void caseAShlBinop(AShlBinop var1);

   void caseAShrBinop(AShrBinop var1);

   void caseAUshrBinop(AUshrBinop var1);

   void caseAPlusBinop(APlusBinop var1);

   void caseAMinusBinop(AMinusBinop var1);

   void caseAMultBinop(AMultBinop var1);

   void caseADivBinop(ADivBinop var1);

   void caseALengthofUnop(ALengthofUnop var1);

   void caseANegUnop(ANegUnop var1);

   void caseAQuotedClassName(AQuotedClassName var1);

   void caseAIdentClassName(AIdentClassName var1);

   void caseAFullIdentClassName(AFullIdentClassName var1);

   void caseAQuotedName(AQuotedName var1);

   void caseAIdentName(AIdentName var1);

   void caseTIgnored(TIgnored var1);

   void caseTAbstract(TAbstract var1);

   void caseTFinal(TFinal var1);

   void caseTNative(TNative var1);

   void caseTPublic(TPublic var1);

   void caseTProtected(TProtected var1);

   void caseTPrivate(TPrivate var1);

   void caseTStatic(TStatic var1);

   void caseTSynchronized(TSynchronized var1);

   void caseTTransient(TTransient var1);

   void caseTVolatile(TVolatile var1);

   void caseTStrictfp(TStrictfp var1);

   void caseTEnum(TEnum var1);

   void caseTAnnotation(TAnnotation var1);

   void caseTClass(TClass var1);

   void caseTInterface(TInterface var1);

   void caseTVoid(TVoid var1);

   void caseTBoolean(TBoolean var1);

   void caseTByte(TByte var1);

   void caseTShort(TShort var1);

   void caseTChar(TChar var1);

   void caseTInt(TInt var1);

   void caseTLong(TLong var1);

   void caseTFloat(TFloat var1);

   void caseTDouble(TDouble var1);

   void caseTNullType(TNullType var1);

   void caseTUnknown(TUnknown var1);

   void caseTExtends(TExtends var1);

   void caseTImplements(TImplements var1);

   void caseTBreakpoint(TBreakpoint var1);

   void caseTCase(TCase var1);

   void caseTCatch(TCatch var1);

   void caseTCmp(TCmp var1);

   void caseTCmpg(TCmpg var1);

   void caseTCmpl(TCmpl var1);

   void caseTDefault(TDefault var1);

   void caseTEntermonitor(TEntermonitor var1);

   void caseTExitmonitor(TExitmonitor var1);

   void caseTGoto(TGoto var1);

   void caseTIf(TIf var1);

   void caseTInstanceof(TInstanceof var1);

   void caseTInterfaceinvoke(TInterfaceinvoke var1);

   void caseTLengthof(TLengthof var1);

   void caseTLookupswitch(TLookupswitch var1);

   void caseTNeg(TNeg var1);

   void caseTNew(TNew var1);

   void caseTNewarray(TNewarray var1);

   void caseTNewmultiarray(TNewmultiarray var1);

   void caseTNop(TNop var1);

   void caseTRet(TRet var1);

   void caseTReturn(TReturn var1);

   void caseTSpecialinvoke(TSpecialinvoke var1);

   void caseTStaticinvoke(TStaticinvoke var1);

   void caseTDynamicinvoke(TDynamicinvoke var1);

   void caseTTableswitch(TTableswitch var1);

   void caseTThrow(TThrow var1);

   void caseTThrows(TThrows var1);

   void caseTVirtualinvoke(TVirtualinvoke var1);

   void caseTNull(TNull var1);

   void caseTFrom(TFrom var1);

   void caseTTo(TTo var1);

   void caseTWith(TWith var1);

   void caseTCls(TCls var1);

   void caseTComma(TComma var1);

   void caseTLBrace(TLBrace var1);

   void caseTRBrace(TRBrace var1);

   void caseTSemicolon(TSemicolon var1);

   void caseTLBracket(TLBracket var1);

   void caseTRBracket(TRBracket var1);

   void caseTLParen(TLParen var1);

   void caseTRParen(TRParen var1);

   void caseTColon(TColon var1);

   void caseTDot(TDot var1);

   void caseTQuote(TQuote var1);

   void caseTColonEquals(TColonEquals var1);

   void caseTEquals(TEquals var1);

   void caseTAnd(TAnd var1);

   void caseTOr(TOr var1);

   void caseTXor(TXor var1);

   void caseTMod(TMod var1);

   void caseTCmpeq(TCmpeq var1);

   void caseTCmpne(TCmpne var1);

   void caseTCmpgt(TCmpgt var1);

   void caseTCmpge(TCmpge var1);

   void caseTCmplt(TCmplt var1);

   void caseTCmple(TCmple var1);

   void caseTShl(TShl var1);

   void caseTShr(TShr var1);

   void caseTUshr(TUshr var1);

   void caseTPlus(TPlus var1);

   void caseTMinus(TMinus var1);

   void caseTMult(TMult var1);

   void caseTDiv(TDiv var1);

   void caseTQuotedName(TQuotedName var1);

   void caseTFullIdentifier(TFullIdentifier var1);

   void caseTIdentifier(TIdentifier var1);

   void caseTAtIdentifier(TAtIdentifier var1);

   void caseTBoolConstant(TBoolConstant var1);

   void caseTIntegerConstant(TIntegerConstant var1);

   void caseTFloatConstant(TFloatConstant var1);

   void caseTStringConstant(TStringConstant var1);

   void caseEOF(EOF var1);
}
