$(document).ready(function(){
    
    var from = 0;
    var to = 10;
    
    $("#load-more-button").click(function(){
        $.ajax({
            url: "getMazes",
            type: "POST",
            data: {
                scope: "all",
                from: from,
                to: to
            },
            success: function(result) {
                if(result['type']==='error') {
                    $("#mazes-error-div").html(result['type'] + ": " + result['message']);
                    return;
                }
                for(var i in result) {
                    if(i==='type')continue;
                    addMaze(result[i]);
                }
            }
        });
    });
    
    function addMaze(maze) {
        var div = $(document.createElement("div"));
        div.attr("class","maze col-xs-12");
        
        var a = $(document.createElement("a"));
        a.attr("href","maze?id="+maze['id']);
        a.attr("class","play-icon glyphicon glyphicon-play");
        
        div.html("author: " + maze['author'] + "<br>size: " + maze['width']+" x "+maze['height']);
        
        div.append(a);
        
        $("#mazes-wrapper").prepend(div);
        console.log(maze);
    }
    
    $("#load-more-button").click();
    
});