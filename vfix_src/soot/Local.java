package soot;

import soot.util.Numberable;

public interface Local extends Value, Numberable, Immediate {
   String getName();

   void setName(String var1);

   void setType(Type var1);
}
