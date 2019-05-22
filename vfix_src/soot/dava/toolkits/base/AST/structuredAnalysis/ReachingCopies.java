package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.Iterator;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class ReachingCopies extends StructuredAnalysis {
   public ReachingCopies(Object analyze) {
      this.process(analyze, new DavaFlowSet());
   }

   public DavaFlowSet emptyFlowSet() {
      return new DavaFlowSet();
   }

   public void setMergeType() {
      this.MERGETYPE = 2;
   }

   public DavaFlowSet newInitialFlow() {
      return new DavaFlowSet();
   }

   public DavaFlowSet cloneFlowSet(DavaFlowSet flowSet) {
      return flowSet.clone();
   }

   public DavaFlowSet processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet input) {
      return input;
   }

   public DavaFlowSet processSynchronizedLocal(Local local, DavaFlowSet input) {
      return input;
   }

   public DavaFlowSet processSwitchKey(Value key, DavaFlowSet input) {
      return input;
   }

   public DavaFlowSet processStatement(Stmt s, DavaFlowSet input) {
      if (input == this.NOPATH) {
         return input;
      } else if (s instanceof DefinitionStmt) {
         DavaFlowSet toReturn = this.cloneFlowSet(input);
         Value leftOp = ((DefinitionStmt)s).getLeftOp();
         Value rightOp = ((DefinitionStmt)s).getRightOp();
         if (leftOp instanceof Local) {
            this.kill(toReturn, (Local)leftOp);
         }

         if (leftOp instanceof Local && rightOp instanceof Local) {
            this.gen(toReturn, (Local)leftOp, (Local)rightOp);
         }

         return toReturn;
      } else {
         return input;
      }
   }

   public void gen(DavaFlowSet in, Local left, Local right) {
      ReachingCopies.LocalPair localp = new ReachingCopies.LocalPair(left, right);
      in.add(localp);
   }

   public void kill(DavaFlowSet<ReachingCopies.LocalPair> in, Local redefined) {
      Iterator listIt = in.iterator();

      while(listIt.hasNext()) {
         ReachingCopies.LocalPair tempPair = (ReachingCopies.LocalPair)listIt.next();
         if (tempPair.contains(redefined)) {
            listIt.remove();
         }
      }

   }

   public DavaFlowSet getReachingCopies(Object node) {
      DavaFlowSet beforeSet = this.getBeforeSet(node);
      if (beforeSet == null) {
         throw new RuntimeException("Could not get reaching copies of node/stmt");
      } else {
         return beforeSet;
      }
   }

   public class LocalPair {
      private final Local leftLocal;
      private final Local rightLocal;

      public LocalPair(Local left, Local right) {
         this.leftLocal = left;
         this.rightLocal = right;
      }

      public Local getLeftLocal() {
         return this.leftLocal;
      }

      public Local getRightLocal() {
         return this.rightLocal;
      }

      public boolean equals(Object other) {
         return other instanceof ReachingCopies.LocalPair && this.leftLocal.toString().equals(((ReachingCopies.LocalPair)other).getLeftLocal().toString()) && this.rightLocal.toString().equals(((ReachingCopies.LocalPair)other).getRightLocal().toString());
      }

      public boolean contains(Local local) {
         return this.leftLocal.toString().equals(local.toString()) || this.rightLocal.toString().equals(local.toString());
      }

      public String toString() {
         StringBuffer b = new StringBuffer();
         b.append("<" + this.leftLocal.toString() + "," + this.rightLocal.toString() + ">");
         return b.toString();
      }
   }
}
