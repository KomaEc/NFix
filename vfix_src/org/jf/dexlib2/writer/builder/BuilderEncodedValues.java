package org.jf.dexlib2.writer.builder;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseAnnotationEncodedValue;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.base.value.BaseBooleanEncodedValue;
import org.jf.dexlib2.base.value.BaseEnumEncodedValue;
import org.jf.dexlib2.base.value.BaseFieldEncodedValue;
import org.jf.dexlib2.base.value.BaseMethodEncodedValue;
import org.jf.dexlib2.base.value.BaseNullEncodedValue;
import org.jf.dexlib2.base.value.BaseStringEncodedValue;
import org.jf.dexlib2.base.value.BaseTypeEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableByteEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableCharEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableDoubleEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableFloatEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableIntEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableLongEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableShortEncodedValue;
import org.jf.util.ExceptionWithContext;

public abstract class BuilderEncodedValues {
   @Nonnull
   public static BuilderEncodedValues.BuilderEncodedValue defaultValueForType(String type) {
      switch(type.charAt(0)) {
      case 'B':
         return new BuilderEncodedValues.BuilderByteEncodedValue((byte)0);
      case 'C':
         return new BuilderEncodedValues.BuilderCharEncodedValue('\u0000');
      case 'D':
         return new BuilderEncodedValues.BuilderDoubleEncodedValue(0.0D);
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
         return new BuilderEncodedValues.BuilderFloatEncodedValue(0.0F);
      case 'I':
         return new BuilderEncodedValues.BuilderIntEncodedValue(0);
      case 'J':
         return new BuilderEncodedValues.BuilderLongEncodedValue(0L);
      case 'L':
      case '[':
         return BuilderEncodedValues.BuilderNullEncodedValue.INSTANCE;
      case 'S':
         return new BuilderEncodedValues.BuilderShortEncodedValue((short)0);
      case 'Z':
         return BuilderEncodedValues.BuilderBooleanEncodedValue.FALSE_VALUE;
      }
   }

   public static class BuilderTypeEncodedValue extends BaseTypeEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      @Nonnull
      final BuilderTypeReference typeReference;

      BuilderTypeEncodedValue(@Nonnull BuilderTypeReference typeReference) {
         this.typeReference = typeReference;
      }

      @Nonnull
      public String getValue() {
         return this.typeReference.getType();
      }
   }

   public static class BuilderStringEncodedValue extends BaseStringEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      @Nonnull
      final BuilderStringReference stringReference;

      BuilderStringEncodedValue(@Nonnull BuilderStringReference stringReference) {
         this.stringReference = stringReference;
      }

      @Nonnull
      public String getValue() {
         return this.stringReference.getString();
      }
   }

   public static class BuilderShortEncodedValue extends ImmutableShortEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public BuilderShortEncodedValue(short value) {
         super(value);
      }
   }

   public static class BuilderNullEncodedValue extends BaseNullEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public static final BuilderEncodedValues.BuilderNullEncodedValue INSTANCE = new BuilderEncodedValues.BuilderNullEncodedValue();

      private BuilderNullEncodedValue() {
      }
   }

   public static class BuilderMethodEncodedValue extends BaseMethodEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      @Nonnull
      final BuilderMethodReference methodReference;

      BuilderMethodEncodedValue(@Nonnull BuilderMethodReference methodReference) {
         this.methodReference = methodReference;
      }

      public BuilderMethodReference getValue() {
         return this.methodReference;
      }
   }

   public static class BuilderLongEncodedValue extends ImmutableLongEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public BuilderLongEncodedValue(long value) {
         super(value);
      }
   }

   public static class BuilderIntEncodedValue extends ImmutableIntEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public BuilderIntEncodedValue(int value) {
         super(value);
      }
   }

   public static class BuilderFloatEncodedValue extends ImmutableFloatEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public BuilderFloatEncodedValue(float value) {
         super(value);
      }
   }

   public static class BuilderFieldEncodedValue extends BaseFieldEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      @Nonnull
      final BuilderFieldReference fieldReference;

      BuilderFieldEncodedValue(@Nonnull BuilderFieldReference fieldReference) {
         this.fieldReference = fieldReference;
      }

      @Nonnull
      public BuilderFieldReference getValue() {
         return this.fieldReference;
      }
   }

   public static class BuilderEnumEncodedValue extends BaseEnumEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      @Nonnull
      final BuilderFieldReference enumReference;

      BuilderEnumEncodedValue(@Nonnull BuilderFieldReference enumReference) {
         this.enumReference = enumReference;
      }

      @Nonnull
      public BuilderFieldReference getValue() {
         return this.enumReference;
      }
   }

   public static class BuilderDoubleEncodedValue extends ImmutableDoubleEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public BuilderDoubleEncodedValue(double value) {
         super(value);
      }
   }

   public static class BuilderCharEncodedValue extends ImmutableCharEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public BuilderCharEncodedValue(char value) {
         super(value);
      }
   }

   public static class BuilderByteEncodedValue extends ImmutableByteEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public BuilderByteEncodedValue(byte value) {
         super(value);
      }
   }

   public static class BuilderBooleanEncodedValue extends BaseBooleanEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      public static final BuilderEncodedValues.BuilderBooleanEncodedValue TRUE_VALUE = new BuilderEncodedValues.BuilderBooleanEncodedValue(true);
      public static final BuilderEncodedValues.BuilderBooleanEncodedValue FALSE_VALUE = new BuilderEncodedValues.BuilderBooleanEncodedValue(false);
      private final boolean value;

      private BuilderBooleanEncodedValue(boolean value) {
         this.value = value;
      }

      public boolean getValue() {
         return this.value;
      }
   }

   public static class BuilderArrayEncodedValue extends BaseArrayEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      @Nonnull
      final List<? extends BuilderEncodedValues.BuilderEncodedValue> elements;

      BuilderArrayEncodedValue(@Nonnull List<? extends BuilderEncodedValues.BuilderEncodedValue> elements) {
         this.elements = elements;
      }

      @Nonnull
      public List<? extends EncodedValue> getValue() {
         return this.elements;
      }
   }

   public static class BuilderAnnotationEncodedValue extends BaseAnnotationEncodedValue implements BuilderEncodedValues.BuilderEncodedValue {
      @Nonnull
      final BuilderTypeReference typeReference;
      @Nonnull
      final Set<? extends BuilderAnnotationElement> elements;

      BuilderAnnotationEncodedValue(@Nonnull BuilderTypeReference typeReference, @Nonnull Set<? extends BuilderAnnotationElement> elements) {
         this.typeReference = typeReference;
         this.elements = elements;
      }

      @Nonnull
      public String getType() {
         return this.typeReference.getType();
      }

      @Nonnull
      public Set<? extends BuilderAnnotationElement> getElements() {
         return this.elements;
      }
   }

   public interface BuilderEncodedValue extends EncodedValue {
   }
}
