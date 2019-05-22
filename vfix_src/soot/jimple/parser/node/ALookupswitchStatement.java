package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class ALookupswitchStatement extends PStatement {
   private TLookupswitch _lookupswitch_;
   private TLParen _lParen_;
   private PImmediate _immediate_;
   private TRParen _rParen_;
   private TLBrace _lBrace_;
   private final LinkedList<PCaseStmt> _caseStmt_ = new LinkedList();
   private TRBrace _rBrace_;
   private TSemicolon _semicolon_;

   public ALookupswitchStatement() {
   }

   public ALookupswitchStatement(TLookupswitch _lookupswitch_, TLParen _lParen_, PImmediate _immediate_, TRParen _rParen_, TLBrace _lBrace_, List<?> _caseStmt_, TRBrace _rBrace_, TSemicolon _semicolon_) {
      this.setLookupswitch(_lookupswitch_);
      this.setLParen(_lParen_);
      this.setImmediate(_immediate_);
      this.setRParen(_rParen_);
      this.setLBrace(_lBrace_);
      this.setCaseStmt(_caseStmt_);
      this.setRBrace(_rBrace_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new ALookupswitchStatement((TLookupswitch)this.cloneNode(this._lookupswitch_), (TLParen)this.cloneNode(this._lParen_), (PImmediate)this.cloneNode(this._immediate_), (TRParen)this.cloneNode(this._rParen_), (TLBrace)this.cloneNode(this._lBrace_), this.cloneList(this._caseStmt_), (TRBrace)this.cloneNode(this._rBrace_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseALookupswitchStatement(this);
   }

   public TLookupswitch getLookupswitch() {
      return this._lookupswitch_;
   }

   public void setLookupswitch(TLookupswitch node) {
      if (this._lookupswitch_ != null) {
         this._lookupswitch_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lookupswitch_ = node;
   }

   public TLParen getLParen() {
      return this._lParen_;
   }

   public void setLParen(TLParen node) {
      if (this._lParen_ != null) {
         this._lParen_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lParen_ = node;
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

   public TRParen getRParen() {
      return this._rParen_;
   }

   public void setRParen(TRParen node) {
      if (this._rParen_ != null) {
         this._rParen_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._rParen_ = node;
   }

   public TLBrace getLBrace() {
      return this._lBrace_;
   }

   public void setLBrace(TLBrace node) {
      if (this._lBrace_ != null) {
         this._lBrace_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lBrace_ = node;
   }

   public LinkedList<PCaseStmt> getCaseStmt() {
      return this._caseStmt_;
   }

   public void setCaseStmt(List<?> list) {
      Iterator var2 = this._caseStmt_.iterator();

      while(var2.hasNext()) {
         PCaseStmt e = (PCaseStmt)var2.next();
         e.parent((Node)null);
      }

      this._caseStmt_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PCaseStmt e = (PCaseStmt)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._caseStmt_.add(e);
      }

   }

   public TRBrace getRBrace() {
      return this._rBrace_;
   }

   public void setRBrace(TRBrace node) {
      if (this._rBrace_ != null) {
         this._rBrace_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._rBrace_ = node;
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
      return "" + this.toString(this._lookupswitch_) + this.toString(this._lParen_) + this.toString(this._immediate_) + this.toString(this._rParen_) + this.toString(this._lBrace_) + this.toString(this._caseStmt_) + this.toString(this._rBrace_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._lookupswitch_ == child) {
         this._lookupswitch_ = null;
      } else if (this._lParen_ == child) {
         this._lParen_ = null;
      } else if (this._immediate_ == child) {
         this._immediate_ = null;
      } else if (this._rParen_ == child) {
         this._rParen_ = null;
      } else if (this._lBrace_ == child) {
         this._lBrace_ = null;
      } else if (!this._caseStmt_.remove(child)) {
         if (this._rBrace_ == child) {
            this._rBrace_ = null;
         } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
         } else {
            throw new RuntimeException("Not a child.");
         }
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._lookupswitch_ == oldChild) {
         this.setLookupswitch((TLookupswitch)newChild);
      } else if (this._lParen_ == oldChild) {
         this.setLParen((TLParen)newChild);
      } else if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else if (this._rParen_ == oldChild) {
         this.setRParen((TRParen)newChild);
      } else if (this._lBrace_ == oldChild) {
         this.setLBrace((TLBrace)newChild);
      } else {
         ListIterator i = this._caseStmt_.listIterator();

         do {
            if (!i.hasNext()) {
               if (this._rBrace_ == oldChild) {
                  this.setRBrace((TRBrace)newChild);
                  return;
               }

               if (this._semicolon_ == oldChild) {
                  this.setSemicolon((TSemicolon)newChild);
                  return;
               }

               throw new RuntimeException("Not a child.");
            }
         } while(i.next() != oldChild);

         if (newChild != null) {
            i.set((PCaseStmt)newChild);
            newChild.parent(this);
            oldChild.parent((Node)null);
         } else {
            i.remove();
            oldChild.parent((Node)null);
         }
      }
   }
}
