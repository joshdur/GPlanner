package com.drk.tools.gplannercore.core.search.context;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.streams.GOutputStream;
import com.drk.tools.gplannercore.core.streams.GStreamException;

class SafeStream {

    private final GOutputStream outputStream;

    SafeStream(GOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    void pushPlan(Plan plan, long remainingTime) throws SearchException {
        try {
            outputStream.write(plan, remainingTime);
        } catch (GStreamException e) {
            throw new SearchException(e);
        }
    }

    boolean isClosed() {
        return outputStream.isClosed();
    }

    void close() throws SearchException {
        try {
            outputStream.close();
        } catch (GStreamException e) {
            throw new SearchException(e);
        }
    }

}
