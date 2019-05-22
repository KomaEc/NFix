package soot.grimp;

import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Singletons;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.grimp.internal.ExprBox;
import soot.grimp.internal.GAddExpr;
import soot.grimp.internal.GAndExpr;
import soot.grimp.internal.GArrayRef;
import soot.grimp.internal.GAssignStmt;
import soot.grimp.internal.GCastExpr;
import soot.grimp.internal.GCmpExpr;
import soot.grimp.internal.GCmpgExpr;
import soot.grimp.internal.GCmplExpr;
import soot.grimp.internal.GDivExpr;
import soot.grimp.internal.GDynamicInvokeExpr;
import soot.grimp.internal.GEnterMonitorStmt;
import soot.grimp.internal.GEqExpr;
import soot.grimp.internal.GExitMonitorStmt;
import soot.grimp.internal.GGeExpr;
import soot.grimp.internal.GGtExpr;
import soot.grimp.internal.GIdentityStmt;
import soot.grimp.internal.GIfStmt;
import soot.grimp.internal.GInstanceFieldRef;
import soot.grimp.internal.GInstanceOfExpr;
import soot.grimp.internal.GInterfaceInvokeExpr;
import soot.grimp.internal.GInvokeStmt;
import soot.grimp.internal.GLeExpr;
import soot.grimp.internal.GLengthExpr;
import soot.grimp.internal.GLookupSwitchStmt;
import soot.grimp.internal.GLtExpr;
import soot.grimp.internal.GMulExpr;
import soot.grimp.internal.GNeExpr;
import soot.grimp.internal.GNegExpr;
import soot.grimp.internal.GNewArrayExpr;
import soot.grimp.internal.GNewInvokeExpr;
import soot.grimp.internal.GNewMultiArrayExpr;
import soot.grimp.internal.GOrExpr;
import soot.grimp.internal.GRValueBox;
import soot.grimp.internal.GRemExpr;
import soot.grimp.internal.GReturnStmt;
import soot.grimp.internal.GShlExpr;
import soot.grimp.internal.GShrExpr;
import soot.grimp.internal.GSpecialInvokeExpr;
import soot.grimp.internal.GStaticInvokeExpr;
import soot.grimp.internal.GSubExpr;
import soot.grimp.internal.GTableSwitchStmt;
import soot.grimp.internal.GThrowStmt;
import soot.grimp.internal.GTrap;
import soot.grimp.internal.GUshrExpr;
import soot.grimp.internal.GVirtualInvokeExpr;
import soot.grimp.internal.GXorExpr;
import soot.grimp.internal.ObjExprBox;
import soot.jimple.AbstractExprSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.Expr;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

public class Grimp {
   public Grimp(Singletons.Global g) {
   }

   public static Grimp v() {
      return G.v().soot_grimp_Grimp();
   }

   public XorExpr newXorExpr(Value op1, Value op2) {
      return new GXorExpr(op1, op2);
   }

   public UshrExpr newUshrExpr(Value op1, Value op2) {
      return new GUshrExpr(op1, op2);
   }

   public SubExpr newSubExpr(Value op1, Value op2) {
      return new GSubExpr(op1, op2);
   }

   public ShrExpr newShrExpr(Value op1, Value op2) {
      return new GShrExpr(op1, op2);
   }

   public ShlExpr newShlExpr(Value op1, Value op2) {
      return new GShlExpr(op1, op2);
   }

   public RemExpr newRemExpr(Value op1, Value op2) {
      return new GRemExpr(op1, op2);
   }

   public OrExpr newOrExpr(Value op1, Value op2) {
      return new GOrExpr(op1, op2);
   }

   public NeExpr newNeExpr(Value op1, Value op2) {
      return new GNeExpr(op1, op2);
   }

   public MulExpr newMulExpr(Value op1, Value op2) {
      return new GMulExpr(op1, op2);
   }

   public LeExpr newLeExpr(Value op1, Value op2) {
      return new GLeExpr(op1, op2);
   }

   public GeExpr newGeExpr(Value op1, Value op2) {
      return new GGeExpr(op1, op2);
   }

   public EqExpr newEqExpr(Value op1, Value op2) {
      return new GEqExpr(op1, op2);
   }

   public DivExpr newDivExpr(Value op1, Value op2) {
      return new GDivExpr(op1, op2);
   }

   public CmplExpr newCmplExpr(Value op1, Value op2) {
      return new GCmplExpr(op1, op2);
   }

   public CmpgExpr newCmpgExpr(Value op1, Value op2) {
      return new GCmpgExpr(op1, op2);
   }

   public CmpExpr newCmpExpr(Value op1, Value op2) {
      return new GCmpExpr(op1, op2);
   }

   public GtExpr newGtExpr(Value op1, Value op2) {
      return new GGtExpr(op1, op2);
   }

   public LtExpr newLtExpr(Value op1, Value op2) {
      return new GLtExpr(op1, op2);
   }

   public AddExpr newAddExpr(Value op1, Value op2) {
      return new GAddExpr(op1, op2);
   }

   public AndExpr newAndExpr(Value op1, Value op2) {
      return new GAndExpr(op1, op2);
   }

   public NegExpr newNegExpr(Value op) {
      return new GNegExpr(op);
   }

   public LengthExpr newLengthExpr(Value op) {
      return new GLengthExpr(op);
   }

   public CastExpr newCastExpr(Value op1, Type t) {
      return new GCastExpr(op1, t);
   }

   public InstanceOfExpr newInstanceOfExpr(Value op1, Type t) {
      return new GInstanceOfExpr(op1, t);
   }

   NewExpr newNewExpr(RefType type) {
      return Jimple.v().newNewExpr(type);
   }

   public NewArrayExpr newNewArrayExpr(Type type, Value size) {
      return new GNewArrayExpr(type, size);
   }

   public NewMultiArrayExpr newNewMultiArrayExpr(ArrayType type, List sizes) {
      return new GNewMultiArrayExpr(type, sizes);
   }

   public NewInvokeExpr newNewInvokeExpr(RefType base, SootMethodRef method, List args) {
      return new GNewInvokeExpr(base, method, args);
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method, List args) {
      return new GStaticInvokeExpr(method, args);
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method, List args) {
      return new GSpecialInvokeExpr(base, method, args);
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method, List args) {
      return new GVirtualInvokeExpr(base, method, args);
   }

   public DynamicInvokeExpr newDynamicInvokeExpr(SootMethodRef bootstrapMethodRef, List<Value> bootstrapArgs, SootMethodRef methodRef, int tag, List args) {
      return new GDynamicInvokeExpr(bootstrapMethodRef, bootstrapArgs, methodRef, tag, args);
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method, List args) {
      return new GInterfaceInvokeExpr(base, method, args);
   }

   public ThrowStmt newThrowStmt(Value op) {
      return new GThrowStmt(op);
   }

   public ThrowStmt newThrowStmt(ThrowStmt s) {
      return new GThrowStmt(s.getOp());
   }

   public ExitMonitorStmt newExitMonitorStmt(Value op) {
      return new GExitMonitorStmt(op);
   }

   public ExitMonitorStmt newExitMonitorStmt(ExitMonitorStmt s) {
      return new GExitMonitorStmt(s.getOp());
   }

   public EnterMonitorStmt newEnterMonitorStmt(Value op) {
      return new GEnterMonitorStmt(op);
   }

   public EnterMonitorStmt newEnterMonitorStmt(EnterMonitorStmt s) {
      return new GEnterMonitorStmt(s.getOp());
   }

   public BreakpointStmt newBreakpointStmt() {
      return Jimple.v().newBreakpointStmt();
   }

   public BreakpointStmt newBreakpointStmt(BreakpointStmt s) {
      return Jimple.v().newBreakpointStmt();
   }

   public GotoStmt newGotoStmt(Unit target) {
      return Jimple.v().newGotoStmt(target);
   }

   public GotoStmt newGotoStmt(GotoStmt s) {
      return Jimple.v().newGotoStmt(s.getTarget());
   }

   public NopStmt newNopStmt() {
      return Jimple.v().newNopStmt();
   }

   public NopStmt newNopStmt(NopStmt s) {
      return Jimple.v().newNopStmt();
   }

   public ReturnVoidStmt newReturnVoidStmt() {
      return Jimple.v().newReturnVoidStmt();
   }

   public ReturnVoidStmt newReturnVoidStmt(ReturnVoidStmt s) {
      return Jimple.v().newReturnVoidStmt();
   }

   public ReturnStmt newReturnStmt(Value op) {
      return new GReturnStmt(op);
   }

   public ReturnStmt newReturnStmt(ReturnStmt s) {
      return new GReturnStmt(s.getOp());
   }

   public IfStmt newIfStmt(Value condition, Unit target) {
      return new GIfStmt(condition, target);
   }

   public IfStmt newIfStmt(IfStmt s) {
      return new GIfStmt(s.getCondition(), s.getTarget());
   }

   public IdentityStmt newIdentityStmt(Value local, Value identityRef) {
      return new GIdentityStmt(local, identityRef);
   }

   public IdentityStmt newIdentityStmt(IdentityStmt s) {
      return new GIdentityStmt(s.getLeftOp(), s.getRightOp());
   }

   public AssignStmt newAssignStmt(Value variable, Value rvalue) {
      return new GAssignStmt(variable, rvalue);
   }

   public AssignStmt newAssignStmt(AssignStmt s) {
      return new GAssignStmt(s.getLeftOp(), s.getRightOp());
   }

   public InvokeStmt newInvokeStmt(Value op) {
      return new GInvokeStmt(op);
   }

   public InvokeStmt newInvokeStmt(InvokeStmt s) {
      return new GInvokeStmt(s.getInvokeExpr());
   }

   public TableSwitchStmt newTableSwitchStmt(Value key, int lowIndex, int highIndex, List targets, Unit defaultTarget) {
      return new GTableSwitchStmt(key, lowIndex, highIndex, targets, defaultTarget);
   }

   public TableSwitchStmt newTableSwitchStmt(TableSwitchStmt s) {
      return new GTableSwitchStmt(s.getKey(), s.getLowIndex(), s.getHighIndex(), s.getTargets(), s.getDefaultTarget());
   }

   public LookupSwitchStmt newLookupSwitchStmt(Value key, List lookupValues, List targets, Unit defaultTarget) {
      return new GLookupSwitchStmt(key, lookupValues, targets, defaultTarget);
   }

   public LookupSwitchStmt newLookupSwitchStmt(LookupSwitchStmt s) {
      return new GLookupSwitchStmt(s.getKey(), s.getLookupValues(), s.getTargets(), s.getDefaultTarget());
   }

   public Local newLocal(String name, Type t) {
      return Jimple.v().newLocal(name, t);
   }

   public Trap newTrap(SootClass exception, Unit beginStmt, Unit endStmt, Unit handlerStmt) {
      return new GTrap(exception, beginStmt, endStmt, handlerStmt);
   }

   public Trap newTrap(Trap trap) {
      return new GTrap(trap.getException(), trap.getBeginUnit(), trap.getEndUnit(), trap.getHandlerUnit());
   }

   public StaticFieldRef newStaticFieldRef(SootFieldRef f) {
      return Jimple.v().newStaticFieldRef(f);
   }

   public ThisRef newThisRef(RefType t) {
      return Jimple.v().newThisRef(t);
   }

   public ParameterRef newParameterRef(Type paramType, int number) {
      return Jimple.v().newParameterRef(paramType, number);
   }

   public InstanceFieldRef newInstanceFieldRef(Value base, SootFieldRef f) {
      return new GInstanceFieldRef(base, f);
   }

   public CaughtExceptionRef newCaughtExceptionRef() {
      return Jimple.v().newCaughtExceptionRef();
   }

   public ArrayRef newArrayRef(Value base, Value index) {
      return new GArrayRef(base, index);
   }

   public ValueBox newVariableBox(Value value) {
      return Jimple.v().newVariableBox(value);
   }

   public ValueBox newLocalBox(Value value) {
      return Jimple.v().newLocalBox(value);
   }

   public ValueBox newRValueBox(Value value) {
      return new GRValueBox(value);
   }

   public ValueBox newImmediateBox(Value value) {
      return Jimple.v().newImmediateBox(value);
   }

   public ValueBox newExprBox(Value value) {
      return new ExprBox(value);
   }

   public ValueBox newArgBox(Value value) {
      return new ExprBox(value);
   }

   public ValueBox newObjExprBox(Value value) {
      return new ObjExprBox(value);
   }

   public ValueBox newIdentityRefBox(Value value) {
      return Jimple.v().newIdentityRefBox(value);
   }

   public ValueBox newConditionExprBox(Value value) {
      return Jimple.v().newConditionExprBox(value);
   }

   public ValueBox newInvokeExprBox(Value value) {
      return Jimple.v().newInvokeExprBox(value);
   }

   public UnitBox newStmtBox(Unit unit) {
      return Jimple.v().newStmtBox(unit);
   }

   public Value newExpr(Value value) {
      if (value instanceof Expr) {
         final ExprBox returnedExpr = new ExprBox(IntConstant.v(0));
         ((Expr)value).apply(new AbstractExprSwitch() {
            public void caseAddExpr(AddExpr v) {
               returnedExpr.setValue(Grimp.this.newAddExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseAndExpr(AndExpr v) {
               returnedExpr.setValue(Grimp.this.newAndExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseCmpExpr(CmpExpr v) {
               returnedExpr.setValue(Grimp.this.newCmpExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseCmpgExpr(CmpgExpr v) {
               returnedExpr.setValue(Grimp.this.newCmpgExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseCmplExpr(CmplExpr v) {
               returnedExpr.setValue(Grimp.this.newCmplExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseDivExpr(DivExpr v) {
               returnedExpr.setValue(Grimp.this.newDivExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseEqExpr(EqExpr v) {
               returnedExpr.setValue(Grimp.this.newEqExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseNeExpr(NeExpr v) {
               returnedExpr.setValue(Grimp.this.newNeExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseGeExpr(GeExpr v) {
               returnedExpr.setValue(Grimp.this.newGeExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseGtExpr(GtExpr v) {
               returnedExpr.setValue(Grimp.this.newGtExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseLeExpr(LeExpr v) {
               returnedExpr.setValue(Grimp.this.newLeExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseLtExpr(LtExpr v) {
               returnedExpr.setValue(Grimp.this.newLtExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseMulExpr(MulExpr v) {
               returnedExpr.setValue(Grimp.this.newMulExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseOrExpr(OrExpr v) {
               returnedExpr.setValue(Grimp.this.newOrExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseRemExpr(RemExpr v) {
               returnedExpr.setValue(Grimp.this.newRemExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseShlExpr(ShlExpr v) {
               returnedExpr.setValue(Grimp.this.newShlExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseShrExpr(ShrExpr v) {
               returnedExpr.setValue(Grimp.this.newShrExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseUshrExpr(UshrExpr v) {
               returnedExpr.setValue(Grimp.this.newUshrExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseSubExpr(SubExpr v) {
               returnedExpr.setValue(Grimp.this.newSubExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseXorExpr(XorExpr v) {
               returnedExpr.setValue(Grimp.this.newXorExpr(Grimp.this.newExpr(v.getOp1()), Grimp.this.newExpr(v.getOp2())));
            }

            public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
               ArrayList newArgList = new ArrayList();

               for(int i = 0; i < v.getArgCount(); ++i) {
                  newArgList.add(Grimp.this.newExpr(v.getArg(i)));
               }

               returnedExpr.setValue(Grimp.this.newInterfaceInvokeExpr((Local)((Local)v.getBase()), v.getMethodRef(), newArgList));
            }

            public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
               ArrayList newArgList = new ArrayList();

               for(int i = 0; i < v.getArgCount(); ++i) {
                  newArgList.add(Grimp.this.newExpr(v.getArg(i)));
               }

               returnedExpr.setValue(Grimp.this.newSpecialInvokeExpr((Local)((Local)v.getBase()), v.getMethodRef(), newArgList));
            }

            public void caseStaticInvokeExpr(StaticInvokeExpr v) {
               ArrayList newArgList = new ArrayList();

               for(int i = 0; i < v.getArgCount(); ++i) {
                  newArgList.add(Grimp.this.newExpr(v.getArg(i)));
               }

               returnedExpr.setValue(Grimp.this.newStaticInvokeExpr(v.getMethodRef(), newArgList));
            }

            public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
               ArrayList newArgList = new ArrayList();

               for(int i = 0; i < v.getArgCount(); ++i) {
                  newArgList.add(Grimp.this.newExpr(v.getArg(i)));
               }

               returnedExpr.setValue(Grimp.this.newVirtualInvokeExpr((Local)((Local)v.getBase()), v.getMethodRef(), newArgList));
            }

            public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
               ArrayList newArgList = new ArrayList();

               for(int i = 0; i < v.getArgCount(); ++i) {
                  newArgList.add(Grimp.this.newExpr(v.getArg(i)));
               }

               returnedExpr.setValue(Grimp.this.newDynamicInvokeExpr(v.getBootstrapMethodRef(), v.getBootstrapArgs(), v.getMethodRef(), v.getHandleTag(), newArgList));
            }

            public void caseCastExpr(CastExpr v) {
               returnedExpr.setValue(Grimp.this.newCastExpr(Grimp.this.newExpr(v.getOp()), v.getType()));
            }

            public void caseInstanceOfExpr(InstanceOfExpr v) {
               returnedExpr.setValue(Grimp.this.newInstanceOfExpr(Grimp.this.newExpr(v.getOp()), v.getCheckType()));
            }

            public void caseNewArrayExpr(NewArrayExpr v) {
               returnedExpr.setValue(Grimp.this.newNewArrayExpr(v.getBaseType(), v.getSize()));
            }

            public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
               returnedExpr.setValue(Grimp.this.newNewMultiArrayExpr(v.getBaseType(), v.getSizes()));
            }

            public void caseNewExpr(NewExpr v) {
               returnedExpr.setValue(Grimp.this.newNewExpr(v.getBaseType()));
            }

            public void caseLengthExpr(LengthExpr v) {
               returnedExpr.setValue(Grimp.this.newLengthExpr(Grimp.this.newExpr(v.getOp())));
            }

            public void caseNegExpr(NegExpr v) {
               returnedExpr.setValue(Grimp.this.newNegExpr(Grimp.this.newExpr(v.getOp())));
            }

            public void defaultCase(Object v) {
               returnedExpr.setValue((Expr)v);
            }
         });
         return returnedExpr.getValue();
      } else if (value instanceof ArrayRef) {
         return this.newArrayRef(((ArrayRef)value).getBase(), this.newExpr(((ArrayRef)value).getIndex()));
      } else {
         return (Value)(value instanceof InstanceFieldRef ? this.newInstanceFieldRef(this.newExpr(((InstanceFieldRef)value).getBase()), ((InstanceFieldRef)value).getFieldRef()) : value);
      }
   }

   public GrimpBody newBody(SootMethod m) {
      return new GrimpBody(m);
   }

   public GrimpBody newBody(Body b, String phase) {
      return new GrimpBody(b);
   }

   public static Value cloneIfNecessary(Value val) {
      return !(val instanceof Local) && !(val instanceof Constant) ? (Value)val.clone() : val;
   }
}
