package com.mks.api.response;

public interface SubRoutineIterator {
   SubRoutine next() throws APIException;

   boolean hasNext();

   SubRoutine getLast();
}
