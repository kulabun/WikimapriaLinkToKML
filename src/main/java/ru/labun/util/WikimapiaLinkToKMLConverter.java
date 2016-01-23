package ru.labun.util;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by constantine on 23/01/16.
 */
public class WikimapiaLinkToKMLConverter {
    private final String wikimapiaUrl;
    private final Path kmlPath;
    private WaypointParser parser;

    public WikimapiaLinkToKMLConverter(String wikimapiaUrl, String kmlPath) {
        this.kmlPath = Paths.get(kmlPath);
        this.wikimapiaUrl = wikimapiaUrl;
        this.parser = new WaypointParser();
    }

    public static void main(String[] args) {
        if (isNothingToDo(args)) {
            return;
        }

        WikimapiaLinkToKMLConverter converter = new WikimapiaLinkToKMLConverter(getUrlWikimapia(args), getKmlPath(args));
        converter.run();
    }

    private static String getUrlWikimapia(String[] args) {
        return args[0];
    }

    private static boolean isNothingToDo(String[] args) {
        return args == null || args.length < 1;
    }

    private static String getKmlPath(String[] args) {
        String kmlPath = "result.kml";
        if (args.length == 2) {
            kmlPath = args[1];
        }
        return kmlPath;
    }

    private void run() {
        try {
            List<Waypoint> waypoints = parser.parseWaypointsFromLink(wikimapiaUrl);
            createTrackAsKML(waypoints)
                    .marshal(Files.newBufferedWriter(kmlPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Kml createTrackAsKML(List<Waypoint> waypoints) {
        Kml kml = KmlFactory.createKml();
        Document doc = kml.createAndSetDocument();
        doc.createAndAddStyle()
                .withId("track_style")
                .createAndSetLineStyle()
                .withColor("7f00ffff")
                .setWidth(4d);

        LineString trackLine = doc.createAndAddPlacemark()
                .withName("Personal Route")
                .withVisibility(true)
                .withOpen(false)
                .withStyleUrl("#track_style")
                .createAndSetMultiGeometry()
                .createAndAddLineString()
                .withExtrude(false)
                .withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);

        waypoints.forEach(it -> trackLine.addToCoordinates(latitude(it), longitude(it)));
        return kml;
    }

    private Double latitude(Waypoint wp){
        return wp.latitude().doubleValue() / 10000000d;
    }

    private Double longitude(Waypoint wp){
        return wp.longitude().doubleValue() / 10000000d;
    }
}
