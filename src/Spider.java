import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
/**
 * Creates instances of SpiderMethods for HTTP requests and for parsing the web page
 * 
 * Retrieve html doc (i.e. web page),
 * collect all links and words,
 * check if search string has occurred,
 * go to next href
 * 
 * Keep track of pages already visited
 * Limit number of pages to visit
 */

public class Spider {
	//Fields
	private static int limitPages = 100; //Limit how many pages to visit
	private List<String> visitPages = new LinkedList<String>(); //pages not yet visited
	private Set<String> visitedPages = new HashSet<String>(); // Pages already visited
	private Set<String> pagesWithSearchString = new HashSet<String>(); //Pages containing search string

	// Starting point of URL and which search string to use
	public void visit(String url, String searchString) {
		while(this.visitedPages.size() < limitPages) { 

			String currentUrl;
			SpiderMethods spidey = new SpiderMethods();

			if(this.visitPages.isEmpty()) {
				currentUrl = url; //First url i.e. starting point.
				this.visitedPages.add(url);
			}
			else {
				currentUrl = this.nextUrl();
			}


			if(!currentUrl.isEmpty())
				System.out.println("\n*** Next url to crawl : -----------" + currentUrl);


			if(!currentUrl.isEmpty() && spidey.crawl(currentUrl) == true) {

				boolean wordFound = spidey.searchForWord(searchString); //Is search string found? 
				if(wordFound) { // if search string is present in document, add to list
					System.out.println("******* Search string: " + searchString  + " found at: " + currentUrl + "*******");
					pagesWithSearchString.add(currentUrl);
				}

				this.visitPages.addAll(spidey.getLinks()); //Get all links and add it to visitPages
			}
		}
		System.out.println("\n\n Done. \n Found a total of " + this.visitPages.size() + " pages.");
		System.out.println("Visited a total of : " + this.visitedPages.size() + " pages");
		System.out.println("Found a total of : " + this.pagesWithSearchString.size() + " pages where search string occurs.");


		//    System.out.println(this.visitedPages);
		//		System.out.println(this.pagesWithSearchString);
	}


	//Returns next unvisited URL
	public String nextUrl() {
		String nextUrl;

		// Remove last visited page from visitPages list and get the next one while there are more pages
		do {
			nextUrl = this.visitPages.remove(0);
		} while(this.visitedPages.contains(nextUrl));


		this.visitedPages.add(nextUrl); //nextUrl is now a page not yet visited 
		return nextUrl;
	}
}
