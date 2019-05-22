package soot.jimple;

import soot.ArrayType;
import soot.Local;
import soot.RefType;
import soot.Value;

public abstract class PointerStmtSwitch extends AbstractStmtSwitch {
   Stmt statement;

   protected abstract void caseAssignConstStmt(Value var1, Constant var2);

   protected abstract void caseCopyStmt(Local var1, Local var2);

   protected void caseCastStmt(Local dest, Local src, CastExpr c) {
      this.caseCopyStmt(dest, src);
   }

   protected abstract void caseIdentityStmt(Local var1, IdentityRef var2);

   protected abstract void caseLoadStmt(Local var1, InstanceFieldRef var2);

   protected abstract void caseStoreStmt(InstanceFieldRef var1, Local var2);

   protected abstract void caseArrayLoadStmt(Local var1, ArrayRef var2);

   protected abstract void caseArrayStoreStmt(ArrayRef var1, Local var2);

   protected abstract void caseGlobalLoadStmt(Local var1, StaticFieldRef var2);

   protected abstract void caseGlobalStoreStmt(StaticFieldRef var1, Local var2);

   protected abstract void caseReturnStmt(Local var1);

   protected void caseReturnConstStmt(Constant val) {
      this.caseUninterestingStmt(this.statement);
   }

   protected abstract void caseAnyNewStmt(Local var1, Expr var2);

   protected void caseNewStmt(Local dest, NewExpr e) {
      this.caseAnyNewStmt(dest, e);
   }

   protected void caseNewArrayStmt(Local dest, NewArrayExpr e) {
      this.caseAnyNewStmt(dest, e);
   }

   protected void caseNewMultiArrayStmt(Local dest, NewMultiArrayExpr e) {
      this.caseAnyNewStmt(dest, e);
   }

   protected abstract void caseInvokeStmt(Local var1, InvokeExpr var2);

   protected void caseThrowStmt(Local thrownException) {
      this.caseUninterestingStmt(this.statement);
   }

   protected void caseCatchStmt(Local dest, CaughtExceptionRef cer) {
      this.caseUninterestingStmt(this.statement);
   }

   protected void caseUninterestingStmt(Stmt s) {
   }

   public final void caseAssignStmt(AssignStmt s) {
      this.statement = s;
      Value lhs = s.getLeftOp();
      Value rhs = s.getRightOp();
      if (!(lhs.getType() instanceof RefType) && !(lhs.getType() instanceof ArrayType)) {
         if (rhs instanceof InvokeExpr) {
            this.caseInvokeStmt((Local)null, (InvokeExpr)rhs);
         } else {
            this.caseUninterestingStmt(s);
         }
      } else if (rhs instanceof InvokeExpr) {
         this.caseInvokeStmt((Local)lhs, (InvokeExpr)rhs);
      } else {
         if (lhs instanceof Local) {
            if (rhs instanceof Local) {
               this.caseCopyStmt((Local)lhs, (Local)rhs);
            } else if (rhs instanceof InstanceFieldRef) {
               this.caseLoadStmt((Local)lhs, (InstanceFieldRef)rhs);
            } else if (rhs instanceof ArrayRef) {
               this.caseArrayLoadStmt((Local)lhs, (ArrayRef)rhs);
            } else if (rhs instanceof StaticFieldRef) {
               this.caseGlobalLoadStmt((Local)lhs, (StaticFieldRef)rhs);
            } else if (rhs instanceof NewExpr) {
               this.caseNewStmt((Local)lhs, (NewExpr)rhs);
            } else if (rhs instanceof NewArrayExpr) {
               this.caseNewArrayStmt((Local)lhs, (NewArrayExpr)rhs);
            } else if (rhs instanceof NewMultiArrayExpr) {
               this.caseNewMultiArrayStmt((Local)lhs, (NewMultiArrayExpr)rhs);
            } else if (rhs instanceof CastExpr) {
               CastExpr r = (CastExpr)rhs;
               Value rv = r.getOp();
               if (rv instanceof Constant) {
                  this.caseAssignConstStmt(lhs, (Constant)rv);
               } else {
                  this.caseCastStmt((Local)lhs, (Local)rv, r);
               }
            } else {
               if (!(rhs instanceof Constant)) {
                  throw new RuntimeException("unhandled stmt " + s);
               }

               this.caseAssignConstStmt(lhs, (Constant)rhs);
            }
         } else if (lhs instanceof InstanceFieldRef) {
            if (rhs instanceof Local) {
               this.caseStoreStmt((InstanceFieldRef)lhs, (Local)rhs);
            } else {
               if (!(rhs instanceof Constant)) {
                  throw new RuntimeException("unhandled stmt " + s);
               }

               this.caseAssignConstStmt(lhs, (Constant)rhs);
            }
         } else if (lhs instanceof ArrayRef) {
            if (rhs instanceof Local) {
               this.caseArrayStoreStmt((ArrayRef)lhs, (Local)rhs);
            } else {
               if (!(rhs instanceof Constant)) {
                  throw new RuntimeException("unhandled stmt " + s);
               }

               this.caseAssignConstStmt(lhs, (Constant)rhs);
            }
         } else if (lhs instanceof StaticFieldRef) {
            if (rhs instanceof Local) {
               this.caseGlobalStoreStmt((StaticFieldRef)lhs, (Local)rhs);
            } else {
               if (!(rhs instanceof Constant)) {
                  throw new RuntimeException("unhandled stmt " + s);
               }

               this.caseAssignConstStmt(lhs, (Constant)rhs);
            }
         } else {
            if (!(rhs instanceof Constant)) {
               throw new RuntimeException("unhandled stmt " + s);
            }

            this.caseAssignConstStmt(lhs, (Constant)rhs);
         }

      }
   }

   public final void caseReturnStmt(ReturnStmt s) {
      this.statement = s;
      Value op = s.getOp();
      if (!(op.getType() instanceof RefType) && !(op.getType() instanceof ArrayType)) {
         this.caseReturnStmt((Local)null);
      } else if (op instanceof Constant) {
         this.caseReturnConstStmt((Constant)op);
      } else {
         this.caseReturnStmt((Local)op);
      }

   }

   public final void caseReturnVoidStmt(ReturnVoidStmt s) {
      this.statement = s;
      this.caseReturnStmt((Local)null);
   }

   public final void caseInvokeStmt(InvokeStmt s) {
      this.statement = s;
      this.caseInvokeStmt((Local)null, s.getInvokeExpr());
   }

   public final void caseIdentityStmt(IdentityStmt s) {
      this.statement = s;
      Value lhs = s.getLeftOp();
      Value rhs = s.getRightOp();
      if (!(lhs.getType() instanceof RefType) && !(lhs.getType() instanceof ArrayType)) {
         this.caseUninterestingStmt(s);
      } else {
         Local llhs = (Local)lhs;
         if (rhs instanceof CaughtExceptionRef) {
            this.caseCatchStmt(llhs, (CaughtExceptionRef)rhs);
         } else {
            IdentityRef rrhs = (IdentityRef)rhs;
            this.caseIdentityStmt(llhs, rrhs);
         }

      }
   }

   public final void caseThrowStmt(ThrowStmt s) {
      this.statement = s;
      this.caseThrowStmt((Local)s.getOp());
   }
}
