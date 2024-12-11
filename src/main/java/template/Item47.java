package template;

public class Item47 {
    public short id;
    public short quantity;
    public byte category;
    
    public Item47(){}
    public Item47(Item47 Origin){
        this.id = Origin.id;
        this.quantity = Origin.quantity;
        this.category = Origin.category;
    }
}
