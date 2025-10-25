package org.engine.virtual;

import org.engine.core.VirtualThreadOven;

import java.util.ArrayList;
import java.util.List;

public class VirtualMain {

    public static void main(String[] args) {
        VirtualThreadOven oven = new VirtualThreadOven();
        VirtuaLahmacunlChef chef = new VirtuaLahmacunlChef(oven);
        VirtuaLahmacunFlow virtualFlow = new VirtuaLahmacunFlow(chef);
        UserService userService = new UserService();
        EventService eventService = new EventService();

        VirtualLahmacunTask<UserProfile> profileTask = virtualFlow.addTask(userService::getProfile);
        VirtualLahmacunTask<List<Event>> eventTask = virtualFlow.addTask(eventService::eventList);
        VirtualLahmacunTask<String> stringTask = virtualFlow.addTask(() -> "Hello");

        chef.waitAll();
        virtualFlow.bakeAll();
        UserProfile profile = profileTask.join();
        List<Event> events = eventTask.join();
        String string = stringTask.join();

        System.out.println("Profile: " + profile);
        System.out.println("Events: " + events);
        System.out.println("String: " + string);

    }
}

class EventService {
    public List<Event> eventList() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("event1", 1));
        events.add(new Event("event2", 2));
        return events;
    }
}

class UserService {
    public UserProfile getProfile() {
        return new UserProfile("emre", "emre@gmail.com");
    }
}

record UserProfile(String userName, String email) {
}

record Event(String name, int time) {
}