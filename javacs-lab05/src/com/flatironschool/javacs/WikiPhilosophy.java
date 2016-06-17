package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.lang.*;
import java.util.StringTokenizer;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	final static WikiFetcher wf = new WikiFetcher();
	public static Deque<String> parenthesis = new ArrayDeque<String>();
	private static Element findFirstValidURL(Elements paragraphs) {
		for(Element paragraph: paragraphs) {
			Iterable<Node> iter = new WikiNodeIterable(paragraph);
			for (Node node: iter) {
				if (node instanceof Element) {
					Element tempElement = (Element) node;
					if(tempElement.tagName().equals("a") && parenthesis.isEmpty()) {
						Elements parents = tempElement.parents();
						if (!(parents.hasAttr("i"))) {
							if(!parents.hasAttr("em")) {
								return tempElement;
							}
						}
					}
				}
				if (node instanceof TextNode) {
					TextNode n = (TextNode) node;
					System.out.println(node);
					
					StringTokenizer st = new StringTokenizer(n.text(),"()",true);
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						if(token.equals("(")) {
							parenthesis.push(token);
						}
						if (token.equals(")")) {
							if (!parenthesis.isEmpty()) {
								parenthesis.pop();
							}
							else {
								System.err.print("Unbalanced parenthesis");
							}
						}
					}
				}
	        }
	     }
        return null;
	}

	// private static boolean checkValid (String currURL, Element element) {
	// 	String validLink = "http://en.wikipedia.org" + element.attr("href");
	// 	//base case
	// 	if (element.attr("href").equals("") || currURL.equals(validLink) || isItalics(element) 
	// 		||element.attr("href").startsWith("/wiki/Help:") || element.attr("href").startsWith("#cite_note")) {
	// 		return false;
	// 	}
	// 	else {
	// 		return true;
	// 	}
	// }


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
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		boolean isPhilosophy = false;
		List<String> urlList = new ArrayList<String>();
		String philosophy = "https://en.wikipedia.org/wiki/Philosophy";
		Elements paragraphs = wf.fetchWikipedia(url);
		
		for(int i = 0; i<20; i++) {
			if (urlList.contains(url)) {
				System.out.println("error:linkvisited.");
			}
			else {
				urlList.add(url);
			}

			Element firstPara = findFirstValidURL(paragraphs);

			if (firstPara == null) {
				System.out.println("ERROR: NULL URL");
			}
			else {
				String link = "https://en.wikipedia.org" + firstPara.attr("href");
				if(link.equals(philosophy)) {
					isPhilosophy = true;
					break;
				}
			}
		}
		// urlList.add(url);
		// Iterable<Node> iter = new WikiNodeIterable(firstPara);
		// String firstValidURL = findFirstValidURL(iter, url);
		// System.out.println("firstValidURL" + firstValidURL);

		// if (firstValidURL!=null && url.equals(philosophy)) {
		// 	urlList.add(firstValidURL);
		// 	isPhilosophy = true;
		// }
		// if (firstValidURL!=null && (!url.equals(philosophy))) {
		// 	url = firstValidURL;
		// }
		// if (firstValidURL==null) {
		// 	System.out.println("An occur has occured.");

		// }
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
