package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.BaseDexBuffer;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.StringUtils;

public class HeaderItem {
   public static final int ITEM_SIZE = 112;
   private static final byte[] MAGIC_VALUE = new byte[]{100, 101, 120, 10, 0, 0, 0, 0};
   private static final int[] SUPPORTED_DEX_VERSIONS = new int[]{35, 37};
   public static final int LITTLE_ENDIAN_TAG = 305419896;
   public static final int BIG_ENDIAN_TAG = 2018915346;
   public static final int CHECKSUM_OFFSET = 8;
   public static final int CHECKSUM_DATA_START_OFFSET = 12;
   public static final int SIGNATURE_OFFSET = 12;
   public static final int SIGNATURE_SIZE = 20;
   public static final int SIGNATURE_DATA_START_OFFSET = 32;
   public static final int FILE_SIZE_OFFSET = 32;
   public static final int HEADER_SIZE_OFFSET = 36;
   public static final int ENDIAN_TAG_OFFSET = 40;
   public static final int MAP_OFFSET = 52;
   public static final int STRING_COUNT_OFFSET = 56;
   public static final int STRING_START_OFFSET = 60;
   public static final int TYPE_COUNT_OFFSET = 64;
   public static final int TYPE_START_OFFSET = 68;
   public static final int PROTO_COUNT_OFFSET = 72;
   public static final int PROTO_START_OFFSET = 76;
   public static final int FIELD_COUNT_OFFSET = 80;
   public static final int FIELD_START_OFFSET = 84;
   public static final int METHOD_COUNT_OFFSET = 88;
   public static final int METHOD_START_OFFSET = 92;
   public static final int CLASS_COUNT_OFFSET = 96;
   public static final int CLASS_START_OFFSET = 100;
   @Nonnull
   private RawDexFile dexFile;

   public HeaderItem(@Nonnull RawDexFile dexFile) {
      this.dexFile = dexFile;
   }

   public int getChecksum() {
      return this.dexFile.readSmallUint(8);
   }

   @Nonnull
   public byte[] getSignature() {
      return this.dexFile.readByteRange(12, 20);
   }

   public int getMapOffset() {
      return this.dexFile.readSmallUint(52);
   }

   public int getHeaderSize() {
      return this.dexFile.readSmallUint(36);
   }

   public int getStringCount() {
      return this.dexFile.readSmallUint(56);
   }

   public int getStringOffset() {
      return this.dexFile.readSmallUint(60);
   }

   public int getTypeCount() {
      return this.dexFile.readSmallUint(64);
   }

   public int getTypeOffset() {
      return this.dexFile.readSmallUint(68);
   }

   public int getProtoCount() {
      return this.dexFile.readSmallUint(72);
   }

   public int getProtoOffset() {
      return this.dexFile.readSmallUint(76);
   }

   public int getFieldCount() {
      return this.dexFile.readSmallUint(80);
   }

   public int getFieldOffset() {
      return this.dexFile.readSmallUint(84);
   }

   public int getMethodCount() {
      return this.dexFile.readSmallUint(88);
   }

   public int getMethodOffset() {
      return this.dexFile.readSmallUint(92);
   }

   public int getClassCount() {
      return this.dexFile.readSmallUint(96);
   }

   public int getClassOffset() {
      return this.dexFile.readSmallUint(100);
   }

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "header_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int startOffset = out.getCursor();
            StringBuilder magicBuilder = new StringBuilder();

            int endianTag;
            for(endianTag = 0; endianTag < 8; ++endianTag) {
               magicBuilder.append((char)this.dexFile.readUbyte(startOffset + endianTag));
            }

            out.annotate(8, "magic: %s", StringUtils.escapeString(magicBuilder.toString()));
            out.annotate(4, "checksum");
            out.annotate(20, "signature");
            out.annotate(4, "file_size: %d", this.dexFile.readInt(out.getCursor()));
            int headerSize = this.dexFile.readInt(out.getCursor());
            out.annotate(4, "header_size: %d", headerSize);
            endianTag = this.dexFile.readInt(out.getCursor());
            out.annotate(4, "endian_tag: 0x%x (%s)", endianTag, HeaderItem.getEndianText(endianTag));
            out.annotate(4, "link_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "link_offset: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "map_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "string_ids_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "string_ids_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "type_ids_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "type_ids_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "proto_ids_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "proto_ids_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "field_ids_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "field_ids_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "method_ids_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "method_ids_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "class_defs_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "class_defs_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "data_size: %d", this.dexFile.readInt(out.getCursor()));
            out.annotate(4, "data_off: 0x%x", this.dexFile.readInt(out.getCursor()));
            if (headerSize > 112) {
               out.annotateTo(headerSize, "header padding");
            }

         }
      };
   }

   private static String getEndianText(int endianTag) {
      if (endianTag == 305419896) {
         return "Little Endian";
      } else {
         return endianTag == 2018915346 ? "Big Endian" : "Invalid";
      }
   }

   public static byte[] getMagicForApi(int api) {
      return api < 24 ? getMagicForDexVersion(35) : getMagicForDexVersion(37);
   }

   public static byte[] getMagicForDexVersion(int dexVersion) {
      byte[] magic = (byte[])MAGIC_VALUE.clone();
      if (dexVersion >= 0 && dexVersion <= 999) {
         for(int i = 6; i >= 4; --i) {
            int digit = dexVersion % 10;
            magic[i] = (byte)(48 + digit);
            dexVersion /= 10;
         }

         return magic;
      } else {
         throw new IllegalArgumentException("dexVersion must be within [0, 999]");
      }
   }

   public static boolean verifyMagic(byte[] buf, int offset) {
      if (buf.length - offset < 8) {
         return false;
      } else {
         int i;
         for(i = 0; i < 4; ++i) {
            if (buf[offset + i] != MAGIC_VALUE[i]) {
               return false;
            }
         }

         for(i = 4; i < 7; ++i) {
            if (buf[offset + i] < 48 || buf[offset + i] > 57) {
               return false;
            }
         }

         if (buf[offset + 7] != MAGIC_VALUE[7]) {
            return false;
         } else {
            return true;
         }
      }
   }

   public static int getVersion(byte[] buf, int offset) {
      return !verifyMagic(buf, offset) ? -1 : getVersionUnchecked(buf, offset);
   }

   private static int getVersionUnchecked(byte[] buf, int offset) {
      int version = (buf[offset + 4] - 48) * 100;
      version += (buf[offset + 5] - 48) * 10;
      version += buf[offset + 6] - 48;
      return version;
   }

   public static boolean isSupportedDexVersion(int version) {
      for(int i = 0; i < SUPPORTED_DEX_VERSIONS.length; ++i) {
         if (SUPPORTED_DEX_VERSIONS[i] == version) {
            return true;
         }
      }

      return false;
   }

   public static int getEndian(byte[] buf, int offset) {
      BaseDexBuffer bdb = new BaseDexBuffer(buf);
      return bdb.readInt(offset + 40);
   }
}
