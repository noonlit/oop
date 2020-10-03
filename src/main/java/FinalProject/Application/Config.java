package FinalProject.Application;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Handles retrieval of application configuration values.
 *
 * A sample configuration file is provided in resources/config.xml.dist.
 */
public class Config {
    private static final String PATH = "env/config.xml";
    private static Document document;

    /**
     * @param section The section where the configuration value is found (e.g. "db")
     * @param key     The node in the section that contains the value (e.g. "user")
     * @return The configuration value, e.g. "root"
     */
    public static String getConfigValue(String section, String key) {
        if (document == null) {
            File file = new File(PATH);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                document = documentBuilder.parse(file);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        Element sectionNode = (Element) document.getElementsByTagName(section).item(0);
        return sectionNode.getElementsByTagName(key).item(0).getTextContent();
    }
}
