public class Goal {
  private ProductionRule rule;
  private int position, length;

  public Goal(ProductionRule rule, int position, int length) {
    this.rule = rule;
    this.position = position;
    this.length = length;
  }

  public ProductionRule getRule() {
    return rule;
  }

  public int getPosition() {
    return position;
  }

  public int getLength() {
    return length;
  }
}
