import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderMethods {
	//Fake a USER_AGENT to mimic a real user
	private static final String USER_AGENT = 
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";	

	private List<String> links = new LinkedList<String>(); //all links found
	private Document htmlDoc; // the DOM / entire webpage.

	//Crawing the given url, finds all links, determines if crawl is successful or not 
	public boolean crawl(String url) {
		try {
			// Connect to url using jsoup and predefined user agent
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDoc = connection.get();
			this.htmlDoc = htmlDoc; //set the html document after establishing connection

			if(connection.response().statusCode() == 200) { // HTTP status code for OK
				System.out.println("\nCurrent page being visited: " + url);
				System.out.println("Title for page: " + htmlDoc.title());

			}

			// In case we dont get a html doc
			try {
				if(!connection.response().contentType().contains("text/html")) { 
					System.out.println("Error: Response not html. Content type is: " + connection.response().contentType());
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("e ..... " + e);
			}	
			//			if(!connection.response().contentType().contains("text/html")) { 
			//				System.out.println("Error: Response not html. Content type is: " + connection.response().contentType());
			//				return false;
			//			}



			//			List<String> excludeLinks = new LinkedList<>();
			//			excludeLinks.addAll(Arrays.asList("a[href*=olark.com]", "a[href*=twitter.com]", "a[href*=recitequran.com]", "a[href*=google.com]"));


			// All hyperlink references (excluding specified hrefs)
			Elements findAllLinks = htmlDoc.select("a[href]")
					.not("a[href*=olark.com]")
					.not("a[href*=twitter.com]")
					.not("a[href*=recitequran.com]")
					.not("a[href*=google.com]"); 


			for (Element element : findAllLinks) {			
				this.links.add(element.absUrl("href"));

			}
			return true; //crawl successful

		}
		//HTTP error
		catch(IOException e) {
			System.out.println("IO Exception: " + e);
			return false; //Crawl not successful
		}

	}

	/*
	 * In Spider class, after crawling the doc we then search for the search string
	 * in the given document
	 * 
	 * Only run if crawl is success.
	 * Returns true/false depending on if search string has occurred or not.
	 */
	public boolean searchForWord(String searchWord)	{

		System.out.println("Searching for the word " + searchWord + " in the current document");
		String bodyText = this.htmlDoc.body().text();

		return bodyText.toLowerCase().contains(searchWord.toLowerCase());


	}
	public List<String> getLinks() {
		return this.links;
	}
}
