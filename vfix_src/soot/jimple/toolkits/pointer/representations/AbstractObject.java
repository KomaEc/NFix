package soot.jimple.toolkits.pointer.representations;

import soot.Type;

public interface AbstractObject {
   Type getType();

   String toString();

   String shortString();
}
