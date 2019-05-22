package com.github.javaparser.resolution.types;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.parametrization.ResolvedTypeParameterValueProvider;
import com.github.javaparser.resolution.types.parametrization.ResolvedTypeParametersMap;
import com.github.javaparser.resolution.types.parametrization.ResolvedTypeParametrized;
import com.github.javaparser.utils.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ResolvedReferenceType implements ResolvedType, ResolvedTypeParametrized, ResolvedTypeParameterValueProvider {
   protected ResolvedReferenceTypeDeclaration typeDeclaration;
   protected ResolvedTypeParametersMap typeParametersMap;

   public ResolvedReferenceType(ResolvedReferenceTypeDeclaration typeDeclaration) {
      this(typeDeclaration, deriveParams(typeDeclaration));
   }

   public ResolvedReferenceType(ResolvedReferenceTypeDeclaration typeDeclaration, List<ResolvedType> typeArguments) {
      if (typeDeclaration == null) {
         throw new IllegalArgumentException("TypeDeclaration is not expected to be null");
      } else if (typeDeclaration.isTypeParameter()) {
         throw new IllegalArgumentException("You should use only Classes, Interfaces and enums");
      } else if (typeArguments.size() > 0 && typeArguments.size() != typeDeclaration.getTypeParameters().size()) {
         throw new IllegalArgumentException(String.format("expected either zero type arguments or has many as defined in the declaration (%d). Found %d", typeDeclaration.getTypeParameters().size(), typeArguments.size()));
      } else {
         ResolvedTypeParametersMap.Builder typeParametersMapBuilder = new ResolvedTypeParametersMap.Builder();

         for(int i = 0; i < typeArguments.size(); ++i) {
            typeParametersMapBuilder.setValue((ResolvedTypeParameterDeclaration)typeDeclaration.getTypeParameters().get(i), (ResolvedType)typeArguments.get(i));
         }

         this.typeParametersMap = typeParametersMapBuilder.build();
         this.typeDeclaration = typeDeclaration;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ResolvedReferenceType that = (ResolvedReferenceType)o;
         if (!this.typeDeclaration.equals(that.typeDeclaration)) {
            return false;
         } else {
            return this.typeParametersMap.equals(that.typeParametersMap);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.typeDeclaration.hashCode();
      result = 31 * result + this.typeParametersMap.hashCode();
      return result;
   }

   public String toString() {
      return "ReferenceType{" + this.getQualifiedName() + ", typeParametersMap=" + this.typeParametersMap + '}';
   }

   public final boolean isReferenceType() {
      return true;
   }

   public ResolvedReferenceType asReferenceType() {
      return this;
   }

   public String describe() {
      StringBuilder sb = new StringBuilder();
      if (this.hasName()) {
         sb.append(this.typeDeclaration.getQualifiedName());
      } else {
         sb.append("<anonymous class>");
      }

      if (!this.typeParametersMap().isEmpty()) {
         sb.append("<");
         sb.append(String.join(", ", (Iterable)this.typeDeclaration.getTypeParameters().stream().map((tp) -> {
            return this.typeParametersMap().getValue(tp).describe();
         }).collect(Collectors.toList())));
         sb.append(">");
      }

      return sb.toString();
   }

   public abstract ResolvedType transformTypeParameters(ResolvedTypeTransformer transformer);

   public ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tpToReplace, ResolvedType replaced, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      if (replaced == null) {
         throw new IllegalArgumentException();
      } else {
         ResolvedReferenceType result = this;
         int i = 0;

         for(Iterator var6 = this.typeParametersValues().iterator(); var6.hasNext(); ++i) {
            ResolvedType tp = (ResolvedType)var6.next();
            ResolvedType transformedTp = tp.replaceTypeVariables(tpToReplace, replaced, inferredTypes);
            if (tp.isTypeVariable() && tp.asTypeVariable().describe().equals(tpToReplace.getName())) {
               inferredTypes.put(tp.asTypeParameter(), replaced);
            }

            List<ResolvedType> typeParametersCorrected = result.asReferenceType().typeParametersValues();
            typeParametersCorrected.set(i, transformedTp);
            result = this.create(this.typeDeclaration, typeParametersCorrected);
         }

         List<ResolvedType> values = result.typeParametersValues();
         if (values.contains(tpToReplace)) {
            int index = values.indexOf(tpToReplace);
            values.set(index, replaced);
            return this.create(result.getTypeDeclaration(), values);
         } else {
            return result;
         }
      }
   }

   public abstract boolean isAssignableBy(ResolvedType other);

   public abstract List<ResolvedReferenceType> getAllAncestors();

   public abstract List<ResolvedReferenceType> getDirectAncestors();

   public final List<ResolvedReferenceType> getAllInterfacesAncestors() {
      return (List)this.getAllAncestors().stream().filter((it) -> {
         return it.getTypeDeclaration().isInterface();
      }).collect(Collectors.toList());
   }

   public final List<ResolvedReferenceType> getAllClassesAncestors() {
      return (List)this.getAllAncestors().stream().filter((it) -> {
         return it.getTypeDeclaration().isClass();
      }).collect(Collectors.toList());
   }

   public Optional<ResolvedType> getGenericParameterByName(String name) {
      Iterator var2 = this.typeDeclaration.getTypeParameters().iterator();

      ResolvedTypeParameterDeclaration tp;
      do {
         if (!var2.hasNext()) {
            return Optional.empty();
         }

         tp = (ResolvedTypeParameterDeclaration)var2.next();
      } while(!tp.getName().equals(name));

      return Optional.of(this.typeParametersMap().getValue(tp));
   }

   public List<ResolvedType> typeParametersValues() {
      return this.typeParametersMap.isEmpty() ? Collections.emptyList() : (List)this.typeDeclaration.getTypeParameters().stream().map((tp) -> {
         return this.typeParametersMap.getValue(tp);
      }).collect(Collectors.toList());
   }

   public List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> getTypeParametersMap() {
      List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap = new ArrayList();
      if (!this.isRawType()) {
         for(int i = 0; i < this.typeDeclaration.getTypeParameters().size(); ++i) {
            typeParametersMap.add(new Pair(this.typeDeclaration.getTypeParameters().get(0), this.typeParametersValues().get(i)));
         }
      }

      return typeParametersMap;
   }

   public ResolvedTypeParametersMap typeParametersMap() {
      return this.typeParametersMap;
   }

   public final ResolvedReferenceTypeDeclaration getTypeDeclaration() {
      return this.typeDeclaration;
   }

   public Optional<ResolvedType> getFieldType(String name) {
      if (!this.typeDeclaration.hasField(name)) {
         return Optional.empty();
      } else {
         ResolvedType type = this.typeDeclaration.getField(name).getType();
         type = this.useThisTypeParametersOnTheGivenType(type);
         return Optional.of(type);
      }
   }

   public boolean hasName() {
      return this.typeDeclaration.hasName();
   }

   public String getQualifiedName() {
      return this.typeDeclaration.getQualifiedName();
   }

   public String getId() {
      return this.typeDeclaration.getId();
   }

   public abstract Set<MethodUsage> getDeclaredMethods();

   public abstract Set<ResolvedFieldDeclaration> getDeclaredFields();

   public boolean isRawType() {
      if (this.typeDeclaration.getTypeParameters().isEmpty()) {
         return false;
      } else if (this.typeParametersMap().isEmpty()) {
         return true;
      } else {
         Iterator var1 = this.typeParametersMap().getNames().iterator();

         String name;
         Optional value;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            name = (String)var1.next();
            value = this.typeParametersMap().getValueBySignature(name);
         } while(value.isPresent() && ((ResolvedType)value.get()).isTypeVariable() && ((ResolvedType)value.get()).asTypeVariable().qualifiedName().equals(name));

         return false;
      }
   }

   public Optional<ResolvedType> typeParamValue(ResolvedTypeParameterDeclaration typeParameterDeclaration) {
      if (typeParameterDeclaration.declaredOnMethod()) {
         throw new IllegalArgumentException();
      } else {
         String typeId = this.getTypeDeclaration().getId();
         if (typeId.equals(typeParameterDeclaration.getContainerId())) {
            return Optional.of(this.typeParametersMap().getValue(typeParameterDeclaration));
         } else {
            Iterator var3 = this.getAllAncestors().iterator();

            ResolvedReferenceType ancestor;
            do {
               if (!var3.hasNext()) {
                  return Optional.empty();
               }

               ancestor = (ResolvedReferenceType)var3.next();
            } while(!ancestor.getId().equals(typeParameterDeclaration.getContainerId()));

            return Optional.of(ancestor.typeParametersMap().getValue(typeParameterDeclaration));
         }
      }
   }

   public abstract ResolvedType toRawType();

   public List<ResolvedMethodDeclaration> getAllMethods() {
      List<ResolvedMethodDeclaration> allMethods = new LinkedList();
      allMethods.addAll((Collection)this.getDeclaredMethods().stream().map(MethodUsage::getDeclaration).collect(Collectors.toList()));
      this.getDirectAncestors().forEach((a) -> {
         allMethods.addAll(a.getAllMethods());
      });
      return allMethods;
   }

   public List<ResolvedFieldDeclaration> getAllFieldsVisibleToInheritors() {
      List<ResolvedFieldDeclaration> res = new LinkedList((Collection)this.getDeclaredFields().stream().filter((f) -> {
         return f.accessSpecifier() != AccessSpecifier.PRIVATE;
      }).collect(Collectors.toList()));
      this.getDirectAncestors().forEach((a) -> {
         res.addAll(a.getAllFieldsVisibleToInheritors());
      });
      return res;
   }

   public List<ResolvedMethodDeclaration> getAllMethodsVisibleToInheritors() {
      List<ResolvedMethodDeclaration> res = new LinkedList((Collection)this.getDeclaredMethods().stream().map((m) -> {
         return m.getDeclaration();
      }).filter((m) -> {
         return m.accessSpecifier() != AccessSpecifier.PRIVATE;
      }).collect(Collectors.toList()));
      if (!Object.class.getCanonicalName().equals(this.getQualifiedName())) {
         this.getDirectAncestors().forEach((a) -> {
            res.addAll(a.getAllMethodsVisibleToInheritors());
         });
      }

      return res;
   }

   protected abstract ResolvedReferenceType create(ResolvedReferenceTypeDeclaration typeDeclaration, List<ResolvedType> typeParameters);

   protected ResolvedReferenceType create(ResolvedReferenceTypeDeclaration typeDeclaration, ResolvedTypeParametersMap typeParametersMap) {
      Stream var10002 = typeDeclaration.getTypeParameters().stream();
      typeParametersMap.getClass();
      return this.create(typeDeclaration, (List)var10002.map(typeParametersMap::getValue).collect(Collectors.toList()));
   }

   protected abstract ResolvedReferenceType create(ResolvedReferenceTypeDeclaration typeDeclaration);

   protected boolean isCorrespondingBoxingType(String typeName) {
      byte var3 = -1;
      switch(typeName.hashCode()) {
      case -1325958191:
         if (typeName.equals("double")) {
            var3 = 7;
         }
         break;
      case 104431:
         if (typeName.equals("int")) {
            var3 = 4;
         }
         break;
      case 3039496:
         if (typeName.equals("byte")) {
            var3 = 2;
         }
         break;
      case 3052374:
         if (typeName.equals("char")) {
            var3 = 1;
         }
         break;
      case 3327612:
         if (typeName.equals("long")) {
            var3 = 5;
         }
         break;
      case 64711720:
         if (typeName.equals("boolean")) {
            var3 = 0;
         }
         break;
      case 97526364:
         if (typeName.equals("float")) {
            var3 = 6;
         }
         break;
      case 109413500:
         if (typeName.equals("short")) {
            var3 = 3;
         }
      }

      switch(var3) {
      case 0:
         return this.getQualifiedName().equals(Boolean.class.getCanonicalName());
      case 1:
         return this.getQualifiedName().equals(Character.class.getCanonicalName());
      case 2:
         return this.getQualifiedName().equals(Byte.class.getCanonicalName());
      case 3:
         return this.getQualifiedName().equals(Short.class.getCanonicalName());
      case 4:
         return this.getQualifiedName().equals(Integer.class.getCanonicalName());
      case 5:
         return this.getQualifiedName().equals(Long.class.getCanonicalName());
      case 6:
         return this.getQualifiedName().equals(Float.class.getCanonicalName());
      case 7:
         return this.getQualifiedName().equals(Double.class.getCanonicalName());
      default:
         throw new UnsupportedOperationException(typeName);
      }
   }

   protected boolean compareConsideringTypeParameters(ResolvedReferenceType other) {
      if (other.equals(this)) {
         return true;
      } else if (!this.getQualifiedName().equals(other.getQualifiedName())) {
         return false;
      } else if (!this.isRawType() && !other.isRawType()) {
         if (this.typeParametersValues().size() != other.typeParametersValues().size()) {
            throw new IllegalStateException();
         } else {
            for(int i = 0; i < this.typeParametersValues().size(); ++i) {
               ResolvedType thisParam = (ResolvedType)this.typeParametersValues().get(i);
               ResolvedType otherParam = (ResolvedType)other.typeParametersValues().get(i);
               if (!thisParam.equals(otherParam)) {
                  if (!(thisParam instanceof ResolvedWildcard)) {
                     if (thisParam instanceof ResolvedTypeVariable && otherParam instanceof ResolvedTypeVariable) {
                        List<ResolvedType> thisBounds = (List)thisParam.asTypeVariable().asTypeParameter().getBounds().stream().map(ResolvedTypeParameterDeclaration.Bound::getType).collect(Collectors.toList());
                        List<ResolvedType> otherBounds = (List)otherParam.asTypeVariable().asTypeParameter().getBounds().stream().map(ResolvedTypeParameterDeclaration.Bound::getType).collect(Collectors.toList());
                        return thisBounds.size() == otherBounds.size() && otherBounds.containsAll(thisBounds);
                     }

                     return false;
                  }

                  ResolvedWildcard thisParamAsWildcard = (ResolvedWildcard)thisParam;
                  if ((!thisParamAsWildcard.isSuper() || !otherParam.isAssignableBy(thisParamAsWildcard.getBoundedType())) && (!thisParamAsWildcard.isExtends() || !thisParamAsWildcard.getBoundedType().isAssignableBy(otherParam)) && thisParamAsWildcard.isBounded()) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   private static List<ResolvedType> deriveParams(ResolvedReferenceTypeDeclaration typeDeclaration) {
      if (typeDeclaration == null) {
         throw new IllegalArgumentException("TypeDeclaration is not expected to be null");
      } else {
         List<ResolvedTypeParameterDeclaration> typeParameters = typeDeclaration.getTypeParameters();
         if (typeParameters == null) {
            throw new RuntimeException("Type parameters are not expected to be null");
         } else {
            return (List)typeParameters.stream().map(ResolvedTypeVariable::new).collect(Collectors.toList());
         }
      }
   }

   public abstract ResolvedReferenceType deriveTypeParameters(ResolvedTypeParametersMap typeParametersMap);
}
