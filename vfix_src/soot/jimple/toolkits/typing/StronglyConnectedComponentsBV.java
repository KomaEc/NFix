package soot.jimple.toolkits.typing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.util.BitSetIterator;
import soot.util.BitVector;

/** @deprecated */
@Deprecated
class StronglyConnectedComponentsBV {
   private static final Logger logger = LoggerFactory.getLogger(StronglyConnectedComponentsBV.class);
   BitVector variables;
   Set<TypeVariableBV> black;
   LinkedList<TypeVariableBV> finished;
   TypeResolverBV resolver;
   LinkedList<LinkedList<TypeVariableBV>> forest = new LinkedList();
   LinkedList<TypeVariableBV> current_tree;
   private static final boolean DEBUG = false;

   public StronglyConnectedComponentsBV(BitVector typeVariableList, TypeResolverBV resolver) throws TypeException {
      this.resolver = resolver;
      this.variables = typeVariableList;
      this.black = new TreeSet();
      this.finished = new LinkedList();
      BitSetIterator i = this.variables.iterator();

      TypeVariableBV var;
      while(i.hasNext()) {
         var = resolver.typeVariableForId(i.next());
         if (!this.black.contains(var)) {
            this.black.add(var);
            this.dfsg_visit(var);
         }
      }

      this.black = new TreeSet();
      Iterator i = this.finished.iterator();

      while(i.hasNext()) {
         var = (TypeVariableBV)i.next();
         if (!this.black.contains(var)) {
            this.current_tree = new LinkedList();
            this.forest.add(this.current_tree);
            this.black.add(var);
            this.dfsgt_visit(var);
         }
      }

      i = this.forest.iterator();

      while(i.hasNext()) {
         LinkedList<TypeVariableBV> list = (LinkedList)i.next();
         TypeVariableBV previous = null;
         StringBuffer s = null;
         Iterator j = list.iterator();

         while(j.hasNext()) {
            TypeVariableBV current = (TypeVariableBV)j.next();
            if (previous == null) {
               previous = current;
            } else {
               try {
                  previous = previous.union(current);
               } catch (TypeException var10) {
                  throw var10;
               }
            }
         }
      }

   }

   private void dfsg_visit(TypeVariableBV var) {
      BitVector parents = var.parents();
      BitSetIterator i = parents.iterator();

      while(i.hasNext()) {
         TypeVariableBV parent = this.resolver.typeVariableForId(i.next());
         if (!this.black.contains(parent)) {
            this.black.add(parent);
            this.dfsg_visit(parent);
         }
      }

      this.finished.add(0, var);
   }

   private void dfsgt_visit(TypeVariableBV var) {
      this.current_tree.add(var);
      BitVector children = var.children();
      BitSetIterator i = children.iterator();

      while(i.hasNext()) {
         TypeVariableBV child = this.resolver.typeVariableForId(i.next());
         if (!this.black.contains(child)) {
            this.black.add(child);
            this.dfsgt_visit(child);
         }
      }

   }
}
