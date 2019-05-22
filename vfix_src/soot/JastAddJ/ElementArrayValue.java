package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import soot.tagkit.AnnotationArrayElem;

public class ElementArrayValue extends ElementValue implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ElementArrayValue clone() throws CloneNotSupportedException {
      ElementArrayValue node = (ElementArrayValue)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ElementArrayValue copy() {
      try {
         ElementArrayValue node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ElementArrayValue fullCopy() {
      ElementArrayValue tree = this.copy();
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
      s.append("{");

      for(int i = 0; i < this.getNumElementValue(); ++i) {
         this.getElementValue(i).toString(s);
         s.append(", ");
      }

      s.append("}");
   }

   public void appendAsAttributeTo(Collection list, String name) {
      ArrayList elemVals = new ArrayList();

      for(int i = 0; i < this.getNumElementValue(); ++i) {
         this.getElementValue(i).appendAsAttributeTo(elemVals, "default");
      }

      list.add(new AnnotationArrayElem(elemVals, '[', name));
   }

   public ElementArrayValue() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public ElementArrayValue(List<ElementValue> p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setElementValueList(List<ElementValue> list) {
      this.setChild(list, 0);
   }

   public int getNumElementValue() {
      return this.getElementValueList().getNumChild();
   }

   public int getNumElementValueNoTransform() {
      return this.getElementValueListNoTransform().getNumChildNoTransform();
   }

   public ElementValue getElementValue(int i) {
      return (ElementValue)this.getElementValueList().getChild(i);
   }

   public void addElementValue(ElementValue node) {
      List<ElementValue> list = this.parent != null && state != null ? this.getElementValueList() : this.getElementValueListNoTransform();
      list.addChild(node);
   }

   public void addElementValueNoTransform(ElementValue node) {
      List<ElementValue> list = this.getElementValueListNoTransform();
      list.addChild(node);
   }

   public void setElementValue(ElementValue node, int i) {
      List<ElementValue> list = this.getElementValueList();
      list.setChild(node, i);
   }

   public List<ElementValue> getElementValues() {
      return this.getElementValueList();
   }

   public List<ElementValue> getElementValuesNoTransform() {
      return this.getElementValueListNoTransform();
   }

   public List<ElementValue> getElementValueList() {
      List<ElementValue> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<ElementValue> getElementValueListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public boolean validTarget(Annotation a) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumElementValue(); ++i) {
         if (this.getElementValue(i).validTarget(a)) {
            return true;
         }
      }

      return false;
   }

   public ElementValue definesElementTypeValue(String name) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumElementValue(); ++i) {
         if (this.getElementValue(i).definesElementTypeValue(name) != null) {
            return this.getElementValue(i).definesElementTypeValue(name);
         }
      }

      return null;
   }

   public boolean hasValue(String s) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumElementValue(); ++i) {
         if (this.getElementValue(i).hasValue(s)) {
            return true;
         }
      }

      return false;
   }

   public boolean commensurateWithArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumElementValue(); ++i) {
         if (!type.componentType().commensurateWith(this.getElementValue(i))) {
            return false;
         }
      }

      return true;
   }

   public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getElementValueListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.definesElementTypeValue(name);
      } else {
         return this.getParent().Define_ElementValue_lookupElementTypeValue(this, caller, name);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
