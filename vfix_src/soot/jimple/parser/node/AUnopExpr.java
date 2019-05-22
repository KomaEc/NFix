package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AUnopExpr extends PUnopExpr {
   private PUnop _unop_;
   private PImmediate _immediate_;

   public AUnopExpr() {
   }

   public AUnopExpr(PUnop _unop_, PImmediate _immediate_) {
      this.setUnop(_unop_);
      this.setImmediate(_immediate_);
   }

   public Object clone() {
      return new AUnopExpr((PUnop)this.cloneNode(this._unop_), (PImmediate)this.cloneNode(this._immediate_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAUnopExpr(this);
   }

   public PUnop getUnop() {
      return this._unop_;
   }

   public void setUnop(PUnop node) {
      if (this._unop_ != null) {
         this._unop_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._unop_ = node;
   }

   public PImmediate getImmediate() {
      return this._immediate_;
   }

   public void setImmediate(PImmediate node) {
      if (this._immediate_ != null) {
         this._immediate_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._immediate_ = node;
   }

   public String toString() {
      return "" + this.toString(this._unop_) + this.toString(this._immediate_);
   }

   void removeChild(Node child) {
      if (this._unop_ == child) {
         this._unop_ = null;
      } else if (this._immediate_ == child) {
         this._immediate_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._unop_ == oldChild) {
         this.setUnop((PUnop)newChild);
      } else if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
