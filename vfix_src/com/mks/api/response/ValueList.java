package com.mks.api.response;

import java.util.List;

public interface ValueList extends List {
   String getDataType();

   String getDisplayValueOf(Object var1);
}
