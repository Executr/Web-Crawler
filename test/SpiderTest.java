import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpiderTest {

	private Spider spider;

	@BeforeEach
	void setUp() throws Exception {
		spider= new Spider();
	}

	@Test
	void testVisit() {
		String url = "https://www.version2.dk/";
		String searcString = "software";
		spider.visit(url, searcString);		
	}

	//	@Test
	//	void testNextUrl() {
	//		String nextUrl=spider.nextUrl();
	//		assertNotNull(nextUrl);
	//	}

	@Test
	void testCrawlUrl() {
		String url = "https://www.version2.dk/";
		Document document = spider.crawlUrl(url);
		assertNotNull(document);
	}

	@Test
	void testSearchForWord() {
		String url = "https://www.version2.dk/";
		Document document = spider.crawlUrl(url);
		String searcString = "software";
		boolean isFound = spider.searchForWord(document, searcString);
		assertTrue(isFound);
	}

	@Test
	void testGetLinks() {
		String url = "https://www.version2.dk/";
		Document document = spider.crawlUrl(url);
		String searcString = "software";
		spider.searchForWord(document, searcString);
		List<String> links = spider.getLinks();
		assertNotNull(links);
	}

	@AfterEach
	void tearDown() throws Exception {
		spider = null;
	}
}