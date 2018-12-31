package com.pluralsight.duckair.query;

import com.pluralsight.duckair.models.Reservations;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheEntry;
import org.apache.ignite.cache.affinity.*;
import org.apache.ignite.cache.query.*;

import java.util.List;

public class ReservationQueries {

    private Ignite ignite;
    private IgniteCache<String, Reservations> reservationsCache;

    public ReservationQueries(Ignite ignite) {
        this.ignite = ignite;
        reservationsCache = ignite.getOrCreateCache("ReservationsCache");
    }

    public CacheEntry<String, Reservations> getRandomReservation() {

        // Get a random key from the reservations table.  _key is a special field that gets the key from the key value pair.
        // _val gets the value of the key value pair.
        SqlFieldsQuery randomReservationKeyQuery = new SqlFieldsQuery("SELECT _key FROM reservations ORDER BY RAND() LIMIT 1");
        try (FieldsQueryCursor<List<?>> cursor = reservationsCache.query(randomReservationKeyQuery)) {
            String key = cursor.getAll().get(0).get(0).toString();
            return reservationsCache.getEntry(key);
        }
    }

    public AffinityKey<String> getAffinityKeyForReservation() {
        ReservationQueries reservationQueries = new ReservationQueries(ignite);
        CacheEntry<String, Reservations> reservation = reservationQueries.getRandomReservation();
        String customerId = reservation.getValue().getCustomerId();
        String reservationId = reservation.getKey();

        return new AffinityKey<>(customerId, reservationId);
    }
}