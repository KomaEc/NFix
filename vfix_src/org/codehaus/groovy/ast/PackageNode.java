package org.codehaus.groovy.ast;

public class PackageNode extends AnnotatedNode {
   private String name;

   public PackageNode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String getText() {
      return "package " + this.name;
   }

   public void visit(GroovyCodeVisitor visitor) {
   }
}
