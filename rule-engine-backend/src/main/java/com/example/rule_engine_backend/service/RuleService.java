package com.example.rule_engine_backend.service;

import com.example.rule_engine_backend.model.Node;
import com.example.rule_engine_backend.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

@Service
public class RuleService {

    @Autowired
    private NodeRepository nodeRepository; // Inject the repository

    // 1. Create Rule Function: Parses a rule string into an AST
    public Node createRule(String ruleString) {
        ruleString = ruleString.trim();
        if (ruleString.contains(" AND ")) {
            String[] parts = ruleString.split("(?i) AND ");
            return new Node("AND", createRule(parts[0].trim()), createRule(parts[1].trim()));
        } else if (ruleString.contains(" OR ")) {
            String[] parts = ruleString.split("(?i) OR ");
            return new Node("OR", createRule(parts[0].trim()), createRule(parts[1].trim()));
        }
        return new Node("operand", ruleString); // Single condition
    }

    // 2. Evaluate Rule Function: Evaluates the AST (rule) against provided data
    public boolean evaluateRule(Node node, Map<String, Object> data) throws ScriptException {
        if ("operand".equals(node.getType())) {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

            // Get the rule expression (e.g., "department == 'IT'")
            String expression = node.getValue();

            // Replace variables with values from the data map
            expression = replaceVariables(expression, data);

            // Log the expression being evaluated for debugging
            System.out.println("Evaluating expression: " + expression);

            // Evaluate the expression safely and return the result
            return (Boolean) engine.eval(expression);
        }

        // Evaluate logical operators (AND, OR)
        if ("AND".equals(node.getType())) {
            return evaluateRule(node.getLeft(), data) && evaluateRule(node.getRight(), data);
        } else if ("OR".equals(node.getType())) {
            return evaluateRule(node.getLeft(), data) || evaluateRule(node.getRight(), data);
        }

        return false; // If neither operand nor operator, return false
    }

    // Utility method for replacing variables in the rule expression
    private String replaceVariables(String expression, Map<String, Object> data) {
        // Ensure the equality operator is converted to '==' for comparison
        expression = expression.replaceAll("(?<![><!])=", "==");

        // Replace variables with their values from the data map
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value instanceof String) {
                // Ensure string values are quoted
                expression = expression.replace(key, "\"" + value + "\"");
            } else {
                expression = expression.replace(key, value.toString());
            }
        }

        // Debug logging for the final expression
        System.out.println("Final expression after variable replacement: " + expression);
        
        return expression;
    }

    // Save rule to the database
    public void saveRule(Node rule) {
        nodeRepository.save(rule); // This will save the node to the database
    }
}
