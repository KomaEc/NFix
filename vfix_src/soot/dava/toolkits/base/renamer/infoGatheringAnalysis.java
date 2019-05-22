package soot.dava.toolkits.base.renamer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import soot.BooleanType;
import soot.Local;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.grimp.NewInvokeExpr;
import soot.grimp.internal.GAssignStmt;
import soot.jimple.ArrayRef;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.EqExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.internal.AbstractInstanceFieldRef;

public class infoGatheringAnalysis extends DepthFirstAdapter {
   public boolean DEBUG = false;
   public static final int CLASSNAME = 0;
   public static final int METHODNAME = 1;
   public static final int GETSET = 2;
   public static final int IF = 3;
   public static final int WHILE = 4;
   public static final int SWITCH = 5;
   public static final int ARRAYINDEX = 6;
   public static final int MAINARG = 7;
   public static final int FIELDASSIGN = 8;
   public static final int FORLOOPUPDATE = 9;
   public static final int CAST = 10;
   public static final int NUMBITS = 11;
   heuristicSet info = new heuristicSet();
   boolean inDefinitionStmt = false;
   Local definedLocal = null;
   boolean inIf = false;
   boolean inWhile = false;
   boolean inFor = false;

   public infoGatheringAnalysis(DavaBody davaBody) {
      List localList = new ArrayList();
      HashSet params = new HashSet();
      HashSet<Object> thisLocals = davaBody.get_ThisLocals();
      Iterator localIt = davaBody.getLocals().iterator();

      while(localIt.hasNext()) {
         Local local = (Local)localIt.next();
         if (!params.contains(local) && !thisLocals.contains(local)) {
            localList.add(local);
         }
      }

      Iterator it = localList.iterator();

      while(it.hasNext()) {
         Local local = (Local)it.next();
         this.info.add(local, 11);
         this.debug("infoGatheringAnalysis", "added " + local.getName() + " to the heuristicset");
      }

      SootMethod method = davaBody.getMethod();
      if (method.getSubSignature().compareTo("void main(java.lang.String[])") == 0) {
         it = davaBody.get_ParamMap().values().iterator();
         int num = 0;

         Local param;
         for(param = null; it.hasNext(); param = (Local)it.next()) {
            ++num;
         }

         if (num > 1) {
            throw new DecompilationException("main method has greater than 1 args!!");
         }

         this.info.setHeuristic(param, 7);
      }

   }

   public void inDefinitionStmt(DefinitionStmt s) {
      this.inDefinitionStmt = true;
      Value v = s.getLeftOp();
      if (v instanceof Local) {
         if (this.info.contains((Local)v)) {
            this.definedLocal = (Local)v;
         } else {
            this.definedLocal = null;
         }
      }

   }

   public void outDefinitionStmt(DefinitionStmt s) {
      if (this.definedLocal != null && s.getRightOp() instanceof CastExpr) {
         Type castType = ((CastExpr)s.getRightOp()).getCastType();
         this.info.addCastString(this.definedLocal, castType.toString());
      }

      this.inDefinitionStmt = false;
      this.definedLocal = null;
   }

   public void inStaticFieldRef(StaticFieldRef sfr) {
      if (this.inDefinitionStmt && this.definedLocal != null) {
         SootField field = sfr.getField();
         this.info.setFieldName(this.definedLocal, field.getName());
      }

   }

   public void inInstanceFieldRef(InstanceFieldRef ifr) {
      if (ifr instanceof AbstractInstanceFieldRef && this.inDefinitionStmt && this.definedLocal != null) {
         SootField field = ((AbstractInstanceFieldRef)ifr).getField();
         this.info.setFieldName(this.definedLocal, field.getName());
      }

   }

   public void outInvokeExpr(InvokeExpr ie) {
      if (this.inDefinitionStmt && this.definedLocal != null) {
         String className;
         if (ie instanceof NewInvokeExpr) {
            RefType ref = ((NewInvokeExpr)ie).getBaseType();
            className = ref.getClassName();
            this.debug("outInvokeExpr", "defined local is" + this.definedLocal);
            this.info.setObjectClassName(this.definedLocal, className);
         } else {
            SootMethodRef methodRef = ie.getMethodRef();
            className = methodRef.name();
            this.info.setMethodName(this.definedLocal, className);
         }
      }

   }

   public void inASTUnaryCondition(ASTUnaryCondition uc) {
      Value val = uc.getValue();
      if (val instanceof Local) {
         if (this.inIf) {
            this.info.setHeuristic((Local)val, 3);
         }

         if (this.inWhile) {
            this.info.setHeuristic((Local)val, 4);
         }
      }

   }

   public void inASTBinaryCondition(ASTBinaryCondition bc) {
      ConditionExpr condition = bc.getConditionExpr();
      Local local = this.checkBooleanUse(condition);
      if (local != null) {
         if (this.inIf) {
            this.info.setHeuristic(local, 3);
         }

         if (this.inWhile) {
            this.info.setHeuristic(local, 4);
         }
      }

   }

   public void inASTIfNode(ASTIfNode node) {
      this.inIf = true;
   }

   public void outASTIfNode(ASTIfNode node) {
      this.inIf = false;
   }

   public void inASTIfElseNode(ASTIfElseNode node) {
      this.inIf = true;
   }

   public void outASTIfElseNode(ASTIfElseNode node) {
      this.inIf = false;
   }

   public void inASTWhileNode(ASTWhileNode node) {
      this.inWhile = true;
   }

   public void outASTWhileNode(ASTWhileNode node) {
      this.inWhile = false;
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      this.inWhile = true;
   }

   public void outASTDoWhileNode(ASTDoWhileNode node) {
      this.inWhile = false;
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      Value key = node.get_Key();
      if (key instanceof Local) {
         this.info.setHeuristic((Local)key, 5);
      }

   }

   public void inArrayRef(ArrayRef ar) {
      Value index = ar.getIndex();
      if (index instanceof Local) {
         this.info.setHeuristic((Local)index, 6);
      }

   }

   public void inASTTryNode(ASTTryNode node) {
   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      this.inFor = true;
      Iterator var2 = node.getUpdate().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         if (s instanceof GAssignStmt) {
            Value leftOp = ((GAssignStmt)s).getLeftOp();
            if (leftOp instanceof Local) {
               this.info.setHeuristic((Local)leftOp, 9);
            }
         }
      }

   }

   public void outASTForLoopNode(ASTForLoopNode node) {
      this.inFor = false;
   }

   public void outASTMethodNode(ASTMethodNode node) {
      if (this.DEBUG) {
         System.out.println("SET START");
         this.info.print();
         System.out.println("SET END");
      }

   }

   private Local checkBooleanUse(ConditionExpr condition) {
      boolean booleanUse = false;
      if (condition instanceof NeExpr || condition instanceof EqExpr) {
         Value op1 = condition.getOp1();
         Value op2 = condition.getOp2();
         Type op2Type;
         if (op1 instanceof DIntConstant) {
            op2Type = ((DIntConstant)op1).type;
            if (op2Type instanceof BooleanType) {
               booleanUse = true;
            }
         } else if (op2 instanceof DIntConstant) {
            op2Type = ((DIntConstant)op2).type;
            if (op2Type instanceof BooleanType) {
               booleanUse = true;
            }
         }

         if (!booleanUse) {
            return null;
         }

         if (op1 instanceof Local) {
            return (Local)op1;
         }

         if (op2 instanceof Local) {
            return (Local)op2;
         }
      }

      return null;
   }

   public heuristicSet getHeuristicSet() {
      return this.info;
   }

   public void debug(String methodName, String debug) {
      if (this.DEBUG) {
         System.out.println(methodName + "    DEBUG: " + debug);
      }

   }
}
