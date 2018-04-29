package bp.retrieve.web.controller;

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
	 * Access home page of the application.
	 */
	public static Route home = (Request request, Response response) -> {
		final int size = ProcessModelRepositoryService.retrieveAll().size();
		return ProcessModelRepositoryView.viewHome(size);
	};

	/**
	 * Access search page of the application.
	 */
	public static Route search = (Request request, Response response) -> {
		return ProcessModelRepositoryView.viewSearch();
	};

	/**
	 * Access shared models page of the application.
	 */
	public static Route sharedModels = (Request request, Response response) -> {
		return ProcessModelRepositoryView.viewProcessModels(ProcessModelRepositoryService.retrieveAll());
	};

	/**
	 * Access results of the range-based retrievement of process models.
	 */
	public static Route retrieveOnRange = (Request request, Response response) -> {
		try {
			final int from = Integer.parseInt(request.queryParams("from")) - 1;
			final int to = Integer.parseInt(request.queryParams("to")) - 1;

			return ProcessModelRepositoryView
					.viewProcessModels(ProcessModelRepositoryService.retrieveOnRange(from, to));
		} catch (Exception e) {
			return ProcessModelRepositoryView.viewError(e.getMessage());
		}
	};

	/**
	 * Access results of the keywords-based retrievement of process models.
	 */
	public static Route retrieveByKeywords = (Request request, Response response) -> {
		try {
			final String keywords = request.queryParams("keywords");

			return ProcessModelRepositoryView
					.viewProcessModels(ProcessModelRepositoryService.retrieveByKeywords(keywords));
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

			return ProcessModelRepositoryView.viewProcessModels(ProcessModelRepositoryService.retrieveSimilar(id));
		} catch (Exception e) {
			return ProcessModelRepositoryView.viewError(e.getMessage());
		}
	};
}