package net.iceedge.catalogs.icd.panel;

public class ICDTabQuantity implements Comparable<ICDTabQuantity>
{
    private float size;
    private int tabCount;
    
    public ICDTabQuantity(final float size, final int tabCount) {
        this.size = size;
        this.tabCount = tabCount;
    }
    
    public float getSize() {
        return this.size;
    }
    
    public void setSize(final float size) {
        this.size = size;
    }
    
    public int getTabCount() {
        return this.tabCount;
    }
    
    public void setTabCount(final int tabCount) {
        this.tabCount = tabCount;
    }
    
    @Override
    public int compareTo(final ICDTabQuantity icdTabQuantity) {
        return (int)(this.size - icdTabQuantity.size);
    }
    
    @Override
    public String toString() {
        return "Length: " + this.size + " Requires " + this.tabCount + " Tabs";
    }
}
