package com.learning.hello;

import com.learning.hello.controller.Game;
import com.learning.hello.controller.setscore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.dbcp.dbcp2.SQLExceptionList;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import com.learning.hello.controller.Notice;
import com.learning.hello.controller.NoticeController;
import com.learning.hello.controller.TennisController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Tennis")
public class TennisServlet extends HttpServlet {

    private static final long serialVersionUID = -4626049725684496527L;
	private TemplateEngine templateEngine;
    private TennisController tennisControl;
    private JakartaServletWebApplication application;

    @Override
    public void init() throws ServletException {
        super.init();

        application = JakartaServletWebApplication.buildApplication(getServletContext());	
		final WebApplicationTemplateResolver templateResolver = 
		    new WebApplicationTemplateResolver(application);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		tennisControl = new TennisController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	final IWebExchange webExchange = this.application.buildExchange(request, response);
        final WebContext ctx = new WebContext(webExchange);
//        List<Notice> notices = noticeControl.getAllNotices();
//        request.setAttribute("notices", notices);
        ctx.setVariable("player1_score", tennisControl.game.getPlayer1Score());
        ctx.setVariable("player2_score", tennisControl.game.getPlayer2Score());
        ctx.setVariable("player1_GameScore", tennisControl.game.getPlayer1GameScore());
        ctx.setVariable("player2_Gamescore", tennisControl.game.getPlayer2GameScore());
        ctx.setVariable("player1_Setscore", tennisControl.game.set.getSetScoreOfPlayer1());
        ctx.setVariable("player2_Setscore", tennisControl.game.set.getSetScoreOfPlayer2());
        
        List<Map<String, Object>> playbackPointData = retrievePointDataFromDatabase(1);
//        List<Map<String, Object>> playbackMatchData = retrieveMatchDataFromDatabase(1);
//        ctx.setVariable("playbackMatchData", playbackMatchData);
        ctx.setVariable("playbackPointData", playbackPointData);
        templateEngine.process("Tennis", ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	final IWebExchange webExchange = this.application.buildExchange(request, response);
    	final WebContext ctx = new WebContext(webExchange);
          String player = request.getParameter("player");
          if(player != null) {
          tennisControl.addPoint(player);
          System.out.println(player);}
          ctx.setVariable("text", tennisControl.getPoint());
          int player1_score = tennisControl.game.getPlayer1Score();
          int player2_score = tennisControl.game.getPlayer2Score();
          int player1_GameScore = tennisControl.game.getPlayer1GameScore();
          int player2_Gamescore = tennisControl.game.getPlayer2GameScore();
          int player1_Setscore = tennisControl.game.set.getSetScoreOfPlayer1();
          int player2_Setscore = tennisControl.game.set.getSetScoreOfPlayer2();
          int match_id = tennisControl.game.set.match.getMatchId();
          addTennisToDB(player1_score, player2_score, player1_GameScore, player2_Gamescore, player1_Setscore, player2_Setscore, match_id);
          
//          int previousPlayer1MatchScore = retrieveMatchScores(match_id)[0]; 
//          int previousPlayer2MatchScore = retrieveMatchScores(match_id)[1]; 
//
//          int player1MatchScore = tennisControl.game.set.match.getPlayer1WonMatches();
//          int player2MatchScore = tennisControl.game.set.match.getPlayer2WonMatches();
//
//          if (player1MatchScore != previousPlayer1MatchScore || player2MatchScore != previousPlayer2MatchScore) {
//        	  
//        	  match_id = tennisControl.game.set.match.getMatchId();
//        	  addMatchToDB(match_id, player1MatchScore, player2MatchScore);
//        	  ctx.setVariable("player1_MatchScore", player1MatchScore);
//        	  ctx.setVariable("player2_MatchScore", player2MatchScore);
//        	  
//          }
			String playbackMatchIdStr = request.getParameter("matchId");
		    if (playbackMatchIdStr != null && !playbackMatchIdStr.isEmpty()) {
		        int playbackMatchId = Integer.parseInt(playbackMatchIdStr);
		        
		        // Retrieve playback match data and point data
		        List<Map<String, Object>> playbackPointData = retrievePointDataFromDatabase(playbackMatchId);
//		        List<Map<String, Object>> playbackMatchData = retrieveMatchDataFromDatabase(playbackMatchId);
		        
		        ctx.setVariable("playbackPointData", playbackPointData);
//		        ctx.setVariable("playbackMatchData", playbackMatchData);
		    }
          String deleteMatchIdStr = request.getParameter("deleteMatchId");
          if (deleteMatchIdStr != null && !deleteMatchIdStr.isEmpty()) {
        	  int deleteMatchId = Integer.parseInt(deleteMatchIdStr);
        	  deletePointDataFromDatabase(deleteMatchId);
        	  deleteMatchDataFromDatabase(deleteMatchId);
          }
          
        doGet(request, response);
    }

	private void addTennisToDB(int player1_score, int player2_score,int player1GameScore,int player2GameScore,int player1SetScore,int player2SetScore,int matchId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	        try (
	        		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tennis", "joseph", "Jos@ph")) {
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Point (player1_score, player2_score, player1_gameScore, player2_gameScore, player1_setScore,player2_setScore,match_id) VALUES (?, ?, ?, ?, ?, ?, ?);");
//             PreparedStatement selectCountStatement = connection.prepareStatement("SELECT COUNT(*) FROM Notice;");
//             PreparedStatement deleteLeastUsedStatement = connection.prepareStatement("DELETE FROM Notice ORDER BY id LIMIT 1;"); {
            	System.out.println("printed");
	            insertStatement.setInt(1, player1_score);
	            insertStatement.setInt(2, player2_score);
	            insertStatement.setInt(3, player1GameScore);
	            insertStatement.setInt(4, player2GameScore);
	            insertStatement.setInt(5, player1SetScore);
	            insertStatement.setInt(6, player2SetScore);
	            insertStatement.setInt(7, matchId);
	            insertStatement.executeUpdate();
		
//            ResultSet countResult = selectCountStatement.executeQuery();
//            if (countResult.next()) {
//                int totalCount = countResult.getInt(1);
//                if (totalCount > 6) 
//                	deleteLeastUsedStatement.executeUpdate();
//            	}
//	        } 
	        }catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	private void addMatchToDB(int matchId,int player1MatchesWin, int player2MatchesWin) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tennis", "joseph", "Jos@ph")) {
			PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Match VALUES (?, ?, ?);");
//             PreparedStatement selectCountStatement = connection.prepareStatement("SELECT COUNT(*) FROM Notice;");
//             PreparedStatement deleteLeastUsedStatement = connection.prepareStatement("DELETE FROM Notice ORDER BY id LIMIT 1;"); {
			System.out.println("printed matches");
			insertStatement.setInt(0, matchId);
			insertStatement.setInt(1, player1MatchesWin);
			insertStatement.setInt(2, player2MatchesWin);
			insertStatement.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
    public int[] retrieveMatchScores(int matchId) {
    	int[] prevMatchScores = new int[2];
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tennis", "joseph", "Jos@ph")) {

            // Define the query
            String query = "SELECT player1_won_matches, player2_won_matches FROM Match WHERE match_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, matchId);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the results
            if (resultSet.next()) {
                prevMatchScores[0] = resultSet.getInt(1);
                prevMatchScores[1] = resultSet.getInt(2);

                // Use the retrieved values as needed
                System.out.println("Player 1 Won Matches: " + prevMatchScores[0]);
                System.out.println("Player 2 Won Matches: " + prevMatchScores[1]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prevMatchScores;
    }
private List<Map<String, Object>> retrieveMatchDataFromDatabase(int playbackMatchId) {
    List<Map<String, Object>> matchDataList = new ArrayList<>();
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }

    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tennis", "joseph", "Jos@ph")) {
        String sql = "SELECT * FROM Match WHERE match_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playbackMatchId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> matchData = new HashMap<>();
                    matchData.put("matchId", resultSet.getInt("match_id"));
                    matchData.put("player1_MatchScore", resultSet.getInt("player1_matchScore"));
                    matchData.put("player2_MatchScore", resultSet.getInt("player2_matchScore"));
                    matchDataList.add(matchData);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return matchDataList;
}
private List<Map<String, Object>> retrievePointDataFromDatabase(int playbackMatchId) {
    List<Map<String, Object>> pointDataList = new ArrayList<>();
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    System.out.println("enterd");
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tennis", "joseph", "Jos@ph")) {
        String sql = "SELECT * FROM Point WHERE match_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playbackMatchId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> pointData = new HashMap<>();
                    pointData.put("pointId", resultSet.getInt("point_id"));
                    pointData.put("matchId", resultSet.getInt("match_id"));
                    pointData.put("player1Score", resultSet.getInt("player1_score"));
                    pointData.put("player2Score", resultSet.getInt("player2_score"));
                    pointData.put("player1gameScore", resultSet.getInt("player1_gameScore"));
                    pointData.put("player2gameScore", resultSet.getInt("player2_gameScore"));
                    pointData.put("player1setScore", resultSet.getInt("player1_setScore"));
                    pointData.put("player2setScore", resultSet.getInt("player2_setScore"));
                    pointDataList.add(pointData);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    System.out.println(pointDataList);
if (!pointDataList.isEmpty()) {
    int firstPointId = (int) pointDataList.get(0).get("pointId");
    System.out.println("PointId of the first row: " + firstPointId);
} else {
    System.out.println("No point data available.");
}
    for(Map<String, Object> point : pointDataList) {
   Object pointIdValue = point.get("pointId");
    if (pointIdValue != null) {
        System.out.println("PointId: " + pointIdValue);
    } else {
        System.out.println("PointId is NULL");
    }
    }
    return pointDataList;
}

private void deleteMatchDataFromDatabase(int matchId) {
	try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tennis", "joseph", "Jos@ph")) {
        String deleteQuery = "DELETE FROM `Match` WHERE `match_id` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, matchId);
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void deletePointDataFromDatabase(int matchId) {
	try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tennis", "joseph", "Jos@ph")) {
        String deleteQuery = "DELETE FROM Point WHERE match_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, matchId);
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
