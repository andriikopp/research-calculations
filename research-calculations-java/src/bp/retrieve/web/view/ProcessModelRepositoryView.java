package bp.retrieve.web.view;

import static j2html.TagCreator.a;
import static j2html.TagCreator.b;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.hr;
import static j2html.TagCreator.html;
import static j2html.TagCreator.img;
import static j2html.TagCreator.input;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import static j2html.TagCreator.title;

import java.util.List;

import bp.retrieve.collection.GenericProcessModel;
import j2html.tags.ContainerTag;

/**
 * Provides presentation of the repository application.
 * 
 * @author Andrii Kopp
 */
public class ProcessModelRepositoryView {
	private static final String TITLE = "Business Process Model Repository";
	
	// Reusable tags.
	private static final ContainerTag HOME = a("Home").withHref("./home");
	private static final ContainerTag SEARCH = a("Search").withHref("./search");
	private static final ContainerTag SHARED = a("Shared models").withHref("./sharedModels");
	
	/**
	 * Shows the home page.
	 * 
	 * @param size
	 *            - the number of stored process models.
	 * @return the render of home page.
	 */
	public static String viewHome(int size) {
		return html(
			head(
				title(TITLE)
			),
			body(
				h2(TITLE),
				form(
					span("View models in range between "),
					input().withType("number").withName("from")
						.attr("min", "1")
						.attr("max", String.valueOf(size))
						.isRequired(),
					span(" and "),
					input().withType("number").withName("to")
						.attr("min", "1")
						.attr("max", String.valueOf(size))
						.isRequired(),
					span(" "),
					input().withType("submit").withValue("View")
				).withAction("./retrieveOnRange"),
				HOME, span(" "), SEARCH, span(" "), SHARED,
				hr(),
				p("Collection size: " + size + " model(s)")
			)
		).render();
	}
	
	/**
	 * Shows the search page.
	 * 
	 * @return the render of search page.
	 */
	public static String viewSearch() {
		return html(
			head(
				title(TITLE)
			),
			body(
				h2(TITLE),
				form(
					input().withType("text").withName("keywords")
						.withPlaceholder("name and description keywords")
						.attr("size", "60")
						.isRequired(),
					span(" "),
					input().withType("submit").withValue("Search")
				).withAction("./retrieveByKeywords"),
				HOME, span(" "), SEARCH, span(" "), SHARED,
				hr()
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
	public static String viewProcessModels(List<GenericProcessModel> processModels) {
		return html(
			head(
				title(TITLE)
			),
			body(
				h2(TITLE),
				HOME, span(" "), SEARCH, span(" "), SHARED,
				hr(),
				p("Retrieved: " + processModels.size() + " model(s)"),
				each(processModels, processModel -> div(
						p(b("Name: "), span(processModel.getName())),
						p(b("File: "), span(processModel.getFile())),
						p(b("Description: ")),
						p(span(processModel.getDescription())),
						p(img().withSrc("./images/" + processModel.getImage())),
						a("Similar models").withHref("./retrieveSimilar?id=" + processModel.getId()),
						hr()
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
				title(TITLE)
			),
			body(
				h2(TITLE),
				HOME, span(" "), SEARCH, span(" "), SHARED,
				hr(),
				p(errorMessage)
			)
		).render();
	}
}
