var pathname = window.location.pathname;

if (pathname !== '/login.html') {
    if (typeof($.cookie('user')) === 'undefined') {
        window.location.href = '/login.html';
    } else {
        var user = $.cookie('user');
        var password = $.cookie('pass');

        $.getJSON('login/' + user, function(data) {
            if (data == null || password != data.password) {
                $.removeCookie('user');
                $.removeCookie('pass');
                $.removeCookie('prof');

                window.location.href = '/login.html';
            } else {
                var userLink = document.getElementById('userLink');
                userLink.href = $.cookie('prof');
                userLink.innerHTML = $.cookie('user');
            }
        });
    }
} else {
    $.removeCookie('user');
    $.removeCookie('pass');
    $.removeCookie('prof');
}

function login() {
    var user = $('#username').val();
    var password = $('#password').val();

    $.getJSON('login/' + user + '/' + password, function(data) {
        console.log(data);

        if (data != null) {
            $.cookie('user', data.login);
            $.cookie('pass', data.password);
            $.cookie('prof', data.profile);

            window.location.href = '/';
        } else {
            $('#login-message').text('Invalid user name or password!');
        }
    });

    return false;
}
