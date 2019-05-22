package soot.JastAddJ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Block extends Stmt implements Cloneable, VariableScope {
   protected Map checkReturnDA_Variable_values;
   protected Map isDAafter_Variable_values;
   protected Map checkReturnDU_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map localVariableDeclaration_String_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected Map lookupType_String_values;
   protected Map lookupVariable_String_values;

   public void flushCache() {
      super.flushCache();
      this.checkReturnDA_Variable_values = null;
      this.isDAafter_Variable_values = null;
      this.checkReturnDU_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.localVariableDeclaration_String_values = null;
      this.canCompleteNormally_computed = false;
      this.lookupType_String_values = null;
      this.lookupVariable_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Block clone() throws CloneNotSupportedException {
      Block node = (Block)super.clone();
      node.checkReturnDA_Variable_values = null;
      node.isDAafter_Variable_values = null;
      node.checkReturnDU_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.localVariableDeclaration_String_values = null;
      node.canCompleteNormally_computed = false;
      node.lookupType_String_values = null;
      node.lookupVariable_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Block copy() {
      try {
         Block node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public Block fullCopy() {
      Block tree = this.copy();
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

   public boolean declaredBeforeUse(Variable decl, ASTNode use) {
      int indexDecl = ((ASTNode)decl).varChildIndex(this);
      int indexUse = use.varChildIndex(this);
      return indexDecl <= indexUse;
   }

   public boolean declaredBeforeUse(Variable decl, int indexUse) {
      int indexDecl = ((ASTNode)decl).varChildIndex(this);
      return indexDecl <= indexUse;
   }

   public void toString(StringBuffer s) {
      String indent = this.indent();
      s.append(this.shouldHaveIndent() ? indent : "");
      s.append("{");

      for(int i = 0; i < this.getNumStmt(); ++i) {
         this.getStmt(i).toString(s);
      }

      s.append(this.shouldHaveIndent() ? indent : indent.substring(0, indent.length() - 2));
      s.append("}");
   }

   public void jimplify2(Body b) {
      for(int i = 0; i < this.getNumStmt(); ++i) {
         this.getStmt(i).jimplify2(b);
      }

   }

   public Block() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public Block(List<Stmt> p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setStmtList(List<Stmt> list) {
      this.setChild(list, 0);
   }

   public int getNumStmt() {
      return this.getStmtList().getNumChild();
   }

   public int getNumStmtNoTransform() {
      return this.getStmtListNoTransform().getNumChildNoTransform();
   }

   public Stmt getStmt(int i) {
      return (Stmt)this.getStmtList().getChild(i);
   }

   public void addStmt(Stmt node) {
      List<Stmt> list = this.parent != null && state != null ? this.getStmtList() : this.getStmtListNoTransform();
      list.addChild(node);
   }

   public void addStmtNoTransform(Stmt node) {
      List<Stmt> list = this.getStmtListNoTransform();
      list.addChild(node);
   }

   public void setStmt(Stmt node, int i) {
      List<Stmt> list = this.getStmtList();
      list.setChild(node, i);
   }

   public List<Stmt> getStmts() {
      return this.getStmtList();
   }

   public List<Stmt> getStmtsNoTransform() {
      return this.getStmtListNoTransform();
   }

   public List<Stmt> getStmtList() {
      List<Stmt> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<Stmt> getStmtListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public boolean checkReturnDA(Variable v) {
      if (this.checkReturnDA_Variable_values == null) {
         this.checkReturnDA_Variable_values = new HashMap(4);
      }

      if (this.checkReturnDA_Variable_values.containsKey(v)) {
         return (Boolean)this.checkReturnDA_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean checkReturnDA_Variable_value = this.checkReturnDA_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.checkReturnDA_Variable_values.put(v, checkReturnDA_Variable_value);
         }

         return checkReturnDA_Variable_value;
      }
   }

   private boolean checkReturnDA_compute(Variable v) {
      HashSet set = new HashSet();
      this.collectBranches(set);
      Iterator iter = set.iterator();

      while(iter.hasNext()) {
         Object o = iter.next();
         if (o instanceof ReturnStmt) {
            ReturnStmt stmt = (ReturnStmt)o;
            if (!stmt.isDAafterReachedFinallyBlocks(v)) {
               return false;
            }
         }
      }

      return true;
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
      return this.getNumStmt() == 0 ? this.isDAbefore(v) : this.getStmt(this.getNumStmt() - 1).isDAafter(v);
   }

   public boolean isDUeverywhere(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUbefore(v) && this.checkDUeverywhere(v);
   }

   public boolean checkReturnDU(Variable v) {
      if (this.checkReturnDU_Variable_values == null) {
         this.checkReturnDU_Variable_values = new HashMap(4);
      }

      if (this.checkReturnDU_Variable_values.containsKey(v)) {
         return (Boolean)this.checkReturnDU_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean checkReturnDU_Variable_value = this.checkReturnDU_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.checkReturnDU_Variable_values.put(v, checkReturnDU_Variable_value);
         }

         return checkReturnDU_Variable_value;
      }
   }

   private boolean checkReturnDU_compute(Variable v) {
      HashSet set = new HashSet();
      this.collectBranches(set);
      Iterator iter = set.iterator();

      while(iter.hasNext()) {
         Object o = iter.next();
         if (o instanceof ReturnStmt) {
            ReturnStmt stmt = (ReturnStmt)o;
            if (!stmt.isDUafterReachedFinallyBlocks(v)) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean isDUafter(Variable v) {
      if (this.isDUafter_Variable_values == null) {
         this.isDUafter_Variable_values = new HashMap(4);
      }

      if (this.isDUafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUafter_Variable_value = this.isDUafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, isDUafter_Variable_value);
         }

         return isDUafter_Variable_value;
      }
   }

   private boolean isDUafter_compute(Variable v) {
      return this.getNumStmt() == 0 ? this.isDUbefore(v) : this.getStmt(this.getNumStmt() - 1).isDUafter(v);
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
      for(int i = 0; i < this.getNumStmt(); ++i) {
         if (this.getStmt(i).declaresVariable(name)) {
            return (VariableDeclaration)this.getStmt(i);
         }
      }

      return null;
   }

   public boolean addsIndentationLevel() {
      ASTNode$State state = this.state();
      return this.shouldHaveIndent();
   }

   public boolean shouldHaveIndent() {
      ASTNode$State state = this.state();
      return this.getParent() instanceof List && this.getParent().getParent() instanceof Block;
   }

   public boolean canCompleteNormally() {
      if (this.canCompleteNormally_computed) {
         return this.canCompleteNormally_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.canCompleteNormally_value = this.canCompleteNormally_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.canCompleteNormally_computed = true;
         }

         return this.canCompleteNormally_value;
      }
   }

   private boolean canCompleteNormally_compute() {
      return this.getNumStmt() == 0 ? this.reachable() : this.getStmt(this.getNumStmt() - 1).canCompleteNormally();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      Iterator var3 = this.getStmtList().iterator();

      Stmt stmt;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         stmt = (Stmt)var3.next();
      } while(!stmt.modifiedInScope(var));

      return true;
   }

   public SimpleSet lookupType(String name) {
      if (this.lookupType_String_values == null) {
         this.lookupType_String_values = new HashMap(4);
      }

      if (this.lookupType_String_values.containsKey(name)) {
         return (SimpleSet)this.lookupType_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet lookupType_String_value = this.getParent().Define_SimpleSet_lookupType(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupType_String_values.put(name, lookupType_String_value);
         }

         return lookupType_String_value;
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

   public boolean reachable() {
      ASTNode$State state = this.state();
      boolean reachable_value = this.getParent().Define_boolean_reachable(this, (ASTNode)null);
      return reachable_value;
   }

   public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isIncOrDec(this, caller);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getStmtListNoTransform()) {
         int index = caller.getIndexOfChild(child);
         return index == 0 ? this.isDAbefore(v) : this.getStmt(index - 1).isDAafter(v);
      } else {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getStmtListNoTransform()) {
         int index = caller.getIndexOfChild(child);
         return index == 0 ? this.isDUbefore(v) : this.getStmt(index - 1).isDUafter(v);
      } else {
         return this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      if (caller != this.getStmtListNoTransform()) {
         return this.getParent().Define_SimpleSet_lookupType(this, caller, name);
      } else {
         int index = caller.getIndexOfChild(child);
         SimpleSet c = SimpleSet.emptySet;

         for(int i = index; i >= 0 && !(this.getStmt(i) instanceof Case); --i) {
            if (this.getStmt(i) instanceof LocalClassDeclStmt) {
               TypeDecl t = ((LocalClassDeclStmt)this.getStmt(i)).getClassDecl();
               if (t.name().equals(name)) {
                  c = c.add(t);
               }
            }
         }

         return !c.isEmpty() ? c : this.lookupType(name);
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getStmtListNoTransform()) {
         int index = caller.getIndexOfChild(child);
         VariableDeclaration v = this.localVariableDeclaration(name);
         return (SimpleSet)(v != null && this.declaredBeforeUse(v, index) ? v : this.lookupVariable(name));
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtListNoTransform()) {
         caller.getIndexOfChild(child);
         return this;
      } else {
         return this.getParent().Define_VariableScope_outerScope(this, caller);
      }
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.EXPRESSION_NAME;
      } else {
         return this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtListNoTransform()) {
         int childIndex = caller.getIndexOfChild(child);
         return childIndex == 0 ? this.reachable() : this.getStmt(childIndex - 1).canCompleteNormally();
      } else {
         return this.getParent().Define_boolean_reachable(this, caller);
      }
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtListNoTransform()) {
         int i = caller.getIndexOfChild(child);
         return i == 0 ? this.reachable() : this.getStmt(i - 1).reachable();
      } else {
         return this.getParent().Define_boolean_reportUnreachable(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
