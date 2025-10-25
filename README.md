# Lahmacun Task Engine 

**Lahmacun Task Engine** is a lightweight Java 21 task flow engine leveraging Virtual Threads for parallel execution. It allows you to run tasks of different types, collect results, and share data between tasks using a context.

---

## Features

- **Parallel task execution** using Virtual Threads
- Supports **tasks with different return types** (results collected as `Object`)
- **TaskContext** for sharing data between tasks
- Lambda-friendly and simple **API**
- Task status tracking and error handling

---

## Example Usage

```java
VirtualThreadOven oven = new VirtualThreadOven();
LahmacunChef chef = new LahmacunChef(oven);

LahmacunFlow flow = new LahmacunFlow(chef)
        .addTask(() -> "Hello")
        .addTask(() -> 42)
        .addTask(() -> new UserResponse(1, "Emre"));

List<Object> results = flow.bakeAll();

results.forEach(System.out::println);
