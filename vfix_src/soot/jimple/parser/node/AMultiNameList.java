package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AMultiNameList extends PNameList {
   private PName _name_;
   private TComma _comma_;
   private PNameList _nameList_;

   public AMultiNameList() {
   }

   public AMultiNameList(PName _name_, TComma _comma_, PNameList _nameList_) {
      this.setName(_name_);
      this.setComma(_comma_);
      this.setNameList(_nameList_);
   }

   public Object clone() {
      return new AMultiNameList((PName)this.cloneNode(this._name_), (TComma)this.cloneNode(this._comma_), (PNameList)this.cloneNode(this._nameList_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMultiNameList(this);
   }

   public PName getName() {
      return this._name_;
   }

   public void setName(PName node) {
      if (this._name_ != null) {
         this._name_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._name_ = node;
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

   public PNameList getNameList() {
      return this._nameList_;
   }

   public void setNameList(PNameList node) {
      if (this._nameList_ != null) {
         this._nameList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._nameList_ = node;
   }

   public String toString() {
      return "" + this.toString(this._name_) + this.toString(this._comma_) + this.toString(this._nameList_);
   }

   void removeChild(Node child) {
      if (this._name_ == child) {
         this._name_ = null;
      } else if (this._comma_ == child) {
         this._comma_ = null;
      } else if (this._nameList_ == child) {
         this._nameList_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._name_ == oldChild) {
         this.setName((PName)newChild);
      } else if (this._comma_ == oldChild) {
         this.setComma((TComma)newChild);
      } else if (this._nameList_ == oldChild) {
         this.setNameList((PNameList)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
