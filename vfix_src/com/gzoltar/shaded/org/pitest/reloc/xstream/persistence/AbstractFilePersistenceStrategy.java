package com.gzoltar.shaded.org.pitest.reloc.xstream.persistence;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class AbstractFilePersistenceStrategy implements PersistenceStrategy {
   private final FilenameFilter filter;
   private final File baseDirectory;
   private final String encoding;
   private final transient XStream xstream;

   public AbstractFilePersistenceStrategy(File baseDirectory, XStream xstream, String encoding) {
      this.baseDirectory = baseDirectory;
      this.xstream = xstream;
      this.encoding = encoding;
      this.filter = new AbstractFilePersistenceStrategy.ValidFilenameFilter();
   }

   protected ConverterLookup getConverterLookup() {
      return this.xstream.getConverterLookup();
   }

   protected Mapper getMapper() {
      return this.xstream.getMapper();
   }

   protected boolean isValid(File dir, String name) {
      return name.endsWith(".xml");
   }

   protected abstract Object extractKey(String var1);

   protected abstract String getName(Object var1);

   private void writeFile(File file, Object value) {
      try {
         FileOutputStream out = new FileOutputStream(file);
         OutputStreamWriter writer = this.encoding != null ? new OutputStreamWriter(out, this.encoding) : new OutputStreamWriter(out);

         try {
            this.xstream.toXML(value, (Writer)writer);
         } finally {
            writer.close();
         }

      } catch (IOException var9) {
         throw new StreamException(var9);
      }
   }

   private File getFile(String filename) {
      return new File(this.baseDirectory, filename);
   }

   private Object readFile(File file) {
      try {
         FileInputStream in = new FileInputStream(file);
         InputStreamReader reader = this.encoding != null ? new InputStreamReader(in, this.encoding) : new InputStreamReader(in);

         Object var4;
         try {
            var4 = this.xstream.fromXML((Reader)reader);
         } finally {
            reader.close();
         }

         return var4;
      } catch (FileNotFoundException var10) {
         return null;
      } catch (IOException var11) {
         throw new StreamException(var11);
      }
   }

   public Object put(Object key, Object value) {
      Object oldValue = this.get(key);
      String filename = this.getName(key);
      this.writeFile(new File(this.baseDirectory, filename), value);
      return oldValue;
   }

   public Iterator iterator() {
      return new AbstractFilePersistenceStrategy.XmlMapEntriesIterator();
   }

   public int size() {
      return this.baseDirectory.list(this.filter).length;
   }

   public boolean containsKey(Object key) {
      File file = this.getFile(this.getName(key));
      return file.isFile();
   }

   public Object get(Object key) {
      return this.readFile(this.getFile(this.getName(key)));
   }

   public Object remove(Object key) {
      File file = this.getFile(this.getName(key));
      Object value = null;
      if (file.isFile()) {
         value = this.readFile(file);
         file.delete();
      }

      return value;
   }

   protected class XmlMapEntriesIterator implements Iterator {
      private final File[] files;
      private int position;
      private File current;

      protected XmlMapEntriesIterator() {
         this.files = AbstractFilePersistenceStrategy.this.baseDirectory.listFiles(AbstractFilePersistenceStrategy.this.filter);
         this.position = -1;
         this.current = null;
      }

      public boolean hasNext() {
         return this.position + 1 < this.files.length;
      }

      public void remove() {
         if (this.current == null) {
            throw new IllegalStateException();
         } else {
            this.current.delete();
         }
      }

      public Object next() {
         return new Entry() {
            private final File file;
            private final Object key;

            {
               this.file = XmlMapEntriesIterator.this.current = XmlMapEntriesIterator.this.files[++XmlMapEntriesIterator.this.position];
               this.key = AbstractFilePersistenceStrategy.this.extractKey(this.file.getName());
            }

            public Object getKey() {
               return this.key;
            }

            public Object getValue() {
               return AbstractFilePersistenceStrategy.this.readFile(this.file);
            }

            public Object setValue(Object value) {
               return AbstractFilePersistenceStrategy.this.put(this.key, value);
            }

            public boolean equals(Object obj) {
               if (!(obj instanceof Entry)) {
                  return false;
               } else {
                  boolean var10000;
                  label38: {
                     label27: {
                        Object value = this.getValue();
                        Entry e2 = (Entry)obj;
                        Object key2 = e2.getKey();
                        Object value2 = e2.getValue();
                        if (this.key == null) {
                           if (key2 != null) {
                              break label27;
                           }
                        } else if (!this.key.equals(key2)) {
                           break label27;
                        }

                        if (value == null) {
                           if (value2 == null) {
                              break label38;
                           }
                        } else if (this.getValue().equals(e2.getValue())) {
                           break label38;
                        }
                     }

                     var10000 = false;
                     return var10000;
                  }

                  var10000 = true;
                  return var10000;
               }
            }
         };
      }
   }

   protected class ValidFilenameFilter implements FilenameFilter {
      public boolean accept(File dir, String name) {
         return (new File(dir, name)).isFile() && AbstractFilePersistenceStrategy.this.isValid(dir, name);
      }
   }
}
