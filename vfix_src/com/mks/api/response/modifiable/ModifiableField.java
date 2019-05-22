package com.mks.api.response.modifiable;

import com.mks.api.response.Field;

public interface ModifiableField extends Field {
   void setValue(Object var1);

   void setModelType(String var1);

   void setDataType(String var1);

   void setDisplayValue(String var1);
}
