package com.test.demo.rental.repository;

import com.test.demo.rental.model.Tool;
import com.test.demo.rental.model.ToolType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ToolRepository {

    private static final ToolType chainsawType = new ToolType("Chainsaw", BigDecimal.valueOf(1.49),
            true, false, true);
    private static final ToolType ladderType = new ToolType("Ladder", BigDecimal.valueOf(1.99),
            true, true, false);
    private static final ToolType jackhammerType = new ToolType("Jackhammer", BigDecimal.valueOf(2.99),
            true, false, false);

    private static final List<Tool> tools = Arrays.asList(
            new Tool("CHNS", chainsawType, "Stihl"),
            new Tool("LADW", ladderType, "Werner"),
            new Tool("JAKD", jackhammerType, "DeWalt"),
            new Tool("JAKR", jackhammerType, "Ridgid")
    );

    public Optional<Tool> getToolByCode(String toolCode) {
        return tools.stream()
                .filter(tool -> tool.getToolCode().equals(toolCode))
                .findFirst();
    }

}

