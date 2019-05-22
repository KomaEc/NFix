package soot;

import java.util.ArrayDeque;
import soot.util.Switch;

public class RefType extends RefLikeType implements Comparable<RefType> {
   private String className;
   private volatile SootClass sootClass;
   private AnySubType anySubType;

   public RefType(Singletons.Global g) {
      this.className = "";
   }

   public static RefType v() {
      return G.v().soot_RefType();
   }

   public String getClassName() {
      return this.className;
   }

   private RefType(String className) {
      if (className.startsWith("[")) {
         throw new RuntimeException("Attempt to create RefType whose name starts with [ --> " + className);
      } else if (className.indexOf("/") >= 0) {
         throw new RuntimeException("Attempt to create RefType containing a / --> " + className);
      } else if (className.indexOf(";") >= 0) {
         throw new RuntimeException("Attempt to create RefType containing a ; --> " + className);
      } else {
         this.className = className;
      }
   }

   public static RefType v(String className) {
      RefType rt = Scene.v().getRefTypeUnsafe(className);
      if (rt == null) {
         rt = new RefType(className);
         return Scene.v().getOrAddRefType(rt);
      } else {
         return rt;
      }
   }

   public int compareTo(RefType t) {
      return this.toString().compareTo(t.toString());
   }

   public static RefType v(SootClass c) {
      return v(c.getName());
   }

   public SootClass getSootClass() {
      if (this.sootClass == null) {
         this.sootClass = SootResolver.v().makeClassRef(this.className);
      }

      return this.sootClass;
   }

   public boolean hasSootClass() {
      return this.sootClass != null;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public void setSootClass(SootClass sootClass) {
      this.sootClass = sootClass;
   }

   public boolean equals(Object t) {
      return t instanceof RefType && this.className.equals(((RefType)t).className);
   }

   public String toString() {
      return this.className;
   }

   public String toQuotedString() {
      return Scene.v().quotedNameOf(this.className);
   }

   public int hashCode() {
      return this.className.hashCode();
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseRefType(this);
   }

   public Type merge(Type other, Scene cm) {
      if (!other.equals(UnknownType.v()) && !this.equals(other)) {
         if (!(other instanceof RefType)) {
            throw new RuntimeException("illegal type merge: " + this + " and " + other);
         } else {
            SootClass thisClass = cm.getSootClass(this.className);
            SootClass otherClass = cm.getSootClass(((RefType)other).className);
            SootClass javalangObject = cm.getObjectType().getSootClass();
            ArrayDeque<SootClass> thisHierarchy = new ArrayDeque();
            ArrayDeque<SootClass> otherHierarchy = new ArrayDeque();
            SootClass commonClass = thisClass;

            while(commonClass != null) {
               thisHierarchy.addFirst(commonClass);
               if (commonClass == javalangObject) {
                  break;
               }

               commonClass = commonClass.getSuperclassUnsafe();
               if (commonClass == null) {
                  commonClass = javalangObject;
               }
            }

            commonClass = otherClass;

            while(commonClass != null) {
               otherHierarchy.addFirst(commonClass);
               if (commonClass == javalangObject) {
                  break;
               }

               commonClass = commonClass.getSuperclassUnsafe();
               if (commonClass == null) {
                  commonClass = javalangObject;
               }
            }

            commonClass = null;

            while(!otherHierarchy.isEmpty() && !thisHierarchy.isEmpty() && otherHierarchy.getFirst() == thisHierarchy.getFirst()) {
               commonClass = (SootClass)otherHierarchy.removeFirst();
               thisHierarchy.removeFirst();
            }

            if (commonClass == null) {
               throw new RuntimeException("Could not find a common superclass for " + this + " and " + other);
            } else {
               return commonClass.getType();
            }
         }
      } else {
         return this;
      }
   }

   public Type getArrayElementType() {
      if (!this.className.equals("java.lang.Object") && !this.className.equals("java.io.Serializable") && !this.className.equals("java.lang.Cloneable")) {
         throw new RuntimeException("Attempt to get array base type of a non-array");
      } else {
         return v("java.lang.Object");
      }
   }

   public AnySubType getAnySubType() {
      return this.anySubType;
   }

   public void setAnySubType(AnySubType anySubType) {
      this.anySubType = anySubType;
   }

   public boolean isAllowedInFinalCode() {
      return true;
   }
}
