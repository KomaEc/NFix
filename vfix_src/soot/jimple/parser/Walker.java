package soot.jimple.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.Immediate;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnknownType;
import soot.Value;
import soot.VoidType;
import soot.jimple.BinopExpr;
import soot.jimple.ClassConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.UnopExpr;
import soot.jimple.parser.analysis.DepthFirstAdapter;
import soot.jimple.parser.node.AAbstractModifier;
import soot.jimple.parser.node.AAndBinop;
import soot.jimple.parser.node.AAnnotationModifier;
import soot.jimple.parser.node.AArrayDescriptor;
import soot.jimple.parser.node.AArrayNewExpr;
import soot.jimple.parser.node.AArrayReference;
import soot.jimple.parser.node.AAssignStatement;
import soot.jimple.parser.node.ABaseNonvoidType;
import soot.jimple.parser.node.ABinopBoolExpr;
import soot.jimple.parser.node.ABinopExpr;
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
import soot.jimple.parser.node.ADeclaration;
import soot.jimple.parser.node.ADivBinop;
import soot.jimple.parser.node.ADoubleBaseType;
import soot.jimple.parser.node.ADoubleBaseTypeNoName;
import soot.jimple.parser.node.ADynamicInvokeExpr;
import soot.jimple.parser.node.AEntermonitorStatement;
import soot.jimple.parser.node.AEnumModifier;
import soot.jimple.parser.node.AExitmonitorStatement;
import soot.jimple.parser.node.AFieldMember;
import soot.jimple.parser.node.AFieldSignature;
import soot.jimple.parser.node.AFile;
import soot.jimple.parser.node.AFinalModifier;
import soot.jimple.parser.node.AFloatBaseType;
import soot.jimple.parser.node.AFloatBaseTypeNoName;
import soot.jimple.parser.node.AFloatConstant;
import soot.jimple.parser.node.AFullIdentNonvoidType;
import soot.jimple.parser.node.AFullMethodBody;
import soot.jimple.parser.node.AGotoStatement;
import soot.jimple.parser.node.AIdentNonvoidType;
import soot.jimple.parser.node.AIdentityNoTypeStatement;
import soot.jimple.parser.node.AIdentityStatement;
import soot.jimple.parser.node.AIfStatement;
import soot.jimple.parser.node.AInstanceofExpression;
import soot.jimple.parser.node.AIntBaseType;
import soot.jimple.parser.node.AIntBaseTypeNoName;
import soot.jimple.parser.node.AIntegerConstant;
import soot.jimple.parser.node.AInterfaceFileType;
import soot.jimple.parser.node.AInterfaceNonstaticInvoke;
import soot.jimple.parser.node.AInvokeStatement;
import soot.jimple.parser.node.ALabelStatement;
import soot.jimple.parser.node.ALengthofUnop;
import soot.jimple.parser.node.ALocalFieldRef;
import soot.jimple.parser.node.ALocalImmediate;
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
import soot.jimple.parser.node.AMultiNewExpr;
import soot.jimple.parser.node.AMultiParameterList;
import soot.jimple.parser.node.ANativeModifier;
import soot.jimple.parser.node.ANegUnop;
import soot.jimple.parser.node.ANonstaticInvokeExpr;
import soot.jimple.parser.node.ANopStatement;
import soot.jimple.parser.node.ANovoidType;
import soot.jimple.parser.node.ANullBaseType;
import soot.jimple.parser.node.ANullBaseTypeNoName;
import soot.jimple.parser.node.ANullConstant;
import soot.jimple.parser.node.AOrBinop;
import soot.jimple.parser.node.APlusBinop;
import soot.jimple.parser.node.APrivateModifier;
import soot.jimple.parser.node.AProtectedModifier;
import soot.jimple.parser.node.APublicModifier;
import soot.jimple.parser.node.AQuotedNonvoidType;
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
import soot.jimple.parser.node.AUnopExpr;
import soot.jimple.parser.node.AUnopExpression;
import soot.jimple.parser.node.AUshrBinop;
import soot.jimple.parser.node.AVirtualNonstaticInvoke;
import soot.jimple.parser.node.AVoidType;
import soot.jimple.parser.node.AVolatileModifier;
import soot.jimple.parser.node.AXorBinop;
import soot.jimple.parser.node.Node;
import soot.jimple.parser.node.PModifier;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.node.TAtIdentifier;
import soot.jimple.parser.node.TFloatConstant;
import soot.jimple.parser.node.TFullIdentifier;
import soot.jimple.parser.node.TIdentifier;
import soot.jimple.parser.node.TIntegerConstant;
import soot.jimple.parser.node.TQuotedName;
import soot.jimple.parser.node.TStringConstant;
import soot.jimple.parser.node.Token;
import soot.util.StringTools;

public class Walker extends DepthFirstAdapter {
   private static final Logger logger = LoggerFactory.getLogger(Walker.class);
   boolean debug = false;
   LinkedList mProductions = new LinkedList();
   SootClass mSootClass = null;
   Map<String, Local> mLocals = null;
   Value mValue = IntConstant.v(1);
   Map<Object, Unit> mLabelToStmtMap;
   Map<String, List> mLabelToPatchList;
   protected final SootResolver mResolver;

   public Walker(SootResolver resolver) {
      this.mResolver = resolver;
      if (this.debug) {
         this.mProductions = new LinkedList() {
            public Object removeLast() {
               Object o = super.removeLast();
               if (Walker.this.debug) {
                  Walker.logger.debug("popped: " + o);
               }

               return o;
            }
         };
      }

   }

   public Walker(SootClass sc, SootResolver resolver) {
      this.mSootClass = sc;
      this.mResolver = resolver;
   }

   public void outStart(Start node) {
      SootClass c = (SootClass)this.mProductions.removeLast();
   }

   public SootClass getSootClass() {
      if (this.mSootClass == null) {
         throw new RuntimeException("did not parse class yet....");
      } else {
         return this.mSootClass;
      }
   }

   public void inAFile(AFile node) {
      if (this.debug) {
         logger.debug("reading class " + node.getClassName());
      }

   }

   public void caseAFile(AFile node) {
      this.inAFile(node);
      Object[] temp = node.getModifier().toArray();
      Object[] var3 = temp;
      int var4 = temp.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object element = var3[var5];
         ((PModifier)element).apply(this);
      }

      if (node.getFileType() != null) {
         node.getFileType().apply(this);
      }

      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      String className = (String)this.mProductions.removeLast();
      if (this.mSootClass == null) {
         this.mSootClass = new SootClass(className);
         this.mSootClass.setResolvingLevel(3);
      } else if (!this.mSootClass.getName().equals(className)) {
         throw new RuntimeException("Invalid SootClass for this JimpleAST. The SootClass provided is of type: >" + this.mSootClass.getName() + "< whereas this parse tree is for type: >" + className + "<");
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

   public void outAFile(AFile node) {
      List implementsList = null;
      String superClass = null;
      String classType = null;
      if (node.getImplementsClause() != null) {
         implementsList = (List)this.mProductions.removeLast();
      }

      if (node.getExtendsClause() != null) {
         superClass = (String)this.mProductions.removeLast();
      }

      classType = (String)this.mProductions.removeLast();
      int modifierCount = node.getModifier().size();
      int modifierFlags = this.processModifiers(node.getModifier());
      if (classType.equals("interface")) {
         modifierFlags |= 512;
      }

      this.mSootClass.setModifiers(modifierFlags);
      if (superClass != null) {
         this.mSootClass.setSuperclass(this.mResolver.makeClassRef(superClass));
      }

      if (implementsList != null) {
         Iterator implIt = implementsList.iterator();

         while(implIt.hasNext()) {
            SootClass interfaceClass = this.mResolver.makeClassRef((String)implIt.next());
            this.mSootClass.addInterface(interfaceClass);
         }
      }

      this.mProductions.addLast(this.mSootClass);
   }

   public void outAFieldMember(AFieldMember node) {
      int modifier = false;
      Type type = null;
      String name = null;
      name = (String)this.mProductions.removeLast();
      type = (Type)this.mProductions.removeLast();
      int modifier = this.processModifiers(node.getModifier());
      SootField f = Scene.v().makeSootField(name, type, modifier);
      this.mSootClass.addField(f);
   }

   public void outAMethodMember(AMethodMember node) {
      int modifier = false;
      List parameterList = null;
      List<SootClass> throwsClause = null;
      JimpleBody methodBody = null;
      if (node.getMethodBody() instanceof AFullMethodBody) {
         methodBody = (JimpleBody)this.mProductions.removeLast();
      }

      if (node.getThrowsClause() != null) {
         throwsClause = (List)this.mProductions.removeLast();
      }

      if (node.getParameterList() != null) {
         parameterList = (List)this.mProductions.removeLast();
      } else {
         parameterList = new ArrayList();
      }

      Object o = this.mProductions.removeLast();
      String name = (String)o;
      Type type = (Type)this.mProductions.removeLast();
      int modifier = this.processModifiers(node.getModifier());
      SootMethod method;
      if (throwsClause != null) {
         method = Scene.v().makeSootMethod(name, (List)parameterList, type, modifier, throwsClause);
      } else {
         method = Scene.v().makeSootMethod(name, (List)parameterList, type, modifier);
      }

      this.mSootClass.addMethod(method);
      if (method.isConcrete()) {
         methodBody.setMethod(method);
         method.setActiveBody(methodBody);
      } else if (node.getMethodBody() instanceof AFullMethodBody) {
         throw new RuntimeException("Impossible: !concrete => ! instanceof");
      }

   }

   public void outAVoidType(AVoidType node) {
      this.mProductions.addLast(VoidType.v());
   }

   public void outABaseNonvoidType(ABaseNonvoidType node) {
      Type t = (Type)this.mProductions.removeLast();
      int dim = node.getArrayBrackets().size();
      if (dim > 0) {
         t = ArrayType.v((Type)t, dim);
      }

      this.mProductions.addLast(t);
   }

   public void outAQuotedNonvoidType(AQuotedNonvoidType node) {
      String typeName = (String)this.mProductions.removeLast();
      Type t = RefType.v(typeName);
      int dim = node.getArrayBrackets().size();
      if (dim > 0) {
         t = ArrayType.v((Type)t, dim);
      }

      this.mProductions.addLast(t);
   }

   public void outAIdentNonvoidType(AIdentNonvoidType node) {
      String typeName = (String)this.mProductions.removeLast();
      Type t = RefType.v(typeName);
      int dim = node.getArrayBrackets().size();
      if (dim > 0) {
         t = ArrayType.v((Type)t, dim);
      }

      this.mProductions.addLast(t);
   }

   public void outAFullIdentNonvoidType(AFullIdentNonvoidType node) {
      String typeName = (String)this.mProductions.removeLast();
      Type t = RefType.v(typeName);
      int dim = node.getArrayBrackets().size();
      if (dim > 0) {
         t = ArrayType.v((Type)t, dim);
      }

      this.mProductions.addLast(t);
   }

   public void outABooleanBaseTypeNoName(ABooleanBaseTypeNoName node) {
      this.mProductions.addLast(BooleanType.v());
   }

   public void outAByteBaseTypeNoName(AByteBaseTypeNoName node) {
      this.mProductions.addLast(ByteType.v());
   }

   public void outACharBaseTypeNoName(ACharBaseTypeNoName node) {
      this.mProductions.addLast(CharType.v());
   }

   public void outAShortBaseTypeNoName(AShortBaseTypeNoName node) {
      this.mProductions.addLast(ShortType.v());
   }

   public void outAIntBaseTypeNoName(AIntBaseTypeNoName node) {
      this.mProductions.addLast(IntType.v());
   }

   public void outALongBaseTypeNoName(ALongBaseTypeNoName node) {
      this.mProductions.addLast(LongType.v());
   }

   public void outAFloatBaseTypeNoName(AFloatBaseTypeNoName node) {
      this.mProductions.addLast(FloatType.v());
   }

   public void outADoubleBaseTypeNoName(ADoubleBaseTypeNoName node) {
      this.mProductions.addLast(DoubleType.v());
   }

   public void outANullBaseTypeNoName(ANullBaseTypeNoName node) {
      this.mProductions.addLast(NullType.v());
   }

   public void outABooleanBaseType(ABooleanBaseType node) {
      this.mProductions.addLast(BooleanType.v());
   }

   public void outAByteBaseType(AByteBaseType node) {
      this.mProductions.addLast(ByteType.v());
   }

   public void outACharBaseType(ACharBaseType node) {
      this.mProductions.addLast(CharType.v());
   }

   public void outAShortBaseType(AShortBaseType node) {
      this.mProductions.addLast(ShortType.v());
   }

   public void outAIntBaseType(AIntBaseType node) {
      this.mProductions.addLast(IntType.v());
   }

   public void outALongBaseType(ALongBaseType node) {
      this.mProductions.addLast(LongType.v());
   }

   public void outAFloatBaseType(AFloatBaseType node) {
      this.mProductions.addLast(FloatType.v());
   }

   public void outADoubleBaseType(ADoubleBaseType node) {
      this.mProductions.addLast(DoubleType.v());
   }

   public void outANullBaseType(ANullBaseType node) {
      this.mProductions.addLast(NullType.v());
   }

   public void outAClassNameBaseType(AClassNameBaseType node) {
      String type = (String)this.mProductions.removeLast();
      if (type.equals("int")) {
         throw new RuntimeException();
      } else {
         this.mProductions.addLast(RefType.v(type));
      }
   }

   public void inAFullMethodBody(AFullMethodBody node) {
      this.mLocals = new HashMap();
      this.mLabelToStmtMap = new HashMap();
      this.mLabelToPatchList = new HashMap();
   }

   public void outAFullMethodBody(AFullMethodBody node) {
      JimpleBody jBody = Jimple.v().newBody();
      int size;
      int i;
      if (node.getCatchClause() != null) {
         size = node.getCatchClause().size();

         for(i = 0; i < size; ++i) {
            jBody.getTraps().addFirst((Trap)this.mProductions.removeLast());
         }
      }

      if (node.getStatement() != null) {
         size = node.getStatement().size();
         Unit lastStmt = null;

         for(int i = 0; i < size; ++i) {
            Object o = this.mProductions.removeLast();
            if (o instanceof Unit) {
               jBody.getUnits().addFirst((Unit)o);
               lastStmt = (Unit)o;
            } else {
               if (!(o instanceof String)) {
                  throw new RuntimeException("impossible");
               }

               if (lastStmt == null) {
                  throw new RuntimeException("impossible");
               }

               this.mLabelToStmtMap.put(o, lastStmt);
            }
         }
      }

      if (node.getDeclaration() != null) {
         size = node.getDeclaration().size();

         for(i = 0; i < size; ++i) {
            List<Local> localList = (List)this.mProductions.removeLast();
            jBody.getLocals().addAll(localList);
         }
      }

      Iterator it = this.mLabelToPatchList.keySet().iterator();

      while(it.hasNext()) {
         String label = (String)it.next();
         Unit target = (Unit)this.mLabelToStmtMap.get(label);
         Iterator patchIt = ((List)this.mLabelToPatchList.get(label)).iterator();

         while(patchIt.hasNext()) {
            UnitBox box = (UnitBox)patchIt.next();
            box.setUnit(target);
         }
      }

      this.mProductions.addLast(jBody);
   }

   public void outANovoidType(ANovoidType node) {
   }

   public void outASingleParameterList(ASingleParameterList node) {
      List<Type> l = new ArrayList();
      l.add((Type)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outAMultiParameterList(AMultiParameterList node) {
      List<Type> l = (List)this.mProductions.removeLast();
      l.add(0, (Type)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outASingleArgList(ASingleArgList node) {
      List<Value> l = new ArrayList();
      l.add((Value)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outAMultiArgList(AMultiArgList node) {
      List<Value> l = (List)this.mProductions.removeLast();
      l.add(0, (Value)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outAClassNameSingleClassNameList(AClassNameSingleClassNameList node) {
      List<String> l = new ArrayList();
      l.add((String)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outAClassNameMultiClassNameList(AClassNameMultiClassNameList node) {
      List<String> l = (List)this.mProductions.removeLast();
      l.add(0, (String)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outAClassFileType(AClassFileType node) {
      this.mProductions.addLast("class");
   }

   public void outAInterfaceFileType(AInterfaceFileType node) {
      this.mProductions.addLast("interface");
   }

   public void outACatchClause(ACatchClause node) {
      UnitBox withUnit = Jimple.v().newStmtBox((Unit)null);
      this.addBoxToPatch((String)this.mProductions.removeLast(), withUnit);
      UnitBox toUnit = Jimple.v().newStmtBox((Unit)null);
      this.addBoxToPatch((String)this.mProductions.removeLast(), toUnit);
      UnitBox fromUnit = Jimple.v().newStmtBox((Unit)null);
      this.addBoxToPatch((String)this.mProductions.removeLast(), fromUnit);
      String exceptionName = (String)this.mProductions.removeLast();
      Trap trap = Jimple.v().newTrap(this.mResolver.makeClassRef(exceptionName), fromUnit, toUnit, withUnit);
      this.mProductions.addLast(trap);
   }

   public void outADeclaration(ADeclaration node) {
      List localNameList = (List)this.mProductions.removeLast();
      Type type = (Type)this.mProductions.removeLast();
      Iterator it = localNameList.iterator();
      ArrayList localList = new ArrayList();

      while(it.hasNext()) {
         Local l = Jimple.v().newLocal((String)it.next(), type);
         this.mLocals.put(l.getName(), l);
         localList.add(l);
      }

      this.mProductions.addLast(localList);
   }

   public void outAUnknownJimpleType(AUnknownJimpleType node) {
      this.mProductions.addLast(UnknownType.v());
   }

   public void outASingleLocalNameList(ASingleLocalNameList node) {
      List<String> l = new ArrayList();
      l.add((String)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outAMultiLocalNameList(AMultiLocalNameList node) {
      List<String> l = (List)this.mProductions.removeLast();
      l.add(0, (String)this.mProductions.removeLast());
      this.mProductions.addLast(l);
   }

   public void outALabelStatement(ALabelStatement node) {
   }

   public void outABreakpointStatement(ABreakpointStatement node) {
      Unit u = Jimple.v().newBreakpointStmt();
      this.mProductions.addLast(u);
   }

   public void outAEntermonitorStatement(AEntermonitorStatement node) {
      Value op = (Value)this.mProductions.removeLast();
      Unit u = Jimple.v().newEnterMonitorStmt(op);
      this.mProductions.addLast(u);
   }

   public void outAExitmonitorStatement(AExitmonitorStatement node) {
      Value op = (Value)this.mProductions.removeLast();
      Unit u = Jimple.v().newExitMonitorStmt(op);
      this.mProductions.addLast(u);
   }

   public void outACaseStmt(ACaseStmt node) {
      String labelName = (String)this.mProductions.removeLast();
      UnitBox box = Jimple.v().newStmtBox((Unit)null);
      this.addBoxToPatch(labelName, box);
      Value labelValue = null;
      if (node.getCaseLabel() instanceof AConstantCaseLabel) {
         labelValue = (Value)this.mProductions.removeLast();
      }

      if (labelValue == null) {
         this.mProductions.addLast(box);
      } else {
         Object[] valueTargetPair = new Object[]{labelValue, box};
         this.mProductions.addLast(valueTargetPair);
      }

   }

   public void outATableswitchStatement(ATableswitchStatement node) {
      List<UnitBox> targets = new ArrayList();
      UnitBox defaultTarget = null;
      int lowIndex = 0;
      int highIndex = 0;
      if (node.getCaseStmt() == null) {
         throw new RuntimeException("error: switch stmt has no case stmts");
      } else {
         int size = node.getCaseStmt().size();

         for(int i = 0; i < size; ++i) {
            Object valueTargetPair = this.mProductions.removeLast();
            if (valueTargetPair instanceof UnitBox) {
               if (defaultTarget != null) {
                  throw new RuntimeException("error: can't ;have more than 1 default stmt");
               }

               defaultTarget = (UnitBox)valueTargetPair;
            } else {
               Object[] pair = (Object[])((Object[])valueTargetPair);
               if (i == 0 && defaultTarget == null || i == 1 && defaultTarget != null) {
                  highIndex = ((IntConstant)pair[0]).value;
               }

               if (i == size - 1) {
                  lowIndex = ((IntConstant)pair[0]).value;
               }

               targets.add(0, (UnitBox)pair[1]);
            }
         }

         Value key = (Value)this.mProductions.removeLast();
         Unit switchStmt = Jimple.v().newTableSwitchStmt(key, lowIndex, highIndex, targets, (UnitBox)defaultTarget);
         this.mProductions.addLast(switchStmt);
      }
   }

   public void outALookupswitchStatement(ALookupswitchStatement node) {
      List<IntConstant> lookupValues = new ArrayList();
      List<UnitBox> targets = new ArrayList();
      UnitBox defaultTarget = null;
      if (node.getCaseStmt() != null) {
         int size = node.getCaseStmt().size();

         for(int i = 0; i < size; ++i) {
            Object valueTargetPair = this.mProductions.removeLast();
            if (valueTargetPair instanceof UnitBox) {
               if (defaultTarget != null) {
                  throw new RuntimeException("error: can't ;have more than 1 default stmt");
               }

               defaultTarget = (UnitBox)valueTargetPair;
            } else {
               Object[] pair = (Object[])((Object[])valueTargetPair);
               lookupValues.add(0, (IntConstant)pair[0]);
               targets.add(0, (UnitBox)pair[1]);
            }
         }

         Value key = (Value)this.mProductions.removeLast();
         Unit switchStmt = Jimple.v().newLookupSwitchStmt(key, lookupValues, targets, (UnitBox)defaultTarget);
         this.mProductions.addLast(switchStmt);
      } else {
         throw new RuntimeException("error: switch stmt has no case stmts");
      }
   }

   public void outAIdentityStatement(AIdentityStatement node) {
      Type identityRefType = (Type)this.mProductions.removeLast();
      String atClause = (String)this.mProductions.removeLast();
      Value local = (Value)this.mLocals.get(this.mProductions.removeLast());
      Value ref = null;
      if (atClause.startsWith("@this")) {
         ref = Jimple.v().newThisRef((RefType)identityRefType);
      } else {
         if (!atClause.startsWith("@parameter")) {
            throw new RuntimeException("shouldn't @caughtexception be handled by outAIdentityNoTypeStatement: got" + atClause);
         }

         int index = Integer.parseInt(atClause.substring(10, atClause.length() - 1));
         ref = Jimple.v().newParameterRef(identityRefType, index);
      }

      Unit u = Jimple.v().newIdentityStmt(local, (Value)ref);
      this.mProductions.addLast(u);
   }

   public void outAIdentityNoTypeStatement(AIdentityNoTypeStatement node) {
      this.mProductions.removeLast();
      Value local = (Value)this.mLocals.get(this.mProductions.removeLast());
      Unit u = Jimple.v().newIdentityStmt(local, Jimple.v().newCaughtExceptionRef());
      this.mProductions.addLast(u);
   }

   public void outAAssignStatement(AAssignStatement node) {
      Object removeLast = this.mProductions.removeLast();
      Value rvalue = (Value)removeLast;
      Value variable = (Value)this.mProductions.removeLast();
      Unit u = Jimple.v().newAssignStmt(variable, rvalue);
      this.mProductions.addLast(u);
   }

   public void outAIfStatement(AIfStatement node) {
      String targetLabel = (String)this.mProductions.removeLast();
      Value condition = (Value)this.mProductions.removeLast();
      UnitBox box = Jimple.v().newStmtBox((Unit)null);
      Unit u = Jimple.v().newIfStmt(condition, box);
      this.addBoxToPatch(targetLabel, box);
      this.mProductions.addLast(u);
   }

   public void outAReturnStatement(AReturnStatement node) {
      Stmt s = null;
      if (node.getImmediate() != null) {
         Immediate v = (Immediate)this.mProductions.removeLast();
         s = Jimple.v().newReturnStmt(v);
      } else {
         s = Jimple.v().newReturnVoidStmt();
      }

      this.mProductions.addLast(s);
   }

   public void outAGotoStatement(AGotoStatement node) {
      String targetLabel = (String)this.mProductions.removeLast();
      UnitBox box = Jimple.v().newStmtBox((Unit)null);
      Unit branch = Jimple.v().newGotoStmt(box);
      this.addBoxToPatch(targetLabel, box);
      this.mProductions.addLast(branch);
   }

   public void outANopStatement(ANopStatement node) {
      Unit u = Jimple.v().newNopStmt();
      this.mProductions.addLast(u);
   }

   public void outARetStatement(ARetStatement node) {
      throw new RuntimeException("ret not yet implemented.");
   }

   public void outAThrowStatement(AThrowStatement node) {
      Value op = (Value)this.mProductions.removeLast();
      Unit u = Jimple.v().newThrowStmt(op);
      this.mProductions.addLast(u);
   }

   public void outAInvokeStatement(AInvokeStatement node) {
      Value op = (Value)this.mProductions.removeLast();
      Unit u = Jimple.v().newInvokeStmt(op);
      this.mProductions.addLast(u);
   }

   public void outAConstantCaseLabel(AConstantCaseLabel node) {
      String s = (String)this.mProductions.removeLast();
      int sign = 1;
      if (node.getMinus() != null) {
         sign = -1;
      }

      if (s.endsWith("L")) {
         this.mProductions.addLast(LongConstant.v((long)sign * Long.parseLong(s.substring(0, s.length() - 1))));
      } else if (s.equals("2147483648")) {
         this.mProductions.addLast(IntConstant.v(sign * Integer.MIN_VALUE));
      } else {
         this.mProductions.addLast(IntConstant.v(sign * Integer.parseInt(s)));
      }

   }

   public void outALocalImmediate(ALocalImmediate node) {
      String local = (String)this.mProductions.removeLast();
      Local l = (Local)this.mLocals.get(local);
      if (l == null) {
         throw new RuntimeException("did not find local: " + local);
      } else {
         this.mProductions.addLast(l);
      }
   }

   public void outANullConstant(ANullConstant node) {
      this.mProductions.addLast(NullConstant.v());
   }

   public void outAIntegerConstant(AIntegerConstant node) {
      String s = (String)this.mProductions.removeLast();
      StringBuffer buf = new StringBuffer();
      if (node.getMinus() != null) {
         buf.append('-');
      }

      buf.append(s);
      s = buf.toString();
      if (s.endsWith("L")) {
         this.mProductions.addLast(LongConstant.v(Long.parseLong(s.substring(0, s.length() - 1))));
      } else if (s.equals("2147483648")) {
         this.mProductions.addLast(IntConstant.v(Integer.MIN_VALUE));
      } else {
         this.mProductions.addLast(IntConstant.v(Integer.parseInt(s)));
      }

   }

   public void outAStringConstant(AStringConstant node) {
      String s = (String)this.mProductions.removeLast();
      this.mProductions.addLast(StringConstant.v(s));
   }

   public void outAClzzConstant(AClzzConstant node) {
      String s = (String)this.mProductions.removeLast();
      this.mProductions.addLast(ClassConstant.v(s));
   }

   public void outAFloatConstant(AFloatConstant node) {
      String s = (String)this.mProductions.removeLast();
      boolean isDouble = true;
      float value = 0.0F;
      double dvalue = 0.0D;
      if (s.endsWith("f") || s.endsWith("F")) {
         isDouble = false;
      }

      if (s.charAt(0) == '#') {
         if (s.charAt(1) == '-') {
            if (isDouble) {
               dvalue = Double.NEGATIVE_INFINITY;
            } else {
               value = Float.NEGATIVE_INFINITY;
            }
         } else if (s.charAt(1) == 'I') {
            if (isDouble) {
               dvalue = Double.POSITIVE_INFINITY;
            } else {
               value = Float.POSITIVE_INFINITY;
            }
         } else if (isDouble) {
            dvalue = Double.NaN;
         } else {
            value = Float.NaN;
         }
      } else {
         StringBuffer buf = new StringBuffer();
         if (node.getMinus() != null) {
            buf.append('-');
         }

         buf.append(s);
         s = buf.toString();
         if (isDouble) {
            dvalue = Double.parseDouble(s);
         } else {
            value = Float.parseFloat(s);
         }
      }

      Object res;
      if (isDouble) {
         res = DoubleConstant.v(dvalue);
      } else {
         res = FloatConstant.v(value);
      }

      this.mProductions.addLast(res);
   }

   public void outABinopExpr(ABinopExpr node) {
      Value right = (Value)this.mProductions.removeLast();
      BinopExpr expr = (BinopExpr)this.mProductions.removeLast();
      Value left = (Value)this.mProductions.removeLast();
      expr.setOp1(left);
      expr.setOp2(right);
      this.mProductions.addLast(expr);
   }

   public void outABinopBoolExpr(ABinopBoolExpr node) {
   }

   public void outAUnopExpression(AUnopExpression node) {
   }

   public void outAAndBinop(AAndBinop node) {
      this.mProductions.addLast(Jimple.v().newAndExpr(this.mValue, this.mValue));
   }

   public void outAOrBinop(AOrBinop node) {
      this.mProductions.addLast(Jimple.v().newOrExpr(this.mValue, this.mValue));
   }

   public void outAXorBinop(AXorBinop node) {
      this.mProductions.addLast(Jimple.v().newXorExpr(this.mValue, this.mValue));
   }

   public void outAModBinop(AModBinop node) {
      this.mProductions.addLast(Jimple.v().newRemExpr(this.mValue, this.mValue));
   }

   public void outACmpBinop(ACmpBinop node) {
      this.mProductions.addLast(Jimple.v().newCmpExpr(this.mValue, this.mValue));
   }

   public void outACmpgBinop(ACmpgBinop node) {
      this.mProductions.addLast(Jimple.v().newCmpgExpr(this.mValue, this.mValue));
   }

   public void outACmplBinop(ACmplBinop node) {
      this.mProductions.addLast(Jimple.v().newCmplExpr(this.mValue, this.mValue));
   }

   public void outACmpeqBinop(ACmpeqBinop node) {
      this.mProductions.addLast(Jimple.v().newEqExpr(this.mValue, this.mValue));
   }

   public void outACmpneBinop(ACmpneBinop node) {
      this.mProductions.addLast(Jimple.v().newNeExpr(this.mValue, this.mValue));
   }

   public void outACmpgtBinop(ACmpgtBinop node) {
      this.mProductions.addLast(Jimple.v().newGtExpr(this.mValue, this.mValue));
   }

   public void outACmpgeBinop(ACmpgeBinop node) {
      this.mProductions.addLast(Jimple.v().newGeExpr(this.mValue, this.mValue));
   }

   public void outACmpltBinop(ACmpltBinop node) {
      this.mProductions.addLast(Jimple.v().newLtExpr(this.mValue, this.mValue));
   }

   public void outACmpleBinop(ACmpleBinop node) {
      this.mProductions.addLast(Jimple.v().newLeExpr(this.mValue, this.mValue));
   }

   public void outAShlBinop(AShlBinop node) {
      this.mProductions.addLast(Jimple.v().newShlExpr(this.mValue, this.mValue));
   }

   public void outAShrBinop(AShrBinop node) {
      this.mProductions.addLast(Jimple.v().newShrExpr(this.mValue, this.mValue));
   }

   public void outAUshrBinop(AUshrBinop node) {
      this.mProductions.addLast(Jimple.v().newUshrExpr(this.mValue, this.mValue));
   }

   public void outAPlusBinop(APlusBinop node) {
      this.mProductions.addLast(Jimple.v().newAddExpr(this.mValue, this.mValue));
   }

   public void outAMinusBinop(AMinusBinop node) {
      this.mProductions.addLast(Jimple.v().newSubExpr(this.mValue, this.mValue));
   }

   public void outAMultBinop(AMultBinop node) {
      this.mProductions.addLast(Jimple.v().newMulExpr(this.mValue, this.mValue));
   }

   public void outADivBinop(ADivBinop node) {
      this.mProductions.addLast(Jimple.v().newDivExpr(this.mValue, this.mValue));
   }

   public void outAThrowsClause(AThrowsClause node) {
      List l = (List)this.mProductions.removeLast();
      Iterator it = l.iterator();
      ArrayList exceptionClasses = new ArrayList(l.size());

      while(it.hasNext()) {
         String className = (String)it.next();
         exceptionClasses.add(this.mResolver.makeClassRef(className));
      }

      this.mProductions.addLast(exceptionClasses);
   }

   public void outALocalVariable(ALocalVariable node) {
      String local = (String)this.mProductions.removeLast();
      Local l = (Local)this.mLocals.get(local);
      if (l == null) {
         throw new RuntimeException("did not find local: " + local);
      } else {
         this.mProductions.addLast(l);
      }
   }

   public void outAArrayReference(AArrayReference node) {
      Value immediate = (Value)this.mProductions.removeLast();
      String identifier = (String)this.mProductions.removeLast();
      Local l = (Local)this.mLocals.get(identifier);
      if (l == null) {
         throw new RuntimeException("did not find local: " + identifier);
      } else {
         this.mProductions.addLast(Jimple.v().newArrayRef(l, immediate));
      }
   }

   public void outALocalFieldRef(ALocalFieldRef node) {
      SootFieldRef field = (SootFieldRef)this.mProductions.removeLast();
      String local = (String)this.mProductions.removeLast();
      Local l = (Local)this.mLocals.get(local);
      if (l == null) {
         throw new RuntimeException("did not find local: " + local);
      } else {
         this.mProductions.addLast(Jimple.v().newInstanceFieldRef(l, field));
      }
   }

   public void outASigFieldRef(ASigFieldRef node) {
      SootFieldRef field = (SootFieldRef)this.mProductions.removeLast();
      field = Scene.v().makeFieldRef(field.declaringClass(), field.name(), field.type(), true);
      this.mProductions.addLast(Jimple.v().newStaticFieldRef(field));
   }

   public void outAFieldSignature(AFieldSignature node) {
      String fieldName = (String)this.mProductions.removeLast();
      Type t = (Type)this.mProductions.removeLast();
      String className = (String)this.mProductions.removeLast();
      SootClass cl = this.mResolver.makeClassRef(className);
      SootFieldRef field = Scene.v().makeFieldRef(cl, fieldName, t, false);
      this.mProductions.addLast(field);
   }

   public void outACastExpression(ACastExpression node) {
      Value val = (Value)this.mProductions.removeLast();
      Type type = (Type)this.mProductions.removeLast();
      this.mProductions.addLast(Jimple.v().newCastExpr(val, type));
   }

   public void outAInstanceofExpression(AInstanceofExpression node) {
      Type nonvoidType = (Type)this.mProductions.removeLast();
      Value immediate = (Value)this.mProductions.removeLast();
      this.mProductions.addLast(Jimple.v().newInstanceOfExpr(immediate, nonvoidType));
   }

   public void outAUnopExpr(AUnopExpr node) {
      Value v = (Value)this.mProductions.removeLast();
      UnopExpr expr = (UnopExpr)this.mProductions.removeLast();
      expr.setOp(v);
      this.mProductions.addLast(expr);
   }

   public void outALengthofUnop(ALengthofUnop node) {
      this.mProductions.addLast(Jimple.v().newLengthExpr(this.mValue));
   }

   public void outANegUnop(ANegUnop node) {
      this.mProductions.addLast(Jimple.v().newNegExpr(this.mValue));
   }

   public void outANonstaticInvokeExpr(ANonstaticInvokeExpr node) {
      Object args;
      if (node.getArgList() != null) {
         args = (List)this.mProductions.removeLast();
      } else {
         args = new ArrayList();
      }

      SootMethodRef method = (SootMethodRef)this.mProductions.removeLast();
      String local = (String)this.mProductions.removeLast();
      Local l = (Local)this.mLocals.get(local);
      if (l == null) {
         throw new RuntimeException("did not find local: " + local);
      } else {
         Node invokeType = node.getNonstaticInvoke();
         Object invokeExpr;
         if (invokeType instanceof ASpecialNonstaticInvoke) {
            invokeExpr = Jimple.v().newSpecialInvokeExpr(l, method, (List)args);
         } else if (invokeType instanceof AVirtualNonstaticInvoke) {
            invokeExpr = Jimple.v().newVirtualInvokeExpr(l, method, (List)args);
         } else {
            if (this.debug && !(invokeType instanceof AInterfaceNonstaticInvoke)) {
               throw new RuntimeException("expected interface invoke.");
            }

            invokeExpr = Jimple.v().newInterfaceInvokeExpr(l, method, (List)args);
         }

         this.mProductions.addLast(invokeExpr);
      }
   }

   public void outAStaticInvokeExpr(AStaticInvokeExpr node) {
      Object args;
      if (node.getArgList() != null) {
         args = (List)this.mProductions.removeLast();
      } else {
         args = new ArrayList();
      }

      SootMethodRef method = (SootMethodRef)this.mProductions.removeLast();
      method = Scene.v().makeMethodRef(method.declaringClass(), method.name(), method.parameterTypes(), method.returnType(), true);
      this.mProductions.addLast(Jimple.v().newStaticInvokeExpr(method, (List)args));
   }

   public void outADynamicInvokeExpr(ADynamicInvokeExpr node) {
      List bsmArgs;
      if (node.getStaticargs() != null) {
         bsmArgs = (List)this.mProductions.removeLast();
      } else {
         bsmArgs = Collections.emptyList();
      }

      SootMethodRef bsmMethodRef = (SootMethodRef)this.mProductions.removeLast();
      List dynArgs;
      if (node.getDynargs() != null) {
         dynArgs = (List)this.mProductions.removeLast();
      } else {
         dynArgs = Collections.emptyList();
      }

      SootMethodRef dynMethodRef = (SootMethodRef)this.mProductions.removeLast();
      this.mProductions.addLast(Jimple.v().newDynamicInvokeExpr(bsmMethodRef, bsmArgs, dynMethodRef, dynArgs));
   }

   public void outAUnnamedMethodSignature(AUnnamedMethodSignature node) {
      List parameterList = new ArrayList();
      if (node.getParameterList() != null) {
         parameterList = (List)this.mProductions.removeLast();
      }

      Type type = (Type)this.mProductions.removeLast();
      String name = (String)this.mProductions.removeLast();
      SootClass sootClass = this.mResolver.makeClassRef("soot.dummy.InvokeDynamic");
      SootMethodRef sootMethod = Scene.v().makeMethodRef(sootClass, name, (List)parameterList, type, false);
      this.mProductions.addLast(sootMethod);
   }

   public void outAMethodSignature(AMethodSignature node) {
      List parameterList = new ArrayList();
      if (node.getParameterList() != null) {
         parameterList = (List)this.mProductions.removeLast();
      }

      String methodName = (String)this.mProductions.removeLast();
      Type type = (Type)this.mProductions.removeLast();
      String className = (String)this.mProductions.removeLast();
      SootClass sootClass = this.mResolver.makeClassRef(className);
      SootMethodRef sootMethod = Scene.v().makeMethodRef(sootClass, methodName, (List)parameterList, type, false);
      this.mProductions.addLast(sootMethod);
   }

   public void outASimpleNewExpr(ASimpleNewExpr node) {
      this.mProductions.addLast(Jimple.v().newNewExpr((RefType)this.mProductions.removeLast()));
   }

   public void outAArrayNewExpr(AArrayNewExpr node) {
      Value size = (Value)this.mProductions.removeLast();
      Type type = (Type)this.mProductions.removeLast();
      this.mProductions.addLast(Jimple.v().newNewArrayExpr(type, size));
   }

   public void outAMultiNewExpr(AMultiNewExpr node) {
      LinkedList arrayDesc = node.getArrayDescriptor();
      int descCnt = arrayDesc.size();
      List sizes = new LinkedList();
      Iterator it = arrayDesc.iterator();

      while(it.hasNext()) {
         AArrayDescriptor o = (AArrayDescriptor)it.next();
         if (o.getImmediate() == null) {
            break;
         }

         sizes.add(0, this.mProductions.removeLast());
      }

      Type type = (Type)this.mProductions.removeLast();
      ArrayType arrayType = ArrayType.v(type, descCnt);
      this.mProductions.addLast(Jimple.v().newNewMultiArrayExpr(arrayType, sizes));
   }

   public void defaultCase(Node node) {
      if (node instanceof TQuotedName || node instanceof TFullIdentifier || node instanceof TIdentifier || node instanceof TStringConstant || node instanceof TIntegerConstant || node instanceof TFloatConstant || node instanceof TAtIdentifier) {
         if (this.debug) {
            logger.debug("Default case -pushing token:" + ((Token)node).getText());
         }

         String tokenString = ((Token)node).getText();
         if (!(node instanceof TStringConstant) && !(node instanceof TQuotedName)) {
            if (node instanceof TFullIdentifier) {
               tokenString = Scene.v().unescapeName(tokenString);
            }
         } else {
            tokenString = tokenString.substring(1, tokenString.length() - 1);
         }

         if (node instanceof TIdentifier || node instanceof TFullIdentifier || node instanceof TQuotedName || node instanceof TStringConstant) {
            try {
               tokenString = StringTools.getUnEscapedStringOf(tokenString);
            } catch (RuntimeException var4) {
               logger.debug("Invalid escaped string: " + tokenString);
            }
         }

         this.mProductions.addLast(tokenString);
      }

   }

   protected int processModifiers(List l) {
      int modifier = 0;
      Iterator it = l.iterator();

      while(it.hasNext()) {
         Object t = it.next();
         if (t instanceof AAbstractModifier) {
            modifier |= 1024;
         } else if (t instanceof AFinalModifier) {
            modifier |= 16;
         } else if (t instanceof ANativeModifier) {
            modifier |= 256;
         } else if (t instanceof APublicModifier) {
            modifier |= 1;
         } else if (t instanceof AProtectedModifier) {
            modifier |= 4;
         } else if (t instanceof APrivateModifier) {
            modifier |= 2;
         } else if (t instanceof AStaticModifier) {
            modifier |= 8;
         } else if (t instanceof ASynchronizedModifier) {
            modifier |= 32;
         } else if (t instanceof ATransientModifier) {
            modifier |= 128;
         } else if (t instanceof AVolatileModifier) {
            modifier |= 64;
         } else if (t instanceof AStrictfpModifier) {
            modifier |= 2048;
         } else if (t instanceof AEnumModifier) {
            modifier |= 16384;
         } else {
            if (!(t instanceof AAnnotationModifier)) {
               throw new RuntimeException("Impossible: modifier unknown - Have you added a new modifier and not updated this file?");
            }

            modifier |= 8192;
         }
      }

      return modifier;
   }

   private void addBoxToPatch(String aLabelName, UnitBox aUnitBox) {
      List<UnitBox> patchList = (List)this.mLabelToPatchList.get(aLabelName);
      if (patchList == null) {
         patchList = new ArrayList();
         this.mLabelToPatchList.put(aLabelName, patchList);
      }

      ((List)patchList).add(aUnitBox);
   }
}
