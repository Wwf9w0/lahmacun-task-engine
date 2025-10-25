package org.engine;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        VirtualThreadOven oven = new VirtualThreadOven();
        LahmacunChef chef = new LahmacunChef(oven);
        LahmacunFlow flow = new LahmacunFlow(chef)
                .addTask(() -> "Hello World")
                .addTask(() -> List.of(1, 2, 3))
                .addTask(() -> new UserResponse(1, "Emre"));

        List<TaskResult<?>> result = flow.bakeAll();
        result.forEach(System.out::println);

    }
}

class UserResponse {
    private int id;
    private String name;

    public UserResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}