package soot.jimple.toolkits.typing;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.options.Options;
import soot.util.BitVector;

class TypeVariable implements Comparable<Object> {
   private static final Logger logger = LoggerFactory.getLogger(TypeVariable.class);
   private static final boolean DEBUG = false;
   private final int id;
   private final TypeResolver resolver;
   private TypeVariable rep = this;
   private int rank = 0;
   private TypeNode approx;
   private TypeNode type;
   private TypeVariable array;
   private TypeVariable element;
   private int depth;
   private List<TypeVariable> parents = Collections.emptyList();
   private List<TypeVariable> children = Collections.emptyList();
   private BitVector ancestors;
   private BitVector indirectAncestors;

   public TypeVariable(int id, TypeResolver resolver) {
      this.id = id;
      this.resolver = resolver;
   }

   public TypeVariable(int id, TypeResolver resolver, TypeNode type) {
      this.id = id;
      this.resolver = resolver;
      this.type = type;
      this.approx = type;
      Iterator parentIt = type.parents().iterator();

      while(parentIt.hasNext()) {
         TypeNode parent = (TypeNode)parentIt.next();
         this.addParent(resolver.typeVariable(parent));
      }

      if (type.hasElement()) {
         this.element = resolver.typeVariable(type.element());
         this.element.array = this;
      }

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
      this.approx = null;
      this.type = null;
      this.element = null;
      this.array = null;
      this.parents = null;
      this.children = null;
      this.ancestors = null;
      this.indirectAncestors = null;
   }

   private void merge(TypeVariable var) throws TypeException {
      if (this.depth == 0 && var.depth == 0) {
         if (this.type == null) {
            this.type = var.type;
         } else if (var.type != null) {
            error("Type Error(1): Attempt to merge two types.");
         }

         Set<TypeVariable> set = new TreeSet(this.parents);
         set.addAll(var.parents);
         set.remove(this);
         this.parents = Collections.unmodifiableList(new LinkedList(set));
         set = new TreeSet(this.children);
         set.addAll(var.children);
         set.remove(this);
         this.children = Collections.unmodifiableList(new LinkedList(set));
      } else {
         throw new InternalTypingException();
      }
   }

   void validate() throws TypeException {
      if (this.rep != this) {
         this.ecr().validate();
      } else {
         if (this.type != null) {
            Iterator var1 = this.parents.iterator();

            TypeVariable typeVariable;
            TypeVariable child;
            while(var1.hasNext()) {
               typeVariable = (TypeVariable)var1.next();
               child = typeVariable.ecr();
               if (child.type != null && !this.type.hasAncestor(child.type)) {
                  error("Type Error(2): Parent type is not a valid ancestor.");
               }
            }

            var1 = this.children.iterator();

            while(var1.hasNext()) {
               typeVariable = (TypeVariable)var1.next();
               child = typeVariable.ecr();
               if (child.type != null && !this.type.hasDescendant(child.type)) {
                  error("Type Error(3): Child type is not a valid descendant.");
               }
            }
         }

      }
   }

   public void removeIndirectRelations() {
      if (this.rep != this) {
         this.ecr().removeIndirectRelations();
      } else {
         if (this.indirectAncestors == null) {
            this.fixAncestors();
         }

         List<TypeVariable> parentsToRemove = new LinkedList();
         Iterator var2 = this.parents.iterator();

         TypeVariable parent;
         while(var2.hasNext()) {
            parent = (TypeVariable)var2.next();
            if (this.indirectAncestors.get(parent.id())) {
               parentsToRemove.add(parent);
            }
         }

         var2 = parentsToRemove.iterator();

         while(var2.hasNext()) {
            parent = (TypeVariable)var2.next();
            this.removeParent(parent);
         }

      }
   }

   private void fixAncestors() {
      BitVector ancestors = new BitVector(0);
      BitVector indirectAncestors = new BitVector(0);
      Iterator var3 = this.parents.iterator();

      while(var3.hasNext()) {
         TypeVariable typeVariable = (TypeVariable)var3.next();
         TypeVariable parent = typeVariable.ecr();
         if (parent.ancestors == null) {
            parent.fixAncestors();
         }

         ancestors.set(parent.id);
         ancestors.or(parent.ancestors);
         indirectAncestors.or(parent.ancestors);
      }

      this.ancestors = ancestors;
      this.indirectAncestors = indirectAncestors;
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

   public int depth() {
      return this.rep != this ? this.ecr().depth() : this.depth;
   }

   public void makeElement() {
      if (this.rep != this) {
         this.ecr().makeElement();
      } else {
         if (this.element == null) {
            this.element = this.resolver.typeVariable();
            this.element.array = this;
         }

      }
   }

   public TypeVariable element() {
      if (this.rep != this) {
         return this.ecr().element();
      } else {
         return this.element == null ? null : this.element.ecr();
      }
   }

   public TypeVariable array() {
      if (this.rep != this) {
         return this.ecr().array();
      } else {
         return this.array == null ? null : this.array.ecr();
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

   private void fixApprox(TreeSet<TypeVariable> workList) throws TypeException {
      if (this.rep != this) {
         this.ecr().fixApprox(workList);
      } else {
         TypeNode type;
         TypeVariable array;
         if (this.type == null && this.approx != this.resolver.hierarchy().NULL) {
            TypeVariable element = this.element();
            TypeNode type;
            if (element != null) {
               if (!this.approx.hasElement()) {
                  logger.debug("*** " + this + " ***");
                  error("Type Error(4)");
               }

               TypeNode temp = this.approx.element();
               if (element.approx == null) {
                  element.approx = temp;
                  workList.add(element);
               } else {
                  type = element.approx.lca(temp);
                  if (type != element.approx) {
                     element.approx = type;
                     workList.add(element);
                  } else if (element.approx != this.resolver.hierarchy().INT) {
                     type = this.approx.lca(element.approx.array());
                     if (type != this.approx) {
                        this.approx = type;
                        workList.add(this);
                     }
                  }
               }
            }

            array = this.array();
            if (array != null && this.approx != this.resolver.hierarchy().NULL && this.approx != this.resolver.hierarchy().INT) {
               type = this.approx.array();
               if (array.approx == null) {
                  array.approx = type;
                  workList.add(array);
               } else {
                  type = array.approx.lca(type);
                  if (type != array.approx) {
                     array.approx = type;
                     workList.add(array);
                  } else {
                     type = this.approx.lca(array.approx.element());
                     if (type != this.approx) {
                        this.approx = type;
                        workList.add(this);
                     }
                  }
               }
            }
         }

         Iterator var6 = this.parents.iterator();

         while(var6.hasNext()) {
            array = (TypeVariable)var6.next();
            TypeVariable parent = array.ecr();
            if (parent.approx == null) {
               parent.approx = this.approx;
               workList.add(parent);
            } else {
               type = parent.approx.lca(this.approx);
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

   public void fixDepth() throws TypeException {
      if (this.rep != this) {
         this.ecr().fixDepth();
      } else {
         ArrayType at;
         if (this.type != null) {
            if (this.type.type() instanceof ArrayType) {
               at = (ArrayType)this.type.type();
               this.depth = at.numDimensions;
            } else {
               this.depth = 0;
            }
         } else if (this.approx.type() instanceof ArrayType) {
            at = (ArrayType)this.approx.type();
            this.depth = at.numDimensions;
         } else {
            this.depth = 0;
         }

         if (this.depth == 0 && this.element() != null) {
            error("Type Error(11)");
         } else if (this.depth > 0 && this.element() == null) {
            this.makeElement();
            TypeVariable element = this.element();

            for(element.depth = this.depth - 1; element.depth != 0; element = element.element()) {
               element.makeElement();
               element.element().depth = element.depth - 1;
            }
         }

      }
   }

   public void propagate() {
      if (this.rep != this) {
         this.ecr().propagate();
      }

      if (this.depth != 0) {
         Iterator var1 = this.parents.iterator();

         TypeVariable var;
         while(var1.hasNext()) {
            var = (TypeVariable)var1.next();
            TypeVariable var = var.ecr();
            if (var.depth() == this.depth) {
               this.element().addParent(var.element());
            } else if (var.depth() == 0) {
               if (var.type() == null && !Options.v().j2me()) {
                  var.addChild(this.resolver.typeVariable(this.resolver.hierarchy().CLONEABLE));
                  var.addChild(this.resolver.typeVariable(this.resolver.hierarchy().SERIALIZABLE));
               }
            } else if (var.type() == null && !Options.v().j2me()) {
               var.addChild(this.resolver.typeVariable((Type)ArrayType.v(RefType.v("java.lang.Cloneable"), var.depth())));
               var.addChild(this.resolver.typeVariable((Type)ArrayType.v(RefType.v("java.io.Serializable"), var.depth())));
            }
         }

         var1 = this.parents.iterator();

         while(var1.hasNext()) {
            var = (TypeVariable)var1.next();
            this.removeParent(var);
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
         return "[id:" + this.id + ",depth:" + this.depth + (this.type != null ? ",type:" + this.type : "") + ",approx:" + this.approx + s + (this.element == null ? "" : ",arrayof:" + this.element.id()) + "]";
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
