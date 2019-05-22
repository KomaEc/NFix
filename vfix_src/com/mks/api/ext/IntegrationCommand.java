package com.mks.api.ext;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.IntegrationPoint;
import com.mks.api.response.APIException;

public interface IntegrationCommand {
   int execute(IntegrationPoint var1, CmdRunnerCreator var2, ResponseWriter var3, CommandOptions var4, CommandSelection var5) throws APIException;
}
