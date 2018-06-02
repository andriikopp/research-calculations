package bp.retrieve.web.controller;

import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.web.service.ProcessModelRepositoryService;
import bp.retrieve.web.view.ProcessModelRepositoryView;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Provides interaction between business logic and presentation of the
 * repository application.
 * 
 * @author Andrii Kopp
 */
public class ProcessModelRepositoryController {

	/**
	 * Access shared models page of the application.
	 */
	public static Route sharedModels = (Request request, Response response) -> {
		return ProcessModelRepositoryView.viewProcessModels(ProcessModelRepositoryService.retrieveAll());
	};

	/**
	 * Access results of the keywords-based retrievement of process models.
	 */
	public static Route retrieveByKeywords = (Request request, Response response) -> {
		try {
			final String keywords = request.queryParams("keywords");

			return ProcessModelRepositoryView
					.viewProcessModels(ProcessModelRepositoryService.retrieveByKeywords(keywords), keywords);
		} catch (Exception e) {
			return ProcessModelRepositoryView.viewError(e.getMessage());
		}
	};

	/**
	 * Access results of the similarity search results of process models.
	 */
	public static Route retrieveSimilar = (Request request, Response response) -> {
		try {
			final String id = request.queryParams("id");
			final GenericProcessModel pattern = ProcessModelRepositoryService.retrieveById(id);

			return ProcessModelRepositoryView.viewProcessModelsClustering(pattern, ProcessModelRepositoryService.retrieveSimilar(id));
		} catch (Exception e) {
			return ProcessModelRepositoryView.viewError(e.getMessage());
		}
	};

	/**
	 * Access delailed page of the process model.
	 */
	public static Route retrieveById = (Request request, Response response) -> {
		try {
			final String id = request.queryParams("id");

			return ProcessModelRepositoryView.viewProcessModel(ProcessModelRepositoryService.retrieveById(id));
		} catch (Exception e) {
			return ProcessModelRepositoryView.viewError(e.getMessage());
		}
	};
}
