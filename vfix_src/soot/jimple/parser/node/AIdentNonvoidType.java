package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class AIdentNonvoidType extends PNonvoidType {
   private TIdentifier _identifier_;
   private final LinkedList<PArrayBrackets> _arrayBrackets_ = new LinkedList();

   public AIdentNonvoidType() {
   }

   public AIdentNonvoidType(TIdentifier _identifier_, List<?> _arrayBrackets_) {
      this.setIdentifier(_identifier_);
      this.setArrayBrackets(_arrayBrackets_);
   }

   public Object clone() {
      return new AIdentNonvoidType((TIdentifier)this.cloneNode(this._identifier_), this.cloneList(this._arrayBrackets_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAIdentNonvoidType(this);
   }

   public TIdentifier getIdentifier() {
      return this._identifier_;
   }

   public void setIdentifier(TIdentifier node) {
      if (this._identifier_ != null) {
         this._identifier_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._identifier_ = node;
   }

   public LinkedList<PArrayBrackets> getArrayBrackets() {
      return this._arrayBrackets_;
   }

   public void setArrayBrackets(List<?> list) {
      Iterator var2 = this._arrayBrackets_.iterator();

      while(var2.hasNext()) {
         PArrayBrackets e = (PArrayBrackets)var2.next();
         e.parent((Node)null);
      }

      this._arrayBrackets_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PArrayBrackets e = (PArrayBrackets)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._arrayBrackets_.add(e);
      }

   }

   public String toString() {
      return "" + this.toString(this._identifier_) + this.toString(this._arrayBrackets_);
   }

   void removeChild(Node child) {
      if (this._identifier_ == child) {
         this._identifier_ = null;
      } else if (!this._arrayBrackets_.remove(child)) {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._identifier_ == oldChild) {
         this.setIdentifier((TIdentifier)newChild);
      } else {
         ListIterator i = this._arrayBrackets_.listIterator();

         do {
            if (!i.hasNext()) {
               throw new RuntimeException("Not a child.");
            }
         } while(i.next() != oldChild);

         if (newChild != null) {
            i.set((PArrayBrackets)newChild);
            newChild.parent(this);
            oldChild.parent((Node)null);
         } else {
            i.remove();
            oldChild.parent((Node)null);
         }
      }
   }
}
