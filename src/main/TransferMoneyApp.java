package main;

import dao.AccountDAO;
import DBContext.ConnectionMySql;

import java.sql.Connection;

public class TransferMoneyApp {

    public static void main(String[] args) {

        String sender = "ACC01";
        String receiver = "ACC02";
        double amount = 1000;

        AccountDAO dao = new AccountDAO();
        Connection conn = null;

        try {
            conn = ConnectionMySql.getConnection();

            conn.setAutoCommit(false);

            double balance = dao.getBalance(conn, sender);

            if (balance < amount) {
                throw new Exception("Không đủ tiền");
            }

            dao.updateBalance(conn, sender, -amount);

            dao.updateBalance(conn, receiver, amount);

            conn.commit();
            System.out.println("Chuyển khoản thành công");

            dao.printAccounts(conn, sender, receiver);

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());

            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("↩Đã rollback");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}