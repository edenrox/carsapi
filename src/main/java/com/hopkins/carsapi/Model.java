package com.hopkins.carsapi;

public class Model {
    private final int id;
    private final int makeId;
    private final String name;
    private final ModelType type;
    
    public Model(int id, int makeId, String name, ModelType type) {
        this.id = id;
        this.makeId = makeId;
        this.name = name;
        this.type = type;
    }
    
    public int getId() {
        return id;
    }
    
    public int getMakeId() {
        return makeId;
    }
    
    public String getName() {
        return name;
    }
    
    public ModelType getType() {
        return type;
    }
}
