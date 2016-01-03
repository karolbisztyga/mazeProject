$(document).ready(function(){
    
    //var isEdited = false;
    var mazeFieldSize = 100;
    var fields = [];
    var items = [];
    /*
    var edgesSettings = {
        //topEdge: "top",
        rightEdge: "right",
        bottomEdge: "bottom",
        //leftEdge: "left"
    };
    */
    (function(){
        resize();
        $("#generate-height").val("5");
        $("#generate-width").val("5");
        if(getCookie("maze")!==false) {
            $("#load").css("display","initial");
        }
    })();
    
    function resize() {
        var h = $(window).innerHeight()-200;
        $("#map-wrapper").css("height",h+"px");
    }
    
    $(window).resize(resize);
    
    var mazeElementsCodes = {
        floor: 0,
        start: 1,
        finish: 2,
        trap: 3,
        hole: 4,
        portal: 5,
        /*empty: 6,
        wall: 7,*/
        door: 8,
        secretDoor: 9,
    };
    
    var edgeCodes = {
        empty: 0,
        wall: 1
    };
    
    
    /**
     * field object: {
     *      col:,
     *      row:,
     *      topEdge:,
     *      rightEdge:,
     *      bottomEdge:,
     *      leftEdge:,
     *      
     * }
     */
    
    function assignItemToField(itemCode, field) {
        
    }
    
    function giveItemBack() {
        
    }
    
    $("#generate-button").click(function(){
        var width = $("#generate-width").val();
        var height = $("#generate-height").val();
        
        for(var i=0 ; i<height ; ++i) {
            fields[fields.length] = [];
            for(var j=0 ; j<width ; ++j) {
                fields[i][fields[i].length] = {
                    col:i,
                    row:j,
                    //topEdge:mazeElementsCodes.empty,
                    rightEdge:edgeCodes.empty,
                    bottomEdge:edgeCodes.empty,
                    //leftEdge:mazeElementsCodes.empty,
                };
                /*if(j===0) {
                    fields[i][j].leftEdge = edgeCodes.wall;
                } else if(j===width-1) {
                    fields[i][j].rightEdge = edgeCodes.wall;
                }
                if(i===0) {
                    fields[i][j].topEdge = edgeCodes.wall;
                } else if(i===height-1) {
                    fields[i][j].bottomEdge = edgeCodes.wall;
                }*/
            }
        }
        refreshBoard();
        //isEdited = true;
        $("#save").css("display","initial");
    });
    
    function generateItems() {
        for(var item in items) {
            var div = $(document.createElement("div"));
            div.className = "col-xs-4 item-wrapper";
            var btn = $(document.createElement("input"));
            btn.attr("type","button");
            btn.attr("value","...");
            //...
        }
    }
    
    function refreshBoard() {
        $("#map-wrapper").empty();
        $("#map-wrapper").css("width",(fields[0].length*mazeFieldSize)+"px");
        $("#map-wrapper").css("height",(fields.length*mazeFieldSize)+"px");
        $("#map-wrapper").css("display","block");
        for(var i=0 ; i<fields.length ; ++i) {
            var rowDiv = $(document.createElement("div"));
            rowDiv.addClass("maze-fields-row");
            rowDiv.css("width",(fields[i].length*mazeFieldSize)+"px");
            rowDiv.css("height",(mazeFieldSize)+"px");
            for(var j=0 ; j<fields[i].length ; ++j) {
                var div = $(document.createElement("div"));
                div.attr("id","maze-field-"+i+"-"+j);
                div.addClass("maze-field");
                
                if(i>0) {
                    div.append(createWallEdit(i,j,"top"));
                }
                if(i<fields.length-1) {
                    div.append(createWallEdit(i,j,"bottom"));
                }
                if(j>0) {
                    div.append(createWallEdit(i,j,"left"));
                }
                if(j<fields[i].length-1) {
                    div.append(createWallEdit(i,j,"right"));
                }
                
                //adding wall builders
                /*
                var topWallBuilder = $(document.createElement("div"));
                topWallBuilder.addClass("wall-builder");
                topWallBuilder.css("width",(mazeFieldSize/2)+"px");
                topWallBuilder.css("height","20px");
                topWallBuilder.click(function(){
                    
                });
                div.append(topWallBuilder);*/
                
                //click event
                (function(i,j){
                    div.click(function(){
                        for(var i=0 ; i<items.length ; ++i) {
                            
                        }
                    });
                })(i,j);
                
                setEdges(fields[i][j], div);
                
                rowDiv.append(div);
            }
            $("#map-wrapper").append(rowDiv);
        }
    }
    
    function createWallEdit(i,j,side) {
        var div = $(document.createElement("div"));
        div.addClass("maze-field-wall-edit");
        div.addClass("maze-field-wall-edit-"+side);
        div.click(function(){
            //isEdited = true;
            switch(side) {
                case "top":
                    fields[i-1][j].bottomEdge = (fields[i-1][j].bottomEdge===edgeCodes.empty)
                    ? edgeCodes.wall 
                    : edgeCodes.empty;
                    break;
                case "right":
                    fields[i][j].rightEdge = (fields[i][j].rightEdge===edgeCodes.empty)
                    ? edgeCodes.wall 
                    : edgeCodes.empty;
                    break;
                case "bottom":
                    fields[i][j].bottomEdge = (fields[i][j].bottomEdge===edgeCodes.empty)
                    ? edgeCodes.wall 
                    : edgeCodes.empty;
                    break;
                case "left":
                    fields[i][j-1].rightEdge = (fields[i][j-1].rightEdge===edgeCodes.empty)
                    ? edgeCodes.wall 
                    : edgeCodes.empty;
                    break;
            }
            refreshBoard();
        });
        return div;
    }
    
    function setEdges(element, div) {
        var edges = {
            rightEdge: "right",
            bottomEdge: "bottom"
        };
        for(var i in edges) {
            switch(element[i]) {
                case edgeCodes.empty:
                    break;
                case edgeCodes.wall:
                    div.css("border-"+edges[i],"solid 3px #000");
                    break;
                //case: ...
            }
        }
    }
    /*
    function clickField(i,j) {
        //edges options
        $(".edge-switch").empty();
        if(j>0) {
            //add select left edge
            $("#edge-switch-left").append(generateEdgeSelect(i,j,"leftEdge"));
        } else {
            //unchangable value
            $("#edge-switch-left").append(getKey(mazeElementsCodes,fields[i][j].leftEdge));
        }
        if(j<fields[0].length-1) {
            //add select right edge
            $("#edge-switch-right").append(generateEdgeSelect(i,j,"rightEdge"));
        } else {
            //unchangable value
            $("#edge-switch-right").append(getKey(mazeElementsCodes,fields[i][j].rightEdge));
        }
        if(i>0) {
            //add select top edge
            $("#edge-switch-top").append(generateEdgeSelect(i,j,"topEdge"));
        } else {
            $("#edge-switch-top").append(getKey(mazeElementsCodes,fields[i][j].topEdge));
            //unchangable value
        }
        if(i<fields.length-1) {
            //add select bottom edge
            $("#edge-switch-bottom").append(generateEdgeSelect(i,j,"bottomEdge"));
        } else {
            //unchangable value
            $("#edge-switch-bottom").append(getKey(mazeElementsCodes,fields[i][j].bottomEdge));
        }
        $("#field-manager-wrapper").css("opacity",1);
    }
    /*
    function generateEdgeSelect(i,j,flag) {
        var select = $(document.createElement("select"));
        select.append("<option value='"+ mazeElementsCodes.empty +"'>empty</option>");
        select.append("<option value='"+ mazeElementsCodes.wall +"'>wall</option>");
        select.change(function(){
            fields[i][j][flag] = parseInt($(this).val());
            refreshBoard();
        });
        return select;
    }
    */
    function getKey(obj, value) {
        for(var i in obj) {
            if(obj[i]===value) {
                return i;
            }
        }
        return false;
    }
    /*
    function saveBeforeLeave() {
        if(!isEdited) {
            return;
        }
        if(confirm("Do You want to save changes?")) {
            $("#save").click();
        }
        $("#map-wrapper").empty();
        fields = [];
    }
    */
    $("#save").click(function(){
        setCookie("maze",JSON.stringify(fields));
    });
    
    $("#load").click(function(){
        fields = JSON.parse(getCookie("maze"));
        refreshBoard();
        $("#save").css("display","initial");
    });
    
    function setCookie(name, value) {
        document.cookie = name+"="+value;
    }
    
    function getCookie(name) {
        var cookies = document.cookie.split(";");
        for(var i=0; i<cookies.length ; ++i) {
            var cookieArr = cookies[i].split("=");
            if(cookieArr[0]===name) {
                return cookieArr[1];
            }
        }
        return false;
    }
    
});