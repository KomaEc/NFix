package com.mks.api.helper;

import com.mks.api.Session;
import com.mks.api.response.Response;
import com.mks.api.response.modifiable.ResponseFactory;

/** @deprecated */
public interface HelperCommand {
   Response execute(Session var1, ResponseFactory var2, String[] var3);
}
