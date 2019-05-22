package org.codehaus.groovy.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codehaus.groovy.GroovyBugError;

public class Reduction extends CSTNode {
   public static final Reduction EMPTY = new Reduction();
   private List elements = null;
   private boolean marked = false;

   public Reduction(Token root) {
      this.elements = new ArrayList();
      this.set(0, root);
   }

   private Reduction() {
      this.elements = Collections.EMPTY_LIST;
   }

   public static Reduction newContainer() {
      return new Reduction(Token.NULL);
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public int size() {
      return this.elements.size();
   }

   public CSTNode get(int index) {
      CSTNode element = null;
      if (index < this.size()) {
         element = (CSTNode)this.elements.get(index);
      }

      return element;
   }

   public Token getRoot() {
      return this.size() > 0 ? (Token)this.elements.get(0) : null;
   }

   public void markAsExpression() {
      this.marked = true;
   }

   public boolean isAnExpression() {
      return this.isA(1911) ? true : this.marked;
   }

   public CSTNode add(CSTNode element) {
      return this.set(this.size(), element);
   }

   public CSTNode set(int index, CSTNode element) {
      if (this.elements == null) {
         throw new GroovyBugError("attempt to set() on a EMPTY Reduction");
      } else if (index == 0 && !(element instanceof Token)) {
         throw new GroovyBugError("attempt to set() a non-Token as root of a Reduction");
      } else {
         int count = this.elements.size();
         if (index >= count) {
            for(int i = count; i <= index; ++i) {
               this.elements.add((Object)null);
            }
         }

         this.elements.set(index, element);
         return element;
      }
   }

   public CSTNode remove(int index) {
      if (index < 1) {
         throw new GroovyBugError("attempt to remove() root node of Reduction");
      } else {
         return (CSTNode)this.elements.remove(index);
      }
   }

   public Reduction asReduction() {
      return this;
   }
}
