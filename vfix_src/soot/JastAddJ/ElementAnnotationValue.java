package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationTag;

public class ElementAnnotationValue extends ElementValue implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ElementAnnotationValue clone() throws CloneNotSupportedException {
      ElementAnnotationValue node = (ElementAnnotationValue)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ElementAnnotationValue copy() {
      try {
         ElementAnnotationValue node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ElementAnnotationValue fullCopy() {
      ElementAnnotationValue tree = this.copy();
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

   public void toString(StringBuffer s) {
      this.getAnnotation().toString(s);
   }

   public void appendAsAttributeTo(Collection list, String name) {
      ArrayList elemVals = new ArrayList();
      this.getAnnotation().appendAsAttributeTo(elemVals);
      list.add(new AnnotationAnnotationElem((AnnotationTag)elemVals.get(0), '@', name));
   }

   public ElementAnnotationValue() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ElementAnnotationValue(Annotation p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setAnnotation(Annotation node) {
      this.setChild(node, 0);
   }

   public Annotation getAnnotation() {
      return (Annotation)this.getChild(0);
   }

   public Annotation getAnnotationNoTransform() {
      return (Annotation)this.getChildNoTransform(0);
   }

   public boolean commensurateWithTypeDecl(TypeDecl type) {
      ASTNode$State state = this.state();
      return this.type() == type;
   }

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.getAnnotation().type();
   }

   public Annotation lookupAnnotation(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      Annotation lookupAnnotation_TypeDecl_value = this.getParent().Define_Annotation_lookupAnnotation(this, (ASTNode)null, typeDecl);
      return lookupAnnotation_TypeDecl_value;
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return caller == this.getAnnotationNoTransform() ? true : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      if (caller == this.getAnnotationNoTransform()) {
         return this.getAnnotation().type() == typeDecl ? this.getAnnotation() : this.lookupAnnotation(typeDecl);
      } else {
         return this.getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
