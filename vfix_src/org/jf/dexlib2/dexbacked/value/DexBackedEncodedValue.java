package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableBooleanEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableByteEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableCharEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableDoubleEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableFloatEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableIntEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableLongEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableNullEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableShortEncodedValue;
import org.jf.dexlib2.util.Preconditions;
import org.jf.util.ExceptionWithContext;

public abstract class DexBackedEncodedValue {
   @Nonnull
   public static EncodedValue readFrom(@Nonnull DexReader reader) {
      int startOffset = reader.getOffset();

      try {
         int b = reader.readUbyte();
         int valueType = b & 31;
         int valueArg = b >>> 5;
         switch(valueType) {
         case 0:
            Preconditions.checkValueArg(valueArg, 0);
            return new ImmutableByteEncodedValue((byte)reader.readByte());
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
            throw new ExceptionWithContext("Invalid encoded_value type: 0x%x", new Object[]{valueType});
         case 2:
            Preconditions.checkValueArg(valueArg, 1);
            return new ImmutableShortEncodedValue((short)reader.readSizedInt(valueArg + 1));
         case 3:
            Preconditions.checkValueArg(valueArg, 1);
            return new ImmutableCharEncodedValue((char)reader.readSizedSmallUint(valueArg + 1));
         case 4:
            Preconditions.checkValueArg(valueArg, 3);
            return new ImmutableIntEncodedValue(reader.readSizedInt(valueArg + 1));
         case 6:
            Preconditions.checkValueArg(valueArg, 7);
            return new ImmutableLongEncodedValue(reader.readSizedLong(valueArg + 1));
         case 16:
            Preconditions.checkValueArg(valueArg, 3);
            return new ImmutableFloatEncodedValue(Float.intBitsToFloat(reader.readSizedRightExtendedInt(valueArg + 1)));
         case 17:
            Preconditions.checkValueArg(valueArg, 7);
            return new ImmutableDoubleEncodedValue(Double.longBitsToDouble(reader.readSizedRightExtendedLong(valueArg + 1)));
         case 23:
            Preconditions.checkValueArg(valueArg, 3);
            return new DexBackedStringEncodedValue(reader, valueArg);
         case 24:
            Preconditions.checkValueArg(valueArg, 3);
            return new DexBackedTypeEncodedValue(reader, valueArg);
         case 25:
            Preconditions.checkValueArg(valueArg, 3);
            return new DexBackedFieldEncodedValue(reader, valueArg);
         case 26:
            Preconditions.checkValueArg(valueArg, 3);
            return new DexBackedMethodEncodedValue(reader, valueArg);
         case 27:
            Preconditions.checkValueArg(valueArg, 3);
            return new DexBackedEnumEncodedValue(reader, valueArg);
         case 28:
            Preconditions.checkValueArg(valueArg, 0);
            return new DexBackedArrayEncodedValue(reader);
         case 29:
            Preconditions.checkValueArg(valueArg, 0);
            return new DexBackedAnnotationEncodedValue(reader);
         case 30:
            Preconditions.checkValueArg(valueArg, 0);
            return ImmutableNullEncodedValue.INSTANCE;
         case 31:
            Preconditions.checkValueArg(valueArg, 1);
            return ImmutableBooleanEncodedValue.forBoolean(valueArg == 1);
         }
      } catch (Exception var5) {
         throw ExceptionWithContext.withContext(var5, "Error while reading encoded value at offset 0x%x", startOffset);
      }
   }

   public static void skipFrom(@Nonnull DexReader reader) {
      int startOffset = reader.getOffset();

      try {
         int b = reader.readUbyte();
         int valueType = b & 31;
         switch(valueType) {
         case 0:
            reader.skipByte();
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
            throw new ExceptionWithContext("Invalid encoded_value type: 0x%x", new Object[]{valueType});
         case 2:
         case 3:
         case 4:
         case 6:
         case 16:
         case 17:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
            int valueArg = b >>> 5;
            reader.moveRelative(valueArg + 1);
            break;
         case 28:
            DexBackedArrayEncodedValue.skipFrom(reader);
            break;
         case 29:
            DexBackedAnnotationEncodedValue.skipFrom(reader);
         case 30:
         case 31:
         }

      } catch (Exception var5) {
         throw ExceptionWithContext.withContext(var5, "Error while skipping encoded value at offset 0x%x", startOffset);
      }
   }
}
