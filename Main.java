package ParticularTime;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

    public class Main {
        static Logger LOGGER;

        static {
            try (FileInputStream ins = new FileInputStream("C:\\\\Users\\\\konop\\loog.cfg")) { //полный путь до файла с конфигами
                LogManager.getLogManager().readConfiguration(ins);
                LOGGER = Logger.getLogger(Main.class.getName());
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }

        public static void main(String[] args) throws IOException {
            try {
                LOGGER.log(Level.INFO, "Задается местоположение файла с входными данными");
                System.out.println("Введите местоположение входного файла: "); // C:\\Users\\konop\\Downloads\\access.log
                Scanner scanner = new Scanner(System.in);
//            String strInputLocation = scanner.nextLine();
                BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\konop\\Downloads\\access.log"));//strInputLocation));

                LOGGER.log(Level.INFO, "Задается местоположение файла с выходными данными");
                System.out.println("Введите местоположение выходного файла: "); // fileResult.json
                //   String strOutputLocation = scanner.nextLine();
                FileWriter myWriter = new FileWriter("fileResult.json");//strOutputLocation);

                ArrayList<ParticularTime> arrTime = new ArrayList<>();
                HashMap<String, Integer> mapIP = new HashMap<>();

                LOGGER.log(Level.INFO, "Вводится время начала и конца периода");
                int dBeg, dEnd, MBeg, MEnd, yBeg, yEnd, hBeg, hEnd, mBeg, mEnd, sBeg, sEnd;
                System.out.println("Введите время начала и конца периода    ");
                String strBeg, strEnd;
                strBeg = scanner.nextLine();
                strEnd = scanner.nextLine();
                //      try{
                if(strBeg.length() < 18)
                    return;
                dBeg = Integer.parseInt(strBeg.substring(0, 2));
                MBeg = Integer.parseInt(strBeg.substring(3, 5));
                yBeg = Integer.parseInt(strBeg.substring(6, 10));
                hBeg = Integer.parseInt(strBeg.substring(11, 13));
                mBeg = Integer.parseInt(strBeg.substring(14, 16));
                sBeg = Integer.parseInt(strBeg.substring(17, 19));
                dEnd = Integer.parseInt(strEnd.substring(0, 2));
                MEnd = Integer.parseInt(strEnd.substring(3, 5));
                yEnd = Integer.parseInt(strEnd.substring(6, 10));
                hEnd = Integer.parseInt(strEnd.substring(11, 13));
                mEnd = Integer.parseInt(strEnd.substring(14, 16));
                sEnd = Integer.parseInt(strEnd.substring(17, 19));
                //      } catch (NumberFormatException e){
                //          LOGGER.log(Level.WARNING, "Ошибка с данными начала и конца временного отрезка", e);
                //     }
                System.out.println(dBeg + " " + MBeg + " " + yBeg + " " + hBeg + " " + mBeg + " " + sBeg);
                System.out.println(" && & && & & &");
                String line;
                LOGGER.log(Level.INFO, "Считываются данные в файле из заданного временного промежутка");
                int iterations = 0;
                while ((line = reader.readLine()) != null) {
                    if (iterations > 0 && iterations % 1000 == 0) {
                        LOGGER.log(Level.INFO, "Считаны данные на первых " + iterations + " строках");
                    }
                    String timeStr = "", IPStr = "", HTTPStr = "", requestStr = "0";
                    for (int i = 10; ; i++) {
                        if (line.charAt(i) == '[') {
                            timeStr = line.substring(i, i + 28);
                            break;
                        }
                    }
                    String substringDay = timeStr.substring(1, 3);
                    int day = Integer.parseInt(substringDay);
                    int month;
                    int numToAdd = 0; // если June, July, Sept скокращаются до четырех букв, то numToAdd = 1
                    switch (timeStr.substring(4, 7)) {
                        case "Jan":
                            month = 1;
                            break;
                        case "Feb":
                            month = 2;
                            break;
                        case "Mar":
                            month = 3;
                            break;
                        case "Apr":
                            month = 4;
                            break;
                        case "May":
                            month = 5;
                            break;
                        case "Jun":
                            month = 6;
                            numToAdd = 1;
                            break;
                        case "Jul":
                            month = 7;
                            numToAdd = 1;
                            break;
                        case "Aug":
                            month = 8;
                            break;
                        case "Sep":
                            month = 9;
                            numToAdd = 1;
                            break;
                        case "Oct":
                            month = 10;
                            break;
                        case "Nov":
                            month = 11;
                            break;
                        case "Dec":
                            month = 12;
                            break;
                        default:
                            month = 0;
                            break;
                    }
                    if(month == 0)
                        LOGGER.log(Level.WARNING, "Неправильный формат месяца");
                    String substringYear = timeStr.substring(8 + numToAdd, 12 + numToAdd);
                    int year = Integer.parseInt(substringYear);
                    String substringHours = timeStr.substring(13 + numToAdd, 15 + numToAdd);
                    int hours = Integer.parseInt(substringHours);
                    String substringMinutes = timeStr.substring(16 + numToAdd, 18 + numToAdd);
                    int minutes = Integer.parseInt(substringMinutes);
                    String substringSeconds = timeStr.substring(19 + numToAdd, 21 + numToAdd);
                    int seconds = Integer.parseInt(substringSeconds);

                    String substringMonths = (month < 10 ? ("0" + String.valueOf(month)) : String.valueOf(month));

                    String timeStrMy = substringDay + "." + substringMonths + "." + substringYear + "-" +
                            substringHours + "." + substringMinutes + "." + substringSeconds;
                    //    System.out.println(timeStrMy);
                    if((year < yBeg) || (year == yBeg && month < MBeg) || (year == yBeg && month == MBeg && day < dBeg) ||
                            (year == yBeg && month == MBeg && day == dBeg && hours < hBeg) ||
                            (year == yBeg && month == MBeg && day == dBeg && hours == hBeg && minutes < mBeg) ||
                            (year == yBeg && month == MBeg && day == dBeg && hours == hBeg && minutes == mBeg && seconds < sBeg)) {
                        continue;
                    }

                    else if((year > yEnd) || (year == yEnd && month > MEnd) || (year == yEnd && month == MEnd && day > dEnd) ||
                            (year == yEnd && month == MEnd && day == dEnd && hours > hEnd) ||
                            (year == yEnd && month == MEnd && day == dEnd && hours == hEnd && minutes > mEnd) ||
                            (year == yEnd && month == MEnd && day == dEnd && hours == hEnd && minutes == mEnd && seconds > sEnd)) {
                        break;
                    }
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i + 1) == ' ') {
                            IPStr = line.substring(0, i + 1);
                            int freq = 0;
                            if (mapIP.get(IPStr) != null) {
                                freq = mapIP.get(IPStr);
                            }
                            mapIP.put(IPStr, freq + 1);
                            break;
                        }
                    }
                    for (int i = 37; i < line.length(); i++) {
                        if (line.startsWith("HTTP", i)) {
                            HTTPStr = line.substring(i, i + 8);
                            break;
                        }
                    }
                    for (int i = 38; i < line.length(); i++) {
                        if (line.startsWith("\"", i)) {
                            requestStr = line.substring(i);
                            break;
                        }
                    }

                    if (arrTime.isEmpty() || !timeStrMy.equals(arrTime.getLast().time)) {
                        ParticularTime moment = new ParticularTime();
                        moment.time = timeStrMy;
                        moment.frequency = 1;
                        IPResults res = new IPResults();
                        res.IP_Number = IPStr;
                        res.IP_Frequency = 1;
                        res.HTTP_Version = HTTPStr;
                        res.IP_Requests.add(requestStr);
                        moment.listIP.add(res);
                        arrTime.add(moment);
                    } else {
                        ParticularTime moment = arrTime.getLast();
                        moment.frequency++;
                        boolean IP_inArray = false;
                        int it = -1;
                        for (int i = 0; i < moment.listIP.size(); i++) {
                            if (Objects.equals(moment.listIP.get(i).IP_Number, IPStr)) {
                                it = i;
                                IP_inArray = true;
                                break;
                            }
                        }
                        if (!IP_inArray) {
                            IPResults resForIP = new IPResults();
                            resForIP.IP_Number = IPStr;
                            resForIP.IP_Frequency = 1;
                            resForIP.HTTP_Version = HTTPStr;
                            resForIP.IP_Requests.add(requestStr);
                            moment.listIP.add(resForIP);
                        } else {
                            moment.listIP.get(it).IP_Frequency++;
                            moment.listIP.get(it).IP_Requests.add(requestStr);
                        }
                    }
                    iterations++;
                }
                LOGGER.log(Level.INFO, "Все данные из файла считаны");
                LOGGER.log(Level.INFO, "Находим IP, встречающийся наиболее часто");
                int mxAmount = 0;
                String mxStringIP = "";
                for (String i : mapIP.keySet()) {
                    if (mxAmount < mapIP.get(i)) {
                        mxAmount = mapIP.get(i);
                        mxStringIP = i;
                    }
                }
                JSONObject obj = new JSONObject();
                obj.put("The most frequent: ", mxStringIP);
                obj.put("Times: ", mxAmount);
                //myWriter.write("Наиболее часто встречающийся: " + mxStringIP + "  -  " + mxAmount + " раз \n\n");
                LOGGER.log(Level.INFO, "Производится вывод результатов");
                //myWriter.write("{\n");
                JSONArray arrOfJSON = new JSONArray();
                for (ParticularTime pTime : arrTime) {
                    //myWriter.write(pTime.time + "\n " + pTime.frequency + "\n\n ");
                    //myWriter.write("\t{\n");
                    JSONArray arrOfTime = new JSONArray();
                    for (IPResults ipRes : pTime.listIP) {
                        JSONArray arrOfParticularIP = new JSONArray();
                        int i = 0;
                        //myWriter.write("\t " + ipRes.IP_Number + "\n\t " + ipRes.IP_Frequency + "\n\t " + ipRes.HTTP_Version + '\n' + "\t\t{" + '\n');
                        for (String it : ipRes.IP_Requests) {
                            i++;
                            JSONObject objIP = new JSONObject();
                            objIP.put(Integer.toString(i), it);
                            arrOfParticularIP.put(objIP);
                            //myWriter.write("\t\t " + it + "\n");
                        }
                        JSONObject objTime = new JSONObject();
                        objTime.put("IP number:", ipRes.IP_Number);
                        objTime.put("IP frequency:", ipRes.IP_Frequency);
                        objTime.put("IP HTTP version:", ipRes.HTTP_Version);
                        objTime.put("Array of requests:", arrOfParticularIP);
                        arrOfTime.put(objTime);
                        // myWriter.write('\n');
                        //myWriter.write("\t\t}\n");
                    }
                    JSONObject objTime = new JSONObject();
                    objTime.put("Time: ", pTime.time);
                    objTime.put("Frequency", pTime.frequency);
                    objTime.put("List of IP:", arrOfTime);
                    arrOfJSON.put(objTime);
                    //myWriter.write("\t}\n");
                }
                obj.put("Result: ", arrOfJSON);
                //System.out.println(obj);
                myWriter.write(obj.toString());
                myWriter.close();
                reader.close();
                LOGGER.log(Level.INFO, "Работа завершена");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Ошибка на этом этапе", e);
            }
        }
    }


/*

06.03.2024-00.00.02
06.03.2024-04.01.30

*/

