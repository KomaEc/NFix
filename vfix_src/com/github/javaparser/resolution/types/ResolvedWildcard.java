package com.github.javaparser.resolution.types;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import java.util.List;
import java.util.Map;

public class ResolvedWildcard implements ResolvedType {
   public static ResolvedWildcard UNBOUNDED = new ResolvedWildcard((ResolvedWildcard.BoundType)null, (ResolvedType)null);
   private ResolvedWildcard.BoundType type;
   private ResolvedType boundedType;

   private ResolvedWildcard(ResolvedWildcard.BoundType type, ResolvedType boundedType) {
      if (type == null && boundedType != null) {
         throw new IllegalArgumentException();
      } else if (type != null && boundedType == null) {
         throw new IllegalArgumentException();
      } else {
         this.type = type;
         this.boundedType = boundedType;
      }
   }

   public static ResolvedWildcard superBound(ResolvedType type) {
      return new ResolvedWildcard(ResolvedWildcard.BoundType.SUPER, type);
   }

   public static ResolvedWildcard extendsBound(ResolvedType type) {
      return new ResolvedWildcard(ResolvedWildcard.BoundType.EXTENDS, type);
   }

   public String toString() {
      return "WildcardUsage{type=" + this.type + ", boundedType=" + this.boundedType + '}';
   }

   public boolean isWildcard() {
      return true;
   }

   public ResolvedWildcard asWildcard() {
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ResolvedWildcard)) {
         return false;
      } else {
         ResolvedWildcard that = (ResolvedWildcard)o;
         if (this.boundedType != null) {
            if (this.boundedType.equals(that.boundedType)) {
               return this.type == that.type;
            }
         } else if (that.boundedType == null) {
            return this.type == that.type;
         }

         return false;
      }
   }

   public int hashCode() {
      int result = this.type != null ? this.type.hashCode() : 0;
      result = 31 * result + (this.boundedType != null ? this.boundedType.hashCode() : 0);
      return result;
   }

   public String describe() {
      if (this.type == null) {
         return "?";
      } else if (this.type == ResolvedWildcard.BoundType.SUPER) {
         return "? super " + this.boundedType.describe();
      } else if (this.type == ResolvedWildcard.BoundType.EXTENDS) {
         return "? extends " + this.boundedType.describe();
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public boolean isSuper() {
      return this.type == ResolvedWildcard.BoundType.SUPER;
   }

   public boolean isExtends() {
      return this.type == ResolvedWildcard.BoundType.EXTENDS;
   }

   public boolean isBounded() {
      return this.isSuper() || this.isExtends();
   }

   public ResolvedType getBoundedType() {
      if (this.boundedType == null) {
         throw new IllegalStateException();
      } else {
         return this.boundedType;
      }
   }

   public boolean isAssignableBy(ResolvedType other) {
      if (this.boundedType == null) {
         return false;
      } else if (this.type == ResolvedWildcard.BoundType.SUPER) {
         return this.boundedType.isAssignableBy(other);
      } else if (this.type == ResolvedWildcard.BoundType.EXTENDS) {
         return false;
      } else {
         throw new RuntimeException();
      }
   }

   public ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tpToReplace, ResolvedType replaced, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      if (replaced == null) {
         throw new IllegalArgumentException();
      } else if (this.boundedType == null) {
         return this;
      } else {
         ResolvedType boundedTypeReplaced = this.boundedType.replaceTypeVariables(tpToReplace, replaced, inferredTypes);
         if (boundedTypeReplaced == null) {
            throw new RuntimeException();
         } else {
            return boundedTypeReplaced != this.boundedType ? new ResolvedWildcard(this.type, boundedTypeReplaced) : this;
         }
      }
   }

   public boolean mention(List<ResolvedTypeParameterDeclaration> typeParameters) {
      return this.boundedType != null && this.boundedType.mention(typeParameters);
   }

   public boolean isUpperBounded() {
      return this.isSuper();
   }

   public boolean isLowerBounded() {
      return this.isExtends();
   }

   public static enum BoundType {
      SUPER,
      EXTENDS;
   }
}
