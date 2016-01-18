$(document).ready(function(){
    
    $.ajax({
        url: "getUserData",
        type: "POST",
        data: {
            requestType:"ajax"
        },
        success: function(result){
            //console.log(result);
            var loggedIn = result["loggedIn"];
            
            var logInLink = $(document.createElement("a"));
            var href = (loggedIn) ? "logout" : "login" ;
            logInLink.attr("href",href);
            logInLink.addClass("glyphicon");
            if(loggedIn) {
                logInLink.addClass("glyphicon-off");
            } else {
                logInLink.addClass("glyphicon-user");
            }
            logInLink.attr("id","login-div");
            $(document.body).append(logInLink);
            if(loggedIn) {
                var dataDiv = $(document.createElement("div"));
                dataDiv.attr("id","data-div");
                dataDiv.html("Hello <i>" + result['name'] + "</i>[" + result['money']+"$]");
                $(document.body).append(dataDiv);
            }
        }
    });
    
});