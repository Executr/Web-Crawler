import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Creates instances of SpiderMethods for HTTP requests and for parsing the web page
 * 
 * Retrieve html doc (i.e. web page), collect all links and words, check if
 * search string has occurred, go to next href
 * 
 * Keep track of pages already visited Limit number of pages to visit
 */

public class Spider {

	// Fake a USER_AGENT to mimic a real user
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";
	// all links found
	private List<String> links = new LinkedList<String>();

	// Fields
	private static int limitPages = 100; // Limit how many pages to visit
	private List<String> visitPages = new LinkedList<String>(); // pages not yet visited
	private Set<String> visitedPages = new HashSet<String>(); // Pages already visited
	private Set<String> pagesWithSearchString = new HashSet<String>(); // Pages containing search string

	// Starting point of URL and which search string to use
	public void visit(String url, String searchString) {
		while (this.visitedPages.size() < limitPages) {
			String currentUrl;
			if (this.visitPages.isEmpty()) {
				// First url i.e. starting point. Only happens once.
				currentUrl = url;
				this.visitedPages.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			if (!currentUrl.isEmpty()) {
				Document document = crawlUrl(currentUrl);
				if (document != null) {
					System.out.println("\n*** Next url to crawl : -----------" + currentUrl);
					boolean wordFound = searchForWord(document, searchString); // Is search string found?
					if (wordFound) {
						// if search string is present in document, add to list
						System.out.println(
								"******* Search string: " + searchString + " found at: " + currentUrl + "*******");
						pagesWithSearchString.add(currentUrl);
					}

					this.visitPages.addAll(links); // Get all links and add it to visitPages
				}
			}
		}
		System.out.println("\n\n Done. \n Found a total of " + this.visitPages.size() + " pages.");
		System.out.println("Visited a total of : " + this.visitedPages.size() + " pages");
		System.out.println(
				"Found a total of : " + this.pagesWithSearchString.size() + " pages where search string occurs.");
	}

	// Returns next unvisited URL
	public String nextUrl() {
		String nextUrl;
		// Remove last visited page from visitPages list and get the next one while
		// there are more pages
		do {
			nextUrl = this.visitPages.remove(0);
		} while (this.visitedPages.contains(nextUrl));
		this.visitedPages.add(nextUrl); // nextUrl is now a page not yet visited
		return nextUrl;
	}

	// Crawing the given url, finds all links, and return a document object
	public Document crawlUrl(String url) {
		Document document = null;
		try {
			// Connect to url using jsoup and predefined user agent
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			document = connection.get();
			// HTTP status code for OK
			if (connection.response().statusCode() == 200) {

				System.out.println("\nCurrent page being visited: " + url);
				System.out.println("Title for page: " + document.title());
			}
			try {
				if (connection.response() != null && !connection.response().contentType().contains("text/html")) {
					System.out.println(
							"Error: Response not html. Content type is: " + connection.response().contentType());
					return null;
				}
			} catch (Exception e) {

			}
			// All hyperlink references (excluding specified hrefs)
			Elements findAllLinks = document.select("a[href]").not("a[href*=olark.com]").not("a[href*=twitter.com]")
					.not("a[href*=recitequran.com]").not("a[href*=google.com]");
			for (Element element : findAllLinks) {
				this.links.add(element.absUrl("href"));
			}

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		return document;
	}

	/*
	 * After crawling the doc we then search for the search string in the given document
	 * 
	 * Only run if crawl is success. Returns true/false depending on search
	 * string has occurred or not.
	 */
	public boolean searchForWord(Document document, String searchWord) {
		System.out.println("Searching for the word " + searchWord + " in the current document");
		String bodyText = document.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());

	}

	public List<String> getLinks() {
		return this.links;
	}
}
