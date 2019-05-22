package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AVoidType extends PType {
   private TVoid _void_;

   public AVoidType() {
   }

   public AVoidType(TVoid _void_) {
      this.setVoid(_void_);
   }

   public Object clone() {
      return new AVoidType((TVoid)this.cloneNode(this._void_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAVoidType(this);
   }

   public TVoid getVoid() {
      return this._void_;
   }

   public void setVoid(TVoid node) {
      if (this._void_ != null) {
         this._void_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._void_ = node;
   }

   public String toString() {
      return "" + this.toString(this._void_);
   }

   void removeChild(Node child) {
      if (this._void_ == child) {
         this._void_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._void_ == oldChild) {
         this.setVoid((TVoid)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
