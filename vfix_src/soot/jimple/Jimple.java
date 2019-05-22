package soot.jimple;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import soot.ArrayType;
import soot.ErroneousType;
import soot.G;
import soot.Immediate;
import soot.Local;
import soot.RefType;
import soot.Singletons;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.internal.ConditionExprBox;
import soot.jimple.internal.IdentityRefBox;
import soot.jimple.internal.ImmediateBox;
import soot.jimple.internal.InvokeExprBox;
import soot.jimple.internal.JAddExpr;
import soot.jimple.internal.JAndExpr;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JBreakpointStmt;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JCaughtExceptionRef;
import soot.jimple.internal.JCmpExpr;
import soot.jimple.internal.JCmpgExpr;
import soot.jimple.internal.JCmplExpr;
import soot.jimple.internal.JDivExpr;
import soot.jimple.internal.JDynamicInvokeExpr;
import soot.jimple.internal.JEnterMonitorStmt;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JExitMonitorStmt;
import soot.jimple.internal.JGeExpr;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JGtExpr;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInstanceOfExpr;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JLeExpr;
import soot.jimple.internal.JLengthExpr;
import soot.jimple.internal.JLookupSwitchStmt;
import soot.jimple.internal.JLtExpr;
import soot.jimple.internal.JMulExpr;
import soot.jimple.internal.JNeExpr;
import soot.jimple.internal.JNegExpr;
import soot.jimple.internal.JNewArrayExpr;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JNewMultiArrayExpr;
import soot.jimple.internal.JNopStmt;
import soot.jimple.internal.JOrExpr;
import soot.jimple.internal.JRemExpr;
import soot.jimple.internal.JRetStmt;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;
import soot.jimple.internal.JShlExpr;
import soot.jimple.internal.JShrExpr;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JSubExpr;
import soot.jimple.internal.JTableSwitchStmt;
import soot.jimple.internal.JThrowStmt;
import soot.jimple.internal.JTrap;
import soot.jimple.internal.JUshrExpr;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.internal.JXorExpr;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.internal.JimpleLocalBox;
import soot.jimple.internal.RValueBox;
import soot.jimple.internal.StmtBox;
import soot.jimple.internal.VariableBox;

public class Jimple {
   public static final String NEWARRAY = "newarray";
   public static final String NEWMULTIARRAY = "newmultiarray";
   public static final String NOP = "nop";
   public static final String RET = "ret";
   public static final String SPECIALINVOKE = "specialinvoke";
   public static final String DYNAMICINVOKE = "dynamicinvoke";
   public static final String STATICINVOKE = "staticinvoke";
   public static final String TABLESWITCH = "tableswitch";
   public static final String VIRTUALINVOKE = "virtualinvoke";
   public static final String NULL_TYPE = "null_type";
   public static final String UNKNOWN = "unknown";
   public static final String CMP = "cmp";
   public static final String CMPG = "cmpg";
   public static final String CMPL = "cmpl";
   public static final String ENTERMONITOR = "entermonitor";
   public static final String EXITMONITOR = "exitmonitor";
   public static final String INTERFACEINVOKE = "interfaceinvoke";
   public static final String LENGTHOF = "lengthof";
   public static final String LOOKUPSWITCH = "lookupswitch";
   public static final String NEG = "neg";
   public static final String IF = "if";
   public static final String ABSTRACT = "abstract";
   public static final String BOOLEAN = "boolean";
   public static final String BREAK = "break";
   public static final String BYTE = "byte";
   public static final String CASE = "case";
   public static final String CATCH = "catch";
   public static final String CHAR = "char";
   public static final String CLASS = "class";
   public static final String FINAL = "final";
   public static final String NATIVE = "native";
   public static final String PUBLIC = "public";
   public static final String PROTECTED = "protected";
   public static final String PRIVATE = "private";
   public static final String STATIC = "static";
   public static final String SYNCHRONIZED = "synchronized";
   public static final String TRANSIENT = "transient";
   public static final String VOLATILE = "volatile";
   public static final String STRICTFP = "strictfp";
   public static final String ENUM = "enum";
   public static final String ANNOTATION = "annotation";
   public static final String INTERFACE = "interface";
   public static final String VOID = "void";
   public static final String SHORT = "short";
   public static final String INT = "int";
   public static final String LONG = "long";
   public static final String FLOAT = "float";
   public static final String DOUBLE = "double";
   public static final String EXTENDS = "extends";
   public static final String IMPLEMENTS = "implements";
   public static final String BREAKPOINT = "breakpoint";
   public static final String DEFAULT = "default";
   public static final String GOTO = "goto";
   public static final String INSTANCEOF = "instanceof";
   public static final String NEW = "new";
   public static final String RETURN = "return";
   public static final String THROW = "throw";
   public static final String THROWS = "throws";
   public static final String NULL = "null";
   public static final String FROM = "from";
   public static final String TO = "to";
   public static final String WITH = "with";
   public static final String CLS = "cls";
   public static final String TRUE = "true";
   public static final String FALSE = "false";

   public Jimple(Singletons.Global g) {
   }

   public static Jimple v() {
      return G.v().soot_jimple_Jimple();
   }

   public static List<String> jimpleKeywordList() {
      List<String> l = new LinkedList();
      Collections.addAll(l, new String[]{"newarray", "newmultiarray", "nop", "ret", "specialinvoke", "staticinvoke", "tableswitch", "virtualinvoke", "null_type", "unknown", "cmp", "cmpg", "cmpl", "entermonitor", "exitmonitor", "interfaceinvoke", "lengthof", "lookupswitch", "neg", "if", "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "final", "native", "public", "protected", "private", "static", "synchronized", "transient", "volatile", "strictfp", "enum", "annotation", "interface", "void", "short", "int", "long", "float", "double", "extends", "implements", "breakpoint", "default", "goto", "instanceof", "new", "return", "throw", "throws", "null", "from", "to", "with", "cls", "true", "false"});
      return l;
   }

   public static boolean isJavaKeywordType(Type t) {
      return !(t instanceof StmtAddressType) && !(t instanceof UnknownType) && !(t instanceof RefType) && (!(t instanceof ArrayType) || isJavaKeywordType(((ArrayType)t).baseType)) && !(t instanceof ErroneousType);
   }

   public static Value cloneIfNecessary(Value val) {
      return val instanceof Immediate ? val : (Value)val.clone();
   }

   public XorExpr newXorExpr(Value op1, Value op2) {
      return new JXorExpr(op1, op2);
   }

   public UshrExpr newUshrExpr(Value op1, Value op2) {
      return new JUshrExpr(op1, op2);
   }

   public SubExpr newSubExpr(Value op1, Value op2) {
      return new JSubExpr(op1, op2);
   }

   public ShrExpr newShrExpr(Value op1, Value op2) {
      return new JShrExpr(op1, op2);
   }

   public ShlExpr newShlExpr(Value op1, Value op2) {
      return new JShlExpr(op1, op2);
   }

   public RemExpr newRemExpr(Value op1, Value op2) {
      return new JRemExpr(op1, op2);
   }

   public OrExpr newOrExpr(Value op1, Value op2) {
      return new JOrExpr(op1, op2);
   }

   public NeExpr newNeExpr(Value op1, Value op2) {
      return new JNeExpr(op1, op2);
   }

   public MulExpr newMulExpr(Value op1, Value op2) {
      return new JMulExpr(op1, op2);
   }

   public LeExpr newLeExpr(Value op1, Value op2) {
      return new JLeExpr(op1, op2);
   }

   public GeExpr newGeExpr(Value op1, Value op2) {
      return new JGeExpr(op1, op2);
   }

   public EqExpr newEqExpr(Value op1, Value op2) {
      return new JEqExpr(op1, op2);
   }

   public DivExpr newDivExpr(Value op1, Value op2) {
      return new JDivExpr(op1, op2);
   }

   public CmplExpr newCmplExpr(Value op1, Value op2) {
      return new JCmplExpr(op1, op2);
   }

   public CmpgExpr newCmpgExpr(Value op1, Value op2) {
      return new JCmpgExpr(op1, op2);
   }

   public CmpExpr newCmpExpr(Value op1, Value op2) {
      return new JCmpExpr(op1, op2);
   }

   public GtExpr newGtExpr(Value op1, Value op2) {
      return new JGtExpr(op1, op2);
   }

   public LtExpr newLtExpr(Value op1, Value op2) {
      return new JLtExpr(op1, op2);
   }

   public AddExpr newAddExpr(Value op1, Value op2) {
      return new JAddExpr(op1, op2);
   }

   public AndExpr newAndExpr(Value op1, Value op2) {
      return new JAndExpr(op1, op2);
   }

   public NegExpr newNegExpr(Value op) {
      return new JNegExpr(op);
   }

   public LengthExpr newLengthExpr(Value op) {
      return new JLengthExpr(op);
   }

   public CastExpr newCastExpr(Value op1, Type t) {
      return new JCastExpr(op1, t);
   }

   public InstanceOfExpr newInstanceOfExpr(Value op1, Type t) {
      return new JInstanceOfExpr(op1, t);
   }

   public NewExpr newNewExpr(RefType type) {
      return new JNewExpr(type);
   }

   public NewArrayExpr newNewArrayExpr(Type type, Value size) {
      return new JNewArrayExpr(type, size);
   }

   public NewMultiArrayExpr newNewMultiArrayExpr(ArrayType type, List<? extends Value> sizes) {
      return new JNewMultiArrayExpr(type, sizes);
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method, List<? extends Value> args) {
      return new JStaticInvokeExpr(method, args);
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method, Value... args) {
      return this.newStaticInvokeExpr(method, Arrays.asList(args));
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method, Value arg) {
      return this.newStaticInvokeExpr(method, Collections.singletonList(arg));
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method) {
      return this.newStaticInvokeExpr(method, Collections.emptyList());
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method, List<? extends Value> args) {
      return new JSpecialInvokeExpr(base, method, args);
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method, Value... args) {
      return this.newSpecialInvokeExpr(base, method, Arrays.asList(args));
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method, Value arg) {
      return this.newSpecialInvokeExpr(base, method, Collections.singletonList(arg));
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method) {
      return this.newSpecialInvokeExpr(base, method, Collections.emptyList());
   }

   public DynamicInvokeExpr newDynamicInvokeExpr(SootMethodRef bootstrapMethodRef, List<? extends Value> bootstrapArgs, SootMethodRef methodRef, List<? extends Value> args) {
      return new JDynamicInvokeExpr(bootstrapMethodRef, bootstrapArgs, methodRef, args);
   }

   public DynamicInvokeExpr newDynamicInvokeExpr(SootMethodRef bootstrapMethodRef, List<? extends Value> bootstrapArgs, SootMethodRef methodRef, int tag, List<? extends Value> args) {
      return new JDynamicInvokeExpr(bootstrapMethodRef, bootstrapArgs, methodRef, tag, args);
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method, List<? extends Value> args) {
      return new JVirtualInvokeExpr(base, method, args);
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method, Value... args) {
      return this.newVirtualInvokeExpr(base, method, Arrays.asList(args));
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method, Value arg) {
      return this.newVirtualInvokeExpr(base, method, Collections.singletonList(arg));
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method) {
      return this.newVirtualInvokeExpr(base, method, Collections.emptyList());
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method, List<? extends Value> args) {
      return new JInterfaceInvokeExpr(base, method, args);
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method, Value... args) {
      return this.newInterfaceInvokeExpr(base, method, Arrays.asList(args));
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method, Value arg) {
      return this.newInterfaceInvokeExpr(base, method, Collections.singletonList(arg));
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method) {
      return this.newInterfaceInvokeExpr(base, method, Collections.emptyList());
   }

   public ThrowStmt newThrowStmt(Value op) {
      return new JThrowStmt(op);
   }

   public ExitMonitorStmt newExitMonitorStmt(Value op) {
      return new JExitMonitorStmt(op);
   }

   public EnterMonitorStmt newEnterMonitorStmt(Value op) {
      return new JEnterMonitorStmt(op);
   }

   public BreakpointStmt newBreakpointStmt() {
      return new JBreakpointStmt();
   }

   public GotoStmt newGotoStmt(Unit target) {
      return new JGotoStmt(target);
   }

   public GotoStmt newGotoStmt(UnitBox stmtBox) {
      return new JGotoStmt(stmtBox);
   }

   public NopStmt newNopStmt() {
      return new JNopStmt();
   }

   public ReturnVoidStmt newReturnVoidStmt() {
      return new JReturnVoidStmt();
   }

   public ReturnStmt newReturnStmt(Value op) {
      return new JReturnStmt(op);
   }

   public RetStmt newRetStmt(Value stmtAddress) {
      return new JRetStmt(stmtAddress);
   }

   public IfStmt newIfStmt(Value condition, Unit target) {
      return new JIfStmt(condition, target);
   }

   public IfStmt newIfStmt(Value condition, UnitBox target) {
      return new JIfStmt(condition, target);
   }

   public IdentityStmt newIdentityStmt(Value local, Value identityRef) {
      return new JIdentityStmt(local, identityRef);
   }

   public AssignStmt newAssignStmt(Value variable, Value rvalue) {
      return new JAssignStmt(variable, rvalue);
   }

   public InvokeStmt newInvokeStmt(Value op) {
      return new JInvokeStmt(op);
   }

   public TableSwitchStmt newTableSwitchStmt(Value key, int lowIndex, int highIndex, List<? extends Unit> targets, Unit defaultTarget) {
      return new JTableSwitchStmt(key, lowIndex, highIndex, targets, defaultTarget);
   }

   public TableSwitchStmt newTableSwitchStmt(Value key, int lowIndex, int highIndex, List<? extends UnitBox> targets, UnitBox defaultTarget) {
      return new JTableSwitchStmt(key, lowIndex, highIndex, targets, defaultTarget);
   }

   public LookupSwitchStmt newLookupSwitchStmt(Value key, List<IntConstant> lookupValues, List<? extends Unit> targets, Unit defaultTarget) {
      return new JLookupSwitchStmt(key, lookupValues, targets, defaultTarget);
   }

   public LookupSwitchStmt newLookupSwitchStmt(Value key, List<IntConstant> lookupValues, List<? extends UnitBox> targets, UnitBox defaultTarget) {
      return new JLookupSwitchStmt(key, lookupValues, targets, defaultTarget);
   }

   public Local newLocal(String name, Type t) {
      return new JimpleLocal(name, t);
   }

   public Trap newTrap(SootClass exception, Unit beginStmt, Unit endStmt, Unit handlerStmt) {
      return new JTrap(exception, beginStmt, endStmt, handlerStmt);
   }

   public Trap newTrap(SootClass exception, UnitBox beginStmt, UnitBox endStmt, UnitBox handlerStmt) {
      return new JTrap(exception, beginStmt, endStmt, handlerStmt);
   }

   public StaticFieldRef newStaticFieldRef(SootFieldRef f) {
      return new StaticFieldRef(f);
   }

   public ThisRef newThisRef(RefType t) {
      return new ThisRef(t);
   }

   public ParameterRef newParameterRef(Type paramType, int number) {
      return new ParameterRef(paramType, number);
   }

   public InstanceFieldRef newInstanceFieldRef(Value base, SootFieldRef f) {
      return new JInstanceFieldRef(base, f);
   }

   public CaughtExceptionRef newCaughtExceptionRef() {
      return new JCaughtExceptionRef();
   }

   public ArrayRef newArrayRef(Value base, Value index) {
      return new JArrayRef(base, index);
   }

   public ValueBox newVariableBox(Value value) {
      return new VariableBox(value);
   }

   public ValueBox newLocalBox(Value value) {
      return new JimpleLocalBox(value);
   }

   public ValueBox newRValueBox(Value value) {
      return new RValueBox(value);
   }

   public ValueBox newImmediateBox(Value value) {
      return new ImmediateBox(value);
   }

   public ValueBox newArgBox(Value value) {
      return new ImmediateBox(value);
   }

   public ValueBox newIdentityRefBox(Value value) {
      return new IdentityRefBox(value);
   }

   public ValueBox newConditionExprBox(Value value) {
      return new ConditionExprBox(value);
   }

   public ValueBox newInvokeExprBox(Value value) {
      return new InvokeExprBox(value);
   }

   public UnitBox newStmtBox(Unit unit) {
      return new StmtBox((Stmt)unit);
   }

   public JimpleBody newBody(SootMethod m) {
      return new JimpleBody(m);
   }

   public JimpleBody newBody() {
      return new JimpleBody();
   }
}
