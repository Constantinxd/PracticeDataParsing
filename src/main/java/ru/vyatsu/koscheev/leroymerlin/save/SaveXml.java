package ru.vyatsu.koscheev.leroymerlin.save;

import ru.vyatsu.koscheev.OnNewDataHandler;
import ru.vyatsu.koscheev.leroymerlin.model.Feedback;
import ru.vyatsu.koscheev.leroymerlin.model.Product;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class SaveXml implements OnNewDataHandler<Product> {
    private final String outputFile;
    private XMLStreamWriter writer;
    public int lineIndent;

    public SaveXml(String outputFile) {
        this.outputFile = outputFile;
        lineIndent = 3;

        try {
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            writer = output.createXMLStreamWriter(new FileOutputStream(outputFile), String.valueOf(StandardCharsets.UTF_8));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeStartDocument() {
        try {
            writer.writeStartDocument(String.valueOf(StandardCharsets.UTF_8), "1.0");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeEndDocument() {
        try {
            println(writer, 0); //0
            writer.writeEndDocument();

            writer.flush();
            writer.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeStartElement(String name, String attributeName, int lineIndent) {
        try {
            println(writer, lineIndent);
            writer.writeStartElement(name);
            writer.writeAttribute("name", attributeName);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeEndElement(int lineIndent) {
        try {
            println(writer, lineIndent);
            writer.writeEndElement();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void println(XMLStreamWriter writer, int recursion) throws XMLStreamException {
        writer.writeCharacters("\n" + "\t".repeat(recursion));
    }

    public void OnNewData(Object sender, Product product) {
        try {
            println(writer, lineIndent);
            writer.writeStartElement("product");
            //writeStartElement("product", lineIndent);

            println(writer, lineIndent + 1);
            writer.writeStartElement("name");
            //writeStartElement("name", lineIndent + 1);

            writer.writeCharacters(product.name);
            writer.writeEndElement();


            if (product.feedbacks.size() != 0) {
                //writeStartElement("feedbacks", lineIndent + 1);
                println(writer, lineIndent + 1);
                writer.writeStartElement("feedbacks");

                for (Feedback f : product.feedbacks) {
                    //writeStartElement("feedback", lineIndent + 2);
                    println(writer, lineIndent + 2);
                    writer.writeStartElement("feedback");

                    if (f.review != null) {
                        //writeStartElement("review", lineIndent + 3);
                        println(writer, lineIndent + 3);
                        writer.writeStartElement("review");

                        writer.writeCharacters(f.review);
                        writer.writeEndElement();
                    }

                    if (f.reviewProns != null) {
                        //writeStartElement("reviewProns", lineIndent + 3);
                        println(writer, lineIndent + 3);
                        writer.writeStartElement("reviewProns");
                        writer.writeCharacters(f.reviewProns);
                        writer.writeEndElement();
                    }

                    if (f.reviewCons != null) {
                        //writeStartElement("reviewCons", lineIndent + 3);
                        println(writer, lineIndent + 3);
                        writer.writeStartElement("reviewCons");

                        writer.writeCharacters(f.reviewCons);
                        writer.writeEndElement();
                    }

                    println(writer, lineIndent + 2); //3
                    writer.writeEndElement();
                }

                println(writer, lineIndent + 1); //2
                writer.writeEndElement();
            }


            println(writer, lineIndent); //1
            writer.writeEndElement();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
