package soot.jimple.toolkits.typing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.G;
import soot.IntType;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Type;
import soot.TypeSwitch;
import soot.options.Options;

public class ClassHierarchy {
   public final TypeNode OBJECT;
   public final TypeNode CLONEABLE;
   public final TypeNode SERIALIZABLE;
   public final TypeNode NULL;
   public final TypeNode INT;
   private final List<TypeNode> typeNodeList = new ArrayList();
   private final HashMap<Type, TypeNode> typeNodeMap = new HashMap();
   private final ClassHierarchy.ToInt transform = new ClassHierarchy.ToInt();
   private final ClassHierarchy.ConstructorChooser make = new ClassHierarchy.ConstructorChooser();

   private ClassHierarchy(Scene scene) {
      if (scene == null) {
         throw new InternalTypingException();
      } else {
         G.v().ClassHierarchy_classHierarchyMap.put(scene, this);
         this.NULL = this.typeNode(NullType.v());
         this.OBJECT = this.typeNode(RefType.v("java.lang.Object"));
         if (!Options.v().j2me()) {
            this.CLONEABLE = this.typeNode(RefType.v("java.lang.Cloneable"));
            this.SERIALIZABLE = this.typeNode(RefType.v("java.io.Serializable"));
         } else {
            this.CLONEABLE = null;
            this.SERIALIZABLE = null;
         }

         this.INT = this.typeNode(IntType.v());
      }
   }

   public static ClassHierarchy classHierarchy(Scene scene) {
      if (scene == null) {
         throw new InternalTypingException();
      } else {
         ClassHierarchy classHierarchy = (ClassHierarchy)G.v().ClassHierarchy_classHierarchyMap.get(scene);
         if (classHierarchy == null) {
            classHierarchy = new ClassHierarchy(scene);
         }

         return classHierarchy;
      }
   }

   public TypeNode typeNode(Type type) {
      if (type == null) {
         throw new InternalTypingException();
      } else {
         type = this.transform.toInt(type);
         TypeNode typeNode = (TypeNode)this.typeNodeMap.get(type);
         if (typeNode == null) {
            int id = this.typeNodeList.size();
            this.typeNodeList.add((Object)null);
            typeNode = this.make.typeNode(id, type, this);
            this.typeNodeList.set(id, typeNode);
            this.typeNodeMap.put(type, typeNode);
         }

         return typeNode;
      }
   }

   public String toString() {
      StringBuffer s = new StringBuffer();
      boolean colon = false;
      s.append("ClassHierarchy:{");

      TypeNode typeNode;
      for(Iterator var3 = this.typeNodeList.iterator(); var3.hasNext(); s.append(typeNode)) {
         typeNode = (TypeNode)var3.next();
         if (colon) {
            s.append(",");
         } else {
            colon = true;
         }
      }

      s.append("}");
      return s.toString();
   }

   private static class ConstructorChooser extends TypeSwitch {
      private int id;
      private ClassHierarchy hierarchy;
      private TypeNode result;

      ConstructorChooser() {
      }

      TypeNode typeNode(int id, Type type, ClassHierarchy hierarchy) {
         if (type != null && hierarchy != null) {
            this.id = id;
            this.hierarchy = hierarchy;
            type.apply(this);
            return this.result;
         } else {
            throw new InternalTypingException();
         }
      }

      public void caseRefType(RefType type) {
         this.result = new TypeNode(this.id, type, this.hierarchy);
      }

      public void caseArrayType(ArrayType type) {
         this.result = new TypeNode(this.id, type, this.hierarchy);
      }

      public void defaultCase(Type type) {
         this.result = new TypeNode(this.id, type, this.hierarchy);
      }
   }

   private static class ToInt extends TypeSwitch {
      private Type result;
      private final Type intType;

      private ToInt() {
         this.intType = IntType.v();
      }

      Type toInt(Type type) {
         type.apply(this);
         return this.result;
      }

      public void caseBooleanType(BooleanType type) {
         this.result = this.intType;
      }

      public void caseByteType(ByteType type) {
         this.result = this.intType;
      }

      public void caseShortType(ShortType type) {
         this.result = this.intType;
      }

      public void caseCharType(CharType type) {
         this.result = this.intType;
      }

      public void defaultCase(Type type) {
         this.result = type;
      }

      // $FF: synthetic method
      ToInt(Object x0) {
         this();
      }
   }
}
