package pl.app.projektgrupowy.assets;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Klasa realizująca tłumaczenie
 *
 * TODO: Ulepszyć maszynkę do segmentowania, zrobić parsera xml
 */
public class Translation {
    private static final String xliffHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<xliff xmlns=\"urn:oasis:names:tc:xliff:document:1.2\" version=\"1.2\">" +
            "<file datatype=\"plaintext\" original=\"%s\" source-language=\"en\" " +
            "target-language=\"de\"><body>";
    private static final String xliffFooter = "</body></file></xliff>";
    private static final String splitRegex = "(?<=\\.)(?= )";

    private final String title;
    private final String sourceText;
    public final Segment[] segments;

    public Translation(String title, String sourceText) {
        String[] segmentedSourceText = sourceText.split(splitRegex);
        int segmentsLength = segmentedSourceText.length;
        Segment[] segments = new Segment[segmentsLength];

        for (int i = 0; i < segmentsLength; i++) segments[i] = new Segment(i, segmentedSourceText[i]);

        this.title = title;
        this.sourceText = sourceText;
        this.segments = segments;
    }

    public String getSourceText() {
        return sourceText;
    }

    public String getTargetText() {
        StringBuilder targetTextString = new StringBuilder();

        for (Segment s : segments) targetTextString.append(s.getTargetText());

        return targetTextString.toString();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder segmentsString = new StringBuilder();

        for (Segment s : segments) segmentsString.append(s.toString());

        return String.format(xliffHeader, title) + segmentsString + xliffFooter;
    }
}
