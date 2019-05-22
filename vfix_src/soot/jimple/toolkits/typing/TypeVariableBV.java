package soot.jimple.toolkits.typing;

import java.util.Iterator;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.options.Options;
import soot.util.BitSetIterator;
import soot.util.BitVector;

/** @deprecated */
@Deprecated
class TypeVariableBV implements Comparable<Object> {
   private static final Logger logger = LoggerFactory.getLogger(TypeVariableBV.class);
   private static final boolean DEBUG = false;
   private final int id;
   private final TypeResolverBV resolver;
   private TypeVariableBV rep = this;
   private int rank = 0;
   private TypeNode approx;
   private TypeNode type;
   private TypeVariableBV array;
   private TypeVariableBV element;
   private int depth;
   private BitVector parents = new BitVector();
   private BitVector children = new BitVector();
   private BitVector ancestors;
   private BitVector indirectAncestors;

   public TypeVariableBV(int id, TypeResolverBV resolver) {
      this.id = id;
      this.resolver = resolver;
   }

   public TypeVariableBV(int id, TypeResolverBV resolver, TypeNode type) {
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
         TypeVariableBV ecr = ((TypeVariableBV)obj).ecr();
         return ecr == this;
      }
   }

   public int compareTo(Object o) {
      return this.rep != this ? this.ecr().compareTo(o) : this.id - ((TypeVariableBV)o).ecr().id;
   }

   private TypeVariableBV ecr() {
      if (this.rep != this) {
         this.rep = this.rep.ecr();
      }

      return this.rep;
   }

   public TypeVariableBV union(TypeVariableBV var) throws TypeException {
      if (this.rep != this) {
         return this.ecr().union(var);
      } else {
         TypeVariableBV y = var.ecr();
         if (this == y) {
            this.parents.clear(var.ownId());
            this.children.clear(var.ownId());
            return this;
         } else if (this.rank > y.rank) {
            this.resolver.invalidateId(y.id());
            y.rep = this;
            this.merge(y);
            y.clear();
            return this;
         } else {
            this.resolver.invalidateId(this.id());
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

   private void merge(TypeVariableBV var) throws TypeException {
      if (this.depth == 0 && var.depth == 0) {
         if (this.type == null) {
            this.type = var.type;
         } else if (var.type != null) {
            error("Type Error(1): Attempt to merge two types.");
         }

         this.parents.or(var.parents);
         this.parents.clear(var.ownId());
         this.parents.clear(this.ownId());
         this.children.or(var.children);
         this.children.clear(var.ownId());
         this.children.clear(this.ownId());
      } else {
         throw new InternalTypingException();
      }
   }

   void validate() throws TypeException {
      if (this.rep != this) {
         this.ecr().validate();
      } else {
         if (this.type != null) {
            BitSetIterator i = this.parents.iterator();

            TypeVariableBV child;
            while(i.hasNext()) {
               child = this.resolver.typeVariableForId(i.next()).ecr();
               if (child.type != null && !this.type.hasAncestor(child.type)) {
                  error("Type Error(2): Parent type is not a valid ancestor.");
               }
            }

            i = this.children.iterator();

            while(i.hasNext()) {
               child = this.resolver.typeVariableForId(i.next()).ecr();
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

         BitVector parentsToRemove = new BitVector();
         BitSetIterator i = this.parents.iterator();

         while(i.hasNext()) {
            int parent = i.next();
            if (this.indirectAncestors.get(parent)) {
               parentsToRemove.set(parent);
            }
         }

         i = parentsToRemove.iterator();

         while(i.hasNext()) {
            this.removeParent(this.resolver.typeVariableForId(i.next()));
         }

      }
   }

   private void fixAncestors() {
      BitVector ancestors = new BitVector(0);
      BitVector indirectAncestors = new BitVector(0);
      this.fixParents();
      BitSetIterator i = this.parents.iterator();

      while(i.hasNext()) {
         TypeVariableBV parent = this.resolver.typeVariableForId(i.next()).ecr();
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

   private void fixParents() {
      if (this.rep != this) {
         this.ecr().fixParents();
      }

      BitVector invalid = new BitVector();
      invalid.or(this.parents);
      invalid.and(this.resolver.invalidIds());
      BitSetIterator i = invalid.iterator();

      while(i.hasNext()) {
         this.parents.set(this.resolver.typeVariableForId(i.next()).id());
      }

      this.parents.clear(this.id);
      this.parents.clear(this.id());
      this.parents.andNot(invalid);
   }

   public int id() {
      return this.rep != this ? this.ecr().id() : this.id;
   }

   public int ownId() {
      return this.id;
   }

   public void addParent(TypeVariableBV variable) {
      if (this.rep != this) {
         this.ecr().addParent(variable);
      } else {
         TypeVariableBV var = variable.ecr();
         if (var != this) {
            this.parents.set(var.id);
            var.children.set(this.id);
         }
      }
   }

   public void removeParent(TypeVariableBV variable) {
      if (this.rep != this) {
         this.ecr().removeParent(variable);
      } else {
         this.parents.clear(variable.id());
         this.parents.clear(variable.ownId());
         variable.children().clear(this.id);
      }
   }

   public void addChild(TypeVariableBV variable) {
      if (this.rep != this) {
         this.ecr().addChild(variable);
      } else {
         TypeVariableBV var = variable.ecr();
         if (var != this) {
            this.children.set(var.id);
            this.parents.set(var.id);
         }
      }
   }

   public void removeChild(TypeVariableBV variable) {
      if (this.rep != this) {
         this.ecr().removeChild(variable);
      } else {
         TypeVariableBV var = variable.ecr();
         this.children.clear(var.id);
         var.parents.clear(var.id);
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

   public TypeVariableBV element() {
      if (this.rep != this) {
         return this.ecr().element();
      } else {
         return this.element == null ? null : this.element.ecr();
      }
   }

   public TypeVariableBV array() {
      if (this.rep != this) {
         return this.ecr().array();
      } else {
         return this.array == null ? null : this.array.ecr();
      }
   }

   public BitVector parents() {
      return this.rep != this ? this.ecr().parents() : this.parents;
   }

   public BitVector children() {
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

   public static void computeApprox(TreeSet<TypeVariableBV> workList) throws TypeException {
      while(workList.size() > 0) {
         TypeVariableBV var = (TypeVariableBV)workList.first();
         workList.remove(var);
         var.fixApprox(workList);
      }

   }

   private void fixApprox(TreeSet<TypeVariableBV> workList) throws TypeException {
      if (this.rep != this) {
         this.ecr().fixApprox(workList);
      } else {
         TypeNode type;
         TypeVariableBV array;
         if (this.type == null && this.approx != this.resolver.hierarchy().NULL) {
            TypeVariableBV element = this.element();
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
                  TypeNode type = array.approx.lca(type);
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

         BitSetIterator i = this.parents.iterator();

         while(i.hasNext()) {
            array = this.resolver.typeVariableForId(i.next()).ecr();
            if (array.approx == null) {
               array.approx = this.approx;
               workList.add(array);
            } else {
               type = array.approx.lca(this.approx);
               if (type != array.approx) {
                  array.approx = type;
                  workList.add(array);
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
            TypeVariableBV element = this.element();

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
         BitSetIterator varIt = this.parents.iterator();

         TypeVariableBV var;
         while(varIt.hasNext()) {
            var = this.resolver.typeVariableForId(varIt.next()).ecr();
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

         varIt = this.parents.iterator();

         while(varIt.hasNext()) {
            var = this.resolver.typeVariableForId(varIt.next());
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

         BitSetIterator i;
         for(i = this.parents.iterator(); i.hasNext(); s.append(i.next())) {
            if (comma) {
               s.append(",");
            } else {
               comma = true;
            }
         }

         s.append("],[children:");
         comma = false;

         for(i = this.children.iterator(); i.hasNext(); s.append(i.next())) {
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
}
