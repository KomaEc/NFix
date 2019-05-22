package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.Opcodes;
import java.util.LinkedList;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.SourceUnit;

public abstract class ClassGenerator extends ClassCodeVisitorSupport implements Opcodes {
   protected ClassLoader classLoader;
   protected LinkedList<ClassNode> innerClasses = new LinkedList();
   public static final int asmJDKVersion = 47;

   public ClassGenerator(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public LinkedList<ClassNode> getInnerClasses() {
      return this.innerClasses;
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   protected SourceUnit getSourceUnit() {
      return null;
   }

   public void visitBytecodeSequence(BytecodeSequence bytecodeSequence) {
   }
}
