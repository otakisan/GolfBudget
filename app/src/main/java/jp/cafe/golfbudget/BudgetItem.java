package jp.cafe.golfbudget;

import java.io.Serializable;

/**
 * Created by takashi on 2015/01/24.
 */
public class BudgetItem implements Serializable{

    public String sectionName;
    public String itemName;
    public double rate;
    public double amount;

    public BudgetItem(){
        this("", "", 0.0, 0.0);
    }

    public BudgetItem(String sectionName, String itemName, double rate, double amount){
        this.sectionName = sectionName;
        this.itemName = itemName;
        this.rate = rate;
        this.amount = amount;
    }

    @Override
    public String toString() {
        // TODO: ローカライズして返却する(例：Prize1 -> 順位1)
        // TODO: 小数点以下の処理は暫定的に下記
        return this.itemName + ":" + (int)this.amount;
    }
}
