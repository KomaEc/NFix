package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class AMultiNewExpr extends PNewExpr {
   private TNewmultiarray _newmultiarray_;
   private TLParen _lParen_;
   private PBaseType _baseType_;
   private TRParen _rParen_;
   private final LinkedList<PArrayDescriptor> _arrayDescriptor_ = new LinkedList();

   public AMultiNewExpr() {
   }

   public AMultiNewExpr(TNewmultiarray _newmultiarray_, TLParen _lParen_, PBaseType _baseType_, TRParen _rParen_, List<?> _arrayDescriptor_) {
      this.setNewmultiarray(_newmultiarray_);
      this.setLParen(_lParen_);
      this.setBaseType(_baseType_);
      this.setRParen(_rParen_);
      this.setArrayDescriptor(_arrayDescriptor_);
   }

   public Object clone() {
      return new AMultiNewExpr((TNewmultiarray)this.cloneNode(this._newmultiarray_), (TLParen)this.cloneNode(this._lParen_), (PBaseType)this.cloneNode(this._baseType_), (TRParen)this.cloneNode(this._rParen_), this.cloneList(this._arrayDescriptor_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMultiNewExpr(this);
   }

   public TNewmultiarray getNewmultiarray() {
      return this._newmultiarray_;
   }

   public void setNewmultiarray(TNewmultiarray node) {
      if (this._newmultiarray_ != null) {
         this._newmultiarray_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._newmultiarray_ = node;
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

   public PBaseType getBaseType() {
      return this._baseType_;
   }

   public void setBaseType(PBaseType node) {
      if (this._baseType_ != null) {
         this._baseType_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._baseType_ = node;
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

   public LinkedList<PArrayDescriptor> getArrayDescriptor() {
      return this._arrayDescriptor_;
   }

   public void setArrayDescriptor(List<?> list) {
      Iterator var2 = this._arrayDescriptor_.iterator();

      while(var2.hasNext()) {
         PArrayDescriptor e = (PArrayDescriptor)var2.next();
         e.parent((Node)null);
      }

      this._arrayDescriptor_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PArrayDescriptor e = (PArrayDescriptor)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._arrayDescriptor_.add(e);
      }

   }

   public String toString() {
      return "" + this.toString(this._newmultiarray_) + this.toString(this._lParen_) + this.toString(this._baseType_) + this.toString(this._rParen_) + this.toString(this._arrayDescriptor_);
   }

   void removeChild(Node child) {
      if (this._newmultiarray_ == child) {
         this._newmultiarray_ = null;
      } else if (this._lParen_ == child) {
         this._lParen_ = null;
      } else if (this._baseType_ == child) {
         this._baseType_ = null;
      } else if (this._rParen_ == child) {
         this._rParen_ = null;
      } else if (!this._arrayDescriptor_.remove(child)) {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._newmultiarray_ == oldChild) {
         this.setNewmultiarray((TNewmultiarray)newChild);
      } else if (this._lParen_ == oldChild) {
         this.setLParen((TLParen)newChild);
      } else if (this._baseType_ == oldChild) {
         this.setBaseType((PBaseType)newChild);
      } else if (this._rParen_ == oldChild) {
         this.setRParen((TRParen)newChild);
      } else {
         ListIterator i = this._arrayDescriptor_.listIterator();

         do {
            if (!i.hasNext()) {
               throw new RuntimeException("Not a child.");
            }
         } while(i.next() != oldChild);

         if (newChild != null) {
            i.set((PArrayDescriptor)newChild);
            newChild.parent(this);
            oldChild.parent((Node)null);
         } else {
            i.remove();
            oldChild.parent((Node)null);
         }
      }
   }
}
