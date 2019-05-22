package com.gzoltar.shaded.javassist.bytecode.annotation;

import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.bytecode.ConstPool;
import java.io.IOException;
import java.lang.reflect.Method;

public class BooleanMemberValue extends MemberValue {
   int valueIndex;

   public BooleanMemberValue(int index, ConstPool cp) {
      super('Z', cp);
      this.valueIndex = index;
   }

   public BooleanMemberValue(boolean b, ConstPool cp) {
      super('Z', cp);
      this.setValue(b);
   }

   public BooleanMemberValue(ConstPool cp) {
      super('Z', cp);
      this.setValue(false);
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
      return new Boolean(this.getValue());
   }

   Class getType(ClassLoader cl) {
      return Boolean.TYPE;
   }

   public boolean getValue() {
      return this.cp.getIntegerInfo(this.valueIndex) != 0;
   }

   public void setValue(boolean newValue) {
      this.valueIndex = this.cp.addIntegerInfo(newValue ? 1 : 0);
   }

   public String toString() {
      return this.getValue() ? "true" : "false";
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitBooleanMemberValue(this);
   }
}
