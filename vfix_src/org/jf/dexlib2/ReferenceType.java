package org.jf.dexlib2;

import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ExceptionWithContext;

public final class ReferenceType {
   public static final int STRING = 0;
   public static final int TYPE = 1;
   public static final int FIELD = 2;
   public static final int METHOD = 3;
   public static final int METHOD_PROTO = 4;
   public static final int NONE = 5;

   public static String toString(int referenceType) {
      switch(referenceType) {
      case 0:
         return "string";
      case 1:
         return "type";
      case 2:
         return "field";
      case 3:
         return "method";
      case 4:
         return "method_proto";
      default:
         throw new ReferenceType.InvalidReferenceTypeException(referenceType);
      }
   }

   public static int getReferenceType(Reference reference) {
      if (reference instanceof StringReference) {
         return 0;
      } else if (reference instanceof TypeReference) {
         return 1;
      } else if (reference instanceof FieldReference) {
         return 2;
      } else if (reference instanceof MethodReference) {
         return 3;
      } else if (reference instanceof MethodProtoReference) {
         return 4;
      } else {
         throw new IllegalStateException("Invalid reference");
      }
   }

   public static void validateReferenceType(int referenceType) {
      if (referenceType < 0 || referenceType > 4) {
         throw new ReferenceType.InvalidReferenceTypeException(referenceType);
      }
   }

   private ReferenceType() {
   }

   public static class InvalidReferenceTypeException extends ExceptionWithContext {
      private final int referenceType;

      public InvalidReferenceTypeException(int referenceType) {
         super("Invalid reference type: %d", referenceType);
         this.referenceType = referenceType;
      }

      public InvalidReferenceTypeException(int referenceType, String message, Object... formatArgs) {
         super(message, formatArgs);
         this.referenceType = referenceType;
      }

      public int getReferenceType() {
         return this.referenceType;
      }
   }
}
