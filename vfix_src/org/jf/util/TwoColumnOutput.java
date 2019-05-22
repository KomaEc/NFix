package org.jf.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.annotation.Nonnull;

public final class TwoColumnOutput {
   private final Writer out;
   private final int leftWidth;
   private final int rightWidth;
   private final String spacer;
   private String[] leftLines;
   private String[] rightLines;

   public TwoColumnOutput(@Nonnull Writer out, int leftWidth, int rightWidth, @Nonnull String spacer) {
      this.leftLines = null;
      this.rightLines = null;
      if (leftWidth < 1) {
         throw new IllegalArgumentException("leftWidth < 1");
      } else if (rightWidth < 1) {
         throw new IllegalArgumentException("rightWidth < 1");
      } else {
         this.out = out;
         this.leftWidth = leftWidth;
         this.rightWidth = rightWidth;
         this.spacer = spacer;
      }
   }

   public TwoColumnOutput(OutputStream out, int leftWidth, int rightWidth, String spacer) {
      this((Writer)(new OutputStreamWriter(out)), leftWidth, rightWidth, spacer);
   }

   public void write(String left, String right) throws IOException {
      this.leftLines = StringWrapper.wrapString(left, this.leftWidth, this.leftLines);
      this.rightLines = StringWrapper.wrapString(right, this.rightWidth, this.rightLines);
      int leftCount = this.leftLines.length;
      int rightCount = this.rightLines.length;

      for(int i = 0; i < leftCount || i < rightCount; ++i) {
         String leftLine = null;
         String rightLine = null;
         if (i < leftCount) {
            leftLine = this.leftLines[i];
            if (leftLine == null) {
               leftCount = i;
            }
         }

         if (i < rightCount) {
            rightLine = this.rightLines[i];
            if (rightLine == null) {
               rightCount = i;
            }
         }

         if (leftLine != null || rightLine != null) {
            int written = 0;
            if (leftLine != null) {
               this.out.write(leftLine);
               written = leftLine.length();
            }

            int remaining = this.leftWidth - written;
            if (remaining > 0) {
               writeSpaces(this.out, remaining);
            }

            this.out.write(this.spacer);
            if (rightLine != null) {
               this.out.write(rightLine);
            }

            this.out.write(10);
         }
      }

   }

   private static void writeSpaces(Writer out, int amt) throws IOException {
      while(amt > 0) {
         out.write(32);
         --amt;
      }

   }
}
