package soot.jimple.parser.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import soot.jimple.parser.node.Node;
import soot.jimple.parser.node.PArrayBrackets;
import soot.jimple.parser.node.PArrayDescriptor;
import soot.jimple.parser.node.PCaseStmt;
import soot.jimple.parser.node.PCatchClause;
import soot.jimple.parser.node.PDeclaration;
import soot.jimple.parser.node.PMember;
import soot.jimple.parser.node.PModifier;
import soot.jimple.parser.node.PStatement;
import soot.jimple.parser.node.Start;

public class DepthFirstAdapter extends AnalysisAdapter {
   public void inStart(Start node) {
      this.defaultIn(node);
   }

   public void outStart(Start node) {
      this.defaultOut(node);
   }

   public void defaultIn(Node node) {
   }

   public void defaultOut(Node node) {
   }

   public void caseStart(Start node) {
      this.inStart(node);
      node.getPFile().apply(this);
      node.getEOF().apply(this);
      this.outStart(node);
   }

   public void inAFile(AFile node) {
      this.defaultIn(node);
   }

   public void outAFile(AFile node) {
      this.defaultOut(node);
   }

   public void caseAFile(AFile node) {
      this.inAFile(node);
      List<PModifier> copy = new ArrayList(node.getModifier());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PModifier e = (PModifier)var3.next();
         e.apply(this);
      }

      if (node.getFileType() != null) {
         node.getFileType().apply(this);
      }

      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      if (node.getExtendsClause() != null) {
         node.getExtendsClause().apply(this);
      }

      if (node.getImplementsClause() != null) {
         node.getImplementsClause().apply(this);
      }

      if (node.getFileBody() != null) {
         node.getFileBody().apply(this);
      }

      this.outAFile(node);
   }

   public void inAAbstractModifier(AAbstractModifier node) {
      this.defaultIn(node);
   }

   public void outAAbstractModifier(AAbstractModifier node) {
      this.defaultOut(node);
   }

   public void caseAAbstractModifier(AAbstractModifier node) {
      this.inAAbstractModifier(node);
      if (node.getAbstract() != null) {
         node.getAbstract().apply(this);
      }

      this.outAAbstractModifier(node);
   }

   public void inAFinalModifier(AFinalModifier node) {
      this.defaultIn(node);
   }

   public void outAFinalModifier(AFinalModifier node) {
      this.defaultOut(node);
   }

   public void caseAFinalModifier(AFinalModifier node) {
      this.inAFinalModifier(node);
      if (node.getFinal() != null) {
         node.getFinal().apply(this);
      }

      this.outAFinalModifier(node);
   }

   public void inANativeModifier(ANativeModifier node) {
      this.defaultIn(node);
   }

   public void outANativeModifier(ANativeModifier node) {
      this.defaultOut(node);
   }

   public void caseANativeModifier(ANativeModifier node) {
      this.inANativeModifier(node);
      if (node.getNative() != null) {
         node.getNative().apply(this);
      }

      this.outANativeModifier(node);
   }

   public void inAPublicModifier(APublicModifier node) {
      this.defaultIn(node);
   }

   public void outAPublicModifier(APublicModifier node) {
      this.defaultOut(node);
   }

   public void caseAPublicModifier(APublicModifier node) {
      this.inAPublicModifier(node);
      if (node.getPublic() != null) {
         node.getPublic().apply(this);
      }

      this.outAPublicModifier(node);
   }

   public void inAProtectedModifier(AProtectedModifier node) {
      this.defaultIn(node);
   }

   public void outAProtectedModifier(AProtectedModifier node) {
      this.defaultOut(node);
   }

   public void caseAProtectedModifier(AProtectedModifier node) {
      this.inAProtectedModifier(node);
      if (node.getProtected() != null) {
         node.getProtected().apply(this);
      }

      this.outAProtectedModifier(node);
   }

   public void inAPrivateModifier(APrivateModifier node) {
      this.defaultIn(node);
   }

   public void outAPrivateModifier(APrivateModifier node) {
      this.defaultOut(node);
   }

   public void caseAPrivateModifier(APrivateModifier node) {
      this.inAPrivateModifier(node);
      if (node.getPrivate() != null) {
         node.getPrivate().apply(this);
      }

      this.outAPrivateModifier(node);
   }

   public void inAStaticModifier(AStaticModifier node) {
      this.defaultIn(node);
   }

   public void outAStaticModifier(AStaticModifier node) {
      this.defaultOut(node);
   }

   public void caseAStaticModifier(AStaticModifier node) {
      this.inAStaticModifier(node);
      if (node.getStatic() != null) {
         node.getStatic().apply(this);
      }

      this.outAStaticModifier(node);
   }

   public void inASynchronizedModifier(ASynchronizedModifier node) {
      this.defaultIn(node);
   }

   public void outASynchronizedModifier(ASynchronizedModifier node) {
      this.defaultOut(node);
   }

   public void caseASynchronizedModifier(ASynchronizedModifier node) {
      this.inASynchronizedModifier(node);
      if (node.getSynchronized() != null) {
         node.getSynchronized().apply(this);
      }

      this.outASynchronizedModifier(node);
   }

   public void inATransientModifier(ATransientModifier node) {
      this.defaultIn(node);
   }

   public void outATransientModifier(ATransientModifier node) {
      this.defaultOut(node);
   }

   public void caseATransientModifier(ATransientModifier node) {
      this.inATransientModifier(node);
      if (node.getTransient() != null) {
         node.getTransient().apply(this);
      }

      this.outATransientModifier(node);
   }

   public void inAVolatileModifier(AVolatileModifier node) {
      this.defaultIn(node);
   }

   public void outAVolatileModifier(AVolatileModifier node) {
      this.defaultOut(node);
   }

   public void caseAVolatileModifier(AVolatileModifier node) {
      this.inAVolatileModifier(node);
      if (node.getVolatile() != null) {
         node.getVolatile().apply(this);
      }

      this.outAVolatileModifier(node);
   }

   public void inAStrictfpModifier(AStrictfpModifier node) {
      this.defaultIn(node);
   }

   public void outAStrictfpModifier(AStrictfpModifier node) {
      this.defaultOut(node);
   }

   public void caseAStrictfpModifier(AStrictfpModifier node) {
      this.inAStrictfpModifier(node);
      if (node.getStrictfp() != null) {
         node.getStrictfp().apply(this);
      }

      this.outAStrictfpModifier(node);
   }

   public void inAEnumModifier(AEnumModifier node) {
      this.defaultIn(node);
   }

   public void outAEnumModifier(AEnumModifier node) {
      this.defaultOut(node);
   }

   public void caseAEnumModifier(AEnumModifier node) {
      this.inAEnumModifier(node);
      if (node.getEnum() != null) {
         node.getEnum().apply(this);
      }

      this.outAEnumModifier(node);
   }

   public void inAAnnotationModifier(AAnnotationModifier node) {
      this.defaultIn(node);
   }

   public void outAAnnotationModifier(AAnnotationModifier node) {
      this.defaultOut(node);
   }

   public void caseAAnnotationModifier(AAnnotationModifier node) {
      this.inAAnnotationModifier(node);
      if (node.getAnnotation() != null) {
         node.getAnnotation().apply(this);
      }

      this.outAAnnotationModifier(node);
   }

   public void inAClassFileType(AClassFileType node) {
      this.defaultIn(node);
   }

   public void outAClassFileType(AClassFileType node) {
      this.defaultOut(node);
   }

   public void caseAClassFileType(AClassFileType node) {
      this.inAClassFileType(node);
      if (node.getTheclass() != null) {
         node.getTheclass().apply(this);
      }

      this.outAClassFileType(node);
   }

   public void inAInterfaceFileType(AInterfaceFileType node) {
      this.defaultIn(node);
   }

   public void outAInterfaceFileType(AInterfaceFileType node) {
      this.defaultOut(node);
   }

   public void caseAInterfaceFileType(AInterfaceFileType node) {
      this.inAInterfaceFileType(node);
      if (node.getInterface() != null) {
         node.getInterface().apply(this);
      }

      this.outAInterfaceFileType(node);
   }

   public void inAExtendsClause(AExtendsClause node) {
      this.defaultIn(node);
   }

   public void outAExtendsClause(AExtendsClause node) {
      this.defaultOut(node);
   }

   public void caseAExtendsClause(AExtendsClause node) {
      this.inAExtendsClause(node);
      if (node.getExtends() != null) {
         node.getExtends().apply(this);
      }

      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      this.outAExtendsClause(node);
   }

   public void inAImplementsClause(AImplementsClause node) {
      this.defaultIn(node);
   }

   public void outAImplementsClause(AImplementsClause node) {
      this.defaultOut(node);
   }

   public void caseAImplementsClause(AImplementsClause node) {
      this.inAImplementsClause(node);
      if (node.getImplements() != null) {
         node.getImplements().apply(this);
      }

      if (node.getClassNameList() != null) {
         node.getClassNameList().apply(this);
      }

      this.outAImplementsClause(node);
   }

   public void inAFileBody(AFileBody node) {
      this.defaultIn(node);
   }

   public void outAFileBody(AFileBody node) {
      this.defaultOut(node);
   }

   public void caseAFileBody(AFileBody node) {
      this.inAFileBody(node);
      if (node.getLBrace() != null) {
         node.getLBrace().apply(this);
      }

      List<PMember> copy = new ArrayList(node.getMember());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PMember e = (PMember)var3.next();
         e.apply(this);
      }

      if (node.getRBrace() != null) {
         node.getRBrace().apply(this);
      }

      this.outAFileBody(node);
   }

   public void inASingleNameList(ASingleNameList node) {
      this.defaultIn(node);
   }

   public void outASingleNameList(ASingleNameList node) {
      this.defaultOut(node);
   }

   public void caseASingleNameList(ASingleNameList node) {
      this.inASingleNameList(node);
      if (node.getName() != null) {
         node.getName().apply(this);
      }

      this.outASingleNameList(node);
   }

   public void inAMultiNameList(AMultiNameList node) {
      this.defaultIn(node);
   }

   public void outAMultiNameList(AMultiNameList node) {
      this.defaultOut(node);
   }

   public void caseAMultiNameList(AMultiNameList node) {
      this.inAMultiNameList(node);
      if (node.getName() != null) {
         node.getName().apply(this);
      }

      if (node.getComma() != null) {
         node.getComma().apply(this);
      }

      if (node.getNameList() != null) {
         node.getNameList().apply(this);
      }

      this.outAMultiNameList(node);
   }

   public void inAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
      this.defaultIn(node);
   }

   public void outAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
      this.defaultOut(node);
   }

   public void caseAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
      this.inAClassNameSingleClassNameList(node);
      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      this.outAClassNameSingleClassNameList(node);
   }

   public void inAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
      this.defaultIn(node);
   }

   public void outAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
      this.defaultOut(node);
   }

   public void caseAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
      this.inAClassNameMultiClassNameList(node);
      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      if (node.getComma() != null) {
         node.getComma().apply(this);
      }

      if (node.getClassNameList() != null) {
         node.getClassNameList().apply(this);
      }

      this.outAClassNameMultiClassNameList(node);
   }

   public void inAFieldMember(AFieldMember node) {
      this.defaultIn(node);
   }

   public void outAFieldMember(AFieldMember node) {
      this.defaultOut(node);
   }

   public void caseAFieldMember(AFieldMember node) {
      this.inAFieldMember(node);
      List<PModifier> copy = new ArrayList(node.getModifier());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PModifier e = (PModifier)var3.next();
         e.apply(this);
      }

      if (node.getType() != null) {
         node.getType().apply(this);
      }

      if (node.getName() != null) {
         node.getName().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAFieldMember(node);
   }

   public void inAMethodMember(AMethodMember node) {
      this.defaultIn(node);
   }

   public void outAMethodMember(AMethodMember node) {
      this.defaultOut(node);
   }

   public void caseAMethodMember(AMethodMember node) {
      this.inAMethodMember(node);
      List<PModifier> copy = new ArrayList(node.getModifier());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PModifier e = (PModifier)var3.next();
         e.apply(this);
      }

      if (node.getType() != null) {
         node.getType().apply(this);
      }

      if (node.getName() != null) {
         node.getName().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getParameterList() != null) {
         node.getParameterList().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getThrowsClause() != null) {
         node.getThrowsClause().apply(this);
      }

      if (node.getMethodBody() != null) {
         node.getMethodBody().apply(this);
      }

      this.outAMethodMember(node);
   }

   public void inAVoidType(AVoidType node) {
      this.defaultIn(node);
   }

   public void outAVoidType(AVoidType node) {
      this.defaultOut(node);
   }

   public void caseAVoidType(AVoidType node) {
      this.inAVoidType(node);
      if (node.getVoid() != null) {
         node.getVoid().apply(this);
      }

      this.outAVoidType(node);
   }

   public void inANovoidType(ANovoidType node) {
      this.defaultIn(node);
   }

   public void outANovoidType(ANovoidType node) {
      this.defaultOut(node);
   }

   public void caseANovoidType(ANovoidType node) {
      this.inANovoidType(node);
      if (node.getNonvoidType() != null) {
         node.getNonvoidType().apply(this);
      }

      this.outANovoidType(node);
   }

   public void inASingleParameterList(ASingleParameterList node) {
      this.defaultIn(node);
   }

   public void outASingleParameterList(ASingleParameterList node) {
      this.defaultOut(node);
   }

   public void caseASingleParameterList(ASingleParameterList node) {
      this.inASingleParameterList(node);
      if (node.getParameter() != null) {
         node.getParameter().apply(this);
      }

      this.outASingleParameterList(node);
   }

   public void inAMultiParameterList(AMultiParameterList node) {
      this.defaultIn(node);
   }

   public void outAMultiParameterList(AMultiParameterList node) {
      this.defaultOut(node);
   }

   public void caseAMultiParameterList(AMultiParameterList node) {
      this.inAMultiParameterList(node);
      if (node.getParameter() != null) {
         node.getParameter().apply(this);
      }

      if (node.getComma() != null) {
         node.getComma().apply(this);
      }

      if (node.getParameterList() != null) {
         node.getParameterList().apply(this);
      }

      this.outAMultiParameterList(node);
   }

   public void inAParameter(AParameter node) {
      this.defaultIn(node);
   }

   public void outAParameter(AParameter node) {
      this.defaultOut(node);
   }

   public void caseAParameter(AParameter node) {
      this.inAParameter(node);
      if (node.getNonvoidType() != null) {
         node.getNonvoidType().apply(this);
      }

      this.outAParameter(node);
   }

   public void inAThrowsClause(AThrowsClause node) {
      this.defaultIn(node);
   }

   public void outAThrowsClause(AThrowsClause node) {
      this.defaultOut(node);
   }

   public void caseAThrowsClause(AThrowsClause node) {
      this.inAThrowsClause(node);
      if (node.getThrows() != null) {
         node.getThrows().apply(this);
      }

      if (node.getClassNameList() != null) {
         node.getClassNameList().apply(this);
      }

      this.outAThrowsClause(node);
   }

   public void inABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
      this.inABooleanBaseTypeNoName(node);
      if (node.getBoolean() != null) {
         node.getBoolean().apply(this);
      }

      this.outABooleanBaseTypeNoName(node);
   }

   public void inAByteBaseTypeNoName(AByteBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outAByteBaseTypeNoName(AByteBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseAByteBaseTypeNoName(AByteBaseTypeNoName node) {
      this.inAByteBaseTypeNoName(node);
      if (node.getByte() != null) {
         node.getByte().apply(this);
      }

      this.outAByteBaseTypeNoName(node);
   }

   public void inACharBaseTypeNoName(ACharBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outACharBaseTypeNoName(ACharBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseACharBaseTypeNoName(ACharBaseTypeNoName node) {
      this.inACharBaseTypeNoName(node);
      if (node.getChar() != null) {
         node.getChar().apply(this);
      }

      this.outACharBaseTypeNoName(node);
   }

   public void inAShortBaseTypeNoName(AShortBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outAShortBaseTypeNoName(AShortBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseAShortBaseTypeNoName(AShortBaseTypeNoName node) {
      this.inAShortBaseTypeNoName(node);
      if (node.getShort() != null) {
         node.getShort().apply(this);
      }

      this.outAShortBaseTypeNoName(node);
   }

   public void inAIntBaseTypeNoName(AIntBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outAIntBaseTypeNoName(AIntBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseAIntBaseTypeNoName(AIntBaseTypeNoName node) {
      this.inAIntBaseTypeNoName(node);
      if (node.getInt() != null) {
         node.getInt().apply(this);
      }

      this.outAIntBaseTypeNoName(node);
   }

   public void inALongBaseTypeNoName(ALongBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outALongBaseTypeNoName(ALongBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseALongBaseTypeNoName(ALongBaseTypeNoName node) {
      this.inALongBaseTypeNoName(node);
      if (node.getLong() != null) {
         node.getLong().apply(this);
      }

      this.outALongBaseTypeNoName(node);
   }

   public void inAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
      this.inAFloatBaseTypeNoName(node);
      if (node.getFloat() != null) {
         node.getFloat().apply(this);
      }

      this.outAFloatBaseTypeNoName(node);
   }

   public void inADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
      this.inADoubleBaseTypeNoName(node);
      if (node.getDouble() != null) {
         node.getDouble().apply(this);
      }

      this.outADoubleBaseTypeNoName(node);
   }

   public void inANullBaseTypeNoName(ANullBaseTypeNoName node) {
      this.defaultIn(node);
   }

   public void outANullBaseTypeNoName(ANullBaseTypeNoName node) {
      this.defaultOut(node);
   }

   public void caseANullBaseTypeNoName(ANullBaseTypeNoName node) {
      this.inANullBaseTypeNoName(node);
      if (node.getNullType() != null) {
         node.getNullType().apply(this);
      }

      this.outANullBaseTypeNoName(node);
   }

   public void inABooleanBaseType(ABooleanBaseType node) {
      this.defaultIn(node);
   }

   public void outABooleanBaseType(ABooleanBaseType node) {
      this.defaultOut(node);
   }

   public void caseABooleanBaseType(ABooleanBaseType node) {
      this.inABooleanBaseType(node);
      if (node.getBoolean() != null) {
         node.getBoolean().apply(this);
      }

      this.outABooleanBaseType(node);
   }

   public void inAByteBaseType(AByteBaseType node) {
      this.defaultIn(node);
   }

   public void outAByteBaseType(AByteBaseType node) {
      this.defaultOut(node);
   }

   public void caseAByteBaseType(AByteBaseType node) {
      this.inAByteBaseType(node);
      if (node.getByte() != null) {
         node.getByte().apply(this);
      }

      this.outAByteBaseType(node);
   }

   public void inACharBaseType(ACharBaseType node) {
      this.defaultIn(node);
   }

   public void outACharBaseType(ACharBaseType node) {
      this.defaultOut(node);
   }

   public void caseACharBaseType(ACharBaseType node) {
      this.inACharBaseType(node);
      if (node.getChar() != null) {
         node.getChar().apply(this);
      }

      this.outACharBaseType(node);
   }

   public void inAShortBaseType(AShortBaseType node) {
      this.defaultIn(node);
   }

   public void outAShortBaseType(AShortBaseType node) {
      this.defaultOut(node);
   }

   public void caseAShortBaseType(AShortBaseType node) {
      this.inAShortBaseType(node);
      if (node.getShort() != null) {
         node.getShort().apply(this);
      }

      this.outAShortBaseType(node);
   }

   public void inAIntBaseType(AIntBaseType node) {
      this.defaultIn(node);
   }

   public void outAIntBaseType(AIntBaseType node) {
      this.defaultOut(node);
   }

   public void caseAIntBaseType(AIntBaseType node) {
      this.inAIntBaseType(node);
      if (node.getInt() != null) {
         node.getInt().apply(this);
      }

      this.outAIntBaseType(node);
   }

   public void inALongBaseType(ALongBaseType node) {
      this.defaultIn(node);
   }

   public void outALongBaseType(ALongBaseType node) {
      this.defaultOut(node);
   }

   public void caseALongBaseType(ALongBaseType node) {
      this.inALongBaseType(node);
      if (node.getLong() != null) {
         node.getLong().apply(this);
      }

      this.outALongBaseType(node);
   }

   public void inAFloatBaseType(AFloatBaseType node) {
      this.defaultIn(node);
   }

   public void outAFloatBaseType(AFloatBaseType node) {
      this.defaultOut(node);
   }

   public void caseAFloatBaseType(AFloatBaseType node) {
      this.inAFloatBaseType(node);
      if (node.getFloat() != null) {
         node.getFloat().apply(this);
      }

      this.outAFloatBaseType(node);
   }

   public void inADoubleBaseType(ADoubleBaseType node) {
      this.defaultIn(node);
   }

   public void outADoubleBaseType(ADoubleBaseType node) {
      this.defaultOut(node);
   }

   public void caseADoubleBaseType(ADoubleBaseType node) {
      this.inADoubleBaseType(node);
      if (node.getDouble() != null) {
         node.getDouble().apply(this);
      }

      this.outADoubleBaseType(node);
   }

   public void inANullBaseType(ANullBaseType node) {
      this.defaultIn(node);
   }

   public void outANullBaseType(ANullBaseType node) {
      this.defaultOut(node);
   }

   public void caseANullBaseType(ANullBaseType node) {
      this.inANullBaseType(node);
      if (node.getNullType() != null) {
         node.getNullType().apply(this);
      }

      this.outANullBaseType(node);
   }

   public void inAClassNameBaseType(AClassNameBaseType node) {
      this.defaultIn(node);
   }

   public void outAClassNameBaseType(AClassNameBaseType node) {
      this.defaultOut(node);
   }

   public void caseAClassNameBaseType(AClassNameBaseType node) {
      this.inAClassNameBaseType(node);
      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      this.outAClassNameBaseType(node);
   }

   public void inABaseNonvoidType(ABaseNonvoidType node) {
      this.defaultIn(node);
   }

   public void outABaseNonvoidType(ABaseNonvoidType node) {
      this.defaultOut(node);
   }

   public void caseABaseNonvoidType(ABaseNonvoidType node) {
      this.inABaseNonvoidType(node);
      if (node.getBaseTypeNoName() != null) {
         node.getBaseTypeNoName().apply(this);
      }

      List<PArrayBrackets> copy = new ArrayList(node.getArrayBrackets());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PArrayBrackets e = (PArrayBrackets)var3.next();
         e.apply(this);
      }

      this.outABaseNonvoidType(node);
   }

   public void inAQuotedNonvoidType(AQuotedNonvoidType node) {
      this.defaultIn(node);
   }

   public void outAQuotedNonvoidType(AQuotedNonvoidType node) {
      this.defaultOut(node);
   }

   public void caseAQuotedNonvoidType(AQuotedNonvoidType node) {
      this.inAQuotedNonvoidType(node);
      if (node.getQuotedName() != null) {
         node.getQuotedName().apply(this);
      }

      List<PArrayBrackets> copy = new ArrayList(node.getArrayBrackets());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PArrayBrackets e = (PArrayBrackets)var3.next();
         e.apply(this);
      }

      this.outAQuotedNonvoidType(node);
   }

   public void inAIdentNonvoidType(AIdentNonvoidType node) {
      this.defaultIn(node);
   }

   public void outAIdentNonvoidType(AIdentNonvoidType node) {
      this.defaultOut(node);
   }

   public void caseAIdentNonvoidType(AIdentNonvoidType node) {
      this.inAIdentNonvoidType(node);
      if (node.getIdentifier() != null) {
         node.getIdentifier().apply(this);
      }

      List<PArrayBrackets> copy = new ArrayList(node.getArrayBrackets());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PArrayBrackets e = (PArrayBrackets)var3.next();
         e.apply(this);
      }

      this.outAIdentNonvoidType(node);
   }

   public void inAFullIdentNonvoidType(AFullIdentNonvoidType node) {
      this.defaultIn(node);
   }

   public void outAFullIdentNonvoidType(AFullIdentNonvoidType node) {
      this.defaultOut(node);
   }

   public void caseAFullIdentNonvoidType(AFullIdentNonvoidType node) {
      this.inAFullIdentNonvoidType(node);
      if (node.getFullIdentifier() != null) {
         node.getFullIdentifier().apply(this);
      }

      List<PArrayBrackets> copy = new ArrayList(node.getArrayBrackets());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PArrayBrackets e = (PArrayBrackets)var3.next();
         e.apply(this);
      }

      this.outAFullIdentNonvoidType(node);
   }

   public void inAArrayBrackets(AArrayBrackets node) {
      this.defaultIn(node);
   }

   public void outAArrayBrackets(AArrayBrackets node) {
      this.defaultOut(node);
   }

   public void caseAArrayBrackets(AArrayBrackets node) {
      this.inAArrayBrackets(node);
      if (node.getLBracket() != null) {
         node.getLBracket().apply(this);
      }

      if (node.getRBracket() != null) {
         node.getRBracket().apply(this);
      }

      this.outAArrayBrackets(node);
   }

   public void inAEmptyMethodBody(AEmptyMethodBody node) {
      this.defaultIn(node);
   }

   public void outAEmptyMethodBody(AEmptyMethodBody node) {
      this.defaultOut(node);
   }

   public void caseAEmptyMethodBody(AEmptyMethodBody node) {
      this.inAEmptyMethodBody(node);
      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAEmptyMethodBody(node);
   }

   public void inAFullMethodBody(AFullMethodBody node) {
      this.defaultIn(node);
   }

   public void outAFullMethodBody(AFullMethodBody node) {
      this.defaultOut(node);
   }

   public void caseAFullMethodBody(AFullMethodBody node) {
      this.inAFullMethodBody(node);
      if (node.getLBrace() != null) {
         node.getLBrace().apply(this);
      }

      List<PDeclaration> copy = new ArrayList(node.getDeclaration());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PDeclaration e = (PDeclaration)var3.next();
         e.apply(this);
      }

      copy = new ArrayList(node.getStatement());
      var3 = copy.iterator();

      while(var3.hasNext()) {
         PStatement e = (PStatement)var3.next();
         e.apply(this);
      }

      copy = new ArrayList(node.getCatchClause());
      var3 = copy.iterator();

      while(var3.hasNext()) {
         PCatchClause e = (PCatchClause)var3.next();
         e.apply(this);
      }

      if (node.getRBrace() != null) {
         node.getRBrace().apply(this);
      }

      this.outAFullMethodBody(node);
   }

   public void inADeclaration(ADeclaration node) {
      this.defaultIn(node);
   }

   public void outADeclaration(ADeclaration node) {
      this.defaultOut(node);
   }

   public void caseADeclaration(ADeclaration node) {
      this.inADeclaration(node);
      if (node.getJimpleType() != null) {
         node.getJimpleType().apply(this);
      }

      if (node.getLocalNameList() != null) {
         node.getLocalNameList().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outADeclaration(node);
   }

   public void inAUnknownJimpleType(AUnknownJimpleType node) {
      this.defaultIn(node);
   }

   public void outAUnknownJimpleType(AUnknownJimpleType node) {
      this.defaultOut(node);
   }

   public void caseAUnknownJimpleType(AUnknownJimpleType node) {
      this.inAUnknownJimpleType(node);
      if (node.getUnknown() != null) {
         node.getUnknown().apply(this);
      }

      this.outAUnknownJimpleType(node);
   }

   public void inANonvoidJimpleType(ANonvoidJimpleType node) {
      this.defaultIn(node);
   }

   public void outANonvoidJimpleType(ANonvoidJimpleType node) {
      this.defaultOut(node);
   }

   public void caseANonvoidJimpleType(ANonvoidJimpleType node) {
      this.inANonvoidJimpleType(node);
      if (node.getNonvoidType() != null) {
         node.getNonvoidType().apply(this);
      }

      this.outANonvoidJimpleType(node);
   }

   public void inALocalName(ALocalName node) {
      this.defaultIn(node);
   }

   public void outALocalName(ALocalName node) {
      this.defaultOut(node);
   }

   public void caseALocalName(ALocalName node) {
      this.inALocalName(node);
      if (node.getName() != null) {
         node.getName().apply(this);
      }

      this.outALocalName(node);
   }

   public void inASingleLocalNameList(ASingleLocalNameList node) {
      this.defaultIn(node);
   }

   public void outASingleLocalNameList(ASingleLocalNameList node) {
      this.defaultOut(node);
   }

   public void caseASingleLocalNameList(ASingleLocalNameList node) {
      this.inASingleLocalNameList(node);
      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      this.outASingleLocalNameList(node);
   }

   public void inAMultiLocalNameList(AMultiLocalNameList node) {
      this.defaultIn(node);
   }

   public void outAMultiLocalNameList(AMultiLocalNameList node) {
      this.defaultOut(node);
   }

   public void caseAMultiLocalNameList(AMultiLocalNameList node) {
      this.inAMultiLocalNameList(node);
      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      if (node.getComma() != null) {
         node.getComma().apply(this);
      }

      if (node.getLocalNameList() != null) {
         node.getLocalNameList().apply(this);
      }

      this.outAMultiLocalNameList(node);
   }

   public void inALabelStatement(ALabelStatement node) {
      this.defaultIn(node);
   }

   public void outALabelStatement(ALabelStatement node) {
      this.defaultOut(node);
   }

   public void caseALabelStatement(ALabelStatement node) {
      this.inALabelStatement(node);
      if (node.getLabelName() != null) {
         node.getLabelName().apply(this);
      }

      if (node.getColon() != null) {
         node.getColon().apply(this);
      }

      this.outALabelStatement(node);
   }

   public void inABreakpointStatement(ABreakpointStatement node) {
      this.defaultIn(node);
   }

   public void outABreakpointStatement(ABreakpointStatement node) {
      this.defaultOut(node);
   }

   public void caseABreakpointStatement(ABreakpointStatement node) {
      this.inABreakpointStatement(node);
      if (node.getBreakpoint() != null) {
         node.getBreakpoint().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outABreakpointStatement(node);
   }

   public void inAEntermonitorStatement(AEntermonitorStatement node) {
      this.defaultIn(node);
   }

   public void outAEntermonitorStatement(AEntermonitorStatement node) {
      this.defaultOut(node);
   }

   public void caseAEntermonitorStatement(AEntermonitorStatement node) {
      this.inAEntermonitorStatement(node);
      if (node.getEntermonitor() != null) {
         node.getEntermonitor().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAEntermonitorStatement(node);
   }

   public void inAExitmonitorStatement(AExitmonitorStatement node) {
      this.defaultIn(node);
   }

   public void outAExitmonitorStatement(AExitmonitorStatement node) {
      this.defaultOut(node);
   }

   public void caseAExitmonitorStatement(AExitmonitorStatement node) {
      this.inAExitmonitorStatement(node);
      if (node.getExitmonitor() != null) {
         node.getExitmonitor().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAExitmonitorStatement(node);
   }

   public void inATableswitchStatement(ATableswitchStatement node) {
      this.defaultIn(node);
   }

   public void outATableswitchStatement(ATableswitchStatement node) {
      this.defaultOut(node);
   }

   public void caseATableswitchStatement(ATableswitchStatement node) {
      this.inATableswitchStatement(node);
      if (node.getTableswitch() != null) {
         node.getTableswitch().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getLBrace() != null) {
         node.getLBrace().apply(this);
      }

      List<PCaseStmt> copy = new ArrayList(node.getCaseStmt());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PCaseStmt e = (PCaseStmt)var3.next();
         e.apply(this);
      }

      if (node.getRBrace() != null) {
         node.getRBrace().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outATableswitchStatement(node);
   }

   public void inALookupswitchStatement(ALookupswitchStatement node) {
      this.defaultIn(node);
   }

   public void outALookupswitchStatement(ALookupswitchStatement node) {
      this.defaultOut(node);
   }

   public void caseALookupswitchStatement(ALookupswitchStatement node) {
      this.inALookupswitchStatement(node);
      if (node.getLookupswitch() != null) {
         node.getLookupswitch().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getLBrace() != null) {
         node.getLBrace().apply(this);
      }

      List<PCaseStmt> copy = new ArrayList(node.getCaseStmt());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PCaseStmt e = (PCaseStmt)var3.next();
         e.apply(this);
      }

      if (node.getRBrace() != null) {
         node.getRBrace().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outALookupswitchStatement(node);
   }

   public void inAIdentityStatement(AIdentityStatement node) {
      this.defaultIn(node);
   }

   public void outAIdentityStatement(AIdentityStatement node) {
      this.defaultOut(node);
   }

   public void caseAIdentityStatement(AIdentityStatement node) {
      this.inAIdentityStatement(node);
      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      if (node.getColonEquals() != null) {
         node.getColonEquals().apply(this);
      }

      if (node.getAtIdentifier() != null) {
         node.getAtIdentifier().apply(this);
      }

      if (node.getType() != null) {
         node.getType().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAIdentityStatement(node);
   }

   public void inAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
      this.defaultIn(node);
   }

   public void outAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
      this.defaultOut(node);
   }

   public void caseAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
      this.inAIdentityNoTypeStatement(node);
      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      if (node.getColonEquals() != null) {
         node.getColonEquals().apply(this);
      }

      if (node.getAtIdentifier() != null) {
         node.getAtIdentifier().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAIdentityNoTypeStatement(node);
   }

   public void inAAssignStatement(AAssignStatement node) {
      this.defaultIn(node);
   }

   public void outAAssignStatement(AAssignStatement node) {
      this.defaultOut(node);
   }

   public void caseAAssignStatement(AAssignStatement node) {
      this.inAAssignStatement(node);
      if (node.getVariable() != null) {
         node.getVariable().apply(this);
      }

      if (node.getEquals() != null) {
         node.getEquals().apply(this);
      }

      if (node.getExpression() != null) {
         node.getExpression().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAAssignStatement(node);
   }

   public void inAIfStatement(AIfStatement node) {
      this.defaultIn(node);
   }

   public void outAIfStatement(AIfStatement node) {
      this.defaultOut(node);
   }

   public void caseAIfStatement(AIfStatement node) {
      this.inAIfStatement(node);
      if (node.getIf() != null) {
         node.getIf().apply(this);
      }

      if (node.getBoolExpr() != null) {
         node.getBoolExpr().apply(this);
      }

      if (node.getGotoStmt() != null) {
         node.getGotoStmt().apply(this);
      }

      this.outAIfStatement(node);
   }

   public void inAGotoStatement(AGotoStatement node) {
      this.defaultIn(node);
   }

   public void outAGotoStatement(AGotoStatement node) {
      this.defaultOut(node);
   }

   public void caseAGotoStatement(AGotoStatement node) {
      this.inAGotoStatement(node);
      if (node.getGotoStmt() != null) {
         node.getGotoStmt().apply(this);
      }

      this.outAGotoStatement(node);
   }

   public void inANopStatement(ANopStatement node) {
      this.defaultIn(node);
   }

   public void outANopStatement(ANopStatement node) {
      this.defaultOut(node);
   }

   public void caseANopStatement(ANopStatement node) {
      this.inANopStatement(node);
      if (node.getNop() != null) {
         node.getNop().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outANopStatement(node);
   }

   public void inARetStatement(ARetStatement node) {
      this.defaultIn(node);
   }

   public void outARetStatement(ARetStatement node) {
      this.defaultOut(node);
   }

   public void caseARetStatement(ARetStatement node) {
      this.inARetStatement(node);
      if (node.getRet() != null) {
         node.getRet().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outARetStatement(node);
   }

   public void inAReturnStatement(AReturnStatement node) {
      this.defaultIn(node);
   }

   public void outAReturnStatement(AReturnStatement node) {
      this.defaultOut(node);
   }

   public void caseAReturnStatement(AReturnStatement node) {
      this.inAReturnStatement(node);
      if (node.getReturn() != null) {
         node.getReturn().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAReturnStatement(node);
   }

   public void inAThrowStatement(AThrowStatement node) {
      this.defaultIn(node);
   }

   public void outAThrowStatement(AThrowStatement node) {
      this.defaultOut(node);
   }

   public void caseAThrowStatement(AThrowStatement node) {
      this.inAThrowStatement(node);
      if (node.getThrow() != null) {
         node.getThrow().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAThrowStatement(node);
   }

   public void inAInvokeStatement(AInvokeStatement node) {
      this.defaultIn(node);
   }

   public void outAInvokeStatement(AInvokeStatement node) {
      this.defaultOut(node);
   }

   public void caseAInvokeStatement(AInvokeStatement node) {
      this.inAInvokeStatement(node);
      if (node.getInvokeExpr() != null) {
         node.getInvokeExpr().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAInvokeStatement(node);
   }

   public void inALabelName(ALabelName node) {
      this.defaultIn(node);
   }

   public void outALabelName(ALabelName node) {
      this.defaultOut(node);
   }

   public void caseALabelName(ALabelName node) {
      this.inALabelName(node);
      if (node.getIdentifier() != null) {
         node.getIdentifier().apply(this);
      }

      this.outALabelName(node);
   }

   public void inACaseStmt(ACaseStmt node) {
      this.defaultIn(node);
   }

   public void outACaseStmt(ACaseStmt node) {
      this.defaultOut(node);
   }

   public void caseACaseStmt(ACaseStmt node) {
      this.inACaseStmt(node);
      if (node.getCaseLabel() != null) {
         node.getCaseLabel().apply(this);
      }

      if (node.getColon() != null) {
         node.getColon().apply(this);
      }

      if (node.getGotoStmt() != null) {
         node.getGotoStmt().apply(this);
      }

      this.outACaseStmt(node);
   }

   public void inAConstantCaseLabel(AConstantCaseLabel node) {
      this.defaultIn(node);
   }

   public void outAConstantCaseLabel(AConstantCaseLabel node) {
      this.defaultOut(node);
   }

   public void caseAConstantCaseLabel(AConstantCaseLabel node) {
      this.inAConstantCaseLabel(node);
      if (node.getCase() != null) {
         node.getCase().apply(this);
      }

      if (node.getMinus() != null) {
         node.getMinus().apply(this);
      }

      if (node.getIntegerConstant() != null) {
         node.getIntegerConstant().apply(this);
      }

      this.outAConstantCaseLabel(node);
   }

   public void inADefaultCaseLabel(ADefaultCaseLabel node) {
      this.defaultIn(node);
   }

   public void outADefaultCaseLabel(ADefaultCaseLabel node) {
      this.defaultOut(node);
   }

   public void caseADefaultCaseLabel(ADefaultCaseLabel node) {
      this.inADefaultCaseLabel(node);
      if (node.getDefault() != null) {
         node.getDefault().apply(this);
      }

      this.outADefaultCaseLabel(node);
   }

   public void inAGotoStmt(AGotoStmt node) {
      this.defaultIn(node);
   }

   public void outAGotoStmt(AGotoStmt node) {
      this.defaultOut(node);
   }

   public void caseAGotoStmt(AGotoStmt node) {
      this.inAGotoStmt(node);
      if (node.getGoto() != null) {
         node.getGoto().apply(this);
      }

      if (node.getLabelName() != null) {
         node.getLabelName().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outAGotoStmt(node);
   }

   public void inACatchClause(ACatchClause node) {
      this.defaultIn(node);
   }

   public void outACatchClause(ACatchClause node) {
      this.defaultOut(node);
   }

   public void caseACatchClause(ACatchClause node) {
      this.inACatchClause(node);
      if (node.getCatch() != null) {
         node.getCatch().apply(this);
      }

      if (node.getName() != null) {
         node.getName().apply(this);
      }

      if (node.getFrom() != null) {
         node.getFrom().apply(this);
      }

      if (node.getFromLabel() != null) {
         node.getFromLabel().apply(this);
      }

      if (node.getTo() != null) {
         node.getTo().apply(this);
      }

      if (node.getToLabel() != null) {
         node.getToLabel().apply(this);
      }

      if (node.getWith() != null) {
         node.getWith().apply(this);
      }

      if (node.getWithLabel() != null) {
         node.getWithLabel().apply(this);
      }

      if (node.getSemicolon() != null) {
         node.getSemicolon().apply(this);
      }

      this.outACatchClause(node);
   }

   public void inANewExpression(ANewExpression node) {
      this.defaultIn(node);
   }

   public void outANewExpression(ANewExpression node) {
      this.defaultOut(node);
   }

   public void caseANewExpression(ANewExpression node) {
      this.inANewExpression(node);
      if (node.getNewExpr() != null) {
         node.getNewExpr().apply(this);
      }

      this.outANewExpression(node);
   }

   public void inACastExpression(ACastExpression node) {
      this.defaultIn(node);
   }

   public void outACastExpression(ACastExpression node) {
      this.defaultOut(node);
   }

   public void caseACastExpression(ACastExpression node) {
      this.inACastExpression(node);
      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getNonvoidType() != null) {
         node.getNonvoidType().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      this.outACastExpression(node);
   }

   public void inAInstanceofExpression(AInstanceofExpression node) {
      this.defaultIn(node);
   }

   public void outAInstanceofExpression(AInstanceofExpression node) {
      this.defaultOut(node);
   }

   public void caseAInstanceofExpression(AInstanceofExpression node) {
      this.inAInstanceofExpression(node);
      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getInstanceof() != null) {
         node.getInstanceof().apply(this);
      }

      if (node.getNonvoidType() != null) {
         node.getNonvoidType().apply(this);
      }

      this.outAInstanceofExpression(node);
   }

   public void inAInvokeExpression(AInvokeExpression node) {
      this.defaultIn(node);
   }

   public void outAInvokeExpression(AInvokeExpression node) {
      this.defaultOut(node);
   }

   public void caseAInvokeExpression(AInvokeExpression node) {
      this.inAInvokeExpression(node);
      if (node.getInvokeExpr() != null) {
         node.getInvokeExpr().apply(this);
      }

      this.outAInvokeExpression(node);
   }

   public void inAReferenceExpression(AReferenceExpression node) {
      this.defaultIn(node);
   }

   public void outAReferenceExpression(AReferenceExpression node) {
      this.defaultOut(node);
   }

   public void caseAReferenceExpression(AReferenceExpression node) {
      this.inAReferenceExpression(node);
      if (node.getReference() != null) {
         node.getReference().apply(this);
      }

      this.outAReferenceExpression(node);
   }

   public void inABinopExpression(ABinopExpression node) {
      this.defaultIn(node);
   }

   public void outABinopExpression(ABinopExpression node) {
      this.defaultOut(node);
   }

   public void caseABinopExpression(ABinopExpression node) {
      this.inABinopExpression(node);
      if (node.getBinopExpr() != null) {
         node.getBinopExpr().apply(this);
      }

      this.outABinopExpression(node);
   }

   public void inAUnopExpression(AUnopExpression node) {
      this.defaultIn(node);
   }

   public void outAUnopExpression(AUnopExpression node) {
      this.defaultOut(node);
   }

   public void caseAUnopExpression(AUnopExpression node) {
      this.inAUnopExpression(node);
      if (node.getUnopExpr() != null) {
         node.getUnopExpr().apply(this);
      }

      this.outAUnopExpression(node);
   }

   public void inAImmediateExpression(AImmediateExpression node) {
      this.defaultIn(node);
   }

   public void outAImmediateExpression(AImmediateExpression node) {
      this.defaultOut(node);
   }

   public void caseAImmediateExpression(AImmediateExpression node) {
      this.inAImmediateExpression(node);
      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      this.outAImmediateExpression(node);
   }

   public void inASimpleNewExpr(ASimpleNewExpr node) {
      this.defaultIn(node);
   }

   public void outASimpleNewExpr(ASimpleNewExpr node) {
      this.defaultOut(node);
   }

   public void caseASimpleNewExpr(ASimpleNewExpr node) {
      this.inASimpleNewExpr(node);
      if (node.getNew() != null) {
         node.getNew().apply(this);
      }

      if (node.getBaseType() != null) {
         node.getBaseType().apply(this);
      }

      this.outASimpleNewExpr(node);
   }

   public void inAArrayNewExpr(AArrayNewExpr node) {
      this.defaultIn(node);
   }

   public void outAArrayNewExpr(AArrayNewExpr node) {
      this.defaultOut(node);
   }

   public void caseAArrayNewExpr(AArrayNewExpr node) {
      this.inAArrayNewExpr(node);
      if (node.getNewarray() != null) {
         node.getNewarray().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getNonvoidType() != null) {
         node.getNonvoidType().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getFixedArrayDescriptor() != null) {
         node.getFixedArrayDescriptor().apply(this);
      }

      this.outAArrayNewExpr(node);
   }

   public void inAMultiNewExpr(AMultiNewExpr node) {
      this.defaultIn(node);
   }

   public void outAMultiNewExpr(AMultiNewExpr node) {
      this.defaultOut(node);
   }

   public void caseAMultiNewExpr(AMultiNewExpr node) {
      this.inAMultiNewExpr(node);
      if (node.getNewmultiarray() != null) {
         node.getNewmultiarray().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getBaseType() != null) {
         node.getBaseType().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      List<PArrayDescriptor> copy = new ArrayList(node.getArrayDescriptor());
      Iterator var3 = copy.iterator();

      while(var3.hasNext()) {
         PArrayDescriptor e = (PArrayDescriptor)var3.next();
         e.apply(this);
      }

      this.outAMultiNewExpr(node);
   }

   public void inAArrayDescriptor(AArrayDescriptor node) {
      this.defaultIn(node);
   }

   public void outAArrayDescriptor(AArrayDescriptor node) {
      this.defaultOut(node);
   }

   public void caseAArrayDescriptor(AArrayDescriptor node) {
      this.inAArrayDescriptor(node);
      if (node.getLBracket() != null) {
         node.getLBracket().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getRBracket() != null) {
         node.getRBracket().apply(this);
      }

      this.outAArrayDescriptor(node);
   }

   public void inAReferenceVariable(AReferenceVariable node) {
      this.defaultIn(node);
   }

   public void outAReferenceVariable(AReferenceVariable node) {
      this.defaultOut(node);
   }

   public void caseAReferenceVariable(AReferenceVariable node) {
      this.inAReferenceVariable(node);
      if (node.getReference() != null) {
         node.getReference().apply(this);
      }

      this.outAReferenceVariable(node);
   }

   public void inALocalVariable(ALocalVariable node) {
      this.defaultIn(node);
   }

   public void outALocalVariable(ALocalVariable node) {
      this.defaultOut(node);
   }

   public void caseALocalVariable(ALocalVariable node) {
      this.inALocalVariable(node);
      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      this.outALocalVariable(node);
   }

   public void inABinopBoolExpr(ABinopBoolExpr node) {
      this.defaultIn(node);
   }

   public void outABinopBoolExpr(ABinopBoolExpr node) {
      this.defaultOut(node);
   }

   public void caseABinopBoolExpr(ABinopBoolExpr node) {
      this.inABinopBoolExpr(node);
      if (node.getBinopExpr() != null) {
         node.getBinopExpr().apply(this);
      }

      this.outABinopBoolExpr(node);
   }

   public void inAUnopBoolExpr(AUnopBoolExpr node) {
      this.defaultIn(node);
   }

   public void outAUnopBoolExpr(AUnopBoolExpr node) {
      this.defaultOut(node);
   }

   public void caseAUnopBoolExpr(AUnopBoolExpr node) {
      this.inAUnopBoolExpr(node);
      if (node.getUnopExpr() != null) {
         node.getUnopExpr().apply(this);
      }

      this.outAUnopBoolExpr(node);
   }

   public void inANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
      this.defaultIn(node);
   }

   public void outANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
      this.defaultOut(node);
   }

   public void caseANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
      this.inANonstaticInvokeExpr(node);
      if (node.getNonstaticInvoke() != null) {
         node.getNonstaticInvoke().apply(this);
      }

      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      if (node.getDot() != null) {
         node.getDot().apply(this);
      }

      if (node.getMethodSignature() != null) {
         node.getMethodSignature().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getArgList() != null) {
         node.getArgList().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      this.outANonstaticInvokeExpr(node);
   }

   public void inAStaticInvokeExpr(AStaticInvokeExpr node) {
      this.defaultIn(node);
   }

   public void outAStaticInvokeExpr(AStaticInvokeExpr node) {
      this.defaultOut(node);
   }

   public void caseAStaticInvokeExpr(AStaticInvokeExpr node) {
      this.inAStaticInvokeExpr(node);
      if (node.getStaticinvoke() != null) {
         node.getStaticinvoke().apply(this);
      }

      if (node.getMethodSignature() != null) {
         node.getMethodSignature().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getArgList() != null) {
         node.getArgList().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      this.outAStaticInvokeExpr(node);
   }

   public void inADynamicInvokeExpr(ADynamicInvokeExpr node) {
      this.defaultIn(node);
   }

   public void outADynamicInvokeExpr(ADynamicInvokeExpr node) {
      this.defaultOut(node);
   }

   public void caseADynamicInvokeExpr(ADynamicInvokeExpr node) {
      this.inADynamicInvokeExpr(node);
      if (node.getDynamicinvoke() != null) {
         node.getDynamicinvoke().apply(this);
      }

      if (node.getStringConstant() != null) {
         node.getStringConstant().apply(this);
      }

      if (node.getDynmethod() != null) {
         node.getDynmethod().apply(this);
      }

      if (node.getFirstl() != null) {
         node.getFirstl().apply(this);
      }

      if (node.getDynargs() != null) {
         node.getDynargs().apply(this);
      }

      if (node.getFirstr() != null) {
         node.getFirstr().apply(this);
      }

      if (node.getBsm() != null) {
         node.getBsm().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getStaticargs() != null) {
         node.getStaticargs().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      this.outADynamicInvokeExpr(node);
   }

   public void inABinopExpr(ABinopExpr node) {
      this.defaultIn(node);
   }

   public void outABinopExpr(ABinopExpr node) {
      this.defaultOut(node);
   }

   public void caseABinopExpr(ABinopExpr node) {
      this.inABinopExpr(node);
      if (node.getLeft() != null) {
         node.getLeft().apply(this);
      }

      if (node.getBinop() != null) {
         node.getBinop().apply(this);
      }

      if (node.getRight() != null) {
         node.getRight().apply(this);
      }

      this.outABinopExpr(node);
   }

   public void inAUnopExpr(AUnopExpr node) {
      this.defaultIn(node);
   }

   public void outAUnopExpr(AUnopExpr node) {
      this.defaultOut(node);
   }

   public void caseAUnopExpr(AUnopExpr node) {
      this.inAUnopExpr(node);
      if (node.getUnop() != null) {
         node.getUnop().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      this.outAUnopExpr(node);
   }

   public void inASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
      this.defaultIn(node);
   }

   public void outASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
      this.defaultOut(node);
   }

   public void caseASpecialNonstaticInvoke(ASpecialNonstaticInvoke node) {
      this.inASpecialNonstaticInvoke(node);
      if (node.getSpecialinvoke() != null) {
         node.getSpecialinvoke().apply(this);
      }

      this.outASpecialNonstaticInvoke(node);
   }

   public void inAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
      this.defaultIn(node);
   }

   public void outAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
      this.defaultOut(node);
   }

   public void caseAVirtualNonstaticInvoke(AVirtualNonstaticInvoke node) {
      this.inAVirtualNonstaticInvoke(node);
      if (node.getVirtualinvoke() != null) {
         node.getVirtualinvoke().apply(this);
      }

      this.outAVirtualNonstaticInvoke(node);
   }

   public void inAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
      this.defaultIn(node);
   }

   public void outAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
      this.defaultOut(node);
   }

   public void caseAInterfaceNonstaticInvoke(AInterfaceNonstaticInvoke node) {
      this.inAInterfaceNonstaticInvoke(node);
      if (node.getInterfaceinvoke() != null) {
         node.getInterfaceinvoke().apply(this);
      }

      this.outAInterfaceNonstaticInvoke(node);
   }

   public void inAUnnamedMethodSignature(AUnnamedMethodSignature node) {
      this.defaultIn(node);
   }

   public void outAUnnamedMethodSignature(AUnnamedMethodSignature node) {
      this.defaultOut(node);
   }

   public void caseAUnnamedMethodSignature(AUnnamedMethodSignature node) {
      this.inAUnnamedMethodSignature(node);
      if (node.getCmplt() != null) {
         node.getCmplt().apply(this);
      }

      if (node.getType() != null) {
         node.getType().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getParameterList() != null) {
         node.getParameterList().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getCmpgt() != null) {
         node.getCmpgt().apply(this);
      }

      this.outAUnnamedMethodSignature(node);
   }

   public void inAMethodSignature(AMethodSignature node) {
      this.defaultIn(node);
   }

   public void outAMethodSignature(AMethodSignature node) {
      this.defaultOut(node);
   }

   public void caseAMethodSignature(AMethodSignature node) {
      this.inAMethodSignature(node);
      if (node.getCmplt() != null) {
         node.getCmplt().apply(this);
      }

      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      if (node.getFirst() != null) {
         node.getFirst().apply(this);
      }

      if (node.getType() != null) {
         node.getType().apply(this);
      }

      if (node.getMethodName() != null) {
         node.getMethodName().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getParameterList() != null) {
         node.getParameterList().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getCmpgt() != null) {
         node.getCmpgt().apply(this);
      }

      this.outAMethodSignature(node);
   }

   public void inAArrayReference(AArrayReference node) {
      this.defaultIn(node);
   }

   public void outAArrayReference(AArrayReference node) {
      this.defaultOut(node);
   }

   public void caseAArrayReference(AArrayReference node) {
      this.inAArrayReference(node);
      if (node.getArrayRef() != null) {
         node.getArrayRef().apply(this);
      }

      this.outAArrayReference(node);
   }

   public void inAFieldReference(AFieldReference node) {
      this.defaultIn(node);
   }

   public void outAFieldReference(AFieldReference node) {
      this.defaultOut(node);
   }

   public void caseAFieldReference(AFieldReference node) {
      this.inAFieldReference(node);
      if (node.getFieldRef() != null) {
         node.getFieldRef().apply(this);
      }

      this.outAFieldReference(node);
   }

   public void inAIdentArrayRef(AIdentArrayRef node) {
      this.defaultIn(node);
   }

   public void outAIdentArrayRef(AIdentArrayRef node) {
      this.defaultOut(node);
   }

   public void caseAIdentArrayRef(AIdentArrayRef node) {
      this.inAIdentArrayRef(node);
      if (node.getIdentifier() != null) {
         node.getIdentifier().apply(this);
      }

      if (node.getFixedArrayDescriptor() != null) {
         node.getFixedArrayDescriptor().apply(this);
      }

      this.outAIdentArrayRef(node);
   }

   public void inAQuotedArrayRef(AQuotedArrayRef node) {
      this.defaultIn(node);
   }

   public void outAQuotedArrayRef(AQuotedArrayRef node) {
      this.defaultOut(node);
   }

   public void caseAQuotedArrayRef(AQuotedArrayRef node) {
      this.inAQuotedArrayRef(node);
      if (node.getQuotedName() != null) {
         node.getQuotedName().apply(this);
      }

      if (node.getFixedArrayDescriptor() != null) {
         node.getFixedArrayDescriptor().apply(this);
      }

      this.outAQuotedArrayRef(node);
   }

   public void inALocalFieldRef(ALocalFieldRef node) {
      this.defaultIn(node);
   }

   public void outALocalFieldRef(ALocalFieldRef node) {
      this.defaultOut(node);
   }

   public void caseALocalFieldRef(ALocalFieldRef node) {
      this.inALocalFieldRef(node);
      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      if (node.getDot() != null) {
         node.getDot().apply(this);
      }

      if (node.getFieldSignature() != null) {
         node.getFieldSignature().apply(this);
      }

      this.outALocalFieldRef(node);
   }

   public void inASigFieldRef(ASigFieldRef node) {
      this.defaultIn(node);
   }

   public void outASigFieldRef(ASigFieldRef node) {
      this.defaultOut(node);
   }

   public void caseASigFieldRef(ASigFieldRef node) {
      this.inASigFieldRef(node);
      if (node.getFieldSignature() != null) {
         node.getFieldSignature().apply(this);
      }

      this.outASigFieldRef(node);
   }

   public void inAFieldSignature(AFieldSignature node) {
      this.defaultIn(node);
   }

   public void outAFieldSignature(AFieldSignature node) {
      this.defaultOut(node);
   }

   public void caseAFieldSignature(AFieldSignature node) {
      this.inAFieldSignature(node);
      if (node.getCmplt() != null) {
         node.getCmplt().apply(this);
      }

      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      if (node.getFirst() != null) {
         node.getFirst().apply(this);
      }

      if (node.getType() != null) {
         node.getType().apply(this);
      }

      if (node.getFieldName() != null) {
         node.getFieldName().apply(this);
      }

      if (node.getCmpgt() != null) {
         node.getCmpgt().apply(this);
      }

      this.outAFieldSignature(node);
   }

   public void inAFixedArrayDescriptor(AFixedArrayDescriptor node) {
      this.defaultIn(node);
   }

   public void outAFixedArrayDescriptor(AFixedArrayDescriptor node) {
      this.defaultOut(node);
   }

   public void caseAFixedArrayDescriptor(AFixedArrayDescriptor node) {
      this.inAFixedArrayDescriptor(node);
      if (node.getLBracket() != null) {
         node.getLBracket().apply(this);
      }

      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getRBracket() != null) {
         node.getRBracket().apply(this);
      }

      this.outAFixedArrayDescriptor(node);
   }

   public void inASingleArgList(ASingleArgList node) {
      this.defaultIn(node);
   }

   public void outASingleArgList(ASingleArgList node) {
      this.defaultOut(node);
   }

   public void caseASingleArgList(ASingleArgList node) {
      this.inASingleArgList(node);
      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      this.outASingleArgList(node);
   }

   public void inAMultiArgList(AMultiArgList node) {
      this.defaultIn(node);
   }

   public void outAMultiArgList(AMultiArgList node) {
      this.defaultOut(node);
   }

   public void caseAMultiArgList(AMultiArgList node) {
      this.inAMultiArgList(node);
      if (node.getImmediate() != null) {
         node.getImmediate().apply(this);
      }

      if (node.getComma() != null) {
         node.getComma().apply(this);
      }

      if (node.getArgList() != null) {
         node.getArgList().apply(this);
      }

      this.outAMultiArgList(node);
   }

   public void inALocalImmediate(ALocalImmediate node) {
      this.defaultIn(node);
   }

   public void outALocalImmediate(ALocalImmediate node) {
      this.defaultOut(node);
   }

   public void caseALocalImmediate(ALocalImmediate node) {
      this.inALocalImmediate(node);
      if (node.getLocalName() != null) {
         node.getLocalName().apply(this);
      }

      this.outALocalImmediate(node);
   }

   public void inAConstantImmediate(AConstantImmediate node) {
      this.defaultIn(node);
   }

   public void outAConstantImmediate(AConstantImmediate node) {
      this.defaultOut(node);
   }

   public void caseAConstantImmediate(AConstantImmediate node) {
      this.inAConstantImmediate(node);
      if (node.getConstant() != null) {
         node.getConstant().apply(this);
      }

      this.outAConstantImmediate(node);
   }

   public void inAIntegerConstant(AIntegerConstant node) {
      this.defaultIn(node);
   }

   public void outAIntegerConstant(AIntegerConstant node) {
      this.defaultOut(node);
   }

   public void caseAIntegerConstant(AIntegerConstant node) {
      this.inAIntegerConstant(node);
      if (node.getMinus() != null) {
         node.getMinus().apply(this);
      }

      if (node.getIntegerConstant() != null) {
         node.getIntegerConstant().apply(this);
      }

      this.outAIntegerConstant(node);
   }

   public void inAFloatConstant(AFloatConstant node) {
      this.defaultIn(node);
   }

   public void outAFloatConstant(AFloatConstant node) {
      this.defaultOut(node);
   }

   public void caseAFloatConstant(AFloatConstant node) {
      this.inAFloatConstant(node);
      if (node.getMinus() != null) {
         node.getMinus().apply(this);
      }

      if (node.getFloatConstant() != null) {
         node.getFloatConstant().apply(this);
      }

      this.outAFloatConstant(node);
   }

   public void inAStringConstant(AStringConstant node) {
      this.defaultIn(node);
   }

   public void outAStringConstant(AStringConstant node) {
      this.defaultOut(node);
   }

   public void caseAStringConstant(AStringConstant node) {
      this.inAStringConstant(node);
      if (node.getStringConstant() != null) {
         node.getStringConstant().apply(this);
      }

      this.outAStringConstant(node);
   }

   public void inAClzzConstant(AClzzConstant node) {
      this.defaultIn(node);
   }

   public void outAClzzConstant(AClzzConstant node) {
      this.defaultOut(node);
   }

   public void caseAClzzConstant(AClzzConstant node) {
      this.inAClzzConstant(node);
      if (node.getId() != null) {
         node.getId().apply(this);
      }

      if (node.getStringConstant() != null) {
         node.getStringConstant().apply(this);
      }

      this.outAClzzConstant(node);
   }

   public void inANullConstant(ANullConstant node) {
      this.defaultIn(node);
   }

   public void outANullConstant(ANullConstant node) {
      this.defaultOut(node);
   }

   public void caseANullConstant(ANullConstant node) {
      this.inANullConstant(node);
      if (node.getNull() != null) {
         node.getNull().apply(this);
      }

      this.outANullConstant(node);
   }

   public void inAAndBinop(AAndBinop node) {
      this.defaultIn(node);
   }

   public void outAAndBinop(AAndBinop node) {
      this.defaultOut(node);
   }

   public void caseAAndBinop(AAndBinop node) {
      this.inAAndBinop(node);
      if (node.getAnd() != null) {
         node.getAnd().apply(this);
      }

      this.outAAndBinop(node);
   }

   public void inAOrBinop(AOrBinop node) {
      this.defaultIn(node);
   }

   public void outAOrBinop(AOrBinop node) {
      this.defaultOut(node);
   }

   public void caseAOrBinop(AOrBinop node) {
      this.inAOrBinop(node);
      if (node.getOr() != null) {
         node.getOr().apply(this);
      }

      this.outAOrBinop(node);
   }

   public void inAXorBinop(AXorBinop node) {
      this.defaultIn(node);
   }

   public void outAXorBinop(AXorBinop node) {
      this.defaultOut(node);
   }

   public void caseAXorBinop(AXorBinop node) {
      this.inAXorBinop(node);
      if (node.getXor() != null) {
         node.getXor().apply(this);
      }

      this.outAXorBinop(node);
   }

   public void inAModBinop(AModBinop node) {
      this.defaultIn(node);
   }

   public void outAModBinop(AModBinop node) {
      this.defaultOut(node);
   }

   public void caseAModBinop(AModBinop node) {
      this.inAModBinop(node);
      if (node.getMod() != null) {
         node.getMod().apply(this);
      }

      this.outAModBinop(node);
   }

   public void inACmpBinop(ACmpBinop node) {
      this.defaultIn(node);
   }

   public void outACmpBinop(ACmpBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpBinop(ACmpBinop node) {
      this.inACmpBinop(node);
      if (node.getCmp() != null) {
         node.getCmp().apply(this);
      }

      this.outACmpBinop(node);
   }

   public void inACmpgBinop(ACmpgBinop node) {
      this.defaultIn(node);
   }

   public void outACmpgBinop(ACmpgBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpgBinop(ACmpgBinop node) {
      this.inACmpgBinop(node);
      if (node.getCmpg() != null) {
         node.getCmpg().apply(this);
      }

      this.outACmpgBinop(node);
   }

   public void inACmplBinop(ACmplBinop node) {
      this.defaultIn(node);
   }

   public void outACmplBinop(ACmplBinop node) {
      this.defaultOut(node);
   }

   public void caseACmplBinop(ACmplBinop node) {
      this.inACmplBinop(node);
      if (node.getCmpl() != null) {
         node.getCmpl().apply(this);
      }

      this.outACmplBinop(node);
   }

   public void inACmpeqBinop(ACmpeqBinop node) {
      this.defaultIn(node);
   }

   public void outACmpeqBinop(ACmpeqBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpeqBinop(ACmpeqBinop node) {
      this.inACmpeqBinop(node);
      if (node.getCmpeq() != null) {
         node.getCmpeq().apply(this);
      }

      this.outACmpeqBinop(node);
   }

   public void inACmpneBinop(ACmpneBinop node) {
      this.defaultIn(node);
   }

   public void outACmpneBinop(ACmpneBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpneBinop(ACmpneBinop node) {
      this.inACmpneBinop(node);
      if (node.getCmpne() != null) {
         node.getCmpne().apply(this);
      }

      this.outACmpneBinop(node);
   }

   public void inACmpgtBinop(ACmpgtBinop node) {
      this.defaultIn(node);
   }

   public void outACmpgtBinop(ACmpgtBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpgtBinop(ACmpgtBinop node) {
      this.inACmpgtBinop(node);
      if (node.getCmpgt() != null) {
         node.getCmpgt().apply(this);
      }

      this.outACmpgtBinop(node);
   }

   public void inACmpgeBinop(ACmpgeBinop node) {
      this.defaultIn(node);
   }

   public void outACmpgeBinop(ACmpgeBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpgeBinop(ACmpgeBinop node) {
      this.inACmpgeBinop(node);
      if (node.getCmpge() != null) {
         node.getCmpge().apply(this);
      }

      this.outACmpgeBinop(node);
   }

   public void inACmpltBinop(ACmpltBinop node) {
      this.defaultIn(node);
   }

   public void outACmpltBinop(ACmpltBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpltBinop(ACmpltBinop node) {
      this.inACmpltBinop(node);
      if (node.getCmplt() != null) {
         node.getCmplt().apply(this);
      }

      this.outACmpltBinop(node);
   }

   public void inACmpleBinop(ACmpleBinop node) {
      this.defaultIn(node);
   }

   public void outACmpleBinop(ACmpleBinop node) {
      this.defaultOut(node);
   }

   public void caseACmpleBinop(ACmpleBinop node) {
      this.inACmpleBinop(node);
      if (node.getCmple() != null) {
         node.getCmple().apply(this);
      }

      this.outACmpleBinop(node);
   }

   public void inAShlBinop(AShlBinop node) {
      this.defaultIn(node);
   }

   public void outAShlBinop(AShlBinop node) {
      this.defaultOut(node);
   }

   public void caseAShlBinop(AShlBinop node) {
      this.inAShlBinop(node);
      if (node.getShl() != null) {
         node.getShl().apply(this);
      }

      this.outAShlBinop(node);
   }

   public void inAShrBinop(AShrBinop node) {
      this.defaultIn(node);
   }

   public void outAShrBinop(AShrBinop node) {
      this.defaultOut(node);
   }

   public void caseAShrBinop(AShrBinop node) {
      this.inAShrBinop(node);
      if (node.getShr() != null) {
         node.getShr().apply(this);
      }

      this.outAShrBinop(node);
   }

   public void inAUshrBinop(AUshrBinop node) {
      this.defaultIn(node);
   }

   public void outAUshrBinop(AUshrBinop node) {
      this.defaultOut(node);
   }

   public void caseAUshrBinop(AUshrBinop node) {
      this.inAUshrBinop(node);
      if (node.getUshr() != null) {
         node.getUshr().apply(this);
      }

      this.outAUshrBinop(node);
   }

   public void inAPlusBinop(APlusBinop node) {
      this.defaultIn(node);
   }

   public void outAPlusBinop(APlusBinop node) {
      this.defaultOut(node);
   }

   public void caseAPlusBinop(APlusBinop node) {
      this.inAPlusBinop(node);
      if (node.getPlus() != null) {
         node.getPlus().apply(this);
      }

      this.outAPlusBinop(node);
   }

   public void inAMinusBinop(AMinusBinop node) {
      this.defaultIn(node);
   }

   public void outAMinusBinop(AMinusBinop node) {
      this.defaultOut(node);
   }

   public void caseAMinusBinop(AMinusBinop node) {
      this.inAMinusBinop(node);
      if (node.getMinus() != null) {
         node.getMinus().apply(this);
      }

      this.outAMinusBinop(node);
   }

   public void inAMultBinop(AMultBinop node) {
      this.defaultIn(node);
   }

   public void outAMultBinop(AMultBinop node) {
      this.defaultOut(node);
   }

   public void caseAMultBinop(AMultBinop node) {
      this.inAMultBinop(node);
      if (node.getMult() != null) {
         node.getMult().apply(this);
      }

      this.outAMultBinop(node);
   }

   public void inADivBinop(ADivBinop node) {
      this.defaultIn(node);
   }

   public void outADivBinop(ADivBinop node) {
      this.defaultOut(node);
   }

   public void caseADivBinop(ADivBinop node) {
      this.inADivBinop(node);
      if (node.getDiv() != null) {
         node.getDiv().apply(this);
      }

      this.outADivBinop(node);
   }

   public void inALengthofUnop(ALengthofUnop node) {
      this.defaultIn(node);
   }

   public void outALengthofUnop(ALengthofUnop node) {
      this.defaultOut(node);
   }

   public void caseALengthofUnop(ALengthofUnop node) {
      this.inALengthofUnop(node);
      if (node.getLengthof() != null) {
         node.getLengthof().apply(this);
      }

      this.outALengthofUnop(node);
   }

   public void inANegUnop(ANegUnop node) {
      this.defaultIn(node);
   }

   public void outANegUnop(ANegUnop node) {
      this.defaultOut(node);
   }

   public void caseANegUnop(ANegUnop node) {
      this.inANegUnop(node);
      if (node.getNeg() != null) {
         node.getNeg().apply(this);
      }

      this.outANegUnop(node);
   }

   public void inAQuotedClassName(AQuotedClassName node) {
      this.defaultIn(node);
   }

   public void outAQuotedClassName(AQuotedClassName node) {
      this.defaultOut(node);
   }

   public void caseAQuotedClassName(AQuotedClassName node) {
      this.inAQuotedClassName(node);
      if (node.getQuotedName() != null) {
         node.getQuotedName().apply(this);
      }

      this.outAQuotedClassName(node);
   }

   public void inAIdentClassName(AIdentClassName node) {
      this.defaultIn(node);
   }

   public void outAIdentClassName(AIdentClassName node) {
      this.defaultOut(node);
   }

   public void caseAIdentClassName(AIdentClassName node) {
      this.inAIdentClassName(node);
      if (node.getIdentifier() != null) {
         node.getIdentifier().apply(this);
      }

      this.outAIdentClassName(node);
   }

   public void inAFullIdentClassName(AFullIdentClassName node) {
      this.defaultIn(node);
   }

   public void outAFullIdentClassName(AFullIdentClassName node) {
      this.defaultOut(node);
   }

   public void caseAFullIdentClassName(AFullIdentClassName node) {
      this.inAFullIdentClassName(node);
      if (node.getFullIdentifier() != null) {
         node.getFullIdentifier().apply(this);
      }

      this.outAFullIdentClassName(node);
   }

   public void inAQuotedName(AQuotedName node) {
      this.defaultIn(node);
   }

   public void outAQuotedName(AQuotedName node) {
      this.defaultOut(node);
   }

   public void caseAQuotedName(AQuotedName node) {
      this.inAQuotedName(node);
      if (node.getQuotedName() != null) {
         node.getQuotedName().apply(this);
      }

      this.outAQuotedName(node);
   }

   public void inAIdentName(AIdentName node) {
      this.defaultIn(node);
   }

   public void outAIdentName(AIdentName node) {
      this.defaultOut(node);
   }

   public void caseAIdentName(AIdentName node) {
      this.inAIdentName(node);
      if (node.getIdentifier() != null) {
         node.getIdentifier().apply(this);
      }

      this.outAIdentName(node);
   }
}
