package org.codehaus.groovy.ast;

import groovy.lang.GroovyClassLoader;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

public class CompileUnit {
   private final List<ModuleNode> modules;
   private Map<String, ClassNode> classes;
   private CompilerConfiguration config;
   private GroovyClassLoader classLoader;
   private CodeSource codeSource;
   private Map<String, ClassNode> classesToCompile;
   private Map<String, SourceUnit> classNameToSource;

   public CompileUnit(GroovyClassLoader classLoader, CompilerConfiguration config) {
      this(classLoader, (CodeSource)null, config);
   }

   public CompileUnit(GroovyClassLoader classLoader, CodeSource codeSource, CompilerConfiguration config) {
      this.modules = new ArrayList();
      this.classes = new HashMap();
      this.classesToCompile = new HashMap();
      this.classNameToSource = new HashMap();
      this.classLoader = classLoader;
      this.config = config;
      this.codeSource = codeSource;
   }

   public List<ModuleNode> getModules() {
      return this.modules;
   }

   public void addModule(ModuleNode node) {
      if (node != null) {
         this.modules.add(node);
         node.setUnit(this);
         this.addClasses(node.getClasses());
      }
   }

   public ClassNode getClass(String name) {
      ClassNode cn = (ClassNode)this.classes.get(name);
      return cn != null ? cn : (ClassNode)this.classesToCompile.get(name);
   }

   public List getClasses() {
      List<ClassNode> answer = new ArrayList();
      Iterator i$ = this.modules.iterator();

      while(i$.hasNext()) {
         ModuleNode module = (ModuleNode)i$.next();
         answer.addAll(module.getClasses());
      }

      return answer;
   }

   public CompilerConfiguration getConfig() {
      return this.config;
   }

   public GroovyClassLoader getClassLoader() {
      return this.classLoader;
   }

   public CodeSource getCodeSource() {
      return this.codeSource;
   }

   void addClasses(List<ClassNode> classList) {
      Iterator i$ = classList.iterator();

      while(i$.hasNext()) {
         ClassNode node = (ClassNode)i$.next();
         this.addClass(node);
      }

   }

   public void addClass(ClassNode node) {
      node = node.redirect();
      String name = node.getName();
      ClassNode stored = (ClassNode)this.classes.get(name);
      if (stored != null && stored != node) {
         SourceUnit nodeSource = node.getModule().getContext();
         SourceUnit storedSource = stored.getModule().getContext();
         String txt = "Invalid duplicate class definition of class " + node.getName() + " : ";
         if (nodeSource == storedSource) {
            txt = txt + "The source " + nodeSource.getName() + " contains at least two definitions of the class " + node.getName() + ".\n";
            if (node.isScriptBody() || stored.isScriptBody()) {
               txt = txt + "One of the classes is a explicit generated class using the class statement, the other is a class generated from the script body based on the file name. Solutions are to change the file name or to change the class name.\n";
            }
         } else {
            txt = txt + "The sources " + nodeSource.getName() + " and " + storedSource.getName() + " are containing both a class of the name " + node.getName() + ".\n";
         }

         nodeSource.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(txt, node.getLineNumber(), node.getColumnNumber()), nodeSource));
      }

      this.classes.put(name, node);
      if (this.classesToCompile.containsKey(name)) {
         ClassNode cn = (ClassNode)this.classesToCompile.get(name);
         cn.setRedirect(node);
         this.classesToCompile.remove(name);
      }

   }

   public void addClassNodeToCompile(ClassNode node, SourceUnit location) {
      this.classesToCompile.put(node.getName(), node);
      this.classNameToSource.put(node.getName(), location);
   }

   public SourceUnit getScriptSourceLocation(String className) {
      return (SourceUnit)this.classNameToSource.get(className);
   }

   public boolean hasClassNodeToCompile() {
      return !this.classesToCompile.isEmpty();
   }

   public Iterator<String> iterateClassNodeToCompile() {
      return this.classesToCompile.keySet().iterator();
   }
}
