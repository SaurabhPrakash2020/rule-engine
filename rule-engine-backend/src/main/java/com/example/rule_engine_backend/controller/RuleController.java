package com.example.rule_engine_backend.controller;

import com.example.rule_engine_backend.model.Node;
import com.example.rule_engine_backend.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rule-engine")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/evaluate_rule")
    public boolean evaluateRule(@RequestBody Map<String, Object> request) {
        String ruleString = (String) request.get("ruleString");
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) request.get("data");

        Node rule = ruleService.createRule(ruleString);
        
        // Save the created rule to the database
        ruleService.saveRule(rule);

        try {
            return ruleService.evaluateRule(rule, data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
