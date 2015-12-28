package tw.com.e_newken.keroro.genauthkey;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by keroro on 2015/12/25.
 */
public class PlatformXmlHandler {
    //region final parameters
    private static final String TAG = "PlatformXmlHandler";
    private static final String _XML_TAG_MODIFIED = "modified";
    private static final String _XML_TAG_PLATFORM_ROOT = "platforms";
    private static final String _XML_TAG_PLATFORM = "platform";
    private static final String _XML_TAG_NAME = "name";
    private static final String _XML_TAG_CREATED_DATE = "created_date";
    private static final String _XML_TAG_CHARACTERISTIC = "characteristic";
    private static final String _XML_TAG_COMMENT = "comment";
    //endregion

    //region private variables
    private File xmlFile;
    private Document doc;
    private boolean modified_flag = false;
    //endregion

    //region Constructor
    public PlatformXmlHandler(File src) throws IOException, ParserConfigurationException, SAXException {
        xmlFile = src;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(xmlFile);
    }
    //endregion

    //region private functions
    private int findDuplicatePlatform(String platformName) {
        NodeList nodes = ((Element) doc.getElementsByTagName(_XML_TAG_PLATFORM_ROOT).item(0)).getElementsByTagName(_XML_TAG_PLATFORM);

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            Element elementName = (Element) element.getElementsByTagName(_XML_TAG_NAME).item(0);
            if (elementName.getTextContent().compareTo(platformName) == 0) {
                return i;
            }
        }
        return -1;
    }
    //endregion

    //region public functions
    public void tagModifiedDate() {
        Node nodes = doc.getElementsByTagName(_XML_TAG_MODIFIED).item(0);
        Date d = new Date();
        nodes.setTextContent(d.toString());

        modified_flag = true;
    }

    public boolean addPlatform(PlatformInfo p) {
        if (findDuplicatePlatform(p.getName()) != -1)
            return false;

        Element elementPlatform = doc.createElement(_XML_TAG_PLATFORM);
        Element elementName = doc.createElement(_XML_TAG_NAME);
        Element elementCreatedDate = doc.createElement(_XML_TAG_CREATED_DATE);
        Element elementCharacteristic = doc.createElement(_XML_TAG_CHARACTERISTIC);
        Element elementComment = doc.createElement(_XML_TAG_COMMENT);

        elementName.setTextContent(p.getName());
        elementCreatedDate.setTextContent(new Date().toString());
        elementCharacteristic.setTextContent(p.getCharacteristic());
        elementComment.setTextContent(p.getComment());

        elementPlatform.appendChild(elementName);
        elementPlatform.appendChild(elementCreatedDate);
        elementPlatform.appendChild(elementCharacteristic);
        elementPlatform.appendChild(elementComment);

        NodeList nodes = ((Element) doc.getElementsByTagName(_XML_TAG_PLATFORM_ROOT).item(0)).getElementsByTagName(_XML_TAG_PLATFORM);
        Node node = nodes.item(nodes.getLength() - 1);
        node.appendChild(elementPlatform);

        modified_flag = true;
        return true;
    }

    public int removePlatfrom(String platformName) {
        NodeList nodes = ((Element) doc.getElementsByTagName(_XML_TAG_PLATFORM_ROOT).item(0)).getElementsByTagName(_XML_TAG_PLATFORM);

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            Element elementName = (Element) element.getElementsByTagName(_XML_TAG_NAME).item(0);
            if (elementName.getTextContent().compareTo(platformName) == 0) {
                doc.removeChild(nodes.item(i));
                modified_flag = true;
                return i;
            }
        }

        return 0;
    }

    public boolean save() {
        // initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(xmlFile);
        DOMSource source = new DOMSource(doc);
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        modified_flag = false;
        return true;
    }

    public int getPlatformNumber() {
        NodeList nodes = ((Element) doc.getElementsByTagName(_XML_TAG_PLATFORM_ROOT).item(0)).getElementsByTagName(_XML_TAG_PLATFORM);
        return nodes.getLength();
    }

    public final ArrayList<PlatformInfo> enumeratePlatforms() {
        ArrayList<PlatformInfo> platformArrayList = new ArrayList<>();

        NodeList nodes = ((Element) doc.getElementsByTagName(_XML_TAG_PLATFORM_ROOT).item(0)).getElementsByTagName(_XML_TAG_PLATFORM);
        for (int i = 0; i < nodes.getLength(); i++) {
            String name = ((Element) nodes.item(i)).getElementsByTagName(_XML_TAG_NAME).item(0).getTextContent();
            String createdDate = ((Element) nodes.item(i)).getElementsByTagName(_XML_TAG_CREATED_DATE).item(0).getTextContent();
            String characteristic = ((Element) nodes.item(i)).getElementsByTagName(_XML_TAG_CHARACTERISTIC).item(0).getTextContent();
            String comment = ((Element) nodes.item(i)).getElementsByTagName(_XML_TAG_COMMENT).item(0).getTextContent();
            try {
                PlatformInfo platformInfo = new PlatformInfo(name, createdDate, characteristic, comment);
                platformArrayList.add(platformInfo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return platformArrayList;
    }
    //endregion
}
