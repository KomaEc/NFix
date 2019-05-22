package soot.JastAddJ;

import beaver.Symbol;
import java.util.Map;

public class ElementValuePair extends ASTNode<ASTNode> implements Cloneable {
   protected String tokenString_Name;
   public int Namestart;
   public int Nameend;
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

   public ElementValuePair clone() throws CloneNotSupportedException {
      ElementValuePair node = (ElementValuePair)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ElementValuePair copy() {
      try {
         ElementValuePair node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ElementValuePair fullCopy() {
      ElementValuePair tree = this.copy();
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

   public void typeCheck() {
      if (!this.type().commensurateWith(this.getElementValue())) {
         this.error("can not construct annotation with " + this.getName() + " = " + this.getElementValue().toString() + "; " + this.type().typeName() + " is not commensurate with " + this.getElementValue().type().typeName());
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.getName() + " = ");
      this.getElementValue().toString(s);
   }

   public ElementValuePair() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ElementValuePair(String p0, ElementValue p1) {
      this.setName(p0);
      this.setChild(p1, 0);
   }

   public ElementValuePair(Symbol p0, ElementValue p1) {
      this.setName(p0);
      this.setChild(p1, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return true;
   }

   public void setName(String value) {
      this.tokenString_Name = value;
   }

   public void setName(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setName is only valid for String lexemes");
      } else {
         this.tokenString_Name = (String)symbol.value;
         this.Namestart = symbol.getStart();
         this.Nameend = symbol.getEnd();
      }
   }

   public String getName() {
      return this.tokenString_Name != null ? this.tokenString_Name : "";
   }

   public void setElementValue(ElementValue node) {
      this.setChild(node, 0);
   }

   public ElementValue getElementValue() {
      return (ElementValue)this.getChild(0);
   }

   public ElementValue getElementValueNoTransform() {
      return (ElementValue)this.getChildNoTransform(0);
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
      Map methodMap = this.enclosingAnnotationDecl().localMethodsSignatureMap();
      MethodDecl method = (MethodDecl)methodMap.get(this.getName() + "()");
      return method != null ? method.type() : this.unknownType();
   }

   public TypeDecl unknownType() {
      ASTNode$State state = this.state();
      TypeDecl unknownType_value = this.getParent().Define_TypeDecl_unknownType(this, (ASTNode)null);
      return unknownType_value;
   }

   public TypeDecl enclosingAnnotationDecl() {
      ASTNode$State state = this.state();
      TypeDecl enclosingAnnotationDecl_value = this.getParent().Define_TypeDecl_enclosingAnnotationDecl(this, (ASTNode)null);
      return enclosingAnnotationDecl_value;
   }

   public ASTNode rewriteTo() {
      if (this.type().isArrayDecl() && this.getElementValue() instanceof ElementConstantValue) {
         ++this.state().duringAnnotations;
         ASTNode result = this.rewriteRule0();
         --this.state().duringAnnotations;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private ElementValuePair rewriteRule0() {
      this.setElementValue(new ElementArrayValue((new List()).add(this.getElementValue())));
      return this;
   }
}
