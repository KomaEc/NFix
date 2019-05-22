package pxb.android.axml;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import pxb.android.ResConst;
import pxb.android.StringItems;

public class AxmlParser implements ResConst {
   public static final int END_FILE = 7;
   public static final int END_NS = 5;
   public static final int END_TAG = 3;
   public static final int START_FILE = 1;
   public static final int START_NS = 4;
   public static final int START_TAG = 2;
   public static final int TEXT = 6;
   private int attributeCount;
   private IntBuffer attrs;
   private int classAttribute;
   private int fileSize;
   private int idAttribute;
   private ByteBuffer in;
   private int lineNumber;
   private int nameIdx;
   private int nsIdx;
   private int prefixIdx;
   private int[] resourceIds;
   private String[] strings;
   private int styleAttribute;
   private int textIdx;

   public AxmlParser(byte[] data) {
      this(ByteBuffer.wrap(data));
   }

   public AxmlParser(ByteBuffer in) {
      this.fileSize = -1;
      this.in = in.order(ByteOrder.LITTLE_ENDIAN);
   }

   public int getAttrCount() {
      return this.attributeCount;
   }

   public int getAttributeCount() {
      return this.attributeCount;
   }

   public String getAttrName(int i) {
      int idx = this.attrs.get(i * 5 + 1);
      return this.strings[idx];
   }

   public String getAttrNs(int i) {
      int idx = this.attrs.get(i * 5 + 0);
      return idx >= 0 ? this.strings[idx] : null;
   }

   String getAttrRawString(int i) {
      int idx = this.attrs.get(i * 5 + 2);
      return idx >= 0 ? this.strings[idx] : null;
   }

   public int getAttrResId(int i) {
      if (this.resourceIds != null) {
         int idx = this.attrs.get(i * 5 + 1);
         if (idx >= 0 && idx < this.resourceIds.length) {
            return this.resourceIds[idx];
         }
      }

      return -1;
   }

   public int getAttrType(int i) {
      return this.attrs.get(i * 5 + 3) >> 24;
   }

   public Object getAttrValue(int i) {
      int v = this.attrs.get(i * 5 + 4);
      if (i == this.idAttribute) {
         return ValueWrapper.wrapId(v, this.getAttrRawString(i));
      } else if (i == this.styleAttribute) {
         return ValueWrapper.wrapStyle(v, this.getAttrRawString(i));
      } else if (i == this.classAttribute) {
         return ValueWrapper.wrapClass(v, this.getAttrRawString(i));
      } else {
         switch(this.getAttrType(i)) {
         case 3:
            return this.strings[v];
         case 18:
            return v != 0;
         default:
            return v;
         }
      }
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public String getName() {
      return this.strings[this.nameIdx];
   }

   public String getNamespacePrefix() {
      return this.strings[this.prefixIdx];
   }

   public String getNamespaceUri() {
      return this.nsIdx >= 0 ? this.strings[this.nsIdx] : null;
   }

   public String getText() {
      return this.strings[this.textIdx];
   }

   public int next() throws IOException {
      if (this.fileSize < 0) {
         int type = this.in.getInt() & '\uffff';
         if (type != 3) {
            throw new RuntimeException();
         } else {
            this.fileSize = this.in.getInt();
            return 1;
         }
      } else {
         int event = true;
         int p = this.in.position();

         while(p < this.fileSize) {
            int size;
            byte event;
            label47: {
               int type = this.in.getInt() & '\uffff';
               size = this.in.getInt();
               int count;
               switch(type) {
               case 1:
                  this.strings = StringItems.read(this.in);
                  this.in.position(p + size);
                  break;
               case 256:
                  this.lineNumber = this.in.getInt();
                  this.in.getInt();
                  this.prefixIdx = this.in.getInt();
                  this.nsIdx = this.in.getInt();
                  event = 4;
                  break label47;
               case 257:
                  this.in.position(p + size);
                  event = 5;
                  break label47;
               case 258:
                  this.lineNumber = this.in.getInt();
                  this.in.getInt();
                  this.nsIdx = this.in.getInt();
                  this.nameIdx = this.in.getInt();
                  count = this.in.getInt();
                  if (count != 1310740) {
                     throw new RuntimeException();
                  }

                  this.attributeCount = this.in.getShort() & '\uffff';
                  this.idAttribute = (this.in.getShort() & '\uffff') - 1;
                  this.classAttribute = (this.in.getShort() & '\uffff') - 1;
                  this.styleAttribute = (this.in.getShort() & '\uffff') - 1;
                  this.attrs = this.in.asIntBuffer();
                  event = 2;
                  break label47;
               case 259:
                  this.in.position(p + size);
                  event = 3;
                  break label47;
               case 260:
                  this.lineNumber = this.in.getInt();
                  this.in.getInt();
                  this.textIdx = this.in.getInt();
                  this.in.getInt();
                  this.in.getInt();
                  event = 6;
                  break label47;
               case 384:
                  count = size / 4 - 2;
                  this.resourceIds = new int[count];

                  for(int i = 0; i < count; ++i) {
                     this.resourceIds[i] = this.in.getInt();
                  }

                  this.in.position(p + size);
                  break;
               default:
                  throw new RuntimeException();
               }

               p = this.in.position();
               continue;
            }

            this.in.position(p + size);
            return event;
         }

         return 7;
      }
   }
}
