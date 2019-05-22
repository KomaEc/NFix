package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class Start extends Node {
   private PFile _pFile_;
   private EOF _eof_;

   public Start() {
   }

   public Start(PFile _pFile_, EOF _eof_) {
      this.setPFile(_pFile_);
      this.setEOF(_eof_);
   }

   public Object clone() {
      return new Start((PFile)this.cloneNode(this._pFile_), (EOF)this.cloneNode(this._eof_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseStart(this);
   }

   public PFile getPFile() {
      return this._pFile_;
   }

   public void setPFile(PFile node) {
      if (this._pFile_ != null) {
         this._pFile_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._pFile_ = node;
   }

   public EOF getEOF() {
      return this._eof_;
   }

   public void setEOF(EOF node) {
      if (this._eof_ != null) {
         this._eof_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._eof_ = node;
   }

   void removeChild(Node child) {
      if (this._pFile_ == child) {
         this._pFile_ = null;
      } else if (this._eof_ == child) {
         this._eof_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._pFile_ == oldChild) {
         this.setPFile((PFile)newChild);
      } else if (this._eof_ == oldChild) {
         this.setEOF((EOF)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   public String toString() {
      return "" + this.toString(this._pFile_) + this.toString(this._eof_);
   }
}
