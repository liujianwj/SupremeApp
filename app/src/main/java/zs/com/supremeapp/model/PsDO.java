package zs.com.supremeapp.model;

public class PsDO extends BaseDO{

    private int count;
    private int allpages;
    private int thispage;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAllpages() {
        return allpages;
    }

    public void setAllpages(int allpages) {
        this.allpages = allpages;
    }

    public int getThispage() {
        return thispage;
    }

    public void setThispage(int thispage) {
        this.thispage = thispage;
    }
}
