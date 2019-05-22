package soot.JastAddJ;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import soot.ArrayType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootFieldRef;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.UnopExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLnNamePosTag;
import soot.tagkit.Tag;

public class Body {
   int nextTempIndex = 0;
   JimpleBody body;
   Stack chains;
   TypeDecl typeDecl;
   private Tag lineTag;
   Local thisName;
   ArrayList list = null;
   private HashMap<Value, Tag> tagMap = new HashMap();

   public Body(TypeDecl typeDecl, JimpleBody body, ASTNode container) {
      this.typeDecl = typeDecl;
      this.body = body;
      this.chains = new Stack();
      this.chains.push(body.getUnits());
      this.setLine(container);
      if (!body.getMethod().isStatic()) {
         this.emitThis(typeDecl);
      }

   }

   public Local getParam(int i) {
      return this.body.getParameterLocal(i);
   }

   public Local newTemp(Type type) {
      Local local = Jimple.v().newLocal("temp$" + this.nextTempIndex++, type);
      this.body.getLocals().add(local);
      return local;
   }

   public Local newTemp(Value v) {
      if (v == NullConstant.v()) {
         throw new UnsupportedOperationException("Cannot create a temporary local for null literal");
      } else {
         Local local = this.newTemp(v.getType());
         if (v instanceof ParameterRef) {
            this.add(this.newIdentityStmt(local, (ParameterRef)v, (ASTNode)null));
         } else {
            this.add(this.newAssignStmt(local, v, (ASTNode)null));
         }

         this.copyLocation(v, local);
         return local;
      }
   }

   public Local newLocal(String name, Type type) {
      Local local = Jimple.v().newLocal(name, type);
      this.body.getLocals().add(local);
      if (name.equals("this") && this.thisName == null) {
         this.thisName = local;
      }

      return local;
   }

   public void setLine(ASTNode node) {
      if (node.getStart() != 0 && node.getEnd() != 0) {
         int line = ASTNode.getLine(node.getStart());
         int column = ASTNode.getColumn(node.getStart());
         int endLine = ASTNode.getLine(node.getEnd());
         int endColumn = ASTNode.getColumn(node.getEnd());
         String s = node.sourceFile();
         s = s != null ? s.substring(s.lastIndexOf(File.separatorChar) + 1) : "Unknown";
         this.lineTag = new SourceLnNamePosTag(s, line, endLine, column, endColumn);
      } else {
         this.lineTag = new LineNumberTag(node.lineNumber());
      }

   }

   private Tag currentSourceRangeTag() {
      return this.lineTag;
   }

   public Body add(soot.jimple.Stmt stmt) {
      if (this.list != null) {
         this.list.add(stmt);
         this.list = null;
      }

      stmt.addTag(this.currentSourceRangeTag());
      PatchingChain<Unit> chain = (PatchingChain)this.chains.peek();
      if (stmt instanceof IdentityStmt && chain.size() != 0) {
         IdentityStmt idstmt = (IdentityStmt)stmt;
         if (!(idstmt.getRightOp() instanceof CaughtExceptionRef)) {
            Unit s;
            for(s = chain.getFirst(); s instanceof IdentityStmt; s = chain.getSuccOf((Unit)((soot.jimple.Stmt)s))) {
            }

            if (s != null) {
               chain.insertBefore((Unit)stmt, (Unit)((soot.jimple.Stmt)s));
               return this;
            }
         }
      }

      chain.add((Unit)stmt);
      return this;
   }

   public void pushBlock(PatchingChain c) {
      this.chains.push(c);
   }

   public void popBlock() {
      this.chains.pop();
   }

   public soot.jimple.Stmt newLabel() {
      return Jimple.v().newNopStmt();
   }

   public Body addLabel(soot.jimple.Stmt label) {
      this.add(label);
      return this;
   }

   public Local emitThis(TypeDecl typeDecl) {
      if (this.thisName == null) {
         this.thisName = this.newLocal("this", typeDecl.getSootType());
         if (this.body.getMethod().isStatic()) {
            this.add(Jimple.v().newIdentityStmt(this.thisName, Jimple.v().newParameterRef(typeDecl.getSootType(), 0)));
         } else {
            this.add(Jimple.v().newIdentityStmt(this.thisName, Jimple.v().newThisRef(typeDecl.sootRef())));
         }
      }

      return this.thisName;
   }

   public Body addTrap(TypeDecl type, soot.jimple.Stmt firstStmt, soot.jimple.Stmt lastStmt, soot.jimple.Stmt handler) {
      this.body.getTraps().add(Jimple.v().newTrap(type.getSootClassDecl(), (Unit)firstStmt, (Unit)lastStmt, (Unit)handler));
      return this;
   }

   public soot.jimple.Stmt previousStmt() {
      PatchingChain<Unit> o = (PatchingChain)this.chains.lastElement();
      return (soot.jimple.Stmt)o.getLast();
   }

   public void addNextStmt(ArrayList list) {
      this.list = list;
   }

   public BinopExpr newXorExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newXorExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newUshrExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newUshrExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newSubExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newSubExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newShrExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newShrExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newShlExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newShlExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newRemExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newRemExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newOrExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newOrExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newNeExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newNeExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newMulExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newMulExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newLeExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newLeExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newGeExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newGeExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newEqExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newEqExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newDivExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newDivExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newCmplExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newCmplExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newCmpgExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newCmpgExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newCmpExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newCmpExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newGtExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newGtExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newLtExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newLtExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newAddExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newAddExpr(op1, op2), location, op1, op2);
   }

   public BinopExpr newAndExpr(Value op1, Value op2, ASTNode location) {
      return this.updateTags(Jimple.v().newAndExpr(op1, op2), location, op1, op2);
   }

   public UnopExpr newNegExpr(Value op, ASTNode location) {
      return this.updateTags(Jimple.v().newNegExpr(op), location, op);
   }

   public UnopExpr newLengthExpr(Value op, ASTNode location) {
      return this.updateTags(Jimple.v().newLengthExpr(op), location, op);
   }

   public soot.jimple.CastExpr newCastExpr(Value op1, Type t, ASTNode location) {
      soot.jimple.CastExpr expr = Jimple.v().newCastExpr(op1, t);
      this.createTag(expr, location);
      Tag op1tag = this.getTag(op1);
      if (op1tag != null) {
         expr.getOpBox().addTag(op1tag);
      }

      return expr;
   }

   public soot.jimple.InstanceOfExpr newInstanceOfExpr(Value op1, Type t, ASTNode location) {
      soot.jimple.InstanceOfExpr expr = Jimple.v().newInstanceOfExpr(op1, t);
      this.createTag(expr, location);
      Tag op1tag = this.getTag(op1);
      if (op1tag != null) {
         expr.getOpBox().addTag(op1tag);
      }

      return expr;
   }

   public NewExpr newNewExpr(RefType type, ASTNode location) {
      NewExpr expr = Jimple.v().newNewExpr(type);
      this.createTag(expr, location);
      return expr;
   }

   public NewArrayExpr newNewArrayExpr(Type type, Value size, ASTNode location) {
      NewArrayExpr expr = Jimple.v().newNewArrayExpr(type, size);
      this.createTag(expr, location);
      Tag tag = this.getTag(size);
      if (tag != null) {
         expr.getSizeBox().addTag(tag);
      }

      return expr;
   }

   public NewMultiArrayExpr newNewMultiArrayExpr(ArrayType type, java.util.List sizes, ASTNode location) {
      NewMultiArrayExpr expr = Jimple.v().newNewMultiArrayExpr(type, sizes);
      this.createTag(expr, location);

      for(int i = 0; i < sizes.size(); ++i) {
         Tag tag = this.getTag((Value)sizes.get(i));
         if (tag != null) {
            expr.getSizeBox(i).addTag(tag);
         }
      }

      return expr;
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method, java.util.List args, ASTNode location) {
      StaticInvokeExpr expr = Jimple.v().newStaticInvokeExpr(method, args);
      this.createTag(expr, location);

      for(int i = 0; i < args.size(); ++i) {
         Tag tag = this.getTag((Value)args.get(i));
         if (tag != null) {
            expr.getArgBox(i).addTag(tag);
         }
      }

      return expr;
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method, java.util.List args, ASTNode location) {
      SpecialInvokeExpr expr = Jimple.v().newSpecialInvokeExpr(base, method, args);
      this.createTag(expr, location);

      for(int i = 0; i < args.size(); ++i) {
         Tag tag = this.getTag((Value)args.get(i));
         if (tag != null) {
            expr.getArgBox(i).addTag(tag);
         }
      }

      return expr;
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method, java.util.List args, ASTNode location) {
      VirtualInvokeExpr expr = Jimple.v().newVirtualInvokeExpr(base, method, args);
      this.createTag(expr, location);

      for(int i = 0; i < args.size(); ++i) {
         Tag tag = this.getTag((Value)args.get(i));
         if (tag != null) {
            expr.getArgBox(i).addTag(tag);
         }
      }

      return expr;
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method, java.util.List args, ASTNode location) {
      InterfaceInvokeExpr expr = Jimple.v().newInterfaceInvokeExpr(base, method, args);
      this.createTag(expr, location);

      for(int i = 0; i < args.size(); ++i) {
         Tag tag = this.getTag((Value)args.get(i));
         if (tag != null) {
            expr.getArgBox(i).addTag(tag);
         }
      }

      return expr;
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method, ASTNode location) {
      return this.newStaticInvokeExpr(method, (java.util.List)(new ArrayList()), location);
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method, ASTNode location) {
      return this.newSpecialInvokeExpr(base, method, (java.util.List)(new ArrayList()), location);
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method, ASTNode location) {
      return this.newVirtualInvokeExpr(base, method, (java.util.List)(new ArrayList()), location);
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method, ASTNode location) {
      return this.newInterfaceInvokeExpr(base, method, (java.util.List)(new ArrayList()), location);
   }

   public StaticInvokeExpr newStaticInvokeExpr(SootMethodRef method, Value arg, ASTNode location) {
      return this.newStaticInvokeExpr(method, Arrays.asList(arg), location);
   }

   public SpecialInvokeExpr newSpecialInvokeExpr(Local base, SootMethodRef method, Value arg, ASTNode location) {
      return this.newSpecialInvokeExpr(base, method, Arrays.asList(arg), location);
   }

   public VirtualInvokeExpr newVirtualInvokeExpr(Local base, SootMethodRef method, Value arg, ASTNode location) {
      return this.newVirtualInvokeExpr(base, method, Arrays.asList(arg), location);
   }

   public InterfaceInvokeExpr newInterfaceInvokeExpr(Local base, SootMethodRef method, Value arg, ASTNode location) {
      return this.newInterfaceInvokeExpr(base, method, Arrays.asList(arg), location);
   }

   public soot.jimple.ThrowStmt newThrowStmt(Value op, ASTNode location) {
      soot.jimple.ThrowStmt stmt = Jimple.v().newThrowStmt(op);
      Tag tag = this.getTag(op);
      if (tag != null) {
         stmt.getOpBox().addTag(tag);
      }

      return stmt;
   }

   public ExitMonitorStmt newExitMonitorStmt(Value op, ASTNode location) {
      ExitMonitorStmt stmt = Jimple.v().newExitMonitorStmt(op);
      Tag tag = this.getTag(op);
      if (tag != null) {
         stmt.getOpBox().addTag(tag);
      }

      return stmt;
   }

   public EnterMonitorStmt newEnterMonitorStmt(Value op, ASTNode location) {
      EnterMonitorStmt stmt = Jimple.v().newEnterMonitorStmt(op);
      Tag tag = this.getTag(op);
      if (tag != null) {
         stmt.getOpBox().addTag(tag);
      }

      return stmt;
   }

   public GotoStmt newGotoStmt(Unit target, ASTNode location) {
      GotoStmt stmt = Jimple.v().newGotoStmt(target);
      return stmt;
   }

   public ReturnVoidStmt newReturnVoidStmt(ASTNode location) {
      return Jimple.v().newReturnVoidStmt();
   }

   public soot.jimple.ReturnStmt newReturnStmt(Value op, ASTNode location) {
      soot.jimple.ReturnStmt stmt = Jimple.v().newReturnStmt(op);
      Tag tag = this.getTag(op);
      if (tag != null) {
         stmt.getOpBox().addTag(tag);
      }

      return stmt;
   }

   public soot.jimple.IfStmt newIfStmt(Value op, Unit target, ASTNode location) {
      soot.jimple.IfStmt stmt = Jimple.v().newIfStmt(op, target);
      Tag tag = this.getTag(op);
      if (tag != null) {
         stmt.getConditionBox().addTag(tag);
      }

      return stmt;
   }

   public IdentityStmt newIdentityStmt(Value local, Value identityRef, ASTNode location) {
      IdentityStmt stmt = Jimple.v().newIdentityStmt(local, identityRef);
      Tag left = this.getTag(local);
      if (left != null) {
         stmt.getLeftOpBox().addTag(left);
      }

      Tag right = this.getTag(identityRef);
      if (right != null) {
         stmt.getRightOpBox().addTag(right);
      }

      return stmt;
   }

   public AssignStmt newAssignStmt(Value variable, Value rvalue, ASTNode location) {
      AssignStmt stmt = Jimple.v().newAssignStmt(variable, rvalue);
      Tag left = this.getTag(variable);
      if (left != null) {
         stmt.getLeftOpBox().addTag(left);
      }

      Tag right = this.getTag(rvalue);
      if (right != null) {
         stmt.getRightOpBox().addTag(right);
      }

      return stmt;
   }

   public InvokeStmt newInvokeStmt(Value op, ASTNode location) {
      InvokeStmt stmt = Jimple.v().newInvokeStmt(op);
      Tag tag = this.getTag(op);
      if (tag != null) {
         stmt.getInvokeExprBox().addTag(tag);
      }

      return stmt;
   }

   public TableSwitchStmt newTableSwitchStmt(Value key, int lowIndex, int highIndex, java.util.List targets, Unit defaultTarget, ASTNode location) {
      TableSwitchStmt stmt = Jimple.v().newTableSwitchStmt(key, lowIndex, highIndex, targets, defaultTarget);
      Tag tag = this.getTag(key);
      if (tag != null) {
         stmt.getKeyBox().addTag(tag);
      }

      return stmt;
   }

   public LookupSwitchStmt newLookupSwitchStmt(Value key, java.util.List lookupValues, java.util.List targets, Unit defaultTarget, ASTNode location) {
      LookupSwitchStmt stmt = Jimple.v().newLookupSwitchStmt(key, lookupValues, targets, defaultTarget);
      Tag tag = this.getTag(key);
      if (tag != null) {
         stmt.getKeyBox().addTag(tag);
      }

      return stmt;
   }

   public StaticFieldRef newStaticFieldRef(SootFieldRef f, ASTNode location) {
      StaticFieldRef ref = Jimple.v().newStaticFieldRef(f);
      this.createTag(ref, location);
      return ref;
   }

   public ThisRef newThisRef(RefType t, ASTNode location) {
      ThisRef ref = Jimple.v().newThisRef(t);
      this.createTag(ref, location);
      return ref;
   }

   public ParameterRef newParameterRef(Type paramType, int number, ASTNode location) {
      ParameterRef ref = Jimple.v().newParameterRef(paramType, number);
      this.createTag(ref, location);
      return ref;
   }

   public InstanceFieldRef newInstanceFieldRef(Value base, SootFieldRef f, ASTNode location) {
      InstanceFieldRef ref = Jimple.v().newInstanceFieldRef(base, f);
      this.createTag(ref, location);
      Tag tag = this.getTag(base);
      if (tag != null) {
         ref.getBaseBox().addTag(tag);
      }

      return ref;
   }

   public CaughtExceptionRef newCaughtExceptionRef(ASTNode location) {
      CaughtExceptionRef ref = Jimple.v().newCaughtExceptionRef();
      this.createTag(ref, location);
      return ref;
   }

   public ArrayRef newArrayRef(Value base, Value index, ASTNode location) {
      ArrayRef ref = Jimple.v().newArrayRef(base, index);
      this.createTag(ref, location);
      Tag baseTag = this.getTag(base);
      if (baseTag != null) {
         ref.getBaseBox().addTag(baseTag);
      }

      Tag indexTag = this.getTag(index);
      if (indexTag != null) {
         ref.getIndexBox().addTag(indexTag);
      }

      return ref;
   }

   private BinopExpr updateTags(BinopExpr binary, ASTNode binaryLocation, Value op1, Value op2) {
      this.createTag(binary, binaryLocation);
      Tag op1tag = this.getTag(op1);
      if (op1tag != null) {
         binary.getOp1Box().addTag(op1tag);
      }

      Tag op2tag = this.getTag(op2);
      if (op2tag != null) {
         binary.getOp2Box().addTag(op2tag);
      }

      return binary;
   }

   private UnopExpr updateTags(UnopExpr unary, ASTNode unaryLocation, Value op) {
      this.createTag(unary, unaryLocation);
      Tag optag = this.getTag(op);
      if (optag != null) {
         unary.getOpBox().addTag(optag);
      }

      return unary;
   }

   private Tag getTag(Value value) {
      return (Tag)this.tagMap.get(value);
   }

   private void createTag(Value value, ASTNode node) {
      if (node != null && !this.tagMap.containsKey(value)) {
         if (node.getStart() != 0 && node.getEnd() != 0) {
            int line = ASTNode.getLine(node.getStart());
            int column = ASTNode.getColumn(node.getStart());
            int endLine = ASTNode.getLine(node.getEnd());
            int endColumn = ASTNode.getColumn(node.getEnd());
            String s = node.sourceFile();
            s = s != null ? s.substring(s.lastIndexOf(File.separatorChar) + 1) : "Unknown";
            this.tagMap.put(value, new SourceLnNamePosTag(s, line, endLine, column, endColumn));
         } else {
            this.tagMap.put(value, new LineNumberTag(node.lineNumber()));
         }

      }
   }

   public void copyLocation(Value fromValue, Value toValue) {
      Tag tag = (Tag)this.tagMap.get(fromValue);
      if (tag != null) {
         this.tagMap.put(toValue, tag);
      }

   }
}
