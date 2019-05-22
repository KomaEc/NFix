package org.jf.dexlib2.util;

import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.VerificationError;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class Preconditions {
   public static void checkFormat(Opcode opcode, Format expectedFormat) {
      if (opcode.format != expectedFormat) {
         throw new IllegalArgumentException(String.format("Invalid opcode %s for %s", opcode.name, expectedFormat.name()));
      }
   }

   public static int checkNibbleRegister(int register) {
      if ((register & -16) != 0) {
         throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v15, inclusive.", register));
      } else {
         return register;
      }
   }

   public static int checkByteRegister(int register) {
      if ((register & -256) != 0) {
         throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v255, inclusive.", register));
      } else {
         return register;
      }
   }

   public static int checkShortRegister(int register) {
      if ((register & -65536) != 0) {
         throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v65535, inclusive.", register));
      } else {
         return register;
      }
   }

   public static int checkNibbleLiteral(int literal) {
      if (literal >= -8 && literal <= 7) {
         return literal;
      } else {
         throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -8 and 7, inclusive.", literal));
      }
   }

   public static int checkByteLiteral(int literal) {
      if (literal >= -128 && literal <= 127) {
         return literal;
      } else {
         throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -128 and 127, inclusive.", literal));
      }
   }

   public static int checkShortLiteral(int literal) {
      if (literal >= -32768 && literal <= 32767) {
         return literal;
      } else {
         throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -32768 and 32767, inclusive.", literal));
      }
   }

   public static int checkIntegerHatLiteral(int literal) {
      if ((literal & '\uffff') != 0) {
         throw new IllegalArgumentException(String.format("Invalid literal value: %d. Low 16 bits must be zeroed out.", literal));
      } else {
         return literal;
      }
   }

   public static long checkLongHatLiteral(long literal) {
      if ((literal & 281474976710655L) != 0L) {
         throw new IllegalArgumentException(String.format("Invalid literal value: %d. Low 48 bits must be zeroed out.", literal));
      } else {
         return literal;
      }
   }

   public static int checkByteCodeOffset(int offset) {
      if (offset >= -128 && offset <= 127) {
         return offset;
      } else {
         throw new IllegalArgumentException(String.format("Invalid code offset: %d. Must be between -128 and 127, inclusive.", offset));
      }
   }

   public static int checkShortCodeOffset(int offset) {
      if (offset >= -32768 && offset <= 32767) {
         return offset;
      } else {
         throw new IllegalArgumentException(String.format("Invalid code offset: %d. Must be between -32768 and 32767, inclusive.", offset));
      }
   }

   public static int check35cAnd45ccRegisterCount(int registerCount) {
      if (registerCount >= 0 && registerCount <= 5) {
         return registerCount;
      } else {
         throw new IllegalArgumentException(String.format("Invalid register count: %d. Must be between 0 and 5, inclusive.", registerCount));
      }
   }

   public static int checkRegisterRangeCount(int registerCount) {
      if ((registerCount & -256) != 0) {
         throw new IllegalArgumentException(String.format("Invalid register count: %d. Must be between 0 and 255, inclusive.", registerCount));
      } else {
         return registerCount;
      }
   }

   public static void checkValueArg(int valueArg, int maxValue) {
      if (valueArg > maxValue) {
         if (maxValue == 0) {
            throw new IllegalArgumentException(String.format("Invalid value_arg value %d for an encoded_value. Expecting 0", valueArg));
         } else {
            throw new IllegalArgumentException(String.format("Invalid value_arg value %d for an encoded_value. Expecting 0..%d, inclusive", valueArg, maxValue));
         }
      }
   }

   public static int checkFieldOffset(int fieldOffset) {
      if (fieldOffset >= 0 && fieldOffset <= 65535) {
         return fieldOffset;
      } else {
         throw new IllegalArgumentException(String.format("Invalid field offset: 0x%x. Must be between 0x0000 and 0xFFFF inclusive", fieldOffset));
      }
   }

   public static int checkVtableIndex(int vtableIndex) {
      if (vtableIndex >= 0 && vtableIndex <= 65535) {
         return vtableIndex;
      } else {
         throw new IllegalArgumentException(String.format("Invalid vtable index: %d. Must be between 0 and 65535, inclusive", vtableIndex));
      }
   }

   public static int checkInlineIndex(int inlineIndex) {
      if (inlineIndex >= 0 && inlineIndex <= 65535) {
         return inlineIndex;
      } else {
         throw new IllegalArgumentException(String.format("Invalid inline index: %d. Must be between 0 and 65535, inclusive", inlineIndex));
      }
   }

   public static int checkVerificationError(int verificationError) {
      if (!VerificationError.isValidVerificationError(verificationError)) {
         throw new IllegalArgumentException(String.format("Invalid verification error value: %d. Must be between 1 and 9, inclusive", verificationError));
      } else {
         return verificationError;
      }
   }

   public static <T extends Reference> T checkReference(int referenceType, T reference) {
      switch(referenceType) {
      case 0:
         if (!(reference instanceof StringReference)) {
            throw new IllegalArgumentException("Invalid reference type, expecting a string reference");
         }
         break;
      case 1:
         if (!(reference instanceof TypeReference)) {
            throw new IllegalArgumentException("Invalid reference type, expecting a type reference");
         }
         break;
      case 2:
         if (!(reference instanceof FieldReference)) {
            throw new IllegalArgumentException("Invalid reference type, expecting a field reference");
         }
         break;
      case 3:
         if (!(reference instanceof MethodReference)) {
            throw new IllegalArgumentException("Invalid reference type, expecting a method reference");
         }
         break;
      default:
         throw new IllegalArgumentException(String.format("Not a valid reference type: %d", referenceType));
      }

      return reference;
   }
}
