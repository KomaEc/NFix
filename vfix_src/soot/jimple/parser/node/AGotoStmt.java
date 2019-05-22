package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AGotoStmt extends PGotoStmt {
   private TGoto _goto_;
   private PLabelName _labelName_;
   private TSemicolon _semicolon_;

   public AGotoStmt() {
   }

   public AGotoStmt(TGoto _goto_, PLabelName _labelName_, TSemicolon _semicolon_) {
      this.setGoto(_goto_);
      this.setLabelName(_labelName_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AGotoStmt((TGoto)this.cloneNode(this._goto_), (PLabelName)this.cloneNode(this._labelName_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAGotoStmt(this);
   }

   public TGoto getGoto() {
      return this._goto_;
   }

   public void setGoto(TGoto node) {
      if (this._goto_ != null) {
         this._goto_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._goto_ = node;
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

   public TSemicolon getSemicolon() {
      return this._semicolon_;
   }

   public void setSemicolon(TSemicolon node) {
      if (this._semicolon_ != null) {
         this._semicolon_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._semicolon_ = node;
   }

   public String toString() {
      return "" + this.toString(this._goto_) + this.toString(this._labelName_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._goto_ == child) {
         this._goto_ = null;
      } else if (this._labelName_ == child) {
         this._labelName_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._goto_ == oldChild) {
         this.setGoto((TGoto)newChild);
      } else if (this._labelName_ == oldChild) {
         this.setLabelName((PLabelName)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
