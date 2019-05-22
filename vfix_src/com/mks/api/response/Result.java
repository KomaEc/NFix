package com.mks.api.response;

public interface Result extends FieldContainer {
   String getMessage();

   Item getPrimaryValue();
}
