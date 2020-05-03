var editor = ace.edit("editor");
editor.setTheme("ace/theme/eclipse");
editor.session.setMode("ace/mode/xml");

var viewer = null;

$(document).ready(function() {
    if (Cookies.get('hidewarn')) {
        $('#warning').hide();
    }

    $('#zoombuttons').hide();
    $('#canvas').hide();

    let bpmnLinkParam = getParameterByName('doc');

    if (bpmnLinkParam) {
        localStorage.setItem('editor', '');
        localStorage.setItem('link', '');

        $('#bpmnLink').val(bpmnLinkParam);

        loadDocumentByLink();
    }

    if (localStorage.getItem('editor')) {
        editor.insert(localStorage.getItem('editor'));

        let bpmnXML = editor.getValue();

        defineXMLNamespace(bpmnXML);
    }

    if (localStorage.getItem('link')) {
        $('#bpmnLink').val(localStorage.getItem('link'));
    }

    $('#bpmnLink').keyup(function() {
        loadDocumentByLink();
    });

    $('#editor').keyup(function() {
        let bpmnXML = editor.getValue();

        localStorage.setItem('editor', bpmnXML);

        defineXMLNamespace(bpmnXML);
    });

    $('#expand').click(function() {
        $('#editor').height(370);

        editor.resize();
    });

    $('#collapse').click(function() {
        $('#editor').height(170);

        editor.resize();
    });

    $('#clear').click(function() {
        editor.setValue('');
        localStorage.setItem('editor', '');

        $('#bpmnLink').val('');
        localStorage.setItem('link', '');
    });

    $('#paste').click(function() {
        navigator.clipboard.readText().then(clipText => {
            editor.setValue('');
            editor.insert(clipText);

            localStorage.setItem('editor', clipText);

            let bpmnXML = editor.getValue();

            defineXMLNamespace(bpmnXML);

            $('#bpmnLink').val('');
            localStorage.setItem('link', '');
        });
    });

    $('#copy').click(function() {
        navigator.clipboard.writeText(editor.getValue()).then(clipText => {});
    });

    $('#zoomin').click(function() {
        resizeCanvas(50);
    });

    $('#zoomout').click(function() {
        resizeCanvas(-50);
    });

    $('#reload').click(function() {
        let bpmnLink = $('#bpmnLink').val();

        if (bpmnLink === null || bpmnLink === '') {
            window.location.href = './js-bpmai.html';
        } else {
            window.location.href = './js-bpmai.html?doc=' + bpmnLink;
        }
    });

    $('#analyzeDoc').click(function() {
        $('#zoombuttons').show();
        $('#canvas').show();

        $('#canvas').empty();

        let bpmnXML = editor.getValue();

        let prefix = $('#bpmnPrefix').val();

        if (prefix.length > 1) {
            prefix = prefix + ':';
        }

        viewer = new BpmnJS({ container: '#canvas' });

        viewer.importXML(bpmnXML, function(err) {
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

                bpmnValidation(xmlDoc, prefix);
            }
        });
    });

    $('#closewarn').click(function() {
        Cookies.set('hidewarn', 'true');

        $('#warning').hide();
    });
});

function loadDocumentByLink() {
    let bpmnLink = $('#bpmnLink').val();

    if (bpmnLink.length === 0) {
        localStorage.setItem('link', '');
    } else {
        $.get(bpmnLink, function(data) {
            editor.setValue('');
            editor.insert(data);

            localStorage.setItem('editor', data);
            localStorage.setItem('link', bpmnLink);

            defineXMLNamespace(data);
        });
    }
}

function defineXMLNamespace(bpmnXML) {
    if (bpmnXML.includes('<definitions')) {
        $('#bpmnPrefix').val('');
    } else {
        let matched = bpmnXML.match(/<[a-z]*:definitions/gi);

        if (matched !== null && matched.length > 0) {
            let xmlns = matched[0];

            xmlns = xmlns.replace('<', '').replace(':definitions', '');

            $('#bpmnPrefix').val(xmlns);
        }
    }
}

function resizeCanvas(change) {
    let height = parseInt($('#canvas').height());

    if (height > 400 || (height >= 400 && change > 0)) {
        $('#canvas').height(height + change);

        let bpmnXML = editor.getValue();

        viewer.importXML(bpmnXML, function(err) {
            if (err) {
                $('#canvas').append('<div class="alert alert-danger">' + err + '</div>');
            } else {
                let canvas = viewer.get('canvas');

                canvas.zoom('fit-viewport');
            }
        });
    }
}

function bpmnValidation(xmlDoc, prefix) {
    let processList = xmlDoc.getElementsByTagName(prefix + 'process');

    for (let k = 0; k < processList.length; k++) {
        let splits = {};
        let joins = {};

        let process = processList[k].childNodes;

        let processName = processList[k].attributes['name'] === undefined ?
            processList[k].attributes['id'].nodeValue :
            processList[k].attributes['name'].nodeValue;
        processName = processName === '' ? processList[k].attributes['id'].nodeValue : processName;

        let warnings = {
            invalidTasks: 0,
            invalidEvents: 0,
            gatewaysMismatch: 0,
            startEvents: 0,
            endEvents: 0,
            inclusiveGateways: 0,
            uncertainGateways: 0,

            validate: function() {
                return this.invalidTasks === 0 && this.invalidEvents === 0 && this.gatewaysMismatch === 0 &&
                    this.startEvents === 1 && this.endEvents === 1 && this.inclusiveGateways === 0 &&
                    this.uncertainGateways === 0;
            }
        }

        $('#recommendations').append('<div class="alert alert-light">' +
            'Process <b>"' + processName + '"</b>' + '</div>');

        for (let i = 0; i < process.length; i++) {
            // [start] Tasks analysis
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

                if (incoming !== 1 || outgoing !== 1) {
                    warnings.invalidTasks++;
                }

                if (incoming < 1) {
                    $('#recommendations').append('<div class="alert alert-danger">' +
                        'Task <b>"' + name + '"</b> does not have any incoming flows' + '</div>');
                }

                if (incoming > 1) {
                    $('#recommendations').append('<div class="alert alert-danger">' +
                        'Task <b>"' + name + '"</b> has multiple incoming flows' + '</div>');
                }

                if (outgoing < 1) {
                    $('#recommendations').append('<div class="alert alert-danger">' +
                        'Task <b>"' + name + '"</b> does not have any outgoing flows' + '</div>');
                }

                if (outgoing > 1) {
                    $('#recommendations').append('<div class="alert alert-danger">' +
                        'Task <b>"' + name + '"</b> has multiple outgoing flows' + '</div>');
                }
            }
            // [end] Tasks analysis

            // [start] Events analysis
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

                if (process[i].nodeName.toLowerCase().includes('startEvent'.toLowerCase())) {
                    warnings.startEvents++;
                } else if (process[i].nodeName.toLowerCase().includes('endEvent'.toLowerCase())) {
                    warnings.endEvents++;
                } else {
                    if (incoming !== 1 || outgoing !== 1) {
                        warnings.invalidEvents++;
                    }

                    if (incoming < 1) {
                        $('#recommendations').append('<div class="alert alert-danger">' +
                            'Event <b>"' + name + '"</b> does not have any incoming flows' + '</div>');
                    }

                    if (incoming > 1) {
                        $('#recommendations').append('<div class="alert alert-danger">' +
                            'Event <b>"' + name + '"</b> has multiple incoming flows' + '</div>');
                    }

                    if (outgoing < 1) {
                        $('#recommendations').append('<div class="alert alert-danger">' +
                            'Event <b>"' + name + '"</b> does not have any outgoing flows' + '</div>');
                    }

                    if (outgoing > 1) {
                        $('#recommendations').append('<div class="alert alert-danger">' +
                            'Event <b>"' + name + '"</b> has multiple outgoing flows' + '</div>');
                    }
                }
            }
            // [end] Events analysis

            // [start] Gateways analysis
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
                } else if (incoming > 1 && outgoing === 1) {
                    if (joins[process[i].nodeName] === undefined) {
                        joins[process[i].nodeName] = 1;
                    } else {
                        let old = joins[process[i].nodeName];
                        joins[process[i].nodeName] = old + 1;
                    }

                    if (splits[process[i].nodeName] === undefined) {
                        splits[process[i].nodeName] = 0;
                    }
                } else {
                    warnings.uncertainGateways++;
                }

                if (process[i].nodeName.toLowerCase().includes('inclusiveGateway'.toLowerCase())) {
                    warnings.inclusiveGateways++;
                }
            }
        }

        if (warnings.inclusiveGateways > 0) {
            $('#recommendations').append('<div class="alert alert-danger">' +
                'Process contains <b>inclusive gateways</b>' + '</div>');
        }

        if (warnings.uncertainGateways > 0) {
            $('#recommendations').append('<div class="alert alert-danger">' +
                'Process contains <b>uncertain gateways</b> (neither splits nor joins)' + '</div>');
        }

        for (var key in splits) {
            if (splits.hasOwnProperty(key) && joins.hasOwnProperty(key)) {
                let gatewaysMismatch = splits[key] - joins[key];

                warnings.gatewaysMismatch += Math.abs(gatewaysMismatch);

                if (Math.abs(gatewaysMismatch) > 0) {
                    $('#recommendations').append('<div class="alert alert-danger">' +
                        'Mismatch of <b>' + key.replace('G', ' g') + 's</b>' + '</div>');
                }
            }
        }
        // [end] Gateways analysis

        if (warnings.startEvents < 1) {
            $('#recommendations').append('<div class="alert alert-danger">' +
                'Process does not have any <b>start events</b>' + '</div>');
        }

        if (warnings.startEvents > 1) {
            $('#recommendations').append('<div class="alert alert-danger">' +
                'Process has multiple <b>start events</b>' + '</div>');
        }

        if (warnings.endEvents < 1) {
            $('#recommendations').append('<div class="alert alert-danger">' +
                'Process does not have any <b>end events</b>' + '</div>');
        }

        if (warnings.endEvents > 1) {
            $('#recommendations').append('<div class="alert alert-danger">' +
                'Process has multiple <b>end events</b>' + '</div>');
        }

        if (warnings.validate()) {
            $('#recommendations').append('<div class="alert alert-info">' +
                'No warnings found</div>');
        }
    }
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;

    name = name.replace(/[\[\]]/g, '\\$&');

    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);

    if (!results) return null;
    if (!results[2]) return '';

    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}