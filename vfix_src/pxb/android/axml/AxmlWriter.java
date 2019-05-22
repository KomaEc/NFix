package pxb.android.axml;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Map.Entry;
import pxb.android.StringItem;
import pxb.android.StringItems;

public class AxmlWriter extends AxmlVisitor {
   static final Comparator<AxmlWriter.Attr> ATTR_CMP = new Comparator<AxmlWriter.Attr>() {
      public int compare(AxmlWriter.Attr a, AxmlWriter.Attr b) {
         int x = a.resourceId - b.resourceId;
         if (x == 0) {
            x = a.name.data.compareTo(b.name.data);
            if (x == 0) {
               boolean aNsIsnull = a.ns == null;
               boolean bNsIsnull = b.ns == null;
               if (aNsIsnull) {
                  if (bNsIsnull) {
                     x = 0;
                  } else {
                     x = -1;
                  }
               } else if (bNsIsnull) {
                  x = 1;
               } else {
                  x = a.ns.data.compareTo(b.ns.data);
               }
            }
         }

         return x;
      }
   };
   private List<AxmlWriter.NodeImpl> firsts = new ArrayList(3);
   private Map<String, AxmlWriter.Ns> nses = new HashMap();
   private List<StringItem> otherString = new ArrayList();
   private Map<String, StringItem> resourceId2Str = new HashMap();
   private List<Integer> resourceIds = new ArrayList();
   private List<StringItem> resourceString = new ArrayList();
   private StringItems stringItems = new StringItems();

   public NodeVisitor child(String ns, String name) {
      AxmlWriter.NodeImpl first = new AxmlWriter.NodeImpl(ns, name);
      this.firsts.add(first);
      return first;
   }

   public void end() {
   }

   public void ns(String prefix, String uri, int ln) {
      this.nses.put(uri, new AxmlWriter.Ns(prefix == null ? null : new StringItem(prefix), new StringItem(uri), ln));
   }

   private int prepare() throws IOException {
      int size = 0;

      AxmlWriter.NodeImpl first;
      for(Iterator var2 = this.firsts.iterator(); var2.hasNext(); size += first.prepare(this)) {
         first = (AxmlWriter.NodeImpl)var2.next();
      }

      int stringSize = 0;

      AxmlWriter.Ns ns;
      for(Iterator var7 = this.nses.entrySet().iterator(); var7.hasNext(); ns.uri = this.update(ns.uri)) {
         Entry<String, AxmlWriter.Ns> e = (Entry)var7.next();
         ns = (AxmlWriter.Ns)e.getValue();
         if (ns == null) {
            ns = new AxmlWriter.Ns((StringItem)null, new StringItem((String)e.getKey()), 0);
            e.setValue(ns);
         }

         if (ns.prefix == null) {
            ns.prefix = new StringItem(String.format("axml_auto_%02d", stringSize++));
         }

         ns.prefix = this.update(ns.prefix);
      }

      size += this.nses.size() * 24 * 2;
      this.stringItems.addAll(this.resourceString);
      this.resourceString = null;
      this.stringItems.addAll(this.otherString);
      this.otherString = null;
      this.stringItems.prepare();
      stringSize = this.stringItems.getSize();
      if (stringSize % 4 != 0) {
         stringSize += 4 - stringSize % 4;
      }

      size += 8 + stringSize;
      size += 8 + this.resourceIds.size() * 4;
      return size;
   }

   public byte[] toByteArray() throws IOException {
      int size = 8 + this.prepare();
      ByteBuffer out = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
      out.putInt(524291);
      out.putInt(size);
      int stringSize = this.stringItems.getSize();
      int padding = 0;
      if (stringSize % 4 != 0) {
         padding = 4 - stringSize % 4;
      }

      out.putInt(1835009);
      out.putInt(stringSize + padding + 8);
      this.stringItems.write(out);
      out.put(new byte[padding]);
      out.putInt(524672);
      out.putInt(8 + this.resourceIds.size() * 4);
      Iterator var5 = this.resourceIds.iterator();

      while(var5.hasNext()) {
         Integer i = (Integer)var5.next();
         out.putInt(i);
      }

      Stack<AxmlWriter.Ns> stack = new Stack();
      Iterator var10 = this.nses.entrySet().iterator();

      while(var10.hasNext()) {
         Entry<String, AxmlWriter.Ns> e = (Entry)var10.next();
         AxmlWriter.Ns ns = (AxmlWriter.Ns)e.getValue();
         stack.push(ns);
         out.putInt(1048832);
         out.putInt(24);
         out.putInt(-1);
         out.putInt(-1);
         out.putInt(ns.prefix.index);
         out.putInt(ns.uri.index);
      }

      var10 = this.firsts.iterator();

      while(var10.hasNext()) {
         AxmlWriter.NodeImpl first = (AxmlWriter.NodeImpl)var10.next();
         first.write(out);
      }

      while(stack.size() > 0) {
         AxmlWriter.Ns ns = (AxmlWriter.Ns)stack.pop();
         out.putInt(1048833);
         out.putInt(24);
         out.putInt(ns.ln);
         out.putInt(-1);
         out.putInt(ns.prefix.index);
         out.putInt(ns.uri.index);
      }

      return out.array();
   }

   StringItem update(StringItem item) {
      if (item == null) {
         return null;
      } else {
         int i = this.otherString.indexOf(item);
         if (i < 0) {
            StringItem copy = new StringItem(item.data);
            this.otherString.add(copy);
            return copy;
         } else {
            return (StringItem)this.otherString.get(i);
         }
      }
   }

   StringItem updateNs(StringItem item) {
      if (item == null) {
         return null;
      } else {
         String ns = item.data;
         if (!this.nses.containsKey(ns)) {
            this.nses.put(ns, (Object)null);
         }

         return this.update(item);
      }
   }

   StringItem updateWithResourceId(StringItem name, int resourceId) {
      String key = name.data + resourceId;
      StringItem item = (StringItem)this.resourceId2Str.get(key);
      if (item != null) {
         return item;
      } else {
         StringItem copy = new StringItem(name.data);
         this.resourceIds.add(resourceId);
         this.resourceString.add(copy);
         this.resourceId2Str.put(key, copy);
         return copy;
      }
   }

   static class Ns {
      int ln;
      StringItem prefix;
      StringItem uri;

      public Ns(StringItem prefix, StringItem uri, int ln) {
         this.prefix = prefix;
         this.uri = uri;
         this.ln = ln;
      }
   }

   static class NodeImpl extends NodeVisitor {
      private Set<AxmlWriter.Attr> attrs;
      private List<AxmlWriter.NodeImpl> children;
      private int line;
      private StringItem name;
      private StringItem ns;
      private StringItem text;
      private int textLineNumber;
      AxmlWriter.Attr id;
      AxmlWriter.Attr style;
      AxmlWriter.Attr clz;

      public NodeImpl(String ns, String name) {
         super((NodeVisitor)null);
         this.attrs = new TreeSet(AxmlWriter.ATTR_CMP);
         this.children = new ArrayList();
         this.ns = ns == null ? null : new StringItem(ns);
         this.name = name == null ? null : new StringItem(name);
      }

      public void attr(String ns, String name, int resourceId, int type, Object value) {
         if (name == null) {
            throw new RuntimeException("name can't be null");
         } else {
            AxmlWriter.Attr a = new AxmlWriter.Attr(ns == null ? null : new StringItem(ns), new StringItem(name), resourceId);
            a.type = type;
            if (value instanceof ValueWrapper) {
               ValueWrapper valueWrapper = (ValueWrapper)value;
               if (valueWrapper.raw != null) {
                  a.raw = new StringItem(valueWrapper.raw);
               }

               a.value = valueWrapper.ref;
               switch(valueWrapper.type) {
               case 1:
                  this.id = a;
                  break;
               case 2:
                  this.style = a;
                  break;
               case 3:
                  this.clz = a;
               }
            } else if (type == 3) {
               StringItem raw = new StringItem((String)value);
               a.raw = raw;
               a.value = raw;
            } else {
               a.raw = null;
               a.value = value;
            }

            this.attrs.add(a);
         }
      }

      public NodeVisitor child(String ns, String name) {
         AxmlWriter.NodeImpl child = new AxmlWriter.NodeImpl(ns, name);
         this.children.add(child);
         return child;
      }

      public void end() {
      }

      public void line(int ln) {
         this.line = ln;
      }

      public int prepare(AxmlWriter axmlWriter) {
         this.ns = axmlWriter.updateNs(this.ns);
         this.name = axmlWriter.update(this.name);
         int attrIndex = 0;
         Iterator var3 = this.attrs.iterator();

         while(var3.hasNext()) {
            AxmlWriter.Attr attr = (AxmlWriter.Attr)var3.next();
            attr.index = attrIndex++;
            attr.prepare(axmlWriter);
         }

         this.text = axmlWriter.update(this.text);
         int size = 60 + this.attrs.size() * 20;

         AxmlWriter.NodeImpl child;
         for(Iterator var7 = this.children.iterator(); var7.hasNext(); size += child.prepare(axmlWriter)) {
            child = (AxmlWriter.NodeImpl)var7.next();
         }

         if (this.text != null) {
            size += 28;
         }

         return size;
      }

      public void text(int ln, String value) {
         this.text = new StringItem(value);
         this.textLineNumber = ln;
      }

      void write(ByteBuffer out) throws IOException {
         out.putInt(1048834);
         out.putInt(36 + this.attrs.size() * 20);
         out.putInt(this.line);
         out.putInt(-1);
         out.putInt(this.ns != null ? this.ns.index : -1);
         out.putInt(this.name.index);
         out.putInt(1310740);
         out.putShort((short)this.attrs.size());
         out.putShort((short)(this.id == null ? 0 : this.id.index + 1));
         out.putShort((short)(this.clz == null ? 0 : this.clz.index + 1));
         out.putShort((short)(this.style == null ? 0 : this.style.index + 1));
         Iterator var2 = this.attrs.iterator();

         while(var2.hasNext()) {
            AxmlWriter.Attr attr = (AxmlWriter.Attr)var2.next();
            out.putInt(attr.ns == null ? -1 : attr.ns.index);
            out.putInt(attr.name.index);
            out.putInt(attr.raw != null ? attr.raw.index : -1);
            out.putInt(attr.type << 24 | 8);
            Object v = attr.value;
            if (v instanceof StringItem) {
               out.putInt(((StringItem)attr.value).index);
            } else if (v instanceof Boolean) {
               out.putInt(Boolean.TRUE.equals(v) ? -1 : 0);
            } else {
               out.putInt((Integer)attr.value);
            }
         }

         if (this.text != null) {
            out.putInt(1048836);
            out.putInt(28);
            out.putInt(this.textLineNumber);
            out.putInt(-1);
            out.putInt(this.text.index);
            out.putInt(8);
            out.putInt(0);
         }

         var2 = this.children.iterator();

         while(var2.hasNext()) {
            AxmlWriter.NodeImpl child = (AxmlWriter.NodeImpl)var2.next();
            child.write(out);
         }

         out.putInt(1048835);
         out.putInt(24);
         out.putInt(-1);
         out.putInt(-1);
         out.putInt(this.ns != null ? this.ns.index : -1);
         out.putInt(this.name.index);
      }
   }

   static class Attr {
      public int index;
      public StringItem name;
      public StringItem ns;
      public int resourceId;
      public int type;
      public Object value;
      public StringItem raw;

      public Attr(StringItem ns, StringItem name, int resourceId) {
         this.ns = ns;
         this.name = name;
         this.resourceId = resourceId;
      }

      public void prepare(AxmlWriter axmlWriter) {
         this.ns = axmlWriter.updateNs(this.ns);
         if (this.name != null) {
            if (this.resourceId != -1) {
               this.name = axmlWriter.updateWithResourceId(this.name, this.resourceId);
            } else {
               this.name = axmlWriter.update(this.name);
            }
         }

         if (this.value instanceof StringItem) {
            this.value = axmlWriter.update((StringItem)this.value);
         }

         if (this.raw != null) {
            this.raw = axmlWriter.update(this.raw);
         }

      }
   }
}
