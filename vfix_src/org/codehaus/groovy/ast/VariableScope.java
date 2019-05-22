package org.codehaus.groovy.ast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VariableScope {
   private Map<String, Variable> declaredVariables = Collections.emptyMap();
   private Map<String, Variable> referencedLocalVariables = Collections.emptyMap();
   private Map<String, Variable> referencedClassVariables = Collections.emptyMap();
   private boolean inStaticContext = false;
   private boolean resolvesDynamic = false;
   private ClassNode clazzScope;
   private VariableScope parent;

   public VariableScope() {
   }

   public VariableScope(VariableScope parent) {
      this.parent = parent;
   }

   public Variable getDeclaredVariable(String name) {
      return (Variable)this.declaredVariables.get(name);
   }

   public boolean isReferencedLocalVariable(String name) {
      return this.referencedLocalVariables.containsKey(name);
   }

   public boolean isReferencedClassVariable(String name) {
      return this.referencedClassVariables.containsKey(name);
   }

   public VariableScope getParent() {
      return this.parent;
   }

   public boolean isInStaticContext() {
      return this.inStaticContext;
   }

   public void setInStaticContext(boolean inStaticContext) {
      this.inStaticContext = inStaticContext;
   }

   /** @deprecated */
   @Deprecated
   public boolean isResolvingDynamic() {
      return this.resolvesDynamic;
   }

   /** @deprecated */
   @Deprecated
   public void setDynamicResolving(boolean resolvesDynamic) {
      this.resolvesDynamic = resolvesDynamic;
   }

   public void setClassScope(ClassNode node) {
      this.clazzScope = node;
   }

   public ClassNode getClassScope() {
      return this.clazzScope;
   }

   public boolean isClassScope() {
      return this.clazzScope != null;
   }

   public boolean isRoot() {
      return this.parent == null;
   }

   public VariableScope copy() {
      VariableScope copy = new VariableScope();
      copy.clazzScope = this.clazzScope;
      if (this.declaredVariables.size() > 0) {
         copy.declaredVariables = new HashMap();
         copy.declaredVariables.putAll(this.declaredVariables);
      }

      copy.inStaticContext = this.inStaticContext;
      copy.parent = this.parent;
      if (this.referencedClassVariables.size() > 0) {
         copy.referencedClassVariables = new HashMap();
         copy.referencedClassVariables.putAll(this.referencedClassVariables);
      }

      if (this.referencedLocalVariables.size() > 0) {
         copy.referencedLocalVariables = new HashMap();
         copy.referencedLocalVariables.putAll(this.referencedLocalVariables);
      }

      copy.resolvesDynamic = this.resolvesDynamic;
      return copy;
   }

   public void putDeclaredVariable(Variable var) {
      if (this.declaredVariables == Collections.EMPTY_MAP) {
         this.declaredVariables = new HashMap();
      }

      this.declaredVariables.put(var.getName(), var);
   }

   public Iterator<Variable> getReferencedLocalVariablesIterator() {
      return this.referencedLocalVariables.values().iterator();
   }

   public int getReferencedLocalVariablesCount() {
      return this.referencedLocalVariables.size();
   }

   public Variable getReferencedLocalVariable(String name) {
      return (Variable)this.referencedLocalVariables.get(name);
   }

   public void putReferencedLocalVariable(Variable var) {
      if (this.referencedLocalVariables == Collections.EMPTY_MAP) {
         this.referencedLocalVariables = new HashMap();
      }

      this.referencedLocalVariables.put(var.getName(), var);
   }

   public void putReferencedClassVariable(Variable var) {
      if (this.referencedClassVariables == Collections.EMPTY_MAP) {
         this.referencedClassVariables = new HashMap();
      }

      this.referencedClassVariables.put(var.getName(), var);
   }

   public Variable getReferencedClassVariable(String name) {
      return (Variable)this.referencedClassVariables.get(name);
   }

   public Object removeReferencedClassVariable(String name) {
      return this.referencedClassVariables == Collections.EMPTY_MAP ? null : this.referencedClassVariables.remove(name);
   }

   public Map<String, Variable> getReferencedClassVariables() {
      return this.referencedClassVariables == Collections.EMPTY_MAP ? this.referencedClassVariables : Collections.unmodifiableMap(this.referencedClassVariables);
   }

   public Iterator<Variable> getReferencedClassVariablesIterator() {
      return this.getReferencedClassVariables().values().iterator();
   }
}
