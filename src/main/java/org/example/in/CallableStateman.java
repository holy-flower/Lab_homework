package org.example.in;

import java.sql.*;

public class CallableStateman {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pass";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String createProcedureSQL = "CREATE OR REPLACE FUNCTION calculate_sum(a integer, b integer)" +
                    "RETURNS integer AS $$" +
                    "BEGIN " +
                    " RETURN a + b; " +
                    "END; " +
                    "$$ LANGUAGE plpgsql";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createProcedureSQL);
            statement.close();
            System.out.println("Procedure is created");
            String functionCall = " { ? = call calculate_sum(?, ?) }";
            int a = 10;
            int b = 20;
            CallableStatement callableStatement = connection.prepareCall(functionCall);
            callableStatement.setInt(2, a);
            callableStatement.setInt(3, b);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();

            int sum = callableStatement.getInt(1);
            System.out.println("Sum of " + a + " and " + b + " is equal to " + sum);
            callableStatement.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
