import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TM {
	static DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy, hh:mm:ss a");
	
	public static void main(String args[]) {
		try {
			switch(args[0])
			{
			case "start":
				start(args[1]);
				break;
			case "stop":
				stop(args[1]);
				break;
			case "describe":
            if(args.length == 4){
				   describe(args[1], args[2], args[3]);
            }
            else {
               describe(args[1], args[2]);
            }
				break;
			case "summary":
				if(args.length == 1) {
					summary();
				}
				else {
					summary(args[1]);
				}
				break;
			case "size":
				size(args[1], args[2]);
				
			default:
				System.err.println("Enter a valid input: TM <Command> <\"TaskName\"> <\"Input\">");
				System.exit(1);
			}
		}
		catch(Exception e) {
			System.err.println("Enter a valid input: TM <Command> <\"TaskName\"> <\"Input\">");
			System.exit(1);
		}
	}
	
	public static void start(String name) throws IOException {
		Log.put(name);
	}
	
	public static void stop(String name) throws ClassNotFoundException, IOException {
		LogFile tempLogFile = Log.get(name);
		Date date = new Date();
		
		tempLogFile.stopTime = System.currentTimeMillis();
		tempLogFile.stopDate = dateFormat.format(date);
		
		tempLogFile.totalTime = tempLogFile.stopTime - tempLogFile.startTime;
		
		Log.put(name, tempLogFile);
	}
	
	public static void describe(String name, String data) throws ClassNotFoundException, IOException {
		LogFile tempLogFile = Log.get(name);
		
      if(tempLogFile.description.equals("No current description.")){
         tempLogFile.description = data;
      }
      else {
         tempLogFile.description = tempLogFile.description + "\t\t\n     " +data;
      }
		
		Log.put(name, tempLogFile);
	}
   
   public static void describe(String name, String data, String size) throws ClassNotFoundException, IOException {
      describe(name, data);
      size(name, size);
	}
	
	public static void summary(String name) {
		LogFile tempLogFile = Log.get(name);
		long totalTime;
		
		System.out.println(tempLogFile.toString());
		
		if (tempLogFile.totalTime == 0) {
			totalTime = System.currentTimeMillis() - tempLogFile.startTime;
		}
		else {
			totalTime = tempLogFile.totalTime;
		}
		
		System.out.println("\tTotal time: " + readableTime(totalTime));
		System.out.println();
	}
	
	public static void summary() {
		HashMap<String, LogFile> tempLogMap = null;
		long totalTime = 0;
		
		try {
			tempLogMap = Log.read();
		}
		catch (Exception e){
			System.err.println("No Tasks Found.");
			System.exit(1);
		}
		
		for(Map.Entry<String, LogFile> entry : tempLogMap.entrySet()) {
			String currentLogFile = entry.getKey();
			LogFile tempLogFile = tempLogMap.get(currentLogFile);
			summary(currentLogFile);
			
			if(tempLogFile.stopTime == 0) {
				totalTime += System.currentTimeMillis() - tempLogFile.startTime;
			}
			else {
				totalTime = tempLogFile.totalTime;
			}
		}
		
		int numberOfTasks = tempLogMap.size();
		
		System.out.println("There is " + numberOfTasks + " logged tasks.");
		System.out.println("Total Task Time: " + readableTime(totalTime));
	}
	
	public static void size(String name, String data) throws ClassNotFoundException, IOException {
		LogFile tempLogFile = Log.get(name);
		
		tempLogFile.size = data;
		
		Log.put(name, tempLogFile);
	}
	
	public static String readableTime(long time) {
		long different = time;
		
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli *24;
		
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
		
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
		
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
		
		long elapsedSeconds = different / secondsInMilli;
		different = different % secondsInMilli;
		
		return "" + elapsedDays + "D, " + elapsedHours + "H, "
				  + elapsedMinutes + "M, " + elapsedSeconds + "S";
	}
}

class LogFile implements java.io.Serializable{
	DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy, hh:mm:ss a");
	static final long serialVersionUID = 1L;
	
	String name;
	long startTime;
	long stopTime = 0;
	long totalTime = 0;
	String size = "No declared size.";
	String startDate;
	String stopDate = "Ongoing.";
	String description = "No current description.";
	
	
	public LogFile(String name){
		Date date = new Date();
		
		this.name = name;
		this.startTime = System.currentTimeMillis();
		this.startDate = dateFormat.format(date);
	}
	
	public String toString() {
		String status;
		
		if (stopTime == 0) {status = "Active";}
		else {status = "Done";}
		
		return name + " <" + status + ">: " + size + "\n" + 
			   "\tDescription: " + description + "\n" +
			   "\tStart Time: " + startDate + "\n" +
			   "\tStop Time: " + stopDate;
	}
}

class Log{
	public static void put(String name) throws IOException {
		LogFile tempLogFile = new LogFile(name);
		HashMap<String, LogFile> tempLogMap;
		
		try {
			tempLogMap = read();
		} 
		catch(Exception e) {
			tempLogMap = new HashMap<>();
		}
		
		tempLogMap.put(name, tempLogFile);
		write(tempLogMap);
	}
	
	public static void put(String name, LogFile tempLogFile) throws IOException, ClassNotFoundException {
		HashMap<String, LogFile> tempLogMap = read();
		
		tempLogMap.put(name, tempLogFile);
		write(tempLogMap);
	}
	
	public static LogFile get(String name) {
		try {
			HashMap<String, LogFile> tempLogMap = read();
			
			if(!tempLogMap.containsKey(name)) {
				System.err.println("Task not found.");
				System.exit(1);
			}
			
			return tempLogMap.get(name);
		} 
		catch(Exception e) {
			System.err.println("Task not found.");
			System.exit(1);
		}
		
		return null;
	}
	
	public static void write(HashMap<String, LogFile> logMap) throws IOException {
		FileOutputStream fos = new FileOutputStream("Log.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(logMap);
		oos.close();
		fos.close();
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, LogFile> read() throws IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream("Log.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		HashMap<String, LogFile> tempLogFile = (HashMap<String, LogFile>) ois.readObject();
		ois.close();
		fis.close();
		
		return tempLogFile;
	}
}