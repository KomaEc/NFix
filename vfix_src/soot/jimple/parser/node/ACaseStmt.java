package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACaseStmt extends PCaseStmt {
   private PCaseLabel _caseLabel_;
   private TColon _colon_;
   private PGotoStmt _gotoStmt_;

   public ACaseStmt() {
   }

   public ACaseStmt(PCaseLabel _caseLabel_, TColon _colon_, PGotoStmt _gotoStmt_) {
      this.setCaseLabel(_caseLabel_);
      this.setColon(_colon_);
      this.setGotoStmt(_gotoStmt_);
   }

   public Object clone() {
      return new ACaseStmt((PCaseLabel)this.cloneNode(this._caseLabel_), (TColon)this.cloneNode(this._colon_), (PGotoStmt)this.cloneNode(this._gotoStmt_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACaseStmt(this);
   }

   public PCaseLabel getCaseLabel() {
      return this._caseLabel_;
   }

   public void setCaseLabel(PCaseLabel node) {
      if (this._caseLabel_ != null) {
         this._caseLabel_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._caseLabel_ = node;
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

   public PGotoStmt getGotoStmt() {
      return this._gotoStmt_;
   }

   public void setGotoStmt(PGotoStmt node) {
      if (this._gotoStmt_ != null) {
         this._gotoStmt_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._gotoStmt_ = node;
   }

   public String toString() {
      return "" + this.toString(this._caseLabel_) + this.toString(this._colon_) + this.toString(this._gotoStmt_);
   }

   void removeChild(Node child) {
      if (this._caseLabel_ == child) {
         this._caseLabel_ = null;
      } else if (this._colon_ == child) {
         this._colon_ = null;
      } else if (this._gotoStmt_ == child) {
         this._gotoStmt_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._caseLabel_ == oldChild) {
         this.setCaseLabel((PCaseLabel)newChild);
      } else if (this._colon_ == oldChild) {
         this.setColon((TColon)newChild);
      } else if (this._gotoStmt_ == oldChild) {
         this.setGotoStmt((PGotoStmt)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
