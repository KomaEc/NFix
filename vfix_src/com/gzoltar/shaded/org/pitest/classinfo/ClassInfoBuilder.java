package com.gzoltar.shaded.org.pitest.classinfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ClassInfoBuilder {
   int access;
   ClassIdentifier id;
   String outerClass;
   String superClass;
   String sourceFile;
   final Set<Integer> codeLines = new HashSet();
   final Set<String> annotations = new HashSet(0);
   final Map<ClassName, Object> classAnnotationValues = new HashMap(0);

   public void registerCodeLine(int line) {
      this.codeLines.add(line);
   }

   public void registerAnnotation(String annotation) {
      this.annotations.add(annotation);
   }

   public void registerClassAnnotationValue(ClassName annotation, Object value) {
      this.classAnnotationValues.put(annotation, value);
   }
}
