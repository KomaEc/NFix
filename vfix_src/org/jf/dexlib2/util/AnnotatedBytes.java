package org.jf.dexlib2.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.util.ExceptionWithContext;
import org.jf.util.Hex;
import org.jf.util.TwoColumnOutput;

public class AnnotatedBytes {
   @Nonnull
   private TreeMap<Integer, AnnotatedBytes.AnnotationEndpoint> annotatations = Maps.newTreeMap();
   private int cursor;
   private int indentLevel;
   private int outputWidth;
   private int hexCols = 8;
   private int startLimit = -1;
   private int endLimit = -1;

   public AnnotatedBytes(int width) {
      this.outputWidth = width;
   }

   public void moveTo(int offset) {
      this.cursor = offset;
   }

   public void moveBy(int offset) {
      this.cursor += offset;
   }

   public void annotateTo(int offset, @Nonnull String msg, Object... formatArgs) {
      this.annotate(offset - this.cursor, msg, formatArgs);
   }

   public void annotate(int length, @Nonnull String msg, Object... formatArgs) {
      if (this.startLimit == -1 || this.endLimit == -1 || this.cursor >= this.startLimit && this.cursor < this.endLimit) {
         String formattedMsg;
         if (formatArgs != null && formatArgs.length > 0) {
            formattedMsg = String.format(msg, formatArgs);
         } else {
            formattedMsg = msg;
         }

         int exclusiveEndOffset = this.cursor + length;
         AnnotatedBytes.AnnotationEndpoint endPoint = null;
         AnnotatedBytes.AnnotationEndpoint startPoint = (AnnotatedBytes.AnnotationEndpoint)this.annotatations.get(this.cursor);
         Entry nextEntry;
         if (startPoint == null) {
            nextEntry = this.annotatations.lowerEntry(this.cursor);
            if (nextEntry != null) {
               AnnotatedBytes.AnnotationEndpoint previousAnnotations = (AnnotatedBytes.AnnotationEndpoint)nextEntry.getValue();
               AnnotatedBytes.AnnotationItem previousRangeAnnotation = previousAnnotations.rangeAnnotation;
               if (previousRangeAnnotation != null) {
                  throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", new Object[]{this.formatAnnotation(this.cursor, this.cursor + length, formattedMsg), this.formatAnnotation((Integer)nextEntry.getKey(), previousRangeAnnotation.annotation)});
               }
            }
         } else if (length > 0) {
            AnnotatedBytes.AnnotationItem existingRangeAnnotation = startPoint.rangeAnnotation;
            if (existingRangeAnnotation != null) {
               throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", new Object[]{this.formatAnnotation(this.cursor, this.cursor + length, formattedMsg), this.formatAnnotation(this.cursor, existingRangeAnnotation.annotation)});
            }
         }

         if (length > 0) {
            nextEntry = this.annotatations.higherEntry(this.cursor);
            if (nextEntry != null) {
               int nextKey = (Integer)nextEntry.getKey();
               if (nextKey < exclusiveEndOffset) {
                  AnnotatedBytes.AnnotationEndpoint nextEndpoint = (AnnotatedBytes.AnnotationEndpoint)nextEntry.getValue();
                  AnnotatedBytes.AnnotationItem nextRangeAnnotation = nextEndpoint.rangeAnnotation;
                  if (nextRangeAnnotation != null) {
                     throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", new Object[]{this.formatAnnotation(this.cursor, this.cursor + length, formattedMsg), this.formatAnnotation(nextKey, nextRangeAnnotation.annotation)});
                  }

                  if (nextEndpoint.pointAnnotations.size() > 0) {
                     throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", new Object[]{this.formatAnnotation(this.cursor, this.cursor + length, formattedMsg), this.formatAnnotation(nextKey, nextKey, ((AnnotatedBytes.AnnotationItem)nextEndpoint.pointAnnotations.get(0)).annotation)});
                  }

                  throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation endpoint at %d", new Object[]{this.formatAnnotation(this.cursor, this.cursor + length, formattedMsg), nextKey});
               }

               if (nextKey == exclusiveEndOffset) {
                  endPoint = (AnnotatedBytes.AnnotationEndpoint)nextEntry.getValue();
               }
            }
         }

         if (startPoint == null) {
            startPoint = new AnnotatedBytes.AnnotationEndpoint();
            this.annotatations.put(this.cursor, startPoint);
         }

         if (length == 0) {
            startPoint.pointAnnotations.add(new AnnotatedBytes.AnnotationItem(this.indentLevel, formattedMsg));
         } else {
            startPoint.rangeAnnotation = new AnnotatedBytes.AnnotationItem(this.indentLevel, formattedMsg);
            if (endPoint == null) {
               endPoint = new AnnotatedBytes.AnnotationEndpoint();
               this.annotatations.put(exclusiveEndOffset, endPoint);
            }
         }

         this.cursor += length;
      } else {
         throw new ExceptionWithContext("Annotating outside the parent bounds", new Object[0]);
      }
   }

   private String formatAnnotation(int offset, String annotationMsg) {
      Integer endOffset = (Integer)this.annotatations.higherKey(offset);
      return this.formatAnnotation(offset, endOffset, annotationMsg);
   }

   private String formatAnnotation(int offset, Integer endOffset, String annotationMsg) {
      return endOffset != null ? String.format("[0x%x, 0x%x) \"%s\"", offset, endOffset, annotationMsg) : String.format("[0x%x, ) \"%s\"", offset, annotationMsg);
   }

   public void indent() {
      ++this.indentLevel;
   }

   public void deindent() {
      --this.indentLevel;
      if (this.indentLevel < 0) {
         this.indentLevel = 0;
      }

   }

   public int getCursor() {
      return this.cursor;
   }

   public int getAnnotationWidth() {
      int leftWidth = 8 + this.hexCols * 2 + this.hexCols / 2;
      return this.outputWidth - leftWidth;
   }

   public void writeAnnotations(Writer out, byte[] data) throws IOException {
      int rightWidth = this.getAnnotationWidth();
      int leftWidth = this.outputWidth - rightWidth - 1;
      String padding = Strings.repeat(" ", 1000);
      TwoColumnOutput twoc = new TwoColumnOutput(out, leftWidth, rightWidth, "|");
      Integer[] keys = new Integer[this.annotatations.size()];
      keys = (Integer[])this.annotatations.keySet().toArray(keys);
      AnnotatedBytes.AnnotationEndpoint[] values = new AnnotatedBytes.AnnotationEndpoint[this.annotatations.size()];
      values = (AnnotatedBytes.AnnotationEndpoint[])this.annotatations.values().toArray(values);

      int lastKey;
      for(lastKey = 0; lastKey < keys.length - 1; ++lastKey) {
         int rangeStart = keys[lastKey];
         int rangeEnd = keys[lastKey + 1];
         AnnotatedBytes.AnnotationEndpoint annotations = values[lastKey];
         Iterator var13 = annotations.pointAnnotations.iterator();

         AnnotatedBytes.AnnotationItem rangeAnnotation;
         String left;
         while(var13.hasNext()) {
            rangeAnnotation = (AnnotatedBytes.AnnotationItem)var13.next();
            left = padding.substring(0, rangeAnnotation.indentLevel * 2);
            twoc.write("", left + rangeAnnotation.annotation);
         }

         rangeAnnotation = annotations.rangeAnnotation;
         String right;
         if (rangeAnnotation != null) {
            right = padding.substring(0, rangeAnnotation.indentLevel * 2);
            right = right + rangeAnnotation.annotation;
         } else {
            right = "";
         }

         left = Hex.dump(data, rangeStart, rangeEnd - rangeStart, rangeStart, this.hexCols, 6);
         twoc.write(left, right);
      }

      lastKey = keys[keys.length - 1];
      if (lastKey < data.length) {
         String left = Hex.dump(data, lastKey, data.length - lastKey, lastKey, this.hexCols, 6);
         twoc.write(left, "");
      }

   }

   public void setLimit(int start, int end) {
      this.startLimit = start;
      this.endLimit = end;
   }

   public void clearLimit() {
      this.startLimit = -1;
      this.endLimit = -1;
   }

   private static class AnnotationItem {
      public final int indentLevel;
      public final String annotation;

      public AnnotationItem(int indentLevel, String annotation) {
         this.indentLevel = indentLevel;
         this.annotation = annotation;
      }
   }

   private static class AnnotationEndpoint {
      @Nonnull
      public final List<AnnotatedBytes.AnnotationItem> pointAnnotations;
      @Nullable
      public AnnotatedBytes.AnnotationItem rangeAnnotation;

      private AnnotationEndpoint() {
         this.pointAnnotations = Lists.newArrayList();
         this.rangeAnnotation = null;
      }

      // $FF: synthetic method
      AnnotationEndpoint(Object x0) {
         this();
      }
   }
}
