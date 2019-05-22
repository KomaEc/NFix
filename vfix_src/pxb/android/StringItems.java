package pxb.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringItems extends ArrayList<StringItem> {
   private static final int UTF8_FLAG = 256;
   byte[] stringData;
   private boolean useUTF8 = true;

   public static String[] read(ByteBuffer in) throws IOException {
      int trunkOffset = in.position() - 8;
      int stringCount = in.getInt();
      int styleOffsetCount = in.getInt();
      int flags = in.getInt();
      int stringDataOffset = in.getInt();
      int stylesOffset = in.getInt();
      int[] offsets = new int[stringCount];
      String[] strings = new String[stringCount];

      int base;
      for(base = 0; base < stringCount; ++base) {
         offsets[base] = in.getInt();
      }

      base = trunkOffset + stringDataOffset;

      for(int i = 0; i < offsets.length; ++i) {
         in.position(base + offsets[i]);
         String s;
         int u8len;
         if (0 == (flags & 256)) {
            u8len = u16length(in);
            s = new String(in.array(), in.position(), u8len * 2, "UTF-16LE");
         } else {
            u8length(in);
            u8len = u8length(in);
            int start = in.position();

            int blength;
            for(blength = u8len; in.get(start + blength) != 0; ++blength) {
            }

            s = new String(in.array(), start, blength, "UTF-8");
         }

         strings[i] = s;
      }

      return strings;
   }

   static int u16length(ByteBuffer in) {
      int length = in.getShort() & '\uffff';
      if (length > 32767) {
         length = (length & 32767) << 8 | in.getShort() & '\uffff';
      }

      return length;
   }

   static int u8length(ByteBuffer in) {
      int len = in.get() & 255;
      if ((len & 128) != 0) {
         len = (len & 127) << 8 | in.get() & 255;
      }

      return len;
   }

   public int getSize() {
      return 20 + this.size() * 4 + this.stringData.length + 0;
   }

   public void prepare() throws IOException {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         StringItem s = (StringItem)var1.next();
         if (s.data.length() > 32767) {
            this.useUTF8 = false;
         }
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int i = 0;
      int offset = 0;
      baos.reset();
      Map<String, Integer> map = new HashMap();
      Iterator var5 = this.iterator();

      while(var5.hasNext()) {
         StringItem item = (StringItem)var5.next();
         item.index = i++;
         String stringData = item.data;
         Integer of = (Integer)map.get(stringData);
         if (of != null) {
            item.dataOffset = of;
         } else {
            item.dataOffset = offset;
            map.put(stringData, offset);
            int length;
            byte[] data;
            int u8lenght;
            if (this.useUTF8) {
               length = stringData.length();
               data = stringData.getBytes("UTF-8");
               u8lenght = data.length;
               if (length > 127) {
                  ++offset;
                  baos.write(length >> 8 | 128);
               }

               baos.write(length);
               if (u8lenght > 127) {
                  ++offset;
                  baos.write(u8lenght >> 8 | 128);
               }

               baos.write(u8lenght);
               baos.write(data);
               baos.write(0);
               offset += 3 + u8lenght;
            } else {
               length = stringData.length();
               data = stringData.getBytes("UTF-16LE");
               if (length > 32767) {
                  u8lenght = length >> 16 | '耀';
                  baos.write(u8lenght);
                  baos.write(u8lenght >> 8);
                  offset += 2;
               }

               baos.write(length);
               baos.write(length >> 8);
               baos.write(data);
               baos.write(0);
               baos.write(0);
               offset += 4 + data.length;
            }
         }
      }

      this.stringData = baos.toByteArray();
   }

   public void write(ByteBuffer out) throws IOException {
      out.putInt(this.size());
      out.putInt(0);
      out.putInt(this.useUTF8 ? 256 : 0);
      out.putInt(28 + this.size() * 4);
      out.putInt(0);
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         StringItem item = (StringItem)var2.next();
         out.putInt(item.dataOffset);
      }

      out.put(this.stringData);
   }
}
