import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.DateFormat;
import java.applet.Applet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    int x = 480;
    int y = 420;




    public void paint(Graphics g) {
        //get the time and convert it to a date
        //Calendar cal = Calendar.getInstance();
//        Date date = cal.getTime();
//        String s = "hh:mm:ss a";
//        SimpleDateFormat sff = new SimpleDateFormat(s);
        //format it and display it
//       DateFormat dateFormatter =
//                DateFormat.getTimeInstance();
        // g.drawString(new SimpleDateFormat.parseFormat(sff),5,60);
        Date date = new Date();
        Font f = new Font("Verdana", Font.BOLD, 36);
        g.setFont(f);
        g.setColor(Color.white);
        SimpleDateFormat sff = new SimpleDateFormat("HH:mm:ss a");
        g.drawString(sff.format(date),x,y);


    }
}
