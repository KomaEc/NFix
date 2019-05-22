package com.mks.api.response.modifiable;

import com.mks.api.response.Field;
import com.mks.api.response.FieldContainer;

public interface ModifiableFieldContainer extends FieldContainer {
   void add(Field var1);
}
