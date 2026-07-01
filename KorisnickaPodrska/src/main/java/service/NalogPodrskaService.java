package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import beans.NalogPodrska;
import oodb.ConnectionPool;

public class NalogPodrskaService {

	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	private static final String SQL_SELECT_BY_USERNAME = "SELECT * FROM administrator WHERE korisnicko_ime=?";

	public NalogPodrska provjeraKredencijala(String korisnickoIme, String unesenaLozinka) {
		NalogPodrska nalog = null;
		Connection connection = null;
		ResultSet rs = null;
		Object values[] = { korisnickoIme };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_SELECT_BY_USERNAME, false, values);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				nalog = new NalogPodrska(rs.getInt("id"), rs.getString("ime"), rs.getString("prezime"),
						rs.getString("korisnicko_ime"), rs.getString("lozinka"), rs.getInt("uloga"));
			}
			pstmt.close();

			if (nalog != null && korisnickoIme.equals(nalog.getKorisnickoIme())) {
				String zasticenaLozinka = nalog.getLozinka();
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				boolean prijavljen = passwordEncoder.matches(unesenaLozinka, zasticenaLozinka);
				if (!prijavljen)
					nalog = null;
			}

		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return nalog;
	}

}
