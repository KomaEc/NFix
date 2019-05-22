package pxb.android.axml;

import java.util.HashMap;
import java.util.Map;

public class DumpAdapter extends AxmlVisitor {
   protected int deep;
   protected Map<String, String> nses;

   public DumpAdapter() {
      this((NodeVisitor)null);
   }

   public DumpAdapter(NodeVisitor nv) {
      this(nv, 0, new HashMap());
   }

   public DumpAdapter(NodeVisitor nv, int x, Map<String, String> nses) {
      super(nv);
      this.deep = x;
      this.nses = nses;
   }

   public void attr(String ns, String name, int resourceId, int type, Object obj) {
      for(int i = 0; i < this.deep; ++i) {
         System.out.print("  ");
      }

      if (ns != null) {
         System.out.print(String.format("%s:", this.getPrefix(ns)));
      }

      System.out.print(name);
      if (resourceId != -1) {
         System.out.print(String.format("(%08x)", resourceId));
      }

      if (obj instanceof String) {
         System.out.print(String.format("=[%08x]\"%s\"", type, obj));
      } else if (obj instanceof Boolean) {
         System.out.print(String.format("=[%08x]\"%b\"", type, obj));
      } else if (obj instanceof ValueWrapper) {
         ValueWrapper w = (ValueWrapper)obj;
         System.out.print(String.format("=[%08x]@%08x, raw: \"%s\"", type, w.ref, w.raw));
      } else if (type == 1) {
         System.out.print(String.format("=[%08x]@%08x", type, obj));
      } else {
         System.out.print(String.format("=[%08x]%08x", type, obj));
      }

      System.out.println();
      super.attr(ns, name, resourceId, type, obj);
   }

   public NodeVisitor child(String ns, String name) {
      for(int i = 0; i < this.deep; ++i) {
         System.out.print("  ");
      }

      System.out.print("<");
      if (ns != null) {
         System.out.print(this.getPrefix(ns) + ":");
      }

      System.out.println(name);
      NodeVisitor nv = super.child(ns, name);
      return nv != null ? new DumpAdapter(nv, this.deep + 1, this.nses) : null;
   }

   protected String getPrefix(String uri) {
      if (this.nses != null) {
         String prefix = (String)this.nses.get(uri);
         if (prefix != null) {
            return prefix;
         }
      }

      return uri;
   }

   public void ns(String prefix, String uri, int ln) {
      System.out.println(prefix + "=" + uri);
      this.nses.put(uri, prefix);
      super.ns(prefix, uri, ln);
   }

   public void text(int ln, String value) {
      for(int i = 0; i < this.deep + 1; ++i) {
         System.out.print("  ");
      }

      System.out.print("T: ");
      System.out.println(value);
      super.text(ln, value);
   }
}
