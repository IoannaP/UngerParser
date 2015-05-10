import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Grammar {
  private List<ProductionRule> productionRules;
  private char startSymbol;

  public Grammar(Path filePath) throws IOException {
    productionRules = new ArrayList<>();
    Files.lines(filePath, Charset.forName("UTF-8")).forEach(line -> addProductionRule(line));
  }

  public void addProductionRule(String line) {
    if (!line.contains("->")) {
      startSymbol = line.charAt(0);
    } else {
      ProductionRule rule = new ProductionRule(line.charAt(0), line.substring(3) + "$");
      productionRules.add(rule);
    }
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

  public char getStartSymbol() {
    return startSymbol;
  }

  public boolean isNonTerminal(char symbol) {
    return symbol >= 'A' && symbol <= 'Z';
  }
}
