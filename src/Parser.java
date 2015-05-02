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
    tryRulesFor('S', 0, word.length());
    return parsingForest;
  }

  private void tryRulesFor(char symbol, int position, int length) {
    List<ProductionRule> rulesForSymbol = grammar.getRulesForSymbol(symbol);
    for (int i = 0; i < rulesForSymbol.size(); i++) {
      tryRule(rulesForSymbol.get(i), position, length);
    }
  }

  private void tryRule(ProductionRule rule, int position, int length) {
    Goal goal = new Goal(rule, position, length);
    if (!isToBeAvoided(goal) && !knownGoalSucceeds(goal)) {
      operationalStack.push(new OperationalTuple(goal, -1, 0));
      startNewKnownGoal(goal);
      derivationStack.push(rule);
      doTopOfStack();
      derivationStack.pop();
      operationalStack.pop();
    }
  }

  private void doTopOfStack() {
    OperationalTuple operationalTop = operationalStack.peek();
    Goal goal = operationalTop.getGoal();
    char nextRhsSymbol = goal.getRule().getRightHandSide().charAt(operationalTop.getRhsPosition() + 1);
    if (nextRhsSymbol == '$') {
      if (operationalTop.getInRec() == goal.getLength()) {
        doNextOnStack();
      }
    } else if (operationalTop.getInRec() <  goal.getLength() && nextRhsSymbol == word.charAt(goal.getPosition() + operationalTop.getInRec())) {
      operationalTop.incrementRhsPosition(1);
      operationalTop.incrementInRec(1);
      doTopOfStack();
      operationalTop.decrementRhsPosition(1);
      operationalTop.decrementInRec(1);
    } else if (isNonTerminal(nextRhsSymbol)) {
      tryAllLengthsFor(nextRhsSymbol, goal.getPosition() + operationalTop.getInRec(), goal.getLength() - operationalTop.getInRec());
    }
  }

  private void doNextOnStack() {
    recordKnownParsing();
    OperationalTuple s = operationalStack.peek();
    operationalStack.pop();
    if (operationalStack.empty()) {
      parsingForest.add(derivationStack);
    } else {
      s.incrementRhsPosition(1);
      s.incrementInRec(s.getGoal().getLength());
      doTopOfStack();
      s.decrementInRec(s.getGoal().getLength());
      s.decrementRhsPosition(1);
    }
    operationalStack.push(s);
  }

  private void tryAllLengthsFor(char nonTerminal, int position, int length) {
    for (int i = 0; i <= length; i++) {
      tryRulesFor(nonTerminal, position, i);
    }
  }

  private boolean isToBeAvoided(Goal goal) {
    for (int i = 0; i < operationalStack.size(); i++) {
      if (operationalStack.get(i).getGoal() == goal) {
        return true;
      }
    }
    return false;
  }

  private void recordKnownParsing() {
    /* used for optimization */
  }

  private boolean knownGoalSucceeds(Goal goal) {
    /* used for optimization */
    return false;
  }

  private void startNewKnownGoal(Goal goal) {
    /* used for optimization */
  }

  private boolean isNonTerminal(char symbol) {
    return symbol >= 'A' && symbol <= 'Z';
  }
}
