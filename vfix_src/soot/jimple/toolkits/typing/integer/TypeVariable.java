package soot.jimple.toolkits.typing.integer;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TypeVariable implements Comparable<Object> {
   private static final Logger logger = LoggerFactory.getLogger(TypeVariable.class);
   private static final boolean DEBUG = false;
   private final int id;
   private TypeVariable rep = this;
   private int rank = 0;
   private TypeNode approx;
   private TypeNode inv_approx;
   private TypeNode type;
   private List<TypeVariable> parents = Collections.emptyList();
   private List<TypeVariable> children = Collections.emptyList();

   public TypeVariable(int id, TypeResolver resolver) {
      this.id = id;
   }

   public TypeVariable(int id, TypeResolver resolver, TypeNode type) {
      this.id = id;
      this.type = type;
      this.approx = type;
      this.inv_approx = type;
   }

   public int hashCode() {
      return this.rep != this ? this.ecr().hashCode() : this.id;
   }

   public boolean equals(Object obj) {
      if (this.rep != this) {
         return this.ecr().equals(obj);
      } else if (obj == null) {
         return false;
      } else if (!obj.getClass().equals(this.getClass())) {
         return false;
      } else {
         TypeVariable ecr = ((TypeVariable)obj).ecr();
         return ecr == this;
      }
   }

   public int compareTo(Object o) {
      return this.rep != this ? this.ecr().compareTo(o) : this.id - ((TypeVariable)o).ecr().id;
   }

   private TypeVariable ecr() {
      if (this.rep != this) {
         this.rep = this.rep.ecr();
      }

      return this.rep;
   }

   public TypeVariable union(TypeVariable var) throws TypeException {
      if (this.rep != this) {
         return this.ecr().union(var);
      } else {
         TypeVariable y = var.ecr();
         if (this == y) {
            return this;
         } else if (this.rank > y.rank) {
            y.rep = this;
            this.merge(y);
            y.clear();
            return this;
         } else {
            this.rep = y;
            if (this.rank == y.rank) {
               ++y.rank;
            }

            y.merge(this);
            this.clear();
            return y;
         }
      }
   }

   private void clear() {
      this.inv_approx = null;
      this.approx = null;
      this.type = null;
      this.parents = null;
      this.children = null;
   }

   private void merge(TypeVariable var) throws TypeException {
      if (this.type == null) {
         this.type = var.type;
      } else if (var.type != null) {
         error("Type Error(22): Attempt to merge two types.");
      }

      Set<TypeVariable> set = new TreeSet(this.parents);
      set.addAll(var.parents);
      set.remove(this);
      this.parents = Collections.unmodifiableList(new LinkedList(set));
      set = new TreeSet(this.children);
      set.addAll(var.children);
      set.remove(this);
      this.children = Collections.unmodifiableList(new LinkedList(set));
   }

   public int id() {
      return this.rep != this ? this.ecr().id() : this.id;
   }

   public void addParent(TypeVariable variable) {
      if (this.rep != this) {
         this.ecr().addParent(variable);
      } else {
         TypeVariable var = variable.ecr();
         if (var != this) {
            Set<TypeVariable> set = new TreeSet(this.parents);
            set.add(var);
            this.parents = Collections.unmodifiableList(new LinkedList(set));
            set = new TreeSet(var.children);
            set.add(this);
            var.children = Collections.unmodifiableList(new LinkedList(set));
         }
      }
   }

   public void removeParent(TypeVariable variable) {
      if (this.rep != this) {
         this.ecr().removeParent(variable);
      } else {
         TypeVariable var = variable.ecr();
         Set<TypeVariable> set = new TreeSet(this.parents);
         set.remove(var);
         this.parents = Collections.unmodifiableList(new LinkedList(set));
         set = new TreeSet(var.children);
         set.remove(this);
         var.children = Collections.unmodifiableList(new LinkedList(set));
      }
   }

   public void addChild(TypeVariable variable) {
      if (this.rep != this) {
         this.ecr().addChild(variable);
      } else {
         TypeVariable var = variable.ecr();
         if (var != this) {
            Set<TypeVariable> set = new TreeSet(this.children);
            set.add(var);
            this.children = Collections.unmodifiableList(new LinkedList(set));
            set = new TreeSet(var.parents);
            set.add(this);
            var.parents = Collections.unmodifiableList(new LinkedList(set));
         }
      }
   }

   public void removeChild(TypeVariable variable) {
      if (this.rep != this) {
         this.ecr().removeChild(variable);
      } else {
         TypeVariable var = variable.ecr();
         Set<TypeVariable> set = new TreeSet(this.children);
         set.remove(var);
         this.children = Collections.unmodifiableList(new LinkedList(set));
         set = new TreeSet(var.parents);
         set.remove(this);
         var.parents = Collections.unmodifiableList(new LinkedList(set));
      }
   }

   public List<TypeVariable> parents() {
      return this.rep != this ? this.ecr().parents() : this.parents;
   }

   public List<TypeVariable> children() {
      return this.rep != this ? this.ecr().children() : this.children;
   }

   public TypeNode approx() {
      return this.rep != this ? this.ecr().approx() : this.approx;
   }

   public TypeNode inv_approx() {
      return this.rep != this ? this.ecr().inv_approx() : this.inv_approx;
   }

   public TypeNode type() {
      return this.rep != this ? this.ecr().type() : this.type;
   }

   static void error(String message) throws TypeException {
      try {
         throw new TypeException(message);
      } catch (TypeException var2) {
         throw var2;
      }
   }

   public static void computeApprox(TreeSet<TypeVariable> workList) throws TypeException {
      while(workList.size() > 0) {
         TypeVariable var = (TypeVariable)workList.first();
         workList.remove(var);
         var.fixApprox(workList);
      }

   }

   public static void computeInvApprox(TreeSet<TypeVariable> workList) throws TypeException {
      while(workList.size() > 0) {
         TypeVariable var = (TypeVariable)workList.first();
         workList.remove(var);
         var.fixInvApprox(workList);
      }

   }

   private void fixApprox(TreeSet<TypeVariable> workList) throws TypeException {
      if (this.rep != this) {
         this.ecr().fixApprox(workList);
      } else {
         Iterator var2 = this.parents.iterator();

         while(var2.hasNext()) {
            TypeVariable typeVariable = (TypeVariable)var2.next();
            TypeVariable parent = typeVariable.ecr();
            if (parent.approx == null) {
               parent.approx = this.approx;
               workList.add(parent);
            } else {
               TypeNode type = parent.approx.lca_2(this.approx);
               if (type != parent.approx) {
                  parent.approx = type;
                  workList.add(parent);
               }
            }
         }

         if (this.type != null) {
            this.approx = this.type;
         }

      }
   }

   private void fixInvApprox(TreeSet<TypeVariable> workList) throws TypeException {
      if (this.rep != this) {
         this.ecr().fixInvApprox(workList);
      } else {
         Iterator var2 = this.children.iterator();

         while(var2.hasNext()) {
            TypeVariable typeVariable = (TypeVariable)var2.next();
            TypeVariable child = typeVariable.ecr();
            if (child.inv_approx == null) {
               child.inv_approx = this.inv_approx;
               workList.add(child);
            } else {
               TypeNode type = child.inv_approx.gcd_2(this.inv_approx);
               if (type != child.inv_approx) {
                  child.inv_approx = type;
                  workList.add(child);
               }
            }
         }

         if (this.type != null) {
            this.inv_approx = this.type;
         }

      }
   }

   public String toString() {
      if (this.rep != this) {
         return this.ecr().toString();
      } else {
         StringBuffer s = new StringBuffer();
         s.append(",[parents:");
         boolean comma = false;

         Iterator var3;
         TypeVariable typeVariable;
         for(var3 = this.parents.iterator(); var3.hasNext(); s.append(typeVariable.id())) {
            typeVariable = (TypeVariable)var3.next();
            if (comma) {
               s.append(",");
            } else {
               comma = true;
            }
         }

         s.append("],[children:");
         comma = false;

         for(var3 = this.children.iterator(); var3.hasNext(); s.append(typeVariable.id())) {
            typeVariable = (TypeVariable)var3.next();
            if (comma) {
               s.append(",");
            } else {
               comma = true;
            }
         }

         s.append("]");
         return "[id:" + this.id + (this.type != null ? ",type:" + this.type : "") + ",approx:" + this.approx + ",inv_approx:" + this.inv_approx + s + "]";
      }
   }

   public void fixParents() {
      if (this.rep != this) {
         this.ecr().fixParents();
      } else {
         Set<TypeVariable> set = new TreeSet(this.parents);
         this.parents = Collections.unmodifiableList(new LinkedList(set));
      }
   }

   public void fixChildren() {
      if (this.rep != this) {
         this.ecr().fixChildren();
      } else {
         Set<TypeVariable> set = new TreeSet(this.children);
         this.children = Collections.unmodifiableList(new LinkedList(set));
      }
   }
}
