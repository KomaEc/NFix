package org.jf.dexlib2.dexbacked.raw;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.AlignmentUtils;

public abstract class SectionAnnotator {
   @Nonnull
   public final DexAnnotator annotator;
   @Nonnull
   public final RawDexFile dexFile;
   public final int itemType;
   public final int sectionOffset;
   public final int itemCount;
   private Map<Integer, String> itemIdentities = Maps.newHashMap();

   public SectionAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      this.annotator = annotator;
      this.dexFile = annotator.dexFile;
      this.itemType = mapItem.getType();
      this.sectionOffset = mapItem.getOffset();
      this.itemCount = mapItem.getItemCount();
   }

   @Nonnull
   public abstract String getItemName();

   protected abstract void annotateItem(@Nonnull AnnotatedBytes var1, int var2, @Nullable String var3);

   public void annotateSection(@Nonnull AnnotatedBytes out) {
      out.moveTo(this.sectionOffset);
      this.annotateSectionInner(out, this.itemCount);
   }

   protected void annotateSectionInner(@Nonnull AnnotatedBytes out, int itemCount) {
      String itemName = this.getItemName();
      int itemAlignment = this.getItemAlignment();
      if (itemCount > 0) {
         out.annotate(0, "");
         out.annotate(0, "-----------------------------");
         out.annotate(0, "%s section", itemName);
         out.annotate(0, "-----------------------------");
         out.annotate(0, "");

         for(int i = 0; i < itemCount; ++i) {
            out.moveTo(AlignmentUtils.alignOffset(out.getCursor(), itemAlignment));
            String itemIdentity = this.getItemIdentity(out.getCursor());
            if (itemIdentity != null) {
               out.annotate(0, "[%d] %s: %s", i, itemName, itemIdentity);
            } else {
               out.annotate(0, "[%d] %s", i, itemName);
            }

            out.indent();
            this.annotateItem(out, i, itemIdentity);
            out.deindent();
         }
      }

   }

   @Nullable
   private String getItemIdentity(int itemOffset) {
      return (String)this.itemIdentities.get(itemOffset);
   }

   public void setItemIdentity(int itemOffset, String identity) {
      this.itemIdentities.put(itemOffset, identity);
   }

   public int getItemAlignment() {
      return 1;
   }
}
