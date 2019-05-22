package org.jf.dexlib2.immutable;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.util.FieldUtil;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;

public class ImmutableClassDef extends BaseTypeReference implements ClassDef {
   @Nonnull
   protected final String type;
   protected final int accessFlags;
   @Nullable
   protected final String superclass;
   @Nonnull
   protected final ImmutableList<String> interfaces;
   @Nullable
   protected final String sourceFile;
   @Nonnull
   protected final ImmutableSet<? extends ImmutableAnnotation> annotations;
   @Nonnull
   protected final ImmutableSortedSet<? extends ImmutableField> staticFields;
   @Nonnull
   protected final ImmutableSortedSet<? extends ImmutableField> instanceFields;
   @Nonnull
   protected final ImmutableSortedSet<? extends ImmutableMethod> directMethods;
   @Nonnull
   protected final ImmutableSortedSet<? extends ImmutableMethod> virtualMethods;
   private static final ImmutableConverter<ImmutableClassDef, ClassDef> CONVERTER = new ImmutableConverter<ImmutableClassDef, ClassDef>() {
      protected boolean isImmutable(@Nonnull ClassDef item) {
         return item instanceof ImmutableClassDef;
      }

      @Nonnull
      protected ImmutableClassDef makeImmutable(@Nonnull ClassDef item) {
         return ImmutableClassDef.of(item);
      }
   };

   public ImmutableClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable Collection<String> interfaces, @Nullable String sourceFile, @Nullable Collection<? extends Annotation> annotations, @Nullable Iterable<? extends Field> fields, @Nullable Iterable<? extends Method> methods) {
      if (fields == null) {
         fields = ImmutableList.of();
      }

      if (methods == null) {
         methods = ImmutableList.of();
      }

      this.type = type;
      this.accessFlags = accessFlags;
      this.superclass = superclass;
      this.interfaces = interfaces == null ? ImmutableList.of() : ImmutableList.copyOf(interfaces);
      this.sourceFile = sourceFile;
      this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
      this.staticFields = ImmutableField.immutableSetOf(Iterables.filter((Iterable)fields, (Predicate)FieldUtil.FIELD_IS_STATIC));
      this.instanceFields = ImmutableField.immutableSetOf(Iterables.filter((Iterable)fields, (Predicate)FieldUtil.FIELD_IS_INSTANCE));
      this.directMethods = ImmutableMethod.immutableSetOf(Iterables.filter((Iterable)methods, (Predicate)MethodUtil.METHOD_IS_DIRECT));
      this.virtualMethods = ImmutableMethod.immutableSetOf(Iterables.filter((Iterable)methods, (Predicate)MethodUtil.METHOD_IS_VIRTUAL));
   }

   public ImmutableClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable Collection<String> interfaces, @Nullable String sourceFile, @Nullable Collection<? extends Annotation> annotations, @Nullable Iterable<? extends Field> staticFields, @Nullable Iterable<? extends Field> instanceFields, @Nullable Iterable<? extends Method> directMethods, @Nullable Iterable<? extends Method> virtualMethods) {
      this.type = type;
      this.accessFlags = accessFlags;
      this.superclass = superclass;
      this.interfaces = interfaces == null ? ImmutableList.of() : ImmutableList.copyOf(interfaces);
      this.sourceFile = sourceFile;
      this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
      this.staticFields = ImmutableField.immutableSetOf(staticFields);
      this.instanceFields = ImmutableField.immutableSetOf(instanceFields);
      this.directMethods = ImmutableMethod.immutableSetOf(directMethods);
      this.virtualMethods = ImmutableMethod.immutableSetOf(virtualMethods);
   }

   public ImmutableClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable ImmutableList<String> interfaces, @Nullable String sourceFile, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable ImmutableSortedSet<? extends ImmutableField> staticFields, @Nullable ImmutableSortedSet<? extends ImmutableField> instanceFields, @Nullable ImmutableSortedSet<? extends ImmutableMethod> directMethods, @Nullable ImmutableSortedSet<? extends ImmutableMethod> virtualMethods) {
      this.type = type;
      this.accessFlags = accessFlags;
      this.superclass = superclass;
      this.interfaces = ImmutableUtils.nullToEmptyList(interfaces);
      this.sourceFile = sourceFile;
      this.annotations = ImmutableUtils.nullToEmptySet(annotations);
      this.staticFields = ImmutableUtils.nullToEmptySortedSet(staticFields);
      this.instanceFields = ImmutableUtils.nullToEmptySortedSet(instanceFields);
      this.directMethods = ImmutableUtils.nullToEmptySortedSet(directMethods);
      this.virtualMethods = ImmutableUtils.nullToEmptySortedSet(virtualMethods);
   }

   public static ImmutableClassDef of(ClassDef classDef) {
      return classDef instanceof ImmutableClassDef ? (ImmutableClassDef)classDef : new ImmutableClassDef(classDef.getType(), classDef.getAccessFlags(), classDef.getSuperclass(), classDef.getInterfaces(), classDef.getSourceFile(), classDef.getAnnotations(), classDef.getStaticFields(), classDef.getInstanceFields(), classDef.getDirectMethods(), classDef.getVirtualMethods());
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   @Nullable
   public String getSuperclass() {
      return this.superclass;
   }

   @Nonnull
   public ImmutableList<String> getInterfaces() {
      return this.interfaces;
   }

   @Nullable
   public String getSourceFile() {
      return this.sourceFile;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableAnnotation> getAnnotations() {
      return this.annotations;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableField> getStaticFields() {
      return this.staticFields;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableField> getInstanceFields() {
      return this.instanceFields;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableMethod> getDirectMethods() {
      return this.directMethods;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableMethod> getVirtualMethods() {
      return this.virtualMethods;
   }

   @Nonnull
   public Collection<? extends ImmutableField> getFields() {
      return new AbstractCollection<ImmutableField>() {
         @Nonnull
         public Iterator<ImmutableField> iterator() {
            return Iterators.concat(ImmutableClassDef.this.staticFields.iterator(), ImmutableClassDef.this.instanceFields.iterator());
         }

         public int size() {
            return ImmutableClassDef.this.staticFields.size() + ImmutableClassDef.this.instanceFields.size();
         }
      };
   }

   @Nonnull
   public Collection<? extends ImmutableMethod> getMethods() {
      return new AbstractCollection<ImmutableMethod>() {
         @Nonnull
         public Iterator<ImmutableMethod> iterator() {
            return Iterators.concat(ImmutableClassDef.this.directMethods.iterator(), ImmutableClassDef.this.virtualMethods.iterator());
         }

         public int size() {
            return ImmutableClassDef.this.directMethods.size() + ImmutableClassDef.this.virtualMethods.size();
         }
      };
   }

   @Nonnull
   public static ImmutableSet<ImmutableClassDef> immutableSetOf(@Nullable Iterable<? extends ClassDef> iterable) {
      return CONVERTER.toSet(iterable);
   }
}
