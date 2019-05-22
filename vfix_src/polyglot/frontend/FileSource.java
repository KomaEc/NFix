package polyglot.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

public class FileSource extends Source {
   protected final File file;
   protected FileReader reader;

   public FileSource(File file) throws IOException {
      this(file, false);
   }

   public FileSource(File file, boolean userSpecified) throws IOException {
      super(file.getName(), userSpecified);
      this.file = file;
      if (!file.exists()) {
         throw new FileNotFoundException(file.getName());
      } else {
         this.path = file.getPath();
         this.lastModified = new Date(file.lastModified());
      }
   }

   public boolean equals(Object o) {
      if (o instanceof FileSource) {
         FileSource s = (FileSource)o;
         return this.file.equals(s.file);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.file.getPath().hashCode();
   }

   public Reader open() throws IOException {
      if (this.reader == null) {
         this.reader = new FileReader(this.file);
      }

      return this.reader;
   }

   public void close() throws IOException {
      if (this.reader != null) {
         this.reader.close();
         this.reader = null;
      }

   }

   public String toString() {
      return this.file.getPath();
   }
}
