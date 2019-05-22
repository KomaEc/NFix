package com.mks.api.response;

import java.util.Enumeration;

public interface Item extends FieldContainer {
   String getId();

   String getModelType();

   String getContext();

   String getDisplayId();

   String getContext(String var1);

   Enumeration getContextKeys();

   String toString();
}
