package com.mks.api.response.modifiable;

import com.mks.api.response.ValueList;

public interface ModifiableValueList extends ValueList {
   void setDataType(String var1);

   void setDisplayValueOf(Object var1, String var2);
}
