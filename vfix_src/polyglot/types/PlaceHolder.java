package polyglot.types;

import java.io.Serializable;

public interface PlaceHolder extends Serializable {
   TypeObject resolve(TypeSystem var1);
}
