package groovy.lang;

import java.io.IOException;
import java.io.Writer;

public interface Writable {
   Writer writeTo(Writer var1) throws IOException;
}
