package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AClassNameMultiClassNameList extends PClassNameList {
   private PClassName _className_;
   private TComma _comma_;
   private PClassNameList _classNameList_;

   public AClassNameMultiClassNameList() {
   }

   public AClassNameMultiClassNameList(PClassName _className_, TComma _comma_, PClassNameList _classNameList_) {
      this.setClassName(_className_);
      this.setComma(_comma_);
      this.setClassNameList(_classNameList_);
   }

   public Object clone() {
      return new AClassNameMultiClassNameList((PClassName)this.cloneNode(this._className_), (TComma)this.cloneNode(this._comma_), (PClassNameList)this.cloneNode(this._classNameList_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAClassNameMultiClassNameList(this);
   }

   public PClassName getClassName() {
      return this._className_;
   }

   public void setClassName(PClassName node) {
      if (this._className_ != null) {
         this._className_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._className_ = node;
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

   public PClassNameList getClassNameList() {
      return this._classNameList_;
   }

   public void setClassNameList(PClassNameList node) {
      if (this._classNameList_ != null) {
         this._classNameList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._classNameList_ = node;
   }

   public String toString() {
      return "" + this.toString(this._className_) + this.toString(this._comma_) + this.toString(this._classNameList_);
   }

   void removeChild(Node child) {
      if (this._className_ == child) {
         this._className_ = null;
      } else if (this._comma_ == child) {
         this._comma_ = null;
      } else if (this._classNameList_ == child) {
         this._classNameList_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._className_ == oldChild) {
         this.setClassName((PClassName)newChild);
      } else if (this._comma_ == oldChild) {
         this.setComma((TComma)newChild);
      } else if (this._classNameList_ == oldChild) {
         this.setClassNameList((PClassNameList)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
