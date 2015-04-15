import java.io.IOException;

//import javax.lang.model.util.Elements;
import java.sql.*;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JsoupTest {
	public static void main (String args[]) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost/test";
		String user = "root";
		String password = "";
		int j=0;
		java.sql.Connection connection = DriverManager.getConnection(url,user,password);
         Statement str = connection.createStatement();
		String current ="";
		ArrayList<String> sites = new ArrayList<String>();
		ArrayList<InteractiveSite> finish = new ArrayList<InteractiveSite>();
		/* ADD ALL SITES TO ARRAY*/
		sites.add("http://www.internet4classrooms.com/links_grades_kindergarten_12/astronomy_general.htm");
		sites.add("http://www.internet4classrooms.com/links_grades_kindergarten_12/astronomy_near_earth.htm");
		sites.add("http://www.internet4classrooms.com/links_grades_kindergarten_12/astronomy_solar_system.htm");
		sites.add("http://www.internet4classrooms.com/links_grades_kindergarten_12/astronomy_stars_galaxies.htm");
		sites.add("http://www.internet4classrooms.com/biology.htm");
		sites.add("http://www.internet4classrooms.com/chemistry.htm");
		sites.add("http://www.internet4classrooms.com/earthspace.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_animals.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_earth.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_force.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_general.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_machines.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_magnets.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_plants.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_sound.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_space.htm");
		sites.add("http://www.internet4classrooms.com/science_elem_weather.htm");
		sites.add("http://www.internet4classrooms.com/physics.htm");
		sites.add("http://www.internet4classrooms.com/assessment_assistance/physics_standards_mechanics.htm");
		sites.add("http://www.internet4classrooms.com/assessment_assistance/physics_standards_thermodynamics.htm");
		sites.add("http://www.internet4classrooms.com/assessment_assistance/physics_standards_waves_and_sound.htm");
		sites.add("http://www.internet4classrooms.com/assessment_assistance/physics_standards_light_and_optics.htm");
		sites.add("http://www.internet4classrooms.com/assessment_assistance/physics_standards_electricity_and_magnetism.htm");
		sites.add("http://www.internet4classrooms.com/assessment_assistance/physics_standards_nuclear_physics.htm");
		
        /* Grab all the Li tags in the site*/
		for(String a: sites){
			Document doc = Jsoup.connect(a).get();
			Elements links = doc.select("li");
			for(Element src : links){
				if(src.getElementsByTag("img").attr("src").equals("/images/icon-interactive.gif") ){
					InteractiveSite temp = new InteractiveSite(src.select("a[href]").text(),src.text(),doc.select("h2").first().text(),"K-12",src.getElementsByTag("img").attr("src"),src.getElementsByTag("a").attr("href"));
					finish.add(temp);

				}
			}
		}
		
		/*Make sure to delete any outer Lists records since they are already included*/
		
		current = finish.get(0).link;
		for(int i=1; i < finish.size();i++){
			if(finish.get(i).link.equals(current)){
				current=finish.get(i).link;
				finish.remove(--i);
			}
			else{
				current=finish.get(i).link;
			}
		}
		
		/* Print just to check how our data is formatted. */
		for(InteractiveSite a: finish){
			String create = "INSERT INTO EDUCATION VALUES("+ (++j)+",'"+a.getTitle().replaceAll("\"|'","#" )+"','"+a.getDescription().replaceAll("\"|'","#" )+
					"','"+a.getLink()+"','"+a.getImgSource()+"','"+a.getCategory()+"','"+'K'+"','"+a.getAuthor()+"','"
					+a.getContentType()+"',CURRENT_TIMESTAMP)";
		
			str.execute(create);
			a.print();
		}
		str.close();
		connection.close();
	}
}
