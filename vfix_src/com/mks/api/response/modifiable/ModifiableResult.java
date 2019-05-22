package com.mks.api.response.modifiable;

import com.mks.api.response.Result;

public interface ModifiableResult extends Result, ModifiableFieldContainer {
   void setMessage(String var1);
}
