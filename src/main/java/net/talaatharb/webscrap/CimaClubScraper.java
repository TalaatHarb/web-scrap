package net.talaatharb.webscrap;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CimaClubScraper {
	public static final String PAGE_URL = "https://ciima-clup.store/watch/انمى-slam-dunk-الحلقة-1-الاولى-مترجمة";
	public static final String TARGET_MARKER = "slam-dunk";
	public static final String EPISODE_MARKER = "episode";
	public static final String WATCH_MARKER = "watch";

	public Set<String> scrap(String url, Set<String> result) {
		Set<String> toScrap = new HashSet<>();
		if (url.contains(TARGET_MARKER)) {

			// To switch a link to episode to a watch link
			if (url.contains(EPISODE_MARKER)) {
				url = url.replace(EPISODE_MARKER, WATCH_MARKER);
			}

			try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
				webClient.getOptions().setCssEnabled(false);
				webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
				webClient.getOptions().setThrowExceptionOnScriptError(false);
				webClient.getOptions().setPrintContentOnFailingStatusCode(false);

				HtmlPage page = webClient.getPage(url);

				String title = page.getTitleText();
				log.info("====================================================================");
				log.info("Page Title: {}", title);
				log.info("Page URL: {}", url);

				List<HtmlAnchor> links = page.getAnchors();
				for (HtmlAnchor link : links) {
					String href = link.getHrefAttribute();
					toScrap.add(href);
				}

				webClient.getCurrentWindow().getJobManager().removeAllJobs();
				log.info("====================================================================");

			} catch (IOException e) {
				log.debug(e.getMessage());
			}

		}

		toScrap.removeAll(result);
		result.addAll(toScrap);

		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			log.debug(e.getMessage());
			Thread.currentThread().interrupt();
		}

		var nextLevel = toScrap.parallelStream().map(u -> this.scrap(u, result)).toList();

		log.info("Next Level: {}", nextLevel.size());

		return result;
	}
}
