package hu.elte.fi.progtech.snake.persistence.dao;

import hu.elte.fi.progtech.snake.persistence.connection.DataSource;
import hu.elte.fi.progtech.snake.persistence.entity.HighScore;

import java.sql.*;

public class HighScoreDao implements IEntityDao<HighScore> {

    private static final String INSERT_QUERY = "INSERT INTO high_score (name, score) VALUES (?, ?)";
    private static final String TOP_10_HIGH_SCORE_QUERY = "SELECT score FROM high_score ORDER BY score DESC LIMIT 10";

    private static final String TOP_10_HIGH_PLAYER_QUERY = "SELECT name FROM high_score ORDER BY score DESC LIMIT 10";

    private static final String ATTR_NAME_NAME = "name";
    private static final String ATTR_NAME_SCORE = "score";


    /**
     * új értékek hozzáadása a pontokat és neveket atartalmazó adatbázishoz
     */
    @Override
    public void add(HighScore entity) throws SQLException {
        try(Connection connection = DataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getScore());

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Megadja az adatbázisban az első 10 legnagyobb pontal rendelkező játékos pontszámát.
     * @return scores int array
     */
    public int[] getTopScore() throws SQLException {
        int[] topScores = new int[]{0,0,0,0,0,0,0,0,0,0};
        try(Connection connection = DataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(TOP_10_HIGH_SCORE_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            int i = 0;
            while(resultSet.next() && i < 10) {
                topScores[i] = resultSet.getInt(ATTR_NAME_SCORE);
                ++i;
            }
            if(i > 0) {
                return topScores;
            }
        } catch(SQLException e) {
            System.err.println("Reading high score points from the database was failed. " + e);
        }
        return topScores;
    }
    /**
     * Megadja az adatbázisban az első 10 legnagyobb pontal rendelkező játékos nevét.
     * @return scores string array
     */
    public String[] getTopPlayer() throws SQLException {
        String[] topPlayers = new String[]{"", "", "", "", "", "", "", "", "", ""};

        try(Connection connection = DataSource.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(TOP_10_HIGH_PLAYER_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            int i = 0;
            while(resultSet.next() && i < 10) {
                topPlayers[i] = resultSet.getString(ATTR_NAME_NAME);
                ++i;
            }
            if(i > 0) {
                return topPlayers;
            }
        } catch(SQLException e) {
            System.err.println("Reading high score names from the database was failed. " + e);
        }
        return topPlayers;
    }
}
