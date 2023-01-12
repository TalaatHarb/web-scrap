package net.talaatharb.webscrap;

import java.util.HashSet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebScrapingApplication {
	private static final double NANO_TO_S = 1000000000.0;

	public static void main(String[] args) {
        log.info("Application Started");
        CimaClubScraper scrapper = new CimaClubScraper();
        
        // Start scraping
        final long startTime = System.nanoTime();

		// Solving the problem
		var result = scrapper.scrap(CimaClubScraper.PAGE_URL, new HashSet<>());

		final double period = (System.nanoTime() - startTime) / NANO_TO_S;
        
        
        log.info("===================================================================================");
        log.info("time taken: {}", period);
        result.stream().forEach(log::info);
        log.info("===================================================================================");
    }
}
