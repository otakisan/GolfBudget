package jp.cafe.golfbudget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by takashi on 2015/01/24.
 */
public class Budget implements Serializable {

    public List<BudgetSection> sections = new ArrayList<>();

    @Override public String toString() {
        String description = "GolfCompetitionBudgetReportTitle";

        for( BudgetSection budgetSection : this.sections){
            description.concat(System.lineSeparator() + budgetSection.toString());
        }

        return description;
    }
}
