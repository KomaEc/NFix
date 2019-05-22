package soot.dava.toolkits.base.AST.structuredAnalysis;

import soot.dava.DavaFlowAnalysisException;

public class CPTuple {
   private String sootClass;
   private CPVariable variable;
   private Object constant;
   private Boolean TOP = new Boolean(false);

   public CPTuple clone() {
      if (this.isTop()) {
         return new CPTuple(this.sootClass, this.variable, true);
      } else if (this.isValueADouble()) {
         return new CPTuple(this.sootClass, this.variable, new Double((Double)this.constant));
      } else if (this.isValueAFloat()) {
         return new CPTuple(this.sootClass, this.variable, new Float((Float)this.constant));
      } else if (this.isValueALong()) {
         return new CPTuple(this.sootClass, this.variable, new Long((Long)this.constant));
      } else if (this.isValueABoolean()) {
         return new CPTuple(this.sootClass, this.variable, new Boolean((Boolean)this.constant));
      } else if (this.isValueAInteger()) {
         return new CPTuple(this.sootClass, this.variable, new Integer((Integer)this.constant));
      } else {
         throw new RuntimeException("illegal Constant Type...report to developer" + this.constant);
      }
   }

   public CPTuple(String sootClass, CPVariable variable, Object constant) {
      if (!(constant instanceof Float) && !(constant instanceof Double) && !(constant instanceof Long) && !(constant instanceof Boolean) && !(constant instanceof Integer)) {
         throw new DavaFlowAnalysisException("Third argument of VariableValuePair not an acceptable constant value...report to developer");
      } else {
         this.sootClass = sootClass;
         this.variable = variable;
         this.constant = constant;
         this.TOP = new Boolean(false);
      }
   }

   public CPTuple(String sootClass, CPVariable variable, boolean top) {
      this.sootClass = sootClass;
      this.variable = variable;
      this.setTop();
   }

   public boolean containsLocal() {
      return this.variable.containsLocal();
   }

   public boolean containsField() {
      return this.variable.containsSootField();
   }

   public boolean isTop() {
      return this.TOP;
   }

   public void setTop() {
      this.constant = null;
      this.TOP = new Boolean(true);
   }

   public boolean isValueADouble() {
      return this.constant instanceof Double;
   }

   public boolean isValueAFloat() {
      return this.constant instanceof Float;
   }

   public boolean isValueALong() {
      return this.constant instanceof Long;
   }

   public boolean isValueABoolean() {
      return this.constant instanceof Boolean;
   }

   public boolean isValueAInteger() {
      return this.constant instanceof Integer;
   }

   public Object getValue() {
      return this.constant;
   }

   public void setValue(Object constant) {
      if (!(constant instanceof Float) && !(constant instanceof Double) && !(constant instanceof Long) && !(constant instanceof Boolean) && !(constant instanceof Integer)) {
         throw new DavaFlowAnalysisException("argument to setValue not an acceptable constant value...report to developer");
      } else {
         this.constant = constant;
         this.TOP = new Boolean(false);
      }
   }

   public String getSootClassName() {
      return this.sootClass;
   }

   public CPVariable getVariable() {
      return this.variable;
   }

   public boolean equals(Object other) {
      if (other instanceof CPTuple) {
         CPTuple var = (CPTuple)other;
         if (this.sootClass.equals(var.getSootClassName()) && this.variable.equals(var.getVariable()) && this.isTop() & var.isTop()) {
            return true;
         }

         if (this.isTop() || var.isTop()) {
            return false;
         }

         if (this.sootClass.equals(var.getSootClassName()) && this.variable.equals(var.getVariable()) && this.constant.equals(var.getValue())) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      if (this.isTop()) {
         b.append("<" + this.sootClass + ", " + this.variable.toString() + ", TOP>");
      } else {
         b.append("<" + this.sootClass + ", " + this.variable.toString() + "," + this.constant.toString() + ">");
      }

      return b.toString();
   }
}
