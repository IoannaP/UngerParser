import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
  private Stack<OperationalTuple> operationalStack;
  private Stack<ProductionRule> derivationStack;
  private List<Stack<ProductionRule>> parsingForest;
  private Grammar grammar;
  private String word;

  public Parser(Grammar grammar, String word) {
    operationalStack = new Stack<>();
    derivationStack = new Stack<>();
    parsingForest = new ArrayList<>();

    this.grammar = grammar;
    this.word = word;
  }

  public List<Stack<ProductionRule>> parse() {
    tryAllRulesFor(grammar.getStartSymbol(), 0, word.length());
    return parsingForest;
  }

  private void tryAllRulesFor(char symbol, int position, int length) {
    List<ProductionRule> rulesForSymbol = grammar.getRulesForSymbol(symbol);
    for (int i = 0; i < rulesForSymbol.size(); i++) {
      tryRule(rulesForSymbol.get(i), position, length);
    }
  }

  private void tryRule(ProductionRule rule, int position, int length) {
    Goal goal = new Goal(rule, position, length);
    if (!isToBeAvoided(goal)) {
      operationalStack.push(new OperationalTuple(goal, -1, 0));
      derivationStack.push(rule);
      doTopOfStack();
      derivationStack.pop();
      operationalStack.pop();
    }
  }

  private void doTopOfStack() {
    OperationalTuple s = operationalStack.peek();
    Goal goal = s.getGoal();
    char nextRhsSymbol = goal.getRule().getRightHandSide().charAt(s.getRhsPosition() + 1);
    if (nextRhsSymbol == '$') {
      if (s.getInRec() == goal.getLength()) {
        doNextOnStack();
      }
    } else if (s.getInRec() <  goal.getLength() && nextRhsSymbol == word.charAt(goal.getPosition() + s.getInRec())) {
      s.incrementRhsPosition(1);
      s.incrementInRec(1);
      doTopOfStack();
      s.decrementRhsPosition(1);
      s.decrementInRec(1);
    } else if (grammar.isNonTerminal(nextRhsSymbol)) {
      tryAllLengthsFor(nextRhsSymbol, goal.getPosition() + s.getInRec(), goal.getLength() - s.getInRec());
    }
  }

  private void doNextOnStack() {
    OperationalTuple s = operationalStack.peek();
    operationalStack.pop();
    if (operationalStack.empty()) {
      parsingForest.add((Stack<ProductionRule>)derivationStack.clone());
    } else {
      OperationalTuple s1 = operationalStack.peek();
      s1.incrementRhsPosition(1);
      s1.incrementInRec(s.getGoal().getLength());
      doTopOfStack();
      s1.decrementInRec(s.getGoal().getLength());
      s1.decrementRhsPosition(1);
    }
    operationalStack.push(s);
  }

  private void tryAllLengthsFor(char nonTerminal, int position, int length) {
    for (int i = 0; i <= length; i++) {
      tryAllRulesFor(nonTerminal, position, i);
    }
  }

  private boolean isToBeAvoided(Goal goal) {
    for (int i = 0; i < operationalStack.size(); i++) {
      if (operationalStack.get(i).getGoal().equals(goal) == true) {
        return true;
      }
    }
    return false;
  }


}
