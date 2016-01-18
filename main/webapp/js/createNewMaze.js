$(document).ready(function(){
    
    var isEdited = false;
    var mazeFieldSize = 100;
    var fields = [];
    var userMoney = false;
    var items = [
        {
            label:"START",
            sign:"S",
            price: 5
        },
        {
            label:"FINISH",
            sign:"F",
            price: 5
        },
        {
            label:"TRAP",
            sign:"T",
            price: 50
        },
        {
            label:"PORTAL",
            sign:"P",
            price: 100
        }
    ];
    var edgeMaterials = [
        {
            label: "empty",
            price: 0
        },
        {
            label: "wall",
            price: .25
        },
        {
            label: "door",
            price: 5
        },
        {
            label: "secret door",
            price: 70
        }
    ];
    var holdedItemIndex = -1;
    var holdedMaterialIndex = 0;
    
    /**
     * field object: {
     *      r:,
     *      c:,
     *      re:,
     *      be:,
     *      i:,
     * }
     */
    
    (function(){
        //resize();
        $("#generate-height").val("5");
        $("#generate-width").val("5");
        if(getCookie("maze")!==false) {
            fields = JSON.parse(getCookie("maze"));
            drawBoard();
            saveState();
            updatePrice();
        }
        generateItems();
    })();
    
    function updatePrice() {
        var price = 0;
        for(var i=0 ; i<fields.length ; ++i) {
            for(var j=0 ; j<fields[i].length ; ++j) {
                var field = fields[i][j];
                price += edgeMaterials[field["re"]]["price"];
                price += edgeMaterials[field["be"]]["price"];
                if(field["i"] !== 'f') {
                    var itemPrice = getItemPrice(field["i"]);
                    price = (itemPrice !== false) ? price+itemPrice : price ;
                }
            }
        }
        $("#current-cost").html(parseInt(price) + "/unknown");
    }
    
    function getItemPrice(sign) {
        for(var i=0 ; i<items.length ; ++i) {
            var item = items[i];
            if(item["sign"] === sign) {
                return item["price"];
            }
        }
        return false;
    }
    
    $("#generate-button").click(function(){
        var width = $("#generate-width").val();
        var height = $("#generate-height").val();
        fields = [];
        for(var i=0 ; i<width ; ++i) {
            fields[fields.length] = [];
            for(var j=0 ; j<height ; ++j) {
                fields[i][fields[i].length] = {
                    c:i,
                    r:j,
                    re:0,
                    be:0,
                    i: 'f'
                };
            }
        }
        drawBoard();
    });
    
    function generateItems() {
        $("#field-manager").empty();
        for(var i=0 ; i<items.length ; ++i) {
            addItemToToolbar(i);
        }
        for(var i=0 ; i<edgeMaterials.length ; ++i) {
            addEdgeMaterialToToolbar(i);
        }
    }
    
    function addEdgeMaterialToToolbar(i) {
        var material = edgeMaterials[i];
        var wrapper = $(document.createElement("div"));
        wrapper.attr("id","material-"+i);
        wrapper.attr("class","col-xs-3 material-wrapper");
        var materialDiv = $(document.createElement("div"));
        materialDiv.addClass("material");
        materialDiv.html(material.label);
        (function(i){
            materialDiv.click(function(e){
                $(".material-selected").removeClass("material-selected");
                $(e.target).addClass("material-selected");
                holdedMaterialIndex = i;
            });
        })(i);
        wrapper.append(materialDiv);
        $("#edge-manager").append(wrapper);
    }
    
    function addItemToToolbar(i) {
        var item = items[i];
        var wrapper = $(document.createElement("div"));
        wrapper.attr("id","item-"+i);
        wrapper.attr("class","col-xs-3 item-wrapper");
        var itemDiv = $(document.createElement("div"));
        itemDiv.addClass("item");
        itemDiv.html(item.label);
        (function(i){
            itemDiv.click(function(e){
                $(".item-selected").removeClass("item-selected");
                $(e.target).addClass("item-selected");
                holdedItemIndex = i;
            });
        })(i);
        wrapper.append(itemDiv);
        $("#field-manager").append(wrapper);
    }
    
    function drawBoard() {
        var width = fields.length;
        var height = fields[0].length;
        $("#map-wrapper").empty();
        $("#map-wrapper").css("width",(width*mazeFieldSize)+"px");
        $("#map-wrapper").css("height",(height*mazeFieldSize)+"px");
        $("#map-wrapper").css("display","block");
        for(var j=0 ; j<height ; ++j) {
            var rowDiv = $(document.createElement("div"));
            rowDiv.addClass("maze-fields-row");
            rowDiv.css("width",(width*mazeFieldSize)+"px");
            rowDiv.css("height",(mazeFieldSize)+"px");
            for(var i=0 ; i<width ; ++i) {
                var div = $(document.createElement("div"));
                div.attr("id","maze-field-"+i+"-"+j);
                div.addClass("maze-field");
                if(j>0) {
                    div.append(createWallEdit(i,j,"top"));
                }
                if(j<height-1) {
                    div.append(createWallEdit(i,j,"bottom"));
                }
                if(i>0) {
                    div.append(createWallEdit(i,j,"left"));
                }
                if(i<width-1) {
                    div.append(createWallEdit(i,j,"right"));
                }
                
                var labelDiv = $(document.createElement("div"));
                labelDiv.addClass("item-label");
                labelDiv.attr("id","item-label-"+i+"-"+j);
                if(fields[i][j]["i"] !== 'f') {
                    labelDiv.html(fields[i][j]["i"]);
                }
                div.append(labelDiv);
                
                //click event
                (function(i,j){
                    div.click(function(){
                        if(holdedItemIndex !== -1) {
                            fields[i][j]["i"] = items[holdedItemIndex]["sign"];
                            $("#item-label-"+i+"-"+j).html(fields[i][j]["i"]);
                            $(".item-selected").removeClass("item-selected");
                            holdedItemIndex = -1;
                            isEdited = true;
                        } else if(fields[i][j]["i"] !== 'f') {
                            fields[i][j]["i"] = 'f';
                            $("#item-label-"+i+"-"+j).html("");
                            isEdited = true;
                        }
                        saveState();
                        updatePrice();
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
            isEdited = true;
            var styles =[
                "3px solid rgba(0,0,0,.1)",
                "solid 3px #000",
                "solid 3px #630",
                "dotted 3px #630"
            ];
            switch(side) {
                case "top":
                    fields[i][j-1].be = (fields[i][j-1].be===holdedMaterialIndex)
                    ? 0 
                    : holdedMaterialIndex;
                    $("#maze-field-"+i+"-"+(j-1))
                            .css("border-bottom",styles[fields[i][j-1].be]);
                    break;
                case "right":
                    fields[i][j].re = (fields[i][j].re===holdedMaterialIndex)
                    ? 0
                    : holdedMaterialIndex;
                    $("#maze-field-"+i+"-"+j)
                            .css("border-right",styles[fields[i][j].re]);
                    break;
                case "bottom":
                    fields[i][j].be = (fields[i][j].be===holdedMaterialIndex)
                    ? 0
                    : holdedMaterialIndex;
                    $("#maze-field-"+i+"-"+j)
                            .css("border-bottom",styles[fields[i][j].be]);
                    break;
                case "left":
                    fields[i-1][j].re = (fields[i-1][j].re===holdedMaterialIndex)
                    ? 0
                    : holdedMaterialIndex;
                    $("#maze-field-"+(i-1)+"-"+j)
                            .css("border-right",styles[fields[i-1][j].re]);
                    break;
            }
            updatePrice();
            saveState();
        });
        return div;
    }
    
    function setEdges(element, div) {
        var edges = {
            re: "right",
            be: "bottom"
        };
        for(var i in edges) {
            switch(element[i]) {
                case 0://empty
                    break;
                case 1://wall
                    div.css("border-"+edges[i],"solid 3px #000");
                    break;
                case 2://door
                    div.css("border-"+edges[i],"solid 3px #630");
                    break;
                case 3://secret door
                    div.css("border-"+edges[i],"dotted 3px #630");
                    break;
            }
        }
    }
    
    var savingEnabled = true;
    function saveState() {
        if(!savingEnabled) {
            return;
        }
        displaySave();
        setCookie("maze",JSON.stringify(fields));
        isEdited = false;
        savingEnabled = false;
        setTimeout(function(){
            savingEnabled = true;
            if(isEdited) {
                saveState();
            }
        },3000);
    }
    
    function displaySave() {
        $("#maze-saved").css("opacity",.5);
        setTimeout(function(){
            $("#maze-saved").css("opacity",0);
        },700);
    }
   
    $("#upload").click(function(){
        $.ajax({
            url: "uploadMaze",
            type: "POST",
            data: {
                maze: JSON.stringify(fields)
            },
            success: function(result) {
                //console.log(JSON.stringify(fields));
                if(result["type"]==='success') {
                    document.cookie = "maze=;";
                    window.location = "myMazes";
                } else {
                    alert(result["type"] + ": " + result["message"]);
                }
            }
        });
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