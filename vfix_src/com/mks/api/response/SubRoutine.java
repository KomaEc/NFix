package com.mks.api.response;

public interface SubRoutine extends WorkItemContainer, SubRoutineContainer, APIExceptionContainer {
   String getRoutine();

   Result getResult();
}
