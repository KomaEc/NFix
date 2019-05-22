package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AInstanceofExpression extends PExpression {
   private PImmediate _immediate_;
   private TInstanceof _instanceof_;
   private PNonvoidType _nonvoidType_;

   public AInstanceofExpression() {
   }

   public AInstanceofExpression(PImmediate _immediate_, TInstanceof _instanceof_, PNonvoidType _nonvoidType_) {
      this.setImmediate(_immediate_);
      this.setInstanceof(_instanceof_);
      this.setNonvoidType(_nonvoidType_);
   }

   public Object clone() {
      return new AInstanceofExpression((PImmediate)this.cloneNode(this._immediate_), (TInstanceof)this.cloneNode(this._instanceof_), (PNonvoidType)this.cloneNode(this._nonvoidType_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAInstanceofExpression(this);
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

   public TInstanceof getInstanceof() {
      return this._instanceof_;
   }

   public void setInstanceof(TInstanceof node) {
      if (this._instanceof_ != null) {
         this._instanceof_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._instanceof_ = node;
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
      return "" + this.toString(this._immediate_) + this.toString(this._instanceof_) + this.toString(this._nonvoidType_);
   }

   void removeChild(Node child) {
      if (this._immediate_ == child) {
         this._immediate_ = null;
      } else if (this._instanceof_ == child) {
         this._instanceof_ = null;
      } else if (this._nonvoidType_ == child) {
         this._nonvoidType_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else if (this._instanceof_ == oldChild) {
         this.setInstanceof((TInstanceof)newChild);
      } else if (this._nonvoidType_ == oldChild) {
         this.setNonvoidType((PNonvoidType)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
