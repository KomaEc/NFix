package com.mks.api.response;

public interface SubRoutineContainer {
   int getSubRoutineListSize();

   SubRoutineIterator getSubRoutines();

   SubRoutine getSubRoutine(String var1);

   boolean containsSubRoutine(String var1);
}
