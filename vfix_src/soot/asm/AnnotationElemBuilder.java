package soot.asm;

import java.util.ArrayList;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;

abstract class AnnotationElemBuilder extends AnnotationVisitor {
   protected final ArrayList<AnnotationElem> elems;

   AnnotationElemBuilder(int expected) {
      super(327680);
      this.elems = new ArrayList(expected);
   }

   AnnotationElemBuilder() {
      this(4);
   }

   public AnnotationElem getAnnotationElement(String name, Object value) {
      Object elem;
      if (value instanceof Byte) {
         elem = new AnnotationIntElem((Byte)value, 'B', name);
      } else if (value instanceof Boolean) {
         elem = new AnnotationIntElem((Boolean)value ? 1 : 0, 'Z', name);
      } else if (value instanceof Character) {
         elem = new AnnotationIntElem((Character)value, 'C', name);
      } else if (value instanceof Short) {
         elem = new AnnotationIntElem((Short)value, 'S', name);
      } else if (value instanceof Integer) {
         elem = new AnnotationIntElem((Integer)value, 'I', name);
      } else if (value instanceof Long) {
         elem = new AnnotationLongElem((Long)value, 'J', name);
      } else if (value instanceof Float) {
         elem = new AnnotationFloatElem((Float)value, 'F', name);
      } else if (value instanceof Double) {
         elem = new AnnotationDoubleElem((Double)value, 'D', name);
      } else if (value instanceof String) {
         elem = new AnnotationStringElem(value.toString(), 's', name);
      } else if (value instanceof Type) {
         Type t = (Type)value;
         elem = new AnnotationClassElem(t.getDescriptor(), 'c', name);
      } else {
         if (!value.getClass().isArray()) {
            throw new UnsupportedOperationException("Unsupported value type: " + value.getClass());
         }

         ArrayList<AnnotationElem> annotationArray = new ArrayList();
         int var6;
         int var7;
         if (value instanceof byte[]) {
            byte[] var18 = (byte[])((byte[])value);
            var6 = var18.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var18[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof boolean[]) {
            boolean[] var17 = (boolean[])((boolean[])value);
            var6 = var17.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var17[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof char[]) {
            char[] var16 = (char[])((char[])value);
            var6 = var16.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var16[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof short[]) {
            short[] var15 = (short[])((short[])value);
            var6 = var15.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var15[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof int[]) {
            int[] var14 = (int[])((int[])value);
            var6 = var14.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var14[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof long[]) {
            long[] var13 = (long[])((long[])value);
            var6 = var13.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var13[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof float[]) {
            float[] var12 = (float[])((float[])value);
            var6 = var12.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var12[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof double[]) {
            double[] var11 = (double[])((double[])value);
            var6 = var11.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var11[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else if (value instanceof String[]) {
            String[] var10 = (String[])((String[])value);
            var6 = var10.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var10[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         } else {
            if (!(value instanceof Type[])) {
               throw new UnsupportedOperationException("Unsupported array value type: " + value.getClass());
            }

            Type[] var5 = (Type[])((Type[])value);
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Object element = var5[var7];
               annotationArray.add(this.getAnnotationElement(name, element));
            }
         }

         elem = new AnnotationArrayElem(annotationArray, '[', name);
      }

      return (AnnotationElem)elem;
   }

   public void visit(String name, Object value) {
      AnnotationElem elem = this.getAnnotationElement(name, value);
      this.elems.add(elem);
   }

   public void visitEnum(String name, String desc, String value) {
      this.elems.add(new AnnotationEnumElem(desc, value, 'e', name));
   }

   public AnnotationVisitor visitArray(final String name) {
      return new AnnotationElemBuilder() {
         public void visitEnd() {
            String ename = name;
            if (ename == null) {
               ename = "default";
            }

            AnnotationElemBuilder.this.elems.add(new AnnotationArrayElem(this.elems, '[', ename));
         }
      };
   }

   public AnnotationVisitor visitAnnotation(final String name, final String desc) {
      return new AnnotationElemBuilder() {
         public void visitEnd() {
            AnnotationTag tag = new AnnotationTag(desc, this.elems);
            AnnotationElemBuilder.this.elems.add(new AnnotationAnnotationElem(tag, '@', name));
         }
      };
   }

   public abstract void visitEnd();
}
