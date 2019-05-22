package com.mks.api.response.impl;

import com.mks.api.response.modifiable.ModifiableResponse;

interface ModifiableXMLResponse extends ModifiableResponse, ModifiableXMLResponseContainer {
   void interrupt();
}
