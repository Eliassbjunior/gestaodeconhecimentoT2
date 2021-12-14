/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jenat2.crawler;

import org.apache.jena.rdf.model.Model;

/**
 *
 * @author elias
 */
public interface SemanticCrawler {

    public void search(Model graph, String resourceURI);
    
}
