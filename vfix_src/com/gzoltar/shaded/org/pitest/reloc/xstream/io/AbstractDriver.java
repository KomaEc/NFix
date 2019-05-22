package com.gzoltar.shaded.org.pitest.reloc.xstream.io;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NoNameCoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class AbstractDriver implements HierarchicalStreamDriver {
   private NameCoder replacer;

   public AbstractDriver() {
      this(new NoNameCoder());
   }

   public AbstractDriver(NameCoder nameCoder) {
      this.replacer = nameCoder;
   }

   protected NameCoder getNameCoder() {
      return this.replacer;
   }

   public HierarchicalStreamReader createReader(URL in) {
      InputStream stream = null;

      try {
         stream = in.openStream();
      } catch (IOException var4) {
         throw new StreamException(var4);
      }

      return this.createReader((InputStream)stream);
   }

   public HierarchicalStreamReader createReader(File in) {
      try {
         return this.createReader((InputStream)(new FileInputStream(in)));
      } catch (FileNotFoundException var3) {
         throw new StreamException(var3);
      }
   }
}
