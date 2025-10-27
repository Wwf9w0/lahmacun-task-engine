# VirtualFlow 🌊

## Description

VirtualFlow is a Java-based DAG (Directed Acyclic Graph) task execution system. It manages task dependencies similarly to Airflow and ensures tasks are executed in the correct order.

This project models nodes and their dependencies to manage workflows. Cycle detection and root node identification guarantee the DAG's validity.

---

## FlowGraph Structure

### 1️⃣ FlowNode

Each task is represented by a `FlowNode`.

```java
FlowNode node = new FlowNode("FetchUsers", () -> System.out.println("Fetching users..."));
```

* `id`: Unique identifier for the node.
* `task`: Runnable task to execute.
* `nextNodes`: Nodes that run after this node.
* `prevNodes`: Nodes that ran before this node.

### 2️⃣ FlowGraph

The entire DAG is stored here.

```java
FlowGraph graph = new FlowGraph();
graph.addNode("FetchUsers", task);
graph.addEdge("FetchUsers", "TransformData");
```

* `addNode(String, Runnable)`: Adds a node.
* `addEdge(String, String)`: Connects two nodes.
* `validate()`: Validates the DAG and checks for cycles.
* `hasCycle()`: Checks for cycles.

---

## Example DAG and Dependencies

```java
FetchUsers -> TransformData -> PushToDB
FetchUsers -> SendNotification
```

* `FetchUsers` is the root node.
* `TransformData` depends on `PushToDB`.
* `SendNotification` runs independently after `FetchUsers`.

### Code Example

```java
FlowGraph graph = new FlowGraph();
graph.addNode("FetchUsers", () -> System.out.println("Fetching users..."));
graph.addNode("TransformData", () -> System.out.println("Transforming data..."));
graph.addNode("PushToDB", () -> System.out.println("Pushing data to DB..."));
graph.addNode("SendNotification", () -> System.out.println("Sending notification..."));

graph.addEdge("FetchUsers", "TransformData");
graph.addEdge("FetchUsers", "SendNotification");
graph.addEdge("TransformData", "PushToDB");

graph.validate();

return graph;

```

### Expected Output

```
--- VirtualFlow Runtime Results ---
FetchUsersProfile -> SUCCESS | UserProfile[userName=emre, email=emre@gmail.com] | 0
TransformData -> SUCCESS | TransformData | 0
PushToDB -> SUCCESS | Data pushed to DB | 0
SendNotification -> SUCCESS | Notification sent | 0
EventList -> SUCCESS | [Event[name=event1, time=1], Event[name=event2, time=2]] | 0
Completed Flows -> 0 ms

```

---

## Notes

* Using `LinkedHashMap` preserves insertion order.
* `validate()` checks the DAG for cycles.
* Nodes should be added in a dependency-respecting order to ensure correct execution.

---
