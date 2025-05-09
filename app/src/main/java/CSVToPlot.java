import java.nio.file.*;
import java.util.Arrays;
import java.io.IOException;
import java.net.URISyntaxException;

public class CSVToPlot {

  private static String csvToPlot(String csv){
    String[] splitLine =csv.split("\n"); // Zeilen aufbrechen
    StringBuilder sb = new StringBuilder();

    double sum = 0;
    int count = 0;
    double mean = 0;
    String currYear = "2008";
    String currMonth = "04";

    for(int i=1; i<splitLine.length; i++){
      String[] split = splitLine[i].split(";"); // Datum = split[1], Temp = split[3]
      String year = split[1].substring(0,4); // Jahr filtern
      String month = split[1].substring(4,6); // Monat filtern
      double temp = Double.parseDouble(split[3]); // Temperatur rausfiltern
      if (temp > 50 || temp < -20) { // Sensorfehler filtern
        temp = 0;
      }

      /*Monatswechsel Erkennung:
      * Wenn der Monat gleich der currMonth Variable ist, wird die Temperatur aufsummiert und ein Counter zählt mit.
      * Sobald der Monat wechselt springt das Programm in die Else, baut den String für den Monat und resettet die WErte
      */
      if(currMonth.equals(month)){
        sum += temp;
        count += 1;
      }
      else {
        mean = sum / (count);
        sb.append(currYear + "/");
        sb.append(currMonth + " ");
        sb.append(mean + "\n");
        currMonth = month;
        currYear = year;
        sum = temp;
        count = 1;
      }
    }
    mean = sum / count;
    sb.append(currYear + "/");
    sb.append(currMonth + " ");
    sb.append(mean + "\n");
    System.out.println(sb + "\n");

    return String.format(sb.toString());
  }

  // Copy the following code snippet into the field 'Script' on 
  // https://hostcat.fhsu.edu/cdclark/static/apps/gnuplot/
  // Copy the content from `temperatures.txt` into the 'Data' field.
  /* 
  set title 'Temperaturentwicklung (Landshut Reithof)'
  set xlabel 'Monat [J/m]'
  set xdata time
  set timefmt "%Y/%m"
  set format x '%y/%m'
  set ylabel 'Durchschnittstemperatur monatlich'
  set autoscale
  f(x)=b*x+c
  fit f(x) 'data.txt' using 1:2 via b,c
  plot 'data.txt' using 1:2 w l title "Durchschnitt", f(x) w l title "Trend"
  */
    

  public static void main(String[] args) {

    // DON'T CHANGE

    try {
      String csv = Files.readString(
          Paths.get(
            ClassLoader.getSystemClassLoader()
            .getResource("produkt_tu_stunde_20080401_20231231_13710.txt")
            .toURI()));

      String plotData = csvToPlot(csv);
      Files.write(Paths.get("temperatures.txt"), Arrays.asList(plotData.split("\r\n")));
    } catch (IOException ioException){
      System.err.println(ioException.getMessage());
      System.exit(1);
    } catch (URISyntaxException ioException){
      System.err.println(ioException.getMessage());
      System.exit(1);
    }

  }

}
