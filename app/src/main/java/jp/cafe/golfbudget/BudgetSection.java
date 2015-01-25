package jp.cafe.golfbudget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by takashi on 2015/01/24.
 */
public class BudgetSection implements Serializable {
    public String name = "(no section)";
    List<BudgetItem> items = new ArrayList<>();

    @Override
    public String toString() {
        // TODO: ローカライズする
        String description = this.name;

        for ( BudgetItem budgetItem : this.items){
            description.concat(System.lineSeparator() + items.toString());
        }

        return description;
    }
}
