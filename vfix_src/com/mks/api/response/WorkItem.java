package com.mks.api.response;

public interface WorkItem extends Item, SubRoutineContainer, APIExceptionContainer {
   Result getResult();
}
