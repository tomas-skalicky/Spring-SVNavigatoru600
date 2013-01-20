package com.svnavigatoru600.service.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.Text;
import nu.xom.ValidityException;

import org.apache.commons.lang3.StringEscapeUtils;

import com.svnavigatoru600.service.Configuration;

/**
 * Provides a set of convenient functions related to HTML.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class HtmlUtils {

    private HtmlUtils() {
    }

    /**
     * Crops the given <code>escapedHtml</code> text in a way that its length is not beyond the given upper
     * bound <code>maxLength</code>.
     * <p>
     * If the text is
     * <ul>
     * <li>a <i>plain text</i>, the function just does a simple crop (see {@link String#substring(int)
     * substring}).</li>
     * <li><i>not a well-formed</i> HTML text, the function unescapes the text, crops it (the same as by the
     * plain text option) and escapes the whole result again.</li>
     * <li>a <i>well-formed</i> HTML text, the function unescapes the text and crops it carefully so that the
     * cropping does not break text's well-formed-ness (all tags correctly closed). In terms of the length
     * limit, only contents of {@link Text} nodes are counted to the result's length. Finally, the function
     * escapes all CDATA.</li>
     * </ul>
     * 
     * @param escapedHtml
     *            <i>Escaped</i> HTML text
     */
    public static String cropEscaped(String escapedHtml, int maxLength) {
        String unescaped = StringEscapeUtils.unescapeHtml4(escapedHtml);
        String withRootElement = HtmlUtils.wrapWithRootElement(unescaped);
        try {
            Element root = HtmlUtils.getRoot(withRootElement);
            if (HtmlUtils.isLeaf(root)) {
                return HtmlUtils.cropPlainText(escapedHtml, maxLength);
            } else {
                return HtmlUtils.cropWellFormed(root, maxLength);
            }
        } catch (Exception e) {
            return HtmlUtils.cropBadFormed(unescaped, maxLength);
        }
    }

    /**
     * Encapsulates the given <code>html</code> text with a block element <code>div</code> to secure that the
     * text has exactly one root XML element.
     * <p>
     * NOTE: If this method was not been invoked before the {@link Builder#build(java.io.Reader) build}, the
     * XOM would throw a {@link ParsingException}.
     */
    private static String wrapWithRootElement(String html) {
        return "<div>" + html + "</div>";
    }

    /**
     * Gets the root element of the given <code>html</code> text.
     * <p>
     * NOTE: It is checked whether the HTML text is well-formed XML. If it is not, {@link ParsingException} is
     * thrown. Validity of the HTML text in terms of DOCTYPE and XML Schema is not checked.
     */
    private static Element getRoot(String html) throws ParsingException, ValidityException, IOException {
        // Builder(false) checks whether the text is well-formed XML document.
        // Builder(true) do the same, but moreover, it checks whether the XML meets requirements of DOCTYPE
        // DTD, or XML Schema.
        Document parsedXml = (new Builder(false)).build(new StringReader(html));
        return parsedXml.getRootElement();
    }

    /**
     * Indicates whether the given {@link Element} contains just a plain text. In other words, whether the
     * element is a leaf of the XML DOM.
     * 
     * @return <code>true</code> if the element contains just a plain text; otherwise <code>false</code>.
     */
    private static boolean isLeaf(Element element) {
        return (element.getChildCount() == 1) && (element.getChild(0) instanceof Text);
    }

    /**
     * Crops the given plain <code>text</code> in a way that its length is not beyond the given upper bound
     * <code>maxLength</code>.
     */
    private static String cropPlainText(String text, int maxLength) {
        return text.substring(0, Math.min(text.length(), maxLength));
    }

    /**
     * Crops the given <code>unescapedHtml</code> text in a way that its length is not beyond the given upper
     * bound <code>maxLength</code>. Then, <i>escapes</i> the cropped HTML text.
     * 
     * @param unescapedHtml
     *            <i>Unescaped</i> HTML text
     */
    private static String cropBadFormed(String unescapedHtml, int maxLength) {
        String croppedHtml = unescapedHtml.substring(0, Math.min(unescapedHtml.length(), maxLength));
        return StringEscapeUtils.escapeHtml4(croppedHtml);
    }

    /**
     * Crops an unescaped HTML text represented by an XML tree where the given {@link Element} is root. The
     * crop does not break well-formed-ness of the HTML text.
     * 
     * @param root
     *            Root of the whole DOM
     * @return Cropped well-formed HTML text which has escaped all CDATA.
     */
    private static String cropWellFormed(Element root, int maxLength) throws IOException {
        HtmlUtils.cropWellFormed(root, maxLength, 0);

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream(maxLength);
        BufferedOutputStream bufferedOut = new BufferedOutputStream(byteArrayOut);
        Serializer serializer = new Serializer(bufferedOut, Configuration.DEFAULT_ENCODING);
        serializer.write(root.getDocument());
        serializer.flush();
        // The serializer adds automatically the XML header and "amp;" behind all "&", hence let's undo these
        // changes.
        return byteArrayOut.toString(Configuration.DEFAULT_ENCODING)
                .replaceFirst("<[?]xml version=\"1.0\" encoding=\"UTF-8\"[?]>\r\n", "")
                .replaceAll("amp;", "");
    }

    /**
     * Performs the crop operation and stores changes back to the {@link Node} and its XML sub-tree.
     * <p>
     * Crops texts visible to the user and unescapes them. Unescapes localized attribute values (but does not
     * crop them). Does nothing with elements. If reaches the limit, detaches those nodes which have not been
     * processed yet.
     * 
     * @param node
     *            DOM node which is to be processed now.
     * @return Length of the whole composed text after the processing of the <code>node</code>.
     */
    private static int cropWellFormed(Node node, int maxLength, int currentLength) {
        if (node instanceof Text) {
            return HtmlUtils.processTextNode((Text) node, maxLength, currentLength);
        } else if (node instanceof Element) {
            HtmlUtils.processAttributeNodes((Element) node);
        }
        return HtmlUtils.processChildren(node, maxLength, currentLength);
    }

    /**
     * Crops an unescaped text of the given {@link Text} node and escapes the cropped text. Replaces the old
     * value with the result.
     * 
     * @param currentLength
     *            Length of the whole already-composed text visible to the user.
     * @return Length of the whole composed text after the processing of the <code>textNode</code>.
     */
    private static int processTextNode(Text textNode, int maxLength, int currentLength) {
        String oldUnescapedText = textNode.getValue();
        int newLength = Math.min(oldUnescapedText.length(), maxLength - currentLength);
        String newUnescapedText = oldUnescapedText.substring(0, newLength);
        textNode.setValue(StringEscapeUtils.escapeHtml4(newUnescapedText));
        currentLength += newUnescapedText.length();
        return currentLength;
    }

    /**
     * Processes attributes of the given {@link Element}. Values of some of them are to be escaped (see
     * {@link #processAttributeNode(Attribute) processAttributeNode}).
     */
    private static void processAttributeNodes(Element node) {
        int attributeCount = node.getAttributeCount();
        for (int attrIndex = 0; attrIndex < attributeCount; ++attrIndex) {
            HtmlUtils.processAttributeNode(node.getAttribute(attrIndex));
        }
    }

    /**
     * Escapes the value of the given {@link Attribute} if the attribute is of such a type which holds
     * localized texts.
     */
    private static void processAttributeNode(Attribute attributeNode) {
        String attributeName = attributeNode.getLocalName();
        if ("title".equals(attributeName) || "alt".equals(attributeName)) {
            attributeNode.setValue(StringEscapeUtils.escapeHtml4(attributeNode.getValue()));
        }
    }

    /**
     * Propagates the processing of the crop operation down to the children of the given {@link Node}. If the
     * limit <code>maxLength</code> is met during the processing of a certain node, all its right siblings
     * (those which have not been processed) are detached from the DOM.
     * 
     * @param currentLength
     *            Length of the whole already-composed text visible to the user.
     * @return Length of the whole composed text after the processing of <code>node</code>'s children.
     */
    private static int processChildren(Node node, int maxLength, int currentLength) {
        int childCount = node.getChildCount();
        int childIndex;
        for (childIndex = 0; (childIndex < childCount) && (currentLength < maxLength); ++childIndex) {
            Node child = node.getChild(childIndex);
            currentLength = HtmlUtils.cropWellFormed(child, maxLength, currentLength);
        }
        // Removes the rest of nodes.
        for (int childIndex2 = childCount - 1; childIndex2 >= childIndex; --childIndex2) {
            node.getChild(childIndex2).detach();
        }
        return currentLength;
    }
}
