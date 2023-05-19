package common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtils {
	public XmlUtils() {
		super();
	}

	/**
	 * Chuyen doi Document XML sang chuoi XML
	 * 
	 * @param doc Co kieu org.w3c.dom.Document
	 * @return Chuoi XML
	 */
	public static String convertDocumentToString(Document doc) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String output = null;
		try {
			transformer = tf.newTransformer();
			// below code to remove XML declaration
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Writer out = new StringWriter();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			// output = out.toString();
			output = writer.getBuffer().toString();

		} catch (TransformerException e) {
			e.printStackTrace();
			throw e;
		}

		return output;
	}

	/**
	 * Chuyen doi chuoi XML sang document XML
	 * 
	 * @param xmlStr Co kieu String la chuoi XML
	 * @return org.w3c.dom.Document
	 */
	public static Document convertStringToDocument(String xmlStr, boolean acceptAllPrefix) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		if (acceptAllPrefix)
			factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			// doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlStr));
			doc = builder.parse(is);
			doc.getDocumentElement().normalize();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return doc;
	}

	/**
	 * Chuyen doi file XML sang document XML
	 * 
	 * @param fileUrl Co kieu String la duong dan toi file XML
	 * @return org.w3c.dom.Document
	 */
	public static Document convertXmlFileToDocument(String fileUrl) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			File xmlFile = new File(fileUrl);
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return doc;
	}

	/**
	 * Chuyen doi file XML sang document XML
	 * 
	 * @param xmlFile Co kieu java.io.File la file XML
	 * @return org.w3c.dom.Document
	 */
	public static Document convertXmlFileToDocument(File xmlFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return doc;
	}

	/**
	 * Chuyen doi file XML sang document XML
	 * 
	 * @param in Co kieu java.io.InputStream la file XML
	 * @return org.w3c.dom.Document
	 */
	public static Document convertXmlFileToDocument(InputStream in) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(in);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return doc;
	}

	/**
	 * Chuyen doi file XML sang document XML
	 * 
	 * @param is co kieu org.xml.sax.InputSource la file XML
	 * @return org.w3c.dom.Document
	 */
	public static Document convertXmlFileToDocument(InputSource is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return doc;
	}

	/**
	 * Chuyen doi file XML sang chuoi XML
	 * 
	 * @param fileUrl co kieu String la duong dan toi file XML
	 * @return Chuoi XML
	 */
	public static String convertXmlFileToString(String fileUrl) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String output = null;
		try {
			File xmlFile = new File(fileUrl);
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);

			transformer = tf.newTransformer();
			// below code to remove XML declaration
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			output = writer.getBuffer().toString();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return output;
	}

	/**
	 * Chuyen doi file XML sang chuoi XML
	 * 
	 * @param xmlFile co kieu java.io.File la file XML
	 * @return Chuoi XML
	 */
	public static String convertXmlFileToString(File xmlFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String output = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);

			transformer = tf.newTransformer();
			// below code to remove XML declaration
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			output = writer.getBuffer().toString();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return output;
	}

	/**
	 * Chuyen doi file XML sang chuoi XML
	 * 
	 * @param in co kieu org.xml.sax.InputSource
	 * @return Chuoi XML
	 */
	public static String convertXmlFileToString(InputSource is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String output = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);

			transformer = tf.newTransformer();
			// below code to remove XML declaration
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			output = writer.getBuffer().toString();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return output;
	}

	/**
	 * Chuyen doi file XML sang chuoi XML
	 * 
	 * @param in co kieu java.io.InputStream
	 * @return Chuoi XML
	 */
	public static String convertXmlFileToString(InputStream in) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String output = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);

			transformer = tf.newTransformer();
			// below code to remove XML declaration
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			output = writer.getBuffer().toString();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return output;
	}

	/**
	 * Xoa mot node trong XML
	 * 
	 * @param doc     Co kieu org.w3c.dom.Document la doi tuong XML
	 * @param tagName Co kieu String la ten node can xoa
	 */
	public static void removeElementByTagName(Document doc, String tagName) throws Exception {
		try {
			// retrieve the element 'link'
			NodeList elements = (NodeList) doc.getElementsByTagNameNS("*", tagName);
			// remove the specific node
			for (int i = 0; i < elements.getLength(); i++) {
				elements.item(i).getParentNode().removeChild(elements.item(i));
			}
			// Normalize the DOM tree, puts all text nodes in the
			// full depth of the sub-tree underneath this node
			doc.normalize();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	/**
	 * Lay gia tri cua mot node trong XML
	 * 
	 * @param doc     Co kieu org.w3c.dom.Document la doi tuong XML
	 * @param tagName Co kieu String la ten node can lay gia tri
	 * @return Gia tri cua node can lay co kieu String
	 */
	public static String getTextContent(Document doc, String tagName) throws Exception {
		String val = "";
		try {
			Element element = (Element) doc.getElementsByTagNameNS("*", tagName).item(0);
			if (element != null)
				val = element.getTextContent();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return val;
	}

	public static Element getNodeByTagName(Document doc, String tagName) throws Exception {
		Element element = null;
		try {
			element = (Element) doc.getElementsByTagNameNS("*", tagName).item(0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return element;
	}

	/**
	 * Lay gia tri cua mot node trong XML
	 * 
	 * @param doc     Co kieu org.w3c.dom.Document la doi tuong XML
	 * @param tagName Co kieu String la ten node can lay gia tri
	 * @return Gia tri cua node can lay co kieu String
	 */
	public static String getNodeValue(Document doc, String tagName) throws Exception {
		String val = null;
		try {
			Element element = (Element) doc.getElementsByTagNameNS("*", tagName).item(0);
			if (element != null)
				val = element.getNodeValue();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
		return val;
	}

	/**
	 * Gan gia tri cho mot node trong XML
	 * 
	 * @param doc     Co kieu org.w3c.dom.Document la doi tuong XML
	 * @param tagName Co kieu String la ten node can gan gia tri
	 * @param Gia     tri can gan co kieu String
	 */
	public static void setTextContent(Document doc, String tagName, String val) throws Exception {
		try {
			Element element = (Element) doc.getElementsByTagName(tagName).item(0);
			if (element != null)
				element.setTextContent(val);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	/**
	 * Them mot node con trong node cha
	 * 
	 * @param doc           Co kieu org.w3c.dom.Document la doi tuong XML
	 * @param parentTagName Co kieu String la ten node can gan them node con
	 * @param childNodeName Co kieu String la ten node con
	 * @param value         Co kieu String la gia tri cua node con
	 */
	public static void appendChildNode(Document doc, String parentTagName, String childNodeName, String value)
			throws Exception {
		try {
			Element element = (Element) doc.getElementsByTagName(parentTagName).item(0);
			Element newE = doc.createElement(childNodeName);
			// newE.appendChild(doc.createTextNode(value));
			newE.setTextContent(value);
			element.appendChild(newE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	/**
	 * Ham lay gia tri node theo path : dau vao va Document
	 * 
	 * @param doc
	 * @param pathNode
	 * @return
	 */
	public static String getTextContentByPath(Document doc, String pathNode) {
		String sValue = "";
		try {
			Element element = null;
			for (String subPath : pathNode.split("/")) {
				if (element == null)
					element = (Element) doc.getElementsByTagNameNS("*", subPath).item(0);
				else
					element = (Element) element.getElementsByTagNameNS("*", subPath).item(0);
				if (element == null) {
					break;
				}
			}
			if (element != null)
				return element.getTextContent();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sValue;
	}

	/**
	 * Ham lay gia tri theo path: dau vao va Element
	 * 
	 * @param doc
	 * @param pathNode
	 * @return
	 */
	public static String getTextContentByPath(Element doc, String pathNode) {
		String sValue = "";
		try {
			Element element = doc;
			for (String subPath : pathNode.split("/")) {
				element = (Element) element.getElementsByTagNameNS("*", subPath).item(0);
				if (element == null) {
					break;
				}
			}
			if (element != null)
				return element.getTextContent();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sValue;
	}

	public static void saveXmlStringToFile(String xmlStr, String outputFile) throws Exception {
		OutputStream os = null;

		try {
			Document doc = convertStringToDocument(xmlStr, false);
			os = new FileOutputStream(outputFile);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform(new DOMSource(doc), new StreamResult(os));
		} catch (Exception e) {
			try {
				if (os != null)
					os.close();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
			System.out.println(e.getMessage());
			throw e;
		}
	}

	private static Logger lg = Logger.getLogger(XmlUtils.class);

	public static String convertObjToXML(Object object) throws Exception {
		String xml = null;
		try {
			Marshaller m = makeMarshaller(object.getClass());
			StringWriter sw = new StringWriter();
			m.marshal(object, sw);
			xml = sw.toString();
			lg.info("xml of the object: ");
			lg.info(xml);
			sw.close();
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
			throw e;
		}
		return xml;
	}

	private static Marshaller makeMarshaller(Class<? extends Object> objClss) throws Exception {
		JAXBContext context = JAXBContext.newInstance(objClss);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		return m;
	}
}
