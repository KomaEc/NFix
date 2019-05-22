package com.gzoltar.shaded.javassist.bytecode.annotation;

import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.bytecode.ConstPool;
import java.io.IOException;
import java.lang.reflect.Method;

public class FloatMemberValue extends MemberValue {
   int valueIndex;

   public FloatMemberValue(int index, ConstPool cp) {
      super('F', cp);
      this.valueIndex = index;
   }

   public FloatMemberValue(float f, ConstPool cp) {
      super('F', cp);
      this.setValue(f);
   }

   public FloatMemberValue(ConstPool cp) {
      super('F', cp);
      this.setValue(0.0F);
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
      return new Float(this.getValue());
   }

   Class getType(ClassLoader cl) {
      return Float.TYPE;
   }

   public float getValue() {
      return this.cp.getFloatInfo(this.valueIndex);
   }

   public void setValue(float newValue) {
      this.valueIndex = this.cp.addFloatInfo(newValue);
   }

   public String toString() {
      return Float.toString(this.getValue());
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitFloatMemberValue(this);
   }
}
