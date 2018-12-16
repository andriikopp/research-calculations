var nodesArray = [];
var edgesArray = [];

var regularColor = '#ffffff';

var nodesTypes = [
    { id: 'Start-Event#', label: 'Start-Event', color: regularColor },
    { id: 'End-Event#', label: 'End-Event', color: regularColor },
    { id: 'Intermediate-Event#', label: 'Intermediate-Event', color: regularColor },
    { id: 'Activity#', label: 'Activity', color: regularColor },
    { id: 'Parallel-Split#', label: 'Parallel-Split', color: regularColor },
    { id: 'Exclusive-Split#', label: 'Exclusive-Split', color: regularColor },
    { id: 'Parallel-Join#', label: 'Parallel-Join', color: regularColor },
    { id: 'Exclusive-Join#', label: 'Exclusive-Join', color: regularColor },
    { id: 'Data-Store#', label: 'Data-Store', color: regularColor },
    { id: 'External-Entity#', label: 'External-Entity', color: regularColor },
    { id: 'Organizational-Unit#', label: 'Organizational-Unit', color: regularColor },
    { id: 'Application-System#', label: 'Application-System', color: regularColor },
    { id: 'Business-Object#', label: 'Business-Object', color: regularColor }
];

var edgesTypes = [
    'sequenceFlow', 'dataFlow', 'isPerformedBy', 'isSupportedBy', 'requires',
    'isRegulatedBy', 'produces'
];

var labelPattern = /^[a-zA-Z\-_0-9]+$/;

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

    $('#graphJSON').val(JSON.stringify(bpModel));
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
    var nodeLabel = $('#nodeLabel').val();
    var nodeType = $('#nodeType').val();

    if (nodeLabel == null || nodeLabel.length == 0 || !labelPattern.test(nodeLabel)) {
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
    var modelName = prompt('Model name:');

    if (modelName == null || modelName.length == 0 || !labelPattern.test(modelName)) {
        alert('Invalid file name!');
    } else {
        var bpModel = {
            name: modelName,
            nodes: nodesArray,
            edges: edgesArray
        };

        download(JSON.stringify(bpModel), modelName + '.json', 'text/plain');
    }
});

$('#upload').click(function() {
    var uploadedGraph = $('#graphJSON').val();
    var graph = JSON.parse(uploadedGraph);

    nodesArray = graph.nodes;
    edgesArray = graph.edges;

    updateNodesSelectOptions('#flowFrom');
    updateNodesSelectOptions('#flowTo');
    updateNodesSelectOptions('#nodeToRemove');

    renderModel();
});

window.addEventListener("beforeunload", function (e) {
    var confirmationMessage = 'If you leave before saving, your model will be lost.';

    (e || window.event).returnValue = confirmationMessage; //Gecko + IE
    return confirmationMessage; //Gecko + Webkit, Safari, Chrome etc.
});
