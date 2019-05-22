package org.apache.tools.ant.types;

import org.apache.tools.ant.Project;

public class Substitution extends DataType {
   public static final String DATA_TYPE_NAME = "substitition";
   private String expression = null;

   public void setExpression(String expression) {
      this.expression = expression;
   }

   public String getExpression(Project p) {
      return this.isReference() ? this.getRef(p).getExpression(p) : this.expression;
   }

   public Substitution getRef(Project p) {
      return (Substitution)this.getCheckedRef(p);
   }
}
