package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import java.lang.annotation.Annotation;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ClassInfo {
   private final ClassIdentifier id;
   private final int access;
   private final Set<Integer> codeLines;
   private final ClassPointer outerClass;
   private final ClassPointer superClass;
   private final Collection<ClassName> annotations;
   private final String sourceFile;
   private final Map<ClassName, Object> classAnnotationValues;

   public ClassInfo(ClassPointer superClass, ClassPointer outerClass, ClassInfoBuilder builder) {
      this.superClass = superClass;
      this.outerClass = outerClass;
      this.id = builder.id;
      this.access = builder.access;
      this.codeLines = builder.codeLines;
      this.annotations = FCollection.map(builder.annotations, ClassName.stringToClassName());
      this.sourceFile = builder.sourceFile;
      this.classAnnotationValues = builder.classAnnotationValues;
   }

   public int getNumberOfCodeLines() {
      return this.codeLines.size();
   }

   public boolean isCodeLine(int line) {
      return this.codeLines.contains(line);
   }

   public ClassIdentifier getId() {
      return this.id;
   }

   public ClassName getName() {
      return this.id.getName();
   }

   public boolean isInterface() {
      return (this.access & 512) != 0;
   }

   public boolean isAbstract() {
      return (this.access & 1024) != 0;
   }

   public boolean isTopLevelClass() {
      return this.getOuterClass().hasNone();
   }

   public Option<ClassInfo> getOuterClass() {
      return this.outerClass.fetch();
   }

   public Option<ClassInfo> getSuperClass() {
      return this.getParent();
   }

   public String getSourceFileName() {
      return this.sourceFile;
   }

   public boolean hasAnnotation(Class<? extends Annotation> annotation) {
      return this.hasAnnotation(new ClassName(annotation));
   }

   public boolean hasAnnotation(ClassName annotation) {
      return this.annotations.contains(annotation);
   }

   public Object getClassAnnotationValue(ClassName annotation) {
      return this.classAnnotationValues.get(annotation);
   }

   public boolean descendsFrom(Class<?> clazz) {
      return this.descendsFrom(new ClassName(clazz.getName()));
   }

   public HierarchicalClassId getHierarchicalId() {
      return new HierarchicalClassId(this.id, this.getDeepHash());
   }

   public BigInteger getDeepHash() {
      BigInteger hash = this.getHash();
      Option<ClassInfo> parent = this.getParent();
      if (parent.hasSome()) {
         hash = hash.add(((ClassInfo)parent.value()).getHash());
      }

      Option<ClassInfo> outer = this.getOuterClass();
      if (outer.hasSome()) {
         hash = hash.add(((ClassInfo)outer.value()).getHash());
      }

      return hash;
   }

   public BigInteger getHash() {
      return BigInteger.valueOf(this.id.getHash());
   }

   private Option<ClassInfo> getParent() {
      return (Option)(this.superClass == null ? Option.none() : this.superClass.fetch());
   }

   private boolean descendsFrom(ClassName clazz) {
      if (this.getSuperClass().hasNone()) {
         return false;
      } else {
         return ((ClassInfo)this.getSuperClass().value()).getName().equals(clazz) ? true : ((ClassInfo)this.getSuperClass().value()).descendsFrom(clazz);
      }
   }

   public static F<ClassInfo, Boolean> matchIfAbstract() {
      return new F<ClassInfo, Boolean>() {
         public Boolean apply(ClassInfo a) {
            return a.isAbstract();
         }
      };
   }

   public String toString() {
      return this.id.getName().asJavaName();
   }

   public static F<ClassInfo, ClassName> toClassName() {
      return new F<ClassInfo, ClassName>() {
         public ClassName apply(ClassInfo a) {
            return a.getName();
         }
      };
   }

   public static F<ClassInfo, HierarchicalClassId> toFullClassId() {
      return new F<ClassInfo, HierarchicalClassId>() {
         public HierarchicalClassId apply(ClassInfo a) {
            return a.getHierarchicalId();
         }
      };
   }
}
