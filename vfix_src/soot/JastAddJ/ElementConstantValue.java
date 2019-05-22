package soot.JastAddJ;

import java.util.Collection;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;

public class ElementConstantValue extends ElementValue implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ElementConstantValue clone() throws CloneNotSupportedException {
      ElementConstantValue node = (ElementConstantValue)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ElementConstantValue copy() {
      try {
         ElementConstantValue node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ElementConstantValue fullCopy() {
      ElementConstantValue tree = this.copy();
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
      if (this.enclosingAnnotationDecl().fullName().equals("java.lang.annotation.Target")) {
         Variable v = this.getExpr().varDecl();
         if (v != null && v.hostType().fullName().equals("java.lang.annotation.ElementType") && this.lookupElementTypeValue(v.name()) != this) {
            this.error("repeated annotation target");
         }
      }

   }

   public void toString(StringBuffer s) {
      this.getExpr().toString(s);
   }

   public void appendAsAttributeTo(Collection list, String name) {
      if (this.getExpr().isConstant() && !this.getExpr().type().isEnumDecl()) {
         char kind = this.getExpr().type().isString() ? 115 : this.getExpr().type().typeDescriptor().charAt(0);
         TypeDecl type = this.getExpr().type();
         if (type.isLong()) {
            list.add(new AnnotationLongElem(this.getExpr().constant().longValue(), kind, name));
         } else if (type.isDouble()) {
            list.add(new AnnotationDoubleElem(this.getExpr().constant().doubleValue(), kind, name));
         } else if (type.isFloat()) {
            list.add(new AnnotationFloatElem(this.getExpr().constant().floatValue(), kind, name));
         } else if (type.isString()) {
            list.add(new AnnotationStringElem(this.getExpr().constant().stringValue(), kind, name));
         } else if (type.isIntegralType()) {
            list.add(new AnnotationIntElem(this.getExpr().constant().intValue(), kind, name));
         } else {
            if (!this.type().isBoolean()) {
               throw new UnsupportedOperationException("Unsupported attribute constant type " + type.typeName());
            }

            list.add(new AnnotationBooleanElem(this.getExpr().constant().booleanValue(), kind, name));
         }
      } else if (this.getExpr().isClassAccess()) {
         list.add(new AnnotationClassElem(this.getExpr().type().typeDescriptor(), 'c', name));
      } else {
         Variable v = this.getExpr().varDecl();
         if (v == null) {
            throw new Error("Expected Enumeration constant");
         }

         list.add(new AnnotationEnumElem(v.type().typeDescriptor(), v.name(), 'e', name));
      }

   }

   public ElementConstantValue() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ElementConstantValue(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setExpr(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(0);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public boolean validTarget(Annotation a) {
      ASTNode$State state = this.state();
      Variable v = this.getExpr().varDecl();
      if (v == null) {
         return true;
      } else {
         return v.hostType().fullName().equals("java.lang.annotation.ElementType") && a.mayUseAnnotationTarget(v.name());
      }
   }

   public ElementValue definesElementTypeValue(String name) {
      ASTNode$State state = this.state();
      Variable v = this.getExpr().varDecl();
      return v != null && v.hostType().fullName().equals("java.lang.annotation.ElementType") && v.name().equals(name) ? this : null;
   }

   public boolean hasValue(String s) {
      ASTNode$State state = this.state();
      return this.getExpr().type().isString() && this.getExpr().isConstant() && this.getExpr().constant().stringValue().equals(s);
   }

   public boolean commensurateWithTypeDecl(TypeDecl type) {
      ASTNode$State state = this.state();
      Expr v = this.getExpr();
      if (!v.type().assignConversionTo(type, v)) {
         return false;
      } else if ((type.isPrimitive() || type.isString()) && !v.isConstant()) {
         return false;
      } else if (v.type().isNull()) {
         return false;
      } else if (type.fullName().equals("java.lang.Class") && !v.isClassAccess()) {
         return false;
      } else {
         return !type.isEnumDecl() || v.varDecl() != null && v.varDecl() instanceof EnumConstant;
      }
   }

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.getExpr().type();
   }

   public ElementValue lookupElementTypeValue(String name) {
      ASTNode$State state = this.state();
      ElementValue lookupElementTypeValue_String_value = this.getParent().Define_ElementValue_lookupElementTypeValue(this, (ASTNode)null, name);
      return lookupElementTypeValue_String_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getExprNoTransform() ? NameType.AMBIGUOUS_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public String Define_String_methodHost(ASTNode caller, ASTNode child) {
      return caller == this.getExprNoTransform() ? this.enclosingAnnotationDecl().typeName() : this.getParent().Define_String_methodHost(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
