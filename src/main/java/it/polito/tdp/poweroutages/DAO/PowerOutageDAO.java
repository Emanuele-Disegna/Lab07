package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	
	public List<PowerOutage> getPowerOutages(Nerc nerc) {
		
		String sql = "SELECT p.id,nerc_id,customers_affected,date_event_began,date_event_finished "
				+ "FROM poweroutages p, nerc n "
				+ "WHERE p.nerc_id=n.id AND n.id=? ORDER BY date_event_began";
		
		List<PowerOutage> powerOutageList = new ArrayList<PowerOutage>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				PowerOutage p = new PowerOutage(res.getInt("id"), res.getInt("nerc_id"),
					res.getInt("customers_affected"), res.getTimestamp("date_event_began").toLocalDateTime(),
					res.getTimestamp("date_event_finished").toLocalDateTime());
					
				powerOutageList.add(p);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return powerOutageList;
		
	}
}
