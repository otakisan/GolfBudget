package jp.cafe.golfbudget;

import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takashi on 2015/01/24.
 */
public class BudgetCalculator {

    private class RateInfo {
        public RateInfo(){
            this("", "", 0.0);
        }
        public RateInfo(String sectionName, String itemName, double rate){
            this.sectionName = sectionName;
            this.itemName = itemName;
            this.rate = rate;
        }
        public String sectionName;
        public String itemName;
        public double rate;
    }

    public Budget calculate(BudgetParameter parameter) {

        // 係数を取得
        List<RateInfo> rates = this.rates(parameter);

        // 全体と乗算し、個別の金額を算出
        List<BudgetItem> budgetItems = this.calc(parameter, rates);

        // 予算セクションごとに分類し、予算データを構成
        Budget budget = this.compose(budgetItems);

        return budget;
    }

    private List<RateInfo> rates(BudgetParameter parameter) {

        final double rateCelemony = parameter.celemony ? 0.4 : 0.0;

        // 賞品
        double rateTotalAwards = 1.0 - rateCelemony;
        final double rateTrophy = rateTotalAwards * (parameter.trophy ? 0.1 : 0.0);
        rateTotalAwards -= rateTrophy;

        // 特別賞
        double rateSpecialAwards = rateTotalAwards * 0.4;
        final double rateLowest = rateSpecialAwards * (parameter.lowest ? 0.4 : 0.0);
        final double rateTotalLongest = (parameter.longest > 0 ? (rateSpecialAwards - rateLowest) * 0.5 : 0.0);
        final double rateTotalClosest = (parameter.closest > 0 ? rateSpecialAwards - rateLowest - rateTotalLongest : 0.0);
        // 誤差出るけど… 特別賞なしのときに比率がゼロになるように
        rateSpecialAwards = rateLowest + rateTotalLongest + rateTotalClosest;

        // 順位ごとの賞品
        double rateNormalAwards = rateTotalAwards - rateSpecialAwards;

        // 比率データ
        List<RateInfo> rates = new ArrayList<>();

        // 順位
        final double rateBooby = (parameter.booby ? rateNormalAwards / 15.0 : 0.0);
        rateNormalAwards -= rateBooby;
        double prizeRate = 1.0;

        if (parameter.golfers > 0) {

            for (int prizeOrder = 1; prizeOrder <= parameter.golfers; ++prizeOrder) {
                // ブービーを考慮しないと…

                RateInfo rateInfo = new RateInfo();
                rateInfo.sectionName = "Prize";
                if (!parameter.booby || prizeOrder != parameter.golfers - 1) {
                    prizeRate = (rateNormalAwards * 0.3);
                    rateNormalAwards = (rateNormalAwards - prizeRate);

                    rateInfo.itemName = String.format("Prize%d", prizeOrder);
                    rateInfo.rate = prizeRate;
                    rates.add(rateInfo);
                }
                else {
                    rateInfo.itemName = "Booby";
                    rateInfo.rate = rateBooby;
                    rates.add(rateInfo);
                }
            }

            // 残りは全て優勝者に追加する
            rates.get(0).rate += rateNormalAwards;
        }

        // 特別賞
        rates.add(new RateInfo("Special", "Lowest", rateLowest));

        // ドラコンとニアピンの下記の処理は汎用かできるはず
        // ドラコン
        if (parameter.longest > 0) {
            final double rateLongest = rateTotalLongest / ((double)parameter.longest);
            for (int longestOrder = 1; longestOrder <= parameter.longest; ++longestOrder) {
                rates.add(new RateInfo("Special", String.format("Longest%d", longestOrder), rateLongest));
            }
        }
        // ニアピン
        if (parameter.closest > 0) {
            final double rateClosest = rateTotalClosest / ((double)parameter.closest);
            for (int closestOrder = 1; closestOrder <= parameter.closest; ++ closestOrder) {
                rates.add(new RateInfo("Special", String.format("Closest%d", closestOrder), rateClosest));
            }
        }

        // トロフィー
        rates.add(new RateInfo("Trophy", "Trophy", rateTrophy));

        // 表彰式
        rates.add(new RateInfo("Ceremony", "Ceremony", rateCelemony));

        return rates;
    }

    private List<BudgetItem> calc(BudgetParameter parameter, List<RateInfo> rates) {

        List<BudgetItem> budgetItems = new ArrayList<>();
        budgetItems.add(new BudgetItem("General", "TotalAmount", 1.0, 0.0));

        double totalRate = 0.0;
        double totalAmount = 0.0;
        for (RateInfo rate : rates) {
            final double amount = rate.rate * ((double)parameter.budgetTotalAmount);
            budgetItems.add(new BudgetItem(rate.sectionName, rate.itemName, rate.rate, amount));

            totalRate += rate.rate;
            totalAmount += amount;
        }

        budgetItems.get(0).rate = totalRate;
        budgetItems.get(0).amount = totalAmount;

        return budgetItems;
    }

    private Budget compose(List<BudgetItem> budgetDetails) {

        Budget budget = new Budget();

        // セクション名が変化したら、セクションインスタンスを生成して、配列に足す、みたいにすれば汎用化できる
        String[] sectionNames = new String[]{"General", "Prize", "Special", "Trophy", "Ceremony"};
        for ( final String sectionNameInner : sectionNames) {

            Iterable<BudgetItem> eachSection = Iterables.filter(budgetDetails, new Predicate<BudgetItem>(){
                @Override
                public boolean apply(BudgetItem budgetItem){
                    return budgetItem.sectionName.equals(sectionNameInner) ;
                }
            });

//            var eachSection = budgetDetails.filter { (sectionName, itemName, rate, amount) -> Bool in
//                return sectionName == sectionNameInner
//            }

            BudgetSection bs = new BudgetSection();
            bs.name = sectionNameInner;
            for (BudgetItem item : eachSection) {
                BudgetItem bi = new BudgetItem();
                bi.sectionName = item.sectionName;
                bi.itemName = item.itemName;
                bi.amount = item.amount;
                bs.items.add(bi);
            }

            budget.sections.add(bs);
        }

        return budget;
    }
}
