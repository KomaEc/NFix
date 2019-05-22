package org.jf.dexlib2.writer.builder;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.util.FieldUtil;
import org.jf.dexlib2.util.MethodUtil;

public class BuilderClassDef extends BaseTypeReference implements ClassDef {
   @Nonnull
   final BuilderTypeReference type;
   final int accessFlags;
   @Nullable
   final BuilderTypeReference superclass;
   @Nonnull
   final BuilderTypeList interfaces;
   @Nullable
   final BuilderStringReference sourceFile;
   @Nonnull
   final BuilderAnnotationSet annotations;
   @Nonnull
   final SortedSet<BuilderField> staticFields;
   @Nonnull
   final SortedSet<BuilderField> instanceFields;
   @Nonnull
   final SortedSet<BuilderMethod> directMethods;
   @Nonnull
   final SortedSet<BuilderMethod> virtualMethods;
   int classDefIndex = -1;
   int encodedArrayOffset = 0;
   int annotationDirectoryOffset = 0;

   BuilderClassDef(@Nonnull BuilderTypeReference type, int accessFlags, @Nullable BuilderTypeReference superclass, @Nonnull BuilderTypeList interfaces, @Nullable BuilderStringReference sourceFile, @Nonnull BuilderAnnotationSet annotations, @Nullable Iterable<? extends BuilderField> fields, @Nullable Iterable<? extends BuilderMethod> methods) {
      if (fields == null) {
         fields = ImmutableList.of();
      }

      if (methods == null) {
         methods = ImmutableList.of();
      }

      this.type = type;
      this.accessFlags = accessFlags;
      this.superclass = superclass;
      this.interfaces = interfaces;
      this.sourceFile = sourceFile;
      this.annotations = annotations;
      this.staticFields = ImmutableSortedSet.copyOf(Iterables.filter((Iterable)fields, (Predicate)FieldUtil.FIELD_IS_STATIC));
      this.instanceFields = ImmutableSortedSet.copyOf(Iterables.filter((Iterable)fields, (Predicate)FieldUtil.FIELD_IS_INSTANCE));
      this.directMethods = ImmutableSortedSet.copyOf(Iterables.filter((Iterable)methods, (Predicate)MethodUtil.METHOD_IS_DIRECT));
      this.virtualMethods = ImmutableSortedSet.copyOf(Iterables.filter((Iterable)methods, (Predicate)MethodUtil.METHOD_IS_VIRTUAL));
   }

   @Nonnull
   public String getType() {
      return this.type.getType();
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   @Nullable
   public String getSuperclass() {
      return this.superclass == null ? null : this.superclass.getType();
   }

   @Nullable
   public String getSourceFile() {
      return this.sourceFile == null ? null : this.sourceFile.getString();
   }

   @Nonnull
   public BuilderAnnotationSet getAnnotations() {
      return this.annotations;
   }

   @Nonnull
   public SortedSet<BuilderField> getStaticFields() {
      return this.staticFields;
   }

   @Nonnull
   public SortedSet<BuilderField> getInstanceFields() {
      return this.instanceFields;
   }

   @Nonnull
   public SortedSet<BuilderMethod> getDirectMethods() {
      return this.directMethods;
   }

   @Nonnull
   public SortedSet<BuilderMethod> getVirtualMethods() {
      return this.virtualMethods;
   }

   @Nonnull
   public List<String> getInterfaces() {
      return Lists.transform(this.interfaces, Functions.toStringFunction());
   }

   @Nonnull
   public Collection<BuilderField> getFields() {
      return new AbstractCollection<BuilderField>() {
         @Nonnull
         public Iterator<BuilderField> iterator() {
            return Iterators.mergeSorted(ImmutableList.of(BuilderClassDef.this.staticFields.iterator(), BuilderClassDef.this.instanceFields.iterator()), Ordering.natural());
         }

         public int size() {
            return BuilderClassDef.this.staticFields.size() + BuilderClassDef.this.instanceFields.size();
         }
      };
   }

   @Nonnull
   public Collection<BuilderMethod> getMethods() {
      return new AbstractCollection<BuilderMethod>() {
         @Nonnull
         public Iterator<BuilderMethod> iterator() {
            return Iterators.mergeSorted(ImmutableList.of(BuilderClassDef.this.directMethods.iterator(), BuilderClassDef.this.virtualMethods.iterator()), Ordering.natural());
         }

         public int size() {
            return BuilderClassDef.this.directMethods.size() + BuilderClassDef.this.virtualMethods.size();
         }
      };
   }
}
