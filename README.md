# GPLANNER

GPlanner is a state-space planner based on a code generation concept.

It use annotations and Java types in order to define a planification language

## USE

Define your problem using atoms, variables and operators


### Variables

Define the variables of your problem using simple Enums

```
public enum Location {
    POSITION_A,
    POSITION_B,
    POSITION_C
}
```


### Atoms

Define your own atoms by extending the class Atom

```
public class At extends Atom<Location> {
}
```

### Operators

Define your operators using the @Operator annotation. 

```
public class Test {

  private final static At at = new At();

  @Operator
  public StateTransition move(Location from, Location to) {
        return new GStateTransition()
                  .check(at, from)
                  .not(at, from)
                  .set(at, to);
  }

}
```

Every method annotated with @Operator must follow this rules:

* returns an StateTransition.
* Parameters must be Enums


### Define initial and final state

```
private static final At at = new At();

private static State initialState() {
        GStateBuilder stateBuilder = new GStateBuilder();
        stateBuilder.set(at, Location.POSITION_A);
        return stateBuilder.build();
}

private static State finalState() {
        GStateBuilder stateBuilder = new GStateBuilder();
        stateBuilder.set(at, Location.POSITION_C);
        return stateBuilder.build();
}
```

### Start Searching Plans

Compiler use annotations to generate a Context class using the name of the class which holds your operators.

```
public static void main(String[] args) throws Throwable {
        Planner planner = new Planner(new GraphPlan(new SimpleForward()), 10);
        TestContext testContext = new TestContext();
        PlanStream planStream = planner.search(testContext, initialState(), finalState());
        List<Plan> plans = planStream.read();
        while (!plans.isEmpty()) {
            printPlans(plans, testContext);
            plans = planStream.read();
        }

}
```

### Configuration

```
apply plugin: 'java'
apply plugin: 'net.ltgt.apt'
apply plugin: 'idea'


repositories {
    maven {
      url  'https://dl.bintray.com/jddjose/joshdur/'
    }
}

dependencies {
    compile 'com.drk.tools:gplanner-core:0.7'
    apt 'com.drk.tools:gplanner-compiler:0.7'
}
```


