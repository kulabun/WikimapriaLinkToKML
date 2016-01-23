package ru.labun.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by constantine on 23/01/16.
 */
@RunWith(Parameterized.class)
public class WaypointParserTest {

    @Parameterized.Parameter(0)
    public String url;

    @Parameterized.Parameter(1)
    public List<Waypoint> expectedWaypoints;

    private WaypointParser waypointParser;

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[]{"http://wikimapia.org/#lang=ru&lat=12.345678&lon=91.345678&z=13&m=o&gz=0;12345678;91345678;0;1;2;3;4;5&search=москва",
                        Arrays.asList(ImmutableWaypoint.of(12345678, 91345679),
                                ImmutableWaypoint.of(12345680, 91345681),
                                ImmutableWaypoint.of(12345682, 91345683))
                },
                new Object[]{"http://wikimapia.org/#lang=ru&lat=21.345678&lon=19.345678&z=13&m=o&gz=0;21345678;19345678;100;100;200;200;300;300&search=москва",
                        Arrays.asList(ImmutableWaypoint.of(21345778, 19345778),
                                ImmutableWaypoint.of(21345878, 19345878),
                                ImmutableWaypoint.of(21345978, 19345978))
                });
    }

    @Before
    public void setUp() {
        this.waypointParser = new WaypointParser();
    }

    @Test
    public void should_return_waypoint_list() {
        List<Waypoint> waypoints = waypointParser.parseWaypointsFromLink(url);
        Assert.assertArrayEquals("Received waypoints are unexpected", expectedWaypoints.toArray(), waypoints.toArray());
    }
}
