package com.ifpb.model;

import org.neo4j.graphdb.RelationshipType;

public enum Relacionamentos implements RelationshipType {
    
    AMIGO, SEGUE, PUBLICA;
    
}
