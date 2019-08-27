var editor = ace.edit("editor");
editor.setTheme("ace/theme/eclipse");
editor.session.setMode("ace/mode/xml");

var viewer = null;

$(document).ready(function () {
    $('#zoombuttons').hide();
    $('#canvas').hide();

    if (localStorage.getItem('editor')) {
        editor.insert(localStorage.getItem('editor'));

        let bpmnXML = editor.getValue();

        defineXMLNamespace(bpmnXML);
    }

    $('#editor').keyup(function () {
        let bpmnXML = editor.getValue();

        localStorage.setItem('editor', bpmnXML);

        defineXMLNamespace(bpmnXML);
    });

    $('#expand').click(function () {
        $('#editor').height(370);

        editor.resize();
    });

    $('#collapse').click(function () {
        $('#editor').height(170);

        editor.resize();
    });

    $('#clear').click(function () {
        editor.setValue('');
        localStorage.setItem('editor', '');
    });

    $('#paste').click(function () {
        navigator.clipboard.readText().then(clipText => {
            editor.setValue('');
            editor.insert(clipText);

            localStorage.setItem('editor', clipText);

            let bpmnXML = editor.getValue();

            defineXMLNamespace(bpmnXML);
        });
    });

    $('#copy').click(function () {
        navigator.clipboard.writeText(editor.getValue()).then(clipText => {
        });
    });

    $('#zoomin').click(function () {
        resizeCanvas(50);
    });

    $('#zoomout').click(function () {
        resizeCanvas(-50);
    });

    $('#analyzeDoc').click(function () {
        $('#zoombuttons').show();
        $('#canvas').show();

        $('#canvas').empty();

        let bpmnXML = editor.getValue();

        let prefix = $('#bpmnPrefix').val();

        if (prefix.length > 1) {
            prefix = prefix + ':';
        }

        viewer = new BpmnJS({ container: '#canvas' });

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

                bpmnValidation(xmlDoc, recommendations, prefix);

                for (let value in recommendations) {
                    $('#recommendations').append('<div class="alert alert-danger">' +
                        recommendations[value] + '</div>');
                }
            }
        });
    });
});

function defineXMLNamespace(bpmnXML) {
    if (bpmnXML.includes('<definitions')) {
        $('#bpmnPrefix').val('');
    } else {
        let matched = bpmnXML.match(/<[a-z]*:definitions/gi);
        let xmlns = matched[0];

        xmlns = xmlns.replace('<', '').replace(':definitions', '');

        $('#bpmnPrefix').val(xmlns);
    }
}

function resizeCanvas(change) {
    let height = parseInt($('#canvas').height());

    if (height > 400 || (height >= 400 && change > 0)) {
        $('#canvas').height(height + change);

        let bpmnXML = editor.getValue();

        viewer.importXML(bpmnXML, function (err) {
            if (err) {
                $('#canvas').append('<div class="alert alert-danger">' + err + '</div>');
            } else {
                let canvas = viewer.get('canvas');

                canvas.zoom('fit-viewport');
            }
        });
    }
}

function bpmnValidation(xmlDoc, recommendations, prefix) {
    let processList = xmlDoc.getElementsByTagName(prefix + 'process');

    for (let k = 0; k < processList.length; k++) {
        let startEvents = 0;
        let endEvents = 0;

        let inclusiveGateways = 0;

        let splits = {};
        let joins = {};

        let process = processList[k].childNodes;

        for (let i = 0; i < process.length; i++) {
            if (process[i].nodeName.toLowerCase().includes('task'.toLowerCase()) ||
                process[i].nodeName.toLowerCase().includes('subProcess'.toLowerCase())) {
                let name = process[i].attributes['name'] === undefined ?
                    process[i].attributes['id'].nodeValue :
                    process[i].attributes['name'].nodeValue;
                name = name === '' ? process[i].attributes['id'].nodeValue : name;

                let incoming = 0;
                let outgoing = 0;

                for (let j = 0; j < process[i].childNodes.length; j++) {
                    if (process[i].childNodes[j].nodeName.toLowerCase().includes('incoming'.toLowerCase())) {
                        incoming++;
                    }

                    if (process[i].childNodes[j].nodeName.toLowerCase().includes('outgoing'.toLowerCase())) {
                        outgoing++;
                    }
                }

                let incomingChange = 1 - incoming;

                if (incomingChange !== 0) {
                    if (incomingChange > 0) {
                        recommendations.push('Add ' + incomingChange +
                            ' incoming arc(s) to "' + name + '" task');
                    } else {
                        recommendations.push('Remove ' + Math.abs(incomingChange) +
                            ' incoming arc(s) from "' + name + '" task');
                    }
                }

                let outgoingChange = 1 - outgoing;

                if (outgoingChange !== 0) {
                    if (outgoingChange > 0) {
                        recommendations.push('Add ' + outgoingChange +
                            ' outgoing arc(s) to "' + name + '" task');
                    } else {
                        recommendations.push('Remove ' + Math.abs(outgoingChange) +
                            ' outoing arc(s) from "' + name + '" task');
                    }
                }
            }

            if (process[i].nodeName.includes('Event')) {
                let name = process[i].attributes['name'] === undefined ?
                    process[i].attributes['id'].nodeValue :
                    process[i].attributes['name'].nodeValue;
                name = name === '' ? process[i].attributes['id'].nodeValue : name;

                let incoming = 0;
                let outgoing = 0;

                for (let j = 0; j < process[i].childNodes.length; j++) {
                    if (process[i].childNodes[j].nodeName.toLowerCase().includes('incoming'.toLowerCase())) {
                        incoming++;
                    }

                    if (process[i].childNodes[j].nodeName.toLowerCase().includes('outgoing'.toLowerCase())) {
                        outgoing++;
                    }
                }

                let inIdeal = 1;

                if (process[i].nodeName.toLowerCase().includes('startEvent'.toLowerCase())) {
                    inIdeal = 0;

                    startEvents++;
                }

                let incomingChange = inIdeal - incoming;

                if (incomingChange !== 0) {
                    if (incomingChange > 0) {
                        recommendations.push('Add ' + incomingChange +
                            ' incoming arc(s) to "' + name + '" event');
                    } else {
                        recommendations.push('Remove ' + Math.abs(incomingChange) +
                            ' incoming arc(s) from "' + name + '" event');
                    }
                }

                let outIdeal = 1;

                if (process[i].nodeName.toLowerCase().includes('endEvent'.toLowerCase())) {
                    outIdeal = 0;

                    endEvents++;
                }

                let outgoingChange = outIdeal - outgoing;

                if (outgoingChange !== 0) {
                    if (outgoingChange > 0) {
                        recommendations.push('Add ' + outgoingChange +
                            ' outgoing arc(s) to "' + name + '" event');
                    } else {
                        recommendations.push('Remove ' + Math.abs(outgoingChange) +
                            ' outoing arc(s) from "' + name + '" event');
                    }
                }
            }

            if (process[i].nodeName.toLowerCase().includes('gateway'.toLowerCase())) {
                let name = process[i].attributes['name'] === undefined ?
                    process[i].attributes['id'].nodeValue :
                    process[i].attributes['name'].nodeValue;
                name = name === '' ? process[i].attributes['id'].nodeValue : name;

                let incoming = 0;
                let outgoing = 0;

                for (let j = 0; j < process[i].childNodes.length; j++) {
                    if (process[i].childNodes[j].nodeName.toLowerCase().includes('incoming'.toLowerCase())) {
                        incoming++;
                    }

                    if (process[i].childNodes[j].nodeName.toLowerCase().includes('outgoing'.toLowerCase())) {
                        outgoing++;
                    }
                }

                if (incoming === 1 && outgoing > 1) {
                    if (splits[process[i].nodeName] === undefined) {
                        splits[process[i].nodeName] = 1;
                    } else {
                        let old = splits[process[i].nodeName];
                        splits[process[i].nodeName] = old + 1;
                    }

                    if (joins[process[i].nodeName] === undefined) {
                        joins[process[i].nodeName] = 0;
                    }
                }

                if (incoming > 1 && outgoing === 1) {
                    if (joins[process[i].nodeName] === undefined) {
                        joins[process[i].nodeName] = 1;
                    } else {
                        let old = joins[process[i].nodeName];
                        joins[process[i].nodeName] = old + 1;
                    }

                    if (splits[process[i].nodeName] === undefined) {
                        splits[process[i].nodeName] = 0;
                    }
                }

                if (process[i].nodeName.toLowerCase().includes('inclusiveGateway'.toLowerCase())) {
                    inclusiveGateways++;
                }

                let change = 3 - (incoming + outgoing);

                if (change !== 0) {
                    if (change > 0) {
                        recommendations.push('Add ' + change +
                            ' arc(s) to "' + name + '" gateway');
                    } else {
                        recommendations.push('Remove ' + Math.abs(change) +
                            ' arc(s) from "' + name + '" gateway');
                    }
                }
            }
        }

        let startEventsChange = 1 - startEvents;

        if (startEventsChange !== 0) {
            recommendations.push('Remove ' + Math.abs(startEventsChange) + ' start event(s)');
        }

        let endEventsChange = 1 - endEvents;

        if (endEventsChange !== 0) {
            recommendations.push('Remove ' + Math.abs(endEventsChange) + ' end event(s)');
        }

        let inclusiveGatewaysChange = 0 - inclusiveGateways;

        if (inclusiveGatewaysChange !== 0) {
            recommendations.push('Remove ' + Math.abs(inclusiveGatewaysChange) + ' inclusive gateways(s)');
        }

        for (var key in splits) {
            if (splits.hasOwnProperty(key) && joins.hasOwnProperty(key)) {
                let splitsChanges = joins[key] - splits[key];
                let joinsChanges = splits[key] - joins[key];

                if (splitsChanges !== 0 || joinsChanges !== 0) {
                    let splitsAction = splitsChanges > 0 ? 'Add ' : 'Remove ';
                    let joinsAction = joinsChanges > 0 ? 'add ' : 'remove ';

                    recommendations.push(splitsAction + ' ' + Math.abs(splitsChanges) + ' ' + key +
                        '-split gateway(s) or ' + joinsAction + ' ' + Math.abs(joinsChanges) + ' ' + key +
                        '-join gateway(s)');
                }
            }
        }

        if (recommendations.length === 0) {
            $('#recommendations').append('<div class="alert alert-info">' +
                'No changes required</div>');
        }
    }
}
