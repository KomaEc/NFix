package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AMultiLocalNameList extends PLocalNameList {
   private PLocalName _localName_;
   private TComma _comma_;
   private PLocalNameList _localNameList_;

   public AMultiLocalNameList() {
   }

   public AMultiLocalNameList(PLocalName _localName_, TComma _comma_, PLocalNameList _localNameList_) {
      this.setLocalName(_localName_);
      this.setComma(_comma_);
      this.setLocalNameList(_localNameList_);
   }

   public Object clone() {
      return new AMultiLocalNameList((PLocalName)this.cloneNode(this._localName_), (TComma)this.cloneNode(this._comma_), (PLocalNameList)this.cloneNode(this._localNameList_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMultiLocalNameList(this);
   }

   public PLocalName getLocalName() {
      return this._localName_;
   }

   public void setLocalName(PLocalName node) {
      if (this._localName_ != null) {
         this._localName_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._localName_ = node;
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

   public PLocalNameList getLocalNameList() {
      return this._localNameList_;
   }

   public void setLocalNameList(PLocalNameList node) {
      if (this._localNameList_ != null) {
         this._localNameList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._localNameList_ = node;
   }

   public String toString() {
      return "" + this.toString(this._localName_) + this.toString(this._comma_) + this.toString(this._localNameList_);
   }

   void removeChild(Node child) {
      if (this._localName_ == child) {
         this._localName_ = null;
      } else if (this._comma_ == child) {
         this._comma_ = null;
      } else if (this._localNameList_ == child) {
         this._localNameList_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._localName_ == oldChild) {
         this.setLocalName((PLocalName)newChild);
      } else if (this._comma_ == oldChild) {
         this.setComma((TComma)newChild);
      } else if (this._localNameList_ == oldChild) {
         this.setLocalNameList((PLocalNameList)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
