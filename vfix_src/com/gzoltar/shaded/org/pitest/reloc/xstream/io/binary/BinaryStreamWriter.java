package com.gzoltar.shaded.org.pitest.reloc.xstream.io.binary;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class BinaryStreamWriter implements ExtendedHierarchicalStreamWriter {
   private final BinaryStreamWriter.IdRegistry idRegistry = new BinaryStreamWriter.IdRegistry();
   private final DataOutputStream out;
   private final Token.Formatter tokenFormatter = new Token.Formatter();

   public BinaryStreamWriter(OutputStream outputStream) {
      this.out = new DataOutputStream(outputStream);
   }

   public void startNode(String name) {
      this.write(new Token.StartNode(this.idRegistry.getId(name)));
   }

   public void startNode(String name, Class clazz) {
      this.startNode(name);
   }

   public void addAttribute(String name, String value) {
      this.write(new Token.Attribute(this.idRegistry.getId(name), value));
   }

   public void setValue(String text) {
      this.write(new Token.Value(text));
   }

   public void endNode() {
      this.write(new Token.EndNode());
   }

   public void flush() {
      try {
         this.out.flush();
      } catch (IOException var2) {
         throw new StreamException(var2);
      }
   }

   public void close() {
      try {
         this.out.close();
      } catch (IOException var2) {
         throw new StreamException(var2);
      }
   }

   public HierarchicalStreamWriter underlyingWriter() {
      return this;
   }

   private void write(Token token) {
      try {
         this.tokenFormatter.write(this.out, token);
      } catch (IOException var3) {
         throw new StreamException(var3);
      }
   }

   private class IdRegistry {
      private long nextId;
      private Map ids;

      private IdRegistry() {
         this.nextId = 0L;
         this.ids = new HashMap();
      }

      public long getId(String value) {
         Long id = (Long)this.ids.get(value);
         if (id == null) {
            id = new Long(++this.nextId);
            this.ids.put(value, id);
            BinaryStreamWriter.this.write(new Token.MapIdToValue(id, value));
         }

         return id;
      }

      // $FF: synthetic method
      IdRegistry(Object x1) {
         this();
      }
   }
}
