package soot.JastAddJ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TryWithResources extends TryStmt implements Cloneable, VariableScope {
   protected Map localLookup_String_values;
   protected Map localVariableDeclaration_String_values;
   protected Map isDAafter_Variable_values;
   protected Map catchableException_TypeDecl_values;
   protected Map handlesException_TypeDecl_values;
   protected boolean typeError_computed = false;
   protected TypeDecl typeError_value;
   protected boolean typeRuntimeException_computed = false;
   protected TypeDecl typeRuntimeException_value;
   protected Map lookupVariable_String_values;

   public void flushCache() {
      super.flushCache();
      this.localLookup_String_values = null;
      this.localVariableDeclaration_String_values = null;
      this.isDAafter_Variable_values = null;
      this.catchableException_TypeDecl_values = null;
      this.handlesException_TypeDecl_values = null;
      this.typeError_computed = false;
      this.typeError_value = null;
      this.typeRuntimeException_computed = false;
      this.typeRuntimeException_value = null;
      this.lookupVariable_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public TryWithResources clone() throws CloneNotSupportedException {
      TryWithResources node = (TryWithResources)super.clone();
      node.localLookup_String_values = null;
      node.localVariableDeclaration_String_values = null;
      node.isDAafter_Variable_values = null;
      node.catchableException_TypeDecl_values = null;
      node.handlesException_TypeDecl_values = null;
      node.typeError_computed = false;
      node.typeError_value = null;
      node.typeRuntimeException_computed = false;
      node.typeRuntimeException_value = null;
      node.lookupVariable_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public TryWithResources copy() {
      try {
         TryWithResources node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public TryWithResources fullCopy() {
      TryWithResources tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            ASTNode child = this.children[i];
            if (child != null) {
               child = child.fullCopy();
               tree.setChild(child, i);
            }
         }
      }

      return tree;
   }

   public void exceptionHandling() {
      Iterator var1 = this.getResourceList().iterator();

      while(true) {
         ResourceDeclaration resource;
         MethodDecl close;
         do {
            if (!var1.hasNext()) {
               return;
            }

            resource = (ResourceDeclaration)var1.next();
            close = this.lookupClose(resource);
         } while(close == null);

         Iterator var4 = close.getExceptionList().iterator();

         while(var4.hasNext()) {
            Access exception = (Access)var4.next();
            TypeDecl exceptionType = exception.type();
            if (!this.twrHandlesException(exceptionType)) {
               this.error("automatic closing of resource " + resource.name() + " may raise the uncaught exception " + exceptionType.fullName() + "; it must be caught or declared as being thrown");
            }
         }
      }
   }

   protected boolean reachedException(TypeDecl catchType) {
      boolean found = false;

      int i;
      for(i = 0; i < this.getNumCatchClause() && !found; ++i) {
         if (this.getCatchClause(i).handles(catchType)) {
            found = true;
         }
      }

      if (!found && (!this.hasFinally() || this.getFinally().canCompleteNormally()) && this.catchableException(catchType)) {
         return true;
      } else {
         for(i = 0; i < this.getNumCatchClause(); ++i) {
            if (this.getCatchClause(i).reachedException(catchType)) {
               return true;
            }
         }

         return this.hasFinally() && this.getFinally().reachedException(catchType);
      }
   }

   public void toString(StringBuffer sb) {
      sb.append(this.indent() + "try (");
      Iterator var2 = this.getResourceList().iterator();

      while(var2.hasNext()) {
         ResourceDeclaration resource = (ResourceDeclaration)var2.next();
         sb.append(resource.toString());
      }

      sb.append(") ");
      this.getBlock().toString(sb);
      var2 = this.getCatchClauseList().iterator();

      while(var2.hasNext()) {
         CatchClause cc = (CatchClause)var2.next();
         sb.append(" ");
         cc.toString(sb);
      }

      if (this.hasFinally()) {
         sb.append(" finally ");
         this.getFinally().toString(sb);
      }

   }

   public TryWithResources() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new List(), 0);
      this.setChild(new List(), 2);
      this.setChild(new Opt(), 3);
   }

   public TryWithResources(List<ResourceDeclaration> p0, Block p1, List<CatchClause> p2, Opt<Block> p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
      this.setChild(p3, 3);
   }

   protected int numChildren() {
      return 4;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setResourceList(List<ResourceDeclaration> list) {
      this.setChild(list, 0);
   }

   public int getNumResource() {
      return this.getResourceList().getNumChild();
   }

   public int getNumResourceNoTransform() {
      return this.getResourceListNoTransform().getNumChildNoTransform();
   }

   public ResourceDeclaration getResource(int i) {
      return (ResourceDeclaration)this.getResourceList().getChild(i);
   }

   public void addResource(ResourceDeclaration node) {
      List<ResourceDeclaration> list = this.parent != null && state != null ? this.getResourceList() : this.getResourceListNoTransform();
      list.addChild(node);
   }

   public void addResourceNoTransform(ResourceDeclaration node) {
      List<ResourceDeclaration> list = this.getResourceListNoTransform();
      list.addChild(node);
   }

   public void setResource(ResourceDeclaration node, int i) {
      List<ResourceDeclaration> list = this.getResourceList();
      list.setChild(node, i);
   }

   public List<ResourceDeclaration> getResources() {
      return this.getResourceList();
   }

   public List<ResourceDeclaration> getResourcesNoTransform() {
      return this.getResourceListNoTransform();
   }

   public List<ResourceDeclaration> getResourceList() {
      List<ResourceDeclaration> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<ResourceDeclaration> getResourceListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public void setBlock(Block node) {
      this.setChild(node, 1);
   }

   public Block getBlock() {
      return (Block)this.getChild(1);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(1);
   }

   public void setCatchClauseList(List<CatchClause> list) {
      this.setChild(list, 2);
   }

   public int getNumCatchClause() {
      return this.getCatchClauseList().getNumChild();
   }

   public int getNumCatchClauseNoTransform() {
      return this.getCatchClauseListNoTransform().getNumChildNoTransform();
   }

   public CatchClause getCatchClause(int i) {
      return (CatchClause)this.getCatchClauseList().getChild(i);
   }

   public void addCatchClause(CatchClause node) {
      List<CatchClause> list = this.parent != null && state != null ? this.getCatchClauseList() : this.getCatchClauseListNoTransform();
      list.addChild(node);
   }

   public void addCatchClauseNoTransform(CatchClause node) {
      List<CatchClause> list = this.getCatchClauseListNoTransform();
      list.addChild(node);
   }

   public void setCatchClause(CatchClause node, int i) {
      List<CatchClause> list = this.getCatchClauseList();
      list.setChild(node, i);
   }

   public List<CatchClause> getCatchClauses() {
      return this.getCatchClauseList();
   }

   public List<CatchClause> getCatchClausesNoTransform() {
      return this.getCatchClauseListNoTransform();
   }

   public List<CatchClause> getCatchClauseList() {
      List<CatchClause> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<CatchClause> getCatchClauseListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setFinallyOpt(Opt<Block> opt) {
      this.setChild(opt, 3);
   }

   public boolean hasFinally() {
      return this.getFinallyOpt().getNumChild() != 0;
   }

   public Block getFinally() {
      return (Block)this.getFinallyOpt().getChild(0);
   }

   public void setFinally(Block node) {
      this.getFinallyOpt().setChild(node, 0);
   }

   public Opt<Block> getFinallyOpt() {
      return (Opt)this.getChild(3);
   }

   public Opt<Block> getFinallyOptNoTransform() {
      return (Opt)this.getChildNoTransform(3);
   }

   public boolean catchHandlesException(TypeDecl exceptionType) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumCatchClause(); ++i) {
         if (this.getCatchClause(i).handles(exceptionType)) {
            return true;
         }
      }

      return false;
   }

   public boolean twrHandlesException(TypeDecl exceptionType) {
      ASTNode$State state = this.state();
      if (this.catchHandlesException(exceptionType)) {
         return true;
      } else {
         return this.hasFinally() && !this.getFinally().canCompleteNormally() ? true : this.handlesException(exceptionType);
      }
   }

   public MethodDecl lookupClose(ResourceDeclaration resource) {
      ASTNode$State state = this.state();
      TypeDecl resourceType = resource.getTypeAccess().type();
      Iterator var4 = resourceType.memberMethods("close").iterator();

      MethodDecl method;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         method = (MethodDecl)var4.next();
      } while(method.getNumParameter() != 0);

      return method;
   }

   public SimpleSet localLookup(String name) {
      if (this.localLookup_String_values == null) {
         this.localLookup_String_values = new HashMap(4);
      }

      if (this.localLookup_String_values.containsKey(name)) {
         return (SimpleSet)this.localLookup_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet localLookup_String_value = this.localLookup_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localLookup_String_values.put(name, localLookup_String_value);
         }

         return localLookup_String_value;
      }
   }

   private SimpleSet localLookup_compute(String name) {
      VariableDeclaration v = this.localVariableDeclaration(name);
      return (SimpleSet)(v != null ? v : this.lookupVariable(name));
   }

   public VariableDeclaration localVariableDeclaration(String name) {
      if (this.localVariableDeclaration_String_values == null) {
         this.localVariableDeclaration_String_values = new HashMap(4);
      }

      if (this.localVariableDeclaration_String_values.containsKey(name)) {
         return (VariableDeclaration)this.localVariableDeclaration_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         VariableDeclaration localVariableDeclaration_String_value = this.localVariableDeclaration_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localVariableDeclaration_String_values.put(name, localVariableDeclaration_String_value);
         }

         return localVariableDeclaration_String_value;
      }
   }

   private VariableDeclaration localVariableDeclaration_compute(String name) {
      Iterator var2 = this.getResourceList().iterator();

      ResourceDeclaration resource;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         resource = (ResourceDeclaration)var2.next();
      } while(!resource.declaresVariable(name));

      return resource;
   }

   public boolean isDAafter(Variable v) {
      if (this.isDAafter_Variable_values == null) {
         this.isDAafter_Variable_values = new HashMap(4);
      }

      if (this.isDAafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafter_Variable_value = this.isDAafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, isDAafter_Variable_value);
         }

         return isDAafter_Variable_value;
      }
   }

   private boolean isDAafter_compute(Variable v) {
      return this.getBlock().isDAafter(v);
   }

   public boolean resourceClosingException(TypeDecl catchType) {
      ASTNode$State state = this.state();
      Iterator var3 = this.getResourceList().iterator();

      while(true) {
         MethodDecl close;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            ResourceDeclaration resource = (ResourceDeclaration)var3.next();
            close = this.lookupClose(resource);
         } while(close == null);

         Iterator var6 = close.getExceptionList().iterator();

         while(var6.hasNext()) {
            Access exception = (Access)var6.next();
            TypeDecl exceptionType = exception.type();
            if (catchType.mayCatch(exception.type())) {
               return true;
            }
         }
      }
   }

   public boolean resourceInitializationException(TypeDecl catchType) {
      ASTNode$State state = this.state();
      Iterator var3 = this.getResourceList().iterator();

      ResourceDeclaration resource;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         resource = (ResourceDeclaration)var3.next();
      } while(!resource.reachedException(catchType));

      return true;
   }

   public boolean catchableException(TypeDecl type) {
      if (this.catchableException_TypeDecl_values == null) {
         this.catchableException_TypeDecl_values = new HashMap(4);
      }

      if (this.catchableException_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.catchableException_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean catchableException_TypeDecl_value = this.catchableException_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.catchableException_TypeDecl_values.put(type, catchableException_TypeDecl_value);
         }

         return catchableException_TypeDecl_value;
      }
   }

   private boolean catchableException_compute(TypeDecl type) {
      return this.getBlock().reachedException(type) || this.resourceClosingException(type) || this.resourceInitializationException(type);
   }

   public boolean handlesException(TypeDecl exceptionType) {
      if (this.handlesException_TypeDecl_values == null) {
         this.handlesException_TypeDecl_values = new HashMap(4);
      }

      if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
         return (Boolean)this.handlesException_TypeDecl_values.get(exceptionType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, handlesException_TypeDecl_value);
         }

         return handlesException_TypeDecl_value;
      }
   }

   public TypeDecl typeError() {
      if (this.typeError_computed) {
         return this.typeError_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeError_value = this.getParent().Define_TypeDecl_typeError(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeError_computed = true;
         }

         return this.typeError_value;
      }
   }

   public TypeDecl typeRuntimeException() {
      if (this.typeRuntimeException_computed) {
         return this.typeRuntimeException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeRuntimeException_value = this.getParent().Define_TypeDecl_typeRuntimeException(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeRuntimeException_computed = true;
         }

         return this.typeRuntimeException_value;
      }
   }

   public SimpleSet lookupVariable(String name) {
      if (this.lookupVariable_String_values == null) {
         this.lookupVariable_String_values = new HashMap(4);
      }

      if (this.lookupVariable_String_values.containsKey(name)) {
         return (SimpleSet)this.lookupVariable_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupVariable_String_values.put(name, lookupVariable_String_value);
         }

         return lookupVariable_String_value;
      }
   }

   public boolean resourcePreviouslyDeclared(String name) {
      ASTNode$State state = this.state();
      boolean resourcePreviouslyDeclared_String_value = this.getParent().Define_boolean_resourcePreviouslyDeclared(this, (ASTNode)null, name);
      return resourcePreviouslyDeclared_String_value;
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getBlockNoTransform()) {
         return this.twrHandlesException(exceptionType);
      } else if (caller == this.getResourceListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.twrHandlesException(exceptionType);
      } else {
         return super.Define_boolean_handlesException(caller, child, exceptionType);
      }
   }

   public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getCatchClauseListNoTransform()) {
         int childIndex = caller.getIndexOfChild(child);

         for(int i = 0; i < childIndex; ++i) {
            if (this.getCatchClause(i).handles(exceptionType)) {
               return false;
            }
         }

         if (this.catchableException(exceptionType)) {
            return true;
         } else if (!exceptionType.mayCatch(this.typeError()) && !exceptionType.mayCatch(this.typeRuntimeException())) {
            return false;
         } else {
            return true;
         }
      } else {
         return super.Define_boolean_reachableCatchClause(caller, child, exceptionType);
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      return caller == this.getBlockNoTransform() ? this.localLookup(name) : this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      if (caller == this.getResourceListNoTransform()) {
         caller.getIndexOfChild(child);
         return this;
      } else {
         return this.getParent().Define_VariableScope_outerScope(this, caller);
      }
   }

   public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getResourceListNoTransform()) {
         int index = caller.getIndexOfChild(child);

         for(int i = 0; i < index; ++i) {
            if (this.getResource(i).name().equals(name)) {
               return true;
            }
         }

         return false;
      } else {
         return this.getParent().Define_boolean_resourcePreviouslyDeclared(this, caller, name);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getBlockNoTransform()) {
         return this.getNumResource() == 0 ? this.isDAbefore(v) : this.getResource(this.getNumResource() - 1).isDAafter(v);
      } else if (caller == this.getResourceListNoTransform()) {
         int index = caller.getIndexOfChild(child);
         return index == 0 ? this.isDAbefore(v) : this.getResource(index - 1).isDAafter(v);
      } else {
         return super.Define_boolean_isDAbefore(caller, child, v);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
