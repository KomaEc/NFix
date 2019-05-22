package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AMultiArgList extends PArgList {
   private PImmediate _immediate_;
   private TComma _comma_;
   private PArgList _argList_;

   public AMultiArgList() {
   }

   public AMultiArgList(PImmediate _immediate_, TComma _comma_, PArgList _argList_) {
      this.setImmediate(_immediate_);
      this.setComma(_comma_);
      this.setArgList(_argList_);
   }

   public Object clone() {
      return new AMultiArgList((PImmediate)this.cloneNode(this._immediate_), (TComma)this.cloneNode(this._comma_), (PArgList)this.cloneNode(this._argList_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMultiArgList(this);
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

   public TComma getComma() {
      return this._comma_;
   }

   public void setComma(TComma node) {
      if (this._comma_ != null) {
         this._comma_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._comma_ = node;
   }

   public PArgList getArgList() {
      return this._argList_;
   }

   public void setArgList(PArgList node) {
      if (this._argList_ != null) {
         this._argList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._argList_ = node;
   }

   public String toString() {
      return "" + this.toString(this._immediate_) + this.toString(this._comma_) + this.toString(this._argList_);
   }

   void removeChild(Node child) {
      if (this._immediate_ == child) {
         this._immediate_ = null;
      } else if (this._comma_ == child) {
         this._comma_ = null;
      } else if (this._argList_ == child) {
         this._argList_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else if (this._comma_ == oldChild) {
         this.setComma((TComma)newChild);
      } else if (this._argList_ == oldChild) {
         this.setArgList((PArgList)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
