package org.jf.dexlib2.base;

import java.util.Iterator;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;

public abstract class BaseMethodParameter extends BaseTypeReference implements MethodParameter {
   @Nullable
   public String getSignature() {
      Annotation signatureAnnotation = null;
      Iterator var2 = this.getAnnotations().iterator();

      while(var2.hasNext()) {
         Annotation annotation = (Annotation)var2.next();
         if (annotation.getType().equals("Ldalvik/annotation/Signature;")) {
            signatureAnnotation = annotation;
            break;
         }
      }

      if (signatureAnnotation == null) {
         return null;
      } else {
         ArrayEncodedValue signatureValues = null;
         Iterator var7 = signatureAnnotation.getElements().iterator();

         EncodedValue signatureValue;
         while(var7.hasNext()) {
            AnnotationElement annotationElement = (AnnotationElement)var7.next();
            if (annotationElement.getName().equals("value")) {
               signatureValue = annotationElement.getValue();
               if (signatureValue.getValueType() != 28) {
                  return null;
               }

               signatureValues = (ArrayEncodedValue)signatureValue;
               break;
            }
         }

         if (signatureValues == null) {
            return null;
         } else {
            StringBuilder sb = new StringBuilder();
            Iterator var9 = signatureValues.getValue().iterator();

            while(var9.hasNext()) {
               signatureValue = (EncodedValue)var9.next();
               if (signatureValue.getValueType() != 23) {
                  return null;
               }

               sb.append(((StringEncodedValue)signatureValue).getValue());
            }

            return sb.toString();
         }
      }
   }
}
