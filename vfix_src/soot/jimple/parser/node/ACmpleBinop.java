package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpleBinop extends PBinop {
   private TCmple _cmple_;

   public ACmpleBinop() {
   }

   public ACmpleBinop(TCmple _cmple_) {
      this.setCmple(_cmple_);
   }

   public Object clone() {
      return new ACmpleBinop((TCmple)this.cloneNode(this._cmple_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpleBinop(this);
   }

   public TCmple getCmple() {
      return this._cmple_;
   }

   public void setCmple(TCmple node) {
      if (this._cmple_ != null) {
         this._cmple_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmple_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmple_);
   }

   void removeChild(Node child) {
      if (this._cmple_ == child) {
         this._cmple_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmple_ == oldChild) {
         this.setCmple((TCmple)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
