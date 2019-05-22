package com.mks.api.response.modifiable;

import com.mks.api.response.Response;

public interface ModifiableResponse extends Response, ModifiableResultContainer, ModifiableSubRoutineContainer, ModifiableWorkItemContainer, ModifiableAPIExceptionContainer {
   void setConnectionHostname(String var1);

   void setConnectionPort(int var1);

   void setConnectionUsername(String var1);

   void setExitCode(int var1);

   void setApplicationName(String var1);

   void setCacheContents(boolean var1);

   void setUseInterim(boolean var1);
}
