package dao;

import java.sql.*;

public class AccountDAO {

    public double getBalance(Connection conn, String accountId) throws Exception {
        String sql = "SELECT Balance FROM Accounts WHERE AccountId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("Balance");
            } else {
                throw new Exception("Tài khoản không tồn tại");
            }
        }
    }
    public void updateBalance(Connection conn, String accountId, double amount) throws Exception {
        String sql = "{CALL sp_UpdateBalance(?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, accountId);
            cs.setDouble(2, amount);
            cs.executeUpdate();
        }
    }

    public void printAccounts(Connection conn, String acc1, String acc2) throws Exception {
        String sql = "SELECT * FROM Accounts WHERE AccountId IN (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acc1);
            ps.setString(2, acc2);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n=== KẾT QUẢ SAU CHUYỂN ===");
            while (rs.next()) {
                System.out.println(
                        rs.getString("AccountId") + " | " +
                                rs.getString("FullName") + " | " +
                                rs.getDouble("Balance")
                );
            }
        }
    }
}