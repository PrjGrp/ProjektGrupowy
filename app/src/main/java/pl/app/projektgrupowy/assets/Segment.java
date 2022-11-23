package pl.app.projektgrupowy.assets;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

public class Segment {
    private final int id;
    private final String sourceText;
    private String targetText;

    public Segment(int id, String sourceText) {
        this.sourceText = sourceText;
        this.id = id;
    }

    public String getTargetText() {
        return targetText;
    }

    public void translate(String translatedText) {
        targetText = translatedText;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("<trans-unit id=\"%d\" xmlns:sap=\"urn:x-sap:sls-mlt\">" +
                "<source>%s</source><target>%s</target></trans-unit>", id, sourceText,
                targetText == null ? "" : targetText);
    }
}
