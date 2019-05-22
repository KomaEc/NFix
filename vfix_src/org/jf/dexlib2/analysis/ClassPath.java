package org.jf.dexlib2.analysis;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.analysis.reflection.ReflectionClassDef;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.immutable.ImmutableDexFile;

public class ClassPath {
   @Nonnull
   private final TypeProto unknownClass;
   @Nonnull
   private List<ClassProvider> classProviders;
   private final boolean checkPackagePrivateAccess;
   public final int oatVersion;
   public static final int NOT_ART = -1;
   private final CacheLoader<String, TypeProto> classLoader;
   @Nonnull
   private LoadingCache<String, TypeProto> loadedClasses;
   private final Supplier<OdexedFieldInstructionMapper> fieldInstructionMapperSupplier;

   public ClassPath(ClassProvider... classProviders) throws IOException {
      this(Arrays.asList(classProviders), false, -1);
   }

   public ClassPath(Iterable<ClassProvider> classProviders) throws IOException {
      this(classProviders, false, -1);
   }

   public ClassPath(@Nonnull Iterable<? extends ClassProvider> classProviders, boolean checkPackagePrivateAccess, int oatVersion) {
      this.classLoader = new CacheLoader<String, TypeProto>() {
         public TypeProto load(String type) throws Exception {
            return (TypeProto)(type.charAt(0) == '[' ? new ArrayProto(ClassPath.this, type) : new ClassProto(ClassPath.this, type));
         }
      };
      this.loadedClasses = CacheBuilder.newBuilder().build(this.classLoader);
      this.fieldInstructionMapperSupplier = Suppliers.memoize(new Supplier<OdexedFieldInstructionMapper>() {
         public OdexedFieldInstructionMapper get() {
            return new OdexedFieldInstructionMapper(ClassPath.this.isArt());
         }
      });
      this.unknownClass = new UnknownClassProto(this);
      this.loadedClasses.put(this.unknownClass.getType(), this.unknownClass);
      this.checkPackagePrivateAccess = checkPackagePrivateAccess;
      this.oatVersion = oatVersion;
      this.loadPrimitiveType("Z");
      this.loadPrimitiveType("B");
      this.loadPrimitiveType("S");
      this.loadPrimitiveType("C");
      this.loadPrimitiveType("I");
      this.loadPrimitiveType("J");
      this.loadPrimitiveType("F");
      this.loadPrimitiveType("D");
      this.loadPrimitiveType("L");
      this.classProviders = Lists.newArrayList(classProviders);
      this.classProviders.add(getBasicClasses());
   }

   private void loadPrimitiveType(String type) {
      this.loadedClasses.put(type, new PrimitiveProto(this, type));
   }

   private static ClassProvider getBasicClasses() {
      return new DexClassProvider(new ImmutableDexFile(Opcodes.getDefault(), ImmutableSet.of(new ReflectionClassDef(Class.class), new ReflectionClassDef(Cloneable.class), new ReflectionClassDef(Object.class), new ReflectionClassDef(Serializable.class), new ReflectionClassDef(String.class), new ReflectionClassDef(Throwable.class))));
   }

   public boolean isArt() {
      return this.oatVersion != -1;
   }

   @Nonnull
   public TypeProto getClass(@Nonnull CharSequence type) {
      return (TypeProto)this.loadedClasses.getUnchecked(type.toString());
   }

   @Nonnull
   public ClassDef getClassDef(String type) {
      Iterator var2 = this.classProviders.iterator();

      ClassDef classDef;
      do {
         if (!var2.hasNext()) {
            throw new UnresolvedClassException("Could not resolve class %s", new Object[]{type});
         }

         ClassProvider provider = (ClassProvider)var2.next();
         classDef = provider.getClassDef(type);
      } while(classDef == null);

      return classDef;
   }

   @Nonnull
   public TypeProto getUnknownClass() {
      return this.unknownClass;
   }

   public boolean shouldCheckPackagePrivateAccess() {
      return this.checkPackagePrivateAccess;
   }

   @Nonnull
   public OdexedFieldInstructionMapper getFieldInstructionMapper() {
      return (OdexedFieldInstructionMapper)this.fieldInstructionMapperSupplier.get();
   }
}
