package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ASigFieldRef extends PFieldRef {
   private PFieldSignature _fieldSignature_;

   public ASigFieldRef() {
   }

   public ASigFieldRef(PFieldSignature _fieldSignature_) {
      this.setFieldSignature(_fieldSignature_);
   }

   public Object clone() {
      return new ASigFieldRef((PFieldSignature)this.cloneNode(this._fieldSignature_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseASigFieldRef(this);
   }

   public PFieldSignature getFieldSignature() {
      return this._fieldSignature_;
   }

   public void setFieldSignature(PFieldSignature node) {
      if (this._fieldSignature_ != null) {
         this._fieldSignature_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._fieldSignature_ = node;
   }

   public String toString() {
      return "" + this.toString(this._fieldSignature_);
   }

   void removeChild(Node child) {
      if (this._fieldSignature_ == child) {
         this._fieldSignature_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._fieldSignature_ == oldChild) {
         this.setFieldSignature((PFieldSignature)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
