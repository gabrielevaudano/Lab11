package it.polito.tdp.rivers.db;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RiversDAO {

	public List<River> getAllRivers() {
		
		final String sql = "SELECT id, name FROM river";

		List<River> rivers = new LinkedList<River>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				rivers.add(new River(res.getInt("id"), res.getString("name")));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return rivers;
	}
	
	public List<Flow> fillRiver(River r) {
		final String sql = "SELECT flow.id, flow.day, flow.flow "
				+ "FROM river, flow " + 
				 " WHERE river.id = flow.river AND river.id = ?";

		List<Flow> flow = new LinkedList<Flow>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r.getId());
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				flow.add(new Flow(res.getDate("flow.day").toLocalDate(),res.getDouble("flow.flow"), r));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		
		return flow;

	}
	
	public void fillCompositeRiver(River r) {
		final String sql = "SELECT MIN(flow.day) as minday, MAX(flow.day) as maxday, AVG(flow.flow) as avgflow "
				+ "FROM river, flow " + 
				 " WHERE river.id = flow.river AND river.id = ? "
				+ "GROUP BY flow.river ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r.getId());
			
			ResultSet res = st.executeQuery();

			if (res.next()) {
				r.setFlowAvg(res.getDouble("avgflow"));
				r.setMaxDay(res.getDate("maxday").toLocalDate());
				r.setMinDay(res.getDate("minday").toLocalDate());
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
}
