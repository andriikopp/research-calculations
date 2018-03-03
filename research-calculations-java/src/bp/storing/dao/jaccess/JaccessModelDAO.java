package bp.storing.dao.jaccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.healthmarketscience.jackcess.Row;

import bp.storing.beans.Model;
import bp.storing.dao.api.IModelDAO;

public class JaccessModelDAO extends JaccessAbstractDAO implements IModelDAO {
	@Override
	public void store(Model model) {
		connect();

		Map<String, Object> row = new HashMap<String, Object>();

		row.put("ModelId", model.getId());
		row.put("ProcessId", model.getProcess());
		row.put("ModelFileName", model.getFile());

		try {
			connection.addRowFromMap(row);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Model retrieveById(String id) {
		connect();

		for (Row row : connection) {
			String modelId = row.getString("ModelId");

			if (modelId.equals(id)) {
				String processId = row.getString("ProcessId");
				String fileName = row.getString("ModelFileName");

				return new Model(modelId, processId, fileName);
			}
		}

		return null;
	}

	@Override
	public Model retrieveByName(String name) {
		connect();

		for (Row row : connection) {
			String fileName = row.getString("ModelFileName");

			if (fileName.equals(name)) {
				String modelId = row.getString("ModelId");
				String processId = row.getString("ProcessId");

				return new Model(modelId, processId, fileName);
			}
		}

		return null;
	}

	@Override
	public List<Model> retrieve() {
		connect();

		List<Model> rows = new ArrayList<Model>();

		for (Row row : connection) {
			String modelId = row.getString("ModelId");
			String processId = row.getString("ProcessId");
			String fileName = row.getString("ModelFileName");

			rows.add(new Model(modelId, processId, fileName));
		}

		return rows;
	}
}
