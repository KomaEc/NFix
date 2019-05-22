package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import polyglot.ast.Node;
import polyglot.util.Copy;
import polyglot.util.InternalCompilerError;

public abstract class HaltingVisitor extends NodeVisitor implements Copy {
   Node bypassParent;
   Collection bypass;

   public HaltingVisitor bypassChildren(Node n) {
      HaltingVisitor v = (HaltingVisitor)this.copy();
      v.bypassParent = n;
      return v;
   }

   public HaltingVisitor visitChildren() {
      HaltingVisitor v = (HaltingVisitor)this.copy();
      v.bypassParent = null;
      v.bypass = null;
      return v;
   }

   public HaltingVisitor bypass(Node n) {
      if (n == null) {
         return this;
      } else {
         HaltingVisitor v = (HaltingVisitor)this.copy();
         if (this.bypass == null) {
            v.bypass = Collections.singleton(n);
         } else {
            v.bypass = new ArrayList(this.bypass.size() + 1);
            v.bypass.addAll(this.bypass);
            v.bypass.add(n);
         }

         return v;
      }
   }

   public HaltingVisitor bypass(Collection c) {
      if (c == null) {
         return this;
      } else {
         HaltingVisitor v = (HaltingVisitor)this.copy();
         if (this.bypass == null) {
            v.bypass = new ArrayList(c);
         } else {
            v.bypass = new ArrayList(this.bypass.size() + c.size());
            v.bypass.addAll(this.bypass);
            v.bypass.addAll(c);
         }

         return v;
      }
   }

   public final Node override(Node parent, Node n) {
      if (this.bypassParent != null && this.bypassParent == parent) {
         return n;
      } else {
         if (this.bypass != null) {
            Iterator i = this.bypass.iterator();

            while(i.hasNext()) {
               if (i.next() == n) {
                  return n;
               }
            }
         }

         return null;
      }
   }

   public Object copy() {
      try {
         HaltingVisitor v = (HaltingVisitor)super.clone();
         return v;
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone() weirdness.");
      }
   }
}
