package com.example.rule_engine_backend.repository;

import com.example.rule_engine_backend.model.Node;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends MongoRepository<Node, String> {
    // Additional query methods can be defined here if needed
}
