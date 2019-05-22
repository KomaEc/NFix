package scm;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class scmOutputStream extends DataOutputStream {
   scmOutputStream(String path) throws IOException {
      super(new BufferedOutputStream(new FileOutputStream(path)));
   }
}
