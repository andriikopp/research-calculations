$.getJSON("/bpmai/api/models", function (data) {
    for (var i = 0; i < data.length; i++) {
        $("#processModelsList").append('<p>' +
            '<div class="card" id="' + data[i].id + '">' +
            '<div class="card-body">' +
            '<a onclick="loadModel(\'' + data[i].id + '\')" href="javascript:void(0)">' +
            data[i].name + '</br>' + data[i].timeStamp + '</a>' + ' <kbd>' + data[i].notation + '</kbd>' +
            '</div>' +
            '</div>' +
            '</p>');
    }

    var get = function (name) {
        if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search)) {
            return decodeURIComponent(name[1]);
        }
    };

    var modelId = get('id');

    if (typeof modelId !== "undefined") {
        loadModel(modelId);
    }
});

var activeModel = null;
var graphVisible = false;
var similarVisible = false;
var modelsListExpanded = false;

var similarModelsSet = null;

function loadModel(id) {
    $.getJSON("/bpmai/api/model/" + id, function (data) {
        var modelInfo = $("#modelInfo");
        modelInfo.empty();

        modelInfo.append("<p><b>Business Process Model Name<b></p>");
        modelInfo.append("<p>" + data.name + "</p>");

        modelInfo.append("<p><b>Business Process Model Notation<b></p>");
        modelInfo.append("<p>" + data.notation + "</p>");

        modelInfo.append("<p><b>Business Process Model File<b></p>");
        modelInfo.append("<p><a href='/models/" + data.fileName + "'>" + data.fileName + "</a></p>");

        $("#modelAnalysis").empty();

        if (activeModel != null) {
            $('#' + activeModel).css('background-color', 'white');
        }

        $('#' + data.id).css('background-color', 'lightGrey');
        activeModel = data.id;

        var updateURLParameter = function (url, param, paramVal) {
            var newAdditionalURL = "";
            var tempArray = url.split("?");
            var baseURL = tempArray[0];
            var additionalURL = tempArray[1];
            var temp = "";

            if (additionalURL) {
                tempArray = additionalURL.split("&");

                for (var i = 0; i < tempArray.length; i++) {

                    if (tempArray[i].split('=')[0] != param) {
                        newAdditionalURL += temp + tempArray[i];
                        temp = "&";
                    }
                }
            }

            var rows_txt = temp + "" + param + "=" + paramVal;
            return baseURL + "?" + newAdditionalURL + rows_txt;
        }

        window.history.replaceState('', '', updateURLParameter(window.location.href, "id", data.id));

        analyzeModel(id);

        drawModel(data);

        drawGraph(data);
    });
}

function drawModel(data) {
    $('#canvas').empty();

    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            var viewer = new BpmnJS({ container: '#canvas' });

            viewer.importXML(xhr.response, function (err) {
                var canvas = viewer.get('canvas');
                canvas.zoom('fit-viewport');
            });
        }
    };

    xhr.open('GET', '/models/' + data.fileName, true);
    xhr.send(null);
}

function drawGraph(data) {
    $('#rdfButton').empty();
    $('#rdfButton').append('<a href="javascript:void(0);" onclick="displayGraph();" id="descriptionLink">Show/hide graph</a><br/>');
    $('#rdfButton').append('<div id="modelDescription" style="border: 1px solid lightgray; border-radius: 5px; height: 500px;"></div>');
    $('#modelDescription').hide();

    graphVisible = false;

    var container = document.getElementById('modelDescription');

    var graphData = {
        nodes: data.graph.nodes,
        edges: data.graph.edges
    };

    var options = {};

    new vis.Network(container, graphData, options);
}

function analyzeModel(id) {
    $.getJSON("/bpmai/api/model/" + id, function (data) {
        var modelAnalysis = $("#modelAnalysis");

        modelAnalysis.empty();

        modelAnalysis.append("<p><b>Size:</b> " + data.size + "</p>");
        modelAnalysis.append("<p><b>Functions:</b> " + data.functions + "</p>");
        modelAnalysis.append("<p><b>Connectors Balance:</b> " + data.connectorsBalance + "</p>");
        modelAnalysis.append("<p><b>Functions Balance:</b> " + data.functionsBalance + "</p>");
        modelAnalysis.append("<p><b>Start Events:</b> " + data.startEvents + "</p>");
        modelAnalysis.append("<p><b>End Events:</b> " + data.endEvents + "</p>");
        modelAnalysis.append("<p><b>Connectors Mismatch:</b> " + data.mismatch + "</p>");
        modelAnalysis.append("<p><b>OR Connectors:</b> " + data.orConnectors + "</p>");

        modelAnalysis.append("<p><b>Quality:</b> " + data.quality + "</p>");

        if (data.quality < 1.0) {
            modelAnalysis.append('<div class="alert alert-danger" role="danger">' +
                'This model might contain errors!</div>');
        }

        for (var x in data.recommendations) {
            modelAnalysis.append('<div class="alert alert-info" role="info">' + data.recommendations[x] + '</div>');
        }

        modelAnalysis.append('<div id="similarModels"></div>');
        modelAnalysis.append('<div id="searchForm">' +
            '<p><input type="text" class="form-control" id="searchQuery" placeholder="Filter similar models"/></p>' +
            '<p><button class="btn btn-info" onclick="similaritySearch();">Search</button></p>' +
        '</div>');
        modelAnalysis.append('<div id="similarModelsLinks"></div>');

        var similarCounter = 0;

        if (data.similarModels != null) {
            similarModelsSet = data.similarModels;

            Object.keys(data.similarModels).forEach(function (key, index) {
                $('#similarModelsLinks').append('<div class="alert alert-secondary" role="info">' +
                    '<a target="_blank" href="?id=' + key + '">' + data.similarModels[key] + '</a>' +
                    '</div>');
                similarCounter++;
            });
        }

        if (similarCounter > 0) {
            $('#similarModels').append('<div class="alert alert-warning" role="alert">' +
                'There are ' + similarCounter + ' model(s) found that have similar quality characteristics</div>');
            $('#similarModels').append('<a href="javascript:void(0);" onclick="displaySimilarModels();" id="descriptionLink">Show/hide similar models</a><br/>');
        }

        $('#similarModelsLinks').hide();
    });
}

function displayGraph() {
    if (graphVisible) {
        $('#modelDescription').hide();
        graphVisible = false;
    } else {
        $('#modelDescription').show();
        graphVisible = true;
    }
}

function displaySimilarModels() {
    if (similarVisible) {
        $('#similarModelsLinks').hide();
        similarVisible = false;
    } else {
        $('#similarModelsLinks').show();
        similarVisible = true;
    }
}

function resizeModelsList() {
    if (modelsListExpanded) {
        $('#processModelsList').css('height', '600px');
        modelsListExpanded = false;
    } else {
        $('#processModelsList').css('height', '100%');
        modelsListExpanded = true;
    }
}

function similaritySearch() {
    $('#similarModelsLinks').empty();
    var request = $('#searchQuery').val();

    if (similarModelsSet != null) {
        Object.keys(similarModelsSet).forEach(function (key, index) {
            if (similarModelsSet[key].includes(request)) {
                $('#similarModelsLinks').append('<div class="alert alert-secondary" role="info">' +
                    '<a target="_blank" href="?id=' + key + '">' + similarModelsSet[key] + '</a>' +
                    '</div>');
            }
        });
    }
}
