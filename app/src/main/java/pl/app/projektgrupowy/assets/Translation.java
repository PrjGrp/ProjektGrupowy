package pl.app.projektgrupowy.assets;

/**
 * Klasa realizująca tłumaczenie
 *
 * TODO: Dokończyć tę klasę. Dorobić segmentowanie i metody później potrzebne do dashboardu.
 */
public class Translation {
    private static final String xliffHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<xliff xmlns=\"urn:oasis:names:tc:xliff:document:1.2\" version=\"1.2\">" +
            "<file datatype=\"plaintext\" original=\"self\" source-language=\"en\" " +
            "target-language=\"de\"><body>";
    private static final String xliffFooter = "</body></file></xliff>";

    private final String sourceText;
    private String targetText;

    public Translation(String sourceText) {
        this.sourceText = sourceText;
    }
}
