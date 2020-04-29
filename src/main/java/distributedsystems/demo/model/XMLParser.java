package distributedsystems.demo.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class XMLParser {
    public void addToXML(Node node) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("./maps.xml");
            Element root = document.getDocumentElement();

            Element newServer = document.createElement("map");

            Element nameElement = document.createElement("name");
            nameElement.appendChild(document.createTextNode(node.getName()));
            newServer.appendChild(nameElement);

            Element ipElement = document.createElement("ip");
            ipElement.appendChild(document.createTextNode(node.getIp()));
            newServer.appendChild(ipElement);

            Element previousIdElement = document.createElement("previousId");
            previousIdElement.appendChild(document.createTextNode(String.valueOf(node.getPrevHash())));
            newServer.appendChild(previousIdElement);

            Element nextIdElement = document.createElement("nextId");
            nextIdElement.appendChild(document.createTextNode(String.valueOf(node.getNextHash())));
            newServer.appendChild(nextIdElement);

            root.appendChild(newServer);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("./maps.xml");
            transformer.transform(source, result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
