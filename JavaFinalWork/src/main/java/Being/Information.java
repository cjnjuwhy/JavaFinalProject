package Being;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Information {

	public static final int SIZE = 15;
	public static final int MAX_MOVE = 3;
	public static final int MAX_ATTACK = 3;
	public static final int DELAY = 1500;
	
	static final SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("_MMdd_HHmm");//设置日期格式
	public static final String DATE_FOR_FILENAME = LOG_FORMAT.format(new Date());// new Date()为获取当前系统时间
	static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm");//设置日期格式
	
	static final String WORK_DIR = "";
	static final String FORMATION_DIR = "";
	public static final String LOG_DIR = "./";//"/Users/huanyu_wang/";
	
	public static final String FILE_TO_READ = "./BsttleLog_1231.txt";
	public static final String FILE_TO_WRITE = "";
	
	public static final String PIC_DIR = "file:./pictures/";//"file:/Users/huanyu_wang/Final/pictures/";
	public static final String BACKGROUND_PATH = PIC_DIR + "background.jpg";
	public static final String WARRIOR_PATH[] = {
			"",
			PIC_DIR + "1.jpg",
			PIC_DIR + "2.jpg",
			PIC_DIR + "3.jpg",
			PIC_DIR + "4.jpg",
			PIC_DIR + "5.jpg",
			PIC_DIR + "6.jpg",
			PIC_DIR + "7.jpg",
			PIC_DIR + "8.jpg",
			PIC_DIR + "9.jpg",
			PIC_DIR + "10.jpg",
			PIC_DIR + "11.jpg",
			PIC_DIR + "12.jpg",
			PIC_DIR + "13.jpg",
			PIC_DIR + "14.jpg",
			PIC_DIR + "15.jpg",
			PIC_DIR + "16.jpg"};
}
