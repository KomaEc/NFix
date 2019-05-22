package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class AFileBody extends PFileBody {
   private TLBrace _lBrace_;
   private final LinkedList<PMember> _member_ = new LinkedList();
   private TRBrace _rBrace_;

   public AFileBody() {
   }

   public AFileBody(TLBrace _lBrace_, List<?> _member_, TRBrace _rBrace_) {
      this.setLBrace(_lBrace_);
      this.setMember(_member_);
      this.setRBrace(_rBrace_);
   }

   public Object clone() {
      return new AFileBody((TLBrace)this.cloneNode(this._lBrace_), this.cloneList(this._member_), (TRBrace)this.cloneNode(this._rBrace_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFileBody(this);
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

   public LinkedList<PMember> getMember() {
      return this._member_;
   }

   public void setMember(List<?> list) {
      Iterator var2 = this._member_.iterator();

      while(var2.hasNext()) {
         PMember e = (PMember)var2.next();
         e.parent((Node)null);
      }

      this._member_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PMember e = (PMember)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._member_.add(e);
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

   public String toString() {
      return "" + this.toString(this._lBrace_) + this.toString(this._member_) + this.toString(this._rBrace_);
   }

   void removeChild(Node child) {
      if (this._lBrace_ == child) {
         this._lBrace_ = null;
      } else if (!this._member_.remove(child)) {
         if (this._rBrace_ == child) {
            this._rBrace_ = null;
         } else {
            throw new RuntimeException("Not a child.");
         }
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._lBrace_ == oldChild) {
         this.setLBrace((TLBrace)newChild);
      } else {
         ListIterator i = this._member_.listIterator();

         do {
            if (!i.hasNext()) {
               if (this._rBrace_ == oldChild) {
                  this.setRBrace((TRBrace)newChild);
                  return;
               }

               throw new RuntimeException("Not a child.");
            }
         } while(i.next() != oldChild);

         if (newChild != null) {
            i.set((PMember)newChild);
            newChild.parent(this);
            oldChild.parent((Node)null);
         } else {
            i.remove();
            oldChild.parent((Node)null);
         }
      }
   }
}
