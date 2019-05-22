package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

public class SignatureRemover {
   private static final Pattern SIGNATURE_FILES = Pattern.compile("META-INF/[^/]*\\.SF|META-INF/[^/]*\\.DSA|META-INF/[^/]*\\.RSA|META-INF/SIG-[^/]*");
   private static final String MANIFEST_MF = "META-INF/MANIFEST.MF";
   private static final String DIGEST_SUFFIX = "-Digest";
   private boolean active = true;

   public void setActive(boolean active) {
      this.active = active;
   }

   public boolean removeEntry(String name) {
      return this.active && SIGNATURE_FILES.matcher(name).matches();
   }

   public boolean filterEntry(String name, InputStream in, OutputStream out) throws IOException {
      if (this.active && "META-INF/MANIFEST.MF".equals(name)) {
         Manifest mf = new Manifest(in);
         this.filterManifestEntry(mf.getEntries().values());
         mf.write(out);
         return true;
      } else {
         return false;
      }
   }

   private void filterManifestEntry(Collection<Attributes> entry) {
      Iterator i = entry.iterator();

      while(i.hasNext()) {
         Attributes attributes = (Attributes)i.next();
         this.filterManifestEntryAttributes(attributes);
         if (attributes.isEmpty()) {
            i.remove();
         }
      }

   }

   private void filterManifestEntryAttributes(Attributes attrs) {
      Iterator i = attrs.keySet().iterator();

      while(i.hasNext()) {
         if (String.valueOf(i.next()).endsWith("-Digest")) {
            i.remove();
         }
      }

   }
}
