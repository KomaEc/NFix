package soot.util.annotations;

import java.util.Iterator;
import org.jboss.util.Classes;
import soot.tagkit.AbstractAnnotationElemTypeSwitch;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;

public class AnnotationElemSwitch extends AbstractAnnotationElemTypeSwitch {
   public void caseAnnotationAnnotationElem(AnnotationAnnotationElem v) {
      AnnotationInstanceCreator aic = new AnnotationInstanceCreator();
      Object result = aic.create(v.getValue());
      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), result));
   }

   public void caseAnnotationArrayElem(AnnotationArrayElem v) {
      Object[] result = new Object[v.getNumValues()];
      int i = 0;

      for(Iterator var4 = v.getValues().iterator(); var4.hasNext(); ++i) {
         AnnotationElem elem = (AnnotationElem)var4.next();
         AnnotationElemSwitch sw = new AnnotationElemSwitch();
         elem.apply(sw);
         result[i] = ((AnnotationElemSwitch.AnnotationElemResult)sw.getResult()).getValue();
      }

      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), result));
   }

   public void caseAnnotationBooleanElem(AnnotationBooleanElem v) {
      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), v.getValue()));
   }

   public void caseAnnotationClassElem(AnnotationClassElem v) {
      try {
         Class<?> clazz = Classes.loadClass(v.getDesc().replace('/', '.'));
         this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), clazz));
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException("Could not load class: " + v.getDesc());
      }
   }

   public void caseAnnotationDoubleElem(AnnotationDoubleElem v) {
      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), v.getValue()));
   }

   public void caseAnnotationEnumElem(AnnotationEnumElem v) {
      try {
         Class<?> clazz = Classes.loadClass(v.getTypeName().replace('/', '.'));
         Enum<?> result = null;
         Object[] var4 = clazz.getEnumConstants();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object o = var4[var6];

            try {
               Enum<?> t = (Enum)o;
               if (t.name().equals(v.getConstantName())) {
                  result = t;
                  break;
               }
            } catch (ClassCastException var9) {
               throw new RuntimeException("Class " + v.getTypeName() + " is no Enum");
            }
         }

         if (result == null) {
            throw new RuntimeException(v.getConstantName() + " is not a EnumConstant of " + v.getTypeName());
         } else {
            this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), result));
         }
      } catch (ClassNotFoundException var10) {
         throw new RuntimeException("Could not load class: " + v.getTypeName());
      }
   }

   public void caseAnnotationFloatElem(AnnotationFloatElem v) {
      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), v.getValue()));
   }

   public void caseAnnotationIntElem(AnnotationIntElem v) {
      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), v.getValue()));
   }

   public void caseAnnotationLongElem(AnnotationLongElem v) {
      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), v.getValue()));
   }

   public void caseAnnotationStringElem(AnnotationStringElem v) {
      this.setResult(new AnnotationElemSwitch.AnnotationElemResult(v.getName(), v.getValue()));
   }

   public void defaultCase(Object object) {
      throw new RuntimeException("Unexpected AnnotationElem");
   }

   public class AnnotationElemResult<V> {
      private String name;
      private V value;

      public AnnotationElemResult(String name, V value) {
         this.name = name;
         this.value = value;
      }

      public String getKey() {
         return this.name;
      }

      public V getValue() {
         return this.value;
      }
   }
}
