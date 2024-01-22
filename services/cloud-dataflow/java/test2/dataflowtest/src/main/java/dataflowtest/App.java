package dataflowtest;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        String word ="<?xml version=\"1.0\" encoding=\"UTF-8\" ?><ProgramingList><Language id=\"001\" name=\"Java\">Javaは標準的なプログラミング言語です</Language><Language id=\"002\" name=\"Python\">Pythonは標準的なプログラミング言語です</Language></ProgramingList>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(word));
        Document doc = builder.parse(is);
    }
}
