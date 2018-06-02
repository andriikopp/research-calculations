package bp.retrieve.web.view;

import java.util.List;

import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.container.clustering.ProcessModelCloseness;
import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

/**
 * Provides presentation of the repository application.
 * 
 * @author Andrii Kopp
 */
public class ProcessModelRepositoryView {
    private static final String TITLE_LABEL = "Business Process Model Repository";

	// Reusable tags.
	private static final ContainerTag TITLE = a(TITLE_LABEL).withHref("./");
	private static final ContainerTag SEARCH_FORM = form(
                input().withType("text").withName("keywords")
                        .attr("size", "60")
                        .isRequired(),
                span(" "),
                input().withType("submit").withValue("Search")
        ).withAction("./retrieveByKeywords");

	/**
	 * Shows detailed information about the business process model.
	 *
	 * @param processModel - the business process model to be shown.
	 * @return the corresponding render.
	 */
	public static String viewProcessModel(GenericProcessModel processModel) {
		return html(
			head(
				title(TITLE_LABEL)
			),
			body(
				h2(TITLE),
                    SEARCH_FORM,
				hr(),
				div(
					p(b("Name: "), a(processModel.getName()).withHref("./retrieveById?id=" + processModel.getId())),
					p(b("File: "), span(processModel.getFile()).withStyle("color: green")),
					p(b("Description: ")),
					p(span(processModel.getDescription())),
                        p(a("Similar models").withHref("./retrieveSimilar?id=" + processModel.getId())),
					p(img().withSrc("./images/" + processModel.getImage()))
				)
			)
		).render();
	}

	/**
	 * Shows the list of process models.
	 *
	 * @param processModels
	 *            - the list of process models that should be demonstrated.
	 * @return the render of process models list.
	 */
	public static String viewProcessModels(List<GenericProcessModel> processModels, String... keywords) {
		return html(
				head(
						title(TITLE_LABEL)
				),
				body(
						h2(TITLE),
                        getSearchForm(keywords),
						hr(),
						p("Retrieved: " + processModels.size() + " model(s)"),
						each(processModels, processModel -> div(
                                p(a(processModel.getName())
                                                .withHref("./retrieveById?id=" + processModel.getId()),
                                        br(),
                                        small(processModel.getFile()).withStyle("color: green"),
                                        br(),
                                        small(processModel.getDescription()),
                                        br(),
                                        small(a("Similar models").withHref("./retrieveSimilar?id="
                                                + processModel.getId()))
                                )
						    )
				        )
                )
        ).render();
	}

	/**
	 * Shows business process models clustering results.
	 *
	 * @param pattern - the pattern business process model;
	 * @param clusteringResults - the list of clustered business process models.
	 * @return the corresponding render.
	 */
	public static String viewProcessModelsClustering(GenericProcessModel pattern, List<ProcessModelCloseness> clusteringResults) {
		return html(
				head(
						title(TITLE_LABEL)
				),
				body(
						h2(TITLE),
                        SEARCH_FORM,
						p(b("Pattern: "), a(pattern.getName()).withHref("./retrieveById?id=" + pattern.getId())),
						hr(),
						p("Retrieved: " + clusteringResults.size() + " model(s)"),
						each(clusteringResults, clusteringResult -> div(
								p(a(clusteringResult.getProcessModel().getName())
										.withHref("./retrieveById?id=" + clusteringResult.getProcessModel().getId()),
										br(),
										small(clusteringResult.getProcessModel().getFile()).withStyle("color: green"),
										br(),
										small(clusteringResult.getProcessModel().getDescription()),
										br(),
										small(String.format("Closeness: %.2f ", clusteringResult.getClosenessValue()))
                                                .withStyle("color: blue"),
                                        small(a("Similar models").withHref("./retrieveSimilar?id="
                                                + clusteringResult.getProcessModel().getId()))
                                        )
								)
						)
				)
		).render();
	}

	/**
	 * Shows the error page.
	 * 
	 * @param errorMessage
	 *            - the error message that should be demonstrated.
	 * @return the render of error page.
	 */
	public static String viewError(String errorMessage) {
		return html(
			head(
				title(TITLE_LABEL)
			),
			body(
				h2(TITLE),
                    SEARCH_FORM,
				hr(),
				p(errorMessage)
			)
		).render();
	}

	// Search form with the text.
    private static ContainerTag getSearchForm(String... keywords) {
        if (keywords.length == 0)
            return SEARCH_FORM;

        return form(
                input().withType("text").withName("keywords")
                        .withValue(keywords[0])
                        .attr("size", "60")
                        .isRequired(),
                span(" "),
                input().withType("submit").withValue("Search")
        ).withAction("./retrieveByKeywords");
    }
}
