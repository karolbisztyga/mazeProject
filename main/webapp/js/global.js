$(document).ready(function(){
    /*for(var i=0,as=$("a") ; i<as.length ; ++i) {
        var href = $(as[i]).attr("href");
        var delimiter = (href.indexOf("?")!==-1)?"&":"?";
        //$(as[i]).attr("href", href+delimiter+"cache="+Math.floor(Math.random()*10000000000000));
    }*/
    
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
            //logInLink.attr("class","glyphicon glyphicon-off");
            logInLink.addClass("glyphicon");
            if(loggedIn) {
                logInLink.addClass("glyphicon-off");
            } else {
                logInLink.addClass("glyphicon-user");
            }
            logInLink.attr("id","login-div");
            $(document.body).append(logInLink);
        }
    });
    
});