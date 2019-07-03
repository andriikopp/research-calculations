var labelPattern = /^[a-zA-Z\-_\[\]\.0-9\s]+$/;

$('#save').click(function() {
    if (confirm('Store business process model?')) {
        var modelId = $("#modelId").val();
        var modelName = $("#modelName").val();
        var modelLevel = $("#modelLevel").val();
        var modelText = $("#modelText").val();

        if (modelName == null || modelName.length == 0 || !labelPattern.test(modelName)) {
            alert('Invalid model name!');
        } else {
            var bpModel = {
                id: modelId,
                name: modelName,
                level: modelLevel,
                description: modelText
            };

            $.ajax({
                type: "POST",
                url: "/edit",
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
        $('#modelId').val(modelId);
        $('#modelName').val(data.name);
        $('#modelNotation').val(data.notation).change();
        $('#modelLevel').val(data.level).change();
        $('#modelText').append(data.description);
        $('#modelFile').val(data.fileName);
    });
}
