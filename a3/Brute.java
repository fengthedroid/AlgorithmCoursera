import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class Brute {

  private List<Point> repo;

  private Brute() {
    repo = new ArrayList<Point>();
  }

  private void addPoint(int x, int y) {
    repo.add(new Point(x, y));
  }

  private int size() {
    return repo.size();
  }

  private void output() {
    Collections.sort(repo);
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (int i = 0; i < repo.size(); ++i) {
      repo.get(i).draw();
      for (int j = i + 1; j < repo.size(); ++j) {
        for (int h = j + 1; h < repo.size(); ++h) {
          for (int k = h + 1; k < repo.size(); ++k) {
            if (repo.get(i).slopeTo(repo.get(j)) == repo.get(i).slopeTo(
                repo.get(j))
                && repo.get(i).slopeTo(repo.get(j)) == repo.get(i).slopeTo(
                    repo.get(h))
                && repo.get(i).slopeTo(repo.get(j)) == repo.get(i).slopeTo(
                    repo.get(k))) {
              StdOut.println(repo.get(i) + " -> " + repo.get(j) + " -> "
                  + repo.get(h) + " -> " + repo.get(k));
              // StdOut.println(repo.get(i).slopeTo(repo.get(j))+" "+repo.get(i).slopeTo(repo.get(h))+" "+repo.get(i).slopeTo(repo.get(k)));
              repo.get(i).drawTo(repo.get(k));
            }

          }
        }
      }
    }
  }

  public static void main(String[] args) {
    Brute b = new Brute();
    Path file = Paths.get(args[0]);
    try {
      List<String> input = new ArrayList<>(Files.readAllLines(file,
          StandardCharsets.UTF_8));
      for (int i = 1; i < input.size(); ++i) {
        String[] coord = input.get(i).trim().split("\\s+");
        b.addPoint(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
      }
    } catch (IOException e) {
    }
    System.out.println("points size " + b.size());
    b.output();
  }
}