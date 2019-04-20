var nodesArray = [];
var edgesArray = [];

var regularColor = '#ffffff';

var connectorColor = '#A0A0A0';
var dataColor = '#CCCCFF';
var processColor = '#66ccff'

var nodesTypes = [
    // process nodes
    { id: 'event#', label: 'Event', color: '#FF9999' },
    { id: 'function#', label: 'Function', color: '#66FF66' },
    { id: 'AND#', label: 'AND', color: connectorColor },
    { id: 'OR#', label: 'OR', color: connectorColor },
    { id: 'XOR#', label: 'XOR', color: connectorColor },
    { id: 'dataStore#', label: 'Data Store', color: dataColor },
    { id: 'externalEntity#', label: 'External Entity', color: dataColor },
    { id: 'interface#', label: 'Interface', color: '#E5CCFF' },

    // hierarchy elements
    { id: 'subProcess#', label: 'Sub-Process', color: processColor },

    // choreography elements
    { id: 'process#', label: 'Process', color: processColor },

    // organizational structure elements
    { id: 'orgUnit#', label: 'Organizational Unit', color: '#ffcc66' }
];

var edgesTypes = [
    // process arcs
    'sequenceFlow',
    'dataFlow',
    'inputArc', 'controlArc', 'outputArc', 'mechanismArc',

    // hierarchy arcs
    'isA',
    'composedOf',

    // choreography arcs
    'messageFlow',

    // organizational structure arcs
    'responsibleFor'
];

var labelPattern = /^[a-zA-Z\-_0-9\s]+$/;

for (var type in nodesTypes) {
    $('#nodeType').append('<option value="' + nodesTypes[type].id + '">'
        + nodesTypes[type].label + '</option>');
}

for (var type in edgesTypes) {
    $('#edgeType').append('<option value="' + edgesTypes[type] + '">'
        + edgesTypes[type] + '</option>');
}

function getColorByNodeType(nodeType) {
    for (var type in nodesTypes) {
        if (nodesTypes[type].id == nodeType) {
            return nodesTypes[type].color;
        }
    }

    return null;
}

function renderModel() {
    shadowState = false;

    var nodes = new vis.DataSet(nodesArray);
    var edges = new vis.DataSet(edgesArray);

    var container = document.getElementById('modelDescription');

    var data = {
        nodes: nodes,
        edges: edges
    };

    var options = {};

    new vis.Network(container, data, options);

    var bpModel = {
        nodes: nodesArray,
        edges: edgesArray
    };

    // $('#graphJSON').val(JSON.stringify(bpModel));
}

function updateNodesSelectOptions(id) {
    $(id).empty();

    for (var node in nodesArray) {
        if (!nodesTypes.includes(nodesArray[node].id)) {
            $(id).append('<option value="' + nodesArray[node].id + '">'
                + nodesArray[node].label + '</option>');
        }
    }
}

function isNodeExist(id) {
    for (var node in nodesArray) {
        if (nodesArray[node].id == id) {
            return true;
        }
    }

    return false;
}

$('#addNode').click(function() {
    var nodeLabel = $('#nodeLabel').val().replace(/\s+/g,"_");
    var nodeType = $('#nodeType').val();

    if (nodeLabel == null || nodeLabel.length == 0) {
        nodeLabel = "Node" + (nodesArray.length + 1);
    }

    if (!labelPattern.test(nodeLabel)) {
        alert('Invalid label!');
    } else {
        var nodeId = nodeType + nodeLabel;

        if (!isNodeExist(nodeId)) {
            nodesArray.push({id: nodeId, label: nodeId, color: getColorByNodeType(nodeType)});

            $('#nodeLabel').val('');

            updateNodesSelectOptions('#flowFrom');
            updateNodesSelectOptions('#flowTo');
            updateNodesSelectOptions('#nodeToRemove');

            renderModel();
        } else {
            alert('Node already exists!');
        }
    }
});

$('#addFlow').click(function() {
    var flowFrom = $('#flowFrom').val();
    var flowTo = $('#flowTo').val();

    var edgeType = $('#edgeType').val();

    edgesArray.push({from: flowFrom, to: flowTo, label: edgeType, arrows: 'to'});

    renderModel();
});

$('#removeNode').click(function() {
    var nodeToRemove = $('#nodeToRemove').val();

    if (confirm('Remove node ' + nodeToRemove + '?')) {
        var nodeToRemoveIndex = -1;

        for (var node in nodesArray) {
            if (nodesArray[node].id == nodeToRemove) {
                nodeToRemoveIndex = node;
                break;
            }
        }

        nodesArray.splice(nodeToRemoveIndex, 1);

        var edgesToRemove = [];

        for (var edge in edgesArray) {
            if (edgesArray[edge].from == nodeToRemove || edgesArray[edge].to == nodeToRemove) {
                edgesToRemove.push(edge);
            }
        }

        for (var i = edgesToRemove.length - 1; i >= 0; i--) {
            edgesArray.splice(edgesToRemove[i], 1);
        }

        updateNodesSelectOptions('#flowFrom');
        updateNodesSelectOptions('#flowTo');
        updateNodesSelectOptions('#nodeToRemove');

        renderModel();
    }
});

$('#refresh').click(function() {
    renderModel();
});

$('#removeFlow').click(function() {
    var flowFrom = $('#flowFrom').val();
    var flowTo = $('#flowTo').val();

    if (confirm('Remove flow from ' + flowFrom + ' to ' + flowTo + '?')) {
        var edgeToRemoveIndex = -1;

        for (var edge in edgesArray) {
            if (edgesArray[edge].from == flowFrom && edgesArray[edge].to == flowTo) {
                edgeToRemoveIndex = edge;
            }
        }

        if (edgeToRemoveIndex >= 0) {
            edgesArray.splice(edgeToRemoveIndex, 1);

            updateNodesSelectOptions('#flowFrom');
            updateNodesSelectOptions('#flowTo');
            updateNodesSelectOptions('#nodeToRemove');

            renderModel();
        } else {
            alert('Flow does not exist!')
        }
    }
});

function download(content, fileName, contentType) {
    var link = document.createElement('a');
    var file = new Blob([content], {type: contentType});

    link.href = URL.createObjectURL(file);
    link.download = fileName;

    link.click();
}

$('#save').click(function() {
    if (confirm('Store business process model?')) {
        var modelName = $("#modelName").val();
        var modelNotation = $("#modelNotation").val();
        var modelLevel = $("#modelLevel").val();
        var modelText = $("#modelText").val();

        if (modelName == null || modelName.length == 0 || !labelPattern.test(modelName)) {
            alert('Invalid model name!');
        } else {
            var bpModel = {
                name: modelName,
                notation: modelNotation,
                level: modelLevel,
                graph: {
                    nodes: nodesArray,
                    edges: edgesArray
                },
                description: modelText
            };

            $.ajax({
                type: "POST",
                url: "/store",
                dataType: "json",
                async: false,
                data: JSON.stringify(bpModel)
            });

            window.location.replace("./");
        }
    }

    return false;
});

window.addEventListener("beforeunload", function (e) {
    var confirmationMessage = 'If you leave before saving, your model will be lost.';

    (e || window.event).returnValue = confirmationMessage; //Gecko + IE
    return confirmationMessage; //Gecko + Webkit, Safari, Chrome etc.
});

function get(name) {
   if (name = (new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

var modelId = get('id');

if (typeof modelId !== "undefined") {
    $.getJSON("retrieve/" + modelId, function(data) {
        nodesArray = data.graph.nodes;
        edgesArray = data.graph.edges;

        updateNodesSelectOptions('#flowFrom');
        updateNodesSelectOptions('#flowTo');
        updateNodesSelectOptions('#nodeToRemove');

        renderModel();

        $('#modelName').val(data.name);
        $('#modelNotation').val(data.notation).change();
        $('#modelLevel').val(data.level).change();
        $('#modelText').append(data.description);
    });
}
