package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.Label;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;

public class Variable {
   public static final Variable THIS_VARIABLE = new Variable();
   public static final Variable SUPER_VARIABLE = new Variable();
   private int index;
   private ClassNode type;
   private String name;
   private final int prevCurrent;
   private boolean holder;
   private boolean property;
   private Label startLabel = null;
   private Label endLabel = null;
   private boolean dynamicTyped;
   private int prevIndex;

   private Variable() {
      this.dynamicTyped = true;
      this.index = 0;
      this.holder = false;
      this.property = false;
      this.prevCurrent = 0;
   }

   public Variable(int index, ClassNode type, String name, int prevCurrent) {
      this.index = index;
      this.type = type;
      this.name = name;
      this.prevCurrent = prevCurrent;
   }

   public String getName() {
      return this.name;
   }

   public ClassNode getType() {
      return this.type;
   }

   public String getTypeName() {
      return this.type.getName();
   }

   public int getIndex() {
      return this.index;
   }

   public boolean isHolder() {
      return this.holder;
   }

   public void setHolder(boolean holder) {
      this.holder = holder;
   }

   public boolean isProperty() {
      return this.property;
   }

   public void setProperty(boolean property) {
      this.property = property;
   }

   public Label getStartLabel() {
      return this.startLabel;
   }

   public void setStartLabel(Label startLabel) {
      this.startLabel = startLabel;
   }

   public Label getEndLabel() {
      return this.endLabel;
   }

   public void setEndLabel(Label endLabel) {
      this.endLabel = endLabel;
   }

   public String toString() {
      return super.toString() + "[" + this.type + " " + this.name + " (" + this.index + ")";
   }

   public void setType(ClassNode type) {
      this.type = type;
      this.dynamicTyped |= type == ClassHelper.DYNAMIC_TYPE;
   }

   public void setDynamicTyped(boolean b) {
      this.dynamicTyped = b;
   }

   public boolean isDynamicTyped() {
      return this.dynamicTyped;
   }

   public int getPrevIndex() {
      return this.prevIndex;
   }
}
