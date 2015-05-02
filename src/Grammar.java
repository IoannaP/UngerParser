import java.util.ArrayList;
import java.util.List;

public class Grammar {
  private List<ProductionRule> productionRules;

  public Grammar() {
    productionRules = new ArrayList<>();
  }

  public void addRule(ProductionRule rule) {
    productionRules.add(rule);
  }

  public List<ProductionRule> getRulesForSymbol(char symbol) {
    List<ProductionRule> rulesForSymbol = new ArrayList<>();
    for (int i = 0; i < productionRules.size(); i++) {
      ProductionRule rule = productionRules.get(i);
      if (rule.getLeftHandSide() == symbol) {
        rulesForSymbol.add(rule);
      }
    }
    return rulesForSymbol;
  }
}
