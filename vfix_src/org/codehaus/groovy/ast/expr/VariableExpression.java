package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.Variable;

public class VariableExpression extends Expression implements Variable {
   public static final VariableExpression THIS_EXPRESSION;
   public static final VariableExpression SUPER_EXPRESSION;
   private String variable;
   private boolean inStaticContext;
   private boolean isDynamicTyped;
   private Variable accessedVariable;
   boolean closureShare;
   boolean useRef;
   private ClassNode originType;

   public Variable getAccessedVariable() {
      return this.accessedVariable;
   }

   public void setAccessedVariable(Variable origin) {
      this.accessedVariable = origin;
   }

   public VariableExpression(String variable, ClassNode type) {
      this.isDynamicTyped = false;
      this.closureShare = false;
      this.useRef = false;
      this.variable = variable;
      this.originType = type;
      this.setType(ClassHelper.getWrapper(type));
   }

   public VariableExpression(String variable) {
      this(variable, ClassHelper.DYNAMIC_TYPE);
   }

   public VariableExpression(Variable variable) {
      this(variable.getName(), variable.getOriginType());
      this.setAccessedVariable(variable);
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitVariableExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      return this;
   }

   public String getText() {
      return this.variable;
   }

   public String getName() {
      return this.variable;
   }

   public String toString() {
      return super.toString() + "[variable: " + this.variable + (this.isDynamicTyped() ? "" : " type: " + this.getType()) + "]";
   }

   public Expression getInitialExpression() {
      return null;
   }

   public boolean hasInitialExpression() {
      return false;
   }

   public boolean isInStaticContext() {
      return this.accessedVariable != null && this.accessedVariable != this ? this.accessedVariable.isInStaticContext() : this.inStaticContext;
   }

   public void setInStaticContext(boolean inStaticContext) {
      this.inStaticContext = inStaticContext;
   }

   public void setType(ClassNode cn) {
      super.setType(cn);
      this.isDynamicTyped |= ClassHelper.DYNAMIC_TYPE == cn;
   }

   public boolean isDynamicTyped() {
      return this.accessedVariable != null && this.accessedVariable != this ? this.accessedVariable.isDynamicTyped() : this.isDynamicTyped;
   }

   public boolean isClosureSharedVariable() {
      return this.accessedVariable != null && this.accessedVariable != this ? this.accessedVariable.isClosureSharedVariable() : this.closureShare;
   }

   public void setClosureSharedVariable(boolean inClosure) {
      this.closureShare = inClosure;
   }

   public void setUseReferenceDirectly(boolean useRef) {
      this.useRef = useRef;
   }

   public boolean isUseReferenceDirectly() {
      return this.useRef;
   }

   public ClassNode getType() {
      return this.accessedVariable != null && this.accessedVariable != this ? this.accessedVariable.getType() : super.getType();
   }

   public ClassNode getOriginType() {
      return this.originType;
   }

   public boolean isThisExpression() {
      return "this".equals(this.variable);
   }

   public boolean isSuperExpression() {
      return "super".equals(this.variable);
   }

   static {
      THIS_EXPRESSION = new VariableExpression("this", ClassHelper.DYNAMIC_TYPE);
      SUPER_EXPRESSION = new VariableExpression("super", ClassHelper.DYNAMIC_TYPE);
   }
}
