package com.drk.tools.gplannercore.core.variables.collection;

import com.drk.tools.gplannercore.core.variables.VariableIterator;
import com.drk.tools.gplannercore.core.variables.VariableRange;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class CollectionVariableRange<T extends CollectionVariable> implements VariableRange<T> {

    private List<T> variables = new ArrayList<>();

    @Override
    public int count() {
        return variables.size();
    }

    @Override
    public T variableAt(int position) {
        return variables.get(position);
    }

    public void add(T value) {
        if (!variables.contains(value)) {
            variables.add(value);
        }
    }

    public void remove(T value) {
        variables.remove(value);
    }

    public void clear() {
        variables.clear();
    }

    @Override
    @NotNull
    public Iterator<T> iterator(){
        return new VariableIterator<>(this);
    }

}
