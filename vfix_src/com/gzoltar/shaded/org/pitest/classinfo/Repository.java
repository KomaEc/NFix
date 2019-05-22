package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.Option;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Repository implements ClassInfoSource {
   private final HashFunction hashFunction;
   private final Map<ClassName, ClassInfo> knownClasses;
   private final Set<ClassName> unknownClasses;
   private final ClassByteArraySource source;

   public Repository(ClassByteArraySource source) {
      this(source, new AddlerHash());
   }

   Repository(ClassByteArraySource source, HashFunction hashFunction) {
      this.knownClasses = new HashMap();
      this.unknownClasses = new HashSet();
      this.source = source;
      this.hashFunction = hashFunction;
   }

   public boolean hasClass(ClassName name) {
      return this.knownClasses.containsKey(name) || this.querySource(name).hasSome();
   }

   public Option<ClassInfo> fetchClass(Class<?> clazz) {
      return this.fetchClass(clazz.getName());
   }

   private Option<ClassInfo> fetchClass(String name) {
      return this.fetchClass(new ClassName(name));
   }

   public Option<ClassInfo> fetchClass(ClassName name) {
      ClassInfo info = (ClassInfo)this.knownClasses.get(name);
      if (info != null) {
         return Option.some(info);
      } else {
         Option<ClassInfo> maybeInfo = this.nameToClassInfo(name);
         if (maybeInfo.hasSome()) {
            this.knownClasses.put(name, maybeInfo.value());
         }

         return maybeInfo;
      }
   }

   private Option<ClassInfo> nameToClassInfo(ClassName name) {
      Option<byte[]> bytes = this.querySource(name);
      if (bytes.hasSome()) {
         ClassInfoBuilder classData = ClassInfoVisitor.getClassInfo(name, (byte[])bytes.value(), this.hashFunction.hash((byte[])bytes.value()));
         return this.contructClassInfo(classData);
      } else {
         return Option.none();
      }
   }

   public Option<byte[]> querySource(ClassName name) {
      if (this.unknownClasses.contains(name)) {
         return Option.none();
      } else {
         Option<byte[]> option = this.source.getBytes(name.asJavaName());
         if (option.hasSome()) {
            return option;
         } else {
            this.unknownClasses.add(name);
            return option;
         }
      }
   }

   private Option<ClassInfo> contructClassInfo(ClassInfoBuilder classData) {
      return Option.some(new ClassInfo(this.resolveClass(classData.superClass), this.resolveClass(classData.outerClass), classData));
   }

   private ClassPointer resolveClass(String clazz) {
      if (clazz == null) {
         return new DefaultClassPointer((ClassInfo)null);
      } else {
         ClassInfo alreadyResolved = (ClassInfo)this.knownClasses.get(ClassName.fromString(clazz));
         return (ClassPointer)(alreadyResolved != null ? new DefaultClassPointer(alreadyResolved) : new DeferredClassPointer(this, ClassName.fromString(clazz)));
      }
   }
}
