package bp.storing.dao.jaccess;

import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;

/**
 * Provides database and table properties. Holds database connection instance.
 * 
 * @author Andrii Kopp
 */
public abstract class JaccessAbstractDAO {
	private String database;
	private String table;
	protected Table connection;

	/**
	 * Establishes connection to database.
	 */
	protected void connect() {
		try {
			if (connection == null) {
				connection = DatabaseBuilder.open(new File(database)).getTable(table);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}
