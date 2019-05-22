package soot.jimple.toolkits.typing;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.SootClass;
import soot.Type;
import soot.options.Options;
import soot.util.BitVector;

class TypeNode {
   private static final Logger logger = LoggerFactory.getLogger(TypeNode.class);
   private static final boolean DEBUG = false;
   private final int id;
   private final Type type;
   private final ClassHierarchy hierarchy;
   private TypeNode parentClass;
   private TypeNode element;
   private TypeNode array;
   private List<TypeNode> parents;
   private final BitVector ancestors;
   private final BitVector descendants;

   public TypeNode(int id, Type type, ClassHierarchy hierarchy) {
      this.parents = Collections.emptyList();
      this.ancestors = new BitVector(0);
      this.descendants = new BitVector(0);
      if (type != null && hierarchy != null) {
         if (!(type instanceof PrimType) && !(type instanceof RefType) && !(type instanceof ArrayType) && !(type instanceof NullType)) {
            logger.debug("Unhandled type: " + type);
            throw new InternalTypingException();
         } else {
            this.id = id;
            this.type = type;
            this.hierarchy = hierarchy;
         }
      } else {
         throw new InternalTypingException();
      }
   }

   public TypeNode(int id, RefType type, ClassHierarchy hierarchy) {
      this(id, (Type)type, hierarchy);
      SootClass sClass = type.getSootClass();
      if (sClass == null) {
         throw new RuntimeException("Oops, forgot to load " + type);
      } else if (sClass.isPhantomClass()) {
         throw new RuntimeException("Jimplification requires " + sClass + ", but it is a phantom ref.");
      } else {
         List<TypeNode> plist = new LinkedList();
         SootClass superclass = sClass.getSuperclassUnsafe();
         if (superclass != null && !sClass.getName().equals("java.lang.Object")) {
            TypeNode parent = hierarchy.typeNode(RefType.v(sClass.getSuperclass().getName()));
            plist.add(parent);
            this.parentClass = parent;
         }

         Iterator i = sClass.getInterfaces().iterator();

         while(i.hasNext()) {
            TypeNode parent = hierarchy.typeNode(RefType.v(((SootClass)i.next()).getName()));
            plist.add(parent);
         }

         this.parents = Collections.unmodifiableList(plist);
         this.descendants.set(hierarchy.NULL.id);
         hierarchy.NULL.ancestors.set(id);
         Iterator parentIt = this.parents.iterator();

         while(parentIt.hasNext()) {
            TypeNode parent = (TypeNode)parentIt.next();
            this.ancestors.set(parent.id);
            this.ancestors.or(parent.ancestors);
            parent.fixDescendants(id);
         }

      }
   }

   public TypeNode(int id, ArrayType type, ClassHierarchy hierarchy) {
      this(id, (Type)type, hierarchy);
      if (type.numDimensions < 1) {
         throw new InternalTypingException();
      } else {
         if (type.numDimensions == 1) {
            this.element = hierarchy.typeNode(type.baseType);
         } else {
            this.element = hierarchy.typeNode(ArrayType.v(type.baseType, type.numDimensions - 1));
         }

         if (this.element != hierarchy.INT) {
            if (this.element.array != null) {
               throw new InternalTypingException();
            }

            this.element.array = this;
         }

         List<TypeNode> plist = new LinkedList();
         if (type.baseType instanceof RefType) {
            RefType baseType = (RefType)type.baseType;
            SootClass sClass = baseType.getSootClass();
            SootClass superClass = sClass.getSuperclassUnsafe();
            if (superClass != null && !superClass.getName().equals("java.lang.Object")) {
               TypeNode parent = hierarchy.typeNode(ArrayType.v(RefType.v(sClass.getSuperclass().getName()), type.numDimensions));
               plist.add(parent);
               this.parentClass = parent;
            } else if (type.numDimensions == 1) {
               plist.add(hierarchy.OBJECT);
               if (!Options.v().j2me()) {
                  plist.add(hierarchy.CLONEABLE);
                  plist.add(hierarchy.SERIALIZABLE);
               }

               this.parentClass = hierarchy.OBJECT;
            } else {
               plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1)));
               if (!Options.v().j2me()) {
                  plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.CLONEABLE.type(), type.numDimensions - 1)));
                  plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.SERIALIZABLE.type(), type.numDimensions - 1)));
               }

               this.parentClass = hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1));
            }

            Iterator i = sClass.getInterfaces().iterator();

            while(i.hasNext()) {
               TypeNode parent = hierarchy.typeNode(ArrayType.v(RefType.v(((SootClass)i.next()).getName()), type.numDimensions));
               plist.add(parent);
            }
         } else if (type.numDimensions == 1) {
            plist.add(hierarchy.OBJECT);
            if (!Options.v().j2me()) {
               plist.add(hierarchy.CLONEABLE);
               plist.add(hierarchy.SERIALIZABLE);
            }

            this.parentClass = hierarchy.OBJECT;
         } else {
            plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1)));
            if (!Options.v().j2me()) {
               plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.CLONEABLE.type(), type.numDimensions - 1)));
               plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.SERIALIZABLE.type(), type.numDimensions - 1)));
            }

            this.parentClass = hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1));
         }

         this.parents = Collections.unmodifiableList(plist);
         this.descendants.set(hierarchy.NULL.id);
         hierarchy.NULL.ancestors.set(id);
         Iterator parentIt = this.parents.iterator();

         while(parentIt.hasNext()) {
            TypeNode parent = (TypeNode)parentIt.next();
            this.ancestors.set(parent.id);
            this.ancestors.or(parent.ancestors);
            parent.fixDescendants(id);
         }

      }
   }

   private void fixDescendants(int id) {
      if (!this.descendants.get(id)) {
         Iterator parentIt = this.parents.iterator();

         while(parentIt.hasNext()) {
            TypeNode parent = (TypeNode)parentIt.next();
            parent.fixDescendants(id);
         }

         this.descendants.set(id);
      }
   }

   public int id() {
      return this.id;
   }

   public Type type() {
      return this.type;
   }

   public boolean hasAncestor(TypeNode typeNode) {
      return this.ancestors.get(typeNode.id);
   }

   public boolean hasAncestorOrSelf(TypeNode typeNode) {
      return typeNode == this ? true : this.ancestors.get(typeNode.id);
   }

   public boolean hasDescendant(TypeNode typeNode) {
      return this.descendants.get(typeNode.id);
   }

   public boolean hasDescendantOrSelf(TypeNode typeNode) {
      return typeNode == this ? true : this.descendants.get(typeNode.id);
   }

   public List<TypeNode> parents() {
      return this.parents;
   }

   public TypeNode parentClass() {
      return this.parentClass;
   }

   public String toString() {
      return this.type.toString() + "(" + this.id + ")";
   }

   public TypeNode lca(TypeNode type) throws TypeException {
      if (type == null) {
         throw new InternalTypingException();
      } else if (type == this) {
         return this;
      } else if (this.hasAncestor(type)) {
         return type;
      } else if (this.hasDescendant(type)) {
         return this;
      } else {
         do {
            type = type.parentClass;
            if (type == null) {
               try {
                  TypeVariable.error("Type Error(12)");
               } catch (TypeException var3) {
                  throw var3;
               }
            }
         } while(!this.hasAncestor(type));

         return type;
      }
   }

   public TypeNode lcaIfUnique(TypeNode type) throws TypeException {
      if (type == null) {
         throw new InternalTypingException();
      } else if (type == this) {
         return this;
      } else if (this.hasAncestor(type)) {
         return type;
      } else if (this.hasDescendant(type)) {
         return this;
      } else {
         do {
            if (type.parents.size() != 1) {
               return null;
            }

            type = (TypeNode)type.parents.get(0);
         } while(!this.hasAncestor(type));

         return type;
      }
   }

   public boolean hasElement() {
      return this.element != null;
   }

   public TypeNode element() {
      if (this.element == null) {
         throw new InternalTypingException();
      } else {
         return this.element;
      }
   }

   public TypeNode array() {
      if (this.array != null) {
         return this.array;
      } else if (this.type instanceof ArrayType) {
         ArrayType atype = (ArrayType)this.type;
         this.array = this.hierarchy.typeNode(ArrayType.v(atype.baseType, atype.numDimensions + 1));
         return this.array;
      } else if (!(this.type instanceof PrimType) && !(this.type instanceof RefType)) {
         throw new InternalTypingException();
      } else {
         this.array = this.hierarchy.typeNode(ArrayType.v(this.type, 1));
         return this.array;
      }
   }

   public boolean isNull() {
      return this.type instanceof NullType;
   }

   public boolean isClass() {
      return this.type instanceof ArrayType || this.type instanceof NullType || this.type instanceof RefType && !((RefType)this.type).getSootClass().isInterface();
   }

   public boolean isClassOrInterface() {
      return this.type instanceof ArrayType || this.type instanceof NullType || this.type instanceof RefType;
   }

   public boolean isArray() {
      return this.type instanceof ArrayType || this.type instanceof NullType;
   }
}
