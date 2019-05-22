package com.gzoltar.shaded.org.jacoco.core.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContentTypeDetector {
   public static final int UNKNOWN = -1;
   public static final int CLASSFILE = -889275714;
   public static final int ZIPFILE = 1347093252;
   public static final int GZFILE = 529203200;
   public static final int PACK200FILE = -889270259;
   private static final int BUFFER_SIZE = 8;
   private final InputStream in;
   private final int type;

   public ContentTypeDetector(InputStream in) throws IOException {
      if (in.markSupported()) {
         this.in = in;
      } else {
         this.in = new BufferedInputStream(in, 8);
      }

      this.in.mark(8);
      this.type = determineType(this.in);
      this.in.reset();
   }

   private static int determineType(InputStream in) throws IOException {
      int header = readInt(in);
      switch(header) {
      case -889275714:
         switch(readInt(in)) {
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 196653:
            return -889275714;
         }
      default:
         if ((header & -65536) == 529203200) {
            return 529203200;
         }

         return -1;
      case -889270259:
         return -889270259;
      case 1347093252:
         return 1347093252;
      }
   }

   private static int readInt(InputStream in) throws IOException {
      return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read();
   }

   public InputStream getInputStream() {
      return this.in;
   }

   public int getType() {
      return this.type;
   }
}
