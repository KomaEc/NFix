package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AArrayReference extends PReference {
   private PArrayRef _arrayRef_;

   public AArrayReference() {
   }

   public AArrayReference(PArrayRef _arrayRef_) {
      this.setArrayRef(_arrayRef_);
   }

   public Object clone() {
      return new AArrayReference((PArrayRef)this.cloneNode(this._arrayRef_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAArrayReference(this);
   }

   public PArrayRef getArrayRef() {
      return this._arrayRef_;
   }

   public void setArrayRef(PArrayRef node) {
      if (this._arrayRef_ != null) {
         this._arrayRef_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._arrayRef_ = node;
   }

   public String toString() {
      return "" + this.toString(this._arrayRef_);
   }

   void removeChild(Node child) {
      if (this._arrayRef_ == child) {
         this._arrayRef_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._arrayRef_ == oldChild) {
         this.setArrayRef((PArrayRef)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
