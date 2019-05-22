package polyglot.types;

import java.io.Serializable;
import polyglot.util.Copy;
import polyglot.util.Position;

public interface TypeObject extends Copy, Serializable {
   boolean isCanonical();

   TypeSystem typeSystem();

   Position position();

   boolean equalsImpl(TypeObject var1);
}
