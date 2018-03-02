package bp.storing.dao.jaccess;

import java.util.ArrayList;
import java.util.List;

import bp.storing.beans.Process;
import bp.storing.dao.api.IProcessDAO;

public class JaccessProcessDAO implements IProcessDAO {

	@Override
	public void store(Process process) {
		// TODO Auto-generated method stub

	}

	@Override
	public Process retrieveById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Process retrieveByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Process> retrieve() {
		// TODO Auto-generated method stub
		List<Process> processes = new ArrayList<Process>();
		return processes;
	}

}
