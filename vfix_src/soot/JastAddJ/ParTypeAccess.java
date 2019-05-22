package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ParTypeAccess extends Access implements Cloneable {
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

   public ParTypeAccess clone() throws CloneNotSupportedException {
      ParTypeAccess node = (ParTypeAccess)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParTypeAccess copy() {
      try {
         ParTypeAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParTypeAccess fullCopy() {
      ParTypeAccess tree = this.copy();
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

   public boolean isRaw() {
      return false;
   }

   public void typeCheck() {
      super.typeCheck();
      if (!this.genericDecl().isUnknown()) {
         TypeDecl type = this.type();
         if (!this.genericDecl().isGenericType()) {
            this.error(this.genericDecl().typeName() + " is not a generic type but used as one in " + this);
         } else if (!type.isRawType() && type.isNestedType() && type.enclosingType().isRawType()) {
            this.error("Can not access a member type of a raw type as a parameterized type");
         } else {
            GenericTypeDecl decl = (GenericTypeDecl)this.genericDecl();
            GenericTypeDecl original = (GenericTypeDecl)decl.original();
            if (original.getNumTypeParameter() != this.getNumTypeArgument()) {
               this.error(decl.typeName() + " takes " + original.getNumTypeParameter() + " type parameters, not " + this.getNumTypeArgument() + " as used in " + this);
            } else {
               ParTypeDecl typeDecl = (ParTypeDecl)this.type();

               for(int i = 0; i < this.getNumTypeArgument(); ++i) {
                  if (!this.getTypeArgument(i).type().instanceOf(original.getTypeParameter(i))) {
                     this.error("type argument " + i + " is of type " + this.getTypeArgument(i).type().typeName() + " which is not a subtype of " + original.getTypeParameter(i).typeName());
                  }
               }
            }
         }
      }

   }

   public void toString(StringBuffer s) {
      this.getTypeAccess().toString(s);
      s.append("<");

      for(int i = 0; i < this.getNumTypeArgument(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         this.getTypeArgument(i).toString(s);
      }

      s.append(">");
   }

   public ParTypeAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 1);
   }

   public ParTypeAccess(Access p0, List<Access> p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 0);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(0);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(0);
   }

   public void setTypeArgumentList(List<Access> list) {
      this.setChild(list, 1);
   }

   public int getNumTypeArgument() {
      return this.getTypeArgumentList().getNumChild();
   }

   public int getNumTypeArgumentNoTransform() {
      return this.getTypeArgumentListNoTransform().getNumChildNoTransform();
   }

   public Access getTypeArgument(int i) {
      return (Access)this.getTypeArgumentList().getChild(i);
   }

   public void addTypeArgument(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getTypeArgumentList() : this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void addTypeArgumentNoTransform(Access node) {
      List<Access> list = this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void setTypeArgument(Access node, int i) {
      List<Access> list = this.getTypeArgumentList();
      list.setChild(node, i);
   }

   public List<Access> getTypeArguments() {
      return this.getTypeArgumentList();
   }

   public List<Access> getTypeArgumentsNoTransform() {
      return this.getTypeArgumentListNoTransform();
   }

   public List<Access> getTypeArgumentList() {
      List<Access> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeArgumentListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public Expr unqualifiedScope() {
      ASTNode$State state = this.state();
      return this.getParent() instanceof Access ? ((Access)this.getParent()).unqualifiedScope() : super.unqualifiedScope();
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
      TypeDecl typeDecl = this.genericDecl();
      if (!(typeDecl instanceof GenericTypeDecl)) {
         return typeDecl;
      } else if (this.unqualifiedScope().inExtendsOrImplements()) {
         return ((GenericTypeDecl)typeDecl).lookupParTypeDecl(this);
      } else {
         ArrayList args = new ArrayList();

         for(int i = 0; i < this.getNumTypeArgument(); ++i) {
            args.add(this.getTypeArgument(i).type());
         }

         return ((GenericTypeDecl)typeDecl).lookupParTypeDecl(args);
      }
   }

   public TypeDecl genericDecl() {
      ASTNode$State state = this.state();
      return this.getTypeAccess().type();
   }

   public boolean isTypeAccess() {
      ASTNode$State state = this.state();
      return true;
   }

   public Access substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
      ASTNode$State state = this.state();
      List<Access> substArgs = new List();
      Iterator var5 = this.getTypeArgumentList().iterator();

      while(var5.hasNext()) {
         Access arg = (Access)var5.next();
         substArgs.add(arg.substituted(original, substitution));
      }

      return new ParTypeAccess(this.getTypeAccess().substituted(original, substitution), substArgs);
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getTypeArgumentListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().lookupType(name);
      } else {
         return this.getParent().Define_SimpleSet_lookupType(this, caller, name);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
