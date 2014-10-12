import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class Fast {
  private Point[] repo, ySorted;

  // private Set<Integer> visited;

  private Fast(int length) {
    repo = new Point[length];
    ySorted = new Point[length];
    // visited = new HashSet<Integer>();
  }

  private void output() {
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    Arrays.sort(ySorted);
    for (int i = 0; i < ySorted.length; ++i) {
      ySorted[i].draw();
      Arrays.sort(repo);
      Arrays.sort(repo, ySorted[i].SLOPE_ORDER);
      int counter = 2;
      for (int j = 0; j < repo.length - 1; ++j) {
        if (repo[j].slopeTo(ySorted[i]) == repo[j + 1].slopeTo(ySorted[i])
            && repo[j] != ySorted[i]) {
          ++counter;
        } else {
          counter = 2;
        }
        if (counter >= 4
            && (((j + 2) >= repo.length) 
                || (repo[j + 1].slopeTo(ySorted[i]) != repo[j + 2]
                .slopeTo(ySorted[i])))) {
          boolean flag = false;
          for (int k = counter - 2; k >= 0; --k) {
            if (repo[j + 1 - k].compareTo(ySorted[i]) < 0)
              flag = true;
          }
          if (flag == false) {
            ySorted[i].drawTo(repo[j + 1]);
            StdOut.print(ySorted[i] + " -> ");
            for (int k = counter - 2; k >= 0; --k) {
              if (k > 0)
                StdOut.print(repo[j + 1 - k] + " -> ");
              else
                StdOut.println(repo[j + 1 - k]);
            }
          }
        }
        // StdOut.println(counter);
      }
    }
  }

  public static void main(String[] args) {
    Fast f;
    List<String> input = new ArrayList<>();
    Path file = Paths.get(args[0]);
    try {
      input.addAll(Files.readAllLines(file, StandardCharsets.UTF_8));

    } catch (IOException e) {
    }
    f = new Fast(Integer.parseInt(input.get(0).trim()));
    int j = 1;
    for (int i = 1; i < input.size(); ++i) {
      if (!input.get(i).trim().equals("")) {
        String[] coord = input.get(i).trim().split("\\s+");
        Point p = new Point(Integer.parseInt(coord[0]),
            Integer.parseInt(coord[1]));
        f.repo[j - 1] = p;
        f.ySorted[j - 1] = p;
        ++j;
      }
    }
    // System.out.println("points size " + f.repo.length);
    f.output();
  }
}