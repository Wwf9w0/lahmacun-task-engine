package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        VirtualThreadOven oven = new VirtualThreadOven();
        LahmacunChef lahmacunChef = new LahmacunChef(oven);
        LahmacunFlow flow = new LahmacunFlow(lahmacunChef)
                .addTask(() -> "Merhaba")
                .addTask(() -> 42)
                .addTask(() -> new UserResponse(1, "Emre"));

        List<Object> result = flow.bakeAll();
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