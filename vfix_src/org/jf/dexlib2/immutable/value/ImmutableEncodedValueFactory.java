package org.jf.dexlib2.immutable.value;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.EnumEncodedValue;
import org.jf.dexlib2.iface.value.FieldEncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import org.jf.util.ExceptionWithContext;
import org.jf.util.ImmutableConverter;

public class ImmutableEncodedValueFactory {
   private static final ImmutableConverter<ImmutableEncodedValue, EncodedValue> CONVERTER = new ImmutableConverter<ImmutableEncodedValue, EncodedValue>() {
      protected boolean isImmutable(@Nonnull EncodedValue item) {
         return item instanceof ImmutableEncodedValue;
      }

      @Nonnull
      protected ImmutableEncodedValue makeImmutable(@Nonnull EncodedValue item) {
         return ImmutableEncodedValueFactory.of(item);
      }
   };

   @Nonnull
   public static ImmutableEncodedValue of(@Nonnull EncodedValue encodedValue) {
      switch(encodedValue.getValueType()) {
      case 0:
         return ImmutableByteEncodedValue.of((ByteEncodedValue)encodedValue);
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
         Preconditions.checkArgument(false);
         return null;
      case 2:
         return ImmutableShortEncodedValue.of((ShortEncodedValue)encodedValue);
      case 3:
         return ImmutableCharEncodedValue.of((CharEncodedValue)encodedValue);
      case 4:
         return ImmutableIntEncodedValue.of((IntEncodedValue)encodedValue);
      case 6:
         return ImmutableLongEncodedValue.of((LongEncodedValue)encodedValue);
      case 16:
         return ImmutableFloatEncodedValue.of((FloatEncodedValue)encodedValue);
      case 17:
         return ImmutableDoubleEncodedValue.of((DoubleEncodedValue)encodedValue);
      case 23:
         return ImmutableStringEncodedValue.of((StringEncodedValue)encodedValue);
      case 24:
         return ImmutableTypeEncodedValue.of((TypeEncodedValue)encodedValue);
      case 25:
         return ImmutableFieldEncodedValue.of((FieldEncodedValue)encodedValue);
      case 26:
         return ImmutableMethodEncodedValue.of((MethodEncodedValue)encodedValue);
      case 27:
         return ImmutableEnumEncodedValue.of((EnumEncodedValue)encodedValue);
      case 28:
         return ImmutableArrayEncodedValue.of((ArrayEncodedValue)encodedValue);
      case 29:
         return ImmutableAnnotationEncodedValue.of((AnnotationEncodedValue)encodedValue);
      case 30:
         return ImmutableNullEncodedValue.INSTANCE;
      case 31:
         return ImmutableBooleanEncodedValue.of((BooleanEncodedValue)encodedValue);
      }
   }

   @Nonnull
   public static EncodedValue defaultValueForType(String type) {
      switch(type.charAt(0)) {
      case 'B':
         return new ImmutableByteEncodedValue((byte)0);
      case 'C':
         return new ImmutableCharEncodedValue('\u0000');
      case 'D':
         return new ImmutableDoubleEncodedValue(0.0D);
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      default:
         throw new ExceptionWithContext("Unrecognized type: %s", new Object[]{type});
      case 'F':
         return new ImmutableFloatEncodedValue(0.0F);
      case 'I':
         return new ImmutableIntEncodedValue(0);
      case 'J':
         return new ImmutableLongEncodedValue(0L);
      case 'L':
      case '[':
         return ImmutableNullEncodedValue.INSTANCE;
      case 'S':
         return new ImmutableShortEncodedValue((short)0);
      case 'Z':
         return ImmutableBooleanEncodedValue.FALSE_VALUE;
      }
   }

   @Nullable
   public static ImmutableEncodedValue ofNullable(@Nullable EncodedValue encodedValue) {
      return encodedValue == null ? null : of(encodedValue);
   }

   @Nonnull
   public static ImmutableList<ImmutableEncodedValue> immutableListOf(@Nullable Iterable<? extends EncodedValue> list) {
      return CONVERTER.toList(list);
   }
}
