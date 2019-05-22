package soot.dava.toolkits.base.AST.structuredAnalysis;

import soot.Local;
import soot.PrimType;
import soot.SootField;
import soot.dava.DavaFlowAnalysisException;

public class CPVariable {
   private Local local;
   private SootField field;

   public CPVariable(SootField field) {
      this.field = field;
      this.local = null;
      if (!(field.getType() instanceof PrimType)) {
         throw new DavaFlowAnalysisException("Variables managed for CP should only be primitives");
      }
   }

   public CPVariable(Local local) {
      this.field = null;
      this.local = local;
      if (!(local.getType() instanceof PrimType)) {
         throw new DavaFlowAnalysisException("Variables managed for CP should only be primitives");
      }
   }

   public boolean containsLocal() {
      return this.local != null;
   }

   public boolean containsSootField() {
      return this.field != null;
   }

   public SootField getSootField() {
      if (this.containsSootField()) {
         return this.field;
      } else {
         throw new DavaFlowAnalysisException("getsootField invoked when variable is not a sootfield!!!");
      }
   }

   public Local getLocal() {
      if (this.containsLocal()) {
         return this.local;
      } else {
         throw new DavaFlowAnalysisException("getLocal invoked when variable is not a local");
      }
   }

   public boolean equals(CPVariable var) {
      if (this.containsLocal() && var.containsLocal() && this.getLocal().getName().equals(var.getLocal().getName())) {
         return true;
      } else {
         return this.containsSootField() && var.containsSootField() && this.getSootField().getName().equals(var.getSootField().getName());
      }
   }

   public String toString() {
      if (this.containsLocal()) {
         return "Local: " + this.getLocal().getName();
      } else {
         return this.containsSootField() ? "SootField: " + this.getSootField().getName() : "UNKNOWN CONSTANT_PROPAGATION_VARIABLE";
      }
   }
}
