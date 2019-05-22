package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import soot.Local;
import soot.SootField;
import soot.Value;
import soot.dava.DavaFlowAnalysisException;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;

public class MustMayInitialize extends StructuredAnalysis {
   HashMap<Object, List> mapping = new HashMap();
   DavaFlowSet finalResult;
   public static final int MUST = 0;
   public static final int MAY = 1;
   int MUSTMAY;

   public MustMayInitialize(Object analyze, int MUSTorMAY) {
      this.MUSTMAY = MUSTorMAY;
      this.setMergeType();
      this.finalResult = this.process(analyze, new DavaFlowSet());
   }

   public DavaFlowSet emptyFlowSet() {
      return new DavaFlowSet();
   }

   public void setMergeType() {
      if (this.MUSTMAY == 0) {
         this.MERGETYPE = 2;
      } else {
         if (this.MUSTMAY != 1) {
            throw new DavaFlowAnalysisException("Only allowed 0 or 1 for MUST or MAY values");
         }

         this.MERGETYPE = 1;
      }

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

   public DavaFlowSet processStatement(Stmt s, DavaFlowSet inSet) {
      if (inSet == this.NOPATH) {
         return inSet;
      } else if (s instanceof DefinitionStmt) {
         DavaFlowSet toReturn = this.cloneFlowSet(inSet);
         Value leftOp = ((DefinitionStmt)s).getLeftOp();
         SootField field = null;
         Object temp;
         ArrayList defs;
         if (leftOp instanceof Local) {
            toReturn.add(leftOp);
            temp = this.mapping.get(leftOp);
            if (temp == null) {
               defs = new ArrayList();
            } else {
               defs = (ArrayList)temp;
            }

            defs.add(s);
            this.mapping.put(leftOp, defs);
         } else if (leftOp instanceof FieldRef) {
            field = ((FieldRef)leftOp).getField();
            toReturn.add(field);
            temp = this.mapping.get(field);
            if (temp == null) {
               defs = new ArrayList();
            } else {
               defs = (ArrayList)temp;
            }

            defs.add(s);
            this.mapping.put(field, defs);
         }

         return toReturn;
      } else {
         return inSet;
      }
   }

   public boolean isMayInitialized(SootField field) {
      if (this.MUSTMAY == 1) {
         Object temp = this.mapping.get(field);
         if (temp == null) {
            return false;
         } else {
            List list = (List)temp;
            return list.size() != 0;
         }
      } else {
         throw new RuntimeException("Cannot invoke isMayInitialized for a MUST analysis");
      }
   }

   public boolean isMayInitialized(Value local) {
      if (this.MUSTMAY == 1) {
         Object temp = this.mapping.get(local);
         if (temp == null) {
            return false;
         } else {
            List list = (List)temp;
            return list.size() != 0;
         }
      } else {
         throw new RuntimeException("Cannot invoke isMayInitialized for a MUST analysis");
      }
   }

   public boolean isMustInitialized(SootField field) {
      if (this.MUSTMAY == 0) {
         return this.finalResult.contains(field);
      } else {
         throw new RuntimeException("Cannot invoke isMustinitialized for a MAY analysis");
      }
   }

   public boolean isMustInitialized(Value local) {
      if (this.MUSTMAY == 0) {
         return this.finalResult.contains(local);
      } else {
         throw new RuntimeException("Cannot invoke isMustinitialized for a MAY analysis");
      }
   }

   public List getDefs(Value local) {
      Object temp = this.mapping.get(local);
      return temp == null ? null : (List)temp;
   }

   public List getDefs(SootField field) {
      Object temp = this.mapping.get(field);
      return temp == null ? null : (List)temp;
   }
}
