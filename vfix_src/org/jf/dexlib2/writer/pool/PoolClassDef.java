package org.jf.dexlib2.writer.pool;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;

class PoolClassDef extends BaseTypeReference implements ClassDef {
   @Nonnull
   final ClassDef classDef;
   @Nonnull
   final TypeListPool.Key<List<String>> interfaces;
   @Nonnull
   final ImmutableSortedSet<Field> staticFields;
   @Nonnull
   final ImmutableSortedSet<Field> instanceFields;
   @Nonnull
   final ImmutableSortedSet<PoolMethod> directMethods;
   @Nonnull
   final ImmutableSortedSet<PoolMethod> virtualMethods;
   int classDefIndex = -1;
   int encodedArrayOffset = 0;
   int annotationDirectoryOffset = 0;

   PoolClassDef(@Nonnull ClassDef classDef) {
      this.classDef = classDef;
      this.interfaces = new TypeListPool.Key(ImmutableList.copyOf((Collection)classDef.getInterfaces()));
      this.staticFields = ImmutableSortedSet.copyOf(classDef.getStaticFields());
      this.instanceFields = ImmutableSortedSet.copyOf(classDef.getInstanceFields());
      this.directMethods = ImmutableSortedSet.copyOf(Iterables.transform(classDef.getDirectMethods(), PoolMethod.TRANSFORM));
      this.virtualMethods = ImmutableSortedSet.copyOf(Iterables.transform(classDef.getVirtualMethods(), PoolMethod.TRANSFORM));
   }

   @Nonnull
   public String getType() {
      return this.classDef.getType();
   }

   public int getAccessFlags() {
      return this.classDef.getAccessFlags();
   }

   @Nullable
   public String getSuperclass() {
      return this.classDef.getSuperclass();
   }

   @Nonnull
   public List<String> getInterfaces() {
      return (List)this.interfaces.types;
   }

   @Nullable
   public String getSourceFile() {
      return this.classDef.getSourceFile();
   }

   @Nonnull
   public Set<? extends Annotation> getAnnotations() {
      return this.classDef.getAnnotations();
   }

   @Nonnull
   public SortedSet<Field> getStaticFields() {
      return this.staticFields;
   }

   @Nonnull
   public SortedSet<Field> getInstanceFields() {
      return this.instanceFields;
   }

   @Nonnull
   public Collection<Field> getFields() {
      return new AbstractCollection<Field>() {
         @Nonnull
         public Iterator<Field> iterator() {
            return Iterators.mergeSorted(ImmutableList.of(PoolClassDef.this.staticFields.iterator(), PoolClassDef.this.instanceFields.iterator()), Ordering.natural());
         }

         public int size() {
            return PoolClassDef.this.staticFields.size() + PoolClassDef.this.instanceFields.size();
         }
      };
   }

   @Nonnull
   public SortedSet<PoolMethod> getDirectMethods() {
      return this.directMethods;
   }

   @Nonnull
   public SortedSet<PoolMethod> getVirtualMethods() {
      return this.virtualMethods;
   }

   @Nonnull
   public Collection<PoolMethod> getMethods() {
      return new AbstractCollection<PoolMethod>() {
         @Nonnull
         public Iterator<PoolMethod> iterator() {
            return Iterators.mergeSorted(ImmutableList.of(PoolClassDef.this.directMethods.iterator(), PoolClassDef.this.virtualMethods.iterator()), Ordering.natural());
         }

         public int size() {
            return PoolClassDef.this.directMethods.size() + PoolClassDef.this.virtualMethods.size();
         }
      };
   }
}
