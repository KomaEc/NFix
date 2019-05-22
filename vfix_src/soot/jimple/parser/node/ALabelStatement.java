package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ALabelStatement extends PStatement {
   private PLabelName _labelName_;
   private TColon _colon_;

   public ALabelStatement() {
   }

   public ALabelStatement(PLabelName _labelName_, TColon _colon_) {
      this.setLabelName(_labelName_);
      this.setColon(_colon_);
   }

   public Object clone() {
      return new ALabelStatement((PLabelName)this.cloneNode(this._labelName_), (TColon)this.cloneNode(this._colon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseALabelStatement(this);
   }

   public PLabelName getLabelName() {
      return this._labelName_;
   }

   public void setLabelName(PLabelName node) {
      if (this._labelName_ != null) {
         this._labelName_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._labelName_ = node;
   }

   public TColon getColon() {
      return this._colon_;
   }

   public void setColon(TColon node) {
      if (this._colon_ != null) {
         this._colon_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._colon_ = node;
   }

   public String toString() {
      return "" + this.toString(this._labelName_) + this.toString(this._colon_);
   }

   void removeChild(Node child) {
      if (this._labelName_ == child) {
         this._labelName_ = null;
      } else if (this._colon_ == child) {
         this._colon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._labelName_ == oldChild) {
         this.setLabelName((PLabelName)newChild);
      } else if (this._colon_ == oldChild) {
         this.setColon((TColon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
