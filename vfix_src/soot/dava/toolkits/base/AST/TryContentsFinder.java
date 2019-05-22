package soot.dava.toolkits.base.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ThrowStmt;
import soot.util.IterableSet;

public class TryContentsFinder extends ASTAnalysis {
   private IterableSet curExceptionSet = new IterableSet();
   private final HashMap<Object, IterableSet> node2ExceptionSet = new HashMap();

   public TryContentsFinder(Singletons.Global g) {
   }

   public static TryContentsFinder v() {
      return G.v().soot_dava_toolkits_base_AST_TryContentsFinder();
   }

   public int getAnalysisDepth() {
      return 2;
   }

   public IterableSet remove_CurExceptionSet() {
      IterableSet s = this.curExceptionSet;
      this.set_CurExceptionSet(new IterableSet());
      return s;
   }

   public void set_CurExceptionSet(IterableSet curExceptionSet) {
      this.curExceptionSet = curExceptionSet;
   }

   public void analyseThrowStmt(ThrowStmt s) {
      Value op = s.getOp();
      if (op instanceof Local) {
         this.add_ThrownType(((Local)op).getType());
      } else if (op instanceof FieldRef) {
         this.add_ThrownType(((FieldRef)op).getType());
      }

   }

   private void add_ThrownType(Type t) {
      if (t instanceof RefType) {
         this.curExceptionSet.add(((RefType)t).getSootClass());
      }

   }

   public void analyseInvokeExpr(InvokeExpr ie) {
      this.curExceptionSet.addAll(ie.getMethod().getExceptions());
   }

   public void analyseInstanceInvokeExpr(InstanceInvokeExpr iie) {
      this.analyseInvokeExpr(iie);
   }

   public void analyseASTNode(ASTNode n) {
      if (n instanceof ASTTryNode) {
         ASTTryNode tryNode = (ASTTryNode)n;
         ArrayList<Object> toRemove = new ArrayList();
         IterableSet tryExceptionSet = (IterableSet)this.node2ExceptionSet.get(tryNode.get_TryBodyContainer());
         if (tryExceptionSet == null) {
            tryExceptionSet = new IterableSet();
            this.node2ExceptionSet.put(tryNode.get_TryBodyContainer(), tryExceptionSet);
         }

         List<Object> catchBodies = tryNode.get_CatchList();
         List<Object> subBodies = tryNode.get_SubBodies();
         Iterator cit = catchBodies.iterator();

         while(cit.hasNext()) {
            Object catchBody = cit.next();
            SootClass exception = (SootClass)tryNode.get_ExceptionMap().get(catchBody);
            if (!this.catches_Exception(tryExceptionSet, exception) && !this.catches_RuntimeException(exception)) {
               toRemove.add(catchBody);
            }
         }

         Iterator trit = toRemove.iterator();

         while(trit.hasNext()) {
            Object catchBody = trit.next();
            subBodies.remove(catchBody);
            catchBodies.remove(catchBody);
         }

         IterableSet passingSet = (IterableSet)tryExceptionSet.clone();
         cit = catchBodies.iterator();

         while(cit.hasNext()) {
            passingSet.remove(tryNode.get_ExceptionMap().get(cit.next()));
         }

         cit = catchBodies.iterator();

         while(cit.hasNext()) {
            passingSet.addAll(this.get_ExceptionSet(cit.next()));
         }

         this.node2ExceptionSet.put(n, passingSet);
      } else {
         Iterator sbit = n.get_SubBodies().iterator();

         while(sbit.hasNext()) {
            Iterator it = ((List)sbit.next()).iterator();

            while(it.hasNext()) {
               this.add_ExceptionSet(n, this.get_ExceptionSet(it.next()));
            }
         }
      }

      this.remove_CurExceptionSet();
   }

   public IterableSet get_ExceptionSet(Object node) {
      IterableSet fullSet = (IterableSet)this.node2ExceptionSet.get(node);
      if (fullSet == null) {
         fullSet = new IterableSet();
         this.node2ExceptionSet.put(node, fullSet);
      }

      return fullSet;
   }

   public void add_ExceptionSet(Object node, IterableSet s) {
      IterableSet fullSet = (IterableSet)this.node2ExceptionSet.get(node);
      if (fullSet == null) {
         fullSet = new IterableSet();
         this.node2ExceptionSet.put(node, fullSet);
      }

      fullSet.addAll(s);
   }

   private boolean catches_Exception(IterableSet tryExceptionSet, SootClass c) {
      Iterator it = tryExceptionSet.iterator();

      label23:
      while(it.hasNext()) {
         for(SootClass thrownException = (SootClass)it.next(); thrownException != c; thrownException = thrownException.getSuperclass()) {
            if (!thrownException.hasSuperclass()) {
               continue label23;
            }
         }

         return true;
      }

      return false;
   }

   private boolean catches_RuntimeException(SootClass c) {
      if (c != Scene.v().getSootClass("java.lang.Throwable") && c != Scene.v().getSootClass("java.lang.Exception")) {
         SootClass caughtException = c;

         for(SootClass runtimeException = Scene.v().getSootClass("java.lang.RuntimeException"); caughtException != runtimeException; caughtException = caughtException.getSuperclass()) {
            if (!caughtException.hasSuperclass()) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }
}
