package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
import java.io.FileInputStream;

public class HashvalueAlgorithm implements Algorithm {
   public boolean isValid() {
      return true;
   }

   public String getValue(File file) {
      try {
         if (!file.canRead()) {
            return null;
         } else {
            FileInputStream fis = new FileInputStream(file);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            fis.close();
            String s = new String(content);
            int hash = s.hashCode();
            return Integer.toString(hash);
         }
      } catch (Exception var6) {
         return null;
      }
   }

   public String toString() {
      return "HashvalueAlgorithm";
   }
}
