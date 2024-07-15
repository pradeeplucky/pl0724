package com.test.demo.rental.model;

public class Tool {
    private String toolCode;
    private ToolType toolType;
    private String brand;

    public Tool(String toolCode, ToolType toolType, String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getBrand() {
        return brand;
    }
    public ToolType getToolType() {
        return toolType;
    }
}