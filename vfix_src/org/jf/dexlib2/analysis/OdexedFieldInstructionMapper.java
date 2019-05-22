package org.jf.dexlib2.analysis;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;

public class OdexedFieldInstructionMapper {
   private static final int GET = 0;
   private static final int PUT = 1;
   private static final int INSTANCE = 0;
   private static final int STATIC = 1;
   private static final int PRIMITIVE = 0;
   private static final int WIDE = 1;
   private static final int REFERENCE = 2;
   private static final OdexedFieldInstructionMapper.FieldOpcode[] dalvikFieldOpcodes;
   private static final OdexedFieldInstructionMapper.FieldOpcode[] artFieldOpcodes;
   private final OdexedFieldInstructionMapper.FieldOpcode[][][] opcodeMap = new OdexedFieldInstructionMapper.FieldOpcode[2][2][10];
   private final Map<Opcode, Integer> opcodeValueTypeMap = new HashMap(30);

   private static int getValueType(char type) {
      switch(type) {
      case 'B':
      case 'C':
      case 'F':
      case 'I':
      case 'S':
      case 'Z':
         return 0;
      case 'D':
      case 'J':
         return 1;
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
         throw new RuntimeException(String.format("Unknown type %s: ", type));
      case 'L':
      case '[':
         return 2;
      }
   }

   private static int getTypeIndex(char type) {
      switch(type) {
      case 'B':
         return 1;
      case 'C':
         return 3;
      case 'D':
         return 7;
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
         throw new RuntimeException(String.format("Unknown type %s: ", type));
      case 'F':
         return 5;
      case 'I':
         return 4;
      case 'J':
         return 6;
      case 'L':
         return 8;
      case 'S':
         return 2;
      case 'Z':
         return 0;
      case '[':
         return 9;
      }
   }

   private static boolean isGet(@Nonnull Opcode opcode) {
      return (opcode.flags & 16) != 0;
   }

   private static boolean isStatic(@Nonnull Opcode opcode) {
      return (opcode.flags & 256) != 0;
   }

   public OdexedFieldInstructionMapper(boolean isArt) {
      OdexedFieldInstructionMapper.FieldOpcode[] opcodes;
      if (isArt) {
         opcodes = artFieldOpcodes;
      } else {
         opcodes = dalvikFieldOpcodes;
      }

      OdexedFieldInstructionMapper.FieldOpcode[] var3 = opcodes;
      int var4 = opcodes.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         OdexedFieldInstructionMapper.FieldOpcode fieldOpcode = var3[var5];
         this.opcodeMap[isGet(fieldOpcode.normalOpcode) ? 0 : 1][isStatic(fieldOpcode.normalOpcode) ? 1 : 0][getTypeIndex(fieldOpcode.type)] = fieldOpcode;
         if (fieldOpcode.quickOpcode != null) {
            this.opcodeValueTypeMap.put(fieldOpcode.quickOpcode, getValueType(fieldOpcode.type));
         }

         if (fieldOpcode.volatileOpcode != null) {
            this.opcodeValueTypeMap.put(fieldOpcode.volatileOpcode, getValueType(fieldOpcode.type));
         }
      }

   }

   @Nonnull
   public Opcode getAndCheckDeodexedOpcode(@Nonnull String fieldType, @Nonnull Opcode odexedOpcode) {
      OdexedFieldInstructionMapper.FieldOpcode fieldOpcode = this.opcodeMap[isGet(odexedOpcode) ? 0 : 1][isStatic(odexedOpcode) ? 1 : 0][getTypeIndex(fieldType.charAt(0))];
      if (!this.isCompatible(odexedOpcode, fieldOpcode.type)) {
         throw new AnalysisException(String.format("Incorrect field type \"%s\" for %s", fieldType, odexedOpcode.name), new Object[0]);
      } else {
         return fieldOpcode.normalOpcode;
      }
   }

   private boolean isCompatible(Opcode opcode, char type) {
      Integer valueType = (Integer)this.opcodeValueTypeMap.get(opcode);
      if (valueType == null) {
         throw new RuntimeException("Unexpected opcode: " + opcode.name);
      } else {
         return valueType == getValueType(type);
      }
   }

   static {
      dalvikFieldOpcodes = new OdexedFieldInstructionMapper.FieldOpcode[]{new OdexedFieldInstructionMapper.FieldOpcode('Z', Opcode.IGET_BOOLEAN, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('B', Opcode.IGET_BYTE, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('S', Opcode.IGET_SHORT, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('C', Opcode.IGET_CHAR, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('I', Opcode.IGET, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('F', Opcode.IGET, Opcode.IGET_QUICK, Opcode.IGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('J', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK, Opcode.IGET_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('D', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK, Opcode.IGET_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('L', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK, Opcode.IGET_OBJECT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('[', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK, Opcode.IGET_OBJECT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('Z', Opcode.IPUT_BOOLEAN, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('B', Opcode.IPUT_BYTE, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('S', Opcode.IPUT_SHORT, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('C', Opcode.IPUT_CHAR, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('I', Opcode.IPUT, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('F', Opcode.IPUT, Opcode.IPUT_QUICK, Opcode.IPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('J', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK, Opcode.IPUT_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('D', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK, Opcode.IPUT_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('L', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK, Opcode.IPUT_OBJECT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('[', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK, Opcode.IPUT_OBJECT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('Z', true, Opcode.SPUT_BOOLEAN, Opcode.SPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('B', true, Opcode.SPUT_BYTE, Opcode.SPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('S', true, Opcode.SPUT_SHORT, Opcode.SPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('C', true, Opcode.SPUT_CHAR, Opcode.SPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('I', true, Opcode.SPUT, Opcode.SPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('F', true, Opcode.SPUT, Opcode.SPUT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('J', true, Opcode.SPUT_WIDE, Opcode.SPUT_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('D', true, Opcode.SPUT_WIDE, Opcode.SPUT_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('L', true, Opcode.SPUT_OBJECT, Opcode.SPUT_OBJECT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('[', true, Opcode.SPUT_OBJECT, Opcode.SPUT_OBJECT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('Z', true, Opcode.SGET_BOOLEAN, Opcode.SGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('B', true, Opcode.SGET_BYTE, Opcode.SGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('S', true, Opcode.SGET_SHORT, Opcode.SGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('C', true, Opcode.SGET_CHAR, Opcode.SGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('I', true, Opcode.SGET, Opcode.SGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('F', true, Opcode.SGET, Opcode.SGET_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('J', true, Opcode.SGET_WIDE, Opcode.SGET_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('D', true, Opcode.SGET_WIDE, Opcode.SGET_WIDE_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('L', true, Opcode.SGET_OBJECT, Opcode.SGET_OBJECT_VOLATILE), new OdexedFieldInstructionMapper.FieldOpcode('[', true, Opcode.SGET_OBJECT, Opcode.SGET_OBJECT_VOLATILE)};
      artFieldOpcodes = new OdexedFieldInstructionMapper.FieldOpcode[]{new OdexedFieldInstructionMapper.FieldOpcode('Z', Opcode.IGET_BOOLEAN, Opcode.IGET_BOOLEAN_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('B', Opcode.IGET_BYTE, Opcode.IGET_BYTE_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('S', Opcode.IGET_SHORT, Opcode.IGET_SHORT_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('C', Opcode.IGET_CHAR, Opcode.IGET_CHAR_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('I', Opcode.IGET, Opcode.IGET_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('F', Opcode.IGET, Opcode.IGET_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('J', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('D', Opcode.IGET_WIDE, Opcode.IGET_WIDE_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('L', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('[', Opcode.IGET_OBJECT, Opcode.IGET_OBJECT_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('Z', Opcode.IPUT_BOOLEAN, Opcode.IPUT_BOOLEAN_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('B', Opcode.IPUT_BYTE, Opcode.IPUT_BYTE_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('S', Opcode.IPUT_SHORT, Opcode.IPUT_SHORT_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('C', Opcode.IPUT_CHAR, Opcode.IPUT_CHAR_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('I', Opcode.IPUT, Opcode.IPUT_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('F', Opcode.IPUT, Opcode.IPUT_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('J', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('D', Opcode.IPUT_WIDE, Opcode.IPUT_WIDE_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('L', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK), new OdexedFieldInstructionMapper.FieldOpcode('[', Opcode.IPUT_OBJECT, Opcode.IPUT_OBJECT_QUICK)};
   }

   private static class FieldOpcode {
      public final char type;
      public final boolean isStatic;
      @Nonnull
      public final Opcode normalOpcode;
      @Nullable
      public final Opcode quickOpcode;
      @Nullable
      public final Opcode volatileOpcode;

      public FieldOpcode(char type, @Nonnull Opcode normalOpcode, @Nullable Opcode quickOpcode, @Nullable Opcode volatileOpcode) {
         this.type = type;
         this.isStatic = false;
         this.normalOpcode = normalOpcode;
         this.quickOpcode = quickOpcode;
         this.volatileOpcode = volatileOpcode;
      }

      public FieldOpcode(char type, boolean isStatic, @Nonnull Opcode normalOpcode, @Nullable Opcode volatileOpcode) {
         this.type = type;
         this.isStatic = isStatic;
         this.normalOpcode = normalOpcode;
         this.quickOpcode = null;
         this.volatileOpcode = volatileOpcode;
      }

      public FieldOpcode(char type, @Nonnull Opcode normalOpcode, @Nullable Opcode quickOpcode) {
         this.type = type;
         this.isStatic = false;
         this.normalOpcode = normalOpcode;
         this.quickOpcode = quickOpcode;
         this.volatileOpcode = null;
      }
   }
}
