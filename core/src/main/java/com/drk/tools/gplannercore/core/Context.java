package com.drk.tools.gplannercore.core;

import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.core.variables.VariableRange;
import com.drk.tools.gplannercore.planner.state.atoms.single.None;

import java.util.ArrayList;
import java.util.List;

public abstract class Context {

    private final Mapper operatorMapper;
    private final Mapper variableMapper;
    private final Mapper mapper;
    private final boolean isDebug;

    public Context(boolean isDebug) {
        this.operatorMapper = new Mapper();
        this.variableMapper = new Mapper();
        this.variableMapper.set(None.class, new None.Range());
        this.mapper = new Mapper();
        this.isDebug = isDebug;
    }

    public List<BaseUnifier> getValidUnifiers(){
        List<BaseUnifier> validUnifiers = new ArrayList<>();
        for(BaseUnifier unifier : getUnifiers()){
            if(unifier.hasNext()){
                validUnifiers.add(unifier);
            }
        }
        return validUnifiers;
    }

    public abstract List<BaseUnifier> getUnifiers();

    public String asString(Plan plan) {
        StringBuilder sb = new StringBuilder();
        for (Transition transition : plan.transitions) {
            sb.append(asString(transition));
            sb.append(" | ");
        }
        sb.append("\n");
        return sb.toString();
    }

    public String asString(Transition transition) {
        BaseUnifier unifier = findUnifier(transition);
        return unifier != null ? unifier.asString(transition) : "";
    }

    private BaseUnifier findUnifier(Transition transition) {
        List<BaseUnifier> unifiers = getUnifiers();
        for (BaseUnifier baseUnifier : unifiers) {
            if (baseUnifier.getCode() == transition.unifierCode) {
                return baseUnifier;
            }
        }
        throw new IllegalStateException("Unifier for transition not found");
    }

    public void execute(Plan plan) throws Throwable {
        for (Transition transition : plan.transitions) {
            execute(transition);
        }
    }

    public Transition execute(Transition transition) throws Throwable {
        BaseUnifier unifier = findUnifier(transition);
        return unifier.execute(transition);
    }

    protected void addOperatorToInject(Object object) {
        operatorMapper.set(object.getClass(), object);
    }

    protected void addVariable(Class variable, Object range){
        variableMapper.set(variable, range);
    }

    public void addToInject(Object object) {
        mapper.set(object.getClass(), object);
    }

    public <T> T inject(Class<T> tClass) {
        return mapper.get(tClass);
    }

    public <T> T injectOperator(Class<T> tClass) {
        return operatorMapper.get(tClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends Variable> VariableRange<T> injectRangeFromVariableClass(Class<T> variable){
        return (VariableRange<T>) variableMapper.get(variable);
    }

    @SuppressWarnings("unchecked")
    public <T extends Variable> VariableRange<T> injectRangeFromVariableClass(T variable){
        return (VariableRange<T>) variableMapper.get(variable.getClass());
    }

    public boolean isDebug() {
        return isDebug;
    }

}
