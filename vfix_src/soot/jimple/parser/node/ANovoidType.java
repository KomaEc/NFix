package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ANovoidType extends PType {
   private PNonvoidType _nonvoidType_;

   public ANovoidType() {
   }

   public ANovoidType(PNonvoidType _nonvoidType_) {
      this.setNonvoidType(_nonvoidType_);
   }

   public Object clone() {
      return new ANovoidType((PNonvoidType)this.cloneNode(this._nonvoidType_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseANovoidType(this);
   }

   public PNonvoidType getNonvoidType() {
      return this._nonvoidType_;
   }

   public void setNonvoidType(PNonvoidType node) {
      if (this._nonvoidType_ != null) {
         this._nonvoidType_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._nonvoidType_ = node;
   }

   public String toString() {
      return "" + this.toString(this._nonvoidType_);
   }

   void removeChild(Node child) {
      if (this._nonvoidType_ == child) {
         this._nonvoidType_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._nonvoidType_ == oldChild) {
         this.setNonvoidType((PNonvoidType)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
