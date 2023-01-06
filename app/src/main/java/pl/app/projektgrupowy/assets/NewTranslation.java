package pl.app.projektgrupowy.assets;

import java.io.Serializable;

public class NewTranslation implements Serializable {

    public String title;
    public String sourceText;
    public String sourceLanguage;
    public String targetLanguage;

    public boolean validate() {
        return (title != null && !title.isEmpty())
                && (sourceText != null && !sourceText.isEmpty())
                && (sourceLanguage != null && !sourceLanguage.isEmpty())
                && (targetLanguage != null && !targetLanguage.isEmpty());
    }
}
