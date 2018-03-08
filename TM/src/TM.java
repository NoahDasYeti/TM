import java.util.Set;

public class TM {
	public static ITMModel tmModel = new TMModel();
	public static boolean status = true;
	
	public static void main(String args[]) {
		
		if(args.length == 0) { help(); System.exit(1); }
		
		switch(args[0]) {
		
		case "start"    :	 start(args); break;
		case "stop"     :	  stop(args); break;
		case "size"     :	  size(args); break;
		case "rename"   :	rename(args); break;
		case "delete"   :	delete(args); break;
		case "describe" : describe(args); break;
		case "summary"  :  summary(args); break;
		
		default : help();
		}
	}
	
	private static void start(String data[]) {
		
		status = tmModel.startTask(data[1]);
		if(status = false) { error(); }
	}
	
	private static void stop(String data[]) {
		
		status = tmModel.stopTask(data[1]);
		if(status = false) { error(); }
	}

	private static void size(String data[]) {
		
		status = tmModel.sizeTask(data[1], data[2]);
		if(status = false) { error(); }
	}

	private static void rename(String data[]) {
		
		status = tmModel.renameTask(data[1], data[2]);
		if(status = false) { error(); }
	}

	private static void delete(String data[]) {
		
		status = tmModel.deleteTask(data[1]);
		if(status = false) { error(); }
	}

	private static void describe(String data[]) {
		
		if(data.length > 3) {
			
			status = tmModel.describeTask(data[1], data[2]);
			if(status = false) { error(); }
			
			status = true;
			
			status = tmModel.sizeTask(data[1], data[3]);
			if(status = false) { error(); }
		}
		else {
			
			status = tmModel.describeTask(data[1], data[2]);
			if(status = false) { error(); }
		}
	}
	
	public static void summary(String data[]) {
		
		if(data.length == 1) {
			
			summary();
		}
		else if(data.length > 1) {
			
			summary(data[1]);
		}
		else {
			
			error();
		}
	}
	
	public static void summary(String name) {
		
		String size = tmModel.taskSize(name);
		String description = tmModel.taskDescription(name);
		String elapsedTime = readableTime("" + tmModel.taskElapsedTime(name));
		
		System.out.println(name + ":\n" + "\tTask Size: " + size + 
						   "\n\tTask Description: " + description +
						   "\n\tTime Taken: " + elapsedTime + "\n\n");
	}
	
	public static void summary() {
		for(String name : tmModel.taskNames()) {
			summary(name);
		}
		
		for(String size : tmModel.taskSizes()) {
			Set<String> tempSet = tmModel.taskNamesForSize(size);
			
			if(tempSet.size() >= 2) {
				System.out.println("Size: " + size 
									+ "\n\tMinimum Time: " + readableTime(tmModel.minTimeForSize(size))
									+ "\n\tMaximum Time: " + readableTime(tmModel.maxTimeForSize(size))
									+ "\n\tAverage Time: " + readableTime(tmModel.avgTimeForSize(size))
									+ "\n");
			}
		}
	}
	
	private static void help(){
		
		System.out.println();
	}
	
	private static void error() {
		
		System.err.println("Error.");
	}
	
	private static String readableTime(String time) {
		long different = Long.parseLong(time, 10);
		
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