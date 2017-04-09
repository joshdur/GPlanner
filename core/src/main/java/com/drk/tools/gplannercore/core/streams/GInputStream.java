package com.drk.tools.gplannercore.core.streams;

import com.drk.tools.gplannercore.core.Plan;

public interface GInputStream {

    Plan read() throws GStreamException;

    void close() throws GStreamException;
}
