package soot.JastAddJ;

import java.util.ArrayList;
import soot.Local;
import soot.Value;
import soot.jimple.NullConstant;

public class ClassAccess extends Access implements Cloneable {
   protected boolean type_computed = false;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ClassAccess clone() throws CloneNotSupportedException {
      ClassAccess node = (ClassAccess)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ClassAccess copy() {
      try {
         ClassAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ClassAccess fullCopy() {
      ClassAccess tree = this.copy();
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

   public void nameCheck() {
      if (this.isQualified() && !this.qualifier().isTypeAccess()) {
         this.error("class literal may only contain type names");
      }

   }

   public void toString(StringBuffer s) {
      s.append("class");
   }

   public void transformation() {
      super.transformation();
      if (this.isQualified() && this.qualifier().type().isReferenceType()) {
         this.hostType().topLevelType().createStaticClassMethod();
         FieldDeclaration var1 = this.hostType().topLevelType().createStaticClassField(this.prevExpr().type().referenceClassFieldName());
      }

   }

   public Value eval(Body b) {
      if (!this.prevExpr().type().isPrimitiveType() && !this.prevExpr().type().isVoid()) {
         FieldDeclaration f = this.hostType().topLevelType().createStaticClassField(this.prevExpr().type().referenceClassFieldName());
         MethodDecl m = this.hostType().topLevelType().createStaticClassMethod();
         soot.jimple.Stmt next_label = b.newLabel();
         soot.jimple.Stmt end_label = b.newLabel();
         Local result = b.newTemp(this.type().getSootType());
         Local ref = this.asLocal(b, b.newStaticFieldRef(f.sootRef(), this));
         b.setLine(this);
         b.add(b.newIfStmt(b.newNeExpr(ref, NullConstant.v(), this), next_label, this));
         ArrayList list = new ArrayList();
         list.add((new StringLiteral(this.prevExpr().type().jvmName())).eval(b));
         Local l = this.asLocal(b, b.newStaticInvokeExpr(m.sootRef(), (java.util.List)list, this));
         b.setLine(this);
         b.add(b.newAssignStmt(b.newStaticFieldRef(f.sootRef(), this), l, this));
         b.setLine(this);
         b.add(b.newAssignStmt(result, l, this));
         b.add(b.newGotoStmt(end_label, this));
         b.addLabel(next_label);
         b.add(b.newAssignStmt(result, b.newStaticFieldRef(f.sootRef(), this), this));
         b.addLabel(end_label);
         return result;
      } else {
         TypeDecl typeDecl = this.lookupType("java.lang", this.prevExpr().type().primitiveClassName());
         SimpleSet c = typeDecl.memberFields("TYPE");
         FieldDeclaration f = (FieldDeclaration)c.iterator().next();
         return b.newStaticFieldRef(f.sootRef(), this);
      }
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   private TypeDecl refined_TypeAnalysis_ClassAccess_type() {
      return this.lookupType("java.lang", "Class");
   }

   public boolean isClassAccess() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.TYPE_NAME;
   }

   public TypeDecl type() {
      if (this.type_computed) {
         return this.type_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.type_value = this.type_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.type_computed = true;
         }

         return this.type_value;
      }
   }

   private TypeDecl type_compute() {
      GenericClassDecl d = (GenericClassDecl)this.refined_TypeAnalysis_ClassAccess_type();
      TypeDecl type = this.qualifier().type();
      if (type.isPrimitiveType()) {
         type = type.boxed();
      }

      ArrayList list = new ArrayList();
      list.add(type);
      return d.lookupParTypeDecl(list);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
