
package com.mycompany.jenat2;


import com.mycompany.jenat2.implement.SemanticCrawlerImpl;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class jena {
    public static final String MY_FOAF_FILE = "https://dbpedia.org/resource/Neymar";
    public static void main(String[] args) {
        Model graph = ModelFactory.createDefaultModel();
        SemanticCrawlerImpl impl = new SemanticCrawlerImpl();
        impl.search(graph, MY_FOAF_FILE);
        graph.write(System.out,"N3");
    }   
}
