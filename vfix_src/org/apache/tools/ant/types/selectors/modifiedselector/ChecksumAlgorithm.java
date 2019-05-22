package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.tools.ant.BuildException;

public class ChecksumAlgorithm implements Algorithm {
   private String algorithm = "CRC";
   private Checksum checksum = null;

   public void setAlgorithm(String algorithm) {
      this.algorithm = algorithm;
   }

   public void initChecksum() {
      if (this.checksum == null) {
         if ("CRC".equalsIgnoreCase(this.algorithm)) {
            this.checksum = new CRC32();
         } else {
            if (!"ADLER".equalsIgnoreCase(this.algorithm)) {
               throw new BuildException(new NoSuchAlgorithmException());
            }

            this.checksum = new Adler32();
         }

      }
   }

   public boolean isValid() {
      return "CRC".equalsIgnoreCase(this.algorithm) || "ADLER".equalsIgnoreCase(this.algorithm);
   }

   public String getValue(File file) {
      this.initChecksum();
      String rval = null;

      try {
         if (file.canRead()) {
            this.checksum.reset();
            FileInputStream fis = new FileInputStream(file);
            CheckedInputStream check = new CheckedInputStream(fis, this.checksum);
            BufferedInputStream in = new BufferedInputStream(check);

            while(true) {
               if (in.read() == -1) {
                  rval = Long.toString(check.getChecksum().getValue());
                  in.close();
                  break;
               }
            }
         }
      } catch (Exception var6) {
         rval = null;
      }

      return rval;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("<ChecksumAlgorithm:");
      buf.append("algorithm=").append(this.algorithm);
      buf.append(">");
      return buf.toString();
   }
}
