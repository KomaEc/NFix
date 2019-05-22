package org.codehaus.groovy.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AnnotatedNode extends ASTNode {
   private List<AnnotationNode> annotations = Collections.emptyList();
   private boolean synthetic;
   ClassNode declaringClass;
   private boolean hasNoRealSourcePositionFlag;

   public List<AnnotationNode> getAnnotations() {
      return this.annotations;
   }

   public List<AnnotationNode> getAnnotations(ClassNode type) {
      List<AnnotationNode> ret = new ArrayList(this.annotations.size());
      Iterator i$ = this.annotations.iterator();

      while(i$.hasNext()) {
         AnnotationNode node = (AnnotationNode)i$.next();
         if (type.equals(node.getClassNode())) {
            ret.add(node);
         }
      }

      return ret;
   }

   public void addAnnotation(AnnotationNode value) {
      this.checkInit();
      this.annotations.add(value);
   }

   private void checkInit() {
      if (this.annotations == Collections.EMPTY_LIST) {
         this.annotations = new ArrayList(3);
      }

   }

   public void addAnnotations(List<AnnotationNode> annotations) {
      Iterator i$ = annotations.iterator();

      while(i$.hasNext()) {
         AnnotationNode node = (AnnotationNode)i$.next();
         this.addAnnotation(node);
      }

   }

   public boolean isSynthetic() {
      return this.synthetic;
   }

   public void setSynthetic(boolean synthetic) {
      this.synthetic = synthetic;
   }

   public ClassNode getDeclaringClass() {
      return this.declaringClass;
   }

   public void setDeclaringClass(ClassNode declaringClass) {
      this.declaringClass = declaringClass;
   }

   public boolean hasNoRealSourcePosition() {
      return this.hasNoRealSourcePositionFlag;
   }

   public void setHasNoRealSourcePosition(boolean value) {
      this.hasNoRealSourcePositionFlag = value;
   }
}
