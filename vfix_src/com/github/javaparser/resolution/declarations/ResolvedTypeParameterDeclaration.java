package com.github.javaparser.resolution.declarations;

import com.github.javaparser.resolution.types.ResolvedType;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface ResolvedTypeParameterDeclaration extends ResolvedTypeDeclaration {
   static ResolvedTypeParameterDeclaration onType(final String name, String classQName, List<ResolvedTypeParameterDeclaration.Bound> bounds) {
      return new ResolvedTypeParameterDeclaration() {
         public String getName() {
            return name;
         }

         public boolean declaredOnType() {
            return true;
         }

         public boolean declaredOnMethod() {
            return false;
         }

         public boolean declaredOnConstructor() {
            return false;
         }

         public String getContainerQualifiedName() {
            return classQName;
         }

         public String getContainerId() {
            return classQName;
         }

         public ResolvedTypeParametrizable getContainer() {
            return null;
         }

         public List<ResolvedTypeParameterDeclaration.Bound> getBounds() {
            return bounds;
         }

         public String toString() {
            return "TypeParameter onType " + name;
         }

         public Optional<ResolvedReferenceTypeDeclaration> containerType() {
            throw new UnsupportedOperationException();
         }
      };
   }

   String getName();

   default boolean declaredOnType() {
      return this.getContainer() instanceof ResolvedReferenceTypeDeclaration;
   }

   default boolean declaredOnMethod() {
      return this.getContainer() instanceof ResolvedMethodDeclaration;
   }

   default boolean declaredOnConstructor() {
      return this.getContainer() instanceof ResolvedConstructorDeclaration;
   }

   default String getPackageName() {
      throw new UnsupportedOperationException();
   }

   default String getClassName() {
      throw new UnsupportedOperationException();
   }

   default String getQualifiedName() {
      return String.format("%s.%s", this.getContainerId(), this.getName());
   }

   String getContainerQualifiedName();

   String getContainerId();

   ResolvedTypeParametrizable getContainer();

   List<ResolvedTypeParameterDeclaration.Bound> getBounds();

   default boolean hasLowerBound() {
      Iterator var1 = this.getBounds().iterator();

      ResolvedTypeParameterDeclaration.Bound b;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         b = (ResolvedTypeParameterDeclaration.Bound)var1.next();
      } while(!b.isExtends());

      return true;
   }

   default boolean hasUpperBound() {
      Iterator var1 = this.getBounds().iterator();

      ResolvedTypeParameterDeclaration.Bound b;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         b = (ResolvedTypeParameterDeclaration.Bound)var1.next();
      } while(!b.isSuper());

      return true;
   }

   default ResolvedType getLowerBound() {
      Iterator var1 = this.getBounds().iterator();

      ResolvedTypeParameterDeclaration.Bound b;
      do {
         if (!var1.hasNext()) {
            throw new IllegalStateException();
         }

         b = (ResolvedTypeParameterDeclaration.Bound)var1.next();
      } while(!b.isExtends());

      return b.getType();
   }

   default ResolvedType getUpperBound() {
      Iterator var1 = this.getBounds().iterator();

      ResolvedTypeParameterDeclaration.Bound b;
      do {
         if (!var1.hasNext()) {
            throw new IllegalStateException();
         }

         b = (ResolvedTypeParameterDeclaration.Bound)var1.next();
      } while(!b.isSuper());

      return b.getType();
   }

   public static class Bound {
      private boolean extendsBound;
      private ResolvedType type;

      private Bound(boolean extendsBound, ResolvedType type) {
         this.extendsBound = extendsBound;
         this.type = type;
      }

      public static ResolvedTypeParameterDeclaration.Bound extendsBound(ResolvedType type) {
         return new ResolvedTypeParameterDeclaration.Bound(true, type);
      }

      public static ResolvedTypeParameterDeclaration.Bound superBound(ResolvedType type) {
         return new ResolvedTypeParameterDeclaration.Bound(false, type);
      }

      public ResolvedType getType() {
         return this.type;
      }

      public boolean isExtends() {
         return this.extendsBound;
      }

      public boolean isSuper() {
         return !this.isExtends();
      }

      public String toString() {
         return "Bound{extendsBound=" + this.extendsBound + ", type=" + this.type + '}';
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            ResolvedTypeParameterDeclaration.Bound bound = (ResolvedTypeParameterDeclaration.Bound)o;
            if (this.extendsBound != bound.extendsBound) {
               return false;
            } else {
               return this.type != null ? this.type.equals(bound.type) : bound.type == null;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.extendsBound ? 1 : 0;
         result = 31 * result + (this.type != null ? this.type.hashCode() : 0);
         return result;
      }
   }
}
