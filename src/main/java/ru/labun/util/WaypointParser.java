package ru.labun.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by constantine on 23/01/16.
 */
public class WaypointParser {
    public static final String PARAMETER_DISPLAY_LATITUDE = "lat";
    public static final String PARAMETER_DISPLAY_LONGITUDE = "lon";
    public static final String PARAMETER_COORDS = "gz";
    public static final String PARAMETER_ZOOM = "z";

    public List<Waypoint> parseWaypointsFromLink(String wikimapiaLink) {
        try {
            return parseWaypointsFromLink(new URL(wikimapiaLink));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Waypoint> parseWaypointsFromLink(URL wikimapiaLink) {
        Map<String, String> parameters = getQueryParameters(wikimapiaLink);
        String deltas = parameters.get(PARAMETER_COORDS);

        Scanner coordsScanner = new Scanner(deltas);
        coordsScanner.useDelimiter(";");

        // ignore initial zero
        coordsScanner.next();

        Integer baseLatitude = Integer.valueOf(coordsScanner.next());
        Integer baseLongitude = Integer.valueOf(coordsScanner.next());

        ArrayList<Waypoint> waypoints = new ArrayList<>();
        while (coordsScanner.hasNext()) {
            Integer dLat = Integer.valueOf(coordsScanner.next());
            Integer dLon = Integer.valueOf(coordsScanner.next());
            Waypoint wp = ImmutableWaypoint.of(baseLatitude + dLat, baseLongitude + dLon);
            waypoints.add(wp);
        }
        return waypoints;
    }

    private Map<String, String> getQueryParameters(URL url) {
        String query = url.getRef();
        Scanner scanner = new Scanner(query);
        scanner.useDelimiter("&");

        Map<String, String> parametersMap = new LinkedHashMap<>();
        scanner.forEachRemaining(it -> {
            String[] nameValue = it.split("=");
            String name = nameValue[0];
            String value = nameValue[1];
            parametersMap.put(name, value);
        });
        return parametersMap;
    }

}
