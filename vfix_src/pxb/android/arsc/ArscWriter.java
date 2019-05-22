package pxb.android.arsc;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import pxb.android.ResConst;
import pxb.android.StringItem;
import pxb.android.StringItems;
import pxb.android.axml.Util;

public class ArscWriter implements ResConst {
   private List<ArscWriter.PkgCtx> ctxs = new ArrayList(5);
   private List<Pkg> pkgs;
   private Map<String, StringItem> strTable = new TreeMap();
   private StringItems strTable0 = new StringItems();

   private static void D(String fmt, Object... args) {
   }

   public ArscWriter(List<Pkg> pkgs) {
      this.pkgs = pkgs;
   }

   public static void main(String... args) throws IOException {
      if (args.length < 2) {
         System.err.println("asrc-write-test in.arsc out.arsc");
      } else {
         byte[] data = Util.readFile(new File(args[0]));
         List<Pkg> pkgs = (new ArscParser(data)).parse();
         byte[] data2 = (new ArscWriter(pkgs)).toByteArray();
         Util.writeFile(data2, new File(args[1]));
      }
   }

   private void addString(String str) {
      if (!this.strTable.containsKey(str)) {
         StringItem stringItem = new StringItem(str);
         this.strTable.put(str, stringItem);
         this.strTable0.add(stringItem);
      }
   }

   private int count() {
      int size = 0;
      int size = size + 12;
      int stringSize = this.strTable0.getSize();
      if (stringSize % 4 != 0) {
         stringSize += 4 - stringSize % 4;
      }

      size += 8 + stringSize;

      int pkgSize;
      for(Iterator var16 = this.ctxs.iterator(); var16.hasNext(); size += pkgSize) {
         ArscWriter.PkgCtx ctx = (ArscWriter.PkgCtx)var16.next();
         ctx.offset = size;
         int pkgSize = 0;
         pkgSize = pkgSize + 268;
         pkgSize += 16;
         ctx.typeStringOff = pkgSize;
         int stringSize = ctx.typeNames0.getSize();
         if (stringSize % 4 != 0) {
            stringSize += 4 - stringSize % 4;
         }

         pkgSize += 8 + stringSize;
         ctx.keyStringOff = pkgSize;
         stringSize = ctx.keyNames0.getSize();
         if (stringSize % 4 != 0) {
            stringSize += 4 - stringSize % 4;
         }

         pkgSize += 8 + stringSize;
         Iterator var18 = ctx.pkg.types.values().iterator();

         while(var18.hasNext()) {
            Type type = (Type)var18.next();
            type.wPosition = size + pkgSize;
            pkgSize += 16 + 4 * type.specs.length;

            Config config;
            int configBasePostion;
            for(Iterator var7 = type.configs.iterator(); var7.hasNext(); config.wChunkSize = pkgSize - configBasePostion) {
               config = (Config)var7.next();
               config.wPosition = pkgSize + size;
               configBasePostion = pkgSize;
               pkgSize += 20;
               int size0 = config.id.length;
               if (size0 % 4 != 0) {
                  size0 += 4 - size0 % 4;
               }

               pkgSize += size0;
               if (pkgSize - configBasePostion > 56) {
                  throw new RuntimeException("config id  too big");
               }

               pkgSize = configBasePostion + 56;
               pkgSize += 4 * config.entryCount;
               config.wEntryStart = pkgSize - configBasePostion;
               int entryBase = pkgSize;
               Iterator var12 = config.resources.values().iterator();

               while(var12.hasNext()) {
                  ResEntry e = (ResEntry)var12.next();
                  e.wOffset = pkgSize - entryBase;
                  pkgSize += 8;
                  if (e.value instanceof BagValue) {
                     BagValue big = (BagValue)e.value;
                     pkgSize += 8 + big.map.size() * 12;
                  } else {
                     pkgSize += 8;
                  }
               }
            }
         }

         ctx.pkgSize = pkgSize;
      }

      return size;
   }

   private List<ArscWriter.PkgCtx> prepare() throws IOException {
      Iterator var1 = this.pkgs.iterator();

      while(var1.hasNext()) {
         Pkg pkg = (Pkg)var1.next();
         ArscWriter.PkgCtx ctx = new ArscWriter.PkgCtx();
         ctx.pkg = pkg;
         this.ctxs.add(ctx);
         Iterator var4 = pkg.types.values().iterator();

         while(var4.hasNext()) {
            Type type = (Type)var4.next();
            ctx.addTypeName(type.id - 1, type.name);
            ResSpec[] var6 = type.specs;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               ResSpec spec = var6[var8];
               ctx.addKeyName(spec.name);
            }

            Iterator var11 = type.configs.iterator();

            while(var11.hasNext()) {
               Config config = (Config)var11.next();
               Iterator var13 = config.resources.values().iterator();

               while(var13.hasNext()) {
                  ResEntry e = (ResEntry)var13.next();
                  Object object = e.value;
                  if (object instanceof BagValue) {
                     this.travelBagValue((BagValue)object);
                  } else {
                     this.travelValue((Value)object);
                  }
               }
            }
         }

         ctx.keyNames0.prepare();
         ctx.typeNames0.addAll(ctx.typeNames);
         ctx.typeNames0.prepare();
      }

      this.strTable0.prepare();
      return this.ctxs;
   }

   public byte[] toByteArray() throws IOException {
      this.prepare();
      int size = this.count();
      ByteBuffer out = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
      this.write(out, size);
      return out.array();
   }

   private void travelBagValue(BagValue bag) {
      Iterator var2 = bag.map.iterator();

      while(var2.hasNext()) {
         Entry<Integer, Value> e = (Entry)var2.next();
         this.travelValue((Value)e.getValue());
      }

   }

   private void travelValue(Value v) {
      if (v.raw != null) {
         this.addString(v.raw);
      }

   }

   private void write(ByteBuffer out, int size) throws IOException {
      out.putInt(786434);
      out.putInt(size);
      out.putInt(this.ctxs.size());
      int stringSize = this.strTable0.getSize();
      int padding = 0;
      if (stringSize % 4 != 0) {
         padding = 4 - stringSize % 4;
      }

      out.putInt(1835009);
      out.putInt(stringSize + padding + 8);
      this.strTable0.write(out);
      out.put(new byte[padding]);
      Iterator var21 = this.ctxs.iterator();

      while(var21.hasNext()) {
         ArscWriter.PkgCtx pctx = (ArscWriter.PkgCtx)var21.next();
         if (out.position() != pctx.offset) {
            throw new RuntimeException();
         }

         int basePosition = out.position();
         out.putInt(18612736);
         out.putInt(pctx.pkgSize);
         out.putInt(pctx.pkg.id);
         int p = out.position();
         out.put(pctx.pkg.name.getBytes("UTF-16LE"));
         out.position(p + 256);
         out.putInt(pctx.typeStringOff);
         out.putInt(pctx.typeNames0.size());
         out.putInt(pctx.keyStringOff);
         out.putInt(pctx.keyNames0.size());
         if (out.position() - basePosition != pctx.typeStringOff) {
            throw new RuntimeException();
         }

         int stringSize = pctx.typeNames0.getSize();
         int padding = 0;
         if (stringSize % 4 != 0) {
            padding = 4 - stringSize % 4;
         }

         out.putInt(1835009);
         out.putInt(stringSize + padding + 8);
         pctx.typeNames0.write(out);
         out.put(new byte[padding]);
         if (out.position() - basePosition != pctx.keyStringOff) {
            throw new RuntimeException();
         }

         stringSize = pctx.keyNames0.getSize();
         padding = 0;
         if (stringSize % 4 != 0) {
            padding = 4 - stringSize % 4;
         }

         out.putInt(1835009);
         out.putInt(stringSize + padding + 8);
         pctx.keyNames0.write(out);
         out.put(new byte[padding]);
         Iterator var23 = pctx.pkg.types.values().iterator();

         while(var23.hasNext()) {
            Type t = (Type)var23.next();
            D("[%08x]write spec", out.position(), t.name);
            if (t.wPosition != out.position()) {
               throw new RuntimeException();
            }

            out.putInt(1049090);
            out.putInt(16 + 4 * t.specs.length);
            out.putInt(t.id);
            out.putInt(t.specs.length);
            ResSpec[] var9 = t.specs;
            int var10 = var9.length;

            int typeConfigPosition;
            for(typeConfigPosition = 0; typeConfigPosition < var10; ++typeConfigPosition) {
               ResSpec spec = var9[typeConfigPosition];
               out.putInt(spec.flags);
            }

            Iterator var24 = t.configs.iterator();

            label119:
            while(var24.hasNext()) {
               Config config = (Config)var24.next();
               D("[%08x]write config", out.position());
               typeConfigPosition = out.position();
               if (config.wPosition != typeConfigPosition) {
                  throw new RuntimeException();
               }

               out.putInt(3670529);
               out.putInt(config.wChunkSize);
               out.putInt(t.id);
               out.putInt(t.specs.length);
               out.putInt(config.wEntryStart);
               D("[%08x]write config ids", out.position());
               out.put(config.id);
               int size0 = config.id.length;
               int padding = 0;
               if (size0 % 4 != 0) {
                  padding = 4 - size0 % 4;
               }

               out.put(new byte[padding]);
               out.position(typeConfigPosition + 56);
               D("[%08x]write config entry offsets", out.position());

               ResEntry e;
               for(int i = 0; i < config.entryCount; ++i) {
                  e = (ResEntry)config.resources.get(i);
                  if (e == null) {
                     out.putInt(-1);
                  } else {
                     out.putInt(e.wOffset);
                  }
               }

               if (out.position() - typeConfigPosition != config.wEntryStart) {
                  throw new RuntimeException();
               }

               D("[%08x]write config entrys", out.position());
               Iterator var28 = config.resources.values().iterator();

               while(true) {
                  while(true) {
                     if (!var28.hasNext()) {
                        continue label119;
                     }

                     e = (ResEntry)var28.next();
                     D("[%08x]ResTable_entry", out.position());
                     boolean isBag = e.value instanceof BagValue;
                     out.putShort((short)(isBag ? 16 : 8));
                     int flag = e.flag;
                     if (isBag) {
                        flag |= 1;
                     } else {
                        flag &= -2;
                     }

                     out.putShort((short)flag);
                     out.putInt(((StringItem)pctx.keyNames.get(e.spec.name)).index);
                     if (isBag) {
                        BagValue bag = (BagValue)e.value;
                        out.putInt(bag.parent);
                        out.putInt(bag.map.size());
                        Iterator var19 = bag.map.iterator();

                        while(var19.hasNext()) {
                           Entry<Integer, Value> entry = (Entry)var19.next();
                           out.putInt((Integer)entry.getKey());
                           this.writeValue((Value)entry.getValue(), out);
                        }
                     } else {
                        this.writeValue((Value)e.value, out);
                     }
                  }
               }
            }
         }
      }

   }

   private void writeValue(Value value, ByteBuffer out) {
      out.putShort((short)8);
      out.put((byte)0);
      out.put((byte)value.type);
      if (value.type == 3) {
         out.putInt(((StringItem)this.strTable.get(value.raw)).index);
      } else {
         out.putInt(value.data);
      }

   }

   private static class PkgCtx {
      Map<String, StringItem> keyNames;
      StringItems keyNames0;
      public int keyStringOff;
      int offset;
      Pkg pkg;
      int pkgSize;
      List<StringItem> typeNames;
      StringItems typeNames0;
      int typeStringOff;

      private PkgCtx() {
         this.keyNames = new HashMap();
         this.keyNames0 = new StringItems();
         this.typeNames = new ArrayList();
         this.typeNames0 = new StringItems();
      }

      public void addKeyName(String name) {
         if (!this.keyNames.containsKey(name)) {
            StringItem stringItem = new StringItem(name);
            this.keyNames.put(name, stringItem);
            this.keyNames0.add(stringItem);
         }
      }

      public void addTypeName(int id, String name) {
         while(this.typeNames.size() <= id) {
            this.typeNames.add((Object)null);
         }

         StringItem item = (StringItem)this.typeNames.get(id);
         if (item == null) {
            this.typeNames.set(id, new StringItem(name));
         } else {
            throw new RuntimeException();
         }
      }

      // $FF: synthetic method
      PkgCtx(Object x0) {
         this();
      }
   }
}
