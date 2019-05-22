package pxb.android.arsc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import pxb.android.ResConst;
import pxb.android.StringItems;

public class ArscParser implements ResConst {
   private static final boolean DEBUG = false;
   static final int ENGRY_FLAG_PUBLIC = 2;
   static final short ENTRY_FLAG_COMPLEX = 1;
   public static final int TYPE_STRING = 3;
   private int fileSize = -1;
   private ByteBuffer in;
   private String[] keyNamesX;
   private Pkg pkg;
   private List<Pkg> pkgs = new ArrayList();
   private String[] strings;
   private String[] typeNamesX;

   private static void D(String fmt, Object... args) {
   }

   public ArscParser(byte[] b) {
      this.in = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
   }

   public List<Pkg> parse() throws IOException {
      ArscParser.Chunk chunk;
      if (this.fileSize < 0) {
         chunk = new ArscParser.Chunk();
         if (chunk.type != 2) {
            throw new RuntimeException();
         }

         this.fileSize = chunk.size;
         this.in.getInt();
      }

      for(; this.in.hasRemaining(); this.in.position(chunk.location + chunk.size)) {
         chunk = new ArscParser.Chunk();
         switch(chunk.type) {
         case 1:
            this.strings = StringItems.read(this.in);
            break;
         case 512:
            this.readPackage(this.in);
         }
      }

      return this.pkgs;
   }

   private void readEntry(Config config, ResSpec spec) {
      D("[%08x]read ResTable_entry", this.in.position());
      int size = this.in.getShort();
      D("ResTable_entry %d", Integer.valueOf(size));
      int flags = this.in.getShort();
      int keyStr = this.in.getInt();
      spec.updateName(this.keyNamesX[keyStr]);
      ResEntry resEntry = new ResEntry(flags, spec);
      if (0 != (flags & 1)) {
         int parent = this.in.getInt();
         int count = this.in.getInt();
         BagValue bag = new BagValue(parent);

         for(int i = 0; i < count; ++i) {
            Entry<Integer, Value> entry = new SimpleEntry(this.in.getInt(), this.readValue());
            bag.map.add(entry);
         }

         resEntry.value = bag;
      } else {
         resEntry.value = this.readValue();
      }

      config.resources.put(spec.id, resEntry);
   }

   private void readPackage(ByteBuffer in) throws IOException {
      int pid = in.getInt() % 255;
      int typeStringOff = in.position() + 256;
      StringBuilder sb = new StringBuilder(32);

      int keyStringOff;
      for(keyStringOff = 0; keyStringOff < 128; ++keyStringOff) {
         int s = in.getShort();
         if (s == 0) {
            break;
         }

         sb.append((char)s);
      }

      String name = sb.toString();
      in.position(typeStringOff);
      this.pkg = new Pkg(pid, name);
      this.pkgs.add(this.pkg);
      typeStringOff = in.getInt();
      int typeNameCount = in.getInt();
      keyStringOff = in.getInt();
      int specNameCount = in.getInt();
      ArscParser.Chunk chunk = new ArscParser.Chunk();
      if (chunk.type != 1) {
         throw new RuntimeException();
      } else {
         this.typeNamesX = StringItems.read(in);
         in.position(chunk.location + chunk.size);
         chunk = new ArscParser.Chunk();
         if (chunk.type != 1) {
            throw new RuntimeException();
         } else {
            this.keyNamesX = StringItems.read(in);
            in.position(chunk.location + chunk.size);

            label62:
            for(; in.hasRemaining(); in.position(chunk.location + chunk.size)) {
               chunk = new ArscParser.Chunk();
               int tid;
               int entryCount;
               Type t;
               int entriesStart;
               switch(chunk.type) {
               case 513:
                  D("[%08x]read config", in.position() - 8);
                  tid = in.get() & 255;
                  in.get();
                  in.getShort();
                  entryCount = in.getInt();
                  t = this.pkg.getType(tid, this.typeNamesX[tid - 1], entryCount);
                  entriesStart = in.getInt();
                  D("[%08x]read config id", in.position());
                  int p = in.position();
                  int size = in.getInt();
                  byte[] data = new byte[size];
                  in.position(p);
                  in.get(data);
                  Config config = new Config(data, entryCount);
                  in.position(chunk.location + chunk.headSize);
                  D("[%08x]read config entry offset", in.position());
                  int[] entrys = new int[entryCount];

                  int i;
                  for(i = 0; i < entryCount; ++i) {
                     entrys[i] = in.getInt();
                  }

                  D("[%08x]read config entrys", in.position());

                  for(i = 0; i < entrys.length; ++i) {
                     if (entrys[i] != -1) {
                        in.position(chunk.location + entriesStart + entrys[i]);
                        ResSpec spec = t.getSpec(i);
                        this.readEntry(config, spec);
                     }
                  }

                  t.addConfig(config);
                  break;
               case 514:
                  D("[%08x]read spec", in.position() - 8);
                  tid = in.get() & 255;
                  in.get();
                  in.getShort();
                  entryCount = in.getInt();
                  t = this.pkg.getType(tid, this.typeNamesX[tid - 1], entryCount);
                  entriesStart = 0;

                  while(true) {
                     if (entriesStart >= entryCount) {
                        continue label62;
                     }

                     t.getSpec(entriesStart).flags = in.getInt();
                     ++entriesStart;
                  }
               default:
                  return;
               }
            }

         }
      }
   }

   private Object readValue() {
      int size1 = this.in.getShort();
      int zero = this.in.get();
      int type = this.in.get() & 255;
      int data = this.in.getInt();
      String raw = null;
      if (type == 3) {
         raw = this.strings[data];
      }

      return new Value(type, data, raw);
   }

   class Chunk {
      public final int headSize;
      public final int location;
      public final int size;
      public final int type;

      public Chunk() {
         this.location = ArscParser.this.in.position();
         this.type = ArscParser.this.in.getShort() & '\uffff';
         this.headSize = ArscParser.this.in.getShort() & '\uffff';
         this.size = ArscParser.this.in.getInt();
         ArscParser.D("[%08x]type: %04x, headsize: %04x, size:%08x", this.location, this.type, this.headSize, this.size);
      }
   }
}
