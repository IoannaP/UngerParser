public class ProductionRule {
  private char leftHandSide;
  private String rightHandSide;

  public ProductionRule(char leftHandSide, String rightHandSide) {
    this.leftHandSide = leftHandSide;
    this.rightHandSide = rightHandSide;
  }

  public char getLeftHandSide() {
    return leftHandSide;
  }

  public String getRightHandSide() {
    return rightHandSide;
  }
}
