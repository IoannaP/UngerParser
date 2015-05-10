import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

  public static void main(String args[]) throws IOException {
    Grammar grammar = new Grammar(Paths.get("/home/ioana/grammar.txt"));

    File outputFile = new File("/home/ioana/output.txt");
    FileWriter fileWriter = new FileWriter(outputFile.getAbsoluteFile());
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);



    Files.lines(Paths.get("/home/ioana/word.txt")).forEach((line) -> {
      Parser ungerParser = new Parser(grammar, line);
      List<Stack<ProductionRule>> parsingForest = ungerParser.parse();
      if (parsingForest.size() == 0) {
        try {
          bufferedWriter.write("Couldn't parse word!");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      List<String> derivation;

      for (int i = 0; i < parsingForest.size(); i++) {
        derivation = new ArrayList<>();
        String lastString = String.valueOf(grammar.getStartSymbol());
        derivation.add(lastString);

        for (int j = 0; j < parsingForest.get(i).size(); j++) {
          try {
            bufferedWriter.write(parsingForest.get(i).get(j).getLeftHandSide() + "->" +
                parsingForest.get(i).get(j).getRightHandSide().substring(
                    0, parsingForest.get(i).get(j).getRightHandSide().length() - 1) + "\n");

          } catch (IOException e) {
            e.printStackTrace();
          }
          int length = lastString.length();
          for (int k = 0; k < length; k++) {
            if (grammar.isNonTerminal(lastString.charAt(k))) {
              String ending = lastString.substring(k + 1);

              if (k == 0) {
                lastString = "";
              } else {
                lastString = lastString.substring(0, k);
              }
              lastString = lastString.concat(parsingForest.get(i).get(j).getRightHandSide().substring(
                  0, parsingForest.get(i).get(j).getRightHandSide().length() - 1));

              lastString = lastString.concat(ending);
              derivation.add(lastString);
              break;
          }
        }
      }
      try {
        bufferedWriter.write(derivation.get(0));
      } catch (IOException e) {
        e.printStackTrace();
      }
      for (int j = 1; j < derivation.size(); j++) {
        try {
          bufferedWriter.write("=>" + derivation.get(j));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        bufferedWriter.write("\n\n");
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  });
    bufferedWriter.close();
  }

}
