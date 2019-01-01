/*
 * Copyright (c) 2019. Edward Curren
 */

package com.pluralsight.duckair.app;

import com.pluralsight.duckair.model.AsmarEvent;
import com.pluralsight.duckair.service.AsmarService;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.util.UUID;

public class AsmarSimulatorApp {

    private static AsmarService asmarService;

    public static void main(String args[]) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("DuckAirlines-server.xml")) {
            asmarService = ignite.services().serviceProxy("AsmarService", AsmarService.class, false);
            addNewEvent();
        }
    }

    public static void addNewEvent() {
        AsmarEvent asmarEvent = new AsmarEvent(UUID.randomUUID().toString(), "DA198", "Weather");
        asmarService.addEvent(asmarEvent);
    }
}
