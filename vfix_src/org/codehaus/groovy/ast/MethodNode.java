package org.codehaus.groovy.ast;

import groovyjarjarasm.asm.Opcodes;
import java.util.List;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.Statement;

public class MethodNode extends AnnotatedNode implements Opcodes {
   private final String name;
   private int modifiers;
   private ClassNode returnType;
   private Parameter[] parameters;
   private boolean hasDefaultValue = false;
   private Statement code;
   private boolean dynamicReturnType;
   private VariableScope variableScope;
   private final ClassNode[] exceptions;
   private final boolean staticConstructor;
   private GenericsType[] genericsTypes = null;
   private boolean hasDefault;
   String typeDescriptor;

   public MethodNode(String name, int modifiers, ClassNode returnType, Parameter[] parameters, ClassNode[] exceptions, Statement code) {
      this.name = name;
      this.modifiers = modifiers;
      this.code = code;
      this.setReturnType(returnType);
      VariableScope scope = new VariableScope();
      this.setVariableScope(scope);
      this.setParameters(parameters);
      this.hasDefault = false;
      this.exceptions = exceptions;
      this.staticConstructor = name != null && name.equals("<clinit>");
   }

   public String getTypeDescriptor() {
      if (this.typeDescriptor == null) {
         StringBuffer buf = new StringBuffer(this.name.length() + this.parameters.length * 10);
         buf.append(this.returnType.getName());
         buf.append(' ');
         buf.append(this.name);
         buf.append('(');

         for(int i = 0; i < this.parameters.length; ++i) {
            if (i > 0) {
               buf.append(", ");
            }

            Parameter param = this.parameters[i];
            buf.append(param.getType().getName());
         }

         buf.append(')');
         this.typeDescriptor = buf.toString();
      }

      return this.typeDescriptor;
   }

   private void invalidateCachedData() {
      this.typeDescriptor = null;
   }

   public boolean isVoidMethod() {
      return this.returnType == ClassHelper.VOID_TYPE;
   }

   public Statement getCode() {
      return this.code;
   }

   public void setCode(Statement code) {
      this.code = code;
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public void setModifiers(int modifiers) {
      this.invalidateCachedData();
      this.modifiers = modifiers;
   }

   public String getName() {
      return this.name;
   }

   public Parameter[] getParameters() {
      return this.parameters;
   }

   public void setParameters(Parameter[] parameters) {
      this.invalidateCachedData();
      VariableScope scope = new VariableScope();
      this.parameters = parameters;
      if (parameters != null && parameters.length > 0) {
         Parameter[] arr$ = parameters;
         int len$ = parameters.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter para = arr$[i$];
            if (para.hasInitialExpression()) {
               this.hasDefaultValue = true;
            }

            para.setInStaticContext(this.isStatic());
            scope.putDeclaredVariable(para);
         }
      }

      this.setVariableScope(scope);
   }

   public ClassNode getReturnType() {
      return this.returnType;
   }

   public VariableScope getVariableScope() {
      return this.variableScope;
   }

   public void setVariableScope(VariableScope variableScope) {
      this.variableScope = variableScope;
      variableScope.setInStaticContext(this.isStatic());
   }

   public boolean isDynamicReturnType() {
      return this.dynamicReturnType;
   }

   public boolean isAbstract() {
      return (this.modifiers & 1024) != 0;
   }

   public boolean isStatic() {
      return (this.modifiers & 8) != 0;
   }

   public boolean isPublic() {
      return (this.modifiers & 1) != 0;
   }

   public boolean isPrivate() {
      return (this.modifiers & 2) != 0;
   }

   public boolean isProtected() {
      return (this.modifiers & 4) != 0;
   }

   public boolean hasDefaultValue() {
      return this.hasDefaultValue;
   }

   public String toString() {
      return "MethodNode@" + this.hashCode() + "[" + this.getTypeDescriptor() + "]";
   }

   public void setReturnType(ClassNode returnType) {
      this.invalidateCachedData();
      this.dynamicReturnType |= ClassHelper.DYNAMIC_TYPE == returnType;
      this.returnType = returnType;
      if (returnType == null) {
         this.returnType = ClassHelper.OBJECT_TYPE;
      }

   }

   public ClassNode[] getExceptions() {
      return this.exceptions;
   }

   public Statement getFirstStatement() {
      if (this.code == null) {
         return null;
      } else {
         Statement first = this.code;

         while(first instanceof BlockStatement) {
            List<Statement> list = ((BlockStatement)first).getStatements();
            if (list.isEmpty()) {
               first = null;
            } else {
               first = (Statement)list.get(0);
            }
         }

         return first;
      }
   }

   public GenericsType[] getGenericsTypes() {
      return this.genericsTypes;
   }

   public void setGenericsTypes(GenericsType[] genericsTypes) {
      this.invalidateCachedData();
      this.genericsTypes = genericsTypes;
   }

   public void setAnnotationDefault(boolean b) {
      this.hasDefault = b;
   }

   public boolean hasAnnotationDefault() {
      return this.hasDefault;
   }

   public boolean isStaticConstructor() {
      return this.staticConstructor;
   }
}
