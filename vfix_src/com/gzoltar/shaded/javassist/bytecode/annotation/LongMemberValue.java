package com.gzoltar.shaded.javassist.bytecode.annotation;

import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.javassist.bytecode.ConstPool;
import java.io.IOException;
import java.lang.reflect.Method;

public class LongMemberValue extends MemberValue {
   int valueIndex;

   public LongMemberValue(int index, ConstPool cp) {
      super('J', cp);
      this.valueIndex = index;
   }

   public LongMemberValue(long j, ConstPool cp) {
      super('J', cp);
      this.setValue(j);
   }

   public LongMemberValue(ConstPool cp) {
      super('J', cp);
      this.setValue(0L);
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
      return new Long(this.getValue());
   }

   Class getType(ClassLoader cl) {
      return Long.TYPE;
   }

   public long getValue() {
      return this.cp.getLongInfo(this.valueIndex);
   }

   public void setValue(long newValue) {
      this.valueIndex = this.cp.addLongInfo(newValue);
   }

   public String toString() {
      return Long.toString(this.getValue());
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitLongMemberValue(this);
   }
}
