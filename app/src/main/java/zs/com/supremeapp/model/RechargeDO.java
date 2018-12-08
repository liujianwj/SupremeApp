package zs.com.supremeapp.model;


public class RechargeDO extends BaseDO{
    private String out_trade_no;
    private String dream_order_id;
    private double total_fee;

    public String getDream_order_id() {
        return dream_order_id;
    }

    public void setDream_order_id(String dream_order_id) {
        this.dream_order_id = dream_order_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }
}
