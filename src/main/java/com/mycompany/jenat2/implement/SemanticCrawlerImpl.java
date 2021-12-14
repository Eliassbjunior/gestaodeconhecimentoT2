/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jenat2.implement;

import com.mycompany.jenat2.crawler.SemanticCrawler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import static org.apache.jena.enhanced.BuiltinPersonalities.model;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.OWL;


public class SemanticCrawlerImpl implements SemanticCrawler {
    CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
    List<String> UrisVisitados = new ArrayList<>();
    
    @Override
    public void search(Model graph, String resourceURI) {
        
        UrisVisitados.add(resourceURI);
        
        Model model = ModelFactory.createDefaultModel();
        model.read(resourceURI);
        
        StmtIterator triplas = model.listStatements(model.createResource(resourceURI), (Property) null,(RDFNode) null);
        graph.add(triplas);
        
        StmtIterator sameAs = model.listStatements((Resource)null, OWL.sameAs, (RDFNode)null);
        
        if(sameAs.hasNext()){
            for(Statement uma_tripla : sameAs.toList()) {
                Resource sujeito = uma_tripla.getSubject();
		Resource objeto = (Resource) (uma_tripla.getObject());
                
                if(sujeito.isAnon()){
                    
                    if(objeto.isAnon()){
                       searchnoEmBranco(graph,model,objeto);
                    }else{
                        if(resourceURI.equals(objeto.getURI())){
                            searchnoEmBranco(graph,model,sujeito);
                        }
                    }
                }else{
                    if(objeto.isAnon()){
                        if(resourceURI.equals(sujeito.getURI())){
                            searchnoEmBranco(graph,model,objeto);
                        }else{
                            if(sujeito.isAnon()){
                                searchnoEmBranco(graph,model,sujeito);
                            }
                        }
                    }else{
                        if(resourceURI.equals(sujeito.getURI())){
                            if(enc.canEncode(objeto.getURI())){
                                if(UrisVisitados.contains(objeto.getURI())){
                                    //uri já foi visitado
                                }else{
                                    try{
                                        search(graph, objeto.getURI());
                                    }catch(Exception e){
                                        System.out.println("Erro");
                                    }
                                    
                                }
                            }
                        }
                    }
                } 
            }            
        }
    }
    
    public void searchnoEmBranco(Model model1, Model model2, Resource noEmBranco){
        List<Statement> triplas = todos_NosEmBranco(noEmBranco,model2);
        
        model1.add(triplas);
        
        for(Statement tripla : triplas){
            if(tripla.getObject().isAnon()){
                Resource objeto = (Resource) tripla.getObject();
                searchnoEmBranco(model1,model2,objeto);
            }
        }
    }
    
    public List<Statement> todos_NosEmBranco(Resource no, Model model){
        List<Statement> triplas_NoEmBranco = new ArrayList<>();
        StmtIterator triplas = model.listStatements();
        
        while(triplas.hasNext()){
            Statement tripla = triplas.nextStatement();
            Resource sujeito = tripla.getSubject();
            if(no.equals(sujeito)){
                triplas_NoEmBranco.add(tripla);
            }
        }
        
        return triplas_NoEmBranco;
    }
}
