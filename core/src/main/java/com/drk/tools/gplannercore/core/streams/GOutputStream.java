package com.drk.tools.gplannercore.core.streams;


import com.drk.tools.gplannercore.core.Plan;

public interface GOutputStream {

    void write(Plan plan) throws GStreamException;

    void write(Plan plan, long timeout) throws GStreamException;

    void close() throws GStreamException;
}
