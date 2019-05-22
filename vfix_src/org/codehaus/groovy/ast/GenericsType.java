package org.codehaus.groovy.ast;

public class GenericsType extends ASTNode {
   private final ClassNode[] upperBounds;
   private final ClassNode lowerBound;
   private ClassNode type;
   private String name;
   private boolean placeholder;
   private boolean resolved;
   private boolean wildcard;

   public GenericsType(ClassNode type, ClassNode[] upperBounds, ClassNode lowerBound) {
      this.type = type;
      this.name = type.isGenericsPlaceHolder() ? type.getUnresolvedName() : type.getName();
      this.upperBounds = upperBounds;
      this.lowerBound = lowerBound;
      this.placeholder = type.isGenericsPlaceHolder();
      this.resolved = false;
   }

   public GenericsType(ClassNode basicType) {
      this(basicType, (ClassNode[])null, (ClassNode)null);
   }

   public ClassNode getType() {
      return this.type;
   }

   public void setType(ClassNode type) {
      this.type = type;
   }

   public String toString() {
      String ret = this.name;
      if (this.upperBounds != null) {
         ret = ret + " extends ";

         for(int i = 0; i < this.upperBounds.length; ++i) {
            ret = ret + this.upperBounds[i].toString();
            if (i + 1 < this.upperBounds.length) {
               ret = ret + " & ";
            }
         }
      } else if (this.lowerBound != null) {
         ret = ret + " super " + this.lowerBound;
      }

      return ret;
   }

   public ClassNode[] getUpperBounds() {
      return this.upperBounds;
   }

   public String getName() {
      return this.name;
   }

   public boolean isPlaceholder() {
      return this.placeholder;
   }

   public void setPlaceholder(boolean placeholder) {
      this.placeholder = placeholder;
      this.type.setGenericsPlaceHolder(true);
   }

   public boolean isResolved() {
      return this.resolved || this.placeholder;
   }

   public void setResolved(boolean res) {
      this.resolved = res;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isWildcard() {
      return this.wildcard;
   }

   public void setWildcard(boolean wildcard) {
      this.wildcard = wildcard;
   }

   public ClassNode getLowerBound() {
      return this.lowerBound;
   }
}
