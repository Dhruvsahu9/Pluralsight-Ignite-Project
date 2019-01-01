/*
 * Copyright (c) 2019. Edward Curren
 */

package com.pluralsight.duckair.app;

import com.pluralsight.duckair.model.AsmarEvent;
import com.pluralsight.duckair.service.AsmarService;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.util.UUID;

public class AsmarFeederApp {

    private static AsmarService asmarService;

    public static void main(String args[]) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("DuckAirlines-server.xml")) {
            asmarService = ignite.services().serviceProxy("AsmarService", AsmarService.class, false);
            feedOne();
        }
    }

    /* NOTE: When using an affinity key, the key will look for a matching key in any of the caches.

        If it finds more than one
        Thanks for reply.
        Just confirm I understand right.

        IgniteCache<Integer, Organization> orgCache
        IgniteCache<Integer, Company> comCache
        IgniteCache<EmployeeKey, Employee> empCache

        In these three caches. The partition function will use EmployeeKey.organizationId to decide which node to store Employee.

        If orgCache, comCache's key have the same value with EmployeeKey.organizationId. Then all the three value will store in the same node. Because the partition function is the same to specify data type?
        http://apache-ignite-users.70518.x6.nabble.com/How-does-AffinityKey-mapped-td6260.html


    * There are no lookups to another cache when affinity key is used. "Affinity" is a synonym for "locality" or
    * "which node to store a cache entry on". There is function in Ignite that takes a key and returns which node the
    * key should belong to. It is called "affinity function". Ignite uses Rendezvous Hashing function by default.
    * By default Ignite passes the cache entry's key to the affinity function. For example, when you do
    * "cache.put(123, someValue)", Ignite would use key 123 to calculate which node "someValue" should be stored on.
    * But what if you want to store "someValue" on a node where "anotherValue" from another cache is stored?
    * In this case you need to tell Ignite to use anotherValue's key INSTEAD OF 123. Suppose anotherValue's key is 456.
    * Now you need a custom affinity key like "AffinityKey<Integer> affKey = new AffinityKey(123, 456)" and you add
    * someValue to the cache using the affinity key: "cache.put(affKey, someValue)".
    * Ignite will use "456" from the affinity key to get someValue's location, which will be same node
    * storing "anotherValeue".

      Thus, there are no lookups: you just tell Ignite which key to use to calculate the cache entry's location.

    */

    public static void feedOne() {
        AsmarEvent asmarEvent = new AsmarEvent(UUID.randomUUID().toString(), "DA198", "Weather");
        asmarService.addEvent(asmarEvent);
    }
}
