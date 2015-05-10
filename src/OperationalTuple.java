public class OperationalTuple {
  private Goal goal;
  private int rhsPosition;
  private int inRec;

  public OperationalTuple(Goal goal, int rhsPosition, int inRec) {
    this.goal = goal;
    this.rhsPosition = rhsPosition;
    this.inRec = inRec;
  }

  public Goal getGoal() {
    return goal;
  }

  public int getRhsPosition() {
    return rhsPosition;
  }

  public int getInRec() {
    return inRec;
  }

  public void incrementRhsPosition(int value) {
    rhsPosition += value;
  }

  public void incrementInRec(int value) {
    inRec += value;
  }

  public void decrementRhsPosition(int value) {
    rhsPosition -= value;
  }

  public void decrementInRec(int value) {
    inRec -= value;
  }
}
