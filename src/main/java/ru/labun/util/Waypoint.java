package ru.labun.util;

import org.immutables.value.Value;

/**
 * Created by constantine on 23/01/16.
 */
@Value.Immutable
public interface Waypoint {
    @Value.Parameter
    Integer latitude();

    @Value.Parameter
    Integer longitude();
}
