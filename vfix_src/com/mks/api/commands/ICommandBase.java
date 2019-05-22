package com.mks.api.commands;

import com.mks.api.OptionList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

interface ICommandBase {
   Response execute(boolean var1) throws APIException;

   Response execute(String[] var1, boolean var2) throws APIException;

   Response execute(String var1, boolean var2) throws APIException;

   void addOptionList(OptionList var1);
}
