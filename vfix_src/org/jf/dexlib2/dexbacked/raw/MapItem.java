package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class MapItem {
   public static final int ITEM_SIZE = 12;
   public static final int TYPE_OFFSET = 0;
   public static final int SIZE_OFFSET = 4;
   public static final int OFFSET_OFFSET = 8;
   private final DexBackedDexFile dexFile;
   private final int offset;

   public MapItem(DexBackedDexFile dexFile, int offset) {
      this.dexFile = dexFile;
      this.offset = offset;
   }

   public int getType() {
      return this.dexFile.readUshort(this.offset + 0);
   }

   @Nonnull
   public String getName() {
      return ItemType.getItemTypeName(this.getType());
   }

   public int getItemCount() {
      return this.dexFile.readSmallUint(this.offset + 4);
   }

   public int getOffset() {
      return this.dexFile.readSmallUint(this.offset + 8);
   }

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "map_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int itemType = this.dexFile.readUshort(out.getCursor());
            out.annotate(2, "type = 0x%x: %s", itemType, ItemType.getItemTypeName(itemType));
            out.annotate(2, "unused");
            int size = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "size = %d", size);
            int offset = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "offset = 0x%x", offset);
         }

         public void annotateSection(@Nonnull AnnotatedBytes out) {
            out.moveTo(this.sectionOffset);
            int mapItemCount = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "size = %d", mapItemCount);
            super.annotateSectionInner(out, mapItemCount);
         }
      };
   }
}
