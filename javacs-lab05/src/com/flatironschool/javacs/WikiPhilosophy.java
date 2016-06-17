package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	final static WikiFetcher wf = new WikiFetcher();

	private static String findFirstValidURL(Iterable<Node> iter, String currURL) {
		for (Node node: iter) {
			if (node instanceof Element) {
				Element tempElement = (Element) node;
				if (checkValid(currURL, tempElement)) {
					return "http://en.wikipedia.org" + node.attr("href");
				}
			}
        }
        return null;
	}

	private static boolean checkValid (String currURL, Element element) {
		String validLink = "http://en.wikipedia.org" + element.attr("href");
		//base case
		if (element.attr("href").equals("")) {
			return false;
		}
		else if (currURL.equals(validLink)) {
			return false;
		}
		else if (isItalics(element)) {
			return false;
		}
		else if(element.attr("href").startsWith("/wiki/Help:")) {
			return false;
		}
		else if (element.attr("href").startsWith("#cite_note")) {
			return false;
		}
		else {
			return true;
		}
	}


	private static boolean isItalics(Element element) {
		Element curr = element;
		while (curr!= null) {
			if (curr.tagName().equals("i") || curr.tagName().equals("em")) {
				return true;
			}
			curr = element.parent();
		}
		return false;
	}


	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("testing");
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		boolean isPhilosophy = false;
		List<String> urlList = new ArrayList<String>();
		while (!isPhilosophy) {
			System.out.println("ENTERED INTO WHILE LOOP");
			Elements paragraphs = wf.fetchWikipedia(url);
			Element firstPara = paragraphs.get(0);
			System.out.println("the first para:" + firstPara);
			urlList.add(url);
			Iterable<Node> iter = new WikiNodeIterable(firstPara);
			String firstValidURL = findFirstValidURL(iter, url);
			System.out.println("firstValidURL" + firstValidURL);
			String philosophy = "https://en.wikipedia.org/wiki/Philosophy";
			if (firstValidURL!=null && url.equals(philosophy)) {
				urlList.add(firstValidURL);
				isPhilosophy = true;
			}
			if (firstValidURL!=null && (!url.equals(philosophy))) {
				url = firstValidURL;
			}
			if (firstValidURL==null) {
				System.out.println("An occur has occured.");
				isPhilosophy = true;
			}
		}

		for(String temporaryURL: urlList) {
				System.out.println(temporaryURL);
		}
	}
        // some example code to get you started

		// String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		// Elements paragraphs = wf.fetchWikipedia(url);

		// Element firstPara = paragraphs.get(0);
		
		// Iterable<Node> iter = new WikiNodeIterable(firstPara);
		// for (Node node: iter) {
		// 	if (node instanceof TextNode) {
		// 		System.out.print(node);
		// 	}
  //       }

        // // the following throws an exception so the test fails
        // // until you update the code
        // String msg = "Complete this lab by adding your code and removing this statement.";
        // throw new UnsupportedOperationException(msg);
	
}
