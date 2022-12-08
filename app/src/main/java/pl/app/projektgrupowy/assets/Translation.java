package pl.app.projektgrupowy.assets;

import androidx.annotation.NonNull;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Klasa realizująca tłumaczenie
 *
 * TODO: Ulepszyć maszynkę do segmentowania
 */
public class Translation {
    public static final String BRITISH_ENGLISH = "en-GB";
    public static final String AMERICAN_ENGLISH = "en-US";
    public static final String POLISH = "pl-PL";
    public static final String GERMAN = "de-DE";
    public static final String AUSTRIAN = "de-AT";


    private static final String xliffHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<xliff xmlns=\"urn:oasis:names:tc:xliff:document:1.2\" version=\"1.2\">" +
            "<file datatype=\"plaintext\" original=\"%s\" source-language=\"%s\" " +
            "target-language=\"%s\"><body>";
    private static final String xliffFooter = "</body></file></xliff>";
    private static final String splitRegex = "(?<=\\.)(?= )";

    private final String title;
    private final String sourceText;
    private final String sourceLanguage;
    private final String targetLanguage;
    private final int id;
    public final Segment[] segments;

    public Translation(String title, String sourceText, String sourceLanguage, String targetLanguage) {
        String[] segmentedSourceText = sourceText.split(splitRegex);
        int segmentsLength = segmentedSourceText.length;
        Segment[] segments = new Segment[segmentsLength];

        for (int i = 0; i < segmentsLength; i++) segments[i] = new Segment(i, segmentedSourceText[i]);

        this.title = title;
        this.sourceText = sourceText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.segments = segments;
        this.id = 0;
    }

    private Translation(NodeList titleNodes, NodeList segmentNodeList, int id) {
        Element titleElement = (Element) titleNodes.item(0);
        int segmentNodeListLength = segmentNodeList.getLength();
        Segment[] segments = new Segment[segmentNodeListLength];
        StringBuilder sourceString = new StringBuilder();
        String title, sourceLanguage, targetLanguage;

        for (int i = 0; i < segmentNodeListLength; i++) {
            Element e = (Element) segmentNodeList.item(i);
            int idSegment = Integer.parseInt(e.getAttribute("id"));

            NodeList sourceNodeList = e.getElementsByTagName("source");
            NodeList targetNodeList = e.getElementsByTagName("target");
            Element sourceElement = (Element) sourceNodeList.item(0);
            Element targetElement = (Element) targetNodeList.item(0);
            String source = getCharacterData(sourceElement);
            String target = getCharacterData(targetElement);

            Segment segment = new Segment(idSegment, source);
            segment.translate(target);
            sourceString.append(source);

            segments[i] = segment;
        }

        title = titleElement.getAttribute("original");
        sourceLanguage = titleElement.getAttribute("source-language");
        targetLanguage = titleElement.getAttribute("target-language");

        this.title = title;
        this.sourceText = sourceString.toString();
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.segments = segments;
        this.id = id;
    }

    private static String getCharacterData(Element e) {
        Node child = e.getFirstChild();
        String result = "";

        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            result = cd.getData();
        }

        return result;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getSourceText() {
        return sourceText;
    }

    public String getTargetText() {
        StringBuilder targetTextString = new StringBuilder();

        for (Segment s : segments) targetTextString.append(s.getTargetText());

        return targetTextString.toString();
    }

    public static Translation parseXliff(String xliff, int id) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Translation translation = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xliff)));
            NodeList fileNodes = doc.getElementsByTagName("file");
            NodeList segmentNodes = doc.getElementsByTagName("trans-unit");

            translation = new Translation(fileNodes, segmentNodes, id);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return translation;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder segmentsString = new StringBuilder();

        for (Segment s : segments) segmentsString.append(s.toString());

        return String.format(xliffHeader, title, sourceLanguage, targetLanguage) + segmentsString + xliffFooter;
    }
}