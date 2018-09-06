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
		String url = "http://www.qtafsir.com/";
		String searchWord = "Yusuf";
		spider.visit(url, searchWord);		
	}

	//	@Test
	//	void testNextUrl() {
	//		String nextUrl=spider.nextUrl();
	//		assertNotNull(nextUrl);
	//	}

	@Test
	void testCrawlUrl() {
		String url = "http://www.qtafsir.com/";
		Document document = spider.crawlUrl(url);
		assertNotNull(document);
	}

	@Test
	void testSearchForWord() {
		String url = "http://www.qtafsir.com/";
		Document document = spider.crawlUrl(url);
		String searchWord = "Yusuf";
		boolean isFound = spider.searchForWord(document, searchWord);
		assertTrue(isFound);
	}

	@Test
	void testGetLinks() {
		String url = "http://www.qtafsir.com/";
		Document document = spider.crawlUrl(url);
		String searchWord = "Yusuf";
		spider.searchForWord(document, searchWord);
		List<String> links = spider.getLinks();
		assertNotNull(links);
	}

	@AfterEach
	void tearDown() throws Exception {
		spider = null;
	}
}