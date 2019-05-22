package org.jf.dexlib2.dexbacked.raw.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.raw.AnnotationDirectoryItem;
import org.jf.dexlib2.dexbacked.raw.AnnotationSetItem;
import org.jf.dexlib2.dexbacked.raw.AnnotationSetRefList;
import org.jf.dexlib2.dexbacked.raw.ClassDataItem;
import org.jf.dexlib2.dexbacked.raw.ClassDefItem;
import org.jf.dexlib2.dexbacked.raw.CodeItem;
import org.jf.dexlib2.dexbacked.raw.DebugInfoItem;
import org.jf.dexlib2.dexbacked.raw.EncodedArrayItem;
import org.jf.dexlib2.dexbacked.raw.FieldIdItem;
import org.jf.dexlib2.dexbacked.raw.HeaderItem;
import org.jf.dexlib2.dexbacked.raw.MapItem;
import org.jf.dexlib2.dexbacked.raw.MethodIdItem;
import org.jf.dexlib2.dexbacked.raw.ProtoIdItem;
import org.jf.dexlib2.dexbacked.raw.RawDexFile;
import org.jf.dexlib2.dexbacked.raw.SectionAnnotator;
import org.jf.dexlib2.dexbacked.raw.StringDataItem;
import org.jf.dexlib2.dexbacked.raw.StringIdItem;
import org.jf.dexlib2.dexbacked.raw.TypeIdItem;
import org.jf.dexlib2.dexbacked.raw.TypeListItem;
import org.jf.dexlib2.util.AnnotatedBytes;

public class DexAnnotator extends AnnotatedBytes {
   @Nonnull
   public final RawDexFile dexFile;
   private final Map<Integer, SectionAnnotator> annotators = Maps.newHashMap();
   private static final Map<Integer, Integer> sectionAnnotationOrder = Maps.newHashMap();

   public DexAnnotator(@Nonnull RawDexFile dexFile, int width) {
      super(width);
      this.dexFile = dexFile;
      Iterator var3 = dexFile.getMapItems().iterator();

      while(var3.hasNext()) {
         MapItem mapItem = (MapItem)var3.next();
         switch(mapItem.getType()) {
         case 0:
            this.annotators.put(mapItem.getType(), HeaderItem.makeAnnotator(this, mapItem));
            break;
         case 1:
            this.annotators.put(mapItem.getType(), StringIdItem.makeAnnotator(this, mapItem));
            break;
         case 2:
            this.annotators.put(mapItem.getType(), TypeIdItem.makeAnnotator(this, mapItem));
            break;
         case 3:
            this.annotators.put(mapItem.getType(), ProtoIdItem.makeAnnotator(this, mapItem));
            break;
         case 4:
            this.annotators.put(mapItem.getType(), FieldIdItem.makeAnnotator(this, mapItem));
            break;
         case 5:
            this.annotators.put(mapItem.getType(), MethodIdItem.makeAnnotator(this, mapItem));
            break;
         case 6:
            this.annotators.put(mapItem.getType(), ClassDefItem.makeAnnotator(this, mapItem));
            break;
         case 4096:
            this.annotators.put(mapItem.getType(), MapItem.makeAnnotator(this, mapItem));
            break;
         case 4097:
            this.annotators.put(mapItem.getType(), TypeListItem.makeAnnotator(this, mapItem));
            break;
         case 4098:
            this.annotators.put(mapItem.getType(), AnnotationSetRefList.makeAnnotator(this, mapItem));
            break;
         case 4099:
            this.annotators.put(mapItem.getType(), AnnotationSetItem.makeAnnotator(this, mapItem));
            break;
         case 8192:
            this.annotators.put(mapItem.getType(), ClassDataItem.makeAnnotator(this, mapItem));
            break;
         case 8193:
            this.annotators.put(mapItem.getType(), CodeItem.makeAnnotator(this, mapItem));
            break;
         case 8194:
            this.annotators.put(mapItem.getType(), StringDataItem.makeAnnotator(this, mapItem));
            break;
         case 8195:
            this.annotators.put(mapItem.getType(), DebugInfoItem.makeAnnotator(this, mapItem));
            break;
         case 8196:
            this.annotators.put(mapItem.getType(), org.jf.dexlib2.dexbacked.raw.AnnotationItem.makeAnnotator(this, mapItem));
            break;
         case 8197:
            this.annotators.put(mapItem.getType(), EncodedArrayItem.makeAnnotator(this, mapItem));
            break;
         case 8198:
            this.annotators.put(mapItem.getType(), AnnotationDirectoryItem.makeAnnotator(this, mapItem));
            break;
         default:
            throw new RuntimeException(String.format("Unrecognized item type: 0x%x", mapItem.getType()));
         }
      }

   }

   public void writeAnnotations(Writer out) throws IOException {
      List<MapItem> mapItems = this.dexFile.getMapItems();
      Ordering<MapItem> ordering = Ordering.from(new Comparator<MapItem>() {
         public int compare(MapItem o1, MapItem o2) {
            return Ints.compare((Integer)DexAnnotator.sectionAnnotationOrder.get(o1.getType()), (Integer)DexAnnotator.sectionAnnotationOrder.get(o2.getType()));
         }
      });
      ImmutableList mapItems = ordering.immutableSortedCopy(mapItems);

      try {
         Iterator var4 = mapItems.iterator();

         while(var4.hasNext()) {
            MapItem mapItem = (MapItem)var4.next();
            SectionAnnotator annotator = (SectionAnnotator)this.annotators.get(mapItem.getType());
            annotator.annotateSection(this);
         }
      } finally {
         this.dexFile.writeAnnotations(out, this);
      }

   }

   @Nullable
   public SectionAnnotator getAnnotator(int itemType) {
      return (SectionAnnotator)this.annotators.get(itemType);
   }

   static {
      int[] sectionOrder = new int[]{4096, 0, 1, 2, 3, 4, 5, 6, 8192, 8193, 8195, 4097, 4098, 4099, 8194, 8196, 8197, 8198};

      for(int i = 0; i < sectionOrder.length; ++i) {
         sectionAnnotationOrder.put(sectionOrder[i], i);
      }

   }
}
