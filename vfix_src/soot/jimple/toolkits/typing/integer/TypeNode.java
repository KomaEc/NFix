package soot.jimple.toolkits.typing.integer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Type;

class TypeNode {
   private static final Logger logger = LoggerFactory.getLogger(TypeNode.class);
   public static final boolean DEBUG = false;
   private final int id;
   private final Type type;

   public TypeNode(int id, Type type) {
      this.id = id;
      this.type = type;
   }

   public int id() {
      return this.id;
   }

   public Type type() {
      return this.type;
   }

   public boolean hasAncestor_1(TypeNode typeNode) {
      return typeNode == this ? true : ClassHierarchy.v().hasAncestor_1(this.id, typeNode.id);
   }

   public boolean hasAncestor_2(TypeNode typeNode) {
      return typeNode == this ? true : ClassHierarchy.v().hasAncestor_2(this.id, typeNode.id);
   }

   public TypeNode lca_1(TypeNode typeNode) {
      return ClassHierarchy.v().lca_1(this.id, typeNode.id);
   }

   public TypeNode lca_2(TypeNode typeNode) {
      return ClassHierarchy.v().lca_2(this.id, typeNode.id);
   }

   public TypeNode gcd_1(TypeNode typeNode) {
      return ClassHierarchy.v().gcd_1(this.id, typeNode.id);
   }

   public TypeNode gcd_2(TypeNode typeNode) {
      return ClassHierarchy.v().gcd_2(this.id, typeNode.id);
   }

   public String toString() {
      if (this.type != null) {
         return this.type + "(" + this.id + ")";
      } else if (this == ClassHierarchy.v().TOP) {
         return "TOP(" + this.id + ")";
      } else if (this == ClassHierarchy.v().R0_1) {
         return "R0_1(" + this.id + ")";
      } else if (this == ClassHierarchy.v().R0_127) {
         return "R0_127(" + this.id + ")";
      } else {
         return this == ClassHierarchy.v().R0_32767 ? "R0_32767(" + this.id + ")" : "ERROR!!!!";
      }
   }
}
