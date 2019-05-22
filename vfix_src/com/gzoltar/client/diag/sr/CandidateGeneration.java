package com.gzoltar.client.diag.sr;

import com.gzoltar.instrumentation.spectra.Spectra;
import java.util.List;

public interface CandidateGeneration {
   List<String> getFaultyComponents(Spectra var1);
}
