package org.jf.dexlib2.dexbacked.raw;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.BaseDexBuffer;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.util.AnnotatedBytes;

public class RawDexFile extends DexBackedDexFile {
   @Nonnull
   public final HeaderItem headerItem = new HeaderItem(this);

   public RawDexFile(@Nonnull Opcodes opcodes, @Nonnull BaseDexBuffer buf) {
      super(opcodes, buf);
   }

   public RawDexFile(@Nonnull Opcodes opcodes, @Nonnull byte[] buf) {
      super(opcodes, buf);
   }

   @Nonnull
   public byte[] readByteRange(int start, int length) {
      return Arrays.copyOfRange(this.getBuf(), this.getBaseOffset() + start, this.getBaseOffset() + start + length);
   }

   public int getMapOffset() {
      return this.headerItem.getMapOffset();
   }

   @Nullable
   public MapItem getMapItemForSection(int itemType) {
      Iterator var2 = this.getMapItems().iterator();

      MapItem mapItem;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         mapItem = (MapItem)var2.next();
      } while(mapItem.getType() != itemType);

      return mapItem;
   }

   public List<MapItem> getMapItems() {
      final int mapOffset = this.getMapOffset();
      final int mapSize = this.readSmallUint(mapOffset);
      return new FixedSizeList<MapItem>() {
         public MapItem readItem(int index) {
            int mapItemOffset = mapOffset + 4 + index * 12;
            return new MapItem(RawDexFile.this, mapItemOffset);
         }

         public int size() {
            return mapSize;
         }
      };
   }

   public void writeAnnotations(@Nonnull Writer out, @Nonnull AnnotatedBytes annotatedBytes) throws IOException {
      annotatedBytes.writeAnnotations(out, this.getBuf());
   }
}
