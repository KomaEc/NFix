package org.jf.dexlib2.immutable.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ExceptionWithContext;

public class ImmutableReferenceFactory {
   @Nonnull
   public static ImmutableReference of(Reference reference) {
      if (reference instanceof StringReference) {
         return ImmutableStringReference.of((StringReference)reference);
      } else if (reference instanceof TypeReference) {
         return ImmutableTypeReference.of((TypeReference)reference);
      } else if (reference instanceof FieldReference) {
         return ImmutableFieldReference.of((FieldReference)reference);
      } else if (reference instanceof MethodReference) {
         return ImmutableMethodReference.of((MethodReference)reference);
      } else if (reference instanceof MethodProtoReference) {
         return ImmutableMethodProtoReference.of((MethodProtoReference)reference);
      } else {
         throw new ExceptionWithContext("Invalid reference type", new Object[0]);
      }
   }

   @Nonnull
   public static ImmutableReference of(int referenceType, Reference reference) {
      switch(referenceType) {
      case 0:
         return ImmutableStringReference.of((StringReference)reference);
      case 1:
         return ImmutableTypeReference.of((TypeReference)reference);
      case 2:
         return ImmutableFieldReference.of((FieldReference)reference);
      case 3:
         return ImmutableMethodReference.of((MethodReference)reference);
      case 4:
         return ImmutableMethodProtoReference.of((MethodProtoReference)reference);
      default:
         throw new ExceptionWithContext("Invalid reference type: %d", new Object[]{referenceType});
      }
   }
}
