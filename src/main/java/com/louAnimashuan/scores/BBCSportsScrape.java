package com.louAnimashuan.scores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.amazon.speech.slu.Slot;

public class BBCSportsScrape {
	
	private static final String bbcSportsURL = "http://www.bbc.co.uk/sport/football/live-scores";
	
	public BBCSportsScrape(){
		
	}
	
	public Document getDocument() throws IOException{
		return Jsoup.connect(bbcSportsURL).get();
	}
	
	public ArrayList<Game> getScores(Document bbcDoc) throws IOException{ //TODO change name to get matches
	
		String homeTeam = null;
		String awayTeam = null;
		String elapsedTime = null;
		String score = null;
		
		ArrayList<Game> games = new ArrayList<Game>();
		
		/*
		 * TODO add feature to select league playing in
		 * - current best solution: is to search for the premier league header and then navigate to parent node and then to the child node
		 */
		
		/*Sample inputs:
		 * class: fixture
		 * time: "15:00"
		 * scr: " v "
		 * 
		 * class: live
		 * time: "12 mins"
		 * scr: "1 - 0"
		 * 
		 * class: result
		 * time: Result
		 * scr: "1 - 0"
		 */
		
		Elements fixtures = bbcDoc.select(".fixture");
		Elements liveMatches = bbcDoc.select(".live");
		Elements results = bbcDoc.select(".result");
		int i =0;
		for(Element match : fixtures ){
			try{
				homeTeam = match.select(".team-home").first().text();
				awayTeam = match.select(".team-away").first().text();
				elapsedTime = match.select(".elapsed-Time").first().text();
			}catch(NullPointerException e){
				continue;
			}
			games.add(new Game(homeTeam, awayTeam, null, elapsedTime, MatchStatus.TOSTART));
		}
		
		for(Element match : liveMatches  ){
			homeTeam = match.select(".team-home").first().text();
			awayTeam = match.select(".team-away").first().text();
			elapsedTime = match.select(".elapsed-Time").first().text();
			score = match.select(".score").first().text();
			
			games.add(new Game(homeTeam, awayTeam, score, elapsedTime, MatchStatus.PLAYING));
		}
		
		for(Element match : results){
			homeTeam = match.select(".team-home").first().text();
			awayTeam = match.select(".team-away").first().text();
			score = match.select(".score").first().text();
			
			games.add(new Game(homeTeam, awayTeam, score, null, MatchStatus.FINISHED));
		}
		return games;
	}
	
	
	
	public Game getScore(Document bbcDoc, Map<String, Slot> slots){
		
		String homeTeam = slots.get("HomeTeam").getValue();
		String awayTeam = null;

		if(slots.containsKey("AwayTeam")){
			awayTeam = slots.get("AwayTeam").getValue();
		}
		return getScore(bbcDoc, homeTeam, awayTeam);
	}
	
	public Game getScore(Document bbcDoc, String h, String a){
		Elements fixtures = bbcDoc.select(".fixture");
		Elements liveMatches = bbcDoc.select(".live");
		Elements results = bbcDoc.select(".result");
		
		System.out.print("*");
		String homeTeam = h;
		System.out.print(h);
		System.out.print("*");
		String awayTeam = a;
		System.out.print(a);
		System.out.print("*");
		
		for(Element match : fixtures ){
			System.out.println("size: "+fixtures.size());
			try {
				 String home = match.select(".team-home").first().text();
				 String away = match.select(".team-away").first().text();
				 System.out.println("*"+home +"*"+away+"*");
				 if (homeTeam.equals(home)|| homeTeam.equals(away)){
					 String score = match.select(".score").first().text();
					 String time = match.select(".elapsed-time").first().text();
					 return new Game(home, away, score, time, MatchStatus.TOSTART);
				 }else{
					 System.out.println("not found: home");
				 }
				 
				 if( (awayTeam != null && awayTeam.equals(home)) || (awayTeam != null && awayTeam.equals(away))){
					 String score = match.select(".score").first().text();
					 String time = match.select(".elapsedTime").first().text();
					 return new Game(home, away, score, time, MatchStatus.TOSTART );
				 }else{
					 System.out.println("not found: away");
				 }
			}catch(NullPointerException e){
				System.out.println("continue");
				continue;
				
			}
		}
					
		for(Element match : liveMatches ){
			try {
				 String home = match.select(".team-home").first().text();
				 String away = match.select(".team-away").first().text();
				 
				 	if (homeTeam.equals(home) || homeTeam.equals(away)){
					String score = match.select(".score").first().text();
					String time = match.select(".elapsed-time").first().text();
					return new Game(home, away, score, time, MatchStatus.PLAYING );
					}
							 
					if( (awayTeam != null && awayTeam.equals(home)) || (awayTeam != null && awayTeam.equals(away))){
						String score = match.select(".score").first().text();
						String time = match.select(".elapsed-time").first().text();
						return new Game(home, away, score, time, MatchStatus.PLAYING );
					}
			}catch(NullPointerException e){
				continue;
			}
		}
		
		for (Element match : results){
			try {
				 String home = match.select(".team-home").first().text();
				 String away = match.select(".team-away").first().text();
				 
					if (homeTeam.equals(home) || homeTeam.equals(away)){
					String score = match.select(".score").first().text();
					String time = match.select(".elapsed-time").first().text();
					return new Game(home, away, score, time, MatchStatus.FINISHED );
						}else{
							System.out.println("not found home");
						}
						if( (awayTeam != null && awayTeam.equals(home)) || (awayTeam != null && awayTeam.equals(away))){
							String score = match.select(".score").first().text();
							String time = match.select(".elapsed-time").first().text();
							return new Game(home, away, score, time, MatchStatus.FINISHED );
							 }
			}catch(NullPointerException e){
				continue;
			}
		}
		return null;
	}
	

	
	
	public boolean currentlyPlaying(String team) throws IOException{
		
		boolean isPlaying = false;
		//TODO implement isCurrentlyPlaying Method
		Document doc = Jsoup.connect(bbcSportsURL).get();
		
		return isPlaying;
		

	}
	
}
