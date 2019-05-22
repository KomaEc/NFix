package soot.jimple.toolkits.base;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.FastHierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.tagkit.SourceLnPosTag;
import soot.util.NumberedString;

public class ExceptionChecker extends BodyTransformer {
   FastHierarchy hierarchy;
   ExceptionCheckerErrorReporter reporter;

   public ExceptionChecker(ExceptionCheckerErrorReporter r) {
      this.reporter = r;
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      Iterator it = b.getUnits().iterator();

      while(it.hasNext()) {
         Stmt s = (Stmt)it.next();
         if (s instanceof ThrowStmt) {
            ThrowStmt ts = (ThrowStmt)s;
            this.checkThrow(b, ts);
         } else if (s instanceof InvokeStmt) {
            InvokeStmt is = (InvokeStmt)s;
            this.checkInvoke(b, is);
         } else if (s instanceof AssignStmt && ((AssignStmt)s).getRightOp() instanceof InvokeExpr) {
            InvokeExpr ie = (InvokeExpr)((AssignStmt)s).getRightOp();
            this.checkInvokeExpr(b, ie, s);
         }
      }

   }

   protected void checkThrow(Body b, ThrowStmt ts) {
      if (!this.isThrowDeclared(b, ((RefType)ts.getOp().getType()).getSootClass()) && !this.isThrowFromCompiler(ts) && !this.isExceptionCaught(b, ts, (RefType)ts.getOp().getType())) {
         if (this.reporter != null) {
            this.reporter.reportError(new ExceptionCheckerError(b.getMethod(), ((RefType)ts.getOp().getType()).getSootClass(), ts, (SourceLnPosTag)ts.getOpBox().getTag("SourceLnPosTag")));
         }

      }
   }

   protected boolean isThrowDeclared(Body b, SootClass throwClass) {
      if (this.hierarchy == null) {
         this.hierarchy = new FastHierarchy();
      }

      if (!throwClass.equals(Scene.v().getSootClass("java.lang.RuntimeException")) && !throwClass.equals(Scene.v().getSootClass("java.lang.Error"))) {
         if (!this.hierarchy.isSubclass(throwClass, Scene.v().getSootClass("java.lang.RuntimeException")) && !this.hierarchy.isSubclass(throwClass, Scene.v().getSootClass("java.lang.Error"))) {
            if (b.getMethod().throwsException(throwClass)) {
               return true;
            } else {
               List<SootClass> exceptions = b.getMethod().getExceptionsUnsafe();
               if (exceptions != null) {
                  Iterator it = exceptions.iterator();

                  while(it.hasNext()) {
                     SootClass nextEx = (SootClass)it.next();
                     if (this.hierarchy.isSubclass(throwClass, nextEx)) {
                        return true;
                     }
                  }
               }

               return false;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   protected boolean isThrowFromCompiler(ThrowStmt ts) {
      return ts.hasTag("ThrowCreatedByCompilerTag");
   }

   protected boolean isExceptionCaught(Body b, Stmt s, RefType throwType) {
      if (this.hierarchy == null) {
         this.hierarchy = new FastHierarchy();
      }

      Iterator it = b.getTraps().iterator();

      Trap trap;
      do {
         do {
            if (!it.hasNext()) {
               return false;
            }

            trap = (Trap)it.next();
         } while(!trap.getException().getType().equals(throwType) && !this.hierarchy.isSubclass(throwType.getSootClass(), trap.getException().getType().getSootClass()));
      } while(!this.isThrowInStmtRange(b, (Stmt)trap.getBeginUnit(), (Stmt)trap.getEndUnit(), s));

      return true;
   }

   protected boolean isThrowInStmtRange(Body b, Stmt begin, Stmt end, Stmt s) {
      Iterator it = b.getUnits().iterator((Unit)begin, (Unit)end);

      do {
         if (!it.hasNext()) {
            return false;
         }
      } while(!it.next().equals(s));

      return true;
   }

   protected void checkInvoke(Body b, InvokeStmt is) {
      this.checkInvokeExpr(b, is.getInvokeExpr(), is);
   }

   private List<SootClass> getExceptionSpec(SootClass intrface, NumberedString sig) {
      SootMethod sm = intrface.getMethodUnsafe(sig);
      if (sm != null) {
         return sm.getExceptions();
      } else {
         List<SootClass> result = null;
         SootClass obj = Scene.v().getSootClass("java.lang.Object");
         sm = obj.getMethodUnsafe(sig);
         if (sm.getExceptionsUnsafe() == null) {
            return Collections.emptyList();
         } else {
            if (sm != null) {
               result = new Vector(sm.getExceptions());
            }

            Iterator var6 = intrface.getInterfaces().iterator();

            while(var6.hasNext()) {
               SootClass suprintr = (SootClass)var6.next();
               List<SootClass> other = this.getExceptionSpec(suprintr, sig);
               if (other != null) {
                  if (result == null) {
                     result = other;
                  } else {
                     ((List)result).retainAll(other);
                  }
               }
            }

            return (List)result;
         }
      }
   }

   protected void checkInvokeExpr(Body b, InvokeExpr ie, Stmt s) {
      if (!(ie instanceof InstanceInvokeExpr) || !(((InstanceInvokeExpr)ie).getBase().getType() instanceof ArrayType) || !ie.getMethodRef().name().equals("clone") || ie.getMethodRef().parameterTypes().size() != 0) {
         List exceptions = ie instanceof InterfaceInvokeExpr ? this.getExceptionSpec(ie.getMethodRef().declaringClass(), ie.getMethodRef().getSubSignature()) : ie.getMethod().getExceptionsUnsafe();
         if (exceptions != null) {
            Iterator it = exceptions.iterator();

            while(it.hasNext()) {
               SootClass sc = (SootClass)it.next();
               if (!this.isThrowDeclared(b, sc) && !this.isExceptionCaught(b, s, sc.getType()) && this.reporter != null) {
                  if (s instanceof InvokeStmt) {
                     this.reporter.reportError(new ExceptionCheckerError(b.getMethod(), sc, s, (SourceLnPosTag)s.getTag("SourceLnPosTag")));
                  } else if (s instanceof AssignStmt) {
                     this.reporter.reportError(new ExceptionCheckerError(b.getMethod(), sc, s, (SourceLnPosTag)((AssignStmt)s).getRightOpBox().getTag("SourceLnPosTag")));
                  }
               }
            }

         }
      }
   }
}
