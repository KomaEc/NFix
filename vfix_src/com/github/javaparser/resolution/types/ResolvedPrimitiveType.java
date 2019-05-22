package com.github.javaparser.resolution.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public enum ResolvedPrimitiveType implements ResolvedType {
   BYTE("byte", Byte.class.getCanonicalName(), Collections.emptyList()),
   SHORT("short", Short.class.getCanonicalName(), Collections.singletonList(BYTE)),
   CHAR("char", Character.class.getCanonicalName(), Collections.emptyList()),
   INT("int", Integer.class.getCanonicalName(), Arrays.asList(BYTE, SHORT, CHAR)),
   LONG("long", Long.class.getCanonicalName(), Arrays.asList(BYTE, SHORT, INT, CHAR)),
   BOOLEAN("boolean", Boolean.class.getCanonicalName(), Collections.emptyList()),
   FLOAT("float", Float.class.getCanonicalName(), Arrays.asList(LONG, INT, SHORT, BYTE, CHAR)),
   DOUBLE("double", Double.class.getCanonicalName(), Arrays.asList(FLOAT, LONG, INT, SHORT, BYTE, CHAR));

   private String name;
   private String boxTypeQName;
   private List<ResolvedPrimitiveType> promotionTypes;

   private ResolvedPrimitiveType(String name, String boxTypeQName, List<ResolvedPrimitiveType> promotionTypes) {
      this.name = name;
      this.boxTypeQName = boxTypeQName;
      this.promotionTypes = promotionTypes;
   }

   public static ResolvedType byName(String name) {
      name = name.toLowerCase();
      ResolvedPrimitiveType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ResolvedPrimitiveType ptu = var1[var3];
         if (ptu.describe().equals(name)) {
            return ptu;
         }
      }

      throw new IllegalArgumentException("Name " + name);
   }

   public String toString() {
      return "PrimitiveTypeUsage{name='" + this.name + '\'' + '}';
   }

   public ResolvedPrimitiveType asPrimitive() {
      return this;
   }

   public boolean isArray() {
      return false;
   }

   public boolean isPrimitive() {
      return true;
   }

   public boolean isReferenceType() {
      return false;
   }

   public String describe() {
      return this.name;
   }

   public boolean isTypeVariable() {
      return false;
   }

   public boolean isAssignableBy(ResolvedType other) {
      if (other.isPrimitive()) {
         return this == other || this.promotionTypes.contains(other);
      } else if (other.isReferenceType()) {
         if (other.asReferenceType().getQualifiedName().equals(this.boxTypeQName)) {
            return true;
         } else {
            Iterator var2 = this.promotionTypes.iterator();

            ResolvedPrimitiveType promotion;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               promotion = (ResolvedPrimitiveType)var2.next();
            } while(!other.asReferenceType().getQualifiedName().equals(promotion.boxTypeQName));

            return true;
         }
      } else {
         return other.isConstraint() && this.isAssignableBy(other.asConstraintType().getBound());
      }
   }

   public String getBoxTypeQName() {
      return this.boxTypeQName;
   }

   public boolean isNumeric() {
      return this != BOOLEAN;
   }
}
