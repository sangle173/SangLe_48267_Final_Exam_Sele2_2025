package models;

public class TikiTestData {
    private String supplier;
    private int minPrice;
    private int maxPrice;
    private String category;
    
    public TikiTestData() {}
    
    public TikiTestData(String supplier, int minPrice, int maxPrice, String category) {
        this.supplier = supplier;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.category = category;
    }
    
    // Getters and Setters
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    
    public int getMinPrice() { return minPrice; }
    public void setMinPrice(int minPrice) { this.minPrice = minPrice; }
    
    public int getMaxPrice() { return maxPrice; }
    public void setMaxPrice(int maxPrice) { this.maxPrice = maxPrice; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
