package org.jf.dexlib2;

import com.google.common.collect.Maps;
import com.google.common.collect.RangeMap;
import java.util.EnumMap;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Opcodes {
   public final int api;
   public final int artVersion;
   @Nonnull
   private final Opcode[] opcodesByValue = new Opcode[255];
   @Nonnull
   private final EnumMap<Opcode, Short> opcodeValues;
   @Nonnull
   private final HashMap<String, Opcode> opcodesByName;

   @Nonnull
   public static Opcodes forApi(int api) {
      return new Opcodes(api, -1);
   }

   @Nonnull
   public static Opcodes forArtVersion(int artVersion) {
      return new Opcodes(-1, artVersion);
   }

   @Nonnull
   public static Opcodes getDefault() {
      return forApi(20);
   }

   private Opcodes(int api, int artVersion) {
      if (api >= 21) {
         this.api = api;
         this.artVersion = VersionMap.mapApiToArtVersion(api);
      } else if (artVersion >= 0 && artVersion < 39) {
         this.api = VersionMap.mapArtVersionToApi(artVersion);
         this.artVersion = artVersion;
      } else {
         this.api = api;
         this.artVersion = artVersion;
      }

      this.opcodeValues = new EnumMap(Opcode.class);
      this.opcodesByName = Maps.newHashMap();
      int version;
      if (this.isArt()) {
         version = this.artVersion;
      } else {
         version = this.api;
      }

      Opcode[] var4 = Opcode.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Opcode opcode = var4[var6];
         RangeMap versionToValueMap;
         if (this.isArt()) {
            versionToValueMap = opcode.artVersionToValueMap;
         } else {
            versionToValueMap = opcode.apiToValueMap;
         }

         Short opcodeValue = (Short)versionToValueMap.get(version);
         if (opcodeValue != null) {
            if (!opcode.format.isPayloadFormat) {
               this.opcodesByValue[opcodeValue] = opcode;
            }

            this.opcodeValues.put(opcode, opcodeValue);
            this.opcodesByName.put(opcode.name.toLowerCase(), opcode);
         }
      }

   }

   @Nullable
   public Opcode getOpcodeByName(@Nonnull String opcodeName) {
      return (Opcode)this.opcodesByName.get(opcodeName.toLowerCase());
   }

   @Nullable
   public Opcode getOpcodeByValue(int opcodeValue) {
      switch(opcodeValue) {
      case 256:
         return Opcode.PACKED_SWITCH_PAYLOAD;
      case 512:
         return Opcode.SPARSE_SWITCH_PAYLOAD;
      case 768:
         return Opcode.ARRAY_PAYLOAD;
      default:
         return opcodeValue >= 0 && opcodeValue < this.opcodesByValue.length ? this.opcodesByValue[opcodeValue] : null;
      }
   }

   @Nullable
   public Short getOpcodeValue(@Nonnull Opcode opcode) {
      return (Short)this.opcodeValues.get(opcode);
   }

   public boolean isArt() {
      return this.artVersion != -1;
   }
}
