package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.ExceptionWithContext;

public class EncodedValue {
   public static void annotateEncodedValue(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader) {
      int valueArgType = reader.readUbyte();
      int valueArg = valueArgType >>> 5;
      int valueType = valueArgType & 31;
      int intValue;
      int fieldIndex;
      switch(valueType) {
      case 0:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: byte", valueArg, valueType);
         intValue = reader.readByte();
         out.annotate(1, "value = 0x%x", intValue);
         break;
      case 1:
      case 5:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      default:
         throw new ExceptionWithContext("Invalid encoded value type 0x%x at offset 0x%x", new Object[]{valueType, out.getCursor()});
      case 2:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: short", valueArg, valueType);
         intValue = reader.readSizedInt(valueArg + 1);
         out.annotate(valueArg + 1, "value = 0x%x", intValue);
         break;
      case 3:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: char", valueArg, valueType);
         intValue = reader.readSizedSmallUint(valueArg + 1);
         out.annotate(valueArg + 1, "value = 0x%x", intValue);
         break;
      case 4:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: int", valueArg, valueType);
         intValue = reader.readSizedInt(valueArg + 1);
         out.annotate(valueArg + 1, "value = 0x%x", intValue);
         break;
      case 6:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: long", valueArg, valueType);
         long longValue = reader.readSizedLong(valueArg + 1);
         out.annotate(valueArg + 1, "value = 0x%x", longValue);
         break;
      case 16:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: float", valueArg, valueType);
         float floatValue = Float.intBitsToFloat(reader.readSizedRightExtendedInt(valueArg + 1));
         out.annotate(valueArg + 1, "value = %f", floatValue);
         break;
      case 17:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: double", valueArg, valueType);
         double doubleValue = Double.longBitsToDouble(reader.readSizedRightExtendedLong(valueArg + 1));
         out.annotate(valueArg + 1, "value = %f", doubleValue);
         break;
      case 23:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: string", valueArg, valueType);
         int stringIndex = reader.readSizedSmallUint(valueArg + 1);
         out.annotate(valueArg + 1, "value = %s", StringIdItem.getReferenceAnnotation((DexBackedDexFile)reader.dexBuf, stringIndex, true));
         break;
      case 24:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: type", valueArg, valueType);
         int typeIndex = reader.readSizedSmallUint(valueArg + 1);
         out.annotate(valueArg + 1, "value = %s", TypeIdItem.getReferenceAnnotation((DexBackedDexFile)reader.dexBuf, typeIndex));
         break;
      case 25:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: field", valueArg, valueType);
         fieldIndex = reader.readSizedSmallUint(valueArg + 1);
         out.annotate(valueArg + 1, "value = %s", FieldIdItem.getReferenceAnnotation((DexBackedDexFile)reader.dexBuf, fieldIndex));
         break;
      case 26:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: method", valueArg, valueType);
         int methodIndex = reader.readSizedSmallUint(valueArg + 1);
         out.annotate(valueArg + 1, "value = %s", MethodIdItem.getReferenceAnnotation((DexBackedDexFile)reader.dexBuf, methodIndex));
         break;
      case 27:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: enum", valueArg, valueType);
         fieldIndex = reader.readSizedSmallUint(valueArg + 1);
         out.annotate(valueArg + 1, "value = %s", FieldIdItem.getReferenceAnnotation((DexBackedDexFile)reader.dexBuf, fieldIndex));
         break;
      case 28:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: array", valueArg, valueType);
         annotateEncodedArray(out, reader);
         break;
      case 29:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: annotation", valueArg, valueType);
         annotateEncodedAnnotation(out, reader);
         break;
      case 30:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: null", valueArg, valueType);
         break;
      case 31:
         out.annotate(1, "valueArg = %d, valueType = 0x%x: boolean, value=%s", valueArg, valueType, valueArg == 1);
      }

   }

   public static void annotateEncodedAnnotation(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader) {
      assert out.getCursor() == reader.getOffset();

      int typeIndex = reader.readSmallUleb128();
      out.annotateTo(reader.getOffset(), TypeIdItem.getReferenceAnnotation((DexBackedDexFile)reader.dexBuf, typeIndex));
      int size = reader.readSmallUleb128();
      out.annotateTo(reader.getOffset(), "size: %d", size);

      for(int i = 0; i < size; ++i) {
         out.annotate(0, "element[%d]", i);
         out.indent();
         int nameIndex = reader.readSmallUleb128();
         out.annotateTo(reader.getOffset(), "name = %s", StringIdItem.getReferenceAnnotation((DexBackedDexFile)reader.dexBuf, nameIndex));
         annotateEncodedValue(out, reader);
         out.deindent();
      }

   }

   public static void annotateEncodedArray(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader) {
      assert out.getCursor() == reader.getOffset();

      int size = reader.readSmallUleb128();
      out.annotateTo(reader.getOffset(), "size: %d", size);

      for(int i = 0; i < size; ++i) {
         out.annotate(0, "element[%d]", i);
         out.indent();
         annotateEncodedValue(out, reader);
         out.deindent();
      }

   }
}
