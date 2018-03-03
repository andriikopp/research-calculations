package bp.storing.dao.jaccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.healthmarketscience.jackcess.Row;

import bp.storing.beans.Process;
import bp.storing.dao.api.IProcessDAO;

public class JaccessProcessDAO extends JaccessAbstractDAO implements IProcessDAO {
	@Override
	public void store(Process process) {
		connect();

		Map<String, Object> row = new HashMap<String, Object>();

		row.put("ProcessId", process.getId());
		row.put("ProcessName", process.getName());
		row.put("ProcessDescription", process.getDescription());

		try {
			connection.addRowFromMap(row);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Process retrieveById(String id) {
		connect();

		for (Row row : connection) {
			String processId = row.getString("ProcessId");

			if (processId.equals(id)) {
				String name = row.getString("ProcessName");
				String description = row.getString("ProcessDescription");

				return new Process(processId, name, description);
			}
		}

		return null;
	}

	@Override
	public Process retrieveByName(String name) {
		connect();

		for (Row row : connection) {
			String processName = row.getString("ProcessName");

			if (processName.equals(name)) {
				String processId = row.getString("ProcessId");
				String description = row.getString("ProcessDescription");

				return new Process(processId, name, description);
			}
		}

		return null;
	}

	@Override
	public List<Process> retrieve() {
		connect();

		List<Process> rows = new ArrayList<Process>();

		for (Row row : connection) {
			String id = row.getString("ProcessId");
			String name = row.getString("ProcessName");
			String description = row.getString("ProcessDescription");

			rows.add(new Process(id, name, description));
		}

		return rows;
	}
}
