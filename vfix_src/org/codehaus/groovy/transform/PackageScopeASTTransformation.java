package org.codehaus.groovy.transform;

import groovy.lang.PackageScope;
import groovyjarjarasm.asm.Opcodes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class PackageScopeASTTransformation implements ASTTransformation, Opcodes {
   private static final Class MY_CLASS = PackageScope.class;
   private static final ClassNode MY_TYPE;
   private static final String MY_TYPE_NAME;

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (MY_TYPE.equals(node.getClassNode())) {
            if (parent instanceof ClassNode) {
               this.visitClassNode((ClassNode)parent);
            } else if (parent instanceof FieldNode) {
               this.visitFieldNode((FieldNode)parent);
            }

         }
      } else {
         throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
      }
   }

   private void visitClassNode(ClassNode cNode) {
      String cName = cNode.getName();
      if (cNode.isInterface()) {
         throw new RuntimeException("Error processing interface '" + cName + "'. " + MY_TYPE_NAME + " not allowed for interfaces.");
      } else {
         List<PropertyNode> pList = cNode.getProperties();
         List<PropertyNode> foundProps = new ArrayList();
         List<String> foundNames = new ArrayList();
         Iterator i$ = pList.iterator();

         PropertyNode pNode;
         while(i$.hasNext()) {
            pNode = (PropertyNode)i$.next();
            foundProps.add(pNode);
            foundNames.add(pNode.getName());
         }

         i$ = foundProps.iterator();

         while(i$.hasNext()) {
            pNode = (PropertyNode)i$.next();
            pList.remove(pNode);
         }

         List<FieldNode> fList = cNode.getFields();
         Iterator i$ = fList.iterator();

         while(i$.hasNext()) {
            FieldNode fNode = (FieldNode)i$.next();
            if (foundNames.contains(fNode.getName())) {
               this.revertVisibility(fNode);
            }
         }

      }
   }

   private void visitFieldNode(FieldNode fNode) {
      ClassNode cNode = fNode.getDeclaringClass();
      List<PropertyNode> pList = cNode.getProperties();
      PropertyNode foundProp = null;
      Iterator i$ = pList.iterator();

      while(i$.hasNext()) {
         PropertyNode pNode = (PropertyNode)i$.next();
         if (pNode.getName().equals(fNode.getName())) {
            foundProp = pNode;
            break;
         }
      }

      if (foundProp != null) {
         this.revertVisibility(fNode);
         pList.remove(foundProp);
      }

   }

   private void revertVisibility(FieldNode fNode) {
      fNode.setModifiers(fNode.getModifiers() & -3);
   }

   static {
      MY_TYPE = new ClassNode(MY_CLASS);
      MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
   }
}
