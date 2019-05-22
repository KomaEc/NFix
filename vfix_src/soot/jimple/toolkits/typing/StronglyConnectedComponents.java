package soot.jimple.toolkits.typing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StronglyConnectedComponents {
   private static final Logger logger = LoggerFactory.getLogger(StronglyConnectedComponents.class);
   List<TypeVariable> variables;
   Set<TypeVariable> black;
   List<TypeVariable> finished;
   List<List<TypeVariable>> forest = new LinkedList();
   List<TypeVariable> current_tree;
   private static final boolean DEBUG = false;

   public static void merge(List<TypeVariable> typeVariableList) throws TypeException {
      new StronglyConnectedComponents(typeVariableList);
   }

   private StronglyConnectedComponents(List<TypeVariable> typeVariableList) throws TypeException {
      this.variables = typeVariableList;
      this.black = new TreeSet();
      this.finished = new LinkedList();
      Iterator i = this.variables.iterator();

      TypeVariable var;
      while(i.hasNext()) {
         var = (TypeVariable)i.next();
         if (!this.black.add(var)) {
            this.dfsg_visit(var);
         }
      }

      this.black = new TreeSet();
      i = this.finished.iterator();

      while(i.hasNext()) {
         var = (TypeVariable)i.next();
         if (!this.black.add(var)) {
            this.current_tree = new LinkedList();
            this.forest.add(this.current_tree);
            this.dfsgt_visit(var);
         }
      }

      i = this.forest.iterator();

      while(i.hasNext()) {
         List<TypeVariable> list = (List)i.next();
         TypeVariable previous = null;
         StringBuffer s = null;
         Iterator j = list.iterator();

         while(j.hasNext()) {
            TypeVariable current = (TypeVariable)j.next();
            if (previous == null) {
               previous = current;
            } else {
               try {
                  previous = previous.union(current);
               } catch (TypeException var9) {
                  throw var9;
               }
            }
         }
      }

   }

   private void dfsg_visit(TypeVariable var) {
      List<TypeVariable> parents = var.parents();
      Iterator var3 = parents.iterator();

      while(var3.hasNext()) {
         TypeVariable parent = (TypeVariable)var3.next();
         if (!this.black.add(parent)) {
            this.dfsg_visit(parent);
         }
      }

      this.finished.add(0, var);
   }

   private void dfsgt_visit(TypeVariable var) {
      this.current_tree.add(var);
      List<TypeVariable> children = var.children();
      Iterator var3 = children.iterator();

      while(var3.hasNext()) {
         TypeVariable child = (TypeVariable)var3.next();
         if (!this.black.add(child)) {
            this.dfsgt_visit(child);
         }
      }

   }
}
