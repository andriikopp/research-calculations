$(document).ready(function () {
    $('#analyzeDoc').click(function () {
        $('#canvas').empty();

        let bpmnXML = $('#bpmnDoc').val();

        if (bpmnXML.includes('<bpmn:')) {
            prefix = 'bpmn:';
        }

        let viewer = new BpmnJS({ container: '#canvas' });

        viewer.importXML(bpmnXML, function (err) {
            if (err) {
                $('#canvas').append('<div class="alert alert-danger">' + err + '</div>');
            } else {
                let canvas = viewer.get('canvas');

                canvas.zoom('fit-viewport');

                let xmlDoc = null;

                if (window.DOMParser) {
                    let parser = new DOMParser();
                    xmlDoc = parser.parseFromString(bpmnXML, 'text/xml');
                } else {
                    xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
                    xmlDoc.async = false;
                    xmlDoc.loadXML(bpmnXML);
                }

                $('#recommendations').empty();

                let recommendations = [];

                sizeValidation(xmlDoc, recommendations);
                eventsValidation(xmlDoc, recommendations);
                gatewaysValidation(xmlDoc, recommendations);
                tasksValidation(xmlDoc, recommendations);
                routingValidation(xmlDoc, recommendations);
                branchingValidation(xmlDoc, recommendations);

                for (let value in recommendations) {
                    $('#recommendations').append('<div class="alert alert-danger">' +
                        recommendations[value] + '</div>');
                }
            }
        });
    });
});

var prefix = '';

function sizeValidation(xmlDoc, recommendations) {
    let process = xmlDoc.getElementsByTagName(prefix + 'process')[0].childNodes;

    let size = 0;

    for (let i = 0; i < process.length; i++) {
        if (process[i].nodeName.toLowerCase().includes('task'.toLowerCase())) {
            size++;
        }

        if (process[i].nodeName.toLowerCase().includes('event'.toLowerCase())) {
            size++;
        }

        if (process[i].nodeName.toLowerCase().includes('gateway'.toLowerCase())) {
            size++;
        }
    }

    let sizeChange = Math.min(31, size) - size;

    if (sizeChange !== 0) {
        recommendations.push('Remove ' + Math.abs(sizeChange) + ' element(s)');
    }

    let tasks = xmlDoc.getElementsByTagName(prefix + 'task');
    let tasksChange = Math.max(1, tasks.length) - tasks.length;

    if (tasksChange !== 0) {
        recommendations.push('Add ' + tasksChange + ' task');
    }
}

function eventsValidation(xmlDoc, recommendations) {
    let startEvents = xmlDoc.getElementsByTagName(prefix + 'startEvent');
    let endEvents = xmlDoc.getElementsByTagName(prefix + 'endEvent');

    let startEventsChange = 1 - startEvents.length;
    let endEventsChange = 1 - endEvents.length;

    if (startEventsChange !== 0) {
        if (startEventsChange > 0) {
            recommendations.push('Add ' + startEventsChange + ' start event');
        } else {
            recommendations.push('Remove ' + Math.abs(startEventsChange) + ' start event(s)');
        }
    }

    if (endEventsChange !== 0) {
        if (endEventsChange > 0) {
            recommendations.push('Add ' + endEventsChange + ' end event');
        } else {
            recommendations.push('Remove ' + Math.abs(endEventsChange) + ' end event(s)');
        }
    }
}

function gatewaysValidation(xmlDoc, recommendations) {
    let exclusiveGateways = xmlDoc.getElementsByTagName(prefix + 'exclusiveGateway');
    let parallelGateways = xmlDoc.getElementsByTagName(prefix + 'parallelGateway');

    let validateGatewaysOfType = function (gateways, recommendations) {
        for (let i = 0; i < gateways.length; i++) {
            let name = gateways[i].attributes[1] === undefined ? gateways[i].attributes[0].nodeValue :
                gateways[i].attributes[1].nodeValue;

            let incoming = 0;
            let outgoing = 0;

            let childNodes = gateways[i].childNodes;

            for (let j = 0; j < childNodes.length; j++) {
                if (childNodes[j].nodeName.toLowerCase().includes('incoming'.toLowerCase())) {
                    incoming++;
                }

                if (childNodes[j].nodeName.toLowerCase().includes('outgoing'.toLowerCase())) {
                    outgoing++;
                }
            }

            let routingChange = 3 - (incoming + outgoing);

            if (routingChange !== 0) {
                if (routingChange > 0) {
                    recommendations.push('Add ' + routingChange +
                        ' arc(s) to "' + name + '" gateway');
                } else {
                    recommendations.push('Remove ' + Math.abs(routingChange) +
                        ' arc(s) from "' + name + '" gateway');
                }
            }
        }
    }

    validateGatewaysOfType(exclusiveGateways, recommendations);
    validateGatewaysOfType(parallelGateways, recommendations);
}

function tasksValidation(xmlDoc, recommendations) {
    let tasks = xmlDoc.getElementsByTagName(prefix + 'task');

    for (let i = 0; i < tasks.length; i++) {
        let name = tasks[i].attributes[1] === undefined ? tasks[i].attributes[0].nodeValue :
            tasks[i].attributes[1].nodeValue;

        let taskIncoming = 0;
        let taskOutgoing = 0;

        let childNodes = tasks[i].childNodes;

        for (let j = 0; j < childNodes.length; j++) {
            if (childNodes[j].nodeName.toLowerCase().includes('incoming'.toLowerCase())) {
                taskIncoming++;
            }

            if (childNodes[j].nodeName.toLowerCase().includes('outgoing'.toLowerCase())) {
                taskOutgoing++;
            }
        }

        let taskIncomingChange = 1 - taskIncoming;

        if (taskIncomingChange !== 0) {
            if (taskIncomingChange > 0) {
                recommendations.push('Add ' + taskIncomingChange +
                    ' incoming arc(s) to "' + name + '" task');
            } else {
                recommendations.push('Remove ' + Math.abs(taskIncomingChange) +
                    ' incoming arc(s) from "' + name + '" task');
            }
        }

        let taskOutgoingChange = 1 - taskOutgoing;

        if (taskOutgoingChange !== 0) {
            if (taskOutgoingChange > 0) {
                recommendations.push('Add ' + taskOutgoingChange +
                    ' outgoing arc(s) to "' + name + '" task');
            } else {
                recommendations.push('Remove ' + Math.abs(taskOutgoingChange) +
                    ' outoing arc(s) from "' + name + '" task');
            }
        }
    }
}

function routingValidation(xmlDoc, recommendations) {
    let exclusiveGateways = xmlDoc.getElementsByTagName(prefix + 'exclusiveGateway');
    let parallelGateways = xmlDoc.getElementsByTagName(prefix + 'parallelGateway');

    let validateGatewaysOfType = function (gateways, gwType, recommendations) {
        let splits = 0;
        let joins = 0;

        for (let i = 0; i < gateways.length; i++) {
            let name = gateways[i].attributes[1] === undefined ? gateways[i].attributes[0].nodeValue :
                gateways[i].attributes[1].nodeValue;

            let incoming = 0;
            let outgoing = 0;

            let childNodes = gateways[i].childNodes;

            for (let j = 0; j < childNodes.length; j++) {
                if (childNodes[j].nodeName.toLowerCase().includes('incoming'.toLowerCase())) {
                    incoming++;
                }

                if (childNodes[j].nodeName.toLowerCase().includes('outgoing'.toLowerCase())) {
                    outgoing++;
                }
            }

            if (incoming === 1 && outgoing > 1) {
                splits++;
            }

            if (incoming > 1 && outgoing === 1) {
                joins++;
            }
        }

        let splitsChanges = joins - splits;
        let joinsChanges = splits - joins;

        if (splitsChanges !== 0 || joinsChanges !== 0) {
            let splitsAction = splitsChanges > 0 ? 'Add ' : 'Remove ';
            let joinsAction = joinsChanges > 0 ? 'add ' : 'remove ';

            recommendations.push(splitsAction + ' ' + Math.abs(splitsChanges) + ' ' + gwType +
                '-split gateway(s) or ' + joinsAction + ' ' + Math.abs(joinsChanges) + ' ' + gwType +
                '-join gateway(s)');
        }
    }

    validateGatewaysOfType(exclusiveGateways, 'XOR', recommendations);
    validateGatewaysOfType(parallelGateways, 'AND', recommendations);
}

function branchingValidation(xmlDoc, recommendations) {
    let inclusiveGateways = xmlDoc.getElementsByTagName(prefix + 'inclusiveGateway');

    let inclusiveGatewaysChange = 0 - inclusiveGateways.length;

    if (inclusiveGatewaysChange !== 0) {
        recommendations.push('Remove ' + Math.abs(inclusiveGatewaysChange) + ' OR gateways(s)');
    }
}
